package com.android.systemui.statusbar.phone;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Pair;
import android.view.DisplayCutout;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000<\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\u001aP\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\b\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u00052\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0001\u001a(\u0010\u0011\u001a\u00020\u00052\u0006\u0010\u0012\u001a\u00020\u00052\u0006\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000fH\u0007\u001a\u0018\u0010\u0014\u001a\u00020\u00052\u0006\u0010\u0015\u001a\u00020\u00052\u0006\u0010\u0016\u001a\u00020\u0001H\u0002\u001aj\u0010\u0017\u001a\u00020\u00052\b\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u00012\u0006\u0010\u001b\u001a\u00020\u00012\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u00012\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0001H\u0002\u001a,\u0010\u001d\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u00012\u0006\u0010\u0018\u001a\u00020\u00012\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00010 H\u0002\u001a(\u0010!\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020\u00052\u0006\u0010#\u001a\u00020\u00012\u0006\u0010$\u001a\u00020\u0001H\u0002\u001a\f\u0010%\u001a\u00020\u000f*\u00020\u0001H\u0002\u001a\u0014\u0010&\u001a\u00020\u0001*\u00020\u00052\u0006\u0010'\u001a\u00020\u0001H\u0002\u001a\u0014\u0010(\u001a\u00020\u0001*\u00020\u00052\u0006\u0010'\u001a\u00020\u0001H\u0002\u001a\u0014\u0010)\u001a\u00020\u0001*\u00020\u00052\u0006\u0010'\u001a\u00020\u0001H\u0002\u001a\u0014\u0010*\u001a\u00020\u0001*\u00020+2\u0006\u0010'\u001a\u00020\u0001H\u0002\u001a\u0014\u0010*\u001a\u00020\u0001*\u00020\u00052\u0006\u0010'\u001a\u00020\u0001H\u0002\u001a\u0014\u0010,\u001a\u00020-*\u00020+2\u0006\u0010'\u001a\u00020\u0001H\u0002\u001a$\u0010.\u001a\u00020\u000f*\u00020\u00052\u0006\u0010'\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u0001H\u0002\u001a$\u0010/\u001a\u00020\u000f*\u00020\u00052\u0006\u0010'\u001a\u00020\u00012\u0006\u0010\u0019\u001a\u00020\u00012\u0006\u0010\u001a\u001a\u00020\u0001H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003XT¢\u0006\u0002\n\u0000¨\u00060"}, mo65043d2 = {"MAX_CACHE_SIZE", "", "TAG", "", "calculateInsetsForRotationWithRotatedResources", "Landroid/graphics/Rect;", "currentRotation", "targetRotation", "displayCutout", "Landroid/view/DisplayCutout;", "maxBounds", "statusBarHeight", "minLeft", "minRight", "isRtl", "", "dotWidth", "getPrivacyChipBoundingRectForInsets", "contentRect", "chipWidth", "getRotationZeroDisplayBounds", "bounds", "exactRotation", "getStatusBarLeftRight", "sbHeight", "width", "height", "cWidth", "cHeight", "sbRect", "relativeRotation", "displaySize", "Landroid/util/Pair;", "shareShortEdge", "cutoutRect", "currentWidth", "currentHeight", "isHorizontal", "logicalLeft", "rot", "logicalRight", "logicalTop", "logicalWidth", "Landroid/graphics/Point;", "orientToRotZero", "", "touchesLeftEdge", "touchesRightEdge", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: StatusBarContentInsetsProvider.kt */
public final class StatusBarContentInsetsProviderKt {
    private static final int MAX_CACHE_SIZE = 16;
    private static final String TAG = "StatusBarInsetsProvider";

    private static final boolean isHorizontal(int i) {
        return i == 1 || i == 3;
    }

    private static final Rect getRotationZeroDisplayBounds(Rect rect, int i) {
        return (i == 0 || i == 2) ? rect : new Rect(0, 0, rect.bottom, rect.right);
    }

    public static final Rect getPrivacyChipBoundingRectForInsets(Rect rect, int i, int i2, boolean z) {
        Intrinsics.checkNotNullParameter(rect, "contentRect");
        if (z) {
            return new Rect(rect.left - i, rect.top, rect.left + i2, rect.bottom);
        }
        return new Rect(rect.right - i2, rect.top, rect.right + i, rect.bottom);
    }

    public static final Rect calculateInsetsForRotationWithRotatedResources(int i, int i2, DisplayCutout displayCutout, Rect rect, int i3, int i4, int i5, boolean z, int i6) {
        Rect rect2 = rect;
        Intrinsics.checkNotNullParameter(rect2, "maxBounds");
        int i7 = i;
        Rect rotationZeroDisplayBounds = getRotationZeroDisplayBounds(rect2, i);
        return getStatusBarLeftRight(displayCutout, i3, rotationZeroDisplayBounds.right, rotationZeroDisplayBounds.bottom, rect.width(), rect.height(), i4, i5, z, i6, i2, i);
    }

    private static final Rect getStatusBarLeftRight(DisplayCutout displayCutout, int i, int i2, int i3, int i4, int i5, int i6, int i7, boolean z, int i8, int i9, int i10) {
        if (isHorizontal(i9)) {
            i2 = i3;
        }
        List<Rect> boundingRects = displayCutout != null ? displayCutout.getBoundingRects() : null;
        if (boundingRects == null || boundingRects.isEmpty()) {
            return new Rect(i6, 0, i2 - i7, i);
        }
        int i11 = i10 - i9;
        if (i11 < 0) {
            i11 += 4;
        }
        Rect sbRect = sbRect(i11, i, new Pair(Integer.valueOf(i4), Integer.valueOf(i5)));
        for (Rect next : boundingRects) {
            Intrinsics.checkNotNullExpressionValue(next, "cutoutRect");
            if (shareShortEdge(sbRect, next, i4, i5)) {
                if (touchesLeftEdge(next, i11, i4, i5)) {
                    int logicalWidth = logicalWidth(next, i11);
                    if (z) {
                        logicalWidth += i8;
                    }
                    i6 = Math.max(logicalWidth, i6);
                } else if (touchesRightEdge(next, i11, i4, i5)) {
                    int logicalWidth2 = logicalWidth(next, i11);
                    if (!z) {
                        logicalWidth2 += i8;
                    }
                    i7 = Math.max(i7, logicalWidth2);
                }
            }
        }
        return new Rect(i6, 0, i2 - i7, i);
    }

    private static final Rect sbRect(int i, int i2, Pair<Integer, Integer> pair) {
        Integer num = (Integer) pair.first;
        Integer num2 = (Integer) pair.second;
        if (i == 0) {
            Intrinsics.checkNotNullExpressionValue(num, "w");
            return new Rect(0, 0, num.intValue(), i2);
        } else if (i == 1) {
            Intrinsics.checkNotNullExpressionValue(num2, "h");
            return new Rect(0, 0, i2, num2.intValue());
        } else if (i != 2) {
            int intValue = num.intValue() - i2;
            Intrinsics.checkNotNullExpressionValue(num, "w");
            int intValue2 = num.intValue();
            Intrinsics.checkNotNullExpressionValue(num2, "h");
            return new Rect(intValue, 0, intValue2, num2.intValue());
        } else {
            int intValue3 = num2.intValue() - i2;
            Intrinsics.checkNotNullExpressionValue(num, "w");
            int intValue4 = num.intValue();
            Intrinsics.checkNotNullExpressionValue(num2, "h");
            return new Rect(0, intValue3, intValue4, num2.intValue());
        }
    }

    private static final boolean shareShortEdge(Rect rect, Rect rect2, int i, int i2) {
        if (i < i2) {
            return rect.intersects(0, rect2.top, i, rect2.bottom);
        }
        if (i > i2) {
            return rect.intersects(rect2.left, 0, rect2.right, i2);
        }
        return false;
    }

    private static final boolean touchesRightEdge(Rect rect, int i, int i2, int i3) {
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (rect.bottom < i3) {
                        return false;
                    }
                } else if (rect.left > 0) {
                    return false;
                }
            } else if (rect.top > 0) {
                return false;
            }
        } else if (rect.right < i2) {
            return false;
        }
        return true;
    }

    private static final boolean touchesLeftEdge(Rect rect, int i, int i2, int i3) {
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (rect.top > 0) {
                        return false;
                    }
                } else if (rect.right < i2) {
                    return false;
                }
            } else if (rect.bottom < i3) {
                return false;
            }
        } else if (rect.left > 0) {
            return false;
        }
        return true;
    }

    private static final int logicalTop(Rect rect, int i) {
        if (i == 0) {
            return rect.top;
        }
        if (i == 1) {
            return rect.left;
        }
        if (i != 2) {
            return rect.right;
        }
        return rect.bottom;
    }

    private static final int logicalRight(Rect rect, int i) {
        if (i == 0) {
            return rect.right;
        }
        if (i == 1) {
            return rect.top;
        }
        if (i != 2) {
            return rect.bottom;
        }
        return rect.left;
    }

    private static final int logicalLeft(Rect rect, int i) {
        if (i == 0) {
            return rect.left;
        }
        if (i == 1) {
            return rect.bottom;
        }
        if (i != 2) {
            return rect.top;
        }
        return rect.right;
    }

    private static final int logicalWidth(Rect rect, int i) {
        if (i == 0 || i == 2) {
            return rect.width();
        }
        return rect.height();
    }

    /* access modifiers changed from: private */
    public static final void orientToRotZero(Point point, int i) {
        if (i != 0 && i != 2) {
            int i2 = point.y;
            point.y = point.x;
            point.x = i2;
        }
    }

    /* access modifiers changed from: private */
    public static final int logicalWidth(Point point, int i) {
        if (i == 0 || i == 2) {
            return point.x;
        }
        return point.y;
    }
}
