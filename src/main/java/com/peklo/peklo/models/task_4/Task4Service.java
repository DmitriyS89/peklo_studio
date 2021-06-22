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
            result = List.of("error", String.valueOf(responseCode));
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

    public List<List<Results4Task>> results(List<String> urls) {
        List<List<Results4Task>> resultsTask4s = new ArrayList<>();

        for (String siteVersion : siteVersions) {
            List<Results4Task> resultsTask4s2 = new ArrayList<>();
            for (String url : urls) {
                if (url.startsWith("https://")) {
                    url = url.substring(8);
                } else if (url.startsWith("http://")) {
                    url = url.substring(7);
                }
                String format = String.format("%s%s", siteVersion, url);
                resultsTask4s2.add(checkRedirect(format));
            }
            resultsTask4s.add(resultsTask4s2);
        }
        return resultsTask4s;
    }

    public List<Results4Task> setMessagesToResult(List<List<Results4Task>> results, List<String> urlLinks) {
        List<Results4Task> results4Tasks = new ArrayList<>();
        for (List<Results4Task> r : results) {
            for (int i = 0; i < urlLinks.size(); i++) {
                boolean b1 = !r.get(i).getUrlTo().equals(urlLinks.get(i));
                boolean b2 = (r.get(i).getCode() > 199 && r.get(i).getCode() < 300);
                if (b1 && b2) {
                    r.get(i).setMessage("Дубликат!");
                } else {
                    if (r.get(i).getCode() > 199 && r.get(i).getCode() < 300) {
                        r.get(i).setMessage("Успешный запрос!");
                    } else if (r.get(i).getCode() > 299 && r.get(i).getCode() < 400) {
                        r.get(i).setMessage("Редирект!");
                    } else if (r.get(i).getCode() > 399 && r.get(i).getCode() < 500) {
                        r.get(i).setMessage("Запрещено!");
                    } else if (r.get(i).getCode() > 499) {
                        r.get(i).setMessage("Серверная ошибка!");
                    } else {
                        r.get(i).setMessage("ошибка соединения");
                    }
                }
                results4Tasks.add(r.get(i));
            }
        }
        return results4Tasks;
    }

    public List<String> urlLinks(Document document) throws URISyntaxException {
        List<String> links = new ArrayList<>();
        URI uri = new URI(document.location());
        String patt = String.format("%s://%s", uri.getScheme(), uri.getHost());
        Pattern pattern = Pattern.compile(patt);
        for (Element element : document.select("a[href]")) {
            String attr = element.attr("abs:href");
            if (pattern.matcher(attr).find())
                links.add(attr);
        }
        return links;
    }
}
