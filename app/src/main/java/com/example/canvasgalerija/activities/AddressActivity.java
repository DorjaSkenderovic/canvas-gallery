package com.example.canvasgalerija.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canvasgalerija.R;
import com.example.canvasgalerija.adapters.AddressAdapter;
import com.example.canvasgalerija.models.AddressModel;
import com.example.canvasgalerija.models.NoveSlikeModel;
import com.example.canvasgalerija.models.ShowAllModel;
import com.example.canvasgalerija.models.VasIzborModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity {

    Button addAddress;
    RecyclerView recyclerView;
    private List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button paymentBtn;
    Toolbar toolbar;
    String mAddress= " ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        toolbar = findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Object obj = getIntent().getSerializableExtra("item");

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.address_recycler);
        paymentBtn = findViewById(R.id.payment_btn);
        addAddress = findViewById(R.id.add_address_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(getApplicationContext(),addressModelList,"");
        recyclerView.setAdapter(addressAdapter);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                /*if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult().getDocuments()) {

                        AddressModel addressModel = doc.toObject(AddressModel.class);
                        addressModelList.add(addressModel);
                        addressAdapter.notifyDataSetChanged();
                    }
                }*/

                if(task.isSuccessful()){
                    for (DocumentSnapshot doc :task.getResult().getDocuments()){
                        AddressModel addressModel = new AddressModel(
                                doc.getId(),
                                doc.getString("userAddress"));
                        addressModelList.add(addressModel);
                        addressAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    double amount = 0.0;
                if (obj instanceof NoveSlikeModel) {
                    NoveSlikeModel noveSlikeModel = (NoveSlikeModel) obj;
                    amount = noveSlikeModel.getPrice();
                }
                if (obj instanceof VasIzborModel) {
                    VasIzborModel vasIzborModel = (VasIzborModel) obj;
                    amount = vasIzborModel.getPrice();
                }
                if (obj instanceof ShowAllModel) {
                    ShowAllModel showAllModel = (ShowAllModel) obj;
                    amount = showAllModel.getPrice();
                }

                if(addressModelList.isEmpty()){
                    Toast.makeText(AddressActivity.this, "Dodaj podatke za isporuku.", Toast.LENGTH_SHORT).show();
                } else if(addressAdapter.selectedAddress() == "") {
                    Toast.makeText(AddressActivity.this, "Izberi podatke za isporuku.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                    intent.putExtra("amount", amount);
                    startActivity(intent);
                }
            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this,AddAddressActivity.class));
            }
        });
    }
}