package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.spark.swarajyabiz.Extra.PayoutService;

public class Withdraw extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        //Checkout.preload(getApplicationContext());

        //initiateWithdrawal();


        PayoutService payoutService = new PayoutService();
        payoutService.initiatePayout(new PayoutService.PayoutCallback() {
            @Override
            public void onSuccess() {
                Log.d("Payout", "Payout initiated successfully");
            }

            @Override
            public void onError(int code, String message) {
                Log.e("Payout", "Payout initiation failed: " + message);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Payout", "Payout initiation failed", throwable);
            }
        });

    }


}