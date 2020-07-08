package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btLogIn;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser() != null ){ goMainActivity();}

        etUsername = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btLogIn    = findViewById(R.id.btnLogIn);
        btnSignUp = findViewById(R.id.btnSignUp);


        btLogIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username,password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });





    }

    private void loginUser(String username, String password)
    {
        Log.i(TAG, "loginUser: Attempting to Login" + username );
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e)
            {
                if(e != null)
                {
                    //TODO : better error handling and ui
                    Log.e(TAG, "issue with login: ",e );
                    return;
                }
                goMainActivity();
                Toast.makeText(LogInActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}