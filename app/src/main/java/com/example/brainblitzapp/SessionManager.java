package com.example.brainblitzapp;

import static android.content.Context.MODE_PRIVATE;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessionManager {
    private static final String LAST_LOGIN = "lastLoginKey";
    private static final String FILE_NAME = "saved_info";
    private static final String POINTS = "pointsKey";
    private final SharedPreferences sharedPreferences;
    private final Context context;

    public SessionManager(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);
    }
    protected void loadData(){
        String lastLogin;
        int points;

        if(sharedPreferences.contains(LAST_LOGIN)){
            lastLogin = sharedPreferences.getString(LAST_LOGIN, null);
        }
        else return;

        //if last saved login time was more than 12 hours ago, data isn't loaded and user is shown the signup/login screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String currentTime = LocalDateTime.now().toString();
            if(getHoursDifference(lastLogin, currentTime) <= 12) {
                Intent intent = new Intent(context, HomeActivity.class);

                points = sharedPreferences.getInt(POINTS, -1);

                intent.putExtra("points_from_session_manager", points);

                context.startActivity(intent);
            }
        }
    }

    private static long getHoursDifference(String startDateTime, String endDateTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime start = LocalDateTime.parse(startDateTime);
            LocalDateTime end = LocalDateTime.parse(endDateTime);

            Duration duration = Duration.between(start, end);

            return duration.toHours();
        }

        return 0;
    }

    protected void saveLoginTime(){
        LocalDateTime localDateTime;
        String currentTime;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localDateTime = LocalDateTime.now();
            currentTime = localDateTime.toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LAST_LOGIN, currentTime);
            editor.apply();
        }
    }

    protected void updateUserPoints(int points){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(POINTS, points);
        editor.apply();
    }

    protected int getUserPoints(){
        return sharedPreferences.getInt(POINTS, 1);
    }
}
