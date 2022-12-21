package com.android.systemui.statusbar;

import android.view.View;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import javax.inject.Inject;

@SysUISingleton
public class QsFrameTranslateImpl extends QsFrameTranslateController {
    public float getNotificationsTopPadding(float f, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return f;
    }

    public void translateQsFrame(View view, C2301QS qs, float f, float f2) {
    }

    @Inject
    public QsFrameTranslateImpl(CentralSurfaces centralSurfaces) {
        super(centralSurfaces);
    }
}
