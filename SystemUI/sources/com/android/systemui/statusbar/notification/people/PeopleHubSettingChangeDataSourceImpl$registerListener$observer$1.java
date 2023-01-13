package com.android.systemui.statusbar.notification.people;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\"\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/statusbar/notification/people/PeopleHubSettingChangeDataSourceImpl$registerListener$observer$1", "Landroid/database/ContentObserver;", "onChange", "", "selfChange", "", "uri", "Landroid/net/Uri;", "flags", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubViewController.kt */
public final class PeopleHubSettingChangeDataSourceImpl$registerListener$observer$1 extends ContentObserver {
    final /* synthetic */ DataListener<Boolean> $listener;
    final /* synthetic */ PeopleHubSettingChangeDataSourceImpl this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PeopleHubSettingChangeDataSourceImpl$registerListener$observer$1(PeopleHubSettingChangeDataSourceImpl peopleHubSettingChangeDataSourceImpl, DataListener<? super Boolean> dataListener, Handler handler) {
        super(handler);
        this.this$0 = peopleHubSettingChangeDataSourceImpl;
        this.$listener = dataListener;
    }

    public void onChange(boolean z, Uri uri, int i) {
        super.onChange(z, uri, i);
        this.this$0.updateListener(this.$listener);
    }
}
