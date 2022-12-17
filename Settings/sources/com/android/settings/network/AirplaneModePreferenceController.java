package com.android.settings.network;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.AirplaneModeEnabler;
import com.android.settings.R$bool;
import com.android.settings.R$string;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.io.IOException;

public class AirplaneModePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop, OnDestroy, AirplaneModeEnabler.OnAirplaneModeChangedListener {
    public static final int REQUEST_CODE_EXIT_ECM = 1;
    public static final int REQUEST_CODE_EXIT_SCBM = 2;
    public static final Uri SLICE_URI = new Uri.Builder().scheme("content").authority("android.settings.slices").appendPath("action").appendPath("airplane_mode").build();
    private AirplaneModeEnabler mAirplaneModeEnabler;
    private SwitchPreference mAirplaneModePreference;
    private Fragment mFragment;

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public boolean isPublicSlice() {
        return true;
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public AirplaneModePreferenceController(Context context, String str) {
        super(context, str);
        if (isAvailable(this.mContext)) {
            this.mAirplaneModeEnabler = new AirplaneModeEnabler(this.mContext, this);
        }
    }

    public void setFragment(Fragment fragment) {
        this.mFragment = fragment;
    }

    /* access modifiers changed from: package-private */
    public void setAirplaneModeEnabler(AirplaneModeEnabler airplaneModeEnabler) {
        this.mAirplaneModeEnabler = airplaneModeEnabler;
    }

    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"airplane_mode".equals(preference.getKey())) {
            return false;
        }
        if (this.mAirplaneModeEnabler.isInEcmMode()) {
            Fragment fragment = this.mFragment;
            if (fragment != null) {
                fragment.startActivityForResult(new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS", (Uri) null), 1);
            }
            return true;
        } else if (!this.mAirplaneModeEnabler.isInScbm()) {
            return false;
        } else {
            Fragment fragment2 = this.mFragment;
            if (fragment2 != null) {
                fragment2.startActivityForResult(new Intent("org.codeaurora.intent.action.SHOW_NOTICE_SCM_BLOCK_OTHERS", (Uri) null), 2);
            }
            return true;
        }
    }

    public Uri getSliceUri() {
        return SLICE_URI;
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mAirplaneModePreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public static boolean isAvailable(Context context) {
        return context.getResources().getBoolean(R$bool.config_show_toggle_airplane) && !context.getPackageManager().hasSystemFeature("android.software.leanback");
    }

    public int getAvailabilityStatus() {
        return isAvailable(this.mContext) ? 0 : 3;
    }

    public int getSliceHighlightMenuRes() {
        return R$string.menu_key_network;
    }

    public void onStart() {
        if (isAvailable()) {
            this.mAirplaneModeEnabler.start();
        }
    }

    public void onStop() {
        if (isAvailable()) {
            this.mAirplaneModeEnabler.stop();
        }
    }

    public void onDestroy() {
        this.mAirplaneModeEnabler.close();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        boolean z = false;
        if (i == 1) {
            if (i2 == -1) {
                z = true;
            }
            this.mAirplaneModeEnabler.setAirplaneModeInEmergencyMode(z, this.mAirplaneModePreference.isChecked());
        } else if (i == 2) {
            if (i2 == -1) {
                z = true;
            }
            this.mAirplaneModeEnabler.setAirplaneModeInEmergencyMode(z, this.mAirplaneModePreference.isChecked());
        }
    }

    public boolean isChecked() {
        return this.mAirplaneModeEnabler.isAirplaneModeOn();
    }

    public boolean setChecked(boolean z) {
        if (isChecked() == z) {
            return false;
        }
        this.mAirplaneModeEnabler.setAirplaneMode(z);
        return true;
    }

    public void onAirplaneModeChanged(boolean z) {
        SwitchPreference switchPreference = this.mAirplaneModePreference;
        if (switchPreference != null) {
            switchPreference.setChecked(z);
        }
    }

    public Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return AirplaneModeSliceWorker.class;
    }

    public static class AirplaneModeSliceWorker extends SliceBackgroundWorker<Void> {
        private AirplaneModeContentObserver mContentObserver = new AirplaneModeContentObserver(new Handler(Looper.getMainLooper()), this);

        public AirplaneModeSliceWorker(Context context, Uri uri) {
            super(context, uri);
        }

        /* access modifiers changed from: protected */
        public void onSlicePinned() {
            this.mContentObserver.register(getContext());
        }

        /* access modifiers changed from: protected */
        public void onSliceUnpinned() {
            this.mContentObserver.unRegister(getContext());
        }

        public void close() throws IOException {
            this.mContentObserver = null;
        }

        public void updateSlice() {
            notifySliceChange();
        }

        public class AirplaneModeContentObserver extends ContentObserver {
            private final AirplaneModeSliceWorker mSliceBackgroundWorker;

            public AirplaneModeContentObserver(Handler handler, AirplaneModeSliceWorker airplaneModeSliceWorker) {
                super(handler);
                this.mSliceBackgroundWorker = airplaneModeSliceWorker;
            }

            public void onChange(boolean z) {
                this.mSliceBackgroundWorker.updateSlice();
            }

            public void register(Context context) {
                context.getContentResolver().registerContentObserver(Settings.Global.getUriFor("airplane_mode_on"), false, this);
            }

            public void unRegister(Context context) {
                context.getContentResolver().unregisterContentObserver(this);
            }
        }
    }
}
