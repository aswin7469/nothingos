package com.nothingos.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.statusbar.policy.CallbackController;
/* loaded from: classes2.dex */
public interface TeslaInfoController extends CallbackController<Callback>, Dumpable {

    /* loaded from: classes2.dex */
    public interface Callback {
        void onActiveStateChanged();

        void onInfoChanged();
    }

    String getBatteryRange();

    QSTile.Icon getProfile();

    String getUserName();

    boolean isConnected();

    boolean shouldShowTeslaInfo();
}
