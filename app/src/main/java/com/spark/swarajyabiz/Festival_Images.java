package com.spark.swarajyabiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.FestivalImagesAdapter;

import java.util.ArrayList;
import java.util.Iterator;

public class Festival_Images extends AppCompatActivity implements FestivalImagesAdapter.OnClick{

    String event, date, month;
    String selectedImageUrl;
    int selectedPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_festival_images);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        RecyclerView recyclerView = findViewById(R.id.festivalview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Set grid layout with 2 columns

        // Retrieve image URLs from intent extras
        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("IMAGE_URL");

        event = getIntent().getStringExtra("FESTIVAL_NAME");
        date = getIntent().getStringExtra("date");
        month = getIntent().getStringExtra("month");

        TextView textView =findViewById(R.id.titletextthoughts);
        textView.setText(event);


        if (imageUrls != null && !imageUrls.isEmpty()) {
            // Set up adapter for RecyclerView
            FestivalImagesAdapter adapter = new FestivalImagesAdapter(this, imageUrls, this::onClick);
            recyclerView.setAdapter(adapter);
        } else {
            Log.e("ImageDisplayActivity", "No image URLs found");
            Toast.makeText(this, "No images to display", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(String imageUrl, int position) {

        selectedImageUrl = imageUrl;
        selectedPosition = position;

        // Show dialog
        Dialog(position);

    }

    public void Dialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setMessage("Choose an action:")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call method to delete image from Firebase
                        deleteImage(position);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void deleteImage(int position) {
        DatabaseReference festivalsRef = FirebaseDatabase.getInstance().getReference().child("Festival");

        festivalsRef.child(month).child(date).child(event).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int index = 0;
                    for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                        if (index == position) {
                            // Remove the image at the specified position
                            imageSnapshot.getRef().removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Image deleted successfully
                                            Toast.makeText(Festival_Images.this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
                                            // Refresh RecyclerView if needed
                                            // adapter.notifyDataSetChanged();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Failed to delete image
                                            Toast.makeText(Festival_Images.this, "Failed to delete image", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            break; // Exit the loop after deleting the image
                        }
                        index++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });
    }
}