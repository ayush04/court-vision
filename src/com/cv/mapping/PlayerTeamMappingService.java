/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.mapping;

import com.cv.utils.Constants;
import com.cv.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class PlayerTeamMappingService {
    public static void registerUserToTeam(long userId, long teamId) {
        PlayerTeamMappingData data = new PlayerTeamMappingData();
        data.setTeamId(teamId);
        data.setUserId(userId);
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
    
    public static List<Long> getCoachOfTeam(long teamId) {
        String query = "from PlayerTeamMappingData ptd, UserRoleMappingData urd "
                + "where ptd.userId = urd.userId and urd.roleId = " + Constants.COACH_ROLE_ID
                + " and ptd.teamId = " + teamId;
        Session session = null;
        List<Long> ids = new ArrayList<>();
        try {
            session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            List<Object[]> coaches = session.createQuery(query).list();
            tx.commit();
            if(coaches != null && !coaches.isEmpty()) {
                coaches.forEach((Object[] coach) -> {
                    PlayerTeamMappingData data = (PlayerTeamMappingData) coach[0];
                    ids.add(data.getUserId());
                });
            }
        }
        catch(HibernateException e) {
            System.err.println("Exception : " + e);
        }
        finally {
            if(session != null) {
                session.close();
            }
        }
        
        return ids;
    }
    
    public static List<PlayerTeamMappingData> getAllUsersOfTeam(long teamId) {
        String query = "from PlayerTeamMappingData where teamId = " + teamId;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<PlayerTeamMappingData> mappingData = session.createQuery(query).list();
        tx.commit();
        session.close();
        return mappingData;
    }
    
    public static long getTeamOfUser(long userId) {
        long teamId = -1;
        String query = "from PlayerTeamMappingData where userId = " + userId;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<PlayerTeamMappingData> mappingData = session.createQuery(query).list();
        tx.commit();
        session.close();
        if(mappingData != null && mappingData.size() == 1) {
            teamId = mappingData.get(0).getTeamId();
        }
        return teamId;
    }
    
    public static void main(String[] args) {
        //System.out.println(PlayerTeamMappingService.getAllUsersOfTeam(1));
        
        System.out.println(PlayerTeamMappingService.getCoachOfTeam(2));
        //PlayerTeamMappingService.registerUserToTeam(1, 1);
    }
}