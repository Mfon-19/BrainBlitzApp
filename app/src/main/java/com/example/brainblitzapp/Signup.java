package com.example.brainblitzapp;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    SessionManager sessionManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        sessionManager = new SessionManager(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final Button signupButton = findViewById(R.id.signupButton);
        final EditText emailText = findViewById(R.id.email);
        final EditText usernameText = findViewById(R.id.usernameEditText);
        final EditText passwordText = findViewById(R.id.password);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailText.getText().toString().trim();
                final String password = passwordText.getText().toString().trim();
                final String username = usernameText.getText().toString().trim();

                //first validate user inputs
                if(email.isEmpty()) {
                    Toast.makeText(Signup.this, "Please enter an Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidEmail(email)){
                    Toast.makeText(Signup.this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(username.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter a Username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter a Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidPassword(password)){
                    Toast.makeText(Signup.this, "Please enter a valid Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //if all fields are valid
                signupUser(email, password, username);
            }
        });
    }

    private void signupUser(String email, String password, String username){
        //register the user with their email and password
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if user signup successful, register user in db with username and email
                if(task.isSuccessful()){
                    Toast.makeText(Signup.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                    sessionManager.saveLoginTime();
                    registerUserInDatabase(username, email);
                }
                else {
                    Toast.makeText(Signup.this, "Signup Unsuccessful!", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private void registerUserInDatabase(String username, String email){
        //create a username and email field and store provided parameters
        Map<String, Object> users = new HashMap<>();
        users.put("username", username);
        users.put("email", email);

        db.collection("users").add(users).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                //users points are 0 because they have not yet done any quiz
                sessionManager.updateUserPoints(0);
                //send user to the homepage
                startActivity(new Intent( Signup.this, HomeActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Debugging texts", "Error creating user in db", e);
            }
        });
    }

    private static boolean isValidEmail(String email){
        String regex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidPassword(String password){
        return (PasswordValidator.checkPasswordStrength(password) >= 5);
    }
}