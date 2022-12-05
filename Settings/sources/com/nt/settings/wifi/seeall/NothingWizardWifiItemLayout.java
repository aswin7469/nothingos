package com.nt.settings.wifi.seeall;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settingslib.Utils;
import com.android.wifitrackerlib.WifiEntry;
/* loaded from: classes2.dex */
public class NothingWizardWifiItemLayout extends LinearLayout {
    public TextView mContentTextView;
    public ImageView mIconImageView;
    public TextView mTitleTextView;
    public WifiEntry mWifiInfo;

    public NothingWizardWifiItemLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mIconImageView = (ImageView) findViewById(R.id.layout_wifi_item_icon_imageview);
        this.mTitleTextView = (TextView) findViewById(R.id.layout_wifi_item_title_textview);
        this.mContentTextView = (TextView) findViewById(R.id.layout_wifi_item_content_textview);
        RippleUtils.getInstance().setRippleEffect(this);
    }

    public void showData(WifiEntry wifiEntry) {
        this.mWifiInfo = wifiEntry;
        this.mIconImageView.setImageResource(Utils.getWifiIconResource(Math.max(wifiEntry.getLevel(), 0), wifiEntry.getWifiStandard(), wifiEntry.canConnect()));
        this.mTitleTextView.setText(wifiEntry.getTitle());
        this.mContentTextView.setText(wifiEntry.getSecondSummary());
        String summary = wifiEntry.getSummary(false);
        if (wifiEntry.isPskSaeTransitionMode()) {
            summary = "WPA3(SAE Transition Mode) " + summary;
        } else if (wifiEntry.isOweTransitionMode()) {
            summary = "WPA3(OWE Transition Mode) " + summary;
        } else if (wifiEntry.getSecurity() == 5) {
            summary = "WPA3(SAE) " + summary;
        } else if (wifiEntry.getSecurity() == 4) {
            summary = "WPA3(OWE) " + summary;
        }
        this.mContentTextView.setText(summary);
        if (summary == null || summary.length() == 0) {
            this.mContentTextView.setVisibility(8);
        } else {
            this.mContentTextView.setVisibility(0);
        }
    }
}
