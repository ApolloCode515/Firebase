package com.spark.swarajyabiz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_10;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_11;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_12;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_13;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_14;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_15;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_16;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_17;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_18;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_19;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_20;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_21;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_22;
import com.spark.swarajyabiz.PersonalFrame.Fragment_personal_9;
import com.spark.swarajyabiz.ui.Fragment1;
import com.spark.swarajyabiz.ui.Fragment10;
import com.spark.swarajyabiz.ui.Fragment11;
import com.spark.swarajyabiz.ui.Fragment12;
import com.spark.swarajyabiz.ui.Fragment13;
import com.spark.swarajyabiz.ui.Fragment14;
import com.spark.swarajyabiz.ui.Fragment15;
import com.spark.swarajyabiz.ui.Fragment16;
import com.spark.swarajyabiz.ui.Fragment17;
import com.spark.swarajyabiz.ui.Fragment2;
import com.spark.swarajyabiz.ui.Fragment3;
import com.spark.swarajyabiz.ui.Fragment4;
import com.spark.swarajyabiz.ui.Fragment5;
import com.spark.swarajyabiz.ui.Fragment6;
import com.spark.swarajyabiz.ui.Fragment7;
import com.spark.swarajyabiz.ui.Fragment8;
import com.spark.swarajyabiz.ui.Fragment9;
import com.spark.swarajyabiz.ui.FragmentPremiumFrame;
import com.spark.swarajyabiz.ui.Fragment_personal_1;
import com.spark.swarajyabiz.ui.Fragment_personal_2;
import com.spark.swarajyabiz.ui.Fragment_personal_3;
import com.spark.swarajyabiz.ui.Fragment_personal_4;
import com.spark.swarajyabiz.ui.Fragment_personal_5;
import com.spark.swarajyabiz.ui.Fragment_personal_6;
import com.spark.swarajyabiz.ui.Fragment_personal_7;
import com.spark.swarajyabiz.ui.Fragment_personal_8;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;


public class CreateBanner extends AppCompatActivity implements BusinessBannerAdapter.OnItemClickListener
                                                        , FrameAdapter.OnFrameClickListener{

    RecyclerView recyclerView;
    DatabaseReference databaseRef, userRef, bannerDesignRef, adref, frameRef;
    StorageReference storageReference;
    BusinessBannerAdapter businessBannerAdapter;
    List<String> imageUrls ;
    ImageView back, imageView, logoimage, bannerdesign1, bannerdesign2,
            bannerdesign3, bannerdesign4, bannerdesign5, bannerdesign6, bannerdesign7, bannerdesign8,
            bannerdesign9, bannerdesign10, bannerdesign11, bannerdesign12, bannerdesign13, bannerdesign14,
            bannerdesign15, bannerdesign16, bannerdesign17 ;

    ImageView userdesign1, userdesign2, userdesign3, userdesign4, userdesign5, userdesign6, userdesign7, userdesign8, userdesign9, userdesign10,
            userdesign11, userdesign12, userdesign13, userdesign14, userdesign15, userdesign16, userdesign17, userdesign18, userdesign19, userdesign20,
            userdesign21, userdesign22;
    TextView shopnametext, gradientcolorpicker, shopcontactnumbertext, shopownernametext, text1, text2;
    Button addlogobtn, addtextbtn, btnSave, btnshare;
    FrameLayout frameLayout, logoframelayout, framelayoutcontainer;
    private ScaleGestureDetector scaleGestureDetector;

    private RelativeLayout relativeLayout;
    private ImageView selectedImageView;
    private float lastTouchX, lastTouchY, scaleFactor = 1f;;
    private boolean isFragmentActive = false;
    View customImageView;

    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private FrameLayout frameLayout1;
    private LinearLayout edittextlayout;
    private EditText texteditor;
    private ImageView cancelimage, cancellogoimageview, logoimageview;
    private ImageView checkimage;
    private TextView usertextview, businesstextview, premiumtextview;
    CardView addimagecard, addtextcard, usercard, businesscard, premiumcard, savecard, sharecard, postcard;
    GridLayout gridLayout;
    String contactNumber, shopName, shopimage, ownername, shopaddress, image,businessfragment, switchUser;
    Boolean isdownloaded, premium;
    private GestureDetector gestureDetector;
    private boolean isTextViewClicked = false;
    private ButtonClickListener buttonClickListener;
    RecyclerView framerecyclerview, premiumframereyclerview;
    FrameAdapter frameAdapter;
    HorizontalScrollView businessframes, userframes;

    private int selectedBorderColor = Color.BLUE;
    private int defaultPosition = 0;
    private int defaultPositionPremiumRecyclerView = 0;
    AlertDialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_banner);

        savecard = findViewById(R.id.savecard);
        sharecard = findViewById(R.id.sharecard);
        postcard = findViewById(R.id.postcard);
        imageView = findViewById(R.id.bannerimage);
        cancellogoimageview = findViewById(R.id.cancelimageview);
        logoimageview = findViewById(R.id.logoimageview);
        back = findViewById(R.id.back);
        addlogobtn = findViewById(R.id.addlogo);
        addtextbtn = findViewById(R.id.addtext);
        logoimage = findViewById(R.id.logoimage);
        frameLayout = findViewById(R.id.frameLayout);
        shopnametext = findViewById(R.id.shopnametext);
        addtextcard = findViewById(R.id.addtextcard);
        edittextlayout = findViewById(R.id.edittextlayout);
        texteditor = findViewById(R.id.texteditor);
        cancelimage = findViewById(R.id.cancelimage);
        checkimage = findViewById(R.id.checkimage);
        gridLayout = findViewById(R.id.gridLayout);
        btnSave = findViewById(R.id.savebtn);
        btnshare = findViewById(R.id.sharebtn);
        shopcontactnumbertext = findViewById(R.id.shopcontactNumbertext);
        shopownernametext = findViewById(R.id.shopownernametext);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        seekBar1 = findViewById(R.id.seekbar1);
        seekBar2 = findViewById(R.id.seekbar2);
        framerecyclerview = findViewById(R.id.frameview);
        premiumframereyclerview = findViewById(R.id.premiumview);
        frameLayout1 = findViewById(R.id.frameLayout1);
        logoframelayout = findViewById(R.id.logoframelayout);
        framelayoutcontainer = findViewById(R.id.fragment_container);
        bannerdesign1 = findViewById(R.id.design1);
        bannerdesign2 = findViewById(R.id.design2);
        bannerdesign3 = findViewById(R.id.design3);
        bannerdesign4 = findViewById(R.id.design4);
        bannerdesign5 = findViewById(R.id.design5);
        bannerdesign6 = findViewById(R.id.design6);
        bannerdesign7 = findViewById(R.id.design7);
        bannerdesign8 = findViewById(R.id.design8);
        bannerdesign9 = findViewById(R.id.design9);
        bannerdesign10 = findViewById(R.id.design10);
        bannerdesign11 = findViewById(R.id.design11);
        bannerdesign12 = findViewById(R.id.design12);
        bannerdesign13 = findViewById(R.id.design13);
        bannerdesign14 = findViewById(R.id.design14);
        bannerdesign15 = findViewById(R.id.design15);
        bannerdesign16 = findViewById(R.id.design16);
        bannerdesign17 = findViewById(R.id.design17);

        userdesign1 = findViewById(R.id.userdesign1);
        userdesign2 = findViewById(R.id.userdesign2);
        userdesign3 = findViewById(R.id.userdesign3);
        userdesign4 = findViewById(R.id.userdesign4);
        userdesign5 = findViewById(R.id.userdesign5);
        userdesign6 = findViewById(R.id.userdesign6);
        userdesign7 = findViewById(R.id.userdesign7);
        userdesign8 = findViewById(R.id.userdesign8);
        userdesign9 = findViewById(R.id.userdesign9);
        userdesign10 = findViewById(R.id.userdesign10);
        userdesign11 = findViewById(R.id.userdesign11);
        userdesign12 = findViewById(R.id.userdesign12);
        userdesign13 = findViewById(R.id.userdesign13);
        userdesign14 = findViewById(R.id.userdesign14);
        userdesign15 = findViewById(R.id.userdesign15);
        userdesign16 = findViewById(R.id.userdesign16);
        userdesign17 = findViewById(R.id.userdesign17);
        userdesign18 = findViewById(R.id.userdesign18);
        userdesign19 = findViewById(R.id.userdesign19);
        userdesign20 = findViewById(R.id.userdesign20);
        userdesign21 = findViewById(R.id.userdesign21);
        userdesign22 = findViewById(R.id.userdesign22);

        businessframes = findViewById(R.id.businessframes);
        userframes = findViewById(R.id.userframes);
        usercard = findViewById(R.id.usercard);
        businesscard = findViewById(R.id.businesscard);
        premiumcard = findViewById(R.id.premiumcard);
        premiumtextview = findViewById(R.id.premiumtextview);
        usertextview = findViewById(R.id.usertextview);
        businesstextview = findViewById(R.id.businesstextview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.mainsecondcolor));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        // Iterate over each child (TextView) in the GridLayout
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View child = gridLayout.getChildAt(i);
            if (child instanceof TextView) {
                TextView colorTextView = (TextView) child;
                colorTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Change the color of shopnametext when a color TextView is clicked
                        int color = ((ColorDrawable) colorTextView.getBackground()).getColor();
                        shopnametext.setTextColor(color);
                    }
                });
            }
        }

        // Set click listeners



        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference().child("Shop");
        userRef = database.getReference("Users");
        bannerDesignRef = FirebaseDatabase.getInstance().getReference("Business");
        adref = FirebaseDatabase.getInstance().getReference("ads");
        frameRef = FirebaseDatabase.getInstance().getReference("Frames");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        contactNumber = getIntent().getStringExtra("contactNumber");
        shopName = getIntent().getStringExtra("shopName");
        shopimage = getIntent().getStringExtra("shopimage");
        ownername = getIntent().getStringExtra("ownerName");
        shopaddress = getIntent().getStringExtra("shopaddress");

        System.out.println("rsgrfcd " +ownername);
        shopnametext.setText(shopName);
        shopcontactnumbertext.setText(contactNumber);
        shopownernametext.setText(ownername);
        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        Glide.with(this).load(imageUrl).centerCrop().into(imageView);

        if (shopimage != null){
            Glide.with(CreateBanner.this).load(shopimage).into(logoimage);
        }else {
            logoimage.setVisibility(View.GONE);
        }

        SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        switchUser = preferences.getString("userType", null);




        recyclerView = findViewById(R.id.bannerdesignview);
        // Initialize RecyclerView
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        businessBannerAdapter = new BusinessBannerAdapter(this,this, true);
//        recyclerView.setAdapter(businessBannerAdapter);

        System.out.println("contacTNUefr " +contactNumber);
//        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @SuppressLint("WrongViewCast")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    Boolean premium = snapshot.child("premium").getValue(Boolean.class);
//                    System.out.println("hfdbv " +premium);
//                    if (premium != null && premium == true){
//                        framesretrive();
//                    }else{
//                      //  premiumcard.setVisibility(View.GONE);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled event
//            }
//        });

        postcard.setVisibility(View.GONE);
         businessfragment = getIntent().getStringExtra("BusinessFragment");
         System.out.println("dfxb " +businessfragment);
        if (businessfragment != null){
            postcard.setVisibility(View.VISIBLE);
        }

        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    boolean contactNumberExists = false;

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String key = dataSnapshot.getKey();
                        System.out.println("Key: " + key);

                        if (key.equals(contactNumber)) {
                            contactNumberExists = true;
                            break;  // No need to continue checking if found
                        }
                    }

                    // Check if the contactNumber exists
                    if (contactNumberExists) {
                        businesscard.setVisibility(View.VISIBLE);
                    } else {
                        businesscard.setVisibility(View.GONE);
                    }



                    if (contactNumberExists && businessfragment != null && switchUser.equals("user")){
                        postcard.setVisibility(View.GONE);
                    }else {
                        if (switchUser==null){
                            postcard.setVisibility(View.GONE);
                        }else {
                            postcard.setVisibility(View.VISIBLE);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });




        // Set default selection
        setDefaultSelection(usercard, usertextview);
        showDefaultFragments();
        usercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelection(usercard, usertextview);
                businessframes.setVisibility(View.GONE);
                userframes.setVisibility(View.VISIBLE);
                premiumframereyclerview.setVisibility(View.GONE);
                // Reset the selected banner when switching to usercard
                resetBannerSelection();

                setDefaultSelectedImageView(userdesign1, new Fragment_personal_1());
                setImageViewClickListener(userdesign1, new Fragment_personal_1());
                setImageViewClickListener(userdesign2, new Fragment_personal_2());
                setImageViewClickListener(userdesign3, new Fragment_personal_3());
                setImageViewClickListener(userdesign4, new Fragment_personal_4());
                setImageViewClickListener(userdesign5, new Fragment_personal_5());
                setImageViewClickListener(userdesign6, new Fragment_personal_6());
                setImageViewClickListener(userdesign7, new Fragment_personal_7());
                setImageViewClickListener(userdesign8, new Fragment_personal_8());
                setImageViewClickListener(userdesign9, new Fragment_personal_9());
                setImageViewClickListener(userdesign10, new Fragment_personal_10());
                setImageViewClickListener(userdesign11, new Fragment_personal_11());
                setImageViewClickListener(userdesign12, new Fragment_personal_12());
                setImageViewClickListener(userdesign13, new Fragment_personal_13());
                setImageViewClickListener(userdesign14, new Fragment_personal_14());
                setImageViewClickListener(userdesign15, new Fragment_personal_15());
                setImageViewClickListener(userdesign16, new Fragment_personal_16());
                setImageViewClickListener(userdesign17, new Fragment_personal_17());
                setImageViewClickListener(userdesign18, new Fragment_personal_18());
                setImageViewClickListener(userdesign19, new Fragment_personal_19());
                setImageViewClickListener(userdesign20, new Fragment_personal_20());
                setImageViewClickListener(userdesign21, new Fragment_personal_21());
                setImageViewClickListener(userdesign22, new Fragment_personal_22());
            }
        });

        businesscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSelection(businesscard, businesstextview);
                businessframes.setVisibility(View.VISIBLE);
                userframes.setVisibility(View.GONE);
                premiumframereyclerview.setVisibility(View.GONE);
                // Reset the selected banner when switching to usercard
                resetBannerSelection();

                setDefaultSelectedImageView(bannerdesign1, new Fragment1());
                setImageViewClickListener(bannerdesign1, new Fragment1());
                setImageViewClickListener(bannerdesign2, new Fragment2());
                setImageViewClickListener(bannerdesign3, new Fragment3());
                setImageViewClickListener(bannerdesign4, new Fragment4());
                setImageViewClickListener(bannerdesign5, new Fragment5());
                setImageViewClickListener(bannerdesign6, new Fragment6());
                setImageViewClickListener(bannerdesign7, new Fragment7());
                setImageViewClickListener(bannerdesign8, new Fragment8());
                setImageViewClickListener(bannerdesign9, new Fragment9());
                setImageViewClickListener(bannerdesign10, new Fragment10());
                setImageViewClickListener(bannerdesign11, new Fragment11());
                setImageViewClickListener(bannerdesign12, new Fragment12());
                setImageViewClickListener(bannerdesign13, new Fragment13());
                setImageViewClickListener(bannerdesign14, new Fragment14());
                setImageViewClickListener(bannerdesign15, new Fragment15());
                setImageViewClickListener(bannerdesign16, new Fragment16());
                setImageViewClickListener(bannerdesign17, new Fragment17());
            }
        });


        premiumcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            premium = snapshot.child("premium").getValue(Boolean.class);
                            if (premium != null && !premium) {
                                // User has a premium membership, navigate to PremiumMembership activity
                                showImageSelectiondialog();
                            } else {
                                // User doesn't have a premium membership, show premium frames
                               // framesretrive();
                                userframesrecive();
                                updateSelection(premiumcard, premiumtextview);
                                resetBannerSelection();
                                businessframes.setVisibility(View.GONE);
                                userframes.setVisibility(View.GONE);
                                premiumframereyclerview.setVisibility(View.VISIBLE);

                                // Check if there are images in the premiumframe list
                                if (imageUrls != null && !imageUrls.isEmpty()) {
                                    // Get the first image URL from the list
                                    String defaultImageUrl = imageUrls.get(0);

                                    // Create a bundle to pass data to the fragment
                                    Bundle bundle = new Bundle();
                                    bundle.putString("bannerimage", defaultImageUrl);

                                    // Set the selected image position as an argument (reset to -1)
                                    bundle.putInt("selectedImagePosition", -1);

                                    // Create the fragment and set arguments
                                    FragmentPremiumFrame fragmentPremiumFrame = new FragmentPremiumFrame();
                                    fragmentPremiumFrame.setArguments(bundle);

                                    // Load the fragment
                                    loadFragments(fragmentPremiumFrame);
                                }

                                // Reset the selected image position in the adapter
                                frameAdapter.setSelectedImagePosition(0);
                                frameAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });
            }
        });


        userRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    premium = snapshot.child("premium").getValue(Boolean.class);
                    if (premium.equals(true)) {
                        logoimageview.setVisibility(View.GONE);
                         cancellogoimageview.setVisibility(View.GONE);
                    } else{
                        logoimageview.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });


        logoimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable border = new GradientDrawable();
                border.setStroke(2, Color.GRAY);
                logoimageview.setImageDrawable(border);
                cancellogoimageview.setVisibility(View.VISIBLE);

            }
        });

        cancellogoimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CreateBanner.this, PremiumMembership.class);
//                startActivity(intent);
                showImageselectiondialog();
            }
        });

        framelayoutcontainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GradientDrawable border = new GradientDrawable();
                border.setStroke(2, Color.GRAY);
                logoimageview.setImageDrawable(null);
                logoimageview.setBackgroundResource(0);
                cancellogoimageview.setVisibility(View.GONE);
            }
        });


        postcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureAndPostImage();
            }
        });

        sharecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String currentDate = sdf.format(new Date());

                DatabaseReference clickRef = userRef.child(contactNumber).child("downloadandshare").child(currentDate);

                clickRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            isdownloaded = snapshot.getValue(Boolean.class);
                            userRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        premium = snapshot.child("premium").getValue(Boolean.class);

                                        if (isdownloaded != null && isdownloaded.equals(true) && !premium.equals(true)) {
//                                            Toast.makeText(CreateBanner.this, "You have already share an image for today", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(CreateBanner.this, PremiumMembership.class);
//                                            startActivity(intent);
                                            showImageSelectionDialog();
                                        } else {
                                            // Allow saving the image since premium is true or "downloadandshare" for the current date doesn't exist
                                            captureAndShareImage();
                                            // If needed, update the "downloadandshare" node for the current date to indicate the image has been downloaded
                                            clickRef.setValue(true);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });
                        } else {
                            // "downloadandshare" node for the current date does not exist, save the image
                            captureAndShareImage();
                            // If needed, create the "downloadandshare" node for the current date and set its value to true
                            clickRef.setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });
            }
        });

        savecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String currentDate = sdf.format(new Date());

                DatabaseReference clickRef = userRef.child(contactNumber).child("downloadandshare").child(currentDate);

                clickRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            isdownloaded = snapshot.getValue(Boolean.class);
                            userRef.child(contactNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        premium = snapshot.child("premium").getValue(Boolean.class);

                                        if (isdownloaded != null && isdownloaded.equals(true) && !premium.equals(true)) {
//                                            Toast.makeText(CreateBanner.this, "You have already downloaded an image for today", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(CreateBanner.this, PremiumMembership.class);
//                                            startActivity(intent);

                                            showImageSelectionDialog();
                                        } else {
                                            // Allow saving the image since premium is true or "downloadandshare" for the current date doesn't exist
                                            captureAndSaveImage();
                                            // If needed, update the "downloadandshare" node for the current date to indicate the image has been downloaded
                                            clickRef.setValue(true);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle onCancelled
                                }
                            });
                        } else {
                            // "downloadandshare" node for the current date does not exist, save the image
                            captureAndSaveImage();
                            // If needed, create the "downloadandshare" node for the current date and set its value to true
                            clickRef.setValue(true);
                        }
                    }

                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                        // Handle onCancelled
                    }
                });
            }
        });


        setMovableZoomableTouchListener(logoimage);
        setMovableZoomableTouchListener(shopnametext);  // Apply to your specific View type
        setMovableZoomableTouchListener(shopcontactnumbertext);
        setMovableZoomableTouchListener(shopownernametext);
        setMovableZoomableTouchListener(text1);
        setMovableZoomableTouchListener(text2);

        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        addtextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddTextClick(v);
                edittextlayout.setVisibility(View.VISIBLE);
            }
        });

        shopnametext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showKeyboardAndHandleTextChange();
            }
        });

        cancelimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the layout when the cancel image is clicked
                edittextlayout.setVisibility(View.GONE);
            }
        });

        checkimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the text from the edittext
                String newText = texteditor.getText().toString();

                // Check if text1 is empty
                if (TextUtils.isEmpty(text1.getText())) {
                    text1.setText(newText);
                }
                // Check if text2 is empty
                else if (TextUtils.isEmpty(text2.getText())) {
                    text2.setText(newText);
                }
                // Both text1 and text2 are not empty
                else {
                    // Hide the layout when both text views are not empty
                    edittextlayout.setVisibility(View.GONE);
                    return; // Exit the method to prevent further execution
                }

                // Hide the layout when the check image is clicked
                edittextlayout.setVisibility(View.GONE);
            }
        });





        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update text sizes based on seekBar1 progress
                shopnametext.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle touch start
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle touch stop
            }
        });


        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Ensure progress does not exceed the maximum size
                int maxSize = 500; // Maximum size in dp
                int adjustedProgress = Math.min(progress, maxSize);

                // Handle changes in seekbar2, e.g., update image width
                // Example for updating image width:
                ViewGroup.LayoutParams layoutParams = logoimage.getLayoutParams();
                layoutParams.width = progress;
                logoimage.setLayoutParams(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle touch start
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle touch stop
            }
        });

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // Hide frameLayout1 when a tap occurs outside any movable TextView
                if (!isTextViewClicked) {
                    frameLayout1.setVisibility(View.GONE);
                }
                return true;
            }
        });

        bannerdesign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", shopimage); // Add your data here
                bundle.putString("shopname", shopName); // Add your data here
                bundle.putString("shopcontactnumber", contactNumber); // Add your data here
                bundle.putString("shopownername", ownername); // Add your data here
                bundle.putString("shopaddress", shopaddress);
                bundle.putString("bannerimage", image);

                Fragment1 fragment1 = new Fragment1();
                fragment1.setArguments(bundle);

                loadFragment(fragment1);
             //   setImageViewClickListener(bannerdesign1, new Fragment1());
            }
        });
        bannerdesign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", shopimage); // Add your data here
                bundle.putString("shopname", shopName); // Add your data here
                bundle.putString("shopcontactnumber", contactNumber); // Add your data here
                bundle.putString("shopownername", ownername); // Add your data here
                bundle.putString("shopaddress", shopaddress);

                Fragment2 fragment2 = new Fragment2();
                fragment2.setArguments(bundle);

                loadFragment(fragment2);
             //   setImageViewClickListener(bannerdesign2, new Fragment2());
            }
        });
        bannerdesign3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", shopimage); // Add your data here
                bundle.putString("shopname", shopName); // Add your data here
                bundle.putString("shopcontactnumber", contactNumber); // Add your data here
                bundle.putString("shopownername", ownername); // Add your data here
                bundle.putString("shopaddress", shopaddress);

                Fragment3 fragment3 = new Fragment3();
                fragment3.setArguments(bundle);

                loadFragment(fragment3);
             //   setImageViewClickListener(bannerdesign3, new Fragment3());
            }
        });
        bannerdesign4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", shopimage); // Add your data here
                bundle.putString("shopname", shopName); // Add your data here
                bundle.putString("shopcontactnumber", contactNumber); // Add your data here
                bundle.putString("shopownername", ownername); // Add your data here
                bundle.putString("shopaddress", shopaddress);

                Fragment4 fragment4 = new Fragment4();
                fragment4.setArguments(bundle);

                loadFragment(fragment4);
             //   setImageViewClickListener(bannerdesign4, new Fragment4());
            }
        });
        bannerdesign5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", shopimage); // Add your data here
                bundle.putString("shopname", shopName); // Add your data here
                bundle.putString("shopcontactnumber", contactNumber); // Add your data here
                bundle.putString("shopownername", ownername); // Add your data here
                bundle.putString("shopaddress", shopaddress);

                Fragment5 fragment5 = new Fragment5();
                fragment5.setArguments(bundle);

                loadFragment(fragment5);
              //  setImageViewClickListener(bannerdesign5, new Fragment5());
            }
        });

        bannerdesign6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", shopimage); // Add your data here
                bundle.putString("shopname", shopName); // Add your data here
                bundle.putString("shopcontactnumber", contactNumber); // Add your data here
                bundle.putString("shopownername", ownername); // Add your data here
                bundle.putString("shopaddress", shopaddress);

                Fragment6 fragment6 = new Fragment6();
                fragment6.setArguments(bundle);

                loadFragment(fragment6);
                //  setImageViewClickListener(bannerdesign5, new Fragment5());
            }
        });





//        frameAdapter = new FrameAdapter(this, CreateBanner.this);
//        framerecyclerview.setAdapter(frameAdapter);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        framerecyclerview.setLayoutManager(layoutManager);
//        framesretrive();

        frameAdapter = new FrameAdapter(this, CreateBanner.this);
        premiumframereyclerview.setAdapter(frameAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        premiumframereyclerview.setLayoutManager(layoutManager);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
//            requestManageAllFilesAccessPermission();
//        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void captureAndPostImage() {
        cancellogoimageview.setVisibility(View.GONE);
        Bitmap backgroundBitmap = getBitmapFromView(imageView);
        Bitmap mergedBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mergedBitmap);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        frameLayout.draw(canvas);
        logoframelayout.draw(canvas);
        postMergedImage(mergedBitmap);
    }

    private void postMergedImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String key = databaseRef.child(contactNumber).push().getKey();
        String imagePath = "bannerimages/" + contactNumber + "/" + key + ".jpg";

        // Save the bitmap to a file in the cache directory
        File cachePath = new File(getCacheDir(), "images");
        cachePath.mkdirs();
        File imageFile = new File(cachePath, key + ".jpg");

        try {
            FileOutputStream stream = new FileOutputStream(imageFile);
            stream.write(data);
            stream.flush();
            stream.close();

            // Share the locally saved image using Intent
            postImage(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error
        }
    }

    private void postImage(File imageFile) {
        Uri imageUri = null;

        try {
            imageUri = FileProvider.getUriForFile(
                    CreateBanner.this,
                    "com.spark.swarajyabiz.provider",
                    imageFile
            );
        } catch (Exception e) {
            Toast.makeText(this, "Error creating FileProvider URI "+e, Toast.LENGTH_SHORT).show();
            Log.d("sdfd"," "+e);
            e.printStackTrace();
            return; // Exit the method if an exception occurs
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        userRef.child(contactNumber).child("downloadandshare").child(currentDate).setValue(true);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");

        if (imageUri != null) {
            Intent intent = new Intent(getApplicationContext(), AddPostNew.class);

            intent.putExtra("imageUri", String.valueOf(imageUri));
            System.out.println("dsvfrf " +imageUri);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error: Unable to get FileProvider URI", Toast.LENGTH_SHORT).show();
        }
    }
    private void resetBannerSelection() {
        if (selectedImageView != null) {
            resetImageView(selectedImageView);
            selectedImageView = null; // Reset the selected banner
        }
    }

    private void showDefaultFragments() {
        // Show fragments you want by default

        userframes.setVisibility(View.VISIBLE);
        businessframes.setVisibility(View.GONE);
        setImageViewClickListener(userdesign1, new Fragment_personal_1());
        setDefaultSelectedImageView(userdesign1, new Fragment_personal_1());
        setImageViewClickListener(userdesign2, new Fragment_personal_2());
        setImageViewClickListener(userdesign3, new Fragment_personal_3());
        setImageViewClickListener(userdesign4, new Fragment_personal_4());
        setImageViewClickListener(userdesign5, new Fragment_personal_5());
        setImageViewClickListener(userdesign6, new Fragment_personal_6());
        setImageViewClickListener(userdesign7, new Fragment_personal_7());
        setImageViewClickListener(userdesign8, new Fragment_personal_8());
        setImageViewClickListener(userdesign9, new Fragment_personal_9());
        setImageViewClickListener(userdesign10, new Fragment_personal_10());
        setImageViewClickListener(userdesign11, new Fragment_personal_11());
        setImageViewClickListener(userdesign12, new Fragment_personal_12());
        setImageViewClickListener(userdesign13, new Fragment_personal_13());
        setImageViewClickListener(userdesign14, new Fragment_personal_14());
        setImageViewClickListener(userdesign15, new Fragment_personal_15());
        setImageViewClickListener(userdesign16, new Fragment_personal_16());
        setImageViewClickListener(userdesign17, new Fragment_personal_17());
        setImageViewClickListener(userdesign18, new Fragment_personal_18());
        setImageViewClickListener(userdesign19, new Fragment_personal_19());
        setImageViewClickListener(userdesign20, new Fragment_personal_20());
        setImageViewClickListener(userdesign21, new Fragment_personal_21());
        setImageViewClickListener(userdesign22, new Fragment_personal_22());
    }

    // Method to set default selection
    private void setDefaultSelection(CardView cardView, TextView textView) {
        cardView.setCardBackgroundColor(getResources().getColor(R.color.ListColor));
        textView.setTextColor(getResources().getColor(R.color.white));
    }

    // Method to update selection
    private void updateSelection(CardView selectedCard, TextView selectedTextView) {
        // Reset previous selection
        resetSelection();
        // Update the selected card and text color
        selectedCard.setCardBackgroundColor(getResources().getColor(R.color.ListColor));
        selectedTextView.setTextColor(getResources().getColor(R.color.white));
    }

    // Method to reset the selection to default
    private void resetSelection() {
        usercard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        businesscard.setCardBackgroundColor(getResources().getColor(android.R.color.white));
        premiumcard.setCardBackgroundColor(getResources().getColor(android.R.color.white));

        usertextview.setTextColor(getResources().getColor(R.color.black));
        businesstextview.setTextColor(getResources().getColor(R.color.black));
        premiumtextview.setTextColor(getResources().getColor(R.color.black));
    }

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);  // Optional: Add the transaction to the back stack
        transaction.commit();
       // selectedImageView = null;
    }

    private void setDefaultSelectedImageView(final ImageView imageView, final Fragment fragment) {
        // Update the selected ImageView
        selectedImageView = imageView;
        updateImageView(selectedImageView);
//        if (premium.equals(true)){
//            cancellogoimageview.setVisibility(View.GONE);
//        }else{
//            cancellogoimageview.setVisibility(View.VISIBLE);
//        }

        // Load the corresponding fragment
        Bundle bundle = new Bundle();
        bundle.putString("shopimage", shopimage);
        bundle.putString("shopname", shopName);
        bundle.putString("shopcontactnumber", contactNumber);
        bundle.putString("shopownername", ownername);
        bundle.putString("shopaddress", shopaddress);
        fragment.setArguments(bundle);
        loadFragment(fragment);
    }

    private void setImageViewClickListener(final ImageView imageView, final Fragment fragment) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageView != null) {
                    // Reset the previously selected ImageView
                    resetImageView(selectedImageView);
                }
                GradientDrawable border = new GradientDrawable();
                border.setStroke(2, Color.GRAY);
                logoimageview.setImageDrawable(null);

                // Update the selected ImageView
                selectedImageView = imageView;
                updateImageView(selectedImageView);
                Bundle bundle = new Bundle();
                bundle.putString("shopimage", shopimage);
                bundle.putString("shopname", shopName);
                bundle.putString("shopcontactnumber", contactNumber);
                bundle.putString("shopownername", ownername);
                bundle.putString("shopaddress", shopaddress);
                fragment.setArguments(bundle);
                loadFragment(fragment);
            }
        });
    }

    // Important working border change color

    private void resetImageView(ImageView imageView) {
        GradientDrawable border = new GradientDrawable();
        border.setStroke(4, Color.GRAY);
        imageView.setImageDrawable(border);
        // Reset any other customization you may have applied, e.g., checked icon
    }


    @SuppressLint("ResourceAsColor")
    private void updateImageView(ImageView imageView) {
        // Set the border color to blue
        GradientDrawable border = new GradientDrawable();
//        imageView.setImageResource(R.drawable.ic_baseline_blue_check_24);
        border.setColor(R.color.transparant); // Set transparent background
        border.setStroke(5, Color.BLACK); // Set border color and width
        imageView.setImageDrawable(border);

        // Show a checked icon (you can customize this part)
        // Assuming you have an "ic_checked" drawable in your resources


        // Customize any other appearance as needed
    }



    public interface ButtonClickListener {
        void onAddLogoButtonClick();
        void onAddTextButtonClick();
    }

    public void setButtonClickListener(ButtonClickListener listener) {
        this.buttonClickListener = listener;
    }

    // Button click handlers
    public void onAddLogoClick(View view) {
        if (isFragmentActive) {
            if (buttonClickListener != null) {
                buttonClickListener.onAddLogoButtonClick();
            }
        }
    }

    // Button click handlers
    public void onAddTextClick(View view) {
        // Check if the fragment is active before showing the addtextbtn
        if (isFragmentActive) {
            if (buttonClickListener != null) {
                buttonClickListener.onAddTextButtonClick();
            }
        }
    }

    public void setFragmentActive(boolean active) {
        this.isFragmentActive = active;
    }

    private void setImage(int bannerdesign){
        Drawable existingImage = imageView.getDrawable();

        // Asynchronously load the clicked image into a Drawable
        Glide.with(this)
                .load(bannerdesign)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable newImage, @Nullable Transition<? super Drawable> transition) {
                        // Combine the drawables (layer new image on top of existing image)
                        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{existingImage, newImage});

                        // Set the combined drawable to the ImageView
                        imageView.setImageDrawable(layerDrawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleared state (optional)
                    }
                });
    }


    private void captureAndSaveImage() {
        // Get the background image as a bitmap
        cancellogoimageview.setVisibility(View.GONE);
        Bitmap backgroundBitmap = getBitmapFromView(imageView);

        // Create a new bitmap with the same dimensions as the background
        Bitmap mergedBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // Create a canvas to draw the merged bitmap
        Canvas canvas = new Canvas(mergedBitmap);

        // Draw the background image onto the canvas
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);

        // Draw the frameLayout1 (including logoimage and shopnametext) onto the canvas
        frameLayout.draw(canvas);
        logoframelayout.draw(canvas);


        // Save the merged bitmap
      //  saveMergedImage(mergedBitmap);


        // Save the merged bitmap and get the URI of the saved image
         saveMergedImage(mergedBitmap);

        // Share the saved image
//        if (savedImageUri != null) {
//            shareImage(savedImageUri);
//        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestManageAllFilesAccessPermission() {
        if (!Environment.isExternalStorageManager()) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(intent);
        }

    }




    private Uri saveMergedImage(Bitmap bitmap) {
        ContentResolver resolver = getContentResolver();

        String displayName = "image.jpg";
        String mimeType = "image/jpeg";

        // Get the external storage directory
        String directory = Environment.DIRECTORY_PICTURES;
        String fileName = System.currentTimeMillis() + "_image.jpg";
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
        contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, directory);

        // Insert the image details into the MediaStore
        Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        System.out.println("edfsfc " +imageUri);

        try {
            if (imageUri != null) {
                // Open an output stream using the content resolver and write the bitmap data to it
                try (OutputStream outputStream = resolver.openOutputStream(imageUri)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                    String currentDate = sdf.format(new Date());
                    userRef.child(contactNumber).child("downloadandshare").child(currentDate).setValue(true);

                    return imageUri;
                }
            } else {
                // Handle the case where imageUri is null (insertion failed)
                Toast.makeText(this, "Failed to save image: Image URI is null", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error
            Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    // working for both share and save image
//    private void shareImage(Uri imageUri) {
//        // Create an intent with ACTION_SEND
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//
//        // Set the type of the content (image)
//        shareIntent.setType("image/jpeg");
//
//        // Put the image URI as an extra to the intent
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//
//        // Optionally, set a subject for the shared content
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Image");
//
//        // Start the chooser activity to show all available sharing options
//        startActivity(Intent.createChooser(shareIntent, "Share Image"));
//    }

    private void captureAndShareImage(){
        cancellogoimageview.setVisibility(View.GONE);
        Bitmap backgroundBitmap = getBitmapFromView(imageView);
        Bitmap mergedBitmap = Bitmap.createBitmap(backgroundBitmap.getWidth(), backgroundBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mergedBitmap);
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);
        frameLayout.draw(canvas);
        logoframelayout.draw(canvas);
        shareMergedImage(mergedBitmap);
    }

    private void shareMergedImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        String key = databaseRef.child(contactNumber).push().getKey();
        String imagePath = "bannerimages/" + contactNumber + "/" + key + ".jpg";

        // Save the bitmap to a file in the cache directory
        File cachePath = new File(getCacheDir(), "images");
        cachePath.mkdirs();
        File imageFile = new File(cachePath, key + ".jpg");

        try {
            FileOutputStream stream = new FileOutputStream(imageFile);
            stream.write(data);
            stream.flush();
            stream.close();

            // Share the locally saved image using Intent
            shareImage(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error
        }
    }

    private void shareImage(File imageFile) {
        Uri imageUri = null;

        try {
            imageUri = FileProvider.getUriForFile(
                    CreateBanner.this,
                    "com.spark.swarajyabiz.provider",
                    imageFile
            );
        } catch (Exception e) {
            Toast.makeText(this, "Error creating FileProvider URI "+e, Toast.LENGTH_SHORT).show();
            Log.d("sdfd"," "+e);
            e.printStackTrace();
            return; // Exit the method if an exception occurs
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        userRef.child(contactNumber).child("downloadandshare").child(currentDate).setValue(true);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");

        if (imageUri != null) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            Toast.makeText(this, "Error: Unable to get FileProvider URI", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    // Set the selected image to the ImageView
                    logoimage.setImageBitmap(bitmap);
                    customImageView.setVisibility(View.VISIBLE);

                    // Reset matrix and scale factor when a new image is selected
                    selectedImageView.setScaleType(ImageView.ScaleType.MATRIX);
                    scaleFactor = 1f;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setMovableTouchListener(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return moveView(event, view);
            }
        });
    }

    private void setMovableZoomableTouchListener(final View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scaleGestureDetector.onTouchEvent(event);
                return moveView(event, view);
            }
        });

//        // Set click listener for TextView
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Show frameLayout1 when the TextView is clicked
//                frameLayout1.setVisibility(View.VISIBLE);
//                isTextViewClicked = true;
//            }
//        });
//
//        // Set long click listener for TextView
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                // Handle long click as needed
//                return true;
//            }
//        });
    }

    private boolean moveView(MotionEvent event, View view) {
        float x = event.getRawX();
        float y = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchX = x;
                lastTouchY = y;
                break;

            case MotionEvent.ACTION_MOVE:
                float deltaX = x - lastTouchX;
                float deltaY = y - lastTouchY;

                // Update the position of the view within the bounds of frameLayout
                float newX = view.getX() + deltaX;
                float newY = view.getY() + deltaY;

                if (newX < 0) newX = 0;
                if (newY < 0) newY = 0;
                if (newX + view.getWidth() > frameLayout.getWidth())
                    newX = frameLayout.getWidth() - view.getWidth();
                if (newY + view.getHeight() > frameLayout.getHeight())
                    newY = frameLayout.getHeight() - view.getHeight();

                view.setX(newX);
                view.setY(newY);

                // Check if the moved view is the shopnametext TextView
                if (view.getId() == R.id.shopnametext) {
                    // Show frameLayout1
                  //  frameLayout1.setVisibility(View.VISIBLE);
                    isTextViewClicked = true;
//                    int progress = (int) ((seekBar1.getMax() * newX) / frameLayout.getWidth());
//                    seekBar1.setProgress(progress);
                }

                if (view.getId() == R.id.shopcontactNumbertext) {
                    // Show frameLayout1
                  //  frameLayout1.setVisibility(View.VISIBLE);
                    isTextViewClicked = true;
//                    int progress = (int) ((seekBar1.getMax() * newX) / frameLayout.getWidth());
//                    seekBar1.setProgress(progress);
                }

                if (view.getId() == R.id.shopownernametext) {
                    // Show frameLayout1
                  //  frameLayout1.setVisibility(View.VISIBLE);
                    isTextViewClicked = true;
//                    int progress = (int) ((seekBar1.getMax() * newX) / frameLayout.getWidth());
//                    seekBar1.setProgress(progress);
                }

                if (view.getId() == R.id.text1) {
                    // Show frameLayout1
                  //  frameLayout1.setVisibility(View.VISIBLE);
                    isTextViewClicked = true;
                }
                if (view.getId() == R.id.text2) {
                    // Show frameLayout1
                 //   frameLayout1.setVisibility(View.VISIBLE);
                    isTextViewClicked = true;
                }

                // Update the last touch coordinates
                lastTouchX = x;
                lastTouchY = y;
                break;

        }

        // Detect tap events on the parent layout
        gestureDetector.onTouchEvent(event);

        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Check which view is being touched and apply scaling accordingly
            if (detector.getFocusX() >= logoimage.getX() && detector.getFocusX() < logoimage.getX() + logoimage.getWidth()
                    && detector.getFocusY() >= logoimage.getY() && detector.getFocusY() < logoimage.getY() + logoimage.getHeight()) {
                // Apply the scaling only to the logoImageView
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f)); // Restrict zoom levels
                logoimage.setScaleX(scaleFactor);
                logoimage.setScaleY(scaleFactor);
            }

            return true;
        }
    }

    private void showKeyboardAndHandleTextChange() {
        // Show the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(shopnametext, InputMethodManager.SHOW_IMPLICIT);

        // Set an EditText with the current text to handle text changes
        final EditText editText = new EditText(this);
        editText.setText(shopnametext.getText());
        editText.setSelection(shopnametext.getText().length());
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide the keyboard when focus is lost
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    handleTextChange(editText.getText().toString());
                }
            }
        });

        // Replace TextView with EditText temporarily
        if (shopnametext.getParent() instanceof FrameLayout) {
            FrameLayout parentLayout = (FrameLayout) shopnametext.getParent();
            int index = parentLayout.indexOfChild(shopnametext);
            parentLayout.removeView(shopnametext);
            parentLayout.addView(editText, index);
        }

        // Set focus on EditText
        editText.requestFocus();
    }

    private void handleTextChange(String newText) {
        // Handle text change, e.g., update the TextView
        shopnametext.setText(newText);
    }

    @Override
    public void onItemClick(int position, String imageUrl, String businessname) {
        // Load the existing image into a Drawable
        Drawable existingImage = imageView.getDrawable();

        // Asynchronously load the clicked image into a Drawable
        Glide.with(this)
                .load(imageUrl)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable newImage, @Nullable Transition<? super Drawable> transition) {
                        // Combine the drawables (layer new image on top of existing image)
                        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{existingImage, newImage});

                        // Set the combined drawable to the ImageView
                        imageView.setImageDrawable(layerDrawable);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleared state (optional)
                    }
                });
    }

    @Override
    public void onfavClick(int position, ImageView favimageview,String businessName) {

    }

    private void framesretrive(){
        databaseRef.child(contactNumber).child("premiumframe").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    imageUrls = new ArrayList<>();
                    for (DataSnapshot imageSnapshot : snapshot.getChildren()){
                        String imageUrl = imageSnapshot.getValue(String.class);
                        imageUrls.add(imageUrl);
                        System.out.println("sdvscv "+imageUrls.toString());
                    }
                    frameAdapter.setImageUrls(imageUrls);
                    frameAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void userframesrecive(){
        userRef.child(contactNumber).child("premiumframe").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    imageUrls = new ArrayList<>();
                    for (DataSnapshot imageSnapshot : snapshot.getChildren()){
                        String imageUrl = imageSnapshot.getValue(String.class);
                        imageUrls.add(imageUrl);
                        System.out.println("sdvscv "+imageUrls.toString());
                    }
                    frameAdapter.setImageUrls(imageUrls);
                    frameAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onFrameClick(int position, String imageUrl) throws ExecutionException, InterruptedException {
        // Create a new instance of FragmentPremiumFrame
        FragmentPremiumFrame fragmentPremiumFrame = new FragmentPremiumFrame();

        // Create a bundle to pass data to the fragment
        Bundle bundle = new Bundle();
        bundle.putString("bannerimage", imageUrl);

        // Set the selected image position in the bundle
        bundle.putInt("selectedImagePosition", position);

        // Set the arguments for the fragment
        fragmentPremiumFrame.setArguments(bundle);

        // Load the fragment
        loadFragments(fragmentPremiumFrame);

        // Update the selected image in the adapter
        frameAdapter.setSelectedImagePosition(position);
        frameAdapter.notifyDataSetChanged();
    }



    private void loadFragments(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void showImageSelectionDialog() {
        Dialog builder = new Dialog(this);

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        builder.setContentView(customLayout);

        // Find views in the custom layout
        ImageView alertImageView = customLayout.findViewById(R.id.alertImageView);
        TextView alertTitle = customLayout.findViewById(R.id.alertTitle);
        TextView alertMessage = customLayout.findViewById(R.id.alertMessage);
        Button positiveButton = customLayout.findViewById(R.id.positiveButton);

        // Customize the views as needed
        //Glide.with(this).asGif().load(R.drawable.gif2).into(alertImageView); // Replace with your image resource
        alertTitle.setText("प्रीमियम");
        alertMessage.setText("आपले आजचे फ्री लिमिट संपले आहे .\n"+
                "अधिक बॅनर साठी प्रीमियम प्लॅन निवडा.\n");

        // Set positive button click listener
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateBanner.this, PremiumMembership.class);
                startActivity(intent);
                dialog.dismiss(); // Dismiss the dialog after the button click
            }
        });

        // Create and show the dialog
        builder.show();
    }


    private void showImageSelectiondialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.premium_frame_alertdialog, null);
        builder.setView(customLayout);

        // Find views in the custom layout

        ImageView premiumframe1 = customLayout.findViewById(R.id.prem01ImageView);
        ImageView premiumframe2 = customLayout.findViewById(R.id.prem02ImageView);
        ImageView premiumframe3 = customLayout.findViewById(R.id.prem03ImageView);
        TextView alertTitle = customLayout.findViewById(R.id.alertTitle);
        TextView alertMessage = customLayout.findViewById(R.id.alertMessage);
        Button positiveButton = customLayout.findViewById(R.id.positiveButton);
        premiumframe1.setVisibility(View.VISIBLE);
        premiumframe2.setVisibility(View.VISIBLE);
        premiumframe3.setVisibility(View.VISIBLE);

        // Customize the views as needed
        // Replace with your image resource
        alertTitle.setText("प्रीमियम फ्रेम");
        alertMessage.setText("तुमच्या व्यवसायाच्या प्रोफेशनल व प्रीमियम फ्रेम साठी प्रिमियम प्लॅन खरेदी करावा लागेल.");

        // Set positive button click listener
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateBanner.this, PremiumMembership.class);
                startActivity(intent);
                dialog.dismiss(); // Dismiss the dialog after the button click
            }
        });

        // Create and show the dialog
        dialog = builder.create();
        dialog.show();
    }

    private void showImageselectiondialog() {
        Dialog builder = new Dialog(this);

        // Inflate the custom layout
        View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        builder.setContentView(customLayout);
        builder.show();

        // Find views in the custom layout
        ImageView alertImageView = customLayout.findViewById(R.id.alertImageView);
        TextView alertTitle = customLayout.findViewById(R.id.alertTitle);
        TextView alertMessage = customLayout.findViewById(R.id.alertMessage);
        Button positiveButton = customLayout.findViewById(R.id.positiveButton);

        // Customize the views as needed
       // Glide.with(this).asGif().load(R.drawable.gif3).into(alertImageView); // Replace with your image resource
        alertTitle.setText("प्रीमियम");
        alertMessage.setText("लोगो रीमूव करण्यासाठी प्रीमियम प्लॅन खरेदी करावा लागेल.");

        // Set positive button click listener
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateBanner.this, PremiumMembership.class);
                startActivity(intent);
                dialog.dismiss(); // Dismiss the dialog after the button click
            }
        });



    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        // Check if frameLayout1 is visible
//        if (frameLayout1.getVisibility() == View.VISIBLE) {
//            // If visible, hide it
//            frameLayout1.setVisibility(View.GONE);
//        } else {
//            // If not visible, proceed with the default back button behavior
//            super.onBackPressed();
//        }
        //        Intent intent = new Intent(CreateCatalogList.this, Profile.class); // Replace "PreviousActivity" with the appropriate activity class
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
        finish(); // Finish the current activity
    }
}