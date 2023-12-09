package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class horizantalImageScroll extends AppCompatActivity {


    private TextView[] textViews;
    private int currentTextViewIndex = 0;
    private List<String> imageUrls = new ArrayList<>();
    private static final int PICK_IMAGE_REQUEST = 1;
    private List<Uri> imageUris = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizantal_image_scroll);

        textViews = new TextView[] {
                findViewById(R.id.textView1),
                findViewById(R.id.textView2),
                findViewById(R.id.textView3),
                findViewById(R.id.textView4)
        };

        // Initialize only the first text view as visible
       // textViews[0].setVisibility(View.VISIBLE);

        // Hide all other text views
        hideAllTextViews();
        textViews[0].setVisibility(View.VISIBLE); // Show the first TextView
        currentTextViewIndex = 1; // Set the index to 1 since the first TextView is already visible
    }

    public void selectImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            imageUris.add(selectedImageUri);

            if (currentTextViewIndex <= textViews.length) {
                TextView textView = textViews[currentTextViewIndex - 1];
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.ic_baseline_add_a_photo_24, 0);
                currentTextViewIndex++;

                if (currentTextViewIndex <= textViews.length) {
                    textViews[currentTextViewIndex - 1].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void hideAllTextViews() {
        for (TextView textView : textViews) {
            textView.setVisibility(View.INVISIBLE);
        }
    }

    public void saveImages(View view) {
        // Here, you can save the imageUris list to Firebase Storage and
        // store the download URLs in Firebase Realtime Database.
    }
}