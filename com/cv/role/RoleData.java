/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cv.role;

import java.util.Objects;

/**
 *
 * @author ayush
 */
public class RoleData {
    private long roleId;
    private String roleName;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.roleId ^ (this.roleId >>> 32));
        hash = 97 * hash + Objects.hashCode(this.roleName);
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
        final RoleData other = (RoleData) obj;
        if (this.roleId != other.roleId) {
            return false;
        }
        return Objects.equals(this.roleName, other.roleName);
    }

    @Override
    public String toString() {
        return "UserRoleData{" + "roleId=" + roleId + ", roleName=" + roleName + '}';
    }
}
