/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws.cv.attachment;

import com.cv.attachment.AttachmentData;
import com.cv.attachment.AttachmentService;
import com.cv.bindings.ws.model.gen.attachment.Attachmentdata;
import com.cv.bindings.ws.model.gen.attachment.ObjectFactory;
import com.cv.session.SessionService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import ws.cv.utils.JAXBWrapper;

/**
 *
 * @author ayush
 */
@Path("user/image")
public class AttachmentResource {
    private AttachmentResource() {
    }
    
    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProfilePictureOfUser(@PathParam("userId") String userId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        AttachmentData profileImg = null;

        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.attachment.Attachment attachment = of.createAttachment();
        long uId = -1;
        if (userId != null && !"".equals(userId)) {
            uId = Long.parseLong(userId);
        }

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Response.Status.UNAUTHORIZED;
        } else {
            try {
                profileImg = AttachmentService.getProfileImageOfUser(uId);
            } catch (Exception ex) {
                error = true;
                errorMesg = "Invalid user ID";
                status = Response.Status.PRECONDITION_FAILED;
                Logger.getLogger(AttachmentResource.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (profileImg != null) {
                Attachmentdata ad = of.createAttachmentdata();
                ad.setAttachmentFormat(profileImg.getAttachmentFormat());
                ad.setAttachmentId(profileImg.getAttachmentId());
                ad.setAttachmentName(profileImg.getAttachmentName());
                ad.setWhenCreated(profileImg.getWhenCreated());
                attachment.addAttachment(ad);
            }
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.attachment.Attachment> wrapper = new JAXBWrapper<>();
        if (error) {
            attachment.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(attachment);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(attachment);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("{attachmentId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getAttachmentContent(@PathParam("attachmentId") String attachmentId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        AttachmentData profileImg = null;

        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.attachment.Attachment attachment = of.createAttachment();
        long aId = -1;
        if (attachmentId != null && !"".equals(attachmentId)) {
            aId = Long.parseLong(attachmentId);
        }

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Response.Status.UNAUTHORIZED;
        } else {
            try {
                profileImg = AttachmentService.downloadAttachment(aId);
            } catch (Exception ex) {
                error = true;
                errorMesg = "Invalid user ID";
                status = Response.Status.PRECONDITION_FAILED;
                Logger.getLogger(AttachmentResource.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (profileImg != null) {
                Attachmentdata ad = of.createAttachmentdata();
                ad.setAttachmentFormat(profileImg.getAttachmentFormat());
                ad.setAttachmentId(profileImg.getAttachmentId());
                ad.setAttachmentName(profileImg.getAttachmentName());
                ad.setWhenCreated(profileImg.getWhenCreated());
                attachment.addAttachment(ad);
            }
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.attachment.Attachment> wrapper = new JAXBWrapper<>();
        if (error) {
            attachment.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(attachment);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(attachment);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
}
