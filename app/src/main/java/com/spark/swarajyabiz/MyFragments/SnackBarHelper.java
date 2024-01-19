package com.spark.swarajyabiz.MyFragments;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

public class SnackBarHelper {

    public static void showSnackbar(Activity activity, String message) {
        View parentLayout = findParentLayout(activity);
        if (parentLayout != null) {
            Snackbar.make(parentLayout, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void showSnackbarWithAction(Activity activity, String message, String actionText, View.OnClickListener actionClickListener) {
        View parentLayout = findParentLayout(activity);
        if (parentLayout != null) {
            Snackbar snackbar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG);
            snackbar.setAction(actionText, actionClickListener);
            snackbar.show();
        }
    }

    private static View findParentLayout(Activity activity) {
        // Use the content view of the activity as the parent layout
        View contentView = activity.findViewById(android.R.id.content);
        if (contentView instanceof ViewGroup) {
            return ((ViewGroup) contentView).getChildAt(0);
        } else {
            return null;
        }
    }
}
