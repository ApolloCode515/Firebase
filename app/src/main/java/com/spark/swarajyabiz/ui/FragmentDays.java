package com.spark.swarajyabiz.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.BannerDetails;
import com.spark.swarajyabiz.DaysAdapter;
import com.spark.swarajyabiz.DinvisheshAdapter;
import com.spark.swarajyabiz.Event;
import com.spark.swarajyabiz.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.annotations.NonNull;


public class FragmentDays extends Fragment implements DaysAdapter.OnItemClickListener{


    DatabaseReference Festivalsref, daysRef;
    private ImageView thoughtimage1, thoughtimage2, thoughtimage3, thoughtimage4, thoughtimage5;
    private FrameLayout frameimage1;

    private List<ImageView> thoughtsImages = new ArrayList<>();
    private List<TextView> thoughtstexts = new ArrayList<>();
    private List<String> imageUrls;
    DinvisheshAdapter dinvisheshAdapter;
    DaysAdapter daysAdapter;
    String shopName, shopcontactNumber, shopownerName, shopimage,shopaddress, businessName, bannerimage;

    public FragmentDays() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_days, container, false);

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Festivalsref = FirebaseDatabase.getInstance().getReference("Festival");
        daysRef = FirebaseDatabase.getInstance().getReference("Days");

        daysRetrieveImages();

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView daysrecyclerView = view.findViewById(R.id.daysview);

        daysAdapter = new DaysAdapter(getContext(), FragmentDays.this, true);
        daysrecyclerView.setAdapter(daysAdapter);
        GridLayoutManager layoutManagers = new GridLayoutManager(getContext(), 1);
        daysrecyclerView.setLayoutManager(layoutManagers);

        shopcontactNumber = requireActivity().getIntent().getStringExtra("contactNumber");
        shopName= requireActivity().getIntent().getStringExtra("shopName");
        shopimage =  requireActivity().getIntent().getStringExtra("shopimage");
        shopownerName = requireActivity().getIntent().getStringExtra("ownerName");
        shopaddress = requireActivity().getIntent().getStringExtra("shopaddress");



        return view;
    }


    private void daysRetrieveImages() {
        daysRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> keys = new ArrayList<>();
                    List<String> imageUrls = new ArrayList<>();

                    // Get the current day of the week
                    Calendar calendar = Calendar.getInstance();
                    int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

                    // Iterate through the children and check if the key matches the current day
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String key = childSnapshot.getKey();

                        // Assuming the days are stored as "Sunday", "Monday", ..., "Saturday"
                        if (key.equalsIgnoreCase(getDayOfWeek(currentDayOfWeek))) {
                            keys.add(key);

                            // Retrieve only the image with key "0" for the current day
                            DataSnapshot zeroImageSnapshot = childSnapshot.child("0");
                            String zeroImageUrl = zeroImageSnapshot.getValue(String.class);

                            // Add the URL to the list
                            imageUrls.add(zeroImageUrl);
                        }
                    }

                    // Update the adapter with the new list of image URLs
                    daysAdapter.setBusinessnametexts(keys);
                    daysAdapter.setImageUrls(imageUrls);
                    daysAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    // Helper method to get the day of the week based on Calendar.DAY_OF_WEEK
    private String getDayOfWeek(int dayOfWeek) {
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        return daysOfWeek[dayOfWeek - 1];
    }


    @Override
    public void onDaysClick(int position, String imageUrl, String businessname) throws ExecutionException, InterruptedException {

    }

}