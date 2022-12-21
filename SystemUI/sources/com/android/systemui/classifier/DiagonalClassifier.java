package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.Locale;
import javax.inject.Inject;

class DiagonalClassifier extends FalsingClassifier {
    private static final float DIAGONAL = 0.7853982f;
    private static final float HORIZONTAL_ANGLE_RANGE = 0.08726646f;
    private static final float NINETY_DEG = 1.5707964f;
    private static final float ONE_HUNDRED_EIGHTY_DEG = 3.1415927f;
    private static final float THREE_HUNDRED_SIXTY_DEG = 6.2831855f;
    private static final float VERTICAL_ANGLE_RANGE = 0.08726646f;
    private final float mHorizontalAngleRange;
    private final float mVerticalAngleRange;

    private float normalizeAngle(float f) {
        return f < 0.0f ? (f % THREE_HUNDRED_SIXTY_DEG) + THREE_HUNDRED_SIXTY_DEG : f > THREE_HUNDRED_SIXTY_DEG ? f % THREE_HUNDRED_SIXTY_DEG : f;
    }

    @Inject
    DiagonalClassifier(FalsingDataProvider falsingDataProvider, DeviceConfigProxy deviceConfigProxy) {
        super(falsingDataProvider);
        this.mHorizontalAngleRange = deviceConfigProxy.getFloat("systemui", "brightline_falsing_diagonal_horizontal_angle_range", 0.08726646f);
        this.mVerticalAngleRange = deviceConfigProxy.getFloat("systemui", "brightline_falsing_diagonal_horizontal_angle_range", 0.08726646f);
    }

    /* access modifiers changed from: package-private */
    public FalsingClassifier.Result calculateFalsingResult(int i, double d, double d2) {
        float angle = getAngle();
        if (angle == Float.MAX_VALUE) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        if (i == 5 || i == 6 || i == 14) {
            return FalsingClassifier.Result.passed(0.0d);
        }
        float f = this.mHorizontalAngleRange;
        float f2 = DIAGONAL - f;
        float f3 = f + DIAGONAL;
        if (isVertical()) {
            float f4 = this.mVerticalAngleRange;
            f2 = DIAGONAL - f4;
            f3 = f4 + DIAGONAL;
        }
        return angleBetween(angle, f2, f3) || angleBetween(angle, f2 + NINETY_DEG, f3 + NINETY_DEG) || angleBetween(angle, f2 - NINETY_DEG, f3 - NINETY_DEG) || angleBetween(angle, f2 + ONE_HUNDRED_EIGHTY_DEG, f3 + ONE_HUNDRED_EIGHTY_DEG) ? falsed(0.5d, getReason()) : FalsingClassifier.Result.passed(0.5d);
    }

    private String getReason() {
        Locale locale = null;
        return String.format((Locale) null, "{angle=%f, vertical=%s}", Float.valueOf(getAngle()), Boolean.valueOf(isVertical()));
    }

    private boolean angleBetween(float f, float f2, float f3) {
        float normalizeAngle = normalizeAngle(f2);
        float normalizeAngle2 = normalizeAngle(f3);
        return normalizeAngle > normalizeAngle2 ? f >= normalizeAngle || f <= normalizeAngle2 : f >= normalizeAngle && f <= normalizeAngle2;
    }
}
