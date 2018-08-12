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
public class EventsData {
    private long eventId;
    private long gameId;
    private long userId;
    private long eventTime;
    private long eventType;
    private String eventValue;
    private String eventValue2;
    private String eventValue3;
    private String eventValue4;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public long getEventType() {
        return eventType;
    }

    public void setEventType(long eventType) {
        this.eventType = eventType;
    }

    public String getEventValue() {
        return eventValue;
    }

    public void setEventValue(String eventValue) {
        this.eventValue = eventValue;
    }

    public String getEventValue2() {
        return eventValue2;
    }

    public void setEventValue2(String eventValue2) {
        this.eventValue2 = eventValue2;
    }

    public String getEventValue3() {
        return eventValue3;
    }

    public void setEventValue3(String eventValue3) {
        this.eventValue3 = eventValue3;
    }

    public String getEventValue4() {
        return eventValue4;
    }

    public void setEventValue4(String eventValue4) {
        this.eventValue4 = eventValue4;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (this.eventId ^ (this.eventId >>> 32));
        hash = 59 * hash + (int) (this.gameId ^ (this.gameId >>> 32));
        hash = 59 * hash + (int) (this.userId ^ (this.userId >>> 32));
        hash = 59 * hash + (int) (this.eventTime ^ (this.eventTime >>> 32));
        hash = 59 * hash + (int) (this.eventType ^ (this.eventType >>> 32));
        hash = 59 * hash + Objects.hashCode(this.eventValue);
        hash = 59 * hash + Objects.hashCode(this.eventValue2);
        hash = 59 * hash + Objects.hashCode(this.eventValue3);
        hash = 59 * hash + Objects.hashCode(this.eventValue4);
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
        final EventsData other = (EventsData) obj;
        if (this.eventId != other.eventId) {
            return false;
        }
        if (this.gameId != other.gameId) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        if (this.eventTime != other.eventTime) {
            return false;
        }
        if (this.eventType != other.eventType) {
            return false;
        }
        if (!Objects.equals(this.eventValue, other.eventValue)) {
            return false;
        }
        if (!Objects.equals(this.eventValue2, other.eventValue2)) {
            return false;
        }
        if (!Objects.equals(this.eventValue3, other.eventValue3)) {
            return false;
        }
        return Objects.equals(this.eventValue4, other.eventValue4);
    }

    @Override
    public String toString() {
        return "EventsData{" + "eventId=" + eventId + ", gameId=" + gameId + 
                ", userId=" + userId + ", eventTime=" + eventTime + 
                ", eventType=" + eventType + ", eventValue=" + eventValue + 
                ", eventValue2=" + eventValue2 + ", eventValue3=" + eventValue3 + 
                ", eventValue4=" + eventValue4 + '}';
    }
}
