package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.jvm.functions.Function1;

@Metadata(mo64986d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0015\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H\u0016¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, mo64987d2 = {"com/android/systemui/statusbar/notification/people/ViewPipelineKt$contraMap$1", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "onDataChanged", "", "data", "(Ljava/lang/Object;)V", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ViewPipeline.kt */
public final class ViewPipelineKt$contraMap$1 implements DataListener<S> {
    final /* synthetic */ Function1<S, T> $mapper;
    final /* synthetic */ DataListener<T> $this_contraMap;

    ViewPipelineKt$contraMap$1(DataListener<? super T> dataListener, Function1<? super S, ? extends T> function1) {
        this.$this_contraMap = dataListener;
        this.$mapper = function1;
    }

    public void onDataChanged(S s) {
        this.$this_contraMap.onDataChanged(this.$mapper.invoke(s));
    }
}
