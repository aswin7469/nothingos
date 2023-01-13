package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0000\u0018\u00002\u00020\u0001:\u0001\u0011B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u0004\n\u0002\u0010\f¨\u0006\u0012"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/FoldStateListener;", "Landroid/hardware/devicestate/DeviceStateManager$DeviceStateCallback;", "context", "Landroid/content/Context;", "listener", "Lcom/android/systemui/statusbar/phone/FoldStateListener$OnFoldStateChangeListener;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/phone/FoldStateListener$OnFoldStateChangeListener;)V", "foldedDeviceStates", "", "goToSleepDeviceStates", "wasFolded", "", "Ljava/lang/Boolean;", "onStateChanged", "", "state", "", "OnFoldStateChangeListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FoldStateListener.kt */
public final class FoldStateListener implements DeviceStateManager.DeviceStateCallback {
    private final int[] foldedDeviceStates;
    private final int[] goToSleepDeviceStates;
    private final OnFoldStateChangeListener listener;
    private Boolean wasFolded;

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b`\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0007À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/FoldStateListener$OnFoldStateChangeListener;", "", "onFoldStateChanged", "", "folded", "", "willGoToSleep", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: FoldStateListener.kt */
    public interface OnFoldStateChangeListener {
        void onFoldStateChanged(boolean z, boolean z2);
    }

    public FoldStateListener(Context context, OnFoldStateChangeListener onFoldStateChangeListener) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(onFoldStateChangeListener, "listener");
        this.listener = onFoldStateChangeListener;
        int[] intArray = context.getResources().getIntArray(17236068);
        Intrinsics.checkNotNullExpressionValue(intArray, "context.resources.getInt…onfig_foldedDeviceStates)");
        this.foldedDeviceStates = intArray;
        int[] intArray2 = context.getResources().getIntArray(17236027);
        Intrinsics.checkNotNullExpressionValue(intArray2, "context.resources.getInt…viceStatesOnWhichToSleep)");
        this.goToSleepDeviceStates = intArray2;
    }

    public void onStateChanged(int i) {
        boolean contains = ArraysKt.contains(this.foldedDeviceStates, i);
        if (!Intrinsics.areEqual((Object) this.wasFolded, (Object) Boolean.valueOf(contains))) {
            this.wasFolded = Boolean.valueOf(contains);
            this.listener.onFoldStateChanged(contains, ArraysKt.contains(this.goToSleepDeviceStates, i));
        }
    }
}
