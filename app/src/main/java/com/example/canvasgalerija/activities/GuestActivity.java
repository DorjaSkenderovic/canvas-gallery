package com.example.canvasgalerija.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.canvasgalerija.R;
import com.example.canvasgalerija.fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class GuestActivity extends AppCompatActivity {
    private int currentApiVersion;

    Fragment guestFragment;
    FirebaseAuth auth;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        toolbar = findViewById(R.id.guest_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        guestFragment = new HomeFragment();
        loadFragment(guestFragment);

    }

    private void loadFragment(Fragment guestFragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.guest_container,guestFragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.guest_main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if( id==R.id.menu_login){
            startActivity(new Intent(GuestActivity.this,LoginActivity.class));

        }else if (id == R.id.menu_register){
            startActivity(new Intent(GuestActivity.this,RegistrationActivity.class));
        }
        return true;
    }
}