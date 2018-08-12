/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.team;

import com.cv.utils.Utils;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class TeamService {
    public static long createTeam(TeamData teamData) {
        long teamId = -1;
        Session session = null;
        teamData.setIsDeleted(0);
        teamData.setWhenCreated(Utils.getCurrentTimestamp());
        try {
            session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            teamId = (long) session.save(teamData);
            session.getTransaction().commit();
        }
        catch (HibernateException e) {
            System.err.println("Exception : " + e);
        }
        finally {
            if(session != null) {
                session.close();
            }
        }
        return teamId;
    };
    
    public static TeamData getTeam(long teamId) {
        String query = "from TeamData where teamId = " + teamId + " and isDeleted = 0";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<TeamData> tempData = session.createQuery(query).list();
        TeamData tData = null;
        tx.commit();
        session.close();
        if (tempData != null && tempData.size() > 0) {
            tData = tempData.get(0);
        }
        return tData;
    }
    
    public static List<TeamData> getAllTeams() {
        String query = "from TeamData where isDeleted = 0";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<TeamData> teamData = session.createQuery(query).list();
        tx.commit();
        session.close();
        return teamData;
    };
    
    public static void main(String[] args) {
        TeamData data = new TeamData();
        data.setTeamName("Oxford Eagles");
        TeamService.createTeam(data);
        System.out.println(TeamService.getAllTeams());
    }
}