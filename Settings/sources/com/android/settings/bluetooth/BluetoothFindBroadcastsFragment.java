package com.android.settings.bluetooth;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeBroadcastAssistant;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.bluetooth.BluetoothLeBroadcastReceiveState;
import android.bluetooth.le.ScanFilter;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.RestrictedDashboardFragment;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothLeBroadcastAssistant;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.settingslib.core.AbstractPreferenceController;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BluetoothFindBroadcastsFragment extends RestrictedDashboardFragment {
    BluetoothFindBroadcastsHeaderController mBluetoothFindBroadcastsHeaderController;
    private BluetoothLeBroadcastAssistant.Callback mBroadcastAssistantCallback = new BluetoothLeBroadcastAssistant.Callback() {
        public void onSourceModified(BluetoothDevice bluetoothDevice, int i, int i2) {
        }

        public void onSourceModifyFailed(BluetoothDevice bluetoothDevice, int i, int i2) {
        }

        public void onSearchStarted(int i) {
            Log.d("BtFindBroadcastsFrg", "onSearchStarted: " + i);
            BluetoothFindBroadcastsFragment.this.getActivity().runOnUiThread(new BluetoothFindBroadcastsFragment$1$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSearchStarted$0() {
            BluetoothFindBroadcastsFragment.this.handleSearchStarted();
        }

        public void onSearchStartFailed(int i) {
            Log.d("BtFindBroadcastsFrg", "onSearchStartFailed: " + i);
        }

        public void onSearchStopped(int i) {
            Log.d("BtFindBroadcastsFrg", "onSearchStopped: " + i);
        }

        public void onSearchStopFailed(int i) {
            Log.d("BtFindBroadcastsFrg", "onSearchStopFailed: " + i);
        }

        public void onSourceFound(BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
            Log.d("BtFindBroadcastsFrg", "onSourceFound:");
            BluetoothFindBroadcastsFragment.this.getActivity().runOnUiThread(new BluetoothFindBroadcastsFragment$1$$ExternalSyntheticLambda1(this, bluetoothLeBroadcastMetadata));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSourceFound$1(BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
            BluetoothFindBroadcastsFragment.this.updateListCategoryFromBroadcastMetadata(bluetoothLeBroadcastMetadata, false);
        }

        public void onSourceAdded(BluetoothDevice bluetoothDevice, int i, int i2) {
            Log.d("BtFindBroadcastsFrg", "onSourceAdded");
            BluetoothFindBroadcastsFragment.this.setSourceId(i);
            if (BluetoothFindBroadcastsFragment.this.mSelectedPreference == null) {
                Log.w("BtFindBroadcastsFrg", "onSourceAdded: mSelectedPreference == null!");
            } else {
                BluetoothFindBroadcastsFragment.this.getActivity().runOnUiThread(new BluetoothFindBroadcastsFragment$1$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSourceAdded$2() {
            BluetoothFindBroadcastsFragment bluetoothFindBroadcastsFragment = BluetoothFindBroadcastsFragment.this;
            bluetoothFindBroadcastsFragment.updateListCategoryFromBroadcastMetadata(bluetoothFindBroadcastsFragment.mSelectedPreference.getBluetoothLeBroadcastMetadata(), true);
        }

        public void onSourceAddFailed(BluetoothDevice bluetoothDevice, BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata, int i) {
            BluetoothFindBroadcastsFragment.this.mSelectedPreference = null;
            Log.d("BtFindBroadcastsFrg", "onSourceAddFailed: clear the mSelectedPreference.");
        }

        public void onSourceRemoved(BluetoothDevice bluetoothDevice, int i, int i2) {
            Log.d("BtFindBroadcastsFrg", "onSourceRemoved:");
            BluetoothFindBroadcastsFragment.this.getActivity().runOnUiThread(new BluetoothFindBroadcastsFragment$1$$ExternalSyntheticLambda3(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSourceRemoved$3() {
            BluetoothFindBroadcastsFragment.this.handleSourceRemoved();
        }

        public void onSourceRemoveFailed(BluetoothDevice bluetoothDevice, int i, int i2) {
            Log.d("BtFindBroadcastsFrg", "onSourceRemoveFailed:");
        }

        public void onReceiveStateChanged(BluetoothDevice bluetoothDevice, int i, BluetoothLeBroadcastReceiveState bluetoothLeBroadcastReceiveState) {
            Log.d("BtFindBroadcastsFrg", "onReceiveStateChanged:");
        }
    };
    PreferenceCategory mBroadcastSourceListCategory;
    CachedBluetoothDevice mCachedDevice;
    String mDeviceAddress;
    private Executor mExecutor;
    private LocalBluetoothLeBroadcastAssistant mLeBroadcastAssistant;
    LocalBluetoothManager mManager;
    /* access modifiers changed from: private */
    public BluetoothBroadcastSourcePreference mSelectedPreference;
    private int mSourceId;

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return "BtFindBroadcastsFrg";
    }

    public int getMetricsCategory() {
        return 0;
    }

    public BluetoothFindBroadcastsFragment() {
        super("no_config_bluetooth");
    }

    /* access modifiers changed from: package-private */
    public LocalBluetoothManager getLocalBluetoothManager(Context context) {
        return Utils.getLocalBtManager(context);
    }

    /* access modifiers changed from: package-private */
    public CachedBluetoothDevice getCachedDevice(String str) {
        return this.mManager.getCachedDeviceManager().findDevice(this.mManager.getBluetoothAdapter().getRemoteDevice(str));
    }

    public void onAttach(Context context) {
        this.mDeviceAddress = getArguments().getString("device_address");
        this.mManager = getLocalBluetoothManager(context);
        this.mCachedDevice = getCachedDevice(this.mDeviceAddress);
        this.mLeBroadcastAssistant = getLeBroadcastAssistant();
        this.mExecutor = Executors.newSingleThreadExecutor();
        super.onAttach(context);
        if (this.mCachedDevice == null || this.mLeBroadcastAssistant == null) {
            Log.w("BtFindBroadcastsFrg", "onAttach() CachedDevice or LeBroadcastAssistant is null!");
            finish();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mBroadcastSourceListCategory = (PreferenceCategory) findPreference("broadcast_source_list");
    }

    public void onStart() {
        super.onStart();
        LocalBluetoothLeBroadcastAssistant localBluetoothLeBroadcastAssistant = this.mLeBroadcastAssistant;
        if (localBluetoothLeBroadcastAssistant != null) {
            localBluetoothLeBroadcastAssistant.registerServiceCallBack(this.mExecutor, this.mBroadcastAssistantCallback);
        }
    }

    public void onResume() {
        super.onResume();
        finishFragmentIfNecessary();
        LocalBluetoothLeBroadcastAssistant localBluetoothLeBroadcastAssistant = this.mLeBroadcastAssistant;
        if (localBluetoothLeBroadcastAssistant == null || localBluetoothLeBroadcastAssistant.isSearchInProgress()) {
            addConnectedSourcePreference();
        } else {
            this.mLeBroadcastAssistant.startSearchingForSources(getScanFilter());
        }
    }

    public void onStop() {
        super.onStop();
        LocalBluetoothLeBroadcastAssistant localBluetoothLeBroadcastAssistant = this.mLeBroadcastAssistant;
        if (localBluetoothLeBroadcastAssistant != null) {
            if (localBluetoothLeBroadcastAssistant.isSearchInProgress()) {
                Log.d("BtFindBroadcastsFrg", "Search is in progress, stop searching.");
                this.mLeBroadcastAssistant.stopSearchingForSources();
            }
            this.mLeBroadcastAssistant.unregisterServiceCallBack(this.mBroadcastAssistantCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void finishFragmentIfNecessary() {
        if (this.mCachedDevice.getBondState() == 10) {
            finish();
        }
    }

    public void scanBroadcastSource() {
        LocalBluetoothLeBroadcastAssistant localBluetoothLeBroadcastAssistant = this.mLeBroadcastAssistant;
        if (localBluetoothLeBroadcastAssistant == null) {
            Log.w("BtFindBroadcastsFrg", "scanBroadcastSource: LeBroadcastAssistant is null!");
        } else {
            localBluetoothLeBroadcastAssistant.startSearchingForSources(getScanFilter());
        }
    }

    public void leaveBroadcastSession() {
        CachedBluetoothDevice cachedBluetoothDevice;
        LocalBluetoothLeBroadcastAssistant localBluetoothLeBroadcastAssistant = this.mLeBroadcastAssistant;
        if (localBluetoothLeBroadcastAssistant == null || (cachedBluetoothDevice = this.mCachedDevice) == null) {
            Log.w("BtFindBroadcastsFrg", "leaveBroadcastSession: LeBroadcastAssistant or CachedDevice is null!");
        } else {
            localBluetoothLeBroadcastAssistant.removeSource(cachedBluetoothDevice.getDevice(), getSourceId());
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.bluetooth_find_broadcasts_fragment;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        if (this.mCachedDevice != null) {
            Context context2 = context;
            BluetoothFindBroadcastsHeaderController bluetoothFindBroadcastsHeaderController = new BluetoothFindBroadcastsHeaderController(context2, this, this.mCachedDevice, getSettingsLifecycle(), this.mManager);
            this.mBluetoothFindBroadcastsHeaderController = bluetoothFindBroadcastsHeaderController;
            arrayList.add(bluetoothFindBroadcastsHeaderController);
        }
        return arrayList;
    }

    public LocalBluetoothLeBroadcastAssistant getLeBroadcastAssistant() {
        LocalBluetoothManager localBluetoothManager = this.mManager;
        if (localBluetoothManager == null) {
            Log.w("BtFindBroadcastsFrg", "getLeBroadcastAssistant: LocalBluetoothManager is null!");
            return null;
        }
        LocalBluetoothProfileManager profileManager = localBluetoothManager.getProfileManager();
        if (profileManager != null) {
            return profileManager.getLeAudioBroadcastAssistantProfile();
        }
        Log.w("BtFindBroadcastsFrg", "getLeBroadcastAssistant: LocalBluetoothProfileManager is null!");
        return null;
    }

    private List<ScanFilter> getScanFilter() {
        return Collections.emptyList();
    }

    /* access modifiers changed from: private */
    public void updateListCategoryFromBroadcastMetadata(BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata, boolean z) {
        BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference = (BluetoothBroadcastSourcePreference) this.mBroadcastSourceListCategory.findPreference(Integer.toString(bluetoothLeBroadcastMetadata.getBroadcastId()));
        if (bluetoothBroadcastSourcePreference == null) {
            bluetoothBroadcastSourcePreference = createBluetoothBroadcastSourcePreference();
            bluetoothBroadcastSourcePreference.setKey(Integer.toString(bluetoothLeBroadcastMetadata.getBroadcastId()));
            this.mBroadcastSourceListCategory.addPreference(bluetoothBroadcastSourcePreference);
        }
        bluetoothBroadcastSourcePreference.updateMetadataAndRefreshUi(bluetoothLeBroadcastMetadata, z);
        bluetoothBroadcastSourcePreference.setOrder(z ^ true ? 1 : 0);
        BluetoothFindBroadcastsHeaderController bluetoothFindBroadcastsHeaderController = this.mBluetoothFindBroadcastsHeaderController;
        if (bluetoothFindBroadcastsHeaderController != null) {
            bluetoothFindBroadcastsHeaderController.refreshUi();
        }
    }

    private void updateListCategoryFromBroadcastReceiveState(BluetoothLeBroadcastReceiveState bluetoothLeBroadcastReceiveState) {
        BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference = (BluetoothBroadcastSourcePreference) this.mBroadcastSourceListCategory.findPreference(Integer.toString(bluetoothLeBroadcastReceiveState.getBroadcastId()));
        if (bluetoothBroadcastSourcePreference == null) {
            bluetoothBroadcastSourcePreference = createBluetoothBroadcastSourcePreference();
            bluetoothBroadcastSourcePreference.setKey(Integer.toString(bluetoothLeBroadcastReceiveState.getBroadcastId()));
            this.mBroadcastSourceListCategory.addPreference(bluetoothBroadcastSourcePreference);
        }
        bluetoothBroadcastSourcePreference.updateReceiveStateAndRefreshUi(bluetoothLeBroadcastReceiveState);
        bluetoothBroadcastSourcePreference.setOrder(0);
        setSourceId(bluetoothLeBroadcastReceiveState.getSourceId());
        this.mSelectedPreference = bluetoothBroadcastSourcePreference;
        BluetoothFindBroadcastsHeaderController bluetoothFindBroadcastsHeaderController = this.mBluetoothFindBroadcastsHeaderController;
        if (bluetoothFindBroadcastsHeaderController != null) {
            bluetoothFindBroadcastsHeaderController.refreshUi();
        }
    }

    private BluetoothBroadcastSourcePreference createBluetoothBroadcastSourcePreference() {
        BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference = new BluetoothBroadcastSourcePreference(getContext());
        bluetoothBroadcastSourcePreference.setOnPreferenceClickListener(new BluetoothFindBroadcastsFragment$$ExternalSyntheticLambda0(this, bluetoothBroadcastSourcePreference));
        return bluetoothBroadcastSourcePreference;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$createBluetoothBroadcastSourcePreference$0(BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference, Preference preference) {
        if (bluetoothBroadcastSourcePreference.getBluetoothLeBroadcastMetadata() == null) {
            Log.d("BtFindBroadcastsFrg", "BluetoothLeBroadcastMetadata is null, do nothing.");
            return false;
        } else if (bluetoothBroadcastSourcePreference.isEncrypted()) {
            launchBroadcastCodeDialog(bluetoothBroadcastSourcePreference);
            return true;
        } else {
            addSource(bluetoothBroadcastSourcePreference);
            return true;
        }
    }

    private void addSource(BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference) {
        if (this.mLeBroadcastAssistant == null || this.mCachedDevice == null) {
            Log.w("BtFindBroadcastsFrg", "addSource: LeBroadcastAssistant or CachedDevice is null!");
            return;
        }
        if (this.mSelectedPreference != null) {
            getActivity().runOnUiThread(new BluetoothFindBroadcastsFragment$$ExternalSyntheticLambda2(this));
        }
        this.mSelectedPreference = bluetoothBroadcastSourcePreference;
        this.mLeBroadcastAssistant.addSource(this.mCachedDevice.getDevice(), bluetoothBroadcastSourcePreference.getBluetoothLeBroadcastMetadata(), true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addSource$1() {
        BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference = this.mSelectedPreference;
        bluetoothBroadcastSourcePreference.updateMetadataAndRefreshUi(bluetoothBroadcastSourcePreference.getBluetoothLeBroadcastMetadata(), false);
        this.mSelectedPreference.setOrder(1);
    }

    private void addBroadcastCodeIntoPreference(BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference, String str) {
        bluetoothBroadcastSourcePreference.updateMetadataAndRefreshUi(new BluetoothLeBroadcastMetadata.Builder(bluetoothBroadcastSourcePreference.getBluetoothLeBroadcastMetadata()).setBroadcastCode(str.getBytes(StandardCharsets.UTF_8)).build(), false);
    }

    private void launchBroadcastCodeDialog(BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference) {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.bluetooth_find_broadcast_password_dialog, (ViewGroup) null);
        ((TextView) inflate.requireViewById(R$id.broadcast_name_text)).setText(bluetoothBroadcastSourcePreference.getTitle());
        AlertDialog create = new AlertDialog.Builder(getContext()).setTitle(R$string.find_broadcast_password_dialog_title).setView(inflate).setNeutralButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(R$string.bluetooth_connect_access_dialog_positive, new BluetoothFindBroadcastsFragment$$ExternalSyntheticLambda1(this, bluetoothBroadcastSourcePreference, (EditText) inflate.requireViewById(R$id.broadcast_edit_text))).create();
        create.getWindow().setType(2009);
        create.show();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$launchBroadcastCodeDialog$2(BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference, EditText editText, DialogInterface dialogInterface, int i) {
        Log.d("BtFindBroadcastsFrg", "setPositiveButton: clicked");
        if (bluetoothBroadcastSourcePreference.getBluetoothLeBroadcastMetadata() == null) {
            Log.d("BtFindBroadcastsFrg", "BluetoothLeBroadcastMetadata is null, do nothing.");
            return;
        }
        addBroadcastCodeIntoPreference(bluetoothBroadcastSourcePreference, editText.getText().toString());
        addSource(bluetoothBroadcastSourcePreference);
    }

    /* access modifiers changed from: private */
    public void handleSearchStarted() {
        cacheRemoveAllPrefs(this.mBroadcastSourceListCategory);
        addConnectedSourcePreference();
    }

    /* access modifiers changed from: private */
    public void handleSourceRemoved() {
        BluetoothBroadcastSourcePreference bluetoothBroadcastSourcePreference = this.mSelectedPreference;
        if (bluetoothBroadcastSourcePreference != null) {
            if (bluetoothBroadcastSourcePreference.getBluetoothLeBroadcastMetadata() == null) {
                this.mBroadcastSourceListCategory.removePreference(this.mSelectedPreference);
            } else {
                this.mSelectedPreference.clearReceiveState();
            }
        }
        this.mSelectedPreference = null;
    }

    private void addConnectedSourcePreference() {
        List<BluetoothLeBroadcastReceiveState> allSources = this.mLeBroadcastAssistant.getAllSources(this.mCachedDevice.getDevice());
        if (!allSources.isEmpty()) {
            updateListCategoryFromBroadcastReceiveState(allSources.get(0));
        }
    }

    public int getSourceId() {
        return this.mSourceId;
    }

    public void setSourceId(int i) {
        this.mSourceId = i;
    }
}
