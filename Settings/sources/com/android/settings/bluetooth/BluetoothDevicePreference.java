package com.android.settings.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.UserManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.util.TypedValue;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$dimen;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.widget.GearPreference;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.nothing.settings.bluetooth.NothingBluetoothUtil;
import com.nothing.settings.panel.PanelCircleDrawable;

public final class BluetoothDevicePreference extends GearPreference {
    private static int sDimAlpha = Integer.MIN_VALUE;
    private String contentDescription = null;
    /* access modifiers changed from: private */
    public final CachedBluetoothDevice mCachedDevice;
    final BluetoothDevicePreferenceCallback mCallback;
    private final long mCurrentTime;
    private AlertDialog mDisconnectDialog;
    private boolean mHideSecondTarget = false;
    private final boolean mHideSummary;
    private boolean mIsCallbackRemoved = false;
    private String mLastAirpodsVersion;
    boolean mNeedNotifyHierarchyChanged = false;
    private Drawable mNtEarIcon = null;
    Resources mResources = getContext().getResources();
    private final boolean mShowDevicesWithoutNames;
    private final int mType;
    private final UserManager mUserManager;

    private class BluetoothDevicePreferenceCallback implements CachedBluetoothDevice.Callback {
        private BluetoothDevicePreferenceCallback() {
        }

        public void onDeviceAttributesChanged() {
            BluetoothDevicePreference.this.onPreferenceAttributesChanged();
        }
    }

    public BluetoothDevicePreference(Context context, CachedBluetoothDevice cachedBluetoothDevice, boolean z, int i) {
        super(context, (AttributeSet) null);
        this.mUserManager = (UserManager) context.getSystemService("user");
        this.mShowDevicesWithoutNames = z;
        if (sDimAlpha == Integer.MIN_VALUE) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16842803, typedValue, true);
            sDimAlpha = (int) (typedValue.getFloat() * 255.0f);
        }
        this.mCachedDevice = cachedBluetoothDevice;
        BluetoothDevicePreferenceCallback bluetoothDevicePreferenceCallback = new BluetoothDevicePreferenceCallback();
        this.mCallback = bluetoothDevicePreferenceCallback;
        cachedBluetoothDevice.registerCallback(bluetoothDevicePreferenceCallback);
        this.mCurrentTime = System.currentTimeMillis();
        this.mType = i;
        onPreferenceAttributesChanged();
        this.mHideSummary = false;
    }

    public BluetoothDevicePreference(Context context, CachedBluetoothDevice cachedBluetoothDevice, boolean z, int i, boolean z2) {
        super(context, (AttributeSet) null);
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        this.mShowDevicesWithoutNames = z;
        if (sDimAlpha == Integer.MIN_VALUE) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16842803, typedValue, true);
            sDimAlpha = (int) (typedValue.getFloat() * 255.0f);
        }
        this.mCachedDevice = cachedBluetoothDevice;
        BluetoothDevicePreferenceCallback bluetoothDevicePreferenceCallback = new BluetoothDevicePreferenceCallback();
        this.mCallback = bluetoothDevicePreferenceCallback;
        cachedBluetoothDevice.registerCallback(bluetoothDevicePreferenceCallback);
        this.mCurrentTime = System.currentTimeMillis();
        this.mType = i;
        this.mHideSummary = z2;
        onPreferenceAttributesChanged();
    }

    public void setNeedNotifyHierarchyChanged(boolean z) {
        this.mNeedNotifyHierarchyChanged = z;
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        CachedBluetoothDevice cachedBluetoothDevice = this.mCachedDevice;
        return cachedBluetoothDevice == null || cachedBluetoothDevice.getBondState() != 12 || this.mUserManager.hasUserRestriction("no_config_bluetooth") || this.mHideSecondTarget;
    }

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R$layout.preference_widget_gear;
    }

    /* access modifiers changed from: package-private */
    public CachedBluetoothDevice getCachedDevice() {
        return this.mCachedDevice;
    }

    /* access modifiers changed from: protected */
    public void onPrepareForRemoval() {
        super.onPrepareForRemoval();
        if (!this.mIsCallbackRemoved) {
            this.mCachedDevice.unregisterCallback(this.mCallback);
            this.mIsCallbackRemoved = true;
        }
        AlertDialog alertDialog = this.mDisconnectDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mDisconnectDialog = null;
        }
    }

    public void onAttached() {
        super.onAttached();
        if (this.mIsCallbackRemoved) {
            this.mCachedDevice.registerCallback(this.mCallback);
            this.mIsCallbackRemoved = false;
        }
        onPreferenceAttributesChanged();
    }

    public void onDetached() {
        super.onDetached();
        if (!this.mIsCallbackRemoved) {
            this.mCachedDevice.unregisterCallback(this.mCallback);
            this.mIsCallbackRemoved = true;
        }
    }

    public CachedBluetoothDevice getBluetoothDevice() {
        return this.mCachedDevice;
    }

    public void hideSecondTarget(boolean z) {
        this.mHideSecondTarget = z;
    }

    /* access modifiers changed from: package-private */
    public void onPreferenceAttributesChanged() {
        Pair<Drawable, String> drawableWithDescription = this.mCachedDevice.getDrawableWithDescription();
        String modeID = NothingBluetoothUtil.getinstance().getModeID(getContext(), this.mCachedDevice.getAddress());
        if (TextUtils.isEmpty(modeID) || this.mNtEarIcon != null) {
            boolean isSupportAirpods = NothingBluetoothUtil.getinstance().isSupportAirpods(getContext(), this.mCachedDevice.getAddress());
            String airpodsVersion = NothingBluetoothUtil.getinstance().getAirpodsVersion(getContext(), this.mCachedDevice.getAddress());
            if (!TextUtils.equals(airpodsVersion, this.mLastAirpodsVersion) && isSupportAirpods) {
                this.mNtEarIcon = NothingBluetoothUtil.getinstance().getModuleIDBitmap(getContext(), airpodsVersion);
                this.mLastAirpodsVersion = airpodsVersion;
            }
        } else {
            this.mNtEarIcon = NothingBluetoothUtil.getinstance().getModuleIDBitmap(getContext(), modeID);
        }
        setIcon((Drawable) this.mNtEarIcon != null ? new PanelCircleDrawable(this.mNtEarIcon) : new PanelCircleDrawable((Drawable) drawableWithDescription.first));
        this.contentDescription = (String) drawableWithDescription.second;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null || !this.mCachedDevice.getAddress().equals(defaultAdapter.getAddress())) {
            setTitle((CharSequence) this.mCachedDevice.getName());
        } else {
            setTitle((CharSequence) defaultAdapter.getName() + "(self)");
        }
        if (!this.mHideSummary) {
            setSummary((CharSequence) this.mCachedDevice.getConnectionSummary());
        }
        boolean z = true;
        if (this.mHideSummary) {
            setEnabled(true);
        } else {
            setEnabled(!this.mCachedDevice.isBusy());
        }
        if (!this.mShowDevicesWithoutNames && !this.mCachedDevice.hasHumanReadableName()) {
            z = false;
        }
        setVisible(z);
        if (this.mNeedNotifyHierarchyChanged) {
            notifyHierarchyChanged();
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        ImageView imageView;
        if (findPreferenceInHierarchy("bt_checkbox") != null) {
            setDependency("bt_checkbox");
        }
        if (this.mCachedDevice.getBondState() == 12 && (imageView = (ImageView) preferenceViewHolder.findViewById(R$id.settings_button)) != null) {
            imageView.setOnClickListener(this);
        }
        ImageView imageView2 = (ImageView) preferenceViewHolder.findViewById(16908294);
        if (imageView2 != null) {
            imageView2.setContentDescription(this.contentDescription);
            imageView2.setImportantForAccessibility(2);
            imageView2.setElevation(getContext().getResources().getDimension(R$dimen.bt_icon_elevation));
            setIconSize(1);
        }
        super.onBindViewHolder(preferenceViewHolder);
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BluetoothDevicePreference)) {
            return false;
        }
        return this.mCachedDevice.equals(((BluetoothDevicePreference) obj).mCachedDevice);
    }

    public int hashCode() {
        return this.mCachedDevice.hashCode();
    }

    public int compareTo(Preference preference) {
        if (!(preference instanceof BluetoothDevicePreference)) {
            return super.compareTo(preference);
        }
        int i = this.mType;
        if (i == 1) {
            return this.mCachedDevice.compareTo(((BluetoothDevicePreference) preference).mCachedDevice);
        }
        if (i != 2) {
            return super.compareTo(preference);
        }
        if (this.mCurrentTime > ((BluetoothDevicePreference) preference).mCurrentTime) {
            return 1;
        }
        return -1;
    }

    /* access modifiers changed from: package-private */
    public void onClicked() {
        Context context = getContext();
        int bondState = this.mCachedDevice.getBondState();
        MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
        if (this.mCachedDevice.isConnected()) {
            metricsFeatureProvider.action(context, 868, (Pair<Integer, Object>[]) new Pair[0]);
            askDisconnect();
        } else if (bondState == 12) {
            metricsFeatureProvider.action(context, 867, (Pair<Integer, Object>[]) new Pair[0]);
            this.mCachedDevice.connect();
        } else if (bondState == 10) {
            metricsFeatureProvider.action(context, 866, (Pair<Integer, Object>[]) new Pair[0]);
            if (!this.mCachedDevice.hasHumanReadableName()) {
                metricsFeatureProvider.action(context, 1096, (Pair<Integer, Object>[]) new Pair[0]);
            }
            pair();
        }
    }

    private void askDisconnect() {
        Context context = getContext();
        String name = this.mCachedDevice.getName();
        if (TextUtils.isEmpty(name)) {
            name = context.getString(R$string.bluetooth_device);
        }
        String string = context.getString(R$string.bluetooth_disconnect_all_profiles, new Object[]{name});
        String string2 = context.getString(R$string.bluetooth_disconnect_title);
        this.mDisconnectDialog = Utils.showDisconnectDialog(context, this.mDisconnectDialog, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                BluetoothDevicePreference.this.mCachedDevice.disconnect();
            }
        }, string2, Html.fromHtml(string));
    }

    private void pair() {
        if (!this.mCachedDevice.startPairing()) {
            Utils.showError(getContext(), this.mCachedDevice.getName(), R$string.bluetooth_pairing_error_message);
        } else {
            NothingBluetoothUtil.getinstance().saveConnectedDevice(getContext(), this.mCachedDevice.getAddress());
        }
    }
}
