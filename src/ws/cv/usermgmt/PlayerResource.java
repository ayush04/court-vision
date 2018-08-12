/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws.cv.usermgmt;

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
import com.cv.user.UserData;
import com.cv.user.PlayerService;

import com.cv.bindings.ws.model.gen.player.ObjectFactory;
import com.cv.bindings.ws.model.gen.player.Playerdata;
import com.cv.user.UserService;

import ws.cv.utils.JAXBWrapper;

/**
 *
 * @author ayush
 */
@Path("player")
public class PlayerResource {
    private PlayerResource() {
    }
    
    @GET
    @Path("details/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerProfile(@PathParam("userId") String userId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        UserData userData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        Playerdata pData = of.createPlayerdata();
        com.cv.bindings.ws.model.gen.player.Player player = of.createPlayer();
        long uId = -1;
        if (userId != null && !"".equals(userId)) {
            uId = Long.parseLong(userId);
        }

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            userData = UserService.getUserDetails(uId);
            if (userData == null) {
                error = true;
                errorMesg = "User " + userId + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && userData != null) {
            pData.setFirstName(userData.getFirstName());
            pData.setUserId(userData.getUserId());
            pData.setLastName(userData.getLastName());
            pData.setUserName(userData.getUserName());
            /* Add other details */
            pData.setTotalBaskets((int)PlayerService.totalBasketsScoredByPlayer(uId));
            player.addPlayer(pData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.player.Player> wrapper = new JAXBWrapper<>();
        if (error) {
            player.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(player);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(player);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("game/{gameId}/stats/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerStatsInGame(@PathParam("userId") String userId, 
            @PathParam("gameId") String gameId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        UserData userData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        Playerdata pData = of.createPlayerdata();
        com.cv.bindings.ws.model.gen.player.Player player = of.createPlayer();
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
            userData = UserService.getUserDetails(uId);
            if (userData == null) {
                error = true;
                errorMesg = "User " + userId + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && userData != null) {
            pData.setFirstName(userData.getFirstName());
            pData.setUserId(userData.getUserId());
            pData.setLastName(userData.getLastName());
            pData.setUserName(userData.getUserName());
            /* Add other details */
            pData.setTotalBaskets((int) PlayerService.totalBasketsScoredInGameByPlayer(uId, gId));
            pData.setTotalDistanceCovered(PlayerService.totalDistanceCoveredInGame(uId, gId));
            pData.setAverageSpeed(PlayerService.getAverageSpeedOfPlayerInGame(uId, gId));
            player.addPlayer(pData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.player.Player> wrapper = new JAXBWrapper<>();
        if (error) {
            player.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(player);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(player);
            return Response.status(Status.OK).entity(response).build();
        }
    }
}
