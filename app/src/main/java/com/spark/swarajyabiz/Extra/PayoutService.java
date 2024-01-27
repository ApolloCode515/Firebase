package com.spark.swarajyabiz.Extra;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Base64;

import com.spark.swarajyabiz.Extra.PayoutApi;
import com.spark.swarajyabiz.Extra.PayoutRequest;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PayoutService {

    private static final String BASE_URL = "https://sandbox.hatio.tech";
    private static final String API_CLIENT_ID = "f67aebd8-f66a-41a0-86ce-6f8e909b4ab4";
    private static final String API_CLIENT_SECRET = "zJ2POyEso37sLstnvQyWFP1OrgY4YMVO32DDV8E52Kn5I0hRezgxGGVyvuLs0Fhp";

    private final PayoutApi payoutApi;

    public PayoutService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        payoutApi = retrofit.create(PayoutApi.class);
    }

    public void initiatePayout(final PayoutCallback callback) {
        new AsyncTask<Void, Void, Void>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    String authHeader = "Basic " + Base64.encodeToString((API_CLIENT_ID + ":" + API_CLIENT_SECRET).getBytes(), Base64.NO_WRAP);

                    PayoutRequest payoutRequest = new PayoutRequest(
                            "f67aebd8-f66a-41a0-86ce-6f8e909b4ab4",
                            "34506564-1a6b-4982-b19a-6810d7a13480",
                            150,
                            "INR",
                            "neft",
                            "refund",
                            "HATIO test",
                            "7312124807"
                    );

                    Call<Void> call = payoutApi.initiatePayout(authHeader, payoutRequest);
                    Response<Void> response = call.execute();
                    if (response.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onError(response.code(), response.message());
                    }
                } catch (IOException e) {
                    callback.onFailure(e);
                }
                return null;
            }
        }.execute();
    }

    public interface PayoutCallback {
        void onSuccess();

        void onError(int code, String message);

        void onFailure(Throwable throwable);
    }
}
