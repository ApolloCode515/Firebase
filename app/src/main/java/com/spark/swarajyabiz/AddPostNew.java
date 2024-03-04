package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.oginotihiro.cropview.Crop;
import com.spark.swarajyabiz.Adapters.CategoryAdapter;
import com.spark.swarajyabiz.Adapters.CommAdapter;
import com.spark.swarajyabiz.Adapters.CommClickAdapter;
import com.spark.swarajyabiz.Adapters.SubCateAdapter;
import com.spark.swarajyabiz.ModelClasses.CategoryModel;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.ModelClasses.SubCategoryModel;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;
import com.spark.swarajyabiz.ui.FragmentHome;
import com.spark.swarajyabiz.ui.FragmentShop;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPostNew extends AppCompatActivity implements PostBannerAdapter.onClickImageListener {
    DatabaseReference usersRef, shopRef, newpostRef, postsRef;
    StorageReference storageRef;
    String userId,postType;
    CardView tempCard,mediaCard,postCard, bannerCard;
    EditText postDesc,postKeys, postCaption,writecationedittext, itemServeArea;
    ImageView back, postImg,removeimg, busipostimg,addServeAreaImageView, img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12;
    int count =1;
    TextView txt1, txt2, txt3, txt4, txt5, txt6, errortext;
    ImagePicker imagePicker;

    private static final int PROFILE_IMAGE_REQ_CODE = 101;
    private static final int GALLERY_IMAGE_REQ_CODE = 102;
    private static final int CAMERA_IMAGE_REQ_CODE = 103;
    Uri filePath=null;
    FrameLayout imgFrame;
    String pid, imageUrl, checkstring="Global", servedAreasFirebaseFormat, selectedCategory, selectedSubcategory, bannerimage, AdBal;
    GridLayout gridLayout;
    LinearLayout medialayout, locallayout, imglayout;
    RelativeLayout templateLayout, imagelayout;
    AlertDialog dialog;
    RadioGroup radioGroup;
    RadioButton rdglobal, rdlocal;
    private List<String> servedAreasList = new ArrayList<>();
    Spinner spinner, subspinner;
    CommClickAdapter commAdapter;
    GlobalComClickAdapter globalComClickAdapter;
    ArrayList<CommModel> commModels;
    ArrayList<CommModel> globalcommModels;
    ArrayList<String> selectedCommIds = new ArrayList<>();
    ArrayList<String> selectedGlobalCommIds = new ArrayList<>();
    ChipGroup servedAreasLayout;
    double newTotalAmount;
    boolean isGeneralCardSelected=false, isRecyclerViewItemSelected=false,isRecyclerViewGlobalSelected=false, isBanenrImage=false;
    private PostBannerAdapter postBannerAdapter;
    private ArrayList<PostBannerClass> bannerList = new ArrayList<>();
    BottomSheetDialog bottomSheetDialog;

    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_new);

        progressDialog = new ProgressDialog(this);


        tempCard=findViewById(R.id.tempBtn);
        mediaCard=findViewById(R.id.mediaBtn);
        postCard=findViewById(R.id.postBtn);
        bannerCard = findViewById(R.id.BannerBtn);
        postDesc=findViewById(R.id.postDescr);
        postImg=findViewById(R.id.postImgId);
        removeimg=findViewById(R.id.removImg);
        imgFrame=findViewById(R.id.imgFrame);
        postKeys=findViewById(R.id.bizkeyword);
        back = findViewById(R.id.back);
        imagelayout = findViewById(R.id.busiimagelayout);
        writecationedittext = findViewById(R.id.caption);
        spinner = findViewById(R.id.postctyspinner);
        subspinner = findViewById(R.id.postsubctyspinner);
        errortext = findViewById(R.id.errortext);
        //postKeys=findViewById(R.id.bizkeyword);

        templateLayout = findViewById(R.id.colorPostBackgroundlayout);
        medialayout = findViewById(R.id.medialayout);
        gridLayout = findViewById(R.id.txtgridLayout);
        busipostimg = findViewById(R.id.busipostimg);
        locallayout = findViewById(R.id.locallayout);
        radioGroup = findViewById(R.id.rdgrpx);
        addServeAreaImageView = findViewById(R.id.addServeAreaImageView);
        itemServeArea = findViewById(R.id.itemserveArea);
        imglayout=findViewById(R.id.layout1);
        servedAreasLayout = findViewById(R.id.servedAreasLayout);

        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);
        img7 = findViewById(R.id.img7);
        img8 = findViewById(R.id.img8);
        img9 = findViewById(R.id.img9);
        img10 = findViewById(R.id.img10);
        img11 = findViewById(R.id.img11);
        img12 = findViewById(R.id.img12);

        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        txt5 = findViewById(R.id.txt5);
        txt6 = findViewById(R.id.txt6);
        pid="1";

        // Set click listeners for each CardView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
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


        String imageUri = getIntent().getStringExtra("imageUri");
        System.out.println("aedsx " +imageUri);
        if (imageUri != null) {
            filePath = Uri.parse(imageUri);
            Glide.with(this)
                    .load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(postImg);
            imgFrame.setVisibility(View.VISIBLE);
            tempCard.setVisibility(View.GONE);
            mediaCard.setVisibility(View.GONE);
            bannerCard.setVisibility(View.GONE);
        }

        retrievePostCategory();

        setTextColor(R.color.white);
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.white);
            }
        });

        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.black);
            }
        });

        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.close_red);
            }
        });

        txt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.yellow);
            }
        });

        txt5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.blue);
            }
        });

        txt6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTextColor(R.color.pink);
            }
        });


        setGradientImage(R.drawable.gradient01);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient01);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient02);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient18);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient05);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage( R.drawable.gradient06);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient07);
            }
        });
        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient11);
            }
        });
        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient12);
            }
        });
        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient13);
            }
        });
        img10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient16);
            }
        });
        img11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient17);
            }
        });

        img12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setGradientImage(R.drawable.gradient19);
            }
        });

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        shopRef = FirebaseDatabase.getInstance().getReference("Shop");
        storageRef = FirebaseStorage.getInstance().getReference();
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }

        mediaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pid="1";
                showFileChooser();
                templateLayout.setVisibility(View.GONE);
                postDesc.setVisibility(View.VISIBLE);
            }
        });

        tempCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pid="2";
                templateLayout.setVisibility(View.VISIBLE);
                imgFrame.setVisibility(View.GONE);
                postDesc.setVisibility(View.GONE);
            }
        });

        removeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bannerimage!=null){
                    isBanenrImage=false;
                    bannerimage=null;
                    System.out.println("wgvrwgc " +isBanenrImage);
                }
                filePath=null;
                postImg.setVisibility(View.GONE);
                removeimg.setVisibility(View.GONE);
            }
        });


        postCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategory();
            }
        });

       bannerCard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showBannerImg();
           }
       });


        writecationedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not used in this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Not used in this example
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Adjust text size based on the length of the text
                adjustTextSize(editable.length());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


//        addServeAreaImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String serveAreaText = itemServeArea.getText().toString().trim();
//
//                if (!serveAreaText.isEmpty() && count<=5) {
//                    addServeArea(serveAreaText);
//                    itemServeArea.setText(""); // Clear the EditText
//                    updateServedAreasUI();
//                    count++;
//                }else {
//                    itemServeArea.setError("You have added the maximum number of locations");
//                }
//            }
//        });

//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
//                if (null != rb) {
//                    // checkedId is the RadioButton selected
//                    switch (i) {
//                        case R.id.rdglobal:
//                            // Do Something
//                            checkstring = "Global";
//                           // global = "Global";
//                            locallayout.setVisibility(View.GONE);
//                            break;
//
//                        case R.id.rdlocal:
//                            // Do Something
//                            checkstring = "Local";
//                            locallayout.setVisibility(View.VISIBLE);
//                            break;
//
//                    }
//                }
//            }
//        });

    }

    public void showCategory(){
        // Inflate the layout for the BottomSheetDialog
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.community_bottom_sheet, null);

        // Customize the BottomSheetDialog as needed
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        checkstring = "Global";
        // Disable scrolling for the BottomSheetDialog
//        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
//        behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        // Handle views inside the BottomSheetDialog
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView communityRecyclerview = bottomSheetView.findViewById(R.id.communityview);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView GlobalcommunityRecyclerview = bottomSheetView.findViewById(R.id.GlobalCommunityview);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        CardView generalcard = bottomSheetView.findViewById(R.id.generalcard);
        CardView postcard = bottomSheetView.findViewById(R.id.postBtn);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        CheckBox checkBox = bottomSheetView.findViewById(R.id.checkBox);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView text = bottomSheetView.findViewById(R.id.text);
        TextView text1 = bottomSheetView.findViewById(R.id.text1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        LinearLayout lay1 = bottomSheetView.findViewById(R.id.lay2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView rupeesx = bottomSheetView.findViewById(R.id.rupeesx);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView chrgrupee = bottomSheetView.findViewById(R.id.chrgrupeesx);
        text1.setVisibility(View.GONE);
        lay1.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView back = bottomSheetView.findViewById(R.id.back);
        RadioGroup radioGroup = bottomSheetView.findViewById(R.id.rdgrpx);
        LinearLayout locallayout = bottomSheetView.findViewById(R.id.locallayout);
        ImageView addServeAreaImageView = bottomSheetView.findViewById(R.id.addServeAreaImageView);
        EditText itemServeArea = bottomSheetView.findViewById(R.id.itemserveArea);
        ChipGroup servedAreasLayout = bottomSheetView.findViewById(R.id.servedAreasLayout);;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Categories");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String wallbal = snapshot.child("AdBalance").getValue(String.class);
                    rupeesx.setText(wallbal);
                }else {
                    rupeesx.setText("0.0");
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the CheckBox state when the CardView is clicked
               // checkBox.setChecked(!checkBox.isChecked());

                if (!selectedCommIds.contains("prathamesh")) {
                    // If 'general' is not present, clear the list and add 'general'
                    selectedCommIds.add("prathamesh");
                } else {
                    selectedCommIds.remove("prathamesh");
                }

                if (!selectedGlobalCommIds.contains("prathamesh")) {
                    // If 'general' is not present, clear the list and add 'general'
                    selectedGlobalCommIds.add("prathamesh");
                } else {
                    selectedGlobalCommIds.remove("prathamesh");
                }

                System.out.println("Selected Community Ids: " + selectedCommIds);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = (RadioButton) radioGroup.findViewById(i);
                if (null != rb) {
                    // checkedId is the RadioButton selected
                    switch (i) {
                        case R.id.rdglobal:
                            // Do Something
                            checkstring = "Global";
                            // global = "Global";
                            locallayout.setVisibility(View.GONE);
                            servedAreasLayout.setVisibility(View.GONE);
                            break;

                        case R.id.rdlocal:
                            // Do Something
                            checkstring = "Local";
                            locallayout.setVisibility(View.VISIBLE);
                            servedAreasLayout.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        });


        addServeAreaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder servedAreasString = new StringBuilder();
                String serveAreaText = itemServeArea.getText().toString().trim();
                if (!serveAreaText.isEmpty() && count<=5) {
                    if (itemServeArea.getText().toString().trim().isEmpty()) {
                        Toast.makeText(AddPostNew.this, "Please enter the keyword!", Toast.LENGTH_LONG).show();
                    } else {
                        String keyword = itemServeArea.getText().toString().trim();

                        // Clear the existing servedAreasList
                        servedAreasList.clear();

                        for (int i = 0; i < servedAreasLayout.getChildCount(); i++) {
                            Chip chip = (Chip) servedAreasLayout.getChildAt(i);
                            String data = chip.getText().toString().trim();
                            servedAreasList.add(data);
                        }

                        // Check if the keyword already exists in the list
                        if (servedAreasList.contains(keyword)) {
                            Toast.makeText(AddPostNew.this, "Subject Already Exist", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                LayoutInflater inflater = LayoutInflater.from(AddPostNew.this);
                                // Create a Chip from Layout.
                                Chip newChip = (Chip) inflater.inflate(R.layout.layout_chip_entry, servedAreasLayout, false);
                                newChip.setText(keyword);

                                servedAreasLayout.addView(newChip);

                                newChip.setOnCloseIconClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ChipGroup parent = (ChipGroup) v.getParent();
                                        parent.removeView(v);
                                    }
                                });

                                itemServeArea.setText("");
                                servedAreasFirebaseFormat = "";
                                for (int i = 0; i < servedAreasLayout.getChildCount(); i++) {
                                    Chip chip = (Chip) servedAreasLayout.getChildAt(i);
                                    String chipText = chip.getText().toString().trim();

                                    if (!servedAreasFirebaseFormat.isEmpty()) {
                                        servedAreasFirebaseFormat += "&&"; // Add "&&" only if the string is not empty
                                    }

                                    servedAreasFirebaseFormat += chipText;
                                    Log.d("sdfvdsv", servedAreasFirebaseFormat);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(AddPostNew.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    count++;
                }else {
                    itemServeArea.setError("You have added the maximum number of locations");
                }
            }
        });



        DatabaseReference communityRef = FirebaseDatabase.getInstance().getReference("Community");

        usersRef.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean premium= snapshot.child("premium").getValue(Boolean.class);
                String wallbal = snapshot.child("AdBalance").getValue(String.class);
                if (premium){
                    communityRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            globalcommModels = new ArrayList<>();

                            for (DataSnapshot communitySnapshot : snapshot.getChildren()) {
                                // Assuming the data structure under Communities is as follows
                                String commAdmin = communitySnapshot.child("commAdmin").getValue(String.class);
                                int comCnt= (int) communitySnapshot.child("commMembers").getChildrenCount();
                                String status = communitySnapshot.child("monit").getValue(String.class);
                                System.out.println("eafd "+ commAdmin);

                                if (comCnt>=100 && status.equals("enable")){
                                    System.out.println("rgsfbx "+ commAdmin);
                                    text1.setVisibility(View.VISIBLE);
                                    lay1.setVisibility(View.VISIBLE);
                                    usersRef.child(commAdmin).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                Boolean premium= snapshot.child("premium").getValue(Boolean.class);
                                                if (premium) {
                                                    String commId = communitySnapshot.getKey();
                                                    String commName = communitySnapshot.child("commName").getValue(String.class);
                                                    String commDesc = communitySnapshot.child("commDesc").getValue(String.class);
                                                    String commImg = communitySnapshot.child("commImg").getValue(String.class);
                                                    String mbrCount = communitySnapshot.child("mbrCount").getValue(String.class);
                                                    String cmmLink = communitySnapshot.child("dynamicLink").getValue(String.class);
                                                    String status = communitySnapshot.child("commStatus").getValue(String.class);
                                                    String monit = communitySnapshot.child("monit").getValue(String.class);
                                                    System.out.println("hdgtc " + commName);

                                                    if (!commAdmin.equals(userId)) {
                                                        CommModel commModel = new CommModel(commId, commName, commDesc, commAdmin, commImg, mbrCount, cmmLink, monit, status);
                                                        globalcommModels.add(commModel);

                                                        LinearLayoutManager layoutManager = new LinearLayoutManager(AddPostNew.this); // 'this' can be replaced with your activity or context
                                                        GlobalcommunityRecyclerview.setLayoutManager(layoutManager);
                                                        GlobalcommunityRecyclerview.setAdapter(globalComClickAdapter);

                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }

                            // Pass the commModels list to the adapter
                            globalComClickAdapter = new GlobalComClickAdapter(AddPostNew.this, globalcommModels, wallbal, new GlobalComClickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position, boolean isChecked) {
                                    // Update the isChecked state in your CommModel
                                    globalcommModels.get(position).setChecked(isChecked);
                                    // Get the commId of the clicked item
                                    String clickedCommId = globalcommModels.get(position).getCommId();

                                    // Check if the commId is present in the array list
                                    // If not present and the limit is not reached, add it
                                    // If present, remove it
                                    if (selectedGlobalCommIds.contains(clickedCommId)) {
                                        selectedGlobalCommIds.remove(clickedCommId);
                                        // Reduce 40 rupees from the textview amount
                                        double currentAmount = Double.parseDouble(chrgrupee.getText().toString());
                                        if (currentAmount >= 40.0) {
                                            chrgrupee.setText(String.valueOf(currentAmount - 40.0));
                                        }

                                    } else if (selectedGlobalCommIds.size() < 10) { // Limit to 10 selections
                                       // selectedGlobalCommIds.add(clickedCommId);

                                        // Check if wallet balance is sufficient for the current selection
                                        double currentAmount = Double.parseDouble(chrgrupee.getText().toString());
                                        double wallBal = Double.parseDouble(wallbal);
                                         newTotalAmount = currentAmount + 40.0;

                                        // Check if the new total amount exceeds the wallet balance
                                        if (newTotalAmount > wallBal) {
                                            // Display toast indicating insufficient balance
                                          //  SnackBarHelper.showSnackbar(AddPostNew.this, "Your wallet balance is not sufficient");
                                            Toast.makeText(AddPostNew.this, "Your wallet balance is not sufficient", Toast.LENGTH_SHORT).show();
                                            // Do not allow checkbox to be selected
                                            // Update the isChecked state in your CommModel
                                            globalcommModels.get(position).setChecked(false); // Set isChecked to false to deselect

                                        } else {
                                            // Add the new selection to selectedGlobalCommIds
                                            selectedGlobalCommIds.add(clickedCommId);
                                            double reduce = wallBal-newTotalAmount;
                                            AdBal = String.valueOf(reduce);
                                            // Update the textview amount
                                            chrgrupee.setText(String.valueOf(newTotalAmount));
                                        }
                                    }

                                    // Check if any item in the RecyclerView is selected
                                    isRecyclerViewGlobalSelected = !selectedGlobalCommIds.isEmpty();

                                    // Print the selectedCommIds for verification
                                    System.out.println("sdvxc Community Ids: " + selectedGlobalCommIds);

                                    // Notify the adapter about the data change
                                    globalComClickAdapter.notifyDataSetChanged();
                                }
                            });
                            System.out.println("sdgvf " +isRecyclerViewGlobalSelected);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        communityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commModels = new ArrayList<>();

                for (DataSnapshot communitySnapshot : snapshot.getChildren()) {
                    // Assuming the data structure under Communities is as follows
                    String commAdmin = communitySnapshot.child("commAdmin").getValue(String.class);
                    int comCnt= (int) communitySnapshot.child("commMembers").getChildrenCount();
                    String status = communitySnapshot.child("monit").getValue(String.class);

                    if (userId.equals(commAdmin)) {
                        text.setVisibility(View.VISIBLE);
                        String commId = communitySnapshot.getKey();
                        String commName = communitySnapshot.child("commName").getValue(String.class);
                        String commDesc = communitySnapshot.child("commDesc").getValue(String.class);
                        String commImg = communitySnapshot.child("commImg").getValue(String.class);
                        String mbrCount = communitySnapshot.child("mbrCount").getValue(String.class);
                        String cmmLink = communitySnapshot.child("dynamicLink").getValue(String.class);
                        String statusComm = communitySnapshot.child("commStatus").getValue(String.class);
                        System.out.println("5ergdfdfcx " + commName);

                        CommModel commModel = new CommModel(commId, commName, commDesc, commAdmin, commImg, mbrCount, cmmLink,status,statusComm);
                        commModels.add(commModel);
                    }
                }

                // Pass the commModels list to the adapter
                commAdapter = new CommClickAdapter(AddPostNew.this, commModels, new CommClickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, boolean isChecked) {
                        // Update the isChecked state in your CommModel
                        commModels.get(position).setChecked(isChecked);
                        // Get the commId of the clicked item
                        String clickedCommId = commModels.get(position).getCommId();

                        // Check if the commId is present in the array list
                        // If not present, add it or remove it
                        if (selectedCommIds.contains(clickedCommId)) {
                            selectedCommIds.remove(clickedCommId);
                        } else {
                            selectedCommIds.add(clickedCommId);
                        }



                        // Check if any item in the RecyclerView is selected
                        isRecyclerViewItemSelected = !selectedCommIds.isEmpty();

                        // Print the selectedCommIds for verification
                        System.out.println("Selected Community Ids: " + selectedCommIds);

                        // Notify the adapter about the data change
                        commAdapter.notifyDataSetChanged();
                    }
                });
                System.out.println("sdgvf " +isRecyclerViewItemSelected);
                LinearLayoutManager layoutManager = new LinearLayoutManager(AddPostNew.this); // 'this' can be replaced with your activity or context
                communityRecyclerview.setLayoutManager(layoutManager);
                communityRecyclerview.setAdapter(commAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Toggle the checked state for all items
//                checkBox.setChecked(!checkBox.isChecked());
//
//                // Clear the selectedCommIds list
//                selectedCommIds.clear();
//
//                // Add "prathamesh" to the list
//                selectedCommIds.add("prathamesh");
//
//                // Loop through commModels to add all IDs to selectedCommIds
//                for (int i = 0; i < commModels.size(); i++) {
//                    commModels.get(i).setChecked(!commModels.get(i).isChecked());
//
//                    if (commModels.get(i).isChecked()) {
//                        selectedCommIds.add(commModels.get(i).getCommId());
//                    }
//                }
//
//                for (int i = 0; i < globalcommModels.size(); i++) {
//                    globalcommModels.get(i).setChecked(!globalcommModels.get(i).isChecked());
//
//                    if (globalcommModels.get(i).isChecked()) {
//                        selectedGlobalCommIds.add(globalcommModels.get(i).getCommId());
//                    }
//                }
//
//
//                // Check if any item in the RecyclerView is selected
//                isRecyclerViewItemSelected = !selectedCommIds.isEmpty();
//                isRecyclerViewGlobalSelected = !selectedGlobalCommIds.isEmpty();
//                // Print the selectedCommIds for verification
//                System.out.println("thbdy Community Ids: " + selectedGlobalCommIds);
//
//                // Update the adapter
//                if (commAdapter != null) {
//                    commAdapter.notifyDataSetChanged();
//                }
//                if (globalComClickAdapter != null) {
//                    globalComClickAdapter.notifyDataSetChanged();
//                }
//            }
//        });


        postcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!selectedCommIds.isEmpty() || !selectedGlobalCommIds.isEmpty()) {
                    if (pid.equals("1")) {
                        if (postDesc.getText().toString().isEmpty() && filePath == null && writecationedittext.getText().toString().trim().isEmpty() && !isBanenrImage) {
                            Toast.makeText(AddPostNew.this, "Blank", Toast.LENGTH_SHORT).show();
                        } else {
                            if (!spinner.getSelectedItem().toString().trim().equals("Select")) {
                                if (("Local").equals(checkstring) && servedAreasFirebaseFormat==null) {
                                    Toast.makeText(AddPostNew.this, "Please add location", Toast.LENGTH_SHORT).show();
                                    return; // Return without proceeding if location is not added
                                }

                                if (filePath != null && !postDesc.getText().toString().isEmpty()) {
                                    progressDialog.setMessage("Posting..."); // Set the message to be displayed
                                    progressDialog.setCancelable(false); // Set whether the dialog can be canceled by tapping outside of it
                                    progressDialog.show();
                                    saveImageToStorage(filePath, "1"); // save both
                                } else if (filePath != null && postDesc.getText().toString().isEmpty()) {
                                    progressDialog.setMessage("Posting..."); // Set the message to be displayed
                                    progressDialog.setCancelable(false); // Set whether the dialog can be canceled by tapping outside of it
                                    progressDialog.show();
                                    saveImageToStorage(filePath, "2"); //only image
                                } else if (isBanenrImage && postDesc.getText().toString().isEmpty()) {
                                    progressDialog.setMessage("Posting..."); // Set the message to be displayed
                                    progressDialog.setCancelable(false); // Set whether the dialog can be canceled by tapping outside of it
                                    progressDialog.show();
                                    saveFb();
                                } else if (filePath == null && !postDesc.getText().toString().isEmpty()) {
                                    progressDialog.setMessage("Posting..."); // Set the message to be displayed
                                    progressDialog.setCancelable(false); // Set whether the dialog can be canceled by tapping outside of it
                                    progressDialog.show();
                                    saveFb();
                                }
                            } else {
                                errortext.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        if (!spinner.getSelectedItem().toString().trim().equals("Select")) {
                            if (!writecationedittext.getText().toString().trim().isEmpty()) {
                                shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            if (("Local").equals(checkstring) && servedAreasFirebaseFormat==null) {
                                                Toast.makeText(AddPostNew.this, "Please add location", Toast.LENGTH_SHORT).show();
                                                return; // Return without proceeding if location is not added
                                            }else {
                                                captureAndSaveImage();
                                                showImageSelectiondialog();
                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        // Handle onCancelled
                                    }
                                });
                            } else {
                                Toast.makeText(AddPostNew.this, "Blank Text", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            errortext.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    Toast.makeText(AddPostNew.this, "Please select General or Community", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        postcard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(!selectedCommIds.isEmpty() || !selectedGlobalCommIds.isEmpty()) {
//                    if (pid == "1") {
//                        if (postDesc.getText().toString().isEmpty() && filePath == null && writecationedittext.getText().toString().trim().isEmpty() && !isBanenrImage) {
//                            Toast.makeText(AddPostNew.this, "Blank", Toast.LENGTH_SHORT).show();
//                        } else {
//                            if (!(spinner.getSelectedItem().toString().trim().equals("Select"))) {
//
//                                if (filePath != null && !postDesc.getText().toString().isEmpty()) {
//                                    saveImageToStorage(filePath, "1"); // save both
//                                } else if (filePath != null && postDesc.getText().toString().isEmpty()) {
//                                    saveImageToStorage(filePath, "2"); //only image
//                                } else if (isBanenrImage && postDesc.getText().toString().isEmpty()) {
//                                    saveFb();
//                                } else if (filePath == null && !postDesc.getText().toString().isEmpty()) {
//                                    saveFb();
//                                }
//
//                            } else {
//                                errortext.setVisibility(View.VISIBLE);
//                            }
//
//
//                        }
//                    } else {
//                        if (!(spinner.getSelectedItem().toString().trim().equals("Select"))) {
//                            if (!writecationedittext.getText().toString().trim().isEmpty()) {
//                                shopRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.exists()) {
//                                            captureAndSaveImage();
//                                            showImageSelectiondialog();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError error) {
//                                        // Handle onCancelled
//                                    }
//                                });
//
//                            } else {
//                                Toast.makeText(AddPostNew.this, "Blank Text", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            errortext.setVisibility(View.VISIBLE);
//                        }
//                    }
//                } else {
//                    Toast.makeText(AddPostNew.this, "Please select General or Community", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });



        // Show the BottomSheetDialog
        //  bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();

    }




    public void showBannerImg(){
        String spincate = spinner.getSelectedItem().toString().trim();
        if (!("Select").equals(spincate)) {
        // Inflate the layout for the BottomSheetDialog
        View bottomSheetView = LayoutInflater.from(this).inflate(R.layout.banner_bottom_sheet, null);

        // Customize the BottomSheetDialog as needed
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        checkstring = "Global";
        // Disable scrolling for the BottomSheetDialog
        BottomSheetBehavior<View> behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setPeekHeight(getResources().getDisplayMetrics().heightPixels);

        // Handle views inside the BottomSheetDialog
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView bannerRecyclerview = bottomSheetView.findViewById(R.id.bannerImgView);
        ImageView back = bottomSheetView.findViewById(R.id.back);

       back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Categories");
            bannerList = new ArrayList<>();  // Create a new list

            ref.child(spincate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<String> imageUrls = new ArrayList<>();
                    DataSnapshot imageUrlsSnapshot = snapshot.child("BannerImg");

                    for (DataSnapshot imageUrlSnapshot : imageUrlsSnapshot.getChildren()) {
                        String imageUrl = imageUrlSnapshot.getValue(String.class);
                        if (imageUrl != null) {
                            imageUrls.add(imageUrl);
                            System.out.println("wrgdsv " +imageUrl);

                            PostBannerClass postBannerClass = new PostBannerClass();
                            postBannerClass.setBannerImageUrls(imageUrl);
                            bannerList.add(postBannerClass);
                        }
                    }

                    // Create and set the adapter outside the ValueEventListener
                    postBannerAdapter = new PostBannerAdapter(AddPostNew.this, bannerList, AddPostNew.this);
                    bannerRecyclerview.setLayoutManager(new GridLayoutManager(AddPostNew.this, 4));
                    bannerRecyclerview.setAdapter(postBannerAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled event
                }
            });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        // Show the BottomSheetDialog
        //  bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.show();
        } else {
            Toast.makeText(this, "Please select categories", Toast.LENGTH_SHORT).show();
        }

    }

    private void addServeArea(String serveArea) {
        servedAreasList.add(serveArea);
    }

    // Method to remove a served area from the list
    private void removeServeArea(int position) {
        if (position >= 0 && position < servedAreasList.size()) {
            servedAreasList.remove(position);
            servedAreasLayout.removeAllViews(); // Clear the existing views

            StringBuilder servedAreasString = new StringBuilder(); // StringBuilder to concatenate served areas

            for (int i = 0; i < servedAreasList.size(); i++) {
                View servedAreaView = LayoutInflater.from(this).inflate(R.layout.served_area_item, null);

                TextView servedAreaTextView = servedAreaView.findViewById(R.id.servedAreaTextView);
                servedAreaTextView.setText(servedAreasList.get(i));

                servedAreasLayout.setVisibility(View.VISIBLE);
                servedAreasLayout.addView(servedAreaView);

                // Append the served area to the StringBuilder with the delimiter
                servedAreasString.append(servedAreasList.get(i));
                if (i < servedAreasList.size() - 1) {
                    servedAreasString.append("&&");
                }
            }

            // Now, you can use servedAreasString.toString() to store in Firebase
            servedAreasFirebaseFormat = servedAreasString.toString();
        } else {
            // Log an error or show a message indicating that the position is invalid
            Log.e("RemoveServeArea", "Invalid position: " + position);
        }
    }



    // Method to update the UI with the current list of served areas
// Method to update the UI with the current list of served areas
    private void updateServedAreasUI() {

        servedAreasLayout.removeAllViews(); // Clear the existing views

        StringBuilder servedAreasString = new StringBuilder(); // StringBuilder to concatenate served areas

        for (int i = 0; i < servedAreasList.size(); i++) {
            View servedAreaView = LayoutInflater.from(this).inflate(R.layout.served_area_item, null);

            TextView servedAreaTextView = servedAreaView.findViewById(R.id.servedAreaTextView);
            servedAreaTextView.setText(servedAreasList.get(i));

            ImageView cancelServeAreaImageView = servedAreaView.findViewById(R.id.cancelServeAreaImageView);
            final int position = i;

            cancelServeAreaImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeServeArea(position);
                }
            });

            servedAreasLayout.setVisibility(View.VISIBLE);
            servedAreasLayout.addView(servedAreaView);

            // Append the served area to the StringBuilder with the delimiter
            servedAreasString.append(servedAreasList.get(i));
            if (i < servedAreasList.size() - 1) {
                servedAreasString.append("&&");
            }
        }

        // Now, you can use servedAreasString.toString() to store in Firebase
        servedAreasFirebaseFormat = servedAreasString.toString();

        // Store servedAreasFirebaseFormat in Firebase
    }

    private void captureAndSaveImage() {
        // Get the background image as a bitmap
        Bitmap backgroundBitmap = getBitmapFromView(imagelayout);

        // Create a new bitmap with the same dimensions as the background
        Bitmap mergedBitmap = Bitmap.createBitmap(
                backgroundBitmap.getWidth(),
                backgroundBitmap.getHeight(),
                Bitmap.Config.ARGB_8888
        );

        // Create a canvas to draw the merged bitmap
        Canvas canvas = new Canvas(mergedBitmap);

        // Draw the background image onto the canvas
        canvas.drawBitmap(backgroundBitmap, 0, 0, null);

        // Save the merged image and store information in the Firebase Realtime Database
        saveImageToStorage(mergedBitmap);
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


    private void saveImageToStorage(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique filename for the image
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("businessposts/" +userId+ "/" + fileName);

        // Convert the Bitmap to a byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        // Upload the image to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // Image uploaded successfully, get the download URL
                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                imageUrl = downloadUri.toString();
                                System.out.println("edsrdbf " +imageUrl);
                                // Move the storeBusiImageInfo call here to ensure imageUrl is set
                                storeBusiImageInfo(imageUrl, writecationedittext.getText().toString().trim());
                            } else {

                                // Handle failure to get download URL
                            }
                        }
                    });
                } else {
                    // Handle failure to upload image
                }
            }
        });
    }

    // Add this method to store information in the Firebase Realtime Database
    private void storeBusiImageInfo(String imageUrl, String captionText) {
        // Assuming you have a reference to your Firebase Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Generate a new key for the business post
        String postKey = databaseRef.child("BusinessPosts").child(userId).push().getKey();

        // Create a map to store the data
        Map<String, Object> postData = new HashMap<>();
        postData.put("postImg", imageUrl);
        postData.put("postType","Image");
        postData.put("postKeys",postKeys.getText().toString().trim());
       // postData.put("postCate","-");
        postData.put("status", "In Review");
        postData.put("visibilityCount", "0");
        postData.put("clickCount", "0");
        postData.put("postCate", spinner.getSelectedItem().toString().trim());

        if (AdBal!=null) {
            usersRef.child(userId).child("AdBalance").setValue(AdBal);
        }

        //postData.put("subCategory", selectedSubcategory); // Add a new field for subcategory
        for (String globalCommId : selectedGlobalCommIds) {
            // If the element is not already present in the new ArrayList, add it
            if (!selectedCommIds.contains(globalCommId)) {
                selectedCommIds.add(globalCommId);
            }
        }
        Log.d("rgsfvgrw", selectedCommIds.toString());

        // Check if the general card checkbox is selected
        if ((selectedCommIds.contains("prathamesh") && selectedCommIds.size() > 1)) {
            // If 'prathamesh' is present along with other community IDs
            postData.put("status", "In Review");

            // Create a StringBuilder to concatenate community IDs
            StringBuilder communityIds = new StringBuilder();
            for (String commId : selectedCommIds) {
                communityIds.append(commId).append("&&");
            }

            // Remove the trailing "&&" and add the concatenated community IDs to the postData
            postData.put("Comm", communityIds.substring(0, communityIds.length() - 2));

        } else if (selectedCommIds.contains("prathamesh")) {
            // If only 'prathamesh' is present
            postData.put("status", "In Review");
        } else {
            if (!selectedCommIds.isEmpty()){
                // If other community IDs are present
                postData.put("status", "Community");

                // Create a StringBuilder to concatenate community IDs
                StringBuilder communityIds = new StringBuilder();
                for (String commId : selectedCommIds) {
                    communityIds.append(commId).append("&&");
                }

                // Remove the trailing "&&" and add the concatenated community IDs to the postData
                postData.put("Comm", communityIds.substring(0, communityIds.length() - 2));
            }

        }

        //for global community
        if (checkstring.equals("Global")){
            postData.put("servingArea", "Global");
        }else {
            postData.put("servingArea", servedAreasFirebaseFormat);
        }

        // Set the data under the generated post key
        databaseRef.child("BusinessPosts").child(userId).child(postKey).setValue(postData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data successfully stored in the database
                            System.out.println("Image URL and Caption stored successfully");
                        } else {
                            // Handle the failure to store data
                            System.out.println("Failed to store data: " + task.getException().getMessage());
                        }
                    }
                });
    }



    public void setGradientImage(int drawable){
        Glide.with(this)
                .load(drawable)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(busipostimg);
    }

    public void setTextColor(int color){
        writecationedittext.setTextColor(ContextCompat.getColor(this, color));
    }

    private void adjustTextSize(int textLength) {
        // Define your desired text size range and corresponding steps
        final float minTextSize = 18; // Minimum text size
        final float maxTextSize = 30; // Maximum text size
        final float textSizeStep = 0.1f; // Text size step

        // Calculate the new text size based on the length of the text
        float newSize = Math.max(minTextSize, maxTextSize - textLength * textSizeStep);

        // Apply the new text size to the EditText
        writecationedittext.setTextSize(newSize);
    }


    private void showFileChooser() {
        new ImagePicker.Builder(this)
//                Crop image(Optional), //Check Customization for more option
//                 .compress(1024)			//Final image size will be less than 1 MB(Optional)
//                 .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .crop(400,400).start();

        Log.d("ss","camera");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Log.d("ss",""+resultCode);

        if (resultCode == RESULT_OK && data != null) {
            // Uri object will not be null for RESULT_OK
            filePath = data.getData();
            //ImageUpload();
            Glide.with(this)
                    .load(filePath)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(postImg);
            imgFrame.setVisibility(View.VISIBLE);
        }else {
            Log.d("xcxc",""+data);
        }
    }

    private void saveImageToStorage(Uri imageUri,String action) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Create a unique filename for the image
        String fileName = "image_" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child("businessposts/" + userId + "/" + fileName);

        try {
            InputStream stream = getContentResolver().openInputStream(imageUri);
            // Convert the InputStream to a byte array
            byte[] data = getBytes(stream);

            // Upload the image to Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(data);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Image uploaded successfully, get the download URL
                        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    String imageUrl = downloadUri.toString();
                                    System.out.println("Download URL: " + imageUrl);
                                    // Move the storeBusiImageInfo call here to ensure imageUrl is set

                                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

                                    // Generate a new key for the business post
                                    String postKey = databaseRef.child("BusinessPosts").child(userId).push().getKey();

                                    // Create a map to store the data
                                    Map<String, Object> postData = new HashMap<>();

                                    if(action.equals("2")){
                                        postData.put("postImg", imageUrl);
                                        postData.put("postType","Image");
                                        postData.put("postKeys",postKeys.getText().toString().trim());
                                       // postData.put("postCate","-");
                                        postData.put("status", "In Review");
                                        postData.put("visibilityCount", "0");
                                        postData.put("clickCount", "0");
                                        postData.put("postCate", spinner.getSelectedItem().toString().trim());

                                        if (AdBal!=null) {
                                            usersRef.child(userId).child("AdBalance").setValue(AdBal);
                                        }

                                        //postData.put("subCategory", selectedSubcategory); // Add a new field for subcategory
                                        for (String globalCommId : selectedGlobalCommIds) {
                                            // If the element is not already present in the new ArrayList, add it
                                            if (!selectedCommIds.contains(globalCommId)) {
                                                selectedCommIds.add(globalCommId);
                                            }
                                        }
                                        Log.d("rgsfvgrw", selectedCommIds.toString());

                                        // Check if the general card checkbox is selected
                                        if ((selectedCommIds.contains("prathamesh") && selectedCommIds.size() > 1)) {
                                            // If 'prathamesh' is present along with other community IDs
                                            postData.put("status", "In Review");

                                            // Create a StringBuilder to concatenate community IDs
                                            StringBuilder communityIds = new StringBuilder();
                                            for (String commId : selectedCommIds) {
                                                communityIds.append(commId).append("&&");
                                            }

                                            // Remove the trailing "&&" and add the concatenated community IDs to the postData
                                            postData.put("Comm", communityIds.substring(0, communityIds.length() - 2));

                                        } else if (selectedCommIds.contains("prathamesh")) {
                                            // If only 'prathamesh' is present
                                            postData.put("status", "In Review");
                                        } else {
                                            if (!selectedCommIds.isEmpty()){
                                                // If other community IDs are present
                                                postData.put("status", "Community");

                                                // Create a StringBuilder to concatenate community IDs
                                                StringBuilder communityIds = new StringBuilder();
                                                for (String commId : selectedCommIds) {
                                                    communityIds.append(commId).append("&&");
                                                }

                                                // Remove the trailing "&&" and add the concatenated community IDs to the postData
                                                postData.put("Comm", communityIds.substring(0, communityIds.length() - 2));
                                            }

                                        }

                                        if (checkstring.equals("Global")){
                                            postData.put("servingArea", "Global");
                                        }else {
                                            postData.put("servingArea", servedAreasFirebaseFormat);
                                        }
                                        showImageSelectiondialog();
                                    }else {
                                        postData.put("postImg", imageUrl);
                                        postData.put("postDesc",postDesc.getText().toString().trim());
                                        postData.put("postType","Both");
                                        postData.put("postKeys",postKeys.getText().toString().trim());
                                      //  postData.put("postCate","-");
                                        postData.put("status", "In Review");
                                        postData.put("visibilityCount", "0");
                                        postData.put("clickCount", "0");
                                        postData.put("postCate", spinner.getSelectedItem().toString().trim());

                                        if (AdBal!=null) {
                                            usersRef.child(userId).child("AdBalance").setValue(AdBal);
                                        }

                                        //postData.put("subCategory", selectedSubcategory); // Add a new field for subcategory
                                        for (String globalCommId : selectedGlobalCommIds) {
                                            // If the element is not already present in the new ArrayList, add it
                                            if (!selectedCommIds.contains(globalCommId)) {
                                                selectedCommIds.add(globalCommId);
                                            }
                                        }
                                        Log.d("rgsfvgrw", selectedCommIds.toString());

                                        // Check if the general card checkbox is selected
                                        if ((selectedCommIds.contains("prathamesh") && selectedCommIds.size() > 1)) {
                                            // If 'prathamesh' is present along with other community IDs
                                            postData.put("status", "In Review");

                                            // Create a StringBuilder to concatenate community IDs
                                            StringBuilder communityIds = new StringBuilder();
                                            for (String commId : selectedCommIds) {
                                                communityIds.append(commId).append("&&");
                                            }

                                            // Remove the trailing "&&" and add the concatenated community IDs to the postData
                                            postData.put("Comm", communityIds.substring(0, communityIds.length() - 2));

                                        } else if (selectedCommIds.contains("prathamesh")) {
                                            // If only 'prathamesh' is present
                                            postData.put("status", "In Review");
                                        } else {
                                            if (!selectedCommIds.isEmpty()){
                                                // If other community IDs are present
                                                postData.put("status", "Community");

                                                // Create a StringBuilder to concatenate community IDs
                                                StringBuilder communityIds = new StringBuilder();
                                                for (String commId : selectedCommIds) {
                                                    communityIds.append(commId).append("&&");
                                                }

                                                // Remove the trailing "&&" and add the concatenated community IDs to the postData
                                                postData.put("Comm", communityIds.substring(0, communityIds.length() - 2));
                                            }

                                        }
                                        if (checkstring.equals("Global")){
                                            postData.put("servingArea", "Global");
                                        }else {
                                            postData.put("servingArea", servedAreasFirebaseFormat);
                                        }
                                        showImageSelectiondialog();
                                    }

                                    // Set the data under the generated post key
                                    databaseRef.child("BusinessPosts").child(userId).child(postKey).setValue(postData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Data successfully stored in the database
                                                      //  Toast.makeText(AddPostNew.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                                                        System.out.println("Image URL and Caption stored successfully");
                                                    } else {
                                                        // Handle the failure to store data
                                                        Toast.makeText(AddPostNew.this, "Failed", Toast.LENGTH_SHORT).show();
                                                        System.out.println("Failed to store data: " + task.getException().getMessage());
                                                    }
                                                }
                                            });

                                } else {
                                    // Handle failure to get download URL
                                }
                            }
                        });
                    } else {
                        // Handle failure to upload image
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            // Handle file not found exception
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void saveFb() {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

        // Generate a new key for the business post
        String postKey = databaseRef.child("BusinessPosts").child(userId).push().getKey();

        // Create a map to store the data
        Map<String, Object> postData = new HashMap<>();
        if (bannerimage!=null) {
            postData.put("postImg", bannerimage);
            postData.put("postType", "Image");
            if (AdBal!=null) {
                usersRef.child(userId).child("AdBalance").setValue(AdBal);
            }
        } else{
            postData.put("postImg", "-");
            postData.put("postType", "Text");
        }
        postData.put("postDesc", postDesc.getText().toString().trim());
        postData.put("postKeys", postKeys.getText().toString().trim());
        //postData.put("postCate","-");
        postData.put("status", "In Review");
        postData.put("visibilityCount", "0");
        postData.put("clickCount", "0");
        postData.put("postCate", spinner.getSelectedItem().toString().trim());

        if (AdBal!=null) {
            usersRef.child(userId).child("AdBalance").setValue(AdBal);
        }
        //postData.put("subCategory", selectedSubcategory); // Add a new field for subcategory
        for (String globalCommId : selectedGlobalCommIds) {
            // If the element is not already present in the new ArrayList, add it
            if (!selectedCommIds.contains(globalCommId)) {
                selectedCommIds.add(globalCommId);
            }
        }
        Log.d("rgsfvgrw", selectedCommIds.toString());

        // Check if the general card checkbox is selected
        if ((selectedCommIds.contains("prathamesh") && selectedCommIds.size() > 1)) {
            // If 'prathamesh' is present along with other community IDs
            postData.put("status", "In Review");

            // Create a StringBuilder to concatenate community IDs
            StringBuilder communityIds = new StringBuilder();
            for (String commId : selectedCommIds) {
                communityIds.append(commId).append("&&");
            }

            // Remove the trailing "&&" and add the concatenated community IDs to the postData
            postData.put("Comm", communityIds.substring(0, communityIds.length() - 2));

        } else if (selectedCommIds.contains("prathamesh")) {
            // If only 'prathamesh' is present
            postData.put("status", "In Review");
        } else {
            if (!selectedCommIds.isEmpty()){
                // If other community IDs are present
                postData.put("status", "Community");

                // Create a StringBuilder to concatenate community IDs
                StringBuilder communityIds = new StringBuilder();
                for (String commId : selectedCommIds) {
                    communityIds.append(commId).append("&&");
                }

                // Remove the trailing "&&" and add the concatenated community IDs to the postData
                postData.put("Comm", communityIds.substring(0, communityIds.length() - 2));
            }

        }

        if (checkstring.equals("Global")){
            postData.put("servingArea", "Global");
        }else {
            postData.put("servingArea", servedAreasFirebaseFormat);
        }
        // Set the data under the generated post key
        databaseRef.child("BusinessPosts").child(userId).child(postKey).setValue(postData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Data successfully stored in the database
                          //  Toast.makeText(AddPostNew.this, "Posted Successfully", Toast.LENGTH_SHORT).show();
                            System.out.println("Image URL and Caption stored successfully");
                            showImageSelectiondialog();
                        } else {
                            // Handle the failure to store data
                            Toast.makeText(AddPostNew.this, "Failed", Toast.LENGTH_SHORT).show();
                            System.out.println("Failed to store data: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void showImageSelectiondialog() {
        progressDialog.dismiss();
        Dialog dialog1 = new Dialog(this);
        // Inflate the custom layout
        dialog1.setContentView(R.layout.progress_dialog);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button cancelButton = dialog1.findViewById(R.id.closeButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog1.show();
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
    }

    public void retrievePostCategory() {
        DatabaseReference postcatRef = FirebaseDatabase.getInstance().getReference("Categories");

        postcatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                List<String> categoryKeys = new ArrayList<>();

                // Iterate through the categories and get their keys
                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String categoryKey = categorySnapshot.getKey();
                    if (categoryKey != null) {
                        categoryKeys.add(categoryKey);
                    }
                }

                // Now you can use the categoryKeys list to set the main category spinner data
                setCategorySpinnerData(categoryKeys);
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    private void setCategorySpinnerData(List<String> categoryKeys) {


        // Add "Select category" as the first item
        categoryKeys.add(0, "Select");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryKeys);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position > 0) {
                    String selectedCategory = categoryKeys.get(position);
                    String selectedCat = spinner.getSelectedItem().toString().trim();
                    retrieveKeywords(selectedCategory);
                } else {
                    // Handle "Select category" selection if needed
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle nothing selected
            }
        });

    }
    private void retrieveKeywords(String selectedCategory) {
        DatabaseReference keywordsRef = FirebaseDatabase.getInstance().getReference("Categories")
                .child(selectedCategory).child("Keywords");

        keywordsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String keywords = snapshot.getValue(String.class);
                    // Update the EditText with the retrieved keywords;
                    //postKeys.setVisibility(View.VISIBLE);
                    postKeys.setText(keywords);
                    postKeys.setFocusable(false);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }

    @Override
    public void onClickImage(int position, String imageUrl) {
        bannerimage = imageUrl;
        filePath=null;
        isBanenrImage=true;
        Glide.with(this)
                .load(imageUrl)
                .into(postImg);

        imgFrame.setVisibility(View.VISIBLE);
        postImg.setVisibility(View.VISIBLE);
        removeimg.setVisibility(View.VISIBLE);
        bottomSheetDialog.dismiss();
    }
}