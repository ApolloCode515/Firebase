package com.spark.swarajyabiz.Extra;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PayoutApi {

    @POST("payouts")
    Call<Void> initiatePayout(
            @Header("Authorization") String authHeader,
            @Body PayoutRequest payoutRequest
    );
}
