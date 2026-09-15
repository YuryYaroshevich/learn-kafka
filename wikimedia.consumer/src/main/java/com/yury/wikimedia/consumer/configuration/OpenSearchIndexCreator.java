package com.yury.wikimedia.consumer.configuration;

import lombok.extern.slf4j.Slf4j;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OpenSearchIndexCreator implements ApplicationRunner {
    private final RestHighLevelClient restHighLevelClient;
    private final String indexName;

    public OpenSearchIndexCreator(RestHighLevelClient restHighLevelClient, @Value("${opensearch.index}") String indexName) {
        this.restHighLevelClient = restHighLevelClient;
        this.indexName = indexName;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean indexExists = restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
        if (indexExists) {
            log.info("Index {} already exists in OpenSearch instance", indexName);
            return;
        }

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        log.info("Index {} has been created in OpenSearch instance", indexName);
    }
}
