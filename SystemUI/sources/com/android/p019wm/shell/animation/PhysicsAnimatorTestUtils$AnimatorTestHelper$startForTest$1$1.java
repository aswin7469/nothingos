package com.android.p019wm.shell.animation;

import android.util.ArrayMap;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.p019wm.shell.animation.PhysicsAnimator;
import com.android.p019wm.shell.animation.PhysicsAnimatorTestUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J1\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u00002\u001a\u0010\u0005\u001a\u0016\u0012\f\u0012\n\u0012\u0006\b\u0000\u0012\u00028\u00000\u0007\u0012\u0004\u0012\u00020\b0\u0006H\u0016¢\u0006\u0002\u0010\t¨\u0006\n"}, mo65043d2 = {"com/android/wm/shell/animation/PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$1", "Lcom/android/wm/shell/animation/PhysicsAnimator$UpdateListener;", "onAnimationUpdateForProperty", "", "target", "values", "Landroid/util/ArrayMap;", "Landroidx/dynamicanimation/animation/FloatPropertyCompat;", "Lcom/android/wm/shell/animation/PhysicsAnimator$AnimationUpdate;", "(Ljava/lang/Object;Landroid/util/ArrayMap;)V", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.animation.PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$1 */
/* compiled from: PhysicsAnimatorTestUtils.kt */
public final class PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$1 implements PhysicsAnimator.UpdateListener<T> {
    final /* synthetic */ PhysicsAnimatorTestUtils.AnimatorTestHelper<T> this$0;

    PhysicsAnimatorTestUtils$AnimatorTestHelper$startForTest$1$1(PhysicsAnimatorTestUtils.AnimatorTestHelper<T> animatorTestHelper) {
        this.this$0 = animatorTestHelper;
    }

    public void onAnimationUpdateForProperty(T t, ArrayMap<FloatPropertyCompat<? super T>, PhysicsAnimator.AnimationUpdate> arrayMap) {
        Intrinsics.checkNotNullParameter(arrayMap, "values");
        PhysicsAnimatorTestUtils.AnimatorTestHelper<T> animatorTestHelper = this.this$0;
        for (Map.Entry entry : arrayMap.entrySet()) {
            FloatPropertyCompat floatPropertyCompat = (FloatPropertyCompat) entry.getKey();
            PhysicsAnimator.AnimationUpdate animationUpdate = (PhysicsAnimator.AnimationUpdate) entry.getValue();
            Map access$getAllUpdates$p = animatorTestHelper.allUpdates;
            Object obj = access$getAllUpdates$p.get(floatPropertyCompat);
            if (obj == null) {
                obj = new ArrayList();
                access$getAllUpdates$p.put(floatPropertyCompat, obj);
            }
            ((ArrayList) obj).add(animationUpdate);
        }
        Iterator it = this.this$0.testUpdateListeners.iterator();
        while (it.hasNext()) {
            ((PhysicsAnimator.UpdateListener) it.next()).onAnimationUpdateForProperty(t, arrayMap);
        }
    }
}
