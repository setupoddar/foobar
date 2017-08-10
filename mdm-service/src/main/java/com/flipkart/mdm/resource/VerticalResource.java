package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.client.VHttpClient;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by setu.poddar on 10/08/17.
 */
@Slf4j
@Path("/vertical")
@Produces(MediaType.APPLICATION_JSON)
public class VerticalResource {

    private VHttpClient vHttpClient;

    @Inject
    public VerticalResource(VHttpClient vHttpClient) {
        this.vHttpClient = vHttpClient;
    }

    @GET
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVerticals() {
        return Response.ok(vHttpClient.getVerticals()).build();
    }


}
