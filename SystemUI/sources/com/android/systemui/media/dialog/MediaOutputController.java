package com.android.systemui.media.dialog;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.WallpaperColors;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeBroadcast;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.INearbyMediaDevicesUpdateCallback;
import android.media.MediaMetadata;
import android.media.NearbyDevice;
import android.media.RoutingSessionInfo;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.IBinder;
import android.os.PowerExemptionManager;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.core.graphics.drawable.IconCompat;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.Utils;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.LocalBluetoothLeBroadcast;
import com.android.settingslib.bluetooth.LocalBluetoothLeBroadcastMetadata;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.media.InfoMediaManager;
import com.android.settingslib.media.LocalMediaManager;
import com.android.settingslib.media.MediaDevice;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.C1894R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.nearby.NearbyMediaDevicesManager;
import com.android.systemui.monet.ColorScheme;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.nio.charset.StandardCharsets;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class MediaOutputController implements LocalMediaManager.DeviceCallback, INearbyMediaDevicesUpdateCallback {
    private static final long ALLOWLIST_DURATION_MS = 20000;
    private static final String ALLOWLIST_REASON = "mediaoutput:remote_transfer";
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String PAGE_CONNECTED_DEVICES_KEY = "top_level_connected_devices";
    private static final String TAG = "MediaOutputController";
    private float mActiveRadius;
    private final ActivityStarter mActivityStarter;
    private final AudioManager mAudioManager;
    final List<MediaDevice> mCachedMediaDevices = new CopyOnWriteArrayList();
    Callback mCallback;
    private final MediaController.Callback mCb = new MediaController.Callback() {
        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            MediaOutputController.this.mCallback.onMediaChanged();
        }

        public void onPlaybackStateChanged(PlaybackState playbackState) {
            int state = playbackState.getState();
            if (state == 1 || state == 2) {
                MediaOutputController.this.mCallback.onMediaStoppedOrPaused();
            }
        }
    };
    private int mColorButtonBackground;
    private int mColorConnectedItemBackground;
    private int mColorItemBackground;
    private int mColorItemContent;
    private int mColorPositiveButtonText;
    private int mColorSeekbarProgress;
    private final Context mContext;
    private final DialogLaunchAnimator mDialogLaunchAnimator;
    private final List<MediaDevice> mGroupMediaDevices = new CopyOnWriteArrayList();
    private float mInactiveRadius;
    private boolean mIsRefreshing = false;
    private final LocalBluetoothManager mLocalBluetoothManager;
    LocalMediaManager mLocalMediaManager;
    private MediaController mMediaController;
    final List<MediaDevice> mMediaDevices = new CopyOnWriteArrayList();
    private final Object mMediaDevicesLock = new Object();
    private final MediaSessionManager mMediaSessionManager;
    private MediaOutputMetricLogger mMetricLogger;
    private final Map<String, Integer> mNearbyDeviceInfoMap = new ConcurrentHashMap();
    private final NearbyMediaDevicesManager mNearbyMediaDevicesManager;
    private boolean mNeedRefresh = false;
    private final CommonNotifCollection mNotifCollection;
    private final String mPackageName;
    private final PowerExemptionManager mPowerExemptionManager;

    public enum BroadcastNotifyDialog {
        ACTION_FIRST_LAUNCH,
        ACTION_BROADCAST_INFO_ICON
    }

    interface Callback {
        void dismissDialog();

        void onDeviceListChanged();

        void onMediaChanged();

        void onMediaStoppedOrPaused();

        void onRouteChanged();
    }

    public IBinder asBinder() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldShowLaunchSection() {
        return false;
    }

    @Inject
    public MediaOutputController(Context context, String str, MediaSessionManager mediaSessionManager, LocalBluetoothManager localBluetoothManager, ActivityStarter activityStarter, CommonNotifCollection commonNotifCollection, DialogLaunchAnimator dialogLaunchAnimator, Optional<NearbyMediaDevicesManager> optional, AudioManager audioManager, PowerExemptionManager powerExemptionManager) {
        this.mContext = context;
        this.mPackageName = str;
        this.mMediaSessionManager = mediaSessionManager;
        this.mLocalBluetoothManager = localBluetoothManager;
        this.mActivityStarter = activityStarter;
        this.mNotifCollection = commonNotifCollection;
        this.mAudioManager = audioManager;
        this.mPowerExemptionManager = powerExemptionManager;
        this.mLocalMediaManager = new LocalMediaManager(context, localBluetoothManager, new InfoMediaManager(context, str, (Notification) null, localBluetoothManager), str);
        this.mMetricLogger = new MediaOutputMetricLogger(context, str);
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        this.mNearbyMediaDevicesManager = optional.orElse(null);
        this.mColorItemContent = Utils.getColorStateListDefaultColor(context, C1894R.C1895color.media_dialog_item_main_content);
        this.mColorSeekbarProgress = Utils.getColorStateListDefaultColor(context, C1894R.C1895color.media_dialog_seekbar_progress);
        this.mColorButtonBackground = Utils.getColorStateListDefaultColor(context, C1894R.C1895color.media_dialog_button_background);
        this.mColorItemBackground = Utils.getColorStateListDefaultColor(context, C1894R.C1895color.media_dialog_item_background);
        this.mColorConnectedItemBackground = Utils.getColorStateListDefaultColor(context, C1894R.C1895color.media_dialog_connected_item_background);
        this.mColorPositiveButtonText = Utils.getColorStateListDefaultColor(context, C1894R.C1895color.media_dialog_solid_button_text);
        this.mInactiveRadius = context.getResources().getDimension(C1894R.dimen.media_output_dialog_background_radius);
        this.mActiveRadius = context.getResources().getDimension(C1894R.dimen.media_output_dialog_active_background_radius);
    }

    /* access modifiers changed from: package-private */
    public void start(Callback callback) {
        synchronized (this.mMediaDevicesLock) {
            this.mCachedMediaDevices.clear();
            this.mMediaDevices.clear();
        }
        this.mNearbyDeviceInfoMap.clear();
        NearbyMediaDevicesManager nearbyMediaDevicesManager = this.mNearbyMediaDevicesManager;
        if (nearbyMediaDevicesManager != null) {
            nearbyMediaDevicesManager.registerNearbyDevicesCallback(this);
        }
        if (!TextUtils.isEmpty(this.mPackageName)) {
            Iterator<MediaController> it = this.mMediaSessionManager.getActiveSessions((ComponentName) null).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MediaController next = it.next();
                if (TextUtils.equals(next.getPackageName(), this.mPackageName)) {
                    this.mMediaController = next;
                    next.unregisterCallback(this.mCb);
                    this.mMediaController.registerCallback(this.mCb);
                    break;
                }
            }
        }
        if (this.mMediaController == null && DEBUG) {
            Log.d(TAG, "No media controller for " + this.mPackageName);
        }
        LocalMediaManager localMediaManager = this.mLocalMediaManager;
        if (localMediaManager != null) {
            this.mCallback = callback;
            localMediaManager.unregisterCallback(this);
            this.mLocalMediaManager.stopScan();
            this.mLocalMediaManager.registerCallback(this);
            this.mLocalMediaManager.startScan();
        } else if (DEBUG) {
            Log.d(TAG, "No local media manager " + this.mPackageName);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isRefreshing() {
        return this.mIsRefreshing;
    }

    /* access modifiers changed from: package-private */
    public void setRefreshing(boolean z) {
        this.mIsRefreshing = z;
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        MediaController mediaController = this.mMediaController;
        if (mediaController != null) {
            mediaController.unregisterCallback(this.mCb);
        }
        LocalMediaManager localMediaManager = this.mLocalMediaManager;
        if (localMediaManager != null) {
            localMediaManager.unregisterCallback(this);
            this.mLocalMediaManager.stopScan();
        }
        synchronized (this.mMediaDevicesLock) {
            this.mCachedMediaDevices.clear();
            this.mMediaDevices.clear();
        }
        NearbyMediaDevicesManager nearbyMediaDevicesManager = this.mNearbyMediaDevicesManager;
        if (nearbyMediaDevicesManager != null) {
            nearbyMediaDevicesManager.unregisterNearbyDevicesCallback(this);
        }
        this.mNearbyDeviceInfoMap.clear();
    }

    public void onDeviceListUpdate(List<MediaDevice> list) {
        if (this.mMediaDevices.isEmpty() || !this.mIsRefreshing) {
            buildMediaDevices(list);
            this.mCallback.onDeviceListChanged();
            return;
        }
        synchronized (this.mMediaDevicesLock) {
            this.mNeedRefresh = true;
            this.mCachedMediaDevices.clear();
            this.mCachedMediaDevices.addAll(list);
        }
    }

    public void onSelectedDeviceStateChanged(MediaDevice mediaDevice, int i) {
        this.mCallback.onRouteChanged();
        this.mMetricLogger.logOutputSuccess(mediaDevice.toString(), new ArrayList(this.mMediaDevices));
    }

    public void onDeviceAttributesChanged() {
        this.mCallback.onRouteChanged();
    }

    public void onRequestFailed(int i) {
        this.mCallback.onRouteChanged();
        this.mMetricLogger.logOutputFailure(new ArrayList(this.mMediaDevices), i);
    }

    public boolean hasMutingExpectedDevice() {
        return this.mAudioManager.getMutingExpectedDevice() != null;
    }

    public void cancelMuteAwaitConnection() {
        if (this.mAudioManager.getMutingExpectedDevice() != null) {
            try {
                AudioManager audioManager = this.mAudioManager;
                audioManager.cancelMuteAwaitConnection(audioManager.getMutingExpectedDevice());
            } catch (Exception unused) {
                Log.d(TAG, "Unable to cancel mute await connection");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Drawable getAppSourceIcon() {
        if (this.mPackageName.isEmpty()) {
            return null;
        }
        try {
            Log.d(TAG, "try to get app icon");
            return this.mContext.getPackageManager().getApplicationIcon(this.mPackageName);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.d(TAG, "icon not found");
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public String getAppSourceName() {
        Object obj;
        ApplicationInfo applicationInfo = null;
        if (this.mPackageName.isEmpty()) {
            return null;
        }
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(this.mPackageName, PackageManager.ApplicationInfoFlags.of(0));
        } catch (PackageManager.NameNotFoundException unused) {
        }
        if (applicationInfo != null) {
            obj = packageManager.getApplicationLabel(applicationInfo);
        } else {
            obj = this.mContext.getString(C1894R.string.media_output_dialog_unknown_launch_app_name);
        }
        return (String) obj;
    }

    /* access modifiers changed from: package-private */
    public Intent getAppLaunchIntent() {
        if (this.mPackageName.isEmpty()) {
            return null;
        }
        return this.mContext.getPackageManager().getLaunchIntentForPackage(this.mPackageName);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderTitle() {
        MediaMetadata metadata;
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || (metadata = mediaController.getMetadata()) == null) {
            return this.mContext.getText(C1894R.string.controls_media_title);
        }
        return metadata.getDescription().getTitle();
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderSubTitle() {
        MediaMetadata metadata;
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || (metadata = mediaController.getMetadata()) == null) {
            return null;
        }
        return metadata.getDescription().getSubtitle();
    }

    /* access modifiers changed from: package-private */
    public IconCompat getHeaderIcon() {
        Bitmap iconBitmap;
        MediaController mediaController = this.mMediaController;
        if (mediaController == null) {
            return null;
        }
        MediaMetadata metadata = mediaController.getMetadata();
        if (metadata == null || (iconBitmap = metadata.getDescription().getIconBitmap()) == null) {
            if (DEBUG) {
                Log.d(TAG, "Media meta data does not contain icon information");
            }
            return getNotificationIcon();
        }
        Context context = this.mContext;
        return IconCompat.createWithBitmap(Utils.convertCornerRadiusBitmap(context, iconBitmap, (float) context.getResources().getDimensionPixelSize(C1894R.dimen.media_output_dialog_icon_corner_radius)));
    }

    /* access modifiers changed from: package-private */
    public IconCompat getDeviceIconCompat(MediaDevice mediaDevice) {
        Drawable icon = mediaDevice.getIcon();
        if (icon == null) {
            if (DEBUG) {
                Log.d(TAG, "getDeviceIconCompat() device : " + mediaDevice.getName() + ", drawable is null");
            }
            icon = this.mContext.getDrawable(17302336);
        }
        if (!(icon instanceof BitmapDrawable)) {
            setColorFilter(icon, isActiveItem(mediaDevice));
        }
        return BluetoothUtils.createIconWithDrawable(icon);
    }

    /* access modifiers changed from: package-private */
    public void setColorFilter(Drawable drawable, boolean z) {
        drawable.setColorFilter(new PorterDuffColorFilter(this.mColorItemContent, PorterDuff.Mode.SRC_IN));
    }

    /* access modifiers changed from: package-private */
    public boolean isActiveItem(MediaDevice mediaDevice) {
        boolean equals = this.mLocalMediaManager.getCurrentConnectedDevice().getId().equals(mediaDevice.getId());
        boolean z = getSelectedMediaDevice().size() > 1 && getSelectedMediaDevice().contains(mediaDevice);
        if ((hasAdjustVolumeUserRestriction() || !equals || isTransferring()) && !z) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public IconCompat getNotificationIcon() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            return null;
        }
        Iterator<NotificationEntry> it = this.mNotifCollection.getAllNotifs().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            NotificationEntry next = it.next();
            Notification notification = next.getSbn().getNotification();
            if (notification.isMediaNotification() && TextUtils.equals(next.getSbn().getPackageName(), this.mPackageName)) {
                Icon largeIcon = notification.getLargeIcon();
                if (largeIcon != null) {
                    return IconCompat.createFromIcon(largeIcon);
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void setCurrentColorScheme(WallpaperColors wallpaperColors, boolean z) {
        ColorScheme colorScheme = new ColorScheme(wallpaperColors, z);
        if (z) {
            this.mColorItemContent = colorScheme.getAccent1().get(2).intValue();
            this.mColorSeekbarProgress = colorScheme.getAccent2().get(7).intValue();
            this.mColorButtonBackground = colorScheme.getAccent1().get(4).intValue();
            this.mColorItemBackground = colorScheme.getNeutral2().get(9).intValue();
            this.mColorConnectedItemBackground = colorScheme.getAccent2().get(9).intValue();
            this.mColorPositiveButtonText = colorScheme.getAccent2().get(9).intValue();
            return;
        }
        this.mColorItemContent = colorScheme.getAccent1().get(9).intValue();
        this.mColorSeekbarProgress = colorScheme.getAccent1().get(4).intValue();
        this.mColorButtonBackground = colorScheme.getAccent1().get(7).intValue();
        this.mColorItemBackground = colorScheme.getAccent2().get(1).intValue();
        this.mColorConnectedItemBackground = colorScheme.getAccent1().get(2).intValue();
        this.mColorPositiveButtonText = colorScheme.getNeutral1().get(1).intValue();
    }

    /* access modifiers changed from: package-private */
    public void refreshDataSetIfNeeded() {
        if (this.mNeedRefresh) {
            buildMediaDevices(this.mCachedMediaDevices);
            this.mCallback.onDeviceListChanged();
            this.mNeedRefresh = false;
        }
    }

    public int getColorConnectedItemBackground() {
        return this.mColorConnectedItemBackground;
    }

    public int getColorPositiveButtonText() {
        return this.mColorPositiveButtonText;
    }

    public int getColorItemContent() {
        return this.mColorItemContent;
    }

    public int getColorSeekbarProgress() {
        return this.mColorSeekbarProgress;
    }

    public int getColorButtonBackground() {
        return this.mColorButtonBackground;
    }

    public int getColorItemBackground() {
        return this.mColorItemBackground;
    }

    public float getInactiveRadius() {
        return this.mInactiveRadius;
    }

    public float getActiveRadius() {
        return this.mActiveRadius;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0064, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void buildMediaDevices(java.util.List<com.android.settingslib.media.MediaDevice> r9) {
        /*
            r8 = this;
            java.lang.Object r0 = r8.mMediaDevicesLock
            monitor-enter(r0)
            r8.attachRangeInfo(r9)     // Catch:{ all -> 0x00e6 }
            java.util.Comparator r1 = java.util.Comparator.naturalOrder()     // Catch:{ all -> 0x00e6 }
            java.util.Collections.sort(r9, r1)     // Catch:{ all -> 0x00e6 }
            java.util.List<com.android.settingslib.media.MediaDevice> r1 = r8.mMediaDevices     // Catch:{ all -> 0x00e6 }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x00e6 }
            if (r1 == 0) goto L_0x0091
            boolean r1 = r8.hasMutingExpectedDevice()     // Catch:{ all -> 0x00e6 }
            r2 = 0
            if (r1 == 0) goto L_0x0024
            boolean r1 = r8.isCurrentConnectedDeviceRemote()     // Catch:{ all -> 0x00e6 }
            if (r1 != 0) goto L_0x0024
            r1 = 1
            goto L_0x0025
        L_0x0024:
            r1 = r2
        L_0x0025:
            if (r1 == 0) goto L_0x0029
            r3 = 0
            goto L_0x002d
        L_0x0029:
            com.android.settingslib.media.MediaDevice r3 = r8.getCurrentConnectedMediaDevice()     // Catch:{ all -> 0x00e6 }
        L_0x002d:
            if (r3 != 0) goto L_0x0065
            boolean r3 = DEBUG     // Catch:{ all -> 0x00e6 }
            if (r3 == 0) goto L_0x003a
            java.lang.String r3 = "MediaOutputController"
            java.lang.String r4 = "No connected media device or muting expected device exist."
            android.util.Log.d(r3, r4)     // Catch:{ all -> 0x00e6 }
        L_0x003a:
            if (r1 == 0) goto L_0x005e
            java.util.Iterator r9 = r9.iterator()     // Catch:{ all -> 0x00e6 }
        L_0x0040:
            boolean r1 = r9.hasNext()     // Catch:{ all -> 0x00e6 }
            if (r1 == 0) goto L_0x0063
            java.lang.Object r1 = r9.next()     // Catch:{ all -> 0x00e6 }
            com.android.settingslib.media.MediaDevice r1 = (com.android.settingslib.media.MediaDevice) r1     // Catch:{ all -> 0x00e6 }
            boolean r3 = r1.isMutingExpectedDevice()     // Catch:{ all -> 0x00e6 }
            if (r3 == 0) goto L_0x0058
            java.util.List<com.android.settingslib.media.MediaDevice> r3 = r8.mMediaDevices     // Catch:{ all -> 0x00e6 }
            r3.add(r2, r1)     // Catch:{ all -> 0x00e6 }
            goto L_0x0040
        L_0x0058:
            java.util.List<com.android.settingslib.media.MediaDevice> r3 = r8.mMediaDevices     // Catch:{ all -> 0x00e6 }
            r3.add(r1)     // Catch:{ all -> 0x00e6 }
            goto L_0x0040
        L_0x005e:
            java.util.List<com.android.settingslib.media.MediaDevice> r8 = r8.mMediaDevices     // Catch:{ all -> 0x00e6 }
            r8.addAll(r9)     // Catch:{ all -> 0x00e6 }
        L_0x0063:
            monitor-exit(r0)     // Catch:{ all -> 0x00e6 }
            return
        L_0x0065:
            java.util.Iterator r9 = r9.iterator()     // Catch:{ all -> 0x00e6 }
        L_0x0069:
            boolean r1 = r9.hasNext()     // Catch:{ all -> 0x00e6 }
            if (r1 == 0) goto L_0x008f
            java.lang.Object r1 = r9.next()     // Catch:{ all -> 0x00e6 }
            com.android.settingslib.media.MediaDevice r1 = (com.android.settingslib.media.MediaDevice) r1     // Catch:{ all -> 0x00e6 }
            java.lang.String r4 = r1.getId()     // Catch:{ all -> 0x00e6 }
            java.lang.String r5 = r3.getId()     // Catch:{ all -> 0x00e6 }
            boolean r4 = android.text.TextUtils.equals(r4, r5)     // Catch:{ all -> 0x00e6 }
            if (r4 == 0) goto L_0x0089
            java.util.List<com.android.settingslib.media.MediaDevice> r4 = r8.mMediaDevices     // Catch:{ all -> 0x00e6 }
            r4.add(r2, r1)     // Catch:{ all -> 0x00e6 }
            goto L_0x0069
        L_0x0089:
            java.util.List<com.android.settingslib.media.MediaDevice> r4 = r8.mMediaDevices     // Catch:{ all -> 0x00e6 }
            r4.add(r1)     // Catch:{ all -> 0x00e6 }
            goto L_0x0069
        L_0x008f:
            monitor-exit(r0)     // Catch:{ all -> 0x00e6 }
            return
        L_0x0091:
            java.util.ArrayList r1 = new java.util.ArrayList     // Catch:{ all -> 0x00e6 }
            r1.<init>()     // Catch:{ all -> 0x00e6 }
            java.util.List<com.android.settingslib.media.MediaDevice> r2 = r8.mMediaDevices     // Catch:{ all -> 0x00e6 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x00e6 }
        L_0x009c:
            boolean r3 = r2.hasNext()     // Catch:{ all -> 0x00e6 }
            if (r3 == 0) goto L_0x00ca
            java.lang.Object r3 = r2.next()     // Catch:{ all -> 0x00e6 }
            com.android.settingslib.media.MediaDevice r3 = (com.android.settingslib.media.MediaDevice) r3     // Catch:{ all -> 0x00e6 }
            java.util.Iterator r4 = r9.iterator()     // Catch:{ all -> 0x00e6 }
        L_0x00ac:
            boolean r5 = r4.hasNext()     // Catch:{ all -> 0x00e6 }
            if (r5 == 0) goto L_0x009c
            java.lang.Object r5 = r4.next()     // Catch:{ all -> 0x00e6 }
            com.android.settingslib.media.MediaDevice r5 = (com.android.settingslib.media.MediaDevice) r5     // Catch:{ all -> 0x00e6 }
            java.lang.String r6 = r3.getId()     // Catch:{ all -> 0x00e6 }
            java.lang.String r7 = r5.getId()     // Catch:{ all -> 0x00e6 }
            boolean r6 = android.text.TextUtils.equals(r6, r7)     // Catch:{ all -> 0x00e6 }
            if (r6 == 0) goto L_0x00ac
            r1.add(r5)     // Catch:{ all -> 0x00e6 }
            goto L_0x009c
        L_0x00ca:
            int r2 = r1.size()     // Catch:{ all -> 0x00e6 }
            int r3 = r9.size()     // Catch:{ all -> 0x00e6 }
            if (r2 == r3) goto L_0x00da
            r9.removeAll(r1)     // Catch:{ all -> 0x00e6 }
            r1.addAll(r9)     // Catch:{ all -> 0x00e6 }
        L_0x00da:
            java.util.List<com.android.settingslib.media.MediaDevice> r9 = r8.mMediaDevices     // Catch:{ all -> 0x00e6 }
            r9.clear()     // Catch:{ all -> 0x00e6 }
            java.util.List<com.android.settingslib.media.MediaDevice> r8 = r8.mMediaDevices     // Catch:{ all -> 0x00e6 }
            r8.addAll(r1)     // Catch:{ all -> 0x00e6 }
            monitor-exit(r0)     // Catch:{ all -> 0x00e6 }
            return
        L_0x00e6:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x00e6 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dialog.MediaOutputController.buildMediaDevices(java.util.List):void");
    }

    private void attachRangeInfo(List<MediaDevice> list) {
        for (MediaDevice next : list) {
            if (this.mNearbyDeviceInfoMap.containsKey(next.getId())) {
                next.setRangeZone(this.mNearbyDeviceInfoMap.get(next.getId()).intValue());
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isCurrentConnectedDeviceRemote() {
        MediaDevice currentConnectedMediaDevice = getCurrentConnectedMediaDevice();
        return currentConnectedMediaDevice != null && isActiveRemoteDevice(currentConnectedMediaDevice);
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getGroupMediaDevices() {
        List<MediaDevice> selectedMediaDevice = getSelectedMediaDevice();
        List<MediaDevice> selectableMediaDevice = getSelectableMediaDevice();
        if (this.mGroupMediaDevices.isEmpty()) {
            this.mGroupMediaDevices.addAll(selectedMediaDevice);
            this.mGroupMediaDevices.addAll(selectableMediaDevice);
            return this.mGroupMediaDevices;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        arrayList.addAll(selectedMediaDevice);
        arrayList.addAll(selectableMediaDevice);
        for (MediaDevice next : this.mGroupMediaDevices) {
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MediaDevice mediaDevice = (MediaDevice) it.next();
                if (TextUtils.equals(next.getId(), mediaDevice.getId())) {
                    arrayList2.add(mediaDevice);
                    arrayList.remove(mediaDevice);
                    break;
                }
            }
        }
        if (!arrayList.isEmpty()) {
            arrayList2.addAll(arrayList);
        }
        this.mGroupMediaDevices.clear();
        this.mGroupMediaDevices.addAll(arrayList2);
        return this.mGroupMediaDevices;
    }

    /* access modifiers changed from: package-private */
    public void resetGroupMediaDevices() {
        this.mGroupMediaDevices.clear();
    }

    /* access modifiers changed from: package-private */
    public void connectDevice(MediaDevice mediaDevice) {
        this.mMetricLogger.updateOutputEndPoints(getCurrentConnectedMediaDevice(), mediaDevice);
        ThreadUtils.postOnBackgroundThread((Runnable) new MediaOutputController$$ExternalSyntheticLambda1(this, mediaDevice));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$connectDevice$0$com-android-systemui-media-dialog-MediaOutputController */
    public /* synthetic */ void mo34435xe5ef5630(MediaDevice mediaDevice) {
        this.mLocalMediaManager.connectDevice(mediaDevice);
    }

    /* access modifiers changed from: package-private */
    public Collection<MediaDevice> getMediaDevices() {
        return this.mMediaDevices;
    }

    /* access modifiers changed from: package-private */
    public MediaDevice getCurrentConnectedMediaDevice() {
        return this.mLocalMediaManager.getCurrentConnectedDevice();
    }

    private MediaDevice getMediaDeviceById(String str) {
        return this.mLocalMediaManager.getMediaDeviceById(new ArrayList(this.mMediaDevices), str);
    }

    /* access modifiers changed from: package-private */
    public boolean addDeviceToPlayMedia(MediaDevice mediaDevice) {
        this.mMetricLogger.logInteractionExpansion(mediaDevice);
        return this.mLocalMediaManager.addDeviceToPlayMedia(mediaDevice);
    }

    /* access modifiers changed from: package-private */
    public boolean removeDeviceFromPlayMedia(MediaDevice mediaDevice) {
        return this.mLocalMediaManager.removeDeviceFromPlayMedia(mediaDevice);
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getSelectableMediaDevice() {
        return this.mLocalMediaManager.getSelectableMediaDevice();
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getSelectedMediaDevice() {
        return this.mLocalMediaManager.getSelectedMediaDevice();
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getDeselectableMediaDevice() {
        return this.mLocalMediaManager.getDeselectableMediaDevice();
    }

    /* access modifiers changed from: package-private */
    public void adjustSessionVolume(String str, int i) {
        this.mLocalMediaManager.adjustSessionVolume(str, i);
    }

    /* access modifiers changed from: package-private */
    public void adjustSessionVolume(int i) {
        this.mLocalMediaManager.adjustSessionVolume(i);
    }

    /* access modifiers changed from: package-private */
    public int getSessionVolumeMax() {
        return this.mLocalMediaManager.getSessionVolumeMax();
    }

    /* access modifiers changed from: package-private */
    public int getSessionVolume() {
        return this.mLocalMediaManager.getSessionVolume();
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSessionName() {
        return this.mLocalMediaManager.getSessionName();
    }

    /* access modifiers changed from: package-private */
    public void releaseSession() {
        this.mMetricLogger.logInteractionStopCasting();
        this.mLocalMediaManager.releaseSession();
    }

    /* access modifiers changed from: package-private */
    public List<RoutingSessionInfo> getActiveRemoteMediaDevices() {
        ArrayList arrayList = new ArrayList();
        for (RoutingSessionInfo next : this.mLocalMediaManager.getActiveMediaSession()) {
            if (!next.isSystemSession()) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void adjustVolume(MediaDevice mediaDevice, int i) {
        this.mMetricLogger.logInteractionAdjustVolume(mediaDevice);
        ThreadUtils.postOnBackgroundThread((Runnable) new MediaOutputController$$ExternalSyntheticLambda0(mediaDevice, i));
    }

    /* access modifiers changed from: package-private */
    public String getPackageName() {
        return this.mPackageName;
    }

    /* access modifiers changed from: package-private */
    public boolean hasAdjustVolumeUserRestriction() {
        if (RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, "no_adjust_volume", UserHandle.myUserId()) != null) {
            return true;
        }
        return ((UserManager) this.mContext.getSystemService(UserManager.class)).hasBaseUserRestriction("no_adjust_volume", UserHandle.of(UserHandle.myUserId()));
    }

    /* access modifiers changed from: package-private */
    public boolean isTransferring() {
        synchronized (this.mMediaDevicesLock) {
            for (MediaDevice state : this.mMediaDevices) {
                if (state.getState() == 1) {
                    return true;
                }
            }
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0025, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0027, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isZeroMode() {
        /*
            r3 = this;
            java.lang.Object r0 = r3.mMediaDevicesLock
            monitor-enter(r0)
            java.util.List<com.android.settingslib.media.MediaDevice> r1 = r3.mMediaDevices     // Catch:{ all -> 0x0029 }
            int r1 = r1.size()     // Catch:{ all -> 0x0029 }
            r2 = 1
            if (r1 != r2) goto L_0x0026
            java.util.List<com.android.settingslib.media.MediaDevice> r3 = r3.mMediaDevices     // Catch:{ all -> 0x0029 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ all -> 0x0029 }
            java.lang.Object r3 = r3.next()     // Catch:{ all -> 0x0029 }
            com.android.settingslib.media.MediaDevice r3 = (com.android.settingslib.media.MediaDevice) r3     // Catch:{ all -> 0x0029 }
            int r3 = r3.getDeviceType()     // Catch:{ all -> 0x0029 }
            if (r3 == r2) goto L_0x0024
            r1 = 3
            if (r3 == r1) goto L_0x0024
            r1 = 2
            if (r3 != r1) goto L_0x0026
        L_0x0024:
            monitor-exit(r0)     // Catch:{ all -> 0x0029 }
            return r2
        L_0x0026:
            monitor-exit(r0)     // Catch:{ all -> 0x0029 }
            r3 = 0
            return r3
        L_0x0029:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0029 }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dialog.MediaOutputController.isZeroMode():boolean");
    }

    /* access modifiers changed from: package-private */
    public void launchBluetoothPairing(View view) {
        ActivityLaunchAnimator.Controller createActivityLaunchController = this.mDialogLaunchAnimator.createActivityLaunchController(view);
        if (createActivityLaunchController == null) {
            this.mCallback.dismissDialog();
        }
        Intent addFlags = new Intent("android.settings.BLUETOOTH_PAIRING_SETTINGS").addFlags(335544320);
        Intent intent = new Intent("android.settings.SETTINGS_EMBED_DEEP_LINK_ACTIVITY");
        if (intent.resolveActivity(this.mContext.getPackageManager()) != null) {
            Log.d(TAG, "Device support split mode, launch page with deep link");
            intent.setFlags(268435456);
            intent.putExtra("android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI", addFlags.toUri(1));
            intent.putExtra("android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_HIGHLIGHT_MENU_KEY", PAGE_CONNECTED_DEVICES_KEY);
            this.mActivityStarter.startActivity(intent, true, createActivityLaunchController);
            return;
        }
        this.mActivityStarter.startActivity(addFlags, true, createActivityLaunchController);
    }

    /* renamed from: com.android.systemui.media.dialog.MediaOutputController$2 */
    static /* synthetic */ class C22312 {

        /* renamed from: $SwitchMap$com$android$systemui$media$dialog$MediaOutputController$BroadcastNotifyDialog */
        static final /* synthetic */ int[] f318x2ffb14dc;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.android.systemui.media.dialog.MediaOutputController$BroadcastNotifyDialog[] r0 = com.android.systemui.media.dialog.MediaOutputController.BroadcastNotifyDialog.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f318x2ffb14dc = r0
                com.android.systemui.media.dialog.MediaOutputController$BroadcastNotifyDialog r1 = com.android.systemui.media.dialog.MediaOutputController.BroadcastNotifyDialog.ACTION_FIRST_LAUNCH     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f318x2ffb14dc     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.media.dialog.MediaOutputController$BroadcastNotifyDialog r1 = com.android.systemui.media.dialog.MediaOutputController.BroadcastNotifyDialog.ACTION_BROADCAST_INFO_ICON     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dialog.MediaOutputController.C22312.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public void launchLeBroadcastNotifyDialog(View view, BroadcastSender broadcastSender, BroadcastNotifyDialog broadcastNotifyDialog, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
        int i = C22312.f318x2ffb14dc[broadcastNotifyDialog.ordinal()];
        if (i == 1) {
            builder.setTitle(C1894R.string.media_output_first_broadcast_title);
            builder.setMessage(C1894R.string.media_output_first_notify_broadcast_message);
            builder.setNegativeButton(17039360, (DialogInterface.OnClickListener) null);
            builder.setPositiveButton(C1894R.string.media_output_broadcast, onClickListener);
        } else if (i == 2) {
            builder.setTitle(C1894R.string.media_output_broadcast);
            builder.setMessage(C1894R.string.media_output_broadcasting_message);
            builder.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
        }
        AlertDialog create = builder.create();
        create.getWindow().setType(Types.SQLXML);
        SystemUIDialog.setShowForAllUsers(create, true);
        SystemUIDialog.registerDismissListener(create);
        create.show();
    }

    /* access modifiers changed from: package-private */
    public void launchMediaOutputBroadcastDialog(View view, BroadcastSender broadcastSender) {
        this.mDialogLaunchAnimator.showFromView(new MediaOutputBroadcastDialog(this.mContext, true, broadcastSender, new MediaOutputController(this.mContext, this.mPackageName, this.mMediaSessionManager, this.mLocalBluetoothManager, this.mActivityStarter, this.mNotifCollection, this.mDialogLaunchAnimator, Optional.m1751of(this.mNearbyMediaDevicesManager), this.mAudioManager, this.mPowerExemptionManager)), view);
    }

    /* access modifiers changed from: package-private */
    public String getBroadcastName() {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile != null) {
            return leAudioBroadcastProfile.getProgramInfo();
        }
        Log.d(TAG, "getBroadcastName: LE Audio Broadcast is null");
        return "";
    }

    /* access modifiers changed from: package-private */
    public void setBroadcastName(String str) {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile == null) {
            Log.d(TAG, "setBroadcastName: LE Audio Broadcast is null");
        } else {
            leAudioBroadcastProfile.setProgramInfo(str);
        }
    }

    /* access modifiers changed from: package-private */
    public String getBroadcastCode() {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile != null) {
            return new String(leAudioBroadcastProfile.getBroadcastCode(), StandardCharsets.UTF_8);
        }
        Log.d(TAG, "getBroadcastCode: LE Audio Broadcast is null");
        return "";
    }

    /* access modifiers changed from: package-private */
    public void setBroadcastCode(String str) {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile == null) {
            Log.d(TAG, "setBroadcastCode: LE Audio Broadcast is null");
        } else {
            leAudioBroadcastProfile.setBroadcastCode(str.getBytes(StandardCharsets.UTF_8));
        }
    }

    /* access modifiers changed from: package-private */
    public void setTemporaryAllowListExceptionIfNeeded(MediaDevice mediaDevice) {
        String str;
        PowerExemptionManager powerExemptionManager = this.mPowerExemptionManager;
        if (powerExemptionManager == null || (str = this.mPackageName) == null) {
            Log.w(TAG, "powerExemptionManager or package name is null");
        } else {
            powerExemptionManager.addToTemporaryAllowList(str, 325, ALLOWLIST_REASON, 20000);
        }
    }

    /* access modifiers changed from: package-private */
    public String getBroadcastMetadata() {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile == null) {
            Log.d(TAG, "getBroadcastMetadata: LE Audio Broadcast is null");
            return "";
        } else if (leAudioBroadcastProfile.getLatestBluetoothLeBroadcastMetadata() == null) {
            Log.d(TAG, "getBroadcastMetadata: LE Broadcast Metadata is null");
            return "";
        } else {
            LocalBluetoothLeBroadcastMetadata localBluetoothLeBroadcastMetaData = leAudioBroadcastProfile.getLocalBluetoothLeBroadcastMetaData();
            if (localBluetoothLeBroadcastMetaData != null) {
                return localBluetoothLeBroadcastMetaData.convertToQrCodeString();
            }
            return "";
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isActiveRemoteDevice(MediaDevice mediaDevice) {
        List<String> features = mediaDevice.getFeatures();
        return features.contains("android.media.route.feature.REMOTE_PLAYBACK") || features.contains("android.media.route.feature.REMOTE_AUDIO_PLAYBACK") || features.contains("android.media.route.feature.REMOTE_VIDEO_PLAYBACK") || features.contains("android.media.route.feature.REMOTE_GROUP_PLAYBACK");
    }

    /* access modifiers changed from: package-private */
    public boolean isBluetoothLeDevice(MediaDevice mediaDevice) {
        return mediaDevice.isBLEDevice();
    }

    /* access modifiers changed from: package-private */
    public boolean isBroadcastSupported() {
        return this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile() != null;
    }

    /* access modifiers changed from: package-private */
    public boolean isBluetoothLeBroadcastEnabled() {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile == null) {
            return false;
        }
        return leAudioBroadcastProfile.isEnabled((BluetoothDevice) null);
    }

    /* access modifiers changed from: package-private */
    public boolean startBluetoothLeBroadcast() {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile == null) {
            Log.d(TAG, "The broadcast profile is null");
            return false;
        }
        leAudioBroadcastProfile.startBroadcast(getAppSourceName(), (String) null);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean stopBluetoothLeBroadcast() {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile == null) {
            Log.d(TAG, "The broadcast profile is null");
            return false;
        }
        leAudioBroadcastProfile.stopLatestBroadcast();
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean updateBluetoothLeBroadcast() {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile == null) {
            Log.d(TAG, "The broadcast profile is null");
            return false;
        }
        leAudioBroadcastProfile.updateBroadcast(getAppSourceName(), (String) null);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void registerLeBroadcastServiceCallBack(Executor executor, BluetoothLeBroadcast.Callback callback) {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile == null) {
            Log.d(TAG, "The broadcast profile is null");
        } else {
            leAudioBroadcastProfile.registerServiceCallBack(executor, callback);
        }
    }

    /* access modifiers changed from: package-private */
    public void unregisterLeBroadcastServiceCallBack(BluetoothLeBroadcast.Callback callback) {
        LocalBluetoothLeBroadcast leAudioBroadcastProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioBroadcastProfile();
        if (leAudioBroadcastProfile == null) {
            Log.d(TAG, "The broadcast profile is null");
        } else {
            leAudioBroadcastProfile.unregisterServiceCallBack(callback);
        }
    }

    private boolean isPlayBackInfoLocal() {
        MediaController mediaController = this.mMediaController;
        return (mediaController == null || mediaController.getPlaybackInfo() == null || this.mMediaController.getPlaybackInfo().getPlaybackType() != 1) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public boolean isPlaying() {
        PlaybackState playbackState;
        MediaController mediaController = this.mMediaController;
        if (mediaController == null || (playbackState = mediaController.getPlaybackState()) == null || playbackState.getState() != 3) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isVolumeControlEnabled(MediaDevice mediaDevice) {
        return (isPlayBackInfoLocal() || mediaDevice.getDeviceType() != 7) && !mediaDevice.isVolumeFixed();
    }

    public void onDevicesUpdated(List<NearbyDevice> list) throws RemoteException {
        this.mNearbyDeviceInfoMap.clear();
        for (NearbyDevice next : list) {
            this.mNearbyDeviceInfoMap.put(next.getMediaRoute2Id(), Integer.valueOf(next.getRangeZone()));
        }
        this.mNearbyMediaDevicesManager.unregisterNearbyDevicesCallback(this);
    }
}
