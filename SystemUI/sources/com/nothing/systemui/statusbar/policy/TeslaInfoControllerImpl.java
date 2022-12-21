package com.nothing.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.p011qs.QSTile;
import com.nothing.systemui.statusbar.policy.TeslaInfoController;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import javax.inject.Inject;

@SysUISingleton
public class TeslaInfoControllerImpl implements TeslaInfoController {
    private static final String ACTION_TESLA_CONNECT = "com.nothing.experimental.TESLA_CONNECT";
    private static final String ACTION_TESLA_PERMISSION = "com.nothing.experimental.TESLA_BROADCAST";
    private static final String KEY_BATTERY_RANGE = "battery_range";
    private static final String KEY_CONNECTED = "connected";
    private static final String KEY_EXTRA = "extra";
    private static final String KEY_USER_NAME = "user_name";
    private static final String SETTING_TESLA_ACTIVE = "tesla_active";
    private static final String TAG = "TeslaInfoController";
    private static final int TESLA_ACTIVE = 0;
    private static final int TESLA_INACTIVE = 1;
    private static final Uri URI_TESLA_ACTIVE = Settings.System.getUriFor("tesla_active");
    /* access modifiers changed from: private */
    public String mBatteryRange;
    /* access modifiers changed from: private */
    public boolean mConnected;
    /* access modifiers changed from: private */
    public ContentResolver mContentResolver;
    private Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private QSTile.Icon mProfile = QSTileImpl.ResourceIcon.get(C1893R.C1895drawable.ic_qs_tesla_profile);
    /* access modifiers changed from: private */
    public boolean mShowTeslaInfo;
    private final ContentObserver mTeslaActiveObserver;
    private final BroadcastReceiver mTeslaInfoReceiver;
    /* access modifiers changed from: private */
    public String mUserName;

    @Inject
    public TeslaInfoControllerImpl(Context context, BroadcastDispatcher broadcastDispatcher, @Main Looper looper, DumpManager dumpManager) {
        C42291 r6 = new ContentObserver(new Handler()) {
            public void onChange(boolean z) {
                boolean z2 = Settings.System.getInt(TeslaInfoControllerImpl.this.mContentResolver, "tesla_active", 1) == 0;
                Log.d(TeslaInfoControllerImpl.TAG, "onChange: mShowTeslaInfo " + TeslaInfoControllerImpl.this.mShowTeslaInfo + " showTeslaInfo " + z2);
                if (z2 != TeslaInfoControllerImpl.this.mShowTeslaInfo) {
                    boolean unused = TeslaInfoControllerImpl.this.mShowTeslaInfo = z2;
                    TeslaInfoControllerImpl.this.mHandler.sendEmptyMessage(1);
                }
            }
        };
        this.mTeslaActiveObserver = r6;
        C42302 r0 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TeslaInfoControllerImpl.TAG, "onReceive: action " + action);
                if (TeslaInfoControllerImpl.ACTION_TESLA_CONNECT.equals(action)) {
                    Bundle bundle = (Bundle) intent.getExtra(TeslaInfoControllerImpl.KEY_EXTRA);
                    if (bundle != null) {
                        String string = bundle.getString(TeslaInfoControllerImpl.KEY_USER_NAME);
                        String string2 = bundle.getString(TeslaInfoControllerImpl.KEY_BATTERY_RANGE);
                        if (string != null) {
                            String unused = TeslaInfoControllerImpl.this.mUserName = string;
                        }
                        if (string2 != null) {
                            String unused2 = TeslaInfoControllerImpl.this.mBatteryRange = string2;
                        }
                        boolean unused3 = TeslaInfoControllerImpl.this.mConnected = bundle.getBoolean("connected");
                        TeslaInfoControllerImpl.this.mHandler.sendEmptyMessage(2);
                        Log.d(TeslaInfoControllerImpl.TAG, "onReceive: userName " + string + " mUserName " + TeslaInfoControllerImpl.this.mUserName + " batteryRange " + string2 + " mBatteryRange " + TeslaInfoControllerImpl.this.mBatteryRange + " mConnected " + TeslaInfoControllerImpl.this.mConnected);
                        return;
                    }
                    Log.d(TeslaInfoControllerImpl.TAG, "onReceive: extra bundle is null.");
                }
            }
        };
        this.mTeslaInfoReceiver = r0;
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mHandler = new C4231H(looper);
        this.mShowTeslaInfo = Settings.System.getInt(this.mContentResolver, "tesla_active", 1) != 0 ? false : true;
        this.mUserName = this.mContext.getString(C1893R.string.qs_tesla_def_label);
        this.mBatteryRange = this.mContext.getString(C1893R.string.qs_tesla_def_second_label);
        context.registerReceiver(r0, new IntentFilter(ACTION_TESLA_CONNECT), ACTION_TESLA_PERMISSION, (Handler) null);
        this.mContentResolver.registerContentObserver(URI_TESLA_ACTIVE, false, r6);
        dumpManager.registerDumpable(TAG, this);
    }

    public void addCallback(TeslaInfoController.Callback callback) {
        this.mHandler.obtainMessage(3, callback).sendToTarget();
        this.mHandler.sendEmptyMessage(1);
    }

    public void removeCallback(TeslaInfoController.Callback callback) {
        this.mHandler.obtainMessage(4, callback).sendToTarget();
    }

    public boolean shouldShowTeslaInfo() {
        return this.mShowTeslaInfo;
    }

    public QSTile.Icon getProfile() {
        return this.mProfile;
    }

    public String getUserName() {
        return this.mUserName;
    }

    public String getBatteryRange() {
        return this.mBatteryRange;
    }

    public boolean isConnected() {
        return this.mConnected;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("TeslaInfoController state:");
        printWriter.print("  mShowTeslaInfo=");
        printWriter.println(this.mShowTeslaInfo);
        printWriter.print("  mConnected=");
        printWriter.println(this.mConnected);
        printWriter.print("  mUserName=");
        printWriter.println(this.mUserName);
        printWriter.print("  mBatteryRange=");
        printWriter.println(this.mBatteryRange);
    }

    /* renamed from: com.nothing.systemui.statusbar.policy.TeslaInfoControllerImpl$H */
    private final class C4231H extends Handler {
        private static final int MSG_ACTIVE_STATE_CHANGED = 1;
        private static final int MSG_ADD_CALLBACK = 3;
        private static final int MSG_INFO_CHANGED = 2;
        private static final int MSG_REMOVE_CALLBACK = 4;
        private final ArrayList<TeslaInfoController.Callback> mCallbacks = new ArrayList<>();

        public C4231H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                fireActiveStateChange();
            } else if (i == 2) {
                fireInfoChange();
            } else if (i == 3) {
                this.mCallbacks.add((TeslaInfoController.Callback) message.obj);
            } else if (i == 4) {
                this.mCallbacks.remove((Object) (TeslaInfoController.Callback) message.obj);
            }
        }

        private void fireActiveStateChange() {
            Iterator<TeslaInfoController.Callback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onActiveStateChanged();
            }
        }

        private void fireInfoChange() {
            Iterator<TeslaInfoController.Callback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onInfoChanged();
            }
        }
    }
}
