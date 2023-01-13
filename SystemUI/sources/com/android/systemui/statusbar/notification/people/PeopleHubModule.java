package com.android.systemui.statusbar.notification.people;

import dagger.Binds;
import dagger.Module;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b'\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H'J\u0016\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\u0006\u0010\n\u001a\u00020\u000bH'J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000eH'J\u0016\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\b2\u0006\u0010\n\u001a\u00020\u0011H'J\u0016\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\b2\u0006\u0010\n\u001a\u00020\u0014H'J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\n\u001a\u00020\u0017H'¨\u0006\u0018"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubModule;", "", "()V", "notificationPersonExtractor", "Lcom/android/systemui/statusbar/notification/people/NotificationPersonExtractor;", "pluginImpl", "Lcom/android/systemui/statusbar/notification/people/NotificationPersonExtractorPluginBoundary;", "peopleHubDataSource", "Lcom/android/systemui/statusbar/notification/people/DataSource;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubModel;", "impl", "Lcom/android/systemui/statusbar/notification/people/PeopleHubDataSourceImpl;", "peopleHubSectionFooterViewAdapter", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewAdapter;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewAdapterImpl;", "peopleHubSettingChangeDataSource", "", "Lcom/android/systemui/statusbar/notification/people/PeopleHubSettingChangeDataSourceImpl;", "peopleHubViewModelFactoryDataSource", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModelFactory;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModelFactoryDataSourceImpl;", "peopleNotificationIdentifier", "Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier;", "Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifierImpl;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Module
/* compiled from: PeopleHubModule.kt */
public abstract class PeopleHubModule {
    @Binds
    public abstract NotificationPersonExtractor notificationPersonExtractor(NotificationPersonExtractorPluginBoundary notificationPersonExtractorPluginBoundary);

    @Binds
    public abstract DataSource<PeopleHubModel> peopleHubDataSource(PeopleHubDataSourceImpl peopleHubDataSourceImpl);

    @Binds
    public abstract PeopleHubViewAdapter peopleHubSectionFooterViewAdapter(PeopleHubViewAdapterImpl peopleHubViewAdapterImpl);

    @Binds
    public abstract DataSource<Boolean> peopleHubSettingChangeDataSource(PeopleHubSettingChangeDataSourceImpl peopleHubSettingChangeDataSourceImpl);

    @Binds
    public abstract DataSource<PeopleHubViewModelFactory> peopleHubViewModelFactoryDataSource(PeopleHubViewModelFactoryDataSourceImpl peopleHubViewModelFactoryDataSourceImpl);

    @Binds
    public abstract PeopleNotificationIdentifier peopleNotificationIdentifier(PeopleNotificationIdentifierImpl peopleNotificationIdentifierImpl);
}
