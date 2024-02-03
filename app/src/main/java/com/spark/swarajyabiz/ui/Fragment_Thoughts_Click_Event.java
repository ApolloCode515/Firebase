package com.spark.swarajyabiz.ui;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.AllReferrals;
import com.spark.swarajyabiz.BannerAdapter;
import com.spark.swarajyabiz.CreateBanner;
import com.spark.swarajyabiz.CustomBannerImage;
import com.spark.swarajyabiz.PremiumMembership;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


    public class Fragment_Thoughts_Click_Event extends Fragment implements  BannerAdapter.OnItemClickListener{

        TextView textView;
        Boolean premium;
        DatabaseReference categoryRef, userRef;
        String userId, titletext, shopName, shopcontactNumber, shopimage, shopownername,shopaddress, bannerimage;
        List<String> imageUrls;
        RecyclerView bannerViews;
        BannerAdapter businessBannerAdapter;
        private boolean isPremiumClicked = false;
        AlertDialog dialog;
        LinearLayout moreimglayout;
        Button moreimgbtn;
        AlertDialog shopdialog;

        public Fragment_Thoughts_Click_Event() {
            // Required empty public constructor
        }

        // Add a method to create a new instance of the fragment with arguments
        public static Fragment_Thoughts_Click_Event newInstance(String titleText) {
            Fragment_Thoughts_Click_Event fragment = new Fragment_Thoughts_Click_Event();
            Bundle args = new Bundle();
            args.putString("titleText", titleText);
            fragment.setArguments(args);
            return fragment;
        }

        @SuppressLint("MissingInflatedId")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_thoughts_click_event, container, false);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

            moreimglayout = view.findViewById(R.id.moreimglay);
            moreimgbtn = view.findViewById(R.id.moreimgbtn);

            SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            userId = sharedPreference.getString("mobilenumber", null);
            if (userId != null) {
                // userId = mAuth.getCurrentUser().getUid();
                System.out.println("dffvf  " +userId);
            }

            bannerViews = view.findViewById(R.id.bannerviews);
            businessBannerAdapter = new BannerAdapter(getContext(), sharedPreference, Fragment_Thoughts_Click_Event.this);
            bannerViews.setAdapter(businessBannerAdapter);
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            bannerViews.setLayoutManager(layoutManager);
            // Retrieve the argument
            Bundle args = getArguments();
            if (args != null) {
                titletext = args.getString("TITLE_TEXT");
                shopcontactNumber = args.getString("contactNumber");
                shopName = args.getString("shopName");
                shopimage = args.getString("shopimage");
                shopownername = args.getString("ownerName");
                shopaddress = args.getString("shopaddress");
                bannerimage = args.getString("BANNER_IMAGE_URL");
                // Now you have the titleText value, use it as needed
            }

            System.out.println("dfughv "+shopcontactNumber);
            System.out.println("sdvfb v " +bannerimage);
            categoryRef = FirebaseDatabase.getInstance().getReference().child("Thoughts").child(titletext);
            imageUrls = new ArrayList<>();
            retriveBannerImages();

            userRef = FirebaseDatabase.getInstance().getReference("Users");
            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        premium = snapshot.child("premium").getValue(Boolean.class);
                        if (premium.equals(true)) {
                            moreimgbtn.setVisibility(View.GONE);
                        } else{
                            moreimgbtn.setVisibility(View.VISIBLE);
                            moreimgbtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    showImageSelectiondialog();
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
            return view;
        }

        private void retriveBannerImages(){
            categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){

                        // Assuming that each key has a child node with image URLs
                        for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                            String imageUrl = imageSnapshot.getValue(String.class);
                            if (imageUrl != null) {
                                imageUrls.add(imageUrl);
                                System.out.println("fgnfvb "+imageUrl);
                            }
                        }

                    }
                    businessBannerAdapter.setImageUrls(imageUrls);
                    businessBannerAdapter.notifyDataSetChanged();
                    moreimglayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        @Override
        public void onItemClick(int position,Boolean premium, String imageUrl) throws ExecutionException, InterruptedException {


            if (position < 2) {
                // If the clicked position is within the first two images, proceed as normal
                Intent intent = new Intent(getContext(), CreateBanner.class);
                intent.putExtra("IMAGE_URL", imageUrl);
                intent.putExtra("contactNumber", shopcontactNumber);
                intent.putExtra("shopName", shopName);
                intent.putExtra("shopimage", shopimage);
                intent.putExtra("ownerName", shopownername);
                intent.putExtra("shopaddress", shopaddress);
                System.out.println("sdfvsdc " + shopaddress);
                intent.putExtra("IMAGE_URL", imageUrl);
                startActivity(intent);
            } else if (premium.equals(true)) {

                Intent intent = new Intent(getContext(), CreateBanner.class);
                intent.putExtra("IMAGE_URL", imageUrl);
                intent.putExtra("contactNumber", shopcontactNumber);
                intent.putExtra("shopName", shopName);
                intent.putExtra("shopimage", shopimage);
                intent.putExtra("ownerName", shopownername);
                intent.putExtra("shopaddress", shopaddress);
                System.out.println("sdfvsdc " + shopaddress);
                intent.putExtra("IMAGE_URL", imageUrl);
                startActivity(intent);

            }else {
                // If the clicked position is beyond the first two images, show a "Get Premium" toast
                if (!isPremiumClicked) {

                    showImageSelectionDialog();
//                    Toast.makeText(getContext(), "Get Premium to access more images", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getContext(), PremiumMembership.class);
//                    startActivity(intent);
           //         showCustomDialog();
                }
                // You can optionally implement a logic to redirect to a premium feature screen
            }
        }

        private void showCustomDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_premium_alert_dialog, null);

            ViewPager2 viewPager2 = dialogView.findViewById(R.id.viewpager2);
//            List<SlideImage> slideImages = new ArrayList<>();
//            slideImages.add(new SlideImage(R.drawable.a));
//            slideImages.add(new SlideImage(R.drawable.aa));
//            slideImages.add(new SlideImage(R.drawable.b));
//            slideImages.add(new SlideImage(R.drawable.aaa));
//        slideImages.add(new SlideImage(R.drawable.frame5));
//        slideImages.add(new SlideImage(R.drawable.frame6));


           // viewPager2.setAdapter(new SliderAdapter(slideImages, viewPager2));

            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);
            viewPager2.setOffscreenPageLimit(3);
            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1- Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                }
            });

            viewPager2.setPageTransformer(compositePageTransformer);

            builder.setView(dialogView);

            // Add any other properties to the AlertDialog if needed

            AlertDialog dialog = builder.create();
            dialog.show();
        }


        private void showImageSelectionDialog() {
            Dialog builder = new Dialog(getContext());

            // Inflate the custom layout
            View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
            builder.setContentView(customLayout);

            // Find views in the custom layout
            ImageView alertImageView = customLayout.findViewById(R.id.alertImageView);
            TextView alertTitle = customLayout.findViewById(R.id.alertTitle);
            TextView alertMessage = customLayout.findViewById(R.id.alertMessage);
            Button positiveButton = customLayout.findViewById(R.id.positiveButton);

            // Customize the views as needed
            Glide.with(this).asGif().load(R.drawable.gif3).into(alertImageView); // Replace with your image resource
            alertTitle.setText("प्रीमियम");
            alertMessage.setText("हा बॅनर प्रिमियम प्रकरातला आहे.\n" +
                    "डाऊनलोड करण्यासाठी प्रिमियम प्लॅन निवडा.\n");

            // Set positive button click listener
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), PremiumMembership.class);
                    startActivity(intent);
                    dialog.dismiss(); // Dismiss the dialog after the button click
                }
            });

            // Create and show the dialog
           builder.show();
        }

        private void showImageSelectiondialog() {
            Dialog builder = new Dialog(getContext());

            // Inflate the custom layout
            View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
            builder.setContentView(customLayout);

            // Find views in the custom layout
            ImageView alertImageView = customLayout.findViewById(R.id.alertImageView);
            TextView alertTitle = customLayout.findViewById(R.id.alertTitle);
            TextView alertMessage = customLayout.findViewById(R.id.alertMessage);
            Button positiveButton = customLayout.findViewById(R.id.positiveButton);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            Button referralButton = customLayout.findViewById(R.id.referralButton);

            // Customize the views as needed
            Glide.with(this).asGif().load(R.drawable.gif2).into(alertImageView); // Replace with your image resource
            alertTitle.setText("प्रीमियम");
            alertMessage.setText("अधिक इमेज पहाण्यासाठी प्रिमियम प्लॅन निवडावा लागेल.");

            // Set positive button click listener
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), PremiumMembership.class);
                    startActivity(intent);
                    dialog.dismiss(); // Dismiss the dialog after the button click
                }
            });

            referralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            // Create and show the dialog
            builder.show();
        }

        private void referral(){
            DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Referral");

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.referral_alertdialog, null);
            builder.setView(dialogView);

            // Buttons in the dialog
            Button saveButton = dialogView.findViewById(R.id.sharebtn);
            EditText referralcode = dialogView.findViewById(R.id.referralcodetext);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            ImageView allreferraltext = dialogView.findViewById(R.id.allreferralsImg);
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            TextView errormsgtext = dialogView.findViewById(R.id.errormsgtext);
           RelativeLayout submitcodelayout = dialogView.findViewById(R.id.submitcodelayout);
            RelativeLayout sharecodelayout = dialogView.findViewById(R.id.sharecodelayout);

            DatabaseReference logoRef = FirebaseDatabase.getInstance().getReference("ads");

            logoRef.child("logo").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Retrieve the URL for the current index
                        String currentImageUrl = snapshot.child("logoimage").getValue(String.class);
                        System.out.println("fgfdvg " + currentImageUrl);

                        // shareUrl(saveButton, referralcode, referralRef, currentImageUrl, errormsgtext);
                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(), "button clicked", Toast.LENGTH_SHORT).show();
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
                                        public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // User already registered or app already installed
                                                Toast.makeText(getContext(), "User already registered or app already installed", Toast.LENGTH_SHORT).show();
                                                errormsgtext.setVisibility(View.VISIBLE);
                                                errormsgtext.setText("Application already installed.");
                                            } else {
                                                // User is not installed yet
                                                errormsgtext.setVisibility(View.GONE);

                                                // Now check if the user is already referred
                                                if (referralRef != null) {
                                                    referralRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot snapshot) {
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

                                                              //  downloadImageAndShare(currentImageUrl, referraltext, message);
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
                                        public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError databaseError) {
                                            // Handle potential errors, if needed
                                            Log.e("Firebase", "Error checking user existence: " + databaseError.getMessage());
                                        }
                                    });
                                } else {
                                    // Handle the case where the user ID is empty
                                    Toast.makeText(getContext(), "Please enter a valid user ID", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@io.reactivex.rxjava3.annotations.NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });


            allreferraltext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), AllReferrals.class);
                    startActivity(intent);
                }
            });

            shopdialog = builder.create();
            shopdialog.show();
            shopdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }


    }