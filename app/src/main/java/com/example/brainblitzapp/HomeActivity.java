package com.example.brainblitzapp;


import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import com.example.brainblitzapp.databinding.HomeActivityBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    private HomeActivityBinding binding;
    private List<QuizModel> quizModelList;
    private QuizListAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = HomeActivityBinding.inflate(getLayoutInflater());
        setContentView(R.layout.home_activity);

        quizModelList = new ArrayList<>();

        //this is how we would set up the topics recycler view, not getDataFromFirebase()

        quizModelList.add(new QuizModel(9, "General Knowledge"));
        quizModelList.add(new QuizModel(10, "Computer Science"));
        quizModelList.add(new QuizModel(22, "Geography"));
        quizModelList.add(new QuizModel(24, "Politics"));
        quizModelList.add(new QuizModel(27, "Animals"));
        quizModelList.add(new QuizModel(25, "Art"));

        // on the intent to send the user to QuizActivity, should send both the quiz model id variable
        // and the difficulty the user selected as a string

        setupRecyclerView();
    }
    private void setupRecyclerView() {
        binding.progressBar.setVisibility(View.GONE);
        adapter = new QuizListAdapter(quizModelList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }
}

