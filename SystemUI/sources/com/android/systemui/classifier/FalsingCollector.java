package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;

public interface FalsingCollector {
    void avoidGesture();

    void cleanup();

    boolean isReportingEnabled();

    void onAffordanceSwipingAborted();

    void onAffordanceSwipingStarted(boolean z);

    void onBouncerHidden();

    void onBouncerShown();

    void onCameraHintStarted();

    void onCameraOn();

    void onExpansionFromPulseStopped();

    void onLeftAffordanceHintStarted();

    void onLeftAffordanceOn();

    void onMotionEventComplete();

    void onNotificationActive();

    void onNotificationDismissed();

    void onNotificationDoubleTap(boolean z, float f, float f2);

    void onNotificationStartDismissing();

    void onNotificationStartDraggingDown();

    void onNotificationStopDismissing();

    void onNotificationStopDraggingDown();

    void onQsDown();

    void onScreenOff();

    void onScreenOnFromTouch();

    void onScreenTurningOn();

    void onStartExpandingFromPulse();

    void onSuccessfulUnlock();

    void onTouchEvent(MotionEvent motionEvent);

    void onTrackingStarted(boolean z);

    void onTrackingStopped();

    void onUnlockHintStarted();

    void setNotificationExpanded();

    void setQsExpanded(boolean z);

    void setShowingAod(boolean z);

    boolean shouldEnforceBouncer();

    void updateFalseConfidence(FalsingClassifier.Result result);
}
