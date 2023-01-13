package com.android.systemui.biometrics;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Handler;
import android.view.Display;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\u0018\u00002\u00020\u0001:\u0001\u0018B5\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b¢\u0006\u0002\u0010\rJ\b\u0010\u0010\u001a\u00020\u0011H\u0002J\u0006\u0010\u0012\u001a\u00020\fJ\u0006\u0010\u0013\u001a\u00020\fJ\u0010\u0010\u0014\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u000fH\u0016J\u0010\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u000fH\u0016J\u0010\u0010\u0017\u001a\u00020\f2\u0006\u0010\u0015\u001a\u00020\u000fH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/biometrics/BiometricDisplayListener;", "Landroid/hardware/display/DisplayManager$DisplayListener;", "context", "Landroid/content/Context;", "displayManager", "Landroid/hardware/display/DisplayManager;", "handler", "Landroid/os/Handler;", "sensorType", "Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType;", "onChanged", "Lkotlin/Function0;", "", "(Landroid/content/Context;Landroid/hardware/display/DisplayManager;Landroid/os/Handler;Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType;Lkotlin/jvm/functions/Function0;)V", "lastRotation", "", "didRotationChange", "", "disable", "enable", "onDisplayAdded", "displayId", "onDisplayChanged", "onDisplayRemoved", "SensorType", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: BiometricDisplayListener.kt */
public final class BiometricDisplayListener implements DisplayManager.DisplayListener {
    private final Context context;
    private final DisplayManager displayManager;
    private final Handler handler;
    private int lastRotation;
    private final Function0<Unit> onChanged;
    private final SensorType sensorType;

    public void onDisplayAdded(int i) {
    }

    public void onDisplayRemoved(int i) {
    }

    public BiometricDisplayListener(Context context2, DisplayManager displayManager2, Handler handler2, SensorType sensorType2, Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(displayManager2, "displayManager");
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(sensorType2, "sensorType");
        Intrinsics.checkNotNullParameter(function0, "onChanged");
        this.context = context2;
        this.displayManager = displayManager2;
        this.handler = handler2;
        this.sensorType = sensorType2;
        this.onChanged = function0;
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ BiometricDisplayListener(Context context2, DisplayManager displayManager2, Handler handler2, SensorType sensorType2, Function0 function0, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context2, displayManager2, handler2, (i & 8) != 0 ? SensorType.Generic.INSTANCE : sensorType2, function0);
    }

    public void onDisplayChanged(int i) {
        boolean didRotationChange = didRotationChange();
        if (this.sensorType instanceof SensorType.SideFingerprint) {
            this.onChanged.invoke();
        } else if (didRotationChange) {
            this.onChanged.invoke();
        }
    }

    private final boolean didRotationChange() {
        Display display = this.context.getDisplay();
        if (display == null) {
            return false;
        }
        int rotation = display.getRotation();
        int i = this.lastRotation;
        this.lastRotation = rotation;
        if (i != rotation) {
            return true;
        }
        return false;
    }

    public final void enable() {
        Display display = this.context.getDisplay();
        this.lastRotation = display != null ? display.getRotation() : 0;
        this.displayManager.registerDisplayListener(this, this.handler, 4);
    }

    public final void disable() {
        this.displayManager.unregisterDisplayListener(this);
    }

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001:\u0003\u0003\u0004\u0005B\u0007\b\u0004¢\u0006\u0002\u0010\u0002\u0001\u0003\u0006\u0007\b¨\u0006\t"}, mo65043d2 = {"Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType;", "", "()V", "Generic", "SideFingerprint", "UnderDisplayFingerprint", "Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType$Generic;", "Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType$UnderDisplayFingerprint;", "Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType$SideFingerprint;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: BiometricDisplayListener.kt */
    public static abstract class SensorType {
        public /* synthetic */ SensorType(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo65043d2 = {"Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType$Generic;", "Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType;", "()V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
        /* compiled from: BiometricDisplayListener.kt */
        public static final class Generic extends SensorType {
            public static final Generic INSTANCE = new Generic();

            private Generic() {
                super((DefaultConstructorMarker) null);
            }
        }

        private SensorType() {
        }

        @Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo65043d2 = {"Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType$UnderDisplayFingerprint;", "Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType;", "()V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
        /* compiled from: BiometricDisplayListener.kt */
        public static final class UnderDisplayFingerprint extends SensorType {
            public static final UnderDisplayFingerprint INSTANCE = new UnderDisplayFingerprint();

            private UnderDisplayFingerprint() {
                super((DefaultConstructorMarker) null);
            }
        }

        @Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003HÆ\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fHÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType$SideFingerprint;", "Lcom/android/systemui/biometrics/BiometricDisplayListener$SensorType;", "properties", "Landroid/hardware/fingerprint/FingerprintSensorPropertiesInternal;", "(Landroid/hardware/fingerprint/FingerprintSensorPropertiesInternal;)V", "getProperties", "()Landroid/hardware/fingerprint/FingerprintSensorPropertiesInternal;", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
        /* compiled from: BiometricDisplayListener.kt */
        public static final class SideFingerprint extends SensorType {
            private final FingerprintSensorPropertiesInternal properties;

            public static /* synthetic */ SideFingerprint copy$default(SideFingerprint sideFingerprint, FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal, int i, Object obj) {
                if ((i & 1) != 0) {
                    fingerprintSensorPropertiesInternal = sideFingerprint.properties;
                }
                return sideFingerprint.copy(fingerprintSensorPropertiesInternal);
            }

            public final FingerprintSensorPropertiesInternal component1() {
                return this.properties;
            }

            public final SideFingerprint copy(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
                Intrinsics.checkNotNullParameter(fingerprintSensorPropertiesInternal, "properties");
                return new SideFingerprint(fingerprintSensorPropertiesInternal);
            }

            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                return (obj instanceof SideFingerprint) && Intrinsics.areEqual((Object) this.properties, (Object) ((SideFingerprint) obj).properties);
            }

            public int hashCode() {
                return this.properties.hashCode();
            }

            public String toString() {
                return "SideFingerprint(properties=" + this.properties + ')';
            }

            public final FingerprintSensorPropertiesInternal getProperties() {
                return this.properties;
            }

            /* JADX INFO: super call moved to the top of the method (can break code semantics) */
            public SideFingerprint(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
                super((DefaultConstructorMarker) null);
                Intrinsics.checkNotNullParameter(fingerprintSensorPropertiesInternal, "properties");
                this.properties = fingerprintSensorPropertiesInternal;
            }
        }
    }
}
