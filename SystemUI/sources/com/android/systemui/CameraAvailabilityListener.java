package com.android.systemui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraManager;
import android.util.PathParser;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.text.StringsKt;

@Metadata(mo65042d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u000b\u0018\u0000 #2\u00020\u0001:\u0002\"#B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0007\u0012\u0006\u0010\t\u001a\u00020\n¢\u0006\u0002\u0010\u000bJ\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0014J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0007H\u0002J\b\u0010\u001b\u001a\u00020\u0016H\u0002J\b\u0010\u001c\u001a\u00020\u0016H\u0002J\b\u0010\u001d\u001a\u00020\u0016H\u0002J\u000e\u0010\u001e\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0014J\u0006\u0010\u001f\u001a\u00020\u0016J\u0006\u0010 \u001a\u00020\u0016J\b\u0010!\u001a\u00020\u0016H\u0002R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00070\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, mo65043d2 = {"Lcom/android/systemui/CameraAvailabilityListener;", "", "cameraManager", "Landroid/hardware/camera2/CameraManager;", "cutoutProtectionPath", "Landroid/graphics/Path;", "targetCameraId", "", "excludedPackages", "executor", "Ljava/util/concurrent/Executor;", "(Landroid/hardware/camera2/CameraManager;Landroid/graphics/Path;Ljava/lang/String;Ljava/lang/String;Ljava/util/concurrent/Executor;)V", "availabilityCallback", "Landroid/hardware/camera2/CameraManager$AvailabilityCallback;", "cutoutBounds", "Landroid/graphics/Rect;", "excludedPackageIds", "", "listeners", "", "Lcom/android/systemui/CameraAvailabilityListener$CameraTransitionCallback;", "addTransitionCallback", "", "callback", "isExcluded", "", "packageId", "notifyCameraActive", "notifyCameraInactive", "registerCameraListener", "removeTransitionCallback", "startListening", "stop", "unregisterCameraListener", "CameraTransitionCallback", "Factory", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: CameraAvailabilityListener.kt */
public final class CameraAvailabilityListener {
    public static final Factory Factory = new Factory((DefaultConstructorMarker) null);
    private final CameraManager.AvailabilityCallback availabilityCallback = new CameraAvailabilityListener$availabilityCallback$1(this);
    private final CameraManager cameraManager;
    private Rect cutoutBounds = new Rect();
    private final Path cutoutProtectionPath;
    private final Set<String> excludedPackageIds;
    private final Executor executor;
    private final List<CameraTransitionCallback> listeners = new ArrayList();
    /* access modifiers changed from: private */
    public final String targetCameraId;

    @Metadata(mo65042d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H&J\b\u0010\b\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\tÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/CameraAvailabilityListener$CameraTransitionCallback;", "", "onApplyCameraProtection", "", "protectionPath", "Landroid/graphics/Path;", "bounds", "Landroid/graphics/Rect;", "onHideCameraProtection", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: CameraAvailabilityListener.kt */
    public interface CameraTransitionCallback {
        void onApplyCameraProtection(Path path, Rect rect);

        void onHideCameraProtection();
    }

    public CameraAvailabilityListener(CameraManager cameraManager2, Path path, String str, String str2, Executor executor2) {
        Intrinsics.checkNotNullParameter(cameraManager2, "cameraManager");
        Intrinsics.checkNotNullParameter(path, "cutoutProtectionPath");
        Intrinsics.checkNotNullParameter(str, "targetCameraId");
        Intrinsics.checkNotNullParameter(str2, "excludedPackages");
        Intrinsics.checkNotNullParameter(executor2, "executor");
        this.cameraManager = cameraManager2;
        this.cutoutProtectionPath = path;
        this.targetCameraId = str;
        this.executor = executor2;
        RectF rectF = new RectF();
        path.computeBounds(rectF, false);
        this.cutoutBounds.set(MathKt.roundToInt(rectF.left), MathKt.roundToInt(rectF.top), MathKt.roundToInt(rectF.right), MathKt.roundToInt(rectF.bottom));
        this.excludedPackageIds = CollectionsKt.toSet(StringsKt.split$default((CharSequence) str2, new String[]{NavigationBarInflaterView.BUTTON_SEPARATOR}, false, 0, 6, (Object) null));
    }

    public final void startListening() {
        registerCameraListener();
    }

    public final void stop() {
        unregisterCameraListener();
    }

    public final void addTransitionCallback(CameraTransitionCallback cameraTransitionCallback) {
        Intrinsics.checkNotNullParameter(cameraTransitionCallback, "callback");
        this.listeners.add(cameraTransitionCallback);
    }

    public final void removeTransitionCallback(CameraTransitionCallback cameraTransitionCallback) {
        Intrinsics.checkNotNullParameter(cameraTransitionCallback, "callback");
        this.listeners.remove((Object) cameraTransitionCallback);
    }

    /* access modifiers changed from: private */
    public final boolean isExcluded(String str) {
        return this.excludedPackageIds.contains(str);
    }

    private final void registerCameraListener() {
        this.cameraManager.registerAvailabilityCallback(this.executor, this.availabilityCallback);
    }

    private final void unregisterCameraListener() {
        this.cameraManager.unregisterAvailabilityCallback(this.availabilityCallback);
    }

    /* access modifiers changed from: private */
    public final void notifyCameraActive() {
        for (CameraTransitionCallback onApplyCameraProtection : this.listeners) {
            onApplyCameraProtection.onApplyCameraProtection(this.cutoutProtectionPath, this.cutoutBounds);
        }
    }

    /* access modifiers changed from: private */
    public final void notifyCameraInactive() {
        for (CameraTransitionCallback onHideCameraProtection : this.listeners) {
            onHideCameraProtection.onHideCameraProtection();
        }
    }

    @Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bJ\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0002¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/CameraAvailabilityListener$Factory;", "", "()V", "build", "Lcom/android/systemui/CameraAvailabilityListener;", "context", "Landroid/content/Context;", "executor", "Ljava/util/concurrent/Executor;", "pathFromString", "Landroid/graphics/Path;", "pathString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: CameraAvailabilityListener.kt */
    public static final class Factory {
        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Factory() {
        }

        public final CameraAvailabilityListener build(Context context, Executor executor) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(executor, "executor");
            Object systemService = context.getSystemService("camera");
            if (systemService != null) {
                Resources resources = context.getResources();
                String string = resources.getString(C1894R.string.config_frontBuiltInDisplayCutoutProtection);
                String string2 = resources.getString(C1894R.string.config_protectedCameraId);
                String string3 = resources.getString(C1894R.string.config_cameraProtectionExcludedPackages);
                Intrinsics.checkNotNullExpressionValue(string, "pathString");
                Path pathFromString = pathFromString(string);
                Intrinsics.checkNotNullExpressionValue(string2, "cameraId");
                Intrinsics.checkNotNullExpressionValue(string3, "excluded");
                return new CameraAvailabilityListener((CameraManager) systemService, pathFromString, string2, string3, executor);
            }
            throw new NullPointerException("null cannot be cast to non-null type android.hardware.camera2.CameraManager");
        }

        private final Path pathFromString(String str) {
            try {
                Path createPathFromPathData = PathParser.createPathFromPathData(StringsKt.trim((CharSequence) str).toString());
                Intrinsics.checkNotNullExpressionValue(createPathFromPathData, "createPathFromPathData(spec)");
                return createPathFromPathData;
            } catch (Throwable th) {
                throw new IllegalArgumentException("Invalid protection path", th);
            }
        }
    }
}
