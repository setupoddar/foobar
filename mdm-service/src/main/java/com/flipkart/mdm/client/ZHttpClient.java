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


/**
 * Created by setu.poddar on 04/07/17.
 */
@Slf4j
@Singleton
public class ZHttpClient {

    private Gson gson;
    private Client client;

    @Inject
    public ZHttpClient(Gson gson, Client client) throws Exception {
        this.gson = gson;
        this.client = client;
    }

    @Timed
    public SearchResult getImageUrls(final SearchResult result) {
        String fsns= result.getProductIds().keySet().toString().replace("[","").replace("]","").replaceAll("\\s", "").trim();
        String json = getResource(fsns).get(String.class);
        JsonObject r = gson.fromJson(json, JsonObject.class);
        JsonArray entityViews = (JsonArray) r.get("entityViews");
        for (int i = 0; i < entityViews.size(); i++) {
            JsonObject entity = (JsonObject) entityViews.get(i);
            String fsn = entity.getAsJsonPrimitive("entityId").getAsString();
            JsonObject view = (JsonObject) entity.get("view");
            if(view.has("primary_image_id")){
                String primaryImage = view.getAsJsonPrimitive("primary_image_id").getAsString().toLowerCase();
                JsonArray images = (JsonArray) view.get("static_images");
                for (int j = 0; j < images.size(); j++) {
                    String image  =  images.get(j).getAsJsonPrimitive().getAsString();
                    if(image.contains(primaryImage)){
                        result.getProductIds().get(fsn).add(image.replace("-original-","-400x400-"));
                        break;
                    }
                }
            }
        }
        return result;
    }

    private Invocation.Builder getResource(String path) {
        return client.target("http://10.32.105.161:31100/views/product_base_info?entityIds=" + path)
                .request(MediaType.APPLICATION_JSON);
    }
}
