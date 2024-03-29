package com.example.brainblitzapp;

import java.util.List;

public class QuestionModel {
    private String question;
    private List<String> options;
    private String correct;

    public QuestionModel(String question, List<String> options, String correct) {
        this.question = question;
        this.options = options;
        this.correct = correct;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrect() {
        return correct;
    }
}
