package com.peklo.peklo.models.task_1;

import com.peklo.peklo.exceptions.UrlNotConnection;
import com.peklo.peklo.models.task_3.Task3Service;
import com.peklo.peklo.models.telegram_bot.Bot;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Task1Service {

    private final Tool1ItemRepository tool1ItemRepository;
    private final Bot telegram_bot;

    public Html drawSite(Document doc, Boolean isScript) {
        String links = getElements(doc, isScript);
        setImgUrls(doc);
        return new Html(links, doc.body().html());
    }

    private String getElements(Document doc, Boolean isScript){
        String links = getLinks(doc);
        String styles = getStyles(doc);
        String scripts = isScript ? "" : getScripts(doc);

        if(isScript) doc.body().getElementsByTag("script").remove();

        return links + "\n" +
                styles + "\n" +
                scripts + "\n";
    }

    private String getScripts(Document doc) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Element element : doc.head().getElementsByTag("script")) {
            stringBuilder.append(element.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    private String getStyles(Document doc) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Element element : doc.getElementsByTag("style")) {
            stringBuilder.append(element.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    private String getLinks(Document doc) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Element element : doc.getElementsByTag("link")) {
            String newAttr = element.attr("abs:href");
            element.attributes().put("href", newAttr);
            stringBuilder.append(element.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    public List<String> getElements(String elements) {
        JSONArray objects = new JSONArray(elements.substring(9));

        List<String> elems = new ArrayList<>();
        for (int i = 0; i < objects.length(); i++) {
            String optString = objects.optString(i, "");
            if(!optString.isEmpty()) elems.add(optString);
        }
        return elems;
    }

    private void setImgUrls(Document document){
        for (Element element : document.getElementsByTag("img")) {
            String attr = element.attr("abs:src");
            element.attributes().put("src", attr);
        }
    }
}
