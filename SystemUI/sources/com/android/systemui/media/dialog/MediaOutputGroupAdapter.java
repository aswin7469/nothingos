package com.android.systemui.media.dialog;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.C1893R;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;
import java.util.List;

public class MediaOutputGroupAdapter extends MediaOutputBaseAdapter {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "MediaOutputGroupAdapter";
    private final List<MediaDevice> mGroupMediaDevices;

    public MediaOutputGroupAdapter(MediaOutputController mediaOutputController) {
        super(mediaOutputController);
        this.mGroupMediaDevices = mediaOutputController.getGroupMediaDevices();
    }

    public MediaOutputBaseAdapter.MediaDeviceBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        super.onCreateViewHolder(viewGroup, i);
        return new GroupViewHolder(this.mHolderView);
    }

    public void onBindViewHolder(MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder, int i) {
        boolean z = true;
        if (i == 0) {
            mediaDeviceBaseViewHolder.onBind(2, true, false);
            return;
        }
        int i2 = i - 1;
        int size = this.mGroupMediaDevices.size();
        if (i2 < size) {
            MediaDevice mediaDevice = this.mGroupMediaDevices.get(i2);
            if (i2 != size - 1) {
                z = false;
            }
            mediaDeviceBaseViewHolder.onBind(mediaDevice, false, z, i);
        } else if (DEBUG) {
            Log.d(TAG, "Incorrect position: " + i);
        }
    }

    public int getItemCount() {
        return this.mGroupMediaDevices.size() + 1;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getItemTitle(MediaDevice mediaDevice) {
        return super.getItemTitle(mediaDevice);
    }

    class GroupViewHolder extends MediaOutputBaseAdapter.MediaDeviceBaseViewHolder {
        GroupViewHolder(View view) {
            super(view);
        }

        /* access modifiers changed from: package-private */
        public void onBind(MediaDevice mediaDevice, boolean z, boolean z2, int i) {
            super.onBind(mediaDevice, z, z2, i);
            this.mCheckBox.setVisibility(0);
            this.mCheckBox.setOnCheckedChangeListener(new C2229x1869e5b0(this, mediaDevice));
            boolean z3 = this.mSeekBar.getVisibility() == 8;
            setTwoLineLayout(mediaDevice, false, true, false, false);
            initSeekbar(mediaDevice, z3);
            List<MediaDevice> selectedMediaDevice = MediaOutputGroupAdapter.this.mController.getSelectedMediaDevice();
            if (isDeviceIncluded(MediaOutputGroupAdapter.this.mController.getSelectableMediaDevice(), mediaDevice)) {
                this.mCheckBox.setButtonDrawable(C1893R.C1895drawable.ic_check_box);
                this.mCheckBox.setChecked(false);
                this.mCheckBox.setEnabled(true);
            } else if (!isDeviceIncluded(selectedMediaDevice, mediaDevice)) {
            } else {
                if (selectedMediaDevice.size() == 1 || !isDeviceIncluded(MediaOutputGroupAdapter.this.mController.getDeselectableMediaDevice(), mediaDevice)) {
                    this.mCheckBox.setButtonDrawable(getDisabledCheckboxDrawable());
                    this.mCheckBox.setChecked(true);
                    this.mCheckBox.setEnabled(false);
                    return;
                }
                this.mCheckBox.setButtonDrawable(C1893R.C1895drawable.ic_check_box);
                this.mCheckBox.setChecked(true);
                this.mCheckBox.setEnabled(true);
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBind$0$com-android-systemui-media-dialog-MediaOutputGroupAdapter$GroupViewHolder */
        public /* synthetic */ void mo34463x27986ca6(MediaDevice mediaDevice, CompoundButton compoundButton, boolean z) {
            onCheckBoxClicked(z, mediaDevice);
        }

        /* access modifiers changed from: package-private */
        public void onBind(int i, boolean z, boolean z2) {
            if (i == 2) {
                setTwoLineLayout(MediaOutputGroupAdapter.this.mContext.getText(C1893R.string.media_output_dialog_group), true, true, false, false);
                this.mTitleIcon.setImageDrawable(getSpeakerDrawable());
                this.mCheckBox.setVisibility(8);
                initSessionSeekbar();
            }
        }

        private void onCheckBoxClicked(boolean z, MediaDevice mediaDevice) {
            if (z && isDeviceIncluded(MediaOutputGroupAdapter.this.mController.getSelectableMediaDevice(), mediaDevice)) {
                MediaOutputGroupAdapter.this.mController.addDeviceToPlayMedia(mediaDevice);
            } else if (!z && isDeviceIncluded(MediaOutputGroupAdapter.this.mController.getDeselectableMediaDevice(), mediaDevice)) {
                MediaOutputGroupAdapter.this.mController.removeDeviceFromPlayMedia(mediaDevice);
            }
        }

        private Drawable getDisabledCheckboxDrawable() {
            Drawable mutate = MediaOutputGroupAdapter.this.mContext.getDrawable(C1893R.C1895drawable.ic_check_box_blue_24dp).mutate();
            Canvas canvas = new Canvas(Bitmap.createBitmap(mutate.getIntrinsicWidth(), mutate.getIntrinsicHeight(), Bitmap.Config.ARGB_8888));
            TypedValue typedValue = new TypedValue();
            MediaOutputGroupAdapter.this.mContext.getTheme().resolveAttribute(16842803, typedValue, true);
            mutate.setAlpha((int) (typedValue.getFloat() * 255.0f));
            mutate.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            mutate.draw(canvas);
            return mutate;
        }

        private boolean isDeviceIncluded(List<MediaDevice> list, MediaDevice mediaDevice) {
            for (MediaDevice id : list) {
                if (TextUtils.equals(id.getId(), mediaDevice.getId())) {
                    return true;
                }
            }
            return false;
        }
    }
}
