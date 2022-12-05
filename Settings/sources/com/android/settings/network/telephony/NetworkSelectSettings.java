package com.android.settings.network.telephony;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.CellInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import com.android.internal.telephony.OperatorInfo;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.NetworkScanHelper;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class NetworkSelectSettings extends DashboardFragment implements SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    List<CellInfo> mCellInfoList;
    private List<String> mForbiddenPlmns;
    private boolean mIsAdvancedScanSupported;
    private MetricsFeatureProvider mMetricsFeatureProvider;
    private NetworkScanHelper mNetworkScanHelper;
    PreferenceCategory mPreferenceCategory;
    private View mProgressHeader;
    private long mRequestIdManualNetworkScan;
    private long mRequestIdManualNetworkSelect;
    NetworkOperatorPreference mSelectedPreference;
    private Preference mStatusMessagePreference;
    SubscriptionManager mSubscriptionManager;
    private SubscriptionsChangeListener mSubscriptionsChangeListener;
    TelephonyManager mTelephonyManager;
    private boolean mUseNewApi;
    private long mWaitingForNumberOfScanResults;
    private int mSubId = -1;
    private boolean mShow4GForLTE = false;
    private final ExecutorService mNetworkScanExecutor = Executors.newFixedThreadPool(1);
    boolean mIsAggregationEnabled = false;
    private final Handler mHandler = new Handler() { // from class: com.android.settings.network.telephony.NetworkSelectSettings.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i;
            NetworkOperatorPreference networkOperatorPreference;
            Log.d("NetworkSelectSettings", "handleMessage, msg.what: " + message.what);
            int i2 = message.what;
            if (i2 == 1) {
                boolean booleanValue = ((Boolean) message.obj).booleanValue();
                NetworkSelectSettings.this.setProgressBarVisible(false);
                NetworkSelectSettings.this.getPreferenceScreen().setEnabled(true);
                NetworkOperatorPreference networkOperatorPreference2 = NetworkSelectSettings.this.mSelectedPreference;
                if (networkOperatorPreference2 != null) {
                    if (booleanValue) {
                        i = R.string.network_connected;
                    } else {
                        i = R.string.network_could_not_connect;
                    }
                    networkOperatorPreference2.setSummary(i);
                    return;
                }
                Log.e("NetworkSelectSettings", "No preference to update!");
            } else if (i2 == 2) {
                List<CellInfo> list = (List) message.obj;
                if (NetworkSelectSettings.this.mRequestIdManualNetworkScan < NetworkSelectSettings.this.mRequestIdManualNetworkSelect) {
                    Log.d("NetworkSelectSettings", "CellInfoList (drop): " + CellInfoUtil.cellInfoListToString(new ArrayList(list)));
                    return;
                }
                NetworkSelectSettings.access$410(NetworkSelectSettings.this);
                if (NetworkSelectSettings.this.mWaitingForNumberOfScanResults <= 0 && !NetworkSelectSettings.this.isResumed()) {
                    NetworkSelectSettings.this.stopNetworkQuery();
                }
                NetworkSelectSettings networkSelectSettings = NetworkSelectSettings.this;
                networkSelectSettings.mCellInfoList = networkSelectSettings.doAggregation(list);
                Log.d("NetworkSelectSettings", "CellInfoList size: " + NetworkSelectSettings.this.mCellInfoList.size());
                Log.d("NetworkSelectSettings", "CellInfoList: " + CellInfoUtil.cellInfoListToString(NetworkSelectSettings.this.mCellInfoList));
                List<CellInfo> list2 = NetworkSelectSettings.this.mCellInfoList;
                if (list2 != null && list2.size() != 0) {
                    NetworkOperatorPreference updateAllPreferenceCategory = NetworkSelectSettings.this.updateAllPreferenceCategory();
                    if (updateAllPreferenceCategory != null) {
                        NetworkSelectSettings networkSelectSettings2 = NetworkSelectSettings.this;
                        if (networkSelectSettings2.mSelectedPreference != null) {
                            networkSelectSettings2.mSelectedPreference = updateAllPreferenceCategory;
                        }
                    } else if (!NetworkSelectSettings.this.getPreferenceScreen().isEnabled() && updateAllPreferenceCategory == null && (networkOperatorPreference = NetworkSelectSettings.this.mSelectedPreference) != null) {
                        networkOperatorPreference.setSummary(R.string.network_connecting);
                    }
                    NetworkSelectSettings.this.getPreferenceScreen().setEnabled(true);
                } else if (!NetworkSelectSettings.this.getPreferenceScreen().isEnabled()) {
                } else {
                    NetworkSelectSettings.this.addMessagePreference(R.string.empty_networks_list);
                    NetworkSelectSettings.this.setProgressBarVisible(true);
                }
            } else if (i2 == 3) {
                NetworkSelectSettings.this.stopNetworkQuery();
                Log.i("NetworkSelectSettings", "Network scan failure " + message.arg1 + ": scan request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkScan) + ", waiting for scan results = " + NetworkSelectSettings.this.mWaitingForNumberOfScanResults + ", select request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkSelect));
                if (NetworkSelectSettings.this.mRequestIdManualNetworkScan < NetworkSelectSettings.this.mRequestIdManualNetworkSelect) {
                    return;
                }
                if (!NetworkSelectSettings.this.getPreferenceScreen().isEnabled()) {
                    NetworkSelectSettings.this.clearPreferenceSummary();
                    NetworkSelectSettings.this.getPreferenceScreen().setEnabled(true);
                    return;
                }
                NetworkSelectSettings.this.addMessagePreference(R.string.network_query_error);
            } else if (i2 != 4) {
            } else {
                NetworkSelectSettings.this.stopNetworkQuery();
                Log.d("NetworkSelectSettings", "Network scan complete: scan request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkScan) + ", waiting for scan results = " + NetworkSelectSettings.this.mWaitingForNumberOfScanResults + ", select request 0x" + Long.toHexString(NetworkSelectSettings.this.mRequestIdManualNetworkSelect));
                if (NetworkSelectSettings.this.mRequestIdManualNetworkScan < NetworkSelectSettings.this.mRequestIdManualNetworkSelect) {
                    return;
                }
                if (!NetworkSelectSettings.this.getPreferenceScreen().isEnabled()) {
                    NetworkSelectSettings.this.clearPreferenceSummary();
                    NetworkSelectSettings.this.getPreferenceScreen().setEnabled(true);
                    return;
                }
                NetworkSelectSettings networkSelectSettings3 = NetworkSelectSettings.this;
                if (networkSelectSettings3.mCellInfoList != null) {
                    return;
                }
                networkSelectSettings3.addMessagePreference(R.string.empty_networks_list);
            }
        }
    };
    private final NetworkScanHelper.NetworkScanCallback mCallback = new NetworkScanHelper.NetworkScanCallback() { // from class: com.android.settings.network.telephony.NetworkSelectSettings.3
        @Override // com.android.settings.network.telephony.NetworkScanHelper.NetworkScanCallback
        public void onResults(List<CellInfo> list) {
            NetworkSelectSettings.this.mHandler.obtainMessage(2, list).sendToTarget();
        }

        @Override // com.android.settings.network.telephony.NetworkScanHelper.NetworkScanCallback
        public void onComplete() {
            NetworkSelectSettings.this.mHandler.obtainMessage(4).sendToTarget();
        }

        @Override // com.android.settings.network.telephony.NetworkScanHelper.NetworkScanCallback
        public void onError(int i) {
            NetworkSelectSettings.this.mHandler.obtainMessage(3, i, 0).sendToTarget();
        }
    };

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "NetworkSelectSettings";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1581;
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
    }

    static /* synthetic */ long access$410(NetworkSelectSettings networkSelectSettings) {
        long j = networkSelectSettings.mWaitingForNumberOfScanResults;
        networkSelectSettings.mWaitingForNumberOfScanResults = j - 1;
        return j;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (TelephonyUtils.isServiceConnected()) {
            this.mIsAdvancedScanSupported = TelephonyUtils.isAdvancedPlmnScanSupported(getContext());
        } else {
            Log.d("NetworkSelectSettings", "ExtTelephonyService is not connected!!! ");
        }
        Log.d("NetworkSelectSettings", "mIsAdvancedScanSupported: " + this.mIsAdvancedScanSupported);
        this.mSubId = getArguments().getInt("android.provider.extra.SUB_ID");
        this.mPreferenceCategory = (PreferenceCategory) findPreference("network_operators_preference");
        Preference preference = new Preference(getContext());
        this.mStatusMessagePreference = preference;
        preference.setSelectable(false);
        this.mSelectedPreference = null;
        this.mTelephonyManager = ((TelephonyManager) getContext().getSystemService(TelephonyManager.class)).createForSubscriptionId(this.mSubId);
        this.mSubscriptionManager = (SubscriptionManager) getContext().getSystemService(SubscriptionManager.class);
        this.mSubscriptionsChangeListener = new SubscriptionsChangeListener(getContext(), this);
        this.mNetworkScanHelper = new NetworkScanHelper(getContext(), this.mTelephonyManager, this.mCallback, this.mNetworkScanExecutor);
        PersistableBundle configForSubId = ((CarrierConfigManager) getContext().getSystemService("carrier_config")).getConfigForSubId(this.mSubId);
        if (configForSubId != null) {
            this.mShow4GForLTE = configForSubId.getBoolean("show_4g_for_lte_data_icon_bool");
        }
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(getContext()).getMetricsFeatureProvider();
        this.mIsAggregationEnabled = getContext().getResources().getBoolean(R.bool.config_network_selection_list_aggregation_enabled);
        Log.d("NetworkSelectSettings", "init: mUseNewApi:" + this.mUseNewApi + " ,mIsAggregationEnabled:" + this.mIsAggregationEnabled);
        new AsyncTask<Void, Void, List<String>>() { // from class: com.android.settings.network.telephony.NetworkSelectSettings.1
            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public List<String> doInBackground(Void... voidArr) {
                String[] forbiddenPlmns = NetworkSelectSettings.this.mTelephonyManager.getForbiddenPlmns();
                Log.d("NetworkSelectSettings", "forbiddenPlmns: " + forbiddenPlmns);
                if (forbiddenPlmns != null) {
                    return Arrays.asList(forbiddenPlmns);
                }
                return null;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // android.os.AsyncTask
            public void onPostExecute(List<String> list) {
                NetworkSelectSettings.this.mForbiddenPlmns = list;
                NetworkSelectSettings.this.startNetworkQuery();
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getActivity() != null) {
            this.mProgressHeader = setPinnedHeaderView(R.layout.progress_header).findViewById(R.id.progress_bar_animation);
            setProgressBarVisible(false);
        }
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStart() {
        Log.d("NetworkSelectSettings", "onStart()");
        super.onStart();
        this.mSubscriptionsChangeListener.start();
        updateForbiddenPlmns();
        isProgressBarVisible();
    }

    void updateForbiddenPlmns() {
        List<String> arrayList;
        String[] forbiddenPlmns = this.mTelephonyManager.getForbiddenPlmns();
        if (forbiddenPlmns != null) {
            arrayList = Arrays.asList(forbiddenPlmns);
        } else {
            arrayList = new ArrayList<>();
        }
        this.mForbiddenPlmns = arrayList;
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStop() {
        Log.d("NetworkSelectSettings", "onStop() mWaitingForNumberOfScanResults: " + this.mWaitingForNumberOfScanResults);
        this.mSubscriptionsChangeListener.stop();
        super.onStop();
    }

    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference != this.mSelectedPreference) {
            stopNetworkQuery();
            clearPreferenceSummary();
            NetworkOperatorPreference networkOperatorPreference = this.mSelectedPreference;
            if (networkOperatorPreference != null) {
                networkOperatorPreference.setSummary(R.string.network_disconnected);
            }
            NetworkOperatorPreference networkOperatorPreference2 = (NetworkOperatorPreference) preference;
            this.mSelectedPreference = networkOperatorPreference2;
            networkOperatorPreference2.setSummary(R.string.network_connecting);
            this.mMetricsFeatureProvider.action(getContext(), 1210, new Pair[0]);
            setProgressBarVisible(true);
            getPreferenceScreen().setEnabled(false);
            this.mRequestIdManualNetworkSelect = getNewRequestId();
            this.mWaitingForNumberOfScanResults = 2L;
            final OperatorInfo operatorInfo = this.mSelectedPreference.getOperatorInfo();
            ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.android.settings.network.telephony.NetworkSelectSettings$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    NetworkSelectSettings.this.lambda$onPreferenceTreeClick$0(operatorInfo);
                }
            });
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPreferenceTreeClick$0(OperatorInfo operatorInfo) {
        Message obtainMessage = this.mHandler.obtainMessage(1);
        obtainMessage.obj = Boolean.valueOf(this.mTelephonyManager.setNetworkSelectionModeManual(operatorInfo, true));
        obtainMessage.sendToTarget();
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        FragmentActivity activity;
        boolean isActiveSubscriptionId = this.mSubscriptionManager.isActiveSubscriptionId(this.mSubId);
        Log.d("NetworkSelectSettings", "onSubscriptionsChanged, mSubId: " + this.mSubId + ", isActive: " + isActiveSubscriptionId);
        if (isActiveSubscriptionId || (activity = getActivity()) == null || activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        Log.d("NetworkSelectSettings", "Calling finish");
        activity.finish();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.choose_network;
    }

    List<CellInfo> doAggregation(List<CellInfo> list) {
        if (!this.mIsAggregationEnabled) {
            Log.d("NetworkSelectSettings", "no aggregation");
            return new ArrayList(list);
        }
        ArrayList arrayList = new ArrayList();
        for (CellInfo cellInfo : list) {
            final String networkTitle = CellInfoUtil.getNetworkTitle(cellInfo.getCellIdentity(), CellInfoUtil.getCellIdentityMccMnc(cellInfo.getCellIdentity()));
            final Class<?> cls = cellInfo.getClass();
            if (!arrayList.stream().anyMatch(new Predicate() { // from class: com.android.settings.network.telephony.NetworkSelectSettings$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$doAggregation$1;
                    lambda$doAggregation$1 = NetworkSelectSettings.lambda$doAggregation$1(networkTitle, cls, (CellInfo) obj);
                    return lambda$doAggregation$1;
                }
            })) {
                arrayList.add(cellInfo);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$doAggregation$1(String str, Class cls, CellInfo cellInfo) {
        return CellInfoUtil.getNetworkTitle(cellInfo.getCellIdentity(), CellInfoUtil.getCellIdentityMccMnc(cellInfo.getCellIdentity())).equals(str) && cellInfo.getClass().equals(cls);
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0079  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    NetworkOperatorPreference updateAllPreferenceCategory() {
        NetworkOperatorPreference networkOperatorPreference;
        int preferenceCount = this.mPreferenceCategory.getPreferenceCount();
        while (preferenceCount > this.mCellInfoList.size()) {
            preferenceCount--;
            PreferenceCategory preferenceCategory = this.mPreferenceCategory;
            preferenceCategory.removePreference(preferenceCategory.getPreference(preferenceCount));
        }
        NetworkOperatorPreference networkOperatorPreference2 = null;
        for (int i = 0; i < this.mCellInfoList.size(); i++) {
            CellInfo cellInfo = this.mCellInfoList.get(i);
            if (i < preferenceCount) {
                Preference preference = this.mPreferenceCategory.getPreference(i);
                if (preference instanceof NetworkOperatorPreference) {
                    networkOperatorPreference = (NetworkOperatorPreference) preference;
                    networkOperatorPreference.updateCell(cellInfo);
                    if (networkOperatorPreference == null) {
                        networkOperatorPreference = new NetworkOperatorPreference(getPrefContext(), cellInfo, this.mForbiddenPlmns, this.mShow4GForLTE);
                        networkOperatorPreference.setOrder(i);
                        this.mPreferenceCategory.addPreference(networkOperatorPreference);
                    }
                    networkOperatorPreference.setKey(networkOperatorPreference.getOperatorName());
                    if (!this.mCellInfoList.get(i).isRegistered()) {
                        networkOperatorPreference.setSummary(R.string.network_connected);
                        networkOperatorPreference2 = networkOperatorPreference;
                    } else {
                        networkOperatorPreference.setSummary((CharSequence) null);
                    }
                } else {
                    this.mPreferenceCategory.removePreference(preference);
                }
            }
            networkOperatorPreference = null;
            if (networkOperatorPreference == null) {
            }
            networkOperatorPreference.setKey(networkOperatorPreference.getOperatorName());
            if (!this.mCellInfoList.get(i).isRegistered()) {
            }
        }
        for (int i2 = 0; i2 < this.mCellInfoList.size(); i2++) {
            CellInfo cellInfo2 = this.mCellInfoList.get(i2);
            NetworkOperatorPreference networkOperatorPreference3 = this.mSelectedPreference;
            if (networkOperatorPreference3 != null && networkOperatorPreference3.isSameCell(cellInfo2)) {
                this.mSelectedPreference = (NetworkOperatorPreference) this.mPreferenceCategory.getPreference(i2);
            }
        }
        return networkOperatorPreference2;
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        return view != null && view.getVisibility() == 0;
    }

    protected void setProgressBarVisible(boolean z) {
        View view = this.mProgressHeader;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addMessagePreference(int i) {
        setProgressBarVisible(false);
        this.mStatusMessagePreference.setTitle(i);
        this.mPreferenceCategory.removeAll();
        this.mPreferenceCategory.addPreference(this.mStatusMessagePreference);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startNetworkQuery() {
        int i = 1;
        setProgressBarVisible(true);
        if (this.mNetworkScanHelper != null) {
            this.mRequestIdManualNetworkScan = getNewRequestId();
            this.mWaitingForNumberOfScanResults = 2L;
            NetworkScanHelper networkScanHelper = this.mNetworkScanHelper;
            if (!this.mIsAdvancedScanSupported) {
                i = 2;
            }
            networkScanHelper.startNetworkScan(i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopNetworkQuery() {
        setProgressBarVisible(false);
        NetworkScanHelper networkScanHelper = this.mNetworkScanHelper;
        if (networkScanHelper != null) {
            this.mWaitingForNumberOfScanResults = 0L;
            networkScanHelper.stopNetworkQuery();
        }
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        Log.d("NetworkSelectSettings", "onDestroy()");
        stopNetworkQuery();
        this.mNetworkScanExecutor.shutdown();
        super.onDestroy();
    }
}
