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
import android.widget.FrameLayout;
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
import com.spark.swarajyabiz.ThoughtsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.annotations.NonNull;

public class FragmentThoughts extends Fragment implements  ThoughtsAdapter.OnThoughtClickListener{

    DatabaseReference Thoughtsref;
    private ImageView thoughtimage1, thoughtimage2, thoughtimage3, thoughtimage4, thoughtimage5;
    private FrameLayout frameimage1;

    private List<ImageView> thoughtsImages = new ArrayList<>();
    private List<TextView> thoughtstexts = new ArrayList<>();
    private List<String> imageUrls;
    ThoughtsAdapter thoughtsAdapter;
    String shopName, shopcontactNumber, shopownerName, shopimage,shopaddress, businessName, bannerimage;

    public FragmentThoughts() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_thoughts, container, false);


        thoughtRetrieveImages();

        RecyclerView recyclerView = view.findViewById(R.id.thoughtsview);
        thoughtsAdapter = new ThoughtsAdapter(getContext(),FragmentThoughts.this, true);
        recyclerView.setAdapter(thoughtsAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);


        shopcontactNumber = requireActivity().getIntent().getStringExtra("contactNumber");
        shopName= requireActivity().getIntent().getStringExtra("shopName");
        shopimage =  requireActivity().getIntent().getStringExtra("shopimage");
        shopownerName = requireActivity().getIntent().getStringExtra("ownerName");
        System.out.println("sdcdfv " +shopownerName);
        shopaddress = requireActivity().getIntent().getStringExtra("shopaddress");



        return view;
    }

    private void thoughtRetrieveImages(){
        Thoughtsref = FirebaseDatabase.getInstance().getReference("Thoughts");
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

    @Override
    public void onThoughtClick(int position, String imageUrl, String thoughtsname) throws ExecutionException, InterruptedException {

        Intent intent = new Intent(getContext(), BannerDetails.class);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("contactNumber",shopcontactNumber);
        intent.putExtra("shopName", shopName);
        intent.putExtra("shopimage", shopimage);
        intent.putExtra("ownerName", shopownerName);
        intent.putExtra("shopaddress", shopaddress);
        intent.putExtra("IMAGE_URL", imageUrl);
        intent.putExtra("THOUGHTS_NAME", thoughtsname);
        System.out.println("sdfvd " +imageUrl);
        startActivity(intent);

    }
}