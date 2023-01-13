package com.android.systemui.statusbar.policy;

import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import java.text.FieldPosition;
import java.text.ParsePosition;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u00001\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016J \u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\r\u001a\u00020\u000eH\u0016¨\u0006\u000f"}, mo65043d2 = {"com/android/systemui/statusbar/policy/VariableDateViewControllerKt$EMPTY_FORMAT$1", "Landroid/icu/text/DateFormat;", "format", "Ljava/lang/StringBuffer;", "cal", "Landroid/icu/util/Calendar;", "toAppendTo", "fieldPosition", "Ljava/text/FieldPosition;", "parse", "", "text", "", "pos", "Ljava/text/ParsePosition;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: VariableDateViewController.kt */
public final class VariableDateViewControllerKt$EMPTY_FORMAT$1 extends DateFormat {
    public StringBuffer format(Calendar calendar, StringBuffer stringBuffer, FieldPosition fieldPosition) {
        Intrinsics.checkNotNullParameter(calendar, "cal");
        Intrinsics.checkNotNullParameter(stringBuffer, "toAppendTo");
        Intrinsics.checkNotNullParameter(fieldPosition, "fieldPosition");
        return null;
    }

    public void parse(String str, Calendar calendar, ParsePosition parsePosition) {
        Intrinsics.checkNotNullParameter(str, "text");
        Intrinsics.checkNotNullParameter(calendar, "cal");
        Intrinsics.checkNotNullParameter(parsePosition, "pos");
    }

    VariableDateViewControllerKt$EMPTY_FORMAT$1() {
    }
}
