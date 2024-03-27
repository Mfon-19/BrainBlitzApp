package com.example.brainblitzapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {
    SessionManager sessionManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        sessionManager = new SessionManager(this);

        // get current number of points user has accumulated
        int points = sessionManager.getUserPoints();

        //get the intent sent from the session manager to update the UI with accumulated points
        Intent intent  = getIntent();
        int result = intent.getIntExtra("points_from_session_manager", -1);

        //if the intent didn't come from session manager, and came from quiz activity instead
        //add users current points to the points received and update the points in the file
        if(result == -1){
            result = intent.getIntExtra("points_from_quiz", 0);
            points += result;
            sessionManager.updateUserPoints(points);
        } else points = result;

        //update the points text view
        TextView pointsTextView = findViewById(R.id.points_text_view);
        pointsTextView.setText("Points: " + points+"");

        List<QuizModel> quizModelList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        quizModelList.add(new QuizModel(9, "General Knowledge"));
        quizModelList.add(new QuizModel(18, "Computer Science"));
        quizModelList.add(new QuizModel(22, "Geography"));
        quizModelList.add(new QuizModel(24, "Politics"));
        quizModelList.add(new QuizModel(27, "Animals"));
        quizModelList.add(new QuizModel(25, "Art"));

        progressBar.setVisibility(View.GONE);
        QuizListAdapter adapter = new QuizListAdapter(quizModelList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }
}

