/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ws.cv.game;

import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response;

import javax.servlet.http.HttpServletRequest;

import com.cv.session.SessionService;
import com.cv.game.GameData;
import com.cv.game.GameService;
import com.cv.exception.*;

import com.cv.bindings.ws.model.gen.game.ObjectFactory;
import com.cv.bindings.ws.model.gen.game.Gamedata;
import com.cv.bindings.ws.model.gen.user.Userdata;
import com.cv.events.EventsService;
import com.cv.user.PlayerService;
import com.cv.user.UserData;
import com.cv.utils.Constants;
import com.cv.utils.PubNubListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ws.cv.utils.JAXBWrapper;

/**
 *
 * @author ayush
 */
@Path("game")
public class GameResource {
    private GameResource() {
    }
    
    @GET
    @Path("{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameDetails(@PathParam("gameId") String gameId, 
            @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        GameData gameData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        Gamedata gData = of.createGamedata();
        com.cv.bindings.ws.model.gen.game.Game game = of.createGame();
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
            gameData = GameService.getGameDetails(gId);
            if (gameData == null) {
                error = true;
                errorMesg = "Game " + gameData + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && gameData != null) {
            gData.setGameId(gId);
            gData.setEndTime(gameData.getEndTime());
            gData.setStartTime(gameData.getStartTime());
            gData.setTeam1(gameData.getTeam1());
            gData.setTeam2(gameData.getTeam2());
            game.addGame(gData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.game.Game> wrapper = new JAXBWrapper<>();
        if (error) {
            game.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("current")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentGames(@QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        List<GameData> gameData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.game.Game game = of.createGame();
        
        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            gameData = GameService.getCurrentGames();
            if (gameData == null) {
                error = true;
                errorMesg = "Game " + gameData + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && gameData != null) {
            gameData.forEach((g) -> {
                Gamedata gData = of.createGamedata();
                gData.setGameId(g.getGameId());
                gData.setEndTime(g.getEndTime());
                gData.setStartTime(g.getStartTime());
                gData.setTeam1(g.getTeam1());
                gData.setTeam2(g.getTeam2());
                game.addGame(gData);
            });
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.game.Game> wrapper = new JAXBWrapper<>();
        if (error) {
            game.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGames(@QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        List<GameData> gameData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.game.Game game = of.createGame();

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            gameData = GameService.getAllGames();
            if (gameData == null) {
                error = true;
                errorMesg = "No games exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && gameData != null) {
            gameData.forEach((g) -> {
                Gamedata gData = of.createGamedata();
                gData.setGameId(g.getGameId());
                gData.setEndTime(g.getEndTime());
                gData.setStartTime(g.getStartTime());
                gData.setTeam1(g.getTeam1());
                gData.setTeam2(g.getTeam2());
                game.addGame(gData);
            });
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.game.Game> wrapper = new JAXBWrapper<>();
        if (error) {
            game.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response startGame(@QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        long gameId = -1;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        Gamedata gData = of.createGamedata();
        com.cv.bindings.ws.model.gen.game.Game game = of.createGame();

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            gameId = GameService.startGame();
            if(!"".equals(Constants.PUBNUB_PUBLISH_KEY) && !"".equals(Constants.PUBNUB_SUBSCRIBE_KEY)) {
                PubNubListener.initialize();
            }
            if (gameId == -1) {
                error = true;
                errorMesg = "OOPS! Something bad happened!";
                status = Status.INTERNAL_SERVER_ERROR;
            }
            else {
                EventsService.logStartGameEvent(gameId);
            }
        }
        if (!error) {
            GameData data = GameService.getGameDetails(gameId);
            gData.setGameId(gameId);
            gData.setStartTime(data.getStartTime());
            gData.setEndTime(data.getEndTime());
            gData.setTeam1(data.getTeam1());
            gData.setTeam2(data.getTeam2());
            game.addGame(gData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.game.Game> wrapper = new JAXBWrapper<>();
        if (error) {
            game.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @POST
    @Path("{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response endGame(@PathParam("gameId") String gameId, 
            @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        Gamedata gData = of.createGamedata();
        com.cv.bindings.ws.model.gen.game.Game game = of.createGame();
        
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
            try {
                GameService.endGame(gId);
                if (!"".equals(Constants.PUBNUB_PUBLISH_KEY) && !"".equals(Constants.PUBNUB_SUBSCRIBE_KEY)) {
                    PubNubListener.disconnect();
                }
            } catch (InvalidGameException ex) {
                error = true;
                errorMesg = "Invalid Game Data";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error) {
            EventsService.logEndGameEvent(gId);
            GameData data = GameService.getGameDetails(gId);
            gData.setGameId(gId);
            gData.setStartTime(data.getStartTime());
            gData.setEndTime(data.getEndTime());
            gData.setTeam1(data.getTeam1());
            gData.setTeam2(data.getTeam2());
            game.addGame(gData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.game.Game> wrapper = new JAXBWrapper<>();
        if (error) {
            game.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("{gameId}/players")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPlayersInGame(@PathParam("gameId") String gameId, 
            @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        List<UserData> userData = null;
        Status status = Status.OK;
        com.cv.bindings.ws.model.gen.user.ObjectFactory of = new com.cv.bindings.ws.model.gen.user.ObjectFactory();
        com.cv.bindings.ws.model.gen.user.User user = of.createUser();

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
            try {
                userData = PlayerService.getPlayersInAGame(gId);
            } catch (InvalidGameException ex) {
                error = true;
                errorMesg = "No players in the game " + gameId;
                status = Status.PRECONDITION_FAILED;
            }
            if (userData == null) {
                error = true;
                errorMesg = "No players in the game " + gameId;
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && userData != null) {
            userData.forEach((u) -> {
                Userdata uData = of.createUserdata();
                uData.setFirstName(u.getFirstName());
                uData.setEmailAddress(u.getEmailAddress());
                uData.setLastName(u.getLastName());
                uData.setUserId(u.getUserId());
                uData.setUserName(u.getUserName());
                uData.setIsLoggedIn(u.getIsLoggedIn());
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
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("completed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompletedGames(@QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        List<GameData> gameData = null;
        Status status = Status.OK;
        ObjectFactory of = new ObjectFactory();
        com.cv.bindings.ws.model.gen.game.Game game = of.createGame();

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            gameData = GameService.getCompletedGames();
            if (gameData == null) {
                error = true;
                errorMesg = "Game " + gameData + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && gameData != null) {
            gameData.forEach((g) -> {
                Gamedata gData = of.createGamedata();
                gData.setGameId(g.getGameId());
                gData.setEndTime(g.getEndTime());
                gData.setStartTime(g.getStartTime());
                gData.setTeam1(g.getTeam1());
                gData.setTeam2(g.getTeam2());
                game.addGame(gData);
            });
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.game.Game> wrapper = new JAXBWrapper<>();
        if (error) {
            game.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("{gameId}/player/{userId}/speed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerSpeedInGame(@PathParam("gameId") String gameId,
            @PathParam("userId") String userId, @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Map<Long, Double> playerSpeeds = null;
        Status status = Status.OK;
        com.cv.bindings.ws.model.gen.generic.ObjectFactory of = new com.cv.bindings.ws.model.gen.generic.ObjectFactory();
        com.cv.bindings.ws.model.gen.generic.Generic generic = of.createGeneric();

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            long uId = -1;
            long gId = -1;
            if (gameId != null && !"".equals(gameId)) {
                gId = Long.parseLong(gameId);
            }
            if (userId != null && !"".equals(userId)) {
                uId = Long.parseLong(userId);
            }
            playerSpeeds = PlayerService.getPlayerSpeedInGame(uId, gId);
        }
        if (!error && playerSpeeds != null) {
            for(Map.Entry<Long, Double> entry : playerSpeeds.entrySet()) {
                com.cv.bindings.ws.model.gen.generic.Genericdata genericData = of.createGenericdata();
                genericData.setKey(String.valueOf(entry.getKey()));
                genericData.setValue(String.valueOf(entry.getValue()));
                generic.addGenericData(genericData);
            }            
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
    
    @GET
    @Path("{gameId}/player/speed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayerSpeedInGame(@PathParam("gameId") String gameId,
            @QueryParam("responseType") String responseType, @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        Map<Long, Map<Long, Double>> playerSpeeds = null;
        Status status = Status.OK;
        com.cv.bindings.ws.model.gen.generic.ObjectFactory of = new com.cv.bindings.ws.model.gen.generic.ObjectFactory();
        com.cv.bindings.ws.model.gen.generic.Generic generic = of.createGeneric();

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            long gId = -1;
            if (gameId != null && !"".equals(gameId)) {
                gId = Long.parseLong(gameId);
            }
            long[] playerIds = null;
            try {
                List<UserData> playerData = PlayerService.getPlayersInAGame(gId);
                if(playerData != null) {
                    playerIds = new long[playerData.size()];
                    for(int i=0; i<playerData.size(); i++) {
                        playerIds[i] = playerData.get(i).getUserId();
                    }
                }
            } catch (InvalidGameException ex) {
                Logger.getLogger(GameResource.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(playerIds != null) {
                playerSpeeds = PlayerService.getPlayerSpeedInGame(playerIds, gId);
            }
        }
        if (!error && playerSpeeds != null) {
            for (Map.Entry<Long, Map<Long, Double>> entry : playerSpeeds.entrySet()) {
                com.cv.bindings.ws.model.gen.generic.Genericdata genericData = of.createGenericdata();
                genericData.setKey(String.valueOf(entry.getKey()));
                genericData.setValue(String.valueOf(entry.getValue()));
                generic.addGenericData(genericData);
            }
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
    
    @GET
    @Path("stats/{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGameStats(@PathParam("gameId") String gameId,
            @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        GameData gameData = null;
        Status status = Status.OK;
        com.cv.bindings.ws.model.gen.gamestats.ObjectFactory of = new com.cv.bindings.ws.model.gen.gamestats.ObjectFactory();
        com.cv.bindings.ws.model.gen.gamestats.Gamestatsdata gData = of.createGamestatsdata();
        com.cv.bindings.ws.model.gen.gamestats.Gamestats game = of.createGamestats();
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
            gameData = GameService.getGameDetails(gId);
            if (gameData == null) {
                error = true;
                errorMesg = "Game " + gameData + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && gameData != null) {
            gData.setGameId(gId);
            gData.setEndTime(gameData.getEndTime());
            gData.setStartTime(gameData.getStartTime());
            gData.setTopScorer(GameService.getHighestScorerInGame(gId));
            int[] passes = GameService.getNumberOfPasses(gId);
            gData.setNumberOfPasses(passes[0]);
            gData.setSuccessfulPasses(passes[1]);
            Map<Long, Integer> basketsPerPlayer = GameService.getBasketsPerPlayerInGame(gId);
            basketsPerPlayer.entrySet().stream().map((entry) -> {
                com.cv.bindings.ws.model.gen.gamestats.Gamescorersdata scorerData = of.createGamescorersdata();
                scorerData.setPlayerId(entry.getKey());
                scorerData.setScore(entry.getValue());
                return scorerData;
            }).forEachOrdered((scorerData) -> {
                gData.addGameScorer(scorerData);
            });
            Map<Long, Integer> passesPerPlayer = GameService.getNumberOfPassesByPlayers(gId);
            passesPerPlayer.entrySet().stream().map((entry) -> {
                com.cv.bindings.ws.model.gen.gamestats.Gamepassesdata passesData = of.createGamepassesdata();
                passesData.setPlayerId(entry.getKey());
                passesData.setPasses(entry.getValue());
                return passesData;
            }).forEachOrdered((passesData) -> {
                gData.addGamePasses(passesData);
            });
            game.addGameStats(gData);
        }

        JAXBWrapper<com.cv.bindings.ws.model.gen.gamestats.Gamestats> wrapper = new JAXBWrapper<>();
        if (error) {
            game.setErrorMesg(errorMesg);
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(status).entity(response).build();
        } else {
            String response = wrapper.jaxbObjectToJSON(game);
            return Response.status(Status.OK).entity(response).build();
        }
    }
    
    @GET
    @Path("quarter/stats/{gameId}/player/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuarterStats(@PathParam("gameId") String gameId, @PathParam("userId") String userId,
            @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        GameData gameData = null;
        Status status = Status.OK;
        com.cv.bindings.ws.model.gen.generic.ObjectFactory of = new com.cv.bindings.ws.model.gen.generic.ObjectFactory();
        com.cv.bindings.ws.model.gen.generic.Generic generic = of.createGeneric();
        long gId = -1;
        long uId = -1;
        if (gameId != null && !"".equals(gameId)) {
            gId = Long.parseLong(gameId);
        }
        if(userId != null && !"".equals(userId)) {
            uId = Long.parseLong(userId);
        }

        String sessionId = request.getHeader("sessionId");
        if (sessionId == null || "".equals(sessionId) || !SessionService.isSessionValid(sessionId)) {
            error = true;
            errorMesg = "Invalid session, please login again";
            status = Status.UNAUTHORIZED;
        } else {
            gameData = GameService.getGameDetails(gId);
            if (gameData == null) {
                error = true;
                errorMesg = "Game " + gameData + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && gameData != null) {
            List<Double> speeds = PlayerService.getAverageSpeedOfPlayerInQuarters(uId, gId);
            for(int i=0; i<speeds.size(); i++) {
                com.cv.bindings.ws.model.gen.generic.Genericdata gData = of.createGenericdata();
                gData.setKey("speed-quarter-" + i);
                gData.setValue(String.valueOf(speeds.get(i)));
                generic.addGenericData(gData);
            }
            List<Double> distance = PlayerService.totalDistanceCoveredInQuarters(uId, gId);
            for (int i = 0; i < distance.size(); i++) {
                com.cv.bindings.ws.model.gen.generic.Genericdata gData = of.createGenericdata();
                gData.setKey("distance-quarter-" + i);
                gData.setValue(String.valueOf(distance.get(i)));
                generic.addGenericData(gData);
            }
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
    
    @GET
    @Path("quarter/stats/{gameId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getQuarterGameStats(@PathParam("gameId") String gameId,
            @QueryParam("responseType") String responseType,
            @Context HttpServletRequest request) {
        boolean error = false;
        String errorMesg = "";
        GameData gameData = null;
        Status status = Status.OK;
        com.cv.bindings.ws.model.gen.generic.ObjectFactory of = new com.cv.bindings.ws.model.gen.generic.ObjectFactory();
        com.cv.bindings.ws.model.gen.generic.Generic generic = of.createGeneric();
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
            gameData = GameService.getGameDetails(gId);
            if (gameData == null) {
                error = true;
                errorMesg = "Game " + gameData + " does not exist";
                status = Status.PRECONDITION_FAILED;
            }
        }
        if (!error && gameData != null) {
            HashMap<Integer, Integer[]> passes = GameService.getNumberOfPassesPerQuarter(gId);
            passes.entrySet().forEach((entry) -> {
                com.cv.bindings.ws.model.gen.generic.Genericdata gData = of.createGenericdata();
                gData.setKey("passes-quarter-" + entry.getKey());
                gData.setValue(Arrays.toString(entry.getValue()));
                generic.addGenericData(gData);
            });
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
}
