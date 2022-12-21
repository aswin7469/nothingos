package com.android.systemui.camera;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, mo64987d2 = {"Lcom/android/systemui/camera/CameraIntents;", "", "()V", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: CameraIntents.kt */
public final class CameraIntents {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final String DEFAULT_INSECURE_CAMERA_INTENT_ACTION = "android.media.action.STILL_IMAGE_CAMERA";
    /* access modifiers changed from: private */
    public static final String DEFAULT_SECURE_CAMERA_INTENT_ACTION = "android.media.action.STILL_IMAGE_CAMERA_SECURE";

    @JvmStatic
    public static final Intent getInsecureCameraIntent(Context context) {
        return Companion.getInsecureCameraIntent(context);
    }

    @JvmStatic
    public static final String getOverrideCameraPackage(Context context) {
        return Companion.getOverrideCameraPackage(context);
    }

    @JvmStatic
    public static final Intent getSecureCameraIntent(Context context) {
        return Companion.getSecureCameraIntent(context);
    }

    @JvmStatic
    public static final boolean isInsecureCameraIntent(Intent intent) {
        return Companion.isInsecureCameraIntent(intent);
    }

    @JvmStatic
    public static final boolean isSecureCameraIntent(Intent intent) {
        return Companion.isSecureCameraIntent(intent);
    }

    @Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0012\u0010\r\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0012\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\nH\u0007J\u0012\u0010\u0012\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\nH\u0007R\u0014\u0010\u0003\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006¨\u0006\u0013"}, mo64987d2 = {"Lcom/android/systemui/camera/CameraIntents$Companion;", "", "()V", "DEFAULT_INSECURE_CAMERA_INTENT_ACTION", "", "getDEFAULT_INSECURE_CAMERA_INTENT_ACTION", "()Ljava/lang/String;", "DEFAULT_SECURE_CAMERA_INTENT_ACTION", "getDEFAULT_SECURE_CAMERA_INTENT_ACTION", "getInsecureCameraIntent", "Landroid/content/Intent;", "context", "Landroid/content/Context;", "getOverrideCameraPackage", "getSecureCameraIntent", "isInsecureCameraIntent", "", "intent", "isSecureCameraIntent", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: CameraIntents.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final String getDEFAULT_SECURE_CAMERA_INTENT_ACTION() {
            return CameraIntents.DEFAULT_SECURE_CAMERA_INTENT_ACTION;
        }

        public final String getDEFAULT_INSECURE_CAMERA_INTENT_ACTION() {
            return CameraIntents.DEFAULT_INSECURE_CAMERA_INTENT_ACTION;
        }

        @JvmStatic
        public final String getOverrideCameraPackage(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            String string = context.getResources().getString(C1893R.string.config_cameraGesturePackage);
            if (string == null || TextUtils.isEmpty(string)) {
                return null;
            }
            return string;
        }

        @JvmStatic
        public final Intent getInsecureCameraIntent(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intent intent = new Intent(getDEFAULT_INSECURE_CAMERA_INTENT_ACTION());
            String overrideCameraPackage = getOverrideCameraPackage(context);
            if (overrideCameraPackage != null) {
                intent.setPackage(overrideCameraPackage);
            }
            return intent;
        }

        @JvmStatic
        public final Intent getSecureCameraIntent(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            Intent intent = new Intent(getDEFAULT_SECURE_CAMERA_INTENT_ACTION());
            String overrideCameraPackage = getOverrideCameraPackage(context);
            if (overrideCameraPackage != null) {
                intent.setPackage(overrideCameraPackage);
            }
            Intent addFlags = intent.addFlags(8388608);
            Intrinsics.checkNotNullExpressionValue(addFlags, "intent.addFlags(Intent.F…ITY_EXCLUDE_FROM_RECENTS)");
            return addFlags;
        }

        @JvmStatic
        public final boolean isSecureCameraIntent(Intent intent) {
            String action;
            if (intent == null || (action = intent.getAction()) == null) {
                return false;
            }
            return action.equals(getDEFAULT_SECURE_CAMERA_INTENT_ACTION());
        }

        @JvmStatic
        public final boolean isInsecureCameraIntent(Intent intent) {
            String action;
            if (intent == null || (action = intent.getAction()) == null) {
                return false;
            }
            return action.equals(getDEFAULT_INSECURE_CAMERA_INTENT_ACTION());
        }
    }
}
