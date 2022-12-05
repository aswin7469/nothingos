package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.UserHandle;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextClock;
import com.android.systemui.R$id;
/* loaded from: classes2.dex */
public class SplitClockView extends LinearLayout {
    private TextClock mAmPmView;
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.SplitClockView.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.TIME_SET".equals(action) || "android.intent.action.TIMEZONE_CHANGED".equals(action) || "android.intent.action.LOCALE_CHANGED".equals(action) || "android.intent.action.CONFIGURATION_CHANGED".equals(action) || "android.intent.action.USER_SWITCHED".equals(action)) {
                SplitClockView.this.updatePatterns();
            }
        }
    };
    private TextClock mTimeView;

    public SplitClockView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mTimeView = (TextClock) findViewById(R$id.time_view);
        this.mAmPmView = (TextClock) findViewById(R$id.am_pm_view);
        this.mTimeView.setShowCurrentUserTime(true);
        this.mAmPmView.setShowCurrentUserTime(true);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        getContext().registerReceiverAsUser(this.mIntentReceiver, UserHandle.ALL, intentFilter, null, null);
        updatePatterns();
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(this.mIntentReceiver);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePatterns() {
        String substring;
        String substring2;
        String timeFormatString = DateFormat.getTimeFormatString(getContext(), ActivityManager.getCurrentUser());
        int amPmPartEndIndex = getAmPmPartEndIndex(timeFormatString);
        if (amPmPartEndIndex == -1) {
            substring2 = "";
            substring = timeFormatString;
        } else {
            substring = timeFormatString.substring(0, amPmPartEndIndex);
            substring2 = timeFormatString.substring(amPmPartEndIndex);
        }
        this.mTimeView.setFormat12Hour(substring);
        this.mTimeView.setFormat24Hour(substring);
        this.mTimeView.setContentDescriptionFormat12Hour(timeFormatString);
        this.mTimeView.setContentDescriptionFormat24Hour(timeFormatString);
        this.mAmPmView.setFormat12Hour(substring2);
        this.mAmPmView.setFormat24Hour(substring2);
    }

    private static int getAmPmPartEndIndex(String str) {
        int length = str.length() - 1;
        boolean z = false;
        for (int i = length; i >= 0; i--) {
            char charAt = str.charAt(i);
            boolean z2 = charAt == 'a';
            boolean isWhitespace = Character.isWhitespace(charAt);
            if (z2) {
                z = true;
            }
            if (!z2 && !isWhitespace) {
                if (i == length || !z) {
                    return -1;
                }
                return i + 1;
            }
        }
        return z ? 0 : -1;
    }
}
