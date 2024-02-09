package com.spark.swarajyabiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

public class FullScreen<MyAdapter> extends AppCompatActivity {
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        imageView = findViewById(R.id.popup_image_view);

        Intent intent = getIntent();
        if (intent != null) {
            String imageUrl = intent.getStringExtra("image_url");
            if (imageUrl != null) {
                Glide.with(this)
                        .load(imageUrl)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);
            }
        }
    }
}


