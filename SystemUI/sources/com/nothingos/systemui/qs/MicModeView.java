package com.nothingos.systemui.qs;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import com.android.systemui.R$id;
import com.android.systemui.privacy.PrivacyDialog;
/* loaded from: classes2.dex */
public class MicModeView extends LinearLayout {
    private RadioButton mAmbientSoundButton;
    private String mAppPkg;
    private AudioManager mAudioManager;
    private int mCheckedId;
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private int mMode;
    private PrivacyDialog.PrivacyElement mPrivacyElement;
    private boolean mProtectFromCheckedChange;
    private RadioButton mStandardButton;
    private RadioButton mVoiceFocusButton;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        private CheckedStateTracker() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (MicModeView.this.mProtectFromCheckedChange) {
                return;
            }
            MicModeView.this.mProtectFromCheckedChange = true;
            if (MicModeView.this.mCheckedId != -1) {
                MicModeView micModeView = MicModeView.this;
                micModeView.setCheckedStateForView(micModeView.mCheckedId, false);
            }
            MicModeView.this.mProtectFromCheckedChange = false;
            MicModeView.this.setCheckedId(compoundButton.getId());
        }
    }

    public MicModeView(Context context) {
        this(context, null);
    }

    public MicModeView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public MicModeView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mProtectFromCheckedChange = false;
        this.mCheckedId = -1;
        this.mMode = -1;
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mStandardButton = (RadioButton) findViewById(R$id.mic_standard_radio);
        this.mVoiceFocusButton = (RadioButton) findViewById(R$id.voice_focus_radio);
        this.mAmbientSoundButton = (RadioButton) findViewById(R$id.ambient_sound_radio);
    }

    public void setPrivacyElement(PrivacyDialog.PrivacyElement privacyElement) {
        this.mPrivacyElement = privacyElement;
        this.mAppPkg = privacyElement.getPackageName();
        setMicMode(privacyElement.getMicModeInfo().getMicMode());
        initListener();
    }

    private void setMicMode(int i) {
        if (i == this.mMode) {
            return;
        }
        this.mMode = i;
        boolean speakerMode = this.mPrivacyElement.getMicModeInfo().getSpeakerMode();
        if (!speakerMode) {
            ((RelativeLayout) this.mStandardButton.getParent()).setVisibility(8);
            ((RelativeLayout) this.mVoiceFocusButton.getParent()).setVisibility(8);
            ((RelativeLayout) this.mAmbientSoundButton.getParent()).setVisibility(8);
        }
        int i2 = this.mMode;
        if (i2 == 0) {
            this.mStandardButton.setChecked(true);
            this.mCheckedId = this.mStandardButton.getId();
            if (speakerMode) {
                return;
            }
            ((RelativeLayout) this.mStandardButton.getParent()).setVisibility(0);
            this.mStandardButton.setEnabled(false);
        } else if (i2 == 1) {
            this.mVoiceFocusButton.setChecked(true);
            this.mCheckedId = this.mVoiceFocusButton.getId();
            if (speakerMode) {
                return;
            }
            ((RelativeLayout) this.mVoiceFocusButton.getParent()).setVisibility(0);
            this.mVoiceFocusButton.setEnabled(false);
        } else if (i2 != 2) {
        } else {
            this.mAmbientSoundButton.setChecked(true);
            this.mCheckedId = this.mAmbientSoundButton.getId();
            if (speakerMode) {
                return;
            }
            ((RelativeLayout) this.mAmbientSoundButton.getParent()).setVisibility(0);
            this.mAmbientSoundButton.setEnabled(false);
        }
    }

    private void initListener() {
        CheckedStateTracker checkedStateTracker = new CheckedStateTracker();
        this.mChildOnCheckedChangeListener = checkedStateTracker;
        this.mStandardButton.setOnCheckedChangeListener(checkedStateTracker);
        this.mVoiceFocusButton.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
        this.mAmbientSoundButton.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCheckedStateForView(int i, boolean z) {
        View findViewById = findViewById(i);
        if (findViewById == null || !(findViewById instanceof RadioButton)) {
            return;
        }
        ((RadioButton) findViewById).setChecked(z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCheckedId(int i) {
        boolean z = i != this.mCheckedId;
        this.mCheckedId = i;
        if (z) {
            if (i == this.mStandardButton.getId()) {
                this.mMode = 0;
            } else if (this.mCheckedId == this.mVoiceFocusButton.getId()) {
                this.mMode = 1;
            } else if (this.mCheckedId == this.mAmbientSoundButton.getId()) {
                this.mMode = 2;
            }
            this.mPrivacyElement.getMicModeInfo().setMicMode(this.mMode);
            Log.d("MicModeView", "set mode app = " + this.mAppPkg + ", mode =" + this.mMode);
            if (TextUtils.isEmpty(this.mAppPkg)) {
                return;
            }
            AudioManager audioManager = this.mAudioManager;
            audioManager.setParameters("MIC_MODE=" + this.mMode);
        }
    }
}
