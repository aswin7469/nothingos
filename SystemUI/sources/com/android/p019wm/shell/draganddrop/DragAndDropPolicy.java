package com.android.p019wm.shell.draganddrop;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.ResolveInfo;
import android.graphics.Insets;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import androidx.core.content.p003pm.ShortcutManagerCompat;
import com.android.internal.logging.InstanceId;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import com.android.p019wm.shell.splitscreen.SplitScreenController;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.android.wm.shell.draganddrop.DragAndDropPolicy */
public class DragAndDropPolicy {
    /* access modifiers changed from: private */
    public static final String TAG = "DragAndDropPolicy";
    private final ActivityTaskManager mActivityTaskManager;
    private final Context mContext;
    private InstanceId mLoggerSessionId;
    private DragSession mSession;
    private final SplitScreenController mSplitScreen;
    private final Starter mStarter;
    private final ArrayList<Target> mTargets;

    /* renamed from: com.android.wm.shell.draganddrop.DragAndDropPolicy$Starter */
    public interface Starter {
        void enterSplitScreen(int i, boolean z);

        void exitSplitScreen(int i, int i2);

        void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle);

        void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle);

        void startTask(int i, int i2, Bundle bundle);
    }

    public DragAndDropPolicy(Context context, SplitScreenController splitScreenController) {
        this(context, ActivityTaskManager.getInstance(), splitScreenController, new DefaultStarter(context));
    }

    /* JADX WARNING: type inference failed for: r5v0, types: [com.android.wm.shell.draganddrop.DragAndDropPolicy$Starter] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    DragAndDropPolicy(android.content.Context r2, android.app.ActivityTaskManager r3, com.android.p019wm.shell.splitscreen.SplitScreenController r4, com.android.p019wm.shell.draganddrop.DragAndDropPolicy.Starter r5) {
        /*
            r1 = this;
            r1.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1.mTargets = r0
            r1.mContext = r2
            r1.mActivityTaskManager = r3
            r1.mSplitScreen = r4
            if (r4 == 0) goto L_0x0013
            goto L_0x0014
        L_0x0013:
            r4 = r5
        L_0x0014:
            r1.mStarter = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.p019wm.shell.draganddrop.DragAndDropPolicy.<init>(android.content.Context, android.app.ActivityTaskManager, com.android.wm.shell.splitscreen.SplitScreenController, com.android.wm.shell.draganddrop.DragAndDropPolicy$Starter):void");
    }

    /* access modifiers changed from: package-private */
    public void start(DisplayLayout displayLayout, ClipData clipData, InstanceId instanceId) {
        this.mLoggerSessionId = instanceId;
        DragSession dragSession = new DragSession(this.mActivityTaskManager, displayLayout, clipData);
        this.mSession = dragSession;
        dragSession.update();
    }

    /* access modifiers changed from: package-private */
    public ActivityManager.RunningTaskInfo getLatestRunningTask() {
        return this.mSession.runningTaskInfo;
    }

    /* access modifiers changed from: package-private */
    public int getNumTargets() {
        return this.mTargets.size();
    }

    /* access modifiers changed from: package-private */
    public ArrayList<Target> getTargets(Insets insets) {
        this.mTargets.clear();
        DragSession dragSession = this.mSession;
        if (dragSession == null) {
            return this.mTargets;
        }
        int width = dragSession.displayLayout.width();
        int height = this.mSession.displayLayout.height();
        int i = (width - insets.left) - insets.right;
        int i2 = (height - insets.top) - insets.bottom;
        int i3 = insets.left;
        int i4 = insets.top;
        Rect rect = new Rect(i3, i4, i + i3, i2 + i4);
        Rect rect2 = new Rect(rect);
        Rect rect3 = new Rect(rect);
        boolean isLandscape = this.mSession.displayLayout.isLandscape();
        SplitScreenController splitScreenController = this.mSplitScreen;
        boolean z = splitScreenController != null && splitScreenController.isSplitScreenVisible();
        float dimensionPixelSize = (float) this.mContext.getResources().getDimensionPixelSize(C3353R.dimen.split_divider_bar_width);
        if (z || (this.mSession.runningTaskActType == 1 && this.mSession.runningTaskWinMode == 1)) {
            Rect rect4 = new Rect();
            Rect rect5 = new Rect();
            this.mSplitScreen.getStageBounds(rect4, rect5);
            rect4.intersect(rect);
            rect5.intersect(rect);
            if (isLandscape) {
                Rect rect6 = new Rect();
                Rect rect7 = new Rect();
                if (z) {
                    rect6.set(rect);
                    int i5 = (int) (((float) rect4.right) + (dimensionPixelSize / 2.0f));
                    rect6.right = i5;
                    rect7.set(rect);
                    rect7.left = i5;
                } else {
                    rect.splitVertically(new Rect[]{rect6, rect7});
                }
                this.mTargets.add(new Target(1, rect6, rect4));
                this.mTargets.add(new Target(3, rect7, rect5));
            } else {
                Rect rect8 = new Rect();
                Rect rect9 = new Rect();
                if (z) {
                    rect8.set(rect);
                    int i6 = (int) (((float) rect4.bottom) + (dimensionPixelSize / 2.0f));
                    rect8.bottom = i6;
                    rect9.set(rect);
                    rect9.top = i6;
                } else {
                    rect.splitHorizontally(new Rect[]{rect8, rect9});
                }
                this.mTargets.add(new Target(2, rect8, rect4));
                this.mTargets.add(new Target(4, rect9, rect5));
            }
        } else {
            this.mTargets.add(new Target(0, rect3, rect2));
        }
        return this.mTargets;
    }

    /* access modifiers changed from: package-private */
    public Target getTargetAtLocation(int i, int i2) {
        for (int size = this.mTargets.size() - 1; size >= 0; size--) {
            Target target = this.mTargets.get(size);
            if (target.hitRegion.contains(i, i2)) {
                return target;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void handleDrop(Target target, ClipData clipData) {
        int i;
        SplitScreenController splitScreenController;
        if (target != null && this.mTargets.contains(target)) {
            int i2 = (target.type == 2 || target.type == 1) ? 1 : 0;
            if (target.type == 0 || (splitScreenController = this.mSplitScreen) == null) {
                i = -1;
            } else {
                i = i2 ^ 1;
                splitScreenController.logOnDroppedToSplit(i, this.mLoggerSessionId);
            }
            startClipDescription(clipData.getDescription(), this.mSession.dragData, i);
        }
    }

    private void startClipDescription(ClipDescription clipDescription, Intent intent, int i) {
        boolean hasMimeType = clipDescription.hasMimeType("application/vnd.android.task");
        boolean hasMimeType2 = clipDescription.hasMimeType("application/vnd.android.shortcut");
        Bundle bundleExtra = intent.hasExtra("android.intent.extra.ACTIVITY_OPTIONS") ? intent.getBundleExtra("android.intent.extra.ACTIVITY_OPTIONS") : new Bundle();
        if (hasMimeType) {
            this.mStarter.startTask(intent.getIntExtra("android.intent.extra.TASK_ID", -1), i, bundleExtra);
        } else if (hasMimeType2) {
            this.mStarter.startShortcut(intent.getStringExtra("android.intent.extra.PACKAGE_NAME"), intent.getStringExtra(ShortcutManagerCompat.EXTRA_SHORTCUT_ID), i, bundleExtra, (UserHandle) intent.getParcelableExtra("android.intent.extra.USER"));
        } else {
            PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("android.intent.extra.PENDING_INTENT");
            this.mStarter.startIntent(pendingIntent, getStartIntentFillInIntent(pendingIntent, i), i, bundleExtra);
        }
    }

    /* access modifiers changed from: package-private */
    public Intent getStartIntentFillInIntent(PendingIntent pendingIntent, int i) {
        ComponentName componentName;
        List queryIntentComponents = pendingIntent.queryIntentComponents(0);
        ComponentName componentName2 = !queryIntentComponents.isEmpty() ? ((ResolveInfo) queryIntentComponents.get(0)).activityInfo.getComponentName() : null;
        SplitScreenController splitScreenController = this.mSplitScreen;
        int i2 = 1;
        if (!(splitScreenController != null && splitScreenController.isSplitScreenVisible())) {
            componentName = this.mSession.runningTaskInfo != null ? this.mSession.runningTaskInfo.baseActivity : null;
        } else {
            if (i != 0) {
                i2 = 0;
            }
            componentName = this.mSplitScreen.getTaskInfo(i2).baseActivity;
        }
        if (!componentName.equals(componentName2)) {
            return null;
        }
        Intent intent = new Intent();
        intent.addFlags(134217728);
        ProtoLog.v(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP, "Adding MULTIPLE_TASK", new Object[0]);
        return intent;
    }

    /* renamed from: com.android.wm.shell.draganddrop.DragAndDropPolicy$DragSession */
    private static class DragSession {
        final DisplayLayout displayLayout;
        Intent dragData;
        boolean dragItemSupportsSplitscreen;
        private final ActivityTaskManager mActivityTaskManager;
        private final ClipData mInitialDragData;
        int runningTaskActType = 1;
        ActivityManager.RunningTaskInfo runningTaskInfo;
        int runningTaskWinMode = 0;

        DragSession(ActivityTaskManager activityTaskManager, DisplayLayout displayLayout2, ClipData clipData) {
            this.mActivityTaskManager = activityTaskManager;
            this.mInitialDragData = clipData;
            this.displayLayout = displayLayout2;
        }

        /* access modifiers changed from: package-private */
        public void update() {
            boolean z = true;
            List tasks = this.mActivityTaskManager.getTasks(1, false);
            if (!tasks.isEmpty()) {
                ActivityManager.RunningTaskInfo runningTaskInfo2 = (ActivityManager.RunningTaskInfo) tasks.get(0);
                this.runningTaskInfo = runningTaskInfo2;
                this.runningTaskWinMode = runningTaskInfo2.getWindowingMode();
                this.runningTaskActType = runningTaskInfo2.getActivityType();
            }
            ActivityInfo activityInfo = this.mInitialDragData.getItemAt(0).getActivityInfo();
            if (activityInfo != null && !ActivityInfo.isResizeableMode(activityInfo.resizeMode)) {
                z = false;
            }
            this.dragItemSupportsSplitscreen = z;
            this.dragData = this.mInitialDragData.getItemAt(0).getIntent();
        }
    }

    /* renamed from: com.android.wm.shell.draganddrop.DragAndDropPolicy$DefaultStarter */
    private static class DefaultStarter implements Starter {
        private final Context mContext;

        public DefaultStarter(Context context) {
            this.mContext = context;
        }

        public void startTask(int i, int i2, Bundle bundle) {
            try {
                ActivityTaskManager.getService().startActivityFromRecents(i, bundle);
            } catch (RemoteException e) {
                Slog.e(DragAndDropPolicy.TAG, "Failed to launch task", e);
            }
        }

        public void startShortcut(String str, String str2, int i, Bundle bundle, UserHandle userHandle) {
            try {
                ((LauncherApps) this.mContext.getSystemService(LauncherApps.class)).startShortcut(str, str2, (Rect) null, bundle, userHandle);
            } catch (ActivityNotFoundException e) {
                Slog.e(DragAndDropPolicy.TAG, "Failed to launch shortcut", e);
            }
        }

        public void startIntent(PendingIntent pendingIntent, Intent intent, int i, Bundle bundle) {
            try {
                pendingIntent.send(this.mContext, 0, intent, (PendingIntent.OnFinished) null, (Handler) null, (String) null, bundle);
            } catch (PendingIntent.CanceledException e) {
                Slog.e(DragAndDropPolicy.TAG, "Failed to launch activity", e);
            }
        }

        public void enterSplitScreen(int i, boolean z) {
            throw new UnsupportedOperationException("enterSplitScreen not implemented by starter");
        }

        public void exitSplitScreen(int i, int i2) {
            throw new UnsupportedOperationException("exitSplitScreen not implemented by starter");
        }
    }

    /* renamed from: com.android.wm.shell.draganddrop.DragAndDropPolicy$Target */
    static class Target {
        static final int TYPE_FULLSCREEN = 0;
        static final int TYPE_SPLIT_BOTTOM = 4;
        static final int TYPE_SPLIT_LEFT = 1;
        static final int TYPE_SPLIT_RIGHT = 3;
        static final int TYPE_SPLIT_TOP = 2;
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
