package com.spark.swarajyabiz.ui;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.BannerDetails;
import com.spark.swarajyabiz.BusinessBanner;
import com.spark.swarajyabiz.BusinessBannerAdapter;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.annotations.NonNull;

public class FragmentBusiness extends Fragment implements  BusinessBannerAdapter.OnItemClickListener{

    private ImageView businessimage1, businessimage2, businessimage3, businessimage4, businessimage5, businessimage6, businessimage7, businessimage8, businessimage9, businessimage10;
    private ImageView businessimage11, businessimage12, businessimage13, businessimage14, businessimage15, businessimage16, businessimage17, businessimage18, businessimage19, businessimage20;
    private ImageView businessimage21, businessimage22, businessimage23, businessimage24, businessimage25, businessimage26, businessimage27, businessimage28, businessimage29, businessimage30;
    private ImageView businessimage31, businessimage32, businessimage33, businessimage34, businessimage35, businessimage36, businessimage37, businessimage38, businessimage39, businessimage40;
    private ImageView businessimage41, businessimage42, businessimage43, businessimage44, businessimage45, businessimage46, businessimage47, businessimage48, businessimage49, businessimage50;
    private ImageView businessimage51, businessimage52, businessimage53, businessimage54, businessimage55, businessimage56, businessimage57, businessimage58, businessimage59, businessimage60;
    private ImageView businessimage61, businessimage62, businessimage63, businessimage64, businessimage65, businessimage66, businessimage67, businessimage68, businessimage69, businessimage70;
    private ImageView businessimage71, businessimage72, businessimage73, businessimage74, businessimage75, businessimage76, businessimage77, businessimage78, businessimage79, businessimage80;
    DatabaseReference businessRef;

    private List<ImageView> businessImages = new ArrayList<>();
    private List<TextView> businesstexts = new ArrayList<>();
    private List<String> imageUrls;
    private List<BusinessBanner> businessBannerList;
    BusinessBannerAdapter bannerAdapter;
    String userId, shopName, shopcontactNumber, shopownerName, shopimage,shopaddress, businessName,favkeys, bannerimage;
    private boolean isFilled = false; // Initial state
    SharedPreferences sharedPreferences;


    public FragmentBusiness() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business, container, false);

        businessRef = FirebaseDatabase.getInstance().getReference("Business");
         businessRetrieveImages();

        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
         userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

//        // Initialize your ImageViews and add them to the list
//        businessImages.add(view.findViewById(R.id.businessimage1));
//        businessImages.add(view.findViewById(R.id.businessimage2));
//        businessImages.add(view.findViewById(R.id.businessimage3));
//        businessImages.add(view.findViewById(R.id.businessimage4));
//        businessImages.add(view.findViewById(R.id.businessimage5));
//
//        businesstexts.add(view.findViewById(R.id.businesstextview1));
//        businesstexts.add(view.findViewById(R.id.businesstextview2));
//        businesstexts.add(view.findViewById(R.id.businesstextview3));
//        businesstexts.add(view.findViewById(R.id.businesstextview4));
//        businesstexts.add(view.findViewById(R.id.businesstextview5));
//        // Add more ImageViews as needed
//
//        // Set click listeners for each ImageView
//        for (int i = 0; i < businessImages.size(); i++) {
//            final int position = i;
//            businessImages.get(i).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (position >= 0 && position < imageUrls.size()) {
//                        String selectedText = getTextForPosition(position);
//                        System.out.println("rfguhf "+selectedText);
//                        // Start the BannerDetails activity and pass the selectedText
//                        Intent intent = new Intent(requireContext(), BannerDetails.class);
//                        intent.putExtra("selectedText", selectedText);
//                        startActivity(intent);
//                    }
//                    //System.out.println("rfguhf "+position);
//                }
//            });
//        }

        businessBannerList = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.businessview);
        bannerAdapter = new BusinessBannerAdapter(businessBannerList,sharedPreference, getContext(), FragmentBusiness.this, true);
        recyclerView.setAdapter(bannerAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);


       shopcontactNumber = requireActivity().getIntent().getStringExtra("contactNumber");
       shopName= requireActivity().getIntent().getStringExtra("shopName");
       shopimage =  requireActivity().getIntent().getStringExtra("shopimage");
       shopownerName = requireActivity().getIntent().getStringExtra("ownerName");
       shopaddress = requireActivity().getIntent().getStringExtra("shopaddress");


        return view;
    }

    private void onBusinessImageClick(int position) {

    }

    //    private String getTextForPosition(int position) {
//        // Ensure that position is within the bounds of your texts list
//        if (position >= 0 && position < businesstexts.size()) {
//            // Retrieve the corresponding text for the clicked image
//            return businesstexts.get(position).getText().toString();
//        } else {
//            // Return a default text or handle the out-of-bounds case
//            return "Default Text";
//        }
//    }
//
//
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

    private boolean checkIfBusinessIsFavorite(String businessName) {


        // If the businessName key is not present or is not marked as a favorite, return false
        return false;
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

    @Override
    public void onItemClick(int position, String imageUrl, String businessname) throws ExecutionException, InterruptedException {

        Intent intent = new Intent(getContext(), BannerDetails.class);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("contactNumber",shopcontactNumber);
        intent.putExtra("shopName", shopName);
        intent.putExtra("shopimage", shopimage);
        intent.putExtra("ownerName", shopownerName);
        intent.putExtra("shopaddress", shopaddress);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("BUSINESS_NAME", businessname);
        System.out.println("sdfvd " +businessname);
        startActivity(intent);
    }
    @Override
    public void onfavClick(int position, ImageView favImageView, String businessName) {
        // Toggle the state
        isFilled = !isFilled;

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FavBusiness");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get the count of stored business names
                long count = snapshot.getChildrenCount();

                if (isFilled) {
                    // Check if the count is less than five before storing a new business name
                    if (count < 5) {
                        // Set to filled image
                        favImageView.setImageResource(R.drawable.ic_baseline_favorite_fill_24);
                        // Update the database with true value at the current position
                        updateDatabaseAtPosition(position, true, businessName);
                    } else {
                        // Notify the user or handle the case where the limit is reached
                        // You may want to show a toast message or implement other logic
                        Toast.makeText(getContext(), "You can only have up to five favorite businesses", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Set to border image
                    favImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    // Remove the key from the database at the current position
                    removeKeyFromDatabase(position, businessName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

//    @Override
//    public void onfavClick(int position, ImageView favImageView, String businessName) {
//        // Toggle the state
//        isFilled = !isFilled;
//
//        if (isFilled) {
//            // Set to filled image
//            favImageView.setImageResource(R.drawable.ic_baseline_favorite_fill_24);
//            // Update the database with true value at the current position
//            updateDatabaseAtPosition(position, true, businessName);
//        } else {
//            // Set to border image
//            favImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24);
//            // Update the database with false value at the current position
//            removeKeyFromDatabase(position, businessName);
//        }
//    }

    // Update the database at the specified position with the given value
    private void updateDatabaseAtPosition(int position, boolean value, String businessName) {
        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FavBusiness");

            // Update the 'fav' key in the 'businessname' node
            adref.child(businessName).setValue(value);
    }

    // Remove the key from the database at the specified position
    private void removeKeyFromDatabase(int position, String businessName) {
        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FavBusiness");

        // Remove the key from the 'FavBusiness' node
        adref.child(businessName).removeValue();
    }

    // Check the database value and set the favImageView accordingly
    private void checkAndSetFavImageFromDatabase(ImageView favImageView, String businessName) {
        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("Business");
        adref.child(businessName).child("fav").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check if the 'fav' key exists and its value
                if (snapshot.exists()) {
                    boolean isFav = snapshot.getValue(Boolean.class);
                    if (isFav) {
                        // If 'fav' is true, set the favImageView to the filled image
                        favImageView.setImageResource(R.drawable.ic_baseline_favorite_fill_24);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }


}