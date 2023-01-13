package com.android.systemui.screenrecord;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.qualifiers.LongRunning;
import com.android.systemui.screenrecord.ScreenMediaRecorder;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.IOException;
import java.util.concurrent.Executor;
import javax.inject.Inject;

public class RecordingService extends Service implements MediaRecorder.OnInfoListener {
    private static final String ACTION_SHARE = "com.android.systemui.screenrecord.SHARE";
    private static final String ACTION_START = "com.android.systemui.screenrecord.START";
    private static final String ACTION_STOP = "com.android.systemui.screenrecord.STOP";
    private static final String ACTION_STOP_NOTIF = "com.android.systemui.screenrecord.STOP_FROM_NOTIF";
    private static final String CHANNEL_ID = "screen_record";
    private static final String EXTRA_AUDIO_SOURCE = "extra_useAudio";
    private static final String EXTRA_PATH = "extra_path";
    private static final String EXTRA_RESULT_CODE = "extra_resultCode";
    private static final String EXTRA_SHOW_TAPS = "extra_showTaps";
    private static final int NOTIFICATION_PROCESSING_ID = 4275;
    private static final int NOTIFICATION_RECORDING_ID = 4274;
    private static final int NOTIFICATION_VIEW_ID = 4273;
    private static final String PERMISSION_SELF = "com.android.systemui.permission.SELF";
    public static final int REQUEST_CODE = 2;
    private static final String TAG = "RecordingService";
    private ScreenRecordingAudioSource mAudioSource;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final RecordingController mController;
    private final KeyguardDismissUtil mKeyguardDismissUtil;
    private final Executor mLongExecutor;
    private final NotificationManager mNotificationManager;
    /* access modifiers changed from: private */
    public boolean mOriginalShowTaps;
    private ScreenMediaRecorder mRecorder;
    private boolean mShowTaps;
    private final BroadcastReceiver mShutdownBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            NTLogUtil.m1686d(RecordingService.TAG, "recover show taps:" + RecordingService.this.mOriginalShowTaps);
            RecordingService recordingService = RecordingService.this;
            recordingService.setTapsVisible(recordingService.mOriginalShowTaps);
        }
    };
    private final UiEventLogger mUiEventLogger;
    private final UserContextProvider mUserContextTracker;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Inject
    public RecordingService(RecordingController recordingController, @LongRunning Executor executor, UiEventLogger uiEventLogger, NotificationManager notificationManager, UserContextProvider userContextProvider, KeyguardDismissUtil keyguardDismissUtil) {
        this.mController = recordingController;
        this.mLongExecutor = executor;
        this.mUiEventLogger = uiEventLogger;
        this.mNotificationManager = notificationManager;
        this.mUserContextTracker = userContextProvider;
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mBroadcastDispatcher = (BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class);
    }

    public static Intent getStartIntent(Context context, int i, int i2, boolean z) {
        return new Intent(context, RecordingService.class).setAction(ACTION_START).putExtra(EXTRA_RESULT_CODE, i).putExtra(EXTRA_AUDIO_SOURCE, i2).putExtra(EXTRA_SHOW_TAPS, z);
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int onStartCommand(android.content.Intent r9, int r10, int r11) {
        /*
            r8 = this;
            r10 = 2
            if (r9 != 0) goto L_0x0004
            return r10
        L_0x0004:
            java.lang.String r11 = r9.getAction()
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "onStartCommand "
            r0.<init>((java.lang.String) r1)
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r11)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "RecordingService"
            android.util.Log.d(r1, r0)
            com.android.systemui.settings.UserContextProvider r0 = r8.mUserContextTracker
            android.content.Context r0 = r0.getUserContext()
            int r0 = r0.getUserId()
            android.os.UserHandle r2 = new android.os.UserHandle
            r2.<init>(r0)
            r11.hashCode()
            int r3 = r11.hashCode()
            java.lang.String r4 = "com.android.systemui.screenrecord.STOP_FROM_NOTIF"
            r5 = -1
            r6 = 1
            r7 = 0
            switch(r3) {
                case -1688140755: goto L_0x005c;
                case -1687783248: goto L_0x0051;
                case -470086188: goto L_0x0046;
                case -288359034: goto L_0x003d;
                default: goto L_0x003b;
            }
        L_0x003b:
            r3 = r5
            goto L_0x0066
        L_0x003d:
            boolean r3 = r11.equals(r4)
            if (r3 != 0) goto L_0x0044
            goto L_0x003b
        L_0x0044:
            r3 = 3
            goto L_0x0066
        L_0x0046:
            java.lang.String r3 = "com.android.systemui.screenrecord.STOP"
            boolean r3 = r11.equals(r3)
            if (r3 != 0) goto L_0x004f
            goto L_0x003b
        L_0x004f:
            r3 = r10
            goto L_0x0066
        L_0x0051:
            java.lang.String r3 = "com.android.systemui.screenrecord.START"
            boolean r3 = r11.equals(r3)
            if (r3 != 0) goto L_0x005a
            goto L_0x003b
        L_0x005a:
            r3 = r6
            goto L_0x0066
        L_0x005c:
            java.lang.String r3 = "com.android.systemui.screenrecord.SHARE"
            boolean r3 = r11.equals(r3)
            if (r3 != 0) goto L_0x0065
            goto L_0x003b
        L_0x0065:
            r3 = r7
        L_0x0066:
            switch(r3) {
                case 0: goto L_0x012a;
                case 1: goto L_0x00b4;
                case 2: goto L_0x006b;
                case 3: goto L_0x006b;
                default: goto L_0x0069;
            }
        L_0x0069:
            goto L_0x015c
        L_0x006b:
            boolean r10 = r4.equals(r11)
            if (r10 == 0) goto L_0x0079
            com.android.internal.logging.UiEventLogger r10 = r8.mUiEventLogger
            com.android.systemui.screenrecord.Events$ScreenRecordEvent r11 = com.android.systemui.screenrecord.Events.ScreenRecordEvent.SCREEN_RECORD_END_NOTIFICATION
            r10.log(r11)
            goto L_0x0080
        L_0x0079:
            com.android.internal.logging.UiEventLogger r10 = r8.mUiEventLogger
            com.android.systemui.screenrecord.Events$ScreenRecordEvent r11 = com.android.systemui.screenrecord.Events.ScreenRecordEvent.SCREEN_RECORD_END_QS_TILE
            r10.log(r11)
        L_0x0080:
            java.lang.String r10 = "android.intent.extra.user_handle"
            int r9 = r9.getIntExtra(r10, r5)
            if (r9 != r5) goto L_0x0092
            com.android.systemui.settings.UserContextProvider r9 = r8.mUserContextTracker
            android.content.Context r9 = r9.getUserContext()
            int r9 = r9.getUserId()
        L_0x0092:
            java.lang.StringBuilder r10 = new java.lang.StringBuilder
            java.lang.String r11 = "notifying for user "
            r10.<init>((java.lang.String) r11)
            java.lang.StringBuilder r10 = r10.append((int) r9)
            java.lang.String r10 = r10.toString()
            android.util.Log.d(r1, r10)
            r8.stopRecording(r9)
            android.app.NotificationManager r9 = r8.mNotificationManager
            r10 = 4274(0x10b2, float:5.989E-42)
            r9.cancel(r10)
            r8.stopSelf()
            goto L_0x015c
        L_0x00b4:
            com.android.systemui.screenrecord.ScreenRecordingAudioSource[] r11 = com.android.systemui.screenrecord.ScreenRecordingAudioSource.values()
            java.lang.String r2 = "extra_useAudio"
            int r2 = r9.getIntExtra(r2, r7)
            r11 = r11[r2]
            r8.mAudioSource = r11
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r2 = "recording with audio source"
            r11.<init>((java.lang.String) r2)
            com.android.systemui.screenrecord.ScreenRecordingAudioSource r2 = r8.mAudioSource
            java.lang.StringBuilder r11 = r11.append((java.lang.Object) r2)
            java.lang.String r11 = r11.toString()
            android.util.Log.d(r1, r11)
            java.lang.String r11 = "extra_showTaps"
            boolean r9 = r9.getBooleanExtra(r11, r7)
            r8.mShowTaps = r9
            android.content.Context r9 = r8.getApplicationContext()
            android.content.ContentResolver r9 = r9.getContentResolver()
            java.lang.String r11 = "show_touches"
            int r9 = android.provider.Settings.System.getInt(r9, r11, r7)
            if (r9 == 0) goto L_0x00f2
            r9 = r6
            goto L_0x00f3
        L_0x00f2:
            r9 = r7
        L_0x00f3:
            r8.mOriginalShowTaps = r9
            boolean r9 = r8.mShowTaps
            r8.setTapsVisible(r9)
            com.android.systemui.screenrecord.ScreenMediaRecorder r9 = new com.android.systemui.screenrecord.ScreenMediaRecorder
            com.android.systemui.settings.UserContextProvider r11 = r8.mUserContextTracker
            android.content.Context r11 = r11.getUserContext()
            com.android.systemui.screenrecord.ScreenRecordingAudioSource r1 = r8.mAudioSource
            r9.<init>(r11, r0, r1, r8)
            r8.mRecorder = r9
            boolean r9 = r8.startRecording()
            if (r9 == 0) goto L_0x011d
            r8.updateState(r6)
            r8.createRecordingNotification()
            com.android.internal.logging.UiEventLogger r8 = r8.mUiEventLogger
            com.android.systemui.screenrecord.Events$ScreenRecordEvent r9 = com.android.systemui.screenrecord.Events.ScreenRecordEvent.SCREEN_RECORD_START
            r8.log(r9)
            goto L_0x015c
        L_0x011d:
            r8.updateState(r7)
            r8.createErrorNotification()
            r8.stopForeground(r6)
            r8.stopSelf()
            return r10
        L_0x012a:
            java.lang.String r10 = "extra_path"
            java.lang.String r9 = r9.getStringExtra(r10)
            android.net.Uri r9 = android.net.Uri.parse(r9)
            android.content.Intent r10 = new android.content.Intent
            java.lang.String r11 = "android.intent.action.SEND"
            r10.<init>(r11)
            java.lang.String r11 = "video/mp4"
            android.content.Intent r10 = r10.setType(r11)
            java.lang.String r11 = "android.intent.extra.STREAM"
            android.content.Intent r9 = r10.putExtra(r11, r9)
            com.android.systemui.statusbar.phone.KeyguardDismissUtil r10 = r8.mKeyguardDismissUtil
            com.android.systemui.screenrecord.RecordingService$$ExternalSyntheticLambda0 r11 = new com.android.systemui.screenrecord.RecordingService$$ExternalSyntheticLambda0
            r11.<init>(r8, r9, r2)
            r10.executeWhenUnlocked(r11, r7, r7)
            android.content.Intent r9 = new android.content.Intent
            java.lang.String r10 = "android.intent.action.CLOSE_SYSTEM_DIALOGS"
            r9.<init>(r10)
            r8.sendBroadcast(r9)
        L_0x015c:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.screenrecord.RecordingService.onStartCommand(android.content.Intent, int, int):int");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onStartCommand$0$com-android-systemui-screenrecord-RecordingService */
    public /* synthetic */ boolean mo37255x29197d07(Intent intent, UserHandle userHandle) {
        startActivity(Intent.createChooser(intent, getResources().getString(C1894R.string.screenrecord_share_label)).setFlags(268435456));
        this.mNotificationManager.cancelAsUser((String) null, NOTIFICATION_VIEW_ID, userHandle);
        return false;
    }

    public void onCreate() {
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        this.mBroadcastDispatcher.registerReceiver(this.mShutdownBroadcastReceiver, intentFilter);
    }

    /* access modifiers changed from: protected */
    public ScreenMediaRecorder getRecorder() {
        return this.mRecorder;
    }

    private void updateState(boolean z) {
        if (this.mUserContextTracker.getUserContext().getUserId() == 0) {
            this.mController.updateState(z);
            return;
        }
        Intent intent = new Intent("com.android.systemui.screenrecord.UPDATE_STATE");
        intent.putExtra("extra_state", z);
        intent.addFlags(1073741824);
        sendBroadcast(intent, "com.android.systemui.permission.SELF");
    }

    private boolean startRecording() {
        try {
            getRecorder().start();
            return true;
        } catch (RemoteException | IOException | RuntimeException e) {
            showErrorToast(C1894R.string.screenrecord_start_error);
            e.printStackTrace();
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void createErrorNotification() {
        Resources resources = getResources();
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, getString(C1894R.string.screenrecord_name), 3);
        notificationChannel.setDescription(getString(C1894R.string.screenrecord_channel_description));
        notificationChannel.enableVibration(true);
        this.mNotificationManager.createNotificationChannel(notificationChannel);
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", resources.getString(C1894R.string.screenrecord_name));
        startForeground(NOTIFICATION_RECORDING_ID, new Notification.Builder(this, CHANNEL_ID).setSmallIcon(C1894R.C1896drawable.ic_screenrecord).setContentTitle(resources.getString(C1894R.string.screenrecord_start_error)).addExtras(bundle).build());
    }

    /* access modifiers changed from: protected */
    public void showErrorToast(int i) {
        Toast.makeText(this, i, 1).show();
    }

    /* access modifiers changed from: protected */
    public void createRecordingNotification() {
        String str;
        Resources resources = getResources();
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, getString(C1894R.string.screenrecord_name), 3);
        notificationChannel.setDescription(getString(C1894R.string.screenrecord_channel_description));
        notificationChannel.enableVibration(true);
        this.mNotificationManager.createNotificationChannel(notificationChannel);
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", resources.getString(C1894R.string.screenrecord_name));
        if (this.mAudioSource == ScreenRecordingAudioSource.NONE) {
            str = resources.getString(C1894R.string.screenrecord_ongoing_screen_only);
        } else {
            str = resources.getString(C1894R.string.screenrecord_ongoing_screen_and_audio);
        }
        startForeground(NOTIFICATION_RECORDING_ID, new Notification.Builder(this, CHANNEL_ID).setSmallIcon(C1894R.C1896drawable.ic_screenrecord).setContentTitle(str).setUsesChronometer(true).setColorized(true).setColor(getResources().getColor(C1894R.C1895color.GM2_red_700)).setOngoing(true).setForegroundServiceBehavior(1).addAction(new Notification.Action.Builder(Icon.createWithResource(this, C1894R.C1896drawable.ic_android), getResources().getString(C1894R.string.screenrecord_stop_label), PendingIntent.getService(this, 2, getNotificationIntent(this), 201326592)).build()).addExtras(bundle).build());
    }

    /* access modifiers changed from: protected */
    public Notification createProcessingNotification() {
        String str;
        Resources resources = getApplicationContext().getResources();
        if (this.mAudioSource == ScreenRecordingAudioSource.NONE) {
            str = resources.getString(C1894R.string.screenrecord_ongoing_screen_only);
        } else {
            str = resources.getString(C1894R.string.screenrecord_ongoing_screen_and_audio);
        }
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", resources.getString(C1894R.string.screenrecord_name));
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID).setContentTitle(str).setContentText(getResources().getString(C1894R.string.screenrecord_background_processing_label)).setSmallIcon(C1894R.C1896drawable.ic_screenrecord).addExtras(bundle).build();
    }

    /* access modifiers changed from: protected */
    public Notification createSaveNotification(ScreenMediaRecorder.SavedRecording savedRecording) {
        Uri uri = savedRecording.getUri();
        Intent dataAndType = new Intent("android.intent.action.VIEW").setFlags(268435457).setDataAndType(uri, "video/mp4");
        Notification.Action build = new Notification.Action.Builder(Icon.createWithResource(this, C1894R.C1896drawable.ic_screenrecord), getResources().getString(C1894R.string.screenrecord_share_label), PendingIntent.getService(this, 2, getShareIntent(this, uri.toString()), 201326592)).build();
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", getResources().getString(C1894R.string.screenrecord_name));
        Notification.Builder addExtras = new Notification.Builder(this, CHANNEL_ID).setSmallIcon(C1894R.C1896drawable.ic_screenrecord).setContentTitle(getResources().getString(C1894R.string.screenrecord_save_title)).setContentText(getResources().getString(C1894R.string.screenrecord_save_text)).setContentIntent(PendingIntent.getActivity(this, 2, dataAndType, 67108864)).addAction(build).setAutoCancel(true).addExtras(bundle);
        Bitmap thumbnail = savedRecording.getThumbnail();
        if (thumbnail != null) {
            addExtras.setStyle(new Notification.BigPictureStyle().bigPicture(thumbnail).showBigPictureWhenCollapsed(true));
        }
        return addExtras.build();
    }

    private void stopRecording(int i) {
        setTapsVisible(this.mOriginalShowTaps);
        if (getRecorder() != null) {
            getRecorder().end();
            saveRecording(i);
        } else {
            Log.e(TAG, "stopRecording called, but recorder was null");
        }
        updateState(false);
    }

    private void saveRecording(int i) {
        UserHandle userHandle = new UserHandle(i);
        this.mNotificationManager.notifyAsUser((String) null, NOTIFICATION_PROCESSING_ID, createProcessingNotification(), userHandle);
        this.mLongExecutor.execute(new RecordingService$$ExternalSyntheticLambda1(this, userHandle));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$saveRecording$1$com-android-systemui-screenrecord-RecordingService */
    public /* synthetic */ void mo37256xdb858a54(UserHandle userHandle) {
        try {
            Log.d(TAG, "saving recording");
            Notification createSaveNotification = createSaveNotification(getRecorder().save());
            if (!this.mController.isRecording()) {
                this.mNotificationManager.notifyAsUser((String) null, NOTIFICATION_VIEW_ID, createSaveNotification, userHandle);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error saving screen recording: " + e.getMessage());
            showErrorToast(C1894R.string.screenrecord_delete_error);
        } catch (Throwable th) {
            this.mNotificationManager.cancelAsUser((String) null, NOTIFICATION_PROCESSING_ID, userHandle);
            throw th;
        }
        this.mNotificationManager.cancelAsUser((String) null, NOTIFICATION_PROCESSING_ID, userHandle);
    }

    /* access modifiers changed from: private */
    public void setTapsVisible(boolean z) {
        Settings.System.putInt(getContentResolver(), "show_touches", z ? 1 : 0);
    }

    public static Intent getStopIntent(Context context) {
        return new Intent(context, RecordingService.class).setAction(ACTION_STOP).putExtra("android.intent.extra.user_handle", context.getUserId());
    }

    protected static Intent getNotificationIntent(Context context) {
        return new Intent(context, RecordingService.class).setAction(ACTION_STOP_NOTIF);
    }

    private static Intent getShareIntent(Context context, String str) {
        return new Intent(context, RecordingService.class).setAction(ACTION_SHARE).putExtra(EXTRA_PATH, str);
    }

    public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
        Log.d(TAG, "Media recorder info: " + i);
        onStartCommand(getStopIntent(this), 0, 0);
    }

    public void onDestroy() {
        NTLogUtil.m1686d(TAG, "onDestroy:");
        this.mBroadcastDispatcher.unregisterReceiver(this.mShutdownBroadcastReceiver);
    }
}
