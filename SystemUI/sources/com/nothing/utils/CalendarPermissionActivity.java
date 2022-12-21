package com.nothing.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.android.systemui.C1893R;
import com.nothing.systemui.util.NTLogUtil;

public class CalendarPermissionActivity extends Activity {
    private final int REQUEST_CODE = 1;
    private final String TAG = "PermissionActivity";
    private AlertDialog mGotoSettingDialog;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (hasCalendarReadPermission()) {
            NTLogUtil.m1680d("PermissionActivity", "CalendarReadPermission already granted");
        } else if (shouldShowRequestPermissionRationale("android.permission.READ_CALENDAR")) {
            requestCalendarReadPermission();
        } else {
            AlertDialog createGoToSettingDialog = createGoToSettingDialog();
            this.mGotoSettingDialog = createGoToSettingDialog;
            createGoToSettingDialog.setCanceledOnTouchOutside(false);
            this.mGotoSettingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    CalendarPermissionActivity.this.finish();
                }
            });
            this.mGotoSettingDialog.show();
        }
    }

    private void requestCalendarReadPermission() {
        requestPermissions(new String[]{"android.permission.READ_CALENDAR"}, 1);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 1) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                NTLogUtil.m1680d("PermissionActivity", "Not Granted");
            } else {
                NTLogUtil.m1680d("PermissionActivity", "Granted");
            }
            finish();
        }
    }

    private AlertDialog createGoToSettingDialog() {
        return new AlertDialog.Builder(this).setTitle(C1893R.string.quick_look_widget_calendar_setting_title).setMessage(C1893R.string.quick_look_widget_calendar_setting_msg).setNegativeButton(17039360, new CalendarPermissionActivity$$ExternalSyntheticLambda0(this)).setPositiveButton(C1893R.string.quick_look_widget_calendar_setting_btn_submit, new CalendarPermissionActivity$$ExternalSyntheticLambda1(this)).create();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createGoToSettingDialog$0$com-nothing-utils-CalendarPermissionActivity */
    public /* synthetic */ void mo58259x71977780(DialogInterface dialogInterface, int i) {
        finish();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createGoToSettingDialog$1$com-nothing-utils-CalendarPermissionActivity */
    public /* synthetic */ void mo58260x2b0f051f(DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
        } catch (Exception unused) {
            NTLogUtil.m1681e("PermissionActivity", "Start calendar event activity error.");
        }
    }

    private boolean hasCalendarReadPermission() {
        return checkSelfPermission("android.permission.READ_CALENDAR") == 0;
    }
}
