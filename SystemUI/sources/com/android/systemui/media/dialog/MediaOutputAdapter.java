package com.android.systemui.media.dialog;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import androidx.core.widget.CompoundButtonCompat;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.C1894R;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;
import java.util.List;
import java.util.Objects;

public class MediaOutputAdapter extends MediaOutputBaseAdapter {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "MediaOutputAdapter";
    /* access modifiers changed from: private */
    public ViewGroup mConnectedItem;
    /* access modifiers changed from: private */
    public boolean mIncludeDynamicGroup;
    private final MediaOutputDialog mMediaOutputDialog;

    public MediaOutputAdapter(MediaOutputController mediaOutputController, MediaOutputDialog mediaOutputDialog) {
        super(mediaOutputController);
        this.mMediaOutputDialog = mediaOutputDialog;
        setHasStableIds(true);
    }

    public MediaOutputBaseAdapter.MediaDeviceBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        super.onCreateViewHolder(viewGroup, i);
        return new MediaDeviceViewHolder(this.mHolderView);
    }

    public void onBindViewHolder(MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder, int i) {
        int size = this.mController.getMediaDevices().size();
        boolean z = false;
        if (i == size && this.mController.isZeroMode()) {
            mediaDeviceBaseViewHolder.onBind(1, false, true);
        } else if (i < size) {
            MediaDevice mediaDevice = (MediaDevice) ((List) this.mController.getMediaDevices()).get(i);
            boolean z2 = i == 0;
            if (i == size - 1) {
                z = true;
            }
            mediaDeviceBaseViewHolder.onBind(mediaDevice, z2, z, i);
        } else if (DEBUG) {
            Log.d(TAG, "Incorrect position: " + i);
        }
    }

    public long getItemId(int i) {
        int size = this.mController.getMediaDevices().size();
        if (i == size && this.mController.isZeroMode()) {
            return -1;
        }
        if (i < size) {
            return (long) ((MediaDevice) ((List) this.mController.getMediaDevices()).get(i)).getId().hashCode();
        }
        if (DEBUG) {
            Log.d(TAG, "Incorrect position for item id: " + i);
        }
        return (long) i;
    }

    public int getItemCount() {
        if (this.mController.isZeroMode()) {
            return this.mController.getMediaDevices().size() + 1;
        }
        return this.mController.getMediaDevices().size();
    }

    class MediaDeviceViewHolder extends MediaOutputBaseAdapter.MediaDeviceBaseViewHolder {
        MediaDeviceViewHolder(View view) {
            super(view);
        }

        /* access modifiers changed from: package-private */
        public void onBind(MediaDevice mediaDevice, boolean z, boolean z2, int i) {
            super.onBind(mediaDevice, z, z2, i);
            boolean hasMutingExpectedDevice = MediaOutputAdapter.this.mController.hasMutingExpectedDevice();
            boolean z3 = !MediaOutputAdapter.this.mIncludeDynamicGroup && MediaOutputAdapter.this.isCurrentlyConnected(mediaDevice);
            boolean z4 = this.mSeekBar.getVisibility() == 8;
            if (z3) {
                ViewGroup unused = MediaOutputAdapter.this.mConnectedItem = this.mContainerLayout;
            }
            this.mCheckBox.setVisibility(8);
            this.mStatusIcon.setVisibility(8);
            this.mEndTouchArea.setVisibility(8);
            this.mEndTouchArea.setImportantForAccessibility(2);
            C2214xea444da3 mediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda3 = null;
            this.mContainerLayout.setOnClickListener((View.OnClickListener) null);
            this.mContainerLayout.setContentDescription((CharSequence) null);
            this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
            this.mSubTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
            this.mTwoLineTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
            this.mSeekBar.getProgressDrawable().setColorFilter(new PorterDuffColorFilter(MediaOutputAdapter.this.mController.getColorSeekbarProgress(), PorterDuff.Mode.SRC_IN));
            if (MediaOutputAdapter.this.mCurrentActivePosition == i) {
                MediaOutputAdapter.this.mCurrentActivePosition = -1;
            }
            if (MediaOutputAdapter.this.mController.isTransferring()) {
                if (mediaDevice.getState() != 1 || MediaOutputAdapter.this.mController.hasAdjustVolumeUserRestriction()) {
                    setUpDeviceIcon(mediaDevice);
                    setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false);
                    return;
                }
                setUpDeviceIcon(mediaDevice);
                this.mProgressBar.getIndeterminateDrawable().setColorFilter(new PorterDuffColorFilter(MediaOutputAdapter.this.mController.getColorItemContent(), PorterDuff.Mode.SRC_IN));
                setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, false, true, false);
            } else if (mediaDevice.isMutingExpectedDevice() && !MediaOutputAdapter.this.mController.isCurrentConnectedDeviceRemote()) {
                this.mTitleIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(C1894R.C1896drawable.media_output_icon_volume));
                this.mTitleIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
                setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, false, false, false);
                initMutingExpectedDevice();
                MediaOutputAdapter.this.mCurrentActivePosition = i;
                this.mContainerLayout.setOnClickListener(new C2211xea444da0(this, mediaDevice));
            } else if (mediaDevice.getState() == 3) {
                setUpDeviceIcon(mediaDevice);
                this.mStatusIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(C1894R.C1896drawable.media_output_status_failed));
                this.mStatusIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                setTwoLineLayout(mediaDevice, false, false, false, true, true);
                this.mSubTitleText.setText(C1894R.string.media_output_dialog_connect_failed);
                this.mContainerLayout.setOnClickListener(new C2212xea444da1(this, mediaDevice));
            } else if (mediaDevice.getState() == 5) {
                setUpDeviceIcon(mediaDevice);
                this.mProgressBar.getIndeterminateDrawable().setColorFilter(new PorterDuffColorFilter(MediaOutputAdapter.this.mController.getColorItemContent(), PorterDuff.Mode.SRC_IN));
                setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, false, true, false);
            } else {
                if (MediaOutputAdapter.this.mController.getSelectedMediaDevice().size() > 1) {
                    MediaOutputAdapter mediaOutputAdapter = MediaOutputAdapter.this;
                    if (mediaOutputAdapter.isDeviceIncluded(mediaOutputAdapter.mController.getSelectedMediaDevice(), mediaDevice)) {
                        MediaOutputAdapter mediaOutputAdapter2 = MediaOutputAdapter.this;
                        boolean isDeviceIncluded = mediaOutputAdapter2.isDeviceIncluded(mediaOutputAdapter2.mController.getDeselectableMediaDevice(), mediaDevice);
                        this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
                        this.mTitleIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(C1894R.C1896drawable.media_output_icon_volume));
                        this.mTitleIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                        setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, true, false, false);
                        setUpContentDescriptionForView(this.mContainerLayout, false, mediaDevice);
                        this.mCheckBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
                        this.mCheckBox.setVisibility(0);
                        this.mCheckBox.setChecked(true);
                        this.mCheckBox.setOnCheckedChangeListener(isDeviceIncluded ? new C2213xea444da2(this, mediaDevice) : null);
                        this.mCheckBox.setEnabled(isDeviceIncluded);
                        setCheckBoxColor(this.mCheckBox, MediaOutputAdapter.this.mController.getColorItemContent());
                        initSeekbar(mediaDevice, z4);
                        this.mEndTouchArea.setVisibility(0);
                        this.mEndTouchArea.setOnClickListener((View.OnClickListener) null);
                        LinearLayout linearLayout = this.mEndTouchArea;
                        if (isDeviceIncluded) {
                            mediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda3 = new C2214xea444da3(this);
                        }
                        linearLayout.setOnClickListener(mediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda3);
                        this.mEndTouchArea.setImportantForAccessibility(1);
                        setUpContentDescriptionForView(this.mEndTouchArea, true, mediaDevice);
                        return;
                    }
                }
                if (MediaOutputAdapter.this.mController.hasAdjustVolumeUserRestriction() || !z3) {
                    MediaOutputAdapter mediaOutputAdapter3 = MediaOutputAdapter.this;
                    if (mediaOutputAdapter3.isDeviceIncluded(mediaOutputAdapter3.mController.getSelectableMediaDevice(), mediaDevice)) {
                        setUpDeviceIcon(mediaDevice);
                        this.mCheckBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
                        this.mCheckBox.setVisibility(0);
                        this.mCheckBox.setChecked(false);
                        this.mCheckBox.setOnCheckedChangeListener(new C2216xea444da5(this, mediaDevice));
                        this.mEndTouchArea.setVisibility(0);
                        this.mContainerLayout.setOnClickListener(new C2217xea444da6(this, mediaDevice));
                        setCheckBoxColor(this.mCheckBox, MediaOutputAdapter.this.mController.getColorItemContent());
                        setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false, false, false, false);
                        return;
                    }
                    setUpDeviceIcon(mediaDevice);
                    setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false);
                    this.mContainerLayout.setOnClickListener(new C2218xea444da7(this, mediaDevice));
                } else if (!hasMutingExpectedDevice || MediaOutputAdapter.this.mController.isCurrentConnectedDeviceRemote()) {
                    this.mTitleIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(C1894R.C1896drawable.media_output_icon_volume));
                    this.mTitleIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                    this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
                    setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), true, true, false, false);
                    initSeekbar(mediaDevice, z4);
                    setUpContentDescriptionForView(this.mContainerLayout, false, mediaDevice);
                    MediaOutputAdapter.this.mCurrentActivePosition = i;
                } else {
                    setUpDeviceIcon(mediaDevice);
                    setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false);
                    this.mContainerLayout.setOnClickListener(new C2215xea444da4(this));
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBind$2$com-android-systemui-media-dialog-MediaOutputAdapter$MediaDeviceViewHolder */
        public /* synthetic */ void mo34250x2de61aea(MediaDevice mediaDevice, CompoundButton compoundButton, boolean z) {
            onGroupActionTriggered(false, mediaDevice);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBind$3$com-android-systemui-media-dialog-MediaOutputAdapter$MediaDeviceViewHolder */
        public /* synthetic */ void mo34251x6ffd4849(View view) {
            this.mCheckBox.performClick();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBind$4$com-android-systemui-media-dialog-MediaOutputAdapter$MediaDeviceViewHolder */
        public /* synthetic */ void mo34252xb21475a8(View view) {
            cancelMuteAwaitConnection();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBind$5$com-android-systemui-media-dialog-MediaOutputAdapter$MediaDeviceViewHolder */
        public /* synthetic */ void mo34253xf42ba307(MediaDevice mediaDevice, CompoundButton compoundButton, boolean z) {
            onGroupActionTriggered(true, mediaDevice);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBind$6$com-android-systemui-media-dialog-MediaOutputAdapter$MediaDeviceViewHolder */
        public /* synthetic */ void mo34254x3642d066(MediaDevice mediaDevice, View view) {
            onGroupActionTriggered(true, mediaDevice);
        }

        public void setCheckBoxColor(CheckBox checkBox, int i) {
            CompoundButtonCompat.setButtonTintList(checkBox, new ColorStateList(new int[][]{new int[]{16842912}, new int[0]}, new int[]{i, i}));
        }

        /* access modifiers changed from: package-private */
        public void onBind(int i, boolean z, boolean z2) {
            if (i == 1) {
                this.mTitleText.setTextColor(MediaOutputAdapter.this.mController.getColorItemContent());
                this.mCheckBox.setVisibility(8);
                setSingleLineLayout(MediaOutputAdapter.this.mContext.getText(C1894R.string.media_output_dialog_pairing_new), false);
                this.mTitleIcon.setImageDrawable(MediaOutputAdapter.this.mContext.getDrawable(C1894R.C1896drawable.ic_add));
                this.mTitleIcon.setColorFilter(MediaOutputAdapter.this.mController.getColorItemContent());
                LinearLayout linearLayout = this.mContainerLayout;
                MediaOutputController mediaOutputController = MediaOutputAdapter.this.mController;
                Objects.requireNonNull(mediaOutputController);
                linearLayout.setOnClickListener(new C2219xea444da8(mediaOutputController));
            }
        }

        private void onGroupActionTriggered(boolean z, MediaDevice mediaDevice) {
            disableSeekBar();
            if (z) {
                MediaOutputAdapter mediaOutputAdapter = MediaOutputAdapter.this;
                if (mediaOutputAdapter.isDeviceIncluded(mediaOutputAdapter.mController.getSelectableMediaDevice(), mediaDevice)) {
                    MediaOutputAdapter.this.mController.addDeviceToPlayMedia(mediaDevice);
                    return;
                }
            }
            if (!z) {
                MediaOutputAdapter mediaOutputAdapter2 = MediaOutputAdapter.this;
                if (mediaOutputAdapter2.isDeviceIncluded(mediaOutputAdapter2.mController.getDeselectableMediaDevice(), mediaDevice)) {
                    MediaOutputAdapter.this.mController.removeDeviceFromPlayMedia(mediaDevice);
                }
            }
        }

        /* access modifiers changed from: private */
        /* renamed from: onItemClick */
        public void mo34255x7859fdc5(View view, MediaDevice mediaDevice) {
            if (!MediaOutputAdapter.this.mController.isTransferring()) {
                if (MediaOutputAdapter.this.isCurrentlyConnected(mediaDevice)) {
                    Log.d(MediaOutputAdapter.TAG, "This device is already connected! : " + mediaDevice.getName());
                    return;
                }
                MediaOutputAdapter.this.mController.setTemporaryAllowListExceptionIfNeeded(mediaDevice);
                MediaOutputAdapter.this.mCurrentActivePosition = -1;
                MediaOutputAdapter.this.mController.connectDevice(mediaDevice);
                mediaDevice.setState(1);
                MediaOutputAdapter.this.notifyDataSetChanged();
            }
        }

        private void cancelMuteAwaitConnection() {
            MediaOutputAdapter.this.mController.cancelMuteAwaitConnection();
            MediaOutputAdapter.this.notifyDataSetChanged();
        }

        private void setUpContentDescriptionForView(View view, boolean z, MediaDevice mediaDevice) {
            view.setClickable(z);
            view.setContentDescription(MediaOutputAdapter.this.mContext.getString(mediaDevice.getDeviceType() == 5 ? C1894R.string.accessibility_bluetooth_name : C1894R.string.accessibility_cast_name, new Object[]{mediaDevice.getName()}));
        }
    }
}
