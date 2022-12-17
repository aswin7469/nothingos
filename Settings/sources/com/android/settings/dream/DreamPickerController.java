package com.android.settings.dream;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R$dimen;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.dream.DreamBackend;
import com.android.settingslib.widget.LayoutPreference;
import java.util.List;
import java.util.stream.Collectors;

public class DreamPickerController extends BasePreferenceController {
    /* access modifiers changed from: private */
    public DreamBackend.DreamInfo mActiveDream;
    /* access modifiers changed from: private */
    public DreamAdapter mAdapter;
    /* access modifiers changed from: private */
    public final DreamBackend mBackend;
    private final List<DreamBackend.DreamInfo> mDreamInfos;
    /* access modifiers changed from: private */
    public final MetricsFeatureProvider mMetricsFeatureProvider;

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DreamPickerController(Context context, String str) {
        this(context, str, DreamBackend.getInstance(context));
    }

    public DreamPickerController(Context context, String str, DreamBackend dreamBackend) {
        super(context, str);
        this.mBackend = dreamBackend;
        List<DreamBackend.DreamInfo> dreamInfos = dreamBackend.getDreamInfos();
        this.mDreamInfos = dreamInfos;
        this.mActiveDream = getActiveDreamInfo(dreamInfos);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public int getAvailabilityStatus() {
        return this.mDreamInfos.size() > 0 ? 0 : 2;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        DreamAdapter dreamAdapter = new DreamAdapter(R$layout.dream_preference_layout, (List) this.mDreamInfos.stream().map(new DreamPickerController$$ExternalSyntheticLambda1(this)).collect(Collectors.toList()));
        this.mAdapter = dreamAdapter;
        dreamAdapter.setEnabled(this.mBackend.isEnabled());
        LayoutPreference layoutPreference = (LayoutPreference) preferenceScreen.findPreference(getPreferenceKey());
        if (layoutPreference != null) {
            RecyclerView recyclerView = (RecyclerView) layoutPreference.findViewById(R$id.dream_list);
            recyclerView.setLayoutManager(new AutoFitGridLayoutManager(this.mContext));
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(this.mContext, R$dimen.dream_preference_card_padding));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(this.mAdapter);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ DreamItem lambda$displayPreference$0(DreamBackend.DreamInfo dreamInfo) {
        return new DreamItem(dreamInfo);
    }

    public void updateState(Preference preference) {
        super.updateState(preference);
        DreamAdapter dreamAdapter = this.mAdapter;
        if (dreamAdapter != null) {
            dreamAdapter.setEnabled(preference.isEnabled());
        }
    }

    private static DreamBackend.DreamInfo getActiveDreamInfo(List<DreamBackend.DreamInfo> list) {
        return (DreamBackend.DreamInfo) list.stream().filter(new DreamPickerController$$ExternalSyntheticLambda0()).findFirst().orElse((Object) null);
    }

    private class DreamItem implements IDreamItem {
        DreamBackend.DreamInfo mDreamInfo;

        DreamItem(DreamBackend.DreamInfo dreamInfo) {
            this.mDreamInfo = dreamInfo;
        }

        public CharSequence getTitle() {
            return this.mDreamInfo.caption;
        }

        public CharSequence getSummary() {
            return this.mDreamInfo.description;
        }

        public Drawable getIcon() {
            return this.mDreamInfo.icon;
        }

        public void onItemClicked() {
            DreamPickerController.this.mActiveDream = this.mDreamInfo;
            DreamPickerController.this.mBackend.setActiveDream(this.mDreamInfo.componentName);
            DreamPickerController.this.mMetricsFeatureProvider.action(0, 1788, 0, this.mDreamInfo.componentName.flattenToString(), 1);
        }

        public void onCustomizeClicked() {
            DreamPickerController.this.mBackend.launchSettings(DreamPickerController.this.mContext, this.mDreamInfo);
        }

        public Drawable getPreviewImage() {
            return this.mDreamInfo.previewImage;
        }

        public boolean isActive() {
            if (!DreamPickerController.this.mAdapter.getEnabled() || DreamPickerController.this.mActiveDream == null) {
                return false;
            }
            return this.mDreamInfo.componentName.equals(DreamPickerController.this.mActiveDream.componentName);
        }

        public boolean allowCustomization() {
            return isActive() && this.mDreamInfo.settingsComponentName != null;
        }
    }
}
