package com.spark.swarajyabiz;

public interface SMSListener {
    void onSuccess(String message);

    void onError(String message);
}
