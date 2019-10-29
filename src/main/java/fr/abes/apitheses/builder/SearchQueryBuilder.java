package fr.abes.apitheses.builder;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SearchQueryBuilder {

    @Value("${es.hostname}")
    private String esHostname;
    @Value("${es.port}")
    private String esPort;
    @Value("${es.scheme}")
    private String esScheme;

    public SearchResponse getThesesByTitleAndAbstract(String searchString) throws IOException {

        Map<String, Float> fields = new HashMap<>();
        fields.put("abstractFR", (float) 0);
        fields.put("titre", (float) 5);

        QueryBuilder query = QueryBuilders.queryStringQuery(searchString).fields(fields);
        return executeQuery(query);
    }
    public SearchResponse getThesesByTitleAndAbstractAndEtab(String searchString, String etab) throws IOException {
        Map<String, Float> fields = new HashMap<>();
        fields.put("abstractFR", (float) 0);
        fields.put("titre", (float) 5);

        QueryBuilder queryString = QueryBuilders.queryStringQuery(searchString).fields(fields);
        QueryBuilder queryEtab = QueryBuilders.termQuery("etablissements", etab);
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(queryString).filter(queryEtab);
        return executeQuery(queryBuilder);
    }

    private RestHighLevelClient client = null;
    private RestHighLevelClient getRestHighLevelClient() {
        if (client == null)
            client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(esHostname, Integer.parseInt(esPort), esScheme)));
        return client;
    }
    private RequestOptions requestOptions = RequestOptions.DEFAULT;
    private SearchRequest searchRequest = new SearchRequest("theses");

    private SearchResponse executeQuery(QueryBuilder query) throws IOException {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchRequest.source(searchSourceBuilder);
        try {
            return getRestHighLevelClient().search(searchRequest, requestOptions);
        }
        catch (IOException ex) {
            log.error(ex.toString());
            throw ex;
        }
    }
}
