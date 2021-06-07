package com.peklo.peklo.models.task_5;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class Task5Service {

    @Value("${vkApiToken}")
    private String access_token;

    private static final String NULL = "NULL";

    public String findGroup(String groupId){
        String params = String.format("group_ids=&group_id=%s&fields=", groupId);
        try {
            URL url = vkApiUrlBuilder("groups.getById", params, access_token);
            String vkApiAnswer = getVkApiAnswer(url);
            if(!Pattern.compile("response").matcher(vkApiAnswer).find()) {
                return NULL;
            }
            return vkApiAnswer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NULL;
    }

    public String getUserInfo(String id){
        String params = String.format("user_ids=%s&fields=contacts", id);
        try {
            URL url = vkApiUrlBuilder("users.get", params, access_token);
            String vkApiAnswer = getVkApiAnswer(url);
            if(!Pattern.compile("response").matcher(vkApiAnswer).find()) {
                return NULL;
            }
            return vkApiAnswer;
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

    private List<UserInfo> parseJSON (String json) {
        List<UserInfo> users = new ArrayList<>();
        JSONObject object = new JSONObject(json);
        JSONArray response = object.getJSONArray("response");
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            String firstName = jsonObject.optString("first_name", "null");
            String lastName = jsonObject.optString("last_name", "null");
            String mobileNumber = jsonObject.optString("mobile_phone", "Информация отсутствует");
            users.add(new UserInfo(String.format("%s %s", firstName, lastName), mobileNumber, "Информация отсутствует"));
        }
        return users;
    }
}
