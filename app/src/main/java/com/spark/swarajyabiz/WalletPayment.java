package com.spark.swarajyabiz;

import static com.spark.swarajyabiz.LoginMain.PREFS_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WalletPayment extends AppCompatActivity implements PaymentResultWithDataListener {

    TextView payId,rechAmt,trandate,failId,planName,msg;
    CardView successCard,failCard;
    Button close;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mref, usersRef;
    Boolean premium;
    Checkout checkout;
    String time,amt,desc,Tid,RefId,custMail="sparkcomputer555@gmail.com",plan,expdate,cdate,pvalidity,umob,oldwallbal;
    String userType;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String orderId="dd";
    String packageName, description, price, packagePlan, userId, userMob,oldAdBal;

    LinearLayout successLay,failLay,mainLayoutxx;

    FrameLayout mainFrame;
    com.airbnb.lottie.LottieAnimationView lottieAnimationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_payment);

        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference();

        payId = findViewById(R.id.payId1188);
        rechAmt = findViewById(R.id.rechamt1188);
        trandate = findViewById(R.id.transdate1188);
        close = findViewById(R.id.PaycloseBtn1188);
        planName = findViewById(R.id.planxx88);
        successLay=findViewById(R.id.successFrame88);
        failLay=findViewById(R.id.failedframe88);
        mainFrame=findViewById(R.id.mainFrameLay88);
        mainLayoutxx=findViewById(R.id.mainLayout88);
        msg=findViewById(R.id.msg88);
        lottieAnimationView=findViewById(R.id.lottiview88);

        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }
        usersRef = FirebaseDatabase.getInstance().getReference("Users");



        amt = getIntent().getStringExtra("NewBalance");
        oldAdBal = getIntent().getStringExtra("OldBalance");

        plan = "Ad-Balance";
        custMail="sparkcomputer555@gmail.com";
        umob=userId;

        desc = "Ad-Balance for Kamdhanda community";
        //Log.d("fsdgsg", "" + plan);

        //Toast.makeText(WalletPayment.this, "Step 2", Toast.LENGTH_SHORT).show();

        Checkout.preload(getApplicationContext());

        startPayment();

    }

    public void startPayment() {
        checkout = new Checkout();
        checkout.setKeyID("rzp_live_GyXbMu1y7DNbpK"); // old id rzp_live_PQ3EerEASSG5Nh
        // checkout.setImage(R.drawable.rzp_logo);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            options.put("name", plan);
            options.put("description", desc);
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("currency", "INR");
            double total=Double.parseDouble(amt);
            total=total*100;
            options.put("amount", total);//pass amount in currency subunits
            options.put("prefill.email", custMail);
            options.put("prefill.contact",umob);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 10);
            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("Razorpay Error", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        Log.d("paykkk", ""+s);
        mainLayoutxx.setVisibility(View.VISIBLE);
        successLay.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        payId.setText(s);
        planName.setText(plan);
        msg.setText("Payment Successful !!");
        lottieAnimationView.setAnimation("success.json");
        lottieAnimationView.playAnimation();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Date date = new Date();
        trandate.setText(dateFormat.format(date));

        DateFormat dateFormat2 = new SimpleDateFormat("hh:mm:ss a");
        Date date2 = new Date();
        time=(dateFormat2.format(date2));
        rechAmt.setText("₹ "+amt);

        // Save to firebase
        mref = mdatabase.getReference("Users/"+umob.toString().trim()+"/");
        mref.child("WalletTrans").child(payId.getText().toString().trim()).child("TrDate").setValue(trandate.getText().toString().trim());
        mref.child("WalletTrans").child(payId.getText().toString().trim()).child("Amount").setValue(amt.toString().trim());
        mref.child("WalletTrans").child(payId.getText().toString().trim()).child("Plan").setValue(plan.toString().trim());
        mref.child("WalletTrans").child(payId.getText().toString().trim()).child("Description").setValue(desc.toString().trim());
        mref.child("WalletTrans").child(payId.getText().toString().trim()).child("Status").setValue("Credit");

        double oldBal= Double.parseDouble(oldAdBal);
        double newBal= Double.parseDouble(amt);
        double total=oldBal+newBal;
        mref = mdatabase.getReference("Users/"+umob.toString().trim()+"/");
        mref.child("AdBalance").setValue(String.valueOf(total));

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Error" +s, Toast.LENGTH_SHORT).show();
        Log.d("paykkk", "e "+s);
        Log.d("l","error "+s);
        lottieAnimationView.setAnimation("failed.json");
        lottieAnimationView.playAnimation();
        msg.setText("Payment Failed !!");
        mainLayoutxx.setVisibility(View.VISIBLE);
        successLay.setVisibility(View.GONE);
        failLay.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        int customColor = ContextCompat.getColor(this, R.color.failcolor);
        mainFrame.setBackgroundColor(customColor);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Date date = new Date();
        trandate.setText(dateFormat.format(date));
    }
}