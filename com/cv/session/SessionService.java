/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.session;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.cv.utils.Utils;
import com.cv.exception.UserAlreadyLoggedInException;
import com.cv.exception.InvalidSessionException;
import com.cv.exception.SessionAlreadyExistException;

/**
 *
 * @author ayush
 */
public class SessionService {
    private static final HashSet<String> VALID_SESSIONS = new HashSet<String>();

    public static String createUserSession(long userId, HttpSession session, boolean isForcedLogin)
            throws UserAlreadyLoggedInException, SessionAlreadyExistException {
        String query = "from SessionData where userId = " + userId + " and isValid = 1";
        Session hibernateSession = Utils.getSessionFactory().openSession();
        hibernateSession.beginTransaction();
        List<SessionData> tempData = hibernateSession.createQuery(query).list();
        String userSession = "";

        String sessionQuery = "from SessionData where httpSessionId = '" + session.getId() + "' and isValid = 1";
        List<SessionData> sData = hibernateSession.createQuery(sessionQuery).list();

        if (sData != null && sData.size() > 0) {
            throw new SessionAlreadyExistException();
        }

        if (tempData != null && tempData.size() > 0) {
            if (isForcedLogin) {
                invalidateAllSessionsOfUser(userId);
                SessionData sessionData = new SessionData(userId, session.getId());

                hibernateSession.persist(sessionData);
                hibernateSession.getTransaction().commit();
                userSession = sessionData.getSessionId();
            } else {
                throw new UserAlreadyLoggedInException();
            }
        } else {
            System.out.println("Getting into sessionData");
            SessionData sessionData = new SessionData(userId, session.getId());

            hibernateSession.persist(sessionData);
            hibernateSession.getTransaction().commit();
            userSession = sessionData.getSessionId();
        }
        VALID_SESSIONS.add(userSession);
        return userSession;
    }

    public static void invalidateAllSessionsOfUser(long userId) {
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Criteria crit = session.createCriteria(SessionData.class);
            crit.add(Restrictions.eq("userid", userId));
            crit.add(Restrictions.eq("isdeleted", 0));
            ScrollableResults items = crit.scroll();
            int count = 0;
            while (items.next()) {
                SessionData sessionData = (SessionData) items.get(count);
                sessionData.setIsValid(0);
                sessionData.setSessionEndTime(Utils.getCurrentTimestamp());
                if (++count % 100 == 0) {
                    session.flush();
                    session.clear();
                }
            }
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Exception :: " + ex);
        } finally {
            session.close();
        }
    }

    public static void invalidateSession(String sessionId) throws InvalidSessionException {
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            String query = "from SessionData where session_id = '" + sessionId + "' and is_valid = 1";
            tx = session.beginTransaction();
            SessionData sessionData = (SessionData) session.createQuery(query).list().get(0);
            String sessionQuery = "update SessionData set is_valid = :isSessionValid, session_end_time = :sessionEndTime "
                    + "where userId = :userId and sessionId = :sessionId";
            session.createQuery(sessionQuery)
                    .setInteger("isSessionValid", 0)
                    .setLong("sessionEndTime", Utils.getCurrentTimestamp())
                    .setLong("userId", sessionData.getUserId())
                    .setString("sessionId", sessionId).executeUpdate();

            long userId = getUserIdFromSession(sessionId);
            String userQuery = "update UserData set is_loggedin = :isLoggedIn where user_id = :userId";
            session.createQuery(userQuery).setInteger("isLoggedIn", 0).setLong("userId", userId).executeUpdate();
            tx.commit();
        } catch (HibernateException ex) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Exception :: " + ex);
        } finally {
            session.close();
        }
    }

    public static List<SessionData> getAllSessionsOfUser(long userId) {
        String query = "from SessionData where userid = " + userId + " and issessionvalid = 1";
        Session hibernateSession = Utils.getSessionFactory().openSession();
        List<SessionData> tempData = hibernateSession.createQuery(query).list();
        hibernateSession.close();
        if (tempData != null && tempData.size() > 0) {
            return tempData;
        } else {
            return null;
        }
    }

    public static SessionData getSessionDetails(String sessionId) {
        String query = "from SessionData where sessionid = '" + sessionId + "'";
        Session hibernateSession = Utils.getSessionFactory().openSession();
        List<SessionData> tempData = hibernateSession.createQuery(query).list();
        hibernateSession.close();
        if (tempData != null && tempData.size() > 0) {
            return tempData.get(0);
        } else {
            return null;
        }
    }

    public static HashSet<String> getValidSessions() {
        if (VALID_SESSIONS.isEmpty()) {
            populateSessionsFromDB();
        }
        return VALID_SESSIONS;
    }

    public static boolean isSessionValid(String userSessionId, long userId) {
        if (VALID_SESSIONS.isEmpty()) {
            populateSessionsFromDB();
        }
        return VALID_SESSIONS.contains(userSessionId) && userId == getUserIdFromSession(userSessionId);
    }

    public static boolean isSessionValid(String userSessionId) {
        if (VALID_SESSIONS.isEmpty()) {
            populateSessionsFromDB();
        }
        return VALID_SESSIONS.contains(userSessionId);
    }

    public static long getUserIdFromSession(String userSessionId) {
        String userIdStr = userSessionId.substring(userSessionId.indexOf(SessionData.SESSION_SEP) + 1,
                userSessionId.lastIndexOf(SessionData.SESSION_SEP));

        if (Utils.isNumber(userIdStr)) {
            return Long.parseLong(userIdStr);
        } else {
            return -1l;
        }
    }

    private static void populateSessionsFromDB() {
        String query = "from SessionData where isValid = 1";
        Session hibernateSession = Utils.getSessionFactory().openSession();
        List<SessionData> tempData = hibernateSession.createQuery(query).list();
        hibernateSession.close();

        if (tempData != null && tempData.size() > 0) {
            tempData.forEach((session) -> {
                VALID_SESSIONS.add(session.getSessionId());
            });
        }
    }

    public static void main(String args[]) {
        try {
            SessionService.invalidateSession("5b2ffd5470116ee8050414d21134$9$1511387234297");
        } catch (InvalidSessionException ex) {
            Logger.getLogger(SessionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
