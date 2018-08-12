/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.events;

import com.cv.utils.Constants;
import com.cv.utils.Position;
import com.cv.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class EventsService {
    public static void createEvent(EventsData eventData) {
        try {
            Session session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(eventData);
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        }
    }
    
    public static void createMultipleEvents(List<EventsData> eventsDataList) {
        try {
            Session session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            for(int i=0; i< eventsDataList.size(); i++) {
                session.save(eventsDataList.get(i));
                // Flush session after 20 inserts. hibernate.jdbc.batch_size is set to 20
                if(i % 20 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        }
    }
    
    public static List<EventsData> getAllEventsOfAMatch(long gameId) {
        String query = "from EventsData where gameId = " + gameId;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }
    
    public static List<EventsData> getEventsOfAPlayer(long userId, long gameId) {
        String query = "from EventsData where gameId = " + gameId + " and userId = " + userId;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }
    
    public static List<EventsData> getPlayerPositions(long userId, long gameId) {
        String query = "from EventsData where gameId = " + gameId + 
                " and userId = " + userId + " and eventType = " + Constants.PLAYER_POSITION_EVENT_TYPE;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }
    
    public static List<EventsData> getQuartersInGame(long gameId) {
        String query = "from EventsData where gameId = " + gameId +
                " and eventType = " + Constants.QUARTER_END_EVENT_TYPE;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }
    
    public static List<EventsData> getPlayerPositions(long userId, long gameId, long startTime, long endTime) {
        String query = "from EventsData where gameId = " + gameId + " and event_time >=" + startTime + " and event_time <" + endTime
                + " and userId = " + userId + " and eventType = " + Constants.PLAYER_POSITION_EVENT_TYPE;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }
    
    public static List<EventsData> getBallPositions(long gameId) {
        String query = "from EventsData where gameId = " + gameId
                + " and eventType = " + Constants.BALL_POSITION_EVENT_TYPE;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }

    public static List<EventsData> getBallPositions(long gameId, long startTime, long endTime) {
        String query = "from EventsData where gameId = " + gameId + " and event_time >=" + startTime + " and event_time <" + endTime
                + " and eventType = " + Constants.BALL_POSITION_EVENT_TYPE;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }
    
    public static List<EventsData> getScoreEvents(long gameId) {
        String query = "from EventsData where gameId = " + gameId + 
                       " and eventType = " + Constants.BASKET_SCORED_EVENT_TYPE;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }
    
    public static List<EventsData> getScoreEventsInGameByPlayer(long userId, long gameId) {
        String query = "from EventsData where gameId = " + gameId
                + " and userId = " + userId
                + " and eventType = " + Constants.BASKET_SCORED_EVENT_TYPE;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }
    
    public static List<EventsData> getScoreEventsByPlayer(long userId) {
        String query = "from EventsData where userId = " + userId
                + " and eventType = " + Constants.BASKET_SCORED_EVENT_TYPE;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<EventsData> eventsDataList = session.createQuery(query).list();

        return eventsDataList;
    }
    
    public static long getNumberOfBasketsByPlayer(long userId) {
        String query = "select count(*) from EventsData where userId = " + userId
                + " and eventType = " + Constants.BASKET_SCORED_EVENT_TYPE;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        return (Long)session.createQuery(query).uniqueResult();
    }
    
    public static void logStartGameEvent(long gameId) {
        EventsData event = new EventsData();
        event.setEventTime(Utils.getCurrentTimestamp());
        event.setEventType(Constants.MATCH_START_EVENT_TYPE);
        event.setGameId(gameId);
        EventsService.createEvent(event);
    }
    
    
    
    public static void logEndGameEvent(long gameId) {
        EventsData event = new EventsData();
        event.setEventTime(Utils.getCurrentTimestamp());
        event.setEventType(Constants.MATCH_END_EVENT_TYPE);
        event.setGameId(gameId);
        EventsService.createEvent(event);
    }
    
    public static void logScoreEvent(long userId, long gameId) {
        EventsData event = new EventsData();
        event.setEventTime(Utils.getCurrentTimestamp());
        event.setEventType(Constants.BASKET_SCORED_EVENT_TYPE);
        event.setGameId(gameId);
        event.setUserId(userId);
        EventsService.createEvent(event);
    }
    
    public static void logQuarterEndEvent(long gameId) {
        EventsData event = new EventsData();
        event.setEventTime(Utils.getCurrentTimestamp());
        event.setEventType(Constants.QUARTER_END_EVENT_TYPE);
        event.setGameId(gameId);
        EventsService.createEvent(event);
    }
    
    public static void logPlayerPosition(long gameId, long userId, float x, float y) {
        EventsData event = new EventsData();
        event.setEventTime(Utils.getCurrentTimestamp());
        event.setEventType(Constants.PLAYER_POSITION_EVENT_TYPE);
        event.setUserId(userId);
        event.setGameId(gameId);
        event.setEventValue(String.valueOf(x));
        event.setEventValue2(String.valueOf(y));
        EventsService.createEvent(event);
    }
    
    public static void logPlayerPosition(long gameId, long userId, List<Position> positions) {
        List<EventsData> eData = new ArrayList<>();
        positions.forEach((p) -> {
            EventsData e = new EventsData();
            e.setEventTime(p.getTimeStamp());
            e.setUserId(userId);
            e.setGameId(gameId);
            e.setEventType(Constants.PLAYER_POSITION_EVENT_TYPE);
            e.setEventValue(String.valueOf(p.getX()));
            e.setEventValue2(String.valueOf(p.getY()));
            eData.add(e);
        });
        
        EventsService.createMultipleEvents(eData);
    }
    
    public static void logBallPosition(long gameId, float x, float y) {
        EventsData event = new EventsData();
        event.setEventTime(Utils.getCurrentTimestamp());
        event.setEventType(Constants.BALL_POSITION_EVENT_TYPE);
        event.setGameId(gameId);
        event.setEventValue(String.valueOf(x));
        event.setEventValue2(String.valueOf(y));
        EventsService.createEvent(event);
    }
    
    public static void logBallPosition(long gameId, List<Position> positions) {
        List<EventsData> eData = new ArrayList<>();
        positions.forEach((p) -> {
            EventsData e = new EventsData();
            e.setEventTime(p.getTimeStamp());
            e.setGameId(gameId);
            e.setEventType(Constants.BALL_POSITION_EVENT_TYPE);
            e.setEventValue(String.valueOf(p.getX()));
            e.setEventValue2(String.valueOf(p.getY()));
            eData.add(e);
        });
        EventsService.createMultipleEvents(eData);
    }
    
    public static void logRoleChangeEvent(long userId, long grantedBy) {
        EventsData event = new EventsData();
        event.setEventTime(Utils.getCurrentTimestamp());
        event.setEventType(Constants.USER_ROLE_CHANGED_TO_COACH);
        event.setUserId(grantedBy);
        event.setEventValue(String.valueOf(userId));
        EventsService.createEvent(event);
    }
    
    public static void main(String[] args) {
        //System.out.println(EventsService.getAllEventsOfAMatch(1));
        //System.out.println(EventsService.getEventsOfAPlayer(1, 1));
        //System.out.println(EventsService.getScoreEvents(1));
        //EventsService.logPlayerPosition(1, 1, 250, 300);
        
//        long currentTimeStamp = 1529149634443L;
//        List<Position> positions = new ArrayList<>();
//        for(int i=0; i<100; i++) {
//            Position p = new Position();
//            p.setX(Math.random() * 840);
//            p.setY(Math.random() * 450);
//            p.setTimeStamp(currentTimeStamp);
//            currentTimeStamp += 2000;
//            positions.add(p);
//        }
//        
//        EventsService.logPlayerPosition(1, 3, positions);
//          EventsService.logScoreEvent(1, 1);
//          EventsService.logScoreEvent(1, 1);
//          EventsService.logScoreEvent(3, 1);
//          EventsService.logScoreEvent(1, 1);
//          EventsService.logScoreEvent(3, 1);
//        System.out.println(EventsService.getScoreEventsInGameByPlayer(3, 6));
//        EventsService.logScoreEvent(1, 7);
//        EventsService.logScoreEvent(1, 7);
//        EventsService.logScoreEvent(3, 7);
//        EventsService.logScoreEvent(3, 7);
//        EventsService.logScoreEvent(1, 7);
//        EventsService.logScoreEvent(3, 7);
//        EventsService.logScoreEvent(1, 7);
        System.out.println(EventsService.getQuartersInGame(7));
    }
}
