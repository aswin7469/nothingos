package com.android.settingslib.media;

import android.app.Notification;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class MediaManager {
    private static final String TAG = "MediaManager";
    protected final Collection<MediaDeviceCallback> mCallbacks = new CopyOnWriteArrayList();
    protected Context mContext;
    protected final List<MediaDevice> mMediaDevices = new CopyOnWriteArrayList();
    protected Notification mNotification;

    public interface MediaDeviceCallback {
        void onConnectedDeviceChanged(String str);

        void onDeviceAdded(MediaDevice mediaDevice);

        void onDeviceAttributesChanged();

        void onDeviceListAdded(List<MediaDevice> list);

        void onDeviceListRemoved(List<MediaDevice> list);

        void onDeviceRemoved(MediaDevice mediaDevice);

        void onRequestFailed(int i);
    }

    public abstract void startScan();

    public abstract void stopScan();

    MediaManager(Context context, Notification notification) {
        this.mContext = context;
        this.mNotification = notification;
    }

    /* access modifiers changed from: protected */
    public void registerCallback(MediaDeviceCallback mediaDeviceCallback) {
        if (!this.mCallbacks.contains(mediaDeviceCallback)) {
            this.mCallbacks.add(mediaDeviceCallback);
        }
    }

    /* access modifiers changed from: protected */
    public void unregisterCallback(MediaDeviceCallback mediaDeviceCallback) {
        if (this.mCallbacks.contains(mediaDeviceCallback)) {
            this.mCallbacks.remove(mediaDeviceCallback);
        }
    }

    /* access modifiers changed from: protected */
    public MediaDevice findMediaDevice(String str) {
        for (MediaDevice next : this.mMediaDevices) {
            if (next.getId().equals(str)) {
                return next;
            }
        }
        Log.e(TAG, "findMediaDevice() can't found device");
        return null;
    }

    /* access modifiers changed from: protected */
    public void dispatchDeviceAdded(MediaDevice mediaDevice) {
        for (MediaDeviceCallback onDeviceAdded : getCallbacks()) {
            onDeviceAdded.onDeviceAdded(mediaDevice);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDeviceRemoved(MediaDevice mediaDevice) {
        for (MediaDeviceCallback onDeviceRemoved : getCallbacks()) {
            onDeviceRemoved.onDeviceRemoved(mediaDevice);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDeviceListAdded() {
        for (MediaDeviceCallback onDeviceListAdded : getCallbacks()) {
            onDeviceListAdded.onDeviceListAdded(new ArrayList(this.mMediaDevices));
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDeviceListRemoved(List<MediaDevice> list) {
        for (MediaDeviceCallback onDeviceListRemoved : getCallbacks()) {
            onDeviceListRemoved.onDeviceListRemoved(list);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchConnectedDeviceChanged(String str) {
        for (MediaDeviceCallback onConnectedDeviceChanged : getCallbacks()) {
            onConnectedDeviceChanged.onConnectedDeviceChanged(str);
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchDataChanged() {
        for (MediaDeviceCallback onDeviceAttributesChanged : getCallbacks()) {
            onDeviceAttributesChanged.onDeviceAttributesChanged();
        }
    }

    /* access modifiers changed from: protected */
    public void dispatchOnRequestFailed(int i) {
        for (MediaDeviceCallback onRequestFailed : getCallbacks()) {
            onRequestFailed.onRequestFailed(i);
        }
    }

    private Collection<MediaDeviceCallback> getCallbacks() {
        return new CopyOnWriteArrayList(this.mCallbacks);
    }
}
