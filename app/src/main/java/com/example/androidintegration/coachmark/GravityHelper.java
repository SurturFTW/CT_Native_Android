package com.example.androidintegration.coachmark;

import android.graphics.Rect;
import android.view.View;
import com.example.androidintegration.R;
import com.example.androidintegration.databinding.CoachmarkitemCoachmarkBinding;

public class GravityHelper {

    public interface GravityListener {
        void gravity(CoachmarkitemCoachmarkBinding view, Rect targetViewSize);
    }

    public static class TopGravity implements GravityListener {
        @Override
        public void gravity(CoachmarkitemCoachmarkBinding view, Rect targetViewSize) {
            view.itemDashed.setVisibility(View.VISIBLE);
            view.itemDashed.setImageResource(R.drawable.img_dashed_coachmark_bottom);
            float middle = (float)(targetViewSize.right - targetViewSize.left) / 2;
            view.itemDashed.setTranslationX(targetViewSize.left + middle - 18);
            view.itemDashed.setTranslationY((float)(targetViewSize.top - 120));
            view.itemRoot.setVisibility(View.VISIBLE);
            view.itemRoot.setTranslationY(view.itemDashed.getTranslationY() - view.itemRoot.getHeight() - 30);
            view.itemRoot.setTranslationX(0f);
        }
    }

    public static class BottomGravity implements GravityListener {
        @Override
        public void gravity(CoachmarkitemCoachmarkBinding view, Rect targetViewSize) {
            view.itemDashed.setVisibility(View.VISIBLE);
            float middle = (float)(targetViewSize.right - targetViewSize.left) / 2;
            view.itemDashed.setTranslationX(targetViewSize.left + middle - 18);
            view.itemDashed.setTranslationY((float)(targetViewSize.bottom + 30));
            view.itemDashed.setImageResource(R.drawable.img_dashed_coachmark_top);
            view.itemRoot.setVisibility(View.VISIBLE);
            view.itemRoot.setTranslationY(view.itemDashed.getTranslationY() + view.itemDashed.getHeight() + 30);
            view.itemRoot.setTranslationX(0f);
        }
    }

    public static class StartTopGravity implements GravityListener {
        @Override
        public void gravity(CoachmarkitemCoachmarkBinding view, Rect targetViewSize) {
            view.itemDashed.setVisibility(View.VISIBLE);
            view.itemDashed.setImageResource(R.drawable.img_dashed_coachmark_end_bottom);
            view.itemDashed.setTranslationX((float)(targetViewSize.left - 120));
            view.itemDashed.setTranslationY((float)((targetViewSize.bottom - view.itemDashed.getBottom()) - (targetViewSize.bottom - targetViewSize.top) / 2));
            view.itemRoot.setVisibility(View.VISIBLE);
            view.itemRoot.setTranslationY(view.itemDashed.getTranslationY() - view.itemRoot.getHeight() - 30);
            view.itemRoot.setTranslationX(0f);
        }
    }

    public static class EndTopGravity implements GravityListener {
        @Override
        public void gravity(CoachmarkitemCoachmarkBinding view, Rect targetViewSize) {
            view.itemDashed.setVisibility(View.VISIBLE);
            view.itemDashed.setImageResource(R.drawable.img_dashed_coachmark_left_bottom);
            view.itemDashed.setTranslationX((float)(targetViewSize.right + 30));
            view.itemDashed.setTranslationY((float)((targetViewSize.bottom - view.itemDashed.getHeight()) - (targetViewSize.bottom - targetViewSize.top) / 2 + 4));
            view.itemRoot.setVisibility(View.VISIBLE);
            view.itemRoot.setTranslationY(view.itemDashed.getTranslationY() - view.itemRoot.getHeight() - 30);
            view.itemRoot.setTranslationX(0f);
        }
    }

    public static class EndBottomGravity implements GravityListener {
        @Override
        public void gravity(CoachmarkitemCoachmarkBinding view, Rect targetViewSize) {
            view.itemDashed.setVisibility(View.VISIBLE);
            view.itemDashed.setImageResource(R.drawable.img_dashed_coachmark_left);
            view.itemDashed.setTranslationX((float)(targetViewSize.right + 30));
            view.itemDashed.setTranslationY((float)(targetViewSize.bottom - (targetViewSize.bottom - targetViewSize.top) / 2));
            view.itemRoot.setVisibility(View.VISIBLE);
            view.itemRoot.setTranslationY(view.itemDashed.getTranslationY() + view.itemDashed.getHeight() + 30);
            view.itemRoot.setTranslationX(0f);
        }
    }

    public static class StartBottomGravity implements GravityListener {
        @Override
        public void gravity(CoachmarkitemCoachmarkBinding view, Rect targetViewSize) {
            view.itemDashed.setVisibility(View.VISIBLE);
            view.itemDashed.setImageResource(R.drawable.img_dashed_coachmark_right);
            view.itemDashed.setTranslationX((float)(targetViewSize.left - 120));
            view.itemDashed.setTranslationY((float)(targetViewSize.bottom - (targetViewSize.bottom - targetViewSize.top) / 2));
            view.itemRoot.setVisibility(View.VISIBLE);
            view.itemRoot.setTranslationY(view.itemDashed.getTranslationY() + view.itemDashed.getHeight() + 30);
            view.itemRoot.setTranslationX(0f);
        }
    }
}