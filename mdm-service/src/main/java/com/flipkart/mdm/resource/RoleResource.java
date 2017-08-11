package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.dal.dao.RoleDAO;
import com.flipkart.mdm.dal.exception.DBException;
import com.flipkart.mdm.model.Role;
import com.flipkart.mdm.resource.exception.SystemResourceException;
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
@Path("/role")
@Produces(MediaType.APPLICATION_JSON)
public class RoleResource {

    private RoleDAO roleDAO;

    @Inject
    public RoleResource(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    @GET
    @Path("/{roleId}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRole(@PathParam("roleId") String roleId) {
        return Response.ok(roleDAO.getByName(roleId)).build();
    }


    @PUT
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRole(String role) {

        Role r = new Role();
        r.setName(role);
        try {
            roleDAO.save(r);
        } catch (DBException e) {
            throw new SystemResourceException(500, e.getMessage());
        }
        return Response.ok("Created").build();
    }

}
