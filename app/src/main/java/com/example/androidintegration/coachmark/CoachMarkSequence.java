package com.example.androidintegration.coachmark;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import com.example.androidintegration.R;
import com.example.androidintegration.databinding.CoachmarkitemCoachmarkBinding;
import java.util.*;

public class CoachMarkSequence {

    private Activity mContext;
    private Queue<CoachMarkOverlay.Builder> mSequenceQueue = new LinkedList<>();
    public CoachMarkOverlay mCoachMark;
    private CoachMarkOverlay.Builder mSequenceItem;
    private OnFinishCallback onFinishCallback;

    private SkipClickListener mCoachMarkSkipButtonClickListener = new SkipClickListener() {
        @Override
        public void onSkipClick(View view) {
            ((ViewGroup) mContext.getWindow().getDecorView()).removeView(view);
            mSequenceQueue.clear();
            if (onFinishCallback != null) {
                onFinishCallback.onFinish();
            }
        }
    };

    private SequenceListener mSequenceListener = new SequenceListener() {};

    private OverlayClickListener mCoachMarkOverlayClickListener = new OverlayClickListener() {
        @Override
        public void onOverlayClick(CoachMarkOverlay overlay, CoachmarkitemCoachmarkBinding binding) {
            mCoachMark = overlay;
            if (mSequenceQueue.size() > 0) {
                mSequenceItem = mSequenceQueue.poll();
                if (mSequenceQueue.isEmpty()) {
                    if (overlay.mBuilder != null) {
                        overlay.mBuilder.setOverlayTransparentPadding(0, 0, 0, 0);
                    }
                }
                if (mSequenceItem != null) {
                    if (overlay.mBuilder != null) {
                        CoachMarkOverlay.Builder builder = overlay.mBuilder;
                        builder.setTabPosition(mSequenceItem.getTabPosition());
                        if (builder.getOverlayTargetView() != null) {
                            builder.setInfoText(mSequenceItem.getTitle(), mSequenceItem.getSubTitle(), mSequenceItem.getLimit());
                            builder.setSkipBtn(mSequenceItem.getSkipBtn(), mSequenceItem.getSkipBtnBGColor(), mSequenceItem.getSkipBtnTextColor());
                            builder.setTextBtnPositive(mSequenceItem.getTextBtnPositive(), mSequenceItem.getTextBtnPositiveBGColor(), mSequenceItem.getTextBtnPositiveTextColor());
                            builder.setOverlayTargetView(mSequenceItem.getOverlayTargetView());
                            builder.setGravity(mSequenceItem.getGravity());
                        } else {
                            builder.setInfoText("", "", 0);
                            builder.setSkipBtn("null");
                            builder.setTextBtnPositive("");
                            builder.setOverlayTargetView(null);
                            builder.setGravity(Gravity.NULL);
                            builder.setOverlayTargetCoordinates(mSequenceItem.getOverlayTargetCoordinates());
                        }
                        mSequenceListener.onNextItem(overlay, CoachMarkSequence.this);
                    }
                }
            } else {
                ((ViewGroup) mContext.getWindow().getDecorView()).removeView(overlay);
                mSequenceQueue.clear();
                binding.view.setVisibility(View.GONE);
                if (onFinishCallback != null) {
                    onFinishCallback.onFinish();
                }
            }
        }
    };

    public CoachMarkSequence(Activity context) {
        this.mContext = context;
    }

    // set the data to view the next descriptions.
    public void setNextView() {
        if (mCoachMark != null && mSequenceItem != null) {
            mCoachMark.invalidate();
        }
    }

    public void addItem(View targetView, String title, String subTitle, 
                       String positiveButtonText, int positiveButtonBGColor, int positiveButtonTextColor,
                       String skipButtonText, int skipButtonBGColor, int skipButtonTextColor, 
                       Gravity gravity) {
        CoachMarkOverlay.Builder builder = new CoachMarkOverlay.Builder(mContext);
        builder.setOverlayClickListener(mCoachMarkOverlayClickListener);
        builder.setSkipClickListener(mCoachMarkSkipButtonClickListener);
        builder.setOverlayTargetView(targetView);
        builder.setInfoText(title, subTitle, mSequenceQueue.size());
        builder.setTextBtnPositive(positiveButtonText, positiveButtonBGColor, positiveButtonTextColor);
        builder.setSkipBtn(skipButtonText, skipButtonBGColor, skipButtonTextColor);
        builder.setGravity(gravity);
        mSequenceQueue.add(builder);
    }

    // Overloaded methods for different parameter combinations
    public void addItem(View targetView, String title, String subTitle, 
                       String positiveButtonText, int positiveButtonBGColor, int positiveButtonTextColor,
                       String skipButtonText, int skipButtonBGColor, int skipButtonTextColor) {
        addItem(targetView, title, subTitle, positiveButtonText, positiveButtonBGColor, 
                positiveButtonTextColor, skipButtonText, skipButtonBGColor, skipButtonTextColor, Gravity.NULL);
    }

    public void addItem(View targetView, String title, String subTitle, 
                       String positiveButtonText, int positiveButtonBGColor, int positiveButtonTextColor,
                       String skipButtonText) {
        addItem(targetView, title, subTitle, positiveButtonText, positiveButtonBGColor, 
                positiveButtonTextColor, skipButtonText, 0, 0, Gravity.NULL);
    }

    public void addItem(View targetView, String title, String subTitle) {
        addItem(targetView, title, subTitle, 
                mContext.getString(R.string.coachmarkNext), 0, 0,
                mContext.getString(R.string.coachmarkSkip), 0, 0, Gravity.NULL);
    }

    public void addItem(View targetView, String title, String subTitle, Gravity gravity) {
        addItem(targetView, title, subTitle, 
                mContext.getString(R.string.coachmarkNext), 0, 0,
                mContext.getString(R.string.coachmarkSkip), 0, 0, gravity);
    }

    public void start(ViewGroup rootView) {
        if (mSequenceQueue.size() > 0) {
            CoachMarkOverlay.Builder firstElement = mSequenceQueue.poll();
            if (firstElement != null) {
                if (rootView == null) {
                    firstElement.build(mSequenceQueue.size()).show((ViewGroup) mContext.getWindow().getDecorView());
                } else {
                    firstElement.build(mSequenceQueue.size()).show(rootView);
                }
            }
        }
    }

    public void start() {
        start(null);
    }

    public void setOnFinishCallback(OnFinishCallback onFinishCallback) {
        this.onFinishCallback = onFinishCallback;
    }

    // Interface for backward compatibility with Runnable
    public void setOnFinishCallback(Runnable onFinishCallback) {
        this.onFinishCallback = new OnFinishCallback() {
            @Override
            public void onFinish() {
                onFinishCallback.run();
            }
        };
    }

    public interface OnFinishCallback {
        void onFinish();
    }
}