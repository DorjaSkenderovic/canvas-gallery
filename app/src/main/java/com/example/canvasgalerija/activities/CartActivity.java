package com.example.canvasgalerija.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvasgalerija.R;
import com.example.canvasgalerija.adapters.CartAdapter;
import com.example.canvasgalerija.models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    int overAllTotalAmount;
    TextView overAllAmount;
    Toolbar toolbar;
    Button buyNow;
    RecyclerView recyclerView;
    List<CartModel> cartModelList;
    CartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        toolbar = findViewById(R.id.my_cart_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buyNow = findViewById(R.id.buy_now);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //podaci iz cartadapter
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mMessageReceiver, new IntentFilter("TotalAmount"));


        overAllAmount = findViewById(R.id.textView3);
        recyclerView = findViewById(R.id.cart_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartModelList, overAllTotalAmount);
        recyclerView.setAdapter(cartAdapter);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for (DocumentSnapshot doc :task.getResult().getDocuments()){
                        CartModel cartModel = new CartModel(
                                doc.getId(),
                                doc.getString("productImg"),
                                doc.getString("productPrice"),
                                doc.getString("productName"),
                                Integer.parseInt( doc.get("totalPrice").toString()),
                                doc.getString("totalQuantity"));
                        cartModelList.add(cartModel);
                        overAllTotalAmount += cartModel.getTotalPrice();

                        overAllAmount.setText("Ukupna cena: "+overAllTotalAmount+" RSD");

                        cartAdapter.notifyDataSetChanged();
                    }
                }

            }
        });
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cartModelList.isEmpty()){
                Intent intent = new Intent(CartActivity.this,AddressActivity.class);
                startActivity(intent);

            }else {
                    Toast.makeText(CartActivity.this, "Korpa je prazna.", Toast.LENGTH_SHORT).show();
                }}
        });

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            overAllTotalAmount = intent.getIntExtra("totalAmount",0);
            overAllAmount.setText("Ukupna cena: "+overAllTotalAmount+" RSD");

        }
    };
}