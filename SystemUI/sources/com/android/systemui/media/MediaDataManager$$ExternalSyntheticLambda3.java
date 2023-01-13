package com.android.systemui.media;

import android.app.PendingIntent;
import android.media.MediaDescription;
import android.media.session.MediaSession;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaDataManager$$ExternalSyntheticLambda3 implements Runnable {
    public final /* synthetic */ MediaDataManager f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ MediaDescription f$2;
    public final /* synthetic */ Runnable f$3;
    public final /* synthetic */ MediaSession.Token f$4;
    public final /* synthetic */ String f$5;
    public final /* synthetic */ PendingIntent f$6;
    public final /* synthetic */ String f$7;

    public /* synthetic */ MediaDataManager$$ExternalSyntheticLambda3(MediaDataManager mediaDataManager, int i, MediaDescription mediaDescription, Runnable runnable, MediaSession.Token token, String str, PendingIntent pendingIntent, String str2) {
        this.f$0 = mediaDataManager;
        this.f$1 = i;
        this.f$2 = mediaDescription;
        this.f$3 = runnable;
        this.f$4 = token;
        this.f$5 = str;
        this.f$6 = pendingIntent;
        this.f$7 = str2;
    }

    public final void run() {
        MediaDataManager.m2791addResumptionControls$lambda7(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}
