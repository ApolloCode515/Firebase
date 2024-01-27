package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;

public class ProDashboard extends AppCompatActivity {
    TextView amt100,amt300,amt500,egbal,unamex,earnbal;
    EditText tpedit,usermb,tpedit1;
    ImageView clear,clear1,img1,img2,img3,img4,img5,img6;
    Button addmoney,check,acto,withdraw;
    LinearLayout rcharge,activate,history,rclay,actlay,histlay,silver,gold,platinum,nohist,setpro,incentive;
    String userId,uname,email="dd",wallbal,plan,vldty,pamt,payId,Tid;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mref;
    ArrayList<Integer> Idlist1=new ArrayList<Integer>();
    TextView translog,expdts,uplan;
    LinearLayout sharekk;

    double earn=0;
    String adBalance,wallBalance;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_dashboard);

        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference();

        amt100=findViewById(R.id.amt100);
        amt300=findViewById(R.id.amt300);
        amt500=findViewById(R.id.amt500);
        tpedit=findViewById(R.id.tpamt);
        clear=findViewById(R.id.clear);
        egbal=findViewById(R.id.egbalance);
        unamex=findViewById(R.id.unamev);
        uplan=findViewById(R.id.uplan);

        translog=findViewById(R.id.translogxx);


        rcharge=findViewById(R.id.recharge11);
        activate=findViewById(R.id.activate11);
        history=findViewById(R.id.history11);
        rclay=findViewById(R.id.rclay);
        actlay=findViewById(R.id.actlay);
        histlay=findViewById(R.id.histlay);
        addmoney=findViewById(R.id.addmoney);

        withdraw=findViewById(R.id.withdrawmoney);

        tpedit1=findViewById(R.id.tpamt1);
        clear1=findViewById(R.id.clear1);


        img1=findViewById(R.id.img1);
        img2=findViewById(R.id.img2);
        img3=findViewById(R.id.img3);

        earnbal=findViewById(R.id.earnball);

        SharedPreferences sharedPreference = ProDashboard.this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
            //contacttext.setText(userId);
        } else {
            // Handle the case where the user ID is not available (e.g., not logged in or not registered)
        }
        rcharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rclay.setVisibility(View.VISIBLE);
                actlay.setVisibility(View.GONE);
                histlay.setVisibility(View.GONE);
                img1.setImageResource(R.drawable.ic_baseline_commit_2422);
                img2.setImageResource(R.drawable.ic_baseline_commit_24);
                img3.setImageResource(R.drawable.ic_baseline_commit_24);
            }
        });

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rclay.setVisibility(View.GONE);
                actlay.setVisibility(View.VISIBLE);
                histlay.setVisibility(View.GONE);
                img1.setImageResource(R.drawable.ic_baseline_commit_24);
                img2.setImageResource(R.drawable.ic_baseline_commit_2422);
                img3.setImageResource(R.drawable.ic_baseline_commit_24);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rclay.setVisibility(View.GONE);
                histlay.setVisibility(View.VISIBLE);
                actlay.setVisibility(View.GONE);
                img1.setImageResource(R.drawable.ic_baseline_commit_24);
                img2.setImageResource(R.drawable.ic_baseline_commit_24);
                img3.setImageResource(R.drawable.ic_baseline_commit_2422);
                //ClearAll();
                //loadDataHist();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tpedit.getText().toString().trim().isEmpty()){

                }else {
                    tpedit.setText("");
                }
            }
        });

        clear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tpedit1.getText().toString().trim().isEmpty()){

                }else {
                    tpedit1.setText("");
                }
            }
        });

        tpedit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (!tpedit1.getText().toString().isEmpty()) {
//                    double amt = Double.parseDouble(tpedit1.getText().toString().trim());
//                    if (amt >= 100 && !(amt > earn)) {
//                        withdraw.setEnabled(true);
//                        withdraw.setBackgroundResource(R.drawable.button); // Use setBackgroundResource
//                    } else {
//                        withdraw.setEnabled(false); // Disable the button
//                        withdraw.setBackgroundResource(R.drawable.buttondisable); // Use setBackgroundResource
//                    }
//                } else {
//                    withdraw.setEnabled(false); // Disable the button if the edit text is empty
//                    withdraw.setBackgroundResource(R.drawable.buttondisable); // Use setBackgroundResource
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        amt100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tpedit.getText().toString().trim().isEmpty()){
                    tpedit.setText("100");
                }else {
                    long amt= Long.parseLong(tpedit.getText().toString().trim());
                    long tt=amt+100;
                    tpedit.setText(String.valueOf(tt));
                }
            }
        });

        amt300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tpedit.getText().toString().trim().isEmpty()){
                    tpedit.setText("300");
                }else {
                    long amt= Long.parseLong(tpedit.getText().toString().trim());
                    long tt=amt+300;
                    tpedit.setText(String.valueOf(tt));
                }
            }
        });

        amt500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tpedit.getText().toString().trim().isEmpty()){
                    tpedit.setText("500");
                }else {
                    long amt= Long.parseLong(tpedit.getText().toString().trim());
                    long tt=amt+500;
                    tpedit.setText(String.valueOf(tt));
                }
            }
        });

        egbal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadBalance();
                Toast.makeText(ProDashboard.this, "Balance Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        addmoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tpedit.getText().toString().trim().isEmpty()){

                }else {
                    Intent intent=new Intent(ProDashboard.this,WalletPayment.class);
                    intent.putExtra("NewBalance",tpedit.getText().toString().trim());
                    intent.putExtra("OldBalance", adBalance.trim());
                    startActivity(intent);
                    //Toast.makeText(ProDashboard.this, "Step 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ProDashboard.this, Withdraw.class);
                startActivity(intent);

                if(tpedit1.getText().toString().trim().isEmpty()){

                }else {

                }
            }
        });

        getWallBal();

    }

    public void getWallBal() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String wallbal = snapshot.child("WallBal").getValue(String.class);
                    String adBal = snapshot.child("AdBalance").getValue(String.class);
                    String uname = snapshot.child("name").getValue(String.class);
                    String plan = snapshot.child("Plan").getValue(String.class);

                    unamex.setText(uname);
                    uplan.setText(plan);

                    adBalance=adBal;

                    earn= Double.parseDouble(wallbal);

                    //egbal.setText("₹ " + adBal);
                   // earnbal.setText("₹ " + wallbal);
                    try {
                        float addBall = Float.parseFloat(adBal);
                        animateTextView(0, addBall, egbal,adBal);

                        float EarnBal = Float.parseFloat(wallbal);
                        animateTextView2(0, EarnBal, earnbal,wallbal);
                        if(EarnBal>=100){
                            tpedit1.setText(wallbal);
                        }else {
                            tpedit1.setText("");
                        }

                    } catch (NumberFormatException e) {
                        // Handle the case where wallbal is not a valid float string
                        // Log the error or set a default value for finalWallBal
                    }
                } else {
                    egbal.setText("₹ 0.0");
                    earnbal.setText("₹ 0.0");
                    //  rupeesx.setText("0.0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
    }


    public void animateTextView(int initialValue, float finalValue, final TextView textview,String mainval) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(initialValue, finalValue);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                int intValue = Math.round(animatedValue); // Round to the nearest integer
                textview.setText("₹ " + intValue);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation finished
                // You can perform any actions here when the animation finishes
                // For example, you can show a toast message or execute additional logic
                textview.setText("₹ " + mainval);
            }
        });

        valueAnimator.start();
    }

    public void animateTextView2(int initialValue, float finalValue, final TextView textview,String mainval) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(initialValue, finalValue);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                int intValue = Math.round(animatedValue); // Round to the nearest integer
                textview.setText("₹ " + intValue);
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation finished
                // You can perform any actions here when the animation finishes
                // For example, you can show a toast message or execute additional logic
                textview.setText("₹ " + mainval);
            }
        });

        valueAnimator.start();
    }

}