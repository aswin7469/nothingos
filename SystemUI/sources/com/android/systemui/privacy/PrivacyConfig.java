package com.android.systemui.privacy;

import android.provider.DeviceConfig;
import android.util.IndentingPrintWriter;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.DumpUtilsKt;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.lang.ref.WeakReference;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.JvmDefault;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\b\b\u0007\u0018\u0000 '2\u00020\u0001:\u0002&'B!\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\fJ\u0016\u0010\u0018\u001a\u00020\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J%\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u001d2\u000e\u0010\u001e\u001a\n\u0012\u0006\b\u0001\u0012\u00020 0\u001fH\u0016¢\u0006\u0002\u0010!J\b\u0010\"\u001a\u00020\u0010H\u0002J\b\u0010#\u001a\u00020\u0010H\u0002J\b\u0010$\u001a\u00020\u0010H\u0002J\u000e\u0010%\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\fJ\u0016\u0010%\u001a\u00020\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002R\u001a\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u001e\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u001e\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u000f\u001a\u00020\u0010@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006("}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyConfig;", "Lcom/android/systemui/Dumpable;", "uiExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "deviceConfigProxy", "Lcom/android/systemui/util/DeviceConfigProxy;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/util/DeviceConfigProxy;Lcom/android/systemui/dump/DumpManager;)V", "callbacks", "", "Ljava/lang/ref/WeakReference;", "Lcom/android/systemui/privacy/PrivacyConfig$Callback;", "devicePropertiesChangedListener", "Landroid/provider/DeviceConfig$OnPropertiesChangedListener;", "<set-?>", "", "locationAvailable", "getLocationAvailable", "()Z", "mediaProjectionAvailable", "getMediaProjectionAvailable", "micCameraAvailable", "getMicCameraAvailable", "addCallback", "", "callback", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "isLocationEnabled", "isMediaProjectionEnabled", "isMicCameraEnabled", "removeCallback", "Callback", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyConfig.kt */
public final class PrivacyConfig implements Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean DEFAULT_LOCATION = false;
    private static final boolean DEFAULT_MEDIA_PROJECTION = true;
    private static final boolean DEFAULT_MIC_CAMERA = true;
    private static final String LOCATION = "location_indicators_enabled";
    private static final String MEDIA_PROJECTION = "media_projection_indicators_enabled";
    private static final String MIC_CAMERA = "camera_mic_icons_enabled";
    public static final String TAG = "PrivacyConfig";
    private final List<WeakReference<Callback>> callbacks = new ArrayList();
    private final DeviceConfigProxy deviceConfigProxy;
    private final DeviceConfig.OnPropertiesChangedListener devicePropertiesChangedListener;
    private boolean locationAvailable = isLocationEnabled();
    private boolean mediaProjectionAvailable = isMediaProjectionEnabled();
    private boolean micCameraAvailable = isMicCameraEnabled();
    private final DelayableExecutor uiExecutor;

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0017ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\bÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyConfig$Callback;", "", "onFlagLocationChanged", "", "flag", "", "onFlagMediaProjectionChanged", "onFlagMicCameraChanged", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyConfig.kt */
    public interface Callback {
        @JvmDefault
        void onFlagLocationChanged(boolean z) {
        }

        @JvmDefault
        void onFlagMediaProjectionChanged(boolean z) {
        }

        @JvmDefault
        void onFlagMicCameraChanged(boolean z) {
        }
    }

    @Inject
    public PrivacyConfig(@Main DelayableExecutor delayableExecutor, DeviceConfigProxy deviceConfigProxy2, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(delayableExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(deviceConfigProxy2, "deviceConfigProxy");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.uiExecutor = delayableExecutor;
        this.deviceConfigProxy = deviceConfigProxy2;
        PrivacyConfig$$ExternalSyntheticLambda0 privacyConfig$$ExternalSyntheticLambda0 = new PrivacyConfig$$ExternalSyntheticLambda0(this);
        this.devicePropertiesChangedListener = privacyConfig$$ExternalSyntheticLambda0;
        dumpManager.registerDumpable(TAG, this);
        deviceConfigProxy2.addOnPropertiesChangedListener("privacy", delayableExecutor, privacyConfig$$ExternalSyntheticLambda0);
    }

    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\bXT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyConfig$Companion;", "", "()V", "DEFAULT_LOCATION", "", "DEFAULT_MEDIA_PROJECTION", "DEFAULT_MIC_CAMERA", "LOCATION", "", "MEDIA_PROJECTION", "MIC_CAMERA", "TAG", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyConfig.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final boolean getMicCameraAvailable() {
        return this.micCameraAvailable;
    }

    public final boolean getLocationAvailable() {
        return this.locationAvailable;
    }

    public final boolean getMediaProjectionAvailable() {
        return this.mediaProjectionAvailable;
    }

    /* access modifiers changed from: private */
    /* renamed from: devicePropertiesChangedListener$lambda-3  reason: not valid java name */
    public static final void m2872devicePropertiesChangedListener$lambda3(PrivacyConfig privacyConfig, DeviceConfig.Properties properties) {
        Intrinsics.checkNotNullParameter(privacyConfig, "this$0");
        if (Intrinsics.areEqual((Object) "privacy", (Object) properties.getNamespace())) {
            if (properties.getKeyset().contains(MIC_CAMERA)) {
                privacyConfig.micCameraAvailable = properties.getBoolean(MIC_CAMERA, true);
                for (WeakReference weakReference : privacyConfig.callbacks) {
                    Callback callback = (Callback) weakReference.get();
                    if (callback != null) {
                        callback.onFlagMicCameraChanged(privacyConfig.micCameraAvailable);
                    }
                }
            }
            if (properties.getKeyset().contains(LOCATION)) {
                privacyConfig.locationAvailable = properties.getBoolean(LOCATION, false);
                for (WeakReference weakReference2 : privacyConfig.callbacks) {
                    Callback callback2 = (Callback) weakReference2.get();
                    if (callback2 != null) {
                        callback2.onFlagLocationChanged(privacyConfig.locationAvailable);
                    }
                }
            }
            if (properties.getKeyset().contains(MEDIA_PROJECTION)) {
                privacyConfig.mediaProjectionAvailable = properties.getBoolean(MEDIA_PROJECTION, true);
                for (WeakReference weakReference3 : privacyConfig.callbacks) {
                    Callback callback3 = (Callback) weakReference3.get();
                    if (callback3 != null) {
                        callback3.onFlagMediaProjectionChanged(privacyConfig.mediaProjectionAvailable);
                    }
                }
            }
        }
    }

    private final boolean isMicCameraEnabled() {
        return this.deviceConfigProxy.getBoolean("privacy", MIC_CAMERA, true);
    }

    private final boolean isLocationEnabled() {
        return this.deviceConfigProxy.getBoolean("privacy", LOCATION, false);
    }

    private final boolean isMediaProjectionEnabled() {
        return this.deviceConfigProxy.getBoolean("privacy", MEDIA_PROJECTION, true);
    }

    public final void addCallback(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        addCallback((WeakReference<Callback>) new WeakReference(callback));
    }

    public final void removeCallback(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        removeCallback((WeakReference<Callback>) new WeakReference(callback));
    }

    private final void addCallback(WeakReference<Callback> weakReference) {
        this.uiExecutor.execute(new PrivacyConfig$$ExternalSyntheticLambda2(this, weakReference));
    }

    /* access modifiers changed from: private */
    /* renamed from: addCallback$lambda-4  reason: not valid java name */
    public static final void m2871addCallback$lambda4(PrivacyConfig privacyConfig, WeakReference weakReference) {
        Intrinsics.checkNotNullParameter(privacyConfig, "this$0");
        Intrinsics.checkNotNullParameter(weakReference, "$callback");
        privacyConfig.callbacks.add(weakReference);
    }

    private final void removeCallback(WeakReference<Callback> weakReference) {
        this.uiExecutor.execute(new PrivacyConfig$$ExternalSyntheticLambda1(this, weakReference));
    }

    /* access modifiers changed from: private */
    /* renamed from: removeCallback$lambda-6  reason: not valid java name */
    public static final void m2873removeCallback$lambda6(PrivacyConfig privacyConfig, WeakReference weakReference) {
        Intrinsics.checkNotNullParameter(privacyConfig, "this$0");
        Intrinsics.checkNotNullParameter(weakReference, "$callback");
        privacyConfig.callbacks.removeIf(new PrivacyConfig$$ExternalSyntheticLambda3(weakReference));
    }

    /* access modifiers changed from: private */
    /* renamed from: removeCallback$lambda-6$lambda-5  reason: not valid java name */
    public static final boolean m2874removeCallback$lambda6$lambda5(WeakReference weakReference, WeakReference weakReference2) {
        Intrinsics.checkNotNullParameter(weakReference, "$callback");
        Intrinsics.checkNotNullParameter(weakReference2, "it");
        Callback callback = (Callback) weakReference2.get();
        if (callback != null) {
            return callback.equals(weakReference.get());
        }
        return true;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        IndentingPrintWriter asIndenting = DumpUtilsKt.asIndenting(printWriter);
        asIndenting.println("PrivacyConfig state:");
        asIndenting.increaseIndent();
        try {
            asIndenting.println("micCameraAvailable: " + this.micCameraAvailable);
            asIndenting.println("locationAvailable: " + this.locationAvailable);
            asIndenting.println("mediaProjectionAvailable: " + this.mediaProjectionAvailable);
            asIndenting.println("Callbacks:");
            asIndenting.increaseIndent();
            for (WeakReference weakReference : this.callbacks) {
                Callback callback = (Callback) weakReference.get();
                if (callback != null) {
                    asIndenting.println(callback);
                }
            }
            asIndenting.decreaseIndent();
            asIndenting.flush();
        } catch (Throwable th) {
            throw th;
        } finally {
            asIndenting.decreaseIndent();
        }
    }
}
