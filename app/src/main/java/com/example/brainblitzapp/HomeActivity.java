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
        getDataFromFirebase();
    }
    private void setupRecyclerView() {
        binding.progressBar.setVisibility(View.GONE);
        adapter = new QuizListAdapter(quizModelList, this);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
    }
    private void getDataFromFirebase() {
        binding.progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    QuizModel quizModel = snapshot.getValue(QuizModel.class);
                    if (quizModel != null) {
                        quizModelList.add(quizModel);
                    }
                }
            }
            setupRecyclerView();
        });
    }
}

