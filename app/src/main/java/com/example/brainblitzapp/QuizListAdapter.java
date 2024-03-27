package com.example.brainblitzapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

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
        View view = LayoutInflater.from(context).inflate(R.layout.quiz_item_recycler_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.title.setText(quizModelList.get(position).getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            String difficulty;
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QuizActivity.class);

                int category = quizModelList.get(position).getId();


                final String[] difficulties = {"Easy", "Medium", "Hard"};
                new AlertDialog.Builder(context)
                        .setView(holder.itemView).setTitle("Select a difficulty").setItems(difficulties, new DialogInterface.OnClickListener() {
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
        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.quiz_title_text);
        }
    }
}
