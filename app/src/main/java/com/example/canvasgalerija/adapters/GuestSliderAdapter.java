package com.example.canvasgalerija.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.canvasgalerija.R;

public class GuestSliderAdapter extends PagerAdapter {
    private int currentApiVersion;

    Context context;

    LayoutInflater layoutInflater;
    public GuestSliderAdapter(Context context) {
        this.context = context;
    }

    int imagesArray[] ={
            R.drawable.logo,
            R.drawable.gost1,
            R.drawable.gost2,
            R.drawable.gost3
    };

    int headingArray[] ={
            R.string.guest_first_slide,
            R.string.guest_second_slide,
            R.string.guest_third_slide,
            R.string.guest_fourth_slide

    };

    int descriptiongArray[] ={
            R.string.description,
            R.string.description,
            R.string.description,
            R.string.description

    };

    @Override
    public int getCount() {
        return headingArray.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.sliding_layout,container,false);

        ImageView imageView = view.findViewById(R.id.slider_img);
        TextView heading = view.findViewById(R.id.heading);
        TextView description= view.findViewById(R.id.description);

        imageView.setImageResource(imagesArray[position]);
        heading.setText(headingArray[position]);
        description.setText(descriptiongArray[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
