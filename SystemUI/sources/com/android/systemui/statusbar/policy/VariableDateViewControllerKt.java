package com.android.systemui.statusbar.policy;

import android.icu.text.DateFormat;
import android.icu.text.DisplayContext;
import android.text.TextUtils;
import java.util.Date;
import java.util.Locale;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005H\u0001\u001a\u001a\u0010\b\u001a\u00020\u00052\b\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\u0003H\u0001\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005XT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo65043d2 = {"DEBUG", "", "EMPTY_FORMAT", "Landroid/icu/text/DateFormat;", "TAG", "", "getFormatFromPattern", "pattern", "getTextForFormat", "date", "Ljava/util/Date;", "format", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: VariableDateViewController.kt */
public final class VariableDateViewControllerKt {
    private static final boolean DEBUG = false;
    private static final DateFormat EMPTY_FORMAT = new VariableDateViewControllerKt$EMPTY_FORMAT$1();
    private static final String TAG = "VariableDateViewController";

    public static final String getTextForFormat(Date date, DateFormat dateFormat) {
        Intrinsics.checkNotNullParameter(dateFormat, "format");
        if (dateFormat == EMPTY_FORMAT) {
            return "";
        }
        String format = dateFormat.format(date);
        Intrinsics.checkNotNullExpressionValue(format, "format.format(date)");
        return format;
    }

    public static final DateFormat getFormatFromPattern(String str) {
        if (TextUtils.equals(str, "")) {
            return EMPTY_FORMAT;
        }
        DateFormat instanceForSkeleton = DateFormat.getInstanceForSkeleton(str, Locale.getDefault());
        instanceForSkeleton.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
        Intrinsics.checkNotNullExpressionValue(instanceForSkeleton, "format");
        return instanceForSkeleton;
    }
}
