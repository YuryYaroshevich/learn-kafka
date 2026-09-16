package com.yury.wikimedia.consumer.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WikimediaService {
    private final OpenSearchService openSearchService;

    private final String indexName;

    public WikimediaService(OpenSearchService openSearchService, @Value("${opensearch.index}") String indexName) {
        this.openSearchService = openSearchService;
        this.indexName = indexName;
    }

    public void saveData(String data) {
        openSearchService.saveData(indexName, data);
        log.debug("Saved data to index {}", indexName);
    }
}