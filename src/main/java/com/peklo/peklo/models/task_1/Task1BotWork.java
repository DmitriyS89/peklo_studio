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
import java.util.*;
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
        if (items.size() < 1) return;

        List<Tool1Item> checkedItems = new ArrayList<>();
        checkAndFilterItems(items, checkedItems);

        if (checkedItems.size() < 1) return;

        Set<String> urls = checkedItems.stream().map(Tool1Item::getFromUrl).collect(Collectors.toSet());
        List<Document> documents = new ArrayList<>();
        for (String url : urls) {
            try {
                Document jSoupConnection = task3Service.getJSoupConnection(url);
                documents.add(jSoupConnection);
            } catch (UrlNotConnection urlNotConnection) {
                urlNotConnection.printStackTrace();
            }
        }

        Map<Tool1Item, LinkedList<DiffMatchPatch.Diff>> diffs = task1Service.foundDiffs(checkedItems, documents);
        sendMessages(diffs);

        checkedItems.forEach(item -> item.setLocalDateTime(LocalDateTime.now()));
        task1Service.saveAll(checkedItems);
    }

    @Async
    protected void sendMessages(Map<Tool1Item, LinkedList<DiffMatchPatch.Diff>> diffs) {
        for (Map.Entry<Tool1Item, LinkedList<DiffMatchPatch.Diff>> entry : diffs.entrySet()) {

            Tool1Item item = entry.getKey();
            LinkedList<DiffMatchPatch.Diff> diff = entry.getValue();

            StringBuilder stringBuilder = new StringBuilder();

            String stringFromDiff = task1Service.getStringFromDiff(diff);
            stringBuilder.append("Старое: ").append(item.getHtmlValue()).append("\n\n");
            stringBuilder.append("Новое : ").append(stringFromDiff).append("\n\n");

            stringBuilder.append("\n\n");
            for (DiffMatchPatch.Diff d : diff) {
                switch (d.operation) {
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

            long count = diff.stream().filter(d -> d.operation == DiffMatchPatch.Operation.EQUAL).count();
            if (count == 1) stringBuilder.append("Не каких изменений!");
            task1Service.sendMessage(stringBuilder.toString(), item.getUserChatId());
        }
    }

    @Async
    protected void checkAndFilterItems(List<Tool1Item> items, List<Tool1Item> checkedItems) {
        String text = "Сайт: %s\n\n" + "Элемент: %s\n\n" + "Либо:\n" + "%s";
        for (Tool1Item item : items) {
            String message, errors;
            try {
                task3Service.getJSoupConnection(item.getFromUrl());
                checkedItems.add(item);
                continue;
            } catch (UrlNotConnection urlNotConnection) {
                urlNotConnection.printStackTrace();
                errors = "1. Сайт не доступен\n2. Ошибка при загрузке сайта";
                message = String.format(text, item.getFromUrl(), item.getHtmlValue(), errors);
            } catch (IllegalArgumentException illegalArgumentException) {
                illegalArgumentException.printStackTrace();
                errors = "1. Не правильная ссылка\n2. Пустая ссылка(можете посмотреть на сайте)";
                message = String.format(text, item.getFromUrl(), item.getHtmlValue(), errors);
            }
            task1Service.sendMessage(message, item.getUserChatId());
        }
    }
}
