/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.user;

import com.cv.events.EventsData;
import com.cv.events.EventsService;
import com.cv.exception.InvalidGameException;
import com.cv.game.GameData;
import com.cv.game.GameService;
import com.cv.mapping.MappingService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author ayush
 */
public class PlayerService {
    public static double totalDistanceCoveredInGame(long userId, long gameId) {
        double distance = 0;
        List<EventsData> playerEvents = EventsService.getPlayerPositions(userId, gameId);
        EventsData initialEvent = playerEvents.get(0);
        for(int i=1; i< playerEvents.size(); i++) {
            distance += distance(Double.parseDouble(initialEvent.getEventValue()),
                    Double.parseDouble(initialEvent.getEventValue()), Double.parseDouble(playerEvents.get(i).getEventValue()),
                    Double.parseDouble(playerEvents.get(i).getEventValue()));
            initialEvent = playerEvents.get(i);
        }
        
        return distance;
    }
    
    public static List<Double> totalDistanceCoveredInQuarters(long userId, long gameId) {
        GameData gData = GameService.getGameDetails(gameId);
        List<EventsData> quarters = EventsService.getQuartersInGame(gameId);
        long startTime = gData.getStartTime();
        long endTime = quarters.get(0).getEventTime();
        List<Double> distanceList = new ArrayList();
        for(int j=1; j<=quarters.size(); j++) {
            List<EventsData> playerEvents = EventsService.getPlayerPositions(userId, gameId, startTime, endTime);
            EventsData initialEvent = playerEvents.get(0);
            double distance = 0;
            for (int i = 1; i < playerEvents.size(); i++) {
                distance += distance(Double.parseDouble(initialEvent.getEventValue()),
                        Double.parseDouble(initialEvent.getEventValue()), Double.parseDouble(playerEvents.get(i).getEventValue()),
                        Double.parseDouble(playerEvents.get(i).getEventValue()));
                initialEvent = playerEvents.get(i);
            }
            distanceList.add(distance);
            startTime = endTime;
            endTime = j + 1 > quarters.size() ? gData.getEndTime() : quarters.get(j).getEventTime();
        }
        return distanceList;
    }
    
    public static int totalBasketsScoredInGameByPlayer(long userId, long gameId) {
        List<EventsData> basketEvents = EventsService.getScoreEventsInGameByPlayer(userId, gameId);
        if(basketEvents != null) {
            return basketEvents.size();
        }
        return 0;
    }
    
    public static long totalBasketsScoredByPlayer(long userId) {
        return EventsService.getNumberOfBasketsByPlayer(userId);
    }
    
    public static List<UserData> getPlayersInAGame(long gameId) throws InvalidGameException {
        long[] playerIds = MappingService.getPlayersInAGame(gameId);
        if(playerIds.length > 0) {
            return UserService.getAllUsers(playerIds);
        }
        else {
            throw new InvalidGameException("No players in the game");
        }
    }
    
    public static double getAverageSpeedOfPlayerInGame(long userId, long gameId) {
        double distance = 0;
        long timeDuration = 0;
        List<EventsData> playerEvents = EventsService.getPlayerPositions(userId, gameId);
        EventsData initialEvent = playerEvents.get(0);
        for (int i = 1; i < playerEvents.size(); i++) {
            distance += distance(Double.parseDouble(initialEvent.getEventValue()),
                    Double.parseDouble(initialEvent.getEventValue()), 
                    Double.parseDouble(playerEvents.get(i).getEventValue()),
                    Double.parseDouble(playerEvents.get(i).getEventValue()));
            timeDuration += playerEvents.get(i).getEventTime() - initialEvent.getEventTime();
            initialEvent = playerEvents.get(i);
        }
        return (distance/timeDuration) * 1000;
    }
    
    public static List<Double> getAverageSpeedOfPlayerInQuarters(long userId, long gameId) {
        GameData gData = GameService.getGameDetails(gameId);
        List<EventsData> quarters = EventsService.getQuartersInGame(gameId);
        long startTime = gData.getStartTime();
        long endTime = quarters.get(0).getEventTime();
        List<Double> speedList = new ArrayList();
        for(int j=1; j<=quarters.size(); j++) {
            List<EventsData> playerEvents = EventsService.getPlayerPositions(userId, gameId, startTime, endTime);
            EventsData initialEvent = playerEvents.get(0);
            double distance = 0;
            long timeDuration = 0;
            for (int i = 1; i < playerEvents.size(); i++) {
                distance += distance(Double.parseDouble(initialEvent.getEventValue()),
                        Double.parseDouble(initialEvent.getEventValue()),
                        Double.parseDouble(playerEvents.get(i).getEventValue()),
                        Double.parseDouble(playerEvents.get(i).getEventValue()));
                timeDuration += playerEvents.get(i).getEventTime() - initialEvent.getEventTime();
                initialEvent = playerEvents.get(i);
            }
            speedList.add((distance/timeDuration) * 1000);
            startTime = endTime;
            endTime = j+1>quarters.size() ? gData.getEndTime(): quarters.get(j).getEventTime();
        }
        return speedList;
    }
    
    private static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
    }
    
    public static Map<Long, Double> getPlayerSpeedInGame(long userId, long gameId) {
        List<EventsData> playerEvents = EventsService.getPlayerPositions(userId, gameId);
        Map<Long, Double> playerSpeeds = new TreeMap<>();
        if(playerEvents != null && playerEvents.size() > 0) {
            Double initialX = Double.parseDouble(playerEvents.get(0).getEventValue());
            Double initialY = Double.parseDouble(playerEvents.get(0).getEventValue2());
            long initialTime = playerEvents.get(0).getEventTime();
            
            for(int i = 1; i<playerEvents.size(); i++) {
                Double newX = Double.parseDouble(playerEvents.get(i).getEventValue());
                Double newY = Double.parseDouble(playerEvents.get(i).getEventValue2());
                long newTime = playerEvents.get(i).getEventTime();
                Double speed = distance(newX, newY, initialX, initialY)/(newTime - initialTime);
                playerSpeeds.put(newTime, speed);
                initialX = newX;
                initialY = newY;
                initialTime = newTime;
            }
        }
        return playerSpeeds;
    }
    
    public static Map<Long, Map<Long, Double>> getPlayerSpeedInGame(long[] userIds, long gameId) {
        Map<Long, Map<Long, Double>> playerSpeeds = new TreeMap<>();
        for(int j=0; j<userIds.length; j++) {
            List<EventsData> playerEvents = EventsService.getPlayerPositions(userIds[j], gameId);
            Map<Long, Double> speedDetails = new TreeMap<>();
            if (playerEvents != null && playerEvents.size() > 0) {
                Double initialX = Double.parseDouble(playerEvents.get(0).getEventValue());
                Double initialY = Double.parseDouble(playerEvents.get(0).getEventValue2());
                long initialTime = playerEvents.get(0).getEventTime();

                for (int i = 1; i < playerEvents.size(); i++) {
                    Double newX = Double.parseDouble(playerEvents.get(i).getEventValue());
                    Double newY = Double.parseDouble(playerEvents.get(i).getEventValue2());
                    long newTime = playerEvents.get(i).getEventTime();
                    Double speed = distance(newX, newY, initialX, initialY) / (newTime - initialTime);
                    speedDetails.put(newTime, speed);
                    initialX = newX;
                    initialY = newY;
                    initialTime = newTime;
                    playerSpeeds.put(userIds[j], speedDetails);
                }
            }
        }
        return playerSpeeds;
    }
    
    public static void main(String[] args) throws InvalidGameException {
        System.out.println(getAverageSpeedOfPlayerInQuarters(1, 7));
        System.out.println(totalDistanceCoveredInGame(1, 7));
        //System.out.println(totalDistanceCoveredInGame(1, 7));
        //System.out.println(GameService.getGameDuration(7));
        //long[] arr = new long[]{1L, 3L};
        //System.out.println(getPlayerSpeedInGame(arr, 7));
    }
}
