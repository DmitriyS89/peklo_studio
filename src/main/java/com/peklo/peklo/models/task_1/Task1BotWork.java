package com.peklo.peklo.models.task_1;

import com.peklo.peklo.exceptions.UrlNotConnection;
import com.peklo.peklo.models.task_3.Task3Service;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
    private final File file = new File("result.xlsx");

    @Async
    @Scheduled(fixedRate = 60000)
    public void getItems() {
        List<Tool1Item> allElements = task1Service.getAllElements();
        List<String> userChatIds = task1Service.findUniqueElements();

        for (String userChatId : userChatIds) {
            List<Tool1Item> alpha = new ArrayList<>();
            for (Tool1Item item : allElements) {
                if(item.getUserChatId().equals(userChatId)){
                    alpha.add(item);
                }
            }
            checkItems(alpha);
        }
    }

    @Async
    public void checkItems(List<Tool1Item> allElements) {
        LocalDateTime time = LocalDateTime.now();
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
        String userChatIdFor = "";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Datasets");
        XSSFRow row;
        int column = 0;
        for (Map.Entry<Tool1Item, LinkedList<DiffMatchPatch.Diff>> entry : diffs.entrySet()) {
            row = spreadsheet.createRow(column++);

            Tool1Item item = entry.getKey();
            LinkedList<DiffMatchPatch.Diff> diff = entry.getValue();

            String stringFromDiff = task1Service.getStringFromDiff(diff);

            userChatIdFor = item.getUserChatId();
            row.createCell(0).setCellValue("ID:");
            row.createCell(1).setCellValue(item.getId());
            row.createCell(2).setCellValue("Сайт:");
            row.createCell(3).setCellValue(item.getFromUrl());

            row = spreadsheet.createRow(column++);
            row.createCell(0).setCellValue("Сохранённый:");
            row.createCell(1).setCellValue(item.getHtmlValue());

            row = spreadsheet.createRow(column++);
            row.createCell(0).setCellValue("Новое:");
            row.createCell(1).setCellValue(stringFromDiff);

            long count = diff.stream().filter(d -> d.operation == DiffMatchPatch.Operation.EQUAL).count();
            if (count == 1) {
                row = spreadsheet.createRow(column++);
                row.createCell(0).setCellValue("Не каких изменений!");
            } else {
                int size = 1;
                for (DiffMatchPatch.Diff d : diff) {
                    row = spreadsheet.createRow(column++);
                    switch (d.operation) {
                        case DELETE:
                            row.createCell(0).setCellValue(size + ". Удалено:");
                            row.createCell(1).setCellValue(d.text);
                            break;
                        case EQUAL:
                            row.createCell(0).setCellValue(size + ". Без изменений:");
                            row.createCell(1).setCellValue(d.text);
                            break;
                        case INSERT:
                            row.createCell(0).setCellValue(size + ". Добавлено:");
                            row.createCell(1).setCellValue(d.text);
                            break;
                    }
                    size++;
                }
            }
            column++;
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date date = new Date(System.currentTimeMillis());
        String text = String.format("Время проверки:\n%s", simpleDateFormat.format(date));
        task1Service.sendMessage(text, userChatIdFor);
        task1Service.sendFile(file, userChatIdFor);
    }

    @Async
    protected void checkAndFilterItems(List<Tool1Item> items, List<Tool1Item> checkedItems) {
        String userChatId = "";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Datasets");
        XSSFRow row;
        int count = 0;
        for (Tool1Item item : items) {
            String errors;
            userChatId = item.getUserChatId();
            try {
                task3Service.getJSoupConnection(item.getFromUrl());
                checkedItems.add(item);
                continue;
            } catch (UrlNotConnection urlNotConnection) {
                urlNotConnection.printStackTrace();
                errors = "1. Сайт не доступен 2. Ошибка при загрузке сайта";
            } catch (IllegalArgumentException illegalArgumentException) {
                illegalArgumentException.printStackTrace();
                errors = "1. Не правильная ссылка 2. Пустая ссылка";
            }
            row = spreadsheet.createRow(count++);
            row.createCell(0).setCellValue("ID:");
            row.createCell(1).setCellValue(item.getId());
            row.createCell(2).setCellValue("Сайт:");
            row.createCell(3).setCellValue(item.getFromUrl());

            row = spreadsheet.createRow(count++);
            row.createCell(0).setCellValue("Элемент:");
            row.createCell(1).setCellValue(item.getHtmlValue());
            row = spreadsheet.createRow(count++);
            row.createCell(0).setCellValue(String.format("Либо: %s", errors));
            count++;
        }
        if(count < 1) return;
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date date = new Date(System.currentTimeMillis());
        String text = String.format("Ошибка:\n%s", simpleDateFormat.format(date));
        task1Service.sendMessage(text, userChatId);
        task1Service.sendFile(file, userChatId);
    }
}
