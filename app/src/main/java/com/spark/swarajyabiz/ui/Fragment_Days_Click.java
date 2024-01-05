package com.spark.swarajyabiz.ui;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.BannerAdapter;
import com.spark.swarajyabiz.CreateBanner;
import com.spark.swarajyabiz.PremiumMembership;
import com.spark.swarajyabiz.R;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.annotations.NonNull;


public class Fragment_Days_Click extends Fragment implements  BannerAdapter.OnItemClickListener{

    TextView textView;
    DatabaseReference categoryRef;
    String titletext, shopName, shopcontactNumber, shopimage, shopownername,shopaddress, bannerimage, month;
    List<String> imageUrls;
    RecyclerView bannerViews;
    BannerAdapter businessBannerAdapter;
    private boolean isPremiumClicked = false;
    AlertDialog dialog;

    public Fragment_Days_Click() {
        // Required empty public constructor
    }

    // Add a method to create a new instance of the fragment with arguments
    public static Fragment_Festival_Event_Click newInstance(String titleText) {
        Fragment_Festival_Event_Click fragment = new Fragment_Festival_Event_Click();
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
        View view = inflater.inflate(R.layout.fragment__days__click, container, false);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        bannerViews = view.findViewById(R.id.bannerviews);
        businessBannerAdapter = new BannerAdapter(getContext(), sharedPreference,Fragment_Days_Click.this);
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
            month = args.getString("month");
            // Now you have the titleText value, use it as needed
        }

        System.out.println("dfughv "+titletext);
        System.out.println("sdvfb v " +month);



        imageUrls = new ArrayList<>();
        daysRetrieveImages();

        return view;
    }

    private void daysRetrieveImages() {
        categoryRef = FirebaseDatabase.getInstance().getReference().child("Days").child(titletext);
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@io.reactivex.rxjava3.annotations.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> imageUrls = new ArrayList<>();

                    // Iterate over the children of the snapshot
                    for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                        String imageUrl = imageSnapshot.getValue(String.class);
                        if (imageUrl != null && !imageUrls.contains(imageUrl)) {
                            imageUrls.add(imageUrl);
                            System.out.println("Image URL: " + imageUrl);
                        }
                    }

                    // Update the adapter with the new list of image URLs
                    businessBannerAdapter.setImageUrls(imageUrls);
                    businessBannerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private String getDayOfWeek(int dayOfWeek) {
        String[] daysOfWeek = {"शुभ रविवार", "शुभ सोमवार", "शुभ मंगळवार", "शुभ बुधवार", "शुभ गुरुवार", "शुभ शुक्रवार", "शुभ शनिवार"};
        return daysOfWeek[dayOfWeek - 1];
    }

    // Compare two date strings and return:
    // -1 if date1 is before date2
    // 0 if date1 is equal to date2
    // 1 if date1 is after date2
    private int compareDates(String date1, String date2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date parsedDate1 = sdf.parse(date1);
            Date parsedDate2 = sdf.parse(date2);

            if (parsedDate1 != null && parsedDate2 != null) {
                return parsedDate1.compareTo(parsedDate2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0; // Return 0 in case of an error
    }

    private String getmonths(int dayOfWeek) {
        String[] daysOfWeek = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return daysOfWeek[dayOfWeek - 1];
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
            }
            // You can optionally implement a logic to redirect to a premium feature screen
        }
    }

    private void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        builder.setView(customLayout);

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
        dialog = builder.create();
        dialog.show();
    }
}