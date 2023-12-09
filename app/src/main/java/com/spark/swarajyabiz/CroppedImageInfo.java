package com.spark.swarajyabiz;

import android.net.Uri;

public class CroppedImageInfo {
    private Uri uri;
    private int position;

    public CroppedImageInfo(Uri uri, int position) {
        this.uri = uri;
        this.position = position;
    }

    public Uri getUri() {
        return uri;
    }

    public int getPosition() {
        return position;
    }
}

