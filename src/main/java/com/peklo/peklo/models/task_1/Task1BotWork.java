package com.peklo.peklo.models.task_1;

import com.peklo.peklo.exceptions.UrlNotConnection;
import com.peklo.peklo.models.task_3.Task3Service;
import lombok.RequiredArgsConstructor;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableScheduling
@EnableAsync

@RequiredArgsConstructor
public class Task1BotWork {

    private final Task1Service task1Service;
    private final Task3Service task3Service;

    @Async
    @Scheduled(fixedRate = 60000)
    public void checkItems() {
        LocalDateTime time = LocalDateTime.now();
        List<Tool1Item> allElements = task1Service.getAllElements();

        List<Tool1Item> results = new ArrayList<>();

        if (allElements.size() > 0) {
            for (Tool1Item item : allElements) {
                LocalDateTime dateTime = item.getLocalDateTime();
                String[] times = item.getTime().split("_");
                if (times[0].equals("d")) {
                    dateTime = dateTime.plusDays(Long.parseLong(times[1]));
                } else {
                    dateTime = dateTime.plusHours(Long.parseLong(times[1]));
                }
                if (time.isAfter(dateTime)) {
                    results.add(item);
                }
            }
            changeAndSave(results);
        }
    }

    @Async
    public void changeAndSave(List<Tool1Item> items) {
        if (items.size() > 0) {
            items.forEach(item -> item.setLocalDateTime(LocalDateTime.now()));
            task1Service.saveAll(items);

            Set<String> strings = items.stream().map(Tool1Item::getFromUrl).collect(Collectors.toSet());
            List<Document> documents = new ArrayList<>();
            for (String url : strings) {
                try {
                    Document jSoupConnection = task3Service.getJSoupConnection(url);
                    documents.add(jSoupConnection);
                } catch (UrlNotConnection urlNotConnection) {
                    urlNotConnection.printStackTrace();
                    for (Tool1Item item : items) {
                        if (item.getFromUrl().equals(url)){
                            item.setHtmlValue("");
                        }
                    }
                }
            }
            task1Service.saveAll(items);
            List<LinkedList<DiffMatchPatch.Diff>> diffs = task1Service.foundDiffs(items, documents);

            for (int i = 0; i < diffs.size(); i++){
                StringBuilder stringBuilder = new StringBuilder();

                String stringFromDiff = task1Service.getStringFromDiff(diffs.get(i));
                stringBuilder.append("Старое: ").append(items.get(i).getHtmlValue()).append("\n\n");
                stringBuilder.append("Новое : ").append(stringFromDiff).append("\n\n");

                stringBuilder.append("\n\n");
                for (DiffMatchPatch.Diff d : diffs.get(i)) {
                    switch (d.operation){
//                        case EQUAL:
//                            stringBuilder.append("Без изменений: ").append(d.text).append("\n");
//                            break;
                        case DELETE:
                            stringBuilder.append("Удалено: ").append(d.text).append("\n");
                            break;
                        case INSERT:
                            stringBuilder.append("Добавлено: ").append(d.text).append("\n");
                            break;
                    }

                }

                long count = diffs.get(i).stream().filter(d -> d.operation == DiffMatchPatch.Operation.EQUAL).count();
                if(count == 1) stringBuilder.append("Не каких изменений!");
                task1Service.sendMessage(stringBuilder.toString(), items.get(i).getUserChatId());
            }
        }
    }
}
