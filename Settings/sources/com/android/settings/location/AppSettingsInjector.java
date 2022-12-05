package com.android.settings.location;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.UserHandle;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.location.InjectedSetting;
import com.android.settingslib.location.SettingsInjector;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class AppSettingsInjector extends SettingsInjector {
    private final int mMetricsCategory;
    private final MetricsFeatureProvider mMetricsFeatureProvider;

    public AppSettingsInjector(Context context, int i) {
        super(context);
        this.mMetricsCategory = i;
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settingslib.location.SettingsInjector
    public InjectedSetting parseServiceInfo(ResolveInfo resolveInfo, UserHandle userHandle, PackageManager packageManager) throws XmlPullParserException, IOException {
        InjectedSetting parseServiceInfo = super.parseServiceInfo(resolveInfo, userHandle, packageManager);
        ServiceInfo serviceInfo = resolveInfo.serviceInfo;
        if (parseServiceInfo == null || DimmableIZatIconPreference.showIzat(this.mContext, serviceInfo.packageName)) {
            return parseServiceInfo;
        }
        return null;
    }

    @Override // com.android.settingslib.location.SettingsInjector
    protected Preference createPreference(Context context, InjectedSetting injectedSetting) {
        if (TextUtils.isEmpty(injectedSetting.userRestriction)) {
            return DimmableIZatIconPreference.getAppPreference(context, injectedSetting);
        }
        return DimmableIZatIconPreference.getRestrictedAppPreference(context, injectedSetting);
    }

    @Override // com.android.settingslib.location.SettingsInjector
    protected void logPreferenceClick(Intent intent) {
        this.mMetricsFeatureProvider.logStartedIntent(intent, this.mMetricsCategory);
    }
}
