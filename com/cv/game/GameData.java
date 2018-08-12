/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.game;

/**
 *
 * @author ayush
 */
public class GameData {
    private long gameId;
    private long startTime;
    private long endTime;
    private long team1;
    private long team2;

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
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
    
    public long getTeam1() {
        return team1;
    }

    public void setTeam1(long team1) {
        this.team1 = team1;
    }
    
    public long getTeam2() {
        return team2;
    }

    public void setTeam2(long team2) {
        this.team2 = team2;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.gameId ^ (this.gameId >>> 32));
        hash = 97 * hash + (int) (this.startTime ^ (this.startTime >>> 32));
        hash = 97 * hash + (int) (this.endTime ^ (this.endTime >>> 32));
        hash = 97 * hash + (int) (this.team1 ^ (this.team1 >>> 32));
        hash = 97 * hash + (int) (this.team2 ^ (this.team2 >>> 32));
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
        final GameData other = (GameData) obj;
        if (this.gameId != other.gameId) {
            return false;
        }
        if (this.startTime != other.startTime) {
            return false;
        }
        if (this.team1 != other.team1) {
            return false;
        }if (this.team2 != other.team2) {
            return false;
        }
        
        return this.endTime == other.endTime;
    }

    @Override
    public String toString() {
        return "GameData{" + "gameId=" + gameId + ", startTime=" + startTime + ", endTime=" + endTime + ", team1=" + team1 + ", team2=" + team2 + '}';
    }
}
