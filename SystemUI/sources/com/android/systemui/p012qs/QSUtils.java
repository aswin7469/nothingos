package com.android.systemui.p012qs;

import android.content.Context;
import android.content.res.Resources;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.util.LargeScreenUtils;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/qs/QSUtils;", "", "()V", "getQsHeaderSystemIconsAreaHeight", "", "context", "Landroid/content/Context;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.QSUtils */
/* compiled from: QSUtils.kt */
public final class QSUtils {
    public static final QSUtils INSTANCE = new QSUtils();

    private QSUtils() {
    }

    @JvmStatic
    public static final int getQsHeaderSystemIconsAreaHeight(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Resources resources = context.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "context.resources");
        if (LargeScreenUtils.shouldUseLargeScreenShadeHeader(resources)) {
            return 0;
        }
        return SystemBarUtils.getQuickQsOffsetHeight(context);
    }
}
