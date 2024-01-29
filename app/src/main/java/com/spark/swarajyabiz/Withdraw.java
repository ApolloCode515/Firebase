package com.spark.swarajyabiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.spark.swarajyabiz.Extra.BankAccountTokenService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Payout;
import com.stripe.model.Token;
import com.stripe.param.TokenCreateParams;

import java.util.HashMap;
import java.util.Map;


public class Withdraw extends AppCompatActivity {

   // private BackendService backendService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

//        BankAccountTokenService.createBankAccountTokenInBackground("1004110010003091", "RATN0000041", "Shriram Yashwant Khabale", "IN", "INR", new BankAccountTokenService.TokenCreationListener() {
//            @Override
//            public void onTokenCreated(String tokenId) {
//                // Handle the successful creation of bank account token
//                Log.d("Bank Account Token", "Token created: " + tokenId);
//                initiatePayoutInBackground(tokenId);
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                // Handle the error while creating bank account token
//                Log.e("Bank Account Token", "Error creating token: " + errorMessage);
//            }
//        });

    }

    private void initiatePayoutInBackground(String bankAccountToken) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //Stripe.apiKey = "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";

                    Map<String, Object> payoutParams = new HashMap<>();
                    payoutParams.put("amount", 1000); // Payout amount in cents (e.g., ₹10.00)
                    payoutParams.put("currency", "inr"); // Currency code for Indian Rupees
                    payoutParams.put("destination", bankAccountToken); // Destination account ID (replace with actual account ID)
                    payoutParams.put("method", "standard"); // Payout method (e.g., "standard")
                    payoutParams.put("statement_descriptor", "Your Descriptor"); // Statement descriptor (optional)

                    Payout payout = Payout.create(payoutParams);
                    return payout.getId();
                } catch (StripeException e) {
                    Log.e("Withdraw", "Error initiating payout: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String payoutId) {
                if (payoutId != null) {
                    Log.d("Withdraw", "Payout successful: " + payoutId);
                    Toast.makeText(Withdraw.this, "Success", Toast.LENGTH_SHORT).show();
                    // Handle successful payout
                } else {
                    Log.e("Withdraw", "Error initiating payout");
                    Toast.makeText(Withdraw.this, "Failed", Toast.LENGTH_SHORT).show();
                    // Handle error
                }
            }
        }.execute();
    }
}