package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etpassword;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName = findViewById(R.id.editTextTextPersonName);
        etEmail = findViewById(R.id.editTextTextEmailAddress);
        etpassword = findViewById(R.id.editTextTextPassword);
        btnSubmit = findViewById(R.id.btnSubmit);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                final String password = etpassword.getText().toString();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    // Create the ParseUser
                    ParseUser user = new ParseUser();
                    // Set core properties
                    user.setUsername(name);
                    user.setPassword(password);
                    user.setEmail(email);
                    // Invoke signUpInBackground
                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                goMainActivity(name, password);
                            } else {
                                Log.e("SignUpActivity", "issues with signing up", e);
                                Toast.makeText(SignUpActivity.this, "Username already taken", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "Fill out all fields", Toast.LENGTH_SHORT).show();
                }

            }


        });
    }

    private void goMainActivity(String username, String password) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    //TODO : better error handling and ui
                    Log.e("SignUpActivity", "issue with login: ", e);
                    return;
                }

                Toast.makeText(SignUpActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}