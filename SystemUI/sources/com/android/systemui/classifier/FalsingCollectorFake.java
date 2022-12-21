package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;

public class FalsingCollectorFake implements FalsingCollector {
    public void avoidGesture() {
    }

    public void cleanup() {
    }

    public boolean isReportingEnabled() {
        return false;
    }

    public void onAffordanceSwipingAborted() {
    }

    public void onAffordanceSwipingStarted(boolean z) {
    }

    public void onBouncerHidden() {
    }

    public void onBouncerShown() {
    }

    public void onCameraHintStarted() {
    }

    public void onCameraOn() {
    }

    public void onExpansionFromPulseStopped() {
    }

    public void onLeftAffordanceHintStarted() {
    }

    public void onLeftAffordanceOn() {
    }

    public void onMotionEventComplete() {
    }

    public void onNotificationActive() {
    }

    public void onNotificationDismissed() {
    }

    public void onNotificationDoubleTap(boolean z, float f, float f2) {
    }

    public void onNotificationStartDismissing() {
    }

    public void onNotificationStartDraggingDown() {
    }

    public void onNotificationStopDismissing() {
    }

    public void onNotificationStopDraggingDown() {
    }

    public void onQsDown() {
    }

    public void onScreenOff() {
    }

    public void onScreenOnFromTouch() {
    }

    public void onScreenTurningOn() {
    }

    public void onStartExpandingFromPulse() {
    }

    public void onSuccessfulUnlock() {
    }

    public void onTouchEvent(MotionEvent motionEvent) {
    }

    public void onTrackingStarted(boolean z) {
    }

    public void onTrackingStopped() {
    }

    public void onUnlockHintStarted() {
    }

    public void setNotificationExpanded() {
    }

    public void setQsExpanded(boolean z) {
    }

    public void setShowingAod(boolean z) {
    }

    public boolean shouldEnforceBouncer() {
        return false;
    }

    public void updateFalseConfidence(FalsingClassifier.Result result) {
    }
}
