package com.teams.util;

import com.teams.entity.Orders;
import com.teams.exception.HotelManagementException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class ElasticSearchUtil {

    @Autowired
    RestHighLevelClient elasticSearchClient;

    //to get particular document from the elastic
    public Map<String, Object> getDocumentById(String id, String indexName) {
        try{
            // Create a GetRequest to check document existence
            GetRequest getRequest = new GetRequest(indexName, id);
            if(isIdExists(id,indexName)){
                // Execute the request
                GetResponse response = elasticSearchClient.get(getRequest, RequestOptions.DEFAULT);
                return response.getSource();
            } else{
                throw new HotelManagementException("Particular Id "+id +" is not present in elastic index");
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Boolean isIdExists(String id, String indexName){
        try {
            // Create a GetRequest to check document existence
            GetRequest getRequest = new GetRequest(indexName, id);
            // Disable source retrieval, as we only want to check existence
            getRequest.fetchSourceContext(new FetchSourceContext(false));
            boolean exists = elasticSearchClient.exists(getRequest, RequestOptions.DEFAULT);
            return exists;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    //to check id is exists in elastic with particular pattern
    public Boolean isIdExistsInElasticIndexes(String id, String indexPattern) {
        try{
            // Create a SearchRequest with a wildcard query on the _id field
            SearchRequest searchRequest = new SearchRequest(indexPattern);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.wildcardQuery("_id", id));
            searchRequest.source(searchSourceBuilder);

            // Execute the search request
            SearchResponse searchResponse = elasticSearchClient.search(searchRequest, RequestOptions.DEFAULT);

            // Process the search response and extract document IDs
            Optional<SearchHit> searchHit = Arrays.stream(searchResponse.getHits().getHits()).findFirst();
            return searchHit.isPresent()?true:false;
            
        }catch (Exception e){
            throw new RuntimeException("Error occurred while fetching particular id from elastic",e);
        }
    }
}
