package com.android.wm.shell.draganddrop;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.app.WindowConfiguration;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherApps;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.splitscreen.SplitScreenController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class DragAndDropPolicy {
    private static final String TAG = "DragAndDropPolicy";
    private final ActivityTaskManager mActivityTaskManager;
    private final Context mContext;
    private DragSession mSession;
    private final SplitScreenController mSplitScreen;
    private final Starter mStarter;
    private final ArrayList<Target> mTargets;

    /* loaded from: classes2.dex */
    public interface Starter {
        void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle);

        void startShortcut(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle);

        void startTask(int i, int i2, int i3, Bundle bundle);
    }

    public DragAndDropPolicy(Context context, SplitScreenController splitScreenController) {
        this(context, ActivityTaskManager.getInstance(), splitScreenController, new DefaultStarter(context));
    }

    /* JADX WARN: Multi-variable type inference failed */
    DragAndDropPolicy(Context context, ActivityTaskManager activityTaskManager, SplitScreenController splitScreenController, Starter starter) {
        this.mTargets = new ArrayList<>();
        this.mContext = context;
        this.mActivityTaskManager = activityTaskManager;
        this.mSplitScreen = splitScreenController;
        this.mStarter = splitScreenController == null ? starter : splitScreenController;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void start(DisplayLayout displayLayout, ClipData clipData) {
        DragSession dragSession = new DragSession(this.mContext, this.mActivityTaskManager, displayLayout, clipData);
        this.mSession = dragSession;
        dragSession.update();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public ArrayList<Target> getTargets(Insets insets) {
        boolean z;
        this.mTargets.clear();
        DragSession dragSession = this.mSession;
        if (dragSession == null) {
            return this.mTargets;
        }
        int width = dragSession.displayLayout.width();
        int height = this.mSession.displayLayout.height();
        int i = insets.left;
        int i2 = (width - i) - insets.right;
        int i3 = insets.top;
        Rect rect = new Rect(i, i3, i2 + i, ((height - i3) - insets.bottom) + i3);
        Rect rect2 = new Rect(rect);
        Rect rect3 = new Rect(rect);
        boolean isLandscape = this.mSession.displayLayout.isLandscape();
        SplitScreenController splitScreenController = this.mSplitScreen;
        if (!(splitScreenController != null && splitScreenController.isSplitScreenVisible())) {
            DragSession dragSession2 = this.mSession;
            if (dragSession2.runningTaskActType != 1 || dragSession2.runningTaskWinMode != 1) {
                z = false;
                if (!z) {
                    Rect rect4 = new Rect();
                    Rect rect5 = new Rect();
                    this.mSplitScreen.getStageBounds(rect4, rect5);
                    rect4.intersect(rect);
                    rect5.intersect(rect);
                    if (isLandscape) {
                        Rect rect6 = new Rect();
                        Rect rect7 = new Rect();
                        rect.splitVertically(new Rect[]{rect6, rect3, rect7});
                        this.mTargets.add(new Target(0, rect3, rect2));
                        this.mTargets.add(new Target(1, rect6, rect4));
                        this.mTargets.add(new Target(3, rect7, rect5));
                    } else {
                        Rect rect8 = new Rect();
                        Rect rect9 = new Rect();
                        rect.splitHorizontally(new Rect[]{rect8, rect3, rect9});
                        this.mTargets.add(new Target(0, rect3, rect2));
                        this.mTargets.add(new Target(2, rect8, rect4));
                        this.mTargets.add(new Target(4, rect9, rect5));
                    }
                } else {
                    this.mTargets.add(new Target(0, rect3, rect2));
                }
                return this.mTargets;
            }
        }
        z = true;
        if (!z) {
        }
        return this.mTargets;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Target getTargetAtLocation(int i, int i2) {
        for (int size = this.mTargets.size() - 1; size >= 0; size--) {
            Target target = this.mTargets.get(size);
            if (target.hitRegion.contains(i, i2)) {
                return target;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void handleDrop(Target target, ClipData clipData) {
        if (target == null || !this.mTargets.contains(target)) {
            return;
        }
        SplitScreenController splitScreenController = this.mSplitScreen;
        int i = 0;
        int i2 = 1;
        boolean z = splitScreenController != null && splitScreenController.isSplitScreenVisible();
        int i3 = target.type;
        if (i3 == 2 || i3 == 1) {
            i = 1;
        }
        int i4 = -1;
        if (i3 == 0 || this.mSplitScreen == null) {
            i2 = -1;
        } else {
            int i5 = i ^ 1;
            if (z) {
                i2 = -1;
            }
            i4 = i5;
        }
        startClipDescription(clipData.getDescription(), this.mSession.dragData, i2, i4);
    }

    private void startClipDescription(ClipDescription clipDescription, Intent intent, int i, int i2) {
        boolean hasMimeType = clipDescription.hasMimeType("application/vnd.android.task");
        boolean hasMimeType2 = clipDescription.hasMimeType("application/vnd.android.shortcut");
        Bundle bundleExtra = intent.hasExtra("android.intent.extra.ACTIVITY_OPTIONS") ? intent.getBundleExtra("android.intent.extra.ACTIVITY_OPTIONS") : new Bundle();
        if (hasMimeType) {
            this.mStarter.startTask(intent.getIntExtra("android.intent.extra.TASK_ID", -1), i, i2, bundleExtra);
        } else if (hasMimeType2) {
            this.mStarter.startShortcut(intent.getStringExtra("android.intent.extra.PACKAGE_NAME"), intent.getStringExtra("android.intent.extra.shortcut.ID"), i, i2, bundleExtra, (UserHandle) intent.getParcelableExtra("android.intent.extra.USER"));
        } else {
            this.mStarter.startIntent((PendingIntent) intent.getParcelableExtra("android.intent.extra.PENDING_INTENT"), null, i, i2, bundleExtra);
        }
    }

    /* loaded from: classes2.dex */
    private static class DragSession {
        final DisplayLayout displayLayout;
        Intent dragData;
        boolean dragItemSupportsSplitscreen;
        private final ActivityTaskManager mActivityTaskManager;
        private final Context mContext;
        private final ClipData mInitialDragData;
        int runningTaskId;
        boolean runningTaskIsResizeable;
        @WindowConfiguration.WindowingMode
        int runningTaskWinMode = 0;
        @WindowConfiguration.ActivityType
        int runningTaskActType = 1;

        DragSession(Context context, ActivityTaskManager activityTaskManager, DisplayLayout displayLayout, ClipData clipData) {
            this.mContext = context;
            this.mActivityTaskManager = activityTaskManager;
            this.mInitialDragData = clipData;
            this.displayLayout = displayLayout;
        }

        void update() {
            boolean z = true;
            List tasks = this.mActivityTaskManager.getTasks(1, false);
            if (!tasks.isEmpty()) {
                ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo) tasks.get(0);
                this.runningTaskWinMode = runningTaskInfo.getWindowingMode();
                this.runningTaskActType = runningTaskInfo.getActivityType();
                this.runningTaskId = runningTaskInfo.taskId;
                this.runningTaskIsResizeable = runningTaskInfo.isResizeable;
            }
            ActivityInfo activityInfo = this.mInitialDragData.getItemAt(0).getActivityInfo();
            if (activityInfo != null && !ActivityInfo.isResizeableMode(activityInfo.resizeMode)) {
                z = false;
            }
            this.dragItemSupportsSplitscreen = z;
            this.dragData = this.mInitialDragData.getItemAt(0).getIntent();
        }
    }

    /* loaded from: classes2.dex */
    private static class DefaultStarter implements Starter {
        private final Context mContext;

        public DefaultStarter(Context context) {
            this.mContext = context;
        }

        @Override // com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter
        public void startTask(int i, int i2, int i3, Bundle bundle) {
            try {
                ActivityTaskManager.getService().startActivityFromRecents(i, bundle);
            } catch (RemoteException e) {
                Slog.e(DragAndDropPolicy.TAG, "Failed to launch task", e);
            }
        }

        @Override // com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter
        public void startShortcut(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle) {
            try {
                ((LauncherApps) this.mContext.getSystemService(LauncherApps.class)).startShortcut(str, str2, null, bundle, userHandle);
            } catch (ActivityNotFoundException e) {
                Slog.e(DragAndDropPolicy.TAG, "Failed to launch shortcut", e);
            }
        }

        @Override // com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter
        public void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle) {
            try {
                pendingIntent.send(this.mContext, 0, intent, null, null, null, bundle);
            } catch (PendingIntent.CanceledException e) {
                Slog.e(DragAndDropPolicy.TAG, "Failed to launch activity", e);
            }
        }
    }

    /* loaded from: classes2.dex */
    static class Target {
        final Rect drawRegion;
        final Rect hitRegion;
        final int type;

        public Target(int i, Rect rect, Rect rect2) {
            this.type = i;
            this.hitRegion = rect;
            this.drawRegion = rect2;
        }

        public String toString() {
            return "Target {hit=" + this.hitRegion + " draw=" + this.drawRegion + "}";
        }
    }
}
