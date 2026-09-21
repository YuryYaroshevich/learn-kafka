package com.yury.wikimedia.consumer.service;

import static java.util.stream.Collectors.toMap;

import com.google.gson.JsonParser;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WikimediaService {
    private final OpenSearchService openSearchService;

    private final String indexName;

    public WikimediaService(
            OpenSearchService openSearchService, @Value("${opensearch.index}") String indexName) {
        this.openSearchService = openSearchService;
        this.indexName = indexName;
    }

    public void saveData(String data) {
        openSearchService.saveData(indexName, getId(data), data);
        log.debug("Saved data to index {}", indexName);
    }

    public void saveBulkWikies(List<String> dataList) {
        Map<String, String> idsToData =
                dataList.stream().collect(toMap(WikimediaService::getId, Function.identity()));
        openSearchService.saveBulkData(indexName, idsToData);
        log.debug("Saved {} documents to index {}", idsToData.size(), indexName);
    }

    private static String getId(String data) {
        return JsonParser.parseString(data)
                .getAsJsonObject()
                .get("meta")
                .getAsJsonObject()
                .get("id")
                .getAsString();
    }
}
