package com.android.systemui.media.dialog;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.LayerDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.Utils;
import com.android.settingslib.media.MediaDevice;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.C1894R;
import java.util.List;

public abstract class MediaOutputBaseAdapter extends RecyclerView.Adapter<MediaDeviceBaseViewHolder> {
    static final int CUSTOMIZED_ITEM_DYNAMIC_GROUP = 3;
    static final int CUSTOMIZED_ITEM_GROUP = 2;
    static final int CUSTOMIZED_ITEM_PAIR_NEW = 1;
    Context mContext;
    protected final MediaOutputController mController;
    int mCurrentActivePosition = -1;
    View mHolderView;
    boolean mIsDragging = false;
    /* access modifiers changed from: private */
    public boolean mIsInitVolumeFirstTime = true;
    private int mMargin;

    public MediaOutputBaseAdapter(MediaOutputController mediaOutputController) {
        this.mController = mediaOutputController;
    }

    public MediaDeviceBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        this.mContext = context;
        this.mMargin = context.getResources().getDimensionPixelSize(C1894R.dimen.media_output_dialog_list_margin);
        this.mHolderView = LayoutInflater.from(this.mContext).inflate(C1894R.layout.media_output_list_item, viewGroup, false);
        return null;
    }

    /* access modifiers changed from: package-private */
    public void updateColorScheme(WallpaperColors wallpaperColors, boolean z) {
        this.mController.setCurrentColorScheme(wallpaperColors, z);
    }

    /* access modifiers changed from: package-private */
    public CharSequence getItemTitle(MediaDevice mediaDevice) {
        return mediaDevice.getName();
    }

    /* access modifiers changed from: package-private */
    public boolean isCurrentlyConnected(MediaDevice mediaDevice) {
        if (TextUtils.equals(mediaDevice.getId(), this.mController.getCurrentConnectedMediaDevice().getId())) {
            return true;
        }
        if (this.mController.getSelectedMediaDevice().size() != 1 || !isDeviceIncluded(this.mController.getSelectedMediaDevice(), mediaDevice)) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean isDeviceIncluded(List<MediaDevice> list, MediaDevice mediaDevice) {
        for (MediaDevice id : list) {
            if (TextUtils.equals(id.getId(), mediaDevice.getId())) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isDragging() {
        return this.mIsDragging;
    }

    /* access modifiers changed from: package-private */
    public int getCurrentActivePosition() {
        return this.mCurrentActivePosition;
    }

    public MediaOutputController getController() {
        return this.mController;
    }

    abstract class MediaDeviceBaseViewHolder extends RecyclerView.ViewHolder {
        private static final int ANIM_DURATION = 500;
        final CheckBox mCheckBox;
        final LinearLayout mContainerLayout;
        private ValueAnimator mCornerAnimator;
        private String mDeviceId;
        final LinearLayout mEndTouchArea;
        final FrameLayout mItemLayout;
        final ProgressBar mProgressBar;
        final MediaOutputSeekbar mSeekBar;
        final ImageView mStatusIcon;
        final TextView mSubTitleText;
        final ImageView mTitleIcon;
        final TextView mTitleText;
        final LinearLayout mTwoLineLayout;
        final TextView mTwoLineTitleText;
        private ValueAnimator mVolumeAnimator;

        static /* synthetic */ boolean lambda$disableSeekBar$2(View view, MotionEvent motionEvent) {
            return true;
        }

        static /* synthetic */ boolean lambda$enableSeekBar$3(View view, MotionEvent motionEvent) {
            return false;
        }

        /* access modifiers changed from: package-private */
        public abstract void onBind(int i, boolean z, boolean z2);

        MediaDeviceBaseViewHolder(View view) {
            super(view);
            this.mContainerLayout = (LinearLayout) view.requireViewById(C1894R.C1898id.device_container);
            this.mItemLayout = (FrameLayout) view.requireViewById(C1894R.C1898id.item_layout);
            this.mTitleText = (TextView) view.requireViewById(C1894R.C1898id.title);
            this.mSubTitleText = (TextView) view.requireViewById(C1894R.C1898id.subtitle);
            this.mTwoLineLayout = (LinearLayout) view.requireViewById(C1894R.C1898id.two_line_layout);
            this.mTwoLineTitleText = (TextView) view.requireViewById(C1894R.C1898id.two_line_title);
            this.mTitleIcon = (ImageView) view.requireViewById(C1894R.C1898id.title_icon);
            this.mProgressBar = (ProgressBar) view.requireViewById(C1894R.C1898id.volume_indeterminate_progress);
            this.mSeekBar = (MediaOutputSeekbar) view.requireViewById(C1894R.C1898id.volume_seekbar);
            this.mStatusIcon = (ImageView) view.requireViewById(C1894R.C1898id.media_output_item_status);
            this.mCheckBox = (CheckBox) view.requireViewById(C1894R.C1898id.check_box);
            this.mEndTouchArea = (LinearLayout) view.requireViewById(C1894R.C1898id.end_action_area);
            initAnimator();
        }

        /* access modifiers changed from: package-private */
        public void onBind(MediaDevice mediaDevice, boolean z, boolean z2, int i) {
            this.mDeviceId = mediaDevice.getId();
        }

        /* access modifiers changed from: package-private */
        public void setSingleLineLayout(CharSequence charSequence, boolean z) {
            setSingleLineLayout(charSequence, z, false, false, false);
        }

        /* access modifiers changed from: package-private */
        public void setSingleLineLayout(CharSequence charSequence, boolean z, boolean z2, boolean z3, boolean z4) {
            int i;
            Drawable drawable;
            int i2;
            int i3 = 8;
            this.mTwoLineLayout.setVisibility(8);
            boolean z5 = z2 || z3;
            if (!this.mCornerAnimator.isRunning()) {
                if (z2) {
                    drawable = MediaOutputBaseAdapter.this.mContext.getDrawable(C1894R.C1896drawable.media_output_item_background_active).mutate();
                } else {
                    drawable = MediaOutputBaseAdapter.this.mContext.getDrawable(C1894R.C1896drawable.media_output_item_background).mutate();
                }
                if (z5) {
                    i2 = MediaOutputBaseAdapter.this.mController.getColorConnectedItemBackground();
                } else {
                    i2 = MediaOutputBaseAdapter.this.mController.getColorItemBackground();
                }
                drawable.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.SRC_IN));
                this.mItemLayout.setBackground(drawable);
                if (z2) {
                    ((GradientDrawable) ((ClipDrawable) ((LayerDrawable) this.mSeekBar.getProgressDrawable()).findDrawableByLayerId(16908301)).getDrawable()).setCornerRadius(MediaOutputBaseAdapter.this.mController.getActiveRadius());
                }
            } else {
                Drawable background = this.mItemLayout.getBackground();
                if (z5) {
                    i = MediaOutputBaseAdapter.this.mController.getColorConnectedItemBackground();
                } else {
                    i = MediaOutputBaseAdapter.this.mController.getColorItemBackground();
                }
                background.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
            }
            this.mProgressBar.setVisibility(z3 ? 0 : 8);
            this.mSeekBar.setAlpha(1.0f);
            this.mSeekBar.setVisibility(z2 ? 0 : 8);
            if (!z2) {
                this.mSeekBar.resetVolume();
            }
            ImageView imageView = this.mStatusIcon;
            if (z4) {
                i3 = 0;
            }
            imageView.setVisibility(i3);
            this.mTitleText.setText(charSequence);
            this.mTitleText.setVisibility(0);
        }

        /* access modifiers changed from: package-private */
        public void setTwoLineLayout(MediaDevice mediaDevice, boolean z, boolean z2, boolean z3, boolean z4) {
            setTwoLineLayout(mediaDevice, (CharSequence) null, z, z2, z3, z4, false);
        }

        /* access modifiers changed from: package-private */
        public void setTwoLineLayout(MediaDevice mediaDevice, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
            setTwoLineLayout(mediaDevice, (CharSequence) null, z, z2, z3, z4, z5);
        }

        /* access modifiers changed from: package-private */
        public void setTwoLineLayout(CharSequence charSequence, boolean z, boolean z2, boolean z3, boolean z4) {
            setTwoLineLayout((MediaDevice) null, charSequence, z, z2, z3, z4, false);
        }

        private void setTwoLineLayout(MediaDevice mediaDevice, CharSequence charSequence, boolean z, boolean z2, boolean z3, boolean z4, boolean z5) {
            int i = 8;
            this.mTitleText.setVisibility(8);
            this.mTwoLineLayout.setVisibility(0);
            this.mStatusIcon.setVisibility(z5 ? 0 : 8);
            this.mSeekBar.setAlpha(1.0f);
            this.mSeekBar.setVisibility(z2 ? 0 : 8);
            Drawable mutate = MediaOutputBaseAdapter.this.mContext.getDrawable(C1894R.C1896drawable.media_output_item_background).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(MediaOutputBaseAdapter.this.mController.getColorItemBackground(), PorterDuff.Mode.SRC_IN));
            this.mItemLayout.setBackground(mutate);
            this.mProgressBar.setVisibility(z3 ? 0 : 8);
            TextView textView = this.mSubTitleText;
            if (z4) {
                i = 0;
            }
            textView.setVisibility(i);
            this.mTwoLineTitleText.setTranslationY(0.0f);
            if (mediaDevice == null) {
                this.mTwoLineTitleText.setText(charSequence);
            } else {
                this.mTwoLineTitleText.setText(MediaOutputBaseAdapter.this.getItemTitle(mediaDevice));
            }
            if (z) {
                this.mTwoLineTitleText.setTypeface(Typeface.create(MediaOutputBaseAdapter.this.mContext.getString(17039984), 0));
            } else {
                this.mTwoLineTitleText.setTypeface(Typeface.create(MediaOutputBaseAdapter.this.mContext.getString(17039983), 0));
            }
        }

        /* access modifiers changed from: package-private */
        public void initSeekbar(final MediaDevice mediaDevice, boolean z) {
            if (!MediaOutputBaseAdapter.this.mController.isVolumeControlEnabled(mediaDevice)) {
                disableSeekBar();
            } else {
                enableSeekBar();
            }
            this.mSeekBar.setMaxVolume(mediaDevice.getMaxVolume());
            int currentVolume = mediaDevice.getCurrentVolume();
            if (this.mSeekBar.getVolume() != currentVolume) {
                if (z && !MediaOutputBaseAdapter.this.mIsInitVolumeFirstTime) {
                    animateCornerAndVolume(this.mSeekBar.getProgress(), MediaOutputSeekbar.scaleVolumeToProgress(currentVolume));
                } else if (!this.mVolumeAnimator.isStarted()) {
                    this.mSeekBar.setVolume(currentVolume);
                } else {
                    endAnimateCornerAndVolume();
                    this.mSeekBar.setVolume(currentVolume);
                }
            }
            if (MediaOutputBaseAdapter.this.mIsInitVolumeFirstTime) {
                boolean unused = MediaOutputBaseAdapter.this.mIsInitVolumeFirstTime = false;
            }
            this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    int scaleProgressToVolume;
                    if (mediaDevice != null && z && (scaleProgressToVolume = MediaOutputSeekbar.scaleProgressToVolume(i)) != mediaDevice.getCurrentVolume()) {
                        MediaOutputBaseAdapter.this.mController.adjustVolume(mediaDevice, scaleProgressToVolume);
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    MediaOutputBaseAdapter.this.mIsDragging = true;
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    MediaOutputBaseAdapter.this.mIsDragging = false;
                }
            });
        }

        /* access modifiers changed from: package-private */
        public void initMutingExpectedDevice() {
            disableSeekBar();
            Drawable mutate = MediaOutputBaseAdapter.this.mContext.getDrawable(C1894R.C1896drawable.media_output_item_background_active).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(MediaOutputBaseAdapter.this.mController.getColorConnectedItemBackground(), PorterDuff.Mode.SRC_IN));
            this.mItemLayout.setBackground(mutate);
        }

        /* access modifiers changed from: package-private */
        public void initSessionSeekbar() {
            disableSeekBar();
            this.mSeekBar.setMax(MediaOutputBaseAdapter.this.mController.getSessionVolumeMax());
            this.mSeekBar.setMin(0);
            int sessionVolume = MediaOutputBaseAdapter.this.mController.getSessionVolume();
            if (this.mSeekBar.getProgress() != sessionVolume) {
                this.mSeekBar.setProgress(sessionVolume, true);
            }
            this.mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    if (z) {
                        MediaOutputBaseAdapter.this.mController.adjustSessionVolume(i);
                    }
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    MediaOutputBaseAdapter.this.mIsDragging = true;
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    MediaOutputBaseAdapter.this.mIsDragging = false;
                }
            });
        }

        private void animateCornerAndVolume(int i, int i2) {
            this.mCornerAnimator.addUpdateListener(new C2224x9ea0901((GradientDrawable) this.mItemLayout.getBackground(), (GradientDrawable) ((ClipDrawable) ((LayerDrawable) this.mSeekBar.getProgressDrawable()).findDrawableByLayerId(16908301)).getDrawable()));
            this.mVolumeAnimator.setIntValues(new int[]{i, i2});
            this.mVolumeAnimator.start();
            this.mCornerAnimator.start();
        }

        static /* synthetic */ void lambda$animateCornerAndVolume$0(GradientDrawable gradientDrawable, GradientDrawable gradientDrawable2, ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            gradientDrawable.setCornerRadius(floatValue);
            gradientDrawable2.setCornerRadius(floatValue);
        }

        private void endAnimateCornerAndVolume() {
            this.mVolumeAnimator.end();
            this.mCornerAnimator.end();
        }

        private void initAnimator() {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{MediaOutputBaseAdapter.this.mController.getInactiveRadius(), MediaOutputBaseAdapter.this.mController.getActiveRadius()});
            this.mCornerAnimator = ofFloat;
            ofFloat.setDuration(500);
            this.mCornerAnimator.setInterpolator(new LinearInterpolator());
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[0]);
            this.mVolumeAnimator = ofInt;
            ofInt.addUpdateListener(new C2225x9ea0902(this));
            this.mVolumeAnimator.setDuration(500);
            this.mVolumeAnimator.setInterpolator(new LinearInterpolator());
            this.mVolumeAnimator.addListener(new Animator.AnimatorListener() {
                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                    MediaDeviceBaseViewHolder.this.mSeekBar.setEnabled(false);
                }

                public void onAnimationEnd(Animator animator) {
                    MediaDeviceBaseViewHolder.this.mSeekBar.setEnabled(true);
                }

                public void onAnimationCancel(Animator animator) {
                    MediaDeviceBaseViewHolder.this.mSeekBar.setEnabled(true);
                }
            });
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$initAnimator$1$com-android-systemui-media-dialog-MediaOutputBaseAdapter$MediaDeviceBaseViewHolder */
        public /* synthetic */ void mo34280x16e44a30(ValueAnimator valueAnimator) {
            this.mSeekBar.setProgress(((Integer) valueAnimator.getAnimatedValue()).intValue());
        }

        /* access modifiers changed from: package-private */
        public Drawable getSpeakerDrawable() {
            Drawable mutate = MediaOutputBaseAdapter.this.mContext.getDrawable(C1894R.C1896drawable.ic_speaker_group_black_24dp).mutate();
            mutate.setColorFilter(new PorterDuffColorFilter(Utils.getColorStateListDefaultColor(MediaOutputBaseAdapter.this.mContext, C1894R.C1895color.media_dialog_item_main_content), PorterDuff.Mode.SRC_IN));
            return mutate;
        }

        /* access modifiers changed from: protected */
        public void disableSeekBar() {
            this.mSeekBar.setEnabled(false);
            this.mSeekBar.setOnTouchListener(new C2228x9ea0905());
        }

        private void enableSeekBar() {
            this.mSeekBar.setEnabled(true);
            this.mSeekBar.setOnTouchListener(new C2227x9ea0904());
        }

        /* access modifiers changed from: protected */
        public void setUpDeviceIcon(MediaDevice mediaDevice) {
            ThreadUtils.postOnBackgroundThread((Runnable) new C2226x9ea0903(this, mediaDevice));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setUpDeviceIcon$5$com-android-systemui-media-dialog-MediaOutputBaseAdapter$MediaDeviceBaseViewHolder */
        public /* synthetic */ void mo34282xbe1224d1(MediaDevice mediaDevice) {
            ThreadUtils.postOnMainThread(new C2223x9ea0900(this, mediaDevice, MediaOutputBaseAdapter.this.mController.getDeviceIconCompat(mediaDevice).toIcon(MediaOutputBaseAdapter.this.mContext)));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$setUpDeviceIcon$4$com-android-systemui-media-dialog-MediaOutputBaseAdapter$MediaDeviceBaseViewHolder */
        public /* synthetic */ void mo34281x7612c672(MediaDevice mediaDevice, Icon icon) {
            if (TextUtils.equals(this.mDeviceId, mediaDevice.getId())) {
                this.mTitleIcon.setImageIcon(icon);
            }
        }
    }
}
