/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.mapping;

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
public class MappingService {
    public static long[] getPlayersInAGame(long gameId) {
        //TODO: Fetch only userId
        String query = "from MappingData where gameId =" + gameId;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<MappingData> mappingData = session.createQuery(query).list();
        tx.commit();
        session.close();
        long[] playerIds = new long[mappingData.size()];
        for(int i=0; i<mappingData.size(); i++) {
            playerIds[i] = mappingData.get(i).getUserId();
        }
        return playerIds;
    }
    
    public static void setPlayersInGame(List<Long> playerIds, long gameId) {
        List<MappingData> mappingList = new ArrayList<>();
        playerIds.forEach((p) -> {
            MappingData mapping = new MappingData();
            mapping.setGameId(gameId);
            mapping.setUserId(p);
            mappingList.add(mapping);
        });

        try {
            Session session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            for (int i = 0; i < mappingList.size(); i++) {
                session.save(mappingList.get(i));
                // Flush session after 20 inserts. hibernate.jdbc.batch_size is set to 20
                if (i % 20 == 0) {
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
    
    public static void main(String[] args) {
        List list = new ArrayList();
        list.add(new Long(1));
        list.add(new Long(3));
        MappingService.setPlayersInGame(list, 7);
    }
}
