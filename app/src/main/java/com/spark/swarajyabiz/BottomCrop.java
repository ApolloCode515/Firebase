package com.spark.swarajyabiz;

import android.graphics.Bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class BottomCrop extends BitmapTransformation {
    public BottomCrop() {
        super();
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int height = toTransform.getHeight();
        int width = toTransform.getWidth();

        int startY = height / 2; // Adjust this value based on how much you want to crop from the bottom

        Bitmap result = Bitmap.createBitmap(toTransform, 0, startY, width, height - startY);

        if (result != toTransform) {
            toTransform.recycle();
        }

        return result;
    }


    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        try {
            messageDigest.update("BottomCrop".getBytes(STRING_CHARSET_NAME));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}