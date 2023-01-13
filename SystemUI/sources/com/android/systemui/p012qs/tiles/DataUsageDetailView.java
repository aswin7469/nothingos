package com.android.systemui.p012qs.tiles;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiEnterpriseConfig;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.C1894R;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.p012qs.DataUsageGraph;
import java.text.DecimalFormat;

/* renamed from: com.android.systemui.qs.tiles.DataUsageDetailView */
public class DataUsageDetailView extends LinearLayout {

    /* renamed from: GB */
    private static final double f333GB = 1.073741824E9d;

    /* renamed from: KB */
    private static final double f334KB = 1024.0d;

    /* renamed from: MB */
    private static final double f335MB = 1048576.0d;
    private final DecimalFormat FORMAT = new DecimalFormat("#.##");

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

    public DataUsageDetailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        FontSizeUtils.updateFontSize(this, 16908310, C1894R.dimen.qs_data_usage_text_size);
        FontSizeUtils.updateFontSize(this, C1894R.C1898id.usage_text, C1894R.dimen.qs_data_usage_usage_text_size);
        FontSizeUtils.updateFontSize(this, C1894R.C1898id.usage_carrier_text, C1894R.dimen.qs_data_usage_text_size);
        FontSizeUtils.updateFontSize(this, C1894R.C1898id.usage_info_top_text, C1894R.dimen.qs_data_usage_text_size);
        FontSizeUtils.updateFontSize(this, C1894R.C1898id.usage_period_text, C1894R.dimen.qs_data_usage_text_size);
        FontSizeUtils.updateFontSize(this, C1894R.C1898id.usage_info_bottom_text, C1894R.dimen.qs_data_usage_text_size);
    }

    public void bind(DataUsageController.DataUsageInfo dataUsageInfo) {
        long j;
        int i;
        String str;
        String str2;
        DataUsageController.DataUsageInfo dataUsageInfo2 = dataUsageInfo;
        Resources resources = this.mContext.getResources();
        boolean z = true;
        int i2 = 0;
        ColorStateList colorStateList = null;
        if (dataUsageInfo2.usageLevel < dataUsageInfo2.warningLevel || dataUsageInfo2.limitLevel <= 0) {
            j = dataUsageInfo2.usageLevel;
            str = resources.getString(C1894R.string.quick_settings_cellular_detail_data_warning, new Object[]{formatBytes(dataUsageInfo2.warningLevel)});
            i = C1894R.string.quick_settings_cellular_detail_data_usage;
            str2 = null;
        } else if (dataUsageInfo2.usageLevel <= dataUsageInfo2.limitLevel) {
            j = dataUsageInfo2.limitLevel - dataUsageInfo2.usageLevel;
            str = resources.getString(C1894R.string.quick_settings_cellular_detail_data_used, new Object[]{formatBytes(dataUsageInfo2.usageLevel)});
            str2 = resources.getString(C1894R.string.quick_settings_cellular_detail_data_limit, new Object[]{formatBytes(dataUsageInfo2.limitLevel)});
            i = C1894R.string.quick_settings_cellular_detail_remaining_data;
        } else {
            j = dataUsageInfo2.usageLevel - dataUsageInfo2.limitLevel;
            str = resources.getString(C1894R.string.quick_settings_cellular_detail_data_used, new Object[]{formatBytes(dataUsageInfo2.usageLevel)});
            String string = resources.getString(C1894R.string.quick_settings_cellular_detail_data_limit, new Object[]{formatBytes(dataUsageInfo2.limitLevel)});
            ColorStateList colorError = Utils.getColorError(this.mContext);
            i = C1894R.string.quick_settings_cellular_detail_over_limit;
            String str3 = string;
            colorStateList = colorError;
            str2 = str3;
        }
        if (colorStateList == null) {
            colorStateList = Utils.getColorAccent(this.mContext);
        }
        ((TextView) findViewById(16908310)).setText(i);
        TextView textView = (TextView) findViewById(C1894R.C1898id.usage_text);
        textView.setText(formatBytes(j));
        textView.setTextColor(colorStateList);
        DataUsageGraph dataUsageGraph = (DataUsageGraph) findViewById(C1894R.C1898id.usage_graph);
        dataUsageGraph.setLevels(dataUsageInfo2.limitLevel, dataUsageInfo2.warningLevel, dataUsageInfo2.usageLevel);
        ((TextView) findViewById(C1894R.C1898id.usage_carrier_text)).setText(dataUsageInfo2.carrier);
        ((TextView) findViewById(C1894R.C1898id.usage_period_text)).setText(dataUsageInfo2.period);
        TextView textView2 = (TextView) findViewById(C1894R.C1898id.usage_info_top_text);
        textView2.setVisibility(str != null ? 0 : 8);
        textView2.setText(str);
        TextView textView3 = (TextView) findViewById(C1894R.C1898id.usage_info_bottom_text);
        textView3.setVisibility(str2 != null ? 0 : 8);
        textView3.setText(str2);
        if (dataUsageInfo2.warningLevel <= 0 && dataUsageInfo2.limitLevel <= 0) {
            z = false;
        }
        if (!z) {
            i2 = 8;
        }
        dataUsageGraph.setVisibility(i2);
        if (!z) {
            textView2.setVisibility(8);
        }
    }

    public static String formatBytes(long j) {
        String str;
        double d;
        double abs = (double) Math.abs(j);
        if (abs > 1.048576E8d) {
            d = abs / f333GB;
            str = "GB";
        } else if (abs > 102400.0d) {
            d = abs / f335MB;
            str = "MB";
        } else {
            d = abs / f334KB;
            str = "KB";
        }
        return new DecimalFormat("#.##").format(d * ((double) (j < 0 ? -1 : 1))) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + str;
    }
}
