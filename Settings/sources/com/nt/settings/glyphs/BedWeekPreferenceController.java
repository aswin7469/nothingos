package com.nt.settings.glyphs;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.nt.settings.glyphs.utils.BedTimeFormatter;
import com.nt.settings.glyphs.widget.WeekCheckBoxPreference;
/* loaded from: classes2.dex */
public class BedWeekPreferenceController extends BasePreferenceController implements WeekCheckBoxPreference.OnSelectedChangeListener {
    private static final String KEY_REPEAT_SELECT_DEC = "key_repeat_select_dec";
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
    private BedTimeFormatter mTimeFormatter;
    private Preference mTitlePreference;

    @Override // com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return 0;
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

    public BedWeekPreferenceController(Context context, String str) {
        super(context, str);
        this.mTimeFormatter = new BedTimeFormatter(context);
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mTitlePreference = preferenceScreen.findPreference(KEY_REPEAT_SELECT_DEC);
        WeekCheckBoxPreference weekCheckBoxPreference = (WeekCheckBoxPreference) preferenceScreen.findPreference(getPreferenceKey());
        weekCheckBoxPreference.setOnSelectedChangeListener(this);
        updateState(weekCheckBoxPreference);
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public final void updateState(Preference preference) {
        ((WeekCheckBoxPreference) preference).setCheckedStatus(str2Array(getStatus()));
        Preference preference2 = this.mTitlePreference;
        if (preference2 != null) {
            preference2.setTitle(this.mTimeFormatter.getWeekSelectedTitle(this.mContext, getStatus()));
        }
    }

    public String getStatus() {
        return Settings.Global.getString(this.mContentResolver, "led_bed_time_custom_week");
    }

    private int[] str2Array(String str) {
        int[] iArr = new int[7];
        if (str != null && str.length() != 0) {
            char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(charArray[i]));
            }
        }
        return iArr;
    }

    private String array2Str(int[] iArr) {
        String str = "";
        for (int i = 0; i < iArr.length; i++) {
            str = str + iArr[i];
        }
        return str;
    }

    @Override // com.nt.settings.glyphs.widget.WeekCheckBoxPreference.OnSelectedChangeListener
    public void onSelectedChange(int[] iArr) {
        String array2Str = array2Str(iArr);
        Settings.Global.putString(this.mContentResolver, "led_bed_time_custom_week", array2Str);
        Preference preference = this.mTitlePreference;
        if (preference != null) {
            preference.setTitle(this.mTimeFormatter.getWeekSelectedTitle(this.mContext, array2Str));
        }
    }
}
