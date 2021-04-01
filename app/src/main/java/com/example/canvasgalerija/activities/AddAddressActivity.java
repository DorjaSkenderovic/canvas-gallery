package com.example.canvasgalerija.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.canvasgalerija.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {

    EditText firstName,lastName,address,city,postalCode,phoneNumber;
    Toolbar toolbar;
    Button addAddressBtn;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        toolbar = findViewById(R.id.add_address_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        firstName = findViewById(R.id.add_name);
        lastName = findViewById(R.id.add_lastname);
        address = findViewById(R.id.add_address);
        city = findViewById(R.id.add_city);
        postalCode = findViewById(R.id.add_postal_code);
        phoneNumber = findViewById(R.id.add_phone);
        addAddressBtn = findViewById(R.id.add_address_btn);

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userFirstName = firstName.getText().toString();
                String userLastName = lastName.getText().toString();
                String userAddress = address.getText().toString();
                String userCity = city.getText().toString();
                String userPostalCode = postalCode.getText().toString();
                String userPhoneNumber = phoneNumber.getText().toString();

                String final_address = "";

                if(!userFirstName.isEmpty()){
                    final_address+=userFirstName+" ";
                }
                if(!userLastName.isEmpty()){
                    final_address+=userLastName+", ";
                }
                if(!userAddress.isEmpty()){
                    final_address+=userAddress+", ";
                }
                if(!userPostalCode.isEmpty()){
                    final_address+=userPostalCode+" ";
                }
                if(!userCity.isEmpty()){
                    final_address+=userCity+", ";
                }
                if(!userPhoneNumber.isEmpty()){
                    final_address+=userPhoneNumber;
                }
                if(!userFirstName.isEmpty() && !userLastName.isEmpty() && !userAddress.isEmpty() && !userCity.isEmpty() && !userPostalCode.isEmpty() && !userPhoneNumber.isEmpty()){

                    Map<String, String> map = new HashMap<>();
                    map.put("userAddress", final_address);

                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                            .collection("Address").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(AddAddressActivity.this,"Uspešno ste uneli podatke.",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddAddressActivity.this,AddressActivity.class));
                            }
                        }
                    });
                }else {
                    Toast.makeText(AddAddressActivity.this, "Popuni sva polja!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}