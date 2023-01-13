package com.android.systemui.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.provider.Settings;
import android.view.DisplayCutout;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.C1894R;
import com.android.systemui.shared.system.QuickStepContract;
import java.util.List;
import java.util.function.Consumer;

public class Utils {
    private static Boolean sUseQsMediaPlayer;

    public static <T> void safeForeach(List<T> list, Consumer<T> consumer) {
        for (int size = list.size() - 1; size >= 0; size--) {
            T t = list.get(size);
            if (t != null) {
                consumer.accept(t);
            }
        }
    }

    public static boolean isHeadlessRemoteDisplayProvider(PackageManager packageManager, String str) {
        if (packageManager.checkPermission("android.permission.REMOTE_DISPLAY_PROVIDER", str) != 0) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(str);
        return packageManager.queryIntentActivities(intent, 0).isEmpty();
    }

    public static boolean isGesturalModeOnDefaultDisplay(Context context, int i) {
        return context.getDisplayId() == 0 && QuickStepContract.isGesturalMode(i);
    }

    public static boolean useQsMediaPlayer(Context context) {
        if (sUseQsMediaPlayer == null) {
            boolean z = true;
            if (Settings.Global.getInt(context.getContentResolver(), "qs_media_controls", 1) <= 0) {
                z = false;
            }
            sUseQsMediaPlayer = Boolean.valueOf(z);
        }
        return sUseQsMediaPlayer.booleanValue();
    }

    public static boolean useMediaResumption(Context context) {
        int i = Settings.Secure.getInt(context.getContentResolver(), "qs_media_resumption", 1);
        if (!useQsMediaPlayer(context) || i <= 0) {
            return false;
        }
        return true;
    }

    public static boolean useCollapsedMediaInLandscape(Resources resources) {
        return resources.getBoolean(C1894R.bool.config_quickSettingsMediaLandscapeCollapsed);
    }

    public static int getStatusBarHeaderHeightKeyguard(Context context) {
        int i;
        int statusBarHeight = SystemBarUtils.getStatusBarHeight(context);
        DisplayCutout cutout = context.getDisplay().getCutout();
        if (cutout == null) {
            i = 0;
        } else {
            i = cutout.getWaterfallInsets().top;
        }
        return Math.max(statusBarHeight, context.getResources().getDimensionPixelSize(C1894R.dimen.status_bar_header_height_keyguard) + i);
    }
}
