package com.spark.swarajyabiz.ui;

import static android.content.Context.MODE_PRIVATE;

import static com.facebook.FacebookSdk.getCacheDir;
import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.CategoryAdapter;
import com.spark.swarajyabiz.Adapters.DataFetcher;
import com.spark.swarajyabiz.Adapters.HomeMultiAdapter;
import com.spark.swarajyabiz.Adapters.StringSplit;
import com.spark.swarajyabiz.Adapters.SubCateAdapter;
import com.spark.swarajyabiz.BannerDetails;
import com.spark.swarajyabiz.BuildConfig;
import com.spark.swarajyabiz.Business;
import com.spark.swarajyabiz.ChatJob;
import com.spark.swarajyabiz.EmployeeAdapter;
import com.spark.swarajyabiz.EmployeeDetails;
import com.spark.swarajyabiz.ImageAdapter;
import com.spark.swarajyabiz.ItemDetails;
import com.spark.swarajyabiz.ItemList;
import com.spark.swarajyabiz.JobChat;
import com.spark.swarajyabiz.JobDetails;
import com.spark.swarajyabiz.JobPostAdapter;
import com.spark.swarajyabiz.JobPostDetails;

import com.spark.swarajyabiz.ModelClasses.CategoryModel;
import com.spark.swarajyabiz.ModelClasses.OrderModel;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.ModelClasses.SubCategoryModel;
import com.spark.swarajyabiz.PlaceOrder;
import com.spark.swarajyabiz.Post;
import com.spark.swarajyabiz.PostAdapter;
import com.spark.swarajyabiz.PostInfo;
import com.spark.swarajyabiz.PremiumMembership;
import com.spark.swarajyabiz.ProgressBarClass;
import com.spark.swarajyabiz.R;
import com.spark.swarajyabiz.Shop;
import com.spark.swarajyabiz.ShopDetails;
import com.spark.swarajyabiz.ShowAllItemsList;
import com.spark.swarajyabiz.ViewPagerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import io.reactivex.rxjava3.annotations.NonNull;

public class FragmentHome extends Fragment implements JobPostAdapter.OnClickListener,
                                       HomeMultiAdapter.OnViewDetailsClickListener, DataFetcher,CategoryAdapter.OnItemClickListener, SubCateAdapter.OnItemClickListener {

    private RecyclerView recyclerView, jobpostrecyclerview, informationrecycerview, jobrecyclerview, employeerecyclerview;
    HomeMultiAdapter homeMultiAdapter;
    private List<Post> postList = new ArrayList<>(); // Create a list to store post details
    private List<ItemList> itemList = new ArrayList<>(); // Create a list to store post details
    private List<ItemList> filteredList = new ArrayList<>();
    private List<JobDetails> jobDetailsList ;
    private List<JobDetails> filteredjobpostlist;
    private List<EmployeeDetails> filteredemployeeDetailsList;
    private List<ChatJob> chatJobList;
    private List<EmployeeDetails> employeeDetailsList;
    ImageView searchImage;
    private PostAdapter postAdapter;
    CardView searchcard;
    List<Shop> shopList;
    String shopcontactNumber, taluka, address, shopName, shopimage, destrict;
    private List<ItemList> originalItemList; // Keep a copy of the original list
    private int lastDisplayedIndex = -1;
    FrameLayout frameLayout;
    ImageView adimagecancel,filterx, clearSearch;
    private boolean imageShown = false;
    DatabaseReference userRef, shopRef;
    AlertDialog dialog;
    String userId, jobTitle, companyname, joblocation, jobtype, description, workplacetype, currentdate, postcontactNumber, jobid, experience, skills, salary, jobopenings, switchUser;
    JobPostAdapter jobPostAdapter;
    EmployeeAdapter employeeAdapter;
    TextView usernametextview,location;
    RadioButton businessradiobtn, jobradiobtn, rdemployeebtn;
    RadioGroup radioGroup;
    EditText searchedittext;
    List<Object> homeItemList=new ArrayList<>();
    List<Object> filteredhomeItemList=new ArrayList<>();
    String shopname, premium, postImg, postDesc,postType ,postKeys, postCate, contactkey;
    String shopimagex;
    String shopaddress, checkstring="rdbiz";
    SwipeRefreshLayout swipeRefreshLayout, swipeRefreshLayout2, swipeRefreshLayout3;
    CategoryAdapter categoryAdapter;
    SubCateAdapter subCateAdapter;
    ArrayList<CategoryModel> categoryModels=new ArrayList<>();
    ArrayList<SubCategoryModel> subCategoryModels=new ArrayList<>();
    ArrayList<String> postCategories = new ArrayList<>();
    ArrayList<String> extractedTexts = new ArrayList<>();
    private LottieAnimationView lottieAnimationView;
    SharedPreferences sharedPreference;
    int x = 0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    LinearLayout setLoc;
    String MainCategory="Business";

    private static final String PREFS_NAME = "MyLoginPrefsFile";
    private static final String PREFS_NAME2 = "MyPrefsFile11";
    private static final String LAST_DIALOG_TIME = "lastDialogTime";
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // Permission granted, proceed to get location
                    getLocation();
                } else {
                    Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show();
                    location.setText("Global");
//                    ClearAllHome();
//                    LoadHomeDataNewTest();
                    statuswithglobal();
                }
            }
    );


    public FragmentHome() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__home, container, false);

        // Test Github

        //Change by Ik

        searchImage = view.findViewById(R.id.search_image);
        searchcard = view.findViewById(R.id.search);
        frameLayout = view.findViewById(R.id.frameLayout);
        adimagecancel = view.findViewById(R.id.adimagecancel);
        usernametextview = view.findViewById(R.id.usernametext);
        businessradiobtn = view.findViewById(R.id.rdbusiness);
        jobradiobtn = view.findViewById(R.id.rdjob);
        informationrecycerview = view.findViewById(R.id.information);
        jobrecyclerview = view.findViewById(R.id.job);
        employeerecyclerview = view.findViewById(R.id.employee);
        searchedittext = view.findViewById(R.id.searchedittext);
        radioGroup = view.findViewById(R.id.rdgrpx);
        rdemployeebtn = view.findViewById(R.id.rdjobseeker);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout2 = view.findViewById(R.id.swipeRefreshLayout2);
        swipeRefreshLayout3 = view.findViewById(R.id.swipeRefreshLayout3);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);
        location=view.findViewById(R.id.pincode);
        setLoc=view.findViewById(R.id.locset);
        clearSearch = view.findViewById(R.id.clearsearch);
        clearSearch.setVisibility(View.GONE);
        jobpostrecyclerview = view.findViewById(R.id.jobpostrecyclerview);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout2.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout3.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        filterx = view.findViewById(R.id.filters);

        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("ads");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        // Initialize with -1 to start from the first image
        sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        SharedPreferences preferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        switchUser = preferences.getString("userType", null);
        System.out.println("wdsvcx " +switchUser);

        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            userRef.child(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        // Check and request location permissions if not granted
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissions already granted, proceed to get location

           // getLocation();
        }

        location.setText("Global");

        if (switchUser!=null){
            if (switchUser.equals("user")){
                jobradiobtn.setVisibility(View.VISIBLE);
                rdemployeebtn.setVisibility(View.GONE);
            }else if (switchUser.equals("business")){
                jobradiobtn.setVisibility(View.GONE);
                rdemployeebtn.setVisibility(View.VISIBLE);
            }
        }else {
            shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        jobradiobtn.setVisibility(View.GONE);
                        rdemployeebtn.setVisibility(View.VISIBLE);
                    } else {
                        jobradiobtn.setVisibility(View.VISIBLE);
                        rdemployeebtn.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                }
            });
        }


        checkstring = "rdbiz";

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkstring.equals("rdbiz")) {
                    if(location.getText().toString().equals("Global")){
                        ClearAllHome();
                        LoadHomeDataNewTest();
                    }else {
                        ClearAllHome();
                        LoadHomeDataNewByLocation();
                    }
                    swipeRefreshLayout.setRefreshing(false);

                }
            }
        });

        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkstring.equals("rdjob")) {

                    ClearAll();
                    jobDetailsList = new ArrayList<>();
                    filteredjobpostlist = new ArrayList<>();
                    jobPostAdapter = new JobPostAdapter(jobDetailsList, getContext(), sharedPreference, FragmentHome.this);
                    jobrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    jobrecyclerview.setAdapter(jobPostAdapter);
                    retrieveJobPostDetails();
                    filterx.setVisibility(View.GONE);
                    swipeRefreshLayout2.setRefreshing(false);

                }

            }
        });

        swipeRefreshLayout3.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               if (checkstring.equals("rdemployee")) {
                    ClearAllEmployee();
                    employeeDetailsList = new ArrayList<>();
                    filteredemployeeDetailsList = new ArrayList<>();
                    employeeAdapter = new EmployeeAdapter(employeeDetailsList, getContext(), sharedPreference);
                    employeerecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    employeerecyclerview.setAdapter(employeeAdapter);
                    retrieveEmployeeDetails();
                    filterx.setVisibility(View.GONE);
                    swipeRefreshLayout3.setRefreshing(false);
                }

            }
        });


        if (userId!=null) {
            userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class);

                        if (name != null && name.contains(" ")) {
                            String firstName = name.substring(0, name.indexOf(" "));
                            usernametextview.setText(firstName);
                        }
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
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

        // In your onCreate or wherever you initialize the app
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPreferences", MODE_PRIVATE);
        lastDisplayedIndex = sharedPreferences.getInt("lastDisplayedIndex", -1);
        // Check if it's the first-time app launch
        SharedPreferences sharedPreferencess = getActivity().getSharedPreferences("MyAppPreferences", MODE_PRIVATE);
        boolean isFirstLaunch = sharedPreferencess.getBoolean("isFirstLaunch", true);

        if (isFirstLaunch) {
            // It's the first-time app launch, display the image
            displayImageForFirstLaunch();
            // Set the flag to indicate that the image has been displayed
            SharedPreferences.Editor editor = sharedPreferencess.edit();
            editor.putBoolean("isFirstLaunch", false);
            editor.apply();
        } else {
            // Inside your method to display the image
            shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
//                        List<String> imageUrls = new ArrayList<>();
//
//                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                            String imageUrl = childSnapshot.getValue(String.class);
//                            if (imageUrl != null) {
//                                imageUrls.add(imageUrl);
//                            }
//                        }
//
//                        // Ensure there are images to display
//                        if (!imageUrls.isEmpty()) {
//                            // Increment the index for the next time
//                            lastDisplayedIndex++;
//                            if (lastDisplayedIndex >= imageUrls.size()) {
//                                // If we reached the end, reset to 0
//                                lastDisplayedIndex = 0;
//                            }

                            // Retrieve the URL for the current index
                          // String currentImageUrl = imageUrls.get(lastDisplayedIndex);
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            String bannerimage = dataSnapshot.child("bannerimage").getValue(String.class);
                            System.out.println("urj " +bannerimage);
                            // Create and show the image alert dialog for the current image URL
                            if (bannerimage != null) {
                                showImageAlertDialog(bannerimage);

                                // Save the last displayed index to SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("lastDisplayedIndex", lastDisplayedIndex);
                                editor.apply();
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

      //  retrievePostDetails(); // Call the method to retrieve post details


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




//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
//                if (null != rb) {
//                    // checkedId is the RadioButton selected
//                    switch (i) {
//                        case R.id.rdbusiness:
//                            // Do Something
//                            checkstring = "rdbiz";
//                            if(location.getText().toString().equals("Global")){
////                                ClearAllHome();
////                                LoadHomeDataNewTest();
//                                statuswithglobal();
//                            }else {
////                                ClearAllHome();
////                                LoadHomeDataNewByLocation();
//                                statuswithlocation(location.getText().toString().trim());
//                            }
//                            searchedittext.setText("");
//                            searchedittext.setHint("व्यवसाय शोधा");
//                            filterx.setVisibility(View.VISIBLE);
//                            break;
//
//                        case R.id.rdjob:
//                            // Do Something
//                            checkstring = "rdjob";
//                            String locate = location.getText().toString().trim();
//
//                            System.out.println("ergfdvc "+locate);
//                            shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//
//                                    if (snapshot.exists()){
//                                        if (switchUser!=null && switchUser.equals("business")) {
//                                            checkstring = "bziaccount";
//                                            ClearAllEmployee();
//                                            employeeDetailsList = new ArrayList<>();
//                                            filteredemployeeDetailsList = new ArrayList<>();
//                                            retrieveEmployeeDetails();
//                                            searchedittext.setText("");
//                                            searchedittext.setHint("कर्मचारी शोधा");
//                                            filterx.setVisibility(View.GONE);
//                                            if (locate.equals("Global")){
//                                                statuswithglobal();
//                                            }else {
//                                                statuswithlocation(locate);
//                                            }
//
//                                        }else if (switchUser!=null && switchUser.equals("user")){
//                                            checkstring = "notbiz";
//                                             ClearAll();
//                                    jobDetailsList = new ArrayList<>();
//                                    filteredjobpostlist = new ArrayList<>();
//                                    retrieveJobPostDetails();
//                                    filterx.setVisibility(View.GONE);
//                                    searchedittext.setText("");
//                                    searchedittext.setHint("नोकरी शोधा");
//                                            if (locate.equals("Global")){
//                                                statuswithglobal();
//                                            }else {
//                                                statuswithlocation(locate);
//                                                Toast.makeText(getContext(), ""+locate, Toast.LENGTH_SHORT).show();
//                                            }
//                                        }
//
//                                    } else {
//                                       // Toast.makeText(getContext(), "thdfvdcx", Toast.LENGTH_SHORT).show();
//
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//                                }
//                            });
//
//
//
//                            break;
//
//                    }
//                }
//            }
//        });

        businessradiobtn.setChecked(true);

        if(location.getText().toString().trim().equals("Global")){
            ClearAllHome();
            LoadHomeDataNewTest();
        }else {
            ClearAllHome();
            LoadHomeDataNewByLocation();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if (null != rb) {
                    switch (i) {
                        case R.id.rdbusiness:
                            // Do Something
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout2.setVisibility(View.GONE);
                            swipeRefreshLayout3.setVisibility(View.GONE);
                            checkstring = "rdbiz";
                            searchedittext.setText("");
                            searchedittext.setHint("व्यवसाय शोधा");
                            filterx.setVisibility(View.VISIBLE);
                            String loca = location.getText().toString().trim();
                            System.out.println("erhdberc "+loca);

                            if(loca.equals("Global")){
                                ClearAllHome();
                                LoadHomeDataNewTest();
                            }else {
                                ClearAllHome();
                                LoadHomeDataNewByLocation();
                            }
                            break;

                        case R.id.rdjob:
                            // Do Something
                            swipeRefreshLayout.setVisibility(View.GONE);
                            swipeRefreshLayout2.setVisibility(View.VISIBLE);
                            swipeRefreshLayout3.setVisibility(View.GONE);
                            checkstring = "rdjob";
                            String loc = location.getText().toString().trim();
                            System.out.println("erhdbc "+loc);

                            if(loc.equals("Global")){
                                // load all data
                                    ClearAll();
                                    jobDetailsList = new ArrayList<>();
                                    filteredjobpostlist = new ArrayList<>();
                                jobPostAdapter = new JobPostAdapter(jobDetailsList, getContext(), sharedPreference, FragmentHome.this);
                                jobrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                                jobrecyclerview.setAdapter(jobPostAdapter);
                                    retrieveJobPostDetails();
                                    filterx.setVisibility(View.GONE);
                                    searchedittext.setText("");
                                    searchedittext.setHint("नोकरी शोधा");

                            }else {

                                System.out.println("ergf "+loc);
                                jobPostAdapter = new JobPostAdapter(jobDetailsList, getContext(), sharedPreference, FragmentHome.this);
                                jobrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                                jobrecyclerview.setAdapter(jobPostAdapter);
                                    filterjobpostbylocation(loc);
                                    filterx.setVisibility(View.GONE);
                                    searchedittext.setText(loc);
                                    searchedittext.setHint("नोकरी शोधा");

                            }

                            break;

                        case R.id.rdjobseeker:
                            // Do Something
                            swipeRefreshLayout.setVisibility(View.GONE);
                            swipeRefreshLayout2.setVisibility(View.GONE);
                            swipeRefreshLayout3.setVisibility(View.VISIBLE);
                            checkstring = "rdemployee";
                            String locat = location.getText().toString().trim();

                            if(locat.equals("Global")){
                                // load all data
                                    ClearAllEmployee();
                                    employeeDetailsList = new ArrayList<>();
                                    filteredemployeeDetailsList = new ArrayList<>();
                                employeeAdapter = new EmployeeAdapter(employeeDetailsList, getContext(), sharedPreference);
                                employeerecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                                employeerecyclerview.setAdapter(employeeAdapter);
                                    retrieveEmployeeDetails();
                                    searchedittext.setText("");
                                    searchedittext.setHint("कर्मचारी शोधा");
                                    filterx.setVisibility(View.GONE);

                            } else {
                                employeeAdapter = new EmployeeAdapter(employeeDetailsList, getContext(), sharedPreference);
                                employeerecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                                employeerecyclerview.setAdapter(employeeAdapter);
                                    filterEmployeewithloction(locat);
                                    searchedittext.setText(locat);
                                    searchedittext.setHint("कर्मचारी शोधा");
                                    filterx.setVisibility(View.GONE);
                                }
                            break;
                    }
                }
            }
        });

        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the text in the searchedittext
                searchedittext.setText("");
                // Hide the clearSearch button
                clearSearch.setVisibility(View.GONE);
            }
        });

        searchedittext.addTextChangedListener(new TextWatcher() {
            private CharSequence previousText = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not needed for this implementation
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Check if the entered text is different from the previous text
                if (!charSequence.toString().equals(previousText.toString())) {
                    // Save the current text for comparison in the next change
                    previousText = charSequence;
                    // Your existing logic goes here
                    if (TextUtils.isEmpty(charSequence)) {
                        // Text is empty, show the full list
                        clearSearch.setVisibility(View.GONE);
                        if (checkstring.equals("rdbiz")) {
                            // For business posts
                            ClearAllHome();
                            LoadHomeDataNewTest();
                        } else if (checkstring.equals("rdjob")) {
                            // For job posts
                            ClearAll();
                            jobDetailsList = new ArrayList<>();
                            filteredjobpostlist = new ArrayList<>();
                            jobPostAdapter = new JobPostAdapter(jobDetailsList, getContext(), sharedPreference, FragmentHome.this);
                            jobrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                            jobrecyclerview.setAdapter(jobPostAdapter);
                            retrieveJobPostDetails();
                        } else if (checkstring.equals("rdemployee")) {
                            // For employee posts
                            ClearAllEmployee();
                            employeeDetailsList = new ArrayList<>();
                            filteredemployeeDetailsList = new ArrayList<>();
                            employeeAdapter = new EmployeeAdapter(employeeDetailsList, getContext(), sharedPreference);
                            employeerecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                            employeerecyclerview.setAdapter(employeeAdapter);
                            retrieveEmployeeDetails();
                        }
                    } else {
                        clearSearch.setVisibility(View.VISIBLE);
                        // Text is not empty, apply filtering based on the entered text
                        if (checkstring.equals("rdbiz")) {
                            // For business posts
                            String keywords="";
                            filterpostAndItems(charSequence.toString(), keywords);
                        } else if (checkstring.equals("rdjob")) {
                            // For job posts
                            filterjobpost(charSequence.toString());
                        } else if (checkstring.equals("rdemployee")) {
                            // For employee posts
                            filterEmployee(charSequence.toString());
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not needed for this implementation
            }
        });

        
        filterx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCategory();
            }
        });

        setLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocation();
            }
        });

        //  retrieveitemDetails();
        checkstring = "rdbiz";
       // ClearAllHome();
      //  LoadHomeDataNewTest();

        checkStatus(new StatusCallback() {
            @Override
            public void onStatusChecked(boolean status) {
                // Use the status value here
                if(status){
                  //  Toast.makeText(getActivity(), "Active", Toast.LENGTH_SHORT).show();
                }else {
                    if (shouldShowDialog()) {
                        // Show the dialog
                        showPremiumDialog();
                    }
                  //  Toast.makeText(getActivity(), "Plan Expired", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission granted, proceed to get location
                getLocation();
            } else {
                Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkUserStatus(){
        shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (switchUser!=null && switchUser.equals("business")) {
                        checkstring = "rdemployee";
                        ClearAllEmployee();
                        employeeDetailsList = new ArrayList<>();
                        filteredemployeeDetailsList = new ArrayList<>();
                        retrieveEmployeeDetails();
                        searchedittext.setText("");
                        searchedittext.setHint("कर्मचारी शोधा");
                        filterx.setVisibility(View.GONE);
                    }else if (switchUser!=null && switchUser.equals("user")){
                        checkstring = "rdjob";
                        ClearAll();
                        jobDetailsList = new ArrayList<>();
                        filteredjobpostlist = new ArrayList<>();
                        filterx.setVisibility(View.GONE);
                        retrieveJobPostDetails();
                        searchedittext.setText("");
                        searchedittext.setHint("नोकरी शोधा");
                    }
                } else {
                    // Toast.makeText(getContext(), "thdfvdcx", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }

    private void ClearAll() {
        if (jobDetailsList != null) {
            jobDetailsList.clear();

            if(jobPostAdapter!=null){
                jobPostAdapter.notifyDataSetChanged();
            }
        }
        jobDetailsList=new ArrayList<>();
    }

    private void ClearAllEmployee(){
        if(employeeDetailsList != null){
            employeeDetailsList.clear();

            if(employeeAdapter!=null){
                employeeAdapter.notifyDataSetChanged();
            }
        }
        employeeDetailsList=new ArrayList<>();
    }

    private void ClearAllHome(){
        if(homeItemList != null){
            homeItemList.clear();

            if(homeMultiAdapter!=null){
                homeMultiAdapter.notifyDataSetChanged();
            }
        }
        homeItemList=new ArrayList<>();
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

    private boolean isValidImageUrl(String imageUrl) {
        // Implement your validation logic here
        // Check if the URL is a valid image URL
        // You can use libraries like Picasso or Glide to validate the URL
        // Return true if it's a valid image URL, otherwise return false
        // You may also check if the file format is supported
        // and if the image exists on the server
        return true;
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
                frameLayout.setVisibility(View.VISIBLE);

                // Use Glide to load and display the image from the URL
                Glide.with(getContext())
                        .load(imageUrl)
                        .into(imageView);

                adimagecancel.setNextFocusUpId(R.id.btnCancel);
            }

            builder.setView(dialogView);

            dialog = builder.create(); // Initialize the dialog variable
            dialog.setCanceledOnTouchOutside(false); // Disable dismissing the dialog by clicking outside
            dialog.show();
        }
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

    private void filterjobpost(String query) {
        // Clear the filtered list before updating it
        if (jobDetailsList != null) {
            filteredjobpostlist.clear();

        if (query == null) {
            // If the search query is empty, restore the original list
            filteredjobpostlist.addAll(jobDetailsList);
        } else {
            query = query.toLowerCase().trim();
            for (JobDetails item : jobDetailsList) {
                if (item.getJobtitle().toLowerCase().contains(query)
                        || item.getDescription().toLowerCase().contains(query)
                        || item.getCompanyname().toLowerCase().contains(query)
                        || item.getJoblocation().toLowerCase().contains(query)
                        || item.getJobtype().toLowerCase().contains(query)
                        || item.getWorkplacetype().toLowerCase().contains(query)) {
                    filteredjobpostlist.add(item);

                }
            }
        }
        jobPostAdapter.setData(filteredjobpostlist);
    }
    }

    private void filterjobpostbylocation(String query){
        // Clear the filtered list before updating it
        if (jobDetailsList!=null || filteredjobpostlist!=null) {
            filteredjobpostlist.clear();
            query = query.toLowerCase().trim();
            for (JobDetails item : jobDetailsList) {
                if (item.getJoblocation().toLowerCase().contains(query)) {
                    filteredjobpostlist.add(item);
                    jobPostAdapter.setData(filteredjobpostlist);
                }

            }
        }
    }

    private void filterpostAndItems(String query, String keywords) {

        //ClearAllHome();
       // LoadHomeDataNewTest();
        // Clear the filtered list before updating it
        filteredhomeItemList.clear();

       // Toast.makeText(getActivity(), "size  "+homeItemList.size(), Toast.LENGTH_SHORT).show();

        if (!TextUtils.isEmpty(keywords)) {
            // Filter by keywords if available
            filterByKeywords(keywords);
        } else if (!TextUtils.isEmpty(query)) {
            // Filter by query text if available
            filterByQuery(query);
        } else {
            // If both keywords and query are empty, restore the original list
            filteredhomeItemList.addAll(homeItemList);
        }

        // Update the RecyclerView with the filtered list
        homeMultiAdapter.setData(filteredhomeItemList);
        homeMultiAdapter.notifyDataSetChanged();

    }

    private void filterByKeywords(String keywords) {
        keywords = keywords.toLowerCase().trim();
        Log.d("asdcxd", keywords);

      //  Toast.makeText(getActivity(), " ss "+homeItemList.size(), Toast.LENGTH_SHORT).show();
        for (Object item : homeItemList) {
            if (item instanceof PostModel) {
                PostModel currentPost = (PostModel) item;
                //Log.d("fdsfgsdgsd", currentPost.getPostKeys());
               // System.out.println("h bgyvgyg "+currentPost.getPostKeys());

                // Check if any property matches the query
                if (currentPost.getPostKeys().contains(keywords)) {
                    Log.d("asfasfasfas", currentPost.getPostId());
                    filteredhomeItemList.add(item);
                }
            }
        }
    }

    private void filterByQuery(String query) {
        // Filter the list based on the search query
        query = query.toLowerCase().trim();
        Log.d("wefgrg", query);
        for (Object item : homeItemList) {
            if (item instanceof PostModel) {
                PostModel currentPost = (PostModel) item;
                // Check if any property matches the query
                if (currentPost.getPostKeys().contains(query) || currentPost.getPostCate().contains(query)) {
                    filteredhomeItemList.add(item);
                }
            }
        }
    }

    private void filterEmployee(String query) {
        // Clear the filtered list before updating it
        if (employeeDetailsList!=null) {
            filteredemployeeDetailsList.clear();

            if (TextUtils.isEmpty(query)) {
                // If the search query is empty, restore the original list
                filteredemployeeDetailsList.addAll(employeeDetailsList);
            } else {
                // Filter the list based on the search query
                query = query.toLowerCase().trim();
                for (EmployeeDetails item : employeeDetailsList) {
                    if (item.getCandidateName().toLowerCase().contains(query)
                            || item.getCandidateEmail().toLowerCase().contains(query)
                            || item.getCandidateAddress().toLowerCase().contains(query)
                            || item.getCandidateQualification().toLowerCase().contains(query)
                            || item.getCandidateStream().toLowerCase().contains(query)
                            || item.getCandidateSkills().toLowerCase().contains(query)) {
                        filteredemployeeDetailsList.add(item);
                    }
                }
            }

            // Update the RecyclerView with the filtered list
            employeeAdapter.setData(filteredemployeeDetailsList);
        }
    }

    private void filterEmployeewithloction(String query) {
        // Clear the filtered list before updating it
        if (employeeDetailsList!=null) {

            filteredemployeeDetailsList.clear();

            query = query.toLowerCase().trim();
            for (EmployeeDetails item : employeeDetailsList) {
                if (item.getCandidateAddress().toLowerCase().contains(query)) {
                    filteredemployeeDetailsList.add(item);
                }
            }
            // Update the RecyclerView with the filtered list
            employeeAdapter.setData(filteredemployeeDetailsList);
        }
    }

    private void filter(String query) {
        // Clear the filtered list before updating it
        filteredList.clear();

        if (TextUtils.isEmpty(query)) {
            // If the search query is empty, restore the original list
            filteredList.addAll(itemList);
        } else {
            // Filter the list based on the search query
            query = query.toLowerCase().trim();
            for (ItemList item : itemList) {
                if (item.getName().toLowerCase().contains(query)
                        || item.getDescription().toLowerCase().contains(query)
                        || item.getShopName().toLowerCase().contains(query)
                        || item.getDistrict().toLowerCase().contains(query)) {
                    filteredList.add(item);
                }
            }
        }

        // Update the RecyclerView with the filtered list
//        postAdapter.setData(filteredList);

    }

//    @Override
//    public void onClickClick(int position) {
//        if (position >= 0 && position < filteredList.size()) {
//            ItemList selectedItem = filteredList.get(position);
//
//            String clickedShopcontactNumber = selectedItem.getShopcontactNumber();
//            String shopimage = selectedItem.getShopimage();
//
//            Intent intent = new Intent(getContext(), ShopDetails.class);
//            intent.putExtra("contactNumber", clickedShopcontactNumber);
//            intent.putExtra("image", shopimage);
//            startActivity(intent);
//        }
//    }
//
//    @Override
//    public void onContactClick(int position) {
//
//
//
//    }

//    @Override
//    public void onorderClick(int position) {
//
//        ItemList selectedItem = null;
//
//        if (position >= 0) {
//            if (position < filteredList.size()) {
//                selectedItem = filteredList.get(position);
//            } else if (position < itemList.size()) {
//                selectedItem = itemList.get(position);
//            }
//        }
//
//        if (selectedItem != null) {
//            String clickedShopcontactNumber = selectedItem.getShopcontactNumber();
//            List<String> itemimages = selectedItem.getImagesUrls();
//            String itemimage = selectedItem.getFirstImageUrl();
//            String itemdescription = selectedItem.getDescription();
//            String itemprice = selectedItem.getPrice();
//            String itemname = selectedItem.getName();
//            String itemkey = selectedItem.getItemkey();
//            String shopName = selectedItem.getShopName();
//            String district = selectedItem.getDistrict();
//            String shopimage = selectedItem.getShopimage();
//            String shoptaluka = selectedItem.getTaluka();
//            String shopaddress = selectedItem.getAddress();
//            String wholesale = selectedItem.getWholesaleprice();
//            String minqty = selectedItem.getMinqty();
//            Boolean flag = true;
//
//            Intent intent = new Intent(getContext(), ItemDetails.class);
//            intent.putExtra("itemName", itemname);
//            intent.putExtra("firstImageUrl", itemimage);
//            intent.putExtra("itemDescription", itemdescription);
//            intent.putExtra("itemPrice", itemprice);
//            intent.putExtra("itemKey", itemkey);
//            intent.putExtra("contactNumber", clickedShopcontactNumber);
//            intent.putExtra("shopName", shopName);
//            intent.putExtra("district", district);
//            intent.putExtra("shopimage", shopimage);
//            intent.putExtra("taluka", shoptaluka);
//            intent.putExtra("address", shopaddress);
//            intent.putExtra("itemWholesale", wholesale);
//            intent.putExtra("itemMinqty", minqty);
//
//            intent.putExtra("flag", flag);
//
//            // Pass the list of item images
//            intent.putStringArrayListExtra("itemImages", new ArrayList<>(itemimages));
//            startActivity(intent);
//        }
//    }

//    @Override
//    public void oncallClick(int position) {
//
//        if (position >= 0 && position < filteredList.size()) {
//            String clickedShopcontactNumber = filteredList.get(position).getShopcontactNumber();
//            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + clickedShopcontactNumber));
//            // Start the phone call activity
//            startActivity(callIntent);
//        }
//    }
//
//    @Override
//    public void onseeallClick(int position) {
//
//        ItemList selectedItem = null;
//
//        if (position >= 0) {
//            if (position < filteredList.size()) {
//                selectedItem = filteredList.get(position);
//            } else if (position < itemList.size()) {
//                selectedItem = itemList.get(position);
//            }
//        }
//
//        if (selectedItem != null) {
//
//            String clickedShopcontactNumber = selectedItem.getShopcontactNumber();
//            String shopName = selectedItem.getShopName();
//            String shopimage = selectedItem.getShopimage();
//            String district = selectedItem.getDistrict();
//            String itemkey = selectedItem.getItemkey();
//            String wholesale = selectedItem.getWholesaleprice();
//            String minqty = selectedItem.getMinqty();
//
//            Intent intent = new Intent(getContext(), ShowAllItemsList.class);
//            intent.putExtra("shopName", shopName);
//            intent.putExtra("shopImage", shopimage);
//            intent.putExtra("contactNumber", clickedShopcontactNumber);
//            intent.putExtra("district", district);
//            intent.putExtra("itemKey", itemkey);
//            intent.putExtra("itemWholesale", wholesale);
//            intent.putExtra("itemMinqty", minqty);
//            startActivity(intent);
//        }
//    }

    // Method to retrieve post details from Firebase

    private void retrieveJobPostDetails() {
        lottieAnimationView.setVisibility(View.VISIBLE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JobPosts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ClearAll();
                    chatJobList = new ArrayList<>();
                    for (DataSnapshot jobPostsSnapshot : snapshot.getChildren()) {
                        String contactNumber = jobPostsSnapshot.getKey();
                        System.out.println("Contact Number: " + contactNumber);
                        String jobkey = jobPostsSnapshot.getKey();

                        for (DataSnapshot postSnapshot : jobPostsSnapshot.getChildren()) {
                            String postkey = postSnapshot.getKey();
                            System.out.println("rdvvfc " + postkey);

                            jobTitle = postSnapshot.child("jobtitle").getValue(String.class);
                            System.out.println("Job Title: " + jobTitle);

                            companyname = postSnapshot.child("companyname").getValue(String.class);
                            joblocation = postSnapshot.child("joblocation").getValue(String.class);
                            jobtype = postSnapshot.child("jobtype").getValue(String.class);
                            description = postSnapshot.child("description").getValue(String.class);
                            workplacetype = postSnapshot.child("workplacetype").getValue(String.class);
                            currentdate = postSnapshot.child("currentdate").getValue(String.class);
                            postcontactNumber = postSnapshot.child("contactNumber").getValue(String.class);
                            jobid = postSnapshot.child("jobID").getValue(String.class);
                             experience = postSnapshot.child("experience").getValue(String.class);
                             salary = postSnapshot.child("salary").getValue(String.class);
                             skills = postSnapshot.child("skills").getValue(String.class);
                             jobopenings = postSnapshot.child("jobopenings").getValue(String.class);


                            System.out.println("Company Name: " + companyname);
                            System.out.println("Job Location: " + joblocation);
                            System.out.println("Job Type: " + jobtype);
                            System.out.println("Description: " + description);
                            System.out.println("Workplace Type: " + postcontactNumber);

                            JobDetails jobDetails = new JobDetails(jobTitle, companyname, workplacetype, joblocation, jobtype,
                                    description, currentdate, jobkey, postcontactNumber,experience, salary, skills,jobopenings, jobid);
                            jobDetailsList.add(jobDetails);

                            ChatJob chatJob = new ChatJob(companyname, jobTitle, postcontactNumber,userId, jobid);
                            chatJobList.add(chatJob);
                        }
                    }


                    jobPostAdapter.setData(jobDetailsList);
                    jobPostAdapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void retrieveEmployeeDetails() {
        lottieAnimationView.setVisibility(View.VISIBLE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ApplicationProfile");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ClearAll();
                    employeeDetailsList = new ArrayList<>();
                    for (DataSnapshot jobPostsSnapshot : snapshot.getChildren()) {
                        String contactNumber = jobPostsSnapshot.getKey();
                        System.out.println("Contact Numbersdv: " + contactNumber);
                        String jobkey = jobPostsSnapshot.getKey();

                        String candidateName = jobPostsSnapshot.child("candidateName").getValue(String.class);
                        String candidateEmail = jobPostsSnapshot.child("candidateEmail").getValue(String.class);
                        String candidateContactNumber = jobPostsSnapshot.child("candidateContactNumber").getValue(String.class);
                        String candidateAddress = jobPostsSnapshot.child("candidateAddress").getValue(String.class);
                        String candidateQualification = jobPostsSnapshot.child("candidateQualification").getValue(String.class);
                        String candidateSkills = jobPostsSnapshot.child("candidateSkills").getValue(String.class);
                        String candidateStream = jobPostsSnapshot.child("candidateStream").getValue(String.class);



                        EmployeeDetails employeeDetails = new EmployeeDetails(candidateName, candidateEmail, candidateContactNumber, candidateAddress, candidateQualification,
                                candidateSkills, candidateStream);

                        employeeDetailsList.add(employeeDetails);

                    }


                    employeeAdapter.setData(employeeDetailsList);
                    employeeAdapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
//        if(location.getText().toString().equals("Global")){
//            ClearAllHome();
//            LoadHomeDataNewTest();
//        }else {
//            ClearAllHome();
//            LoadHomeDataNewByLocation();
//        }
   //     businessradiobtn.setChecked(true);
        // Handle the back button press within the fragment
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Close the app when the back button is pressed
                requireActivity().moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        // Assuming you have a list of JobDetails and you want to get the data for the clicked position
        JobDetails clickedJob = jobDetailsList.get(position);

        Intent intent = new Intent(getContext(), JobPostDetails.class);

       // intent.putExtra("contactNumber", userId);
        intent.putExtra("jobtitle", clickedJob.getJobtitle()); // Replace with the actual method to get job title
        intent.putExtra("companyname", clickedJob.getCompanyname()); // Replace with the actual method to get company name
        intent.putExtra("joblocation", clickedJob.getJoblocation()); // Replace with the actual method to get job location
        intent.putExtra("jobtype", clickedJob.getJobtype()); // Replace with the actual method to get job type
        intent.putExtra("workplacetype", clickedJob.getWorkplacetype()); // Replace with the actual method to get workplace type
        intent.putExtra("description", clickedJob.getDescription()); // Replace with the actual method to get description
        intent.putExtra("currentdate", clickedJob.getCurrentdate()); // Replace with the actual method to get current date
        intent.putExtra("postcontactNumber", clickedJob.getContactNumber());
        intent.putExtra("jobID", clickedJob.getJobID());
        intent.putExtra("experience", clickedJob.getExperience());
        intent.putExtra("salary", clickedJob.getSalary());
        intent.putExtra("skills", clickedJob.getSkills());
        intent.putExtra("jobopenings", clickedJob.getJobopenings());
        System.out.println("sdvcf " +clickedJob.getContactNumber());

        startActivity(intent);
    }

    @Override
    public void onchatClick(int position) {

        ChatJob chatJob = chatJobList.get(position);
        Intent intent = new Intent(getContext(), JobChat.class);
        intent.putExtra("companyname", chatJob.getShopName());
        intent.putExtra("jobtitle", chatJob.getJobtitle());
        intent.putExtra("UserContactNum", chatJob.getUserContactNum());
        intent.putExtra("BusiContactNum", chatJob.getBusiContactNum());
        intent.putExtra("jobID", chatJob.getJobId());
        startActivity(intent);

    }


    //code by ik
    public void LoadHomeData(){

//        homeItemList=new ArrayList<>();
//
//        PostModel postModel=new PostModel();
//        postModel.setPostId("1");
//        postModel.setPostDesc("1");
//        postModel.setPostImg("1");
//        postModel.setPostType("1");
//        postModel.setPostUser("1");
//        postModel.setPostKeys("1");
//        postModel.setUserAdd("1");
//        postModel.setUserImg("");
//        homeItemList.add(postModel);
//
//
//        OrderModel orderModel=new OrderModel();
//        orderModel.setProdId("5");
//        orderModel.setProdName("5");
//        orderModel.setProDesc("5");
//        orderModel.setProImg("5");
//        orderModel.setCrossRate("5");
//        orderModel.setShowRate("5");
//        orderModel.setProTag("5");
//        orderModel.setOffer("5");
//        orderModel.setRating("5");
//        orderModel.setDealerCode("5");
//        orderModel.setProSeq("5");
//        orderModel.setProprice("");
//        orderModel.setProsell("");
//
//        homeItemList.add(orderModel);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ClearAll();
                    chatJobList = new ArrayList<>();
                    for (DataSnapshot contactNumberSnapshot : snapshot.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();
                        System.out.println("Contact Number: " + contactNumber);

                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            String key = keySnapshot.getKey();

                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        String shopname = snapshot.child("shopName").getValue(String.class);
                                        String shopimage = snapshot.child("url").getValue(String.class);
                                        String shopaddress= snapshot.child("address").getValue(String.class);
                                    }
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                                }
                            });

                            // Retrieve data under each key
                            String caption = keySnapshot.child("caption").getValue(String.class);
                            String imageUrl = keySnapshot.child("imageURL").getValue(String.class);
                            String shopName = keySnapshot.child("shopName").getValue(String.class);
                            String posttype = keySnapshot.child("posttype").getValue(String.class);
                            String shopimage = keySnapshot.child("shopimage").getValue(String.class);
                            String shopaddress = keySnapshot.child("shopaddress").getValue(String.class);


                            PostModel postModel=new PostModel();
                            postModel.setPostId(key);
                            postModel.setPostDesc(caption);
                            postModel.setPostType(posttype);
                            postModel.setPostImg(imageUrl);
                            postModel.setPostUser(shopName);
                            postModel.setUserImg(shopimage);
                            postModel.setUserAdd(shopaddress);
                            homeItemList.add(postModel);
                        }
                    }
                    homeMultiAdapter.notifyDataSetChanged();
                    // Notify adapter or update UI as needed...
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot contactNumberSnapshot : snapshot.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot productSnapshot : contactNumberSnapshot.getChildren()) {
                            String productId = productSnapshot.getKey();
                            // Now, you can access the data within each product node
                            String itemName = productSnapshot.child("itemname").getValue(String.class);
                            String price = productSnapshot.child("price").getValue(String.class);
                            String sell = productSnapshot.child("sell").getValue(String.class);
                            String description = productSnapshot.child("description").getValue(String.class);
                            String itemKey = productSnapshot.child("itemkey").getValue(String.class);
                            String offer = productSnapshot.child("offer").getValue(String.class);
                            String firstimage = productSnapshot.child("firstImageUrl").getValue(String.class);
                            String sellprice = productSnapshot.child("sell").getValue(String.class);
                            String shopContactNumber = productSnapshot.child("shopContactNumber").getValue(String.class);

                            List<String> imageUrls = new ArrayList<>();
                            DataSnapshot imageUrlsSnapshot = productSnapshot.child("imageUrls");
                            for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                String imageUrl = imageUrlSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                }
                            }
                            // Use the retrieved data as needed
                            System.out.println("Contact Number: " + contactNumber);
                            System.out.println("Product ID: " + productId);
                            System.out.println("Item Name: " + itemName);
                            System.out.println("Price: " + price);
                            System.out.println("Sell: " + sell);
                            System.out.println("Description: " + description);
                            System.out.println("Item Key: " + itemKey);
                            System.out.println("Offer: " + offer);

                            OrderModel orderModel=new OrderModel();
                            orderModel.setProdId(itemKey);
                            orderModel.setProdName(itemName);
                            orderModel.setOffer(offer);
                            orderModel.setProImg(firstimage);
                            orderModel.setProDesc(description);
                            orderModel.setProprice(price);
                            orderModel.setProsell(sellprice);
                            orderModel.setShopContactNum(shopContactNumber);
                            orderModel.setImagesUrls(imageUrls);
                            homeItemList.add(orderModel);
                        }
                        homeMultiAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

//        if (profileverify != null && profileverify) {
//            DataSnapshot itemsSnapshot = shopSnapshot.child("items");
//            for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
//                String itemkey = itemSnapshot.getKey();
//
//                String itemName = itemSnapshot.child("itemname").getValue(String.class);
//                String price = itemSnapshot.child("price").getValue(String.class);
//                String description = itemSnapshot.child("description").getValue(String.class);
//                String firstimage = itemSnapshot.child("firstImageUrl").getValue(String.class);
//                String sellprice = itemSnapshot.child("sell").getValue(String.class);
//                String offer = itemSnapshot.child("offer").getValue(String.class);
//                String itemskey = itemSnapshot.child("itemkey").getValue(String.class);
//                System.out.println("jfhv " + firstimage);
//
//                if (TextUtils.isEmpty(firstimage)) {
//                    // Set a default image URL here
//                    firstimage = String.valueOf(R.drawable.ic_outline_shopping_bag_24);
//                }
//
//                List<String> imageUrls = new ArrayList<>();
//                DataSnapshot imageUrlsSnapshot = itemSnapshot.child("imageUrls");
//                for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
//                    String imageUrl = imageUrlSnapshot.getValue(String.class);
//                    if (imageUrl != null) {
//                        imageUrls.add(imageUrl);
//                    }
//                }
//
//                ItemList item = new ItemList(shopName, shopimage, shopcontactNumber, itemName, price, description,
//                        firstimage, itemkey, imageUrls, destrict,taluka,address );
//                itemList.add(item);
//

//            }
//        }




    }


    @Override
    public void onViewDetailsClick(int position) {
        // Check if position is within the bounds of the list
        Log.d("ItemDetails", "onViewDetailsClick called. Position: " + position);
        if (position >= 0 && position < homeItemList.size()) {
            Object selectedItem = homeItemList.get(position);

            if (selectedItem instanceof OrderModel) {
                OrderModel selectedListItem = (OrderModel) selectedItem;

                String clickedShopcontactNumber = selectedListItem.getShopContactNum();
                List<String> itemimages = selectedListItem.getImagesUrls();
                String itemimage = selectedListItem.getProImg();
                String itemdescription = selectedListItem.getProDesc();
                String itemprice = selectedListItem.getProprice();
                String itemname = selectedListItem.getProdName();
                String itemkey = selectedListItem.getProdId();
                String itemoffer = selectedListItem.getOffer();
                String itemsellprice = selectedListItem.getProsell();
                String itemwholesale = selectedListItem.getWholesale();
                String itemminqty = selectedListItem.getMinqty();
                System.out.println("sdfvcr " +itemwholesale);
                Boolean flag = true;

                Intent intent = new Intent(getContext(), ItemDetails.class);
                intent.putExtra("itemName", itemname);
                intent.putExtra("firstImageUrl", itemimage);
                intent.putExtra("itemDescription", itemdescription);
                intent.putExtra("itemPrice", itemprice);
                intent.putExtra("itemKey", itemkey);
                intent.putExtra("contactNumber", clickedShopcontactNumber);
                intent.putExtra("itemOffer", itemoffer);
                intent.putExtra("itemSellPrice", itemsellprice);
                intent.putExtra("itemWholesale", itemwholesale);
                intent.putExtra("itemMinqty", itemminqty);
               // intent.putExtra("shopName", shopName);
                intent.putExtra("flag", flag);

                // Pass the list of item images
                intent.putStringArrayListExtra("itemImages", new ArrayList<>(itemimages));
                startActivity(intent);
            } else {
                // Handle the case where the item at the specified position is not of type ItemList
                Log.e("ItemDetails", "Invalid item type at position " + position);
            }
        }
    }


    //code by ik
    public void LoadHomeDataNew() {
        ClearAllHome();
        lottieAnimationView.setVisibility(View.VISIBLE);

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot contactNumberSnapshot : snapshot.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot productSnapshot : contactNumberSnapshot.getChildren()) {
                            String productId = productSnapshot.getKey();
                            // Now, you can access the data within each product node
                            String itemName = productSnapshot.child("itemname").getValue(String.class);
                            String price = productSnapshot.child("price").getValue(String.class);
                            String sell = productSnapshot.child("sell").getValue(String.class);
                            String description = productSnapshot.child("description").getValue(String.class);
                            String itemKey = productSnapshot.child("itemkey").getValue(String.class);
                            String offer = productSnapshot.child("offer").getValue(String.class);
                            String firstimage = productSnapshot.child("firstImageUrl").getValue(String.class);
                            String sellprice = productSnapshot.child("sell").getValue(String.class);
                            String shopContactNumber = productSnapshot.child("shopContactNumber").getValue(String.class);
                            System.out.println("sdgsg " + shopContactNumber);

                            List<String> imageUrls = new ArrayList<>();
                            DataSnapshot imageUrlsSnapshot = productSnapshot.child("imageUrls");
                            for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                String imageUrl = imageUrlSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                }
                            }

                            // Use the retrieved data as needed
                            OrderModel orderModel = new OrderModel();
                            orderModel.setProdId(itemKey);
                            orderModel.setProdName(itemName);
                            orderModel.setOffer(offer);
                            orderModel.setProImg(firstimage);
                            orderModel.setProDesc(description);
                            orderModel.setProprice(price);
                            orderModel.setProsell(sellprice);
                            orderModel.setShopContactNum(shopContactNumber);
                            orderModel.setImagesUrls(imageUrls);
                            homeItemList.add(orderModel);
                        }
                    }


                    // Notify adapter or update UI as needed...
                    homeMultiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot contactNumberSnapshot : snapshot.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();
                        System.out.println("Contact Number: " + contactNumber);

                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            contactkey = keySnapshot.getKey();
                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        shopname = snapshot.child("shopName").getValue(String.class);
                                        shopimagex = snapshot.child("url").getValue(String.class);
                                        shopaddress = snapshot.child("address").getValue(String.class);

                                        postImg = keySnapshot.child("postImg").getValue(String.class);
                                        postDesc = keySnapshot.child("postDesc").getValue(String.class);
                                        postType = keySnapshot.child("postType").getValue(String.class);
                                        postKeys = keySnapshot.child("postKeys").getValue(String.class);
                                        postCate = keySnapshot.child("postCate").getValue(String.class);

                                        PostModel postModel = new PostModel();
                                        postModel.setPostId(contactkey);
                                        postModel.setPostDesc(postDesc);
                                        postModel.setPostType(postType);
                                        postModel.setPostImg(postImg);
                                        postModel.setPostKeys(postKeys);
                                        postModel.setPostCate(postCate);

                                        Log.d("fdfdfdsfd", "" + shopname);

                                        postModel.setPostUser(shopname);
                                        postModel.setUserImg(shopimagex);
                                        postModel.setUserAdd(shopaddress);
                                        homeItemList.add(postModel);

                                    }
                                }

                                @Override
                                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });
                        }
                    }
// Shuffle the homeItemList to display items randomly
                    Collections.shuffle(homeItemList);

                    Log.d("dsfsfsf", "" + homeItemList.size());

                    for (Object s : homeItemList) {
                        Log.d("dsfsfsf", " ff  " + s);
                    }

                    // Notify adapter or update UI as needed...
                    homeMultiAdapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

    }

    @Override
    public void onPostClick(int position) {
        if (position >= 0 && position < homeItemList.size()) {
            Object selectedItem = homeItemList.get(position);

            if (selectedItem instanceof PostModel) {
                PostModel selectedPost = (PostModel) selectedItem;
                String contactkey = selectedPost.getPostId();
                String postDesc = selectedPost.getPostDesc();
                String postType = selectedPost.getPostType();
                String postImg = selectedPost.getPostImg();
                String postKeys = selectedPost.getPostKeys();
                String postCate = selectedPost.getPostCate();
                String shopname = selectedPost.getPostUser();
                String shopimagex = selectedPost.getUserImg();
                String shopaddress = selectedPost.getUserAdd();
                String shopcontact = selectedPost.getPostcontactKey();
                String viewcount = selectedPost.getPostvisibilityCount();
                String clickcount = selectedPost.getPostclickCount();
                Boolean flag = true;
                System.out.println("rgdfer " +contactkey);

                // Increment the click count in Firebase
                updateClickCount(contactkey, shopcontact);

                Intent intent = new Intent(getContext(), PostInfo.class);
                intent.putExtra("contactKey", contactkey);
                intent.putExtra("postDesc", postDesc);
                intent.putExtra("postType", postType);
                intent.putExtra("postImg", postImg);
                intent.putExtra("postKeys", postKeys);
                intent.putExtra("postCate", postCate);
                intent.putExtra("shopname", shopname);
                intent.putExtra("shopimagex", shopimagex);
                intent.putExtra("shopaddress", shopaddress);
                intent.putExtra("shopContact", shopcontact);
                intent.putExtra("viewcount", viewcount);
                intent.putExtra("clickcount", clickcount);
                // intent.putExtra("shopName", shopName);
                intent.putExtra("flag", flag);

                // Pass the list of item images
                startActivity(intent);
            } else {
                // Handle the case where the item at the specified position is not of type ItemList
                Log.e("ItemDetails", "Invalid item type at position " + position);
            }
        }

    }

    private void updateClickCount(String postId, String shopContact) {
        DatabaseReference postClickCountRef = FirebaseDatabase.getInstance()
                .getReference("BusinessPosts")
                .child(shopContact)
                .child(postId)
                .child("clickCount");

        // Retrieve the current count from Firebase
        postClickCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentCountString = snapshot.getValue(String.class);

                int currentCount = Integer.parseInt(currentCountString);

                // Increment the count and update it in Firebase
                postClickCountRef.setValue(String.valueOf(currentCount + 1));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void LoadHomeDataNewTest() {
        ClearAllHome();
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x=0;
                    for (DataSnapshot contactNumberSnapshot : snapshotx.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            String key = keySnapshot.getKey();
                            postCategories.clear();
                            extractedTexts.clear();
                            homeItemList.clear();
                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        String status = keySnapshot.child("status").getValue(String.class);

                                        if (status.equals("Posted")) {
                                            System.out.println("fdrethf " +status);
                                            shopname = snapshot.child("shopName").getValue(String.class);
                                            shopimagex = snapshot.child("url").getValue(String.class);
                                            shopaddress = snapshot.child("address").getValue(String.class);

                                            String postImg = keySnapshot.child("postImg").getValue(String.class);
                                            String postDesc = keySnapshot.child("postDesc").getValue(String.class);
                                            String postType = keySnapshot.child("postType").getValue(String.class);
                                            String postKeys = keySnapshot.child("postKeys").getValue(String.class);
                                            String postCate = keySnapshot.child("postCate").getValue(String.class);
                                            String servArea = keySnapshot.child("servingArea").getValue(String.class);
                                            String visibilityCount = keySnapshot.child("visibilityCount").getValue(String.class);
                                            String clickCount = keySnapshot.child("clickCount").getValue(String.class);

                                            postCategories.add(postKeys);
                                            Log.d("efdsvrw", String.valueOf(postCategories));

                                            for (String hashtag : postCategories) {
                                                // Remove '#' symbol from each hashtag
                                                String extractedText = hashtag.replaceAll("#", "").trim();
                                                extractedTexts.add(extractedText);
                                            }
                                            for (String text : extractedTexts) {
                                                System.out.println("4wwfdsvx "+text);

                                            }

                                            Log.d("ergdetbf", String.valueOf(extractedTexts));
                                            PostModel postModel = new PostModel();
                                            postModel.setPostId(key);
                                            postModel.setPostDesc(postDesc);
                                            postModel.setPostType(postType);
                                            postModel.setPostImg(postImg);
                                            postModel.setPostKeys(postKeys);
                                            postModel.setPostCate(postCate);
                                            postModel.setPostcontactKey(contactNumber);
                                            postModel.setPostStatus(status);
                                            postModel.setPostvisibilityCount(visibilityCount);
                                            postModel.setPostclickCount(clickCount);

                                            Log.d("fsfsfdsdn", "" + shopname);

                                            postModel.setPostUser(shopname);
                                            postModel.setUserImg(shopimagex);
                                            postModel.setUserAdd(shopaddress);

                                            homeItemList.add(postModel);
                                            lottieAnimationView.setVisibility(View.GONE);

                                        }
                                        if (x++ == snapshotx.getChildrenCount() - 1) {
                                            getProductData(homeItemList);
                                            Log.d("fsfsfdsdn", "Ok 1");
                                        }
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

    }

    public void getProductData(List<Object> ss){
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Separate list for Products
                    List<OrderModel> productItemList = new ArrayList<>();

                    for (DataSnapshot contactNumberSnapshot : snapshot.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot productSnapshot : contactNumberSnapshot.getChildren()) {
                            String productId = productSnapshot.getKey();
                            String status = productSnapshot.child("status").getValue(String.class);
                            System.out.println("ddsfvd " +status);
                            if (status.equals("Posted")){
                            String itemName = productSnapshot.child("itemname").getValue(String.class);
                            String price = productSnapshot.child("price").getValue(String.class);
                            String sell = productSnapshot.child("sell").getValue(String.class);
                            String description = productSnapshot.child("description").getValue(String.class);
                            String itemKey = productSnapshot.child("itemkey").getValue(String.class);
                            String offer = productSnapshot.child("offer").getValue(String.class);
                            String firstimage = productSnapshot.child("firstImageUrl").getValue(String.class);
                            String sellprice = productSnapshot.child("sell").getValue(String.class);
                            String shopContactNumber = productSnapshot.child("shopContactNumber").getValue(String.class);
                            String wholesale = productSnapshot.child("wholesale").getValue(String.class);
                            String minqty = productSnapshot.child("minquantity").getValue(String.class);
                            String servArea = productSnapshot.child("servingArea").getValue(String.class);

                            String proCate = productSnapshot.child("itemCate").getValue(String.class);
                            String proKeys = productSnapshot.child("itemKeys").getValue(String.class);

                            System.out.println("wedfsrddf " +minqty);

                            List<String> imageUrls = new ArrayList<>();
                            DataSnapshot imageUrlsSnapshot = productSnapshot.child("imageUrls");
                            for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                String imageUrl = imageUrlSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                }
                            }

                            OrderModel orderModel=new OrderModel();
                            orderModel.setProdId(itemKey);
                            orderModel.setProdName(itemName);
                            orderModel.setOffer(offer);
                            orderModel.setProImg(firstimage);
                            orderModel.setProDesc(description);
                            orderModel.setProprice(price);
                            orderModel.setProsell(sellprice);
                            orderModel.setShopContactNum(shopContactNumber);
                            orderModel.setImagesUrls(imageUrls);
                            orderModel.setWholesale(wholesale);
                            orderModel.setMinqty(minqty);
                            orderModel.setProCate(proCate);
                            orderModel.setProKeys(proKeys);
                            productItemList.add(orderModel);

                            }
                        }
                    }

                    Log.d("fdafdsfgdsf",""+ss.size());

                    // Merge the lists in a specific sequence
                    int i = 0;
                    int j = 0;
                    while (i < ss.size() && j < productItemList.size()) {
                        // Insert BusinessPosts item
                        ss.add(i, productItemList.get(j));
                        i += 2;  // Increment by 2 to insert Product item next
                        j++;
                    }

                    // If there are remaining Product items, add them at the end
                    while (j < productItemList.size()) {
                        ss.add(productItemList.get(j));
                        j++;
                    }

                    Collections.shuffle(ss);

                    // Notify adapter or update UI as needed...
                    homeMultiAdapter = new HomeMultiAdapter(true);
                    informationrecycerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    homeMultiAdapter = new HomeMultiAdapter(homeItemList, FragmentHome.this, getContext());
                    informationrecycerview.setAdapter(homeMultiAdapter);
                    RecyclerView.ViewHolder viewHolder = informationrecycerview.findViewHolderForAdapterPosition(i);
                    if (viewHolder instanceof HomeMultiAdapter.PostItemViewHolder) {
                        informationrecycerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                                if (layoutManager != null) {
                                    // Get the first visible item position and last visible item position
                                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                                    // Iterate through the visible items and update the visibility count on each post
                                    for (int i = 0; i < homeItemList.size(); i++) {
                                        Object post = homeItemList.get(i);
                                        if (post instanceof PostModel) {
                                            HomeMultiAdapter.PostItemViewHolder viewHolder = (HomeMultiAdapter.PostItemViewHolder) recyclerView
                                                    .findViewHolderForAdapterPosition(i);

                                            if (viewHolder != null && i < firstVisibleItemPosition
                                                    && i > lastVisibleItemPosition) {
                                                // Reset the visibility count flag for posts that are not visible
                                                viewHolder.resetVisibilityCountFlag();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }

                    homeMultiAdapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }


    public void showCategoryF(){
        // Inflate the layout for the BottomSheetDialog
        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheet, null);

        // Customize the BottomSheetDialog as needed
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bottomSheetView);

        // Disable scrolling for the BottomSheetDialog
        // Disable scrolling for the BottomSheetDialog
        // Set the peek height to disable scrolling when content is not tall enough
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        // Handle views inside the BottomSheetDialog
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView category = bottomSheetView.findViewById(R.id.category);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView subcategory = bottomSheetView.findViewById(R.id.subcategory);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView = bottomSheetView.findViewById(R.id.closesss);
        category.setLayoutManager(new LinearLayoutManager(getContext()));
        subcategory.setLayoutManager(new LinearLayoutManager(getContext()));

        if(categoryModels != null){
            categoryModels.clear();

            if(categoryAdapter!=null){
                categoryAdapter.notifyDataSetChanged();
            }
        }

        categoryModels=new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Categories");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int x=0;
                    for (DataSnapshot dsp:dataSnapshot.getChildren()) {
                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.setCname(dsp.getKey());
                        categoryModel.setCimg(String.valueOf(dsp.child("Img").getValue()));
                        categoryModels.add(categoryModel);
                        categoryAdapter = new CategoryAdapter(getContext(), categoryModels, FragmentHome.this);

                        Log.d("fsdfdf", "key: " + dsp.getKey() + " img : " + String.valueOf(dsp.child("Img").getValue()));

                        category.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();

                    }

                }else {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        // Set the click listener for the adapter



        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                CategoryModel clickedCategory = categoryModels.get(position);
                String categoryName = clickedCategory.getCname();
                String categoryImage = clickedCategory.getCimg();

                // Handle item click here
                // For example, you can show a toast with the clicked category information
                Toast.makeText(getContext(), "Clicked on category: " + categoryName, Toast.LENGTH_SHORT).show();
                // Handle item click here (if needed)

                if(categoryModels != null){
                    categoryModels.clear();

                    if(categoryAdapter!=null){
                        categoryAdapter.notifyDataSetChanged();
                    }
                }
                categoryModels=new ArrayList<>();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference("Categories");

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            int x=0;
                            for (DataSnapshot dsp:dataSnapshot.getChildren()) {
                                CategoryModel categoryModel = new CategoryModel();
                                categoryModel.setCname(dsp.getKey());
                                categoryModel.setCimg(String.valueOf(dsp.child("Img").getValue()));
                                categoryModels.add(categoryModel);
                                categoryAdapter = new CategoryAdapter(getContext(), categoryModels, FragmentHome.this);

                                Log.d("fsdfdf", "key: " + dsp.getKey() + " img : " + String.valueOf(dsp.child("Img").getValue()));

                                category.setAdapter(categoryAdapter);
                                categoryAdapter.notifyDataSetChanged();
                            }

                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Log.w(TAG, "onCancelled", databaseError.toException());
                    }
                });

            }
        });


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click inside the BottomSheetDialog
                bottomSheetDialog.dismiss();
            }
        });

        // Show the BottomSheetDialog
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

    }

    public void showCategory(){
        // Inflate the layout for the BottomSheetDialog
        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.bottomsheet, null);

        // Customize the BottomSheetDialog as needed
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bottomSheetView);

        // Disable scrolling for the BottomSheetDialog
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        // Handle views inside the BottomSheetDialog
        RecyclerView category = bottomSheetView.findViewById(R.id.category);
       // RecyclerView subcategory = bottomSheetView.findViewById(R.id.subcategory);

        TextView textView = bottomSheetView.findViewById(R.id.closesss);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView ccdd = bottomSheetView.findViewById(R.id.xxxxx);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView header = bottomSheetView.findViewById(R.id.headress);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout flay = bottomSheetView.findViewById(R.id.fullay);


        category.setLayoutManager(new LinearLayoutManager(getContext()));
       //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3); // Change 2 to the desired number of columns
      //  subcategory.setLayoutManager(gridLayoutManager);

        if(categoryModels != null){
            categoryModels.clear();

            if(categoryAdapter != null){
                categoryAdapter.notifyDataSetChanged();
            }
        }

        //

        categoryModels = new ArrayList<>();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Categories");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot dsp:dataSnapshot.getChildren()) {
                        CategoryModel categoryModel = new CategoryModel();
                        categoryModel.setCname(dsp.getKey());
                        categoryModel.setCimg(String.valueOf(dsp.child("Img").getValue()));
                        categoryModel.setDetails(String.valueOf(dsp.child("Details").getValue()));
                        categoryModel.setKeywords(String.valueOf(dsp.child("Keywords").getValue()));
                        categoryModels.add(categoryModel);
                    }

                    // Instantiate the adapter after the loop
                    categoryAdapter = new CategoryAdapter(getContext(), categoryModels,FragmentHome.this);

                    // Set the click listener for the adapter
                    categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            CategoryModel clickedCategory = categoryModels.get(position);
                            String categoryName = clickedCategory.getCname();
                           // String categoryImage = clickedCategory.getCimg();
                            //String details=clickedCategory.getDetails();
                            String keys=clickedCategory.getKeywords();


                            // Handle item click here
                            // For example, you can show a toast with the clicked category information
                            //Toast.makeText(getContext(), "Clicked on category: " + categoryName, Toast.LENGTH_SHORT).show();

                            header.setText(categoryName);
                            flay.setVisibility(View.VISIBLE);
                           // subcategory.setVisibility(View.VISIBLE);
                            ccdd.setVisibility(View.GONE);

                            searchedittext.setText(categoryName);
                            String query="";
                            // Filter data based on keywords
                            //Toast.makeText(getActivity(), ""+keys, Toast.LENGTH_SHORT).show();


                            searchByCategory(categoryName);

                            // filterpostAndItems(query,keys);


                            bottomSheetDialog.dismiss();

//                            if(subCategoryModels != null){
//                                subCategoryModels.clear();
//
//                                if(subCateAdapter != null){
//                                    subCateAdapter.notifyDataSetChanged();
//                                }
//                            }
//                            subCategoryModels = new ArrayList<>();

//                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                            DatabaseReference ref = database.getReference("Categories/"+categoryName+"/Sub/");
//
//                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    if(dataSnapshot.exists()){
//                                        for (DataSnapshot dsp:dataSnapshot.getChildren()) {
//                                            SubCategoryModel categoryModel = new SubCategoryModel();
//                                            categoryModel.setSubName(dsp.getKey());
//                                            categoryModel.setSubImg(String.valueOf(dsp.child("Img").getValue()));
//                                            categoryModel.setKeywords(String.valueOf(dsp.child("Keywords").getValue()));
//                                            subCategoryModels.add(categoryModel);
//                                        }
//
//                                        // Instantiate the adapter after the loop
//                                        subCateAdapter = new SubCateAdapter(getContext(), subCategoryModels,FragmentHome.this);
//
//                                        // Set the click listener for the adapter
//                                        subCateAdapter.setOnItemClickListener(new SubCateAdapter.OnItemClickListener() {
//                                            @Override
//                                            public void onItemClick(int position) {
//                                                SubCategoryModel clickedCategory = subCategoryModels.get(position);
//                                                String categoryName = clickedCategory.getSubName();
//                                                String categoryImage = clickedCategory.getSubImg();
//                                                String keywords = clickedCategory.getKeywords();
//
//                                                Log.d("acfxea",keywords);
//                                                // Set the clicked data on the EditText
//                                                searchedittext.setText(categoryName);
//                                                String query="";
//                                                // Filter data based on keywords
//                                                filterpostAndItems(query,keywords);
//
//                                                // Dismiss the BottomSheetDialog
//                                                bottomSheetDialog.dismiss();
//                                            }
//                                        });
//
//                                        // Set the adapter to the RecyclerView
//                                        subcategory.setAdapter(subCateAdapter);
//                                    }
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                    // Log.w(TAG, "onCancelled", databaseError.toException());
//                                }
//                            });


                            // Handle item click here (if needed)
                        }
                    });

                    // Set the adapter to the RecyclerView
                    category.setAdapter(categoryAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle click inside the BottomSheetDialog
                bottomSheetDialog.dismiss();
            }
        });

        // Show the BottomSheetDialog
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();

    }





    private void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Geocode the location to get postal code
                        geocodeLocation(latitude, longitude);
                    } else {
                        Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(requireActivity(), e -> {
                    // Handle failure
                    Toast.makeText(requireContext(), "Location request failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void statuswithlocation(String dd){

        if (checkstring.equals("rdjob")) {
            filterjobpost(dd);
        }else if(checkstring.equals("rdbiz")) {
            ClearAllHome();
            LoadHomeDataNewByLocation();
        } else if (checkstring.equals("rdemployee")) {
            filterEmployee(dd);
        }

    }

    private void statuswithglobal(){
        if (checkstring.equals("rdjob")) {
            ClearAll();
            jobPostAdapter = new JobPostAdapter(jobDetailsList, getContext(), sharedPreference, FragmentHome.this);
            jobrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            jobrecyclerview.setAdapter(jobPostAdapter);
            retrieveJobPostDetails();
        }else if(checkstring.equals("rdbiz")) {
            ClearAllHome();
            LoadHomeDataNewTest();
        } else if (checkstring.equals("rdemployee")) {
            ClearAllEmployee();
            employeeAdapter = new EmployeeAdapter(employeeDetailsList, getContext(), sharedPreference);
            employeerecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
            employeerecyclerview.setAdapter(employeeAdapter);
            retrieveEmployeeDetails();
        }
    }

    private void geocodeLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                String postalCode = addresses.get(0).getPostalCode();
                String dd = addresses.get(0).getLocality();
                Log.d("fsdafda","pin code : "+postalCode+"\n Locality: "+addresses.get(0).getLocality()+"\n Sublocaltiy : "+addresses.get(0).getSubLocality()+"\n Admin Area : "+addresses.get(0).getAdminArea()+"\n Address: "+addresses.get(0).getAddressLine(0));
                // Use the postal code as needed
                location.setText(dd);
                statuswithlocation(dd);
//                if (checkstring.equals("notbiz")){
//                    if (switchUser.equals("user")){
//                        filterjobpost(dd);
//                    }else {
//                        filterEmployee(dd);
//                    }
//                } else {
//                    ClearAllHome();
//                    LoadHomeDataNewByLocation();
//                }

               // Toast.makeText(getActivity(), "Postal Code: " + dd, Toast.LENGTH_SHORT).show();

            } else {
                location.setText("Global");
                statuswithglobal();
                //Toast.makeText(getActivity(), "No address found", Toast.LENGTH_SHORT).show();
//                if (checkstring.equals("notbiz")){
//                    if (switchUser.equals("user")){
//                        retrieveJobPostDetails();
//                    }else {
//                        retrieveEmployeeDetails();
//                    }
//                } else {
//                    ClearAllHome();
//                    LoadHomeDataNewTest();
//                }


            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle geocoding error
            Toast.makeText(getActivity(), "Geocoding failed", Toast.LENGTH_SHORT).show();
            statuswithglobal();
//            if (checkstring.equals("notbiz")){
//                if (switchUser.equals("user")){
//                    retrieveJobPostDetails();
//                }else {
//                    retrieveEmployeeDetails();
//                }
//            } else {
//                ClearAllHome();
//                LoadHomeDataNewTest();
//            }
        }
    }

    public void LoadHomeDataNewByLocation() {
        ClearAllHome();
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x = 0;
                    for (DataSnapshot contactNumberSnapshot : snapshotx.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            String key = keySnapshot.getKey();
                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        shopname = snapshot.child("shopName").getValue(String.class);
                                        shopimagex = snapshot.child("url").getValue(String.class);
                                        shopaddress = snapshot.child("address").getValue(String.class);

                                        String servArea = keySnapshot.child("servingArea").getValue(String.class);

                                     //   assert servArea != null;

                                        if(servArea == null){
                                            servArea="-";
                                        }

                                        boolean isMatch = StringSplit.matchStrings(servArea.toLowerCase(), location.getText().toString().toLowerCase());

                                        // Check serving area conditionser
                                        if (isMatch) {
                                            String status = keySnapshot.child("status").getValue(String.class);
                                            System.out.println("ewdvetfd " +status);
                                            if (status.equals("Posted")) {
                                                //Toast.makeText(getContext(), "Ok1", Toast.LENGTH_SHORT).show();
                                                String postImg = keySnapshot.child("postImg").getValue(String.class);
                                                String postDesc = keySnapshot.child("postDesc").getValue(String.class);
                                                String postType = keySnapshot.child("postType").getValue(String.class);
                                                String postKeys = keySnapshot.child("postKeys").getValue(String.class);
                                                String postCate = keySnapshot.child("postCate").getValue(String.class);
                                                String visibilityCount = keySnapshot.child("visibilityCount").getValue(String.class);
                                                String clickCount = keySnapshot.child("clickCount").getValue(String.class);

                                                postCategories.add(postKeys);
                                                Log.d("efdsvrw", String.valueOf(postCategories));


                                                for (String hashtag : postCategories) {
                                                    // Remove '#' symbol from each hashtag
                                                    String extractedText = hashtag.replaceAll("#", "").trim();
                                                    extractedTexts.add(extractedText);
                                                }
                                                for (String text : extractedTexts) {
                                                    System.out.println("4wwfdsvx "+text);

                                                }
                                                Log.d("ergdetbf", String.valueOf(extractedTexts));
                                                PostModel postModel = new PostModel();
                                                postModel.setPostId(key);
                                                postModel.setPostDesc(postDesc);
                                                postModel.setPostType(postType);
                                                postModel.setPostImg(postImg);
                                                postModel.setPostKeys(postKeys);
                                                postModel.setPostCate(postCate);
                                                postModel.setPostcontactKey(contactNumber);
                                                postModel.setPostStatus(status);
                                                postModel.setPostvisibilityCount(visibilityCount);
                                                postModel.setPostclickCount(clickCount);

                                                Log.d("fsfsfdsdn", "" + shopname);


                                                postModel.setPostUser(shopname);
                                                postModel.setUserImg(shopimagex);
                                                postModel.setUserAdd(shopaddress);

                                                homeItemList.add(postModel);
                                            }
                                        }

                                        if (x++ == snapshotx.getChildrenCount() - 1) {
                                            getProductDataX(homeItemList);
                                           // Toast.makeText(getContext(), "shopname"+ shopname, Toast.LENGTH_SHORT).show();
                                            Log.d("fsfsfdsdn", "Ok 1");
                                        }
                                        lottieAnimationView.setVisibility(View.GONE);
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void getProductDataX(List<Object> ss) {
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Separate list for Products
                    List<OrderModel> productItemList = new ArrayList<>();

                    for (DataSnapshot contactNumberSnapshot : snapshot.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot productSnapshot : contactNumberSnapshot.getChildren()) {
                            String productId = productSnapshot.getKey();

                            String servArea = productSnapshot.child("servingArea").getValue(String.class);
                            String status = productSnapshot.child("status").getValue(String.class);

                            //assert servArea != null;
                            System.out.println("wesdv " +productId);

                            if(servArea == null){
                                servArea="-";
                            }

                         //   Toast.makeText(getContext(), "Ok2 "+servArea, Toast.LENGTH_SHORT).show();

                            boolean isMatch = StringSplit.matchStrings(servArea.toLowerCase(), location.getText().toString().toLowerCase());
                            // Check serving area condition
                            if (isMatch) {
                                System.out.println("dsfrwDFS " +status);
                                if (status.equals("Posted")) {
                                 //   Toast.makeText(getContext(), "status = " +status, Toast.LENGTH_SHORT).show();
                                    //  Toast.makeText(getContext(), "Ok2", Toast.LENGTH_SHORT).show();
                                    String itemName = productSnapshot.child("itemname").getValue(String.class);
                                    String price = productSnapshot.child("price").getValue(String.class);
                                    String sell = productSnapshot.child("sell").getValue(String.class);
                                    String description = productSnapshot.child("description").getValue(String.class);
                                    String itemKey = productSnapshot.child("itemkey").getValue(String.class);
                                    String offer = productSnapshot.child("offer").getValue(String.class);
                                    String firstimage = productSnapshot.child("firstImageUrl").getValue(String.class);
                                    String sellprice = productSnapshot.child("sell").getValue(String.class);
                                    String shopContactNumber = productSnapshot.child("shopContactNumber").getValue(String.class);
                                    String wholesale = productSnapshot.child("wholesale").getValue(String.class);
                                    String minqty = productSnapshot.child("minquantity").getValue(String.class);

                                    String proCate = productSnapshot.child("itemCate").getValue(String.class);
                                    String proKeys = productSnapshot.child("itemKeys").getValue(String.class);
                                    /// String servArea = productSnapshot.child("servingArea").getValue(String.class);

                                    List<String> imageUrls = new ArrayList<>();
                                    DataSnapshot imageUrlsSnapshot = productSnapshot.child("imageUrls");
                                    for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                        String imageUrl = imageUrlSnapshot.getValue(String.class);
                                        if (imageUrl != null) {
                                            imageUrls.add(imageUrl);
                                        }
                                    }

                                    OrderModel orderModel = new OrderModel();
                                    orderModel.setProdId(itemKey);
                                    orderModel.setProdName(itemName);
                                    orderModel.setOffer(offer);
                                    orderModel.setProImg(firstimage);
                                    orderModel.setProDesc(description);
                                    orderModel.setProprice(price);
                                    orderModel.setProsell(sellprice);
                                    orderModel.setShopContactNum(shopContactNumber);
                                    orderModel.setImagesUrls(imageUrls);
                                    orderModel.setWholesale(wholesale);
                                    orderModel.setMinqty(minqty);
                                    orderModel.setProCate(proCate);
                                    orderModel.setProKeys(proKeys);
                                    productItemList.add(orderModel);

                                }
                            }
                        }
                    }

                    Log.d("fdafdsfgdsf", "" + ss.size());

                    // Merge the lists in a specific sequence
                    int i = 0;
                    int j = 0;
                    while (i < ss.size() && j < productItemList.size()) {
                        // Insert BusinessPosts item
                        ss.add(i, productItemList.get(j));
                        i += 2;  // Increment by 2 to insert Product item next
                        j++;
                    }

                    // If there are remaining Product items, add them at the end
                    while (j < productItemList.size()) {
                        ss.add(productItemList.get(j));
                        j++;
                    }

                    Collections.shuffle(ss);

                    // Notify adapter or update UI as needed...
                    homeMultiAdapter = new HomeMultiAdapter(true);
                    informationrecycerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    homeMultiAdapter = new HomeMultiAdapter(homeItemList, FragmentHome.this, getContext());
                    informationrecycerview.setAdapter(homeMultiAdapter);
                    RecyclerView.ViewHolder viewHolder = informationrecycerview.findViewHolderForAdapterPosition(i);
                    if (viewHolder instanceof HomeMultiAdapter.PostItemViewHolder) {
                        informationrecycerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                                if (layoutManager != null) {
                                    // Get the first visible item position and last visible item position
                                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                                    // Iterate through the visible items and update the visibility count on each post
                                    for (int i = 0; i < homeItemList.size(); i++) {
                                        Object post = homeItemList.get(i);
                                        if (post instanceof PostModel) {
                                            HomeMultiAdapter.PostItemViewHolder viewHolder = (HomeMultiAdapter.PostItemViewHolder) recyclerView
                                                    .findViewHolderForAdapterPosition(i);
                                            if (viewHolder != null && i < firstVisibleItemPosition
                                                    && i > lastVisibleItemPosition) {
                                                // Reset the visibility count flag for posts that are not visible
                                                viewHolder.resetVisibilityCountFlag();

                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                    homeMultiAdapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void setLocation(){
        // Inflate the layout for the BottomSheetDialog
        View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.locate, null);

        // Customize the BottomSheetDialog as needed
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(bottomSheetView);

        // Disable scrolling for the BottomSheetDialog
       // BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
     //   behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        // Handle views inside the BottomSheetDialog
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView myLoc = bottomSheetView.findViewById(R.id.myLoc);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView globLoc = bottomSheetView.findViewById(R.id.globLoc);

        myLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check and request location permissions if not granted
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);
                } else {
                    // Permissions already granted, proceed to get location
                    getLocation();
                    bottomSheetDialog.dismiss();
                }
            }
        });

        globLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location.setText("Global");
                statuswithglobal();
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();

    }

    private void updatePostVisibilityCount(String postId, String postcontactNumber) {
        // Use Firebase reference and update the visibility count
        // For example, assuming you have a DatabaseReference named postRef

        System.out.println("wgdef " +postId);

        DatabaseReference postVisibilityRef = FirebaseDatabase.getInstance()
                .getReference("BusinessPosts").child(postcontactNumber)
                .child(postId)
                .child("visibilityCount");

        // Retrieve the current count from Firebase
        postVisibilityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentCountString = snapshot.getValue(String.class);

                // Increment the count and update it in Firebase
                int currentCount = Integer.parseInt(currentCountString);

                // Increment the count and update it in Firebase
                postVisibilityRef.setValue(String.valueOf(currentCount + 1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void filterBySearch(String query) {
        query = query.toLowerCase().trim();
        filteredhomeItemList.clear();

        for (Object item : homeItemList) {
            if (item instanceof PostModel) {
                PostModel currentPost = (PostModel) item;
                // Check if the post's postKeys contain the query
                String postKeys = currentPost.getPostKeys();
                if (postKeys != null && postKeys.toLowerCase().contains(query)) {
                    filteredhomeItemList.add(item);
                }
            } else if (item instanceof OrderModel) {
                OrderModel currentProduct = (OrderModel) item;
                // Check if the product's proKeys contain the query
                String proKeys = currentProduct.getProKeys();
                if (proKeys != null && proKeys.toLowerCase().contains(query)) {
                    filteredhomeItemList.add(item);
                }
            }
        }
    }

    public void searchByCategory(String category){
        ClearAllHome();
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BusinessPosts");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    x=0;
                    for (DataSnapshot contactNumberSnapshot : snapshotx.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot keySnapshot : contactNumberSnapshot.getChildren()) {
                            String key = keySnapshot.getKey();
                            postCategories.clear();
                            extractedTexts.clear();
                            shopRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        String status = keySnapshot.child("status").getValue(String.class);

                                        if (status.equals("Posted")) {
                                            System.out.println("fdrethf " +status);
                                            shopname = snapshot.child("shopName").getValue(String.class);
                                            shopimagex = snapshot.child("url").getValue(String.class);
                                            shopaddress = snapshot.child("address").getValue(String.class);

                                            String postImg = keySnapshot.child("postImg").getValue(String.class);
                                            String postDesc = keySnapshot.child("postDesc").getValue(String.class);
                                            String postType = keySnapshot.child("postType").getValue(String.class);
                                            String postKeys = keySnapshot.child("postKeys").getValue(String.class);
                                            String postCate = keySnapshot.child("postCate").getValue(String.class);
                                            String servArea = keySnapshot.child("servingArea").getValue(String.class);
                                            String visibilityCount = keySnapshot.child("visibilityCount").getValue(String.class);
                                            String clickCount = keySnapshot.child("clickCount").getValue(String.class);

                                            postCategories.add(postKeys);
                                            Log.d("efdsvrw", String.valueOf(postCategories));

                                            for (String hashtag : postCategories) {
                                                // Remove '#' symbol from each hashtag
                                                String extractedText = hashtag.replaceAll("#", "").trim();
                                                extractedTexts.add(extractedText);
                                            }
                                            for (String text : extractedTexts) {
                                                System.out.println("4wwfdsvx "+text);

                                            }

                                            Log.d("ergdetbf", String.valueOf(extractedTexts));

                                            if(category.equals(postCate)){
                                                PostModel postModel = new PostModel();
                                                postModel.setPostId(key);
                                                postModel.setPostDesc(postDesc);
                                                postModel.setPostType(postType);
                                                postModel.setPostImg(postImg);
                                                postModel.setPostKeys(postKeys);
                                                postModel.setPostCate(postCate);
                                                postModel.setPostcontactKey(contactNumber);
                                                postModel.setPostStatus(status);
                                                postModel.setPostvisibilityCount(visibilityCount);
                                                postModel.setPostclickCount(clickCount);

                                                Log.d("fsfsfdsdn", "" + shopname);

                                                postModel.setPostUser(shopname);
                                                postModel.setUserImg(shopimagex);
                                                postModel.setUserAdd(shopaddress);

                                                homeItemList.add(postModel);
                                                lottieAnimationView.setVisibility(View.GONE);
                                            }

                                        }
                                        if (x++ == snapshotx.getChildrenCount() - 1) {
                                            //getProductData(homeItemList);
                                            getProductDataByCategory(homeItemList,category);
                                            Log.d("fsfsfdsdn", "Ok 1");
                                        }
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    public void getProductDataByCategory(List<Object> ss,String category){
        lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products");
        productRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Separate list for Products
                    List<OrderModel> productItemList = new ArrayList<>();

                    for (DataSnapshot contactNumberSnapshot : snapshot.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot productSnapshot : contactNumberSnapshot.getChildren()) {
                            String productId = productSnapshot.getKey();
                            String status = productSnapshot.child("status").getValue(String.class);
                            System.out.println("ddsfvd " +status);
                            if (status.equals("Posted")){
                                String itemName = productSnapshot.child("itemname").getValue(String.class);
                                String price = productSnapshot.child("price").getValue(String.class);
                                String sell = productSnapshot.child("sell").getValue(String.class);
                                String description = productSnapshot.child("description").getValue(String.class);
                                String itemKey = productSnapshot.child("itemkey").getValue(String.class);
                                String offer = productSnapshot.child("offer").getValue(String.class);
                                String firstimage = productSnapshot.child("firstImageUrl").getValue(String.class);
                                String sellprice = productSnapshot.child("sell").getValue(String.class);
                                String shopContactNumber = productSnapshot.child("shopContactNumber").getValue(String.class);
                                String wholesale = productSnapshot.child("wholesale").getValue(String.class);
                                String minqty = productSnapshot.child("minquantity").getValue(String.class);
                                String servArea = productSnapshot.child("servingArea").getValue(String.class);

                                String proCate = productSnapshot.child("itemCate").getValue(String.class);
                                String proKeys = productSnapshot.child("itemKeys").getValue(String.class);

                                System.out.println("wedfsrddf " +minqty);

                                List<String> imageUrls = new ArrayList<>();
                                DataSnapshot imageUrlsSnapshot = productSnapshot.child("imageUrls");
                                for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                                    String imageUrl = imageUrlSnapshot.getValue(String.class);
                                    if (imageUrl != null) {
                                        imageUrls.add(imageUrl);
                                    }
                                }

                                if(category.equals(proCate)){
                                    OrderModel orderModel=new OrderModel();
                                    orderModel.setProdId(itemKey);
                                    orderModel.setProdName(itemName);
                                    orderModel.setOffer(offer);
                                    orderModel.setProImg(firstimage);
                                    orderModel.setProDesc(description);
                                    orderModel.setProprice(price);
                                    orderModel.setProsell(sellprice);
                                    orderModel.setShopContactNum(shopContactNumber);
                                    orderModel.setImagesUrls(imageUrls);
                                    orderModel.setWholesale(wholesale);
                                    orderModel.setMinqty(minqty);
                                    orderModel.setProCate(proCate);
                                    orderModel.setProKeys(proKeys);
                                    productItemList.add(orderModel);
                                }
                            }
                        }
                    }

                    Log.d("fdafdsfgdsf",""+ss.size());

                    // Merge the lists in a specific sequence
                    int i = 0;
                    int j = 0;
                    while (i < ss.size() && j < productItemList.size()) {
                        // Insert BusinessPosts item
                        ss.add(i, productItemList.get(j));
                        i += 2;  // Increment by 2 to insert Product item next
                        j++;
                    }

                    // If there are remaining Product items, add them at the end
                    while (j < productItemList.size()) {
                        ss.add(productItemList.get(j));
                        j++;
                    }

                    Collections.shuffle(ss);

                    // Notify adapter or update UI as needed...

                    informationrecycerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    homeMultiAdapter = new HomeMultiAdapter(homeItemList, FragmentHome.this, getContext());
                    informationrecycerview.setAdapter(homeMultiAdapter);
                    RecyclerView.ViewHolder viewHolder = informationrecycerview.findViewHolderForAdapterPosition(i);
                    if (viewHolder instanceof HomeMultiAdapter.PostItemViewHolder) {
                        informationrecycerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                                if (layoutManager != null) {
                                    // Get the first visible item position and last visible item position
                                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                                    // Iterate through the visible items and update the visibility count on each post
                                    for (int i = 0; i < homeItemList.size(); i++) {
                                        Object post = homeItemList.get(i);
                                        if (post instanceof PostModel) {
                                            HomeMultiAdapter.PostItemViewHolder viewHolder = (HomeMultiAdapter.PostItemViewHolder) recyclerView
                                                    .findViewHolderForAdapterPosition(i);

                                            if (viewHolder != null && i < firstVisibleItemPosition
                                                    && i > lastVisibleItemPosition) {
                                                // Reset the visibility count flag for posts that are not visible
                                                viewHolder.resetVisibilityCountFlag();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }

                    homeMultiAdapter.notifyDataSetChanged();
                    lottieAnimationView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }


    public interface StatusCallback {
        void onStatusChecked(boolean status);
    }

    public void checkStatus(final StatusCallback callback){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                boolean status = false; // Default status
                if(snapshot.exists()){
                    String expDateStr = snapshot.child("ExpDate").getValue(String.class);
                    if (expDateStr != null) {
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            Date expDate = dateFormat.parse(expDateStr);
                            Date currentDate = new Date();

                            // Compare expiration date with current date
                            if (currentDate.after(expDate)) {
                                // Expiration date has passed, set status to false
                                userRef.child("premium").setValue(false);
                                status = false;
                            } else {
                                // Expiration date is still valid, set status to true
                                userRef.child("premium").setValue(true);
                                status = true;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Handle case when user data doesn't exist
                }
                callback.onStatusChecked(status);
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled event
                callback.onStatusChecked(false); // Default status if there's an error
            }
        });
    }

    private boolean shouldShowDialog() {
        // Get the last time the dialog was shown
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME2, Context.MODE_PRIVATE);
        long lastDialogTime = prefs.getLong(LAST_DIALOG_TIME, 0);

        // Check if a day has passed since the last dialog was shown
        long currentTime = Calendar.getInstance().getTimeInMillis();
        long oneDayInMillis = 24 * 60 * 60 * 1000; // One day in milliseconds
        return currentTime - lastDialogTime >= oneDayInMillis;
    }

    private void showPremiumDialog() {
        // Show the dialog
        Dialog dialog1 = new Dialog(getActivity());
        // Inflate the custom layout
        dialog1.setContentView(R.layout.getpremium);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button upgrade = dialog1.findViewById(R.id.upgrade);

        dialog1.show();

        upgrade.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(getActivity(), PremiumMembership.class);
                 startActivity(intent);
             }
        });

        // Save the current time as the last time the dialog was shown
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME2, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(LAST_DIALOG_TIME, Calendar.getInstance().getTimeInMillis());
        editor.apply();
    }

}

