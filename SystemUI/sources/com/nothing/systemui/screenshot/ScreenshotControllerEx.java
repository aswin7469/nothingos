package com.nothing.systemui.screenshot;

import android.content.Context;
import android.provider.Settings;
import com.android.systemui.C1894R;
import com.nothing.NtFeaturesUtils;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.File;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0004"}, mo65043d2 = {"Lcom/nothing/systemui/screenshot/ScreenshotControllerEx;", "", "()V", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ScreenshotControllerEx.kt */
public final class ScreenshotControllerEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String SETTING_SCREENSHOT_SHUTTER_ENABLED = "screenshot_shutter_enabled";
    private static final String TAG = "ScreenshotControllerEx";

    @JvmStatic
    public static final File getScreenshotSound(Context context) {
        return Companion.getScreenshotSound(context);
    }

    @JvmStatic
    public static final boolean isScreenshotSoundEnable(Context context) {
        return Companion.isScreenshotSoundEnable(context);
    }

    @Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0007J\u0012\u0010\n\u001a\u00020\u000b2\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo65043d2 = {"Lcom/nothing/systemui/screenshot/ScreenshotControllerEx$Companion;", "", "()V", "SETTING_SCREENSHOT_SHUTTER_ENABLED", "", "TAG", "getScreenshotSound", "Ljava/io/File;", "context", "Landroid/content/Context;", "isScreenshotSoundEnable", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ScreenshotControllerEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final File getScreenshotSound(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            File file = new File(context.getResources().getString(17039898));
            if (!NtFeaturesUtils.isSupport(new int[]{1})) {
                NTLogUtil.m1686d(ScreenshotControllerEx.TAG, "NTF_SCREENSHOT_SOUND not enable");
                return file;
            }
            NTLogUtil.m1686d(ScreenshotControllerEx.TAG, "Get NT screenshot sound");
            File file2 = new File(context.getResources().getString(C1894R.string.config_screenshotSound));
            if (file2.exists()) {
                return file2;
            }
            NTLogUtil.m1686d(ScreenshotControllerEx.TAG, "Screenshot sound not exists. use default camera shutter sound.");
            return file;
        }

        @JvmStatic
        public final boolean isScreenshotSoundEnable(Context context) {
            if (context == null) {
                return true;
            }
            NTLogUtil.m1686d(ScreenshotControllerEx.TAG, "SCREENSHOT_SHUTTER_ENABLED = " + Settings.System.getInt(context.getContentResolver(), ScreenshotControllerEx.SETTING_SCREENSHOT_SHUTTER_ENABLED, 1));
            if (Settings.System.getInt(context.getContentResolver(), ScreenshotControllerEx.SETTING_SCREENSHOT_SHUTTER_ENABLED, 1) == 1) {
                return true;
            }
            return false;
        }
    }
}
