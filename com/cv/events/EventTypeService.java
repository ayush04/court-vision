/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.events;

import com.cv.utils.Utils;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

/**
 *
 * @author ayush
 */
public class EventTypeService {
    public static void createEventType(EventTypeData eventType) {
        try {
            Session session = Utils.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(eventType);
            session.getTransaction().commit();
            session.close();
        }
        catch(HibernateException e) {
            System.err.println("Exception : " + e);
        }
    }
    
    public static List<EventTypeData> getEventTypes() {
        List<EventTypeData> eventTypes = null;
        try {
            Session session = Utils.getSessionFactory().openSession();
            eventTypes = (List<EventTypeData>) session.createQuery("from EventTypeData").list();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        }
        return eventTypes;
    }
    
    public static EventTypeData getEventType(Long id) {
        EventTypeData eventType = null;
        try {
            Session session = Utils.getSessionFactory().openSession();
            eventType = (EventTypeData) session.get(EventTypeData.class, id);
            session.close();
        } catch (HibernateException e) {
            System.err.println("Exception : " + e);
        }
        return eventType;
    }
    
    public static void main(String args[]) {
        EventTypeData data = new EventTypeData();
        data.setEventName("role_changed_to_coach");
        EventTypeService.createEventType(data);
        System.out.println(EventTypeService.getEventTypes());
        System.out.println(EventTypeService.getEventType(new Long(6)));
    }
}
