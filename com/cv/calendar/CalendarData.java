/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.calendar;

import java.util.Objects;

/**
 *
 * @author ayush
 */
public class CalendarData {
    private long eventId;
    private long createdBy;
    private String title;
    private long startTime;
    private long endTime;
    private String location;
    private long whenCreated;
    private int allDay;
    private String details;

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(long whenCreated) {
        this.whenCreated = whenCreated;
    }

    public int getAllDay() {
        return allDay;
    }

    public void setAllDay(int allDay) {
        this.allDay = allDay;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (int) (this.eventId ^ (this.eventId >>> 32));
        hash = 23 * hash + (int) (this.createdBy ^ (this.createdBy >>> 32));
        hash = 23 * hash + Objects.hashCode(this.title);
        hash = 23 * hash + (int) (this.startTime ^ (this.startTime >>> 32));
        hash = 23 * hash + (int) (this.endTime ^ (this.endTime >>> 32));
        hash = 23 * hash + Objects.hashCode(this.location);
        hash = 23 * hash + (int) (this.whenCreated ^ (this.whenCreated >>> 32));
        hash = 23 * hash + this.allDay;
        hash = 23 * hash + Objects.hashCode(this.details);
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
        final CalendarData other = (CalendarData) obj;
        if (this.eventId != other.eventId) {
            return false;
        }
        if (this.createdBy != other.createdBy) {
            return false;
        }
        if (this.startTime != other.startTime) {
            return false;
        }
        if (this.endTime != other.endTime) {
            return false;
        }
        if (this.whenCreated != other.whenCreated) {
            return false;
        }
        if (this.allDay != other.allDay) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        return Objects.equals(this.details, other.details);
    }

    @Override
    public String toString() {
        return "CalendarData{" + "eventId=" + eventId + ", createdBy=" + createdBy + ", title=" + title + ", startTime=" + startTime + ", endTime=" + endTime + ", location=" + location + ", whenCreated=" + whenCreated + ", allDay=" + allDay + ", details=" + details + '}';
    }
}
