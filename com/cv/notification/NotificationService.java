/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.notification;

import com.cv.mapping.PlayerTeamMappingData;
import com.cv.mapping.PlayerTeamMappingService;
import com.cv.user.UserData;
import com.cv.utils.Constants;
import com.cv.utils.Utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class NotificationService {
    private NotificationService() {};
    
    public static long createNotification(NotificationData data) {
        long notificationId = -1;
        Session session = null;
        data.setWhenCreated(Utils.getCurrentTimestamp());
        data.setIsRead(0);
        try {
            session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            notificationId = (long) session.save(data);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return notificationId;
    };
    
    public static NotificationData getNotification(long notificationId) {
        String query = "from NotificationData where notificationId = " + notificationId;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<NotificationData> tempData = session.createQuery(query).list();
        NotificationData nData = null;
        tx.commit();
        session.close();
        if (tempData != null && tempData.size() > 0) {
            nData = tempData.get(0);
        }
        return nData;
    };
    
    public static void markNotificationAsRead(long notificationId) {
        NotificationData notification = getNotification(notificationId);
        if(notification != null) {
            notification.setIsRead(1);
            notification.setWhenRead(Utils.getCurrentTimestamp());
            Session session = null;
            try {
                session = Utils.getSessionFactory().openSession();
                session.beginTransaction();
                session.update(notification);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                System.err.println("Exception : " + e);
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
    };
    
    public static List<NotificationData> getAllNotificationsForUser(long userId) {
        String query = "from NotificationData where createdFor = " + userId;
        Session session = null;
        List<NotificationData> notifications = null;
        try {
            session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            notifications = session.createQuery(query).list();
            tx.commit();
        }
        catch(HibernateException e) {
            System.err.println("Exception :: " + e);
        }
        finally {
            if(session != null) {
                session.close();
            }
        }
        return notifications;
    };
    
    public static List<NotificationData> getAllPendingNotificationsForUser(long userId) {
        String query = "from NotificationData where createdFor = " + userId + " and isRead = 0";
        Session session = null;
        List<NotificationData> notifications = null;
        try {
            session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            notifications = session.createQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            System.err.println("Exception :: " + e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return notifications;
    };
    
    public static List<NotificationData> getCoachRequestNotification(long[] userIds) {
        String query = "from NotificationData where notificationType = " + Constants.COACH_ACCESS_NOTIFICATION 
                + " and isRead = 0 and createdBy in " + Utils.convertArrToStringObject(userIds);
        Session session = null;
        List<NotificationData> notifications = null;
        try {
            session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            notifications = session.createQuery(query).list();
            tx.commit();
        } catch (HibernateException e) {
            System.err.println("Exception :: " + e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return notifications;
    }
    
    public static List<NotificationData> getCoachRequestNotification(long teamId) {
        List<PlayerTeamMappingData> mappingData = PlayerTeamMappingService.getAllUsersOfTeam(teamId);
        long[] userIds = new long[mappingData.size()];
        for(int i=0; i<mappingData.size(); i++) {
            userIds[i] = mappingData.get(i).getUserId();
        }
        return getCoachRequestNotification(userIds);
    }

    public static void main(String[] args) {
        //markNotificationAsRead(2);
//        NotificationData n = new NotificationData();
//        n.setContent("This is a general notification");
//        n.setCreatedBy(4);
//        n.setCreatedFor(1);
//        n.setNotificationType(Constants.GENERAL_TYPE_NOTIFICATION);
//        n.setTitle("General notification");
//        createNotification(n);
        //markNotificationAsRead(1);
        //long[] userIds = new long[]{1, 2};
        
        System.out.println(getCoachRequestNotification(1L));
    }
}
