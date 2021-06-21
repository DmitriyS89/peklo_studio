package com.peklo.peklo.models.task_4;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class Task4Service {
    private final String[] siteVersions = new String[]{"https://", "http://", "https://www.", "http://www."};

    public List<String> makeRequest(URL uri) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        int responseCode = connection.getResponseCode();
        List<String> result;
        if (responseCode > 199 && responseCode < 300) {
            result = List.of(connection.getURL().toExternalForm(), String.valueOf(responseCode));
        } else if (responseCode > 299 && responseCode < 400) {
            result = List.of(connection.getHeaderField("Location"), String.valueOf(responseCode));
        } else {
            result = List.of("error", "0");
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

    public  List<Results4Task> results(List<String> urls) {
        List<Results4Task> resultsTask4s = new ArrayList<>();

        for (String siteVersion : siteVersions) {
            for (String url : urls) {
                if (url.startsWith("https://")){
                    url = url.substring(8);
                } else if (url.startsWith("http://")) {
                    url = url.substring(7);
                }
                String format = String.format("%s%s", siteVersion, url);
                resultsTask4s.add(checkRedirect(format));
            }
        }
        return setMessagesToResult(resultsTask4s);
    }

    public List<Results4Task> setMessagesToResult(List<Results4Task> results) {
        for (Results4Task r :
                results) {
            if (r.getCode() > 199 && r.getCode() < 300) {
                r.setMessage("Успешный запрос!");
            } else if(r.getCode()==0){
                r.setMessage("ошибка соединения");
            } else {
                r.setMessage("Редирект!");
            }
        }
        return results;
    }

    public List<String> urlLinks(Document document) throws URISyntaxException {
        List<String> links = new ArrayList<>();
        URI uri = new URI(document.location());
        String patt = String.format("%s://%s", uri.getScheme(), uri.getHost());
        Pattern pattern = Pattern.compile(patt);
        for (Element element : document.select("a[href]")) {
            String attr = element.attr("abs:href");
            if(pattern.matcher(attr).find())
                links.add(attr);
        }
        return links;
    }
}
