package com.android.systemui.util;

import android.view.View;
import android.view.ViewGroup;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: ConvenienceExtensions.kt */
/* loaded from: classes2.dex */
public final class ConvenienceExtensionsKt {
    @NotNull
    public static final Sequence<View> getChildren(@NotNull ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<this>");
        return SequencesKt.sequence(new ConvenienceExtensionsKt$children$1(viewGroup, null));
    }

    @NotNull
    public static final <T> Sequence<T> takeUntil(@NotNull Sequence<? extends T> sequence, @NotNull Function1<? super T, Boolean> pred) {
        Intrinsics.checkNotNullParameter(sequence, "<this>");
        Intrinsics.checkNotNullParameter(pred, "pred");
        return SequencesKt.sequence(new ConvenienceExtensionsKt$takeUntil$1(sequence, pred, null));
    }
}
