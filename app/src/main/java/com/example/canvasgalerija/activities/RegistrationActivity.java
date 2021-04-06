package com.example.canvasgalerija.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.canvasgalerija.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {
    private int currentApiVersion;

    EditText firstname, lastname, email, password, repeatpassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            finish();
        }

        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        repeatpassword = findViewById(R.id.repeatpassword);


        currentApiVersion = android.os.Build.VERSION.SDK_INT;
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }
    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void register(View view) {
        String userFirstName = firstname.getText().toString();
        String userLastName = lastname.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();
        String userRepeatPassword = repeatpassword.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        if(TextUtils.isEmpty(userFirstName)){

            Toast.makeText(this,"Unesi ime!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userLastName)){

            Toast.makeText(this,"Unesi prezime!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userEmail)){

            Toast.makeText(this,"Unesi email!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(userPassword)){

            Toast.makeText(this,"Unesi lozinku!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(userPassword.length() < 6){
            Toast.makeText(this,"Lozinka je prekratka!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!userEmail.matches(emailPattern)){
            Toast.makeText(getApplicationContext(),"Unesi ispravan email!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!userPassword.equals(userRepeatPassword)){

            Toast.makeText(this, "Lozinke se ne podudaraju!", Toast.LENGTH_SHORT).show();
            return;

        }

        auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "Nalog je uspešno kreiran!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, OnBoardingActivity.class));
                } else {
                    Toast.makeText(RegistrationActivity.this, "Kreiranje naloga nije uspelo." +task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        );
    }

    public void login(View view) {
        startActivity(new Intent( RegistrationActivity.this, LoginActivity.class));
        finish();

    }

}