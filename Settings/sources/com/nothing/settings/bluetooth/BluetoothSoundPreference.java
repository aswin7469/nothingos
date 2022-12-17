package com.nothing.settings.bluetooth;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.nothing.p005os.device.DeviceServiceController;
import com.nothing.settings.utils.AnimationUtils;

public class BluetoothSoundPreference extends Preference implements View.OnClickListener {
    private LinearOutSlowInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new LinearOutSlowInInterpolator();
    private int ancLevel = 4;
    /* access modifiers changed from: private */
    public View mAncTopPanelLayout;
    /* access modifiers changed from: private */
    public View mAnimatorView;
    private ValueAnimator mBottomAncAnimator;
    /* access modifiers changed from: private */
    public ImageView mBottomAncAnimatorView;
    private ImageView mBottomAncAnimatorViewAdaptive;
    private ImageView mBottomAncAnimatorViewMid;
    private CachedBluetoothDevice mCachedDevice;
    private String mCallBackMode;
    private DeviceServiceController mController;
    private ImageView mDenoiseOff;
    private ImageView mDenoiseOn;
    private View mDenoiseOnAdaptive;
    private View mDenoiseOnAdaptiveBack;
    private ImageView mDenoiseOnAdaptiveImage;
    private View mDenoiseOnHigh;
    private View mDenoiseOnHighBack;
    private ImageView mDenoiseOnHighImage;
    /* access modifiers changed from: private */
    public View mDenoiseOnLevel;
    private View mDenoiseOnLow;
    private View mDenoiseOnLowBack;
    private ImageView mDenoiseOnLowImage;
    private View mDenoiseOnMid;
    private ImageView mDenoiseOnMidImage;
    private ImageView mDenoiseTrans;
    private boolean mIsBindView;
    private boolean mIsClickMode;
    private String mLastNoiseSelectedMode = null;
    private String mNoiseOnSelectedMode = "1";
    private String mNoiseSelectedMode = null;
    private ValueAnimator mValueAnimator;

    public BluetoothSoundPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R$layout.nt_preference_anc_sound);
    }

    public BluetoothSoundPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R$layout.nt_preference_anc_sound);
    }

    public BluetoothSoundPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.nt_preference_anc_sound);
    }

    public BluetoothSoundPreference(Context context, DeviceServiceController deviceServiceController, CachedBluetoothDevice cachedBluetoothDevice) {
        super(context);
        setLayoutResource(R$layout.nt_preference_anc_sound);
        this.mController = deviceServiceController;
        this.mCachedDevice = cachedBluetoothDevice;
        setSelectable(false);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.itemView.findViewById(R$id.denoise_on);
        this.mDenoiseOn = imageView;
        imageView.setOnClickListener(this);
        ImageView imageView2 = (ImageView) preferenceViewHolder.itemView.findViewById(R$id.denoise_off);
        this.mDenoiseOff = imageView2;
        imageView2.setOnClickListener(this);
        ImageView imageView3 = (ImageView) preferenceViewHolder.itemView.findViewById(R$id.denoise_trans);
        this.mDenoiseTrans = imageView3;
        imageView3.setOnClickListener(this);
        this.mDenoiseOnHighImage = (ImageView) preferenceViewHolder.itemView.findViewById(R$id.denoise_on_high_image);
        this.mDenoiseOnHighBack = preferenceViewHolder.itemView.findViewById(R$id.denoise_on_high_back);
        View findViewById = preferenceViewHolder.itemView.findViewById(R$id.denoise_on_high);
        this.mDenoiseOnHigh = findViewById;
        findViewById.setOnClickListener(this);
        View view = preferenceViewHolder.itemView;
        int i = R$id.denoise_on_low_image;
        this.mDenoiseOnLowImage = (ImageView) view.findViewById(i);
        this.mDenoiseOnLowBack = preferenceViewHolder.itemView.findViewById(R$id.denoise_on_low_back);
        View findViewById2 = preferenceViewHolder.itemView.findViewById(R$id.denoise_on_low);
        this.mDenoiseOnLow = findViewById2;
        findViewById2.setOnClickListener(this);
        this.mDenoiseOnLevel = preferenceViewHolder.itemView.findViewById(R$id.denoise_on_level);
        this.mDenoiseOnMidImage = (ImageView) preferenceViewHolder.itemView.findViewById(R$id.denoise_on_mid_image);
        View findViewById3 = preferenceViewHolder.itemView.findViewById(R$id.denoise_on_mid);
        this.mDenoiseOnMid = findViewById3;
        findViewById3.setOnClickListener(this);
        this.mDenoiseOnAdaptiveImage = (ImageView) preferenceViewHolder.itemView.findViewById(R$id.denoise_on_adaptive_image);
        this.mDenoiseOnAdaptiveBack = preferenceViewHolder.itemView.findViewById(R$id.denoise_on_adaptive_back);
        View findViewById4 = preferenceViewHolder.itemView.findViewById(R$id.denoise_on_adaptive);
        this.mDenoiseOnAdaptive = findViewById4;
        findViewById4.setOnClickListener(this);
        this.mBottomAncAnimatorView = (ImageView) preferenceViewHolder.itemView.findViewById(R$id.denoise_bottom_anc_anim);
        this.mBottomAncAnimatorViewMid = (ImageView) preferenceViewHolder.itemView.findViewById(R$id.denoise_bottom_anc_anim_mid);
        this.mBottomAncAnimatorViewAdaptive = (ImageView) preferenceViewHolder.itemView.findViewById(R$id.denoise_bottom_anc_anim_adaptive);
        this.mAncTopPanelLayout = preferenceViewHolder.itemView.findViewById(R$id.denoise_top_panel_layout);
        View findViewById5 = preferenceViewHolder.itemView.findViewById(R$id.denoise_animator_view);
        this.mAnimatorView = findViewById5;
        findViewById5.setBackground((Drawable) null);
        this.mBottomAncAnimatorView.setImageDrawable((Drawable) null);
        this.mIsClickMode = false;
        if (this.ancLevel != 4) {
            this.mDenoiseOnMid.setVisibility(8);
            this.mDenoiseOnAdaptive.setVisibility(8);
            this.mBottomAncAnimatorViewMid.setVisibility(8);
            this.mBottomAncAnimatorViewAdaptive.setVisibility(8);
            if (getContext().getResources().getConfiguration().getLayoutDirection() != 1) {
                this.mDenoiseOnLowBack.setBackgroundResource(R$drawable.nt_bluetooth_denoise_background_two);
            } else {
                this.mDenoiseOnLowBack.setBackgroundResource(R$drawable.nt_bluetooth_denoise_background_one);
                this.mDenoiseOnHighBack.setBackgroundResource(R$drawable.nt_bluetooth_denoise_background_two);
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mDenoiseOnLowBack.getLayoutParams();
            layoutParams.addRule(19, i);
            layoutParams.setMarginEnd(-15);
            this.mDenoiseOnLowBack.setLayoutParams(layoutParams);
        } else if (getContext().getResources().getConfiguration().getLayoutDirection() == 1) {
            this.mDenoiseOnHighBack.setBackgroundResource(R$drawable.nt_bluetooth_denoise_background_two);
            this.mDenoiseOnAdaptiveBack.setBackgroundResource(R$drawable.nt_bluetooth_denoise_background_one);
        }
        initAnimator();
        Log.d("BluetoothSoundPreference", "onBindViewHolder mCallBackMode:" + this.mCallBackMode);
        if (!TextUtils.isEmpty(this.mCallBackMode)) {
            updateUI(this.mCallBackMode);
        }
        this.mIsBindView = true;
    }

    private void setNoiseNoneSelected() {
        ImageView imageView = this.mDenoiseOff;
        if (imageView != null && this.mDenoiseOn != null && this.mDenoiseTrans != null && this.mDenoiseOnLevel != null) {
            imageView.setBackground((Drawable) null);
            this.mDenoiseOff.setImageResource(R$drawable.nt_ic_bluetooth_denoise_off);
            this.mDenoiseOn.setBackground((Drawable) null);
            this.mDenoiseOn.setImageResource(R$drawable.nt_ic_bluetooth_denoise_on);
            this.mDenoiseTrans.setBackground((Drawable) null);
            this.mDenoiseTrans.setImageResource(R$drawable.nt_ic_bluetooth_denoise_trans);
        }
    }

    private void setNoiseOnSelected() {
        ImageView imageView = this.mDenoiseOff;
        if (imageView != null && this.mDenoiseOn != null && this.mDenoiseTrans != null && this.mDenoiseOnLevel != null) {
            imageView.setBackground((Drawable) null);
            this.mDenoiseOff.setImageResource(R$drawable.nt_ic_bluetooth_denoise_off);
            this.mDenoiseOn.setBackgroundResource(R$drawable.nt_bluetooth_denoise_selected_background);
            this.mDenoiseOn.setImageResource(R$drawable.nt_ic_bluetooth_denoise_on_selected);
            this.mDenoiseTrans.setBackground((Drawable) null);
            this.mDenoiseTrans.setImageResource(R$drawable.nt_ic_bluetooth_denoise_trans);
            ancLevelAnimatedIn();
        }
    }

    private void setNoiseOffSelected() {
        ImageView imageView = this.mDenoiseOff;
        if (imageView != null && this.mDenoiseOn != null && this.mDenoiseTrans != null && this.mDenoiseOnLevel != null) {
            imageView.setBackgroundResource(R$drawable.nt_bluetooth_denoise_selected_background);
            this.mDenoiseOff.setImageResource(R$drawable.nt_ic_bluetooth_denoise_off_selected);
            this.mDenoiseOn.setBackground((Drawable) null);
            this.mDenoiseOn.setImageResource(R$drawable.nt_ic_bluetooth_denoise_on);
            this.mDenoiseTrans.setBackground((Drawable) null);
            this.mDenoiseTrans.setImageResource(R$drawable.nt_ic_bluetooth_denoise_trans);
            ancLevelAnimatedOut();
        }
    }

    private void ancLevelAnimatedOut() {
        Log.d("BluetoothSoundPreference", "ancLevelAnimatedOut mIsClickMode:" + this.mIsClickMode);
        if (!this.mIsClickMode || TextUtils.equals(this.mLastNoiseSelectedMode, "7") || TextUtils.equals(this.mLastNoiseSelectedMode, "5")) {
            this.mDenoiseOnLevel.setVisibility(8);
            this.mAncTopPanelLayout.setBackgroundResource(R$drawable.nt_bluetooth_denoise_sound_background);
            return;
        }
        AnimationUtils.collapseTranslate(this.mDenoiseOnLevel, false, new AnimationUtils.TransformationListener() {
            public void applyTransformation(float f) {
                if (f > 0.9f) {
                    BluetoothSoundPreference.this.mAncTopPanelLayout.setBackgroundResource(R$drawable.nt_bluetooth_denoise_sound_background);
                }
            }
        }).setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                BluetoothSoundPreference.this.mAncTopPanelLayout.setBackgroundResource(R$drawable.nt_bluetooth_denoise_sound_background);
                BluetoothSoundPreference.this.mDenoiseOnLevel.setVisibility(8);
            }
        });
    }

    private void ancLevelAnimatedIn() {
        Log.d("BluetoothSoundPreference", "ancLevelAnimatedIn mIsClickMode:" + this.mIsClickMode);
        if (!this.mIsClickMode || (!TextUtils.equals(this.mLastNoiseSelectedMode, "7") && !TextUtils.equals(this.mLastNoiseSelectedMode, "5"))) {
            this.mDenoiseOnLevel.setAlpha(1.0f);
            this.mDenoiseOnLevel.setVisibility(0);
            this.mAncTopPanelLayout.setBackgroundResource(R$drawable.nt_bluetooth_denoise_sound_background_top);
            return;
        }
        this.mDenoiseOnLevel.setAlpha(1.0f);
        this.mDenoiseOnLevel.setVisibility(0);
        AnimationUtils.expandTranslate(this.mDenoiseOnLevel, false, new AnimationUtils.TransformationListener() {
            public void applyTransformation(float f) {
                if (f > 0.9f) {
                    BluetoothSoundPreference.this.mAncTopPanelLayout.setBackgroundResource(R$drawable.nt_bluetooth_denoise_sound_background_top);
                }
            }
        }).setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                BluetoothSoundPreference.this.mAncTopPanelLayout.setBackgroundResource(R$drawable.nt_bluetooth_denoise_sound_background_top);
            }
        });
    }

    private void setNoiseTransSelected() {
        ImageView imageView = this.mDenoiseOff;
        if (imageView != null && this.mDenoiseOn != null && this.mDenoiseTrans != null && this.mDenoiseOnLevel != null) {
            imageView.setBackground((Drawable) null);
            this.mDenoiseOff.setImageResource(R$drawable.nt_ic_bluetooth_denoise_off);
            this.mDenoiseOn.setBackground((Drawable) null);
            this.mDenoiseOn.setImageResource(R$drawable.nt_ic_bluetooth_denoise_on);
            this.mDenoiseTrans.setBackgroundResource(R$drawable.nt_bluetooth_denoise_selected_background);
            this.mDenoiseTrans.setImageResource(R$drawable.nt_ic_bluetooth_denoise_trans_selected);
            ancLevelAnimatedOut();
        }
    }

    private void setNoiseOnNoneSelected() {
        ImageView imageView = this.mDenoiseOnHighImage;
        if (imageView != null && this.mDenoiseOnLowImage != null && this.mDenoiseOnAdaptiveImage != null && this.mDenoiseOnMidImage != null) {
            int i = R$drawable.nt_ic_bluetooth_denoise_sound_unselected;
            imageView.setImageResource(i);
            this.mDenoiseOnLowImage.setImageResource(i);
            this.mDenoiseOnMidImage.setImageResource(i);
            this.mDenoiseOnAdaptiveImage.setImageResource(i);
        }
    }

    private void setNoiseOnHighSelected() {
        ImageView imageView = this.mDenoiseOnHighImage;
        if (imageView != null && this.mDenoiseOnLowImage != null && this.mDenoiseOnAdaptiveImage != null && this.mDenoiseOnMidImage != null) {
            imageView.setImageResource(R$drawable.nt_ic_bluetooth_denoise_sound_selected);
            ImageView imageView2 = this.mDenoiseOnLowImage;
            int i = R$drawable.nt_ic_bluetooth_denoise_sound_unselected;
            imageView2.setImageResource(i);
            this.mDenoiseOnMidImage.setImageResource(i);
            this.mDenoiseOnAdaptiveImage.setImageResource(i);
        }
    }

    private void setNoiseOnLowSelected() {
        ImageView imageView = this.mDenoiseOnHighImage;
        if (imageView != null && this.mDenoiseOnLowImage != null && this.mDenoiseOnAdaptiveImage != null && this.mDenoiseOnMidImage != null) {
            int i = R$drawable.nt_ic_bluetooth_denoise_sound_unselected;
            imageView.setImageResource(i);
            this.mDenoiseOnLowImage.setImageResource(R$drawable.nt_ic_bluetooth_denoise_sound_selected);
            this.mDenoiseOnMidImage.setImageResource(i);
            this.mDenoiseOnAdaptiveImage.setImageResource(i);
        }
    }

    private void setNoiseOnMidSelected() {
        ImageView imageView = this.mDenoiseOnHighImage;
        if (imageView != null && this.mDenoiseOnLowImage != null && this.mDenoiseOnAdaptiveImage != null && this.mDenoiseOnMidImage != null) {
            int i = R$drawable.nt_ic_bluetooth_denoise_sound_unselected;
            imageView.setImageResource(i);
            this.mDenoiseOnLowImage.setImageResource(i);
            this.mDenoiseOnMidImage.setImageResource(R$drawable.nt_ic_bluetooth_denoise_sound_selected);
            this.mDenoiseOnAdaptiveImage.setImageResource(i);
        }
    }

    private void setNoiseOnAdaptiveSelected() {
        ImageView imageView = this.mDenoiseOnHighImage;
        if (imageView != null && this.mDenoiseOnLowImage != null && this.mDenoiseOnAdaptiveImage != null && this.mDenoiseOnMidImage != null) {
            int i = R$drawable.nt_ic_bluetooth_denoise_sound_unselected;
            imageView.setImageResource(i);
            this.mDenoiseOnLowImage.setImageResource(i);
            this.mDenoiseOnMidImage.setImageResource(i);
            this.mDenoiseOnAdaptiveImage.setImageResource(R$drawable.nt_ic_bluetooth_denoise_sound_selected);
        }
    }

    public void onClick(View view) {
        try {
            Log.d("BluetoothSoundPreference", "onclick mIsClickMode:" + this.mIsClickMode);
            if (!this.mIsClickMode) {
                int id = view.getId();
                if (id == R$id.denoise_off) {
                    Bundle bundle = new Bundle();
                    bundle.putString("KEY_VALUE_STRING", "5");
                    bundle.putString("device_address", this.mCachedDevice.getAddress());
                    this.mController.setCommand(610, bundle);
                    this.mNoiseSelectedMode = "5";
                } else if (id == R$id.denoise_on) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("KEY_VALUE_STRING", this.mNoiseOnSelectedMode);
                    bundle2.putString("device_address", this.mCachedDevice.getAddress());
                    this.mController.setCommand(610, bundle2);
                    if ("3".equals(this.mNoiseOnSelectedMode)) {
                        setNoiseOnLowSelected();
                    } else if ("4".equals(this.mNoiseOnSelectedMode)) {
                        setNoiseOnAdaptiveSelected();
                    } else if ("2".equals(this.mNoiseOnSelectedMode)) {
                        setNoiseOnMidSelected();
                    } else {
                        setNoiseOnHighSelected();
                    }
                    this.mNoiseSelectedMode = this.mNoiseOnSelectedMode;
                } else if (id == R$id.denoise_on_low) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("KEY_VALUE_STRING", "3");
                    bundle3.putString("device_address", this.mCachedDevice.getAddress());
                    this.mController.setCommand(610, bundle3);
                    this.mNoiseSelectedMode = "3";
                } else if (id == R$id.denoise_on_high) {
                    Bundle bundle4 = new Bundle();
                    bundle4.putString("KEY_VALUE_STRING", "1");
                    bundle4.putString("device_address", this.mCachedDevice.getAddress());
                    this.mController.setCommand(610, bundle4);
                    this.mNoiseSelectedMode = "1";
                } else if (id == R$id.denoise_trans) {
                    Bundle bundle5 = new Bundle();
                    bundle5.putString("KEY_VALUE_STRING", "7");
                    bundle5.putString("device_address", this.mCachedDevice.getAddress());
                    this.mController.setCommand(610, bundle5);
                    this.mNoiseSelectedMode = "7";
                } else if (id == R$id.denoise_on_mid) {
                    Bundle bundle6 = new Bundle();
                    bundle6.putString("KEY_VALUE_STRING", "2");
                    bundle6.putString("device_address", this.mCachedDevice.getAddress());
                    this.mController.setCommand(610, bundle6);
                    this.mNoiseSelectedMode = "2";
                } else if (id == R$id.denoise_on_adaptive) {
                    Bundle bundle7 = new Bundle();
                    bundle7.putString("KEY_VALUE_STRING", "4");
                    bundle7.putString("device_address", this.mCachedDevice.getAddress());
                    this.mController.setCommand(610, bundle7);
                    this.mNoiseSelectedMode = "4";
                }
                NothingBluetoothUtil.getinstance().setNoiseSelectedMode(this.mNoiseSelectedMode);
                Log.d("BluetoothSoundPreference", "onclick mNoiseSelectedMode:" + this.mNoiseSelectedMode);
                this.mIsClickMode = true;
            }
        } catch (Exception unused) {
        }
    }

    public void setAncLevel(int i) {
        this.ancLevel = i;
    }

    private void initAnimator() {
        ValueAnimator valueAnimator = new ValueAnimator();
        this.mValueAnimator = valueAnimator;
        valueAnimator.setInterpolator(this.FAST_OUT_SLOW_IN_INTERPOLATOR);
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BluetoothSoundPreference.this.mAnimatorView.setX(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.mValueAnimator.setDuration(300);
        ValueAnimator valueAnimator2 = new ValueAnimator();
        this.mBottomAncAnimator = valueAnimator2;
        valueAnimator2.setInterpolator(this.FAST_OUT_SLOW_IN_INTERPOLATOR);
        this.mBottomAncAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                BluetoothSoundPreference.this.mBottomAncAnimatorView.setX(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.mBottomAncAnimator.setDuration(300);
    }

    private void startBottomAncAnimator() {
        float f;
        float f2;
        if (!TextUtils.equals(this.mLastNoiseSelectedMode, this.mNoiseSelectedMode)) {
            setNoiseOnNoneSelected();
            this.mBottomAncAnimatorView.setImageResource(R$drawable.nt_ic_bluetooth_denoise_sound_selected);
            if (TextUtils.equals(this.mLastNoiseSelectedMode, "1")) {
                f = this.mDenoiseOnHigh.getX();
            } else if (TextUtils.equals(this.mLastNoiseSelectedMode, "3")) {
                f = this.mDenoiseOnLow.getX();
            } else if (TextUtils.equals(this.mLastNoiseSelectedMode, "2")) {
                f = this.mDenoiseOnMid.getX();
            } else if (TextUtils.equals(this.mLastNoiseSelectedMode, "4")) {
                f = this.mDenoiseOnAdaptive.getX();
            } else {
                f = this.mDenoiseOnHigh.getX();
            }
            Log.d("BluetoothSoundPreference", "startBottomAncAnimator mLastNoiseSelectedMode:" + this.mLastNoiseSelectedMode + ", mNoiseSelectedMode:" + this.mNoiseSelectedMode);
            if (TextUtils.equals(this.mNoiseSelectedMode, "1")) {
                f2 = this.mDenoiseOnHigh.getX();
            } else if (TextUtils.equals(this.mNoiseSelectedMode, "3")) {
                f2 = this.mDenoiseOnLow.getX();
            } else if (TextUtils.equals(this.mNoiseSelectedMode, "2")) {
                f2 = this.mDenoiseOnMid.getX();
            } else if (TextUtils.equals(this.mNoiseSelectedMode, "4")) {
                f2 = this.mDenoiseOnAdaptive.getX();
            } else {
                f2 = this.mDenoiseOnHigh.getX();
            }
            Log.d("BluetoothSoundPreference", "startBottomAncAnimator startX:" + f + ", endX:" + f2);
            if (f == f2 || TextUtils.equals(this.mLastNoiseSelectedMode, "7") || TextUtils.equals(this.mLastNoiseSelectedMode, "5")) {
                this.mBottomAncAnimatorView.setX(f2);
            } else if (TextUtils.equals(this.mNoiseSelectedMode, "7") || TextUtils.equals(this.mNoiseSelectedMode, "5")) {
                this.mBottomAncAnimatorView.setX(f);
            } else {
                this.mBottomAncAnimator.setFloatValues(new float[]{f, f2});
                this.mBottomAncAnimator.start();
            }
        }
    }

    private void startAnimator() {
        float f;
        float f2;
        if (!TextUtils.equals(this.mLastNoiseSelectedMode, this.mNoiseSelectedMode)) {
            setNoiseNoneSelected();
            this.mAnimatorView.setBackgroundResource(R$drawable.nt_bluetooth_denoise_selected_background);
            if (TextUtils.equals(this.mLastNoiseSelectedMode, "5")) {
                f = this.mDenoiseOff.getX();
            } else if (TextUtils.equals(this.mLastNoiseSelectedMode, "7")) {
                f = this.mDenoiseTrans.getX();
            } else {
                f = this.mDenoiseOn.getX();
            }
            Log.d("BluetoothSoundPreference", "startAnimator mLastNoiseSelectedMode:" + this.mLastNoiseSelectedMode + ", mNoiseSelectedMode:" + this.mNoiseSelectedMode);
            if (TextUtils.equals(this.mNoiseSelectedMode, "5")) {
                f2 = this.mDenoiseOff.getX();
                this.mDenoiseOff.setImageResource(R$drawable.nt_ic_bluetooth_denoise_off_selected);
                ancLevelAnimatedOut();
            } else if (TextUtils.equals(this.mNoiseSelectedMode, "7")) {
                f2 = this.mDenoiseTrans.getX();
                this.mDenoiseTrans.setImageResource(R$drawable.nt_ic_bluetooth_denoise_trans_selected);
                ancLevelAnimatedOut();
            } else {
                f2 = this.mDenoiseOn.getX();
                this.mDenoiseOn.setImageResource(R$drawable.nt_ic_bluetooth_denoise_on_selected);
                ancLevelAnimatedIn();
            }
            Log.d("BluetoothSoundPreference", "startAnimator startX:" + f + ", endX:" + f2);
            if (f == f2) {
                this.mAnimatorView.setX(f);
                return;
            }
            this.mValueAnimator.setFloatValues(new float[]{f, f2});
            this.mValueAnimator.start();
        }
    }

    public void setRemoteDataAndUpdateUI(Bundle bundle) {
        this.mCallBackMode = bundle.getString("KEY_VALUE_STRING", "");
        Log.d("BluetoothSoundPreference", "result = " + this.mCallBackMode);
        updateUI(this.mCallBackMode);
    }

    private void updateUI(String str) {
        Log.d("BluetoothSoundPreference", "updateUI mode = " + str + ", mIsClickMode:" + this.mIsClickMode + ", mLastNoiseSelectedMode:" + this.mLastNoiseSelectedMode + ", mIsBindView:" + this.mIsBindView);
        if (this.mIsClickMode) {
            startAnimator();
            startBottomAncAnimator();
        } else {
            View view = this.mAnimatorView;
            if (view != null) {
                view.setBackground((Drawable) null);
                this.mBottomAncAnimatorView.setImageDrawable((Drawable) null);
            }
        }
        this.mLastNoiseSelectedMode = str;
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 49:
                if (str.equals("1")) {
                    c = 0;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    c = 1;
                    break;
                }
                break;
            case 51:
                if (str.equals("3")) {
                    c = 2;
                    break;
                }
                break;
            case 52:
                if (str.equals("4")) {
                    c = 3;
                    break;
                }
                break;
            case 53:
                if (str.equals("5")) {
                    c = 4;
                    break;
                }
                break;
            case 55:
                if (str.equals("7")) {
                    c = 5;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                if (!this.mIsClickMode) {
                    setNoiseOnSelected();
                    setNoiseOnHighSelected();
                }
                this.mNoiseOnSelectedMode = "1";
                break;
            case 1:
                if (!this.mIsClickMode) {
                    setNoiseOnSelected();
                    setNoiseOnMidSelected();
                }
                this.mNoiseOnSelectedMode = "2";
                break;
            case 2:
                if (!this.mIsClickMode) {
                    setNoiseOnSelected();
                    setNoiseOnLowSelected();
                }
                this.mNoiseOnSelectedMode = "3";
                break;
            case 3:
                if (!this.mIsClickMode) {
                    setNoiseOnSelected();
                    setNoiseOnAdaptiveSelected();
                }
                this.mNoiseOnSelectedMode = "4";
                break;
            case 4:
                if (!this.mIsClickMode) {
                    setNoiseOffSelected();
                    break;
                }
                break;
            case 5:
                if (!this.mIsClickMode) {
                    setNoiseTransSelected();
                    break;
                }
                break;
        }
        this.mIsClickMode = false;
    }
}
