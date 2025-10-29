package com.example.androidintegration.coachmark;

public interface SequenceListener {
    default void onNextItem(CoachMarkOverlay coachMark, CoachMarkSequence coachMarkSequence) {
        coachMarkSequence.setNextView();
    }
}