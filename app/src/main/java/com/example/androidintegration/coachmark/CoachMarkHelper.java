package com.example.androidintegration.coachmark;

import android.graphics.Color;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class CoachMarkHelper {

    private CoachMarkSequence coachMarkSequence;

    public void renderCoachMark(AppCompatActivity context, JSONObject unit, Runnable onComplete) {
        try {
            coachMarkSequence = new CoachMarkSequence(context);
            
            JSONObject customKv = unit.getJSONObject("custom_kv");
            int coachMarkCount = customKv.getInt("nd_coachMarkCount");
            
            for (int i = 1; i <= coachMarkCount; i++) {
                String titleKey = "nd_title" + i;
                String subTitleKey = "nd_subtitle" + i;
                int viewId = context.getResources().getIdentifier(
                    customKv.getString(titleKey + "_id"), 
                    "id", 
                    context.getPackageName()
                );
                boolean isLastItem = (i == coachMarkCount);
                addCoachMarkItem(viewId, titleKey, subTitleKey, isLastItem, context, unit);
            }

            coachMarkSequence.start((ViewGroup) context.getWindow().getDecorView());
            coachMarkSequence.setOnFinishCallback(new Runnable() {
                @Override
                public void run() {
                    onComplete.run();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error - you might want to call onComplete or show an error
            onComplete.run();
        }
    }

    public void addCoachMarkItem(int viewId, String titleKey, String subTitleKey, 
                                boolean isLastItem, AppCompatActivity context, JSONObject unit) {
        try {
            JSONObject customKv = unit.getJSONObject("custom_kv");
            
            String skipButtonText = null;
            int skipButtonBGColor = Color.TRANSPARENT;
            int skipButtonTextColor = Color.TRANSPARENT;
            
            if (!isLastItem) {
                skipButtonText = customKv.getString("nd_skipButtonText");
                skipButtonBGColor = Color.parseColor(customKv.getString("nd_skipBtnBackgroundColor"));
                skipButtonTextColor = Color.parseColor(customKv.getString("nd_skipBtnTextColor"));
            }
            
            coachMarkSequence.addItem(
                context.findViewById(viewId),
                customKv.getString(titleKey),
                customKv.getString(subTitleKey),
                isLastItem ? 
                    customKv.getString("nd_finalPositiveButtonText") : 
                    customKv.getString("nd_positiveButtonText"),
                Color.parseColor(customKv.getString("nd_postiveBtnTextColor")),
                Color.parseColor(customKv.getString("nd_postiveBtnBackgroundColor")),
                skipButtonText,
                skipButtonBGColor,
                skipButtonTextColor
            );
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle JSON parsing error - you might want to log or handle this appropriately
        }
    }

    // Overloaded method for backward compatibility
    public void addCoachMarkItem(int viewId, String titleKey, String subTitleKey, 
                                AppCompatActivity context, JSONObject unit) {
        addCoachMarkItem(viewId, titleKey, subTitleKey, false, context, unit);
    }
}