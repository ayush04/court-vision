/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cv.user;

import java.util.Objects;

/**
 *
 * @author ayush
 */
public class UserData {
    private String userName;
    private String firstName;
    private String lastName;
    private long userId;
    private String emailAddress;
    private int isDeleted;
    private String password;
    private long whenCreated;
    private long whenModified;
    private long lastAccessed;
    private int isLoggedIn;
    private int isPlayer;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getWhenCreated() {
        return whenCreated;
    }

    public void setWhenCreated(long whenCreated) {
        this.whenCreated = whenCreated;
    }

    public long getWhenModified() {
        return whenModified;
    }

    public void setWhenModified(long whenModified) {
        this.whenModified = whenModified;
    }

    public long getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(long lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public int getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(int isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public int getIsPlayer() {
        return isPlayer;
    }

    public void setIsPlayer(int isPlayer) {
        this.isPlayer = isPlayer;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.userName);
        hash = 47 * hash + Objects.hashCode(this.firstName);
        hash = 47 * hash + Objects.hashCode(this.lastName);
        hash = 47 * hash + (int) (this.userId ^ (this.userId >>> 32));
        hash = 47 * hash + Objects.hashCode(this.emailAddress);
        hash = 47 * hash + this.isDeleted;
        hash = 47 * hash + Objects.hashCode(this.password);
        hash = 47 * hash + (int) (this.whenCreated ^ (this.whenCreated >>> 32));
        hash = 47 * hash + (int) (this.whenModified ^ (this.whenModified >>> 32));
        hash = 47 * hash + (int) (this.lastAccessed ^ (this.lastAccessed >>> 32));
        hash = 47 * hash + this.isLoggedIn;
        hash = 47 * hash + this.isPlayer;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserData other = (UserData) obj;
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        if (!Objects.equals(this.emailAddress, other.emailAddress)) {
            return false;
        }
        if (this.isDeleted != other.isDeleted) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (this.whenCreated != other.whenCreated) {
            return false;
        }
        if (this.whenModified != other.whenModified) {
            return false;
        }
        if (this.lastAccessed != other.lastAccessed) {
            return false;
        }
        if (this.isLoggedIn != other.isLoggedIn) {
            return false;
        }
        return this.isPlayer == other.isPlayer;
    }

    @Override
    public String toString() {
        return "UserData{" + "userName=" + userName + ", firstName=" + firstName + ", lastName=" + lastName + ", userId=" + userId + ", emailAddress=" + emailAddress + ", isDeleted=" + isDeleted + ", whenCreated=" + whenCreated + ", whenModified=" + whenModified + ", lastAccessed=" + lastAccessed + ", isLoggedIn=" + isLoggedIn + ", isPlayer=" + isPlayer + '}';
    }
}
