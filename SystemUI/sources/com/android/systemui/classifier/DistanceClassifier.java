package com.android.systemui.classifier;

import android.view.MotionEvent;
import android.view.VelocityTracker;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

class DistanceClassifier extends FalsingClassifier {
    private static final float HORIZONTAL_FLING_THRESHOLD_DISTANCE_IN = 1.0f;
    private static final float HORIZONTAL_SWIPE_THRESHOLD_DISTANCE_IN = 3.0f;
    private static final float SCREEN_FRACTION_MAX_DISTANCE = 0.8f;
    private static final float VELOCITY_TO_DISTANCE = 30.0f;
    private static final float VERTICAL_FLING_THRESHOLD_DISTANCE_IN = 1.5f;
    private static final float VERTICAL_SWIPE_THRESHOLD_DISTANCE_IN = 3.0f;
    private DistanceVectors mCachedDistance;
    private boolean mDistanceDirty = true;
    private final float mHorizontalFlingThresholdPx;
    private final float mHorizontalSwipeThresholdPx;
    private final float mVelocityToDistanceMultiplier;
    private final float mVerticalFlingThresholdPx;
    private final float mVerticalSwipeThresholdPx;

    @Inject
    DistanceClassifier(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        this.mVelocityToDistanceMultiplier = deviceConfigProxy.getFloat("systemui", "brightline_falsing_distance_velcoity_to_distance", VELOCITY_TO_DISTANCE);
        float f = deviceConfigProxy.getFloat("systemui", "brightline_falsing_distance_horizontal_fling_threshold_in", 1.0f);
        float f2 = deviceConfigProxy.getFloat("systemui", "brightline_falsing_distance_vertical_fling_threshold_in", 1.5f);
        float f3 = deviceConfigProxy.getFloat("systemui", "brightline_falsing_distance_horizontal_swipe_threshold_in", 3.0f);
        float f4 = deviceConfigProxy.getFloat("systemui", "brightline_falsing_distance_horizontal_swipe_threshold_in", 3.0f);
        float f5 = deviceConfigProxy.getFloat("systemui", "brightline_falsing_distance_screen_fraction_max_distance", 0.8f);
        this.mHorizontalFlingThresholdPx = Math.min(((float) getWidthPixels()) * f5, f * getXdpi());
        this.mVerticalFlingThresholdPx = Math.min(((float) getHeightPixels()) * f5, f2 * getYdpi());
        this.mHorizontalSwipeThresholdPx = Math.min(((float) getWidthPixels()) * f5, f3 * getXdpi());
        this.mVerticalSwipeThresholdPx = Math.min(((float) getHeightPixels()) * f5, f4 * getYdpi());
    }

    private DistanceVectors getDistances() {
        if (this.mDistanceDirty) {
            this.mCachedDistance = calculateDistances();
            this.mDistanceDirty = false;
        }
        return this.mCachedDistance;
    }

    private DistanceVectors calculateDistances() {
        List<MotionEvent> recentMotionEvents = getRecentMotionEvents();
        if (recentMotionEvents.size() < 3) {
            logDebug("Only " + recentMotionEvents.size() + " motion events recorded.");
            return new DistanceVectors(0.0f, 0.0f, 0.0f, 0.0f);
        }
        VelocityTracker obtain = VelocityTracker.obtain();
        for (MotionEvent addMovement : recentMotionEvents) {
            obtain.addMovement(addMovement);
        }
        obtain.computeCurrentVelocity(1);
        float xVelocity = obtain.getXVelocity();
        float yVelocity = obtain.getYVelocity();
        obtain.recycle();
        return new DistanceVectors(getLastMotionEvent().getX() - getFirstMotionEvent().getX(), getLastMotionEvent().getY() - getFirstMotionEvent().getY(), xVelocity, yVelocity);
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        this.mDistanceDirty = true;
    }

    /* access modifiers changed from: package-private */
    public FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        if (i == 10 || i == 11 || i == 12 || i == 13 || i == 14 || i == 15) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        return !getPassedFlingThreshold() ? falsed(0.5d, getReason()) : FalsingClassifier.Result.passed(0.5d);
    }

    /* access modifiers changed from: package-private */
    public String getReason() {
        Locale locale = null;
        return String.format((Locale) null, "{distanceVectors=%s, isHorizontal=%s, velocityToDistanceMultiplier=%f, horizontalFlingThreshold=%f, verticalFlingThreshold=%f, horizontalSwipeThreshold=%f, verticalSwipeThreshold=%s}", getDistances(), Boolean.valueOf(isHorizontal()), Float.valueOf(this.mVelocityToDistanceMultiplier), Float.valueOf(this.mHorizontalFlingThresholdPx), Float.valueOf(this.mVerticalFlingThresholdPx), Float.valueOf(this.mHorizontalSwipeThresholdPx), Float.valueOf(this.mVerticalSwipeThresholdPx));
    }

    /* access modifiers changed from: package-private */
    public FalsingClassifier.Result isLongSwipe() {
        boolean passedDistanceThreshold = getPassedDistanceThreshold();
        logDebug("Is longSwipe? " + passedDistanceThreshold);
        return passedDistanceThreshold ? FalsingClassifier.Result.passed(0.5d) : falsed(0.5d, getReason());
    }

    private boolean getPassedDistanceThreshold() {
        DistanceVectors distances = getDistances();
        if (isHorizontal()) {
            logDebug("Horizontal swipe distance: " + Math.abs(distances.mDx));
            logDebug("Threshold: " + this.mHorizontalSwipeThresholdPx);
            if (Math.abs(distances.mDx) >= this.mHorizontalSwipeThresholdPx) {
                return true;
            }
            return false;
        }
        logDebug("Vertical swipe distance: " + Math.abs(distances.mDy));
        logDebug("Threshold: " + this.mVerticalSwipeThresholdPx);
        if (Math.abs(distances.mDy) >= this.mVerticalSwipeThresholdPx) {
            return true;
        }
        return false;
    }

    private boolean getPassedFlingThreshold() {
        DistanceVectors distances = getDistances();
        float access$000 = distances.mDx + (distances.mVx * this.mVelocityToDistanceMultiplier);
        float access$100 = distances.mDy + (distances.mVy * this.mVelocityToDistanceMultiplier);
        if (isHorizontal()) {
            logDebug("Horizontal swipe and fling distance: " + distances.mDx + ", " + (distances.mVx * this.mVelocityToDistanceMultiplier));
            logDebug("Threshold: " + this.mHorizontalFlingThresholdPx);
            if (Math.abs(access$000) >= this.mHorizontalFlingThresholdPx) {
                return true;
            }
            return false;
        }
        logDebug("Vertical swipe and fling distance: " + distances.mDy + ", " + (distances.mVy * this.mVelocityToDistanceMultiplier));
        logDebug("Threshold: " + this.mVerticalFlingThresholdPx);
        if (Math.abs(access$100) >= this.mVerticalFlingThresholdPx) {
            return true;
        }
        return false;
    }

    private class DistanceVectors {
        final float mDx;
        final float mDy;
        /* access modifiers changed from: private */
        public final float mVx;
        /* access modifiers changed from: private */
        public final float mVy;

        DistanceVectors(float f, float f2, float f3, float f4) {
            this.mDx = f;
            this.mDy = f2;
            this.mVx = f3;
            this.mVy = f4;
        }

        public String toString() {
            Locale locale = null;
            return String.format((Locale) null, "{dx=%f, vx=%f, dy=%f, vy=%f}", Float.valueOf(this.mDx), Float.valueOf(this.mVx), Float.valueOf(this.mDy), Float.valueOf(this.mVy));
        }
    }
}
