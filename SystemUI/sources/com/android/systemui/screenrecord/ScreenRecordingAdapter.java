package com.android.systemui.screenrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.C1894R;
import java.util.List;

public class ScreenRecordingAdapter extends ArrayAdapter<ScreenRecordingAudioSource> {
    private LinearLayout mInternalOption;
    private LinearLayout mMicAndInternalOption;
    private LinearLayout mMicOption;
    private LinearLayout mSelectedInternal;
    private LinearLayout mSelectedMic;
    private LinearLayout mSelectedMicAndInternal;

    public ScreenRecordingAdapter(Context context, int i, List<ScreenRecordingAudioSource> list) {
        super(context, i, list);
        initViews();
    }

    private void initViews() {
        this.mSelectedInternal = getSelected(C1894R.string.screenrecord_device_audio_label);
        this.mSelectedMic = getSelected(C1894R.string.screenrecord_mic_label);
        this.mSelectedMicAndInternal = getSelected(C1894R.string.screenrecord_device_audio_and_mic_label);
        LinearLayout option = getOption(C1894R.string.screenrecord_mic_label, 0);
        this.mMicOption = option;
        option.removeViewAt(1);
        LinearLayout option2 = getOption(C1894R.string.screenrecord_device_audio_and_mic_label, 0);
        this.mMicAndInternalOption = option2;
        option2.removeViewAt(1);
        this.mInternalOption = getOption(C1894R.string.screenrecord_device_audio_label, C1894R.string.screenrecord_device_audio_description);
    }

    private LinearLayout getOption(int i, int i2) {
        LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(C1894R.layout.screen_record_dialog_audio_source, (ViewGroup) null, false);
        ((TextView) linearLayout.findViewById(C1894R.C1898id.screen_recording_dialog_source_text)).setText(i);
        if (i2 != 0) {
            ((TextView) linearLayout.findViewById(C1894R.C1898id.screen_recording_dialog_source_description)).setText(i2);
        }
        return linearLayout;
    }

    private LinearLayout getSelected(int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(C1894R.layout.screen_record_dialog_audio_source_selected, (ViewGroup) null, false);
        ((TextView) linearLayout.findViewById(C1894R.C1898id.screen_recording_dialog_source_text)).setText(i);
        return linearLayout;
    }

    /* renamed from: com.android.systemui.screenrecord.ScreenRecordingAdapter$1 */
    static /* synthetic */ class C24351 {

        /* renamed from: $SwitchMap$com$android$systemui$screenrecord$ScreenRecordingAudioSource */
        static final /* synthetic */ int[] f336x73e7906a;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|(3:5|6|8)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        static {
            /*
                com.android.systemui.screenrecord.ScreenRecordingAudioSource[] r0 = com.android.systemui.screenrecord.ScreenRecordingAudioSource.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f336x73e7906a = r0
                com.android.systemui.screenrecord.ScreenRecordingAudioSource r1 = com.android.systemui.screenrecord.ScreenRecordingAudioSource.INTERNAL     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f336x73e7906a     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.screenrecord.ScreenRecordingAudioSource r1 = com.android.systemui.screenrecord.ScreenRecordingAudioSource.MIC_AND_INTERNAL     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f336x73e7906a     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.screenrecord.ScreenRecordingAudioSource r1 = com.android.systemui.screenrecord.ScreenRecordingAudioSource.MIC     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.screenrecord.ScreenRecordingAdapter.C24351.<clinit>():void");
        }
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        int i2 = C24351.f336x73e7906a[((ScreenRecordingAudioSource) getItem(i)).ordinal()];
        if (i2 == 1) {
            return this.mInternalOption;
        }
        if (i2 == 2) {
            return this.mMicAndInternalOption;
        }
        if (i2 != 3) {
            return super.getDropDownView(i, view, viewGroup);
        }
        return this.mMicOption;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        int i2 = C24351.f336x73e7906a[((ScreenRecordingAudioSource) getItem(i)).ordinal()];
        if (i2 == 1) {
            return this.mSelectedInternal;
        }
        if (i2 == 2) {
            return this.mSelectedMicAndInternal;
        }
        if (i2 != 3) {
            return super.getView(i, view, viewGroup);
        }
        return this.mSelectedMic;
    }
}
