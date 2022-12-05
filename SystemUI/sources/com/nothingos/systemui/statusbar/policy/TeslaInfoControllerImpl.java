package com.nothingos.systemui.statusbar.policy;

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
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.nothingos.systemui.statusbar.policy.TeslaInfoController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class TeslaInfoControllerImpl implements TeslaInfoController {
    private static final Uri URI_TESLA_ACTIVE = Settings.System.getUriFor("tesla_active");
    private String mBatteryRange;
    private boolean mConnected;
    private ContentResolver mContentResolver;
    private Context mContext;
    private Handler mHandler;
    private QSTile.Icon mProfile = QSTileImpl.ResourceIcon.get(R$drawable.ic_qs_tesla_profile);
    private boolean mShowTeslaInfo;
    private final ContentObserver mTeslaActiveObserver;
    private final BroadcastReceiver mTeslaInfoReceiver;
    private String mUserName;

    public TeslaInfoControllerImpl(Context context, BroadcastDispatcher broadcastDispatcher, Looper looper, DumpManager dumpManager) {
        ContentObserver contentObserver = new ContentObserver(new Handler()) { // from class: com.nothingos.systemui.statusbar.policy.TeslaInfoControllerImpl.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z) {
                boolean z2 = Settings.System.getInt(TeslaInfoControllerImpl.this.mContentResolver, "tesla_active", 1) == 0;
                Log.d("TeslaInfoController", "onChange: mShowTeslaInfo " + TeslaInfoControllerImpl.this.mShowTeslaInfo + " showTeslaInfo " + z2);
                if (z2 != TeslaInfoControllerImpl.this.mShowTeslaInfo) {
                    TeslaInfoControllerImpl.this.mShowTeslaInfo = z2;
                    TeslaInfoControllerImpl.this.mHandler.sendEmptyMessage(1);
                }
            }
        };
        this.mTeslaActiveObserver = contentObserver;
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() { // from class: com.nothingos.systemui.statusbar.policy.TeslaInfoControllerImpl.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                Log.d("TeslaInfoController", "onReceive: action " + action);
                if ("com.nothing.experimental.TESLA_CONNECT".equals(action)) {
                    Bundle bundle = (Bundle) intent.getExtra("extra");
                    if (bundle != null) {
                        TeslaInfoControllerImpl.this.mUserName = bundle.getString("user_name");
                        TeslaInfoControllerImpl.this.mBatteryRange = bundle.getString("battery_range");
                        TeslaInfoControllerImpl.this.mConnected = bundle.getBoolean("connected");
                        TeslaInfoControllerImpl.this.mHandler.sendEmptyMessage(2);
                        Log.d("TeslaInfoController", "onReceive: mUserName " + TeslaInfoControllerImpl.this.mUserName + " mBatteryRange " + TeslaInfoControllerImpl.this.mBatteryRange + " mConnected " + TeslaInfoControllerImpl.this.mConnected);
                        return;
                    }
                    Log.d("TeslaInfoController", "onReceive: extra bundle is null.");
                }
            }
        };
        this.mTeslaInfoReceiver = broadcastReceiver;
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mHandler = new H(looper);
        this.mShowTeslaInfo = Settings.System.getInt(this.mContentResolver, "tesla_active", 1) != 0 ? false : true;
        this.mUserName = this.mContext.getString(R$string.qs_tesla_def_label);
        this.mBatteryRange = this.mContext.getString(R$string.qs_tesla_def_second_label);
        broadcastDispatcher.registerReceiver(broadcastReceiver, new IntentFilter("com.nothing.experimental.TESLA_CONNECT"));
        this.mContentResolver.registerContentObserver(URI_TESLA_ACTIVE, false, contentObserver);
        dumpManager.registerDumpable("TeslaInfoController", this);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void addCallback(TeslaInfoController.Callback callback) {
        this.mHandler.obtainMessage(3, callback).sendToTarget();
        this.mHandler.sendEmptyMessage(1);
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public void removeCallback(TeslaInfoController.Callback callback) {
        this.mHandler.obtainMessage(4, callback).sendToTarget();
    }

    @Override // com.nothingos.systemui.statusbar.policy.TeslaInfoController
    public boolean shouldShowTeslaInfo() {
        return this.mShowTeslaInfo;
    }

    @Override // com.nothingos.systemui.statusbar.policy.TeslaInfoController
    public QSTile.Icon getProfile() {
        return this.mProfile;
    }

    @Override // com.nothingos.systemui.statusbar.policy.TeslaInfoController
    public String getUserName() {
        return this.mUserName;
    }

    @Override // com.nothingos.systemui.statusbar.policy.TeslaInfoController
    public String getBatteryRange() {
        return this.mBatteryRange;
    }

    @Override // com.nothingos.systemui.statusbar.policy.TeslaInfoController
    public boolean isConnected() {
        return this.mConnected;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
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

    /* loaded from: classes2.dex */
    private final class H extends Handler {
        private final ArrayList<TeslaInfoController.Callback> mCallbacks = new ArrayList<>();

        public H(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                fireActiveStateChange();
            } else if (i == 2) {
                fireInfoChange();
            } else if (i == 3) {
                this.mCallbacks.add((TeslaInfoController.Callback) message.obj);
            } else if (i != 4) {
            } else {
                this.mCallbacks.remove((TeslaInfoController.Callback) message.obj);
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
