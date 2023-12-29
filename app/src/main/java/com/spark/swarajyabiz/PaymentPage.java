package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class PaymentPage extends AppCompatActivity implements PaymentResultWithDataListener {

    TextView payId,rechAmt,trandate,failId;
    CardView successCard,failCard;
    Button close;
    private FirebaseDatabase mdatabase;
    private DatabaseReference mref;

    Checkout checkout;
    String time,amt,desc,Tid,RefId,custMail="sparkcomputer555@gmail.com",plan,expdate,cdate,pvalidity,umob,oldwallbal;
    String userType;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    String orderId="dd";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        mdatabase = FirebaseDatabase.getInstance();
        mref = mdatabase.getReference();



        payId = findViewById(R.id.payId11);
        rechAmt = findViewById(R.id.rechamt11);
        trandate = findViewById(R.id.transdate11);
        failId = findViewById(R.id.failId11);
        successCard = findViewById(R.id.SuccesLayout11);
        failCard = findViewById(R.id.FailLayout11);
        close = findViewById(R.id.PaycloseBtn11);

//       String set1=this.getString(R.string.fbdata);
//       String set2=this.getString(R.string.fbsad);

        Intent intent = getIntent();
        ArrayList<String> data1 = new ArrayList<>(intent.getStringArrayListExtra("Datax"));
        plan = data1.get(0);
        amt=data1.get(1);
        custMail=data1.get(2);
        umob=data1.get(3);


        desc = "Kaam-Dhandaa Premium";
        //Log.d("fsdgsg", "" + plan);


        Checkout.preload(getApplicationContext());

        startPayment();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(PaymentPage.this, MainActivity.class);
                startActivity(intent1);
                PaymentPage.this.finish();
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
        successCard.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        payId.setText(s);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Date date = new Date();
        trandate.setText(dateFormat.format(date));

        DateFormat dateFormat2 = new SimpleDateFormat("hh:mm:ss a");
        Date date2 = new Date();
        time=(dateFormat2.format(date2));
        rechAmt.setText("₹ "+amt);

        // Save to firebase
        mref = mdatabase.getReference("Users/"+umob.toString().trim()+"/Pro/");
        mref.child("Trans").child(payId.getText().toString().trim()).child("TrDate").setValue(trandate.getText().toString().trim());
        mref.child("Trans").child(payId.getText().toString().trim()).child("Amount").setValue(amt.toString().trim());


        Date datec = new Date();
        SimpleDateFormat df  = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c1 = Calendar.getInstance();
        String currentDate = df.format(datec);// get current date here
        // now add 30 day in Calendar instance
        c1.add(Calendar.DAY_OF_YEAR, 30);
        df = new SimpleDateFormat("dd/MM/yyyy");
        Date resultDate = c1.getTime();
        String dueDate = df.format(resultDate);

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Error" +s, Toast.LENGTH_SHORT).show();
        Log.d("paykkk", "e "+s);
        Log.d("Randke","error "+s);
        successCard.setVisibility(View.GONE);
        failCard.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        failId.setText(s);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Date date = new Date();
        trandate.setText(dateFormat.format(date));

    }

}