package com.spark.swarajyabiz.ui;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nex3z.notificationbadge.NotificationBadge;
import com.spark.swarajyabiz.BuildConfig;
import com.spark.swarajyabiz.ItemList;
import com.spark.swarajyabiz.NotificationPage;
import com.spark.swarajyabiz.PromoteShop;
import com.spark.swarajyabiz.PromotedShops;
import com.spark.swarajyabiz.R;
import com.spark.swarajyabiz.Shop;
import com.spark.swarajyabiz.ShopAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import ru.nikartm.support.ImageBadgeView;

public class FragmentVerify extends Fragment {

    ImageView create, notificationimage, notification;
    private DatabaseReference databaseRef;
    private DatabaseReference userRef, adref;
    private ShopAdapter shopAdapter;
    private List<Shop> shopList;
    private List<Shop> filteredList;
    List<PromoteShop> promoteShopList;
    private RecyclerView recyclerViewShops;
    private android.widget.Spinner districtSpinner, talukaSpinner;
    private ImageView filterlist, adimage, adimagecancel;
    private CardView Spinner, cardspinner, availableshops;
    private ImageView Logout;
    private long pressedTime;
    private static final String PREF_RANDOM_ORDER = "random_order";
    private String userAddress;
    private String requestcount, adimageurl;
    TextView textViewRequestCount,promotecount, createprofiletext, editprofiletext, createcatalogtext;
    private boolean isTalukaSelected = false;
    private String selectedDistrict;
    private FirebaseAuth mAuth;;
    String selectedTaluka;
    String retrievedTaluka;
    String retrievedDistrict;
    private String contactNumber,currentImageUrl;
    String userId, usersId, currentuserId;
    private ImageBadgeView ibvIcon;
    private List<Shop> filteredShopList;
    NotificationBadge notificationBadge, notificationcount;
    private android.widget.Spinner subDivisionSpinner;
    FrameLayout frameLayout;


    private ArrayAdapter<String> subDivisionAdapter;
    private ArrayAdapter<String> districtAdapter;
    private ArrayAdapter<String> talukaAdapter;
    boolean spinnersVisible = false; // Keep track of spinner visibility status

    private boolean isEditMode = false;
    private boolean isCatalogMode = false;
    Boolean isAllFabsVisible;
    private FloatingActionButton createFab,editProfileFab,createCatalogFab, floating_Buttons;
    private SharedPreferences sharedPreferences;
    private static final String USER_ID_KEY = "userID";
    ImageView searchImage;
    CardView searchcard;
    private boolean imageShown = false;
    private AlertDialog dialog; // Declare the dialog variable
    private int lastDisplayedIndex = -1;


    public FragmentVerify() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        searchImage = view.findViewById(R.id.search_image);
        searchcard = view.findViewById(R.id.search);

        create = view.findViewById(R.id.create);
        recyclerViewShops = view.findViewById(R.id.viewdetails);
        districtSpinner = view.findViewById(R.id.districtSpinner);
        subDivisionSpinner = view.findViewById(R.id.talukaSpinner);
        filterlist = view.findViewById(R.id.filterlist);
        Spinner = view.findViewById(R.id.dspinner);
        cardspinner = view.findViewById(R.id.tspinner);
        Logout = view.findViewById(R.id.Logout);
        textViewRequestCount = view.findViewById(R.id.textViewRequestsCount);
        notificationBadge = view.findViewById(R.id.badge_count);    /// contact request count
        notificationimage = view.findViewById(R.id.notification_image);  // contact request count
        notification = view.findViewById(R.id.notificationimage);     // notification
        notificationcount = view.findViewById(R.id.badgecount); // notification count
        // notification.setVisibility(View.GONE);
//        notificationcount.setVisibility(View.GONE);
//        notificationimage.setVisibility(View.GONE);
        //ibvIcon = findViewById(R.id.ibv_icon1);
//        createFab = view.findViewById(R.id.create);
//        createFab.setVisibility(View.GONE);
//        availableshops = view.findViewById(R.id.availableshop);
//        availableshops.setVisibility(View.GONE);
//        adimage = view.findViewById(R.id.adimage);
//        adimagecancel = view.findViewById(R.id.adimagecancel);
//        frameLayout = view.findViewById(R.id.frameLayout);
        // Initialize RecyclerView
        recyclerViewShops.setHasFixedSize(true);
        recyclerViewShops.setLayoutManager(new LinearLayoutManager(getContext()));

        shopList = new ArrayList<>();
        filteredList = new ArrayList<>();
        shopAdapter = new ShopAdapter(filteredList, getContext(), this::onClick);
        recyclerViewShops.setAdapter(shopAdapter);

        // Initialize SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("MyPrefs", MODE_PRIVATE);

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchcard.getVisibility() == View.VISIBLE) {
                    searchcard.setVisibility(View.GONE); // If visible, hide it
                } else {
                    searchcard.setVisibility(View.VISIBLE); // If hidden, make it visible
                }
            }
        });

        // Initialize views
        frameLayout = view.findViewById(R.id.frameLayout);

        // Check if the image has been shown before
        imageShown = sharedPreferences.getBoolean("image_shown", false);

        if (!imageShown) {
            // Show the image alert dialog
            //showImageAlertDialog();
        }


        // Initialize Firebase
        FirebaseApp.initializeApp(getContext());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Shop");
        userRef = database.getReference("Users");
        adref = FirebaseDatabase.getInstance().getReference("ads");

        // Initialize with -1 to start from the first image

        // In your onCreate or wherever you initialize the app
        sharedPreferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        lastDisplayedIndex = sharedPreferences.getInt("lastDisplayedIndex", -1);
        // Check if it's the first-time app launch
        SharedPreferences sharedPreferencess = getActivity().getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        boolean isFirstLaunch = sharedPreferencess.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            // It's the first-time app launch, display the image
            displayImageForFirstLaunch();
            // Set the flag to indicate that the image has been displayed
//            SharedPreferences.Editor editor = sharedPreferencess.edit();
//            editor.putBoolean("isFirstLaunch", false);
//            editor.apply();
        } else {
            // Inside your method to display the image
            adref.child("images").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        List<String> imageUrls = new ArrayList<>();

                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String imageUrl = childSnapshot.getValue(String.class);
                            if (imageUrl != null) {
                                imageUrls.add(imageUrl);
                            }
                        }

                        // Ensure there are images to display
                        if (!imageUrls.isEmpty()) {
                            // Increment the index for the next time
                            lastDisplayedIndex++;
                            if (lastDisplayedIndex >= imageUrls.size()) {
                                // If we reached the end, reset to 0
                                lastDisplayedIndex = 0;
                            }

                            // Retrieve the URL for the current index
                            currentImageUrl = imageUrls.get(lastDisplayedIndex);

                            // Create and show the image alert dialog for the current image URL
                            //showImageAlertDialog(currentImageUrl);

                            // Save the last displayed index to SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("lastDisplayedIndex", lastDisplayedIndex);
                            editor.apply();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
        }

        // Read data from Firebase
        readDataFromFirebase();

        Spinner districtSpinner = view.findViewById(R.id.districtSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.districts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtSpinner.setAdapter(adapter);

        subDivisionAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        subDivisionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subDivisionSpinner.setAdapter(subDivisionAdapter);

        // Initialize the taluka spinner
        ArrayAdapter<String> talukaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        subDivisionSpinner.setAdapter(talukaAdapter);


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //  userId = getIntent().getStringExtra("userID");
        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            userRef.child(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }


        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                updateRequestCount(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                updateRequestCount(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                updateRequestCount(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Not used in this case, but you can implement if needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(getContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }

            private void updateRequestCount(DataSnapshot dataSnapshot) {
                String userid = dataSnapshot.child("contactNumber").getValue(String.class);
                if (userid != null && userid.equals(userId)) {
                    // Current user found
                    // Retrieve the requestCount value using the correct key "requestCount"
                    Integer requestCount = dataSnapshot.child("requestcount").getValue(Integer.class);
                    Integer noticationcount = dataSnapshot.child("notificationcount").getValue(Integer.class);
                    System.out.println("sdfsv "+noticationcount);

                    if (requestCount != null) {
                        if (requestCount > 0) {
                            // Show the badge count and update it with the request count
                            //    notificationimage.setVisibility(View.VISIBLE);
                            //  notificationBadge.setVisibility(View.VISIBLE);
                            // notificationBadge.setText(requestCount.toString());
                        } else {
                            // Hide the badge count when the request count is zero
                            //   notificationimage.setVisibility(View.VISIBLE);
                            //    notificationBadge.setVisibility(View.VISIBLE);
                        }
                    }
                    if (noticationcount != null) {
                        if (noticationcount > 0) {
                            // Show the badge count and update it with the request count
                            //   notification.setVisibility(View.VISIBLE);
                            //  notificationcount.setVisibility(View.VISIBLE);
                            notificationcount.setText(noticationcount.toString());
                        } else {
                            // Hide the badge count when the request count is zero
                            //  notification.setVisibility(View.VISIBLE);
                            notificationcount.setVisibility(View.GONE);
                        }
                    }


                    if (requestCount != null && requestCount > 0) {
                        // The request count is greater than zero, show the TextView
                        textViewRequestCount.setText("Request count :" + String.valueOf(requestCount));
                        //textViewRequestCount.setVisibility(View.VISIBLE);
                        textViewRequestCount.setTextColor(getResources().getColor(R.color.open_green)); // Using color resource

                    } else {
                        // The request count is zero or less, hide the TextView
                        // textViewRequestCount.setVisibility(View.GONE);
                    }

                    // Retrieve other shop details as needed (contactNumber, userAddress, etc.)
                    String contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    String userAddress = dataSnapshot.child("address").getValue(String.class);
                    System.out.println(userAddress);
                    // Compare user address with spinner districts and set the matching district
                    compareAddressWithDistricts(userAddress);
                }
            }


        });
//
//        notificationimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ContactRequests.class));
//            }
//        });
//
//        notificationBadge.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), ContactRequests.class));
//            }
//        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent businessIntent = new Intent(getContext(), NotificationPage.class);
                businessIntent.putExtra("mobileNumber", contactNumber);
            }
        });

        notificationcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent businessIntent = new Intent(getContext(), NotificationPage.class);
                businessIntent.putExtra("mobileNumber", contactNumber);
            }
        });
//
//
//        // correct answer
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        System.out.println("rrgvfg " +dataSnapshot1.getKey());

                        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    for (DataSnapshot shopsnapshot : snapshot.getChildren()){

                                        if (dataSnapshot1.getKey().equals(shopsnapshot.getKey())){
                                            databaseRef.child(shopsnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()){
                                                        String name = shopsnapshot.child("name").getValue(String.class);
                                                        System.out.println("dsfg " +name);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                                }
                                            });


                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        if (userId != null) {
            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        contactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                        System.out.println("sdfvv " + contactNumber);
                        setdistrctandtaluka(contactNumber);

                        databaseRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot shopSnapshot) {
                                if (!shopSnapshot.exists()) {
                                    //          createFab.setVisibility(View.VISIBLE);
                                } else {
//                                    createFab.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });
        }

//        createFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open EditProfile page
//                startActivity(new Intent(getApplicationContext(), Create_Profile.class));
//                // Implement your code to navigate to EditProfile page
//            }
//        });
//
        SearchView searchView = view.findViewById(R.id.searchview);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false); // Open the SearchView for search
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
        // Set an OnFocusChangeListener to the SearchView
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // Open the keyboard when SearchView gains focus
                    showKeyboard(searchView);
                } else {
                    // Clear and close the SearchView when focus is lost (keyboard is dismissed)
                    searchView.setQuery("", false);
                    searchView.clearFocus();
                }
            }
        });
//
//        Logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                PopupMenu popupMenu = new PopupMenu(getContext(), view);
//                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());
//
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem menuItem) {
//                        switch (menuItem.getItemId()) {
////                            case R.id.menu_logout:
////                                // Handle logout action
////                                // Add your logout logic here
////                                FirebaseAuth.getInstance().signOut();
////
////                                // Start the FirstPage activity and clear the task stack
////                                Intent intent = new Intent(Business.this, LoginActvity.class);
////                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
////                                startActivity(intent);
////                                return true;
////                            default:
////                                return false;
//
//                            case R.id.menu_Profile:
//                                // Start the FirstPage activity and clear the task stack
//                                Intent intents = new Intent(getContext(), Profile.class);
//                                startActivity(intents);
//                                return true;
//                            default:
//                                return false;
//
//                        }
//                    }
//                });
//
//                popupMenu.show();
//            }
//        });
//
//        create.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), Create_Profile.class));
//            }
//        });
//
//
//        // Check if the random order is saved in SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        if (sharedPreferences.contains(PREF_RANDOM_ORDER)) {
            // Retrieve the random order from SharedPreferences
            String jsonOrder = sharedPreferences.getString(PREF_RANDOM_ORDER, null);
            if (jsonOrder != null) {
                // Convert the JSON string to a list of Shop objects
                shopList = convertJsonToList(jsonOrder);
                filteredList.addAll(shopList);
                shopAdapter.notifyDataSetChanged();
            }
        } else {
            // Fetch data from Firebase and save the random order
            fetchShopData();
        }



        Spinner.setVisibility(View.GONE);
        cardspinner.setVisibility(View.GONE);


        filterlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!spinnersVisible) {
                    Spinner.setVisibility(View.VISIBLE);
                    cardspinner.setVisibility(View.VISIBLE);


                    String selectedDistrict = districtSpinner.getSelectedItem().toString();
                    updateTalukaSpinner(selectedDistrict);

                    // Apply filtering based on selected district and taluka
                    String selectedTaluka = subDivisionSpinner.getSelectedItem().toString();
                    filterShopList(selectedDistrict, selectedTaluka);

                    spinnersVisible = true; // Update the visibility status
                } else {
                    Spinner.setVisibility(View.GONE);
                    cardspinner.setVisibility(View.GONE);

                    // Clear filters and show the entire shop list
                    filteredList.clear();
                    filteredList.addAll(shopList);
                    shopAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed

                    spinnersVisible = false; // Update the visibility status
                }
            }
        });

        // Shuffle the data randomly
        shuffleDataRandomly();

        RecyclerView recyclerView = view.findViewById(R.id.viewdetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(shopAdapter);

        districtAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        districtSpinner.setAdapter(districtAdapter);

        talukaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item);
        subDivisionSpinner.setAdapter(talukaAdapter);

        // ... retrieve user data and set district and taluka

        // Set up listeners for spinners
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedDistrict = adapterView.getItemAtPosition(position).toString();
                // Check if the selected item is "Select District"
                if (selectedDistrict.equals("Select District")) {
                    // Set the Taluka spinner to "Select Taluka"
                    ArrayAdapter<String> talukaAdapter = (ArrayAdapter<String>) subDivisionSpinner.getAdapter();
                    int selectTalukaPosition = talukaAdapter.getPosition("Select Taluka");
                    subDivisionSpinner.setSelection(selectTalukaPosition);

                    // Show all shop list without filters
                    filterShopList("Select District", "Select Taluka");
                } else {
                    // Update the Taluka spinner when the district changes and Spinner is visible
                    updateTalukaSpinner(selectedDistrict);

                    // Apply filtering based on selected district and taluka
                    //String selectedTaluka = subDivisionSpinner.getSelectedItem().toString();
                    filterShopList(selectedDistrict, selectedTaluka);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        subDivisionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedTaluka = adapterView.getItemAtPosition(position).toString();
                String selectedDistrict = districtSpinner.getSelectedItem().toString();

                if (!selectedDistrict.equals("Select District")) {
                    // Apply filtering based on selected district and taluka
                    filterShopList(selectedDistrict, selectedTaluka);
                }
//                setTalukaOnSpinner(selectedTaluka);
//                filterShopList(selectedDistrict, selectedTaluka);
                //filterShopList(districtSpinner.getSelectedItem().toString(), selectedTaluka);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

        promoteShopList = new ArrayList<>();


        readDataFromFirebase();
        return view;
    }

    //
    private void setdistrctandtaluka(String contactNumber) {
        Log.d("ContactNumberss", "" + contactNumber);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot datasnapshot) {
                if (datasnapshot.exists()) {
                    for (DataSnapshot shopSnapshot : datasnapshot.getChildren()) {
                        String userid = shopSnapshot.child("contactNumber").getValue(String.class);
                        Log.d("userID", "" + userid);
                        if (userid != null && userid.equals(userId)) {
                            String taluka = shopSnapshot.child("taluka").getValue(String.class);
                            String district = shopSnapshot.child("district").getValue(String.class);
                            Log.d("talukass", "" + taluka);
                            Log.d("districts", "" + district);

                            if (taluka != null && district != null) {
                                retrievedDistrict = district;
                                retrievedTaluka = taluka;
                                Log.d("retrievedDistrict", "" + retrievedDistrict);
                                Log.d("retrievedTaluka", "" + retrievedTaluka);


                                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                                        getContext(),
                                        R.array.districts_array,
                                        android.R.layout.simple_spinner_item
                                );
                                districtSpinner.setAdapter(adapter);

                                updateTalukaSpinner(retrievedDistrict);

                                int districtPosition = adapter.getPosition(retrievedDistrict);
                                districtSpinner.setSelection(districtPosition);

                                String[] subDivisions = getResources().getStringArray(
                                        getResources().getIdentifier(
                                                "Taluka_array_" + retrievedDistrict.toLowerCase(),
                                                "array",
                                                requireContext().getPackageName()
                                        )
                                );
                                ArrayAdapter<String> talukaAdapter = new ArrayAdapter<>(
                                        getContext(),
                                        android.R.layout.simple_spinner_item,
                                        subDivisions
                                );
                                subDivisionSpinner.setAdapter(talukaAdapter);
                                Log.d("talukaAdapter", "" + talukaAdapter);
                                setTalukaOnSpinner(taluka);

                                //updateTalukaSpinner(retrievedDistrict);
                                //setTalukaOnSpinner(retrievedTaluka);
                                filterShopList(retrievedDistrict, retrievedTaluka);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void setTalukaOnSpinner(String taluka) {
        ArrayAdapter<String> talukaAdapter = (ArrayAdapter<String>) subDivisionSpinner.getAdapter();

        int talukaPosition = talukaAdapter.getPosition(taluka);
        //talukaAdapter.clear();
        //talukaAdapter.addAll(subDivisions);

        if (talukaPosition != -1) {
            subDivisionSpinner.setAdapter(talukaAdapter);
            subDivisionSpinner.setSelection(talukaPosition, true);
            Log.d("Taluka Set", "Taluka " + taluka + " set on the spinner.");
        } else {
            Log.d("Taluka Not Found", "Taluka " + taluka + " not found in the adapter.");
        }
        talukaAdapter.notifyDataSetChanged();
    }


    private void updateTalukaSpinner(String selectedDistrict) {
        // Construct the resource identifier for the Taluka array based on the selected district
        String talukaArrayIdentifier = "Taluka_array_" + selectedDistrict.toLowerCase();

        // Get the resource ID of the Taluka array
        int talukaArrayResourceId = getResources().getIdentifier(
                talukaArrayIdentifier,
                "array",
                requireContext().getPackageName()
        );

        // Check if the resource ID is valid
        if (talukaArrayResourceId != 0) {
            String[] subDivisions = getResources().getStringArray(talukaArrayResourceId);

            ArrayAdapter<String> newTalukaAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    subDivisions
            );
            subDivisionSpinner.setAdapter(newTalukaAdapter);

            // Find the position of the retrieved taluka in the adapter
            int talukaPosition = newTalukaAdapter.getPosition(retrievedTaluka);
            if (talukaPosition != -1) {
                subDivisionSpinner.setSelection(talukaPosition);
            } else {
                Log.d("Taluka Not Found", "Taluka " + retrievedTaluka + " not found in the adapter.");
            }
        } else {
            // Handle the case when the resource for the selected district is not found
            Log.e("Taluka Resource", "Resource not found for " + talukaArrayIdentifier);
        }
    }


    private void filterShopList(String selectedDistrict, String selectedTaluka) {
        filteredList.clear();

        if (spinnersVisible) { // Only apply filtering if spinners are visible
            if (selectedDistrict.equals("Select District")) {
                // Show the entire shop list without filter
                filteredList.addAll(shopList);
            } else {
                // Apply filters based on the selected district
                for (Shop shop : shopList) {
                    if (shop.getDistrict().equals(selectedDistrict)) {
                        // Include the selected taluka filter here if needed
                        if (selectedTaluka == null || selectedTaluka.equals("Select Taluka") || shop.getTaluka().equals(selectedTaluka)) {
                            filteredList.add(shop);
                        }
                    }
                }
            }
        } else {
            // If spinners are not visible, show the entire shop list
            filteredList.addAll(shopList);
        }

        shopAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
    }

//    private void filterShopList(String selectedDistrict, String selectedTaluka) {
//        filteredList.clear();
//
//        if (selectedDistrict.equals("Select District") ) {
//            // Show the entire shop list without filter
//            filteredList.addAll(shopList);
//        } else {
//            // Apply filters based on the selected district
//            for (Shop shop : shopList) {
//                if (shop.getDistrict().equals(selectedDistrict)) {
//                    // Include the selected taluka filter here if needed
//                    if (selectedTaluka == null || selectedTaluka.equals("Select Taluka") || shop.getTaluka().equals(selectedTaluka)) {
//                        filteredList.add(shop);
//                    }
//                }
//            }
//        }
//
//        shopAdapter.notifyDataSetChanged(); // Notify the adapter that data has changed
//    }




    private void compareAddressWithDistricts(String userAddress) {
        if (this.userAddress != null) {
            int districtPosition = -1;
            String[] districts = getResources().getStringArray(R.array.districts_array);

            for (int i = 0; i < districts.length; i++) {
                if (this.userAddress.contains(districts[i])) {
                    districtPosition = i;
                    break;
                }
            }

            if (districtPosition != -1) {
                // Set the matching district on the spinner
                districtSpinner.setSelection(districtPosition);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Shuffle the data randomly when the activity resumes
        //   setdistrctandtaluka(contactNumber);
        shuffleDataRandomly();
    }


    private void shuffleDataRandomly() {
        Collections.shuffle(filteredList);
        shopAdapter.notifyDataSetChanged();
    }


    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    //
//
//
    private void fetchShopData() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                shopList.clear();
                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {

                    Boolean profileverify = shopSnapshot.child("profileverified").getValue(Boolean.class);
                    // long promotedShopCount = shopSnapshot.child("promotedShops").getChildrenCount();
                    System.out.println("sdfdf " + profileverify);
                    // Check if profile is verified (true) before adding to the list
                    if (profileverify != null && profileverify) {
                        String name = shopSnapshot.child("name").getValue(String.class);
                        String shopName = shopSnapshot.child("shopName").getValue(String.class);
                        //Log.d("FirebaseData", "shopName: " + shopName);
                        String contactNumber = shopSnapshot.child("contactNumber").getValue(String.class);
                        String address = shopSnapshot.child("address").getValue(String.class);
                        String url = shopSnapshot.child("url").getValue(String.class);
                        String service = shopSnapshot.child("service").getValue(String.class);
                        String taluka = shopSnapshot.child("taluka").getValue(String.class);
                        String district = shopSnapshot.child("district").getValue(String.class);
                        String shopcategory = shopSnapshot.child("shopcategory").getValue(String.class);
                        int ordercount = shopSnapshot.child("promotionCount").getValue(Integer.class);
                        int requestcount = shopSnapshot.child("promotionCount").getValue(Integer.class);

                        // Retrieve the count of promoted shops
                        int promotedShopCount = shopSnapshot.child("promotionCount").getValue(Integer.class);
                        Log.d("TAG", "proc " + contactNumber);

                        List<ItemList> itemList = new ArrayList<>();
                        // Retrieve posts for the current shop
                        DataSnapshot postsSnapshot = shopSnapshot.child("items");
                        for (DataSnapshot itemSnapshot : postsSnapshot.getChildren()) {
                            String itemkey = itemSnapshot.getKey();

                            String itemName = itemSnapshot.child("itemname").getValue(String.class);
                            String price = itemSnapshot.child("price").getValue(String.class);
                            String sellprice = itemSnapshot.child("sell").getValue(String.class);
                            String offer = itemSnapshot.child("offer").getValue(String.class);
                            String description = itemSnapshot.child("description").getValue(String.class);
                            String firstimage = itemSnapshot.child("firstImageUrl").getValue(String.class);
                            System.out.println("jfhv " +firstimage);

                            if (TextUtils.isEmpty(firstimage)) {
                                // Set a default image URL here
                                firstimage = String.valueOf(R.drawable.ic_outline_shopping_bag_24);
                            }

                            List<String> imageUrls = new ArrayList<>();
                            DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
                            for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                String imageUrl = imageUrlSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                }
                            }

                            ItemList item = new ItemList(shopName,url,contactNumber, itemName, price, sellprice,
                                    description, firstimage, itemkey, imageUrls, district,taluka,address, offer);
                            itemList.add(item);
                        }


                        // Create a Shop object and add it to the shop list
                        Shop shop = new Shop(name, shopName, contactNumber, address, url, service, district,
                                taluka, promotedShopCount, itemList, ordercount, requestcount, shopcategory);
                        shopList.add(shop);
                    }
                }


                // Save the random order to SharedPreferences
//                String jsonOrder = convertListToJson(shopList);
//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
//                sharedPreferences.edit().putString(PREF_RANDOM_ORDER, jsonOrder).apply();

                // Update the filtered list with the fetched data
                filteredList.clear();
                filteredList.addAll(shopList);

                // Notify the adapter about the data change
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String convertListToJson(List<Shop> shopList) {
        Gson gson = new Gson();
        return gson.toJson(shopList);
    }

    private List<Shop> convertJsonToList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<Shop>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private void filter(String query) {
        filteredList.clear();

        for (Shop shop : shopList) {

            if (shop == null || shop.getItemList() == null) {
                continue; // Skip null shops or shops with null item lists
            }

            boolean shopAdded = false;
            // Retrieve the values of different fields from the Shop object
            String name = shop.getName().toLowerCase();
            String shopName = shop.getShopName().toLowerCase();
            String address = shop.getAddress().toLowerCase();
//            String service = shop.getService().toLowerCase();
            String district = shop.getDistrict();
            String taluka = shop.getTaluka();

            // Check if any field contains the search query
            if (name.contains(query.toLowerCase())
                    || shopName.contains(query.toLowerCase())
                    || address.contains(query.toLowerCase())
                    || district.contains(query)
                    || taluka.contains(query)) {
                filteredList.add(shop);
                shopAdded = true;
            }

            for (ItemList item : shop.getItemList()) {
                if (item == null) {
                    continue; // Skip null items
                }

                String itemName = item.getName().toLowerCase();
                String description = item.getDescription().toLowerCase();

                // Check if the item name or description contains the search query
                if (itemName.contains(query.toLowerCase()) || description.contains(query.toLowerCase())) {
                    if (!shopAdded) {
                        filteredList.add(shop);
                        shopAdded = true;
                    }
                    break; // Add the shop once if any item matches
                }
            }
        }

        shopAdapter.notifyDataSetChanged();
    }
//

    private void readDataFromFirebase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing shop list
                shopList.clear();

                // Process the retrieved data
                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {

                    Boolean profileverify = shopSnapshot.child("profileverified").getValue(Boolean.class);
                    // long promotedShopCount = shopSnapshot.child("promotedShops").getChildrenCount();
                    System.out.println("sdfdf " + profileverify);
                    // Check if profile is verified (true) before adding to the list
                    if (profileverify != null && profileverify) {
                        String name = shopSnapshot.child("name").getValue(String.class);
                        String shopName = shopSnapshot.child("shopName").getValue(String.class);
                        //Log.d("FirebaseData", "shopName: " + shopName);
                        contactNumber = shopSnapshot.child("contactNumber").getValue(String.class);
                        String address = shopSnapshot.child("address").getValue(String.class);
                        String url = shopSnapshot.child("url").getValue(String.class);
                        String service = shopSnapshot.child("service").getValue(String.class);
                        String taluka = shopSnapshot.child("taluka").getValue(String.class);
                        String district = shopSnapshot.child("district").getValue(String.class);
                        String shopcategory = shopSnapshot.child("shopcategory").getValue(String.class);
                        int ordercount = shopSnapshot.child("promotionCount").getValue(Integer.class);
                        int requestcount = shopSnapshot.child("promotionCount").getValue(Integer.class);

                        // Retrieve the count of promoted shops
                        int promotedShopCount = shopSnapshot.child("promotionCount").getValue(Integer.class);
                        Log.d("TAG", "proc " + contactNumber);

                        List<ItemList> itemList = new ArrayList<>();

                        // Retrieve posts for the current shop
                        DataSnapshot postsSnapshot = shopSnapshot.child("items");
                        for (DataSnapshot itemSnapshot : postsSnapshot.getChildren()) {
                            String itemkey = itemSnapshot.getKey();

                            String itemName = itemSnapshot.child("itemname").getValue(String.class);
                            String price = itemSnapshot.child("price").getValue(String.class);
                            String sellprice = itemSnapshot.child("sell").getValue(String.class);
                            String offer = itemSnapshot.child("offer").getValue(String.class);
                            String description = itemSnapshot.child("description").getValue(String.class);
                            String firstimage = itemSnapshot.child("firstImageUrl").getValue(String.class);
                            System.out.println("jfhv " +firstimage);

                            if (TextUtils.isEmpty(firstimage)) {
                                // Set a default image URL here
                                firstimage = String.valueOf(R.drawable.ic_outline_shopping_bag_24);
                            }

                            List<String> imageUrls = new ArrayList<>();
                            DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
                            for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                String imageUrl = imageUrlSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                }
                            }

                            ItemList item = new ItemList(shopName,url,contactNumber, itemName, price, sellprice,
                                    description, firstimage, itemkey, imageUrls, district,taluka,address, offer);
                            itemList.add(item);
                        }


                        // Create a Shop object and add it to the shop list
                        Shop shop = new Shop(name, shopName, contactNumber, address, url, service, district,
                                taluka, promotedShopCount, itemList, ordercount, requestcount, shopcategory);
                        shopList.add(shop);
                    }
                }

                // Update the filtered list with the original list
                filteredList.clear();
                filteredList.addAll(shopList);
                if (filteredList.isEmpty()) {
                    availableshops.setVisibility(View.GONE); // No shops available
                } else {
//                    availableshops.setVisibility(View.VISIBLE); // Shops are available
                }
                // Notify the adapter about the data change
                shopAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.e("FirebaseError", "Failed to read value: " + databaseError.getMessage());
            }
        });
    }

    private void displayImageForFirstLaunch() {
        // Display the image for the first-time app launch
        // You can set your image source or show an AlertDialog here

        // Once the image is displayed, update the flag to indicate it's not the first launch
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstLaunch", false);
        editor.apply();
    }


    private void showImageAlertDialog(String imageUrl) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        if (!imageShown) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            // Inflate the custom layout for the dialog
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.ads_alertdialog, null);

            // Set the ImageView in the dialog to display the image
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            ImageView imageView = dialogView.findViewById(R.id.adsimageview);
            // Cancel button
            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            ImageView btnCancel = dialogView.findViewById(R.id.cancelimageview);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            TextView nametext = dialogView.findViewById(R.id.headertextview);

            @SuppressLint({"MissingInflatedId", "LocalSuppress"})
            LinearLayout share = dialogView.findViewById(R.id.share);

            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        String name = snapshot.child("name").getValue(String.class);
                        nametext.setText(name);
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                }
            });

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Check if the image URL is valid
                    if (isValidImageUrl(imageUrl)) {
                        String appUrl = "https://play.google.com/store/apps/details?id=com.spark.swarajyabiz&hl=en-IN"; // Replace with your app's actual Play Store URL
                        String message = "Check this app on Playstore: " + appUrl;
                        // Download the image to local storage
                        downloadImageAndShare(imageUrl, message);
                    } else {
                        // Handle the case where the image URL is not valid
                        Toast.makeText(getContext(), "Invalid image URL", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Dismiss the image and update the flag
                    frameLayout.setVisibility(View.GONE);
                    imageShown = true;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("image_shown", true);
                    editor.apply();
                    frameLayout.setVisibility(View.GONE);
                    dialog.dismiss(); // Close the dialog only when the cancel button is clicked
                }
            });

            if (imageUrl != null) {
                //frameLayout.setVisibility(View.VISIBLE);

                // Use Glide to load and display the image from the URL
                Glide.with(getContext())
                        .load(imageUrl)
                        .into(imageView);

//                adimagecancel.setNextFocusUpId(R.id.btnCancel);
            }

            builder.setView(dialogView);

            dialog = builder.create(); // Initialize the dialog variable
            dialog.setCanceledOnTouchOutside(false); // Disable dismissing the dialog by clicking outside
            dialog.show();
        }
    }

    private boolean isValidImageUrl(String imageUrl) {
        // Implement your validation logic here
        // Check if the URL is a valid image URL
        // You can use libraries like Picasso or Glide to validate the URL
        // Return true if it's a valid image URL, otherwise return false
        // You may also check if the file format is supported
        // and if the image exists on the server
        return true;
    }

    private void downloadImageAndShare(String imageUrl, final String message) {
        Glide.with(getContext())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        // Save the bitmap to local storage (e.g., app's cache directory)
                        // Then, share the locally saved image using an Intent

                        // Example code for saving to local storage
                        // Save the bitmap to a file

                        File cachePath = new File(requireContext().getCacheDir(), "images");
                        cachePath.mkdirs();
                        FileOutputStream stream;
                        try {
                            File imageFile = new File(cachePath, "image.jpg");
                            stream = new FileOutputStream(imageFile);
                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            stream.flush();
                            stream.close();

                            // Share the locally saved image with WhatsApp
                            shareImageWithWhatsApp(imageFile, message);
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

    private void shareImageWithWhatsApp(File imageFile, String message) {
        Uri imageUri = FileProvider.getUriForFile(
                getContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                imageFile
        );

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setPackage("com.whatsapp"); // Restrict to WhatsApp
        startActivity(intent);
    }

    private void onClick(int position) {
        PromoteShop promoteShop = promoteShopList.get(position);
        String contactNumber = promoteShop.getContactNumber();
        Intent promoteshopIntent = new Intent(getApplicationContext(), PromotedShops.class);
        startActivity(promoteshopIntent);
    }

}
