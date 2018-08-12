/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws.cv.team;

import com.cv.bindings.ws.model.gen.team.ObjectFactory;
import com.cv.bindings.ws.model.gen.team.Teamdata;
import com.cv.bindings.ws.model.gen.team.Userdata;
import com.cv.mapping.PlayerTeamMappingData;
import com.cv.mapping.PlayerTeamMappingService;
import com.cv.mapping.UserRoleMappingService;
import com.cv.session.SessionService;
import com.cv.team.TeamData;
import com.cv.team.TeamService;
import com.cv.user.UserData;
import com.cv.user.UserService;
import com.cv.utils.Utils;
import java.util.List;
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
@Path("team")
public class TeamResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllTeams(@QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.team.Team team = of.createTeam();

        List<TeamData> teamData = TeamService.getAllTeams();
        if (teamData == null) {
            error = true;
            errorMesg = "No teams exist";
            status = Response.Status.PRECONDITION_FAILED;
        }
        if (!error && teamData != null) {
            teamData.forEach((t) -> {
                Teamdata tData = of.createTeamdata();
                tData.setTeamId(t.getTeamId());
                tData.setTeamName(t.getTeamName());
                team.addTeam(tData);
            });
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.team.Team> wrapper = new JAXBWrapper<>();
        if (error) {
            team.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(team);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(team);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("{teamId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeamDetails(@PathParam("teamId") String teamId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.team.Team team = of.createTeam();
        long tId = -1;
        if (teamId != null && !"".equals(teamId)) {
            tId = Long.parseLong(teamId);
        }
        TeamData teamData = TeamService.getTeam(tId);
        if (teamData == null) {
            error = true;
            errorMesg = "No teams exist";
            status = Response.Status.PRECONDITION_FAILED;
        }
        if (!error && teamData != null) {
            Teamdata tData = of.createTeamdata();
            tData.setTeamId(teamData.getTeamId());
            tData.setTeamName(teamData.getTeamName());

            List<PlayerTeamMappingData> playersInTeam = PlayerTeamMappingService.getAllUsersOfTeam(teamData.getTeamId());
            if (playersInTeam != null) {
                long[] userIds = new long[playersInTeam.size()];
                for (int i = 0; i < playersInTeam.size(); i++) {
                    userIds[i] = playersInTeam.get(i).getUserId();
                }
                List<UserData> userList = UserService.getAllUsers(userIds);

                if (userList != null) {
                    userList.forEach((user) -> {
                        Userdata uData = of.createUserdata();
                        uData.setEmailAddress(user.getEmailAddress());
                        uData.setFirstName(user.getFirstName());
                        uData.setIsCoach(UserRoleMappingService.isUserCoach(user.getUserId()) ? 1 : 0);
                        uData.setLastName(user.getLastName());
                        uData.setUserId(user.getUserId());
                        uData.setUserName(user.getUserName());
                        tData.addUser(uData);
                    });
                }
            }
            team.addTeam(tData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.team.Team> wrapper = new JAXBWrapper<>();
        if (error) {
            team.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(team);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(team);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("{teamId}/coach")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeamCoachDetails(@PathParam("teamId") String teamId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        com.cv.bindings.ws.model.gen.user.ObjectFactory of = new com.cv.bindings.ws.model.gen.user.ObjectFactory();
        com.cv.bindings.ws.model.gen.user.User user = of.createUser();
        long tId = -1;
        if (teamId != null && !"".equals(teamId)) {
            tId = Long.parseLong(teamId);
        }
        List<Long> coachIds = PlayerTeamMappingService.getCoachOfTeam(tId);
        if(coachIds != null && coachIds.size() > 0) {
            long[] ids = Utils.convertListToLongArray(coachIds);
            List<UserData> userData = UserService.getAllUsers(ids);
            userData.forEach((u) -> {
                com.cv.bindings.ws.model.gen.user.Userdata uData = of.createUserdata();
                uData.setEmailAddress(u.getEmailAddress());
                uData.setFirstName(u.getFirstName());
                uData.setIsPlayer(0);
                uData.setLastName(u.getLastName());
                uData.setUserId(u.getUserId());
                uData.setUserName(u.getUserName());
                user.addUser(uData);
            });
        }
        
        JAXBWrapper<com.cv.bindings.ws.model.gen.user.User> wrapper = new JAXBWrapper<>();
        if (error) {
            user.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(user);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(user);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
    @GET
    @Path("users/{teamId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllMembersOfTeam(@PathParam("teamId") String teamId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        com.cv.bindings.ws.model.gen.user.ObjectFactory of = new com.cv.bindings.ws.model.gen.user.ObjectFactory();
        com.cv.bindings.ws.model.gen.user.User user = of.createUser();
        long tId = -1;
        if (teamId != null && !"".equals(teamId)) {
            tId = Long.parseLong(teamId);
        }
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Response.Status.UNAUTHORIZED;
        }
        else {
            List<PlayerTeamMappingData> allUsers = PlayerTeamMappingService.getAllUsersOfTeam(tId);
            List<UserData> userData = null;
            if (allUsers.size() > 0) {
                long[] userIds = new long[allUsers.size()];
                for (int i = 0; i < allUsers.size(); i++) {
                    userIds[i] = allUsers.get(i).getUserId();
                }
                userData = UserService.getAllUsers(userIds);
            }

            if (userData != null && userData.size() > 0) {
                userData.forEach((u) -> {
                    com.cv.bindings.ws.model.gen.user.Userdata uData = of.createUserdata();
                    uData.setEmailAddress(u.getEmailAddress());
                    uData.setFirstName(u.getFirstName());
                    uData.setIsPlayer(u.getIsPlayer());
                    uData.setLastName(u.getLastName());
                    uData.setUserId(u.getUserId());
                    uData.setUserName(u.getUserName());
                    user.addUser(uData);
                });
            }
        }
         
        JAXBWrapper<com.cv.bindings.ws.model.gen.user.User> wrapper = new JAXBWrapper<>();
        if (error) {
            user.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(user);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(user);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("myteam")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyTeam(@QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Response.Status status = Response.Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.team.Team team = of.createTeam();

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Response.Status.UNAUTHORIZED;
        }
        else {
            long userId = SessionService.getUserIdFromSession(sessionId);
            long teamId = PlayerTeamMappingService.getTeamOfUser(userId);
            TeamData teamData = TeamService.getTeam(teamId);
            if (teamData == null) {
                error = true;
                errorMesg = "No teams exist";
                status = Response.Status.PRECONDITION_FAILED;
            }
            if (!error && teamData != null) {
                Teamdata tData = of.createTeamdata();
                tData.setTeamId(teamData.getTeamId());
                tData.setTeamName(teamData.getTeamName());
                team.addTeam(tData);
            }
        }
        
        JAXBWrapper<com.cv.bindings.ws.model.gen.team.Team> wrapper = new JAXBWrapper<>();
        if (error) {
            team.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(team);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(team);
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }
}
