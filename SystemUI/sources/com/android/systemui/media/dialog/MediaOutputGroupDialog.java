package com.android.systemui.media.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastSender;

public class MediaOutputGroupDialog extends MediaOutputBaseDialog {
    /* access modifiers changed from: package-private */
    public Drawable getAppSourceIcon() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public IconCompat getHeaderIcon() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public int getHeaderIconRes() {
        return C1893R.C1895drawable.ic_arrow_back;
    }

    /* access modifiers changed from: package-private */
    public int getStopButtonVisibility() {
        return 0;
    }

    MediaOutputGroupDialog(Context context, boolean z, BroadcastSender broadcastSender, MediaOutputController mediaOutputController) {
        super(context, broadcastSender, mediaOutputController);
        this.mMediaOutputController.resetGroupMediaDevices();
        this.mAdapter = new MediaOutputGroupAdapter(this.mMediaOutputController);
        if (!z) {
            getWindow().setType(2038);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /* access modifiers changed from: package-private */
    public int getHeaderIconSize() {
        return this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.media_output_dialog_header_back_icon_size);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderText() {
        return this.mContext.getString(C1893R.string.media_output_dialog_add_output);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getHeaderSubtitle() {
        int size = this.mMediaOutputController.getSelectedMediaDevice().size();
        if (size == 1) {
            return this.mContext.getText(C1893R.string.media_output_dialog_single_device);
        }
        return this.mContext.getString(C1893R.string.media_output_dialog_multiple_devices, new Object[]{Integer.valueOf(size)});
    }

    /* access modifiers changed from: package-private */
    public void onHeaderIconClick() {
        dismiss();
    }
}
