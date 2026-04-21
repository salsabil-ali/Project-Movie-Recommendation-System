/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.salsabil.main;

/**
 *
 * @author Salsbil
 */
import java.util.List;

public class User {
    private String username;
    private String userId;
    private List<String> likedCategories;

    public User(String username, String userId, List<String> likedCategories) {
        this.username = username;
        this.userId = userId;
        this.likedCategories = likedCategories;
    }

    public String getUsername() { return username; }
    public String getUserId() { return userId; }
    public List<String> getLikedCategories() { return likedCategories; }
}
