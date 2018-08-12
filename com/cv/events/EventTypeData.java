/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.events;

import java.util.Objects;

/**
 *
 * @author ayush
 */
public class EventTypeData {
    private long eventType;
    private String eventName;

    public long getEventType() {
        return eventType;
    }

    public void setEventType(long eventType) {
        this.eventType = eventType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (int) (this.eventType ^ (this.eventType >>> 32));
        hash = 37 * hash + Objects.hashCode(this.eventName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventTypeData other = (EventTypeData) obj;
        if (this.eventType != other.eventType) {
            return false;
        }
        return Objects.equals(this.eventName, other.eventName);
    }

    @Override
    public String toString() {
        return "EventTypeData{" + "eventType=" + eventType + ", eventName=" + eventName + '}';
    }
}
