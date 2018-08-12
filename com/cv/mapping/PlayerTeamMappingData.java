/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.mapping;

/**
 *
 * @author ayush
 */
public class PlayerTeamMappingData {
    private long id;
    private long userId;
    private long teamId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 23 * hash + (int) (this.userId ^ (this.userId >>> 32));
        hash = 23 * hash + (int) (this.teamId ^ (this.teamId >>> 32));
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
        final PlayerTeamMappingData other = (PlayerTeamMappingData) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        return this.teamId == other.teamId;
    }

    @Override
    public String toString() {
        return "PlayerTeamMappingData{" + "id=" + id + ", userId=" + userId + ", teamId=" + teamId + '}';
    }
}