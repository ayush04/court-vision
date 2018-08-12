/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.mapping;

import com.cv.events.EventsService;
import com.cv.exception.InvalidRequestException;
import com.cv.utils.Constants;
import com.cv.utils.Utils;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class UserRoleMappingService {
    public static void registerUserAsCoach(long userId) {
        registerUser(userId, Constants.COACH_ROLE_ID);
    }
    
    public static void registerUserAsPlayer(long userId) {
        registerUser(userId, Constants.PLAYER_ROLE_ID);
    }
    
    public static void grantUserCoachRole(long userId, long grantedBy) throws InvalidRequestException {
        if(isUserCoach(grantedBy) && !isUserCoach(userId)) {
            UserRoleMappingData data;
            Session session = null;
            try {
                String query = "from UserRoleMappingData where userId = " + userId;
                session = Utils.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();
                List<UserRoleMappingData> dataList = session.createQuery(query).list();
                //data = (UserRoleMappingData) session.get(UserRoleMappingData.class, userId);
                if(dataList != null) {
                    data = dataList.get(0);
                    data.setRoleId(Constants.COACH_ROLE_ID);
                    session.update(data);
                }
                tx.commit();
                EventsService.logRoleChangeEvent(userId, grantedBy);
            }
            catch(HibernateException e) {
                System.err.println("Exception : " + e);
            }
            finally {
                if(session != null) {
                    session.close();
                }
            }
        }
        else {
            throw new InvalidRequestException("User " + grantedBy + " is not a coach"
                    + " or User " + userId + " is already a coach");
        }
    }
    
    private static void registerUser(long userId, long roleId) {
        UserRoleMappingData data = new UserRoleMappingData();
        data.setUserId(userId);
        data.setRoleId(roleId);
        Session session = null;
        try {
            session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(data);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    public static boolean isUserPlayer(long userId) {
        String query = "from UserRoleMappingData where userId = " + userId + 
                " and roleId = " + Constants.PLAYER_ROLE_ID;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<UserRoleMappingData> tempData = session.createQuery(query).list();
        tx.commit();
        session.close();
        return tempData != null && tempData.size() > 0;
    }
    
    public static boolean isUserCoach(long userId) {
        String query = "from UserRoleMappingData where userId = " + userId
                + " and roleId = " + Constants.COACH_ROLE_ID;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<UserRoleMappingData> tempData = session.createQuery(query).list();
        tx.commit();
        session.close();
        return tempData != null && tempData.size() > 0;
    }
    
    public static void main(String[] args) throws InvalidRequestException {
        //UserRoleMappingService.grantUserCoachRole(9, 4);
        //System.out.println(UserRoleMappingService.isUserCoach(9));
        //registerUserAsPlayer(1);
        System.out.println(isUserCoach(4));
    }
}
