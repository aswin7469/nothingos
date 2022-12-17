package com.android.settings.network.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.CellIdentity;
import android.telephony.CellInfo;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import androidx.annotation.Keep;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.internal.telephony.OperatorInfo;
import com.android.settings.R$bool;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.NetworkScanHelper;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Keep
public class NetworkSelectSettings extends DashboardFragment implements SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    private static final int EVENT_NETWORK_SCAN_COMPLETED = 4;
    private static final int EVENT_NETWORK_SCAN_ERROR = 3;
    private static final int EVENT_NETWORK_SCAN_RESULTS = 2;
    private static final int EVENT_SET_NETWORK_SELECTION_MANUALLY_DONE = 1;
    private static final int MIN_NUMBER_OF_SCAN_REQUIRED = 2;
    private static final String PREF_KEY_NETWORK_OPERATORS = "network_operators_preference";
    private static final String TAG = "NetworkSelectSettings";
    private final NetworkScanHelper.NetworkScanCallback mCallback = new NetworkScanHelper.NetworkScanCallback() {
        public void onResults(List<CellInfo> list) {
            NetworkSelectSettings.this.mHandler.obtainMessage(2, list).sendToTarget();
        }

        public void onComplete() {
            NetworkSelectSettings.this.mHandler.obtainMessage(4).sendToTarget();
        }

        public void onError(int i) {
            NetworkSelectSettings.this.mHandler.obtainMessage(3, i, 0).sendToTarget();
        }
    };
    List<CellInfo> mCellInfoList;
    private List<String> mForbiddenPlmns;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i;
            Log.d(NetworkSelectSettings.TAG, "handleMessage, msg.what: " + message.what);
            int i2 = message.what;
            if (i2 == 1) {
                boolean booleanValue = ((Boolean) message.obj).booleanValue();
                NetworkSelectSettings.this.setProgressBarVisible(false);
                NetworkSelectSettings.this.enablePreferenceScreen(true);
                NetworkOperatorPreference networkOperatorPreference = NetworkSelectSettings.this.mSelectedPreference;
                if (networkOperatorPreference != null) {
                    if (booleanValue) {
                        i = R$string.network_connected;
                    } else {
                        i = R$string.network_could_not_connect;
                    }
                    networkOperatorPreference.setSummary(i);
                    return;
                }
                Log.e(NetworkSelectSettings.TAG, "No preference to update!");
            } else if (i2 == 2) {
                NetworkSelectSettings.this.scanResultHandler((List) message.obj);
            } else if (i2 == 3) {
                NetworkSelectSettings.this.stopNetworkQuery();
                Log.i(NetworkSelectSettings.TAG, "Network scan failure " + message.arg1 + ": scan request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkScan) + ", waiting for scan results = " + NetworkSelectSettings.this.mWaitingForNumberOfScanResults + ", select request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkSelect));
                if (NetworkSelectSettings.this.mRequestIdManualNetworkScan >= NetworkSelectSettings.this.mRequestIdManualNetworkSelect) {
                    if (!NetworkSelectSettings.this.isPreferenceScreenEnabled()) {
                        NetworkSelectSettings.this.clearPreferenceSummary();
                        NetworkSelectSettings.this.enablePreferenceScreen(true);
                        return;
                    }
                    NetworkSelectSettings.this.addMessagePreference(R$string.network_query_error);
                }
            } else if (i2 == 4) {
                NetworkSelectSettings.this.stopNetworkQuery();
                Log.d(NetworkSelectSettings.TAG, "Network scan complete: scan request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkScan) + ", waiting for scan results = " + NetworkSelectSettings.this.mWaitingForNumberOfScanResults + ", select request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkSelect));
                if (NetworkSelectSettings.this.mRequestIdManualNetworkScan >= NetworkSelectSettings.this.mRequestIdManualNetworkSelect) {
                    if (!NetworkSelectSettings.this.isPreferenceScreenEnabled()) {
                        NetworkSelectSettings.this.clearPreferenceSummary();
                        NetworkSelectSettings.this.enablePreferenceScreen(true);
                        return;
                    }
                    NetworkSelectSettings networkSelectSettings = NetworkSelectSettings.this;
                    if (networkSelectSettings.mCellInfoList == null) {
                        networkSelectSettings.addMessagePreference(R$string.empty_networks_list);
                    }
                }
            }
        }
    };
    private boolean mIsAdvancedScanSupported;
    boolean mIsAggregationEnabled = false;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private final ExecutorService mNetworkScanExecutor = Executors.newFixedThreadPool(1);
    private NetworkScanHelper mNetworkScanHelper;
    private PreferenceCategory mPreferenceCategory;
    private View mProgressHeader;
    /* access modifiers changed from: private */
    public long mRequestIdManualNetworkScan;
    /* access modifiers changed from: private */
    public long mRequestIdManualNetworkSelect;
    NetworkOperatorPreference mSelectedPreference;
    private boolean mShow4GForLTE = false;
    private Preference mStatusMessagePreference;
    private int mSubId = -1;
    SubscriptionManager mSubscriptionManager;
    private SubscriptionsChangeListener mSubscriptionsChangeListener;
    private TelephonyManager mTelephonyManager;
    private boolean mUseNewApi;
    /* access modifiers changed from: private */
    public long mWaitingForNumberOfScanResults;

    public /* bridge */ /* synthetic */ int getHelpResource() {
        return super.getHelpResource();
    }

    /* access modifiers changed from: protected */
    public String getLogTag() {
        return TAG;
    }

    public int getMetricsCategory() {
        return 1581;
    }

    public void onAirplaneModeChanged(boolean z) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        onCreateInitialization();
    }

    /* access modifiers changed from: protected */
    @Keep
    public void onCreateInitialization() {
        this.mUseNewApi = enableNewAutoSelectNetworkUI(getContext());
        if (TelephonyUtils.isServiceConnected()) {
            this.mIsAdvancedScanSupported = TelephonyUtils.isAdvancedPlmnScanSupported(getContext());
        } else {
            Log.d(TAG, "ExtTelephonyService is not connected!!! ");
        }
        Log.d(TAG, "mIsAdvancedScanSupported: " + this.mIsAdvancedScanSupported);
        this.mSubId = getSubId();
        this.mPreferenceCategory = getPreferenceCategory(PREF_KEY_NETWORK_OPERATORS);
        Preference preference = new Preference(getContext());
        this.mStatusMessagePreference = preference;
        preference.setSelectable(false);
        this.mSelectedPreference = null;
        this.mTelephonyManager = getTelephonyManager(getContext(), this.mSubId);
        this.mSubscriptionManager = (SubscriptionManager) getContext().getSystemService(SubscriptionManager.class);
        this.mSubscriptionsChangeListener = new SubscriptionsChangeListener(getContext(), this);
        this.mNetworkScanHelper = new NetworkScanHelper(getContext(), this.mTelephonyManager, this.mCallback, this.mNetworkScanExecutor);
        PersistableBundle configForSubId = getCarrierConfigManager(getContext()).getConfigForSubId(this.mSubId);
        if (configForSubId != null) {
            this.mShow4GForLTE = configForSubId.getBoolean("show_4g_for_lte_data_icon_bool");
        }
        this.mMetricsFeatureProvider = getMetricsFeatureProvider(getContext());
        this.mIsAggregationEnabled = enableAggregation(getContext());
        Log.d(TAG, "init: mUseNewApi:" + this.mUseNewApi + " ,mIsAggregationEnabled:" + this.mIsAggregationEnabled + " ,mSubId:" + this.mSubId);
    }

    /* access modifiers changed from: protected */
    @Keep
    public boolean enableNewAutoSelectNetworkUI(Context context) {
        return context.getResources().getBoolean(17891642);
    }

    /* access modifiers changed from: protected */
    @Keep
    public boolean enableAggregation(Context context) {
        return context.getResources().getBoolean(R$bool.config_network_selection_list_aggregation_enabled);
    }

    /* access modifiers changed from: protected */
    @Keep
    public PreferenceCategory getPreferenceCategory(String str) {
        return (PreferenceCategory) findPreference(str);
    }

    /* access modifiers changed from: protected */
    @Keep
    public TelephonyManager getTelephonyManager(Context context, int i) {
        return ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
    }

    /* access modifiers changed from: protected */
    @Keep
    public CarrierConfigManager getCarrierConfigManager(Context context) {
        return (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
    }

    /* access modifiers changed from: protected */
    @Keep
    public MetricsFeatureProvider getMetricsFeatureProvider(Context context) {
        return FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    /* access modifiers changed from: protected */
    @Keep
    public boolean isPreferenceScreenEnabled() {
        return getPreferenceScreen().isEnabled();
    }

    /* access modifiers changed from: protected */
    @Keep
    public void enablePreferenceScreen(boolean z) {
        getPreferenceScreen().setEnabled(z);
    }

    /* access modifiers changed from: protected */
    @Keep
    public int getSubId() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            return intent.getIntExtra("android.provider.extra.SUB_ID", -1);
        }
        return -1;
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getActivity() != null) {
            this.mProgressHeader = setPinnedHeaderView(R$layout.progress_header).findViewById(R$id.progress_bar_animation);
            setProgressBarVisible(false);
        }
    }

    public void onStart() {
        Log.d(TAG, "onStart()");
        super.onStart();
        this.mSubscriptionsChangeListener.start();
        updateForbiddenPlmns();
        if (!isProgressBarVisible() && this.mWaitingForNumberOfScanResults <= 0) {
            this.mSelectedPreference = null;
            startNetworkQuery();
        }
    }

    /* access modifiers changed from: protected */
    @Keep
    public void updateForbiddenPlmns() {
        List<String> list;
        String[] forbiddenPlmns = this.mTelephonyManager.getForbiddenPlmns();
        if (forbiddenPlmns != null) {
            list = Arrays.asList(forbiddenPlmns);
        } else {
            list = new ArrayList<>();
        }
        this.mForbiddenPlmns = list;
    }

    public void onStop() {
        Log.d(TAG, "onStop() mWaitingForNumberOfScanResults: " + this.mWaitingForNumberOfScanResults);
        this.mSubscriptionsChangeListener.stop();
        super.onStop();
        if (this.mWaitingForNumberOfScanResults <= 0) {
            stopNetworkQuery();
        }
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference != this.mSelectedPreference) {
            stopNetworkQuery();
            clearPreferenceSummary();
            NetworkOperatorPreference networkOperatorPreference = this.mSelectedPreference;
            if (networkOperatorPreference != null) {
                networkOperatorPreference.setSummary(R$string.network_disconnected);
            }
            NetworkOperatorPreference networkOperatorPreference2 = (NetworkOperatorPreference) preference;
            this.mSelectedPreference = networkOperatorPreference2;
            networkOperatorPreference2.setSummary(R$string.network_connecting);
            this.mMetricsFeatureProvider.action(getContext(), 1210, (Pair<Integer, Object>[]) new Pair[0]);
            setProgressBarVisible(true);
            enablePreferenceScreen(false);
            this.mRequestIdManualNetworkSelect = getNewRequestId();
            this.mWaitingForNumberOfScanResults = 2;
            ThreadUtils.postOnBackgroundThread((Runnable) new NetworkSelectSettings$$ExternalSyntheticLambda1(this, this.mSelectedPreference.getOperatorInfo()));
        }
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreferenceTreeClick$0(OperatorInfo operatorInfo) {
        Message obtainMessage = this.mHandler.obtainMessage(1);
        obtainMessage.obj = Boolean.valueOf(this.mTelephonyManager.setNetworkSelectionModeManual(operatorInfo, true));
        obtainMessage.sendToTarget();
    }

    public void onSubscriptionsChanged() {
        FragmentActivity activity;
        boolean isActiveSubscriptionId = this.mSubscriptionManager.isActiveSubscriptionId(this.mSubId);
        Log.d(TAG, "onSubscriptionsChanged, mSubId: " + this.mSubId + ", isActive: " + isActiveSubscriptionId);
        if (!isActiveSubscriptionId && (activity = getActivity()) != null && !activity.isFinishing() && !activity.isDestroyed()) {
            Log.d(TAG, "Calling finish");
            activity.finish();
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.choose_network;
    }

    /* access modifiers changed from: package-private */
    public List<CellInfo> doAggregation(List<CellInfo> list) {
        if (!this.mIsAggregationEnabled) {
            Log.d(TAG, "no aggregation");
            return new ArrayList(list);
        }
        ArrayList arrayList = new ArrayList();
        for (CellInfo next : list) {
            Optional findFirst = arrayList.stream().filter(new NetworkSelectSettings$$ExternalSyntheticLambda0(CellInfoUtil.getNetworkTitle(next.getCellIdentity(), CellInfoUtil.getCellIdentityMccMnc(next.getCellIdentity())), next.getClass())).findFirst();
            if (!findFirst.isPresent()) {
                arrayList.add(next);
            } else if (next.isRegistered() && !((CellInfo) findFirst.get()).isRegistered()) {
                arrayList.set(arrayList.indexOf(findFirst.get()), next);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$doAggregation$1(String str, Class cls, CellInfo cellInfo) {
        return CellInfoUtil.getNetworkTitle(cellInfo.getCellIdentity(), CellInfoUtil.getCellIdentityMccMnc(cellInfo.getCellIdentity())).equals(str) && cellInfo.getClass().equals(cls);
    }

    /* access modifiers changed from: protected */
    @Keep
    public void scanResultHandler(List<CellInfo> list) {
        NetworkOperatorPreference networkOperatorPreference;
        if (this.mRequestIdManualNetworkScan < this.mRequestIdManualNetworkSelect) {
            Log.d(TAG, "CellInfoList (drop): " + CellInfoUtil.cellInfoListToString(new ArrayList(list)));
            return;
        }
        long j = this.mWaitingForNumberOfScanResults - 1;
        this.mWaitingForNumberOfScanResults = j;
        if (j <= 0 && !isResumed()) {
            stopNetworkQuery();
        }
        this.mCellInfoList = doAggregation(list);
        Log.d(TAG, "CellInfoList: " + CellInfoUtil.cellInfoListToString(this.mCellInfoList));
        List<CellInfo> list2 = this.mCellInfoList;
        if (list2 != null && list2.size() != 0) {
            NetworkOperatorPreference updateAllPreferenceCategory = updateAllPreferenceCategory();
            if (updateAllPreferenceCategory != null) {
                if (this.mSelectedPreference != null) {
                    this.mSelectedPreference = updateAllPreferenceCategory;
                }
            } else if (!isPreferenceScreenEnabled() && updateAllPreferenceCategory == null && (networkOperatorPreference = this.mSelectedPreference) != null) {
                networkOperatorPreference.setSummary(R$string.network_connecting);
            }
            enablePreferenceScreen(true);
        } else if (isPreferenceScreenEnabled()) {
            addMessagePreference(R$string.empty_networks_list);
            setProgressBarVisible(true);
        }
    }

    /* access modifiers changed from: protected */
    @Keep
    public NetworkOperatorPreference createNetworkOperatorPreference(CellInfo cellInfo) {
        return new NetworkOperatorPreference(getPrefContext(), cellInfo, this.mForbiddenPlmns, this.mShow4GForLTE, MobileNetworkUtils.getAccessMode(getContext(), this.mTelephonyManager.getSlotIndex()));
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0048  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0082  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.android.settings.network.telephony.NetworkOperatorPreference updateAllPreferenceCategory() {
        /*
            r8 = this;
            androidx.preference.PreferenceCategory r0 = r8.mPreferenceCategory
            int r0 = r0.getPreferenceCount()
        L_0x0006:
            java.util.List<android.telephony.CellInfo> r1 = r8.mCellInfoList
            int r1 = r1.size()
            if (r0 <= r1) goto L_0x001a
            int r0 = r0 + -1
            androidx.preference.PreferenceCategory r1 = r8.mPreferenceCategory
            androidx.preference.Preference r2 = r1.getPreference(r0)
            r1.removePreference(r2)
            goto L_0x0006
        L_0x001a:
            r1 = 0
            r2 = 0
            r3 = r1
            r4 = r2
        L_0x001e:
            java.util.List<android.telephony.CellInfo> r5 = r8.mCellInfoList
            int r5 = r5.size()
            if (r3 >= r5) goto L_0x0088
            java.util.List<android.telephony.CellInfo> r5 = r8.mCellInfoList
            java.lang.Object r5 = r5.get(r3)
            android.telephony.CellInfo r5 = (android.telephony.CellInfo) r5
            if (r3 >= r0) goto L_0x0045
            androidx.preference.PreferenceCategory r6 = r8.mPreferenceCategory
            androidx.preference.Preference r6 = r6.getPreference(r3)
            boolean r7 = r6 instanceof com.android.settings.network.telephony.NetworkOperatorPreference
            if (r7 == 0) goto L_0x0040
            com.android.settings.network.telephony.NetworkOperatorPreference r6 = (com.android.settings.network.telephony.NetworkOperatorPreference) r6
            r6.updateCell(r5)
            goto L_0x0046
        L_0x0040:
            androidx.preference.PreferenceCategory r7 = r8.mPreferenceCategory
            r7.removePreference(r6)
        L_0x0045:
            r6 = r2
        L_0x0046:
            if (r6 != 0) goto L_0x0066
            com.android.settings.network.telephony.NetworkOperatorPreference r6 = r8.createNetworkOperatorPreference(r5)
            r6.setOrder(r3)
            android.content.Context r7 = r8.getContext()
            boolean r7 = com.android.settings.network.telephony.DomesticRoamUtils.isFeatureEnabled(r7)
            if (r7 == 0) goto L_0x0061
            int r7 = r8.mSubId
            r6.setSubId(r7)
            r6.updateCell(r5)
        L_0x0061:
            androidx.preference.PreferenceCategory r5 = r8.mPreferenceCategory
            r5.addPreference(r6)
        L_0x0066:
            java.lang.String r5 = r6.getOperatorName()
            r6.setKey(r5)
            java.util.List<android.telephony.CellInfo> r5 = r8.mCellInfoList
            java.lang.Object r5 = r5.get(r3)
            android.telephony.CellInfo r5 = (android.telephony.CellInfo) r5
            boolean r5 = r5.isRegistered()
            if (r5 == 0) goto L_0x0082
            int r4 = com.android.settings.R$string.network_connected
            r6.setSummary((int) r4)
            r4 = r6
            goto L_0x0085
        L_0x0082:
            r6.setSummary((java.lang.CharSequence) r2)
        L_0x0085:
            int r3 = r3 + 1
            goto L_0x001e
        L_0x0088:
            java.util.List<android.telephony.CellInfo> r0 = r8.mCellInfoList
            int r0 = r0.size()
            if (r1 >= r0) goto L_0x00af
            java.util.List<android.telephony.CellInfo> r0 = r8.mCellInfoList
            java.lang.Object r0 = r0.get(r1)
            android.telephony.CellInfo r0 = (android.telephony.CellInfo) r0
            com.android.settings.network.telephony.NetworkOperatorPreference r2 = r8.mSelectedPreference
            if (r2 == 0) goto L_0x00ac
            boolean r0 = r2.isSameCell(r0)
            if (r0 == 0) goto L_0x00ac
            androidx.preference.PreferenceCategory r0 = r8.mPreferenceCategory
            androidx.preference.Preference r0 = r0.getPreference(r1)
            com.android.settings.network.telephony.NetworkOperatorPreference r0 = (com.android.settings.network.telephony.NetworkOperatorPreference) r0
            r8.mSelectedPreference = r0
        L_0x00ac:
            int r1 = r1 + 1
            goto L_0x0088
        L_0x00af:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.network.telephony.NetworkSelectSettings.updateAllPreferenceCategory():com.android.settings.network.telephony.NetworkOperatorPreference");
    }

    private void forceUpdateConnectedPreferenceCategory() {
        ServiceState serviceState;
        List<NetworkRegistrationInfo> networkRegistrationInfoListForTransportType;
        int dataState = this.mTelephonyManager.getDataState();
        TelephonyManager telephonyManager = this.mTelephonyManager;
        if (dataState == 2 && (serviceState = telephonyManager.getServiceState()) != null && (networkRegistrationInfoListForTransportType = serviceState.getNetworkRegistrationInfoListForTransportType(1)) != null && networkRegistrationInfoListForTransportType.size() != 0) {
            if (this.mForbiddenPlmns == null) {
                updateForbiddenPlmns();
            }
            HashSet<CellIdentity> hashSet = new HashSet<>();
            for (NetworkRegistrationInfo networkRegistrationInfo : networkRegistrationInfoListForTransportType) {
                Log.d(TAG, "regInfo: " + networkRegistrationInfo.toString());
                CellIdentity cellIdentity = networkRegistrationInfo.getCellIdentity();
                if (cellIdentity != null) {
                    hashSet.add(cellIdentity);
                }
            }
            for (CellIdentity cellIdentity2 : hashSet) {
                if (cellIdentity2 != null) {
                    NetworkOperatorPreference networkOperatorPreference = new NetworkOperatorPreference(getPrefContext(), cellIdentity2, this.mForbiddenPlmns, this.mShow4GForLTE, MobileNetworkUtils.getAccessMode(getContext(), this.mTelephonyManager.getSlotIndex()));
                    if (!networkOperatorPreference.isForbiddenNetwork()) {
                        networkOperatorPreference.setSummary(R$string.network_connected);
                        networkOperatorPreference.setIcon(4);
                        this.mPreferenceCategory.addPreference(networkOperatorPreference);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void clearPreferenceSummary() {
        int preferenceCount = this.mPreferenceCategory.getPreferenceCount();
        while (preferenceCount > 0) {
            preferenceCount--;
            ((NetworkOperatorPreference) this.mPreferenceCategory.getPreference(preferenceCount)).setSummary((CharSequence) null);
        }
    }

    private long getNewRequestId() {
        return Math.max(this.mRequestIdManualNetworkSelect, this.mRequestIdManualNetworkScan) + 1;
    }

    private boolean isProgressBarVisible() {
        View view = this.mProgressHeader;
        if (view != null && view.getVisibility() == 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public void setProgressBarVisible(boolean z) {
        View view = this.mProgressHeader;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
        }
    }

    /* access modifiers changed from: private */
    public void addMessagePreference(int i) {
        setProgressBarVisible(false);
        this.mStatusMessagePreference.setTitle(i);
        this.mPreferenceCategory.removeAll();
        this.mPreferenceCategory.addPreference(this.mStatusMessagePreference);
    }

    private void startNetworkQuery() {
        int i = 1;
        setProgressBarVisible(true);
        if (this.mNetworkScanHelper != null) {
            this.mRequestIdManualNetworkScan = getNewRequestId();
            this.mWaitingForNumberOfScanResults = 2;
            NetworkScanHelper networkScanHelper = this.mNetworkScanHelper;
            if (!this.mIsAdvancedScanSupported) {
                i = 2;
            }
            networkScanHelper.startNetworkScan(i);
        }
    }

    /* access modifiers changed from: private */
    public void stopNetworkQuery() {
        setProgressBarVisible(false);
        NetworkScanHelper networkScanHelper = this.mNetworkScanHelper;
        if (networkScanHelper != null) {
            this.mWaitingForNumberOfScanResults = 0;
            networkScanHelper.stopNetworkQuery();
        }
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        stopNetworkQuery();
        this.mNetworkScanExecutor.shutdown();
        super.onDestroy();
    }
}
