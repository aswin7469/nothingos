package com.android.systemui.classifier;

import android.graphics.Point;
import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
class ZigZagClassifier extends FalsingClassifier {
    private float mLastDevianceX;
    private float mLastDevianceY;
    private float mLastMaxXDeviance;
    private float mLastMaxYDeviance;
    private final float mMaxXPrimaryDeviance;
    private final float mMaxXSecondaryDeviance;
    private final float mMaxYPrimaryDeviance;
    private final float mMaxYSecondaryDeviance;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ZigZagClassifier(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        this.mMaxXPrimaryDeviance = deviceConfigProxy.getFloat("systemui", "brightline_falsing_zigzag_x_primary_deviance", 0.05f);
        this.mMaxYPrimaryDeviance = deviceConfigProxy.getFloat("systemui", "brightline_falsing_zigzag_y_primary_deviance", 0.15f);
        this.mMaxXSecondaryDeviance = deviceConfigProxy.getFloat("systemui", "brightline_falsing_zigzag_x_secondary_deviance", 0.4f);
        this.mMaxYSecondaryDeviance = deviceConfigProxy.getFloat("systemui", "brightline_falsing_zigzag_y_secondary_deviance", 0.3f);
    }

    @Override // com.android.systemui.classifier.FalsingClassifier
    FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        List<Point> rotateVertical;
        float xdpi;
        float f;
        float ydpi;
        if (i == 10 || i == 11 || i == 14) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (getRecentMotionEvents().size() < 3) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (isHorizontal()) {
            rotateVertical = rotateHorizontal();
        } else {
            rotateVertical = rotateVertical();
        }
        boolean z = true;
        float abs = Math.abs(rotateVertical.get(0).x - rotateVertical.get(rotateVertical.size() - 1).x);
        float abs2 = Math.abs(rotateVertical.get(0).y - rotateVertical.get(rotateVertical.size() - 1).y);
        FalsingClassifier.logDebug("Actual: (" + abs + "," + abs2 + ")");
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        for (Point point : rotateVertical) {
            if (z) {
                f4 = point.x;
                f5 = point.y;
                z = false;
            } else {
                f2 += Math.abs(point.x - f4);
                f3 += Math.abs(point.y - f5);
                f4 = point.x;
                f5 = point.y;
                FalsingClassifier.logDebug("(x, y, runningAbsDx, runningAbsDy) - (" + f4 + ", " + f5 + ", " + f2 + ", " + f3 + ")");
            }
        }
        float f6 = f2 - abs;
        float f7 = f3 - abs2;
        float xdpi2 = abs / getXdpi();
        float ydpi2 = abs2 / getYdpi();
        float sqrt = (float) Math.sqrt((xdpi2 * xdpi2) + (ydpi2 * ydpi2));
        if (abs > abs2) {
            xdpi = this.mMaxXPrimaryDeviance * sqrt * getXdpi();
            f = this.mMaxYSecondaryDeviance * sqrt;
            ydpi = getYdpi();
        } else {
            xdpi = this.mMaxXSecondaryDeviance * sqrt * getXdpi();
            f = this.mMaxYPrimaryDeviance * sqrt;
            ydpi = getYdpi();
        }
        float f8 = f * ydpi;
        this.mLastDevianceX = f6;
        this.mLastDevianceY = f7;
        this.mLastMaxXDeviance = xdpi;
        this.mLastMaxYDeviance = f8;
        FalsingClassifier.logDebug("Straightness Deviance: (" + f6 + "," + f7 + ") vs (" + xdpi + "," + f8 + ")");
        return (f6 > xdpi || f7 > f8) ? falsed(0.5d, getReason()) : FalsingClassifier.Result.passed(0.5d);
    }

    private String getReason() {
        return String.format(null, "{devianceX=%f, maxDevianceX=%s, devianceY=%s, maxDevianceY=%s}", Float.valueOf(this.mLastDevianceX), Float.valueOf(this.mLastMaxXDeviance), Float.valueOf(this.mLastDevianceY), Float.valueOf(this.mLastMaxYDeviance));
    }

    private float getAtan2LastPoint() {
        MotionEvent firstMotionEvent = getFirstMotionEvent();
        MotionEvent lastMotionEvent = getLastMotionEvent();
        float x = firstMotionEvent.getX();
        return (float) Math.atan2(lastMotionEvent.getY() - firstMotionEvent.getY(), lastMotionEvent.getX() - x);
    }

    private List<Point> rotateVertical() {
        double atan2LastPoint = 1.5707963267948966d - getAtan2LastPoint();
        FalsingClassifier.logDebug("Rotating to vertical by: " + atan2LastPoint);
        return rotateMotionEvents(getRecentMotionEvents(), -atan2LastPoint);
    }

    private List<Point> rotateHorizontal() {
        double atan2LastPoint = getAtan2LastPoint();
        FalsingClassifier.logDebug("Rotating to horizontal by: " + atan2LastPoint);
        return rotateMotionEvents(getRecentMotionEvents(), atan2LastPoint);
    }

    private List<Point> rotateMotionEvents(List<MotionEvent> list, double d) {
        ArrayList arrayList = new ArrayList();
        double cos = Math.cos(d);
        double sin = Math.sin(d);
        MotionEvent motionEvent = list.get(0);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        for (Iterator<MotionEvent> it = list.iterator(); it.hasNext(); it = it) {
            MotionEvent next = it.next();
            double x2 = next.getX() - x;
            double y2 = next.getY() - y;
            arrayList.add(new Point((int) ((cos * x2) + (sin * y2) + x), (int) (((-sin) * x2) + (y2 * cos) + y)));
            motionEvent = motionEvent;
        }
        MotionEvent motionEvent2 = motionEvent;
        MotionEvent motionEvent3 = list.get(list.size() - 1);
        Point point = (Point) arrayList.get(0);
        Point point2 = (Point) arrayList.get(arrayList.size() - 1);
        FalsingClassifier.logDebug("Before: (" + motionEvent2.getX() + "," + motionEvent2.getY() + "), (" + motionEvent3.getX() + "," + motionEvent3.getY() + ")");
        FalsingClassifier.logDebug("After: (" + point.x + "," + point.y + "), (" + point2.x + "," + point2.y + ")");
        return arrayList;
    }
}
