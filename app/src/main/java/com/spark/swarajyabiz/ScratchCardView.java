package com.spark.swarajyabiz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public class ScratchCardView extends View {

    private Bitmap bitmap;
    private Canvas canvas;
    private Path path;
    private Paint paint;
    private float lastTouchX, lastTouchY;
    private RevealListener revealListener;

    public interface RevealListener {
        void onRevealed();
    }

    public ScratchCardView(Context context) {
        super(context);
        init();
    }

    public ScratchCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        path = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0x00000000); // Set the scratch color to transparent
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(40); // Set the scratch stroke width

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        this.canvas = new Canvas(this.bitmap);
        invalidate();
    }

    public void setScratchImageUrl(String imageUrl) {
        Glide.with(getContext())
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        // Scale the bitmap to match the view size
                        int viewWidth = getWidth();
                        int viewHeight = getHeight();
                        if (viewWidth <= 0 || viewHeight <= 0) {
                            // Handle the case where view dimensions are invalid
                            Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cpn11);
                            setBitmap(defaultBitmap);
                            invalidate();
                        }else {
                            // Scale the bitmap to match the view size
                            Bitmap scratchBitmap = Bitmap.createScaledBitmap(resource, viewWidth, viewHeight, true);
                            //Bitmap scratchBitmap = Bitmap.createScaledBitmap(resource, getWidth(), getHeight(), true);
                            //  scratchCanvas = new Canvas(scratchBitmap);
                            setBitmap(scratchBitmap);
                            invalidate();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void setRevealListener(RevealListener revealListener) {
        this.revealListener = revealListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.drawPath(path, paint);
        }

//        canvas.drawBitmap(bitmap, 0, 0, null);
//        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                path.moveTo(touchX, touchY);
                lastTouchX = touchX;
                lastTouchY = touchY;
                break;
            case MotionEvent.ACTION_MOVE:
                path.quadTo(lastTouchX, lastTouchY, (touchX + lastTouchX) / 2, (touchY + lastTouchY) / 2);
                lastTouchX = touchX;
                lastTouchY = touchY;
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(touchX, touchY);
                checkReveal();
                break;
        }

        scratch();
        invalidate();
        return true;
    }

    private void scratch() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawPath(path, paint);
        paint.setXfermode(null);
    }

    private void checkReveal() {
        float revealPercent = calculateRevealPercent();
        if (revealListener != null && revealPercent >= 0.5) {
            revealListener.onRevealed();
        }
    }

    private float calculateRevealPercent() {
        float pathLength = calculatePathLength();
        float totalArea = getWidth() * getHeight();
        float scratchedArea = pathLength * paint.getStrokeWidth();

        return scratchedArea / totalArea;
    }

    private float calculatePathLength() {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float pathLength = 0;

        float[] point = new float[2];
        float[] previousPoint = new float[2];

        for (float t = 0; t <= 1; t += 0.01) {
            pathMeasure.getPosTan(pathMeasure.getLength() * t, point, null);

            if (t > 0) {
                pathLength += Math.sqrt(
                        Math.pow(point[0] - previousPoint[0], 2) +
                                Math.pow(point[1] - previousPoint[1], 2)
                );
            }

            System.arraycopy(point, 0, previousPoint, 0, 2);
        }

        return pathLength;
    }
}
