package com.flipkart.mdm.client;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.resource.response.SearchResult;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


/**
 * Created by setu.poddar on 04/07/17.
 */
@Slf4j
@Singleton
public class SHttpClient {

    private Gson gson;
    private Client client;
    private static final String FILTER_PATH = "filter?q=QUERY&client.tag=mobile-app-retail&products.type=listing&geoBrowse=enabled&redirection=true&facet-show=all";
    private static final String SEARCH_PATH = "iterator?query-guide=true&q=QUERY&client.tag=mobile-app-retail&products.count=RESULT_COUNT&products.type=listing&geoBrowse=enabled&query-stub=true&products.start=RESULT_START&redirection=true&smart-classify=true";

    @Inject
    public SHttpClient(Gson gson, Client client) throws Exception {
        this.gson = gson;
        this.client = client;
    }

    @Timed
    public Map<String, Set<String>> getFilters(String searchString) throws UnsupportedEncodingException {
        Map<String, Set<String>> response = new HashMap<String, Set<String>>();
        String json = getResource(FILTER_PATH.replaceAll("QUERY", URLEncoder.encode(searchString, "UTF-8"))).get(String.class);
        JsonObject r = gson.fromJson(json, JsonObject.class);
        JsonObject status = (JsonObject) r.get("STATUS");
        String statusString = String.valueOf(status.get("code"));
        if ("200".equals(statusString)) {
            JsonObject res = (JsonObject) r.get("RESPONSE");
            JsonArray facets = (JsonArray) res.get("facets");
            for (int i = 0; i < facets.size(); i++) {
                JsonObject facet = (JsonObject) facets.get(i);
                String title = String.valueOf((facet.getAsJsonPrimitive("title").getAsString()));
                response.put(title, new HashSet<String>());
                JsonArray values = (JsonArray) ((JsonObject) ((JsonArray) facet.get("values")).get(0)).get("values");
                for (int j = 0; j < values.size(); j++) {
                    String value = String.valueOf(((JsonObject) values.get(j)).getAsJsonPrimitive("title").getAsString());
                    if ("color".equalsIgnoreCase(title)) {
                        for (String color : Arrays.asList(value.replaceAll("/", ",").replace("+", ",").replaceAll("&", ",").split(",")))
                            response.get(title).add(color.toLowerCase().trim());
                    } else {
                        response.get(title).add(value.toLowerCase());
                    }
                }
            }
        }
        return response;
    }


    @Timed
    public SearchResult getSearchResults(String searchString, int start, int count) throws UnsupportedEncodingException {
        SearchResult response = new SearchResult();
        String path = SEARCH_PATH
                .replaceAll("QUERY", URLEncoder.encode(searchString, "UTF-8"))
                .replaceAll("RESULT_COUNT", String.valueOf(count))
                .replaceAll("RESULT_START", String.valueOf(start));
        String json = getResource(path).get(String.class);
        JsonObject r = gson.fromJson(json, JsonObject.class);
        JsonObject status = (JsonObject) r.get("STATUS");
        String statusString = String.valueOf(status.get("code"));
        if ("200".equals(statusString)) {
            JsonObject res = (JsonObject) r.get("RESPONSE");
            JsonObject products = (JsonObject) res.get("products");
            response.setCount(products.getAsJsonPrimitive("num_returned").getAsInt());
            response.setTotalCount(products.getAsJsonPrimitive("num_found").getAsInt());
            response.setStart(start);
            JsonArray ids = (JsonArray) products.get("ids");
            for (int i = 0; i < ids.size(); i++) {
                response.getProductIds().put(String.valueOf(ids.get(i).getAsJsonPrimitive().getAsString()), new HashSet<String>());
            }
        }
        return response;
    }

    private Invocation.Builder getResource(String path) {
        return client.target("http://10.47.1.95:25290/sherlock/v1/stores/search.flipkart.com/" + path)
                .request(MediaType.APPLICATION_JSON).header("x-flipkart-client", "fashion-hackday");
    }
}
