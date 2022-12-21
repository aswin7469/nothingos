package com.android.systemui.sensorprivacy.television;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorPrivacyManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.systemui.C1893R;
import com.android.systemui.p014tv.TvBottomSheetActivity;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import javax.inject.Inject;

public class TvUnblockSensorActivity extends TvBottomSheetActivity {
    private static final String ACTION_MANAGE_CAMERA_PRIVACY = "android.settings.MANAGE_CAMERA_PRIVACY";
    private static final String ACTION_MANAGE_MICROPHONE_PRIVACY = "android.settings.MANAGE_MICROPHONE_PRIVACY";
    private static final int ALL_SENSORS = Integer.MAX_VALUE;
    private static final String TAG = "TvUnblockSensorActivity";
    private Button mCancelButton;
    private TextView mContent;
    private ImageView mIcon;
    private Button mPositiveButton;
    private ImageView mSecondIcon;
    private int mSensor = -1;
    private IndividualSensorPrivacyController.Callback mSensorPrivacyCallback;
    private final IndividualSensorPrivacyController mSensorPrivacyController;
    private TextView mTitle;

    @Inject
    public TvUnblockSensorActivity(IndividualSensorPrivacyController individualSensorPrivacyController) {
        this.mSensorPrivacyController = individualSensorPrivacyController;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addSystemFlags(524288);
        if (getIntent().getBooleanExtra(SensorPrivacyManager.EXTRA_ALL_SENSORS, false)) {
            this.mSensor = Integer.MAX_VALUE;
        } else {
            this.mSensor = getIntent().getIntExtra(SensorPrivacyManager.EXTRA_SENSOR, -1);
        }
        if (this.mSensor == -1) {
            Log.v(TAG, "Invalid extras");
            finish();
            return;
        }
        this.mSensorPrivacyCallback = new TvUnblockSensorActivity$$ExternalSyntheticLambda1(this);
        initUI();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-android-systemui-sensorprivacy-television-TvUnblockSensorActivity */
    public /* synthetic */ void mo37693x11c64b3f(int i, boolean z) {
        if (this.mSensor == Integer.MAX_VALUE && !this.mSensorPrivacyController.isSensorBlocked(2) && !this.mSensorPrivacyController.isSensorBlocked(1)) {
            showToastAndFinish();
        } else if (this.mSensor != i || z) {
            updateUI();
        } else {
            showToastAndFinish();
        }
    }

    private void showToastAndFinish() {
        int i = this.mSensor;
        Toast.makeText(this, i != 1 ? i != 2 ? C1893R.string.sensor_privacy_mic_camera_unblocked_toast_content : C1893R.string.sensor_privacy_camera_unblocked_toast_content : C1893R.string.sensor_privacy_mic_unblocked_toast_content, 0).show();
        finish();
    }

    private boolean isBlockedByHardwareToggle() {
        int i = this.mSensor;
        if (i != Integer.MAX_VALUE) {
            return this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(i);
        }
        if (this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(2) || this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(1)) {
            return true;
        }
        return false;
    }

    private void initUI() {
        this.mTitle = (TextView) findViewById(C1893R.C1897id.bottom_sheet_title);
        this.mContent = (TextView) findViewById(C1893R.C1897id.bottom_sheet_body);
        this.mIcon = (ImageView) findViewById(C1893R.C1897id.bottom_sheet_icon);
        this.mSecondIcon = (ImageView) findViewById(C1893R.C1897id.bottom_sheet_second_icon);
        this.mPositiveButton = (Button) findViewById(C1893R.C1897id.bottom_sheet_positive_button);
        Button button = (Button) findViewById(C1893R.C1897id.bottom_sheet_negative_button);
        this.mCancelButton = button;
        button.setText(17039360);
        this.mCancelButton.setOnClickListener(new TvUnblockSensorActivity$$ExternalSyntheticLambda2(this));
        updateUI();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$initUI$1$com-android-systemui-sensorprivacy-television-TvUnblockSensorActivity */
    public /* synthetic */ void mo37692x8577dca9(View view) {
        finish();
    }

    private void updateUI() {
        if (isBlockedByHardwareToggle()) {
            updateUiForHardwareToggle();
        } else {
            updateUiForSoftwareToggle();
        }
    }

    private void updateUiForHardwareToggle() {
        Resources resources = getResources();
        int i = this.mSensor;
        boolean z = false;
        boolean z2 = (i == 1 || i == Integer.MAX_VALUE) && this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(1);
        int i2 = this.mSensor;
        if ((i2 == 2 || i2 == Integer.MAX_VALUE) && this.mSensorPrivacyController.isSensorBlockedByHardwareToggle(2)) {
            z = true;
        }
        setIconTint(resources.getBoolean(C1893R.bool.config_unblockHwSensorIconEnableTint));
        setIconSize(C1893R.dimen.unblock_hw_sensor_icon_width, C1893R.dimen.unblock_hw_sensor_icon_height);
        if (z2 && z) {
            this.mTitle.setText(C1893R.string.sensor_privacy_start_use_mic_camera_blocked_dialog_title);
            this.mContent.setText(C1893R.string.sensor_privacy_start_use_mic_camera_blocked_dialog_content);
            this.mIcon.setImageResource(C1893R.C1895drawable.unblock_hw_sensor_all);
            Drawable drawable = resources.getDrawable(C1893R.C1895drawable.unblock_hw_sensor_all_second, getTheme());
            if (drawable == null) {
                this.mSecondIcon.setVisibility(8);
            } else {
                this.mSecondIcon.setImageDrawable(drawable);
            }
        } else if (z) {
            this.mTitle.setText(C1893R.string.sensor_privacy_start_use_camera_blocked_dialog_title);
            this.mContent.setText(C1893R.string.sensor_privacy_start_use_camera_blocked_dialog_content);
            this.mIcon.setImageResource(C1893R.C1895drawable.unblock_hw_sensor_camera);
            this.mSecondIcon.setVisibility(8);
        } else if (z2) {
            this.mTitle.setText(C1893R.string.sensor_privacy_start_use_mic_blocked_dialog_title);
            this.mContent.setText(C1893R.string.sensor_privacy_start_use_mic_blocked_dialog_content);
            this.mIcon.setImageResource(C1893R.C1895drawable.unblock_hw_sensor_microphone);
            this.mSecondIcon.setVisibility(8);
        }
        Drawable drawable2 = this.mIcon.getDrawable();
        if (drawable2 instanceof Animatable) {
            ((Animatable) drawable2).start();
        }
        this.mPositiveButton.setVisibility(8);
        this.mCancelButton.setText(17039370);
    }

    private void updateUiForSoftwareToggle() {
        setIconTint(true);
        setIconSize(C1893R.dimen.bottom_sheet_icon_size, C1893R.dimen.bottom_sheet_icon_size);
        int i = this.mSensor;
        if (i == 1) {
            this.mTitle.setText(C1893R.string.sensor_privacy_start_use_mic_dialog_title);
            this.mContent.setText(C1893R.string.sensor_privacy_start_use_mic_dialog_content);
            this.mIcon.setImageResource(17303177);
            this.mSecondIcon.setVisibility(8);
        } else if (i != 2) {
            this.mTitle.setText(C1893R.string.sensor_privacy_start_use_mic_camera_dialog_title);
            this.mContent.setText(C1893R.string.sensor_privacy_start_use_mic_camera_dialog_content);
            this.mIcon.setImageResource(17303172);
            this.mSecondIcon.setImageResource(17303177);
        } else {
            this.mTitle.setText(C1893R.string.sensor_privacy_start_use_camera_dialog_title);
            this.mContent.setText(C1893R.string.sensor_privacy_start_use_camera_dialog_content);
            this.mIcon.setImageResource(17303172);
            this.mSecondIcon.setVisibility(8);
        }
        this.mPositiveButton.setText(17041475);
        this.mPositiveButton.setOnClickListener(new TvUnblockSensorActivity$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateUiForSoftwareToggle$2$com-android-systemui-sensorprivacy-television-TvUnblockSensorActivity */
    public /* synthetic */ void mo37694x873fca73(View view) {
        int i = this.mSensor;
        if (i == Integer.MAX_VALUE) {
            this.mSensorPrivacyController.setSensorBlocked(5, 2, false);
            this.mSensorPrivacyController.setSensorBlocked(5, 1, false);
            return;
        }
        this.mSensorPrivacyController.setSensorBlocked(5, i, false);
    }

    private void setIconTint(boolean z) {
        Resources resources = getResources();
        if (z) {
            ColorStateList colorStateList = resources.getColorStateList(C1893R.C1894color.bottom_sheet_icon_color, getTheme());
            this.mIcon.setImageTintList(colorStateList);
            this.mSecondIcon.setImageTintList(colorStateList);
        } else {
            this.mIcon.setImageTintList((ColorStateList) null);
            this.mSecondIcon.setImageTintList((ColorStateList) null);
        }
        this.mIcon.invalidate();
        this.mSecondIcon.invalidate();
    }

    private void setIconSize(int i, int i2) {
        Resources resources = getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(i);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(i2);
        this.mIcon.getLayoutParams().width = dimensionPixelSize;
        this.mIcon.getLayoutParams().height = dimensionPixelSize2;
        this.mIcon.invalidate();
        this.mSecondIcon.getLayoutParams().width = dimensionPixelSize;
        this.mSecondIcon.getLayoutParams().height = dimensionPixelSize2;
        this.mSecondIcon.invalidate();
    }

    public void onResume() {
        super.onResume();
        updateUI();
        this.mSensorPrivacyController.addCallback(this.mSensorPrivacyCallback);
    }

    public void onPause() {
        this.mSensorPrivacyController.removeCallback(this.mSensorPrivacyCallback);
        super.onPause();
    }
}
