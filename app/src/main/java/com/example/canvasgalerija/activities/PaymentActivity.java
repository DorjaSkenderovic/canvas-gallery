package com.example.canvasgalerija.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.canvasgalerija.R;

public class PaymentActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView subTotal,shipping,total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar = findViewById(R.id.payment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        double amount = 0.0;

        amount = getIntent().getDoubleExtra("amount",0.0);
        subTotal = findViewById(R.id.sub_total);
        shipping = findViewById(R.id.shipping);
        total= findViewById(R.id.total);

        subTotal.setText(amount+"RSD");
    }
}