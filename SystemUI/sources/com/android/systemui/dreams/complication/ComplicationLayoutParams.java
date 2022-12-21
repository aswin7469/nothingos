package com.android.systemui.dreams.complication;

import android.view.ViewGroup;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ComplicationLayoutParams extends ViewGroup.LayoutParams {
    public static final int DIRECTION_DOWN = 2;
    public static final int DIRECTION_END = 8;
    public static final int DIRECTION_START = 4;
    public static final int DIRECTION_UP = 1;
    private static final int FIRST_POSITION = 1;
    private static final Map<Integer, Integer> INVALID_DIRECTIONS;
    private static final int[] INVALID_POSITIONS = {3, 12};
    private static final int LAST_POSITION = 8;
    public static final int POSITION_BOTTOM = 2;
    public static final int POSITION_END = 8;
    public static final int POSITION_START = 4;
    public static final int POSITION_TOP = 1;
    private final int mDirection;
    private final int mPosition;
    private final boolean mSnapToGuide;
    private final int mWeight;

    @Retention(RetentionPolicy.SOURCE)
    @interface Direction {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface Position {
    }

    static {
        HashMap hashMap = new HashMap();
        INVALID_DIRECTIONS = hashMap;
        hashMap.put(2, 2);
        hashMap.put(1, 1);
        hashMap.put(4, 4);
        hashMap.put(8, 8);
    }

    public ComplicationLayoutParams(int i, int i2, int i3, int i4, int i5) {
        this(i, i2, i3, i4, i5, false);
    }

    public ComplicationLayoutParams(int i, int i2, int i3, int i4, int i5, boolean z) {
        super(i, i2);
        if (validatePosition(i3)) {
            this.mPosition = i3;
            if (validateDirection(i3, i4)) {
                this.mDirection = i4;
                this.mWeight = i5;
                this.mSnapToGuide = z;
                return;
            }
            throw new IllegalArgumentException("invalid direction:" + i4);
        }
        throw new IllegalArgumentException("invalid position:" + i3);
    }

    public ComplicationLayoutParams(ComplicationLayoutParams complicationLayoutParams) {
        super(complicationLayoutParams);
        this.mPosition = complicationLayoutParams.mPosition;
        this.mDirection = complicationLayoutParams.mDirection;
        this.mWeight = complicationLayoutParams.mWeight;
        this.mSnapToGuide = complicationLayoutParams.mSnapToGuide;
    }

    private static boolean validateDirection(int i, int i2) {
        for (int i3 = 1; i3 <= 8; i3 <<= 1) {
            if ((i & i3) == i3) {
                Map<Integer, Integer> map = INVALID_DIRECTIONS;
                if (map.containsKey(Integer.valueOf(i3)) && (map.get(Integer.valueOf(i3)).intValue() & i2) != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void iteratePositions(Consumer<Integer> consumer) {
        for (int i = 1; i <= 8; i <<= 1) {
            if ((this.mPosition & i) == i) {
                consumer.accept(Integer.valueOf(i));
            }
        }
    }

    private static boolean validatePosition(int i) {
        if (i == 0) {
            return false;
        }
        for (int i2 : INVALID_POSITIONS) {
            if ((i & i2) == i2) {
                return false;
            }
        }
        return true;
    }

    public int getDirection() {
        return this.mDirection;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public int getWeight() {
        return this.mWeight;
    }

    public boolean snapsToGuide() {
        return this.mSnapToGuide;
    }
}
