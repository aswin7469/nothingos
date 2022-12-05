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
import com.android.settings.R;
import com.android.settings.core.TogglePreferenceController;
import com.android.settings.slices.SliceBackgroundWorker;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnDestroy;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.io.IOException;
/* loaded from: classes.dex */
public class AirplaneModePreferenceController extends TogglePreferenceController implements LifecycleObserver, OnStart, OnStop, OnDestroy, AirplaneModeEnabler.OnAirplaneModeChangedListener {
    private static final String EXIT_ECM_RESULT = "exit_ecm_result";
    public static final int REQUEST_CODE_EXIT_ECM = 1;
    public static final Uri SLICE_URI = new Uri.Builder().scheme("content").authority("android.settings.slices").appendPath("action").appendPath("airplane_mode").build();
    private AirplaneModeEnabler mAirplaneModeEnabler;
    private SwitchPreference mAirplaneModePreference;
    private Fragment mFragment;

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ void copy() {
        super.copy();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public /* bridge */ /* synthetic */ boolean isCopyableSlice() {
        return super.isCopyableSlice();
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public boolean isPublicSlice() {
        return true;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
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

    void setAirplaneModeEnabler(AirplaneModeEnabler airplaneModeEnabler) {
        this.mAirplaneModeEnabler = airplaneModeEnabler;
    }

    @Override // com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!"airplane_mode".equals(preference.getKey()) || !this.mAirplaneModeEnabler.isInEcmMode()) {
            return false;
        }
        Fragment fragment = this.mFragment;
        if (fragment != null) {
            fragment.startActivityForResult(new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS", (Uri) null), 1);
        }
        return true;
    }

    @Override // com.android.settings.core.BasePreferenceController
    public Uri getSliceUri() {
        return SLICE_URI;
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.core.BasePreferenceController, com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        this.mAirplaneModePreference = (SwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
    }

    public static boolean isAvailable(Context context) {
        return context.getResources().getBoolean(R.bool.config_show_toggle_airplane) && !context.getPackageManager().hasSystemFeature("android.software.leanback");
    }

    @Override // com.android.settings.core.BasePreferenceController
    public int getAvailabilityStatus() {
        return isAvailable(this.mContext) ? 0 : 3;
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        if (isAvailable()) {
            this.mAirplaneModeEnabler.start();
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        if (isAvailable()) {
            this.mAirplaneModeEnabler.stop();
        }
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnDestroy
    public void onDestroy() {
        this.mAirplaneModeEnabler.close();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            this.mAirplaneModeEnabler.setAirplaneModeInECM(intent.getBooleanExtra(EXIT_ECM_RESULT, false), this.mAirplaneModePreference.isChecked());
        }
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean isChecked() {
        return this.mAirplaneModeEnabler.isAirplaneModeOn();
    }

    @Override // com.android.settings.core.TogglePreferenceController
    public boolean setChecked(boolean z) {
        if (isChecked() == z) {
            return false;
        }
        this.mAirplaneModeEnabler.setAirplaneMode(z);
        return true;
    }

    @Override // com.android.settings.AirplaneModeEnabler.OnAirplaneModeChangedListener
    public void onAirplaneModeChanged(boolean z) {
        SwitchPreference switchPreference = this.mAirplaneModePreference;
        if (switchPreference != null) {
            switchPreference.setChecked(z);
        }
    }

    @Override // com.android.settings.core.TogglePreferenceController, com.android.settings.slices.Sliceable
    public Class<? extends SliceBackgroundWorker> getBackgroundWorkerClass() {
        return AirplaneModeSliceWorker.class;
    }

    /* loaded from: classes.dex */
    public static class AirplaneModeSliceWorker extends SliceBackgroundWorker<Void> {
        private AirplaneModeContentObserver mContentObserver = new AirplaneModeContentObserver(new Handler(Looper.getMainLooper()), this);

        public AirplaneModeSliceWorker(Context context, Uri uri) {
            super(context, uri);
        }

        @Override // com.android.settings.slices.SliceBackgroundWorker
        protected void onSlicePinned() {
            this.mContentObserver.register(getContext());
        }

        @Override // com.android.settings.slices.SliceBackgroundWorker
        protected void onSliceUnpinned() {
            this.mContentObserver.unRegister(getContext());
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            this.mContentObserver = null;
        }

        public void updateSlice() {
            notifySliceChange();
        }

        /* loaded from: classes.dex */
        public class AirplaneModeContentObserver extends ContentObserver {
            private final AirplaneModeSliceWorker mSliceBackgroundWorker;

            public AirplaneModeContentObserver(Handler handler, AirplaneModeSliceWorker airplaneModeSliceWorker) {
                super(handler);
                this.mSliceBackgroundWorker = airplaneModeSliceWorker;
            }

            @Override // android.database.ContentObserver
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
