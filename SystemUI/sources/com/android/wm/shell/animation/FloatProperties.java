package com.android.wm.shell.animation;

import android.graphics.Rect;
import android.graphics.RectF;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: FloatProperties.kt */
/* loaded from: classes2.dex */
public final class FloatProperties {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    public static final FloatPropertyCompat<Rect> RECT_X = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_X$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(@Nullable Rect rect, float f) {
            if (rect == null) {
                return;
            }
            rect.offsetTo((int) f, rect.top);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(@Nullable Rect rect) {
            Integer valueOf = rect == null ? null : Integer.valueOf(rect.left);
            if (valueOf == null) {
                return -3.4028235E38f;
            }
            return valueOf.intValue();
        }
    };
    @NotNull
    public static final FloatPropertyCompat<Rect> RECT_Y = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_Y$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(@Nullable Rect rect, float f) {
            if (rect == null) {
                return;
            }
            rect.offsetTo(rect.left, (int) f);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(@Nullable Rect rect) {
            Integer valueOf = rect == null ? null : Integer.valueOf(rect.top);
            if (valueOf == null) {
                return -3.4028235E38f;
            }
            return valueOf.intValue();
        }
    };
    @NotNull
    public static final FloatPropertyCompat<Rect> RECT_WIDTH = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_WIDTH$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(@NotNull Rect rect) {
            Intrinsics.checkNotNullParameter(rect, "rect");
            return rect.width();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(@NotNull Rect rect, float f) {
            Intrinsics.checkNotNullParameter(rect, "rect");
            rect.right = rect.left + ((int) f);
        }
    };
    @NotNull
    public static final FloatPropertyCompat<Rect> RECT_HEIGHT = new FloatPropertyCompat<Rect>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECT_HEIGHT$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(@NotNull Rect rect) {
            Intrinsics.checkNotNullParameter(rect, "rect");
            return rect.height();
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(@NotNull Rect rect, float f) {
            Intrinsics.checkNotNullParameter(rect, "rect");
            rect.bottom = rect.top + ((int) f);
        }
    };
    @NotNull
    public static final FloatPropertyCompat<RectF> RECTF_X = new FloatPropertyCompat<RectF>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECTF_X$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(@Nullable RectF rectF, float f) {
            if (rectF == null) {
                return;
            }
            rectF.offsetTo(f, rectF.top);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(@Nullable RectF rectF) {
            if (rectF == null) {
                return -3.4028235E38f;
            }
            return rectF.left;
        }
    };
    @NotNull
    public static final FloatPropertyCompat<RectF> RECTF_Y = new FloatPropertyCompat<RectF>() { // from class: com.android.wm.shell.animation.FloatProperties$Companion$RECTF_Y$1
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(@Nullable RectF rectF, float f) {
            if (rectF == null) {
                return;
            }
            rectF.offsetTo(rectF.left, f);
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(@Nullable RectF rectF) {
            if (rectF == null) {
                return -3.4028235E38f;
            }
            return rectF.top;
        }
    };

    /* compiled from: FloatProperties.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
