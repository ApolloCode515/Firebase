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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.spark.swarajyabiz.ui.Fragment_Banner;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PaymentPage extends AppCompatActivity implements PaymentResultWithDataListener {

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
    String packageName, description, price, packagePlan, userId, userMob;

    LinearLayout successLay,failLay,mainLayoutxx;

    FrameLayout mainFrame;
    com.airbnb.lottie.LottieAnimationView lottieAnimationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference();

        payId = findViewById(R.id.payId11);
        rechAmt = findViewById(R.id.rechamt11);
        trandate = findViewById(R.id.transdate11);
        close = findViewById(R.id.PaycloseBtn11);
        planName = findViewById(R.id.planxx);
        successLay=findViewById(R.id.successFrame);
        failLay=findViewById(R.id.failedframe);
        mainFrame=findViewById(R.id.mainFrameLay);
        mainLayoutxx=findViewById(R.id.mainLayout);
        msg=findViewById(R.id.msg);
        lottieAnimationView=findViewById(R.id.lottiview);

        packageName = getIntent().getStringExtra("packageName");
        description = getIntent().getStringExtra("description");
        price = getIntent().getStringExtra("price");
        packagePlan = getIntent().getStringExtra("plan");
        userMob = getIntent().getStringExtra("userMob");

        SharedPreferences sharedPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = sharedPreference.getString("mobilenumber", null);
        if (userId != null) {
            // userId = mAuth.getCurrentUser().getUid();
            System.out.println("dffvf  " +userId);
        }
        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        plan = packagePlan;
        amt=price;
        custMail="sparkcomputer555@gmail.com";
        umob=userMob;

        desc = description;
        //Log.d("fsdgsg", "" + plan);

        Checkout.preload(getApplicationContext());

        startPayment();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PaymentPage.this, BottomNavigation.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                finish();
            }
        });

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
        mref.child("Trans").child(payId.getText().toString().trim()).child("TrDate").setValue(trandate.getText().toString().trim());
        mref.child("Trans").child(payId.getText().toString().trim()).child("Amount").setValue(amt.toString().trim());
        mref.child("Trans").child(payId.getText().toString().trim()).child("Plan").setValue(plan.toString().trim());
        mref.child("Trans").child(payId.getText().toString().trim()).child("Description").setValue(description.toString().trim());

        usersRef.child(umob).child("premium").setValue(true);

        if(description.equals("1 Month")){
            int mval=30;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, mval); // Adding 365 days
            expdate = sdf.format(c.getTime());
            usersRef.child(umob).child("ExpDate").setValue(expdate);
        }else if(description.equals("6 Month")){
            int mval=180;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, mval); // Adding 365 days
            expdate = sdf.format(c.getTime());
            usersRef.child(umob).child("ExpDate").setValue(expdate);
        }else if(description.equals("1 Year")){
            int mval=365;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(new Date()); // Now use today date.
            c.add(Calendar.DATE, mval); // Adding 365 days
            expdate = sdf.format(c.getTime());
            usersRef.child(umob).child("ExpDate").setValue(expdate);
        }

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
      //  Toast.makeText(this, "Payment Error" +s, Toast.LENGTH_SHORT).show();
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