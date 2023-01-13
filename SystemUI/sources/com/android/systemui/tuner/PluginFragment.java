package com.android.systemui.tuner;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.view.View;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.SwitchPreference;
import com.android.internal.util.ArrayUtils;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.plugins.PluginEnablerImpl;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.shared.plugins.PluginEnabler;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.plugins.PluginPrefs;
import java.util.Iterator;
import java.util.Set;
import sun.util.locale.BaseLocale;

public class PluginFragment extends PreferenceFragment {
    public static final String ACTION_PLUGIN_SETTINGS = "com.android.systemui.action.PLUGIN_SETTINGS";
    private PluginEnabler mPluginEnabler;
    private PluginPrefs mPluginPrefs;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            PluginFragment.this.loadPrefs();
        }
    };

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        getContext().registerReceiver(this.mReceiver, intentFilter);
        getContext().registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.USER_UNLOCKED"));
    }

    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(this.mReceiver);
    }

    public void onCreatePreferences(Bundle bundle, String str) {
        this.mPluginEnabler = new PluginEnablerImpl(getContext());
        loadPrefs();
    }

    /* access modifiers changed from: private */
    public void loadPrefs() {
        PluginManager pluginManager = (PluginManager) Dependency.get(PluginManager.class);
        PreferenceScreen createPreferenceScreen = getPreferenceManager().createPreferenceScreen(getContext());
        createPreferenceScreen.setOrderingAsAdded(false);
        Context context = getPreferenceManager().getContext();
        this.mPluginPrefs = new PluginPrefs(getContext());
        PackageManager packageManager = getContext().getPackageManager();
        Set<String> pluginList = this.mPluginPrefs.getPluginList();
        ArrayMap arrayMap = new ArrayMap();
        for (String next : pluginList) {
            String name = toName(next);
            for (ResolveInfo resolveInfo : packageManager.queryIntentServices(new Intent(next), 512)) {
                String str = resolveInfo.serviceInfo.packageName;
                if (!arrayMap.containsKey(str)) {
                    arrayMap.put(str, new ArraySet());
                }
                ((ArraySet) arrayMap.get(str)).add(name);
            }
        }
        packageManager.getPackagesHoldingPermissions(new String[]{PluginActionManager.PLUGIN_PERMISSION}, 516).forEach(new PluginFragment$$ExternalSyntheticLambda0(this, arrayMap, pluginManager, context, createPreferenceScreen));
        setPreferenceScreen(createPreferenceScreen);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$loadPrefs$0$com-android-systemui-tuner-PluginFragment  reason: not valid java name */
    public /* synthetic */ void m3265lambda$loadPrefs$0$comandroidsystemuitunerPluginFragment(ArrayMap arrayMap, PluginManager pluginManager, Context context, PreferenceScreen preferenceScreen, PackageInfo packageInfo) {
        if (arrayMap.containsKey(packageInfo.packageName) && !ArrayUtils.contains(pluginManager.getPrivilegedPlugins(), packageInfo.packageName)) {
            PluginPreference pluginPreference = new PluginPreference(context, packageInfo, this.mPluginEnabler);
            pluginPreference.setSummary((CharSequence) "Plugins: " + toString((ArraySet) arrayMap.get(packageInfo.packageName)));
            preferenceScreen.addPreference(pluginPreference);
        }
    }

    private String toString(ArraySet<String> arraySet) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = arraySet.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (sb.length() != 0) {
                sb.append(", ");
            }
            sb.append(next);
        }
        return sb.toString();
    }

    private String toName(String str) {
        String replace = str.replace((CharSequence) "com.android.systemui.action.PLUGIN_", (CharSequence) "");
        StringBuilder sb = new StringBuilder();
        for (String str2 : replace.split(BaseLocale.SEP)) {
            if (sb.length() != 0) {
                sb.append(' ');
            }
            sb.append(str2.substring(0, 1));
            sb.append(str2.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    private static class PluginPreference extends SwitchPreference {
        private final boolean mHasSettings;
        private final PackageInfo mInfo;
        private final PluginEnabler mPluginEnabler;

        public PluginPreference(Context context, PackageInfo packageInfo, PluginEnabler pluginEnabler) {
            super(context);
            PackageManager packageManager = context.getPackageManager();
            this.mHasSettings = packageManager.resolveActivity(new Intent(PluginFragment.ACTION_PLUGIN_SETTINGS).setPackage(packageInfo.packageName), 0) != null;
            this.mInfo = packageInfo;
            this.mPluginEnabler = pluginEnabler;
            setTitle(packageInfo.applicationInfo.loadLabel(packageManager));
            setChecked(isPluginEnabled());
            setWidgetLayoutResource(C1894R.layout.tuner_widget_settings_switch);
        }

        private boolean isPluginEnabled() {
            for (ServiceInfo serviceInfo : this.mInfo.services) {
                if (!this.mPluginEnabler.isEnabled(new ComponentName(this.mInfo.packageName, serviceInfo.name))) {
                    return false;
                }
            }
            return true;
        }

        /* access modifiers changed from: protected */
        public boolean persistBoolean(boolean z) {
            boolean z2 = false;
            for (ServiceInfo serviceInfo : this.mInfo.services) {
                ComponentName componentName = new ComponentName(this.mInfo.packageName, serviceInfo.name);
                if (this.mPluginEnabler.isEnabled(componentName) != z) {
                    if (z) {
                        this.mPluginEnabler.setEnabled(componentName);
                    } else {
                        this.mPluginEnabler.setDisabled(componentName, 1);
                    }
                    z2 = true;
                }
            }
            if (z2) {
                String str = this.mInfo.packageName;
                Uri uri = null;
                if (str != null) {
                    uri = Uri.fromParts("package", str, (String) null);
                }
                getContext().sendBroadcast(new Intent(PluginManager.PLUGIN_CHANGED, uri));
            }
            return true;
        }

        public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            super.onBindViewHolder(preferenceViewHolder);
            int i = 0;
            preferenceViewHolder.findViewById(C1894R.C1898id.settings).setVisibility(this.mHasSettings ? 0 : 8);
            View findViewById = preferenceViewHolder.findViewById(C1894R.C1898id.divider);
            if (!this.mHasSettings) {
                i = 8;
            }
            findViewById.setVisibility(i);
            preferenceViewHolder.findViewById(C1894R.C1898id.settings).setOnClickListener(new PluginFragment$PluginPreference$$ExternalSyntheticLambda0(this));
            preferenceViewHolder.itemView.setOnLongClickListener(new PluginFragment$PluginPreference$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBindViewHolder$0$com-android-systemui-tuner-PluginFragment$PluginPreference */
        public /* synthetic */ void mo46436x7725852a(View view) {
            ResolveInfo resolveActivity = view.getContext().getPackageManager().resolveActivity(new Intent(PluginFragment.ACTION_PLUGIN_SETTINGS).setPackage(this.mInfo.packageName), 0);
            if (resolveActivity != null) {
                view.getContext().startActivity(new Intent().setComponent(new ComponentName(resolveActivity.activityInfo.packageName, resolveActivity.activityInfo.name)));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBindViewHolder$1$com-android-systemui-tuner-PluginFragment$PluginPreference */
        public /* synthetic */ boolean mo46437x7d295089(View view) {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", this.mInfo.packageName, (String) null));
            getContext().startActivity(intent);
            return true;
        }
    }
}
