package com.example.brainblitzapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);

        mAuth = FirebaseAuth.getInstance();

        final Button signupButton = findViewById(R.id.signupButton);
        final String email = findViewById(R.id.email).toString();
        final String username = findViewById(R.id.usernameEditText).toString();
        final String password = findViewById(R.id.password).toString();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                //register the user with their email and password
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Signup.this, "Signup Successful!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(Signup.this, "Signup Unsuccessful!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    private static boolean isValidEmail(String email){
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+(?:\\\\.[a-zA-Z0-9_!#$%&’*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\\\.[a-zA-Z0-9-]+)*$");
    }

    private static boolean isValidPassword(String password){
        return (PasswordValidator.checkPasswordStrength(password) < 5);
    }
}