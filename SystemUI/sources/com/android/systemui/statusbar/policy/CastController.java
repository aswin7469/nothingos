package com.android.systemui.statusbar.policy;

import com.android.systemui.Dumpable;
import java.util.List;

public interface CastController extends CallbackController<Callback>, Dumpable {

    public interface Callback {
        void onCastDevicesChanged();
    }

    public static final class CastDevice {
        public static final int STATE_CONNECTED = 2;
        public static final int STATE_CONNECTING = 1;
        public static final int STATE_DISCONNECTED = 0;
        public String description;

        /* renamed from: id */
        public String f395id;
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
