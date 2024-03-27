package com.example.brainblitzapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.brainblitzapp.databinding.ScoreBinding;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private static List<QuestionModel> questionModelList;
    private int currentQuestionIndex = 0;
    private String selectedAnswer = "";
    private int score = 0;
    private int incrementValue;

    Button btn0, btn1, btn2, btn3, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_activity);

        btn0 = findViewById(R.id.btn0);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        nextBtn = findViewById(R.id.next_btn);

        btn0.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        Intent intent = getIntent();


        int category = intent.getIntExtra("id", 0);
        String difficulty = intent.getStringExtra("difficulty");

        //set up how much a correct question is worth in points
        if(difficulty.equals("easy")) incrementValue = 1;
        if(difficulty.equals("medium")) incrementValue = 2;
        if(difficulty.equals("hard")) incrementValue = 3;

        //i'm thinking we set the questions up in the HomeActivity so this class doesn't have too much processing to do


        questionModelList = getQuestions(category, difficulty);


        //startTimer();
    }

//    private void startTimer() {
//        long totalTimeInMillis = Integer.parseInt(time) * 60 * 1000L;
//        new CountDownTimer(totalTimeInMillis, 1000L) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                long seconds = millisUntilFinished / 1000;
//                long minutes = seconds / 60;
//                long remainingSeconds = seconds % 60;
//                TextView timerIndicatorTextview = findViewById(R.id.timer_indicator_textview);
//                timerIndicatorTextview.setText(String.format("%02d:%02d", minutes, remainingSeconds));
//            }
//
//            @Override
//            public void onFinish() {
//                // Finish the quiz
//            }
//        }.start();
//    }

    private void loadQuestions() {
        Log.d("Debugging Texts", "CurrentQuestionIndex is: " + currentQuestionIndex);

        selectedAnswer = "";
        if (currentQuestionIndex == questionModelList.size()) {
            finishQuiz();
            return;
        }

        TextView questionIndicatorTextView = findViewById(R.id.question_indicator_textview);
        LinearProgressIndicator questionProgressIndicator = findViewById(R.id.question_progress_indicator);
        questionIndicatorTextView.setText("Question " + (currentQuestionIndex + 1) + "/ " + questionModelList.size());
        questionProgressIndicator.setProgress((int) (currentQuestionIndex * 100.0f / questionModelList.size()));

        // set the question
        TextView questionTextview = findViewById(R.id.question_textview);
        questionTextview.setText(questionModelList.get(currentQuestionIndex).getQuestion());
        Log.d("Debugging Texts", "Question is: "+questionModelList.get(currentQuestionIndex).getQuestion());

        //sets the answers to be clicked by the user
        btn0.setText(questionModelList.get(currentQuestionIndex).getCorrect());
        Log.d("Debugging Texts", "Correct is: "+questionModelList.get(currentQuestionIndex).getCorrect());
        btn1.setText(questionModelList.get(currentQuestionIndex).getOptions().get(0));
        Log.d("Debugging Texts", "Other option is: "+questionModelList.get(currentQuestionIndex).getOptions().get(0));
        btn2.setText(questionModelList.get(currentQuestionIndex).getOptions().get(1));
        Log.d("Debugging Texts", "Other option is: "+questionModelList.get(currentQuestionIndex).getOptions().get(1));
        btn3.setText(questionModelList.get(currentQuestionIndex).getOptions().get(2));
        Log.d("Debugging Texts", "Other option is: "+questionModelList.get(currentQuestionIndex).getOptions().get(2));
    }

    @Override
    public void onClick(View view) {
        btn0.setBackgroundColor(getResources().getColor(R.color.gray));
        btn1.setBackgroundColor(getResources().getColor(R.color.gray));
        btn2.setBackgroundColor(getResources().getColor(R.color.gray));
        btn3.setBackgroundColor(getResources().getColor(R.color.gray));

        Button clickedBtn = (Button) view;
        if (view.getId() == R.id.next_btn) {
            // Next button is clicked
            if (selectedAnswer.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please select answer to continue", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedAnswer.equals(questionModelList.get(currentQuestionIndex).getCorrect())) {
                score += incrementValue;
                Log.i("Score of quiz", String.valueOf(score));
            }
            currentQuestionIndex++;
            loadQuestions();
        } else {
            // Options button is clicked
            selectedAnswer = clickedBtn.getText().toString();
            clickedBtn.setBackgroundColor(getResources().getColor(R.color.orange));
        }
    }

    private void finishQuiz() {
        int totalQuestions = questionModelList.size();
        int percentage = (int) ((currentQuestionIndex * 100.0f) / totalQuestions);

        ScoreBinding dialogBinding = ScoreBinding.inflate(getLayoutInflater());
        dialogBinding.scoreProgressIndicator.setProgress(percentage);
        dialogBinding.scoreProgressText.setText(percentage + " %");
        if (percentage > 60) {
            dialogBinding.scoreTitle.setText("Congrats! You have passed");
            dialogBinding.scoreTitle.setTextColor(Color.BLUE);
        } else {
            dialogBinding.scoreTitle.setText("Oops! You have failed");
            dialogBinding.scoreTitle.setTextColor(Color.RED);
        }
        dialogBinding.scoreSubtitle.setText(score + " out of " + totalQuestions + " are correct");
        dialogBinding.finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuizActivity.this, HomeActivity.class);
                intent.putExtra("points_from_quiz", score);

                startActivity(intent);
            }
        });

        new AlertDialog.Builder(this)
                .setView(dialogBinding.getRoot())
                .setCancelable(false)
                .show();
    }

    public static void setQuestionModelList(List<QuestionModel> questionModelList) {
        QuizActivity.questionModelList = questionModelList;
    }

    private List<QuestionModel> getQuestions(int category, String difficulty){
        String API_ENDPOINT = "https://opentdb.com/api.php?amount=10&category="+category+"&difficulty="+difficulty+"&type=multiple";
        Log.d("Debugging Texts", "api endpoint is: "+API_ENDPOINT);
        List<QuestionModel> questions = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                API_ENDPOINT,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Log.d("Debugging Texts", "Started Jsonrequest");
                            //get json array "results"
                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            Log.d("Debugging Texts", "loading jsonarray");
                            //iterate over the number of json objects embedded in the json array
                            for(int i = 0; i < jsonArray.length(); i++){
                                //get the first json object at index 'i'
                                JSONObject result = jsonArray.getJSONObject(i);

                                //put the incorrect answers from the "incorrect_answers" json array in the json object into an arraylist
                                List<String> incorrectAnswers = new ArrayList<>();
                                JSONArray jArray = result.getJSONArray("incorrect_answers");
                                for (int j = 0; j < jArray.length(); j++) {
                                    incorrectAnswers.add(jArray.getString(j));
                                }

                                //add a question model object containing the question, incorrect answers list and correct answer into the questions list
                                questions.add(new QuestionModel(result.getString("question"), incorrectAnswers,
                                                                result.getString("correct_answer")));
                                
                            }

                            Log.d("Debugging Texts", "Starting load questions method");
                            Log.d("Debugging Texts", "list size is: "+questions.size());
                            loadQuestions();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });

        requestQueue.add(jsonObjectRequest);

        return questions;
    }
}

