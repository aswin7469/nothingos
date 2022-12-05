package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.qs.DataUsageGraph;
import java.text.DecimalFormat;
/* loaded from: classes.dex */
public class DataUsageDetailView extends LinearLayout {
    private final DecimalFormat FORMAT = new DecimalFormat("#.##");

    public DataUsageDetailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = R$dimen.qs_data_usage_text_size;
        FontSizeUtils.updateFontSize(this, 16908310, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_text, R$dimen.qs_data_usage_usage_text_size);
        FontSizeUtils.updateFontSize(this, R$id.usage_carrier_text, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_info_top_text, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_period_text, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_info_bottom_text, i);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x008b  */
    /* JADX WARN: Removed duplicated region for block: B:13:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0100  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x011c  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0122  */
    /* JADX WARN: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00ef  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void bind(DataUsageController.DataUsageInfo dataUsageInfo) {
        String string;
        int i;
        String str;
        ColorStateList colorStateList;
        Resources resources = ((LinearLayout) this).mContext.getResources();
        long j = dataUsageInfo.usageLevel;
        long j2 = dataUsageInfo.warningLevel;
        boolean z = true;
        int i2 = 0;
        if (j >= j2) {
            long j3 = dataUsageInfo.limitLevel;
            if (j3 > 0) {
                if (j <= j3) {
                    i = R$string.quick_settings_cellular_detail_remaining_data;
                    long j4 = j3 - j;
                    String string2 = resources.getString(R$string.quick_settings_cellular_detail_data_used, formatBytes(j));
                    str = resources.getString(R$string.quick_settings_cellular_detail_data_limit, formatBytes(dataUsageInfo.limitLevel));
                    string = string2;
                    j = j4;
                    colorStateList = null;
                    if (colorStateList == null) {
                        colorStateList = Utils.getColorAccent(((LinearLayout) this).mContext);
                    }
                    ((TextView) findViewById(16908310)).setText(i);
                    TextView textView = (TextView) findViewById(R$id.usage_text);
                    textView.setText(formatBytes(j));
                    textView.setTextColor(colorStateList);
                    DataUsageGraph dataUsageGraph = (DataUsageGraph) findViewById(R$id.usage_graph);
                    dataUsageGraph.setLevels(dataUsageInfo.limitLevel, dataUsageInfo.warningLevel, dataUsageInfo.usageLevel);
                    ((TextView) findViewById(R$id.usage_carrier_text)).setText(dataUsageInfo.carrier);
                    ((TextView) findViewById(R$id.usage_period_text)).setText(dataUsageInfo.period);
                    TextView textView2 = (TextView) findViewById(R$id.usage_info_top_text);
                    textView2.setVisibility(string != null ? 0 : 8);
                    textView2.setText(string);
                    TextView textView3 = (TextView) findViewById(R$id.usage_info_bottom_text);
                    textView3.setVisibility(str != null ? 0 : 8);
                    textView3.setText(str);
                    if (dataUsageInfo.warningLevel <= 0 && dataUsageInfo.limitLevel <= 0) {
                        z = false;
                    }
                    if (!z) {
                        i2 = 8;
                    }
                    dataUsageGraph.setVisibility(i2);
                    if (z) {
                        return;
                    }
                    textView2.setVisibility(8);
                    return;
                }
                i = R$string.quick_settings_cellular_detail_over_limit;
                long j5 = j - j3;
                String string3 = resources.getString(R$string.quick_settings_cellular_detail_data_used, formatBytes(j));
                String string4 = resources.getString(R$string.quick_settings_cellular_detail_data_limit, formatBytes(dataUsageInfo.limitLevel));
                colorStateList = Utils.getColorError(((LinearLayout) this).mContext);
                string = string3;
                j = j5;
                str = string4;
                if (colorStateList == null) {
                }
                ((TextView) findViewById(16908310)).setText(i);
                TextView textView4 = (TextView) findViewById(R$id.usage_text);
                textView4.setText(formatBytes(j));
                textView4.setTextColor(colorStateList);
                DataUsageGraph dataUsageGraph2 = (DataUsageGraph) findViewById(R$id.usage_graph);
                dataUsageGraph2.setLevels(dataUsageInfo.limitLevel, dataUsageInfo.warningLevel, dataUsageInfo.usageLevel);
                ((TextView) findViewById(R$id.usage_carrier_text)).setText(dataUsageInfo.carrier);
                ((TextView) findViewById(R$id.usage_period_text)).setText(dataUsageInfo.period);
                TextView textView22 = (TextView) findViewById(R$id.usage_info_top_text);
                textView22.setVisibility(string != null ? 0 : 8);
                textView22.setText(string);
                TextView textView32 = (TextView) findViewById(R$id.usage_info_bottom_text);
                textView32.setVisibility(str != null ? 0 : 8);
                textView32.setText(str);
                if (dataUsageInfo.warningLevel <= 0) {
                    z = false;
                }
                if (!z) {
                }
                dataUsageGraph2.setVisibility(i2);
                if (z) {
                }
            }
        }
        int i3 = R$string.quick_settings_cellular_detail_data_usage;
        string = resources.getString(R$string.quick_settings_cellular_detail_data_warning, formatBytes(j2));
        i = i3;
        str = null;
        colorStateList = null;
        if (colorStateList == null) {
        }
        ((TextView) findViewById(16908310)).setText(i);
        TextView textView42 = (TextView) findViewById(R$id.usage_text);
        textView42.setText(formatBytes(j));
        textView42.setTextColor(colorStateList);
        DataUsageGraph dataUsageGraph22 = (DataUsageGraph) findViewById(R$id.usage_graph);
        dataUsageGraph22.setLevels(dataUsageInfo.limitLevel, dataUsageInfo.warningLevel, dataUsageInfo.usageLevel);
        ((TextView) findViewById(R$id.usage_carrier_text)).setText(dataUsageInfo.carrier);
        ((TextView) findViewById(R$id.usage_period_text)).setText(dataUsageInfo.period);
        TextView textView222 = (TextView) findViewById(R$id.usage_info_top_text);
        textView222.setVisibility(string != null ? 0 : 8);
        textView222.setText(string);
        TextView textView322 = (TextView) findViewById(R$id.usage_info_bottom_text);
        textView322.setVisibility(str != null ? 0 : 8);
        textView322.setText(str);
        if (dataUsageInfo.warningLevel <= 0) {
        }
        if (!z) {
        }
        dataUsageGraph22.setVisibility(i2);
        if (z) {
        }
    }

    private String formatBytes(long j) {
        double d;
        String str;
        double abs = Math.abs(j);
        if (abs > 1.048576E8d) {
            d = abs / 1.073741824E9d;
            str = "GB";
        } else if (abs > 102400.0d) {
            d = abs / 1048576.0d;
            str = "MB";
        } else {
            d = abs / 1024.0d;
            str = "KB";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.FORMAT.format(d * (j < 0 ? -1 : 1)));
        sb.append(" ");
        sb.append(str);
        return sb.toString();
    }
}
