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

public class Login extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        final EditText usernameText = findViewById(R.id.username);
        final EditText passwordText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = usernameText.getText().toString().trim();
                final String password = passwordText.getText().toString().trim();
                Log.d("Debugging Texts", "Username is: "+username);
                Log.d("Debugging Texts", "Password is: "+password);
                if(username.isEmpty()){
                    Toast.makeText(Login.this, "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.isEmpty()){
                    Toast.makeText(Login.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String[] userEmail = {""};

                Log.d("Debugging Texts", "Start of Query");
                db.collection("users").whereEqualTo("username", username).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                        userEmail[0] = documentSnapshot.getString("email");
                                        Log.d("Debugging Texts", "User email is: "+userEmail[0]+"Gotten email successfully");
                                    }
                                    Log.d("Debugging Texts", "Starting signin");
                                    Log.d("Debugging Texts", "User email is: "+userEmail[0]+"Password is: "+password);
                                    mAuth.signInWithEmailAndPassword(userEmail[0].trim(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                                Log.d("Debugging Texts", "Signin successful");
                                                //TODO: Create an intent that send the user to the home screen
                                            }else{
                                                Toast.makeText(Login.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                                                Log.d("Debugging Texts", "Signin unsuccessful");
                                            }
                                        }
                                    });
                                    Log.d("Debugging Texts", "End of signin");
                                }
                                else{
                                    Log.d("Debugging Texts", "Error getting documents", task.getException());
                                    Log.d("Debugging Texts", "Email query unsuccessful");
                                }
                            }
                        });
            }
        });
    }
}