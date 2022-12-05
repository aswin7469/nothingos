package com.android.systemui.statusbar.policy;

import android.icu.text.DateFormat;
import android.icu.text.DisplayContext;
import android.icu.util.Calendar;
import android.text.TextUtils;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: VariableDateViewController.kt */
/* loaded from: classes2.dex */
public final class VariableDateViewControllerKt {
    @NotNull
    private static final DateFormat EMPTY_FORMAT = new DateFormat() { // from class: com.android.systemui.statusbar.policy.VariableDateViewControllerKt$EMPTY_FORMAT$1
        @Override // android.icu.text.DateFormat
        @Nullable
        public StringBuffer format(@NotNull Calendar cal, @NotNull StringBuffer toAppendTo, @NotNull FieldPosition fieldPosition) {
            Intrinsics.checkNotNullParameter(cal, "cal");
            Intrinsics.checkNotNullParameter(toAppendTo, "toAppendTo");
            Intrinsics.checkNotNullParameter(fieldPosition, "fieldPosition");
            return null;
        }

        @Override // android.icu.text.DateFormat
        public void parse(@NotNull String text, @NotNull Calendar cal, @NotNull ParsePosition pos) {
            Intrinsics.checkNotNullParameter(text, "text");
            Intrinsics.checkNotNullParameter(cal, "cal");
            Intrinsics.checkNotNullParameter(pos, "pos");
        }
    };

    @NotNull
    public static final String getTextForFormat(@Nullable Date date, @NotNull DateFormat format) {
        Intrinsics.checkNotNullParameter(format, "format");
        if (format == EMPTY_FORMAT) {
            return "";
        }
        String format2 = format.format(date);
        Intrinsics.checkNotNullExpressionValue(format2, "format.format(date)");
        return format2;
    }

    @NotNull
    public static final DateFormat getFormatFromPattern(@Nullable String str) {
        if (TextUtils.equals(str, "")) {
            return EMPTY_FORMAT;
        }
        DateFormat format = DateFormat.getInstanceForSkeleton(str, Locale.getDefault());
        format.setContext(DisplayContext.CAPITALIZATION_FOR_STANDALONE);
        Intrinsics.checkNotNullExpressionValue(format, "format");
        return format;
    }
}
