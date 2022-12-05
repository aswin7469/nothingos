package com.android.settings.security.trustagent;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Slog;
import android.util.Xml;
import com.android.internal.R;
import com.android.internal.widget.LockPatternUtils;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class TrustAgentManager {
    static final String PERMISSION_PROVIDE_AGENT = "android.permission.PROVIDE_TRUST_AGENT";
    private static final Intent TRUST_AGENT_INTENT = new Intent("android.service.trust.TrustAgentService");

    /* loaded from: classes.dex */
    public static class TrustAgentComponentInfo {
        public RestrictedLockUtils.EnforcedAdmin admin = null;
        public ComponentName componentName;
        public String summary;
        public String title;
    }

    public boolean shouldProvideTrust(ResolveInfo resolveInfo, PackageManager packageManager) {
        String str = resolveInfo.serviceInfo.packageName;
        if (packageManager.checkPermission(PERMISSION_PROVIDE_AGENT, str) != 0) {
            Log.w("TrustAgentManager", "Skipping agent because package " + str + " does not have permission " + PERMISSION_PROVIDE_AGENT + ".");
            return false;
        }
        return true;
    }

    public CharSequence getActiveTrustAgentLabel(Context context, LockPatternUtils lockPatternUtils) {
        List<TrustAgentComponentInfo> activeTrustAgents = getActiveTrustAgents(context, lockPatternUtils);
        if (activeTrustAgents.isEmpty()) {
            return null;
        }
        return activeTrustAgents.get(0).title;
    }

    public List<TrustAgentComponentInfo> getActiveTrustAgents(Context context, LockPatternUtils lockPatternUtils) {
        int myUserId = UserHandle.myUserId();
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        PackageManager packageManager = context.getPackageManager();
        ArrayList arrayList = new ArrayList();
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(TRUST_AGENT_INTENT, 128);
        List enabledTrustAgents = lockPatternUtils.getEnabledTrustAgents(myUserId);
        RestrictedLockUtils.EnforcedAdmin checkIfKeyguardFeaturesDisabled = RestrictedLockUtilsInternal.checkIfKeyguardFeaturesDisabled(context, 16, myUserId);
        if (enabledTrustAgents != null && !enabledTrustAgents.isEmpty()) {
            for (ResolveInfo resolveInfo : queryIntentServices) {
                if (resolveInfo.serviceInfo != null && shouldProvideTrust(resolveInfo, packageManager)) {
                    TrustAgentComponentInfo settingsComponent = getSettingsComponent(packageManager, resolveInfo);
                    if (settingsComponent.componentName != null && enabledTrustAgents.contains(getComponentName(resolveInfo)) && !TextUtils.isEmpty(settingsComponent.title)) {
                        if (checkIfKeyguardFeaturesDisabled != null && devicePolicyManager.getTrustAgentConfiguration(null, getComponentName(resolveInfo)) == null) {
                            settingsComponent.admin = checkIfKeyguardFeaturesDisabled;
                        }
                        arrayList.add(settingsComponent);
                    }
                }
            }
        }
        return arrayList;
    }

    public ComponentName getComponentName(ResolveInfo resolveInfo) {
        if (resolveInfo == null || resolveInfo.serviceInfo == null) {
            return null;
        }
        ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        return new ComponentName(serviceInfo.packageName, serviceInfo.name);
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x009f, code lost:
        if (r2 == null) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0092, code lost:
        r2.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0099, code lost:
        if (r2 == null) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0090, code lost:
        if (r2 == null) goto L32;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private TrustAgentComponentInfo getSettingsComponent(PackageManager packageManager, ResolveInfo resolveInfo) {
        ServiceInfo serviceInfo;
        XmlResourceParser xmlResourceParser;
        String str;
        XmlResourceParser xmlResourceParser2 = null;
        ComponentName componentName = null;
        if (resolveInfo == null || (serviceInfo = resolveInfo.serviceInfo) == null || serviceInfo.metaData == null) {
            return null;
        }
        TrustAgentComponentInfo trustAgentComponentInfo = new TrustAgentComponentInfo();
        try {
            xmlResourceParser = resolveInfo.serviceInfo.loadXmlMetaData(packageManager, "android.service.trust.trustagent");
            try {
                try {
                } catch (Throwable th) {
                    th = th;
                    xmlResourceParser2 = xmlResourceParser;
                    if (xmlResourceParser2 != null) {
                        xmlResourceParser2.close();
                    }
                    throw th;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e = e;
                str = null;
            } catch (IOException e2) {
                e = e2;
                str = null;
            } catch (XmlPullParserException e3) {
                e = e3;
                str = null;
            }
        } catch (PackageManager.NameNotFoundException e4) {
            e = e4;
            xmlResourceParser = null;
            str = null;
        } catch (IOException e5) {
            e = e5;
            xmlResourceParser = null;
            str = null;
        } catch (XmlPullParserException e6) {
            e = e6;
            xmlResourceParser = null;
            str = null;
        } catch (Throwable th2) {
            th = th2;
        }
        if (xmlResourceParser == null) {
            Slog.w("TrustAgentManager", "Can't find android.service.trust.trustagent meta-data");
            if (xmlResourceParser != null) {
                xmlResourceParser.close();
            }
            return null;
        }
        Resources resourcesForApplication = packageManager.getResourcesForApplication(resolveInfo.serviceInfo.applicationInfo);
        AttributeSet asAttributeSet = Xml.asAttributeSet(xmlResourceParser);
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 1 || next == 2) {
                break;
            }
        }
        if (!"trust-agent".equals(xmlResourceParser.getName())) {
            Slog.w("TrustAgentManager", "Meta-data does not start with trust-agent tag");
            xmlResourceParser.close();
            return null;
        }
        TypedArray obtainAttributes = resourcesForApplication.obtainAttributes(asAttributeSet, R.styleable.TrustAgent);
        trustAgentComponentInfo.summary = obtainAttributes.getString(1);
        trustAgentComponentInfo.title = obtainAttributes.getString(0);
        str = obtainAttributes.getString(2);
        try {
            obtainAttributes.recycle();
            xmlResourceParser.close();
            e = null;
        } catch (PackageManager.NameNotFoundException e7) {
            e = e7;
        } catch (IOException e8) {
            e = e8;
        } catch (XmlPullParserException e9) {
            e = e9;
        }
        if (e != null) {
            Slog.w("TrustAgentManager", "Error parsing : " + resolveInfo.serviceInfo.packageName, e);
            return null;
        }
        if (str != null && str.indexOf(47) < 0) {
            str = resolveInfo.serviceInfo.packageName + "/" + str;
        }
        if (str != null) {
            componentName = ComponentName.unflattenFromString(str);
        }
        trustAgentComponentInfo.componentName = componentName;
        return trustAgentComponentInfo;
    }
}
