package com.android.systemui.demomode;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0005*\u0002\u0006\u0012\b&\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0014\u001a\u00020\u000bH\u0002J\b\u0010\u0015\u001a\u00020\u000bH\u0002J\b\u0010\u0016\u001a\u00020\u0017H&J\b\u0010\u0018\u001a\u00020\u0017H&J\b\u0010\u0019\u001a\u00020\u0017H&J\u0006\u0010\u001a\u001a\u00020\u0017J\u0006\u0010\u001b\u001a\u00020\u0017R\u0010\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0004\n\u0002\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\f\"\u0004\b\r\u0010\u000eR\u001a\u0010\u000f\u001a\u00020\u000bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\f\"\u0004\b\u0010\u0010\u000eR\u0010\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0004\n\u0002\u0010\u0013¨\u0006\u001c"}, mo64987d2 = {"Lcom/android/systemui/demomode/DemoModeAvailabilityTracker;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "allowedObserver", "com/android/systemui/demomode/DemoModeAvailabilityTracker$allowedObserver$1", "Lcom/android/systemui/demomode/DemoModeAvailabilityTracker$allowedObserver$1;", "getContext", "()Landroid/content/Context;", "isDemoModeAvailable", "", "()Z", "setDemoModeAvailable", "(Z)V", "isInDemoMode", "setInDemoMode", "onObserver", "com/android/systemui/demomode/DemoModeAvailabilityTracker$onObserver$1", "Lcom/android/systemui/demomode/DemoModeAvailabilityTracker$onObserver$1;", "checkIsDemoModeAllowed", "checkIsDemoModeOn", "onDemoModeAvailabilityChanged", "", "onDemoModeFinished", "onDemoModeStarted", "startTracking", "stopTracking", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: DemoModeAvailabilityTracker.kt */
public abstract class DemoModeAvailabilityTracker {
    private final DemoModeAvailabilityTracker$allowedObserver$1 allowedObserver = new DemoModeAvailabilityTracker$allowedObserver$1(this, new Handler(Looper.getMainLooper()));
    private final Context context;
    private boolean isDemoModeAvailable = checkIsDemoModeAllowed();
    private boolean isInDemoMode = checkIsDemoModeOn();
    private final DemoModeAvailabilityTracker$onObserver$1 onObserver = new DemoModeAvailabilityTracker$onObserver$1(this, new Handler(Looper.getMainLooper()));

    public abstract void onDemoModeAvailabilityChanged();

    public abstract void onDemoModeFinished();

    public abstract void onDemoModeStarted();

    public DemoModeAvailabilityTracker(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
    }

    public final Context getContext() {
        return this.context;
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

    /* access modifiers changed from: private */
    public final boolean checkIsDemoModeAllowed() {
        return Settings.Global.getInt(this.context.getContentResolver(), "sysui_demo_allowed", 0) != 0;
    }

    /* access modifiers changed from: private */
    public final boolean checkIsDemoModeOn() {
        return Settings.Global.getInt(this.context.getContentResolver(), "sysui_tuner_demo_on", 0) != 0;
    }
}
