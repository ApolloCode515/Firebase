package com.spark.swarajyabiz.ui;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

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


    public class Fragment_Thoughts_Click_Event extends Fragment implements  BannerAdapter.OnItemClickListener{

        TextView textView;
        DatabaseReference categoryRef;
        String titletext, shopName, shopcontactNumber, shopimage, shopownername,shopaddress, bannerimage;
        List<String> imageUrls;
        RecyclerView bannerViews;
        BannerAdapter businessBannerAdapter;
        private boolean isPremiumClicked = false;

        public Fragment_Thoughts_Click_Event() {
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

        @SuppressLint("MissingInflatedId")
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_thoughts_click_event, container, false);

            SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String userId = sharedPreference.getString("mobilenumber", null);
            if (userId != null) {
                // userId = mAuth.getCurrentUser().getUid();
                System.out.println("dffvf  " +userId);
            }

            bannerViews = view.findViewById(R.id.bannerviews);
            businessBannerAdapter = new BannerAdapter(getContext(), sharedPreference, Fragment_Thoughts_Click_Event.this);
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

            System.out.println("dfughv "+shopcontactNumber);
            System.out.println("sdvfb v " +bannerimage);
            categoryRef = FirebaseDatabase.getInstance().getReference().child("Thoughts").child(titletext);
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
           //         showCustomDialog();
                }
                // You can optionally implement a logic to redirect to a premium feature screen
            }
        }

        private void showCustomDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_premium_alert_dialog, null);

            ViewPager2 viewPager2 = dialogView.findViewById(R.id.viewpager2);
//            List<SlideImage> slideImages = new ArrayList<>();
//            slideImages.add(new SlideImage(R.drawable.a));
//            slideImages.add(new SlideImage(R.drawable.aa));
//            slideImages.add(new SlideImage(R.drawable.b));
//            slideImages.add(new SlideImage(R.drawable.aaa));
//        slideImages.add(new SlideImage(R.drawable.frame5));
//        slideImages.add(new SlideImage(R.drawable.frame6));


           // viewPager2.setAdapter(new SliderAdapter(slideImages, viewPager2));

            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);
            viewPager2.setOffscreenPageLimit(3);
            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1- Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                }
            });

            viewPager2.setPageTransformer(compositePageTransformer);

            builder.setView(dialogView);

            // Add any other properties to the AlertDialog if needed

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }