package com.spark.swarajyabiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spark.swarajyabiz.MyFragments.SnackBarHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Emitter;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;



public class Scratch_Coupon extends AppCompatActivity implements IntrestAdapter.OnImgClickListener, ScratchCardView.RevealListener {

    private ScratchCardView mScratchCard;
    EditText extraDisc, sellingprice;
    TextView couponText;
    RelativeLayout couponLayout;
    ImageView back, couponbackImg;
    IntrestAdapter intrestAdapter;
    List<IntrestClass> intrestClassList = new ArrayList<>();  // Initialize the list outside the loop
    RecyclerView couponRecyclerView;
    ScratchCardView scratchView;
    RelativeLayout lay1, lay2, lay3, lay4, lay5;
    KonfettiView konfettiView;

    String firstCoupon, backCoupon, extraamt, intextraAmt;
    CardView attachCard;
    private IntrestClass selectedShop;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch_coupon);


        scratchView = findViewById(R.id.scrachCard);
        extraDisc = findViewById(R.id.extradiscount);
        couponLayout = findViewById(R.id.couponlayout);
        couponText = findViewById(R.id.coupontext);
        sellingprice = findViewById(R.id.itemsellingprice);
        couponRecyclerView = findViewById(R.id.couponviews);
        back = findViewById(R.id.back);
        couponbackImg = findViewById(R.id.couponback);
        konfettiView = findViewById(R.id.konfettiView);
        attachCard = findViewById(R.id.attachCard);



      //  scratchView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.couponft01));
       // scratchView.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.couponft01));
        scratchView.setRevealListener(new ScratchCardView.RevealListener() {
            @Override
            public void onRevealed() {
                // Handle reveal completion
               // Toast.makeText(Scratch_Coupon.this, "ol", Toast.LENGTH_SHORT).show();
                scratchView.setVisibility(View.GONE);

                konfettiView.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(1000L)
                        .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                        .addSizes(new Size(12,5))
                        .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                        //.setPosition(50f, konfettiView.getWidth()+10f, -50f, -50f)
                        .streamFor(300, 1000L);

            }
        });


        sellingprice.setText(getIntent().getStringExtra("sellingprice"));
        firstCoupon = getIntent().getStringExtra("couponfront");
        backCoupon = getIntent().getStringExtra("couponback");
        intextraAmt = getIntent().getStringExtra("extraAmt");
        System.out.println("5yer "+firstCoupon);

        if (intextraAmt!=null){
            extraDisc.setText(intextraAmt);
            couponText.setText("₹ "+intextraAmt);
            couponLayout.setVisibility(View.VISIBLE);
            couponRecyclerView.setVisibility(View.VISIBLE);
        }
        extraDisc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                couponText.setText("₹ "+extraDisc.getText().toString().trim());
                couponLayout.setVisibility(View.VISIBLE);
                couponRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        retrieveCoupons(firstCoupon, backCoupon, intextraAmt);
    }

    public void retrieveCoupons(String firstCoupon,String backCoupon,String intextraAmt){
        DatabaseReference couponRef = FirebaseDatabase.getInstance().getReference("Coupons");

            couponRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            // Iterate over each child snapshot
                            String key = childSnapshot.getKey();

                            couponRef.child(key).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String front = snapshot.child("front").getValue(String.class);
                                        String back = snapshot.child("back").getValue(String.class);

                                        IntrestClass intrestClass = new IntrestClass();
                                        intrestClass.setShopImage(front);
                                        intrestClass.setShopContactNumber(back);
                                        intrestClassList.add(intrestClass);
                                        if (firstCoupon!=null){
                                            scratchView.setScratchImageUrl(firstCoupon);
                                            Glide.with(Scratch_Coupon.this)
                                                    .load(backCoupon)
                                                    .into(couponbackImg);
                                        }else {
                                            if (front != null) {
                                                scratchView.setScratchImageUrl(front);
                                            }
                                            Glide.with(Scratch_Coupon.this)
                                                    .load(back)
                                                    .into(couponbackImg);
                                        }

                                        intrestAdapter = new IntrestAdapter(Scratch_Coupon.this, intrestClassList, Scratch_Coupon.this);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(Scratch_Coupon.this, LinearLayoutManager.HORIZONTAL, false);
                                        couponRecyclerView.setLayoutManager(layoutManager);
                                        couponRecyclerView.setAdapter(intrestAdapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }


                    } else {
                        // Handle the case when the snapshot doesn't exist
                        System.out.println("Snapshot doesn't exist");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle onCancelled
                    System.out.println("DatabaseError: " + error.getMessage());
                }
            });


    }



    @Override
    public void ImgClick(int position) {
        // Get the selected item from the list
        // Get the selected item from the list
        IntrestClass selectedShop = intrestClassList.get(position);
        scratchView.setVisibility(View.VISIBLE);
        //scratchView.setOnlineImage(selectedShop.getShopImage());
        scratchView.setScratchImageUrl(selectedShop.getShopImage());
        Glide.with(this)
                .load(selectedShop.getShopContactNumber())
                .into(couponbackImg);

        attachCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedShop != null) {
                    // Create an Intent
                    extraamt = extraDisc.getText().toString().trim();
                    if (extraamt != null) {
                        setActivityResult(selectedShop.shopImage, selectedShop.getShopContactNumber(), extraamt);
                    }else {
                        SnackBarHelper.showSnackbar(Scratch_Coupon.this, "Please enter extra discount price");
                    }
                } else {
                    // Handle the case when no shop is selected
                    Toast.makeText(Scratch_Coupon.this, "Select a shop first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRevealed() {
        // Handle the reveal event here
        Toast.makeText(this, "Card Revealed!", Toast.LENGTH_SHORT).show();
    }


    private void setActivityResult(String frontCoupon, String backCoupon, String ExtraAmt) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("frontcoupon", frontCoupon);
        resultIntent.putExtra("backcoupon", backCoupon);
        resultIntent.putExtra("extraamt", ExtraAmt);
      //  resultIntent.putExtra("couponstatus", "Enable");
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}