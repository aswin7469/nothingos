package com.android.systemui.util;

import android.util.IndentingPrintWriter;
import java.p026io.PrintWriter;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000*\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u000e\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003\u001a\n\u0010\u0004\u001a\u00020\u0005*\u00020\u0006\u001a\u001e\u0010\u0007\u001a\u00020\b*\u00020\u00052\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\b0\nH\bø\u0001\u0000\u001a\u0012\u0010\u0007\u001a\u00020\b*\u00020\u00052\u0006\u0010\u000b\u001a\u00020\f\u0002\u0007\n\u0005\b20\u0001¨\u0006\r"}, mo65043d2 = {"visibilityString", "", "visibility", "", "asIndenting", "Landroid/util/IndentingPrintWriter;", "Ljava/io/PrintWriter;", "withIncreasedIndent", "", "block", "Lkotlin/Function0;", "runnable", "Ljava/lang/Runnable;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DumpUtils.kt */
public final class DumpUtilsKt {
    public static final IndentingPrintWriter asIndenting(PrintWriter printWriter) {
        Intrinsics.checkNotNullParameter(printWriter, "<this>");
        IndentingPrintWriter indentingPrintWriter = printWriter instanceof IndentingPrintWriter ? (IndentingPrintWriter) printWriter : null;
        return indentingPrintWriter == null ? new IndentingPrintWriter(printWriter) : indentingPrintWriter;
    }

    public static final void withIncreasedIndent(IndentingPrintWriter indentingPrintWriter, Function0<Unit> function0) {
        Intrinsics.checkNotNullParameter(indentingPrintWriter, "<this>");
        Intrinsics.checkNotNullParameter(function0, "block");
        indentingPrintWriter.increaseIndent();
        try {
            function0.invoke();
        } finally {
            InlineMarker.finallyStart(1);
            indentingPrintWriter.decreaseIndent();
            InlineMarker.finallyEnd(1);
        }
    }

    public static final void withIncreasedIndent(IndentingPrintWriter indentingPrintWriter, Runnable runnable) {
        Intrinsics.checkNotNullParameter(indentingPrintWriter, "<this>");
        Intrinsics.checkNotNullParameter(runnable, "runnable");
        indentingPrintWriter.increaseIndent();
        try {
            runnable.run();
        } finally {
            indentingPrintWriter.decreaseIndent();
        }
    }

    public static final String visibilityString(int i) {
        if (i == 0) {
            return "visible";
        }
        if (i != 4) {
            return i != 8 ? "unknown:" + i : "gone";
        }
        return "invisible";
    }
}
