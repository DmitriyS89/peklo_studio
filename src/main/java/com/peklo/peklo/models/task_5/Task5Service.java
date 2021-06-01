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
import java.net.URL;
import java.util.*;

@Service
public class Task5Service {

    @Value("${vkApiToken}")
    private String access_token;

    private static final String NULL = "NULL";

    public String getUserInfo(String id){
        String params = String.format("user_ids=%s&fields=contacts", id);
        try {
            URL url = vkApiUrlBuilder("users.get", params, access_token);
            return getVkApiAnswer(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NULL;
    }

    private URL vkApiUrlBuilder(String method, String params, String access_token) throws IOException {
        String url = String.format("https://api.vk.com/method/%s?%s&access_token=%s&v=%s",
                method, params, access_token, "5.131");
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
