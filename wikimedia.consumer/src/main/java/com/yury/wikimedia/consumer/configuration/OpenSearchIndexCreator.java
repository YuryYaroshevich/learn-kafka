package com.yury.wikimedia.consumer.configuration;

import com.yury.wikimedia.consumer.service.OpenSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OpenSearchIndexCreator implements ApplicationRunner {
    private final OpenSearchService openSearchService;
    private final String indexName;

    public OpenSearchIndexCreator(
            OpenSearchService openSearchService, @Value("${opensearch.index}") String indexName) {
        this.openSearchService = openSearchService;
        this.indexName = indexName;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        openSearchService.createIndex(indexName);
    }
}
