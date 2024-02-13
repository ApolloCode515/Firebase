package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import io.reactivex.rxjava3.annotations.NonNull;

public class Referrals extends AppCompatActivity {

    String userId;
    AlertDialog shopdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.referral_alertdialog);

        Button saveButton = findViewById(R.id.sharebtn);
        EditText referralcode = findViewById(R.id.referralcodetext);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView allreferraltext = findViewById(R.id.allreferralsImg);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView errormsgtext = findViewById(R.id.errormsgtext);
        RelativeLayout submitcodelayout = findViewById(R.id.submitcodelayout);
        RelativeLayout sharecodelayout = findViewById(R.id.sharecodelayout);

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

        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Referral");
        DatabaseReference logoRef = FirebaseDatabase.getInstance().getReference("ads");
        logoRef.child("logo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve the URL for the current index
                    String currentImageUrl = snapshot.child("logoimage").getValue(String.class);
                    System.out.println("fgfdvg " + currentImageUrl);

                    // shareUrl(saveButton, referralcode, referralRef, currentImageUrl, errormsgtext);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "button clicked", Toast.LENGTH_SHORT).show();
                            String referraltext = referralcode.getText().toString();

                            if (TextUtils.isEmpty(referraltext)) {
                                errormsgtext.setVisibility(View.VISIBLE);
                                errormsgtext.setText("Please enter a mobile number.");
                                return; // Stop further execution
                            }

                            if (!userId.isEmpty()) {
                                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(referraltext);

                                // Use addListenerForSingleValueEvent to check if the user exists
                                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // User already registered or app already installed
                                            errormsgtext.setVisibility(View.VISIBLE);
                                            errormsgtext.setText("Application already installed.");
                                        } else {
                                            // User is not installed yet
                                            errormsgtext.setVisibility(View.GONE);

                                            // Now check if the user is already referred
                                            if (referralRef != null) {
                                                referralRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        boolean isAlreadyReferred = false;

                                                        if (snapshot.exists()) {
                                                            for (DataSnapshot keySnapshot : snapshot.getChildren()) {
                                                                for (DataSnapshot dataSnapshot : keySnapshot.getChildren()) {
                                                                    String referralKey = dataSnapshot.getKey();
                                                                    System.out.println("etfvvda " + referralKey);

                                                                    if (referralKey != null && referralKey.equals(referraltext)) {
                                                                        // User is already referred
                                                                        isAlreadyReferred = true;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (isAlreadyReferred) {
                                                            errormsgtext.setVisibility(View.VISIBLE);
                                                            errormsgtext.setText("This number is already referred.");
                                                        } else {
                                                            // Continue with your logic for a new user...
                                                            long maxKey = -1;
                                                            for (DataSnapshot childSnapshot : snapshot.child(userId).getChildren()) {
                                                                long key = Long.parseLong(childSnapshot.getKey());
                                                                if (key > maxKey) {
                                                                    maxKey = key;
                                                                }
                                                            }
                                                            long newKey = maxKey + 1;
                                                            // referralRef.child(userId).child(String.valueOf(newKey)).setValue(referraltext);
                                                            referralRef.child(userId).child(referraltext).setValue("Working in progress");

                                                            String message = "\nApp URL: https://play.google.com/store/apps/details?id=com.spark.swarajyabiz&hl=en-IN";
                                                            // Get the logo as a drawable (replace with your actual logo image)
                                                            @SuppressLint("UseCompatLoadingForDrawables")
                                                            Drawable logoDrawable = getResources().getDrawable(R.drawable.newlogo);
                                                            System.out.println("dfbfb " + logoDrawable.toString());

                                                            downloadImageAndShare(currentImageUrl, referraltext, message);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                                        // Handle onCancelled
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle potential errors, if needed
                                        Log.e("Firebase", "Error checking user existence: " + databaseError.getMessage());
                                    }
                                });
                            } else {
                                // Handle the case where the user ID is empty
                                Toast.makeText(getApplicationContext(), "Please enter a valid user ID", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });


        allreferraltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Referrals.this, AllReferrals.class);
                startActivity(intent);
            }
        });


    }

//    private void referral(){
//
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.referral_alertdialog, null);
//        builder.setView(dialogView);
//
//        // Buttons in the dialog
//
//
//
//        shopdialog = builder.create();
//        shopdialog.show();
//        shopdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//    }

    private void downloadImageAndShare(String imageUrl, String phoneNumber, String message) {
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        // Save the bitmap to local storage (e.g., app's cache directory)
                        // Then, share the locally saved image using an Intent

                        // Example code for saving to local storage
                        // Save the bitmap to a file
                        File cachePath = new File(getCacheDir(), "images");
                        cachePath.mkdirs();
                        FileOutputStream stream;
                        try {
                            File imageFile = new File(cachePath, "image.jpg");
                            stream = new FileOutputStream(imageFile);
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            stream.flush();
                            stream.close();

                            // Share the locally saved image with WhatsApp
                            shareImageWithWhatsApp(imageFile, phoneNumber, message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                        // Handle case when the resource is cleared
                    }
                });
    }

    private void shareImageWithWhatsApp(File imageFile, String phoneNumber, String message) {
        Uri imageUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + ".provider",
                imageFile
        );

        // Open WhatsApp with the provided phone number and message
        openWhatsApp(phoneNumber, message);
    }

//    private void shareImageWithWhatsApp(File imageFile, String phoneNumber, String message) {
//        Uri imageUri = FileProvider.getUriForFile(
//                requireContext(),
//                BuildConfig.APPLICATION_ID + ".provider",
//                imageFile
//        );
//
//        // Create an Intent with ACTION_SEND
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//
//        // Put the image and message in the intent
//        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        intent.putExtra(Intent.EXTRA_TEXT, message);
//
//        // Set the package to WhatsApp
//        intent.setPackage("com.whatsapp");
//
//        // Verify that WhatsApp is installed
//        PackageManager pm = requireContext().getPackageManager();
//        if (intent.resolveActivity(pm) != null) {
//            // Start the activity
//            startActivity(intent);
//        } else {
//            // If WhatsApp is not installed, show a message or redirect to the Play Store
//            Toast.makeText(getContext(), "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show();
//            // Alternatively, redirect the user to the Play Store to install WhatsApp
//            // openWhatsAppInPlayStore();
//        }
//    }


    private void openWhatsApp(String phoneNumber, String message) {
        try {
            PackageManager packageManager = getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            } else {
                // If WhatsApp is not installed, show a message or redirect to the Play Store
                Toast.makeText(this, "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show();
                // Alternatively, redirect the user to the Play Store to install WhatsApp
                // openWhatsAppInPlayStore();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
