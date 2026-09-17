package com.yury.wikimedia.consumer.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.GetIndexRequest;
import org.opensearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OpenSearchService {
    private final RestHighLevelClient restHighLevelClient;

    @SneakyThrows
    public void createIndex(String indexName) {
        boolean indexExists = restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
        if (indexExists) {
            log.info("Index {} already exists in OpenSearch instance", indexName);
            return;
        }

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
        restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        log.info("Index {} has been created in OpenSearch instance", indexName);
    }

    @SneakyThrows
    public void saveData(String indexName, String id, String data) {
        IndexRequest indexRequest = new IndexRequest(indexName).id(id).source(data, XContentType.JSON);
        restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        log.info("Inserted in open search index {} the data with id {}", indexName, id);
    }
}
