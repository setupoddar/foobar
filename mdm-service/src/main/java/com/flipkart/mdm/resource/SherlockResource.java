package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.client.SHttpClient;
import com.flipkart.mdm.client.ZHttpClient;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;

/**
 * Created by setu.poddar on 10/08/17.
 */
@Slf4j
@Path("/sherlock")
@Produces(MediaType.APPLICATION_JSON)
public class SherlockResource {

    private SHttpClient sHttpClient;
    private ZHttpClient zHttpClient;

    @Inject
    public SherlockResource(SHttpClient sHttpClient, ZHttpClient zHttpClient) {
        this.sHttpClient = sHttpClient;
        this.zHttpClient = zHttpClient;
    }

    @GET
    @Path("/filter/{query}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilters(@PathParam("query") String searchString) throws UnsupportedEncodingException {
        return Response.ok(sHttpClient.getFilters(searchString)).build();
    }


    @GET
    @Path("/search/{query}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(@PathParam("query") String searchString,
                                @DefaultValue("0") @QueryParam("start") Integer start,
                                @DefaultValue("60") @QueryParam("count") Integer count) throws UnsupportedEncodingException {
        return Response.ok(zHttpClient.getImageUrls(sHttpClient.getSearchResults(searchString, start, count))).build();
    }


}
