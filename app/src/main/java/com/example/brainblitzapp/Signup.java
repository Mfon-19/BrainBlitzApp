package com.example.brainblitzapp;

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
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final Button signupButton = findViewById(R.id.signupButton);
        final EditText emailText = (EditText) findViewById(R.id.email);
        final EditText usernameText = (EditText) findViewById(R.id.usernameEditText);
        final EditText passwordText = (EditText) findViewById(R.id.password);


        Log.d("Debugging texts", "Starting user input validations");
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailText.getText().toString().trim();
                final String password = passwordText.getText().toString().trim();
                final String username = usernameText.getText().toString().trim();
                Log.d("Debugging texts", "Email is: "+email);
                Log.d("Debugging texts", "Password is: "+password);
//                //first validate user inputs
                if(email.isEmpty()) {
                    Toast.makeText(Signup.this, "Please enter an Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidEmail(email)){
                    Toast.makeText(Signup.this, "Please enter a valid Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("Debugging texts", "Email validated");
                if(username.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter a Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("Debugging texts", "Username validated");
                if(password.isEmpty()){
                    Toast.makeText(Signup.this, "Please enter a Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isValidPassword(password)){
                    Toast.makeText(Signup.this, "Please enter a valid Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("Debugging texts", "Password validated");
                Log.d("Debugging texts", "Start of creating user with email and pswd");
                //register the user with their email and password
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Signup.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                            Log.d("Debugging texts", "Authentication successful");
                        }
                        else {
                            Toast.makeText(Signup.this, "Signup Unsuccessful!", Toast.LENGTH_SHORT).show();
                            Log.d("Debugging texts", "Authentication unsuccessful");
                        }
                    }
                });
                Log.d("Debugging texts", "End of creating user with email and pswd");
                Log.d("Debugging texts", "Start of creating user in db");
                //to create a username;
                Map<String, Object> users = new HashMap<>();
                users.put("username", username);
                users.put("email", email);

                db.collection("users").add(users).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d("Debugging texts", "Created user in db successfully!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Debugging texts", "Error creating user in db", e);
                    }
                });
                Log.d("Debugging texts", "End of creating user in db");
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
        return (PasswordValidator.checkPasswordStrength(password) < 5);
    }
}