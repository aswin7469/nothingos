package com.nothing.systemui.p024qs;

import android.content.Context;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import com.android.systemui.C1894R;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.privacy.PrivacyDialog;

/* renamed from: com.nothing.systemui.qs.MicModeView */
public class MicModeView extends LinearLayout {
    private static final int MIC_MODE_AMBIENT_SOUND = 2;
    private static final int MIC_MODE_STANDARD = 0;
    private static final int MIC_MODE_VOICE_FOCUS = 1;
    private static final String TAG = "MicModeView";
    private RadioButton mAmbientSoundButton;
    private String mAppPkg;
    private AudioManager mAudioManager;
    /* access modifiers changed from: private */
    public int mCheckedId;
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private int mMode;
    private PrivacyDialog.PrivacyElement mPrivacyElement;
    /* access modifiers changed from: private */
    public boolean mProtectFromCheckedChange;
    private RadioButton mStandardButton;
    private RadioButton mVoiceFocusButton;

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

    /* renamed from: com.nothing.systemui.qs.MicModeView$CheckedStateTracker */
    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        private CheckedStateTracker() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            if (!MicModeView.this.mProtectFromCheckedChange) {
                boolean unused = MicModeView.this.mProtectFromCheckedChange = true;
                if (MicModeView.this.mCheckedId != -1) {
                    MicModeView micModeView = MicModeView.this;
                    micModeView.setCheckedStateForView(micModeView.mCheckedId, false);
                }
                boolean unused2 = MicModeView.this.mProtectFromCheckedChange = false;
                MicModeView.this.setCheckedId(compoundButton.getId());
            }
        }
    }

    public MicModeView(Context context) {
        this(context, (AttributeSet) null);
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

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mStandardButton = (RadioButton) findViewById(C1894R.C1898id.mic_standard_radio);
        this.mVoiceFocusButton = (RadioButton) findViewById(C1894R.C1898id.voice_focus_radio);
        this.mAmbientSoundButton = (RadioButton) findViewById(C1894R.C1898id.ambient_sound_radio);
    }

    public void setPrivacyElement(PrivacyDialog.PrivacyElement privacyElement) {
        this.mPrivacyElement = privacyElement;
        this.mAppPkg = privacyElement.getPackageName();
        setMicMode(privacyElement.getMicModeInfo().getMicMode());
        initListener();
    }

    private void setMicMode(int i) {
        if (i != this.mMode) {
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
                if (!speakerMode) {
                    ((RelativeLayout) this.mStandardButton.getParent()).setVisibility(0);
                    this.mStandardButton.setEnabled(false);
                }
            } else if (i2 == 1) {
                this.mVoiceFocusButton.setChecked(true);
                this.mCheckedId = this.mVoiceFocusButton.getId();
                if (!speakerMode) {
                    ((RelativeLayout) this.mVoiceFocusButton.getParent()).setVisibility(0);
                    this.mVoiceFocusButton.setEnabled(false);
                }
            } else if (i2 == 2) {
                this.mAmbientSoundButton.setChecked(true);
                this.mCheckedId = this.mAmbientSoundButton.getId();
                if (!speakerMode) {
                    ((RelativeLayout) this.mAmbientSoundButton.getParent()).setVisibility(0);
                    this.mAmbientSoundButton.setEnabled(false);
                }
            }
        }
    }

    private void initListener() {
        CheckedStateTracker checkedStateTracker = new CheckedStateTracker();
        this.mChildOnCheckedChangeListener = checkedStateTracker;
        this.mStandardButton.setOnCheckedChangeListener(checkedStateTracker);
        this.mVoiceFocusButton.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
        this.mAmbientSoundButton.setOnCheckedChangeListener(this.mChildOnCheckedChangeListener);
    }

    /* access modifiers changed from: private */
    public void setCheckedStateForView(int i, boolean z) {
        View findViewById = findViewById(i);
        if (findViewById != null && (findViewById instanceof RadioButton)) {
            ((RadioButton) findViewById).setChecked(z);
        }
    }

    /* access modifiers changed from: private */
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
            Log.d(TAG, "set mode app = " + this.mAppPkg + ", mode =" + this.mMode);
            if (!TextUtils.isEmpty(this.mAppPkg)) {
                this.mAudioManager.setParameters("MIC_MODE=" + this.mMode);
            }
        }
    }

    private ArrayMap<String, String> configToMap(String str) {
        ArrayMap<String, String> arrayMap = new ArrayMap<>();
        if (!TextUtils.isEmpty(str)) {
            for (String str2 : str.split(NavigationBarInflaterView.BUTTON_SEPARATOR)) {
                if (!TextUtils.isEmpty(str2) && str2.contains("=")) {
                    String[] split = str2.split("=");
                    if (split.length > 1) {
                        arrayMap.put(split[0], split[1]);
                    }
                }
            }
        }
        return arrayMap;
    }

    private String mapToConfig(ArrayMap arrayMap) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrayMap.size(); i++) {
            if (i > 0) {
                sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            }
            sb.append(arrayMap.keyAt(i) + "=" + arrayMap.valueAt(i));
        }
        return sb.toString();
    }
}
