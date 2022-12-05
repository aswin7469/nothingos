package com.android.settings.development;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.Log;
import androidx.constraintlayout.widget.R$styleable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R;
import com.android.settings.network.SubscriptionUtil;
import com.android.settings.network.SubscriptionsChangeListener;
import com.android.settings.network.telephony.MobileNetworkUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.qti.extphone.Client;
import com.qti.extphone.ExtPhoneCallbackBase;
import com.qti.extphone.ExtTelephonyManager;
import com.qti.extphone.NrConfig;
import com.qti.extphone.ServiceCallback;
import com.qti.extphone.Status;
import com.qti.extphone.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class Prefer5GNetworkListController extends AbstractPreferenceController implements LifecycleObserver, Preference.OnPreferenceChangeListener, SubscriptionsChangeListener.SubscriptionsChangeListenerClient {
    @VisibleForTesting
    static final String KEY_ADD_MORE = "prefer_5g_add_more";
    private SubscriptionsChangeListener mChangeListener;
    private Client mClient;
    private Context mContext;
    private ExtTelephonyManager mExtTelephonyManager;
    private String mPackageName;
    private PreferenceScreen mPreferenceScreen;
    private boolean mRetry;
    private boolean mServiceConnected;
    private SharedPreferences mSharedPreferences;
    private SubscriptionManager mSubscriptionManager;
    private TelephonyManager mTelephonyManager;
    private int userPrefNrConfig;
    private final List<ListPreference> mPreferenceList = new ArrayList();
    private int mSubId = -1;
    private int mPhoneId = -1;
    private ExtPhoneCallbackBase mCallback = new ExtPhoneCallbackBase() { // from class: com.android.settings.development.Prefer5GNetworkListController.1
        public void onSetNrConfig(int i, Token token, Status status) throws RemoteException {
            Log.d("Prefer5gNetworkListCtlr", "onSetNrConfig: slotId = " + i + " token = " + token + " status = " + status);
            if (status.get() == 1) {
                Prefer5GNetworkListController prefer5GNetworkListController = Prefer5GNetworkListController.this;
                prefer5GNetworkListController.updateSharedPreference(i, prefer5GNetworkListController.userPrefNrConfig);
            }
            Prefer5GNetworkListController.this.mMainThreadHandler.sendMessage(Prefer5GNetworkListController.this.mMainThreadHandler.obtainMessage(R$styleable.Constraint_layout_goneMarginRight, i, -1));
        }

        public void onNrConfigStatus(int i, Token token, Status status, NrConfig nrConfig) throws RemoteException {
            Log.d("Prefer5gNetworkListCtlr", "onNrConfigStatus: slotId = " + i + " token = " + token + " status = " + status + " nrConfig = " + nrConfig);
            if (status.get() == 1) {
                Prefer5GNetworkListController.this.updateSharedPreference(i, nrConfig.get());
                Prefer5GNetworkListController.this.mMainThreadHandler.sendMessage(Prefer5GNetworkListController.this.mMainThreadHandler.obtainMessage(R$styleable.Constraint_layout_goneMarginStart, i, -1));
            }
        }
    };
    private Handler mMainThreadHandler = new Handler() { // from class: com.android.settings.development.Prefer5GNetworkListController.2
        @Override // android.os.Handler
        public void handleMessage(Message message) {
            Log.d("Prefer5gNetworkListCtlr", "handleMessage msg.what = " + message.what);
            int i = message.what;
            if (i == 101 || i == 102) {
                int i2 = message.arg1;
                if (Prefer5GNetworkListController.this.mPreferenceScreen == null) {
                    return;
                }
                Prefer5GNetworkListController.this.updatePreferenceForSlot(i2);
            }
        }
    };
    private ServiceCallback mServiceCallback = new ServiceCallback() { // from class: com.android.settings.development.Prefer5GNetworkListController.3
        public void onConnected() {
            Log.d("Prefer5gNetworkListCtlr", "ExtTelephony Service connected");
            Prefer5GNetworkListController.this.mServiceConnected = true;
            Prefer5GNetworkListController prefer5GNetworkListController = Prefer5GNetworkListController.this;
            prefer5GNetworkListController.mClient = prefer5GNetworkListController.mExtTelephonyManager.registerCallback(Prefer5GNetworkListController.this.mPackageName, Prefer5GNetworkListController.this.mCallback);
            Log.d("Prefer5gNetworkListCtlr", "Client = " + Prefer5GNetworkListController.this.mClient);
            if (Prefer5GNetworkListController.this.mRetry) {
                for (int i = 0; i < Prefer5GNetworkListController.this.mTelephonyManager.getPhoneCount(); i++) {
                    Prefer5GNetworkListController.this.mRetry = false;
                    Log.d("Prefer5gNetworkListCtlr", "queryNrConfig: " + Prefer5GNetworkListController.this.mExtTelephonyManager.queryNrConfig(i, Prefer5GNetworkListController.this.mClient));
                }
            }
        }

        public void onDisconnected() {
            Log.d("Prefer5gNetworkListCtlr", "ExtTelephony Service disconnected...");
            if (Prefer5GNetworkListController.this.mServiceConnected) {
                Prefer5GNetworkListController.this.mServiceConnected = false;
                Prefer5GNetworkListController.this.mClient = null;
            }
        }
    };
    private Map<Integer, ListPreference> mPreferences = new ArrayMap();

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public String getPreferenceKey() {
        return null;
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public boolean isAvailable() {
        return true;
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onAirplaneModeChanged(boolean z) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSharedPreference(int i, int i2) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        if (sharedPreferences != null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putInt("nr_mode_" + i, i2).apply();
        }
    }

    public Prefer5GNetworkListController(Context context, Lifecycle lifecycle) {
        super(context);
        Log.i("Prefer5gNetworkListCtlr", "constructor");
        this.mContext = context;
        this.mPackageName = context.getPackageName();
        Context context2 = this.mContext;
        this.mSharedPreferences = context2.getSharedPreferences(context2.getPackageName(), 0);
        this.mSubscriptionManager = (SubscriptionManager) context.getSystemService(SubscriptionManager.class);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
        this.mChangeListener = new SubscriptionsChangeListener(context, this);
        if (this.mExtTelephonyManager == null) {
            this.mExtTelephonyManager = ExtTelephonyManager.getInstance(this.mContext.getApplicationContext());
        }
        Log.d("Prefer5gNetworkListCtlr", "Connect to ExtTelephony bound service...");
        this.mExtTelephonyManager.connectService(this.mServiceCallback);
        lifecycle.addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        Log.i("Prefer5gNetworkListCtlr", "onResume");
        this.mChangeListener.start();
        update();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mChangeListener.stop();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy() {
        Log.d("Prefer5gNetworkListCtlr", "onDestroy");
        this.mExtTelephonyManager.unRegisterCallback(this.mCallback);
        this.mExtTelephonyManager.disconnectService();
    }

    @Override // com.android.settingslib.core.AbstractPreferenceController
    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        Log.i("Prefer5gNetworkListCtlr", "displayPreference");
        this.mPreferenceScreen = preferenceScreen;
        preferenceScreen.findPreference(KEY_ADD_MORE).setVisible(MobileNetworkUtils.showEuiccSettings(this.mContext));
    }

    private void update() {
        Client client;
        if (this.mPreferenceScreen == null) {
            return;
        }
        Map<Integer, ListPreference> map = this.mPreferences;
        this.mPreferences = new ArrayMap();
        for (int i = 0; i < this.mTelephonyManager.getPhoneCount(); i++) {
            SubscriptionInfo activeSubscriptionInfoForSimSlotIndex = this.mSubscriptionManager.getActiveSubscriptionInfoForSimSlotIndex(i);
            if (activeSubscriptionInfoForSimSlotIndex != null) {
                int subscriptionId = activeSubscriptionInfoForSimSlotIndex.getSubscriptionId();
                ListPreference remove = map.remove(Integer.valueOf(subscriptionId));
                if (remove == null) {
                    remove = new ListPreference(this.mPreferenceScreen.getContext());
                    this.mPreferenceScreen.addPreference(remove);
                }
                remove.setTitle(activeSubscriptionInfoForSimSlotIndex.getDisplayName());
                remove.setOrder(i);
                remove.setDialogTitle("Select NR Mode For Slot " + i);
                remove.setOnPreferenceChangeListener(this);
                remove.setEntries(R.array.preferred_5g_network_mode_choices);
                remove.setEntryValues(R.array.preferred_5g_network_mode_values);
                if (this.mServiceConnected && (client = this.mClient) != null) {
                    this.mRetry = false;
                    Log.d("Prefer5gNetworkListCtlr", "queryNrConfig: " + this.mExtTelephonyManager.queryNrConfig(i, client));
                } else {
                    this.mRetry = true;
                }
                this.mPreferences.put(Integer.valueOf(subscriptionId), remove);
                this.mPreferenceList.add(remove);
            } else {
                Log.d("Prefer5gNetworkListCtlr", "sub info is null, add null preference for slot: " + i);
                this.mPreferenceList.add(i, null);
            }
        }
        for (ListPreference listPreference : map.values()) {
            this.mPreferenceScreen.removePreference(listPreference);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePreferenceForSlot(int i) {
        SharedPreferences sharedPreferences = this.mSharedPreferences;
        int i2 = sharedPreferences.getInt("nr_mode_" + i, 0);
        String string = this.mContext.getString(getSummaryResId(i2));
        Log.d("Prefer5gNetworkListCtlr", "updatePreferenceForSlot for " + i + " ,nr mode is " + i2 + " , set summary to " + string);
        ListPreference listPreference = this.mPreferenceList.get(i);
        if (listPreference != null) {
            listPreference.setSummary(string);
            listPreference.setValue(Integer.toString(i2));
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        int parseInt = Integer.parseInt((String) obj);
        int indexOf = this.mPreferenceList.indexOf(preference);
        Log.i("Prefer5gNetworkListCtlr", "onPreferenceChange for slot: " + indexOf + ", setNrConfig: " + parseInt);
        this.userPrefNrConfig = parseInt;
        if (this.mServiceConnected && this.mClient != null) {
            Token nrConfig = this.mExtTelephonyManager.setNrConfig(indexOf, new NrConfig(parseInt), this.mClient);
            Log.d("Prefer5gNetworkListCtlr", "setNrConfig: " + nrConfig);
        }
        ((ListPreference) preference).setSummary(this.mContext.getString(getSummaryResId(parseInt)));
        return true;
    }

    private int getSummaryResId(int i) {
        if (i == 0) {
            return R.string.nr_nsa_sa;
        }
        if (i == 1) {
            return R.string.nr_nsa;
        }
        if (i == 2) {
            return R.string.nr_sa;
        }
        return R.string.nr_nsa_sa;
    }

    @Override // com.android.settings.network.SubscriptionsChangeListener.SubscriptionsChangeListenerClient
    public void onSubscriptionsChanged() {
        boolean z = false;
        for (SubscriptionInfo subscriptionInfo : SubscriptionUtil.getAvailableSubscriptions(this.mContext)) {
            if (subscriptionInfo == null) {
                return;
            }
            if (this.mPreferences.get(Integer.valueOf(subscriptionInfo.getSubscriptionId())) == null) {
                z = true;
            }
        }
        if (z) {
            Log.d("Prefer5gNetworkListCtlr", "sub changed, will update preference");
            update();
        }
    }
}
