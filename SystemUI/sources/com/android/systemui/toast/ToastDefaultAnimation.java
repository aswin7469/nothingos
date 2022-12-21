package com.android.systemui.toast;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import androidx.constraintlayout.motion.widget.Key;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, mo64987d2 = {"Lcom/android/systemui/toast/ToastDefaultAnimation;", "", "()V", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ToastDefaultAnimation.kt */
public final class ToastDefaultAnimation {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\b"}, mo64987d2 = {"Lcom/android/systemui/toast/ToastDefaultAnimation$Companion;", "", "()V", "toastIn", "Landroid/animation/AnimatorSet;", "view", "Landroid/view/View;", "toastOut", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ToastDefaultAnimation.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final AnimatorSet toastIn(View view) {
            Intrinsics.checkNotNullParameter(view, "view");
            View findViewById = view.findViewById(C1893R.C1897id.icon);
            View findViewById2 = view.findViewById(C1893R.C1897id.text);
            if (findViewById == null || findViewById2 == null) {
                return null;
            }
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            PathInterpolator pathInterpolator = new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, Key.SCALE_X, new float[]{0.9f, 1.0f});
            TimeInterpolator timeInterpolator = pathInterpolator;
            ofFloat.setInterpolator(timeInterpolator);
            ofFloat.setDuration(333);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, Key.SCALE_Y, new float[]{0.9f, 1.0f});
            ofFloat2.setInterpolator(timeInterpolator);
            ofFloat2.setDuration(333);
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view, Key.ALPHA, new float[]{0.0f, 1.0f});
            TimeInterpolator timeInterpolator2 = linearInterpolator;
            ofFloat3.setInterpolator(timeInterpolator2);
            ofFloat3.setDuration(66);
            findViewById2.setAlpha(0.0f);
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(findViewById2, Key.ALPHA, new float[]{0.0f, 1.0f});
            ofFloat4.setInterpolator(timeInterpolator2);
            ofFloat4.setDuration(283);
            ofFloat4.setStartDelay(50);
            findViewById.setAlpha(0.0f);
            ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(findViewById, Key.ALPHA, new float[]{0.0f, 1.0f});
            ofFloat5.setInterpolator(timeInterpolator2);
            ofFloat5.setDuration(283);
            ofFloat5.setStartDelay(50);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3, ofFloat4, ofFloat5});
            return animatorSet;
        }

        public final AnimatorSet toastOut(View view) {
            Intrinsics.checkNotNullParameter(view, "view");
            View findViewById = view.findViewById(C1893R.C1897id.icon);
            View findViewById2 = view.findViewById(C1893R.C1897id.text);
            if (findViewById == null || findViewById2 == null) {
                return null;
            }
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            PathInterpolator pathInterpolator = new PathInterpolator(0.3f, 0.0f, 1.0f, 1.0f);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, Key.SCALE_X, new float[]{1.0f, 0.9f});
            TimeInterpolator timeInterpolator = pathInterpolator;
            ofFloat.setInterpolator(timeInterpolator);
            ofFloat.setDuration(250);
            ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, Key.SCALE_Y, new float[]{1.0f, 0.9f});
            ofFloat2.setInterpolator(timeInterpolator);
            ofFloat2.setDuration(250);
            ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(view, Key.ELEVATION, new float[]{view.getElevation(), 0.0f});
            TimeInterpolator timeInterpolator2 = linearInterpolator;
            ofFloat3.setInterpolator(timeInterpolator2);
            ofFloat3.setDuration(40);
            ofFloat3.setStartDelay(150);
            ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(view, Key.ALPHA, new float[]{1.0f, 0.0f});
            ofFloat4.setInterpolator(timeInterpolator2);
            ofFloat4.setDuration(100);
            ofFloat4.setStartDelay(150);
            ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(findViewById2, Key.ALPHA, new float[]{1.0f, 0.0f});
            ofFloat5.setInterpolator(timeInterpolator2);
            ofFloat5.setDuration(166);
            ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(findViewById, Key.ALPHA, new float[]{1.0f, 0.0f});
            ofFloat6.setInterpolator(timeInterpolator2);
            ofFloat6.setDuration(166);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3, ofFloat4, ofFloat5, ofFloat6});
            return animatorSet;
        }
    }
}
