package com.android.systemui;

import android.os.Handler;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.SparseArray;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.Assert;
import javax.inject.Inject;

@SysUISingleton
public class ForegroundServiceController {
    public static final int[] APP_OPS = {24};
    private final Handler mMainHandler;
    private final Object mMutex = new Object();
    private final SparseArray<ForegroundServicesUserState> mUserServices = new SparseArray<>();

    interface UserStateUpdateCallback {
        boolean updateUserState(ForegroundServicesUserState foregroundServicesUserState);

        void userStateNotFound(int i) {
        }
    }

    @Inject
    public ForegroundServiceController(AppOpsController appOpsController, @Main Handler handler) {
        this.mMainHandler = handler;
        appOpsController.addCallback(APP_OPS, new ForegroundServiceController$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-ForegroundServiceController  reason: not valid java name */
    public /* synthetic */ void m2522lambda$new$1$comandroidsystemuiForegroundServiceController(int i, int i2, String str, boolean z) {
        this.mMainHandler.post(new ForegroundServiceController$$ExternalSyntheticLambda0(this, i, i2, str, z));
    }

    public boolean isDisclosureNeededForUser(int i) {
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(i);
            if (foregroundServicesUserState == null) {
                return false;
            }
            boolean isDisclosureNeeded = foregroundServicesUserState.isDisclosureNeeded();
            return isDisclosureNeeded;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0018, code lost:
        return r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isSystemAlertWarningNeeded(int r2, java.lang.String r3) {
        /*
            r1 = this;
            java.lang.Object r0 = r1.mMutex
            monitor-enter(r0)
            android.util.SparseArray<com.android.systemui.ForegroundServicesUserState> r1 = r1.mUserServices     // Catch:{ all -> 0x0019 }
            java.lang.Object r1 = r1.get(r2)     // Catch:{ all -> 0x0019 }
            com.android.systemui.ForegroundServicesUserState r1 = (com.android.systemui.ForegroundServicesUserState) r1     // Catch:{ all -> 0x0019 }
            r2 = 0
            if (r1 != 0) goto L_0x0010
            monitor-exit(r0)     // Catch:{ all -> 0x0019 }
            return r2
        L_0x0010:
            android.util.ArraySet r1 = r1.getStandardLayoutKeys(r3)     // Catch:{ all -> 0x0019 }
            if (r1 != 0) goto L_0x0017
            r2 = 1
        L_0x0017:
            monitor-exit(r0)     // Catch:{ all -> 0x0019 }
            return r2
        L_0x0019:
            r1 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0019 }
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.ForegroundServiceController.isSystemAlertWarningNeeded(int, java.lang.String):boolean");
    }

    public ArraySet<Integer> getAppOps(int i, String str) {
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(i);
            if (foregroundServicesUserState == null) {
                return null;
            }
            ArraySet<Integer> features = foregroundServicesUserState.getFeatures(str);
            return features;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: onAppOpChanged */
    public void m2521lambda$new$0$comandroidsystemuiForegroundServiceController(int i, int i2, String str, boolean z) {
        Assert.isMainThread();
        int userId = UserHandle.getUserId(i2);
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(userId);
            if (foregroundServicesUserState == null) {
                foregroundServicesUserState = new ForegroundServicesUserState();
                this.mUserServices.put(userId, foregroundServicesUserState);
            }
            if (z) {
                foregroundServicesUserState.addOp(str, i);
            } else {
                foregroundServicesUserState.removeOp(str, i);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean updateUserState(int i, UserStateUpdateCallback userStateUpdateCallback, boolean z) {
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(i);
            if (foregroundServicesUserState == null) {
                if (!z) {
                    return false;
                }
                foregroundServicesUserState = new ForegroundServicesUserState();
                this.mUserServices.put(i, foregroundServicesUserState);
            }
            boolean updateUserState = userStateUpdateCallback.updateUserState(foregroundServicesUserState);
            return updateUserState;
        }
    }

    public boolean isDisclosureNotification(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getId() == 40 && statusBarNotification.getTag() == null && statusBarNotification.getPackageName().equals("android");
    }

    public boolean isSystemAlertNotification(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getPackageName().equals("android") && statusBarNotification.getTag() != null && statusBarNotification.getTag().contains("AlertWindowNotification");
    }
}
