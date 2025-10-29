package com.example.androidintegration;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidintegration.coachmark.CoachMarkSequence;
import com.example.androidintegration.coachmark.Gravity;
import com.example.androidintegration.databinding.ActivityCoachMarkBinding;

public class CoachMarkActivity extends AppCompatActivity {

    private ActivityCoachMarkBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCoachMarkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        CoachMarkSequence coachMarkSequence = new CoachMarkSequence(this);
        
        coachMarkSequence.addItem(
            binding.txvTop,
            getString(R.string.title_top),
            getString(R.string.lorem_ipsum_text),
            "Go Next",
            Color.RED,
            Color.WHITE,
            "Go Skip",
            Color.CYAN,
            Color.BLACK
        );
        
        coachMarkSequence.addItem(
            binding.txvStartTop,
            getString(R.string.title_start_top),
            getString(R.string.lorem_ipsum_text),
            "Go Next",
            Color.RED,
            Color.WHITE,
            "Go Skip",
            Color.CYAN,
            Color.BLACK,
            Gravity.END_BOTTOM
        );
        
        coachMarkSequence.addItem(
            binding.txvEndTop,
            getString(R.string.title_end_top),
            getString(R.string.lorem_ipsum_text),
            "Go Next",
            Color.RED,
            Color.WHITE,
            "Go Skip",
            Color.CYAN,
            Color.BLACK
        );
        
        coachMarkSequence.addItem(
            binding.txvEndBottom,
            getString(R.string.title_end_bottom),
            getString(R.string.lorem_ipsum_text),
            "Go Next",
            Color.RED,
            Color.WHITE,
            "Go Skip",
            Color.CYAN,
            Color.BLACK
        );
        
        coachMarkSequence.addItem(
            binding.txvStartBottom,
            getString(R.string.title_start_bottom),
            getString(R.string.lorem_ipsum_text),
            "Go Next",
            Color.RED,
            Color.WHITE,
            "Go Skip",
            Color.CYAN,
            Color.BLACK
        );
        
        coachMarkSequence.addItem(
            binding.txvBottom,
            getString(R.string.title_bottom),
            getString(R.string.lorem_ipsum_text),
            getString(R.string.label_btn_explore),
            Color.RED,
            Color.WHITE,
            null
        );
        
        coachMarkSequence.start((ViewGroup) getWindow().getDecorView());
        
        coachMarkSequence.setOnFinishCallback(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(
                    CoachMarkActivity.this,
                    getString(R.string.label_finish),
                    Toast.LENGTH_SHORT
                ).show();
            }
        });
    }
}