package com.flipkart.mdm.resource;

import com.codahale.metrics.annotation.Timed;
import com.flipkart.mdm.FileUtils;
import com.flipkart.mdm.model.Images;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
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

    @POST
    @Timed
    @UnitOfWork
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(FormDataMultiPart multiPart) {
        List<FormDataBodyPart> parts = multiPart.getFields("images");

        FormDataBodyPart title = multiPart.getField("title");
        FormDataBodyPart vertical = multiPart.getField("vertical");
        FormDataBodyPart must_have = multiPart.getField("must_have");
        FormDataBodyPart must_not_have = multiPart.getField("must_not_have");
        FormDataBodyPart start_date = multiPart.getField("start_date");
        FormDataBodyPart end_date = multiPart.getField("end_date");
        List<Images> images = new ArrayList();
        try {
            for (FormDataBodyPart part : parts) {
                Images image = new Images();
                image.setUrl(FileUtils.saveFile(part.getValueAs(InputStream.class)));
                images.add(image);
            }
        } catch (Exception e) {
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
