package com.peklo.peklo.models.task_5;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface Vk {

    List<Long> getIdsInDocument(Document document);
}
