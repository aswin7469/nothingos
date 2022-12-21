package com.android.p019wm.shell.draganddrop;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.pm.ActivityInfo;
import android.view.DragEvent;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;

/* renamed from: com.android.wm.shell.draganddrop.DragAndDropEventLogger */
public class DragAndDropEventLogger {
    private ActivityInfo mActivityInfo;
    private final InstanceIdSequence mIdSequence = new InstanceIdSequence(Integer.MAX_VALUE);
    private InstanceId mInstanceId;
    private final UiEventLogger mUiEventLogger;

    public DragAndDropEventLogger(UiEventLogger uiEventLogger) {
        this.mUiEventLogger = uiEventLogger;
    }

    public InstanceId logStart(DragEvent dragEvent) {
        ClipDescription clipDescription = dragEvent.getClipDescription();
        ClipData.Item itemAt = dragEvent.getClipData().getItemAt(0);
        InstanceId parcelableExtra = itemAt.getIntent().getParcelableExtra("android.intent.extra.LOGGING_INSTANCE_ID");
        this.mInstanceId = parcelableExtra;
        if (parcelableExtra == null) {
            this.mInstanceId = this.mIdSequence.newInstanceId();
        }
        this.mActivityInfo = itemAt.getActivityInfo();
        log(getStartEnum(clipDescription), this.mActivityInfo);
        return this.mInstanceId;
    }

    public void logDrop() {
        log(DragAndDropUiEventEnum.GLOBAL_APP_DRAG_DROPPED, this.mActivityInfo);
    }

    public void logEnd() {
        log(DragAndDropUiEventEnum.GLOBAL_APP_DRAG_END, this.mActivityInfo);
    }

    private void log(UiEventLogger.UiEventEnum uiEventEnum, ActivityInfo activityInfo) {
        int i;
        String str;
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        if (activityInfo == null) {
            i = 0;
        } else {
            i = activityInfo.applicationInfo.uid;
        }
        if (activityInfo == null) {
            str = null;
        } else {
            str = activityInfo.applicationInfo.packageName;
        }
        uiEventLogger.logWithInstanceId(uiEventEnum, i, str, this.mInstanceId);
    }

    private DragAndDropUiEventEnum getStartEnum(ClipDescription clipDescription) {
        if (clipDescription.hasMimeType("application/vnd.android.activity")) {
            return DragAndDropUiEventEnum.GLOBAL_APP_DRAG_START_ACTIVITY;
        }
        if (clipDescription.hasMimeType("application/vnd.android.shortcut")) {
            return DragAndDropUiEventEnum.GLOBAL_APP_DRAG_START_SHORTCUT;
        }
        if (clipDescription.hasMimeType("application/vnd.android.task")) {
            return DragAndDropUiEventEnum.GLOBAL_APP_DRAG_START_TASK;
        }
        throw new IllegalArgumentException("Not an app drag");
    }

    /* renamed from: com.android.wm.shell.draganddrop.DragAndDropEventLogger$DragAndDropUiEventEnum */
    public enum DragAndDropUiEventEnum implements UiEventLogger.UiEventEnum {
        GLOBAL_APP_DRAG_START_ACTIVITY(884),
        GLOBAL_APP_DRAG_START_SHORTCUT(885),
        GLOBAL_APP_DRAG_START_TASK(888),
        GLOBAL_APP_DRAG_DROPPED(887),
        GLOBAL_APP_DRAG_END(886);
        
        private final int mId;

        private DragAndDropUiEventEnum(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
