package com.spark.swarajyabiz.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.BannerDetails;
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
    BusinessBannerAdapter bannerAdapter;
    String shopName, shopcontactNumber, shopownerName, shopimage,shopaddress, businessName, bannerimage;

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

        RecyclerView recyclerView = view.findViewById(R.id.businessview);
        bannerAdapter = new BusinessBannerAdapter(getContext(), FragmentBusiness.this, true);
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
    private void businessRetrieveImages(){
    DatabaseReference adref = FirebaseDatabase.getInstance().getReference("Business");
        adref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange (@NonNull DataSnapshot snapshot){
            if (snapshot.exists()) {
                List<String> keys = new ArrayList<>();
                List<String> imageUrlsList = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    System.out.println("wfed " + key);
                    keys.add(key);

                    // Retrieve only the image with key "0" for each node
                    DataSnapshot zeroImageSnapshot = childSnapshot.child("0");
                    String zeroImageUrl = zeroImageSnapshot.getValue(String.class);

                    // Add the URL to the list
                    imageUrlsList.add(zeroImageUrl);
                    System.out.println("sdkfj " + zeroImageUrl);
                }



                // Update the adapter with the lists of keys and image URLs
                bannerAdapter.setBusinessnametexts(keys);
                bannerAdapter.setImageUrls(imageUrlsList);
                bannerAdapter.shuffleRelativeLayouts();
                bannerAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled (@NonNull DatabaseError error){
            // Handle onCancelled
        }
    });
}


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
}