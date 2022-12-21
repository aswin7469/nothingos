package com.android.systemui.tuner;

import android.content.Context;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.AttributeSet;
import androidx.preference.DropDownPreference;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.tuner.TunerService;

public class ClockPreference extends DropDownPreference implements TunerService.Tunable {
    private static final String DEFAULT = "default";
    private static final String DISABLED = "disabled";
    private static final String SECONDS = "seconds";
    private final String mClock;
    private boolean mClockEnabled;
    private boolean mHasSeconds;
    private boolean mHasSetValue;
    private ArraySet<String> mHideList;
    private boolean mReceivedClock;
    private boolean mReceivedSeconds;

    public ClockPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mClock = context.getString(17041561);
        setEntryValues(new CharSequence[]{SECONDS, DEFAULT, DISABLED});
    }

    public void onAttached() {
        super.onAttached();
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, StatusBarIconController.ICON_HIDE_LIST, Clock.CLOCK_SECONDS);
    }

    public void onDetached() {
        ((TunerService) Dependency.get(TunerService.class)).removeTunable(this);
        super.onDetached();
    }

    public void onTuningChanged(String str, String str2) {
        if (StatusBarIconController.ICON_HIDE_LIST.equals(str)) {
            this.mReceivedClock = true;
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(getContext(), str2);
            this.mHideList = iconHideList;
            this.mClockEnabled = !iconHideList.contains(this.mClock);
        } else if (Clock.CLOCK_SECONDS.equals(str)) {
            this.mReceivedSeconds = true;
            this.mHasSeconds = (str2 == null || Integer.parseInt(str2) == 0) ? false : true;
        }
        if (!this.mHasSetValue && this.mReceivedClock && this.mReceivedSeconds) {
            this.mHasSetValue = true;
            boolean z = this.mClockEnabled;
            if (z && this.mHasSeconds) {
                setValue(SECONDS);
            } else if (z) {
                setValue(DEFAULT);
            } else {
                setValue(DISABLED);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean persistString(String str) {
        ((TunerService) Dependency.get(TunerService.class)).setValue(Clock.CLOCK_SECONDS, SECONDS.equals(str) ? 1 : 0);
        if (DISABLED.equals(str)) {
            this.mHideList.add(this.mClock);
        } else {
            this.mHideList.remove(this.mClock);
        }
        ((TunerService) Dependency.get(TunerService.class)).setValue(StatusBarIconController.ICON_HIDE_LIST, TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, this.mHideList));
        return true;
    }
}
