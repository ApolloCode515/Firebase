package com.spark.swarajyabiz.ui;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.BannerAdapter;
import com.spark.swarajyabiz.CreateBanner;
import com.spark.swarajyabiz.PremiumMembership;
import com.spark.swarajyabiz.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class Fragment_Festival_Event_Click extends Fragment implements  BannerAdapter.OnItemClickListener{

        TextView textView;
        DatabaseReference categoryRef;
        String titletext, shopName, shopcontactNumber, shopimage, shopownername,shopaddress, bannerimage;
        List<String> imageUrls;
        RecyclerView bannerViews;
        BannerAdapter businessBannerAdapter;
        private boolean isPremiumClicked = false;

        public Fragment_Festival_Event_Click() {
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
            View view = inflater.inflate(R.layout.fragment__festival__event__click, container, false);


            SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String userId = sharedPreference.getString("mobilenumber", null);
            if (userId != null) {
                // userId = mAuth.getCurrentUser().getUid();
                System.out.println("dffvf  " +userId);
            }

            bannerViews = view.findViewById(R.id.bannerviews);
            businessBannerAdapter = new BannerAdapter(getContext(), sharedPreference,Fragment_Festival_Event_Click.this);
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

            System.out.println("dfughv "+titletext);
            System.out.println("sdvfb v " +titletext);
            categoryRef = FirebaseDatabase.getInstance().getReference().child("Festival");


            imageUrls = new ArrayList<>();
            retrieveBannerImages();

            return view;
        }

    private void retrieveBannerImages() {
        // Get the current date in the "dd-MM-yyyy" format
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> imageUrls = new ArrayList<>();

                    for (DataSnapshot monthSnapshot : dataSnapshot.getChildren()) {
                        String monthKey = monthSnapshot.getKey();
                        System.out.println("Month: " + monthKey);

                        for (DataSnapshot dateSnapshot : monthSnapshot.getChildren()) {
                            String dateKey = dateSnapshot.getKey();
                            System.out.println("Date: " + dateKey);

                            for (DataSnapshot eventSnapshot : dateSnapshot.getChildren()) {
                                String eventName = eventSnapshot.getKey();
                                System.out.println("Event: " + eventName);

                                if (eventName.equals(titletext)) {
                                    // This event matches the title text, retrieve its images
                                    for (DataSnapshot imageSnapshot : eventSnapshot.getChildren()) {
                                        String imageUrl = imageSnapshot.getValue(String.class);
                                        if (imageUrl != null && !imageUrls.contains(imageUrl)) {
                                            imageUrls.add(imageUrl);
                                            System.out.println("Image: " + imageUrl);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    businessBannerAdapter.setImageUrls(imageUrls);
                    businessBannerAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Firebase", "No images found for the current date");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving images: " + error.getMessage());
            }
        });
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
                Toast.makeText(getContext(), "Get Premium to access more images", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), PremiumMembership.class);
                startActivity(intent);
            }
            // You can optionally implement a logic to redirect to a premium feature screen
        }
    }
    }