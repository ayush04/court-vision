/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.role;

import com.cv.utils.Utils;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class RoleService {
    public static long createRole(RoleData roleData) {
        long roleId = -1;
        Session session = null;
        try {
            session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            roleId = (long) session.save(roleData);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return roleId;
    }

    ;
    
    public static RoleData getRole(long roleId) {
        String query = "from RoleData where roleId = " + roleId;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<RoleData> tempData = session.createQuery(query).list();
        RoleData rData = null;
        tx.commit();
        session.close();
        if (tempData != null && tempData.size() > 0) {
            rData = tempData.get(0);
        }
        return rData;
    }

    public static List<RoleData> getAllRoles() {
        String query = "from RoleData";
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<RoleData> roleData = session.createQuery(query).list();
        tx.commit();
        session.close();
        return roleData;
    }

    ;
    
    public static void main(String[] args) {
        System.out.println(RoleService.getRole(1));
        System.out.println(RoleService.getRole(2));
    }
}
