package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.Adapters.BoxCpnAdapter;
import com.spark.swarajyabiz.Adapters.CommAdapter;
import com.spark.swarajyabiz.Adapters.CouponAdapter;
import com.spark.swarajyabiz.ModelClasses.BoxCpnModel;
import com.spark.swarajyabiz.ModelClasses.CommModel;
import com.spark.swarajyabiz.ModelClasses.CouponModel;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;
import com.stripe.model.Card;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.annotations.NonNull;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class CouponMaker extends AppCompatActivity implements CouponAdapter.OnItemClickListener, IntrestAdapter.OnImgClickListener,ScratchCardView.RevealListener {
    RecyclerView cpnView;
    ExtendedFloatingActionButton fab;

    ArrayList<CouponModel> couponModels;
    CouponAdapter couponAdapter;
    String userId;

    EditText extraDisc, sellingprice;
    TextView couponText;
    RelativeLayout couponLayout;
    ImageView back, couponbackImg;
    KonfettiView konfettiView;

    String firstCoupon, backCoupon, extraamt, intextraAmt;
    CardView attachCard;
    EditText amount;

    String frontVal,backVal;

    int nextCouponCount=0;

    private FirebaseDatabase mdatabase;
    private DatabaseReference mref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_maker);

        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference();

        cpnView=findViewById(R.id.cpnview);
        //fab=findViewById(R.id.fabtn);
        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);

        fab=findViewById(R.id.fabtnsss);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCoupon();
            }
        });

        getCoupons();

    }

    private void ClearAllEmployee(){
        if(couponModels != null){
            couponModels.clear();

            if(couponAdapter!=null){
                couponAdapter.notifyDataSetChanged();
            }
        }
        couponModels=new ArrayList<>();
    }


    public void getCoupons(){
        ClearAllEmployee();
        //lottieAnimationView.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+userId+"/AvCoupons/");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshotx) {
                if (snapshotx.exists()) {
                    for (DataSnapshot keySnapshot : snapshotx.getChildren()) {
                        String cpnId = keySnapshot.getKey();
                        // Assuming cpnId is in the format "couponX", where X is a number
                        String cpnamt = keySnapshot.child("cpnAmt").getValue(String.class);
                        String cpnfront = keySnapshot.child("cpnFront").getValue(String.class);
                        String cpnback = keySnapshot.child("cpnBack").getValue(String.class);

                        CouponModel commModel=new CouponModel();
                        commModel.setCpnId(cpnId);
                        commModel.setCpnAmt(cpnamt);
                        commModel.setCpnFront(cpnfront);
                        commModel.setCpnBack(cpnback);
                        couponModels.add(commModel);

                        cpnView.setLayoutManager(new LinearLayoutManager(CouponMaker.this));
                        couponAdapter = new CouponAdapter(CouponMaker.this,couponModels, CouponMaker.this);
                        cpnView.setAdapter(couponAdapter);

                        couponAdapter.notifyDataSetChanged();

                    }

                    // At this point, maxCount contains the maximum count of existing coupons
                    // Do whatever you need to do with nextCouponCount
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Coupon")
                .setMessage("Are you sure you want to delete this coupon?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        CouponModel cpm=couponModels.get(position);
                        String id=cpm.getCpnId();
                        mref = mdatabase.getReference("Users/"+userId+"/AvCoupons/"+id+"/");
                        mref.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                SnackBarHelper.showSnackbar(CouponMaker.this,"Coupon Deleted");
                                getCoupons();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@androidx.annotation.NonNull Exception e) {

                            }
                        });
                    }
                }).create().show();
    }

    public void openCoupon() {
        final Dialog dialog = new Dialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.newcoupon, null);
        dialog.setContentView(dialogView);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ScratchCardView scratchCardView = dialog.findViewById(R.id.scrachCard11);
        RecyclerView cpnViewzz = dialog.findViewById(R.id.couponviews11);
        couponText = dialog.findViewById(R.id.coupontext11);
        couponbackImg = dialog.findViewById(R.id.couponback11);
        konfettiView = dialog.findViewById(R.id.konfettiView11);
        EditText amount=dialog.findViewById(R.id.extradiscount11);

        CardView create = dialog.findViewById(R.id.attachCardX);

        frontVal="https://firebasestorage.googleapis.com/v0/b/fir-39c66.appspot.com/o/BannerDesign%2FCoupons%2Fcouponft01.jpg?alt=media&token=8b06011a-c751-4160-9a48-0af48b0179db";
        backVal="https://firebasestorage.googleapis.com/v0/b/fir-39c66.appspot.com/o/BannerDesign%2FCoupons%2Fcouponbg01.jpg?alt=media&token=6965e0b3-03dd-467a-981f-8157015f652f";

        try {
            scratchCardView.setScratchImageUrl("https://firebasestorage.googleapis.com/v0/b/fir-39c66.appspot.com/o/BannerDesign%2FCoupons%2Fcouponft01.jpg?alt=media&token=8b06011a-c751-4160-9a48-0af48b0179db");
            Glide.with(this)
                    .load("https://firebasestorage.googleapis.com/v0/b/fir-39c66.appspot.com/o/BannerDesign%2FCoupons%2Fcouponbg01.jpg?alt=media&token=6965e0b3-03dd-467a-981f-8157015f652f")
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(couponbackImg);
        }catch (Exception exception){
            scratchCardView.setScratchImageUrl("https://firebasestorage.googleapis.com/v0/b/fir-39c66.appspot.com/o/BannerDesign%2FCoupons%2Fcouponft01.jpg?alt=media&token=8b06011a-c751-4160-9a48-0af48b0179db");
            Glide.with(this)
                    .load("https://firebasestorage.googleapis.com/v0/b/fir-39c66.appspot.com/o/BannerDesign%2FCoupons%2Fcouponbg01.jpg?alt=media&token=6965e0b3-03dd-467a-981f-8157015f652f")
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(couponbackImg);
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().trim().isEmpty() || frontVal==null || backVal==null){
                    SnackBarHelper.showSnackbar(CouponMaker.this,"Mandetory fields.");
                }else {
                    String cpnId=generateShortRandomId(userId);
                    mref = mdatabase.getReference("Users/"+userId+"/AvCoupons/"+cpnId+"/");
                    mref.child("cpnAmt").setValue(amount.getText().toString().trim());
                    mref.child("cpnFront").setValue(frontVal);
                    mref.child("cpnBack").setValue(backVal).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.dismiss();
                            SnackBarHelper.showSnackbar(CouponMaker.this,"Coupon Created Successfully.");
                            getCoupons();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@androidx.annotation.NonNull Exception e) {

                        }
                    });
                }
            }
        });

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(amount.getText().toString().trim().isEmpty()){
                    couponText.setText("Demo Amount");
                }else {
                    couponText.setText(amount.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Coupons");
        couponRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<BoxCpnModel>boxCpnModels=new ArrayList<>();
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        // Iterate over each child snapshot
                        String front = childSnapshot.child("front").getValue(String.class);
                        String back = childSnapshot.child("back").getValue(String.class);
                        boxCpnModels.add(new BoxCpnModel(front, back));
                    }
                    // cpnViewzz.setLayoutManager(new GridLayoutManager(CouponMaker.this, 2));
                    LinearLayoutManager layoutManager = new LinearLayoutManager(CouponMaker.this, LinearLayoutManager.HORIZONTAL, false);
                    cpnViewzz.setLayoutManager(layoutManager);
                    // cpnView.setLayoutManager(layoutManager);
                    Log.d("fgdfgrgrg"," b  "+boxCpnModels.size());
                    BoxCpnAdapter boxCpnAdapter = new BoxCpnAdapter(boxCpnModels);
                    cpnViewzz.setAdapter(boxCpnAdapter);
                    boxCpnAdapter.notifyDataSetChanged();
//                  LinearLayoutManager layoutManager = new LinearLayoutManager(CouponMaker.this, LinearLayoutManager.VERTICAL, false);
                    boxCpnAdapter.setOnItemClickListener(new BoxCpnAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BoxCpnModel itemData) {
                            if (itemData != null) {
                                scratchCardView.setVisibility(View.VISIBLE);
                                scratchCardView.setScratchImageUrl(itemData.getFront());
                                frontVal=itemData.getFront();
                                backVal=itemData.getBack();
                                Glide.with(CouponMaker.this)
                                        .load(itemData.getBack())
                                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(couponbackImg);
                            }
                        }
                    });
                } else {
                    // Handle the case when the snapshot doesn't exist
                    Log.d("TAG", "Snapshot doesn't exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
                Log.e("TAG", "DatabaseError: " + error.getMessage());
            }
        });



        // Load default images
        scratchCardView.setRevealListener(new ScratchCardView.RevealListener() {
            @Override
            public void onRevealed() {
                scratchCardView.setVisibility(View.GONE);

                konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(1000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12,5))
                        .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                        .streamFor(300, 1000L);
            }
        });

        dialog.show();
       // dialog.setCancelable(false);

    }


    @Override
    public void onRevealed() {

    }

    @Override
    public void ImgClick(int position) {

    }

    public static String generateShortRandomId(String mobileNumber) {
        // Get the current timestamp in milliseconds
        long timestamp = System.currentTimeMillis();

        // Format the timestamp (including milliseconds)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String formattedTimestamp = dateFormat.format(new Date(timestamp));

        // Combine mobile number and timestamp
        String timestampId = mobileNumber + formattedTimestamp;

        // Generate a random ID from the timestampId using SHA-256 hashing
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(timestampId.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String ss = "Coupon-"+hexString.toString().substring(0, 5);
            // Take the first 10 characters as the shortened random ID
            return ss;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception according to your application's needs
            return null;
        }
    }

}