package com.android.settings.deviceinfo.simstatus;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.telephony.CarrierConfigManager;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.ICellBroadcastService;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SignalStrengthUpdateRequest;
import android.telephony.SignalThresholdInfo;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.euicc.EuiccManager;
import android.telephony.ims.ImsException;
import android.telephony.ims.ImsMmTelManager;
import android.telephony.ims.ImsReasonInfo;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.settings.R$id;
import com.android.settings.R$string;
import com.android.settings.network.telephony.DomesticRoamUtils;
import com.android.settingslib.DeviceInfoUtils;
import com.android.settingslib.Utils;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.settingslib.utils.ThreadUtils;
import com.qti.extphone.Client;
import com.qti.extphone.ExtPhoneCallbackBase;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.IExtPhoneCallback;
import com.qti.extphone.NrIconType;
import com.qti.extphone.ServiceCallback;
import com.qti.extphone.Status;
import com.qti.extphone.Token;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SimStatusDialogController implements LifecycleObserver {
    static final int CELLULAR_NETWORK_STATE = R$id.data_state_value;
    static final int CELL_DATA_NETWORK_TYPE_VALUE_ID = R$id.data_network_type_value;
    static final int CELL_VOICE_NETWORK_TYPE_VALUE_ID = R$id.voice_network_type_value;
    static final int EID_INFO_LABEL_ID = R$id.esim_id_label;
    static final int EID_INFO_VALUE_ID = R$id.esim_id_value;
    static final int ICCID_INFO_LABEL_ID = R$id.icc_id_label;
    static final int ICCID_INFO_VALUE_ID = R$id.icc_id_value;
    static final int IMS_REGISTRATION_STATE_LABEL_ID = R$id.ims_reg_state_label;
    static final int IMS_REGISTRATION_STATE_VALUE_ID = R$id.ims_reg_state_value;
    static final int MAX_PHONE_COUNT_SINGLE_SIM = 1;
    static final int NETWORK_PROVIDER_VALUE_ID = R$id.operator_name_value;
    static final int OPERATOR_INFO_LABEL_ID = R$id.latest_area_info_label;
    static final int OPERATOR_INFO_VALUE_ID = R$id.latest_area_info_value;
    static final int PHONE_NUMBER_VALUE_ID = R$id.number_value;
    static final int ROAMING_INFO_VALUE_ID = R$id.roaming_state_value;
    static final int SERVICE_STATE_VALUE_ID = R$id.service_state_value;
    static final int SIGNAL_STRENGTH_LABEL_ID = R$id.signal_strength_label;
    static final int SIGNAL_STRENGTH_VALUE_ID = R$id.signal_strength_value;
    private final BroadcastReceiver mAreaInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.telephony.action.AREA_INFO_UPDATED".equals(intent.getAction()) && intent.getIntExtra("android.telephony.extra.SLOT_INDEX", 0) == SimStatusDialogController.this.mSlotIndex) {
                SimStatusDialogController.this.updateAreaInfoText();
            }
        }
    };
    private final CarrierConfigManager mCarrierConfigManager;
    private CellBroadcastServiceConnection mCellBroadcastServiceConnection;
    /* access modifiers changed from: private */
    public Client mClient;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final SimStatusDialogFragment mDialog;
    private final EuiccManager mEuiccManager;
    protected IExtPhoneCallback mExtPhoneCallback = new ExtPhoneCallbackBase() {
        public void onNrIconType(int i, Token token, Status status, NrIconType nrIconType) {
            Log.d("SimStatusDialogCtrl", "onNrIconType slotId: " + i + " token: " + token + "  status: " + status + " NrIconType: " + nrIconType);
            if (i == SimStatusDialogController.this.mSlotIndex && status.get() == 1 && nrIconType != null) {
                SimStatusDialogController.this.mNrIconType = nrIconType.get();
                SimStatusDialogController.this.updateSubscriptionStatus();
            }
        }
    };
    private ServiceCallback mExtTelManagerServiceCallback = new ServiceCallback() {
        public void onConnected() {
            SimStatusDialogController.this.mServiceConnected = true;
            SimStatusDialogController simStatusDialogController = SimStatusDialogController.this;
            simStatusDialogController.mClient = simStatusDialogController.mExtTelephonyManager.registerCallback(SimStatusDialogController.this.mContext.getPackageName(), SimStatusDialogController.this.mExtPhoneCallback);
            Token queryNrIconType = SimStatusDialogController.this.mExtTelephonyManager.queryNrIconType(SimStatusDialogController.this.mSlotIndex, SimStatusDialogController.this.mClient);
            Log.i("SimStatusDialogCtrl", "mExtTelManagerServiceCallback: service connected,  token = " + queryNrIconType + ", mClient: " + SimStatusDialogController.this.mClient);
        }

        public void onDisconnected() {
            Log.i("SimStatusDialogCtrl", "mExtTelManagerServiceCallback: service disconnected");
            if (SimStatusDialogController.this.mServiceConnected) {
                SimStatusDialogController.this.mExtTelephonyManager.unRegisterCallback(SimStatusDialogController.this.mExtPhoneCallback);
                SimStatusDialogController.this.mServiceConnected = false;
                SimStatusDialogController.this.mClient = null;
            }
        }
    };
    /* access modifiers changed from: private */
    public ExtTelephonyManager mExtTelephonyManager;
    private ImsMmTelManager.RegistrationCallback mImsRegStateCallback = new ImsMmTelManager.RegistrationCallback() {
        public void onRegistered(int i) {
            SimStatusDialogController.this.mDialog.setText(SimStatusDialogController.IMS_REGISTRATION_STATE_VALUE_ID, SimStatusDialogController.this.mRes.getString(R$string.ims_reg_status_registered));
        }

        public void onRegistering(int i) {
            SimStatusDialogController.this.mDialog.setText(SimStatusDialogController.IMS_REGISTRATION_STATE_VALUE_ID, SimStatusDialogController.this.mRes.getString(R$string.ims_reg_status_not_registered));
        }

        public void onUnregistered(ImsReasonInfo imsReasonInfo) {
            SimStatusDialogController.this.mDialog.setText(SimStatusDialogController.IMS_REGISTRATION_STATE_VALUE_ID, SimStatusDialogController.this.mRes.getString(R$string.ims_reg_status_not_registered));
        }

        public void onTechnologyChangeFailed(int i, ImsReasonInfo imsReasonInfo) {
            SimStatusDialogController.this.mDialog.setText(SimStatusDialogController.IMS_REGISTRATION_STATE_VALUE_ID, SimStatusDialogController.this.mRes.getString(R$string.ims_reg_status_not_registered));
        }
    };
    private boolean mIsRegisteredListener = false;
    /* access modifiers changed from: private */
    public int mNrIconType;
    private final SubscriptionManager.OnSubscriptionsChangedListener mOnSubscriptionsChangedListener = new SubscriptionManager.OnSubscriptionsChangedListener() {
        public void onSubscriptionsChanged() {
            int i = -1;
            int subscriptionId = SimStatusDialogController.this.mSubscriptionInfo != null ? SimStatusDialogController.this.mSubscriptionInfo.getSubscriptionId() : -1;
            SimStatusDialogController simStatusDialogController = SimStatusDialogController.this;
            simStatusDialogController.mSubscriptionInfo = simStatusDialogController.getPhoneSubscriptionInfo(simStatusDialogController.mSlotIndex);
            if (SimStatusDialogController.this.mSubscriptionInfo != null) {
                i = SimStatusDialogController.this.mSubscriptionInfo.getSubscriptionId();
            }
            if (subscriptionId != i) {
                if (SubscriptionManager.isValidSubscriptionId(subscriptionId)) {
                    SimStatusDialogController.this.unregisterImsRegistrationCallback(subscriptionId);
                }
                if (SubscriptionManager.isValidSubscriptionId(i)) {
                    SimStatusDialogController simStatusDialogController2 = SimStatusDialogController.this;
                    simStatusDialogController2.mTelephonyManager = simStatusDialogController2.getTelephonyManager().createForSubscriptionId(i);
                    SimStatusDialogController.this.registerImsRegistrationCallback(i);
                }
            }
            SimStatusDialogController.this.updateSubscriptionStatus();
        }
    };
    /* access modifiers changed from: private */
    public ServiceState mPreviousServiceState;
    /* access modifiers changed from: private */
    public final Resources mRes;
    /* access modifiers changed from: private */
    public boolean mServiceConnected;
    private boolean mShowLatestAreaInfo;
    private SignalStrengthUpdateRequest mSignalStrengthUpdateRequest;
    /* access modifiers changed from: private */
    public final int mSlotIndex;
    /* access modifiers changed from: private */
    public SubscriptionInfo mSubscriptionInfo;
    private final SubscriptionManager mSubscriptionManager;
    protected SimStatusDialogTelephonyCallback mTelephonyCallback;
    /* access modifiers changed from: private */
    public TelephonyDisplayInfo mTelephonyDisplayInfo;
    /* access modifiers changed from: private */
    public TelephonyManager mTelephonyManager;

    static String getNetworkTypeName(int i) {
        switch (i) {
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA";
            case 5:
                return "CDMA - EvDo rev. 0";
            case 6:
                return "CDMA - EvDo rev. A";
            case 7:
                return "CDMA - 1xRTT";
            case 8:
                return "HSDPA";
            case 9:
                return "HSUPA";
            case 10:
                return "HSPA";
            case 11:
                return "iDEN";
            case 12:
                return "CDMA - EvDo rev. B";
            case 13:
                return "LTE";
            case 14:
                return "CDMA - eHRPD";
            case 15:
                return "HSPA+";
            case 16:
                return "GSM";
            case 17:
                return "TD_SCDMA";
            case 18:
                return "IWLAN";
            case 20:
                return "NR SA";
            default:
                return "UNKNOWN";
        }
    }

    private class CellBroadcastServiceConnection implements ServiceConnection {
        private IBinder mService;

        private CellBroadcastServiceConnection() {
        }

        public IBinder getService() {
            return this.mService;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("SimStatusDialogCtrl", "connected to CellBroadcastService");
            this.mService = iBinder;
            SimStatusDialogController.this.updateAreaInfoText();
        }

        public void onServiceDisconnected(ComponentName componentName) {
            this.mService = null;
            Log.d("SimStatusDialogCtrl", "mICellBroadcastService has disconnected unexpectedly");
        }

        public void onBindingDied(ComponentName componentName) {
            this.mService = null;
            Log.d("SimStatusDialogCtrl", "Binding died");
        }

        public void onNullBinding(ComponentName componentName) {
            this.mService = null;
            Log.d("SimStatusDialogCtrl", "Null binding");
        }
    }

    public SimStatusDialogController(SimStatusDialogFragment simStatusDialogFragment, Lifecycle lifecycle, int i) {
        this.mDialog = simStatusDialogFragment;
        Context context = simStatusDialogFragment.getContext();
        this.mContext = context;
        this.mSlotIndex = i;
        this.mSubscriptionInfo = getPhoneSubscriptionInfo(i);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        this.mEuiccManager = (EuiccManager) context.getSystemService(EuiccManager.class);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mRes = context.getResources();
        if (lifecycle != null) {
            lifecycle.addObserver(this);
        }
    }

    public TelephonyManager getTelephonyManager() {
        return this.mTelephonyManager;
    }

    public void initialize() {
        requestForUpdateEid();
        if (this.mSubscriptionInfo != null) {
            this.mTelephonyManager = getTelephonyManager().createForSubscriptionId(this.mSubscriptionInfo.getSubscriptionId());
            this.mTelephonyCallback = new SimStatusDialogTelephonyCallback();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new SignalThresholdInfo.Builder().setRadioAccessNetworkType(6).setSignalMeasurementType(6).setThresholds(new int[]{-140, -120, -114, -106}).build());
            this.mSignalStrengthUpdateRequest = new SignalStrengthUpdateRequest.Builder().setSignalThresholdInfos(arrayList).build();
            updateLatestAreaInfo();
            updateSubscriptionStatus();
            this.mNrIconType = -1;
            ExtTelephonyManager instance = ExtTelephonyManager.getInstance(this.mContext);
            this.mExtTelephonyManager = instance;
            instance.connectService(this.mExtTelManagerServiceCallback);
        }
    }

    /* access modifiers changed from: private */
    public void updateSubscriptionStatus() {
        updateNetworkProvider();
        ServiceState serviceState = getTelephonyManager().getServiceState();
        SignalStrength signalStrength = getTelephonyManager().getSignalStrength();
        updatePhoneNumber();
        updateServiceState(serviceState);
        updateSignalStrength(signalStrength);
        updateNetworkType();
        updateRoamingStatus(serviceState);
        updateIccidNumber();
        updateImsRegistrationState();
    }

    public void deinitialize() {
        ServiceCallback serviceCallback;
        if (this.mShowLatestAreaInfo) {
            CellBroadcastServiceConnection cellBroadcastServiceConnection = this.mCellBroadcastServiceConnection;
            if (!(cellBroadcastServiceConnection == null || cellBroadcastServiceConnection.getService() == null)) {
                this.mContext.unbindService(this.mCellBroadcastServiceConnection);
            }
            this.mCellBroadcastServiceConnection = null;
        }
        ExtTelephonyManager extTelephonyManager = this.mExtTelephonyManager;
        if (extTelephonyManager != null && (serviceCallback = this.mExtTelManagerServiceCallback) != null) {
            extTelephonyManager.disconnectService(serviceCallback);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (this.mSubscriptionInfo != null) {
            this.mTelephonyManager = getTelephonyManager().createForSubscriptionId(this.mSubscriptionInfo.getSubscriptionId());
            getTelephonyManager().registerTelephonyCallback(this.mContext.getMainExecutor(), this.mTelephonyCallback);
            getTelephonyManager().setSignalStrengthUpdateRequest(this.mSignalStrengthUpdateRequest);
            this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mContext.getMainExecutor(), this.mOnSubscriptionsChangedListener);
            registerImsRegistrationCallback(this.mSubscriptionInfo.getSubscriptionId());
            if (this.mShowLatestAreaInfo) {
                updateAreaInfoText();
                this.mContext.registerReceiver(this.mAreaInfoReceiver, new IntentFilter("android.telephony.action.AREA_INFO_UPDATED"));
            }
            this.mIsRegisteredListener = true;
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        if (subscriptionInfo != null) {
            unregisterImsRegistrationCallback(subscriptionInfo.getSubscriptionId());
            this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
            getTelephonyManager().unregisterTelephonyCallback(this.mTelephonyCallback);
            getTelephonyManager().clearSignalStrengthUpdateRequest(this.mSignalStrengthUpdateRequest);
            if (this.mShowLatestAreaInfo) {
                this.mContext.unregisterReceiver(this.mAreaInfoReceiver);
            }
        } else if (this.mIsRegisteredListener) {
            this.mSubscriptionManager.removeOnSubscriptionsChangedListener(this.mOnSubscriptionsChangedListener);
            getTelephonyManager().unregisterTelephonyCallback(this.mTelephonyCallback);
            getTelephonyManager().clearSignalStrengthUpdateRequest(this.mSignalStrengthUpdateRequest);
            if (this.mShowLatestAreaInfo) {
                this.mContext.unregisterReceiver(this.mAreaInfoReceiver);
            }
            this.mIsRegisteredListener = false;
        }
    }

    /* access modifiers changed from: private */
    public void updateNetworkProvider() {
        SubscriptionInfo subscriptionInfo;
        String registeredOperatorName;
        SubscriptionInfo subscriptionInfo2 = this.mSubscriptionInfo;
        CharSequence carrierName = subscriptionInfo2 != null ? subscriptionInfo2.getCarrierName() : null;
        if (!DomesticRoamUtils.isFeatureEnabled(this.mContext) || (subscriptionInfo = this.mSubscriptionInfo) == null || "" == (registeredOperatorName = DomesticRoamUtils.getRegisteredOperatorName(this.mContext, subscriptionInfo.getSubscriptionId()))) {
            this.mDialog.setText(NETWORK_PROVIDER_VALUE_ID, carrierName);
        } else {
            this.mDialog.setText(NETWORK_PROVIDER_VALUE_ID, registeredOperatorName);
        }
    }

    public void updatePhoneNumber() {
        this.mDialog.setText(PHONE_NUMBER_VALUE_ID, DeviceInfoUtils.getBidiFormattedPhoneNumber(this.mContext, this.mSubscriptionInfo));
    }

    /* access modifiers changed from: private */
    public void updateDataState(int i) {
        String str;
        if (i == 0) {
            str = this.mRes.getString(R$string.radioInfo_data_disconnected);
        } else if (i == 1) {
            str = this.mRes.getString(R$string.radioInfo_data_connecting);
        } else if (i != 2) {
            str = i != 3 ? this.mRes.getString(R$string.radioInfo_unknown) : this.mRes.getString(R$string.radioInfo_data_suspended);
        } else {
            str = this.mRes.getString(R$string.radioInfo_data_connected);
        }
        this.mDialog.setText(CELLULAR_NETWORK_STATE, str);
    }

    /* access modifiers changed from: private */
    public void updateAreaInfoText() {
        CellBroadcastServiceConnection cellBroadcastServiceConnection;
        ICellBroadcastService asInterface;
        if (this.mShowLatestAreaInfo && (cellBroadcastServiceConnection = this.mCellBroadcastServiceConnection) != null && (asInterface = ICellBroadcastService.Stub.asInterface(cellBroadcastServiceConnection.getService())) != null) {
            try {
                this.mDialog.setText(OPERATOR_INFO_VALUE_ID, asInterface.getCellBroadcastAreaInfo(this.mSlotIndex));
            } catch (RemoteException e) {
                Log.d("SimStatusDialogCtrl", "Can't get area info. e=" + e);
            }
        }
    }

    private void bindCellBroadcastService() {
        this.mCellBroadcastServiceConnection = new CellBroadcastServiceConnection();
        Intent intent = new Intent("android.telephony.CellBroadcastService");
        String cellBroadcastServicePackage = getCellBroadcastServicePackage();
        if (!TextUtils.isEmpty(cellBroadcastServicePackage)) {
            intent.setPackage(cellBroadcastServicePackage);
            CellBroadcastServiceConnection cellBroadcastServiceConnection = this.mCellBroadcastServiceConnection;
            if (cellBroadcastServiceConnection == null || cellBroadcastServiceConnection.getService() != null) {
                Log.d("SimStatusDialogCtrl", "skipping bindService because connection already exists");
            } else if (!this.mContext.bindService(intent, this.mCellBroadcastServiceConnection, 1)) {
                Log.e("SimStatusDialogCtrl", "Unable to bind to service");
            }
        }
    }

    private String getCellBroadcastServicePackage() {
        PackageManager packageManager = this.mContext.getPackageManager();
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(new Intent("android.telephony.CellBroadcastService"), 1048576);
        if (queryIntentServices.size() != 1) {
            Log.e("SimStatusDialogCtrl", "getCellBroadcastServicePackageName: found " + queryIntentServices.size() + " CBS packages");
        }
        for (ResolveInfo resolveInfo : queryIntentServices) {
            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
            if (serviceInfo != null) {
                String str = serviceInfo.packageName;
                if (TextUtils.isEmpty(str)) {
                    Log.e("SimStatusDialogCtrl", "getCellBroadcastServicePackageName: found a CBS package but packageName is null/empty");
                } else if (packageManager.checkPermission("android.permission.READ_PRIVILEGED_PHONE_STATE", str) == 0) {
                    Log.d("SimStatusDialogCtrl", "getCellBroadcastServicePackageName: " + str);
                    return str;
                } else {
                    Log.e("SimStatusDialogCtrl", "getCellBroadcastServicePackageName: " + str + " does not have READ_PRIVILEGED_PHONE_STATE permission");
                }
            }
        }
        Log.e("SimStatusDialogCtrl", "getCellBroadcastServicePackageName: package name not found");
        return null;
    }

    private void updateLatestAreaInfo() {
        boolean z = Resources.getSystem().getBoolean(17891747) && getTelephonyManager().getPhoneType() != 2;
        this.mShowLatestAreaInfo = z;
        if (z) {
            bindCellBroadcastService();
            return;
        }
        this.mDialog.removeSettingFromScreen(OPERATOR_INFO_LABEL_ID);
        this.mDialog.removeSettingFromScreen(OPERATOR_INFO_VALUE_ID);
    }

    /* access modifiers changed from: private */
    public void updateServiceState(ServiceState serviceState) {
        String str;
        int combinedServiceState = Utils.getCombinedServiceState(serviceState);
        if (!Utils.isInService(serviceState)) {
            resetSignalStrength();
        } else if (!Utils.isInService(this.mPreviousServiceState)) {
            updateSignalStrength(getTelephonyManager().getSignalStrength());
        }
        if (combinedServiceState == 0) {
            str = this.mRes.getString(R$string.radioInfo_service_in);
        } else if (combinedServiceState == 1 || combinedServiceState == 2) {
            str = this.mRes.getString(R$string.radioInfo_service_out);
        } else if (combinedServiceState != 3) {
            str = this.mRes.getString(R$string.radioInfo_unknown);
        } else {
            str = this.mRes.getString(R$string.radioInfo_service_off);
        }
        this.mDialog.setText(SERVICE_STATE_VALUE_ID, str);
    }

    /* access modifiers changed from: private */
    public void updateSignalStrength(SignalStrength signalStrength) {
        PersistableBundle configForSubId;
        if (signalStrength != null) {
            boolean z = true;
            SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
            if (!(subscriptionInfo == null || (configForSubId = this.mCarrierConfigManager.getConfigForSubId(subscriptionInfo.getSubscriptionId())) == null)) {
                z = configForSubId.getBoolean("show_signal_strength_in_sim_status_bool");
            }
            if (!z) {
                this.mDialog.removeSettingFromScreen(SIGNAL_STRENGTH_LABEL_ID);
                this.mDialog.removeSettingFromScreen(SIGNAL_STRENGTH_VALUE_ID);
            } else if (Utils.isInService(getTelephonyManager().getServiceState())) {
                this.mDialog.setText(SIGNAL_STRENGTH_VALUE_ID, buildDisplayOfSignalStrength(signalStrength));
            }
        }
    }

    private String buildDisplayOfSignalStrength(SignalStrength signalStrength) {
        List<CellSignalStrength> cellSignalStrengths = signalStrength.getCellSignalStrengths();
        if (cellSignalStrengths == null) {
            return this.mRes.getString(R$string.sim_signal_strength, new Object[]{0, 0});
        }
        StringBuilder sb = new StringBuilder();
        String str = "";
        boolean z = false;
        for (CellSignalStrength next : cellSignalStrengths) {
            if (next instanceof CellSignalStrengthCdma) {
                str = "CDMA ";
            } else if (next instanceof CellSignalStrengthGsm) {
                str = "GSM ";
            } else if (next instanceof CellSignalStrengthWcdma) {
                str = "WCDMA ";
            } else if (next instanceof CellSignalStrengthTdscdma) {
                str = "TDSCDMA ";
            } else if (next instanceof CellSignalStrengthLte) {
                str = "LTE ";
            } else if (next instanceof CellSignalStrengthNr) {
                str = "NR ";
                z = true;
            }
            int dbm = next.getDbm();
            if (dbm == -1) {
                dbm = 0;
            }
            int asuLevel = next.getAsuLevel();
            if (asuLevel == -1) {
                asuLevel = 0;
            }
            sb.append(str + this.mRes.getString(R$string.sim_signal_strength, new Object[]{Integer.valueOf(dbm), Integer.valueOf(asuLevel)}));
            sb.append("\n");
        }
        Log.d("SimStatusDialogCtrl", "isNrIncluded: " + z + ", mNrIconType: " + this.mNrIconType);
        if (!z && this.mNrIconType == 1) {
            sb.append("NR -- dBm -- asu");
            sb.append("\n");
        }
        if (sb.length() >= 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void resetSignalStrength() {
        this.mDialog.setText(SIGNAL_STRENGTH_VALUE_ID, "0");
    }

    /* access modifiers changed from: private */
    public void updateNetworkType() {
        int i;
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        boolean z = false;
        if (subscriptionInfo == null) {
            String networkTypeName = getNetworkTypeName(0);
            this.mDialog.setText(CELL_VOICE_NETWORK_TYPE_VALUE_ID, networkTypeName);
            this.mDialog.setText(CELL_DATA_NETWORK_TYPE_VALUE_ID, networkTypeName);
            return;
        }
        int subscriptionId = subscriptionInfo.getSubscriptionId();
        int dataNetworkType = getTelephonyManager().getDataNetworkType();
        int voiceNetworkType = getTelephonyManager().getVoiceNetworkType();
        boolean isWifiCallingAvailable = this.mTelephonyManager.isWifiCallingAvailable();
        Log.i("SimStatusDialogCtrl", "actualDataNetworkType = " + dataNetworkType + ", actualVoiceNetworkType = " + voiceNetworkType + ", isWifiCallingAvailable = " + isWifiCallingAvailable);
        if (isWifiCallingAvailable && voiceNetworkType == 0 && dataNetworkType == 18) {
            voiceNetworkType = 18;
        }
        TelephonyDisplayInfo telephonyDisplayInfo = this.mTelephonyDisplayInfo;
        if (telephonyDisplayInfo == null) {
            i = 0;
        } else {
            i = telephonyDisplayInfo.getOverrideNetworkType();
        }
        String str = null;
        String networkTypeName2 = dataNetworkType != 0 ? getNetworkTypeName(dataNetworkType) : null;
        if (voiceNetworkType != 0) {
            str = getNetworkTypeName(voiceNetworkType);
        }
        boolean z2 = i == 5 || i == 3;
        if (dataNetworkType == 13 && (z2 || this.mNrIconType == 1)) {
            networkTypeName2 = "NR NSA";
        }
        PersistableBundle configForSubId = this.mCarrierConfigManager.getConfigForSubId(subscriptionId);
        if (configForSubId != null) {
            z = configForSubId.getBoolean("show_4g_for_lte_data_icon_bool");
        }
        if (z) {
            if ("LTE".equals(networkTypeName2)) {
                networkTypeName2 = "4G";
            }
            if ("LTE".equals(str)) {
                str = "4G";
            }
        }
        this.mDialog.setText(CELL_VOICE_NETWORK_TYPE_VALUE_ID, str);
        this.mDialog.setText(CELL_DATA_NETWORK_TYPE_VALUE_ID, networkTypeName2);
    }

    /* access modifiers changed from: private */
    public void updateRoamingStatus(ServiceState serviceState) {
        if (serviceState == null) {
            this.mDialog.setText(ROAMING_INFO_VALUE_ID, this.mRes.getString(R$string.radioInfo_unknown));
        } else if (serviceState.getRoaming()) {
            this.mDialog.setText(ROAMING_INFO_VALUE_ID, this.mRes.getString(R$string.radioInfo_roaming_in));
        } else {
            this.mDialog.setText(ROAMING_INFO_VALUE_ID, this.mRes.getString(R$string.radioInfo_roaming_not));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r2.mCarrierConfigManager.getConfigForSubId(r0.getSubscriptionId());
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateIccidNumber() {
        /*
            r2 = this;
            android.telephony.SubscriptionInfo r0 = r2.mSubscriptionInfo
            if (r0 == 0) goto L_0x0018
            int r0 = r0.getSubscriptionId()
            android.telephony.CarrierConfigManager r1 = r2.mCarrierConfigManager
            android.os.PersistableBundle r0 = r1.getConfigForSubId(r0)
            if (r0 == 0) goto L_0x0018
            java.lang.String r1 = "show_iccid_in_sim_status_bool"
            boolean r0 = r0.getBoolean(r1)
            goto L_0x0019
        L_0x0018:
            r0 = 0
        L_0x0019:
            if (r0 != 0) goto L_0x002a
            com.android.settings.deviceinfo.simstatus.SimStatusDialogFragment r0 = r2.mDialog
            int r1 = ICCID_INFO_LABEL_ID
            r0.removeSettingFromScreen(r1)
            com.android.settings.deviceinfo.simstatus.SimStatusDialogFragment r2 = r2.mDialog
            int r0 = ICCID_INFO_VALUE_ID
            r2.removeSettingFromScreen(r0)
            goto L_0x0039
        L_0x002a:
            com.android.settings.deviceinfo.simstatus.SimStatusDialogFragment r0 = r2.mDialog
            int r1 = ICCID_INFO_VALUE_ID
            android.telephony.TelephonyManager r2 = r2.getTelephonyManager()
            java.lang.String r2 = r2.getSimSerialNumber()
            r0.setText(r1, r2)
        L_0x0039:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.deviceinfo.simstatus.SimStatusDialogController.updateIccidNumber():void");
    }

    /* access modifiers changed from: protected */
    public void requestForUpdateEid() {
        ThreadUtils.postOnBackgroundThread((Runnable) new SimStatusDialogController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$requestForUpdateEid$1() {
        ThreadUtils.postOnMainThread(new SimStatusDialogController$$ExternalSyntheticLambda1(this, getEid(this.mSlotIndex)));
    }

    public AtomicReference<String> getEid(int i) {
        String str;
        boolean z = true;
        if (getTelephonyManager().getActiveModemCount() > 1) {
            int intValue = ((Integer) this.mTelephonyManager.getLogicalToPhysicalSlotMapping().getOrDefault(Integer.valueOf(i), -1)).intValue();
            if (intValue != -1) {
                Iterator<UiccCardInfo> it = getTelephonyManager().getUiccCardsInfo().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    UiccCardInfo next = it.next();
                    if (next.getPhysicalSlotIndex() == intValue) {
                        if (next.isEuicc()) {
                            str = next.getEid();
                            if (TextUtils.isEmpty(str)) {
                                str = this.mEuiccManager.createForCardId(next.getCardId()).getEid();
                            }
                        }
                    }
                }
            }
        } else if (this.mEuiccManager.isEnabled()) {
            str = this.mEuiccManager.getEid();
            if (!z || str != null) {
                return new AtomicReference<>(str);
            }
            return null;
        }
        str = null;
        z = false;
        if (!z) {
        }
        return new AtomicReference<>(str);
    }

    /* access modifiers changed from: protected */
    /* renamed from: updateEid */
    public void lambda$requestForUpdateEid$0(AtomicReference<String> atomicReference) {
        if (atomicReference == null) {
            this.mDialog.removeSettingFromScreen(EID_INFO_LABEL_ID);
            this.mDialog.removeSettingFromScreen(EID_INFO_VALUE_ID);
        } else if (atomicReference.get() != null) {
            this.mDialog.setText(EID_INFO_VALUE_ID, atomicReference.get());
        }
    }

    private boolean isImsRegistrationStateShowUp() {
        PersistableBundle configForSubId;
        SubscriptionInfo subscriptionInfo = this.mSubscriptionInfo;
        if (subscriptionInfo == null || (configForSubId = this.mCarrierConfigManager.getConfigForSubId(subscriptionInfo.getSubscriptionId())) == null) {
            return false;
        }
        return configForSubId.getBoolean("show_ims_registration_status_bool");
    }

    private void updateImsRegistrationState() {
        if (!isImsRegistrationStateShowUp()) {
            this.mDialog.removeSettingFromScreen(IMS_REGISTRATION_STATE_LABEL_ID);
            this.mDialog.removeSettingFromScreen(IMS_REGISTRATION_STATE_VALUE_ID);
        }
    }

    /* access modifiers changed from: private */
    public void registerImsRegistrationCallback(int i) {
        if (isImsRegistrationStateShowUp()) {
            try {
                ImsMmTelManager.createForSubscriptionId(i).registerImsRegistrationCallback(this.mDialog.getContext().getMainExecutor(), this.mImsRegStateCallback);
            } catch (ImsException e) {
                Log.w("SimStatusDialogCtrl", "fail to register IMS status for subId=" + i, e);
            }
        }
    }

    /* access modifiers changed from: private */
    public void unregisterImsRegistrationCallback(int i) {
        if (isImsRegistrationStateShowUp()) {
            ImsMmTelManager.createForSubscriptionId(i).unregisterImsRegistrationCallback(this.mImsRegStateCallback);
        }
    }

    /* access modifiers changed from: private */
    public SubscriptionInfo getPhoneSubscriptionInfo(int i) {
        return SubscriptionManager.from(this.mContext).getActiveSubscriptionInfoForSimSlotIndex(i);
    }

    class SimStatusDialogTelephonyCallback extends TelephonyCallback implements TelephonyCallback.DataConnectionStateListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.ServiceStateListener, TelephonyCallback.DisplayInfoListener {
        SimStatusDialogTelephonyCallback() {
        }

        public void onDataConnectionStateChanged(int i, int i2) {
            SimStatusDialogController.this.updateDataState(i);
            SimStatusDialogController.this.updateNetworkType();
        }

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            SimStatusDialogController.this.updateSignalStrength(signalStrength);
        }

        public void onServiceStateChanged(ServiceState serviceState) {
            SimStatusDialogController.this.updateNetworkProvider();
            SimStatusDialogController.this.updateServiceState(serviceState);
            SimStatusDialogController.this.updateRoamingStatus(serviceState);
            SimStatusDialogController.this.mPreviousServiceState = serviceState;
        }

        public void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            SimStatusDialogController.this.mTelephonyDisplayInfo = telephonyDisplayInfo;
            SimStatusDialogController.this.updateNetworkType();
        }
    }
}
