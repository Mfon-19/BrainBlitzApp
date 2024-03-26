package com.example.brainblitzapp;

import java.util.List;

public class QuizModel {
    private int id;
    private String title;

    public QuizModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

