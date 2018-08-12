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
public class MappingData {
    private long id;
    private long gameId;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 53 * hash + (int) (this.gameId ^ (this.gameId >>> 32));
        hash = 53 * hash + (int) (this.userId ^ (this.userId >>> 32));
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
        final MappingData other = (MappingData) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.gameId != other.gameId) {
            return false;
        }
        return this.userId == other.userId;
    }

    @Override
    public String toString() {
        return "MappingData{" + "id=" + id + ", gameId=" + gameId + ", userId=" + userId + '}';
    }
}
