package com.android.systemui.statusbar;

import android.view.View;
import com.android.systemui.plugins.p011qs.C2304QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.CentralSurfaces;

public abstract class QsFrameTranslateController {
    protected CentralSurfaces mCentralSurfaces;

    public abstract float getNotificationsTopPadding(float f, NotificationStackScrollLayoutController notificationStackScrollLayoutController);

    public abstract void translateQsFrame(View view, C2304QS qs, float f, float f2);

    public QsFrameTranslateController(CentralSurfaces centralSurfaces) {
        this.mCentralSurfaces = centralSurfaces;
    }
}
