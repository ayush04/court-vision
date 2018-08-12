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
public class UserRoleMappingData {
    private long id;
    private long userId;
    private long roleId;

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

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 97 * hash + (int) (this.userId ^ (this.userId >>> 32));
        hash = 97 * hash + (int) (this.roleId ^ (this.roleId >>> 32));
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
        final UserRoleMappingData other = (UserRoleMappingData) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.userId != other.userId) {
            return false;
        }
        return this.roleId == other.roleId;
    }

    @Override
    public String toString() {
        return "UserRoleMappingData{" + "id=" + id + ", userId=" + userId + ", roleId=" + roleId + '}';
    }
}
