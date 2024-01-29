package com.spark.swarajyabiz.Extra;

import android.os.AsyncTask;
import android.util.Log;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Token;
import com.stripe.param.TokenCreateParams;

import java.util.HashMap;
import java.util.Map;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Token;
import com.stripe.param.TokenCreateParams;
import com.stripe.param.TokenCreateParams.BankAccount;

public class BankAccountTokenService {

    public interface TokenCreationListener {
        void onTokenCreated(String tokenId);
        void onError(String errorMessage);
    }

    public static void createBankAccountTokenInBackground(String accountNumber, String routingNumber, String accountHolderName, String country, String currency, TokenCreationListener listener) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    // Set your secret API key
                   // Stripe.apiKey = "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";

                    // Create bank account object
                    BankAccount bankAccount = BankAccount.builder()
                            .setAccountNumber(accountNumber)
                            .setCountry(country)
                            .setCurrency(currency)
                            .setRoutingNumber(routingNumber) // Use routing number for the bank
                            .setAccountHolderName(accountHolderName)
                            .build();

                    // Create bank account token request
                    TokenCreateParams tokenParams = TokenCreateParams.builder()
                            .setBankAccount(bankAccount)
                            .build();

                    // Create bank account token
                    Token bankAccountToken = Token.create(tokenParams);

                    // Return bank account token ID
                    return bankAccountToken.getId();
                } catch (StripeException e) {
                    // Handle error
                    Log.e("Stripe Error", "Error creating bank account token: " + e.getMessage());
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String bankAccountToken) {
                if (bankAccountToken != null) {
                    listener.onTokenCreated(bankAccountToken);
                } else {
                    listener.onError("Error creating bank account token");
                }
            }
        }.execute();
    }
}

