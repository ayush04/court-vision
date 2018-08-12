/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.game;

import com.cv.events.EventsData;
import com.cv.events.EventsService;
import com.cv.exception.InvalidGameException;
import com.cv.mapping.MappingService;
import com.cv.utils.Constants;
import com.cv.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class GameService {
    public static long startGame() {
        long gameId = -1;
        try {
            GameData gData = new GameData();
            gData.setStartTime(Utils.getCurrentTimestamp());
            Session session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            gameId = (long) session.save(gData);
            session.getTransaction().commit();
            session.close();
        }
        catch(HibernateException e) {
            System.err.println("Exception : " + e);
        }
        EventsService.logStartGameEvent(gameId);
        return gameId;
    }
    
    public static void endGame(long gameId) throws InvalidGameException {
        if(gameId > 0) {
            Session session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            GameData gameData = getGameDetails(gameId);
            gameData.setEndTime(Utils.getCurrentTimestamp());
            session.update(gameData);
            EventsService.logEndGameEvent(gameId);
            tx.commit();
            session.close();
        }
        else {
            throw new InvalidGameException();
        }
    }
    
    public static GameData getGameDetails(long gameId) {
        String query = "from GameData where gameId = " + gameId;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<GameData> tempData = session.createQuery(query).list();
        GameData gameData = null;
        tx.commit();
        session.close();
        if (tempData != null && tempData.size() > 0) {
            gameData = tempData.get(0);
        }

        return gameData;
    }
    
    public static List<GameData> getAllGames() {
        String query = "from GameData";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<GameData> gameData = session.createQuery(query).list();
        tx.commit();
        session.close();
        
        return gameData;
    }
    
    public static List<GameData> getCurrentGames() {
        String query = "from GameData where endTime = 0";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<GameData> gameData = session.createQuery(query).list();
        tx.commit();
        session.close();

        return gameData;
    }
    
    public static List<GameData> getCompletedGames() {
        String query = "from GameData where endTime > 0";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<GameData> gameData = session.createQuery(query).list();
        tx.commit();
        session.close();

        return gameData;
    }
    
    public static int[] getNumberOfPasses(long gameId) {
        int numberOfPasses = 0;
        int successfulPasses = 0;
        long currentPlayerId = -1;
        Double[] previousBallCoordinates = null;
        HashMap<Long, List<Double[]>> map = new HashMap();
        long[] playerIds = MappingService.getPlayersInAGame(gameId);
        for(int i=0; i< playerIds.length; i++) {
            List<EventsData> positions = EventsService.getPlayerPositions(playerIds[i], gameId);
            map = insertPositions(positions, map);
        }
        map = insertPositions(EventsService.getBallPositions(gameId), map);
        for(Map.Entry<Long, List<Double[]>> entry: map.entrySet()) {
            List<Double[]> positionList = entry.getValue();
            Double[] ballCoordinates = positionList.get(playerIds.length);
            for(int i=0; i<playerIds.length; i++) {
                if(Objects.equals(positionList.get(i)[0], ballCoordinates[0]) 
                        && Objects.equals(positionList.get(i)[1], ballCoordinates[1])) {
                    if(currentPlayerId != -1 && currentPlayerId != i) {
                        successfulPasses++;
                    }
                    if (previousBallCoordinates != null) {
                        if (!Objects.equals(previousBallCoordinates[0], ballCoordinates[0])
                                || !Objects.equals(previousBallCoordinates[1], ballCoordinates[1])) {
                            numberOfPasses++;
                        }
                    } else {
                        previousBallCoordinates = ballCoordinates;
                    }
                    currentPlayerId = i;
                }
            }
        }
        return new int[]{numberOfPasses, successfulPasses};
    }
    
    public static HashMap<Long, Integer> getNumberOfPassesByPlayers(long gameId) {
        int numberOfPasses = 0;
        int successfulPasses = 0;
        long currentPlayerId = -1;
        HashMap<Long, List<Double[]>> map = new HashMap();
        HashMap<Long, Integer> passesByPlayer = new HashMap();
        long[] playerIds = MappingService.getPlayersInAGame(gameId);
        for(int i=0; i< playerIds.length; i++) {
            List<EventsData> positions = EventsService.getPlayerPositions(playerIds[i], gameId);
            map = insertPositions(positions, map);
        }
        map = insertPositions(EventsService.getBallPositions(gameId), map);
        for(Map.Entry<Long, List<Double[]>> entry: map.entrySet()) {
            List<Double[]> positionList = entry.getValue();
            Double[] ballCoordinates = positionList.get(playerIds.length);
            for(int i=0; i<playerIds.length; i++) {
                if(Objects.equals(positionList.get(i)[0], ballCoordinates[0]) 
                        && Objects.equals(positionList.get(i)[1], ballCoordinates[1])) {
                    if(currentPlayerId != -1 && currentPlayerId != i) {
                        successfulPasses++;
                        if(passesByPlayer.get(currentPlayerId) != null) {
                            passesByPlayer.put(currentPlayerId, passesByPlayer.get(currentPlayerId) + 1);
                        }
                        else {
                            passesByPlayer.put(currentPlayerId, 1);
                        }
                    }
                    currentPlayerId = i;
                }
            }
        }
        return passesByPlayer;
    }
    
    public static HashMap<Integer, Integer[]> getNumberOfPassesPerQuarter(long gameId) {
        long[] playerIds = MappingService.getPlayersInAGame(gameId);
        GameData gData = GameService.getGameDetails(gameId);
        List<EventsData> quarters = EventsService.getQuartersInGame(gameId);
        long startTime = gData.getStartTime();
        long endTime = quarters.get(0).getEventTime();
        HashMap<Integer, Integer[]> passes = new HashMap();
        for(int j=1; j<=quarters.size(); j++) {
            long currentPlayerId = -1;
            Double[] previousBallCoordinates = null;
            HashMap<Long, List<Double[]>> map = new HashMap();
            int numberOfPasses = 0;
            int successfulPasses = 0;
            for (int i = 0; i < playerIds.length; i++) {
                List<EventsData> positions = EventsService.getPlayerPositions(playerIds[i], gameId, startTime, endTime);
                map = insertPositions(positions, map);
            }
            map = insertPositions(EventsService.getBallPositions(gameId, startTime, endTime), map);
            for (Map.Entry<Long, List<Double[]>> entry : map.entrySet()) {
                List<Double[]> positionList = entry.getValue();
                Double[] ballCoordinates = positionList.get(playerIds.length);
                for (int i = 0; i < playerIds.length; i++) {
                    if (Objects.equals(positionList.get(i)[0], ballCoordinates[0])
                            && Objects.equals(positionList.get(i)[1], ballCoordinates[1])) {
                        if (currentPlayerId != -1 && currentPlayerId != i) {
                            successfulPasses++;
                        }
                        if (previousBallCoordinates != null) {
                            if (!Objects.equals(previousBallCoordinates[0], ballCoordinates[0])
                                    || !Objects.equals(previousBallCoordinates[1], ballCoordinates[1])) {
                                numberOfPasses++;
                            }
                        } else {
                            previousBallCoordinates = ballCoordinates;
                        }
                        currentPlayerId = i;
                    }
                }
            }
            startTime = endTime;
            endTime = j + 1 > quarters.size() ? gData.getEndTime() : quarters.get(j).getEventTime();
            passes.put(j, new Integer[]{numberOfPasses, successfulPasses});
        }
        
        return passes;
    }
    
    private static HashMap<Long, List<Double[]>> insertPositions(List<EventsData> positions, HashMap<Long, List<Double[]>> map) {
        positions.forEach((position) -> {
            if (map.containsKey(position.getEventTime())) {
                List<Double[]> list = map.get(position.getEventTime());
                list.add(new Double[]{Double.parseDouble(position.getEventValue()),
                    Double.parseDouble(position.getEventValue2())});
                map.put(position.getEventTime(), list);
            } else {
                List<Double[]> list = new ArrayList();
                list.add(new Double[]{Double.parseDouble(position.getEventValue()),
                    Double.parseDouble(position.getEventValue2())});
                map.put(position.getEventTime(), list);
            }
        });
        
        return map;
    }
    
    public static long getGameDuration(long gameId) {
        GameData gameData = GameService.getGameDetails(gameId);
        return gameData.getEndTime() - gameData.getStartTime();
    }
    
    public static long getHighestScorerInGame(long gameId) {
        HashMap<Long, Integer> map = (HashMap<Long, Integer>) getBasketsPerPlayerInGame(gameId);
        long highestScorer = Collections.max(map.entrySet(), (Entry<Long, Integer> o1, Entry<Long, Integer> o2) -> o1.getValue().compareTo(o2.getValue())).getKey();
        return highestScorer;
    }
    
    public static Map<Long, Integer> getBasketsPerPlayerInGame(long gameId) {
        List<EventsData> scoreEvents = EventsService.getScoreEvents(gameId);
        Map<Long, Integer> map = new HashMap();
        scoreEvents.forEach((event) -> {
            long userId = event.getUserId();
            if (map.containsKey(userId)) {
                map.put(userId, map.get(userId) + 1);
            } else {
                map.put(userId, 1);
            }
        });
        return map;
    }
    
    public static List<Object[]> getTop5Scorers() {
        String query = "select userId, count(*) as cnt from EventsData where eventType = " + Constants.BASKET_SCORED_EVENT_TYPE
                + " group by userId order by cnt DESC";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query q = session.createQuery(query);
        q.setFirstResult(0);
        q.setMaxResults(5);
        List<Object[]> data = q.list();
        return data;
    }

    public static List<Object[]> getTopScorer(long gameId) {
        String query = "select userId, count(*) as cnt from EventsData where eventType = " + Constants.BASKET_SCORED_EVENT_TYPE
                + " and gameId = " + gameId + " group by userId order by cnt DESC";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Query q = session.createQuery(query);
        q.setFirstResult(0);
        q.setMaxResults(1);
        List<Object[]> data = q.list();
        return data;
    }
    
    public static void main(String[] args) throws InvalidGameException {
//        HashMap<Integer, Integer[]> map = GameService.getNumberOfPassesPerQuarter(7);
//        map.entrySet().forEach((entry) -> {
//            System.out.println(entry.getKey() + " :: " + Arrays.toString(entry.getValue()));
//            System.out.println(((float)entry.getValue()[1] / entry.getValue()[0]) * 100);
//        });
//
//        HashMap<Long, Integer> passes = GameService.getNumberOfPassesByPlayers(7);
//        passes.entrySet().forEach((entry) -> {
//            System.out.println(entry.getKey() + " :: " + entry.getValue());
//        });
//        
//        System.out.println(Arrays.toString(GameService.getNumberOfPasses(7)));
//        
//        System.out.println(GameService.getGameDetails(7));

        System.out.println(GameService.getAllGames());
    }
}
