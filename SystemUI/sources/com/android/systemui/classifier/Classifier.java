package com.android.systemui.classifier;

import android.hardware.SensorEvent;
import android.view.MotionEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class Classifier {
    public static final int BACK_GESTURE = 16;
    public static final int BOUNCER_UNLOCK = 8;
    public static final int BRIGHTNESS_SLIDER = 10;
    public static final int GENERIC = 7;
    public static final int LEFT_AFFORDANCE = 5;
    public static final int LOCK_ICON = 14;
    public static final int NOTIFICATION_DISMISS = 1;
    public static final int NOTIFICATION_DOUBLE_TAP = 3;
    public static final int NOTIFICATION_DRAG_DOWN = 2;
    public static final int PULSE_EXPAND = 9;
    public static final int QS_COLLAPSE = 12;
    public static final int QS_SWIPE = 15;
    public static final int QUICK_SETTINGS = 0;
    public static final int RIGHT_AFFORDANCE = 6;
    public static final int SHADE_DRAG = 11;
    public static final int UDFPS_AUTHENTICATION = 13;
    public static final int UNLOCK = 4;

    @Retention(RetentionPolicy.SOURCE)
    public @interface InteractionType {
    }

    public abstract String getTag();

    public void onSensorChanged(SensorEvent sensorEvent) {
    }

    public void onTouchEvent(MotionEvent motionEvent) {
    }
}
