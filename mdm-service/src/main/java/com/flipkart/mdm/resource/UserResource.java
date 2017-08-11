package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.dal.dao.RoleDAO;
import com.flipkart.mdm.dal.dao.UserDAO;
import com.flipkart.mdm.dal.exception.DBException;
import com.flipkart.mdm.model.Role;
import com.flipkart.mdm.model.User;
import com.flipkart.mdm.resource.exception.SystemResourceException;
import com.flipkart.mdm.resource.request.UserRequest;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by setu.poddar on 10/08/17.
 */
@Slf4j
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private UserDAO userDAO;
    private RoleDAO roleDAO;

    @Inject
    public UserResource(UserDAO userDAO, RoleDAO roleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }

    @GET
    @Path("/{userId}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("userId")String userId) {

        return Response.ok(userDAO.findByName(userId)).build();
    }

    @GET
    @Path("/{userId}/roles")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRoles(@PathParam("userId")String userId) {
        List<String> res = new ArrayList();
        Set<Role> rolesList = userDAO.findByName(userId).getRoles();
        for(Role r : rolesList){
            res.add(r.getName());
        }
        return Response.ok(res).build();
    }


    @PUT
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserRequest request) {
        User user = new User();
        user.setName(request.getUsername());
        user.setPassword(request.getPassword());
        for (String role : request.getRoles()) {
            user.getRoles().add(roleDAO.getByName(role));
        }
        try {
            userDAO.save(user);
        } catch (DBException e) {
            throw new SystemResourceException(500, e.getMessage());
        }
        return Response.ok("User Created").build();
    }

    @POST
    @Path("/{userId}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRole(@PathParam("userId") String userId, @QueryParam("roles") String roles) {
        User user = userDAO.findByName(userId);
        for (String role : roles.split(",")) {
            user.getRoles().add(roleDAO.getByName(role));
        }
        try {
            userDAO.save(user);
        } catch (DBException e) {
            throw new SystemResourceException(500, e.getMessage());
        }
        return Response.ok("User Created").build();
    }

}
