package com.spark.swarajyabiz;

import android.content.Intent;

public class IntentItemDataHolder {
    private static Intent sharedItemIntent;

    public static Intent getSharedItemIntent() {
        return sharedItemIntent;
    }

    public static void setSharedItemIntent(Intent intent) {
        sharedItemIntent = intent;
    }
}
