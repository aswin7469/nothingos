package com.android.systemui.statusbar.notification.people;

import com.android.systemui.dagger.SysUISingleton;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u001a\b\u0007\u0012\u0011\u0010\u0002\u001a\r\u0012\t\u0012\u00070\u0004¢\u0006\u0002\b\u00050\u0003¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016R\u0019\u0010\u0002\u001a\r\u0012\t\u0012\u00070\u0004¢\u0006\u0002\b\u00050\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubViewAdapterImpl;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewAdapter;", "dataSource", "Lcom/android/systemui/statusbar/notification/people/DataSource;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModelFactory;", "Lkotlin/jvm/JvmSuppressWildcards;", "(Lcom/android/systemui/statusbar/notification/people/DataSource;)V", "bindView", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "viewBoundary", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewBoundary;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubViewController.kt */
public final class PeopleHubViewAdapterImpl implements PeopleHubViewAdapter {
    private final DataSource<PeopleHubViewModelFactory> dataSource;

    @Inject
    public PeopleHubViewAdapterImpl(DataSource<PeopleHubViewModelFactory> dataSource2) {
        Intrinsics.checkNotNullParameter(dataSource2, "dataSource");
        this.dataSource = dataSource2;
    }

    public Subscription bindView(PeopleHubViewBoundary peopleHubViewBoundary) {
        Intrinsics.checkNotNullParameter(peopleHubViewBoundary, "viewBoundary");
        return this.dataSource.registerListener(new PeopleHubDataListenerImpl(peopleHubViewBoundary));
    }
}
