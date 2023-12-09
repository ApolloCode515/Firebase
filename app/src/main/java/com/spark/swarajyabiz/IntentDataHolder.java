package com.spark.swarajyabiz;

import android.content.Intent;

public class IntentDataHolder {
    private static Intent sharedIntent;

    public static Intent getSharedIntent() {
        return sharedIntent;
    }

    public static void setSharedIntent(Intent intent) {
        sharedIntent = intent;
    }
}

