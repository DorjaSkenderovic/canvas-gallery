package com.example.canvasgalerija.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.canvasgalerija.models.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    Button addAddress;
    RecyclerView recyclerView;
    private List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;
    private List<CartModel> cartModelList = new ArrayList<>();
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
                if(task.isSuccessful()){
                    for (DocumentSnapshot doc :task.getResult().getDocuments()){
                        AddressModel addressModel = new AddressModel(
                                doc.getString("userAddress"),
                                doc.getId());
                        addressModelList.add(addressModel);
                        addressAdapter.notifyDataSetChanged();
                    }
                }

            }
        });

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
                    }
                }
            }
        });

        paymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(addressModelList.isEmpty()){
                    Toast.makeText(AddressActivity.this, "Dodaj podatke za isporuku.", Toast.LENGTH_SHORT).show();
                } else if(addressAdapter.selectedAddress() == "") {
                    Toast.makeText(AddressActivity.this, "Izberi podatke za isporuku.", Toast.LENGTH_SHORT).show();
                } else {
                    addOrderToFirestore();
                    Intent intent = new Intent(AddressActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(AddressActivity.this, "Hvala što kupujete kod nas.", Toast.LENGTH_SHORT).show();
                    deleteOrderItems();
                }
            }

            private void addOrderToFirestore() {
                Map<String, Object> orderInfo = new HashMap<>();
                orderInfo.put("order_address", addressAdapter.selectedAddress());
                orderInfo.put("timestamp", FieldValue.serverTimestamp());
                
                String id = java.util.UUID.randomUUID().toString();
               firestore.collection("Orders").document(id).set(orderInfo);
               for(CartModel item: cartModelList) {
                   final HashMap<String,Object> cartMap = new HashMap<>();

                   cartMap.put("productName", item.getProductName());
                   cartMap.put("productPrice",item.getProductPrice());
                   cartMap.put("productImg", item.getProductImg());
                   cartMap.put("totalQuantity",item.getTotalQuantity());
                   cartMap.put("totalPrice",item.getTotalPrice());

                   firestore.collection("Orders").document(id).collection("Items").document(item.getId()).set(cartMap);
               }
            }
            
            private void deleteOrderItems() {
                String id = auth.getCurrentUser().getUid();
                firestore.collection("AddToCart").document(id).collection("User")
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                firestore.collection("AddToCart").document(id).
                                        collection("User").document(document.getId()).delete();
                            }
                        }
                    }
                });

                firestore.collection("AddToCart").document(id)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error deleting document", e);
                            }
                        });
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