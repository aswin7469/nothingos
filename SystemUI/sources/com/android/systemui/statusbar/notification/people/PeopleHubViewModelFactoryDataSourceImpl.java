package com.android.systemui.statusbar.notification.people;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.ActivityStarter;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\"\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0011\u0010\u0005\u001a\r\u0012\t\u0012\u00070\u0006¢\u0006\u0002\b\u00070\u0001¢\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00020\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0019\u0010\u0005\u001a\r\u0012\t\u0012\u00070\u0006¢\u0006\u0002\b\u00070\u0001X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModelFactoryDataSourceImpl;", "Lcom/android/systemui/statusbar/notification/people/DataSource;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModelFactory;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "dataSource", "Lcom/android/systemui/statusbar/notification/people/PeopleHubModel;", "Lkotlin/jvm/JvmSuppressWildcards;", "(Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/statusbar/notification/people/DataSource;)V", "registerListener", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "listener", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubViewController.kt */
public final class PeopleHubViewModelFactoryDataSourceImpl implements DataSource<PeopleHubViewModelFactory> {
    private final ActivityStarter activityStarter;
    private final DataSource<PeopleHubModel> dataSource;

    @Inject
    public PeopleHubViewModelFactoryDataSourceImpl(ActivityStarter activityStarter2, DataSource<PeopleHubModel> dataSource2) {
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(dataSource2, "dataSource");
        this.activityStarter = activityStarter2;
        this.dataSource = dataSource2;
    }

    public Subscription registerListener(DataListener<? super PeopleHubViewModelFactory> dataListener) {
        Intrinsics.checkNotNullParameter(dataListener, "listener");
        return new PeopleHubViewModelFactoryDataSourceImpl$registerListener$1(this.dataSource.registerListener(new C2738x8c4359c9(new Ref.ObjectRef(), this, dataListener)));
    }

    /* access modifiers changed from: private */
    public static final void registerListener$updateListener(Ref.ObjectRef<PeopleHubModel> objectRef, PeopleHubViewModelFactoryDataSourceImpl peopleHubViewModelFactoryDataSourceImpl, DataListener<? super PeopleHubViewModelFactory> dataListener) {
        PeopleHubModel peopleHubModel = (PeopleHubModel) objectRef.element;
        if (peopleHubModel != null) {
            dataListener.onDataChanged(new PeopleHubViewModelFactoryImpl(peopleHubModel, peopleHubViewModelFactoryDataSourceImpl.activityStarter));
        }
    }
}
