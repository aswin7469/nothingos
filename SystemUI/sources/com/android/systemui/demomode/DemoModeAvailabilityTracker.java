package com.android.systemui.demomode;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: DemoModeAvailabilityTracker.kt */
/* loaded from: classes.dex */
public abstract class DemoModeAvailabilityTracker {
    @NotNull
    private final DemoModeAvailabilityTracker$allowedObserver$1 allowedObserver;
    @NotNull
    private final Context context;
    @NotNull
    private final DemoModeAvailabilityTracker$onObserver$1 onObserver;
    private boolean isInDemoMode = checkIsDemoModeOn();
    private boolean isDemoModeAvailable = checkIsDemoModeAllowed();

    public abstract void onDemoModeAvailabilityChanged();

    public abstract void onDemoModeFinished();

    public abstract void onDemoModeStarted();

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.demomode.DemoModeAvailabilityTracker$allowedObserver$1] */
    /* JADX WARN: Type inference failed for: r0v4, types: [com.android.systemui.demomode.DemoModeAvailabilityTracker$onObserver$1] */
    public DemoModeAvailabilityTracker(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        final Handler handler = new Handler(Looper.getMainLooper());
        this.allowedObserver = new ContentObserver(handler) { // from class: com.android.systemui.demomode.DemoModeAvailabilityTracker$allowedObserver$1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean checkIsDemoModeAllowed;
                checkIsDemoModeAllowed = DemoModeAvailabilityTracker.this.checkIsDemoModeAllowed();
                if (DemoModeAvailabilityTracker.this.isDemoModeAvailable() == checkIsDemoModeAllowed) {
                    return;
                }
                DemoModeAvailabilityTracker.this.setDemoModeAvailable(checkIsDemoModeAllowed);
                DemoModeAvailabilityTracker.this.onDemoModeAvailabilityChanged();
            }
        };
        final Handler handler2 = new Handler(Looper.getMainLooper());
        this.onObserver = new ContentObserver(handler2) { // from class: com.android.systemui.demomode.DemoModeAvailabilityTracker$onObserver$1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean checkIsDemoModeOn;
                checkIsDemoModeOn = DemoModeAvailabilityTracker.this.checkIsDemoModeOn();
                if (DemoModeAvailabilityTracker.this.isInDemoMode() == checkIsDemoModeOn) {
                    return;
                }
                DemoModeAvailabilityTracker.this.setInDemoMode(checkIsDemoModeOn);
                if (checkIsDemoModeOn) {
                    DemoModeAvailabilityTracker.this.onDemoModeStarted();
                } else {
                    DemoModeAvailabilityTracker.this.onDemoModeFinished();
                }
            }
        };
    }

    public final boolean isInDemoMode() {
        return this.isInDemoMode;
    }

    public final void setInDemoMode(boolean z) {
        this.isInDemoMode = z;
    }

    public final boolean isDemoModeAvailable() {
        return this.isDemoModeAvailable;
    }

    public final void setDemoModeAvailable(boolean z) {
        this.isDemoModeAvailable = z;
    }

    public final void startTracking() {
        ContentResolver contentResolver = this.context.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("sysui_demo_allowed"), false, this.allowedObserver);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("sysui_tuner_demo_on"), false, this.onObserver);
    }

    public final void stopTracking() {
        ContentResolver contentResolver = this.context.getContentResolver();
        contentResolver.unregisterContentObserver(this.allowedObserver);
        contentResolver.unregisterContentObserver(this.onObserver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean checkIsDemoModeAllowed() {
        return Settings.Global.getInt(this.context.getContentResolver(), "sysui_demo_allowed", 0) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean checkIsDemoModeOn() {
        return Settings.Global.getInt(this.context.getContentResolver(), "sysui_tuner_demo_on", 0) != 0;
    }
}
