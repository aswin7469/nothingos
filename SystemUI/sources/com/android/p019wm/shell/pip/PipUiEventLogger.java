package com.android.p019wm.shell.pip;

import android.app.TaskInfo;
import android.content.pm.PackageManager;
import android.net.connectivity.android.net.mdns.aidl.IMDnsEventListener;
import com.android.internal.logging.UiEventLogger;
import com.nothing.p023os.device.DeviceConstant;

/* renamed from: com.android.wm.shell.pip.PipUiEventLogger */
public class PipUiEventLogger {
    private static final int INVALID_PACKAGE_UID = -1;
    private final PackageManager mPackageManager;
    private String mPackageName;
    private int mPackageUid = -1;
    private final UiEventLogger mUiEventLogger;

    public PipUiEventLogger(UiEventLogger uiEventLogger, PackageManager packageManager) {
        this.mUiEventLogger = uiEventLogger;
        this.mPackageManager = packageManager;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        if (taskInfo == null || taskInfo.topActivity == null) {
            this.mPackageName = null;
            this.mPackageUid = -1;
            return;
        }
        String packageName = taskInfo.topActivity.getPackageName();
        this.mPackageName = packageName;
        this.mPackageUid = getUid(packageName, taskInfo.userId);
    }

    public void log(PipUiEventEnum pipUiEventEnum) {
        int i;
        String str = this.mPackageName;
        if (str != null && (i = this.mPackageUid) != -1) {
            this.mUiEventLogger.log(pipUiEventEnum, i, str);
        }
    }

    private int getUid(String str, int i) {
        try {
            return this.mPackageManager.getApplicationInfoAsUser(str, 0, i).uid;
        } catch (PackageManager.NameNotFoundException unused) {
            return -1;
        }
    }

    /* renamed from: com.android.wm.shell.pip.PipUiEventLogger$PipUiEventEnum */
    public enum PipUiEventEnum implements UiEventLogger.UiEventEnum {
        PICTURE_IN_PICTURE_ENTER(IMDnsEventListener.SERVICE_FOUND),
        PICTURE_IN_PICTURE_EXPAND_TO_FULLSCREEN(IMDnsEventListener.SERVICE_LOST),
        PICTURE_IN_PICTURE_TAP_TO_REMOVE(IMDnsEventListener.SERVICE_REGISTRATION_FAILED),
        PICTURE_IN_PICTURE_DRAG_TO_REMOVE(IMDnsEventListener.SERVICE_REGISTERED),
        PICTURE_IN_PICTURE_SHOW_MENU(IMDnsEventListener.SERVICE_RESOLUTION_FAILED),
        PICTURE_IN_PICTURE_HIDE_MENU(IMDnsEventListener.SERVICE_RESOLVED),
        PICTURE_IN_PICTURE_CHANGE_ASPECT_RATIO(609),
        PICTURE_IN_PICTURE_RESIZE(DeviceConstant.ORDER_ANC),
        PICTURE_IN_PICTURE_STASH_UNSTASHED(709),
        PICTURE_IN_PICTURE_STASH_LEFT(DeviceConstant.ORDER_IN_EAR_DETECTION),
        PICTURE_IN_PICTURE_STASH_RIGHT(711),
        PICTURE_IN_PICTURE_SHOW_SETTINGS(933),
        PICTURE_IN_PICTURE_CUSTOM_CLOSE(1058);
        
        private final int mId;

        private PipUiEventEnum(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
