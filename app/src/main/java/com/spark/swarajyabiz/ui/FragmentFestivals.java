package com.spark.swarajyabiz.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



    public class FragmentFestivals extends Fragment implements  DinvisheshAdapter.OnDinvisheshClickListener,
                                                                DaysAdapter.OnItemClickListener{

        DatabaseReference Festivalsref, daysRef;
        private ImageView thoughtimage1, thoughtimage2, thoughtimage3, thoughtimage4, thoughtimage5;
        private FrameLayout frameimage1;

        private List<ImageView> thoughtsImages = new ArrayList<>();
        private List<TextView> thoughtstexts = new ArrayList<>();
        private List<String> imageUrls;
        DinvisheshAdapter dinvisheshAdapter;
        DaysAdapter daysAdapter;
        String shopName, shopcontactNumber, shopownerName, shopimage,shopaddress, businessName, bannerimage;

        public FragmentFestivals() {
            // Required empty public constructor
        }

        @SuppressLint("MissingInflatedId")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view =  inflater.inflate(R.layout.fragment_festivals, container, false);


            Festivalsref = FirebaseDatabase.getInstance().getReference("Festival");
            daysRef = FirebaseDatabase.getInstance().getReference("Days");
            festivalRetrieveImages();
            daysRetrieveImages();

            RecyclerView recyclerView = view.findViewById(R.id.festivalview);
            dinvisheshAdapter = new DinvisheshAdapter(getContext(), FragmentFestivals.this, true);
            recyclerView.setAdapter(dinvisheshAdapter);
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(layoutManager);


            RecyclerView daysrecyclerView = view.findViewById(R.id.daysview);
            daysAdapter = new DaysAdapter(getContext(), FragmentFestivals.this, true);
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

        private void festivalRetrieveImages() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
          //  String currentDate = sdf.format(new Date());
            String currentDate = "6-12-2024";

            Festivalsref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<Event> events = new ArrayList<>();

                        int upcomingDatesCount = 0;

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();

                            // Check if the current date or upcoming dates
                            int dateComparison = compareDates(currentDate, key);

                            if (dateComparison <= 0 && upcomingDatesCount < 5) {
                                upcomingDatesCount++;

                                for (DataSnapshot keySnapshot : snapshot.getChildren()) {
                                    String titleKey = keySnapshot.getKey();

                                    Festivalsref.child(key).child(titleKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                List<String> imageUrls = new ArrayList<>();

                                                for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                                                    String imageUrl = imageSnapshot.getValue(String.class);
                                                    imageUrls.add(imageUrl);
                                                    System.out.println("dfhvn " + imageUrl);
                                                }

                                                // Construct the Event object for each event under the date
                                                Event event = new Event(titleKey, key, imageUrls);
                                                events.add(event);
                                            }

                                            // Notify the adapter after processing each date and its events
                                            dinvisheshAdapter.setEvents(events);
                                            dinvisheshAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                            // Handle errors here
                                        }
                                    });
                                }
                            }
                        }
                    } else {
                        Log.d("Firebase", "No images found for the current date");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error retrieving images: " + databaseError.getMessage());
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
        public void onDinvisheshClick(int position, List<String> imageUrl, String festivalname) throws ExecutionException, InterruptedException {
            Intent intent = new Intent(getContext(), BannerDetails.class);
            // Pass the list of image URLs
            intent.putStringArrayListExtra("IMAGE_URL", (ArrayList<String>) imageUrls);
            intent.putExtra("contactNumber",shopcontactNumber);
            intent.putExtra("shopName", shopName);
            intent.putExtra("shopimage", shopimage);
            intent.putExtra("ownerName", shopownerName);
            intent.putExtra("shopaddress", shopaddress);
            intent.putExtra("FESTIVAL_NAME", festivalname);
            System.out.println("sdfvd " +imageUrl);
            startActivity(intent);
        }

        @Override
        public void onItemClick(int position, String imageUrl, String businessname) throws ExecutionException, InterruptedException {

        }
    }