package com.peklo.peklo.models.task_3;

import com.peklo.peklo.exceptions.UrlNotConnection;
import com.peklo.peklo.exceptions.UrlNotCorrect;
import com.peklo.peklo.models.task_5.UserInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class Task3ServiceImpl implements Task3Service {
    private static final String STR_NULL = "NULL";
    public static final Task3Element NULL_ELEMENT = new Task3Element(STR_NULL, STR_NULL);
    private static final String urlForExcel = "peklo_studio\\results.xlsx";

    @Override
    public List<Task3Element> getLinks(Document document) {
        List<Task3Element> lists = new ArrayList<>();
        for (Element element : document.getElementsByTag("a")) {
            lists.add(getLink(element));
        }
        return lists;
    }

    @Override
    public Task3Element getLink(Element element) {
        String target = getAttr(element, "target");
        String href = element.attr("abs:href");
        return new Task3Element(target, href);
    }

    @Override
    public List<Task3Element> filterPatterns(List<Task3Element> elements, SiteWithDomain site) {
        Pattern p1 = getCompile("^%s://%s\\.%s\\.%s\\.%s", "\\w+", "\\w+", "\\w+", site.getDomain_1rd(), "\\w+");
        Pattern p2 = getCompile("^%s://%s\\.%s\\.%s", "\\w+", "\\w+", site.getDomain_1rd(), "\\w+");
        Pattern p3 = getCompile("^%s://%s\\.%s", "\\w+", site.getDomain_1rd(), "\\w+");

        return elements.stream()
                .filter(value -> !p1  .matcher(value.getHref()).find())
                .filter(value -> !p2  .matcher(value.getHref()).find())
                .filter(value -> !p3  .matcher(value.getHref()).find())
                .collect(Collectors.toList());
    }

    public Pattern getCompile(String url, String protocol, String domain3, String domain2, String domain1, String domain0) {
        return Pattern.compile(String.format(url, protocol, domain3, domain2, domain1, domain0), Pattern.MULTILINE);
    }

    public Pattern getCompile(String url, String protocol, String domain2, String domain1, String domain0) {
        return Pattern.compile(String.format(url, protocol, domain2, domain1, domain0), Pattern.MULTILINE);
    }

    public Pattern getCompile(String url, String protocol, String domain1, String domain0) {
        return Pattern.compile(String.format(url, protocol, domain1, domain0), Pattern.MULTILINE);
    }

    @Override
    public List<Task3Element> filter(List<Task3Element> elements) {
        return elements.stream()
                .filter(value -> !NULL_ELEMENT.equals(value))
                .filter(value -> !"_blank".equals(value.getTarget()))
                .filter(value -> value.getHref().startsWith("http"))
                .filter(value -> value.getHref().startsWith("https"))
                .collect(Collectors.toList());
    }

    @Override
    public String getAttr(Element element, String attr) {
        String result = element.attributes().get(attr);
        return "".equals(result) ? STR_NULL : result;
    }

    @Override
    public Document getJSoupConnection(String url) throws UrlNotConnection {
        try {
            Connection connect = Jsoup.connect(url);
            return connect.get();
        } catch (IOException e) {
            throw new UrlNotConnection();
        }
    }

    @Override
    public SiteWithDomain makeSiteEntity(String url) {
        try {
            URI uri = new URI(url);
            String host = uri.getHost();
            String[] domains = host.split("\\.");
            if (domains.length == 4)
                return new SiteWith3rdDomain(uri.getScheme(), domains[0], domains[1], domains[2], domains[3]);
            else if (domains.length == 3)
                return new SiteWith3rdDomain(uri.getScheme(), STR_NULL, domains[0], domains[1], domains[2]);
            else if (domains.length == 2)
                return new SiteWith3rdDomain(uri.getScheme(), STR_NULL, STR_NULL, domains[0], domains[1]);
            else
                throw new UrlNotCorrect();
        } catch (URISyntaxException e) {
            throw new UrlNotCorrect();
        }
    }

    @Override
    public File makeExcel(List<Task3Element> elements, String url) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Datasets");
        XSSFRow row;
        row = spreadsheet.createRow(0);
        row.createCell(0).setCellValue("Сайт");
        row.createCell(1).setCellValue(url);
        for (int i = 0; i < elements.size(); i++) {
            Task3Element userInfo = elements.get(i);
            row = spreadsheet.createRow(i + 1);
            row.createCell(0).setCellValue("href");
            row.createCell(1).setCellValue(userInfo.getHref());
        }
        File file = new File("result.xlsx");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
