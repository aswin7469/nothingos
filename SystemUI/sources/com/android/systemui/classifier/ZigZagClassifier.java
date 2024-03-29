package com.android.systemui.classifier;

import android.graphics.Point;
import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;

class ZigZagClassifier extends FalsingClassifier {
    private static final float MAX_X_PRIMARY_DEVIANCE = 0.05f;
    private static final float MAX_X_SECONDARY_DEVIANCE = 0.4f;
    private static final float MAX_Y_PRIMARY_DEVIANCE = 0.15f;
    private static final float MAX_Y_SECONDARY_DEVIANCE = 0.3f;
    private float mLastDevianceX;
    private float mLastDevianceY;
    private float mLastMaxXDeviance;
    private float mLastMaxYDeviance;
    private final float mMaxXPrimaryDeviance;
    private final float mMaxXSecondaryDeviance;
    private final float mMaxYPrimaryDeviance;
    private final float mMaxYSecondaryDeviance;

    @Inject
    ZigZagClassifier(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        this.mMaxXPrimaryDeviance = deviceConfigProxy.getFloat("systemui", "brightline_falsing_zigzag_x_primary_deviance", 0.05f);
        this.mMaxYPrimaryDeviance = deviceConfigProxy.getFloat("systemui", "brightline_falsing_zigzag_y_primary_deviance", 0.15f);
        this.mMaxXSecondaryDeviance = deviceConfigProxy.getFloat("systemui", "brightline_falsing_zigzag_x_secondary_deviance", MAX_X_SECONDARY_DEVIANCE);
        this.mMaxYSecondaryDeviance = deviceConfigProxy.getFloat("systemui", "brightline_falsing_zigzag_y_secondary_deviance", 0.3f);
    }

    /* access modifiers changed from: package-private */
    public FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        List<Point> list;
        float f;
        float f2;
        float f3;
        if (i == 10 || i == 11 || i == 14) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (getRecentMotionEvents().size() < 3) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (isHorizontal()) {
            list = rotateHorizontal();
        } else {
            list = rotateVertical();
        }
        boolean z = true;
        float abs = (float) Math.abs(list.get(0).x - list.get(list.size() - 1).x);
        float abs2 = (float) Math.abs(list.get(0).y - list.get(list.size() - 1).y);
        logDebug("Actual: (" + abs + NavigationBarInflaterView.BUTTON_SEPARATOR + abs2 + NavigationBarInflaterView.KEY_CODE_END);
        float f4 = 0.0f;
        float f5 = 0.0f;
        float f6 = 0.0f;
        float f7 = 0.0f;
        for (Point next : list) {
            if (z) {
                f6 = (float) next.x;
                f7 = (float) next.y;
                z = false;
            } else {
                f4 += Math.abs(((float) next.x) - f6);
                f5 += Math.abs(((float) next.y) - f7);
                f6 = (float) next.x;
                f7 = (float) next.y;
                logDebug("(x, y, runningAbsDx, runningAbsDy) - (" + f6 + ", " + f7 + ", " + f4 + ", " + f5 + NavigationBarInflaterView.KEY_CODE_END);
            }
        }
        float f8 = f4 - abs;
        float f9 = f5 - abs2;
        float xdpi = abs / getXdpi();
        float ydpi = abs2 / getYdpi();
        float sqrt = (float) Math.sqrt((double) ((xdpi * xdpi) + (ydpi * ydpi)));
        if (abs > abs2) {
            f2 = this.mMaxXPrimaryDeviance * sqrt * getXdpi();
            f = this.mMaxYSecondaryDeviance * sqrt;
            f3 = getYdpi();
        } else {
            f2 = this.mMaxXSecondaryDeviance * sqrt * getXdpi();
            f = this.mMaxYPrimaryDeviance * sqrt;
            f3 = getYdpi();
        }
        float f10 = f * f3;
        this.mLastDevianceX = f8;
        this.mLastDevianceY = f9;
        this.mLastMaxXDeviance = f2;
        this.mLastMaxYDeviance = f10;
        logDebug("Straightness Deviance: (" + f8 + NavigationBarInflaterView.BUTTON_SEPARATOR + f9 + ") vs (" + f2 + NavigationBarInflaterView.BUTTON_SEPARATOR + f10 + NavigationBarInflaterView.KEY_CODE_END);
        if (f8 > f2 || f9 > f10) {
            return falsed(0.5d, getReason());
        }
        return FalsingClassifier.Result.passed(0.5d);
    }

    private String getReason() {
        Locale locale = null;
        return String.format((Locale) null, "{devianceX=%f, maxDevianceX=%s, devianceY=%s, maxDevianceY=%s}", Float.valueOf(this.mLastDevianceX), Float.valueOf(this.mLastMaxXDeviance), Float.valueOf(this.mLastDevianceY), Float.valueOf(this.mLastMaxYDeviance));
    }

    private float getAtan2LastPoint() {
        MotionEvent firstMotionEvent = getFirstMotionEvent();
        MotionEvent lastMotionEvent = getLastMotionEvent();
        float x = firstMotionEvent.getX();
        return (float) Math.atan2((double) (lastMotionEvent.getY() - firstMotionEvent.getY()), (double) (lastMotionEvent.getX() - x));
    }

    private List<Point> rotateVertical() {
        double atan2LastPoint = 1.5707963267948966d - ((double) getAtan2LastPoint());
        logDebug("Rotating to vertical by: " + atan2LastPoint);
        return rotateMotionEvents(getRecentMotionEvents(), -atan2LastPoint);
    }

    private List<Point> rotateHorizontal() {
        double atan2LastPoint = (double) getAtan2LastPoint();
        logDebug("Rotating to horizontal by: " + atan2LastPoint);
        return rotateMotionEvents(getRecentMotionEvents(), atan2LastPoint);
    }

    private List<Point> rotateMotionEvents(List<MotionEvent> list, double d) {
        List<MotionEvent> list2 = list;
        ArrayList arrayList = new ArrayList();
        double cos = Math.cos(d);
        double sin = Math.sin(d);
        MotionEvent motionEvent = list2.get(0);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        for (Iterator<MotionEvent> it = list.iterator(); it.hasNext(); it = it) {
            MotionEvent next = it.next();
            double x2 = (double) (next.getX() - x);
            double y2 = (double) (next.getY() - y);
            arrayList.add(new Point((int) ((cos * x2) + (sin * y2) + ((double) x)), (int) (((-sin) * x2) + (y2 * cos) + ((double) y))));
            motionEvent = motionEvent;
        }
        MotionEvent motionEvent2 = motionEvent;
        MotionEvent motionEvent3 = list2.get(list.size() - 1);
        Point point = (Point) arrayList.get(0);
        Point point2 = (Point) arrayList.get(arrayList.size() - 1);
        logDebug("Before: (" + motionEvent2.getX() + NavigationBarInflaterView.BUTTON_SEPARATOR + motionEvent2.getY() + "), (" + motionEvent3.getX() + NavigationBarInflaterView.BUTTON_SEPARATOR + motionEvent3.getY() + NavigationBarInflaterView.KEY_CODE_END);
        logDebug("After: (" + point.x + NavigationBarInflaterView.BUTTON_SEPARATOR + point.y + "), (" + point2.x + NavigationBarInflaterView.BUTTON_SEPARATOR + point2.y + NavigationBarInflaterView.KEY_CODE_END);
        return arrayList;
    }
}
