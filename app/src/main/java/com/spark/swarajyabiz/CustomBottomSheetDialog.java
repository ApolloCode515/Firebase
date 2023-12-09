package com.spark.swarajyabiz;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class CustomBottomSheetDialog extends BottomSheetDialog {

    public CustomBottomSheetDialog(Context context) {
        super(context);
    }

    public CustomBottomSheetDialog(Context context, int theme) {
        super(context, theme);
    }

    protected CustomBottomSheetDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

