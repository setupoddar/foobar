package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.dal.dao.TaskDAO;
import com.flipkart.mdm.dal.dao.UserDAO;
import com.flipkart.mdm.dal.exception.DBException;
import com.flipkart.mdm.model.User;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by setu.poddar on 10/08/17.
 */
@Slf4j
@Path("/task")
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {

    private TaskDAO taskDAO;
    private UserDAO userDAO;

    @Inject
    public TaskResource(TaskDAO taskDAO, UserDAO userDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
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
    public Response getTasks(@PathParam("userId")String userId) throws DBException {
        User user = userDAO.findByName(userId);
        return Response.ok(taskDAO.getAll(user.getId())).build();
    }
}
