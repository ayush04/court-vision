/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.user;

import com.cv.utils.Utils;
import com.cv.utils.PasswordManager;
import com.cv.exception.*;
import com.cv.mapping.MappingData;
import com.cv.session.SessionService;


import java.util.List;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class UserService {
    private UserService() {
    }

    public static UserData getUserById(Long id) {
        UserData user = null;
        try {
            Session session = Utils.getSessionFactory().openSession();
            user = (UserData) session.get(UserData.class, id);
            session.close();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        }
        return user;
    }

    public static List<UserData> getUsers() {
        List<UserData> users = null;
        try {
            Session session = Utils.getSessionFactory().openSession();
            users = (List<UserData>) session.createQuery("from UserData").list();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        }
        return users;
    }

    public static List<UserData> getAllUsers(long[] userIds) {
        String query = "from UserData where isDeleted = 0 and userId in " + Utils.convertArrToStringObject(userIds);
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<UserData> users = session.createQuery(query).list();
        tx.commit();
        session.close();

        return users;
    }

    /*public static void save(UserData user) {
        //UserProfile profile = new UserProfile();
        //save(user, profile);
        save(user);
    }*/

    public static long createUser(UserData user) {
        long userId = -1;
        try {
            user.setPassword(PasswordManager.oneWayEncrypt(user.getPassword()));
            Session session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            userId = (long) session.save(user);
            session.getTransaction().commit();
            session.close();
        } catch (HibernateException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            System.err.println("Exception : " + e);
        }
        return userId;
    }

    public static UserData getUserDetails(long userId) {
        String query = "from UserData where id = " + userId + " and is_deleted = 0";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<UserData> tempData = session.createQuery(query).list();
        UserData uData = null;
        tx.commit();
        session.close();
        if (tempData != null && tempData.size() > 0) {
            uData = tempData.get(0);
        }

        return uData;
    }
    
//    public static UserProfile getUserProfile(long userId) {
//        String query = "from UserProfile where userId = " + userId + " and isdeleted = 0";
//        Session session = Utils.getSessionFactory().openSession();
//        Transaction tx = session.beginTransaction();
//        List<UserProfile> tempData = session.createQuery(query).list();
//        UserProfile profileData = null;
//        tx.commit();
//        session.close();
//        if (tempData != null && tempData.size() > 0) {
//            profileData = tempData.get(0);
//        }
//
//        if (tempData != null && tempData.size() > 0) {
//            profileData = tempData.get(0);
//        }
//
//        return profileData;
//    }

    public static String login(String username, String password, HttpSession httpSession, boolean forceLogin)
            throws NoUserExistException, InvalidCredentialsException, UserAlreadyLoggedInException, SessionAlreadyExistException {
        String query = "from UserData where username = '" + username + "' and is_deleted = 0";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<UserData> tempData = session.createQuery(query).list();
        String sessionId = "";

        if (tempData != null && tempData.size() > 0) {
            UserData uData = (UserData) tempData.get(0);
            if (uData.getIsLoggedIn() == 1) {
                if (forceLogin) {
                    SessionService.invalidateAllSessionsOfUser(uData.getUserId());
                } else {
                    throw new UserAlreadyLoggedInException();
                }
            }
            String passwordFromDB = uData.getPassword();
            String encryptedPassword = "";
            try {
                encryptedPassword = PasswordManager.oneWayEncrypt(password);
            } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                System.err.println("Exception : " + e);
            }

            if (passwordFromDB.equals(encryptedPassword)) {
                uData.setIsLoggedIn(1);
                session.update(uData);
                tx.commit();

                sessionId = SessionService.createUserSession(uData.getUserId(), httpSession, true);

            } else {
                tx.commit();
                throw new InvalidCredentialsException();
            }
        } else {
            tx.commit();
            throw new NoUserExistException();
        }

        return sessionId;
    }

    public static void logout(String sessionId) throws InvalidSessionException {
        SessionService.invalidateSession(sessionId);
    }

    public static void registerToGame(long userId, long gameId) {
        MappingData mapping = new MappingData();
        mapping.setGameId(gameId);
        mapping.setUserId(userId);
        try {
            Session session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(mapping);
            session.getTransaction().commit();
            session.close();
        }
        catch(HibernateException e) {
            System.err.println(e);
        }
    };
    
    public static void updatePassword(long userId, String newPassword) {
        UserData uData = UserService.getUserById(userId);
        try {
            uData.setPassword(PasswordManager.oneWayEncrypt(newPassword));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        uData.setWhenModified(Utils.getCurrentTimestamp());
        session.update(uData);
        tx.commit();
        session.close();
    };

    public static void main(String args[]) {
        UserData data = new UserData();
        data.setFirstName("coach");
        data.setUserName("coach");
        data.setEmailAddress("coach@team.com");
        data.setIsPlayer(0);
        data.setPassword("coach");
        UserService.createUser(data);
    }
}
