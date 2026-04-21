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

public class Movie {
    private String title;
    private String id;
    private List<String> categories;

    public Movie(String title, String id, List<String> categories) {
        this.title = title;
        this.id = id;
        this.categories = categories;
    }

    public String getTitle() { return title; }
    public String getId() { return id; }
    public List<String> getCategories() { return categories; }
}