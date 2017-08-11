package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.Utils;
import com.flipkart.mdm.client.ZHttpClient;
import com.flipkart.mdm.dal.dao.QCFSNDAO;
import com.flipkart.mdm.dal.dao.TrendDAO;
import com.flipkart.mdm.dal.exception.DBException;
import com.flipkart.mdm.model.QCFSN;
import com.flipkart.mdm.model.Trend;
import com.flipkart.mdm.model.enums.TaskStatus;
import com.flipkart.mdm.resource.exception.SystemResourceException;
import com.flipkart.mdm.resource.request.TaskCompleteRequest;
import com.flipkart.mdm.resource.response.SearchResult;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by setu.poddar on 10/08/17.
 */
@Slf4j
@Path("/qc")
@Produces(MediaType.APPLICATION_JSON)
public class QCResource {

    private QCFSNDAO qcfsnDAO;
    private TrendDAO trendDAO;
    private ZHttpClient zHttpClient;

    @Inject
    public QCResource(QCFSNDAO qcfsnDAO,TrendDAO trendDAO, ZHttpClient zHttpClient) {
        this.qcfsnDAO = qcfsnDAO;
        this.trendDAO = trendDAO;
        this.zHttpClient= zHttpClient;
    }

    @POST
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response qc(TaskCompleteRequest request) {
        try {
            Trend trend = trendDAO.findById(request.getTrendId());
            QCFSN qcfsn = new QCFSN();
            qcfsn.setTrend(trend);
            qcfsn.setFsns(Utils.convert(request.getFsns()));
            qcfsn.setStatus(TaskStatus.CREATED);
            qcfsnDAO.save(qcfsn);
        } catch (DBException e) {
            throw new SystemResourceException(500,e.getMessage());
        }
        return Response.ok().build();
    }


    @GET
    @Path("/all")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(qcfsnDAO.getAll()).build();
    }



    @GET
    @Path("/{id}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImages(@PathParam("id") String id) {
        try {
            QCFSN qcfsn = qcfsnDAO.findById(id);

            SearchResult searchResult = new SearchResult();
            List<String> productIds = Arrays.asList(qcfsn.getFsns().split(","));
            searchResult.setStart(0);
            searchResult.setTotalCount(productIds.size());
            searchResult.setCount(productIds.size());
            for(String pr : productIds){
                searchResult.getProductIds().put(pr, new HashSet<String>());
            }
            return Response.ok(zHttpClient.getImageUrls(searchResult)).build();
        } catch (DBException e) {
            throw new SystemResourceException(500,e.getMessage());
        }
    }

    @POST
    @Path("/submit/{id}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitTask(@PathParam("id") String id, TaskCompleteRequest request) throws DBException {
        QCFSN qcfsn = qcfsnDAO.findById(id);
        qcfsn.setStatus(TaskStatus.COMPLETED);
        qcfsn.setFinalFsn(Utils.convert(request.getFsns()));
        qcfsnDAO.save(qcfsn);
        return Response.ok("{\"message\":\"save\"}").build();
    }


}
