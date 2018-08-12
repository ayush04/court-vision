/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.cv.usermgmt;

import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;

import javax.servlet.http.HttpServletRequest;

import com.cv.session.SessionService;
import com.cv.user.UserData;
import com.cv.user.UserService;
import com.cv.exception.*;

import com.cv.bindings.ws.model.gen.user.ObjectFactory;
import com.cv.bindings.ws.model.gen.user.Userdata;
import com.cv.mapping.PlayerTeamMappingService;
import com.cv.mapping.UserRoleMappingService;
import com.cv.playerprofile.ProfileData;
import com.cv.playerprofile.ProfileService;
import com.cv.utils.Utils;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedHashMap;

import ws.cv.utils.JAXBWrapper;

/**
 *
 * @author ayush
 */
@Path("user")
public class UserResource {

    /**
     * Creates a new instance of UserResource
     */
    private UserResource() {
    }

    /**
     * Retrieves representation of an instance of
     * ws.cv.usermgmt.UserResource
     *
     * @param userId
     * @param responseType
     * @param request
     * @return an instance of java.lang.String
     */
    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserDetails(@PathParam("userId") String userId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        UserData userData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        Userdata uData = of.createUserdata();
        com.cv.bindings.ws.model.gen.user.User user = of.createUser();
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
            uData.setFirstName(userData.getFirstName());
            uData.setUserId(userData.getUserId());
            uData.setLastName(userData.getLastName());
            uData.setUserName(userData.getUserName());
            uData.setEmailAddress(userData.getEmailAddress());
            uData.setWhenCreated(userData.getWhenCreated());
            uData.setWhenModified(userData.getWhenModified());
            uData.setLastAccessed(userData.getLastAccessed());
            user.addUser(uData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.user.User> wrapper = new JAXBWrapper<>();
        if (error) {
            user.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(user);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(user);
            return Response.status(Status.OK).entity(response).build();
        }
    }

    @POST
    @Path("login")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON, "multipart/form-data", "application/x-www-form-urlencoded"})
    public Response login(Form form, @Context HttpServletRequest request) {
        MultivaluedHashMap requestParams = new MultivaluedHashMap();
        form.asMap().entrySet().forEach((entry) -> {
            requestParams.add(entry.getKey(), entry.getValue());
        });
        boolean error = false;
        String errorMesg = "";
        String userSessionId = "";
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.user.User u = new com.cv.bindings.ws.model.gen.user.User();
        Userdata user = of.createUserdata();

        JAXBWrapper<com.cv.bindings.ws.model.gen.user.User> wrapper = new JAXBWrapper<>();
        String userName = String.valueOf(((LinkedList) requestParams.getFirst("userName")).get(0));
        String password = String.valueOf(((LinkedList) requestParams.getFirst("password")).get(0));
        if (userName == null || "".equals(userName)) {
            error = true;
            errorMesg = "User name passed is null";
            status = Status.BAD_REQUEST;
        } else if (password == null || "".equals(password)) {
            error = true;
            errorMesg = "Password passed is null";
            status = Status.BAD_REQUEST;
        } else {
            try {
                userSessionId = UserService.login(userName, password, request.getSession(), true);
                user.setUserName(userName);
                long userId = SessionService.getUserIdFromSession(userSessionId);
                if (userId != -1) {
                    user.setUserId(userId);
                }
            } catch (NoUserExistException e) {
                error = true;
                errorMesg = e.getMessage();
                status = Status.BAD_REQUEST;
            } catch (InvalidCredentialsException e) {
                error = true;
                errorMesg = e.getMessage();
                status = Status.FORBIDDEN;
            } catch (UserAlreadyLoggedInException e) {
                error = true;
                errorMesg = e.getMessage();
                status = Status.FORBIDDEN;
            } catch (SessionAlreadyExistException e) {
                error = true;
                errorMesg = e.getMessage();
                status = Status.FORBIDDEN;
            }
        }
        String response;
        if (error) {
            u.setErrorMesg(errorMesg);
            response = wrapper.jaxbObjectToJSON(u);
            return Response.status(status).entity(response).build();
        } else {
            u.addUser(user);
            u.setSessionId(userSessionId);
            response = wrapper.jaxbObjectToJSON(u);
            return Response.status(Status.OK).entity(response).build();
        }
    }

    @POST
    @Path("logout")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON, "multipart/form-data", "application/x-www-form-urlencoded"})
    public Response logout(@Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.user.User u = new com.cv.bindings.ws.model.gen.user.User();
        Userdata user = of.createUserdata();

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            try {
                UserService.logout(sessionId);
            } catch (InvalidSessionException e) {
                error = true;
                errorMesg = e.getMessage();
                status = Status.UNAUTHORIZED;
            }
        }
        String response;
        JAXBWrapper<com.cv.bindings.ws.model.gen.user.User> wrapper = new JAXBWrapper<>();
        if (error) {
            u.setErrorMesg(errorMesg);
        } else {
            u.addUser(user);
        }
        response = wrapper.jaxbObjectToJSON(u);
        return Response.status(status).entity(response).build();
    }

    
    @POST
    @Path("signup")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON, "multipart/form-data", "application/x-www-form-urlencoded"})
    public Response signup(Form form, @Context HttpServletRequest request) {
        MultivaluedHashMap requestParams = new MultivaluedHashMap();
        form.asMap().entrySet().forEach((entry) -> {
            requestParams.add(entry.getKey(), entry.getValue());
        });
        boolean error = false;
        String errorMesg = "";
        String userSessionId = "";
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.user.User u = new com.cv.bindings.ws.model.gen.user.User();
        Userdata userData = of.createUserdata();

        JAXBWrapper<com.cv.bindings.ws.model.gen.user.User> wrapper = new JAXBWrapper<>();
        String userName = String.valueOf(((LinkedList) requestParams.getFirst("userName")).get(0));
        String password = String.valueOf(((LinkedList) requestParams.getFirst("password")).get(0));
        String emailAddress = String.valueOf(((LinkedList) requestParams.getFirst("emailAddress")).get(0));
        if (userName == null || "".equals(userName)) {
            error = true;
            errorMesg = "User name passed is null";
            status = Status.BAD_REQUEST;
        } else if (password == null || "".equals(password)) {
            error = true;
            errorMesg = "Password passed is null";
            status = Status.BAD_REQUEST;
        } else {
            UserData user = new UserData();
            user.setEmailAddress(emailAddress);
            user.setUserName(userName);
            user.setPassword(password);
            long userId = UserService.createUser(user);
            ProfileData profile = new ProfileData();
            profile.setUserId(userId);
            ProfileService.saveUserProfile(profile);
            userData.setEmailAddress(emailAddress);
            userData.setUserName(userName);
            userData.setUserId(userId);
            userData.setWhenCreated(Utils.getCurrentTimestamp());
        }
        String response;
        if (error) {
            u.setErrorMesg(errorMesg);
            response = wrapper.jaxbObjectToJSON(u);
            return Response.status(status).entity(response).build();
        } else {
            u.addUser(userData);
            u.setSessionId(userSessionId);
            response = wrapper.jaxbObjectToJSON(u);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("coach/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isUserCoach(@PathParam("userId") String userId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        UserData userData = null;
        Status status = Status.OK;
        com.cv.bindings.ws.model.gen.generic.ObjectFactory of = new com.cv.bindings.ws.model.gen.generic.ObjectFactory();
        com.cv.bindings.ws.model.gen.generic.Genericdata gData = of.createGenericdata();
        com.cv.bindings.ws.model.gen.generic.Generic generic = of.createGeneric();
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
            boolean isCoach = UserRoleMappingService.isUserCoach(uId);
            gData.setKey("isCoach");
            gData.setValue(String.valueOf(isCoach));
            generic.addGenericData(gData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.generic.Generic> wrapper = new JAXBWrapper<>();
        if (error) {
            generic.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @POST
    @Path("{userId}/team/{teamId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUserToTeam(Form form, @PathParam("userId") String userId, @PathParam("teamId") String teamId, 
            @QueryParam("responseType") String responseType, @Context HttpServletRequest request) {
        MultivaluedHashMap requestParams = new MultivaluedHashMap();
        form.asMap().entrySet().forEach((entry) -> {
            requestParams.add(entry.getKey(), entry.getValue());
        });
        boolean error = false;
        String errorMesg = "";
        Status status = Status.OK;
        com.cv.bindings.ws.model.gen.generic.ObjectFactory of = new com.cv.bindings.ws.model.gen.generic.ObjectFactory();
        com.cv.bindings.ws.model.gen.generic.Genericdata gData = of.createGenericdata();
        com.cv.bindings.ws.model.gen.generic.Generic generic = of.createGeneric();
        long uId = -1;
        long tId = -1;
        if (userId != null && !"".equals(userId)) {
            uId = Long.parseLong(userId);
        }
        if (teamId != null && !"".equals(teamId)) {
            tId = Long.parseLong(teamId);
        }
        
        UserData userData = UserService.getUserDetails(uId);
        if (userData == null) {
            error = true;
            errorMesg = "User " + userId + " does not exist";
            status = Status.PRECONDITION_FAILED;
        }
        if (!error && userData != null) {
            PlayerTeamMappingService.registerUserToTeam(uId, tId);
            String isCoach = String.valueOf(((LinkedList) requestParams.getFirst("isCoach")).get(0));
            if("true".equals(isCoach)) {
                UserRoleMappingService.registerUserAsCoach(uId);
            }
            else {
                UserRoleMappingService.registerUserAsPlayer(uId);
            }
            gData.setKey("success");
            gData.setValue(String.valueOf(true));
            generic.addGenericData(gData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.generic.Generic> wrapper = new JAXBWrapper<>();
        if (error) {
            generic.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @POST
    @Path("{userId}/granted-by/{coachId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response grantUserCoachRole(@PathParam("userId") String userId, @PathParam("coachId") String coachId,
            @QueryParam("responseType") String responseType, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Status status = Status.OK;
        com.cv.bindings.ws.model.gen.generic.ObjectFactory of = new com.cv.bindings.ws.model.gen.generic.ObjectFactory();
        com.cv.bindings.ws.model.gen.generic.Genericdata gData = of.createGenericdata();
        com.cv.bindings.ws.model.gen.generic.Generic generic = of.createGeneric();
        long uId = -1;
        long cId = -1;
        if (userId != null && !"".equals(userId)) {
            uId = Long.parseLong(userId);
        }
        if (coachId != null && !"".equals(coachId)) {
            cId = Long.parseLong(coachId);
        }

        UserData userData = UserService.getUserDetails(uId);
        if (userData == null) {
            error = true;
            errorMesg = "User " + userId + " does not exist";
            status = Status.PRECONDITION_FAILED;
        }
        if (!error && userData != null) {
            try {
                UserRoleMappingService.grantUserCoachRole(uId, cId);
            } catch (InvalidRequestException ex) {
                Logger.getLogger(UserResource.class.getName()).log(Level.SEVERE, null, ex);
                error = true;
                errorMesg = "Exception occurred in processing the request. Please check the logs";
                status = Status.INTERNAL_SERVER_ERROR;
            }
        }
        
        gData.setKey("success");
        gData.setValue(String.valueOf(!error));
        generic.addGenericData(gData);
        
        JAXBWrapper<com.cv.bindings.ws.model.gen.generic.Generic> wrapper = new JAXBWrapper<>();
        if (error) {
            generic.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(generic);
            return Response.status(Status.OK).entity(response).build();
        }
    }
}
