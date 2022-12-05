package com.android.systemui.statusbar.policy;

import android.hardware.SensorPrivacyManager;
import android.util.ArraySet;
import android.util.SparseBooleanArray;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import java.util.Set;
/* loaded from: classes2.dex */
public class IndividualSensorPrivacyControllerImpl implements IndividualSensorPrivacyController {
    private static final int[] SENSORS = {2, 1};
    private final SensorPrivacyManager mSensorPrivacyManager;
    private final SparseBooleanArray mState = new SparseBooleanArray();
    private final Set<IndividualSensorPrivacyController.Callback> mCallbacks = new ArraySet();

    public IndividualSensorPrivacyControllerImpl(SensorPrivacyManager sensorPrivacyManager) {
        this.mSensorPrivacyManager = sensorPrivacyManager;
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController
    public void init() {
        int[] iArr;
        for (final int i : SENSORS) {
            this.mSensorPrivacyManager.addSensorPrivacyListener(i, new SensorPrivacyManager.OnSensorPrivacyChangedListener() { // from class: com.android.systemui.statusbar.policy.IndividualSensorPrivacyControllerImpl$$ExternalSyntheticLambda0
                public final void onSensorPrivacyChanged(int i2, boolean z) {
                    IndividualSensorPrivacyControllerImpl.this.lambda$init$0(i, i2, z);
                }
            });
            this.mState.put(i, this.mSensorPrivacyManager.isSensorPrivacyEnabled(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$init$0(int i, int i2, boolean z) {
        onSensorPrivacyChanged(i, z);
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController
    public boolean supportsSensorToggle(int i) {
        return this.mSensorPrivacyManager.supportsSensorToggle(i);
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController
    public boolean isSensorBlocked(int i) {
        return this.mState.get(i, false);
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController
    public void setSensorBlocked(int i, int i2, boolean z) {
        this.mSensorPrivacyManager.setSensorPrivacyForProfileGroup(i, i2, z);
    }

    @Override // com.android.systemui.statusbar.policy.IndividualSensorPrivacyController
    public void suppressSensorPrivacyReminders(int i, boolean z) {
        this.mSensorPrivacyManager.suppressSensorPrivacyReminders(i, z);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(IndividualSensorPrivacyController.Callback callback) {
        this.mCallbacks.add(callback);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(IndividualSensorPrivacyController.Callback callback) {
        this.mCallbacks.remove(callback);
    }

    private void onSensorPrivacyChanged(int i, boolean z) {
        this.mState.put(i, z);
        for (IndividualSensorPrivacyController.Callback callback : this.mCallbacks) {
            callback.onSensorBlockedChanged(i, z);
        }
    }
}
