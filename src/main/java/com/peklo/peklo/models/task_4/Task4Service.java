package com.peklo.peklo.models.task_4;

import com.peklo.peklo.exceptions.ConnectionNotFound;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class
Task4Service {
    private final String[] siteVersions = new String[]{"https://", "http://", "https://www.", "http://www."};
    private String baseUrl = "";
    private int countCode = 0;

    private void checkUrl(String url, String protocol) {
        int count = 0;
        for (String siteVersion : siteVersions) {
            String format = String.format("^%s", siteVersion);
            Pattern pattern = Pattern.compile(format);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                count++;
            }
        }
        if (count < 1) {
            baseUrl = protocol + url;
        } else {
            baseUrl = url;
        }
    }
    public  String getBaseUrl(String urlFromFront, String protocol) {
        checkUrl(urlFromFront, protocol);
        try {
            URL url = new URL(baseUrl);
            url.openStream();
            return url.getHost();
        } catch (IOException e) {
            throw new ConnectionNotFound();
        }
    }

    public List<String> makeRequest(URL uri) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        int responseCode = connection.getResponseCode();
        List<String> result;
        if (responseCode > 199 && responseCode < 300) {
            result = List.of(connection.getURL().toExternalForm(), String.valueOf(responseCode));
            countCode++;
        } else if (responseCode > 299 && responseCode < 400) {
            result = List.of(connection.getHeaderField("Location"), String.valueOf(responseCode));
        } else {
            result = List.of("error", null);
        }
        return result;
    }
    public Results4Task checkRedirect(String url) {
        Results4Task resultsTask4;
        try {
            URL uri = new URL(url);
            List<String> stringList = makeRequest(uri);
            resultsTask4 = Results4Task.builder()
                    .code(Integer.parseInt(stringList.get(1)))
                    .urlFrom(url)
                    .urlTo(stringList.get(0))
                    .build();
        } catch (IOException e) {
            resultsTask4 = Results4Task.builder()
                    .urlFrom(url)
                    .urlTo("Connection failed")
                    .message("Error")
                    .build();
        }
        return resultsTask4;
    }

    public  List<Results4Task> results(String baseUrl) {
        List<Results4Task> resultsTask4s = new ArrayList<>();

        for (String siteVersion : siteVersions) {
            String format = String.format("%s%s", siteVersion, baseUrl);
            resultsTask4s.add(checkRedirect(format));
        }
        return setMessagesToResult(resultsTask4s);
    }

    public List<Results4Task> setMessagesToResult(List<Results4Task> results) {
        for (Results4Task r :
                results) {
            if (r.getCode() > 199 && r.getCode() < 300) {
                if (countCode > 1) {
                    r.setMessage("Дубликат!");
                } else {
                    r.setMessage("Успешный запрос!");
                }
            } else if(r.getCode()==0){
                r.setMessage("ошибка соединения");
            } else {
                r.setMessage("Редирект!");
            }
        }
        return results;
    }
}
