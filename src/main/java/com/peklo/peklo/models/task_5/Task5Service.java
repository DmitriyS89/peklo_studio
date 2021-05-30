package com.peklo.peklo.models.task_5;

import com.peklo.peklo.models.task_5.VkImpls.Account;
import com.peklo.peklo.models.task_5.VkImpls.Group;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class Task5Service {

    @Value("${vkApiToken}")
    private String access_token;

    public Document getDocument(String url) throws IOException {
        try {
            Connection connect = Jsoup.connect(url);
            return connect.get();
        } catch (IOException ioException) {
            throw new IOException();
        }
    }

    public Vk checkUrl(String url) {
        try {
            String id = new URI(url).getPath();
            String substring = id.substring(1);
            Boolean existVkGroup = existVkGroup(substring);
            Boolean existVkUser = existVkUser(substring);

            if (existVkGroup && existVkUser)
                throw new IOException();
            else if (existVkGroup)
                return new Group();
            else if (existVkUser)
                return new Account();
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Boolean existVkUser(String groupId) throws IOException {
        Pattern pattern = Pattern.compile("first_name");
        String format = "user_ids=%s&fields=contacts";
        String params = String.format(format, groupId);
        if (groupId.startsWith("id")) {
            params = String.format(format, groupId.substring(2));
        }
        URL url = vkApiUrlBuilder("users.get", params, access_token, "5.131");
        String page = getVkApiAnswer(url);
        return pattern.matcher(page).find();
    }

    private Boolean existVkGroup(String groupId) throws IOException {
        Pattern pattern = Pattern.compile("screen_name");
        String format = "group_ids=&group_id=%s&fields=";
        String params = String.format(format, groupId);
        if (groupId.startsWith("public")) {
            params = String.format(format, groupId.substring(6));
        }
        URL url = vkApiUrlBuilder("groups.getById", params, access_token, "5.131");
        String page = getVkApiAnswer(url);
        return pattern.matcher(page).find();
    }

    private URL vkApiUrlBuilder(String method, String params,
                                String access_token, String version) throws IOException {
        String url = String.format("https://api.vk.com/method/%s?%s&access_token=%s&v=%s",
                method, params, access_token, version);
        return new URL(url);
    }

    private String getVkApiAnswer(URL url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuilder page = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            page.append(line);
        }
        in.close();
        return page.toString();
    }

    private File makeExcel(List<UserInfo> data, String url){
        Map<Integer, String[]> forExcel = new HashMap<>();
        forExcel.put(0, new String[]{"URL->", url});
        int count = 1;
        for (UserInfo element : data) {
            String[] value = new String[]{
                    "Name", element.getUserName(),
                    "Email", element.getUserMail(),
                    "Number", element.getUserNumber()};
            forExcel.put(count, value);
            count++;
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Data by target attribute");
        XSSFRow row;

        Set<Integer> keyIds = new HashSet<>(forExcel.keySet());

        int rowId = 0;

        for (int key : keyIds) {
            row = spreadsheet.createRow(rowId);
            rowId++;

            Object[] objectArr = forExcel.get(key);
            int cellId = 0;
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellId);
                cell.setCellValue((String) obj);
                cellId++;
            }
        }
        File file = new File("peklo_studio/result.xlsx");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
