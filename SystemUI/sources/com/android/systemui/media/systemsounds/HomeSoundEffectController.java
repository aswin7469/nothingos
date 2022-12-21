package com.android.systemui.media.systemsounds;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.util.Slog;
import com.android.systemui.C1893R;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import javax.inject.Inject;

@SysUISingleton
public class HomeSoundEffectController extends CoreStartable {
    private static final String TAG = "HomeSoundEffectController";
    /* access modifiers changed from: private */
    public final ActivityManagerWrapper mActivityManagerWrapper;
    private final AudioManager mAudioManager;
    private boolean mIsLastTaskHome = true;
    private boolean mLastActivityHasNoHomeSound = false;
    private int mLastActivityType;
    private String mLastHomePackageName;
    private int mLastTaskId;
    private final boolean mPlayHomeSoundAfterAssistant;
    private final boolean mPlayHomeSoundAfterDream;
    private final PackageManager mPm;
    private final TaskStackChangeListeners mTaskStackChangeListeners;

    @Inject
    public HomeSoundEffectController(Context context, AudioManager audioManager, TaskStackChangeListeners taskStackChangeListeners, ActivityManagerWrapper activityManagerWrapper, PackageManager packageManager) {
        super(context);
        this.mAudioManager = audioManager;
        this.mTaskStackChangeListeners = taskStackChangeListeners;
        this.mActivityManagerWrapper = activityManagerWrapper;
        this.mPm = packageManager;
        this.mPlayHomeSoundAfterAssistant = context.getResources().getBoolean(C1893R.bool.config_playHomeSoundAfterAssistant);
        this.mPlayHomeSoundAfterDream = context.getResources().getBoolean(C1893R.bool.config_playHomeSoundAfterDream);
    }

    public void start() {
        if (this.mAudioManager.isHomeSoundEffectEnabled()) {
            this.mTaskStackChangeListeners.registerTaskStackListener(new TaskStackChangeListener() {
                public void onTaskStackChanged() {
                    ActivityManager.RunningTaskInfo runningTask = HomeSoundEffectController.this.mActivityManagerWrapper.getRunningTask();
                    if (runningTask != null && runningTask.topActivityInfo != null) {
                        HomeSoundEffectController.this.handleTaskStackChanged(runningTask);
                    }
                }
            });
        }
    }

    private boolean hasFlagNoSound(ActivityInfo activityInfo) {
        if ((activityInfo.privateFlags & 2) == 0) {
            if (this.mPm.checkPermission("android.permission.DISABLE_SYSTEM_SOUND_EFFECTS", activityInfo.packageName) == 0) {
                return true;
            }
            Slog.w(TAG, "Activity has flag playHomeTransition set to false but doesn't hold required permission android.permission.DISABLE_SYSTEM_SOUND_EFFECTS");
        }
        return false;
    }

    private boolean shouldPlayHomeSoundForCurrentTransition(ActivityManager.RunningTaskInfo runningTaskInfo) {
        boolean z = runningTaskInfo.topActivityType == 2;
        if (runningTaskInfo.taskId == this.mLastTaskId || this.mIsLastTaskHome || !z || this.mLastActivityHasNoHomeSound) {
            return false;
        }
        int i = this.mLastActivityType;
        if (i != 4 || this.mPlayHomeSoundAfterAssistant) {
            return i != 5 || this.mPlayHomeSoundAfterDream;
        }
        return false;
    }

    private void updateLastTaskInfo(ActivityManager.RunningTaskInfo runningTaskInfo) {
        this.mLastTaskId = runningTaskInfo.taskId;
        this.mLastActivityType = runningTaskInfo.topActivityType;
        this.mLastActivityHasNoHomeSound = hasFlagNoSound(runningTaskInfo.topActivityInfo);
        boolean z = true;
        boolean z2 = runningTaskInfo.topActivityType == 2;
        boolean equals = runningTaskInfo.topActivityInfo.packageName.equals(this.mLastHomePackageName);
        if (!z2 && !equals) {
            z = false;
        }
        this.mIsLastTaskHome = z;
        if (z2 && !equals) {
            this.mLastHomePackageName = runningTaskInfo.topActivityInfo.packageName;
        }
    }

    /* access modifiers changed from: private */
    public void handleTaskStackChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        if (shouldPlayHomeSoundForCurrentTransition(runningTaskInfo)) {
            this.mAudioManager.playSoundEffect(11);
        }
        updateLastTaskInfo(runningTaskInfo);
    }
}
