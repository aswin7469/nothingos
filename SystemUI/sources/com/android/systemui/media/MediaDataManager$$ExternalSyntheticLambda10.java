package com.android.systemui.media;

import android.app.Notification;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.service.notification.StatusBarNotification;
import com.android.internal.logging.InstanceId;
import kotlin.jvm.internal.Ref;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MediaDataManager$$ExternalSyntheticLambda10 implements Runnable {
    public final /* synthetic */ MediaDataManager f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ Ref.ObjectRef f$10;
    public final /* synthetic */ MediaButton f$11;
    public final /* synthetic */ MediaSession.Token f$12;
    public final /* synthetic */ Notification f$13;
    public final /* synthetic */ Ref.ObjectRef f$14;
    public final /* synthetic */ int f$15;
    public final /* synthetic */ Boolean f$16;
    public final /* synthetic */ long f$17;
    public final /* synthetic */ InstanceId f$18;
    public final /* synthetic */ int f$19;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ StatusBarNotification f$3;
    public final /* synthetic */ String f$4;
    public final /* synthetic */ Icon f$5;
    public final /* synthetic */ Ref.ObjectRef f$6;
    public final /* synthetic */ Ref.ObjectRef f$7;
    public final /* synthetic */ Icon f$8;
    public final /* synthetic */ Ref.ObjectRef f$9;

    public /* synthetic */ MediaDataManager$$ExternalSyntheticLambda10(MediaDataManager mediaDataManager, String str, String str2, StatusBarNotification statusBarNotification, String str3, Icon icon, Ref.ObjectRef objectRef, Ref.ObjectRef objectRef2, Icon icon2, Ref.ObjectRef objectRef3, Ref.ObjectRef objectRef4, MediaButton mediaButton, MediaSession.Token token, Notification notification, Ref.ObjectRef objectRef5, int i, Boolean bool, long j, InstanceId instanceId, int i2) {
        this.f$0 = mediaDataManager;
        this.f$1 = str;
        this.f$2 = str2;
        this.f$3 = statusBarNotification;
        this.f$4 = str3;
        this.f$5 = icon;
        this.f$6 = objectRef;
        this.f$7 = objectRef2;
        this.f$8 = icon2;
        this.f$9 = objectRef3;
        this.f$10 = objectRef4;
        this.f$11 = mediaButton;
        this.f$12 = token;
        this.f$13 = notification;
        this.f$14 = objectRef5;
        this.f$15 = i;
        this.f$16 = bool;
        this.f$17 = j;
        this.f$18 = instanceId;
        this.f$19 = i2;
    }

    public final void run() {
        MediaDataManager mediaDataManager = this.f$0;
        MediaDataManager.m2806loadMediaDataInBg$lambda24(mediaDataManager, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, this.f$13, this.f$14, this.f$15, this.f$16, this.f$17, this.f$18, this.f$19);
    }
}
