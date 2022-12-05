package com.android.systemui.tuner;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import com.android.systemui.Dependency;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.PluginEnablerImpl;
import com.android.systemui.shared.plugins.PluginEnabler;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.shared.plugins.PluginPrefs;
import com.android.systemui.tuner.PluginFragment;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class PluginFragment extends PreferenceFragment {
    private PluginEnabler mPluginEnabler;
    private PluginPrefs mPluginPrefs;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.tuner.PluginFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            PluginFragment.this.loadPrefs();
        }
    };

    @Override // androidx.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        getContext().registerReceiver(this.mReceiver, intentFilter);
        getContext().registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.USER_UNLOCKED"));
    }

    @Override // android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(this.mReceiver);
    }

    @Override // androidx.preference.PreferenceFragment
    public void onCreatePreferences(Bundle bundle, String str) {
        this.mPluginEnabler = new PluginEnablerImpl(getContext());
        loadPrefs();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadPrefs() {
        final PluginManager pluginManager = (PluginManager) Dependency.get(PluginManager.class);
        final PreferenceScreen createPreferenceScreen = getPreferenceManager().createPreferenceScreen(getContext());
        createPreferenceScreen.setOrderingAsAdded(false);
        final Context context = getPreferenceManager().getContext();
        this.mPluginPrefs = new PluginPrefs(getContext());
        PackageManager packageManager = getContext().getPackageManager();
        Set<String> pluginList = this.mPluginPrefs.getPluginList();
        final ArrayMap arrayMap = new ArrayMap();
        for (String str : pluginList) {
            String name = toName(str);
            for (ResolveInfo resolveInfo : packageManager.queryIntentServices(new Intent(str), 512)) {
                String str2 = resolveInfo.serviceInfo.packageName;
                if (!arrayMap.containsKey(str2)) {
                    arrayMap.put(str2, new ArraySet());
                }
                ((ArraySet) arrayMap.get(str2)).add(name);
            }
        }
        packageManager.getPackagesHoldingPermissions(new String[]{"com.android.systemui.permission.PLUGIN"}, 516).forEach(new Consumer() { // from class: com.android.systemui.tuner.PluginFragment$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PluginFragment.this.lambda$loadPrefs$0(arrayMap, pluginManager, context, createPreferenceScreen, (PackageInfo) obj);
            }
        });
        setPreferenceScreen(createPreferenceScreen);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadPrefs$0(ArrayMap arrayMap, PluginManager pluginManager, Context context, PreferenceScreen preferenceScreen, PackageInfo packageInfo) {
        if (arrayMap.containsKey(packageInfo.packageName) && !ArrayUtils.contains(pluginManager.getWhitelistedPlugins(), packageInfo.packageName)) {
            PluginPreference pluginPreference = new PluginPreference(context, packageInfo, this.mPluginEnabler);
            pluginPreference.setSummary("Plugins: " + toString((ArraySet) arrayMap.get(packageInfo.packageName)));
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
        String[] split;
        String replace = str.replace("com.android.systemui.action.PLUGIN_", "");
        StringBuilder sb = new StringBuilder();
        for (String str2 : replace.split("_")) {
            if (sb.length() != 0) {
                sb.append(' ');
            }
            sb.append(str2.substring(0, 1));
            sb.append(str2.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class PluginPreference extends SwitchPreference {
        private final boolean mHasSettings;
        private final PackageInfo mInfo;
        private final PluginEnabler mPluginEnabler;

        public PluginPreference(Context context, PackageInfo packageInfo, PluginEnabler pluginEnabler) {
            super(context);
            PackageManager packageManager = context.getPackageManager();
            this.mHasSettings = packageManager.resolveActivity(new Intent("com.android.systemui.action.PLUGIN_SETTINGS").setPackage(packageInfo.packageName), 0) != null;
            this.mInfo = packageInfo;
            this.mPluginEnabler = pluginEnabler;
            setTitle(packageInfo.applicationInfo.loadLabel(packageManager));
            setChecked(isPluginEnabled());
            setWidgetLayoutResource(R$layout.tuner_widget_settings_switch);
        }

        private boolean isPluginEnabled() {
            for (int i = 0; i < this.mInfo.services.length; i++) {
                PackageInfo packageInfo = this.mInfo;
                if (!this.mPluginEnabler.isEnabled(new ComponentName(packageInfo.packageName, packageInfo.services[i].name))) {
                    return false;
                }
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.preference.Preference
        public boolean persistBoolean(boolean z) {
            PackageInfo packageInfo;
            int i = 0;
            boolean z2 = false;
            while (true) {
                packageInfo = this.mInfo;
                if (i >= packageInfo.services.length) {
                    break;
                }
                PackageInfo packageInfo2 = this.mInfo;
                ComponentName componentName = new ComponentName(packageInfo2.packageName, packageInfo2.services[i].name);
                if (this.mPluginEnabler.isEnabled(componentName) != z) {
                    if (z) {
                        this.mPluginEnabler.setEnabled(componentName);
                    } else {
                        this.mPluginEnabler.setDisabled(componentName, 1);
                    }
                    z2 = true;
                }
                i++;
            }
            if (z2) {
                String str = packageInfo.packageName;
                Uri uri = null;
                if (str != null) {
                    uri = Uri.fromParts("package", str, null);
                }
                getContext().sendBroadcast(new Intent("com.android.systemui.action.PLUGIN_CHANGED", uri));
            }
            return true;
        }

        @Override // androidx.preference.SwitchPreference, androidx.preference.Preference
        public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            super.onBindViewHolder(preferenceViewHolder);
            int i = R$id.settings;
            int i2 = 0;
            preferenceViewHolder.findViewById(i).setVisibility(this.mHasSettings ? 0 : 8);
            View findViewById = preferenceViewHolder.findViewById(R$id.divider);
            if (!this.mHasSettings) {
                i2 = 8;
            }
            findViewById.setVisibility(i2);
            preferenceViewHolder.findViewById(i).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.tuner.PluginFragment$PluginPreference$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    PluginFragment.PluginPreference.this.lambda$onBindViewHolder$0(view);
                }
            });
            preferenceViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.tuner.PluginFragment$PluginPreference$$ExternalSyntheticLambda1
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    boolean lambda$onBindViewHolder$1;
                    lambda$onBindViewHolder$1 = PluginFragment.PluginPreference.this.lambda$onBindViewHolder$1(view);
                    return lambda$onBindViewHolder$1;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
            ResolveInfo resolveActivity = view.getContext().getPackageManager().resolveActivity(new Intent("com.android.systemui.action.PLUGIN_SETTINGS").setPackage(this.mInfo.packageName), 0);
            if (resolveActivity != null) {
                Context context = view.getContext();
                Intent intent = new Intent();
                ActivityInfo activityInfo = resolveActivity.activityInfo;
                context.startActivity(intent.setComponent(new ComponentName(activityInfo.packageName, activityInfo.name)));
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$onBindViewHolder$1(View view) {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", this.mInfo.packageName, null));
            getContext().startActivity(intent);
            return true;
        }
    }
}
