package com.example.brainblitzapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import java.time.Duration;
import java.time.LocalDateTime;

public class SessionManager {
    private static final String LAST_LOGIN = "lastLoginKey";
    private static final String FILE_NAME = "saved_username";
    private static final String USERNAME = "usernameKey";
    private SharedPreferences sharedPreferences;
    private final Context context;

    public SessionManager(Context context){
        this.context = context;
    }
    protected void loadData(){
        sharedPreferences = context.getSharedPreferences(FILE_NAME, MODE_PRIVATE);

        String lastLogin = sharedPreferences.getString(LAST_LOGIN, null);


        //if last saved login time was more than 12 hours ago, data isn't loaded and user is shown the signup/login screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String currentTime = LocalDateTime.now().toString();
            if(getHoursDifference(lastLogin, currentTime) <= 12) {
                if (sharedPreferences.contains(USERNAME)) {
                    //TODO: login user and then load game data according to their unique ID
                }
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

    protected void saveUsername(String username){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(USERNAME, username);
        editor.apply();
    }
}
