package com.android.systemui.media.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.C1894R;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.dagger.SysUISingleton;

@SysUISingleton
public class MediaOutputDialog extends MediaOutputBaseDialog {
    final UiEventLogger mUiEventLogger;

    /* access modifiers changed from: package-private */
    public int getHeaderIconRes() {
        return 0;
    }

    MediaOutputDialog(Context context, boolean z, BroadcastSender broadcastSender, MediaOutputController mediaOutputController, UiEventLogger uiEventLogger) {
        super(context, broadcastSender, mediaOutputController);
        this.mUiEventLogger = uiEventLogger;
        this.mAdapter = new MediaOutputAdapter(this.mMediaOutputController, this);
        if (!z) {
            getWindow().setType(2038);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUiEventLogger.log(MediaOutputEvent.MEDIA_OUTPUT_DIALOG_SHOW);
    }

    /* access modifiers changed from: package-private */
    public IconCompat getHeaderIcon() {
        return this.mMediaOutputController.getHeaderIcon();
    }

    /* access modifiers changed from: package-private */
    public int getHeaderIconSize() {
        return this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.media_output_dialog_header_album_icon_size);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderText() {
        return this.mMediaOutputController.getHeaderTitle();
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderSubtitle() {
        return this.mMediaOutputController.getHeaderSubTitle();
    }

    /* access modifiers changed from: package-private */
    public Drawable getAppSourceIcon() {
        return this.mMediaOutputController.getAppSourceIcon();
    }

    /* access modifiers changed from: package-private */
    public int getStopButtonVisibility() {
        boolean isActiveRemoteDevice = this.mMediaOutputController.getCurrentConnectedMediaDevice() != null ? this.mMediaOutputController.isActiveRemoteDevice(this.mMediaOutputController.getCurrentConnectedMediaDevice()) : false;
        boolean z = isBroadcastSupported() && this.mMediaOutputController.isPlaying();
        if (isActiveRemoteDevice || z) {
            return 0;
        }
        return 8;
    }

    public boolean isBroadcastSupported() {
        boolean isBluetoothLeDevice = this.mMediaOutputController.getCurrentConnectedMediaDevice() != null ? this.mMediaOutputController.isBluetoothLeDevice(this.mMediaOutputController.getCurrentConnectedMediaDevice()) : false;
        if (!this.mMediaOutputController.isBroadcastSupported() || !isBluetoothLeDevice) {
            return false;
        }
        return true;
    }

    public CharSequence getStopButtonText() {
        return this.mContext.getText((!isBroadcastSupported() || !this.mMediaOutputController.isPlaying() || this.mMediaOutputController.isBluetoothLeBroadcastEnabled()) ? C1894R.string.media_output_dialog_button_stop_casting : C1894R.string.media_output_broadcast);
    }

    public void onStopButtonClick() {
        if (!isBroadcastSupported() || !this.mMediaOutputController.isPlaying()) {
            this.mMediaOutputController.releaseSession();
            dismiss();
        } else if (this.mMediaOutputController.isBluetoothLeBroadcastEnabled()) {
            stopLeBroadcast();
        } else if (!startLeBroadcastDialogForFirstTime()) {
            startLeBroadcast();
        }
    }

    public int getBroadcastIconVisibility() {
        return (!isBroadcastSupported() || !this.mMediaOutputController.isBluetoothLeBroadcastEnabled()) ? 8 : 0;
    }

    public void onBroadcastIconClick() {
        startLeBroadcastDialog();
    }

    public enum MediaOutputEvent implements UiEventLogger.UiEventEnum {
        MEDIA_OUTPUT_DIALOG_SHOW(655);
        
        private final int mId;

        private MediaOutputEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
