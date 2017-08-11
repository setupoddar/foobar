package com.flipkart.mdm.client;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import java.util.Set;


/**
 * Created by setu.poddar on 04/07/17.
 */
@Slf4j
@Singleton
public class MHttpClient {

    private Client client;
    private Gson gson;
    private static final String PATH = "http://172.20.45.47:8080/v2";

    @Inject
    public MHttpClient(Client client, Gson gson) throws Exception {
        this.client = client;
        this.gson = gson;
    }

    @Timed
    public void triggerMLAction(String vertical, String trendId, Set<String> allFSN) {
        getResource(PATH).post(Entity.json(new  MHttpClient.R(vertical, trendId, allFSN)));
    }

    private Invocation.Builder getResource(String path) {
        return client.target(path).request(MediaType.APPLICATION_JSON);
    }


    static class R {
        private String vertical;
        private String trendId;
        private Set<String> fsn;

        public R(String vertical, String trendId, Set<String> fsn) {
            this.vertical = vertical;
            this.trendId = trendId;
            this.fsn = fsn;
        }

        public String getVertical() {
            return vertical;
        }

        public void setVertical(String vertical) {
            this.vertical = vertical;
        }

        public String getTrendId() {
            return trendId;
        }

        public void setTrendId(String trendId) {
            this.trendId = trendId;
        }

        public Set<String> getFsn() {
            return fsn;
        }

        public void setFsn(Set<String> fsn) {
            this.fsn = fsn;
        }
    }

}
