package com.nothing.systemui.p024qs.tiles.settings.tesla;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import com.android.systemui.C1893R;
import com.google.gson.Gson;
import com.nothing.systemui.util.NTLogUtil;
import com.nothing.tesla.service.CmdObject;
import com.nothing.tesla.service.CmdObjectList;
import com.nothing.tesla.service.ICallback;
import com.nothing.tesla.service.ITeslaControlService;
import java.util.HashMap;
import java.util.List;

/* renamed from: com.nothing.systemui.qs.tiles.settings.tesla.TeslaConnectPanelPlugin */
public class TeslaConnectPanelPlugin {
    public static final HashMap<Integer, Integer> CAR_IMAGE_MAPPING;
    public static final HashMap<Integer, Integer> CMD_ICON_MAPPING;
    public static final HashMap<Integer, Integer> CMD_ICON_ON;
    private static final int CMD_ON = 2;
    private static final int CMD_TYPE_TEMP = 2;
    private static final int MSG_ADD_ITEM = 0;
    private static final String NOTHING_EXPERIMENTAL = "com.nothing.experimental";
    public static String TAG = "TeslaPlugin";
    private static final String TESLA_SERVICE = "com.nothing.experimental.TESLA_SERVICE";
    ICallback mCallback = new ICallback.Stub() {
        public void callback(int i, int i2, String str) throws RemoteException {
            NTLogUtil.m1680d(TeslaConnectPanelPlugin.TAG, "Callback cmd: " + i + ", result: " + i2 + ", newValue: " + str + ", (lists != null): " + (TeslaConnectPanelPlugin.this.mLists != null));
            if (TeslaConnectPanelPlugin.this.mLists != null) {
                List<CmdObject> cmdObjects = TeslaConnectPanelPlugin.this.mLists.getCmdObjects();
                for (CmdObject next : cmdObjects) {
                    if (next.getCmd() == i && TeslaConnectPanelPlugin.this.needSwitchStatus(i)) {
                        next.setStatus(i2 == 2);
                    }
                    if (TeslaConnectPanelPlugin.this.needUpdateTemp(i) && !TextUtils.isEmpty(str) && next.getType() == 2) {
                        next.setTitle(str);
                    }
                    if (TeslaConnectPanelPlugin.this.needUpdateDoorTitle(i) && next.getCmd() == i && !TextUtils.isEmpty(str) && next.getType() == 0) {
                        next.setTitle(str);
                    }
                }
                TeslaConnectPanelPlugin.this.mLists.setCmdObjects(cmdObjects);
                if (TeslaConnectPanelPlugin.this.mLoadCallback != null) {
                    TeslaConnectPanelPlugin.this.mLoadCallback.onStatusChange(TeslaConnectPanelPlugin.this.mLists);
                }
            }
        }

        public void dataSyncCallback(String str) throws RemoteException {
            if (str != null) {
                System.currentTimeMillis();
                TeslaConnectPanelPlugin teslaConnectPanelPlugin = TeslaConnectPanelPlugin.this;
                CmdObjectList unused = teslaConnectPanelPlugin.mLists = teslaConnectPanelPlugin.parseCmd(str);
                if (TeslaConnectPanelPlugin.this.mLoadCallback != null) {
                    TeslaConnectPanelPlugin.this.mLoadCallback.onLoaded();
                }
            }
        }
    };
    ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TeslaConnectPanelPlugin.this.mService = ITeslaControlService.Stub.asInterface(iBinder);
            try {
                NTLogUtil.m1680d(TeslaConnectPanelPlugin.TAG, "onServiceConnected");
                TeslaConnectPanelPlugin.this.mService.registerCallback(TeslaConnectPanelPlugin.this.mCallback);
                TeslaConnectPanelPlugin.this.mService.syncStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            if (TeslaConnectPanelPlugin.this.mService != null) {
                try {
                    NTLogUtil.m1680d(TeslaConnectPanelPlugin.TAG, "onServiceDisconnected");
                    TeslaConnectPanelPlugin.this.mService.unregisterCallback(TeslaConnectPanelPlugin.this.mCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            TeslaConnectPanelPlugin.this.mService = null;
        }
    };
    private Context mContext;
    private boolean mIsBinded;
    /* access modifiers changed from: private */
    public CmdObjectList mLists = null;
    /* access modifiers changed from: private */
    public LoadCallback mLoadCallback = null;
    ITeslaControlService mService;

    /* renamed from: com.nothing.systemui.qs.tiles.settings.tesla.TeslaConnectPanelPlugin$LoadCallback */
    public interface LoadCallback {
        void onLoaded();

        void onStatusChange(CmdObjectList cmdObjectList);
    }

    /* access modifiers changed from: private */
    public boolean needSwitchStatus(int i) {
        return i == 100 || i == 200 || i == 301 || i == 500;
    }

    /* access modifiers changed from: private */
    public boolean needUpdateDoorTitle(int i) {
        return i == 100;
    }

    /* access modifiers changed from: private */
    public boolean needUpdateTemp(int i) {
        return i == 8 || i == 9;
    }

    static {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        CMD_ICON_MAPPING = hashMap;
        HashMap<Integer, Integer> hashMap2 = new HashMap<>();
        CMD_ICON_ON = hashMap2;
        HashMap<Integer, Integer> hashMap3 = new HashMap<>();
        CAR_IMAGE_MAPPING = hashMap3;
        hashMap.put(100, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_lock));
        hashMap.put(200, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_ac));
        hashMap.put(300, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_front_trunk));
        hashMap.put(301, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_rear_trunk));
        hashMap.put(400, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_honk));
        hashMap.put(401, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_flash));
        hashMap.put(500, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_vent_off));
        hashMap2.put(100, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_unlocked));
        hashMap2.put(200, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_ac_on));
        hashMap2.put(500, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_vent));
        hashMap2.put(301, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_rear_trunk_on));
        hashMap3.put(0, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_model3));
        hashMap3.put(1, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_model_y));
        hashMap3.put(2, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_models));
        hashMap3.put(3, Integer.valueOf((int) C1893R.C1895drawable.ic_tesla_modelx));
    }

    public TeslaConnectPanelPlugin(Context context) {
        this.mContext = context;
    }

    /* access modifiers changed from: private */
    public CmdObjectList parseCmd(String str) {
        Gson gson = new Gson();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (CmdObjectList) gson.fromJson(str, CmdObjectList.class);
    }

    public void onCreate(LoadCallback loadCallback) {
        Intent intent = new Intent(TESLA_SERVICE);
        intent.setPackage(NOTHING_EXPERIMENTAL);
        this.mIsBinded = this.mContext.bindService(intent, this.mConnection, 1);
        this.mLoadCallback = loadCallback;
    }

    public void onDestory() {
        if (this.mIsBinded) {
            this.mContext.unbindService(this.mConnection);
            this.mIsBinded = false;
        }
        this.mLoadCallback = null;
    }

    public CmdObjectList getCmdList() {
        return this.mLists;
    }

    public void sendCmd(int i, String str) {
        ITeslaControlService iTeslaControlService = this.mService;
        if (iTeslaControlService != null) {
            try {
                iTeslaControlService.sendCmd(i, str);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
