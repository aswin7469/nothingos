package com.android.p019wm.shell.common;

import android.text.TextUtils;
import android.view.View;
import com.android.internal.jank.InteractionJankMonitor;

/* renamed from: com.android.wm.shell.common.InteractionJankMonitorUtils */
public class InteractionJankMonitorUtils {
    public static void beginTracing(int i, View view, String str) {
        InteractionJankMonitor.Configuration.Builder withView = InteractionJankMonitor.Configuration.Builder.withView(i, view);
        if (!TextUtils.isEmpty(str)) {
            withView.setTag(str);
        }
        InteractionJankMonitor.getInstance().begin(withView);
    }

    public static void endTracing(int i) {
        InteractionJankMonitor.getInstance().end(i);
    }

    public static void cancelTracing(int i) {
        InteractionJankMonitor.getInstance().cancel(i);
    }
}
