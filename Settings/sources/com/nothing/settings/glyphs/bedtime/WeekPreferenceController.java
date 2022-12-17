package com.nothing.settings.glyphs.bedtime;

import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.core.BasePreferenceController;
import com.nothing.settings.glyphs.preference.WeekCheckBoxPreference;
import com.nothing.settings.glyphs.utils.BedTimeFormatter;
import com.nothing.settings.utils.NtUtils;

public class WeekPreferenceController extends BasePreferenceController implements WeekCheckBoxPreference.OnSelectedChangeListener {
    private static final String KEY_REPEAT_SELECT_DEC = "key_repeat_select_dec";
    private final ContentResolver mContentResolver = this.mContext.getContentResolver();
    private final BedTimeFormatter mTimeFormatter;
    private Preference mTitlePreference;

    public int getAvailabilityStatus() {
        return 0;
    }

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

    public WeekPreferenceController(Context context, String str) {
        super(context, str);
        this.mTimeFormatter = new BedTimeFormatter(context);
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mTitlePreference = preferenceScreen.findPreference(KEY_REPEAT_SELECT_DEC);
        WeekCheckBoxPreference weekCheckBoxPreference = (WeekCheckBoxPreference) preferenceScreen.findPreference(getPreferenceKey());
        weekCheckBoxPreference.setOnSelectedChangeListener(this);
        updateState(weekCheckBoxPreference);
    }

    public final void updateState(Preference preference) {
        ((WeekCheckBoxPreference) preference).setCheckedStatus(str2Array(getStatus()));
        Preference preference2 = this.mTitlePreference;
        if (preference2 != null) {
            preference2.setTitle((CharSequence) this.mTimeFormatter.getWeekSelectedTitle(this.mContext, getStatus()));
        }
    }

    public String getStatus() {
        return Settings.Global.getString(this.mContentResolver, "led_bed_time_custom_week");
    }

    private int[] str2Array(String str) {
        int[] iArr = new int[7];
        if (!(str == null || str.length() == 0)) {
            char[] charArray = str.toCharArray();
            for (int i = 0; i < charArray.length; i++) {
                iArr[i] = Integer.parseInt(String.valueOf(charArray[i]));
            }
        }
        return iArr;
    }

    private String array2String(int[] iArr) {
        StringBuilder sb = new StringBuilder();
        for (int append : iArr) {
            sb.append(append);
        }
        return sb.toString();
    }

    public void onSelectedChange(int[] iArr) {
        NtUtils.trackIntArrayGlyph(this.mContext, "bts_repeat", iArr);
        String array2String = array2String(iArr);
        Settings.Global.putString(this.mContentResolver, "led_bed_time_custom_week", array2String);
        Preference preference = this.mTitlePreference;
        if (preference != null) {
            preference.setTitle((CharSequence) this.mTimeFormatter.getWeekSelectedTitle(this.mContext, array2String));
        }
    }
}
