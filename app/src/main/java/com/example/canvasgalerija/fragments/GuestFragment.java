package com.example.canvasgalerija.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.canvasgalerija.R;
import com.example.canvasgalerija.adapters.CategoryAdapter;
import com.example.canvasgalerija.adapters.NoveSlikeAdapter;
import com.example.canvasgalerija.adapters.VasIzborAdapter;
import com.example.canvasgalerija.models.CategoryModel;
import com.example.canvasgalerija.models.NoveSlikeModel;
import com.example.canvasgalerija.models.VasIzborModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class GuestFragment extends Fragment {


    TextView noveSlikeShowAll;

    LinearLayout linearLayout;
    ProgressDialog progressDialog;
    RecyclerView catRecyclerview, noveSlikeRecyclerview, vasIzborRecyclerview;

    //Category recyclerview
    CategoryAdapter categoryAdapter;
    List<CategoryModel> categoryModelList;

    //Nove slike RecyclerView
    NoveSlikeAdapter noveSlikeAdapter;
    List<NoveSlikeModel> noveSlikeModelList;

    //Vas izbor Recyclerview
    VasIzborAdapter vasIzborAdapter;
    List<VasIzborModel> vasIzborModelList;

    //Firestore
    FirebaseFirestore db;

    public GuestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_guest, container, false);



        progressDialog = new ProgressDialog(getActivity());

        catRecyclerview = root.findViewById(R.id.rec_category);
        noveSlikeRecyclerview = root.findViewById(R.id.new_product_rec);
        vasIzborRecyclerview = root.findViewById(R.id.popular_rec);

        linearLayout = root.findViewById(R.id.guest_layout);
        linearLayout.setVisibility(View.GONE);

        //image slider
        ImageSlider imageSlider = root.findViewById(R.id.image_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.dinomanija,"", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.dino5,"", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.dino2,"", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.dino3,"", ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.dino4,"", ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModels);

        progressDialog.setTitle("Dobro došli u Canvas galeriju!");
        progressDialog.setMessage("Molimo sačekajte... ");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Category
        catRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        categoryModelList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(getActivity(),categoryModelList);
        catRecyclerview.setAdapter(categoryAdapter);

        db.collection("Category")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                CategoryModel categoryModel = document.toObject(CategoryModel.class);
                                categoryModelList.add(categoryModel);
                                categoryAdapter.notifyDataSetChanged();
                                linearLayout.setVisibility(View.VISIBLE);
                                progressDialog.dismiss();

                            }
                        } else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //Nove slike
        noveSlikeRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false));
        noveSlikeModelList = new ArrayList<>();
        noveSlikeAdapter = new NoveSlikeAdapter(getContext(),noveSlikeModelList);
        noveSlikeRecyclerview.setAdapter(noveSlikeAdapter);

        db.collection("Nove slike")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                NoveSlikeModel noveSlikeModel = document.toObject(NoveSlikeModel.class);
                                noveSlikeModelList.add(noveSlikeModel);
                                noveSlikeAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        //Ponuda nedelje
        vasIzborRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));
        vasIzborModelList = new ArrayList<>();
        vasIzborAdapter = new VasIzborAdapter(getContext(),vasIzborModelList);
        vasIzborRecyclerview.setAdapter(vasIzborAdapter);

        db.collection("Popularno")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                VasIzborModel vasIzborModel = document.toObject(VasIzborModel.class);
                                vasIzborModelList.add(vasIzborModel);
                                vasIzborAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Toast.makeText(getActivity(), ""+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });

        return root;
    }
}