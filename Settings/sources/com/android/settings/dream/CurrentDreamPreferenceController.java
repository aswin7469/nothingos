package com.android.settings.dream;

import android.content.Context;
import android.content.IntentFilter;
import androidx.preference.Preference;
import com.android.settings.Utils;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.dream.DreamBackend;
import java.util.Optional;
/* loaded from: classes.dex */
public class CurrentDreamPreferenceController extends BasePreferenceController {
    private final DreamBackend mBackend;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public CurrentDreamPreferenceController(Context context, String str) {
        super(context, str);
        this.mBackend = DreamBackend.getInstance(context);
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return this.mBackend.getDreamInfos().size() > 0 ? 0 : 2;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void updateState(Preference preference) {
        super.updateState(preference);
        setGearClickListenerForPreference(preference);
        setActiveDreamIcon(preference);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    /* renamed from: getSummary */
    public CharSequence mo485getSummary() {
        return this.mBackend.getActiveDreamName();
    }

    private void setGearClickListenerForPreference(Preference preference) {
        if (!(preference instanceof GearPreference)) {
            return;
        }
        GearPreference gearPreference = (GearPreference) preference;
        Optional<DreamBackend.DreamInfo> activeDreamInfo = getActiveDreamInfo();
        if (!activeDreamInfo.isPresent() || activeDreamInfo.get().settingsComponentName == null) {
            gearPreference.setOnGearClickListener(null);
        } else {
            gearPreference.setOnGearClickListener(new GearPreference.OnGearClickListener() { // from class: com.android.settings.dream.CurrentDreamPreferenceController$$ExternalSyntheticLambda0
                @Override // com.android.settings.widget.GearPreference.OnGearClickListener
                public final void onGearClick(GearPreference gearPreference2) {
                    CurrentDreamPreferenceController.this.lambda$setGearClickListenerForPreference$0(gearPreference2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setGearClickListenerForPreference$0(GearPreference gearPreference) {
        launchScreenSaverSettings();
    }

    private void launchScreenSaverSettings() {
        Optional<DreamBackend.DreamInfo> activeDreamInfo = getActiveDreamInfo();
        if (!activeDreamInfo.isPresent()) {
            return;
        }
        this.mBackend.launchSettings(this.mContext, activeDreamInfo.get());
    }

    private Optional<DreamBackend.DreamInfo> getActiveDreamInfo() {
        return this.mBackend.getDreamInfos().stream().filter(CurrentDreamPreferenceController$$ExternalSyntheticLambda1.INSTANCE).findFirst();
    }

    private void setActiveDreamIcon(Preference preference) {
        if (!(preference instanceof GearPreference)) {
            return;
        }
        GearPreference gearPreference = (GearPreference) preference;
        gearPreference.setIconSize(2);
        gearPreference.setIcon(Utils.getSafeIcon(this.mBackend.getActiveIcon()));
    }
}
