package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.FileUtils;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by setu.poddar on 22/06/17.
 */
@Slf4j
@Path("/trend")
@Produces(MediaType.APPLICATION_JSON)
public class TrendResource {

    @PUT
    @Timed
    @UnitOfWork
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(FormDataMultiPart multiPart) {


        String title = multiPart.getField("title").getValue();
        String vertical = multiPart.getField("vertical").getValue();
        String mustHave = multiPart.getField("must_have").getValue();
        String mustNotHave = multiPart.getField("must_not_have").getValue();
        String startDate = multiPart.getField("start_date").getValue();
        String endDate = multiPart.getField("end_date").getValue();
        List<String> images = new ArrayList();
        List<FormDataBodyPart> bodyParts = multiPart.getFields("images");
        for (FormDataBodyPart part : bodyParts) {
            try {
                images.add(FileUtils.saveFile(part.getValueAs(InputStream.class)));
            } catch (Exception e) {

            }
        }
        return Response.ok().build();
    }


    @GET
    @Timed
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrend(String trendId) {
        return Response.ok().build();
    }

}
