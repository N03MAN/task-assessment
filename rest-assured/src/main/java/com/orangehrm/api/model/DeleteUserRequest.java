package com.orangehrm.api.model;

import java.util.Arrays;

/**
 * Model class representing a request to delete users
 */
public class DeleteUserRequest {
    private int[] ids;
    
    // Default constructor
    public DeleteUserRequest() {
    }
    
    // Constructor with user IDs
    public DeleteUserRequest(int[] ids) {
        this.ids = ids;
    }
    
    // Getters and Setters
    public int[] getIds() {
        return ids;
    }
    
    public void setIds(int[] ids) {
        this.ids = ids;
    }
    
    @Override
    public String toString() {
        return "DeleteUserRequest{" +
                "ids=" + Arrays.toString(ids) +
                '}';
    }
} 