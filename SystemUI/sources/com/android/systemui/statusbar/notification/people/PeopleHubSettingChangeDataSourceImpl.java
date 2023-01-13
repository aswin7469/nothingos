package com.android.systemui.statusbar.notification.people;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u0016\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00020\u0010H\u0016J\u0016\u0010\u0011\u001a\u00020\u00122\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00020\u0010H\u0002R\u0016\u0010\b\u001a\n \n*\u0004\u0018\u00010\t0\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000b\u001a\n \n*\u0004\u0018\u00010\f0\fX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubSettingChangeDataSourceImpl;", "Lcom/android/systemui/statusbar/notification/people/DataSource;", "", "handler", "Landroid/os/Handler;", "context", "Landroid/content/Context;", "(Landroid/os/Handler;Landroid/content/Context;)V", "contentResolver", "Landroid/content/ContentResolver;", "kotlin.jvm.PlatformType", "settingUri", "Landroid/net/Uri;", "registerListener", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "listener", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "updateListener", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubViewController.kt */
public final class PeopleHubSettingChangeDataSourceImpl implements DataSource<Boolean> {
    /* access modifiers changed from: private */
    public final ContentResolver contentResolver;
    private final Handler handler;
    private final Uri settingUri = Settings.Secure.getUriFor("people_strip");

    @Inject
    public PeopleHubSettingChangeDataSourceImpl(@Main Handler handler2, Context context) {
        Intrinsics.checkNotNullParameter(handler2, "handler");
        Intrinsics.checkNotNullParameter(context, "context");
        this.handler = handler2;
        this.contentResolver = context.getContentResolver();
    }

    public Subscription registerListener(DataListener<? super Boolean> dataListener) {
        Intrinsics.checkNotNullParameter(dataListener, "listener");
        updateListener(dataListener);
        PeopleHubSettingChangeDataSourceImpl$registerListener$observer$1 peopleHubSettingChangeDataSourceImpl$registerListener$observer$1 = new PeopleHubSettingChangeDataSourceImpl$registerListener$observer$1(this, dataListener, this.handler);
        this.contentResolver.registerContentObserver(this.settingUri, false, peopleHubSettingChangeDataSourceImpl$registerListener$observer$1, -1);
        return new PeopleHubSettingChangeDataSourceImpl$registerListener$1(this, peopleHubSettingChangeDataSourceImpl$registerListener$observer$1);
    }

    /* access modifiers changed from: private */
    public final void updateListener(DataListener<? super Boolean> dataListener) {
        boolean z = false;
        if (Settings.Secure.getIntForUser(this.contentResolver, "people_strip", 0, -2) != 0) {
            z = true;
        }
        dataListener.onDataChanged(Boolean.valueOf(z));
    }
}
