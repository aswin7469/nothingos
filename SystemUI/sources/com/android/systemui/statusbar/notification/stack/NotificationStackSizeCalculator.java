package com.android.systemui.statusbar.notification.stack;

import android.content.res.Resources;
import android.util.Log;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.util.ConvenienceExtensionsKt;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.properties.Delegates;
import kotlin.properties.ReadWriteProperty;
import kotlin.reflect.KProperty;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001:\u0001DB!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ,\u0010\u001f\u001a\u00020\n2\u0006\u0010 \u001a\u00020\u001c2\b\u0010!\u001a\u0004\u0018\u00010\u001b2\b\u0010\"\u001a\u0004\u0018\u00010\u001b2\u0006\u0010#\u001a\u00020\u0012H\u0002J \u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\n2\u0006\u0010)\u001a\u00020\nH\u0002J\u001e\u0010*\u001a\u00020\n2\u0006\u0010 \u001a\u00020\u001c2\u0006\u0010+\u001a\u00020\u00122\u0006\u0010,\u001a\u00020\nJ\u001e\u0010-\u001a\b\u0012\u0004\u0012\u00020'0\u001a2\u0006\u0010 \u001a\u00020\u001c2\u0006\u0010.\u001a\u00020\nH\u0002J&\u0010/\u001a\u00020\u00122\u0006\u0010 \u001a\u00020\u001c2\u0006\u0010(\u001a\u00020\n2\u0006\u0010)\u001a\u00020\n2\u0006\u0010,\u001a\u00020\nJ\u0010\u00100\u001a\u00020\u00122\u0006\u00101\u001a\u00020\u0012H\u0002J\u0017\u00102\u001a\u0002032\f\u00104\u001a\b\u0012\u0004\u0012\u00020605H\bJ\b\u00107\u001a\u00020%H\u0007J2\u00108\u001a\u00020\n2\u0006\u00109\u001a\u00020\u001b2\u0006\u0010:\u001a\u00020\u00122\b\u0010;\u001a\u0004\u0018\u00010\u001b2\u0006\u0010 \u001a\u00020\u001c2\u0006\u00107\u001a\u00020%H\u0007J\u0006\u0010<\u001a\u000203J\f\u0010=\u001a\u00020%*\u00020\u001bH\u0002J\u0014\u0010>\u001a\u00020%*\u00020\u001b2\u0006\u00107\u001a\u00020%H\u0002J,\u0010?\u001a\u00020\u0012\"\u0004\b\u0000\u0010@*\b\u0012\u0004\u0012\u0002H@0\u001a2\u0012\u0010A\u001a\u000e\u0012\u0004\u0012\u0002H@\u0012\u0004\u0012\u00020%0BH\u0002J\u0012\u0010C\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a*\u00020\u001cH\u0002R+\u0010\u000b\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\n8B@BX\u0002¢\u0006\u0012\n\u0004\b\u0010\u0010\u0011\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R+\u0010\u0013\u001a\u00020\u00122\u0006\u0010\t\u001a\u00020\u00128B@BX\u0002¢\u0006\u0012\n\u0004\b\u0018\u0010\u0011\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a*\u00020\u001c8BX\u0004¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u001e¨\u0006E"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationStackSizeCalculator;", "", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "lockscreenShadeTransitionController", "Lcom/android/systemui/statusbar/LockscreenShadeTransitionController;", "resources", "Landroid/content/res/Resources;", "(Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/statusbar/LockscreenShadeTransitionController;Landroid/content/res/Resources;)V", "<set-?>", "", "dividerHeight", "getDividerHeight", "()F", "setDividerHeight", "(F)V", "dividerHeight$delegate", "Lkotlin/properties/ReadWriteProperty;", "", "maxKeyguardNotifications", "getMaxKeyguardNotifications", "()I", "setMaxKeyguardNotifications", "(I)V", "maxKeyguardNotifications$delegate", "childrenSequence", "Lkotlin/sequences/Sequence;", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayout;", "getChildrenSequence", "(Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayout;)Lkotlin/sequences/Sequence;", "calculateGapAndDividerHeight", "stack", "previous", "current", "currentIndex", "canStackFitInSpace", "", "stackHeight", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackSizeCalculator$StackHeight;", "spaceForNotifications", "spaceForShelf", "computeHeight", "maxNotifications", "shelfIntrinsicHeight", "computeHeightPerNotificationLimit", "shelfHeight", "computeMaxKeyguardNotifications", "infiniteIfNegative", "v", "log", "", "s", "Lkotlin/Function0;", "", "onLockscreen", "spaceNeeded", "view", "visibleIndex", "previousView", "updateResources", "canShowViewOnLockscreen", "isShowable", "lastIndexWhile", "T", "predicate", "Lkotlin/Function1;", "showableChildren", "StackHeight", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationStackSizeCalculator.kt */
public final class NotificationStackSizeCalculator {
    static final /* synthetic */ KProperty<Object>[] $$delegatedProperties;
    private final ReadWriteProperty dividerHeight$delegate = Delegates.INSTANCE.notNull();
    private final LockscreenShadeTransitionController lockscreenShadeTransitionController;
    private final ReadWriteProperty maxKeyguardNotifications$delegate = Delegates.INSTANCE.notNull();
    private final Resources resources;
    private final SysuiStatusBarStateController statusBarStateController;

    private final int infiniteIfNegative(int i) {
        if (i < 0) {
            return Integer.MAX_VALUE;
        }
        return i;
    }

    @Inject
    public NotificationStackSizeCalculator(SysuiStatusBarStateController sysuiStatusBarStateController, LockscreenShadeTransitionController lockscreenShadeTransitionController2, @Main Resources resources2) {
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController2, "lockscreenShadeTransitionController");
        Intrinsics.checkNotNullParameter(resources2, "resources");
        this.statusBarStateController = sysuiStatusBarStateController;
        this.lockscreenShadeTransitionController = lockscreenShadeTransitionController2;
        this.resources = resources2;
        updateResources();
    }

    static {
        Class<NotificationStackSizeCalculator> cls = NotificationStackSizeCalculator.class;
        $$delegatedProperties = new KProperty[]{Reflection.mutableProperty1(new MutablePropertyReference1Impl(cls, "maxKeyguardNotifications", "getMaxKeyguardNotifications()I", 0)), Reflection.mutableProperty1(new MutablePropertyReference1Impl(cls, "dividerHeight", "getDividerHeight()F", 0))};
    }

    private final int getMaxKeyguardNotifications() {
        return ((Number) this.maxKeyguardNotifications$delegate.getValue(this, $$delegatedProperties[0])).intValue();
    }

    private final void setMaxKeyguardNotifications(int i) {
        this.maxKeyguardNotifications$delegate.setValue(this, $$delegatedProperties[0], Integer.valueOf(i));
    }

    private final float getDividerHeight() {
        return ((Number) this.dividerHeight$delegate.getValue(this, $$delegatedProperties[1])).floatValue();
    }

    private final void setDividerHeight(float f) {
        this.dividerHeight$delegate.setValue(this, $$delegatedProperties[1], Float.valueOf(f));
    }

    /* access modifiers changed from: private */
    public final boolean canStackFitInSpace(StackHeight stackHeight, float f, float f2) {
        float component1 = stackHeight.component1();
        float component2 = stackHeight.component2();
        boolean z = true;
        if (component2 == 0.0f) {
            if (component1 > f) {
                z = false;
            }
            if (NotificationStackSizeCalculatorKt.DEBUG) {
                Log.d("NotifStackSizeCalc", "canStackFitInSpace[" + z + "] = notificationsHeight[" + component1 + "] <= spaceForNotifications[" + f + ']');
            }
        } else {
            if (component1 + component2 > f + f2) {
                z = false;
            }
            if (NotificationStackSizeCalculatorKt.DEBUG) {
                Log.d("NotifStackSizeCalc", "canStackFitInSpace[" + z + "] = (notificationsHeight[" + component1 + "] + shelfHeightWithSpaceBefore[" + component2 + "]) <= (spaceForNotifications[" + f + "]  + spaceForShelf[" + f2 + "])");
            }
        }
        return z;
    }

    @Metadata(mo64986d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\t\u0010\t\u001a\u00020\u0003HÆ\u0003J\t\u0010\n\u001a\u00020\u0003HÆ\u0003J\u001d\u0010\u000b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/stack/NotificationStackSizeCalculator$StackHeight;", "", "notificationsHeight", "", "shelfHeightWithSpaceBefore", "(FF)V", "getNotificationsHeight", "()F", "getShelfHeightWithSpaceBefore", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NotificationStackSizeCalculator.kt */
    private static final class StackHeight {
        private final float notificationsHeight;
        private final float shelfHeightWithSpaceBefore;

        public static /* synthetic */ StackHeight copy$default(StackHeight stackHeight, float f, float f2, int i, Object obj) {
            if ((i & 1) != 0) {
                f = stackHeight.notificationsHeight;
            }
            if ((i & 2) != 0) {
                f2 = stackHeight.shelfHeightWithSpaceBefore;
            }
            return stackHeight.copy(f, f2);
        }

        public final float component1() {
            return this.notificationsHeight;
        }

        public final float component2() {
            return this.shelfHeightWithSpaceBefore;
        }

        public final StackHeight copy(float f, float f2) {
            return new StackHeight(f, f2);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof StackHeight)) {
                return false;
            }
            StackHeight stackHeight = (StackHeight) obj;
            return Intrinsics.areEqual((Object) Float.valueOf(this.notificationsHeight), (Object) Float.valueOf(stackHeight.notificationsHeight)) && Intrinsics.areEqual((Object) Float.valueOf(this.shelfHeightWithSpaceBefore), (Object) Float.valueOf(stackHeight.shelfHeightWithSpaceBefore));
        }

        public int hashCode() {
            return (Float.hashCode(this.notificationsHeight) * 31) + Float.hashCode(this.shelfHeightWithSpaceBefore);
        }

        public String toString() {
            return "StackHeight(notificationsHeight=" + this.notificationsHeight + ", shelfHeightWithSpaceBefore=" + this.shelfHeightWithSpaceBefore + ')';
        }

        public StackHeight(float f, float f2) {
            this.notificationsHeight = f;
            this.shelfHeightWithSpaceBefore = f2;
        }

        public final float getNotificationsHeight() {
            return this.notificationsHeight;
        }

        public final float getShelfHeightWithSpaceBefore() {
            return this.shelfHeightWithSpaceBefore;
        }
    }

    private final Sequence<StackHeight> computeHeightPerNotificationLimit(NotificationStackScrollLayout notificationStackScrollLayout, float f) {
        return SequencesKt.sequence(new C2834xf807607a(this, notificationStackScrollLayout, f, (Continuation<? super C2834xf807607a>) null));
    }

    public final void updateResources() {
        setMaxKeyguardNotifications(infiniteIfNegative(this.resources.getInteger(C1893R.integer.keyguard_max_notification_count)));
        setDividerHeight(Math.max(1.0f, (float) this.resources.getDimensionPixelSize(C1893R.dimen.notification_divider_height)));
    }

    private final Sequence<ExpandableView> getChildrenSequence(NotificationStackScrollLayout notificationStackScrollLayout) {
        return SequencesKt.map(ConvenienceExtensionsKt.getChildren(notificationStackScrollLayout), NotificationStackSizeCalculator$childrenSequence$1.INSTANCE);
    }

    public final boolean onLockscreen() {
        if (this.statusBarStateController.getState() != 1) {
            return false;
        }
        if (this.lockscreenShadeTransitionController.getFractionToShade() == 0.0f) {
            return true;
        }
        return false;
    }

    public final float spaceNeeded(ExpandableView expandableView, int i, ExpandableView expandableView2, NotificationStackScrollLayout notificationStackScrollLayout, boolean z) {
        int i2;
        Intrinsics.checkNotNullParameter(expandableView, "view");
        Intrinsics.checkNotNullParameter(notificationStackScrollLayout, "stack");
        isShowable(expandableView, z);
        if (z) {
            i2 = expandableView.getMinHeight(true);
        } else {
            i2 = expandableView.getIntrinsicHeight();
        }
        return ((float) i2) + calculateGapAndDividerHeight(notificationStackScrollLayout, expandableView2, expandableView, i);
    }

    /* access modifiers changed from: private */
    public final boolean isShowable(ExpandableView expandableView, boolean z) {
        if (expandableView.getVisibility() == 8 || expandableView.hasNoContentHeight()) {
            return false;
        }
        if (!z) {
            return true;
        }
        if (expandableView instanceof ExpandableNotificationRow) {
            if (!canShowViewOnLockscreen(expandableView) || ((ExpandableNotificationRow) expandableView).isRemoved()) {
                return false;
            }
            return true;
        } else if (!(expandableView instanceof MediaContainerView) || ((MediaContainerView) expandableView).getIntrinsicHeight() == 0) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: private */
    public final float calculateGapAndDividerHeight(NotificationStackScrollLayout notificationStackScrollLayout, ExpandableView expandableView, ExpandableView expandableView2, int i) {
        if (i == 0) {
            return 0.0f;
        }
        return notificationStackScrollLayout.calculateGapHeight(expandableView, expandableView2, i) + getDividerHeight();
    }

    /* access modifiers changed from: private */
    public final Sequence<ExpandableView> showableChildren(NotificationStackScrollLayout notificationStackScrollLayout) {
        return SequencesKt.filter(getChildrenSequence(notificationStackScrollLayout), new NotificationStackSizeCalculator$showableChildren$1(this));
    }

    private final boolean canShowViewOnLockscreen(ExpandableView expandableView) {
        if (!expandableView.hasNoContentHeight() && expandableView.getVisibility() != 8) {
            return true;
        }
        return false;
    }

    private final void log(Function0<String> function0) {
        if (NotificationStackSizeCalculatorKt.DEBUG) {
            Log.d("NotifStackSizeCalc", function0.invoke());
        }
    }

    private final <T> int lastIndexWhile(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1) {
        return SequencesKt.count(SequencesKt.takeWhile(sequence, function1)) - 1;
    }

    public final int computeMaxKeyguardNotifications(NotificationStackScrollLayout notificationStackScrollLayout, float f, float f2, float f3) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayout, "stack");
        if (NotificationStackSizeCalculatorKt.DEBUG) {
            Log.d("NotifStackSizeCalc", "\n");
        }
        Sequence<StackHeight> computeHeightPerNotificationLimit = computeHeightPerNotificationLimit(notificationStackScrollLayout, f3);
        int lastIndexWhile = lastIndexWhile(computeHeightPerNotificationLimit, new C2835xf2125ae4(this, f, f2));
        if (onLockscreen()) {
            lastIndexWhile = Math.min(getMaxKeyguardNotifications(), lastIndexWhile);
        }
        int max = Math.max(0, lastIndexWhile);
        if (NotificationStackSizeCalculatorKt.DEBUG) {
            Log.d("NotifStackSizeCalc", "computeMaxKeyguardNotifications( spaceForNotifications=" + f + " spaceForShelf=" + f2 + " shelfHeight=" + f3 + ") -> " + max + (NotificationStackSizeCalculatorKt.SPEW ? " stackHeightSequence=" + SequencesKt.toList(computeHeightPerNotificationLimit) : ""));
        }
        return max;
    }

    public final float computeHeight(NotificationStackScrollLayout notificationStackScrollLayout, int i, float f) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayout, "stack");
        if (NotificationStackSizeCalculatorKt.DEBUG) {
            Log.d("NotifStackSizeCalc", "\n");
        }
        Sequence computeHeightPerNotificationLimit = computeHeightPerNotificationLimit(notificationStackScrollLayout, f);
        StackHeight stackHeight = (StackHeight) SequencesKt.elementAtOrElse(computeHeightPerNotificationLimit, i, new NotificationStackSizeCalculator$computeHeight$2(computeHeightPerNotificationLimit));
        float component1 = stackHeight.component1();
        float component2 = stackHeight.component2();
        if (NotificationStackSizeCalculatorKt.DEBUG) {
            Log.d("NotifStackSizeCalc", "computeHeight(maxNotifications=" + i + ",shelfIntrinsicHeight=" + f + ") -> " + (component1 + component2) + " = (" + component1 + " + " + component2 + ')');
        }
        return component1 + component2;
    }
}
