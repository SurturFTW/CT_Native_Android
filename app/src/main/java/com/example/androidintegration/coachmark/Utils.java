package com.example.androidintegration.coachmark;

import android.content.Context;
import android.util.TypedValue;

public class Utils {

    public static float dpToPx(Context context, int dp) {
        return dpToPx(context, (float) dp);
    }

    private static float dpToPx(Context context, float dipValue) {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 
            dipValue, 
            context.getResources().getDisplayMetrics()
        );
    }
}