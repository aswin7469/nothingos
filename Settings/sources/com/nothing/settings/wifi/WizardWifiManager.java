package com.nothing.settings.wifi;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SimpleClock;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.settings.R$string;
import com.android.settings.core.SubSettingLauncher;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.wifi.AddNetworkFragment;
import com.android.settings.wifi.WifiConnectListener;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wifitrackerlib.WifiPickerTracker;
import java.time.ZoneOffset;
import java.util.List;

public class WizardWifiManager implements LifecycleOwner, WifiPickerTracker.WifiPickerTrackerCallback {
    /* access modifiers changed from: private */
    public WizardWifiManagerCallBack mCallBack;
    private boolean mCanToNextPageIfConnected;
    private ConnectivityManager mConnectivityManager;
    /* access modifiers changed from: private */
    public final FragmentActivity mContext;
    private WifiEntry mCurrentConnectedWifiEntry;
    private final NothingFragment mFragment;
    private final Runnable mHideProgressBarRunnable = new Runnable() {
        public void run() {
            WizardWifiManager.this.mCallBack.onProgressBarVisible(false);
        }
    };
    private boolean mIsOnPause;
    private boolean mIsWifiEntryListStale = true;
    private final Lifecycle mLifecycle = new Lifecycle(this);
    private final LifecycleRegistry mLifecycleRegistry = new LifecycleRegistry(this);
    private Handler mMainHandler;
    private ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        public void onAvailable(Network network) {
            super.onAvailable(network);
            if (WizardWifiManager.this.mToNextPageFromReceiver) {
                WizardWifiManager.this.mToNextPageFromReceiver = false;
                WizardWifiManager.this.onConnected();
            }
        }
    };
    private WifiManager.ActionListener mSaveListener = new WifiManager.ActionListener() {
        public void onSuccess() {
            Log.i("WizardWifiManager", "Save wifi success");
        }

        public void onFailure(int i) {
            Toast.makeText(WizardWifiManager.this.mContext, R$string.wifi_failed_save_message, 0).show();
        }
    };
    /* access modifiers changed from: private */
    public boolean mToNextPageFromReceiver;
    private final Runnable mUpdateWifiEntryPreferencesRunnable = new Runnable() {
        public void run() {
            WizardWifiManager.this.updateWifiEntryList();
        }
    };
    /* access modifiers changed from: private */
    public final WifiConnectListener mWifiConnectListener;
    private final WifiManager mWifiManager;
    private WifiPickerTracker mWifiPickerTracker;
    private Handler mWorkerHandler;
    private HandlerThread mWorkerThread;

    public interface WizardWifiManagerCallBack {
        void onConnected();

        void onProgressBarVisible(boolean z);

        void updateWifiEntryList(WifiEntry wifiEntry, List<WifiEntry> list);
    }

    public void onNumSavedNetworksChanged() {
    }

    public void onNumSavedSubscriptionsChanged() {
    }

    public WizardWifiManager(FragmentActivity fragmentActivity) {
        this.mContext = fragmentActivity;
        WifiManager wifiManager = (WifiManager) fragmentActivity.getApplicationContext().getSystemService("wifi");
        this.mWifiManager = wifiManager;
        wifiManager.setWifiEnabled(true);
        C20585 r0 = new WifiConnectListener(fragmentActivity) {
            public void onSuccess() {
                super.onSuccess();
                WizardWifiManager.this.onConnected();
            }

            public void onFailure(int i) {
                super.onFailure(i);
                Toast.makeText(WizardWifiManager.this.mContext, R$string.wifi_failed_connect_message, 0).show();
            }
        };
        this.mWifiConnectListener = r0;
        NothingFragment nothingFragment = new NothingFragment(this, r0);
        this.mFragment = nothingFragment;
        FragmentTransaction beginTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        beginTransaction.add((Fragment) nothingFragment, "");
        beginTransaction.commit();
        initializeWifiPickerTracker();
    }

    /* JADX WARNING: type inference failed for: r6v0, types: [java.time.Clock, com.nothing.settings.wifi.WizardWifiManager$6] */
    private void initializeWifiPickerTracker() {
        HandlerThread handlerThread = new HandlerThread("WizardWifiManager{" + Integer.toHexString(System.identityHashCode(this)) + "}", 10);
        this.mWorkerThread = handlerThread;
        handlerThread.start();
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mWorkerHandler = this.mWorkerThread.getThreadHandler();
        this.mWifiPickerTracker = FeatureFactory.getFactory(this.mContext).getWifiTrackerLibProvider().createWifiPickerTracker(this.mLifecycle, this.mContext, this.mMainHandler, this.mWorkerHandler, new SimpleClock(ZoneOffset.UTC) {
            public long millis() {
                return SystemClock.elapsedRealtime();
            }
        }, 15000, 10000, this);
    }

    private void connectDirect(WifiEntry wifiEntry) {
        this.mToNextPageFromReceiver = true;
        wifiEntry.connect(new WizardWifiEntryConnectCallback(this.mContext, wifiEntry));
    }

    public void startConnect(WifiEntry wifiEntry) {
        Log.i("WizardWifiManager", "Click wifi entry : " + wifiEntry);
        if (wifiEntry.getConnectedState() == 2) {
            Log.i("WizardWifiManager", "Connected");
            onConnected();
        } else if (wifiEntry.canSignIn()) {
            Log.i("WizardWifiManager", "Need to sing in or change");
            this.mCanToNextPageIfConnected = true;
            wifiEntry.signIn((WifiEntry.SignInCallback) null);
        } else if (wifiEntry.shouldEditBeforeConnect() || wifiEntry.getWifiConfiguration() == null) {
            Log.i("WizardWifiManager", "Need to edit");
            this.mCanToNextPageIfConnected = true;
            Intent putExtra = new Intent("com.android.settings.WIFI_DIALOG").putExtra("key_chosen_wifientry_key", wifiEntry.getKey());
            putExtra.addFlags(268435456);
            this.mContext.startActivityForResult(putExtra, 1);
        } else {
            Log.i("WizardWifiManager", "Connect immediate");
            this.mCanToNextPageIfConnected = true;
            connectDirect(wifiEntry);
        }
    }

    public void onWifiEntriesChanged() {
        Log.i("WizardWifiManager", "WizardWifiManger onWifiEntriesChanged");
        updateWifiEntryList();
    }

    public void onWifiStateChanged() {
        int wifiState = this.mWifiPickerTracker.getWifiState();
        Log.i("WizardWifiManager", "WizardWifiManger onWifiStateChanged：" + wifiState);
        if (this.mIsWifiEntryListStale) {
            Log.i("WizardWifiManager", "Is wifi entry list stale");
            this.mIsWifiEntryListStale = false;
            updateWifiEntryList();
        } else {
            updateWifiEntryPreferencesDelayed();
        }
        if (wifiState == 3) {
            updateWifiEntryList();
        }
    }

    public void updateWifiEntryList() {
        WifiEntry connectedWifiEntry = this.mWifiPickerTracker.getConnectedWifiEntry();
        List<WifiEntry> wifiEntries = this.mWifiPickerTracker.getWifiEntries();
        this.mMainHandler.postDelayed(this.mHideProgressBarRunnable, 1700);
        this.mCallBack.updateWifiEntryList(connectedWifiEntry, wifiEntries);
        if (this.mCurrentConnectedWifiEntry != connectedWifiEntry && this.mCanToNextPageIfConnected && !this.mIsOnPause && connectedWifiEntry != null && connectedWifiEntry.getConnectedState() == 2) {
            this.mCanToNextPageIfConnected = false;
            this.mCurrentConnectedWifiEntry = connectedWifiEntry;
            onConnected();
        }
    }

    private void updateWifiEntryPreferencesDelayed() {
        this.mCallBack.onProgressBarVisible(true);
        this.mMainHandler.postDelayed(this.mUpdateWifiEntryPreferencesRunnable, 300);
    }

    public void onCreate(Bundle bundle) {
        this.mLifecycle.onAttach(this.mContext);
        this.mLifecycle.onCreate(bundle);
        this.mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
    }

    public void onStart() {
        this.mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START);
        this.mConnectivityManager.registerNetworkCallback(new NetworkRequest.Builder().addTransportType(1).build(), this.mNetworkCallback);
    }

    public void onResume() {
        this.mIsOnPause = false;
        this.mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    public void onPause() {
        this.mIsOnPause = true;
        this.mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
    }

    public void onStop() {
        this.mLifecycle.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
    }

    public void onConnected() {
        Log.i("WizardWifiManager", "Go to next page");
        this.mMainHandler.postDelayed(new Runnable() {
            public final void run() {
                WizardWifiManager.this.mCallBack.onConnected();
            }
        }, 1000);
    }

    public LifecycleRegistry getLifecycle() {
        return this.mLifecycleRegistry;
    }

    public void connectForResult(WifiConfiguration wifiConfiguration, WifiConnectListener wifiConnectListener) {
        this.mToNextPageFromReceiver = true;
        this.mWifiManager.connect(wifiConfiguration, wifiConnectListener);
    }

    public void save(WifiConfiguration wifiConfiguration) {
        this.mToNextPageFromReceiver = true;
        this.mWifiManager.save(wifiConfiguration, this.mSaveListener);
    }

    public void launchAddNetworkFragment() {
        this.mCanToNextPageIfConnected = true;
        new SubSettingLauncher(this.mContext).setTitleRes(R$string.wifi_add_network).setDestination(AddNetworkFragment.class.getName()).setSourceMetricsCategory(0).setResultListener(this.mFragment, 2).launch();
    }

    public void setCallBack(WizardWifiManagerCallBack wizardWifiManagerCallBack) {
        this.mCallBack = wizardWifiManagerCallBack;
    }

    public static class NothingFragment extends Fragment {
        private WifiConnectListener mWifiConnectListener;
        private WizardWifiManager mWizardWifiManager;

        public NothingFragment() {
            Log.e("WizardWifiManager", "Something error!!!");
        }

        public NothingFragment(WizardWifiManager wizardWifiManager, WifiConnectListener wifiConnectListener) {
            this.mWizardWifiManager = wizardWifiManager;
            this.mWifiConnectListener = wifiConnectListener;
        }

        public void onActivityResult(int i, int i2, Intent intent) {
            WifiConfiguration wifiConfiguration;
            WifiConfiguration wifiConfiguration2;
            super.onActivityResult(i, i2, intent);
            if (i == 3) {
                if (i2 == -1 && (wifiConfiguration2 = (WifiConfiguration) intent.getParcelableExtra("network_config_key")) != null && this.mWizardWifiManager != null) {
                    Log.i("WizardWifiManager", "WizardWifiManager fragment onActivityResult connect wifi");
                    this.mWizardWifiManager.connectForResult(wifiConfiguration2, this.mWifiConnectListener);
                }
            } else if (i == 2 && i2 == -1 && (wifiConfiguration = (WifiConfiguration) intent.getParcelableExtra("network_config_key")) != null && this.mWizardWifiManager != null) {
                Log.i("WizardWifiManager", "WizardWifiManager fragment onActivityResult save wifi");
                this.mWizardWifiManager.save(wifiConfiguration);
            }
        }
    }

    public class WizardWifiEntryConnectCallback implements WifiEntry.ConnectCallback {
        private final WifiEntry mConnectWifiEntry;
        private Context mContext;

        public WizardWifiEntryConnectCallback(Context context, WifiEntry wifiEntry) {
            this.mContext = context;
            this.mConnectWifiEntry = wifiEntry;
        }

        public void onConnectResult(int i) {
            Log.i("WizardWifiManager", "WizardWifiEntryConnectCallback onConnectResult : " + i);
            if (i == 0) {
                WizardWifiManager.this.onConnected();
            } else if (i == 1) {
                WizardWifiManager.this.connectForResult(this.mConnectWifiEntry.getWifiConfiguration(), WizardWifiManager.this.mWifiConnectListener);
            } else if (i == 2) {
                Toast.makeText(this.mContext, R$string.wifi_failed_connect_message, 0).show();
            }
        }
    }
}
