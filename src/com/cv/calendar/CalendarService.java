/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.calendar;

import com.cv.utils.Utils;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author ayush
 */
public class CalendarService {
    public static long createEvent(CalendarData data) {
        long eventId = -1;
        Session session = null;
        data.setWhenCreated(Utils.getCurrentTimestamp());
        try {
            session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            eventId = (long) session.save(data);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return eventId;
    }
    
    public static CalendarData getEvent(long eventId) {
        String query = "from CalendarData where eventId = " + eventId;
        Session session = Utils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<CalendarData> tempData = session.createQuery(query).list();
        CalendarData cData = null;
        tx.commit();
        session.close();
        if (tempData != null && tempData.size() > 0) {
            cData = tempData.get(0);
        }
        return cData;
    }
    
    public static List<CalendarData> getAllEvents() {
        String query = "from CalendarData";
        Session session = null;
        List<CalendarData> cData = null;
        try {
            session = Utils.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            cData = session.createQuery(query).list();
            tx.commit();
        }
        catch(HibernateException e) {
        }
        finally {
            if(session != null) {
                session.close();
            }
        }
        return cData;
    }
    
    public static void main(String[] args) {
        CalendarData data = new CalendarData();
        data.setCreatedBy(1);
        data.setDetails("Match tomorrow morning");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(2018, 7, 8, 12, 0, 0);
        data.setStartTime(cal.getTimeInMillis());
        cal.add(Calendar.HOUR_OF_DAY, 2);
        data.setEndTime(cal.getTimeInMillis());
        data.setLocation("Basketball court");
        data.setTitle("Match");
        //System.out.println(CalendarService.createEvent(data));
        System.out.println(CalendarService.getAllEvents());
    }
}
