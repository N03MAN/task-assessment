package com.orangehrm.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model class representing a User in the OrangeHRM system
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String username;
    private String password;
    private boolean status;
    
    @JsonProperty("userRoleId")
    private int userRoleId;
    
    @JsonProperty("empNumber")
    private int empNumber;
    
    // Default constructor required for Jackson
    public User() {
    }
    
    // Constructor with all fields
    public User(String username, String password, boolean status, int userRoleId, int empNumber) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.userRoleId = userRoleId;
        this.empNumber = empNumber;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isStatus() {
        return status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public int getUserRoleId() {
        return userRoleId;
    }
    
    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }
    
    public int getEmpNumber() {
        return empNumber;
    }
    
    public void setEmpNumber(int empNumber) {
        this.empNumber = empNumber;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", status=" + status +
                ", userRoleId=" + userRoleId +
                ", empNumber=" + empNumber +
                '}';
    }
} 