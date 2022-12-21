package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.util.SparseIntArray;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.policy.DevicePostureController;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class DevicePostureControllerImpl implements DevicePostureController {
    private int mCurrentDevicePosture = 0;
    private final SparseIntArray mDeviceStateToPostureMap = new SparseIntArray();
    private final List<DevicePostureController.Callback> mListeners = new ArrayList();

    @Inject
    public DevicePostureControllerImpl(Context context, DeviceStateManager deviceStateManager, @Main Executor executor) {
        for (String split : context.getResources().getStringArray(17236029)) {
            String[] split2 = split.split(":");
            if (split2.length == 2) {
                try {
                    this.mDeviceStateToPostureMap.put(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]));
                } catch (NumberFormatException unused) {
                }
            }
        }
        deviceStateManager.registerCallback(executor, new DevicePostureControllerImpl$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-statusbar-policy-DevicePostureControllerImpl */
    public /* synthetic */ void mo45687xf7cace0b(int i) {
        Assert.isMainThread();
        this.mCurrentDevicePosture = this.mDeviceStateToPostureMap.get(i, 0);
        this.mListeners.forEach(new DevicePostureControllerImpl$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-policy-DevicePostureControllerImpl */
    public /* synthetic */ void mo45686x1c09524a(DevicePostureController.Callback callback) {
        callback.onPostureChanged(this.mCurrentDevicePosture);
    }

    public void addCallback(DevicePostureController.Callback callback) {
        Assert.isMainThread();
        this.mListeners.add(callback);
    }

    public void removeCallback(DevicePostureController.Callback callback) {
        Assert.isMainThread();
        this.mListeners.remove((Object) callback);
    }

    public int getDevicePosture() {
        return this.mCurrentDevicePosture;
    }
}
