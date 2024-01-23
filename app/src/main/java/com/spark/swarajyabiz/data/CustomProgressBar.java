package com.spark.swarajyabiz.data;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class CustomProgressBar extends View {

    private int progress;
    private int maxProgress;
    private Paint backgroundPaint;
    private Paint progressPaint;
    private Paint textPaint;
    private ObjectAnimator progressAnimator;

    public CustomProgressBar(Context context) {
        super(context);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(android.R.color.darker_gray));
        backgroundPaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint();
        progressPaint.setColor(getResources().getColor(android.R.color.holo_green_light));
        progressPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(android.R.color.black));
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextSize(30); // Set the text size
        textPaint.setTextAlign(Paint.Align.CENTER); // Center-align the text

        progressAnimator = ObjectAnimator.ofInt(this, "progress", 0);
        progressAnimator.setInterpolator(new DecelerateInterpolator());
        progressAnimator.setDuration(1000); // Set the duration of the animation
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw background
        canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);

        // Draw progress
        float progressWidth = ((float) progress / maxProgress) * getWidth();
        canvas.drawRect(0, 0, progressWidth, getHeight(), progressPaint);

        // Draw text
        String progressText = progress + "%";
        RectF bounds = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawText(progressText, bounds.centerX(), bounds.centerY(), textPaint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate(); // Redraw the view
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void startAnimation() {
        progressAnimator.setIntValues(0, progress);
        progressAnimator.start();
    }

    public void stopAnimation() {
        progressAnimator.cancel();
    }
}
