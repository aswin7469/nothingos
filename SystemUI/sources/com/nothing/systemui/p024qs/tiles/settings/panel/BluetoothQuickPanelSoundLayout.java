package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.systemui.C1893R;
import com.nothing.p023os.device.DeviceConstant;
import com.nothing.p023os.device.DeviceServiceController;
import com.nothing.systemui.util.NTLogUtil;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.BluetoothQuickPanelSoundLayout */
public class BluetoothQuickPanelSoundLayout extends LinearLayout implements View.OnClickListener {
    private static final int ANC_HIGH = 4;
    private static final int ANC_MID = 2;
    private static final String TAG = "BluetoothQuickPanelSoundLayout";
    private int mANCLevel;
    private SettingItemAdapter mAdapter;
    private String mAddress;
    private DeviceServiceController mController;
    private ImageView mDenoiseOff;
    private ImageView mDenoiseOn;
    private View mDenoiseOnAdaptive;
    private ImageView mDenoiseOnAdaptiveImage;
    private View mDenoiseOnHigh;
    private ImageView mDenoiseOnHighImage;
    private View mDenoiseOnLevel;
    private View mDenoiseOnLow;
    private ImageView mDenoiseOnLowImage;
    private View mDenoiseOnMid;
    private ImageView mDenoiseOnMidImage;
    private ImageView mDenoiseTrans;
    private String mNoiseOnSelectedMode;
    private String mNoiseSelectedMode;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public BluetoothQuickPanelSoundLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public BluetoothQuickPanelSoundLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BluetoothQuickPanelSoundLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public BluetoothQuickPanelSoundLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mNoiseOnSelectedMode = "1";
        this.mANCLevel = 2;
        this.mNoiseSelectedMode = null;
    }

    public void setAddress(String str) {
        this.mAddress = str;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mDenoiseOn = (ImageView) findViewById(C1893R.C1897id.denoise_on);
        this.mDenoiseOff = (ImageView) findViewById(C1893R.C1897id.denoise_off);
        this.mDenoiseTrans = (ImageView) findViewById(C1893R.C1897id.denoise_trans);
        this.mDenoiseOnHighImage = (ImageView) findViewById(C1893R.C1897id.denoise_on_high_image);
        this.mDenoiseOnHigh = findViewById(C1893R.C1897id.denoise_on_high);
        this.mDenoiseOnLowImage = (ImageView) findViewById(C1893R.C1897id.denoise_on_low_image);
        this.mDenoiseOnLow = findViewById(C1893R.C1897id.denoise_on_low);
        this.mDenoiseOnLevel = findViewById(C1893R.C1897id.denoise_on_level);
        this.mDenoiseOnMidImage = (ImageView) findViewById(C1893R.C1897id.denoise_on_mid_image);
        this.mDenoiseOnMid = findViewById(C1893R.C1897id.denoise_on_mid);
        this.mDenoiseOnAdaptiveImage = (ImageView) findViewById(C1893R.C1897id.denoise_on_adaptive_image);
        this.mDenoiseOnAdaptive = findViewById(C1893R.C1897id.denoise_on_adaptive);
        this.mDenoiseOn.setOnClickListener(this);
        this.mDenoiseOff.setOnClickListener(this);
        this.mDenoiseTrans.setOnClickListener(this);
        this.mDenoiseOnHigh.setOnClickListener(this);
        this.mDenoiseOnLow.setOnClickListener(this);
        this.mDenoiseOnMid.setOnClickListener(this);
        this.mDenoiseOnAdaptive.setOnClickListener(this);
    }

    public void setDeviceServiceController(DeviceServiceController deviceServiceController) {
        this.mController = deviceServiceController;
    }

    public void setItemAdapter(SettingItemAdapter settingItemAdapter) {
        this.mAdapter = settingItemAdapter;
    }

    private void setNoiseOnSelected() {
        ImageView imageView = this.mDenoiseOff;
        if (imageView != null && this.mDenoiseOn != null && this.mDenoiseTrans != null && this.mDenoiseOnLevel != null) {
            imageView.setBackground((Drawable) null);
            this.mDenoiseOff.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_off);
            this.mDenoiseOn.setBackgroundResource(C1893R.C1895drawable.nt_bluetooth_denoise_selected_background);
            this.mDenoiseOn.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_on_selected);
            this.mDenoiseTrans.setBackground((Drawable) null);
            this.mDenoiseTrans.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_trans);
            this.mDenoiseOnLevel.setVisibility(0);
        }
    }

    private void setNoiseOffSelected() {
        ImageView imageView = this.mDenoiseOff;
        if (imageView != null && this.mDenoiseOn != null && this.mDenoiseTrans != null && this.mDenoiseOnLevel != null) {
            imageView.setBackgroundResource(C1893R.C1895drawable.nt_bluetooth_denoise_selected_background);
            this.mDenoiseOff.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_off_selected);
            this.mDenoiseOn.setBackground((Drawable) null);
            this.mDenoiseOn.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_on);
            this.mDenoiseTrans.setBackground((Drawable) null);
            this.mDenoiseTrans.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_trans);
            this.mDenoiseOnLevel.setVisibility(8);
        }
    }

    private void setNoiseTransSelected() {
        ImageView imageView = this.mDenoiseOff;
        if (imageView != null && this.mDenoiseOn != null && this.mDenoiseTrans != null && this.mDenoiseOnLevel != null) {
            imageView.setBackground((Drawable) null);
            this.mDenoiseOff.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_off);
            this.mDenoiseOn.setBackground((Drawable) null);
            this.mDenoiseOn.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_on);
            this.mDenoiseTrans.setBackgroundResource(C1893R.C1895drawable.nt_bluetooth_denoise_selected_background);
            this.mDenoiseTrans.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_trans_selected);
            this.mDenoiseOnLevel.setVisibility(8);
        }
    }

    private void setNoiseOnHighSelected() {
        ImageView imageView = this.mDenoiseOnHighImage;
        if (imageView != null && this.mDenoiseOnLowImage != null && this.mDenoiseOnMid != null && this.mDenoiseOnAdaptiveImage != null && this.mDenoiseOnMidImage != null) {
            imageView.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_selected);
            this.mDenoiseOnLowImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            if (this.mDenoiseOnMid.getVisibility() == 0) {
                this.mDenoiseOnMidImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            }
            if (this.mDenoiseOnAdaptiveImage.getVisibility() == 0) {
                this.mDenoiseOnAdaptiveImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            }
        }
    }

    private void setNoiseOnLowSelected() {
        ImageView imageView = this.mDenoiseOnHighImage;
        if (imageView != null && this.mDenoiseOnLowImage != null && this.mDenoiseOnMid != null && this.mDenoiseOnAdaptiveImage != null && this.mDenoiseOnMidImage != null) {
            imageView.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            this.mDenoiseOnLowImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_selected);
            if (this.mDenoiseOnMid.getVisibility() == 0) {
                this.mDenoiseOnMidImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            }
            if (this.mDenoiseOnAdaptiveImage.getVisibility() == 0) {
                this.mDenoiseOnAdaptiveImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            }
        }
    }

    private void setNoiseOnMidSelected() {
        ImageView imageView = this.mDenoiseOnHighImage;
        if (imageView != null && this.mDenoiseOnLowImage != null && this.mDenoiseOnMidImage != null && this.mDenoiseOnAdaptiveImage != null) {
            imageView.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            this.mDenoiseOnLowImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            this.mDenoiseOnMidImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_selected);
            this.mDenoiseOnAdaptiveImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
        }
    }

    private void setNoiseOnAdaptiveSelected() {
        ImageView imageView = this.mDenoiseOnHighImage;
        if (imageView != null && this.mDenoiseOnLowImage != null && this.mDenoiseOnMidImage != null && this.mDenoiseOnAdaptiveImage != null) {
            imageView.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            this.mDenoiseOnLowImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            this.mDenoiseOnMidImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_unselected);
            this.mDenoiseOnAdaptiveImage.setImageResource(C1893R.C1895drawable.nt_ic_bluetooth_denoise_sound_selected);
        }
    }

    public void onClick(View view) {
        try {
            if (this.mController != null) {
                int id = view.getId();
                if (id == C1893R.C1897id.denoise_off) {
                    Bundle bundle = new Bundle();
                    bundle.putString(DeviceConstant.KEY_MAC_ADDRESS, this.mAddress);
                    bundle.putString(DeviceConstant.KEY_VALUE_STRING, DeviceConstant.NOISE_CANCELLATION_OFF);
                    this.mController.setCommand(DeviceConstant.ORDER_ANC, bundle);
                    setNoiseOffSelected();
                    this.mNoiseSelectedMode = DeviceConstant.NOISE_CANCELLATION_OFF;
                } else if (id == C1893R.C1897id.denoise_on) {
                    Bundle bundle2 = new Bundle();
                    bundle2.putString(DeviceConstant.KEY_VALUE_STRING, this.mNoiseOnSelectedMode);
                    bundle2.putString(DeviceConstant.KEY_MAC_ADDRESS, this.mAddress);
                    this.mController.setCommand(DeviceConstant.ORDER_ANC, bundle2);
                    setNoiseOnSelected();
                    if ("3".equals(this.mNoiseOnSelectedMode)) {
                        setNoiseOnLowSelected();
                    } else if (DeviceConstant.NOISE_CANCELLATION_ADAPTIVE.equals(this.mNoiseOnSelectedMode)) {
                        setNoiseOnAdaptiveSelected();
                    } else if ("2".equals(this.mNoiseOnSelectedMode)) {
                        setNoiseOnMidSelected();
                    } else {
                        setNoiseOnHighSelected();
                    }
                    this.mNoiseSelectedMode = this.mNoiseOnSelectedMode;
                } else if (id == C1893R.C1897id.denoise_on_low) {
                    Bundle bundle3 = new Bundle();
                    bundle3.putString(DeviceConstant.KEY_VALUE_STRING, "3");
                    bundle3.putString(DeviceConstant.KEY_MAC_ADDRESS, this.mAddress);
                    this.mController.setCommand(DeviceConstant.ORDER_ANC, bundle3);
                    setNoiseOnLowSelected();
                    this.mNoiseOnSelectedMode = "3";
                    this.mNoiseSelectedMode = "3";
                } else if (id == C1893R.C1897id.denoise_on_high) {
                    Bundle bundle4 = new Bundle();
                    bundle4.putString(DeviceConstant.KEY_VALUE_STRING, "1");
                    bundle4.putString(DeviceConstant.KEY_MAC_ADDRESS, this.mAddress);
                    this.mController.setCommand(DeviceConstant.ORDER_ANC, bundle4);
                    setNoiseOnHighSelected();
                    this.mNoiseOnSelectedMode = "1";
                    this.mNoiseSelectedMode = "1";
                } else if (id == C1893R.C1897id.denoise_trans) {
                    Bundle bundle5 = new Bundle();
                    bundle5.putString(DeviceConstant.KEY_VALUE_STRING, DeviceConstant.NOISE_CANCELLATION_TRANSPARENCY);
                    bundle5.putString(DeviceConstant.KEY_MAC_ADDRESS, this.mAddress);
                    this.mController.setCommand(DeviceConstant.ORDER_ANC, bundle5);
                    setNoiseTransSelected();
                    this.mNoiseSelectedMode = DeviceConstant.NOISE_CANCELLATION_TRANSPARENCY;
                } else if (id == C1893R.C1897id.denoise_on_mid) {
                    Bundle bundle6 = new Bundle();
                    bundle6.putString(DeviceConstant.KEY_VALUE_STRING, "2");
                    bundle6.putString(DeviceConstant.KEY_MAC_ADDRESS, this.mAddress);
                    this.mController.setCommand(DeviceConstant.ORDER_ANC, bundle6);
                    setNoiseOnMidSelected();
                    this.mNoiseOnSelectedMode = "2";
                    this.mNoiseSelectedMode = "2";
                } else if (id == C1893R.C1897id.denoise_on_adaptive) {
                    Bundle bundle7 = new Bundle();
                    bundle7.putString(DeviceConstant.KEY_VALUE_STRING, DeviceConstant.NOISE_CANCELLATION_ADAPTIVE);
                    bundle7.putString(DeviceConstant.KEY_MAC_ADDRESS, this.mAddress);
                    this.mController.setCommand(DeviceConstant.ORDER_ANC, bundle7);
                    setNoiseOnAdaptiveSelected();
                    this.mNoiseOnSelectedMode = DeviceConstant.NOISE_CANCELLATION_ADAPTIVE;
                    this.mNoiseSelectedMode = DeviceConstant.NOISE_CANCELLATION_ADAPTIVE;
                }
                NTLogUtil.m1680d(TAG, "onclick mNoiseSelectedMode = " + this.mNoiseSelectedMode);
            }
        } catch (Exception unused) {
        }
    }

    public void setAncLevel(int i) {
        this.mANCLevel = i;
        if (i == 4) {
            this.mDenoiseOnMid.setVisibility(0);
            this.mDenoiseOnAdaptive.setVisibility(0);
        }
    }

    public void setRemoteDataAndUpdateUI(Bundle bundle) {
        String string = bundle.getString(DeviceConstant.KEY_VALUE_STRING, "");
        NTLogUtil.m1680d(TAG, "result = " + string + ", mNoiseSelectedMode:" + this.mNoiseSelectedMode);
        string.hashCode();
        char c = 65535;
        switch (string.hashCode()) {
            case 49:
                if (string.equals("1")) {
                    c = 0;
                    break;
                }
                break;
            case 50:
                if (string.equals("2")) {
                    c = 1;
                    break;
                }
                break;
            case 51:
                if (string.equals("3")) {
                    c = 2;
                    break;
                }
                break;
            case 52:
                if (string.equals(DeviceConstant.NOISE_CANCELLATION_ADAPTIVE)) {
                    c = 3;
                    break;
                }
                break;
            case 53:
                if (string.equals(DeviceConstant.NOISE_CANCELLATION_OFF)) {
                    c = 4;
                    break;
                }
                break;
            case 55:
                if (string.equals(DeviceConstant.NOISE_CANCELLATION_TRANSPARENCY)) {
                    c = 5;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                setNoiseOnSelected();
                setNoiseOnHighSelected();
                this.mNoiseOnSelectedMode = "1";
                return;
            case 1:
                setNoiseOnSelected();
                setNoiseOnMidSelected();
                this.mNoiseOnSelectedMode = "2";
                return;
            case 2:
                setNoiseOnSelected();
                setNoiseOnLowSelected();
                this.mNoiseOnSelectedMode = "3";
                return;
            case 3:
                setNoiseOnSelected();
                setNoiseOnAdaptiveSelected();
                this.mNoiseOnSelectedMode = DeviceConstant.NOISE_CANCELLATION_ADAPTIVE;
                return;
            case 4:
                setNoiseOffSelected();
                return;
            case 5:
                setNoiseTransSelected();
                return;
            default:
                return;
        }
    }
}
