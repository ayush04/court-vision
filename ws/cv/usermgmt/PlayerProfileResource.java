/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws.cv.usermgmt;

import com.cv.bindings.ws.model.gen.profile.ObjectFactory;
import com.cv.bindings.ws.model.gen.profile.Profiledata;
import com.cv.events.EventsService;
import com.cv.exception.UserNotExistException;
import com.cv.playerprofile.ProfileData;
import com.cv.playerprofile.ProfileService;
import com.cv.session.SessionService;
import com.cv.user.UserService;
import com.cv.utils.Utils;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.Response;
import ws.cv.utils.JAXBWrapper;

/**
 *
 * @author ayush
 */
@Path("player/profile")
public class PlayerProfileResource {
    private PlayerProfileResource() {}
    
    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerProfile(@PathParam("userId") String userId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        ProfileData profileData = null;
        Response.Status status = Response.Status.OK;
        ObjectFactory of = new ObjectFactory();
        Profiledata pData = of.createProfiledata();
        com.cv.bindings.ws.model.gen.profile.Profile playerProfile = of.createProfile();
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
                profileData = ProfileService.getUserProfile(uId);
            } catch (UserNotExistException ex) {
                Logger.getLogger(PlayerProfileResource.class.getName()).log(Level.SEVERE, null, ex);
                error = true;
                errorMesg = "User " + userId + " does not exist";
                status = Response.Status.PRECONDITION_FAILED;
            }
            if (profileData == null) {
                error = true;
                errorMesg = "User " + userId + " does not exist";
                status = Response.Status.PRECONDITION_FAILED;
            }
        }
        if (!error && profileData != null) {
            pData.setUserId(uId);
            pData.setBasketsScored((int)(long)EventsService.getNumberOfBasketsByPlayer(uId));
            pData.setFitbitClientId(profileData.getFitbitClientId());
            pData.setFitbitEnabled(profileData.getFitbitEnabled());
            pData.setHeight(profileData.getHeight());
            pData.setIsDeleted(profileData.getIsDeleted());
            pData.setRole(profileData.getRole());
            pData.setTotalGamesPlayed(profileData.getTotalGamesPlayed());
            pData.setWeight(profileData.getWeight());
            pData.setWhenModified(profileData.getWhenDeleted());
            pData.setWhenDeleted(profileData.getWhenDeleted());
            playerProfile.addProfile(pData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.profile.Profile> wrapper = new JAXBWrapper<>();
        if (error) {
            playerProfile.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(playerProfile);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(playerProfile);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
    
    @PUT
    @Path("update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes({MediaType.APPLICATION_JSON, "multipart/form-data", "application/x-www-form-urlencoded"})
    public Response update(Form form, @Context HttpServletRequest request) {
        
        MultivaluedHashMap requestParams = new MultivaluedHashMap();
        form.asMap().entrySet().forEach((entry) -> {
            requestParams.add(entry.getKey(), entry.getValue());
        });
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.NO_CONTENT;
        ObjectFactory of = new ObjectFactory();
        Profiledata pData = of.createProfiledata();
        com.cv.bindings.ws.model.gen.profile.Profile playerProfile = of.createProfile();
        
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Response.Status.UNAUTHORIZED;
        }
        else {
            long uId = SessionService.getUserIdFromSession(sessionId);
            ProfileData profileData = new ProfileData();
            profileData.setUserId(uId);
            String height = requestParams.getFirst("height") != null ? String.valueOf(((LinkedList) requestParams.getFirst("height")).get(0)) : null;
            String weight = requestParams.getFirst("weight") != null ? String.valueOf(((LinkedList) requestParams.getFirst("weight")).get(0)) : null;
            String basketsScored = requestParams.getFirst("basketsScored") != null ? String.valueOf(((LinkedList) requestParams.getFirst("basketsScored")).get(0)) : null;
            String totalGamesPlayed = requestParams.getFirst("totalGamesPlayed") != null ? String.valueOf(((LinkedList) requestParams.getFirst("totalGamesPlayed")).get(0)) : null;
            String fitbitEnabled = requestParams.getFirst("fitbitEnabled") != null ? String.valueOf(((LinkedList) requestParams.getFirst("fitbitEnabled")).get(0)) : null;
            String fitbitClientId = requestParams.getFirst("fitbitClientId") != null ? String.valueOf(((LinkedList) requestParams.getFirst("fitbitClientId")).get(0)) : null;
            String role = requestParams.getFirst("role") != null ? String.valueOf(((LinkedList) requestParams.getFirst("role")).get(0)) : null;
            String password = requestParams.getFirst("password") != null ? String.valueOf(((LinkedList) requestParams.getFirst("password")).get(0)) : null;
            if(Utils.isFloat(height)) {
                profileData.setHeight(Float.parseFloat(height));
            }
            if(Utils.isFloat(weight)) {
                profileData.setWeight(Float.parseFloat(weight));
            }
            if(Utils.isNumber(basketsScored)) {
                profileData.setBasketsScored(Integer.parseInt(basketsScored));
            }
            if(Utils.isNumber(totalGamesPlayed)) {
                profileData.setTotalGamesPlayed(Integer.parseInt(totalGamesPlayed));
            }
            if(Utils.isNumber(fitbitEnabled)) {
                profileData.setFitbitEnabled(Integer.parseInt(fitbitEnabled));
            }
            profileData.setFitbitClientId(fitbitClientId);
            profileData.setRole(role);
            
            try {
                ProfileService.updateUserProfile(profileData);
                if(password != null) {
                    UserService.updatePassword(uId, password);
                }
            } catch (UserNotExistException ex) {
                Logger.getLogger(PlayerProfileResource.class.getName()).log(Level.SEVERE, null, ex);
                error = true;
                errorMesg = "Error while updating User Profile";
                status = Response.Status.INTERNAL_SERVER_ERROR;
            }
        }
        
        JAXBWrapper<com.cv.bindings.ws.model.gen.profile.Profile> wrapper = new JAXBWrapper<>();
        if (error) {
            playerProfile.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(playerProfile);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(playerProfile);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
}
