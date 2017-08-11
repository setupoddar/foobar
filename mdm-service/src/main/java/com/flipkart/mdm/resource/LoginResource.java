package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.dal.dao.UserDAO;
import com.flipkart.mdm.model.User;
import com.flipkart.mdm.resource.exception.SystemResourceException;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by setu.poddar on 10/08/17.
 */
@Slf4j
@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private UserDAO userDAO;

    @Inject
    public LoginResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @POST
    @Timed
    @UnitOfWork
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(FormDataMultiPart multiPart) {
        FormDataBodyPart username = multiPart.getField("username");
        FormDataBodyPart password = multiPart.getField("password");
        User user = userDAO.findByName(String.valueOf(username.getValue()));
        if (user != null && user.getPassword().equals(String.valueOf(password.getValue()))) {
            return Response.ok("{\"message\":\"Login Success\"}").build();
        }
        throw new SystemResourceException(500, "Credentials does not match");
    }

}
