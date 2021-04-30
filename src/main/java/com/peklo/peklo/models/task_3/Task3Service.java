package com.peklo.peklo.models.task_3;

import com.peklo.peklo.exceptions.UrlNotConnection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.regex.Pattern;

public interface Task3Service {

    List<Task3Element> getLinks(Document document);

    Task3Element getLink(Element element);

    String getAttr(Element element, String attr);

    List<Task3Element> filter(List<Task3Element> elements);

    List<Task3Element> filterPatterns(List<Task3Element> elements, SiteWithDomain site);

    Document getJSoupConnection(String url) throws UrlNotConnection;

    Pattern getCompile(String url, String protocol, String domain3, String domain2, String domain1);
    Pattern getCompile(String protocol, String domain2, String domain1);
    Pattern getCompile(String protocol, String domain2);

    SiteWithDomain makeSiteEntity(String url);

    void makeExcel(List<Task3Element> elements, String url);
}
