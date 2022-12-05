package com.android.settings.development.bluetooth;

import android.content.Context;
import android.util.AttributeSet;
import com.android.settings.R;
/* loaded from: classes.dex */
public class BluetoothLHDCQualityDialogPreference extends BaseBluetoothDialogPreference {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.development.bluetooth.BaseBluetoothDialogPreference
    public int getDefaultIndex() {
        return 9;
    }

    public BluetoothLHDCQualityDialogPreference(Context context) {
        super(context);
        initialize(context);
    }

    public BluetoothLHDCQualityDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize(context);
    }

    public BluetoothLHDCQualityDialogPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initialize(context);
    }

    public BluetoothLHDCQualityDialogPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initialize(context);
    }

    @Override // com.android.settings.development.bluetooth.BaseBluetoothDialogPreference
    protected int getRadioButtonGroupId() {
        return R.id.bluetooth_lhdc_audio_quality_radio_group;
    }

    private void initialize(Context context) {
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_low0));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_low1));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_low2));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_low3));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_low4));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_low));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_mid));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_high));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_high1));
        this.mRadioButtonIds.add(Integer.valueOf(R.id.bluetooth_lhdc_audio_quality_best_effort));
        for (String str : context.getResources().getStringArray(R.array.bluetooth_a2dp_codec_lhdc_playback_quality_titles)) {
            this.mRadioButtonStrings.add(str);
        }
        for (String str2 : context.getResources().getStringArray(R.array.bluetooth_a2dp_codec_lhdc_playback_quality_summaries)) {
            this.mSummaryStrings.add(str2);
        }
    }
}
