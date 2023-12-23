package com.spark.swarajyabiz.ui;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.AddPost;
import com.spark.swarajyabiz.BannerAdapter;
import com.spark.swarajyabiz.BusinessCardAdapter;
import com.spark.swarajyabiz.CreateBanner;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class Fragment_Business_post extends Fragment implements  BusinessCardAdapter.OnItemClickListener{

    TextView fragmenttext;
    RecyclerView busimgRecyclerview;
    String name;
    BusinessCardAdapter businessBannerAdapter;
    List<String> imageUrls;
    String titletext, shopName, shopcontactNumber, shopimage, shopownername,shopaddress, bannerimage;

    public Fragment_Business_post() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__business_post, container, false);

        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        Bundle args = getArguments();
        if (args != null){
            name = args.getString("selectedRadioButtonId");
            System.out.println("esdn " +name);
            shopcontactNumber = args.getString("contactNumber");
            shopName = args.getString("shopName");
            shopimage = args.getString("shopimage");
            shopownername = args.getString("ownerName");
            shopaddress = args.getString("shopaddress");

            if (name.equals("rdinvestors")){
                retrieveinvestor();
            } else if (name.equals("rdMarket")){
                retrievemarket();
            }else if (name.equals("rdtalent")){
                retrievetalent();
            }else if (name.equals("rdbranding")){
                retrievebranding();
            }else if (name.equals("rdbusihelp")){
                retrievebusihelp();
            }
        }

        imageUrls = new ArrayList<>();
        busimgRecyclerview = view.findViewById(R.id.busiimageview);
        businessBannerAdapter = new BusinessCardAdapter(getContext(), sharedPreference, Fragment_Business_post.this);
        busimgRecyclerview.setAdapter(businessBannerAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        busimgRecyclerview.setLayoutManager(layoutManager);




        return view;

    }

    private void retrieveinvestor(){
        DatabaseReference investRef = FirebaseDatabase.getInstance().getReference("Business");
        System.out.println("dsjvnjv " +investRef);

        investRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        System.out.println("srgv " +key);
                        if (key.equals("अगरबत्ती")){

                            for (DataSnapshot imageSnapshot : dataSnapshot.child("images").getChildren()) {
                                String imageKey = imageSnapshot.getKey();
                                String imageUrl = imageSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                    System.out.println("fgnfvb "+imageUrl);
                                }
                                // Do something with the image URL (e.g., display or store it)
                                System.out.println("Image key: " + imageKey + ", Image URL: " + imageUrl);
                            }
                            businessBannerAdapter.setImageUrls(imageUrls);
                            businessBannerAdapter.notifyDataSetChanged();;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrievemarket(){
        DatabaseReference investRef = FirebaseDatabase.getInstance().getReference("Business");
        System.out.println("dsjvnjv " +investRef);

        investRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        System.out.println("srgv " +key);
                        if (key.equals("किराणा")){

                            for (DataSnapshot imageSnapshot : dataSnapshot.child("images").getChildren()) {
                                String imageKey = imageSnapshot.getKey();
                                String imageUrl = imageSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                    System.out.println("fgnfvb "+imageUrl);
                                }
                                // Do something with the image URL (e.g., display or store it)
                                System.out.println("Image key: " + imageKey + ", Image URL: " + imageUrl);
                            }
                            businessBannerAdapter.setImageUrls(imageUrls);
                            businessBannerAdapter.notifyDataSetChanged();;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrievetalent(){
        DatabaseReference investRef = FirebaseDatabase.getInstance().getReference("Business");
        System.out.println("dsjvnjv " +investRef);

        investRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        System.out.println("srgv " +key);
                        if (key.equals("केक शॉप")){

                            for (DataSnapshot imageSnapshot : dataSnapshot.child("images").getChildren()) {
                                String imageKey = imageSnapshot.getKey();
                                String imageUrl = imageSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                    System.out.println("fgnfvb "+imageUrl);
                                }
                                // Do something with the image URL (e.g., display or store it)
                                System.out.println("Image key: " + imageKey + ", Image URL: " + imageUrl);
                            }
                            businessBannerAdapter.setImageUrls(imageUrls);
                            businessBannerAdapter.notifyDataSetChanged();;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrievebranding(){
        DatabaseReference investRef = FirebaseDatabase.getInstance().getReference("Business");
        System.out.println("dsjvnjv " +investRef);

        investRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        System.out.println("srgv " +key);
                        if (key.equals("केटरिंग सेवा")){

                            for (DataSnapshot imageSnapshot : dataSnapshot.child("images").getChildren()) {
                                String imageKey = imageSnapshot.getKey();
                                String imageUrl = imageSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                    System.out.println("fgnfvb "+imageUrl);
                                }
                                // Do something with the image URL (e.g., display or store it)
                                System.out.println("Image key: " + imageKey + ", Image URL: " + imageUrl);
                            }
                            businessBannerAdapter.setImageUrls(imageUrls);
                            businessBannerAdapter.notifyDataSetChanged();;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrievebusihelp(){
        DatabaseReference investRef = FirebaseDatabase.getInstance().getReference("Business");
        System.out.println("dsjvnjv " +investRef);

        investRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        String key = dataSnapshot.getKey();
                        System.out.println("srgv " +key);
                        if (key.equals("खेळ")){

                            for (DataSnapshot imageSnapshot : dataSnapshot.child("images").getChildren()) {
                                String imageKey = imageSnapshot.getKey();
                                String imageUrl = imageSnapshot.getValue(String.class);
                                if (imageUrl != null) {
                                    imageUrls.add(imageUrl);
                                    System.out.println("fgnfvb "+imageUrl);
                                }
                                // Do something with the image URL (e.g., display or store it)
                                System.out.println("Image key: " + imageKey + ", Image URL: " + imageUrl);
                            }
                            businessBannerAdapter.setImageUrls(imageUrls);
                            businessBannerAdapter.notifyDataSetChanged();;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(int position, Boolean premium, String imageUrl) throws ExecutionException, InterruptedException {

        Intent intent = new Intent(getContext(), AddPost.class);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("contactNumber", shopcontactNumber);
        intent.putExtra("shopName", shopName);
        intent.putExtra("shopimage", shopimage);
        intent.putExtra("ownerName", shopownername);
        intent.putExtra("shopaddress", shopaddress);
        System.out.println("sdfvsdc " + shopaddress);
        intent.putExtra("IMAGE_URL", imageUrl);
        startActivity(intent);
    }
}