package com.spark.swarajyabiz;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class TopCropTransformation extends BitmapTransformation {

    public TopCropTransformation() {
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        if (toTransform.getWidth() == outWidth && toTransform.getHeight() == outHeight) {
            return toTransform;
        }

        float scaleY = (float) outHeight / toTransform.getHeight();

        int newWidth = Math.round(toTransform.getWidth() * scaleY);

        Bitmap result = pool.get(newWidth, outHeight, toTransform.getConfig() != null
                ? toTransform.getConfig() : Bitmap.Config.ARGB_8888);

        float scale = (float) newWidth / toTransform.getWidth();

        float x = 0;
        float y = 0;

        Canvas canvas = new Canvas(result);
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        matrix.postTranslate(-x, -y);
        canvas.drawBitmap(toTransform, matrix, null);

        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        // Not needed
    }
}
