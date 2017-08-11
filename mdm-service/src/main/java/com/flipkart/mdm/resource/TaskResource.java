package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.client.MHttpClient;
import com.flipkart.mdm.dal.dao.CuratedFSNDAO;
import com.flipkart.mdm.dal.dao.TaskDAO;
import com.flipkart.mdm.dal.dao.TrendDAO;
import com.flipkart.mdm.dal.dao.UserDAO;
import com.flipkart.mdm.dal.exception.DBException;
import com.flipkart.mdm.model.CuratedFSN;
import com.flipkart.mdm.model.Task;
import com.flipkart.mdm.model.Trend;
import com.flipkart.mdm.model.User;
import com.flipkart.mdm.model.enums.TaskStatus;
import com.flipkart.mdm.resource.request.TaskCompleteRequest;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by setu.poddar on 10/08/17.
 */
@Slf4j
@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {


    private CuratedFSNDAO curatedFSNDAO;
    private TaskDAO taskDAO;
    private UserDAO userDAO;
    private TrendDAO trendDAO;
    private MHttpClient mHttpClient;

    @Inject
    public TaskResource(TaskDAO taskDAO, UserDAO userDAO, CuratedFSNDAO curatedFSNDAO, TrendDAO trendDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
        this.curatedFSNDAO = curatedFSNDAO;
        this.trendDAO = trendDAO;
    }

    @GET
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTask(String taskId) throws DBException {
        return Response.ok(taskDAO.findById(taskId)).build();
    }


    @GET
    @Path("/{userId}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTasks(@PathParam("userId") String userId) throws DBException {
        User user = userDAO.findByName(userId);
        return Response.ok(taskDAO.getAll(user.getId())).build();
    }


    @POST
    @Path("/submit")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response submitTask(TaskCompleteRequest request) throws DBException {
        User user = userDAO.findByName(request.getUserId());
        Task task = taskDAO.findById(request.getTaskId());
        task.setStatus(TaskStatus.COMPLETED);
        taskDAO.save(task);
        Trend trend = trendDAO.findById(request.getTrendId());
        CuratedFSN curatedFSN = new CuratedFSN();
        curatedFSN.setTaskId(task);
        curatedFSN.setTrend(trend);
        curatedFSN.setUser(user);
        String fsns = request.getFsns().toArray().toString();
        curatedFSN.setFsns(fsns.replace("[", "").replace("]", ""));
        curatedFSNDAO.save(curatedFSN);

        int complete = 0;
        Set<String> allFSN = new HashSet();
        for (Task t : trend.getTasks()) {
            if (TaskStatus.COMPLETED.name().equals(t.getStatus())) {
                complete++;
                CuratedFSN curatedFSN_t = curatedFSNDAO.findByTaskId(t.getId());
                allFSN.addAll(Arrays.asList(curatedFSN_t.getFsns().split(",")));
            }
        }
        if (complete == trend.getTasks().size()) {
            mHttpClient.triggerMLAction(trend.getVertical(),request.getTrendId(), allFSN);
        }
        return Response.ok("{\"message\":\"saved\"}").build();
    }
}
