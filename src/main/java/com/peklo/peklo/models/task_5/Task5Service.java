package com.peklo.peklo.models.task_5;

import com.peklo.peklo.exceptions.ConnectionNotFound;
import com.peklo.peklo.exceptions.UserMailNotFound;
import com.peklo.peklo.models.User.MailSender;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class Task5Service {

    @Value("${vkApiToken}")
    private String access_token;

    private static final String NULL = "NULL";
    private final MailSender mailSender;

    public void start(String id, String mail, String type, String urlFromFront) {
        if ("user".equals(type)) {
            String userInfo = getUserInfo(id);
            List<UserInfo> users = parseJSON(userInfo);
            File file = makeExcel(users, urlFromFront);
            mailSender.sendMailWithAttachment(mail, "Tool 5", "some", file.getAbsolutePath());
        } else if ("group".equals(type)) {
            findGroup(id);
            List<UserInfo> group1 = getGroup(id);
            File file = makeExcel(group1, urlFromFront);
            mailSender.sendMailWithAttachment(mail, "Tool 5", "some", file.getPath());

        }
    }

    public String findGroup(String groupId) {
        String params = String.format("group_ids=&group_id=%s&fields=", groupId);
        try {
            URL url = vkApiUrlBuilder("groups.getById", params, access_token);
            String vkApiAnswer = getVkApiAnswer(url);
            if (!Pattern.compile("response").matcher(vkApiAnswer).find()) {
                throw new ConnectionNotFound();
            }
            return vkApiAnswer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NULL;
    }

    public String getUserInfo(String id) {
        String params = String.format("user_ids=%s&fields=contacts", id);
        try {
            URL url = vkApiUrlBuilder("users.get", params, access_token);
            String vkApiAnswer = getVkApiAnswer(url);
            if (!Pattern.compile("response").matcher(vkApiAnswer).find()) {
                throw new UserMailNotFound();
            }
            return vkApiAnswer;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NULL;
    }

    public List<UserInfo> getGroup(String groupId) {
        List<UserInfo> userInfos = new ArrayList<>();
        Integer count = 0;
        while (true){
            if(count > 9999) {
                break;
            } else {
                String ids = getConcatId(groupId, count);
                String userInfo = getUserInfo(ids);
                userInfos.addAll(parseJSON(userInfo));
                count += 1000;
            }
        }
        return userInfos;
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

    private File makeExcel(List<UserInfo> data, String url) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet("Datasets");
        XSSFRow row;
        row = spreadsheet.createRow(0);
        row.createCell(0).setCellValue("Сайт");
        row.createCell(1).setCellValue(url);
        for (int i = 0; i < data.size(); i++) {
            UserInfo userInfo = data.get(i);
            row = spreadsheet.createRow(i + 1);

            row.createCell(0).setCellValue("_Имя_");
            row.createCell(1).setCellValue(userInfo.getUserName());
            row.createCell(4).setCellValue("Номер");
            row.createCell(5).setCellValue(userInfo.getUserNumber());
            row.createCell(7).setCellValue("Адрес");
            row.createCell(8).setCellValue(userInfo.getUserMail());

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

    private List<UserInfo> parseJSON(String json) {
        List<UserInfo> users = new ArrayList<>();
        JSONObject object = new JSONObject(json);
        JSONArray response = object.getJSONArray("response");
        for (int i = 0; i < response.length(); i++) {
            JSONObject jsonObject = response.getJSONObject(i);
            String firstName = jsonObject.optString("first_name", "");
            String lastName = jsonObject.optString("last_name", "");
            String mobileNumber = jsonObject.optString("mobile_phone", "пусто");
            users.add(new UserInfo(String.format("%s %s", firstName, lastName), mobileNumber, "пусто"));
        }
        return users;
    }

    public String getConcatId(String groupId, Integer offset) {
        String params = String.format("group_id=%s&sort=id_asc&offset=%s&count=%s", groupId, offset, 1000);
        try {
            URL url = vkApiUrlBuilder("groups.getMembers", params, access_token);
            String usersId = getVkApiAnswer(url);
            return parseGroupJSON(usersId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NULL;
    }


    private String parseGroupJSON(String json) {
        JSONObject object = new JSONObject(json);
        JSONObject obj = object.optJSONObject("response");
        JSONArray newItems = obj.getJSONArray("items");
        return newItems.toString().substring(1, newItems.toString().length() - 1);
    }

    public String cutUrlUser(String url) {
        String result = "";
        if (!Pattern.compile("m\\.vk\\.com").matcher(url).find()) {
            if (Pattern.compile("id").matcher(url).find()) {
                result = url.substring(17);
            } else {
                result = url.substring(15);
            }
        } else {
            if (Pattern.compile("id").matcher(url).find()) {
                result = url.substring(19);
            } else {
                result = url.substring(17);
            }
        }
        return result;
    }

    public String cutUrlGroup(String url) {
        String result = "";
        if (!Pattern.compile("m\\.vk\\.com").matcher(url).find()) {
            if (Pattern.compile("public").matcher(url).find()) {
                result = url.substring(21);
            } else {
                result = url.substring(15);
            }
        } else {
            if (Pattern.compile("public").matcher(url).find()) {
                result = url.substring(23);
            } else {
                result = url.substring(17);
            }
        }
        return result;
    }
}
