package com.example.canvasgalerija.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.canvasgalerija.R;
import com.example.canvasgalerija.models.CartModel;
import com.example.canvasgalerija.models.NoveSlikeModel;
import com.example.canvasgalerija.models.ShowAllModel;
import com.example.canvasgalerija.models.VasIzborModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;
    TextView rating,name,price,quantity,description;
    Button addToCart,buyNow;
    ImageView addItems,removeItems;
    List<CartModel> list;
    int totalAmount = 0;

    Toolbar toolbar;

    int totalQuantity = 1;
    int totalPrice = 0;

    //Nove slike
    NoveSlikeModel noveSlikeModel = null;

    //Popularno
    VasIzborModel vasIzborModel = null;

    //Show All
    ShowAllModel showAllModel = null;

    FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        toolbar = findViewById(R.id.detailed_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        final  Object obj = getIntent().getSerializableExtra("detailed");

        if (obj instanceof NoveSlikeModel) {
            noveSlikeModel = (NoveSlikeModel) obj;
        }else if (obj instanceof  VasIzborModel){
            vasIzborModel = (VasIzborModel) obj;
        }else if (obj instanceof  ShowAllModel){
            showAllModel = (ShowAllModel) obj;
        }

        detailedImg = findViewById(R.id.detailed_img);
        quantity = findViewById(R.id.quantity);
        name = findViewById(R.id.detailed_name);
        rating = findViewById(R.id.rating);
        price = findViewById(R.id.detailed_price);
        description = findViewById(R.id.detailed_desc);

        addToCart = findViewById(R.id.add_to_cart);
        buyNow = findViewById(R.id.buy_now);

        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);

        //Nove slike
        if (noveSlikeModel != null) {
            Glide.with(getApplicationContext()).load(noveSlikeModel.getImg_url()).into(detailedImg);
            name.setText(noveSlikeModel.getName());
            description.setText(noveSlikeModel.getDescription());
            rating.setText(noveSlikeModel.getRating());
            price.setText(String.valueOf(noveSlikeModel.getPrice()));
            name.setText(noveSlikeModel.getName());

            totalPrice = noveSlikeModel.getPrice() * totalQuantity;
        }

        //Ponuda nedelje
        if (vasIzborModel != null) {
            Glide.with(getApplicationContext()).load(vasIzborModel.getImg_url()).into(detailedImg);
            name.setText(vasIzborModel.getName());
            rating.setText(vasIzborModel.getRating());
            price.setText(String.valueOf(vasIzborModel.getPrice()));
            description.setText(vasIzborModel.getDescription());
            name.setText(vasIzborModel.getName());

            totalPrice = vasIzborModel.getPrice() * totalQuantity;
        }

        //Show All
        if (showAllModel != null) {
            Glide.with(getApplicationContext()).load(showAllModel.getImg_url()).into(detailedImg);
            name.setText(showAllModel.getName());
            rating.setText(showAllModel.getRating());
            description.setText(showAllModel.getDescription());
            price.setText(String.valueOf(showAllModel.getPrice()));
            name.setText(showAllModel.getName());

            totalPrice = showAllModel.getPrice() * totalQuantity;
        }

        //Buy now
        buyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedActivity.this,AddressActivity.class);

                if(noveSlikeModel !=null){
                    intent.putExtra("item",noveSlikeModel);
                }
                if(vasIzborModel !=null){
                    intent.putExtra("item",vasIzborModel);
                }
                if(showAllModel !=null){
                    intent.putExtra("item",showAllModel);
                }
                startActivity(intent);
            }
        });

        //Add to cart
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });

        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(totalQuantity<15){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));

                    if(noveSlikeModel != null){
                        totalPrice = noveSlikeModel.getPrice() * totalQuantity;
                    }
                    if(vasIzborModel != null){
                        totalPrice = vasIzborModel.getPrice() * totalQuantity;
                    }
                    if(showAllModel != null){
                        totalPrice = showAllModel.getPrice() * totalQuantity;
                    }

                }

            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity>1){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));

                }
            }
        });

    }

    private void addToCart() {

        final HashMap<String,Object> cartMap = new HashMap<>();

        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice",price.getText().toString());
        cartMap.put("productImg",String.valueOf(detailedImg));
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",totalPrice);

        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(DetailedActivity.this, "Slika je dodata u korpu.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}