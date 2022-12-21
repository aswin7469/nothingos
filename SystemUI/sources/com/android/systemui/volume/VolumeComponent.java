package com.android.systemui.volume;

import android.content.res.Configuration;
import com.android.systemui.demomode.DemoMode;
import java.p026io.PrintWriter;

public interface VolumeComponent extends DemoMode {
    void dismissNow();

    void dump(PrintWriter printWriter, String[] strArr);

    void onConfigurationChanged(Configuration configuration);

    void register();
}
