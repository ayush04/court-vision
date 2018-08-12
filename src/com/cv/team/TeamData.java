/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.team;

import java.util.Objects;

/**
 *
 * @author ayush
 */
public class TeamData {
    private long teamId;
    private String teamName;
    private long whenCreated;
    private int isDeleted;
    private long whenDeleted;

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.teamId ^ (this.teamId >>> 32));
        hash = 59 * hash + Objects.hashCode(this.teamName);
        hash = 59 * hash + (int) (this.whenCreated ^ (this.whenCreated >>> 32));
        hash = 59 * hash + this.isDeleted;
        hash = 59 * hash + (int) (this.whenDeleted ^ (this.whenDeleted >>> 32));
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
        final TeamData other = (TeamData) obj;
        if (this.teamId != other.teamId) {
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
        return Objects.equals(this.teamName, other.teamName);
    }

    @Override
    public String toString() {
        return "TeamData{" + "teamId=" + teamId + ", teamName=" + teamName + ", whenCreated=" + whenCreated + ", isDeleted=" + isDeleted + ", whenDeleted=" + whenDeleted + '}';
    }
}