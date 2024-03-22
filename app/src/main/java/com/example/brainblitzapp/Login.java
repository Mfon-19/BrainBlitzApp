package com.example.brainblitzapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.type.DateTime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sessionManager = new SessionManager(this);

        final EditText usernameText = findViewById(R.id.username);
        final EditText passwordText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameText.getText().toString().trim();
                final String password = passwordText.getText().toString().trim();

                //validate username and password fields
                if(username.isEmpty()){
                    Toast.makeText(Login.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.isEmpty()){
                    Toast.makeText(Login.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if fields are valid login user with username and password
                loginWithUsername(username, password);
            }
        });
    }

    protected void loginWithUsername(String username, String password){
        final String[] userEmail = {""};

        //query the database to get the user email associated with the given username
        db.collection("users").whereEqualTo("username", username).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                userEmail[0] = documentSnapshot.getString("email");
                            }

                            //signin user with email and password
                            signinUser(userEmail[0].trim(), password);
                        }
                        else{
                            Toast.makeText(Login.this, "Incorrect Login Info", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signinUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    //save user login time for session management
                    sessionManager.saveLoginTime();

                    //send user to the homepage
                    startActivity(new Intent( Login.this, HomeActivity.class));
                }else{
                    Toast.makeText(Login.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
