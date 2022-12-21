package com.android.p019wm.shell.draganddrop;

import android.content.res.Configuration;
import com.android.p019wm.shell.common.annotations.ExternalThread;

@ExternalThread
/* renamed from: com.android.wm.shell.draganddrop.DragAndDrop */
public interface DragAndDrop {
    void onConfigChanged(Configuration configuration);

    void onThemeChanged();
}
