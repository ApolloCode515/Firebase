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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.BuildConfig;
import com.spark.swarajyabiz.Business;
import com.spark.swarajyabiz.ImageAdapter;
import com.spark.swarajyabiz.ItemDetails;
import com.spark.swarajyabiz.ItemList;
import com.spark.swarajyabiz.JobDetails;
import com.spark.swarajyabiz.JobPostAdapter;
import com.spark.swarajyabiz.PlaceOrder;
import com.spark.swarajyabiz.Post;
import com.spark.swarajyabiz.PostAdapter;
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

import io.reactivex.rxjava3.annotations.NonNull;

public class FragmentHome extends Fragment implements PostAdapter.PostClickListener {

    private RecyclerView recyclerView, jobpostrecyclerview;
    private List<JobDetails> jobDetailsList ;
    private List<Post> postList = new ArrayList<>(); // Create a list to store post details
    private List<ItemList> itemList = new ArrayList<>(); // Create a list to store post details
    private List<ItemList> filteredList = new ArrayList<>();
    ImageView searchImage;
    private PostAdapter postAdapter;
    CardView searchcard;
    List<Shop> shopList;
    String shopcontactNumber, taluka,address;
    private List<ItemList> originalItemList; // Keep a copy of the original list
    private int lastDisplayedIndex = -1;
    FrameLayout frameLayout;
    ImageView adimagecancel;
    private boolean imageShown = false;
    DatabaseReference userRef, shopRef;
    AlertDialog dialog;
    String userId;
    JobPostAdapter jobPostAdapter;

    public FragmentHome() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__home, container, false);

        // Test Github

        searchImage = view.findViewById(R.id.search_image);
        searchcard = view.findViewById(R.id.search);
        frameLayout = view.findViewById(R.id.frameLayout);
        adimagecancel = view.findViewById(R.id.adimagecancel);

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

        jobDetailsList = new ArrayList<>();
        jobpostrecyclerview = view.findViewById(R.id.jobpostrecyclerview);
        jobPostAdapter = new JobPostAdapter(jobDetailsList, getContext(), sharedPreference);
        jobpostrecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        jobpostrecyclerview.setAdapter(jobPostAdapter);



        shopList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.postrecyclerview);
        postAdapter = new PostAdapter(getContext(), itemList, this);
        // Create a GridLayoutManager with 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(postAdapter);

//       TabLayout tabLayout = view.findViewById(R.id.TabLayout);
//       ViewPager2 viewPager2 = view.findViewById(R.id.ViewPager);
//
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
//        viewPager2.setAdapter(viewPagerAdapter);
//
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager2.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                tabLayout.getTabAt(position).select();
//            }
//        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.Background));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(getContext(), R.color.Background));
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
        retrieveJobPostDetails();

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
        SearchView searchView = view.findViewById(R.id.searchview);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the RecyclerView based on the search query
                filter(newText);
                return true;
            }
        });

        CardView searchCardView = view.findViewById(R.id.search);

        searchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Open the SearchView
                searchView.setIconified(false);

                // You can optionally set focus or handle any other actions you need here
            }
        });


        return view;
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
        postAdapter.setData(filteredList);
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
    private void retrievePostDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Shop");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear(); // Clear the existing list before adding new data
                itemList.clear();
                for (DataSnapshot shopSnapshot : dataSnapshot.getChildren()) {
                   // Shop shop = shopSnapshot.getValue(Shop.class);
                    String shopName = shopSnapshot.child("shopName").getValue(String.class);
                    String shopimage = shopSnapshot.child("url").getValue(String.class);
                    String destrict = shopSnapshot.child("district").getValue(String.class);
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

                            ItemList item = new ItemList(shopName, shopimage, shopcontactNumber, itemName, price, description,
                                    firstimage, itemkey, imageUrls, destrict,taluka,address );
                            itemList.add(item);
                        }
                    }
                }
                Collections.shuffle(itemList);
                // Notify the adapter that the data has changed
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

    }

    private void retrieveJobPostDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("JobPosts");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    jobDetailsList.clear();

                    for (DataSnapshot jobPostsSnapshot : snapshot.getChildren()) {
                        String contactNumber = jobPostsSnapshot.getKey();
                        System.out.println("Contact Number: " + contactNumber);
                        String jobkey = jobPostsSnapshot.getKey();

                        String jobTitle = jobPostsSnapshot.child("jobtitle").getValue(String.class);
                        System.out.println("Job Title: " + jobTitle);

                        String companyname = jobPostsSnapshot.child("companyname").getValue(String.class);
                        String joblocation = jobPostsSnapshot.child("joblocation").getValue(String.class);
                        String jobtype = jobPostsSnapshot.child("jobtype").getValue(String.class);
                        String description = jobPostsSnapshot.child("description").getValue(String.class);
                        String workplacetype = jobPostsSnapshot.child("workplacetype").getValue(String.class);
                        String currentdate = jobPostsSnapshot.child("currentdate").getValue(String.class);

                        System.out.println("Company Name: " + companyname);
                        System.out.println("Job Location: " + joblocation);
                        System.out.println("Job Type: " + jobtype);
                        System.out.println("Description: " + description);
                        System.out.println("Workplace Type: " + workplacetype);

                        JobDetails jobDetails = new JobDetails(jobTitle, companyname, workplacetype, joblocation, jobtype, description,currentdate, jobkey);
                        jobDetailsList.add(jobDetails);
                    }

                    jobPostAdapter.notifyDataSetChanged();
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

}

