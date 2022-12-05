package com.nt.settings.panel;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.R;
import com.google.gson.Gson;
import com.nothing.tesla.service.CmdObject;
import com.nothing.tesla.service.CmdObjectList;
import com.nothing.tesla.service.ICallback;
import com.nothing.tesla.service.ITeslaControlService;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes2.dex */
public class TeslaConnectPanelPlugin {
    public static final HashMap<Integer, Integer> CAR_IMAGE_MAPPING;
    public static final HashMap<Integer, Integer> CMD_ICON_MAPPING;
    public static final HashMap<Integer, Integer> CMD_ICON_ON;
    public static String TAG = "TeslaTest";
    public static boolean isDebug = true;
    private Context mContext;
    ITeslaControlService mService;
    private boolean active = false;
    private CmdObjectList lists = null;
    private LoadCallback mLoadCallback = null;
    ICallback callback = new ICallback.Stub() { // from class: com.nt.settings.panel.TeslaConnectPanelPlugin.1
        @Override // com.nothing.tesla.service.ICallback
        public void callback(int i, int i2, String str) throws RemoteException {
            String str2 = TeslaConnectPanelPlugin.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(" callback cmd:");
            sb.append(i);
            sb.append(" result:");
            sb.append(i2);
            sb.append(" newValue:");
            sb.append(str);
            sb.append(" (lists != null)");
            sb.append(TeslaConnectPanelPlugin.this.lists != null);
            TeslaConnectPanelPlugin.logd(str2, sb.toString());
            if (TeslaConnectPanelPlugin.this.lists != null) {
                List<CmdObject> cmdObjects = TeslaConnectPanelPlugin.this.lists.getCmdObjects();
                for (CmdObject cmdObject : cmdObjects) {
                    if (cmdObject.getCmd() == i && TeslaConnectPanelPlugin.this.needSwitchStatus(i)) {
                        cmdObject.setStatus(i2 == 2);
                    }
                    if (TeslaConnectPanelPlugin.this.needUpdateTemp(i) && !TextUtils.isEmpty(str) && cmdObject.getType() == 2) {
                        cmdObject.setTitle(str);
                    }
                    if (TeslaConnectPanelPlugin.this.needUpdateDoorTitle(i) && cmdObject.getCmd() == i && !TextUtils.isEmpty(str) && cmdObject.getType() == 0) {
                        cmdObject.setTitle(str);
                    }
                }
                TeslaConnectPanelPlugin.this.lists.setCmdObjects(cmdObjects);
                if (TeslaConnectPanelPlugin.this.mLoadCallback == null) {
                    return;
                }
                TeslaConnectPanelPlugin.this.mLoadCallback.onStatusChange(TeslaConnectPanelPlugin.this.lists);
            }
        }

        @Override // com.nothing.tesla.service.ICallback
        public void dataSyncCallback(String str) throws RemoteException {
            if (str != null) {
                System.currentTimeMillis();
                TeslaConnectPanelPlugin teslaConnectPanelPlugin = TeslaConnectPanelPlugin.this;
                teslaConnectPanelPlugin.lists = teslaConnectPanelPlugin.parseCmd(str);
                if (TeslaConnectPanelPlugin.this.mLoadCallback == null) {
                    return;
                }
                TeslaConnectPanelPlugin.this.mLoadCallback.onLoaded();
            }
        }
    };
    ServiceConnection connection = new ServiceConnection() { // from class: com.nt.settings.panel.TeslaConnectPanelPlugin.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TeslaConnectPanelPlugin.this.mService = ITeslaControlService.Stub.asInterface(iBinder);
            try {
                TeslaConnectPanelPlugin teslaConnectPanelPlugin = TeslaConnectPanelPlugin.this;
                teslaConnectPanelPlugin.mService.registerCallback(teslaConnectPanelPlugin.callback);
                TeslaConnectPanelPlugin.this.mService.syncStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            TeslaConnectPanelPlugin teslaConnectPanelPlugin = TeslaConnectPanelPlugin.this;
            ITeslaControlService iTeslaControlService = teslaConnectPanelPlugin.mService;
            if (iTeslaControlService != null) {
                try {
                    iTeslaControlService.unregisterCallback(teslaConnectPanelPlugin.callback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            TeslaConnectPanelPlugin.this.mService = null;
        }
    };

    /* loaded from: classes2.dex */
    public interface LoadCallback {
        void onLoaded();

        void onStatusChange(CmdObjectList cmdObjectList);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needSwitchStatus(int i) {
        return i == 100 || i == 200 || i == 301 || i == 500;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needUpdateDoorTitle(int i) {
        return i == 100;
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        hashMap.put(100, Integer.valueOf(R.drawable.ic_tesla_lock));
        hashMap.put(200, Integer.valueOf(R.drawable.ic_tesla_ac));
        hashMap.put(300, Integer.valueOf(R.drawable.ic_tesla_front_trunk));
        hashMap.put(301, Integer.valueOf(R.drawable.ic_tesla_rear_trunk));
        hashMap.put(400, Integer.valueOf(R.drawable.ic_tesla_honk));
        hashMap.put(401, Integer.valueOf(R.drawable.ic_tesla_flash));
        hashMap.put(500, Integer.valueOf(R.drawable.ic_tesla_vent_off));
        hashMap2.put(100, Integer.valueOf(R.drawable.ic_tesla_unlocked));
        hashMap2.put(200, Integer.valueOf(R.drawable.ic_tesla_ac_on));
        hashMap2.put(500, Integer.valueOf(R.drawable.ic_tesla_vent));
        hashMap2.put(301, Integer.valueOf(R.drawable.ic_tesla_rear_trunk_on));
        hashMap3.put(0, Integer.valueOf(R.drawable.ic_tesla_model3));
        hashMap3.put(1, Integer.valueOf(R.drawable.ic_tesla_model_y));
        hashMap3.put(2, Integer.valueOf(R.drawable.ic_tesla_models));
        hashMap3.put(3, Integer.valueOf(R.drawable.ic_tesla_modelx));
    }

    public static void logd(String str, String str2) {
        if (isDebug) {
            Log.d(str, str2);
        }
    }

    public TeslaConnectPanelPlugin(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public CmdObjectList parseCmd(String str) {
        Gson gson = new Gson();
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return (CmdObjectList) gson.fromJson(str, (Class<Object>) CmdObjectList.class);
    }

    public void onCreate(LoadCallback loadCallback) {
        Intent intent = new Intent("com.nothing.experimental.TESLA_SERVICE");
        intent.setPackage("com.nothing.experimental");
        this.mContext.bindService(intent, this.connection, 1);
        this.mLoadCallback = loadCallback;
    }

    public void onDestory() {
        this.mContext.unbindService(this.connection);
        this.mLoadCallback = null;
    }

    public CmdObjectList getCmdList() {
        return this.lists;
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
