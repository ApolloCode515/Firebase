package com.spark.swarajyabiz;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
public class Fullimages extends AppCompatActivity{

        public static final String EXTRA_IMAGE_URL = "extra_image_url";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_full_images);

            // Set the activity to display in full-screen mode
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            setContentView(R.layout.activity_full_images);

           ImageView imageView = findViewById(R.id.popup_image_view);

            // Retrieve the image URL from the intent
            String imageUrl = getIntent().getStringExtra(EXTRA_IMAGE_URL);

            // Load the image into the ImageView using Glide
           Glide.with(this)
                    .load(imageUrl)
                   .into(imageView);
        }
    }


