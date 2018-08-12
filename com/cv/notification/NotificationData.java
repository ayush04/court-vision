/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.notification;

import java.util.Objects;

/**
 *
 * @author ayush
 */
public class NotificationData {
    private long notificationId;
    private long createdBy;
    private long createdFor;
    private int notificationType;
    private long whenCreated;
    private String title;
    private String content;
    private int isRead;
    private long whenRead;

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getCreatedFor() {
        return createdFor;
    }

    public void setCreatedFor(long createdFor) {
        this.createdFor = createdFor;
    }
    
    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public long getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(long whenCreated) {
        this.whenCreated = whenCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public long getWhenRead() {
        return whenRead;
    }

    public void setWhenRead(long whenRead) {
        this.whenRead = whenRead;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + (int) (this.notificationId ^ (this.notificationId >>> 32));
        hash = 29 * hash + (int) (this.createdBy ^ (this.createdBy >>> 32));
        hash = 29 * hash + (int) (this.createdFor ^ (this.createdFor >>> 32));
        hash = 29 * hash + (int) (this.notificationType ^ (this.notificationType >>> 32));
        hash = 29 * hash + (int) (this.whenCreated ^ (this.whenCreated >>> 32));
        hash = 29 * hash + Objects.hashCode(this.title);
        hash = 29 * hash + Objects.hashCode(this.content);
        hash = 29 * hash + this.isRead;
        hash = 29 * hash + (int) (this.whenRead ^ (this.whenRead >>> 32));
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
        final NotificationData other = (NotificationData) obj;
        if (this.notificationId != other.notificationId) {
            return false;
        }
        if (this.createdBy != other.createdBy) {
            return false;
        }
        if (this.createdFor != other.createdFor) {
            return false;
        }
        if (this.notificationType != other.notificationType) {
            return false;
        }
        if (this.whenCreated != other.whenCreated) {
            return false;
        }
        if (this.isRead != other.isRead) {
            return false;
        }
        if (this.whenRead != other.whenRead) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        return Objects.equals(this.content, other.content);
    }

    @Override
    public String toString() {
        return "NotificationData{" + "notificationId=" + notificationId + 
                ", createdBy=" + createdBy + ", createdFor=" + createdFor + 
                ", notificationType=" + notificationType + ", whenCreated=" + whenCreated + 
                ", title=" + title + ", content=" + content + ", isRead=" + isRead + 
                ", whenRead=" + whenRead + '}';
    }
}
