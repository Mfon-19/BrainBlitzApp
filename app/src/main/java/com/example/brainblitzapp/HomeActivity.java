package com.example.brainblitzapp;


import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        List<QuizModel> quizModelList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        //this is how we would set up the topics recycler view, not getDataFromFirebase()

        quizModelList.add(new QuizModel(9, "General Knowledge"));
        quizModelList.add(new QuizModel(18, "Computer Science"));
        quizModelList.add(new QuizModel(22, "Geography"));
        quizModelList.add(new QuizModel(24, "Politics"));
        quizModelList.add(new QuizModel(27, "Animals"));
        quizModelList.add(new QuizModel(25, "Art"));

        // on the intent to send the user to QuizActivity, should send both the quiz model id variable
        // and the difficulty the user selected as a string

        progressBar.setVisibility(View.GONE);
        QuizListAdapter adapter = new QuizListAdapter(quizModelList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}

