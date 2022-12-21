package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a6\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00012\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0005\u001a6\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00030\u0007\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00072\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u0005¨\u0006\b"}, mo64987d2 = {"contraMap", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "S", "T", "mapper", "Lkotlin/Function1;", "map", "Lcom/android/systemui/statusbar/notification/people/DataSource;", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ViewPipeline.kt */
public final class ViewPipelineKt {
    public static final <S, T> DataListener<S> contraMap(DataListener<? super T> dataListener, Function1<? super S, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(dataListener, "<this>");
        Intrinsics.checkNotNullParameter(function1, "mapper");
        return new ViewPipelineKt$contraMap$1(dataListener, function1);
    }

    public static final <S, T> DataSource<T> map(DataSource<? extends S> dataSource, Function1<? super S, ? extends T> function1) {
        Intrinsics.checkNotNullParameter(dataSource, "<this>");
        Intrinsics.checkNotNullParameter(function1, "mapper");
        return new ViewPipelineKt$map$1(dataSource, function1);
    }
}
