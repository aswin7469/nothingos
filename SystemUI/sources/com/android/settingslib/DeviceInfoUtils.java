package com.android.settingslib;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build;
import android.system.C0308Os;
import android.system.StructUtsname;
import android.telephony.PhoneNumberUtils;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import java.p026io.BufferedReader;
import java.p026io.FileReader;
import java.p026io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceInfoUtils {
    private static final String FILENAME_MSV = "/sys/board_properties/soc/msv";
    private static final String TAG = "DeviceInfoUtils";

    private static String readLine(String str) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(str), 256);
        try {
            return bufferedReader.readLine();
        } finally {
            bufferedReader.close();
        }
    }

    public static String getFormattedKernelVersion(Context context) {
        return formatKernelVersion(context, C0308Os.uname());
    }

    static String formatKernelVersion(Context context, StructUtsname structUtsname) {
        if (structUtsname == null) {
            return context.getString(C1757R.string.status_unavailable);
        }
        Matcher matcher = Pattern.compile("(#\\d+) (?:.*?)?((Sun|Mon|Tue|Wed|Thu|Fri|Sat).+)").matcher(structUtsname.version);
        if (matcher.matches()) {
            return structUtsname.release + "\n" + matcher.group(1) + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + matcher.group(2);
        }
        Log.e(TAG, "Regex did not match on uname version " + structUtsname.version);
        return context.getString(C1757R.string.status_unavailable);
    }

    public static String getMsvSuffix() {
        try {
            return Long.parseLong(readLine(FILENAME_MSV), 16) == 0 ? " (ENGINEERING)" : "";
        } catch (IOException | NumberFormatException unused) {
            return "";
        }
    }

    public static String getFeedbackReporterPackage(Context context) {
        String string = context.getResources().getString(C1757R.string.oem_preferred_feedback_reporter);
        if (TextUtils.isEmpty(string)) {
            return string;
        }
        Intent intent = new Intent("android.intent.action.BUG_REPORT");
        PackageManager packageManager = context.getPackageManager();
        for (ResolveInfo next : packageManager.queryIntentActivities(intent, 64)) {
            if (next.activityInfo != null && !TextUtils.isEmpty(next.activityInfo.packageName)) {
                try {
                    if ((packageManager.getApplicationInfo(next.activityInfo.packageName, 0).flags & 1) != 0 && TextUtils.equals(next.activityInfo.packageName, string)) {
                        return string;
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                    continue;
                }
            }
        }
        return null;
    }

    public static String getSecurityPatch() {
        String str = Build.VERSION.SECURITY_PATCH;
        if ("".equals(str)) {
            return null;
        }
        try {
            return DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), "dMMMMyyyy"), new SimpleDateFormat("yyyy-MM-dd").parse(str)).toString();
        } catch (ParseException unused) {
            return str;
        }
    }

    public static String getFormattedPhoneNumber(Context context, SubscriptionInfo subscriptionInfo) {
        if (subscriptionInfo != null) {
            String line1Number = ((TelephonyManager) context.getSystemService(TelephonyManager.class)).createForSubscriptionId(subscriptionInfo.getSubscriptionId()).getLine1Number();
            if (!TextUtils.isEmpty(line1Number)) {
                return PhoneNumberUtils.formatNumber(line1Number);
            }
        }
        return null;
    }

    public static String getFormattedPhoneNumbers(Context context, List<SubscriptionInfo> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(TelephonyManager.class);
            list.size();
            for (SubscriptionInfo subscriptionId : list) {
                String line1Number = telephonyManager.createForSubscriptionId(subscriptionId.getSubscriptionId()).getLine1Number();
                if (!TextUtils.isEmpty(line1Number)) {
                    sb.append(PhoneNumberUtils.formatNumber(line1Number)).append("\n");
                }
            }
        }
        return sb.toString();
    }

    public static String getBidiFormattedPhoneNumber(Context context, SubscriptionInfo subscriptionInfo) {
        return BidiFormatter.getInstance().unicodeWrap(getFormattedPhoneNumber(context, subscriptionInfo), TextDirectionHeuristics.LTR);
    }
}
