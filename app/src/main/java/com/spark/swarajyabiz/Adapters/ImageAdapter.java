package com.spark.swarajyabiz.Adapters;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.spark.swarajyabiz.ModelClasses.Banner;
import com.spark.swarajyabiz.R;

import java.util.List;

public class ImageAdapter extends PagerAdapter {

    Context context;
    private List<Banner> bannerList;
    private LayoutInflater inflater;

    public ImageAdapter(Context context, List<Banner> bannerList) {
        this.bannerList = bannerList;
        this.inflater = LayoutInflater.from(context);
        this.context=context;
    }

    @Override
    public int getCount() {
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.imageView);
        Glide.with(context)
                .load(bannerList.get(position).getImg())
                .into(imageView);

        // Set a click listener to handle image clicks
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String redirectLink = bannerList.get(position).getRedirect();
                if (redirectLink != null && !redirectLink.isEmpty()) {
                    // Open the link in a browser
                   // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(redirectLink));
                   // context.startActivity(browserIntent);
                }
            }
        });

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
