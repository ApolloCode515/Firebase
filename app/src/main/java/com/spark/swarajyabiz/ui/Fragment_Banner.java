package com.spark.swarajyabiz.ui;

import static android.content.Context.MODE_PRIVATE;
import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;
import com.spark.swarajyabiz.BannerCategory;
import com.spark.swarajyabiz.BannerDetails;
import com.spark.swarajyabiz.BusinessBanner;
import com.spark.swarajyabiz.BusinessBannerAdapter;
import com.spark.swarajyabiz.DaysAdapter;
import com.spark.swarajyabiz.DinvisheshAdapter;
import com.spark.swarajyabiz.EmployeeAdapter;
import com.spark.swarajyabiz.Event;
import com.spark.swarajyabiz.JobPostAdapter;
import com.spark.swarajyabiz.ProgressBarClass;
import com.spark.swarajyabiz.R;
import com.spark.swarajyabiz.ScratchCardView;
import com.spark.swarajyabiz.ThoughtsAdapter;
import com.spark.swarajyabiz.TodayDinvisheshAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.annotations.NonNull;
import nl.dionsegijn.konfetti.KonfettiView;

public class Fragment_Banner extends Fragment implements  BusinessBannerAdapter.OnItemClickListener
        ,ThoughtsAdapter.OnThoughtClickListener ,DinvisheshAdapter.OnDinvisheshClickListener,
        DaysAdapter.OnItemClickListener, TodayDinvisheshAdapter.OnDinvisheshClickListener{

    ImageView create, notificationimage, notification;
    TextView usernametextview, sarvapahatext1,sarvapahatext2,sarvapahatext3,sarvapahatext4;
    private DatabaseReference databaseRef;
    private DatabaseReference userRef, adref, Thoughtsref, FestivalRef, businessRef, daysRef;
    private ImageView thoughtimage1, thoughtimage2, thoughtimage3, thoughtimage4, thoughtimage5;
    private ImageView greetingimage1, greetingimage2, greetingimage3, greetingimage4, greetingimage5;
    private ImageView businessimage1,businessimage2,businessimage3,businessimage4,businessimage5;
    String favkeys, businessName, userId,currentUsername, currentUsershopName, currentUsercontactNumber, currentUserShopimage,currentUseraddress, usersId, currentuserId;
    NotificationBadge notificationBadge, notificationcount;
    BusinessBannerAdapter bannerAdapter;
    DaysAdapter daysAdapter;
    ThoughtsAdapter thoughtsAdapter;
    DinvisheshAdapter dinvisheshAdapter;
    TodayDinvisheshAdapter todayDinvisheshAdapter;
    private SharedPreferences sharedPreferences;
    ImageView searchImage;
    CardView searchcard;
    View thoughtview;
    RecyclerView daysrecyclerview;
    private int lastDisplayedIndex = -1;
    List<BusinessBanner> businessBannerList;
     LottieAnimationView lottieAnimationView;

     LinearLayout tlayout,ulayout;

     CardView upcoming;

    public Fragment_Banner() {
        // Required empty public constructor
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment__banner, container, false);

        searchImage = view.findViewById(R.id.search_image);
        searchcard = view.findViewById(R.id.search);

        create = view.findViewById(R.id.create);
        usernametextview = view.findViewById(R.id.usernametext);
        notificationBadge = view.findViewById(R.id.badge_count);    /// contact request count
        notificationimage = view.findViewById(R.id.notification_image);  // contact request count
        notification = view.findViewById(R.id.notificationimage);     // notification
        notificationcount = view.findViewById(R.id.badgecount); // notification count
        sarvapahatext1 = view.findViewById(R.id.sarvapahatext1);
        sarvapahatext2 = view.findViewById(R.id.sarvapahatext2);
        sarvapahatext3 = view.findViewById(R.id.sarvapahatext3);
        sarvapahatext4 = view.findViewById(R.id.sarvapahatext4);
        thoughtview = view.findViewById(R.id.thoughtsviewLayout);
        thoughtimage1 = view.findViewById(R.id.thoughtimage1);
        thoughtimage2 = view.findViewById(R.id.thoughtimage2);
        thoughtimage3 = view.findViewById(R.id.thoughtimage3);
        thoughtimage4 = view.findViewById(R.id.thoughtimage4);
        thoughtimage5 = view.findViewById(R.id.thoughtimage5);
        greetingimage1 = view.findViewById(R.id.greetingimage1);
        greetingimage2 = view.findViewById(R.id.greetingimage2);
        greetingimage3 = view.findViewById(R.id.greetingimage3);
        greetingimage4 = view.findViewById(R.id.greetingimage4);
        greetingimage5 = view.findViewById(R.id.greetingimage5);
        businessimage1 = view.findViewById(R.id.businessimage1);
        businessimage2 = view.findViewById(R.id.businessimage2);
        businessimage3 = view.findViewById(R.id.businessimage3);
        businessimage4 = view.findViewById(R.id.businessimage4);
        businessimage5 = view.findViewById(R.id.businessimage5);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);

        tlayout = view.findViewById(R.id.todaylayout);
        ulayout = view.findViewById(R.id.upcominglayout);
        upcoming = view.findViewById(R.id.upcomingBtn);


        daysrecyclerview = view.findViewById(R.id.daysview);
        daysAdapter = new DaysAdapter(getContext(), Fragment_Banner.this, false);
        daysrecyclerview.setAdapter(daysAdapter);
// Set the LinearLayoutManager with horizontal orientation
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        daysrecyclerview.setLayoutManager(layout);

        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        businessBannerList = new ArrayList<>();
        RecyclerView businessrecyclerView = view.findViewById(R.id.businessview);
        bannerAdapter = new BusinessBannerAdapter( businessBannerList,sharedPreference, getContext(), Fragment_Banner.this, false);
        businessrecyclerView.setAdapter(bannerAdapter);
// Set the LinearLayoutManager with horizontal orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        businessrecyclerView.setLayoutManager(layoutManager);

        RecyclerView thoughtsrecyclerView = view.findViewById(R.id.thoughtsview);
        thoughtsAdapter = new ThoughtsAdapter(getContext(), Fragment_Banner.this, false);
        thoughtsrecyclerView.setAdapter(thoughtsAdapter);
// Set the LinearLayoutManager with horizontal orientation
        LinearLayoutManager layoutManagers = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        thoughtsrecyclerView.setLayoutManager(layoutManagers);

        RecyclerView dinvisheshrecyclerView = view.findViewById(R.id.dinvisheshview);
        dinvisheshAdapter = new DinvisheshAdapter(getContext(), Fragment_Banner.this, false);
        dinvisheshrecyclerView.setAdapter(dinvisheshAdapter);
        LinearLayoutManager layoutsManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        dinvisheshrecyclerView.setLayoutManager(layoutsManager);


        RecyclerView currentdatedinvisheshrecyclerView = view.findViewById(R.id.currenteventview);
        todayDinvisheshAdapter = new TodayDinvisheshAdapter(getContext(), Fragment_Banner.this, false);
        currentdatedinvisheshrecyclerView.setAdapter(todayDinvisheshAdapter);
        LinearLayoutManager layoutsManagers = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        currentdatedinvisheshrecyclerView.setLayoutManager(layoutsManagers);



        // Check if the fragment is ShopFragment
        if (this instanceof Fragment_Banner) {
            // Apply different sizes for ImageView and TextView in ShopFragment
            bannerAdapter.setShopFragment(true);
            thoughtsAdapter.setShopFragment(true);
            dinvisheshAdapter.setShopFragment(true);
            daysAdapter.setShopFragment(true);
            todayDinvisheshAdapter.setShopFragment(true);
        } else {
            // Apply default sizes for other fragments
            bannerAdapter.setShopFragment(false);
            thoughtsAdapter.setShopFragment(false);
            dinvisheshAdapter.setShopFragment(false);
            daysAdapter.setShopFragment(false);
            todayDinvisheshAdapter.setShopFragment(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.mainsecondcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(getContext(), R.color.mainsecondcolor));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }


        // Initialize Firebase
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Shop");
        userRef = database.getReference("Users");
        adref = FirebaseDatabase.getInstance().getReference("ads");
        Thoughtsref = FirebaseDatabase.getInstance().getReference("Thoughts");
        FestivalRef = FirebaseDatabase.getInstance().getReference("Festival");
        businessRef = FirebaseDatabase.getInstance().getReference("Business");
        daysRef = FirebaseDatabase.getInstance().getReference("Days");
        System.out.println("rgfgij " +daysRef);

        // Initialize with -1 to start from the first image

        // In your onCreate or wherever you initialize the app
        sharedPreferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        lastDisplayedIndex = sharedPreferences.getInt("lastDisplayedIndex", -1);


        // Read data from Firebase
       // festivalRetrieveCurrentImages();

        thoughtRetrieveImages(); //motivational banner
        //festivalRetrieveImages(); //upcoming events //laoding issue
        businessRetrieveImages(); // business banner
         //first one  in todays events days
        //CurrentEvents(); //todays events
        daysRetrieveImages();
        CurrentEvents();

        upcoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUpcomingEvents();
            }
        });


        databaseRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot shopSnapshot) {
                if (shopSnapshot.exists()) {

                   //  currentUsername = shopSnapshot.child("name").getValue(String.class);
                    currentUsershopName = shopSnapshot.child("shopName").getValue(String.class);
                    //Log.d("FirebaseData", "shopName: " + shopName);
                    currentUsercontactNumber = userId;
                    currentUseraddress = shopSnapshot.child("address").getValue(String.class);
                    currentUserShopimage = shopSnapshot.child("url").getValue(String.class);

                } else {
//                                    createFab.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot shopSnapshot) {
                if (shopSnapshot.exists()) {
                    currentUsername = shopSnapshot.child("name").getValue(String.class);
                    if (currentUsername != null && currentUsername.contains(" ")) {
                        String firstName = currentUsername.substring(0, currentUsername.indexOf(" "));
                        usernametextview.setText(firstName);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


        sarvapahatext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BannerCategory.class);
                String text = "Festival";
                intent.putExtra("titletext", text);
                intent.putExtra("contactNumber",userId);
                intent.putExtra("shopName", currentUsershopName);
                intent.putExtra("shopimage", currentUserShopimage);
                intent.putExtra("ownerName",currentUsername);
                intent.putExtra("shopaddress", currentUseraddress);
                System.out.println("sfvhfvv " +currentUseraddress);
                System.out.println("sfvhfvv " +currentUsername);
                startActivity(intent);
            }
        });

        sarvapahatext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BannerCategory.class);
                String text = "Greeting";
                intent.putExtra("titletext", text);
                startActivity(intent);
            }
        });

        sarvapahatext3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BannerCategory.class);
                String text = "Thoughts";
                intent.putExtra("titletext", text);
                intent.putExtra("contactNumber",userId);
                intent.putExtra("shopName", currentUsershopName);
                intent.putExtra("shopimage", currentUserShopimage);
                intent.putExtra("ownerName",currentUsername);
                intent.putExtra("shopaddress", currentUseraddress);
                System.out.println("sfvhfvv " +currentUseraddress);
                System.out.println("sfvhfvv " +currentUsername);
                startActivity(intent);
            }
        });

        sarvapahatext4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BannerCategory.class);
                String text = "Business";
                intent.putExtra("titletext", text);
                intent.putExtra("contactNumber",userId);
                intent.putExtra("shopName", currentUsershopName);
                intent.putExtra("shopimage", currentUserShopimage);
                intent.putExtra("ownerName",currentUsername);
                intent.putExtra("shopaddress", currentUseraddress);
                System.out.println("sfvhfvv " +currentUseraddress);
                System.out.println("sfvhfvv " +currentUsername);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void ClearAllBanner(){
        if(dinvisheshAdapter!=null){
            dinvisheshAdapter.notifyDataSetChanged();
        }
        //homeItemList=new ArrayList<>();
    }

    public void loadUpcomingEvents(){
        Log.d("ProgressDialog", "Initializing ProgressDialog");

        final Dialog bottomSheetView = new Dialog(getContext());
        bottomSheetView.setContentView(R.layout.upcomingevents);
        bottomSheetView.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        RecyclerView recyclerView=bottomSheetView.findViewById(R.id.dinvisheshview1);

        LinearLayoutManager layoutsManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutsManager);
        bottomSheetView.show();

        // Initialize ProgressDialog
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading Upcoming Events...");
        progressDialog.setCancelable(false); // Prevent dismissing by tapping outside

        Log.d("ProgressDialog", "Showing ProgressDialog");
        // Show ProgressDialog
        progressDialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Skip the current date
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        // Create an array to store the next four dates
        String[] dateArray = new String[6];
        for (int i = 0; i < dateArray.length; i++) {
            dateArray[i] = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
        }

        FestivalRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("ProgressDialog", "Data retrieval started");

                // Dismiss ProgressDialog when data retrieval is complete

                if (dataSnapshot.exists()) {
                    List<Event> events = new ArrayList<>();

                    for (String currentDate : dateArray) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();

                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                String dateKey = dataSnapshot1.getKey();
                                String dayMonthPart = dateKey.substring(0, 5);
                                int dateComparison = compareDates(currentDate, dayMonthPart);

                                if (dateComparison == 0) { // Dates match
                                    for (DataSnapshot keySnapshot : dataSnapshot1.getChildren()) {
                                        String titleKey = keySnapshot.getKey();
                                        FestivalRef.child(key).child(dateKey).child(titleKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    List<String> imageUrls = new ArrayList<>();

                                                    for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                                                        String imageUrl = imageSnapshot.getValue(String.class);
                                                        imageUrls.add(imageUrl);
                                                        System.out.println("Image URL: " + imageUrl);
                                                    }

                                                    // Construct the Event object for each event under the date
                                                    Event event = new Event(titleKey, dateKey, imageUrls);
                                                    events.add(event);
                                                }

                                                // Notify the adapter after processing each date and its events
                                                dinvisheshAdapter = new DinvisheshAdapter(getContext(), Fragment_Banner.this, false);
                                                recyclerView.setAdapter(dinvisheshAdapter);
                                                dinvisheshAdapter.setEvents(events);
                                                dinvisheshAdapter.setShopFragment(true);
                                                dinvisheshAdapter.notifyDataSetChanged();
                                                progressDialog.dismiss();
                                            }

                                            @Override
                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                                // Handle errors here
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.d("Firebase", "No images found for the selected dates");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Dismiss ProgressDialog in case of cancellation
                progressDialog.dismiss();

                Log.e("Firebase", "Error retrieving images: " + databaseError.getMessage());
            }
        });

    }



    private void thoughtRetrieveImages(){
        Thoughtsref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    List<String> keys = new ArrayList<>();
                    List<String> imageUrls = new ArrayList<>();

                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        System.out.println("wfed " + key);
                        keys.add(key);

                        // Retrieve only the image with key "0" for each node
                        DataSnapshot zeroImageSnapshot = childSnapshot.child("0");
                        String zeroImageUrl = zeroImageSnapshot.getValue(String.class);

                        // Add the URL to the list
                        imageUrls.add(zeroImageUrl);
                        System.out.println("sdkfj " + zeroImageUrl);
                    }

                    // Update the adapter with the new list of image URLs
                    thoughtsAdapter.setthoughtsnametexts(keys);
                    thoughtsAdapter.setImageUrls(imageUrls);
                    thoughtsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void festivalsretrieve() {
        DatabaseReference festRef = FirebaseDatabase.getInstance().getReference("Demo");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        List<Event> events = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String currentDate = sdf.format(calendar.getTime());

            festRef.child(currentDate).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                            String eventKey = eventSnapshot.getKey();
                            System.out.println("Event Key: " + eventKey);

                            List<String> imageUrls = new ArrayList<>();
                            for (DataSnapshot imageSnapshot : eventSnapshot.getChildren()) {
                                String imageUrl = imageSnapshot.getValue(String.class);
                                imageUrls.add(imageUrl);
                                System.out.println("Image URL: " + imageUrl);
                            }

                            // Construct the Event object for each event under the date
                            Event event = new Event(eventKey, currentDate, imageUrls);
                            events.add(event);
                        }
                    }

                    // If no events are found for the current date, move to the next day
                    if (events.isEmpty()) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        festivalsretrieve(); // Recursively call the method for the next day
                    } else {
                        // Notify the adapter after processing each date and its events
                        dinvisheshAdapter.setEvents(events);
                        dinvisheshAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Handle errors here
                }
            });

            // Move to the next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void festivalRetrieveCurrentImages() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM", Locale.getDefault());
        String currentDate = sdf.format(Calendar.getInstance().getTime());

        FestivalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Event> events = new ArrayList<>();

                    for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                        String dateKey = dateSnapshot.getKey();
                        String dayMonthPart = dateKey.substring(0, 5);
                        int dateComparison = compareDates(currentDate, dayMonthPart);

                        if (dateComparison == 0) { // Dates match
                            for (DataSnapshot titleSnapshot : dateSnapshot.getChildren()) {
                                String titleKey = titleSnapshot.getKey();

                                for (DataSnapshot imageSnapshot : titleSnapshot.getChildren()) {
                                    String imageUrl = imageSnapshot.getValue(String.class);

                                    if (imageUrl != null) {
                                        // Construct the Event object for each image
                                        Event event = new Event(titleKey, dateKey, Collections.singletonList(imageUrl));
                                        events.add(event);
                                        System.out.println("Image URL: " + imageUrl);
                                    }
                                }
                            }
                        }
                    }

                    // Notify the adapter after processing the current date and its events
                    todayDinvisheshAdapter.setEvents(events);
                    todayDinvisheshAdapter.notifyDataSetChanged();
                } else {
                    Log.d("Firebase", "No images found for the selected dates");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error retrieving images: " + databaseError.getMessage());
            }
        });
    }


    private void festivalRetrieveImages() { //Old Method

        lottieAnimationView.setVisibility(View.VISIBLE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

// Skip the current date
        calendar.add(Calendar.DAY_OF_MONTH, 1);

// Create an array to store the next four dates
        String[] dateArray = new String[7];
        for (int i = 0; i < dateArray.length; i++) {
            dateArray[i] = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
        }

        FestivalRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Event> events = new ArrayList<>();

                    for (String currentDate : dateArray) {

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();

                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                String dateKey = dataSnapshot1.getKey();
                                String dayMonthPart = dateKey.substring(0, 5);
                                int dateComparison = compareDates(currentDate, dayMonthPart);

                                if (dateComparison == 0) { // Dates match
                                    for (DataSnapshot keySnapshot : dataSnapshot1.getChildren()) {
                                        String titleKey = keySnapshot.getKey();
                                        FestivalRef.child(key).child(dateKey).child(titleKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    List<String> imageUrls = new ArrayList<>();

                                                    for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                                                        String imageUrl = imageSnapshot.getValue(String.class);
                                                        imageUrls.add(imageUrl);
                                                        System.out.println("Image URL: " + imageUrl);
                                                    }

                                                    // Construct the Event object for each event under the date
                                                    Event event = new Event(titleKey, dateKey, imageUrls);
                                                    events.add(event);
                                                }

                                                // Notify the adapter after processing each date and its events
                                                dinvisheshAdapter.setEvents(events);
                                                dinvisheshAdapter.notifyDataSetChanged();
                                                lottieAnimationView.setVisibility(View.GONE);

                                            }

                                            @Override
                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                                // Handle errors here
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.d("Firebase", "No images found for the selected dates");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error retrieving images: " + databaseError.getMessage());
            }
        });
    }




//    private void festivalRetrieveImages() { //Old Method
//       // lottieAnimationView.setVisibility(View.VISIBLE);
//        ClearAllBanner();
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM", Locale.getDefault());
//        Calendar calendar = Calendar.getInstance();
//
//        // Skip the current date
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//
//        // Create an array to store the next four dates
//        String[] dateArray = new String[7];
//        for (int i = 0; i < dateArray.length; i++) {
//            dateArray[i] = sdf.format(calendar.getTime());
//            calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
//        }
//
//        // Create a new thread for data retrieval
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // Perform data retrieval operations
//                // This will execute in a separate thread
//
//                FestivalRef.addListenerForSingleValueEvent(new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            List<Event> events = new ArrayList<>();
//
//                            for (String currentDate : dateArray) {
//
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    String key = snapshot.getKey();
//
//                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
//                                        String dateKey = dataSnapshot1.getKey();
//                                        String dayMonthPart = dateKey.substring(0, 5);
//                                        int dateComparison = compareDates(currentDate, dayMonthPart);
//
//                                        if (dateComparison == 0) { // Dates match
//                                            for (DataSnapshot keySnapshot : dataSnapshot1.getChildren()) {
//                                                String titleKey = keySnapshot.getKey();
//                                                FestivalRef.child(key).child(dateKey).child(titleKey).addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//                                                        if (snapshot.exists()) {
//                                                            List<String> imageUrls = new ArrayList<>();
//
//                                                            for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
//                                                                String imageUrl = imageSnapshot.getValue(String.class);
//                                                                imageUrls.add(imageUrl);
//                                                                System.out.println("Image URL: " + imageUrl);
//                                                            }
//
//                                                            // Construct the Event object for each event under the date
//                                                            Event event = new Event(titleKey, dateKey, imageUrls);
//                                                            events.add(event);
//                                                        }
//
//                                                        // Notify the adapter after processing each date and its events
//                                                        dinvisheshAdapter.setEvents(events);
//                                                        dinvisheshAdapter.notifyDataSetChanged();
//                                                       // lottieAnimationView.setVisibility(View.GONE);
//
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//                                                        // Handle errors here
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        } else {
//                            Log.d("Firebase", "No images found for the selected dates");
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.e("Firebase", "Error retrieving images: " + databaseError.getMessage());
//                    }
//                });
//            }
//        }).start();
//    }



    private void CurrentEvents() {
       // progressBarClass.load(getActivity(), true);
        ClearAllBanner();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Create an array to store current date and the next four dates
        String[] dateArray = new String[1];
        for (int i = 0; i < dateArray.length; i++) {
            dateArray[i] = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Move to the next day
        }


        FestivalRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<Event> events = new ArrayList<>();

                    for (String currentDate : dateArray) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String key = snapshot.getKey();

                            for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                String dateKey = dataSnapshot1.getKey();
                                String dayMonthPart = dateKey.substring(0, 5);
                                int dateComparison = compareDates(currentDate, dayMonthPart);

                                if (dateComparison == 0) { // Dates match
                                    for (DataSnapshot keySnapshot : dataSnapshot1.getChildren()) {
                                        String titleKey = keySnapshot.getKey();
                                        FestivalRef.child(key).child(dateKey).child(titleKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    List<String> imageUrls = new ArrayList<>();

                                                    for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                                                        String imageUrl = imageSnapshot.getValue(String.class);
                                                        imageUrls.add(imageUrl);
                                                        System.out.println("Image URL: " + imageUrl);
                                                    }

                                                    // Construct the Event object for each event under the date
                                                    Event event = new Event(titleKey, dateKey, imageUrls);
                                                    events.add(event);
                                                }

                                                // Notify the adapter after processing each date and its events
                                                todayDinvisheshAdapter.setEvents(events);
                                                todayDinvisheshAdapter.notifyDataSetChanged();
                                               // progressBarClass.load(getActivity(), false);

                                            }

                                            @Override
                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                                // Handle errors here
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Log.d("Firebase", "No images found for the selected dates");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error retrieving images: " + databaseError.getMessage());
            }
        });
    }

    private int compareDates(String date1, String date2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM", Locale.getDefault());
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


//    private void businessRetrieveImages(){
//        businessRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                if (snapshot.exists()) {
//                    List<String> keys = new ArrayList<>();
//                    List<String> imageUrls = new ArrayList<>();
//
//                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                        String key = childSnapshot.getKey();
//                        System.out.println("fejgn " + key);
//                        keys.add(key);
//
//                        // Retrieve only the image with key "0" for each node
//                        DataSnapshot zeroImageSnapshot = childSnapshot.child("0");
//                        String zeroImageUrl = zeroImageSnapshot.getValue(String.class);
//
//                        // Add the URL to the list
//                        imageUrls.add(zeroImageUrl);
//                        System.out.println("sdkfj " + zeroImageUrl);
//                    }
//
//                    // Update the adapter with the new list of image URLs
//                    bannerAdapter.setBusinessnametexts(keys);
//                    bannerAdapter.setImageUrls(imageUrls);
//                    bannerAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error
//            }
//        });
//    }

    private void businessRetrieveImages() {
        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("Business");
        adref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<BusinessBanner> favBusinessList = new ArrayList<>();
                    List<BusinessBanner> otherBusinessList = new ArrayList<>();

                    for (DataSnapshot businessSnapshot : snapshot.getChildren()) {
                        String businessName = businessSnapshot.child("name").getValue(String.class);

                        // Retrieve the image URL for the key "0"
                        String zeroImageUrl = businessSnapshot.child("images").child("0").getValue(String.class);

                        if (userId != null && businessName != null && !businessName.trim().isEmpty()) {
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    // Check if the user has a favorites node
                                    if (snapshot.child("FavBusiness").child(businessName).exists()) {
                                        Boolean isFav = snapshot.child("FavBusiness").child(businessName).getValue(Boolean.class);
                                        BusinessBanner businessBanner = new BusinessBanner(zeroImageUrl, businessName);
                                        businessBanner.setFav(isFav);

                                        // If it's a favorite, add it to the favBusinessList
                                        if (isFav) {
                                            favBusinessList.add(businessBanner);
                                        } else {
                                            // If it's not a favorite, add it to the otherBusinessList
                                            otherBusinessList.add(businessBanner);
                                        }
                                    } else {
                                        // The businessName key does not exist in the "FavBusiness" node
                                        System.out.println("Business is not a favorite.");
                                        BusinessBanner businessBanner = new BusinessBanner(zeroImageUrl, businessName);
                                        otherBusinessList.add(businessBanner);
                                    }

                                    // Notify the adapter after processing all businesses
                                    businessBannerList.clear();
                                    businessBannerList.addAll(favBusinessList);
                                    businessBannerList.addAll(otherBusinessList);
                                    bannerAdapter.notifyDataSetChanged();
                                    lottieAnimationView.setVisibility(View.GONE);
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

//    private void businessRetrieveImages() {
//        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("Business");
//        adref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    List<String> businessKeys = new ArrayList<>();
//                    List<String> businessNames = new ArrayList<>();
//                    List<List<String>> imageUrlsLists = new ArrayList<>();
//                    businessBannerList = new ArrayList<>();
//
//                    for (DataSnapshot businessSnapshot : snapshot.getChildren()) {
//                        String businessKey = businessSnapshot.getKey();
//                        businessKeys.add(businessKey);
//                        System.out.println("redgv " + businessKey);
//
//                        // Retrieve business name
//                        String businessName = businessSnapshot.child("name").getValue(String.class);
//                        System.out.println("sfcb " + businessName);
//                        businessNames.add(businessName);
//
//                        // Retrieve image URLs
//                        List<String> imageUrls = new ArrayList<>();
//                        DataSnapshot imagesSnapshot = businessSnapshot.child("images");
//
//                        for (DataSnapshot imageSnapshot : imagesSnapshot.getChildren()) {
//                            String imageUrl = imageSnapshot.getValue(String.class);
//                            System.out.println("dsxcv " + imageUrl);
//                            imageUrls.add(imageUrl);
//                        }
//                        imageUrlsLists.add(imageUrls);
//                        BusinessBanner businessBanner = new BusinessBanner(imageUrls, businessName);
//                        businessBannerList.add(businessBanner);
//                    }
//
//                    // Now you have lists of business keys, business names, and image URLs
//                    // Do whatever you need to do with these lists
//
//                    // Update your adapter after adding all items to the list
//                    bannerAdapter.setBusinessInfoList(businessBannerList);
//                    bannerAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled
//            }
//        });
//    }

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
                        System.out.println("rdgvc " +key);

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
        String[] daysOfWeek = {"शुभ रविवार", "शुभ सोमवार", "शुभ मंगळवार", "शुभ बुधवार", "शुभ गुरुवार", "शुभ शुक्रवार", "शुभ शनिवार"};
        return daysOfWeek[dayOfWeek - 1];
    }


    @Override
    public void onItemClick(int position, String imageUrl, String businessname) {
        Log.d("BannerAdapter", "Item clicked at position: " + position);
        Log.d("BannerAdapter", "Image URL: " + imageUrl);
        Log.d("BannerAdapter", "Business Name: " + businessname);



        Intent intent = new Intent(getContext(), BannerDetails.class);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("contactNumber",userId);
        intent.putExtra("shopName", currentUsershopName);
        intent.putExtra("shopimage", currentUserShopimage);
        intent.putExtra("ownerName", currentUsername);
        intent.putExtra("shopaddress", currentUseraddress);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("BUSINESS_NAME", businessname);
        System.out.println("sdfvd " +currentUseraddress);
        startActivity(intent);
    }

    @Override
    public void onfavClick(int position, ImageView favimageview, String businessName) {

    }

    @Override
    public void onThoughtClick(int position, String imageUrl, String thoughtsname) throws ExecutionException, InterruptedException {

        Log.d("BannerAdapter", "Item clicked at position: " + position);
        Log.d("BannerAdapter", "Image URL: " + imageUrl);
        Log.d("BannerAdapter", "Business Name: " + thoughtsname);



        Intent intent = new Intent(getContext(), BannerDetails.class);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("contactNumber",userId);
        intent.putExtra("shopName", currentUsershopName);
        intent.putExtra("shopimage", currentUserShopimage);
        intent.putExtra("ownerName", currentUsername);
        intent.putExtra("shopaddress", currentUseraddress);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("THOUGHTS_NAME", thoughtsname);
        System.out.println("sdfvd " +userId);
        startActivity(intent);

    }

    @Override
    public void onDinvisheshClick(int position, List<String> imageUrls, String dinvisheshname, String currentdate) throws ExecutionException, InterruptedException {

        String month = currentdate.substring(3,5);
        System.out.println("rgsasfdv " +month);
        Intent intent = new Intent(getContext(), BannerDetails.class);
        // Pass the list of image URLs
        intent.putStringArrayListExtra("IMAGE_URL", (ArrayList<String>) imageUrls);
        intent.putExtra("contactNumber",userId);
        intent.putExtra("shopName", currentUsershopName);
        intent.putExtra("shopimage", currentUserShopimage);
        intent.putExtra("ownerName", currentUsername);
        intent.putExtra("shopaddress", currentUseraddress);
        intent.putExtra("month", month);
        intent.putExtra("FESTIVAL_NAME", dinvisheshname);
        System.out.println("sdfvd " +currentUseraddress);
        startActivity(intent);
    }


    @Override
    public void onDaysClick(int position, String imageUrl, String businessname) throws ExecutionException, InterruptedException {
        Log.d("BannerAdapter", "Item clicked at position: " + position);
        Log.d("BannerAdapter", "Image saxzv: " + imageUrl);
        Log.d("BannerAdapter", "Business Name: " + businessname);



        Intent intent = new Intent(getContext(), BannerDetails.class);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("contactNumber",userId);
        intent.putExtra("shopName", currentUsershopName);
        intent.putExtra("shopimage", currentUserShopimage);
        intent.putExtra("ownerName", currentUsername);
        intent.putExtra("shopaddress", currentUseraddress);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("Days_NAME", businessname);
        System.out.println("sdfvd " +currentUseraddress);
        startActivity(intent);
    }
}

