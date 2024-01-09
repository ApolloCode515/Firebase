package com.spark.swarajyabiz.ui;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.BannerAdapter;
import com.spark.swarajyabiz.CreateBanner;
import com.spark.swarajyabiz.CustomBannerImage;
import com.spark.swarajyabiz.PremiumMembership;
import com.spark.swarajyabiz.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.reactivex.rxjava3.annotations.NonNull;

public class Fragment_Business_Click_Event extends Fragment implements  BannerAdapter.OnItemClickListener{

    TextView textView;
    DatabaseReference categoryRef, userRef;
    Boolean premium;
    String titletext, shopName, shopcontactNumber, shopimage, shopownername,shopaddress, bannerimage;
    List<String> imageUrls;
    RecyclerView bannerViews;
    BannerAdapter businessBannerAdapter;
    private boolean isPremiumClicked = false;
    LinearLayout moreimglayout;
    Button moreimgbtn, customimgbtn;
    AlertDialog dialog;

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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__business__click__event, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }

        bannerViews = view.findViewById(R.id.bannerviews);
        moreimglayout = view.findViewById(R.id.moreimglay);
        moreimgbtn = view.findViewById(R.id.moreimgbtn);
        customimgbtn = view.findViewById(R.id.customimgbtn);
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


        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    premium = snapshot.child("premium").getValue(Boolean.class);
                    if (premium.equals(true)) {
                        moreimgbtn.setVisibility(View.GONE);
                        customimgbtn.setVisibility(View.VISIBLE);

                        customimgbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), CustomBannerImage.class);
                                intent.putExtra("hint", "इथे तुमच्या इमेज वरील मॅटर टाइप करा");
                                startActivity(intent);
                            }
                        });
                    } else{
                        customimgbtn.setVisibility(View.GONE);
                        moreimgbtn.setVisibility(View.VISIBLE);
                        moreimgbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showImageSelectiondialog();
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

        return view;
    }

    private void retriveBannerImages() {
        DatabaseReference adref = FirebaseDatabase.getInstance().getReference("Business");
        adref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<String> imageUrlsList = new ArrayList<>();

                    for (DataSnapshot businessSnapshot : snapshot.getChildren()) {
                        String businessName = businessSnapshot.child("name").getValue(String.class);

                        if (titletext.equals(businessName)) {
                            DataSnapshot imagesSnapshot = businessSnapshot.child("images");

                            for (DataSnapshot imageSnapshot : imagesSnapshot.getChildren()) {
                                String imageUrl = imageSnapshot.getValue(String.class);
                                System.out.println("esdrdf " + imageUrl);

                                // Add the URL to the list
                                imageUrlsList.add(imageUrl);
                            }
                        }
                    }

                    businessBannerAdapter.setImageUrls(imageUrlsList);
                    businessBannerAdapter.notifyDataSetChanged();
                    moreimglayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
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
            intent.putExtra("BusinessFragment", titletext);
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
            intent.putExtra("BusinessFragment", titletext);
            startActivity(intent);

        }else {
            // If the clicked position is beyond the first two images, show a "Get Premium" toast
            if (!isPremiumClicked) {
//                Toast.makeText(getContext(), "Get Premium to access more images", Toast.LENGTH_SHORT).show();

                showImageSelectionDialog();
//                Intent intent = new Intent(getContext(), PremiumMembership.class);
//                startActivity(intent);
            }
            // You can optionally implement a logic to redirect to a premium feature screen
        }
    }

    private void showImageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        builder.setView(customLayout);

        // Find views in the custom layout
        ImageView alertImageView = customLayout.findViewById(R.id.alertImageView);
        TextView alertTitle = customLayout.findViewById(R.id.alertTitle);
        TextView alertMessage = customLayout.findViewById(R.id.alertMessage);
        Button positiveButton = customLayout.findViewById(R.id.positiveButton);

        // Customize the views as needed
        Glide.with(this).asGif().load(R.drawable.gif3).into(alertImageView); // Replace with your image resource
        alertTitle.setText("प्रीमियम");
        alertMessage.setText("हा बॅनर प्रिमियम प्रकरातला आहे.\n" +
                "डाऊनलोड करण्यासाठी प्रिमियम प्लॅन निवडा.\n");

        // Set positive button click listener
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PremiumMembership.class);
                startActivity(intent);
                dialog.dismiss(); // Dismiss the dialog after the button click
            }
        });

        // Create and show the dialog
        dialog = builder.create();
        dialog.show();
    }

    private void showImageSelectiondialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        builder.setView(customLayout);

        // Find views in the custom layout
        ImageView alertImageView = customLayout.findViewById(R.id.alertImageView);
        TextView alertTitle = customLayout.findViewById(R.id.alertTitle);
        TextView alertMessage = customLayout.findViewById(R.id.alertMessage);
        Button positiveButton = customLayout.findViewById(R.id.positiveButton);

        // Customize the views as needed
        Glide.with(this).asGif().load(R.drawable.gif2).into(alertImageView); // Replace with your image resource
        alertTitle.setText("प्रीमियम");
        alertMessage.setText("अधिक इमेज पहाण्यासाठी प्रिमियम प्लॅन निवडावा लागेल.");

        // Set positive button click listener
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PremiumMembership.class);
                startActivity(intent);
                dialog.dismiss(); // Dismiss the dialog after the button click
            }
        });

        // Create and show the dialog
        dialog = builder.create();
        dialog.show();
    }

}