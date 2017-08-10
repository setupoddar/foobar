package com.flipkart.mdm.client;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.util.List;


/**
 * Created by setu.poddar on 04/07/17.
 */
@Slf4j
@Singleton
public class VHttpClient {

    private Gson gson;
    private Client client;

    @Inject
    public VHttpClient(Gson gson, Client client) throws Exception {
        this.gson = gson;
        this.client = client;
    }

    @Timed
    public List<String> getVerticals() {
        String json = getResource("listAllCategoryNames").get(String.class);
        return gson.fromJson(json, List.class);
    }

    private Invocation.Builder getResource(String path) {
        return client.target("http://10.85.52.21/category-service-manager/rest/category/" + path)
                .request(MediaType.APPLICATION_JSON);
    }
}
