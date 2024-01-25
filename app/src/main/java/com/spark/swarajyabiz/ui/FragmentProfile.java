package com.spark.swarajyabiz.ui;

import static android.app.Activity.RESULT_OK;
import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kyleduo.switchbutton.SwitchButton;
import com.nex3z.notificationbadge.NotificationBadge;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.spark.swarajyabiz.AllReferrals;
import com.spark.swarajyabiz.BuildConfig;
import com.spark.swarajyabiz.BusinessCard;
import com.spark.swarajyabiz.BusinessPosts;
import com.spark.swarajyabiz.CreateCatalogList;
import com.spark.swarajyabiz.Create_Profile;
import com.spark.swarajyabiz.EditProfile;
import com.spark.swarajyabiz.ItemList;
import com.spark.swarajyabiz.LoginMain;
import com.spark.swarajyabiz.MyOrders;
import com.spark.swarajyabiz.NotificationPage;
import com.spark.swarajyabiz.OrderLists;
import com.spark.swarajyabiz.PomoteShop;
import com.spark.swarajyabiz.Post;
import com.spark.swarajyabiz.PostAdapter;
import com.spark.swarajyabiz.PostJobs;
import com.spark.swarajyabiz.R;
import com.spark.swarajyabiz.Referrals;
import com.spark.swarajyabiz.Scratch_Coupon;
import com.spark.swarajyabiz.Shop;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.reactivex.rxjava3.annotations.NonNull;

public class FragmentProfile extends Fragment implements PostAdapter.PostClickListener{

    String shopcontactNumber, userId, shopimage, image, shopName, name, shopcontactnumber, shopaddress;
    ImageView profileimage, notifiimage, notification;
    TextView username, verifytext, contacttext, usernametext, plantextview, plandesc, infotextview, expDate, dateCount;
    NotificationBadge notificationcount, notificationBadge, referralCount, userreferralCount;
    RelativeLayout notificationcard;
    Uri croppedImageUri;
    private final int PICK_SINGLE_IMAGE_REQUEST = 1;
    private RecyclerView recyclerView;
    private List<Post> postList = new ArrayList<>(); // Create a list to store post details
    private List<ItemList> itemList = new ArrayList<>(); // Create a list to store post details

    private PostAdapter postAdapter;
    List<Shop> shopList;
    Integer notificationCount;
    LinearLayout imagelayout;
    DatabaseReference shopRef;
    RelativeLayout submitcodelayout, sharecodelayout;
    CardView editcard, catlogcard, promotedcard, orderscard, myorderscrd, logoutcard, businesscard, userbusinesscard,
            notificatoncard, createprofilecard, referralcard, postjobcard, businesscardpost,
            usermyorder, userreferral, userlogout;
    private boolean hasLoggedIn = false;
    AlertDialog shopdialog, userdialog;
    private boolean isCreateProfileVisible = true;
    GridLayout businessgrid, usergrid;
    private long remainingDays;
    Button invitebtn;
    ShimmerTextView shimmerTextView;
    SwitchButton switchButton;

    public FragmentProfile() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileimage = view.findViewById(R.id.profileimage);
        username = view.findViewById(R.id.text_name);
        usernametext = view.findViewById(R.id.usernametext);
        plantextview = view.findViewById(R.id.plan_textview);
        plandesc = view.findViewById(R.id.remdays_textview);
        editcard = view.findViewById(R.id.profiledetails);
        catlogcard = view.findViewById(R.id.catalog);
        promotedcard = view.findViewById(R.id.Promoteshop);
        orderscard = view.findViewById(R.id.orders);
        myorderscrd = view.findViewById(R.id.myorder);
        logoutcard = view.findViewById(R.id.message);
        referralcard = view.findViewById(R.id.referralcard);
        postjobcard = view.findViewById(R.id.CreatePostcard);
        businesscardpost = view.findViewById(R.id.bussinesspostcard);
        businesscard = view.findViewById(R.id.Businesscard);
//        userbusinesscard = view.findViewById(R.id.Businesscard_card);
        verifytext = view.findViewById(R.id.verifytext);
        verifytext.setVisibility(View.GONE);
        imagelayout = view.findViewById(R.id.imagelayout);
        createprofilecard = view.findViewById(R.id.createprofilecard);
        contacttext = view.findViewById(R.id.text_contact);
        imagelayout.setVisibility(View.GONE);
        notification = view.findViewById(R.id.notificationimage);     // notification
        notificationcount = view.findViewById(R.id.badgecount); // notification count
        notificationBadge = view.findViewById(R.id.badge_count); // order count
        notificationcard = view.findViewById(R.id.notificationcard);
        referralCount = view.findViewById(R.id.referral_count);
//        userreferralCount = view.findViewById(R.id.referral_Count);
       // notification.setVisibility(View.GONE);
        notificationcount.setVisibility(View.GONE);
//        businessgrid = view.findViewById(R.id.businessgridlayout);
//        usergrid = view.findViewById(R.id.usergridlayout);
//        usermyorder = view.findViewById(R.id.myOrder);
//        userreferral = view.findViewById(R.id.referralCard);
//        userlogout = view.findViewById(R.id.logout);
        infotextview = view.findViewById(R.id.infotextview);
        invitebtn = view.findViewById(R.id.invitebtn);
        shimmerTextView = view.findViewById(R.id.planTags);
        expDate = view.findViewById(R.id.expdateTextView);
        dateCount = view.findViewById(R.id.dateCountText);
        switchButton = view.findViewById(R.id.switchButton);

//        notificatoncard = view.findViewById(R.id.notificationcard);
//        notifiimage = view.findViewById(R.id.notifiimage);


        shopList = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.mainsecondcolor));
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            // Change color of the navigation bar
            window.setNavigationBarColor(ContextCompat.getColor(getContext(), R.color.mainsecondcolor));
            View decorsView = window.getDecorView();
            // Make the status bar icons dark
            decorsView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }


//        recyclerView = view.findViewById(R.id.itemdetails);
//        postAdapter = new PostAdapter(getContext(), itemList, this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(postAdapter);


//        profileimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_SINGLE_IMAGE_REQUEST);
//            }
//        });

//        notificatoncard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), NotificationPage.class);
//                startActivity(intent);
//
//                int count =0;
//                if (notificationCount>= 0){
//                    notifiimage.setVisibility(View.VISIBLE);
//                    count++;
//                }
//
//                notifiimage.setVisibility(View.GONE);
//            }
//        });

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationPage.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                startActivity(intent);
            }
        });

        notificationcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NotificationPage.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                startActivity(intent);
            }
        });

        editcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                startActivity(intent);
            }
        });

        catlogcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateCatalogList.class);
                startActivity(intent);
            }
        });

        promotedcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PomoteShop.class);
                startActivity(intent);
            }
        });


        myorderscrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyOrders.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                startActivity(intent);
            }
        });

        createprofilecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Create_Profile.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                startActivity(intent);
            }
        });

        businesscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BusinessCard.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                startActivity(intent);
            }
        });

//        userbusinesscard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), BusinessCard.class);
//                intent.putExtra("contactNumber",shopcontactNumber);
//                startActivity(intent);
//            }
//        });

        logoutcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });


        referralcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Referrals.class);
//                startActivity(intent);
//                referral();
            }
        });


        invitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         //       Toast.makText(getContext(), "This feature coming soon...", Toast.LENGTH_SHORT).show();
                referral();
            }
        });

        postjobcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostJobs.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                intent.putExtra("shopName", shopName);
                intent.putExtra("shopimage", shopimage);
                intent.putExtra("ownerName", name);
                intent.putExtra("shopaddress", shopaddress);
                startActivity(intent);
            }
        });

        businesscardpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BusinessPosts.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                intent.putExtra("shopName", shopName);
                intent.putExtra("shopimage", shopimage);
                intent.putExtra("ownerName", name);
                intent.putExtra("shopaddress", shopaddress);
                startActivity(intent);

            }
        });


        SharedPreferences sharedPreference = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            contacttext.setText(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String expdate = snapshot.child("ExpDate").getValue(String.class);
                    expDate.setText(expdate);
                    System.out.println("erfbrg " +expdate);

                    userRef.child("Trans").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    System.out.println("rgfsx " +dataSnapshot.getKey());
                                    String plan = dataSnapshot.child("Plan").getValue(String.class);
                                    String trdate = dataSnapshot.child("TrDate").getValue(String.class);
                                    System.out.println("edvdsv"+ plan);
                                    shimmerTextView.setText(plan);
                                    Shimmer shimmer = new Shimmer();
                                    shimmer.start(shimmerTextView);

                                    String expd=expdate;
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    Date date2=null;
                                    try {
                                        date2 = sdf.parse(expd);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date date = new Date();
                                    //ChronoUnit.DAYS.between(date.toInstant(),date.toInstant());
                                    assert date2 != null;
                                    //Log.d("ffgdggg",""+ChronoUnit.DAYS.between(date.toInstant(),date2.toInstant()));
                                    String day= String.valueOf(ChronoUnit.DAYS.between(date.toInstant(),date2.toInstant()));
                                    dateCount.setText(day);
                                    if(day.equals("0")){
                                        dateCount.setTextColor(Color.RED);
                                    }else {

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });


        orderscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderLists.class);
                intent.putExtra("contactNumber",shopcontactNumber);
                intent.putExtra("shopimage",shopimage);
                intent.putExtra("shopName",shopName);
                System.out.println("jhfvbhj " +shopcontactNumber);
                System.out.println("jhfvbhj " +shopimage);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = getContext().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String userType = preferences.getString("userType", ""); // Assuming you store "user" or "business"
        //Toast.makeText(getContext(), ""+userType, Toast.LENGTH_SHORT).show();

        if ("user".equals(userType)) {
            // User mode
            switchButton.setChecked(true);
            editcard.setVisibility(View.GONE);
            catlogcard.setVisibility(View.GONE);
            promotedcard.setVisibility(View.GONE);
            orderscard.setVisibility(View.GONE);
            postjobcard.setVisibility(View.GONE);
            notificationcard.setVisibility(View.GONE);
            businesscardpost.setVisibility(View.GONE);
            businesscard.setVisibility(View.VISIBLE);
            myorderscrd.setVisibility(View.VISIBLE);
            createprofilecard.setVisibility(View.VISIBLE);
        } else if ("business".equals(userType)) {
            // Business mode
            switchButton.setChecked(false);
            shopRef = FirebaseDatabase.getInstance().getReference("Shop");
            shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // User is present in the Shop node, set the switch as business
                        switchButton.setChecked(false);
                        editcard.setVisibility(View.VISIBLE);
                        catlogcard.setVisibility(View.VISIBLE);
                        promotedcard.setVisibility(View.VISIBLE);
                        orderscard.setVisibility(View.VISIBLE);
                        myorderscrd.setVisibility(View.VISIBLE);
                        createprofilecard.setVisibility(View.GONE);
                        postjobcard.setVisibility(View.VISIBLE);
                        notificationcard.setVisibility(View.VISIBLE);
                        businesscardpost.setVisibility(View.VISIBLE);
                        businesscard.setVisibility(View.VISIBLE);
                    } else {
                        // User is not present in the Shop node, set the switch as user
                        switchButton.setChecked(true);
                        editcard.setVisibility(View.GONE);
                        catlogcard.setVisibility(View.GONE);
                        promotedcard.setVisibility(View.GONE);
                        orderscard.setVisibility(View.GONE);
                        postjobcard.setVisibility(View.GONE);
                        notificationcard.setVisibility(View.GONE);
                        businesscardpost.setVisibility(View.GONE);
                        businesscard.setVisibility(View.VISIBLE);
                        myorderscrd.setVisibility(View.VISIBLE);
                        createprofilecard.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "You do not have business profile. Please create a profile.", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                    // Handle onCancelled
                }
            });
        } else if ("".equals(userType)) {
            switchButton.setChecked(true);
            editcard.setVisibility(View.GONE);
            catlogcard.setVisibility(View.GONE);
            promotedcard.setVisibility(View.GONE);
            orderscard.setVisibility(View.GONE);
            postjobcard.setVisibility(View.GONE);
            notificationcard.setVisibility(View.GONE);
            businesscardpost.setVisibility(View.GONE);
            businesscard.setVisibility(View.VISIBLE);
            myorderscrd.setVisibility(View.VISIBLE);
            createprofilecard.setVisibility(View.VISIBLE);

        }

        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Switch is ON (User mode)
                    editor.putString("userType", "user");
                    editor.apply();

                    editcard.setVisibility(View.GONE);
                    catlogcard.setVisibility(View.GONE);
                    promotedcard.setVisibility(View.GONE);
                    orderscard.setVisibility(View.GONE);
                    postjobcard.setVisibility(View.GONE);
                    notificationcard.setVisibility(View.GONE);
                    businesscardpost.setVisibility(View.GONE);
                    businesscard.setVisibility(View.VISIBLE);
                    myorderscrd.setVisibility(View.VISIBLE);
                    createprofilecard.setVisibility(View.VISIBLE);

                 //   Toast.makeText(getContext(), "Switch is ON (User mode)", Toast.LENGTH_SHORT).show();
                } else {
                    // Switch is OFF (Business mode)
                    editor.putString("userType", "business");
                    editor.apply();
                    shopRef = FirebaseDatabase.getInstance().getReference("Shop");
                    shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // User is present in the Shop node, set the switch as business
                                switchButton.setChecked(false);
                                editcard.setVisibility(View.VISIBLE);
                                catlogcard.setVisibility(View.VISIBLE);
                                promotedcard.setVisibility(View.VISIBLE);
                                orderscard.setVisibility(View.VISIBLE);
                                myorderscrd.setVisibility(View.VISIBLE);
                                createprofilecard.setVisibility(View.GONE);
                                postjobcard.setVisibility(View.VISIBLE);
                                notificationcard.setVisibility(View.VISIBLE);
                                businesscardpost.setVisibility(View.VISIBLE);
                                businesscard.setVisibility(View.VISIBLE);
                            } else {
                                // User is not present in the Shop node, set the switch as user
                                switchButton.setChecked(true);
                                editcard.setVisibility(View.GONE);
                                catlogcard.setVisibility(View.GONE);
                                promotedcard.setVisibility(View.GONE);
                                orderscard.setVisibility(View.GONE);
                                postjobcard.setVisibility(View.GONE);
                                notificationcard.setVisibility(View.GONE);
                                businesscardpost.setVisibility(View.GONE);
                                businesscard.setVisibility(View.VISIBLE);
                                myorderscrd.setVisibility(View.VISIBLE);
                                createprofilecard.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), "You do not have business profile. Please create a profile.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                            // Handle onCancelled
                        }
                    });


                }
            }
        });


        // Check if userType is not present, then call retrievePostDetails

        retrievePostDetails();
        retrievecurrentuserItemDetails();
        return view;
    }

    private void referral(){
        DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Referral");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.referral_alertdialog, null);
        builder.setView(dialogView);

        // Buttons in the dialog
        Button saveButton = dialogView.findViewById(R.id.sharebtn);

        EditText referralcode = dialogView.findViewById(R.id.referralcodetext);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView allreferraltext = dialogView.findViewById(R.id.allreferralsImg);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView errormsgtext = dialogView.findViewById(R.id.errormsgtext);
        submitcodelayout = dialogView.findViewById(R.id.submitcodelayout);
        sharecodelayout = dialogView.findViewById(R.id.sharecodelayout);

        DatabaseReference logoRef = FirebaseDatabase.getInstance().getReference("ads");

        logoRef.child("logo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Retrieve the URL for the current index
                    String currentImageUrl = snapshot.child("logoimage").getValue(String.class);
                    System.out.println("fgfdvg " + currentImageUrl);

                   // shareUrl(saveButton, referralcode, referralRef, currentImageUrl, errormsgtext);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String referraltext = referralcode.getText().toString();

                            if (TextUtils.isEmpty(referraltext)) {
                                errormsgtext.setVisibility(View.VISIBLE);
                                errormsgtext.setText("Please enter a mobile number.");
                                return; // Stop further execution
                            }

                            if (!userId.isEmpty()) {
                                DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Users").child(referraltext);

                                // Use addListenerForSingleValueEvent to check if the user exists
                                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            // User already registered or app already installed
                                            Toast.makeText(getContext(), "User already registered or app already installed", Toast.LENGTH_SHORT).show();
                                            errormsgtext.setVisibility(View.VISIBLE);
                                            errormsgtext.setText("Application already installed.");
                                        } else {
                                            // User is not installed yet
                                            errormsgtext.setVisibility(View.GONE);

                                            // Now check if the user is already referred
                                            if (referralRef != null) {
                                                referralRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        boolean isAlreadyReferred = false;

                                                        if (snapshot.exists()) {
                                                            for (DataSnapshot keySnapshot : snapshot.getChildren()) {
                                                                for (DataSnapshot dataSnapshot : keySnapshot.getChildren()) {
                                                                    String referralKey = dataSnapshot.getKey();
                                                                    System.out.println("etfvvda " + referralKey);

                                                                    if (referralKey != null && referralKey.equals(referraltext)) {
                                                                        // User is already referred
                                                                        isAlreadyReferred = true;
                                                                        break;
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        if (isAlreadyReferred) {
                                                            errormsgtext.setVisibility(View.VISIBLE);
                                                            errormsgtext.setText("This number is already referred.");
                                                        } else {
                                                            // Continue with your logic for a new user...
                                                            long maxKey = -1;
                                                            for (DataSnapshot childSnapshot : snapshot.child(userId).getChildren()) {
                                                                long key = Long.parseLong(childSnapshot.getKey());
                                                                if (key > maxKey) {
                                                                    maxKey = key;
                                                                }
                                                            }
                                                            long newKey = maxKey + 1;
                                                           // referralRef.child(userId).child(String.valueOf(newKey)).setValue(referraltext);
                                                            referralRef.child(userId).child(referraltext).setValue("Working in progress");

                                                            String message = "\nApp URL: https://play.google.com/store/apps/details?id=com.spark.swarajyabiz&hl=en-IN";
                                                            // Get the logo as a drawable (replace with your actual logo image)
                                                            @SuppressLint("UseCompatLoadingForDrawables")
                                                            Drawable logoDrawable = getResources().getDrawable(R.drawable.newlogo);
                                                            System.out.println("dfbfb " + logoDrawable.toString());

                                                            downloadImageAndShare(currentImageUrl, referraltext, message);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                                        // Handle onCancelled
                                                    }
                                                });
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        // Handle potential errors, if needed
                                        Log.e("Firebase", "Error checking user existence: " + databaseError.getMessage());
                                    }
                                });
                            } else {
                                // Handle the case where the user ID is empty
                                Toast.makeText(getContext(), "Please enter a valid user ID", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });


        allreferraltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllReferrals.class);
                startActivity(intent);
            }
        });
        shopdialog = builder.create();
        shopdialog.show();
        shopdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }




    private void shareUrl(Button savebtn, EditText referralcode, DatabaseReference referralRef, String currentImageUrl
                          , TextView errormsgtext){

    }
    private void downloadImageAndShare(String imageUrl, String phoneNumber, String message) {
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
                            shareImageWithWhatsApp(imageFile, phoneNumber, message);
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

    private void shareImageWithWhatsApp(File imageFile, String phoneNumber, String message) {
        Uri imageUri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                imageFile
        );

        // Open WhatsApp with the provided phone number and message
        openWhatsApp( phoneNumber, message);
    }

//    private void shareImageWithWhatsApp(File imageFile, String phoneNumber, String message) {
//        Uri imageUri = FileProvider.getUriForFile(
//                requireContext(),
//                BuildConfig.APPLICATION_ID + ".provider",
//                imageFile
//        );
//
//        // Create an Intent with ACTION_SEND
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/*");
//
//        // Put the image and message in the intent
//        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        intent.putExtra(Intent.EXTRA_TEXT, message);
//
//        // Set the package to WhatsApp
//        intent.setPackage("com.whatsapp");
//
//        // Verify that WhatsApp is installed
//        PackageManager pm = requireContext().getPackageManager();
//        if (intent.resolveActivity(pm) != null) {
//            // Start the activity
//            startActivity(intent);
//        } else {
//            // If WhatsApp is not installed, show a message or redirect to the Play Store
//            Toast.makeText(getContext(), "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show();
//            // Alternatively, redirect the user to the Play Store to install WhatsApp
//            // openWhatsAppInPlayStore();
//        }
//    }


    private void openWhatsApp(String phoneNumber, String message) {
        try {
            PackageManager packageManager = requireActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            } else {
                // If WhatsApp is not installed, show a message or redirect to the Play Store
                Toast.makeText(getContext(), "WhatsApp is not installed on your device.", Toast.LENGTH_SHORT).show();
                // Alternatively, redirect the user to the Play Store to install WhatsApp
                // openWhatsAppInPlayStore();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



//    private void referral(){
//        DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Referral").child(userId);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.referral_alertdialog, null);
//        builder.setView(dialogView);
//
//        // Buttons in the dialog
//        Button saveButton = dialogView.findViewById(R.id.sharebtn);
//        ImageView copyreferralcode = dialogView.findViewById(R.id.copyreferralcode);
//        EditText referralcode = dialogView.findViewById(R.id.referralcodetext);
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
//        TextView allreferraltext = dialogView.findViewById(R.id.allreferralstext);
//        submitcodelayout = dialogView.findViewById(R.id.submitcodelayout);
//        sharecodelayout = dialogView.findViewById(R.id.sharecodelayout);
//
//        DatabaseReference logoRef = FirebaseDatabase.getInstance().getReference("ads");
//
//        logoRef.child("logo").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//
//                    // Retrieve the URL for the current index
//                    String currentImageUrl = snapshot.child("logoimage").getValue(String.class);
//                    System.out.println("fgfdvg " + currentImageUrl);
//
//                    saveButton.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            String referraltext = referralcode.getText().toString();
//                            referralRef.child(referraltext).setValue("Not Installed");
//                            String message = "\nApp URL: https://play.google.com/store/apps/details?id=com.spark.swarajyabiz&hl=en-IN";
//                            // Get the logo as a drawable (replace with your actual logo image)
//                            @SuppressLint("UseCompatLoadingForDrawables")
//                            Drawable logoDrawable = getResources().getDrawable(R.drawable.newlogo);
//                            System.out.println("dfbfb " + logoDrawable.toString());
//
//                            downloadImageAndShare(currentImageUrl, message);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle onCancelled
//            }
//        });
//
//
//        allreferraltext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), AllReferrals.class);
//                startActivity(intent);
//            }
//        });
//
//
//        shopdialog = builder.create();
//        shopdialog.show();
//        shopdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
////        shopRef.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                if (dataSnapshot.exists()) {
////                    boolean contactNumberFound = false;
////
////                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
////                        String existcontactnumber = snapshot.child("contactNumber").getValue(String.class);
////                        System.out.println("sdfvdc " + existcontactnumber);
////
////                        // Contact number is present, so set the flag and break out of the loop
////                        if (shopcontactNumber.equals(existcontactnumber)) {
////                            contactNumberFound = true;
////                            break;
////                            //                            contactNumberPresent();
////                        }else {
////                            //                            contactNumberNotPresent();
////                        }
////                    }
////
////                    // Check if the contact number was found
////                    if (contactNumberFound) {
////                        contactNumberPresent();
////                    } else {
////                        contactNumberNotPresent();
////                    }
////                }
////            }
////
////            @Override
////            public void onCancelled(DatabaseError databaseError) {
////                // Handle onCancelled
////            }
////        });
//
//
//    }
//
//    private void downloadImageAndShare(String imageUrl, final String message) {
//        Glide.with(getContext())
//                .asBitmap()
//                .load(imageUrl)
//                .into(new CustomTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
//                        // Save the bitmap to local storage (e.g., app's cache directory)
//                        // Then, share the locally saved image using an Intent
//
//                        // Example code for saving to local storage
//                        // Save the bitmap to a file
//
//                        File cachePath = new File(requireContext().getCacheDir(), "images");
//                        cachePath.mkdirs();
//                        FileOutputStream stream;
//                        try {
//                            File imageFile = new File(cachePath, "image.jpg");
//                            stream = new FileOutputStream(imageFile);
//                            resource.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                            stream.flush();
//                            stream.close();
//
//                            // Share the locally saved image with WhatsApp
//                            shareImageWithWhatsApp(imageFile, message);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onLoadCleared(Drawable placeholder) {
//                        // Handle case when the resource is cleared
//                    }
//                });
//    }
//
//    private void shareImageWithWhatsApp(File imageFile, String message) {
//        Uri imageUri = FileProvider.getUriForFile(
//                getActivity(),
//                BuildConfig.APPLICATION_ID + ".provider",
//                imageFile
//        );
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/jpeg");
//        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        intent.putExtra(Intent.EXTRA_TEXT, message);
//
//// Use createChooser to show a list of sharing apps
//        Intent chooserIntent = Intent.createChooser(intent, "Share using");
//        startActivity(chooserIntent);
//
//
//    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();

        // Start the FirstPage activity and clear the task stack
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");

        // Logout button
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform logout action here (e.g., clear session, go to login page)
                // Redirect to the login page
                hasLoggedIn = false;
                SharedPreferences settings = getActivity().getSharedPreferences(LoginMain.PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasLoggedIn", false);
                editor.apply();
                Intent intent = new Intent(getContext(), LoginMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // Cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog (do nothing)
                dialog.dismiss();
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void contactNumberPresent(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.referral_alertdialog, null);
        builder.setView(dialogView);

        // Buttons in the dialog
        Button saveButton = dialogView.findViewById(R.id.sharebtn);

        EditText referralcode = dialogView.findViewById(R.id.referralcodetext);
        ImageView submitcodetextview = dialogView.findViewById(R.id.allreferralsImg);
        submitcodelayout = dialogView.findViewById(R.id.submitcodelayout);
        sharecodelayout = dialogView.findViewById(R.id.sharecodelayout);

        Button submitbutton = dialogView.findViewById(R.id.submitbtn);
        EditText referralcodeedittext = dialogView.findViewById(R.id.referralcodeedittext);
        TextView errortext = dialogView.findViewById(R.id.errortext);
        ImageView errorimage = dialogView.findViewById(R.id.errorimage);
        submitcodelayout = dialogView.findViewById(R.id.submitcodelayout);

        // Get the first 4 letters of the name
        String namePart = name.substring(0, Math.min(name.length(), 4));
        String numberPart = shopcontactNumber.substring(Math.max(shopcontactNumber.length() - 4, 0));
        String referralCode = (namePart + numberPart).toUpperCase();
        referralcode.setText(referralCode);
        referralcode.setAllCaps(true); // Capitalize the text
        referralcode.setTypeface(null, Typeface.BOLD); // Make the text bold
        DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Shop")
                .child(shopcontactNumber).child("referralcode");
        referralRef.setValue(referralCode);
//        copyreferralcode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Get the referral code text from the TextView
//                String referralCode = referralcode.getText().toString();
//
//                // Get the ClipboardManager
//                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//
//                // Create a ClipData object to store the text you want to copy
//                ClipData clip = ClipData.newPlainText("Referral Code", referralCode);
//
//                // Set the data to clipboard
//                clipboard.setPrimaryClip(clip);
//
//                // Notify the user that the text has been copied (you can use a Toast for this)
//                Toast.makeText(getContext(), "Referral Code copied to clipboard", Toast.LENGTH_SHORT).show();
//            }
//        });

        DatabaseReference logoRef = FirebaseDatabase.getInstance().getReference("ads");

        logoRef.child("logo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    // Retrieve the URL for the current index
                    String currentImageUrl = snapshot.child("logoimage").getValue(String.class);
                    System.out.println("fgfdvg " + currentImageUrl);

                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Obtain the referral code
                            String referralCode = referralcode.getText().toString();

                            String message = "Referral Code: " + referralCode + "\nApp URL: https://play.google.com/store/apps/details?id=com.spark.swarajyabiz&hl=en-IN";
                            // Get the logo as a drawable (replace with your actual logo image)
                            @SuppressLint("UseCompatLoadingForDrawables")
                            Drawable logoDrawable = getResources().getDrawable(R.drawable.newlogo);
                            System.out.println("dfbfb " + logoDrawable.toString());

                         //   downloadImageAndShare(currentImageUrl, message);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });


        submitcodetextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharecodelayout.setVisibility(View.GONE);
                submitcodelayout.setVisibility(View.VISIBLE);

            }
        });

        DatabaseReference refernumberRef = FirebaseDatabase.getInstance().getReference().child("Shop");
        refernumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                boolean found = false;
                if (snapshot.exists()) {
                    for (DataSnapshot referralSnapshot : snapshot.getChildren()) {
                        String keys = referralSnapshot.getKey();
                        String referralcode = referralSnapshot.child("referralcode").getValue(String.class);
                        System.out.println("keysf " + keys);

                        DatabaseReference keysRef = refernumberRef.child(keys);
                        keysRef.child("referral").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        // Retrieve the values of each child
                                        String childValue = childSnapshot.getValue(String.class);
                                        System.out.println("Child Value: " + childValue);

                                        if (shopcontactNumber.equals(childValue)) {
                                            referralcodeedittext.setText(referralcode);
                                            // Disable the referralcodeedittext to make it permanent
                                            referralcodeedittext.setEnabled(false);
                                            submitbutton.setEnabled(false);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String referralcodetext = referralcodeedittext.getText().toString();
                if (referralcodetext.matches("^[A-Z]{4}[0-9]{4}$")) {
                    DatabaseReference referralcodeRef = FirebaseDatabase.getInstance().getReference().child("Shop");
                    referralcodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            boolean found = false;
                            if (snapshot.exists()) {
                                for (DataSnapshot referralSnapshot : snapshot.getChildren()) {
                                    String referralcode = referralSnapshot.child("referralcode").getValue(String.class);
                                    System.out.println("referral " + referralcode);

                                    if (referralcodetext.equals(referralcode)) {
                                        found = true;

                                        shopcontactnumber = referralSnapshot.child("contactNumber").getValue(String.class);
                                        System.out.println("sdfvfc " + shopcontactnumber);
                                        DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Shop")
                                                .child(shopcontactnumber).child("referral");
                                        DatabaseReference referralCountRef = FirebaseDatabase.getInstance().getReference().child("Shop")
                                                .child(shopcontactnumber).child("count").child("referralcount");
                                        System.out.println("referralcount " + referralCountRef);

                                        // Determine the next available key for the user contact number
                                        referralRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                                                long nextKey = dataSnapshot.getChildrenCount();
                                                DatabaseReference userRef = referralRef.child(String.valueOf(nextKey));
                                                userRef.setValue(shopcontactNumber); // Replace 'usercontactnumber' with the actual user contact number

                                                // Update the referral count
                                                referralCountRef.setValue(nextKey + 1);

                                                shopdialog.dismiss();


                                            }

                                            @Override
                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                                // Handle onCancelled
                                            }
                                        });
                                    }
                                }
                            }
                            if (!found) {
                                // Show an error message for incorrect referral code
                                errorimage.setVisibility(View.VISIBLE);
                                errortext.setVisibility(View.VISIBLE);
                                errortext.setText("Invalid Referral Code");
                                //  Toast.makeText(getContext(), "Invalid Referral Code", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });
                } else {
                    errorimage.setVisibility(View.VISIBLE);
                    errortext.setVisibility(View.VISIBLE);
                    errortext.setText("Referral Code should be 4 letters and 4 numbers");
                    //Toast.makeText(getContext(), "Referral Code should be 4 letters and 4 numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Show the dialog only if the fragment is still attached
        if (isAdded()) {
            // Show the dialog
            shopdialog = builder.create();
            shopdialog.show();
            shopdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }

    private void contactNumberNotPresent(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.referral_alertdialog, null);
        builder.setView(dialogView);

        // Buttons in the dialog
        Button submitbutton = dialogView.findViewById(R.id.submitbtn);
        EditText referralcodeedittext = dialogView.findViewById(R.id.referralcodeedittext);
        TextView errortext = dialogView.findViewById(R.id.errortext);
        ImageView errorimage = dialogView.findViewById(R.id.errorimage);
        submitcodelayout = dialogView.findViewById(R.id.submitcodelayout);
        sharecodelayout = dialogView.findViewById(R.id.sharecodelayout);
        sharecodelayout.setVisibility(View.GONE);
        submitcodelayout.setVisibility(View.VISIBLE);
        String referralcodetext = referralcodeedittext.getText().toString();

        DatabaseReference refernumberRef = FirebaseDatabase.getInstance().getReference().child("Shop");
        refernumberRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                boolean found = false;
                if (snapshot.exists()) {
                    for (DataSnapshot referralSnapshot : snapshot.getChildren()) {
                        String keys = referralSnapshot.getKey();
                        String referralcode = referralSnapshot.child("referralcode").getValue(String.class);
                        System.out.println("keysf " + keys);

                        DatabaseReference keysRef = refernumberRef.child(keys);
                        keysRef.child("referral").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                                        // Retrieve the values of each child
                                        String childValue = childSnapshot.getValue(String.class);
                                        System.out.println("Child Value: " + childValue);

                                        if (shopcontactNumber.equals(childValue)) {
                                            referralcodeedittext.setText(referralcode);
                                            // Disable the referralcodeedittext to make it permanent
                                            referralcodeedittext.setEnabled(false);
                                            submitbutton.setEnabled(false);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });


        submitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String referralcodetext = referralcodeedittext.getText().toString();
                if (referralcodetext.matches("^[A-Z]{4}[0-9]{4}$")) {
                    DatabaseReference referralcodeRef = FirebaseDatabase.getInstance().getReference().child("Shop");
                    referralcodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                            boolean found = false;
                            if (snapshot.exists()) {
                                for (DataSnapshot referralSnapshot : snapshot.getChildren()) {
                                    String referralcode = referralSnapshot.child("referralcode").getValue(String.class);
                                    System.out.println("referral " + referralcode);

                                    if (referralcodetext.equals(referralcode)) {
                                        found = true;

                                        shopcontactnumber = referralSnapshot.child("contactNumber").getValue(String.class);
                                        System.out.println("sdfvfc " + shopcontactnumber);
                                        DatabaseReference referralRef = FirebaseDatabase.getInstance().getReference().child("Shop")
                                                .child(shopcontactnumber).child("referral");
                                        DatabaseReference referralCountRef = FirebaseDatabase.getInstance().getReference().child("Shop")
                                                .child(shopcontactnumber).child("count").child("referralcount");
                                        System.out.println("referralcount " + referralCountRef);

                                        // Determine the next available key for the user contact number
                                        referralRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                                                long nextKey = dataSnapshot.getChildrenCount();
                                                DatabaseReference userRef = referralRef.child(String.valueOf(nextKey));
                                                userRef.setValue(shopcontactNumber); // Replace 'usercontactnumber' with the actual user contact number

                                                // Update the referral count
                                                referralCountRef.setValue(nextKey + 1);

                                                shopdialog.dismiss();


                                            }

                                            @Override
                                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                                                // Handle onCancelled
                                            }
                                        });
                                    }
                                }
                            }
                            if (!found) {
                                // Show an error message for incorrect referral code
                                errorimage.setVisibility(View.VISIBLE);
                                errortext.setVisibility(View.VISIBLE);
                                errortext.setText("Invalid Referral Code");
                                //  Toast.makeText(getContext(), "Invalid Referral Code", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });
                } else {
                    errorimage.setVisibility(View.VISIBLE);
                    errortext.setVisibility(View.VISIBLE);
                    errortext.setText("Referral Code should be 4 letters and 4 numbers");
                    //Toast.makeText(getContext(), "Referral Code should be 4 letters and 4 numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Show the dialog only if the fragment is still attached
        if (isAdded()) {
            // Show the dialog
            shopdialog = builder.create();
            shopdialog.show();
            shopdialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
    }


    private void retrievePostDetails() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        System.out.println("efbf " +databaseReference);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String Name = dataSnapshot.child("name").getValue(String.class);
                    String profileImage = dataSnapshot.child("profileimage").getValue(String.class);
                    shopcontactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    System.out.println("dfjgbv " +shopcontactNumber);

                    if (Name != null && Name.contains(" ")) {
                        String firstName = Name.substring(0, Name.indexOf(" "));
                        usernametext.setText(firstName);
                    }
                    DataSnapshot transSnapshot = dataSnapshot.child("Trans");

                    if (transSnapshot.exists()) {
                        for (DataSnapshot transIDSnapshot : transSnapshot.getChildren()) {
                            String transkey = transIDSnapshot.getKey();

                            // If "Trans" has a child node (e.g., "TransactionId"), navigate to it
                            DataSnapshot transactionNode = transSnapshot.child(transkey);

                            if (transactionNode.exists()) {
                                // Retrieve the "trdate" and "Description" from the nested child node
                                String premiumdateandtime = transactionNode.child("TrDate").getValue(String.class);
                                String planDescription = transactionNode.child("Description").getValue(String.class);
                                String subString = premiumdateandtime.substring(0, 10);

                                // Parse the premium date
                                Date premiumDate = parseDate(subString);
                                if (premiumDate != null) {
                                    Date currentDate = new Date();

                                    // Calculate the remaining days dynamically based on plan validity
                                    long remainingDays = calculateRemainingDays(planDescription, premiumDate, currentDate);

                                    // Display premium date substring and remaining days
                                    System.out.println("Premium Date : " + subString);
                                    System.out.println("Remaining Days: " + remainingDays);

                                    if (subString  != null){
                                        plantextview.setVisibility(View.VISIBLE);
                                        plantextview.setText("Premium Date : " + subString);
                                    }

                                    plandesc.setText("Remaining Days: " + remainingDays);
                                } else {
                                    // Handle parsing error
                                    System.out.println("Error parsing premium date: " + subString);
                                }
                            }
                        }
                    }


                    username.setText(Name);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

        DatabaseReference shopRef = FirebaseDatabase.getInstance().getReference("Shop").child(userId);

        shopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  postList.clear(); // Clear the existing list before adding new data
                if (dataSnapshot.exists()) {
                     shopName = dataSnapshot.child("shopName").getValue(String.class);
                    shopimage = dataSnapshot.child("url").getValue(String.class);
                    System.out.println("dgbt " + shopimage);
                    shopcontactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                     name = dataSnapshot.child("name").getValue(String.class);
                     shopaddress = dataSnapshot.child("address").getValue(String.class);

                    Boolean verify = dataSnapshot.child("profileverified").getValue(Boolean.class);
                    System.out.println("ruggj " + shopaddress);
                    Integer noticationcount = dataSnapshot.child("count").child("notificationcount").getValue(Integer.class);
                    Integer noticationCount = dataSnapshot.child("notificationcount").getValue(Integer.class);
                    int ordercount = dataSnapshot.child("ordercount").getValue(Integer.class);

//                    int orderCount = dataSnapshot.child("count").child("ordercount").getValue(Integer.class);

                    Integer referralcount = dataSnapshot.child("count").child("referralcount").getValue(Integer.class);
                    System.out.println("edgfvd " +referralcount);

                    updateBadgeAndUI(ordercount, referralcount);

                    if (verify == true) {
                        verifytext.setVisibility(View.GONE);
                    } else {
                        verifytext.setVisibility(View.VISIBLE);

                    }

                    if (noticationcount != null) {
                        if (noticationcount > 0) {
                            // Show the badge count and update it with the request count
                            notification.setVisibility(View.VISIBLE);
                            notificationcount.setVisibility(View.VISIBLE);
                            notificationcount.setText(noticationcount.toString());
//                            notificationcount.setTextColor(Color.BLACK);
//                            GradientDrawable drawable = new GradientDrawable();
//                            drawable.setShape(GradientDrawable.OVAL);
//                            if (isAdded() && getContext() != null) {
//                                // Fragment is attached to an activity, and context is not null
//                                int color = ContextCompat.getColor(requireContext(), R.color.white);
//                                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//                            }
//
//                            //     drawable.setColorFilter(colors, PorterDuff.Mode.SRC_ATOP);
//
//                            drawable.setCornerRadius(10);
//
//                            // Set the background color of the badge
//                            notificationcount.setBadgeBackgroundDrawable(drawable);

                        } else {
                            // Hide the badge count when the request count is zero
                            notification.setVisibility(View.VISIBLE);
                            notificationcount.setVisibility(View.GONE);
                        }
                    }


                    imagelayout.setVisibility(View.VISIBLE);
                    image = dataSnapshot.child("url").getValue(String.class);
                    Log.d("TAG", "onDataChange: " + image);


                    if (isAdded() && getContext() != null) {
                        // Fragment is attached to an activity, and context is not null
                        Glide.with(requireContext()).load(image).into(profileimage);
                    } else {
                        // Handle the case where the fragment is not properly attached
                    }



                    profileimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           showFullImage(v,image);
                        }
                    });

//                    editcard.setVisibility(View.VISIBLE);
//                    catlogcard.setVisibility(View.VISIBLE);
//                    promotedcard.setVisibility(View.VISIBLE);
//                    orderscard.setVisibility(View.VISIBLE);
//                    myorderscrd.setVisibility(View.VISIBLE);
//                    createprofilecard.setVisibility(View.GONE);
//                    postjobcard.setVisibility(View.VISIBLE);
//                    notificationcard.setVisibility(View.VISIBLE);
//                    businesscardpost.setVisibility(View.VISIBLE);
//                    businesscard.setVisibility(View.VISIBLE);

                } else {

//                    editcard.setVisibility(View.GONE);
//                    catlogcard.setVisibility(View.GONE);
//                    promotedcard.setVisibility(View.GONE);
//                    orderscard.setVisibility(View.GONE);
//                    postjobcard.setVisibility(View.GONE);
//                    notificationcard.setVisibility(View.GONE);
//                    businesscardpost.setVisibility(View.GONE);
//                    businesscard.setVisibility(View.VISIBLE);
//                    myorderscrd.setVisibility(View.VISIBLE);
//                    createprofilecard.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });
    }

    private Date parseDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private long calculateRemainingDays(String planDescription, Date premiumDate, Date currentDate) {
        // Convert Date objects to LocalDate
        LocalDate premiumLocalDate = premiumDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentLocalDate = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Calculate the period between premium date and current date
        Period period = Period.between(premiumLocalDate, currentLocalDate);

        // Log start and end dates for debugging
        System.out.println("Start Date: " + premiumLocalDate);
        System.out.println("End Date: " + currentLocalDate);

        // Log plan description for debugging
        System.out.println("Plan Description: " + planDescription);

        // Calculate remaining days based on the plan description
        long remainingDays = 0;

        if ("1 month".equalsIgnoreCase(planDescription)) {
            remainingDays = period.getDays(); // Days within the current month
        } else if ("6 months".equalsIgnoreCase(planDescription)) {
            remainingDays = period.getDays(); // Days within the current month
            for (int i = 0; i < 5; i++) {
                currentLocalDate = currentLocalDate.minusMonths(1);
                remainingDays += YearMonth.from(currentLocalDate).lengthOfMonth();
            }
        } else if ("1 year".equalsIgnoreCase(planDescription)) {
            remainingDays = period.getDays(); // Days within the current month
            for (int i = 0; i < 11; i++) {
                currentLocalDate = currentLocalDate.minusMonths(1);
                remainingDays += YearMonth.from(currentLocalDate).lengthOfMonth();
            }
        }

        return remainingDays;
    }

    private void updateBadgeAndUI(int ordercount, Integer referralcount) {
        // Update your UI elements based on the new ordercount value
        if (ordercount > 0) {
            // Display the badge count for promoted shops
            notificationBadge.setVisibility(View.VISIBLE);
            notificationBadge.setText(String.valueOf(ordercount));

            // Create a custom Drawable with a solid background color
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            int colorResource = R.color.colorAccent;
//            int colors = getResources().getColor(R.color.colorAccent);

            if (isAdded() && getContext() != null) {
                // Fragment is attached to an activity, and context is not null
                int color = ContextCompat.getColor(requireContext(), R.color.TextColor);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }

       //     drawable.setColorFilter(colors, PorterDuff.Mode.SRC_ATOP);

            drawable.setCornerRadius(10);

            // Set the background color of the badge
            notificationBadge.setBadgeBackgroundDrawable(drawable);

            // Set the text color to white
            notificationBadge.setTextColor(Color.WHITE);
        } else {
            // Hide the badge if the count is zero or negative
            notificationBadge.setVisibility(View.GONE);
        }

        if (referralcount != null && referralcount > 0) {
            // Display the badge count for promoted shops
            System.out.println("fvbv v" +referralcount);
            referralCount.setVisibility(View.VISIBLE);
            referralCount.setText(String.valueOf(referralcount));
//            userreferralCount.setVisibility(View.VISIBLE);
//            userreferralCount.setText(String.valueOf(referralcount));
            // Create a custom Drawable with a solid background color
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL);
            int colorResource = R.color.colorAccent;
//            int colors = getResources().getColor(R.color.colorAccent);

            if (isAdded() && getContext() != null) {
                // Fragment is attached to an activity, and context is not null
                int color = ContextCompat.getColor(requireContext(), R.color.TextColor);
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }

            //     drawable.setColorFilter(colors, PorterDuff.Mode.SRC_ATOP);

            drawable.setCornerRadius(10);

            // Set the background color of the badge
            referralCount.setBadgeBackgroundDrawable(drawable);
            referralCount.setTextColor(Color.WHITE);
        } else {
            // Hide the badge if the count is zero or negative
            referralCount.setVisibility(View.GONE);
        }
    }

    private void retrievecurrentuserItemDetails() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Shop").child(userId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postList.clear(); // Clear the existing list before adding new data
                if (dataSnapshot.exists()){

                    String shopName = dataSnapshot.child("shopName").getValue(String.class);
                    String shopimage = dataSnapshot.child("url").getValue(String.class);
                    shopcontactNumber = dataSnapshot.child("contactNumber").getValue(String.class);
                    String destrict = dataSnapshot.child("district").getValue(String.class);
                    String taluka = dataSnapshot.child("taluka").getValue(String.class);
                    String address = dataSnapshot.child("address").getValue(String.class);
                    notificationCount = dataSnapshot.child("notificationcount").getValue(Integer.class);
                    System.out.println("rgdfg "+notificationCount);
                    System.out.println("rgdfg "+shopName);
                    // postAdapter.setShopName(shopName);

                    DataSnapshot itemsSnapshot = dataSnapshot.child("items");
                    for (DataSnapshot itemSnapshot : itemsSnapshot.getChildren()) {
                        String itemkey = itemSnapshot.getKey();

                        String itemName = itemSnapshot.child("itemname").getValue(String.class);
                        String price = itemSnapshot.child("price").getValue(String.class);
                        String sellprice = itemSnapshot.child("sell").getValue(String.class);
                        String offer = itemSnapshot.child("offer").getValue(String.class);
                        String description = itemSnapshot.child("description").getValue(String.class);
                        String firstimage = itemSnapshot.child("firstImageUrl").getValue(String.class);
                        String wholesale = itemSnapshot.child("wholesale").getValue(String.class);
                        String minqty = itemSnapshot.child("minquantity").getValue(String.class);
                        String servingArea = itemSnapshot.child("servingArea").getValue(String.class);
                        String status = itemSnapshot.child("status").getValue(String.class);
                        String itemCate = itemsSnapshot.child("itemCate").getValue(String.class);

                        System.out.println("jfhv " +firstimage);

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

                        ItemList item = new ItemList(shopName,shopimage,shopcontactNumber, itemName,
                                price, sellprice, description, firstimage, itemkey, imageUrls, destrict,taluka,address, offer, wholesale,
                                minqty, servingArea, status, itemCate);
                        itemList.add(item);
                    }

                }
                Collections.shuffle(itemList);
                // Notify the adapter that the data has changed
//                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
            }
        });

    }

    private void showFullImage(View v, String imageUrl){
        // Inflate the popup layout
        if (imageUrl != null && !imageUrl.isEmpty()) {
            View popupView = LayoutInflater.from(getContext()).inflate(R.layout.activity_full_screen, null);

            // Find the ImageView in the popup layout
            ImageView imageView = popupView.findViewById(R.id.popup_image_view);
            ImageView cancelimageview = popupView.findViewById(R.id.close_image_view);

            // Load the image into the ImageView using Glide
            Glide.with(getContext())
                    .load(image)
                    .into(imageView);

            // Calculate the width and height of the popup window
            int width = LinearLayout.LayoutParams.MATCH_PARENT;
            int height = LinearLayout.LayoutParams.MATCH_PARENT;

            // Create the PopupWindow and set its properties
            PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);

            // Show the popup window at the center of the anchor view
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);


            cancelimageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Close the popup window when cancelimageview is clicked
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
            });
        }else {
            // Show a toast message if imageUrl is empty or null
            Toast.makeText(getContext(), "Image not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void startImageCropper(Uri sourceUri) {
        File cacheDir = requireActivity().getCacheDir();
        Uri destinationUri = Uri.fromFile(new File(cacheDir, "cropped_image"));

        UCrop.of(sourceUri, destinationUri)
                .withAspectRatio(1, 1) // Set the aspect ratio (square in this case)
                .withMaxResultSize(300, 300) // Set the max result size
               .start(requireContext(),FragmentProfile.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_SINGLE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                croppedImageUri = imageUri;
                startImageCropper(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            croppedImageUri = UCrop.getOutput(data);

            if (croppedImageUri != null) {
                // Display the cropped image
                profileimage.setImageURI(croppedImageUri);

                // Upload the cropped image to Firebase Storage and associate it with the user's contactNumber
                uploadCroppedImageToFirebase(croppedImageUri, shopcontactNumber);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            // Handle any errors that occurred during cropping
            Throwable error = UCrop.getError(data);
        }
    }

    private void uploadCroppedImageToFirebase(Uri croppedImageUri, String userContactNumber) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference profileImageRef = storageReference.child("profile_images/" + userContactNumber + ".jpg");

        profileImageRef.putFile(croppedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image uploaded successfully
                    // You can get the download URL and store it in the user's data in the Firestore database or Realtime Database
                    profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();
                        DatabaseReference imageRef = databaseReference.child("Users").child(shopcontactNumber).child("profileimage");
                        imageRef.setValue(downloadUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle any errors during image upload
                });
    }

    @Override
    public void onClickClick(int position) {

    }

    @Override
    public void onContactClick(int position) {

    }

    @Override
    public void onorderClick(int position) {

    }

    @Override
    public void oncallClick(int position) {

    }

    @Override
    public void onseeallClick(int position) {

    }
}
