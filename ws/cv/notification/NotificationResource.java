/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws.cv.notification;

import com.cv.bindings.ws.model.gen.notification.Notificationdata;
import com.cv.bindings.ws.model.gen.notification.ObjectFactory;
import com.cv.mapping.PlayerTeamMappingService;
import com.cv.notification.NotificationData;
import com.cv.notification.NotificationService;
import com.cv.session.SessionService;
import com.cv.user.UserData;
import com.cv.user.UserService;
import com.cv.utils.Constants;
import com.cv.utils.Utils;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path("notification")
public class NotificationResource {
    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPendingNotificationsForUser(@QueryParam("responseType") String responseType,
            @PathParam("userId") String userId, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.notification.Notification notification = of.createNotification();
        long uId = -1;
        if (userId != null && !"".equals(userId)) {
            uId = Long.parseLong(userId);
        }
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Response.Status.UNAUTHORIZED;
        }
        else {
            List<NotificationData> notifications = NotificationService.getAllPendingNotificationsForUser(uId);
            notifications.forEach((n) -> {
                Notificationdata nData = of.createNotificationdata();
                nData.setContent(n.getContent());
                nData.setCreatedBy(n.getCreatedBy());
                nData.setCreatedFor(n.getCreatedFor());
                nData.setNotificationType(n.getNotificationType());
                nData.setIsRead(n.getIsRead());
                nData.setNotificationId(n.getNotificationId());
                nData.setTitle(n.getTitle());
                nData.setWhenCreated(n.getWhenCreated());
                nData.setWhenRead(n.getWhenRead());
                notification.addNotification(nData);
            });
        }
        
        JAXBWrapper<com.cv.bindings.ws.model.gen.notification.Notification> wrapper = new JAXBWrapper<>();
        if (error) {
            notification.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(notification);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(notification);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
    
    @PUT
    @Path("{notificationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, "multipart/form-data", "application/x-www-form-urlencoded"})
    public Response markNotificationAsRead(@QueryParam("responseType") String responseType,
            @PathParam("notificationId") String notificationId, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        com.cv.bindings.ws.model.gen.generic.ObjectFactory of = new com.cv.bindings.ws.model.gen.generic.ObjectFactory();
        com.cv.bindings.ws.model.gen.generic.Generic generic = of.createGeneric();
        long nId = -1;
        if (notificationId != null && !"".equals(notificationId)) {
            nId = Long.parseLong(notificationId);
        }
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Response.Status.UNAUTHORIZED;
        }
        else {
            NotificationService.markNotificationAsRead(nId);
            com.cv.bindings.ws.model.gen.generic.Genericdata data = new com.cv.bindings.ws.model.gen.generic.Genericdata();
            data.setKey("success");
            data.setValue("true");
            generic.addGenericData(data);
        }
        
        JAXBWrapper<com.cv.bindings.ws.model.gen.generic.Generic> wrapper = new JAXBWrapper<>();
        if (error) {
            generic.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
    
    @POST
    @Path("{notificationType}/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, "multipart/form-data", "application/x-www-form-urlencoded"})
    public Response createNotification(@QueryParam("responseType") String responseType,
            @PathParam("notificationType") String notificationType, 
            @PathParam("userId") String userId, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        com.cv.bindings.ws.model.gen.generic.ObjectFactory of = new com.cv.bindings.ws.model.gen.generic.ObjectFactory();
        com.cv.bindings.ws.model.gen.generic.Generic generic = of.createGeneric();
        com.cv.bindings.ws.model.gen.generic.Genericdata genericData = of.createGenericdata();
        int nType = -1;
        long uId = -1;
        if (notificationType != null && !"".equals(notificationType)) {
            nType = Integer.parseInt(notificationType);
        }
        if (userId != null && !"".equals(userId)) {
            uId = Long.parseLong(userId);
        }
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Response.Status.UNAUTHORIZED;
        }
        else {
            if(nType == Constants.COACH_ACCESS_NOTIFICATION) {
                UserData uData = UserService.getUserById(uId);
                long teamId = PlayerTeamMappingService.getTeamOfUser(uId);
                List<Long> coachIds = PlayerTeamMappingService.getCoachOfTeam(teamId);
                if(coachIds != null && coachIds.size() > 0) {
                    for(int i=0; i<coachIds.size(); i++) {
                        NotificationData data = new NotificationData();
                        data.setContent(uData.getUserName() + " has requested access as a coach");
                        data.setTitle("Access request");
                        data.setCreatedBy(uId);
                        data.setCreatedFor(coachIds.get(i));
                        data.setWhenCreated(Utils.getCurrentTimestamp());
                        data.setNotificationType(nType);
                        NotificationService.createNotification(data);
                    }
                    genericData.setKey("success");
                    genericData.setValue("true");
                }
                else {
                    com.cv.bindings.ws.model.gen.generic.Genericdata genericData1 = of.createGenericdata();
                    genericData1.setKey("success");
                    genericData1.setValue("false");
                    genericData.setKey("message");
                    genericData.setValue("No coach for team. Please register as coach");
                    generic.addGenericData(genericData1);
                    generic.addGenericData(genericData);
                }
            }
        }
        
        JAXBWrapper<com.cv.bindings.ws.model.gen.generic.Generic> wrapper = new JAXBWrapper<>();
        if (error) {
            generic.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("coach-request/{teamId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCoachRequestNotifications(@QueryParam("responseType") String responseType,
            @PathParam("teamId") String teamId, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.notification.Notification notification = of.createNotification();
        long tId = -1;
        if (teamId != null && !"".equals(teamId)) {
            tId = Long.parseLong(teamId);
        }
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Response.Status.UNAUTHORIZED;
        } else {
            List<NotificationData> notifications = NotificationService.getCoachRequestNotification(tId);
            notifications.forEach((n) -> {
                Notificationdata nData = of.createNotificationdata();
                nData.setContent(n.getContent());
                nData.setCreatedBy(n.getCreatedBy());
                nData.setCreatedFor(n.getCreatedFor());
                nData.setNotificationType(n.getNotificationType());
                nData.setIsRead(n.getIsRead());
                nData.setNotificationId(n.getNotificationId());
                nData.setTitle(n.getTitle());
                nData.setWhenCreated(n.getWhenCreated());
                nData.setWhenRead(n.getWhenRead());
                notification.addNotification(nData);
            });
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.notification.Notification> wrapper = new JAXBWrapper<>();
        if (error) {
            notification.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(notification);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(notification);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
}
