/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.session;

import java.util.Date;

/**
 *
 * @author ayush
 */
public class SessionData {
    private long userId;
    private String sessionId;
    private String httpSessionId;
    private long sessionStartTime;
    private long sessionEndTime;
    private int isValid;
    public static final String SESSION_SEP = "$";

    public SessionData() {
        super();
    }

    public SessionData(long userId, String httpSessionId) {
        this.userId = userId;
        this.httpSessionId = httpSessionId;
        Date date = new Date();
        this.sessionStartTime = date.getTime();
        this.isValid = 1;
        this.sessionId = httpSessionId + SESSION_SEP + userId + SESSION_SEP + date.getTime();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getHttpSessionId() {
        return httpSessionId;
    }

    public void setHttpSessionId(String httpSessionId) {
        this.httpSessionId = httpSessionId;
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public void setSessionStartTime(long sessionStartTime) {
        this.sessionStartTime = sessionStartTime;
    }

    public long getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(long sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isSessionValid) {
        this.isValid = isSessionValid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((httpSessionId == null) ? 0 : httpSessionId.hashCode());
        result = prime * result + isValid;
        result = prime * result + (int) (sessionEndTime ^ (sessionEndTime >>> 32));
        result = prime * result
                + ((sessionId == null) ? 0 : sessionId.hashCode());
        result = prime
                * result + (int) (sessionStartTime ^ (sessionStartTime >>> 32));
        result = prime * result + (int) (userId ^ (userId >>> 32));
        return result;
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
        SessionData other = (SessionData) obj;
        if (httpSessionId == null) {
            if (other.httpSessionId != null) {
                return false;
            }
        } else if (!httpSessionId.equals(other.httpSessionId)) {
            return false;
        }
        if (isValid != other.isValid) {
            return false;
        }
        if (sessionEndTime != other.sessionEndTime) {
            return false;
        }
        if (sessionId == null) {
            if (other.sessionId != null) {
                return false;
            }
        } else if (!sessionId.equals(other.sessionId)) {
            return false;
        }
        if (sessionStartTime != other.sessionStartTime) {
            return false;
        }

        return userId == other.userId;
    }

    @Override
    public String toString() {
        return "SessionData [userId=" + userId + ", sessionId=" + sessionId
                + ", httpSessionId=" + httpSessionId + ", sessionStartTime="
                + sessionStartTime + ", sessionEndTime=" + sessionEndTime
                + ", isSessionValid=" + isValid + "]";
    }
}
