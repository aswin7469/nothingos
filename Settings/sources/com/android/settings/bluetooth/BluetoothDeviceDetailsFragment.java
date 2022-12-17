package com.android.settings.bluetooth;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemProperties;
import android.provider.DeviceConfig;
import android.sysprop.BluetoothProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.slices.BlockingSlicePrefController;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.nothing.p005os.device.DeviceFunctionItem;
import com.nothing.p005os.device.DeviceServiceConnector;
import com.nothing.p005os.device.DeviceServiceController;
import com.nothing.p005os.device.IDeviceBitmap;
import com.nothing.settings.bluetooth.BluetoothPermissionPreference;
import com.nothing.settings.bluetooth.BluetoothSoundPreference;
import com.nothing.settings.bluetooth.NothingBluetoothPrefController;
import com.nothing.settings.bluetooth.NothingBluetoothUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BluetoothDeviceDetailsFragment extends RestrictedDashboardFragment {
    static int EDIT_DEVICE_NAME_ITEM_ID = 1;
    private static boolean mBAEnabled = false;
    private static boolean mBAPropertyChecked = false;
    static TestDataFactory sTestDataFactory;
    private Map<String, Integer> deviceFunctionItemsStyleCache = new HashMap();
    /* access modifiers changed from: private */
    public int mBatteryCase = -100;
    /* access modifiers changed from: private */
    public int mBatteryLeft = -100;
    /* access modifiers changed from: private */
    public int mBatteryRight = -100;
    CachedBluetoothDevice mCachedDevice;
    /* access modifiers changed from: private */
    public Map<String, Preference> mControllerPreferenceS = new HashMap();
    String mDeviceAddress;
    /* access modifiers changed from: private */
    public DeviceServiceController mDeviceControl;
    private DeviceServiceConnector.Callback mDeviceServiceConnectorCallback = new DeviceServiceConnector.Callback() {
        public void onDeviceServiceConnected() {
            try {
                String modeID = NothingBluetoothUtil.getinstance().getModeID(BluetoothDeviceDetailsFragment.this.getContext(), BluetoothDeviceDetailsFragment.this.mCachedDevice.getAddress());
                if (modeID == null) {
                    modeID = NothingBluetoothUtil.getinstance().getConnectedDevice(BluetoothDeviceDetailsFragment.this.getContext(), BluetoothDeviceDetailsFragment.this.mCachedDevice.getAddress());
                }
                if (modeID == null) {
                    modeID = "";
                }
                Log.d("BTDeviceDetailsFrg", "onDeviceServiceConnected():modeID = " + modeID + ", mIsAirpodsDevice:" + BluetoothDeviceDetailsFragment.this.mIsAirpodsDevice);
                BluetoothDeviceDetailsFragment.this.mDeviceControl.setModelIdAndDeviceConnected(modeID, BluetoothDeviceDetailsFragment.this.mCachedDevice.getDevice(), BluetoothDeviceDetailsFragment.this.mCachedDevice.isConnected());
                Bundle bundle = new Bundle();
                bundle.putString("device_address", BluetoothDeviceDetailsFragment.this.mCachedDevice.getAddress());
                BluetoothDeviceDetailsFragment.this.mDeviceControl.setCommand(8, bundle);
                if (BluetoothDeviceDetailsFragment.this.mIsAirpodsDevice) {
                    bundle.putBoolean("KEY_EAR_CONNECTED", BluetoothDeviceDetailsFragment.this.mCachedDevice.isConnected());
                    bundle.putBoolean("KEY_IS_AIRPODS", true);
                    BluetoothDeviceDetailsFragment.this.mDeviceControl.getCommand(3, bundle);
                    BluetoothDeviceDetailsFragment.this.mDeviceControl.getCommand(1, bundle);
                }
            } catch (Exception e) {
                Log.w("BTDeviceDetailsFrg", e.getStackTrace().toString());
            }
        }

        public void onDeviceServiceDisConnected() {
            Log.d("BTDeviceDetailsFrg", "onDeviceServiceDisConnected()");
        }

        public void onSuccess(int i, Bundle bundle) {
            Class<NothingBluetoothPrefController> cls = NothingBluetoothPrefController.class;
            Class<AdvancedBluetoothDetailsHeaderController> cls2 = AdvancedBluetoothDetailsHeaderController.class;
            if (bundle == null) {
                Log.d("BTDeviceDetailsFrg", "onSuccess bundle null");
                return;
            }
            String string = bundle.getString("device_address");
            boolean btPanelShow = NothingBluetoothUtil.getinstance().getBtPanelShow();
            if (string == null || !string.equals(BluetoothDeviceDetailsFragment.this.mCachedDevice.getAddress())) {
                Log.d("BTDeviceDetailsFrg", "onSuccess KEY_MAC_ADDRESS null or don't equal command:" + i);
            } else if (i == 0) {
                Log.d("BTDeviceDetailsFrg", "onSuccess DeviceConstant.CONNECT, isConnected:" + BluetoothDeviceDetailsFragment.this.mCachedDevice.isConnected() + ", isBtPanelShow:" + btPanelShow);
                if (!btPanelShow) {
                    BluetoothDeviceDetailsFragment.this.checkFastPairPreference();
                    BluetoothDeviceDetailsFragment.this.mIsConnectNothingDeviceService = true;
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("device_address", BluetoothDeviceDetailsFragment.this.mCachedDevice.getAddress());
                    BluetoothDeviceDetailsFragment.this.mDeviceControl.getCommand(1, bundle2);
                    BluetoothDeviceDetailsFragment.this.mDeviceControl.getCommand(3, bundle2);
                    ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).setIsConnectNothingDeviceService(BluetoothDeviceDetailsFragment.this.mIsConnectNothingDeviceService);
                    BluetoothDeviceDetailsFragment.this.mDeviceControl.getCommand(4, bundle2);
                }
            } else if (i == 1) {
                Log.d("BTDeviceDetailsFrg", "onSuccess DeviceConstant.GET_FUNCTION_LIST");
                BluetoothDeviceDetailsFragment.this.mFunList = bundle.getParcelableArrayList("KEY_FUNCTION_LIST");
                BluetoothDeviceDetailsFragment bluetoothDeviceDetailsFragment = BluetoothDeviceDetailsFragment.this;
                bluetoothDeviceDetailsFragment.parseData(bluetoothDeviceDetailsFragment.mFunList, 0);
                ((NothingBluetoothPrefController) BluetoothDeviceDetailsFragment.this.use(cls)).init(BluetoothDeviceDetailsFragment.this.mControllerPreferenceS);
                ((NothingBluetoothPrefController) BluetoothDeviceDetailsFragment.this.use(cls)).refresh();
                Bundle bundle3 = new Bundle();
                bundle3.putString("device_address", BluetoothDeviceDetailsFragment.this.mCachedDevice.getAddress());
                Iterator it = BluetoothDeviceDetailsFragment.this.mGetCommandList.iterator();
                while (it.hasNext()) {
                    BluetoothDeviceDetailsFragment.this.mDeviceControl.getCommand(((Integer) it.next()).intValue(), bundle3);
                }
                BluetoothDeviceDetailsFragment.this.scrollToPreference("bluetooth_top");
            } else if (i == 2) {
                BluetoothDeviceDetailsFragment.this.mIsConnectNothingDeviceService = false;
                ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).setIsConnectNothingDeviceService(BluetoothDeviceDetailsFragment.this.mIsConnectNothingDeviceService);
                ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).refresh();
                ((NothingBluetoothPrefController) BluetoothDeviceDetailsFragment.this.use(cls)).refresh();
                Log.d("BTDeviceDetailsFrg", "onSuccess DeviceConstant.DISCONNECT");
            } else if (i == 3) {
                int i2 = bundle.getInt("KEY_FORM_PAGE");
                Log.d("BTDeviceDetailsFrg", "onSuccess DeviceConstant.GET_EAR_BITMAP from:" + i2);
                if (i2 == 1) {
                    Log.d("BTDeviceDetailsFrg", "BTPanel bitmap return");
                    return;
                }
                IDeviceBitmap asInterface = IDeviceBitmap.Stub.asInterface(bundle.getBinder("KEY_BITMAP"));
                ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).setIDeviceBitmap(asInterface);
                ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).setIsInfoReceived(true);
                ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).refresh();
                try {
                    String modeID = NothingBluetoothUtil.getinstance().getModeID(BluetoothDeviceDetailsFragment.this.getContext(), BluetoothDeviceDetailsFragment.this.mCachedDevice.getAddress());
                    Bitmap defaultBitmap = asInterface.getDefaultBitmap();
                    if (defaultBitmap != null) {
                        NothingBluetoothUtil.getinstance().saveModuleIDEarBitmap(BluetoothDeviceDetailsFragment.this.getContext(), defaultBitmap, modeID);
                        if (BluetoothDeviceDetailsFragment.this.mIsAirpodsDevice) {
                            String airpodsVersion = NothingBluetoothUtil.getinstance().getAirpodsVersion(BluetoothDeviceDetailsFragment.this.getActivity(), BluetoothDeviceDetailsFragment.this.mCachedDevice.getAddress());
                            String string2 = bundle.getString("KEY_VALUE_STRING");
                            Log.d("BTDeviceDetailsFrg", "onSuccess DeviceConstant.GET_EAR_BITMAP version:" + string2 + ", savedVersion:" + airpodsVersion);
                            if (!TextUtils.equals(airpodsVersion, string2) && !TextUtils.isEmpty(string2)) {
                                NothingBluetoothUtil.getinstance().saveAirpodsVersion(BluetoothDeviceDetailsFragment.this.getActivity(), BluetoothDeviceDetailsFragment.this.mCachedDevice.getAddress(), string2);
                            }
                            NothingBluetoothUtil.getinstance().saveModuleIDEarBitmap(BluetoothDeviceDetailsFragment.this.getActivity(), defaultBitmap, string2);
                        }
                    }
                } catch (Exception unused) {
                }
            } else if (i != 4) {
                if (i == 740) {
                    BluetoothDeviceDetailsFragment.this.createOrRemovePermissionPreference(bundle);
                }
                BluetoothDeviceDetailsFragment.this.getRemoteDataAndUpdateUI(i, bundle);
            } else {
                int i3 = bundle.getInt("KEY_BATTERY_LEFT");
                int i4 = bundle.getInt("KEY_BATTERY_RIGHT");
                int i5 = bundle.getInt("KEY_BATTERY_CASE");
                if (!(BluetoothDeviceDetailsFragment.this.mBatteryLeft == i3 && BluetoothDeviceDetailsFragment.this.mBatteryRight == i4 && BluetoothDeviceDetailsFragment.this.mBatteryCase == i5)) {
                    ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).setBatteryLeft(i3);
                    ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).setBatteryRight(i4);
                    ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).setBatteryCase(i5);
                    ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).setIsInfoReceived(true);
                    ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls2)).refresh();
                    BluetoothDeviceDetailsFragment.this.mBatteryLeft = i3;
                    BluetoothDeviceDetailsFragment.this.mBatteryRight = i4;
                    BluetoothDeviceDetailsFragment.this.mBatteryCase = i5;
                    Log.d("BTDeviceDetailsFrg", "onSuccess DeviceConstant.GET_BATTERY refresh");
                }
                Log.d("BTDeviceDetailsFrg", "onSuccess DeviceConstant.GET_BATTERY");
            }
        }

        public void onFail(int i, int i2) {
            Class<AdvancedBluetoothDetailsHeaderController> cls = AdvancedBluetoothDetailsHeaderController.class;
            Log.d("BTDeviceDetailsFrg", "onFail command:" + i + ",errorCode:" + i2);
            if (i == 2 && i2 == -1) {
                BluetoothDeviceDetailsFragment.this.mIsConnectNothingDeviceService = false;
                ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls)).setIsConnectNothingDeviceService(BluetoothDeviceDetailsFragment.this.mIsConnectNothingDeviceService);
                ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls)).setIsInfoReceived(false);
                ((AdvancedBluetoothDetailsHeaderController) BluetoothDeviceDetailsFragment.this.use(cls)).refresh();
            }
        }
    };
    /* access modifiers changed from: private */
    public ArrayList<DeviceFunctionItem> mFunList;
    /* access modifiers changed from: private */
    public ArrayList<Integer> mGetCommandList = new ArrayList<>();
    /* access modifiers changed from: private */
    public boolean mIsAirpodsDevice;
    /* access modifiers changed from: private */
    public boolean mIsConnectNothingDeviceService = false;
    LocalBluetoothManager mManager;
    /* access modifiers changed from: private */
    public final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        public void onGlobalLayout() {
            View view = BluetoothDeviceDetailsFragment.this.getView();
            if (view != null && view.getWidth() > 0) {
                BluetoothDeviceDetailsFragment.this.updateExtraControlUri(view.getWidth() - BluetoothDeviceDetailsFragment.this.getPaddingSize());
                view.getViewTreeObserver().removeOnGlobalLayoutListener(BluetoothDeviceDetailsFragment.this.mOnGlobalLayoutListener);
            }
        }
    };
    private PreferenceCategory preferenceCategory = null;

    interface TestDataFactory {
        CachedBluetoothDevice getDevice(String str);

        LocalBluetoothManager getManager(Context context);
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "BTDeviceDetailsFrg";
    }

    public int getMetricsCategory() {
        return 1009;
    }

    public BluetoothDeviceDetailsFragment() {
        super("no_config_bluetooth");
        Optional isProfileBapBroadcastSourceEnabled = BluetoothProperties.isProfileBapBroadcastSourceEnabled();
        Boolean bool = Boolean.FALSE;
        if (((Boolean) isProfileBapBroadcastSourceEnabled.orElse(bool)).booleanValue() || ((Boolean) BluetoothProperties.isProfileBapBroadcastAssistEnabled().orElse(bool)).booleanValue()) {
            SystemProperties.set("persist.bluetooth.broadcast_ui", "false");
            return;
        }
        Log.d("BTDeviceDetailsFrg", "Use legacy broadcast if available");
        SystemProperties.set("persist.bluetooth.broadcast_ui", "true");
    }

    /* access modifiers changed from: package-private */
    public LocalBluetoothManager getLocalBluetoothManager(Context context) {
        TestDataFactory testDataFactory = sTestDataFactory;
        if (testDataFactory != null) {
            return testDataFactory.getManager(context);
        }
        return Utils.getLocalBtManager(context);
    }

    /* access modifiers changed from: package-private */
    public CachedBluetoothDevice getCachedDevice(String str) {
        TestDataFactory testDataFactory = sTestDataFactory;
        if (testDataFactory != null) {
            return testDataFactory.getDevice(str);
        }
        return this.mManager.getCachedDeviceManager().findDevice(this.mManager.getBluetoothAdapter().getRemoteDevice(str));
    }

    public void onAttach(Context context) {
        this.mGetCommandList = new ArrayList<>(new HashSet(this.mGetCommandList));
        Uri uri = null;
        NothingBluetoothUtil.getinstance().setNoiseSelectedMode((String) null);
        DeviceServiceController deviceServiceController = new DeviceServiceController(context);
        this.mDeviceControl = deviceServiceController;
        deviceServiceController.addCallback(this.mDeviceServiceConnectorCallback);
        Log.d("BTDeviceDetailsFrg", "addCallback(mDeviceServiceConnectorCallback)");
        String string = getArguments().getString("device_address");
        this.mDeviceAddress = string;
        if (string == null) {
            this.mDeviceAddress = getIntent().getStringExtra("device_address");
        }
        this.mManager = getLocalBluetoothManager(context);
        this.mCachedDevice = getCachedDevice(this.mDeviceAddress);
        super.onAttach(context);
        if (this.mCachedDevice == null) {
            Log.w("BTDeviceDetailsFrg", "onAttach() CachedDevice is null!");
            finish();
            return;
        }
        ((AdvancedBluetoothDetailsHeaderController) use(AdvancedBluetoothDetailsHeaderController.class)).init(this.mCachedDevice);
        ((LeAudioBluetoothDetailsHeaderController) use(LeAudioBluetoothDetailsHeaderController.class)).init(this.mCachedDevice, this.mManager);
        NothingBluetoothUtil.getinstance().setBluetoothAddress(this.mCachedDevice.getAddress());
        this.mIsAirpodsDevice = NothingBluetoothUtil.getinstance().checkUUIDIsAirpod(context, this.mCachedDevice.getDevice());
        Log.d("BTDeviceDetailsFrg", "onAttach");
        BluetoothFeatureProvider bluetoothFeatureProvider = FeatureFactory.getFactory(context).getBluetoothFeatureProvider();
        boolean z = DeviceConfig.getBoolean("settings_ui", "bt_slice_settings_enabled", true);
        BlockingSlicePrefController blockingSlicePrefController = (BlockingSlicePrefController) use(BlockingSlicePrefController.class);
        if (z) {
            uri = bluetoothFeatureProvider.getBluetoothDeviceSettingsUri(this.mCachedDevice.getDevice());
        }
        blockingSlicePrefController.setSliceUri(uri);
        ((BADeviceVolumeController) use(BADeviceVolumeController.class)).init(this, this.mManager, this.mCachedDevice);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x004e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateExtraControlUri(int r6) {
        /*
            r5 = this;
            java.lang.Class<com.android.settings.slices.SlicePreferenceController> r0 = com.android.settings.slices.SlicePreferenceController.class
            android.content.Context r1 = r5.getContext()
            com.android.settings.overlay.FeatureFactory r1 = com.android.settings.overlay.FeatureFactory.getFactory(r1)
            com.android.settings.bluetooth.BluetoothFeatureProvider r1 = r1.getBluetoothFeatureProvider()
            java.lang.String r2 = "settings_ui"
            java.lang.String r3 = "bt_slice_settings_enabled"
            r4 = 1
            boolean r2 = android.provider.DeviceConfig.getBoolean(r2, r3, r4)
            com.android.settingslib.bluetooth.CachedBluetoothDevice r3 = r5.mCachedDevice
            android.bluetooth.BluetoothDevice r3 = r3.getDevice()
            java.lang.String r1 = r1.getBluetoothDeviceControlUri(r3)
            boolean r3 = android.text.TextUtils.isEmpty(r1)
            r4 = 0
            if (r3 != 0) goto L_0x0045
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ NullPointerException -> 0x003d }
            r3.<init>()     // Catch:{ NullPointerException -> 0x003d }
            r3.append(r1)     // Catch:{ NullPointerException -> 0x003d }
            r3.append(r6)     // Catch:{ NullPointerException -> 0x003d }
            java.lang.String r6 = r3.toString()     // Catch:{ NullPointerException -> 0x003d }
            android.net.Uri r6 = android.net.Uri.parse(r6)     // Catch:{ NullPointerException -> 0x003d }
            goto L_0x0046
        L_0x003d:
            java.lang.String r6 = "BTDeviceDetailsFrg"
            java.lang.String r1 = "unable to parse uri"
            android.util.Log.d(r6, r1)
        L_0x0045:
            r6 = r4
        L_0x0046:
            com.android.settingslib.core.AbstractPreferenceController r1 = r5.use(r0)
            com.android.settings.slices.SlicePreferenceController r1 = (com.android.settings.slices.SlicePreferenceController) r1
            if (r2 == 0) goto L_0x004f
            r4 = r6
        L_0x004f:
            r1.setSliceUri(r4)
            com.android.settingslib.core.AbstractPreferenceController r5 = r5.use(r0)
            com.android.settings.slices.SlicePreferenceController r5 = (com.android.settings.slices.SlicePreferenceController) r5
            r5.onStart()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bluetooth.BluetoothDeviceDetailsFragment.updateExtraControlUri(int):void");
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        if (onCreateView != null) {
            onCreateView.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        }
        return onCreateView;
    }

    public void onResume() {
        super.onResume();
        finishFragmentIfNecessary();
        checkFastPairPreference();
        Log.d("BTDeviceDetailsFrg", "onResume mDeviceControl:" + this.mDeviceControl);
        if (this.mDeviceControl != null) {
            Bundle bundle = new Bundle();
            bundle.putString("device_address", this.mCachedDevice.getAddress());
            this.mDeviceControl.setCommand(8, bundle);
            int checkBindStatus = this.mDeviceControl.checkBindStatus(true);
            Log.d("BTDeviceDetailsFrg", "onResume bindStatus:" + checkBindStatus);
        }
    }

    /* access modifiers changed from: private */
    public void checkFastPairPreference() {
        Preference findPreference;
        boolean isNothingEarDevice = NothingBluetoothUtil.getinstance().isNothingEarDevice(getContext(), this.mCachedDevice.getAddress());
        Log.d("BTDeviceDetailsFrg", "checkFastPairPreference isNothingEarDevice:" + isNothingEarDevice);
        if (isNothingEarDevice && (findPreference = getPreferenceScreen().findPreference("bt_device_slice")) != null) {
            getPreferenceScreen().removePreference(findPreference);
        }
    }

    public void onStop() {
        super.onStop();
        Log.d("BTDeviceDetailsFrg", "onStop mDeviceControl:" + this.mDeviceControl);
        if (this.mDeviceControl != null) {
            Bundle bundle = new Bundle();
            bundle.putString("device_address", "");
            this.mDeviceControl.setCommand(8, bundle);
        }
    }

    /* access modifiers changed from: package-private */
    public void finishFragmentIfNecessary() {
        if (this.mCachedDevice.getBondState() == 10) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.bluetooth_device_details_fragment;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        MenuItem add = menu.add(0, EDIT_DEVICE_NAME_ITEM_ID, 0, R$string.bluetooth_rename_button);
        add.setIcon(17302779);
        add.setShowAsAction(2);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != EDIT_DEVICE_NAME_ITEM_ID) {
            return super.onOptionsItemSelected(menuItem);
        }
        RemoteDeviceNameDialogFragment.newInstance(this.mCachedDevice).show(getFragmentManager(), "RemoteDeviceName");
        return true;
    }

    /* access modifiers changed from: protected */
    public void displayResourceTilesToScreen(PreferenceScreen preferenceScreen) {
        if (!mBAEnabled || !this.mCachedDevice.isBASeeker()) {
            preferenceScreen.removePreference(preferenceScreen.findPreference("sync_helper_buttons"));
            preferenceScreen.removePreference(preferenceScreen.findPreference("added_sources"));
        }
        super.displayResourceTilesToScreen(preferenceScreen);
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        Class<Lifecycle> cls = Lifecycle.class;
        ArrayList arrayList = new ArrayList();
        if (this.mCachedDevice == null) {
            return arrayList;
        }
        Lifecycle settingsLifecycle = getSettingsLifecycle();
        arrayList.add(new BluetoothDetailsHeaderController(context, this, this.mCachedDevice, settingsLifecycle, this.mManager));
        arrayList.add(new BluetoothDetailsButtonsController(context, this, this.mCachedDevice, settingsLifecycle));
        arrayList.add(new BluetoothDetailsCompanionAppsController(context, this, this.mCachedDevice, settingsLifecycle));
        arrayList.add(new BluetoothDetailsSpatialAudioController(context, this, this.mCachedDevice, settingsLifecycle));
        arrayList.add(new BluetoothDetailsProfilesController(context, this, this.mManager, this.mCachedDevice, settingsLifecycle));
        arrayList.add(new BluetoothDetailsMacAddressController(context, this, this.mCachedDevice, settingsLifecycle));
        arrayList.add(new BluetoothDetailsRelatedToolsController(context, this, this.mCachedDevice, settingsLifecycle));
        arrayList.add(new NothingBluetoothPrefController(context, this.mCachedDevice, settingsLifecycle, this.mDeviceControl, "bluetooth_device_content"));
        if (!mBAPropertyChecked) {
            mBAEnabled = (SystemProperties.getInt("persist.vendor.service.bt.adv_audio_mask", 0) & 2) == 2 && SystemProperties.getBoolean("persist.bluetooth.broadcast_ui", true);
            mBAPropertyChecked = true;
        }
        if (!mBAEnabled) {
            return arrayList;
        }
        Log.d("BTDeviceDetailsFrg", "createPreferenceControllers for BA");
        try {
            if (this.mCachedDevice.isBASeeker()) {
                Constructor<BluetoothDetailsAddSourceButtonController> declaredConstructor = BluetoothDetailsAddSourceButtonController.class.getDeclaredConstructor(new Class[]{Context.class, PreferenceFragmentCompat.class, CachedBluetoothDevice.class, cls});
                Constructor<BADevicePreferenceController> declaredConstructor2 = BADevicePreferenceController.class.getDeclaredConstructor(new Class[]{Context.class, cls, String.class});
                BluetoothDetailsAddSourceButtonController newInstance = declaredConstructor.newInstance(new Object[]{context, this, this.mCachedDevice, settingsLifecycle});
                BADevicePreferenceController newInstance2 = declaredConstructor2.newInstance(new Object[]{context, settingsLifecycle, "added_sources"});
                newInstance2.getClass().getMethod("init", new Class[]{DashboardFragment.class, CachedBluetoothDevice.class}).invoke(newInstance2, new Object[]{this, this.mCachedDevice});
                arrayList.add(newInstance);
                arrayList.add(newInstance2);
            }
            return arrayList;
        } catch (Fragment.InstantiationException | ClassNotFoundException | ExceptionInInitializerError | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            mBAEnabled = false;
        } catch (Throwable unused) {
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public int getPaddingSize() {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(new int[]{16843709, 16843710});
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(0, 0) + obtainStyledAttributes.getDimensionPixelSize(1, 0);
        obtainStyledAttributes.recycle();
        return dimensionPixelSize;
    }

    public void onDestroy() {
        this.mDeviceControl.removeCallback(this.mDeviceServiceConnectorCallback);
        Log.d("BTDeviceDetailsFrg", "onDestroy removeCallback");
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void parseData(ArrayList<DeviceFunctionItem> arrayList, int i) {
        if (arrayList.size() > 0) {
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                int order = arrayList.get(i2).getOrder();
                String valueOf = String.valueOf(order);
                int style = arrayList.get(i2).getStyle();
                arrayList.get(i2).getSummary();
                arrayList.get(i2).getTitle();
                arrayList.get(i2).getAncLevel();
                ArrayList<DeviceFunctionItem> items = arrayList.get(i2).getItems();
                Preference preference = getPreference(arrayList.get(i2));
                if (getPreferenceScreen().findPreference(valueOf) == null) {
                    if (preference != null) {
                        this.deviceFunctionItemsStyleCache.put(String.valueOf(order), Integer.valueOf(style));
                        if (items.size() == 0 && i == 0) {
                            getPreferenceScreen().addPreference(preference);
                            this.mControllerPreferenceS.put(valueOf, preference);
                        } else if (preference instanceof PreferenceCategory) {
                            this.preferenceCategory = (PreferenceCategory) preference;
                            getPreferenceScreen().addPreference(this.preferenceCategory);
                            this.mControllerPreferenceS.put(valueOf, preference);
                        } else {
                            this.preferenceCategory.addPreference(preference);
                        }
                    }
                    if (items.size() > 0) {
                        parseData(items, i + 1);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0060, code lost:
        r1 = new com.nothing.settings.bluetooth.BluetoothPermissionPreference(r0);
        r1.setKey(r3);
        r1.setOrder(r2);
        r1.setPermissionTitle(r4);
        r1.setPermissionSummary(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00a3, code lost:
        r1 = new androidx.preference.Preference(r0);
        r1.setKey(r3);
        r1.setOrder(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00ae, code lost:
        if (r4 == null) goto L_0x00b3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00b0, code lost:
        r1.setTitle((java.lang.CharSequence) r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00b3, code lost:
        if (r5 == null) goto L_0x00b8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00b5, code lost:
        r1.setSummary((java.lang.CharSequence) r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00b8, code lost:
        r1.setOnPreferenceClickListener(new com.android.settings.bluetooth.BluetoothDeviceDetailsFragment.C07853(r7));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public androidx.preference.Preference getPreference(com.nothing.p005os.device.DeviceFunctionItem r8) {
        /*
            r7 = this;
            androidx.preference.PreferenceScreen r0 = r7.getPreferenceScreen()
            android.content.Context r0 = r0.getContext()
            r1 = 0
            if (r8 != 0) goto L_0x000c
            return r1
        L_0x000c:
            int r2 = r8.getOrder()
            int r3 = r8.getOrder()
            java.lang.String r3 = java.lang.String.valueOf(r3)
            java.lang.String r4 = r8.getTitle()
            java.lang.String r5 = r8.getSummary()
            int r6 = r8.getStyle()
            switch(r6) {
                case 1: goto L_0x00e4;
                case 2: goto L_0x00c1;
                case 3: goto L_0x009a;
                case 4: goto L_0x00a3;
                case 5: goto L_0x0073;
                case 6: goto L_0x0029;
                case 7: goto L_0x0060;
                default: goto L_0x0027;
            }
        L_0x0027:
            goto L_0x00f2
        L_0x0029:
            com.android.settingslib.bluetooth.CachedBluetoothDevice r8 = r7.mCachedDevice
            android.bluetooth.BluetoothDevice r8 = r8.getDevice()
            boolean r8 = com.android.settings.bluetooth.Utils.isAdvancedDetailsHeader(r8)
            if (r8 == 0) goto L_0x0036
            return r1
        L_0x0036:
            androidx.preference.Preference r8 = new androidx.preference.Preference
            r8.<init>(r0)
            r8.setKey(r3)
            r8.setOrder(r2)
            if (r4 == 0) goto L_0x0046
            r8.setTitle((java.lang.CharSequence) r4)
        L_0x0046:
            if (r5 == 0) goto L_0x004b
            r8.setSummary((java.lang.CharSequence) r5)
        L_0x004b:
            android.content.res.Resources r1 = r0.getResources()
            int r6 = com.android.settings.R$drawable.nt_ic_bluetooth_see_more
            android.graphics.drawable.Drawable r1 = r1.getDrawable(r6)
            r8.setIcon((android.graphics.drawable.Drawable) r1)
            com.android.settings.bluetooth.BluetoothDeviceDetailsFragment$5 r1 = new com.android.settings.bluetooth.BluetoothDeviceDetailsFragment$5
            r1.<init>(r3, r2)
            r8.setOnPreferenceClickListener(r1)
        L_0x0060:
            com.nothing.settings.bluetooth.BluetoothPermissionPreference r1 = new com.nothing.settings.bluetooth.BluetoothPermissionPreference
            r1.<init>(r0)
            r1.setKey(r3)
            r1.setOrder(r2)
            r1.setPermissionTitle(r4)
            r1.setPermissionSummary(r5)
            goto L_0x00f2
        L_0x0073:
            com.nothing.ui.support.NtCustSwitchPreference r1 = new com.nothing.ui.support.NtCustSwitchPreference
            r1.<init>(r0)
            r1.setKey(r3)
            r1.setOrder(r2)
            if (r4 == 0) goto L_0x0083
            r1.setTitle((java.lang.CharSequence) r4)
        L_0x0083:
            if (r5 == 0) goto L_0x0088
            r1.setSummary((java.lang.CharSequence) r5)
        L_0x0088:
            com.android.settings.bluetooth.BluetoothDeviceDetailsFragment$4 r8 = new com.android.settings.bluetooth.BluetoothDeviceDetailsFragment$4
            r8.<init>(r2)
            r1.setOnPreferenceClickListener(r8)
            java.util.ArrayList<java.lang.Integer> r7 = r7.mGetCommandList
            java.lang.Integer r8 = java.lang.Integer.valueOf(r2)
            r7.add(r8)
            goto L_0x00f2
        L_0x009a:
            java.util.ArrayList<java.lang.Integer> r8 = r7.mGetCommandList
            java.lang.Integer r1 = java.lang.Integer.valueOf(r2)
            r8.add(r1)
        L_0x00a3:
            androidx.preference.Preference r1 = new androidx.preference.Preference
            r1.<init>(r0)
            r1.setKey(r3)
            r1.setOrder(r2)
            if (r4 == 0) goto L_0x00b3
            r1.setTitle((java.lang.CharSequence) r4)
        L_0x00b3:
            if (r5 == 0) goto L_0x00b8
            r1.setSummary((java.lang.CharSequence) r5)
        L_0x00b8:
            com.android.settings.bluetooth.BluetoothDeviceDetailsFragment$3 r8 = new com.android.settings.bluetooth.BluetoothDeviceDetailsFragment$3
            r8.<init>(r3, r2)
            r1.setOnPreferenceClickListener(r8)
            goto L_0x00f2
        L_0x00c1:
            com.nothing.settings.bluetooth.BluetoothSoundPreference r1 = new com.nothing.settings.bluetooth.BluetoothSoundPreference
            com.nothing.os.device.DeviceServiceController r4 = r7.mDeviceControl
            com.android.settingslib.bluetooth.CachedBluetoothDevice r5 = r7.mCachedDevice
            r1.<init>((android.content.Context) r0, (com.nothing.p005os.device.DeviceServiceController) r4, (com.android.settingslib.bluetooth.CachedBluetoothDevice) r5)
            int r0 = r8.getAncLevel()
            r1.setAncLevel(r0)
            r1.setKey(r3)
            r1.setOrder(r2)
            java.util.ArrayList<java.lang.Integer> r7 = r7.mGetCommandList
            java.lang.Integer r0 = java.lang.Integer.valueOf(r2)
            r7.add(r0)
            r8.getAncLevel()
            goto L_0x00f2
        L_0x00e4:
            androidx.preference.PreferenceCategory r1 = new androidx.preference.PreferenceCategory
            r1.<init>(r0)
            r1.setKey(r3)
            r1.setOrder(r2)
            r1.setTitle((java.lang.CharSequence) r4)
        L_0x00f2:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.bluetooth.BluetoothDeviceDetailsFragment.getPreference(com.nothing.os.device.DeviceFunctionItem):androidx.preference.Preference");
    }

    /* access modifiers changed from: private */
    public void getRemoteDataAndUpdateUI(int i, Bundle bundle) {
        String str;
        Map<String, Integer> map;
        Preference preference = null;
        try {
            str = String.valueOf(i);
            try {
                preference = getPreferenceScreen().findPreference(str);
            } catch (Exception unused) {
            }
        } catch (Exception unused2) {
            str = null;
        }
        if (!TextUtils.isEmpty(str) && preference != null && (map = this.deviceFunctionItemsStyleCache) != null && map.size() > 0) {
            if (this.deviceFunctionItemsStyleCache.get(str) == null) {
                Log.i("BTDeviceDetailsFrg", "getRemoteDataAndUpdateUI() failed " + str);
                return;
            }
            int intValue = this.deviceFunctionItemsStyleCache.get(str).intValue();
            if (intValue == 2) {
                ((BluetoothSoundPreference) preference).setRemoteDataAndUpdateUI(bundle);
            } else if (intValue == 3) {
                preference.setSummary((CharSequence) bundle.getString("KEY_VALUE_STRING", ""));
            } else if (intValue == 5) {
                ((SwitchPreference) preference).setChecked(bundle.getBoolean("KEY_VALUE_BOOLEAN", false));
            }
        }
    }

    public void createOrRemovePermissionPreference(Bundle bundle) {
        boolean z = bundle.getBoolean("KEY_VALUE_BOOLEAN");
        DeviceFunctionItem deviceFunctionItem = (DeviceFunctionItem) bundle.getParcelable("KEY_FUNCTION_ITEM", DeviceFunctionItem.class);
        String valueOf = String.valueOf(deviceFunctionItem.getOrder());
        if (z) {
            Preference findPreference = getPreferenceScreen().findPreference(valueOf);
            if (findPreference != null) {
                getPreferenceScreen().removePreference(findPreference);
            }
        } else if (getPreferenceScreen().findPreference(valueOf) == null) {
            getPreferenceScreen().addPreference((BluetoothPermissionPreference) getPreference(deviceFunctionItem));
        }
    }
}
