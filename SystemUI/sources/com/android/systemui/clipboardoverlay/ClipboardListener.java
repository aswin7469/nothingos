package com.android.systemui.clipboardoverlay;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.util.DeviceConfigProxy;
import javax.inject.Inject;

@SysUISingleton
public class ClipboardListener extends CoreStartable implements ClipboardManager.OnPrimaryClipChangedListener {
    static final String EXTRA_SUPPRESS_OVERLAY = "com.android.systemui.SUPPRESS_CLIPBOARD_OVERLAY";
    static final String SHELL_PACKAGE = "com.android.shell";
    private static final String TAG = "ClipboardListener";
    private final ClipboardManager mClipboardManager;
    private ClipboardOverlayController mClipboardOverlayController;
    private final DeviceConfigProxy mDeviceConfig;
    private final ClipboardOverlayControllerFactory mOverlayFactory;
    private final UiEventLogger mUiEventLogger;

    @Inject
    public ClipboardListener(Context context, DeviceConfigProxy deviceConfigProxy, ClipboardOverlayControllerFactory clipboardOverlayControllerFactory, ClipboardManager clipboardManager, UiEventLogger uiEventLogger) {
        super(context);
        this.mDeviceConfig = deviceConfigProxy;
        this.mOverlayFactory = clipboardOverlayControllerFactory;
        this.mClipboardManager = clipboardManager;
        this.mUiEventLogger = uiEventLogger;
    }

    public void start() {
        if (this.mDeviceConfig.getBoolean("systemui", "clipboard_overlay_enabled", true)) {
            this.mClipboardManager.addPrimaryClipChangedListener(this);
        }
    }

    public void onPrimaryClipChanged() {
        if (this.mClipboardManager.hasPrimaryClip()) {
            String primaryClipSource = this.mClipboardManager.getPrimaryClipSource();
            ClipData primaryClip = this.mClipboardManager.getPrimaryClip();
            if (shouldSuppressOverlay(primaryClip, primaryClipSource, isEmulator())) {
                Log.i(TAG, "Clipboard overlay suppressed.");
                return;
            }
            if (this.mClipboardOverlayController == null) {
                this.mClipboardOverlayController = this.mOverlayFactory.create(this.mContext);
                this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_ENTERED, 0, primaryClipSource);
            } else {
                this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_UPDATED, 0, primaryClipSource);
            }
            this.mClipboardOverlayController.setClipData(primaryClip, primaryClipSource);
            this.mClipboardOverlayController.setOnSessionCompleteListener(new ClipboardListener$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onPrimaryClipChanged$0$com-android-systemui-clipboardoverlay-ClipboardListener */
    public /* synthetic */ void mo31290xc6b2a8dd() {
        this.mClipboardOverlayController = null;
    }

    static boolean shouldSuppressOverlay(ClipData clipData, String str, boolean z) {
        if ((!z && !SHELL_PACKAGE.equals(str)) || clipData == null || clipData.getDescription().getExtras() == null) {
            return false;
        }
        return clipData.getDescription().getExtras().getBoolean(EXTRA_SUPPRESS_OVERLAY, false);
    }

    private static boolean isEmulator() {
        return SystemProperties.getBoolean("ro.boot.qemu", false);
    }
}
