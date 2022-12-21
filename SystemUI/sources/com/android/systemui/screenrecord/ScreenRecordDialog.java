package com.android.systemui.screenrecord;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import com.android.systemui.C1893R;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.util.Arrays;
import java.util.List;

public class ScreenRecordDialog extends SystemUIDialog {
    private static final long DELAY_MS = 3000;
    private static final long INTERVAL_MS = 1000;
    private static final List<ScreenRecordingAudioSource> MODES = Arrays.asList(ScreenRecordingAudioSource.INTERNAL, ScreenRecordingAudioSource.MIC, ScreenRecordingAudioSource.MIC_AND_INTERNAL);
    private Switch mAudioSwitch;
    private final RecordingController mController;
    private final Runnable mOnStartRecordingClicked;
    private Spinner mOptions;
    private Switch mTapsSwitch;
    private final UserContextProvider mUserContextProvider;

    public ScreenRecordDialog(Context context, RecordingController recordingController, UserContextProvider userContextProvider, Runnable runnable) {
        super(context);
        this.mController = recordingController;
        this.mUserContextProvider = userContextProvider;
        this.mOnStartRecordingClicked = runnable;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.addPrivateFlags(16);
        window.setGravity(17);
        setTitle(C1893R.string.screenrecord_name);
        setContentView(C1893R.layout.screen_record_dialog);
        ((TextView) findViewById(C1893R.C1897id.button_cancel)).setOnClickListener(new ScreenRecordDialog$$ExternalSyntheticLambda0(this));
        ((TextView) findViewById(C1893R.C1897id.button_start)).setOnClickListener(new ScreenRecordDialog$$ExternalSyntheticLambda1(this));
        this.mAudioSwitch = (Switch) findViewById(C1893R.C1897id.screenrecord_audio_switch);
        this.mTapsSwitch = (Switch) findViewById(C1893R.C1897id.screenrecord_taps_switch);
        this.mOptions = (Spinner) findViewById(C1893R.C1897id.screen_recording_options);
        ScreenRecordingAdapter screenRecordingAdapter = new ScreenRecordingAdapter(getContext().getApplicationContext(), 17367049, MODES);
        screenRecordingAdapter.setDropDownViewResource(17367049);
        this.mOptions.setAdapter(screenRecordingAdapter);
        this.mOptions.setOnItemClickListenerInt(new ScreenRecordDialog$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-android-systemui-screenrecord-ScreenRecordDialog */
    public /* synthetic */ void mo37271x1b01bd75(View view) {
        dismiss();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$com-android-systemui-screenrecord-ScreenRecordDialog */
    public /* synthetic */ void mo37272x351d3c14(View view) {
        Runnable runnable = this.mOnStartRecordingClicked;
        if (runnable != null) {
            runnable.run();
        }
        requestScreenCapture();
        dismiss();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$2$com-android-systemui-screenrecord-ScreenRecordDialog */
    public /* synthetic */ void mo37273x4f38bab3(AdapterView adapterView, View view, int i, long j) {
        this.mAudioSwitch.setChecked(true);
    }

    private void requestScreenCapture() {
        ScreenRecordingAudioSource screenRecordingAudioSource;
        Context userContext = this.mUserContextProvider.getUserContext();
        boolean isChecked = this.mTapsSwitch.isChecked();
        if (this.mAudioSwitch.isChecked()) {
            screenRecordingAudioSource = (ScreenRecordingAudioSource) this.mOptions.getSelectedItem();
        } else {
            screenRecordingAudioSource = ScreenRecordingAudioSource.NONE;
        }
        this.mController.startCountdown(3000, 1000, PendingIntent.getForegroundService(userContext, 2, RecordingService.getStartIntent(userContext, -1, screenRecordingAudioSource.ordinal(), isChecked), 201326592), PendingIntent.getService(userContext, 2, RecordingService.getStopIntent(userContext), 201326592));
    }
}
