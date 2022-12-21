package com.android.systemui.p012qs.tiles;

import android.content.Intent;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.NightDisplayListener;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.NightDisplayListenerModule;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.LocationController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.biometrics.NTColorController;
import java.text.DateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.TimeZone;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.tiles.NightDisplayTile */
public class NightDisplayTile extends QSTileImpl<QSTile.BooleanState> implements NightDisplayListener.Callback {
    private static final String PATTERN_HOUR = "h a";
    private static final String PATTERN_HOUR_MINUTE = "h:mm a";
    private static final String PATTERN_HOUR_NINUTE_24 = "HH:mm";
    private boolean mIsListening;
    private boolean mIsUnavailable = false;
    private NightDisplayListener mListener;
    private final LocationController mLocationController;
    private ColorDisplayManager mManager;
    private final NightDisplayListenerModule.Builder mNightDisplayListenerBuilder;

    public int getMetricsCategory() {
        return 491;
    }

    @Inject
    public NightDisplayTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, LocationController locationController, ColorDisplayManager colorDisplayManager, NightDisplayListenerModule.Builder builder) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mLocationController = locationController;
        this.mManager = colorDisplayManager;
        this.mNightDisplayListenerBuilder = builder;
        this.mListener = builder.setUser(qSHost.getUserContext().getUserId()).build();
    }

    public boolean isAvailable() {
        return ColorDisplayManager.isNightDisplayAvailable(this.mContext);
    }

    public QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        if (!this.mIsUnavailable) {
            if ("1".equals(Settings.Global.getString(this.mContext.getContentResolver(), "night_display_forced_auto_mode_available")) && this.mManager.getNightDisplayAutoModeRaw() == -1) {
                this.mManager.setNightDisplayAutoMode(1);
                Log.i("NightDisplayTile", "Enrolled in forced night display auto mode");
            }
            this.mManager.setNightDisplayActivated(!((QSTile.BooleanState) this.mState).value);
        }
    }

    /* access modifiers changed from: protected */
    public void handleUserSwitch(int i) {
        if (this.mIsListening) {
            this.mListener.setCallback((NightDisplayListener.Callback) null);
        }
        this.mManager = (ColorDisplayManager) getHost().getUserContext().getSystemService(ColorDisplayManager.class);
        NightDisplayListener build = this.mNightDisplayListenerBuilder.setUser(i).build();
        this.mListener = build;
        if (this.mIsListening) {
            build.setCallback(this);
        }
        super.handleUserSwitch(i);
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        CharSequence charSequence;
        booleanState.value = this.mManager.isNightDisplayActivated();
        booleanState.label = this.mContext.getString(C1893R.string.quick_settings_night_display_label);
        booleanState.icon = QSTileImpl.ResourceIcon.get(17302833);
        booleanState.expandedAccessibilityClassName = Switch.class.getName();
        booleanState.state = booleanState.value ? 2 : 1;
        booleanState.secondaryLabel = getSecondaryLabel(booleanState.value);
        if (TextUtils.isEmpty(booleanState.secondaryLabel)) {
            charSequence = booleanState.label;
        } else {
            charSequence = TextUtils.concat(new CharSequence[]{booleanState.label, ", ", booleanState.secondaryLabel});
        }
        booleanState.contentDescription = charSequence;
        boolean isColorControlled = ((NTColorController) NTDependencyEx.get(NTColorController.class)).isColorControlled();
        this.mIsUnavailable = isColorControlled;
        if (isColorControlled) {
            booleanState.state = 0;
        }
    }

    private String getSecondaryLabel(boolean z) {
        LocalTime localTime;
        int i;
        int nightDisplayAutoMode = this.mManager.getNightDisplayAutoMode();
        if (nightDisplayAutoMode == 1) {
            if (z) {
                localTime = this.mManager.getNightDisplayCustomEndTime();
                i = C1893R.string.quick_settings_secondary_label_until;
            } else {
                localTime = this.mManager.getNightDisplayCustomStartTime();
                i = C1893R.string.quick_settings_night_secondary_label_on_at;
            }
            Calendar instance = Calendar.getInstance();
            DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(this.mContext);
            timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            instance.setTimeZone(timeFormat.getTimeZone());
            instance.set(11, localTime.getHour());
            instance.set(12, localTime.getMinute());
            instance.set(13, 0);
            instance.set(14, 0);
            return this.mContext.getString(i, new Object[]{timeFormat.format(instance.getTime())});
        } else if (nightDisplayAutoMode != 2 || !this.mLocationController.isLocationEnabled()) {
            return null;
        } else {
            if (z) {
                return this.mContext.getString(C1893R.string.quick_settings_night_secondary_label_until_sunrise);
            }
            return this.mContext.getString(C1893R.string.quick_settings_night_secondary_label_on_at_sunset);
        }
    }

    public LogMaker populate(LogMaker logMaker) {
        return super.populate(logMaker).addTaggedData(1311, Integer.valueOf(this.mManager.getNightDisplayAutoModeRaw()));
    }

    public Intent getLongClickIntent() {
        return new Intent("android.settings.NIGHT_DISPLAY_SETTINGS");
    }

    /* access modifiers changed from: protected */
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        this.mIsListening = z;
        if (z) {
            this.mListener.setCallback(this);
            refreshState();
            return;
        }
        this.mListener.setCallback((NightDisplayListener.Callback) null);
    }

    public CharSequence getTileLabel() {
        return this.mContext.getString(C1893R.string.quick_settings_night_display_label);
    }

    public void onActivated(boolean z) {
        refreshState();
    }
}
