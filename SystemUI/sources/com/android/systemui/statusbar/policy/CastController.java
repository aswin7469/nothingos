package com.android.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
import java.util.List;
/* loaded from: classes2.dex */
public interface CastController extends CallbackController<Callback>, Dumpable {

    /* loaded from: classes2.dex */
    public interface Callback {
        void onCastDevicesChanged();
    }

    /* loaded from: classes2.dex */
    public static final class CastDevice {
        public String description;
        public String id;
        public String name;
        public int state = 0;
        public Object tag;
    }

    List<CastDevice> getCastDevices();

    void setCurrentUserId(int i);

    void setDiscovering(boolean z);

    void startCasting(CastDevice castDevice);

    void stopCasting(CastDevice castDevice);
}
