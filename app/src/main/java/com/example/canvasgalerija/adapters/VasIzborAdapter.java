package com.example.canvasgalerija.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.canvasgalerija.R;
import com.example.canvasgalerija.activities.DetailedActivity;
import com.example.canvasgalerija.models.VasIzborModel;

import java.util.List;

public class VasIzborAdapter extends RecyclerView.Adapter<VasIzborAdapter.ViewHolder> {

    private Context context;
    private List<VasIzborModel> vasIzborModelList;

    public VasIzborAdapter(Context context, List<VasIzborModel> vasIzborModelList){
        this.context = context;
        this.vasIzborModelList = vasIzborModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.vas_izbor,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(vasIzborModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(vasIzborModelList.get(position).getName());
        holder.price.setText(String.valueOf(vasIzborModelList.get(position).getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed",vasIzborModelList.get(position));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return vasIzborModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.popularno);
            name = itemView.findViewById(R.id.popularno_ime);
            price = itemView.findViewById(R.id.popularno_cena);

        }
    }
}
