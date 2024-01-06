package com.spark.swarajyabiz.ui;

import static android.content.Context.MODE_PRIVATE;

import static com.facebook.FacebookSdk.getCacheDir;
import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import androidx.cardview.widget.CardView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.HomeMultiAdapter;
import com.spark.swarajyabiz.BannerDetails;
import com.spark.swarajyabiz.BuildConfig;
import com.spark.swarajyabiz.Business;
import com.spark.swarajyabiz.ChatJob;
import com.spark.swarajyabiz.ImageAdapter;
import com.spark.swarajyabiz.ItemDetails;
import com.spark.swarajyabiz.ItemList;
import com.spark.swarajyabiz.JobChat;
import com.spark.swarajyabiz.JobDetails;
import com.spark.swarajyabiz.JobPostAdapter;
import com.spark.swarajyabiz.JobPostDetails;
import com.spark.swarajyabiz.ModelClasses.OrderModel;
import com.spark.swarajyabiz.ModelClasses.PostModel;
import com.spark.swarajyabiz.PlaceOrder;
import com.spark.swarajyabiz.Post;
import com.spark.swarajyabiz.PostAdapter;
import com.spark.swarajyabiz.ProgressBarClass;
import com.spark.swarajyabiz.R;
import com.spark.swarajyabiz.Shop;
import com.spark.swarajyabiz.ShopDetails;
import com.spark.swarajyabiz.ShowAllItemsList;
import com.spark.swarajyabiz.ViewPagerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.annotations.NonNull;

public class FragmentHome extends Fragment implements PostAdapter.PostClickListener, JobPostAdapter.OnClickListener,
                                       HomeMultiAdapter.OnViewDetailsClickListener{

    private RecyclerView recyclerView, jobpostrecyclerview, informationrecycerview;
    HomeMultiAdapter homeMultiAdapter;
    private List<Post> postList = new ArrayList<>(); // Create a list to store post details
    private List<ItemList> itemList = new ArrayList<>(); // Create a list to store post details
    private List<ItemList> filteredList = new ArrayList<>();

    private List<JobDetails> jobDetailsList ;
    private List<JobDetails> filteredjobpostlist;
    private List<ChatJob> chatJobList;
    ImageView searchImage;
    private PostAdapter postAdapter;
    CardView searchcard;
    List<Shop> shopList;
    String shopcontactNumber, taluka,address, shopName, shopimage, destrict;
    private List<ItemList> originalItemList; // Keep a copy of the original list
    private int lastDisplayedIndex = -1;
    FrameLayout frameLayout;
    ImageView adimagecancel;
    private boolean imageShown = false;
    DatabaseReference userRef, shopRef;
    AlertDialog dialog;
    String userId, jobTitle, companyname, joblocation, jobtype, description, workplacetype, currentdate,
            postcontactNumber, jobid, experience, skills, salary, jobopenings;
    JobPostAdapter jobPostAdapter;
    TextView usernametextview;
    RadioButton businessradiobtn, jobradiobtn;
    RadioGroup radioGroup;
    EditText searchedittext;

    List<Object> homeItemList=new ArrayList<>();

    String shopname;
    String shopimagex;
    String shopaddress, checkstring="rdbiz";
    SwipeRefreshLayout swipeRefreshLayout;


    private LottieAnimationView lottieAnimationView;
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
        searchedittext = view.findViewById(R.id.searchedittext);
        radioGroup = view.findViewById(R.id.rdgrpx);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        lottieAnimationView = view.findViewById(R.id.lottieAnimationView);

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);



        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("ads");
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        // Initialize with -1 to start from the first image
        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            userRef.child(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkstring.equals("rdbiz")) {
                    ClearAll();
                    informationrecycerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    homeMultiAdapter = new HomeMultiAdapter(homeItemList, FragmentHome.this);
                    informationrecycerview.setAdapter(homeMultiAdapter);
                    // LoadHomeData();

                    LoadHomeDataNew();
                    swipeRefreshLayout.setRefreshing(false);
                } else {

                    ClearAll();
                    jobDetailsList = new ArrayList<>();
                    filteredjobpostlist = new ArrayList<>();
                    jobpostrecyclerview = view.findViewById(R.id.jobpostrecyclerview);
                    jobPostAdapter = new JobPostAdapter(jobDetailsList, getContext(), sharedPreference, FragmentHome.this);
                    informationrecycerview.setLayoutManager(new LinearLayoutManager(getContext()));
                    informationrecycerview.setAdapter(jobPostAdapter);
                    retrieveJobPostDetails();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });


        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
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


       // SearchView searchView = view.findViewById(R.id.searchview);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // Filter the RecyclerView based on the search query
//                filter(newText);
//                return true;
//            }
//        });
//
//        CardView searchCardView = view.findViewById(R.id.search);
//
//        searchCardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // Open the SearchView
//                searchView.setIconified(false);
//
//                // You can optionally set focus or handle any other actions you need here
//            }
//        });

//        informationrecycerview.setLayoutManager(new LinearLayoutManager(getContext()));
//        informationrecycerview.setAdapter(new HomeMultiAdapter(homeItemList));
//        LoadHomeData();

        businessradiobtn.setChecked(true);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if (null != rb) {
                    // checkedId is the RadioButton selected
                    switch (i) {
                        case R.id.rdbusiness:
                            // Do Something
                            checkstring = "rdbiz";
                            ClearAll();
                            informationrecycerview.setLayoutManager(new LinearLayoutManager(getContext()));
                            homeMultiAdapter = new HomeMultiAdapter(homeItemList, FragmentHome.this);
                            informationrecycerview.setAdapter(homeMultiAdapter);
                           // LoadHomeData();

                            LoadHomeDataNew();
                           // LoadHomeDataNewTest();
                            searchedittext.setText("");
                            searchedittext.setHint("व्यवसाय शोधा");
                            break;

                        case R.id.rdjob:
                            // Do Something
                            checkstring = "rdjob";

                            ClearAll();
                            jobDetailsList = new ArrayList<>();
                            filteredjobpostlist = new ArrayList<>();
                            jobpostrecyclerview = view.findViewById(R.id.jobpostrecyclerview);
                            jobPostAdapter = new JobPostAdapter(jobDetailsList, getContext(), sharedPreference, FragmentHome.this);
                            informationrecycerview.setLayoutManager(new LinearLayoutManager(getContext()));
                            informationrecycerview.setAdapter(jobPostAdapter);

                            retrieveJobPostDetails();
                            searchedittext.setText("");
                            searchedittext.setHint("नोकरी शोधा");
                            searchedittext.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    // Not needed for this implementation
                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                    // Filter the job post list based on the search query
                                    filterjobpost(charSequence.toString());
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    // Not needed for this implementation
                                }
                            });

                            break;

                    }
                }
            }
        });

      //  retrieveitemDetails();
        checkstring="rdbiz";
        ClearAll();
        informationrecycerview.setLayoutManager(new LinearLayoutManager(getContext()));
        homeMultiAdapter = new HomeMultiAdapter(homeItemList, FragmentHome.this);
        informationrecycerview.setAdapter(homeMultiAdapter);
        LoadHomeDataNew();

        return view;
    }

    private void ClearAll(){
        if(jobDetailsList != null){
            jobDetailsList.clear();

            if(jobPostAdapter!=null){
                jobPostAdapter.notifyDataSetChanged();
            }
        }
        jobDetailsList=new ArrayList<>();
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
        filteredjobpostlist.clear();

        if (TextUtils.isEmpty(query)) {
            // If the search query is empty, restore the original list
            filteredjobpostlist.addAll(jobDetailsList);
        } else {
            // Filter the list based on the search query
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

        // Update the RecyclerView with the filtered list
        jobPostAdapter.setData(filteredjobpostlist);
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

    @Override
    public void onClickClick(int position) {
        if (position >= 0 && position < filteredList.size()) {
            ItemList selectedItem = filteredList.get(position);

            String clickedShopcontactNumber = selectedItem.getShopcontactNumber();
            String shopimage = selectedItem.getShopimage();

            Intent intent = new Intent(getContext(), ShopDetails.class);
            intent.putExtra("contactNumber", clickedShopcontactNumber);
            intent.putExtra("image", shopimage);
            startActivity(intent);
        }
    }

    @Override
    public void onContactClick(int position) {



    }

    @Override
    public void onorderClick(int position) {

        ItemList selectedItem = null;

        if (position >= 0) {
            if (position < filteredList.size()) {
                selectedItem = filteredList.get(position);
            } else if (position < itemList.size()) {
                selectedItem = itemList.get(position);
            }
        }

        if (selectedItem != null) {
            String clickedShopcontactNumber = selectedItem.getShopcontactNumber();
            List<String> itemimages = selectedItem.getImagesUrls();
            String itemimage = selectedItem.getFirstImageUrl();
            String itemdescription = selectedItem.getDescription();
            String itemprice = selectedItem.getPrice();
            String itemname = selectedItem.getName();
            String itemkey = selectedItem.getItemkey();
            String shopName = selectedItem.getShopName();
            String district = selectedItem.getDistrict();
            String shopimage = selectedItem.getShopimage();
            String shoptaluka = selectedItem.getTaluka();
            String shopaddress = selectedItem.getAddress();
            Boolean flag = true;

            Intent intent = new Intent(getContext(), ItemDetails.class);
            intent.putExtra("itemName", itemname);
            intent.putExtra("firstImageUrl", itemimage);
            intent.putExtra("itemDescription", itemdescription);
            intent.putExtra("itemPrice", itemprice);
            intent.putExtra("itemKey", itemkey);
            intent.putExtra("contactNumber", clickedShopcontactNumber);
            intent.putExtra("shopName", shopName);
            intent.putExtra("district", district);
            intent.putExtra("shopimage", shopimage);
            intent.putExtra("taluka", shoptaluka);
            intent.putExtra("address", shopaddress);
            intent.putExtra("flag", flag);

            // Pass the list of item images
            intent.putStringArrayListExtra("itemImages", new ArrayList<>(itemimages));
            startActivity(intent);
        }
    }

    @Override
    public void oncallClick(int position) {

        if (position >= 0 && position < filteredList.size()) {
            String clickedShopcontactNumber = filteredList.get(position).getShopcontactNumber();
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + clickedShopcontactNumber));
            // Start the phone call activity
            startActivity(callIntent);
        }
    }

    @Override
    public void onseeallClick(int position) {

        ItemList selectedItem = null;

        if (position >= 0) {
            if (position < filteredList.size()) {
                selectedItem = filteredList.get(position);
            } else if (position < itemList.size()) {
                selectedItem = itemList.get(position);
            }
        }

        if (selectedItem != null) {

            String clickedShopcontactNumber = selectedItem.getShopcontactNumber();
            String shopName = selectedItem.getShopName();
            String shopimage = selectedItem.getShopimage();
            String district = selectedItem.getDistrict();
            String itemkey = selectedItem.getItemkey();

            Intent intent = new Intent(getContext(), ShowAllItemsList.class);
            intent.putExtra("shopName", shopName);
            intent.putExtra("shopImage", shopimage);
            intent.putExtra("contactNumber", clickedShopcontactNumber);
            intent.putExtra("district", district);
            intent.putExtra("itemKey", itemkey);
            startActivity(intent);
        }
    }

    // Method to retrieve post details from Firebase
    private void retrieveitemDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Shop");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear(); // Clear the existing list before adding new data
                itemList.clear();
                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                   // Shop shop = shopSnapshot.getValue(Shop.class);
                     shopName = shopSnapshot.child("shopName").getValue(String.class);
                     shopimage = shopSnapshot.child("url").getValue(String.class);
                     destrict = shopSnapshot.child("district").getValue(String.class);
                     taluka = shopSnapshot.child("taluka").getValue(String.class);
                     address = shopSnapshot.child("address").getValue(String.class);
                    shopcontactNumber = shopSnapshot.child("contactNumber").getValue(String.class);
                    Boolean profileverify = shopSnapshot.child("profileverified").getValue(Boolean.class);
                    // long promotedShopCount = shopSnapshot.child("promotedShops").getChildrenCount();
                    System.out.println("sdfdf " + taluka);

                    System.out.println("rgdfg "+shopName);
                    // postAdapter.setShopName(shopName);
                    if (profileverify != null && profileverify) {
                        DataSnapshot itemsSnapshot = shopSnapshot.child("items");
                        for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                            String itemkey = itemSnapshot.getKey();

                            String itemName = itemSnapshot.child("itemname").getValue(String.class);
                            String price = itemSnapshot.child("price").getValue(String.class);
                            String description = itemSnapshot.child("description").getValue(String.class);
                            String firstimage = itemSnapshot.child("firstImageUrl").getValue(String.class);
                            String sellprice = itemSnapshot.child("sell").getValue(String.class);
                            String offer = itemSnapshot.child("offer").getValue(String.class);
                            String itemskey = itemSnapshot.child("itemkey").getValue(String.class);
                            System.out.println("jfhv " + firstimage);

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

                            ItemList item = new ItemList(shopName, shopimage, shopcontactNumber, itemName, price, sellprice, description,
                                    firstimage, itemkey, imageUrls, destrict,taluka,address );
                            itemList.add(item);

                            OrderModel orderModel=new OrderModel();
                            orderModel.setProdId(itemskey);
                            orderModel.setProdName(itemName);
                            orderModel.setOffer(offer);
                            orderModel.setProImg(firstimage);
                            orderModel.setProDesc(description);
                            orderModel.setProprice(price);
                            orderModel.setProsell(sellprice);
                        }
                    }
                }
//                Collections.shuffle(itemList);
//                // Notify the adapter that the data has changed
//                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

    }

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

    private void retrieveBusinessPosts() {

    }


    @Override
    public void onResume() {
        super.onResume();

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
    public void onItemClick(int position) throws ExecutionException, InterruptedException {
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
                Boolean flag = true;

                Intent intent = new Intent(getContext(), ItemDetails.class);
                intent.putExtra("itemName", itemname);
                intent.putExtra("firstImageUrl", itemimage);
                intent.putExtra("itemDescription", itemdescription);
                intent.putExtra("itemPrice", itemprice);
                intent.putExtra("itemKey", itemkey);
                intent.putExtra("contactNumber", clickedShopcontactNumber);
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
    public void LoadHomeDataNew(){
        homeItemList.clear();
        lottieAnimationView.setVisibility(View.VISIBLE);
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
                                        shopname = snapshot.child("shopName").getValue(String.class);
                                        shopimagex = snapshot.child("url").getValue(String.class);
                                        shopaddress= snapshot.child("address").getValue(String.class);

                                        // Move the code inside onDataChange to ensure values are available
                                        String postImg = keySnapshot.child("postImg").getValue(String.class);
                                        String postDesc = keySnapshot.child("postDesc").getValue(String.class);
                                        String postType = keySnapshot.child("postType").getValue(String.class);
                                        String postKeys = keySnapshot.child("postKeys").getValue(String.class);
                                        String postCate = keySnapshot.child("postCate").getValue(String.class);

                                        PostModel postModel=new PostModel();
                                        postModel.setPostId(key);
                                        postModel.setPostDesc(postDesc);
                                        postModel.setPostType(postType);
                                        postModel.setPostImg(postImg);
                                        postModel.setPostKeys(postKeys);
                                        postModel.setPostCate(postCate);

                                        Log.d("fdfdfdsfd",""+shopname);

                                        postModel.setPostUser(shopname);
                                        postModel.setUserImg(shopimagex);
                                        postModel.setUserAdd(shopaddress);
                                        homeItemList.add(postModel);

                                        // Notify adapter or update UI as needed...
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
                    }
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

                    }
                    // Shuffle the homeItemList to display items randomly
                    Collections.shuffle(homeItemList);

                    // Notify adapter or update UI as needed...
                    homeMultiAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

    }

    public void LoadHomeDataNewTest() {
        homeItemList.clear();

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
                                        shopname = snapshot.child("shopName").getValue(String.class);
                                        shopimagex = snapshot.child("url").getValue(String.class);
                                        shopaddress= snapshot.child("address").getValue(String.class);

                                        String postImg = keySnapshot.child("postImg").getValue(String.class);
                                        String postDesc = keySnapshot.child("postDesc").getValue(String.class);
                                        String postType = keySnapshot.child("postType").getValue(String.class);
                                        String postKeys = keySnapshot.child("postKeys").getValue(String.class);
                                        String postCate = keySnapshot.child("postCate").getValue(String.class);

                                        PostModel postModel=new PostModel();
                                        postModel.setPostId(key);
                                        postModel.setPostDesc(postDesc);
                                        postModel.setPostType(postType);
                                        postModel.setPostImg(postImg);
                                        postModel.setPostKeys(postKeys);
                                        postModel.setPostCate(postCate);

                                        Log.d("fdfdfdsfd",""+shopname);

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

                    // Notify adapter or update UI as needed...
                    homeMultiAdapter.notifyDataSetChanged();
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
                    // Separate list for Products
                    List<OrderModel> productItemList = new ArrayList<>();

                    for (DataSnapshot contactNumberSnapshot : snapshot.getChildren()) {
                        String contactNumber = contactNumberSnapshot.getKey();

                        for (DataSnapshot productSnapshot : contactNumberSnapshot.getChildren()) {
                            String productId = productSnapshot.getKey();

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
                            productItemList.add(orderModel);
                        }
                    }

                    // Merge the lists in a specific sequence
                    int i = 0;
                    int j = 0;
                    while (i < homeItemList.size() && j < productItemList.size()) {
                        // Insert BusinessPosts item
                        homeItemList.add(i, productItemList.get(j));
                        i += 2;  // Increment by 2 to insert Product item next
                        j++;
                    }

                    // If there are remaining Product items, add them at the end
                    while (j < productItemList.size()) {
                        homeItemList.add(productItemList.get(j));
                        j++;
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
    }


}

