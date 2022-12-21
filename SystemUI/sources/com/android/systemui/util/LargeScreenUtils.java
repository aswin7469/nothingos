package com.android.systemui.util;

import android.content.res.Resources;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\b"}, mo64987d2 = {"Lcom/android/systemui/util/LargeScreenUtils;", "", "()V", "shouldUseLargeScreenShadeHeader", "", "resources", "Landroid/content/res/Resources;", "shouldUseSplitNotificationShade", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LargeScreenUtils.kt */
public final class LargeScreenUtils {
    public static final LargeScreenUtils INSTANCE = new LargeScreenUtils();

    private LargeScreenUtils() {
    }

    @JvmStatic
    public static final boolean shouldUseSplitNotificationShade(Resources resources) {
        Intrinsics.checkNotNullParameter(resources, "resources");
        return resources.getBoolean(C1893R.bool.config_use_split_notification_shade);
    }

    @JvmStatic
    public static final boolean shouldUseLargeScreenShadeHeader(Resources resources) {
        Intrinsics.checkNotNullParameter(resources, "resources");
        return resources.getBoolean(C1893R.bool.config_use_large_screen_shade_header);
    }
}
