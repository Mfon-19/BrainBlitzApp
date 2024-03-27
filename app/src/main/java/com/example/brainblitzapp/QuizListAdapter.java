package com.example.brainblitzapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.brainblitzapp.QuizActivity;
import com.example.brainblitzapp.databinding.QuizItemRecyclerRowBinding;

import kotlin.jvm.JvmStatic;

public class QuizListAdapter extends RecyclerView.Adapter<QuizListAdapter.MyViewHolder> {

    private final List<QuizModel> quizModelList;
    private final Context context;

    public QuizListAdapter(List<QuizModel> quizModelList, Context context) {
        this.quizModelList = quizModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizItemRecyclerRowBinding binding = QuizItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.quizTitleText.setText(quizModelList.get(position).getTitle());

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            String difficulty;
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuizActivity.class);

                int category = quizModelList.get(position).getId();


                final String[] difficulties = {"Easy", "Medium", "Hard"};
                new AlertDialog.Builder(context)
                        .setView(holder.binding.getRoot()).setTitle("Select a difficulty").setItems(difficulties, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                difficulty = difficulties[which];
                            }
                        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intent.putExtra("difficulty", difficulty);
                                intent.putExtra("id", category);

                                context.startActivity(intent);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // nothing needs to be done because maybe the user wants to select a different topic
                            }
                        }).create();

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizModelList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final QuizItemRecyclerRowBinding binding;

        public MyViewHolder(@NonNull QuizItemRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
