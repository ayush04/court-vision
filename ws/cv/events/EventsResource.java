/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws.cv.events;

import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;

import javax.servlet.http.HttpServletRequest;

import com.cv.session.SessionService;

import com.cv.bindings.ws.model.gen.events.ObjectFactory;
import com.cv.bindings.ws.model.gen.events.Eventsdata;
import com.cv.events.EventsData;
import com.cv.events.EventsService;
import java.util.List;

import ws.cv.utils.JAXBWrapper;
/**
 *
 * @author ayush
 */
@Path("events")
public class EventsResource {
    private EventsResource(){
    }

    @GET
    @Path("game/{gameId}/position/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerPositions(@PathParam("gameId") String gameId, 
            @PathParam("userId") String userId, @QueryParam("responseType") 
            String responseType, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        List<EventsData> eventsData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.events.Events event = of.createEvents();
        long uId = -1;
        long gId = -1;
        if (userId != null && !"".equals(userId)) {
            uId = Long.parseLong(userId);
        }
        if (gameId != null && !"".equals(gameId)) {
            gId = Long.parseLong(gameId);
        }
        
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            eventsData = EventsService.getPlayerPositions(uId, gId);
            if (eventsData == null) {
                error = true;
                errorMesg = "User " + userId + " or game " + gameId + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        
        if (!error && eventsData != null) {
            eventsData.forEach((e) -> {
                Eventsdata eData = of.createEventsdata();
                eData.setEventId(e.getEventId());
                eData.setEventTime(e.getEventTime());
                eData.setEventType(e.getEventType());
                eData.setEventValue(e.getEventValue());
                eData.setEventValue2(e.getEventValue2());
                eData.setEventValue3(e.getEventValue3());
                eData.setEventValue4(e.getEventValue4());
                eData.setGameId(e.getGameId());
                eData.setUserId(e.getUserId());
                
                event.addEvent(eData);
            });
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.events.Events> wrapper = new JAXBWrapper<>();
        if (error) {
            event.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(event);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(event);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("game/{gameId}/position/{userId}/{startTime}/{endTime}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerPositionsInDuration(@PathParam("gameId") String gameId,
            @PathParam("userId") String userId, @PathParam("startTime") String startTime,
            @PathParam("endTime") String endTime, @QueryParam("responseType") String responseType, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        List<EventsData> eventsData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.events.Events event = of.createEvents();
        long uId = -1;
        long gId = -1;
        long sTime = -1;
        long eTime = -1;
        if (userId != null && !"".equals(userId)) {
            uId = Long.parseLong(userId);
        }
        if (gameId != null && !"".equals(gameId)) {
            gId = Long.parseLong(gameId);
        }
        if (startTime != null && !"".equals(startTime)) {
            sTime = Long.parseLong(startTime);
        }
        if (endTime != null && !"".equals(endTime)) {
            eTime = Long.parseLong(endTime);
        }

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            eventsData = EventsService.getPlayerPositions(uId, gId, sTime, eTime);
            if (eventsData == null) {
                error = true;
                errorMesg = "User " + userId + " or game " + gameId + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }

        if (!error && eventsData != null) {
            eventsData.forEach((e) -> {
                Eventsdata eData = of.createEventsdata();
                eData.setEventId(e.getEventId());
                eData.setEventTime(e.getEventTime());
                eData.setEventType(e.getEventType());
                eData.setEventValue(e.getEventValue());
                eData.setEventValue2(e.getEventValue2());
                eData.setEventValue3(e.getEventValue3());
                eData.setEventValue4(e.getEventValue4());
                eData.setGameId(e.getGameId());
                eData.setUserId(e.getUserId());

                event.addEvent(eData);
            });
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.events.Events> wrapper = new JAXBWrapper<>();
        if (error) {
            event.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(event);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(event);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("game/{gameId}/position/ball")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBallPositions(@PathParam("gameId") String gameId,
            @QueryParam("responseType") String responseType, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        List<EventsData> eventsData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.events.Events event = of.createEvents();
        
        long gId = -1;
        if (gameId != null && !"".equals(gameId)) {
            gId = Long.parseLong(gameId);
        }

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            eventsData = EventsService.getBallPositions(gId);
            if (eventsData == null) {
                error = true;
                errorMesg = "Game " + gameId + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }

        if (!error && eventsData != null) {
            eventsData.forEach((e) -> {
                Eventsdata eData = of.createEventsdata();
                eData.setEventId(e.getEventId());
                eData.setEventTime(e.getEventTime());
                eData.setEventType(e.getEventType());
                eData.setEventValue(e.getEventValue());
                eData.setEventValue2(e.getEventValue2());
                eData.setEventValue3(e.getEventValue3());
                eData.setEventValue4(e.getEventValue4());
                eData.setGameId(e.getGameId());
                eData.setUserId(e.getUserId());

                event.addEvent(eData);
            });
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.events.Events> wrapper = new JAXBWrapper<>();
        if (error) {
            event.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(event);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(event);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("game/{gameId}/position/ball/{startTime}/{endTime}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBallPositionsInDuration(@PathParam("gameId") String gameId,
            @PathParam("startTime") String startTime,
            @PathParam("endTime") String endTime, @QueryParam("responseType") String responseType, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        List<EventsData> eventsData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.events.Events event = of.createEvents();
        long gId = -1;
        long sTime = -1;
        long eTime = -1;
        if (gameId != null && !"".equals(gameId)) {
            gId = Long.parseLong(gameId);
        }
        if (startTime != null && !"".equals(startTime)) {
            sTime = Long.parseLong(startTime);
        }
        if (endTime != null && !"".equals(endTime)) {
            eTime = Long.parseLong(endTime);
        }

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            eventsData = EventsService.getBallPositions(gId, sTime, eTime);
            if (eventsData == null) {
                error = true;
                errorMesg = "Game " + gameId + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }

        if (!error && eventsData != null) {
            eventsData.forEach((e) -> {
                Eventsdata eData = of.createEventsdata();
                eData.setEventId(e.getEventId());
                eData.setEventTime(e.getEventTime());
                eData.setEventType(e.getEventType());
                eData.setEventValue(e.getEventValue());
                eData.setEventValue2(e.getEventValue2());
                eData.setEventValue3(e.getEventValue3());
                eData.setEventValue4(e.getEventValue4());
                eData.setGameId(e.getGameId());
                eData.setUserId(e.getUserId());

                event.addEvent(eData);
            });
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.events.Events> wrapper = new JAXBWrapper<>();
        if (error) {
            event.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(event);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(event);
            return Response.status(Status.OK).entity(response).build();
        }
    }
}
