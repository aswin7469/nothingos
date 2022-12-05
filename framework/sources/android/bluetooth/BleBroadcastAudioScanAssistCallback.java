package android.bluetooth;

import android.bluetooth.le.ScanResult;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
/* loaded from: classes.dex */
public abstract class BleBroadcastAudioScanAssistCallback {
    public static final int BASS_STATUS_COLOCATED_SRC_UNAVAILABLE = 5;
    public static final int BASS_STATUS_DUPLICATE_ADDITION = 8;
    public static final int BASS_STATUS_FAILURE = 1;
    public static final int BASS_STATUS_FATAL = 2;
    public static final int BASS_STATUS_INVALID_GROUP_OP = 16;
    public static final int BASS_STATUS_INVALID_SOURCE_ID = 4;
    public static final int BASS_STATUS_INVALID_SOURCE_SELECTED = 6;
    public static final int BASS_STATUS_NO_EMPTY_SLOT = 9;
    public static final int BASS_STATUS_SOURCE_UNAVAILABLE = 7;
    public static final int BASS_STATUS_SUCCESS = 0;
    public static final int BASS_STATUS_TXN_TIMEOUT = 3;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Bass_Status {
    }

    public void onBleBroadcastSourceFound(ScanResult scanres) {
    }

    public void onBleBroadcastSourceSelected(BluetoothDevice device, int status, List<BleBroadcastSourceChannel> broadcastSourceChannels) {
    }

    public void onBleBroadcastAudioSourceAdded(BluetoothDevice device, byte srcId, int status) {
    }

    public void onBleBroadcastAudioSourceUpdated(BluetoothDevice device, byte srcId, int status) {
    }

    public void onBleBroadcastPinUpdated(BluetoothDevice rcvr, byte srcId, int status) {
    }

    public void onBleBroadcastAudioSourceRemoved(BluetoothDevice rcvr, byte srcId, int status) {
    }
}
