package com.spark.admin;

public interface SMSListener {
    void onSuccess(String message);

    void onError(String message);
}
