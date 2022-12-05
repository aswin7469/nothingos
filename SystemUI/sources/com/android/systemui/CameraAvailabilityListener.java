package com.android.systemui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraManager;
import android.util.PathParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt__MathJVMKt;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: CameraAvailabilityListener.kt */
/* loaded from: classes.dex */
public final class CameraAvailabilityListener {
    @NotNull
    public static final Factory Factory = new Factory(null);
    @NotNull
    private final CameraManager cameraManager;
    @NotNull
    private final Path cutoutProtectionPath;
    @NotNull
    private final Set<String> excludedPackageIds;
    @NotNull
    private final Executor executor;
    @NotNull
    private final String targetCameraId;
    @NotNull
    private Rect cutoutBounds = new Rect();
    @NotNull
    private final List<CameraTransitionCallback> listeners = new ArrayList();
    @NotNull
    private final CameraManager.AvailabilityCallback availabilityCallback = new CameraManager.AvailabilityCallback() { // from class: com.android.systemui.CameraAvailabilityListener$availabilityCallback$1
        public void onCameraClosed(@NotNull String cameraId) {
            String str;
            Intrinsics.checkNotNullParameter(cameraId, "cameraId");
            str = CameraAvailabilityListener.this.targetCameraId;
            if (Intrinsics.areEqual(str, cameraId)) {
                CameraAvailabilityListener.this.notifyCameraInactive();
            }
        }

        public void onCameraOpened(@NotNull String cameraId, @NotNull String packageId) {
            String str;
            boolean isExcluded;
            Intrinsics.checkNotNullParameter(cameraId, "cameraId");
            Intrinsics.checkNotNullParameter(packageId, "packageId");
            str = CameraAvailabilityListener.this.targetCameraId;
            if (Intrinsics.areEqual(str, cameraId)) {
                isExcluded = CameraAvailabilityListener.this.isExcluded(packageId);
                if (isExcluded) {
                    return;
                }
                CameraAvailabilityListener.this.notifyCameraActive();
            }
        }
    };

    /* compiled from: CameraAvailabilityListener.kt */
    /* loaded from: classes.dex */
    public interface CameraTransitionCallback {
        void onApplyCameraProtection(@NotNull Path path, @NotNull Rect rect);

        void onHideCameraProtection();
    }

    public CameraAvailabilityListener(@NotNull CameraManager cameraManager, @NotNull Path cutoutProtectionPath, @NotNull String targetCameraId, @NotNull String excludedPackages, @NotNull Executor executor) {
        int roundToInt;
        int roundToInt2;
        int roundToInt3;
        int roundToInt4;
        List split$default;
        Set<String> set;
        Intrinsics.checkNotNullParameter(cameraManager, "cameraManager");
        Intrinsics.checkNotNullParameter(cutoutProtectionPath, "cutoutProtectionPath");
        Intrinsics.checkNotNullParameter(targetCameraId, "targetCameraId");
        Intrinsics.checkNotNullParameter(excludedPackages, "excludedPackages");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.cameraManager = cameraManager;
        this.cutoutProtectionPath = cutoutProtectionPath;
        this.targetCameraId = targetCameraId;
        this.executor = executor;
        RectF rectF = new RectF();
        cutoutProtectionPath.computeBounds(rectF, false);
        Rect rect = this.cutoutBounds;
        roundToInt = MathKt__MathJVMKt.roundToInt(rectF.left);
        roundToInt2 = MathKt__MathJVMKt.roundToInt(rectF.top);
        roundToInt3 = MathKt__MathJVMKt.roundToInt(rectF.right);
        roundToInt4 = MathKt__MathJVMKt.roundToInt(rectF.bottom);
        rect.set(roundToInt, roundToInt2, roundToInt3, roundToInt4);
        split$default = StringsKt__StringsKt.split$default(excludedPackages, new String[]{","}, false, 0, 6, null);
        set = CollectionsKt___CollectionsKt.toSet(split$default);
        this.excludedPackageIds = set;
    }

    public final void startListening() {
        registerCameraListener();
    }

    public final void addTransitionCallback(@NotNull CameraTransitionCallback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        this.listeners.add(callback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isExcluded(String str) {
        return this.excludedPackageIds.contains(str);
    }

    private final void registerCameraListener() {
        this.cameraManager.registerAvailabilityCallback(this.executor, this.availabilityCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifyCameraActive() {
        for (CameraTransitionCallback cameraTransitionCallback : this.listeners) {
            cameraTransitionCallback.onApplyCameraProtection(this.cutoutProtectionPath, this.cutoutBounds);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void notifyCameraInactive() {
        for (CameraTransitionCallback cameraTransitionCallback : this.listeners) {
            cameraTransitionCallback.onHideCameraProtection();
        }
    }

    /* compiled from: CameraAvailabilityListener.kt */
    /* loaded from: classes.dex */
    public static final class Factory {
        public /* synthetic */ Factory(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Factory() {
        }

        @NotNull
        public final CameraAvailabilityListener build(@NotNull Context context, @NotNull Executor executor) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(executor, "executor");
            Object systemService = context.getSystemService("camera");
            Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.hardware.camera2.CameraManager");
            CameraManager cameraManager = (CameraManager) systemService;
            Resources resources = context.getResources();
            String pathString = resources.getString(R$string.config_frontBuiltInDisplayCutoutProtection);
            String cameraId = resources.getString(R$string.config_protectedCameraId);
            String excluded = resources.getString(R$string.config_cameraProtectionExcludedPackages);
            Intrinsics.checkNotNullExpressionValue(pathString, "pathString");
            Path pathFromString = pathFromString(pathString);
            Intrinsics.checkNotNullExpressionValue(cameraId, "cameraId");
            Intrinsics.checkNotNullExpressionValue(excluded, "excluded");
            return new CameraAvailabilityListener(cameraManager, pathFromString, cameraId, excluded, executor);
        }

        private final Path pathFromString(String str) {
            CharSequence trim;
            Objects.requireNonNull(str, "null cannot be cast to non-null type kotlin.CharSequence");
            trim = StringsKt__StringsKt.trim(str);
            try {
                Path createPathFromPathData = PathParser.createPathFromPathData(trim.toString());
                Intrinsics.checkNotNullExpressionValue(createPathFromPathData, "createPathFromPathData(spec)");
                return createPathFromPathData;
            } catch (Throwable th) {
                throw new IllegalArgumentException("Invalid protection path", th);
            }
        }
    }
}
