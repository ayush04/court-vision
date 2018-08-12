/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.attachment;

import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author ayush
 */
public class AttachmentData {
    private long attachmentId;
    private long userId;
    private byte[] content;
    private String attachmentName;
    private String attachmentFormat;
    private long whenCreated;
    private int isDeleted;
    private long whenDeleted;
    private String attachmentUrl;

    public long getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(long attachmentId) {
        this.attachmentId = attachmentId;
    }
    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentFormat() {
        return attachmentFormat;
    }

    public void setAttachmentFormat(String attachmentFormat) {
        this.attachmentFormat = attachmentFormat;
    }

    public long getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(long whenCreated) {
        this.whenCreated = whenCreated;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public long getWhenDeleted() {
        return whenDeleted;
    }

    public void setWhenDeleted(long whenDeleted) {
        this.whenDeleted = whenDeleted;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (int) (this.attachmentId ^ (this.attachmentId >>> 32));
        hash = 43 * hash + (int) (this.userId ^ (this.userId >>> 32));
        hash = 43 * hash + Arrays.hashCode(this.content);
        hash = 43 * hash + Objects.hashCode(this.attachmentName);
        hash = 43 * hash + Objects.hashCode(this.attachmentFormat);
        hash = 43 * hash + (int) (this.whenCreated ^ (this.whenCreated >>> 32));
        hash = 43 * hash + this.isDeleted;
        hash = 43 * hash + (int) (this.whenDeleted ^ (this.whenDeleted >>> 32));
        hash = 43 * hash + Objects.hashCode(this.attachmentUrl);
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
        final AttachmentData other = (AttachmentData) obj;
        if (this.attachmentId != other.attachmentId) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        if (this.whenCreated != other.whenCreated) {
            return false;
        }
        if (this.isDeleted != other.isDeleted) {
            return false;
        }
        if (this.whenDeleted != other.whenDeleted) {
            return false;
        }
        if (!Objects.equals(this.attachmentName, other.attachmentName)) {
            return false;
        }
        if (!Objects.equals(this.attachmentFormat, other.attachmentFormat)) {
            return false;
        }
        if (!Objects.equals(this.attachmentUrl, other.attachmentUrl)) {
            return false;
        }
        return Arrays.equals(this.content, other.content);
    }

    @Override
    public String toString() {
        return "AttachmentData{" + "attachmentId=" + attachmentId + ", userId=" + userId + ", attachmentName=" + attachmentName + ", attachmentFormat=" + attachmentFormat + ", whenCreated=" + whenCreated + ", isDeleted=" + isDeleted + ", whenDeleted=" + whenDeleted + ", attachmentUrl=" + attachmentUrl + '}';
    }
}
