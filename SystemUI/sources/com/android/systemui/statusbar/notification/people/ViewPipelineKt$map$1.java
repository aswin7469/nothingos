package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0016¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/statusbar/notification/people/ViewPipelineKt$map$1", "Lcom/android/systemui/statusbar/notification/people/DataSource;", "registerListener", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "listener", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ViewPipeline.kt */
public final class ViewPipelineKt$map$1 implements DataSource<T> {
    final /* synthetic */ Function1<S, T> $mapper;
    final /* synthetic */ DataSource<S> $this_map;

    ViewPipelineKt$map$1(DataSource<? extends S> dataSource, Function1<? super S, ? extends T> function1) {
        this.$this_map = dataSource;
        this.$mapper = function1;
    }

    public Subscription registerListener(DataListener<? super T> dataListener) {
        Intrinsics.checkNotNullParameter(dataListener, "listener");
        return this.$this_map.registerListener(ViewPipelineKt.contraMap(dataListener, this.$mapper));
    }
}
