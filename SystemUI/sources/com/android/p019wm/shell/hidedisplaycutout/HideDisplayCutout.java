package com.android.p019wm.shell.hidedisplaycutout;

import android.content.res.Configuration;
import com.android.p019wm.shell.common.annotations.ExternalThread;

@ExternalThread
/* renamed from: com.android.wm.shell.hidedisplaycutout.HideDisplayCutout */
public interface HideDisplayCutout {
    void onConfigurationChanged(Configuration configuration);
}
