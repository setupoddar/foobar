package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.FileUtils;
import com.flipkart.mdm.dal.dao.TaskDAO;
import com.flipkart.mdm.dal.dao.TrendDAO;
import com.flipkart.mdm.dal.dao.UserDAO;
import com.flipkart.mdm.dal.exception.DBException;
import com.flipkart.mdm.model.Images;
import com.flipkart.mdm.model.Task;
import com.flipkart.mdm.model.Trend;
import com.flipkart.mdm.model.User;
import com.flipkart.mdm.model.enums.TaskStatus;
import com.flipkart.mdm.resource.exception.SystemResourceException;
import com.google.inject.Inject;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by setu.poddar on 22/06/17.
 */
@Slf4j
@Path("/trend")
@Produces(MediaType.APPLICATION_JSON)
public class TrendResource {

    private static final String[] pattern = {"MM/dd/yyyy"};

    private TrendDAO trendDAO;
    private UserDAO userDAO;
    private TaskDAO taskDAO;

    @Inject
    public TrendResource(TrendDAO trendDAO) {
        this.trendDAO = trendDAO;
    }

    @POST
    @Timed
    @UnitOfWork
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(FormDataMultiPart multiPart) {
        List<FormDataBodyPart> parts = multiPart.getFields("images");

        FormDataBodyPart title = multiPart.getField("title");
        FormDataBodyPart vertical = multiPart.getField("vertical");
        FormDataBodyPart mustHave = multiPart.getField("must_have");
        FormDataBodyPart mustNotHave = multiPart.getField("must_not_have");
        FormDataBodyPart startDate = multiPart.getField("start_date");
        FormDataBodyPart endDate = multiPart.getField("end_date");
        List<Images> images = new ArrayList();
        try {
            for (FormDataBodyPart part : parts) {
                Images image = new Images();
                image.setUrl(FileUtils.saveFile(part.getValueAs(InputStream.class)));
                images.add(image);
            }
        } catch (Exception e) {
            throw new SystemResourceException(500, "Error while dowloading file");
        }

        try {
            Trend trend = new Trend();
            trend.setTitle(String.valueOf(title.getValue()));
            trend.setVertical(String.valueOf(vertical.getValue()));
            trend.setStartDate(DateUtils.parseDate(String.valueOf(startDate.getValue()), pattern));
            trend.setEndDate(DateUtils.parseDate(String.valueOf(endDate.getValue()), pattern));
            trend.setMustHave(String.valueOf(mustHave.getValue()));
            trend.setMustNotHave(String.valueOf(mustNotHave.getValue()));
            trend.getImages().addAll(images);
            trendDAO.save(trend);
        } catch (ParseException e) {
            throw new SystemResourceException(500, "Date format is not correct - " + e.getMessage());
        } catch (DBException e) {
            throw new SystemResourceException(500, "Error while saving entity - " + e.getMessage());
        }
        return Response.ok("{\"message\":\"Trend created\"}").build();
    }


    @GET
    @Path("/{trendid}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrend(@PathParam("trendid") String trendId) {
        try {
            Trend trend = trendDAO.findById(trendId);
            return Response.ok(trend).build();
        } catch (DBException e) {
            throw new SystemResourceException(500, e.getMessage());
        }
    }


    @POST
    @Path("/{trendid}")
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCurator(@PathParam("trendid") String trendId, @QueryParam("users") String userIds) {
        int count =0;
        try {
            Trend trend = trendDAO.findById(trendId);
            if(trend != null){
                for(String  userId :  userIds.split(",")){
                    User user = userDAO.findById(userId);
                    if(user != null){
                        Task task = new Task();
                        task.setStatus(TaskStatus.CREATED);
                        task.setTrend(trend);
                        task.setUser(user);
                        taskDAO.save(task);
                        count++;
                    }
                }
            }
            return Response.ok("Task Created for users").build();
        } catch (DBException e) {
            throw new SystemResourceException(500, e.getMessage());
        }
    }
}
