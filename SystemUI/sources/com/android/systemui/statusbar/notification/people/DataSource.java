package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\u00020\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0007À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/DataSource;", "T", "", "registerListener", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "listener", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ViewPipeline.kt */
public interface DataSource<T> {
    Subscription registerListener(DataListener<? super T> dataListener);
}
