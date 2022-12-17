package com.android.settings.development.bluetooth;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import com.android.settings.R$array;
import com.android.settings.R$id;

public class BluetoothCodecDialogPreference extends BaseBluetoothDialogPreference {
    public BluetoothCodecDialogPreference(Context context) {
        super(context);
        initialize(context);
    }

    public BluetoothCodecDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
    }

    public BluetoothCodecDialogPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context);
    }

    public BluetoothCodecDialogPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initialize(context);
    }

    /* access modifiers changed from: protected */
    public int getRadioButtonGroupId() {
        return R$id.bluetooth_audio_codec_radio_group;
    }

    private void initialize(Context context) {
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_default));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_sbc));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_aac));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_aptx));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_aptx_hd));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_aptx_adaptive));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_ldac));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_lhdcv1));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_lhdcv2));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_lhdcv3));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_lhdcv5));
        this.mRadioButtonIds.add(Integer.valueOf(R$id.bluetooth_audio_codec_aptx_twsp));
        String[] stringArray = context.getResources().getStringArray(R$array.bluetooth_a2dp_codec_titles);
        Log.e("BtCodecDlgPref", "a2dp_codec_titles array length: " + stringArray.length);
        for (int i = 0; i < stringArray.length; i++) {
            this.mRadioButtonStrings.add(stringArray[i]);
        }
        String[] stringArray2 = context.getResources().getStringArray(R$array.bluetooth_a2dp_codec_summaries);
        Log.e("BtCodecDlgPref", "a2dp_codec_summaries array length: " + stringArray2.length);
        for (String add : stringArray2) {
            this.mSummaryStrings.add(add);
        }
    }
}
