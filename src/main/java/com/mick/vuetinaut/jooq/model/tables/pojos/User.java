/*
 * This file is generated by jOOQ.
 */
package com.mick.vuetinaut.jooq.model.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.12"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class User implements Serializable {

    private static final long serialVersionUID = -1996881948;

    private UUID      uuid;
    private String    username;
    private String    password;
    private Timestamp dateCreated;
    private Timestamp lastLogin;
    private String    userType;

    public User() {}

    public User(User value) {
        this.uuid = value.uuid;
        this.username = value.username;
        this.password = value.password;
        this.dateCreated = value.dateCreated;
        this.lastLogin = value.lastLogin;
        this.userType = value.userType;
    }

    public User(
        UUID      uuid,
        String    username,
        String    password,
        Timestamp dateCreated,
        Timestamp lastLogin,
        String    userType
    ) {
        this.uuid = uuid;
        this.username = username;
        this.password = password;
        this.dateCreated = dateCreated;
        this.lastLogin = lastLogin;
        this.userType = userType;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getLastLogin() {
        return this.lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final User other = (User) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        }
        else if (!uuid.equals(other.uuid))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        }
        else if (!username.equals(other.username))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        }
        else if (!password.equals(other.password))
            return false;
        if (dateCreated == null) {
            if (other.dateCreated != null)
                return false;
        }
        else if (!dateCreated.equals(other.dateCreated))
            return false;
        if (lastLogin == null) {
            if (other.lastLogin != null)
                return false;
        }
        else if (!lastLogin.equals(other.lastLogin))
            return false;
        if (userType == null) {
            if (other.userType != null)
                return false;
        }
        else if (!userType.equals(other.userType))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.uuid == null) ? 0 : this.uuid.hashCode());
        result = prime * result + ((this.username == null) ? 0 : this.username.hashCode());
        result = prime * result + ((this.password == null) ? 0 : this.password.hashCode());
        result = prime * result + ((this.dateCreated == null) ? 0 : this.dateCreated.hashCode());
        result = prime * result + ((this.lastLogin == null) ? 0 : this.lastLogin.hashCode());
        result = prime * result + ((this.userType == null) ? 0 : this.userType.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("User (");

        sb.append(uuid);
        sb.append(", ").append(username);
        sb.append(", ").append(password);
        sb.append(", ").append(dateCreated);
        sb.append(", ").append(lastLogin);
        sb.append(", ").append(userType);

        sb.append(")");
        return sb.toString();
    }
}
