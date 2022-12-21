package com.android.p019wm.shell.pip;

import android.app.ActivityTaskManager;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
import android.window.TaskSnapshot;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.util.List;
import java.util.Objects;

/* renamed from: com.android.wm.shell.pip.PipUtils */
public class PipUtils {
    private static final double EPSILON = 1.0E-7d;
    private static final String TAG = "PipUtils";

    public static Pair<ComponentName, Integer> getTopPipActivity(Context context) {
        try {
            String packageName = context.getPackageName();
            ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(2, 0);
            if (!(rootTaskInfo == null || rootTaskInfo.childTaskIds == null || rootTaskInfo.childTaskIds.length <= 0)) {
                for (int length = rootTaskInfo.childTaskNames.length - 1; length >= 0; length--) {
                    ComponentName unflattenFromString = ComponentName.unflattenFromString(rootTaskInfo.childTaskNames[length]);
                    if (unflattenFromString != null && !unflattenFromString.getPackageName().equals(packageName)) {
                        return new Pair<>(unflattenFromString, Integer.valueOf(rootTaskInfo.childTaskUserIds[length]));
                    }
                }
            }
        } catch (RemoteException unused) {
            ProtoLog.w(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Unable to get pinned stack.", new Object[]{TAG});
        }
        return new Pair<>((Object) null, 0);
    }

    public static boolean aspectRatioChanged(float f, float f2) {
        return ((double) Math.abs(f - f2)) > EPSILON;
    }

    public static boolean remoteActionsMatch(RemoteAction remoteAction, RemoteAction remoteAction2) {
        if (remoteAction == remoteAction2) {
            return true;
        }
        if (remoteAction == null || remoteAction2 == null) {
            return false;
        }
        if (!Objects.equals(remoteAction.getTitle(), remoteAction2.getTitle()) || !Objects.equals(remoteAction.getContentDescription(), remoteAction2.getContentDescription()) || !Objects.equals(remoteAction.getActionIntent(), remoteAction2.getActionIntent())) {
            return false;
        }
        return true;
    }

    public static boolean remoteActionsChanged(List<RemoteAction> list, List<RemoteAction> list2) {
        if (list == null && list2 == null) {
            return false;
        }
        if (list == null || list2 == null || list.size() != list2.size()) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!remoteActionsMatch(list.get(i), list2.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static TaskSnapshot getTaskSnapshot(int i, boolean z) {
        if (i <= 0) {
            return null;
        }
        try {
            return ActivityTaskManager.getService().getTaskSnapshot(i, z);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get task snapshot, taskId=" + i, e);
            return null;
        }
    }
}
