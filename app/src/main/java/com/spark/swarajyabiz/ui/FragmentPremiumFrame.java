package com.spark.swarajyabiz.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.spark.swarajyabiz.CreateBanner;
import com.spark.swarajyabiz.R;


public class FragmentPremiumFrame extends Fragment{

    String shopname, shopimage, shopcontactnumber, shopownername, bannerimage;
    TextView shopnametext, shopcontactnumbertext, shopownernametext, text1, text2;
    ImageView shopimageview;
    private EditText texteditor;
    private ImageView cancelimage;
    private ImageView checkimage;
    private LinearLayout edittextlayout;
    private float lastTouchX, lastTouchY, scaleFactor = 1f;
    FrameLayout frameLayout;
    RelativeLayout relativeLayout;
    private ScaleGestureDetector scaleGestureDetector;

    public FragmentPremiumFrame() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premium_frame, container, false);

        frameLayout = view.findViewById(R.id.frameLayout);




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

        // Retrieve data from arguments
        Bundle bundle = getArguments();
        if (bundle != null) {
            shopimage = bundle.getString("shopimage", "default_value");
            shopname = bundle.getString("shopname", "default_value");
            shopcontactnumber = bundle.getString("shopcontactnumber", "default_value");
            shopownername = bundle.getString("shopaddress", "default_value");
            bannerimage = bundle.getString("bannerimage", "default_value");
            System.out.println("djfjnvsd " +shopimage);
            System.out.println("djfjnvsd " +shopname);
            System.out.println("djfjnvsd " +shopcontactnumber);
            System.out.println("sdv cds " +bannerimage);


// Check if the bannerimage is a resource identifier
            if (TextUtils.isDigitsOnly(bannerimage)) {
                // If it's a resource identifier, convert it to an integer
                int resourceId = Integer.parseInt(bannerimage);
                // Set the background using the resource ID
                frameLayout.setBackgroundResource(resourceId);
            } else {
                // If it's a URL, use Glide to load the image and set it as the background
                Glide.with(this)
                        .load(bannerimage)
                        .centerCrop()
                        .into(new CustomViewTarget<FrameLayout, Drawable>(frameLayout) {
                            @Override
                            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                // Handle load failure if needed
                            }

                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                frameLayout.setBackground(resource);
                            }

                            @Override
                            protected void onResourceCleared(@Nullable Drawable placeholder) {
                                // Clear resources if needed
                            }
                        });
            }
        }
//        Glide.with(getActivity()).load(shopimage).circleCrop().into(shopimageview);
//        shopnametext.setText(shopname);
//        shopcontactnumbertext.setText(shopcontactnumber);
//        shopownernametext.setText(shopownername);

        return view;
    }







    @Override
    public void onResume() {
        super.onResume();
        // Set the flag to true when the fragment becomes active
        if (getActivity() instanceof CreateBanner) {
            ((CreateBanner) getActivity()).setFragmentActive(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Set the flag to false when the fragment becomes inactive
        if (getActivity() instanceof CreateBanner) {
            ((CreateBanner) getActivity()).setFragmentActive(false);
        }
    }
}