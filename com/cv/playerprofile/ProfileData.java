/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.playerprofile;

import java.util.Objects;

/**
 *
 * @author ayush
 */
public class ProfileData {
    private long userId;
    private float height;
    private float weight;
    private int basketsScored;
    private int totalGamesPlayed;
    private int fitbitEnabled;
    private String fitbitClientId;
    private String role;
    private int isDeleted;
    private long whenModified;
    private long whenDeleted;

    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getBasketsScored() {
        return basketsScored;
    }

    public void setBasketsScored(int basketsScored) {
        this.basketsScored = basketsScored;
    }

    public int getTotalGamesPlayed() {
        return totalGamesPlayed;
    }

    public void setTotalGamesPlayed(int totalGamesPlayed) {
        this.totalGamesPlayed = totalGamesPlayed;
    }

    public int getFitbitEnabled() {
        return fitbitEnabled;
    }

    public void setFitbitEnabled(int fitbitEnabled) {
        this.fitbitEnabled = fitbitEnabled;
    }

    public String getFitbitClientId() {
        return fitbitClientId;
    }

    public void setFitbitClientId(String fitbitClientId) {
        this.fitbitClientId = fitbitClientId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public long getWhenModified() {
        return whenModified;
    }

    public void setWhenModified(long whenModified) {
        this.whenModified = whenModified;
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
        hash = 11 * hash + (int) (this.userId ^ (this.userId >>> 32));
        hash = 11 * hash + Float.floatToIntBits(this.height);
        hash = 11 * hash + Float.floatToIntBits(this.weight);
        hash = 11 * hash + this.basketsScored;
        hash = 11 * hash + this.totalGamesPlayed;
        hash = 11 * hash + this.fitbitEnabled;
        hash = 11 * hash + Objects.hashCode(this.fitbitClientId);
        hash = 11 * hash + Objects.hashCode(this.role);
        hash = 11 * hash + this.isDeleted;
        hash = 11 * hash + (int) (this.whenModified ^ (this.whenModified >>> 32));
        hash = 11 * hash + (int) (this.whenDeleted ^ (this.whenDeleted >>> 32));
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
        final ProfileData other = (ProfileData) obj;
        if (this.userId != other.userId) {
            return false;
        }
        if (Float.floatToIntBits(this.height) != Float.floatToIntBits(other.height)) {
            return false;
        }
        if (Float.floatToIntBits(this.weight) != Float.floatToIntBits(other.weight)) {
            return false;
        }
        if (this.basketsScored != other.basketsScored) {
            return false;
        }
        if (this.totalGamesPlayed != other.totalGamesPlayed) {
            return false;
        }
        if (this.fitbitEnabled != other.fitbitEnabled) {
            return false;
        }
        if (this.isDeleted != other.isDeleted) {
            return false;
        }
        if (this.whenModified != other.whenModified) {
            return false;
        }
        if (this.whenDeleted != other.whenDeleted) {
            return false;
        }
        if (!Objects.equals(this.fitbitClientId, other.fitbitClientId)) {
            return false;
        }
        return Objects.equals(this.role, other.role);
    }

    @Override
    public String toString() {
        return "ProfileData{" + "userId=" + userId + ", height=" + height + ", weight=" + weight + ", basketsScored=" + basketsScored + ", totalGamesPlayed=" + totalGamesPlayed + ", fitbitEnabled=" + fitbitEnabled + ", fitbitClientId=" + fitbitClientId + ", role=" + role + ", isDeleted=" + isDeleted + ", whenModified=" + whenModified + ", whenDeleted=" + whenDeleted + '}';
    }
}
