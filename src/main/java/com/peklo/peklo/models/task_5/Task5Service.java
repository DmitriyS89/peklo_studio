package com.peklo.peklo.models.task_5;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Task5Service {

    public Document getDocument(String url) throws IOException {
        try {
            Connection connect = Jsoup.connect(url);
            return connect.get();
        } catch (IOException ioException) {
            throw new IOException();
        }
    }
}
