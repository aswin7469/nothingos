package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.view.Display;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: BiometricOrientationEventListener.kt */
/* loaded from: classes.dex */
public final class BiometricOrientationEventListener implements DisplayManager.DisplayListener {
    @NotNull
    private final Context context;
    @NotNull
    private final DisplayManager displayManager;
    @NotNull
    private final Handler handler;
    private int lastRotation;
    @NotNull
    private final Function0<Unit> onOrientationChanged;

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayAdded(int i) {
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayRemoved(int i) {
    }

    public BiometricOrientationEventListener(@NotNull Context context, @NotNull Function0<Unit> onOrientationChanged, @NotNull DisplayManager displayManager, @NotNull Handler handler) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(onOrientationChanged, "onOrientationChanged");
        Intrinsics.checkNotNullParameter(displayManager, "displayManager");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.context = context;
        this.onOrientationChanged = onOrientationChanged;
        this.displayManager = displayManager;
        this.handler = handler;
        Display display = context.getDisplay();
        this.lastRotation = display == null ? 0 : display.getRotation();
    }

    @Override // android.hardware.display.DisplayManager.DisplayListener
    public void onDisplayChanged(int i) {
        int intValue;
        Display display = this.context.getDisplay();
        Integer valueOf = display == null ? null : Integer.valueOf(display.getRotation());
        if (valueOf == null || this.lastRotation == (intValue = valueOf.intValue())) {
            return;
        }
        this.lastRotation = intValue;
        this.onOrientationChanged.mo1951invoke();
    }

    public final void enable() {
        this.displayManager.registerDisplayListener(this, this.handler);
    }

    public final void disable() {
        this.displayManager.unregisterDisplayListener(this);
    }
}
