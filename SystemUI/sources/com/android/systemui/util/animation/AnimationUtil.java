package com.android.systemui.util.animation;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.math.MathKt;

@Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, mo65043d2 = {"Lcom/android/systemui/util/animation/AnimationUtil;", "", "()V", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AnimationUtil.kt */
public final class AnimationUtil {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\u0010\b\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\b\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\u0005R\u0015\u0010\u0003\u001a\u00020\u0004*\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/util/animation/AnimationUtil$Companion;", "", "()V", "frames", "", "", "getFrames", "(I)J", "getMsForFrames", "numFrames", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: AnimationUtil.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final long getMsForFrames(int i) {
            if (i >= 0) {
                return MathKt.roundToLong((((float) i) * 1000.0f) / 60.0f);
            }
            throw new IllegalArgumentException("numFrames must be >= 0");
        }

        public final long getFrames(int i) {
            return getMsForFrames(i);
        }
    }
}
