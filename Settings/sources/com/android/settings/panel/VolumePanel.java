package com.android.settings.panel;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import com.android.settings.R$dimen;
import com.android.settings.R$string;
import com.android.settings.bluetooth.Utils;
import com.android.settings.slices.CustomSliceRegistry;
import com.android.settingslib.bluetooth.A2dpProfile;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class VolumePanel implements PanelContent, LifecycleObserver {
    /* access modifiers changed from: private */
    public PanelContentCallback mCallback;
    private final Context mContext;
    private int mControlSliceWidth;
    private LocalBluetoothProfileManager mProfileManager;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("com.android.settings.panel.action.CLOSE_PANEL".equals(intent.getAction())) {
                VolumePanel.this.mCallback.forceClose();
            }
        }
    };

    public int getMetricsCategory() {
        return 1655;
    }

    public int getViewType() {
        return 1;
    }

    public static VolumePanel create(Context context) {
        return new VolumePanel(context);
    }

    private VolumePanel(Context context) {
        this.mContext = context.getApplicationContext();
        if (context instanceof Activity) {
            this.mControlSliceWidth = ((Activity) context).getWindowManager().getCurrentWindowMetrics().getBounds().width() - (context.getResources().getDimensionPixelSize(R$dimen.panel_slice_Horizontal_padding) * 2);
        }
        FutureTask futureTask = new FutureTask(new VolumePanel$$ExternalSyntheticLambda0(this));
        try {
            futureTask.run();
            LocalBluetoothManager localBluetoothManager = (LocalBluetoothManager) futureTask.get();
            if (localBluetoothManager != null) {
                this.mProfileManager = localBluetoothManager.getProfileManager();
            }
        } catch (InterruptedException | ExecutionException unused) {
            Log.w("VolumePanel", "Error getting LocalBluetoothManager.");
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ LocalBluetoothManager lambda$new$0() throws Exception {
        return Utils.getLocalBtManager(this.mContext);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.android.settings.panel.action.CLOSE_PANEL");
        this.mContext.registerReceiver(this.mReceiver, intentFilter, 2);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    public CharSequence getTitle() {
        return this.mContext.getText(R$string.sound_settings);
    }

    public List<Uri> getSlices() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(CustomSliceRegistry.REMOTE_MEDIA_SLICE_URI);
        arrayList.add(CustomSliceRegistry.VOLUME_MEDIA_URI);
        Uri extraControlUri = getExtraControlUri();
        if (extraControlUri != null) {
            Log.d("VolumePanel", "add extra control slice");
            arrayList.add(extraControlUri);
        }
        arrayList.add(CustomSliceRegistry.MEDIA_OUTPUT_INDICATOR_SLICE_URI);
        arrayList.add(CustomSliceRegistry.VOLUME_RINGER_URI);
        arrayList.add(CustomSliceRegistry.VOLUME_ALARM_URI);
        return arrayList;
    }

    public Intent getSeeMoreIntent() {
        return new Intent("android.settings.SOUND_SETTINGS").addFlags(268435456);
    }

    public void registerCallback(PanelContentCallback panelContentCallback) {
        this.mCallback = panelContentCallback;
    }

    private Uri getExtraControlUri() {
        BluetoothDevice findActiveDevice = findActiveDevice();
        if (findActiveDevice == null) {
            return null;
        }
        String controlUriMetaData = BluetoothUtils.getControlUriMetaData(findActiveDevice);
        if (TextUtils.isEmpty(controlUriMetaData)) {
            return null;
        }
        try {
            return Uri.parse(controlUriMetaData + this.mControlSliceWidth);
        } catch (NullPointerException unused) {
            Log.d("VolumePanel", "unable to parse uri");
            return null;
        }
    }

    private BluetoothDevice findActiveDevice() {
        A2dpProfile a2dpProfile;
        LocalBluetoothProfileManager localBluetoothProfileManager = this.mProfileManager;
        if (localBluetoothProfileManager == null || (a2dpProfile = localBluetoothProfileManager.getA2dpProfile()) == null) {
            return null;
        }
        return a2dpProfile.getActiveDevice();
    }
}
