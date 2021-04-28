package com.peklo.peklo.models.task_3;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.peklo.peklo.exceptions.UrlNotConnection;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class Task3Service {
    private static final String STR_NULL = "NULL";
    public static final Task3Element NULL_ELEMENT = new Task3Element(STR_NULL, STR_NULL);
    private static String BASE_URL;
    private static final String urlForExcel = "C:\\Users\\User\\GitClones\\peklo_studio\\results.xlsx";

    public List<Task3Element> run(Document document){
        List<Task3Element> lists = new ArrayList<>();
        for (Element element : document.getElementsByTag("a")){
            lists.add(getValues(element));
        }
        return filter(lists);
    }

    private List<Task3Element> filter(List<Task3Element> lists) {
        List<Task3Element> results = new ArrayList<>();
        for ( Task3Element element : lists ) {
            Task3Element result = filterFieldWithPattern("#", element.getHref(), element);
            result = filterFieldWithPattern(BASE_URL, result.getHref(), result);
            result = filterFieldWithPattern("/", result.getHref(), result);
            results.add(result);
        }
        results = filterTargetValues(results);
        results = filterGeneralNull(results);
        return results;
    }

    public List<Task3Element> filterGeneralNull(List<Task3Element> elements) {
        return elements.stream()
                .filter(value -> !NULL_ELEMENT.equals(value))
                .collect(Collectors.toList());
    }

    public Task3Element getValues(Element element){
        String target = getAttr(element, "target");
        String href = getAttr(element, "href");
        return new Task3Element(target, href);
    }

    public String getAttr(Element element, String attr) {
        String result = element.attributes().get(attr);
        return "".equals(result) ? STR_NULL : result;
    }

    public List<Task3Element> filterTargetValues(List<Task3Element> elements){
        return elements.stream()
                .filter(value -> !"_blank"  .equals(value.getTarget()))
                .filter(value -> STR_NULL  .equals(value.getTarget()))
                .collect(Collectors.toList());
    }

    public Task3Element filterFieldWithPattern(String word, String value, Task3Element element) {
        Pattern pattern = Pattern.compile(String.format("^%s", word));
        Matcher matcher = pattern.matcher(value);
        if(matcher.find())
            return NULL_ELEMENT;
        else
            return element;
    }

    public Document getJSoupConnection(String url) throws UrlNotConnection {
        try {
            Connection connect = Jsoup.connect(url);
            BASE_URL = connect.get().baseUri();
            return connect.get();
        } catch (IOException e) {
            throw new UrlNotConnection();
        }
    }

    public void makeExcel(List<Task3Element> elements, String url) {
        Map<Integer, String[]> forExcel = new HashMap<>();
        forExcel.put(0, new String[]{"URL->", url});
        int count = 1;
        for (Task3Element element : elements) {
            String[] value = new String[]{"href->", element.getHref()};
            forExcel.put(count, value);
            count++;
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Data by target attribute");
        XSSFRow row;

        Set<Integer> keyIds = forExcel.keySet().stream()
                .sorted(Integer::compareTo)
                .collect(Collectors.toSet());

        int rowId = 0;

        for (int key : keyIds) {
            row = spreadsheet.createRow(rowId);
            rowId++;

            Object[] objectArr = forExcel.get(key);
            int cellId = 0;
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellId);
                cell.setCellValue((String)obj);
                cellId++;
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(
                    new File(urlForExcel));
            workbook.write(out);
            out.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
