package com.android.systemui.screenshot;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.ScreenshotHelper;
import com.android.systemui.R$string;
import com.android.systemui.shared.recents.utilities.BitmapUtil;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class TakeScreenshotService extends Service {
    private static final String TAG = LogConfig.logTag(TakeScreenshotService.class);
    private final BroadcastReceiver mCloseSystemDialogs = new BroadcastReceiver() { // from class: com.android.systemui.screenshot.TakeScreenshotService.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!"android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction()) || TakeScreenshotService.this.mScreenshot == null || TakeScreenshotService.this.mScreenshot.isPendingSharedTransition()) {
                return;
            }
            TakeScreenshotService.this.mScreenshot.dismissScreenshot(false);
        }
    };
    private final Handler mHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() { // from class: com.android.systemui.screenshot.TakeScreenshotService$$ExternalSyntheticLambda0
        @Override // android.os.Handler.Callback
        public final boolean handleMessage(Message message) {
            boolean handleMessage;
            handleMessage = TakeScreenshotService.this.handleMessage(message);
            return handleMessage;
        }
    });
    private final ScreenshotNotificationsController mNotificationsController;
    private ScreenshotController mScreenshot;
    private final UiEventLogger mUiEventLogger;
    private final UserManager mUserManager;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface RequestCallback {
        void onFinish();

        void reportError();
    }

    @Override // android.app.Service
    public void onCreate() {
    }

    public TakeScreenshotService(ScreenshotController screenshotController, UserManager userManager, UiEventLogger uiEventLogger, ScreenshotNotificationsController screenshotNotificationsController) {
        this.mScreenshot = screenshotController;
        this.mUserManager = userManager;
        this.mUiEventLogger = uiEventLogger;
        this.mNotificationsController = screenshotNotificationsController;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        registerReceiver(this.mCloseSystemDialogs, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        return new Messenger(this.mHandler).getBinder();
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        ScreenshotController screenshotController = this.mScreenshot;
        if (screenshotController != null) {
            screenshotController.removeWindow();
            this.mScreenshot = null;
        }
        unregisterReceiver(this.mCloseSystemDialogs);
        return false;
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        ScreenshotController screenshotController = this.mScreenshot;
        if (screenshotController != null) {
            screenshotController.removeWindow();
            this.mScreenshot.releaseContext();
            this.mScreenshot = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class RequestCallbackImpl implements RequestCallback {
        private final Messenger mReplyTo;

        RequestCallbackImpl(Messenger messenger) {
            this.mReplyTo = messenger;
        }

        @Override // com.android.systemui.screenshot.TakeScreenshotService.RequestCallback
        public void reportError() {
            TakeScreenshotService.reportUri(this.mReplyTo, null);
            TakeScreenshotService.sendComplete(this.mReplyTo);
        }

        @Override // com.android.systemui.screenshot.TakeScreenshotService.RequestCallback
        public void onFinish() {
            TakeScreenshotService.sendComplete(this.mReplyTo);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleMessage(Message message) {
        final Messenger messenger = message.replyTo;
        Consumer<Uri> consumer = new Consumer() { // from class: com.android.systemui.screenshot.TakeScreenshotService$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TakeScreenshotService.reportUri(messenger, (Uri) obj);
            }
        };
        RequestCallbackImpl requestCallbackImpl = new RequestCallbackImpl(messenger);
        if (!this.mUserManager.isUserUnlocked()) {
            Log.w(TAG, "Skipping screenshot because storage is locked!");
            this.mNotificationsController.notifyScreenshotError(R$string.screenshot_failed_to_save_user_locked_text);
            requestCallbackImpl.reportError();
            return true;
        }
        ScreenshotHelper.ScreenshotRequest screenshotRequest = (ScreenshotHelper.ScreenshotRequest) message.obj;
        this.mUiEventLogger.log(ScreenshotEvent.getScreenshotSource(screenshotRequest.getSource()));
        int i = message.what;
        if (i == 1) {
            this.mScreenshot.takeScreenshotFullscreen(consumer, requestCallbackImpl);
        } else if (i == 2) {
            this.mScreenshot.takeScreenshotPartial(consumer, requestCallbackImpl);
        } else if (i == 3) {
            Bitmap bundleToHardwareBitmap = BitmapUtil.bundleToHardwareBitmap(screenshotRequest.getBitmapBundle());
            Rect boundsInScreen = screenshotRequest.getBoundsInScreen();
            Insets insets = screenshotRequest.getInsets();
            int taskId = screenshotRequest.getTaskId();
            int userId = screenshotRequest.getUserId();
            ComponentName topComponent = screenshotRequest.getTopComponent();
            if (bundleToHardwareBitmap == null) {
                Log.e(TAG, "Got null bitmap from screenshot message");
                this.mNotificationsController.notifyScreenshotError(R$string.screenshot_failed_to_capture_text);
                requestCallbackImpl.reportError();
            } else {
                this.mScreenshot.handleImageAsScreenshot(bundleToHardwareBitmap, boundsInScreen, insets, taskId, userId, topComponent, consumer, requestCallbackImpl);
            }
        } else {
            String str = TAG;
            Log.w(str, "Invalid screenshot option: " + message.what);
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void sendComplete(Messenger messenger) {
        try {
            messenger.send(Message.obtain((Handler) null, 2));
        } catch (RemoteException e) {
            Log.d(TAG, "ignored remote exception", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void reportUri(Messenger messenger, Uri uri) {
        try {
            messenger.send(Message.obtain(null, 1, uri));
        } catch (RemoteException e) {
            Log.d(TAG, "ignored remote exception", e);
        }
    }
}
