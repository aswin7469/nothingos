package com.android.systemui.util;

import android.util.IndentingPrintWriter;
import android.view.View;
import android.view.ViewGroup;
import java.p026io.PrintWriter;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a)\u0010\u0007\u001a\u00020\b*\u00020\t2\u0017\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\b0\u000b¢\u0006\u0002\b\fH\bø\u0001\u0000\u001a0\u0010\r\u001a\b\u0012\u0004\u0012\u0002H\u000e0\u0001\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\u00012\u0012\u0010\u000f\u001a\u000e\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u00020\u00100\u000b\"#\u0010\u0000\u001a\u0010\u0012\f\u0012\n \u0003*\u0004\u0018\u00010\u00020\u00020\u0001*\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u0002\u0007\n\u0005\b20\u0001¨\u0006\u0011"}, mo64987d2 = {"children", "Lkotlin/sequences/Sequence;", "Landroid/view/View;", "kotlin.jvm.PlatformType", "Landroid/view/ViewGroup;", "getChildren", "(Landroid/view/ViewGroup;)Lkotlin/sequences/Sequence;", "indentIfPossible", "", "Ljava/io/PrintWriter;", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "takeUntil", "T", "pred", "", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConvenienceExtensions.kt */
public final class ConvenienceExtensionsKt {
    public static final Sequence<View> getChildren(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<this>");
        return SequencesKt.sequence(new ConvenienceExtensionsKt$children$1(viewGroup, (Continuation<? super ConvenienceExtensionsKt$children$1>) null));
    }

    public static final <T> Sequence<T> takeUntil(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        Intrinsics.checkNotNullParameter(function1, "pred");
        return SequencesKt.sequence(new ConvenienceExtensionsKt$takeUntil$1(sequence, function1, (Continuation<? super ConvenienceExtensionsKt$takeUntil$1>) null));
    }

    public static final void indentIfPossible(PrintWriter printWriter, Function1<? super PrintWriter, Unit> function1) {
        Intrinsics.checkNotNullParameter(printWriter, "<this>");
        Intrinsics.checkNotNullParameter(function1, "block");
        boolean z = printWriter instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        function1.invoke(printWriter);
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
    }
}
