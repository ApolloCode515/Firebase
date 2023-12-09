package com.spark.swarajyabiz.ui;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.BannerAdapter;
import com.spark.swarajyabiz.CreateBanner;
import com.spark.swarajyabiz.PremiumMembership;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.grpc.internal.SharedResourceHolder;

public class Fragment_Business_Click_Event extends Fragment implements  BannerAdapter.OnItemClickListener{

    TextView textView;
    DatabaseReference categoryRef;
    String titletext, shopName, shopcontactNumber, shopimage, shopownername,shopaddress, bannerimage;
    List<String> imageUrls;
    RecyclerView bannerViews;
    BannerAdapter businessBannerAdapter;
    private boolean isPremiumClicked = false;

    public Fragment_Business_Click_Event() {
        // Required empty public constructor
    }

    // Add a method to create a new instance of the fragment with arguments
    public static Fragment_Thoughts_Click_Event newInstance(String titleText) {
        Fragment_Thoughts_Click_Event fragment = new Fragment_Thoughts_Click_Event();
        Bundle args = new Bundle();
        args.putString("titleText", titleText);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__business__click__event, container, false);

        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        bannerViews = view.findViewById(R.id.bannerviews);
        businessBannerAdapter = new BannerAdapter(getContext(), sharedPreference, Fragment_Business_Click_Event.this);
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
         System.out.println("sefvs " +titletext);
         categoryRef = FirebaseDatabase.getInstance().getReference().child("Business").child(titletext);
        imageUrls = new ArrayList<>();
         retriveBannerImages();

        return view;
    }

    private void retriveBannerImages(){
        categoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                        // Assuming that each key has a child node with image URLs
                        for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                            String imageUrl = imageSnapshot.getValue(String.class);
                            if (imageUrl != null) {
                                imageUrls.add(imageUrl);
                                System.out.println("fgnfvb "+imageUrl);
                            }
                    }

                }
                businessBannerAdapter.setImageUrls(imageUrls);
                businessBannerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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