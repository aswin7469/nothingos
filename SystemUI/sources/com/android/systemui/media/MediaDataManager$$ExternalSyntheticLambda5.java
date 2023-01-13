package com.android.systemui.media;

import android.service.notification.StatusBarNotification;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaDataManager$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ MediaDataManager f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ StatusBarNotification f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ MediaDataManager$$ExternalSyntheticLambda5(MediaDataManager mediaDataManager, String str, StatusBarNotification statusBarNotification, String str2, boolean z) {
        this.f$0 = mediaDataManager;
        this.f$1 = str;
        this.f$2 = statusBarNotification;
        this.f$3 = str2;
        this.f$4 = z;
    }

    public final void run() {
        MediaDataManager.m2805loadMediaData$lambda8(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
