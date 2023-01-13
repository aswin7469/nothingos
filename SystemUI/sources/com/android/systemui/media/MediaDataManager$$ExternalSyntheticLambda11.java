package com.android.systemui.media;

import android.app.PendingIntent;
import android.graphics.drawable.Icon;
import android.media.MediaDescription;
import android.media.session.MediaSession;
import com.android.internal.logging.InstanceId;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaDataManager$$ExternalSyntheticLambda11 implements Runnable {
    public final /* synthetic */ MediaDataManager f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ long f$10;
    public final /* synthetic */ InstanceId f$11;
    public final /* synthetic */ int f$12;
    public final /* synthetic */ int f$2;
    public final /* synthetic */ String f$3;
    public final /* synthetic */ MediaDescription f$4;
    public final /* synthetic */ Icon f$5;
    public final /* synthetic */ MediaAction f$6;
    public final /* synthetic */ MediaSession.Token f$7;
    public final /* synthetic */ PendingIntent f$8;
    public final /* synthetic */ Runnable f$9;

    public /* synthetic */ MediaDataManager$$ExternalSyntheticLambda11(MediaDataManager mediaDataManager, String str, int i, String str2, MediaDescription mediaDescription, Icon icon, MediaAction mediaAction, MediaSession.Token token, PendingIntent pendingIntent, Runnable runnable, long j, InstanceId instanceId, int i2) {
        this.f$0 = mediaDataManager;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = str2;
        this.f$4 = mediaDescription;
        this.f$5 = icon;
        this.f$6 = mediaAction;
        this.f$7 = token;
        this.f$8 = pendingIntent;
        this.f$9 = runnable;
        this.f$10 = j;
        this.f$11 = instanceId;
        this.f$12 = i2;
    }

    public final void run() {
        MediaDataManager.m2807loadMediaDataInBgForResumption$lambda21(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12);
    }
}
