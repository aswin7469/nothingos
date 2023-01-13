package com.android.systemui.p012qs.tiles.dialog;

import com.android.wifitrackerlib.WifiEntry;
import java.util.List;

/* renamed from: com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda13 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class InternetDialog$$ExternalSyntheticLambda13 implements Runnable {
    public final /* synthetic */ InternetDialog f$0;
    public final /* synthetic */ WifiEntry f$1;
    public final /* synthetic */ List f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ InternetDialog$$ExternalSyntheticLambda13(InternetDialog internetDialog, WifiEntry wifiEntry, List list, boolean z, boolean z2) {
        this.f$0 = internetDialog;
        this.f$1 = wifiEntry;
        this.f$2 = list;
        this.f$3 = z;
        this.f$4 = z2;
    }

    public final void run() {
        this.f$0.mo36976x4a74f4e8(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
