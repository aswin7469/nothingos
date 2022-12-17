package com.android.settings.network;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.android.settings.Settings;
import com.android.settings.network.telephony.MobileNetworkUtils;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class MobileNetworkIntentConverter implements Function<Intent, Intent> {
    private static final AtomicReference<String> mCachedClassName = new AtomicReference<>();
    private static final String[] sPotentialActions = {null, "android.intent.action.MAIN", "android.settings.NETWORK_OPERATOR_SETTINGS", "android.settings.DATA_ROAMING_SETTINGS", "android.settings.MMS_MESSAGE_SETTING", "android.telephony.ims.action.SHOW_CAPABILITY_DISCOVERY_OPT_IN"};
    private static final ComponentName sTargetComponent = ComponentName.createRelative("com.android.settings", Settings.MobileNetworkActivity.class.getTypeName());
    private final Context mAppContext;
    private final ComponentName mComponent;

    public MobileNetworkIntentConverter(Activity activity) {
        this.mAppContext = activity.getApplicationContext();
        this.mComponent = activity.getComponentName();
    }

    public Intent apply(Intent intent) {
        Function function;
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        if (isAttachedToExposedComponents()) {
            intent = convertFromDeepLink(intent);
        } else if (!mayRequireConvert(intent)) {
            return null;
        }
        String action = intent.getAction();
        int extractSubscriptionId = extractSubscriptionId(intent);
        Function identity = Function.identity();
        if (TextUtils.equals(action, "android.settings.NETWORK_OPERATOR_SETTINGS") || TextUtils.equals(action, "android.settings.DATA_ROAMING_SETTINGS")) {
            function = identity.andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda0(this, extractSubscriptionId)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda7(this, intent)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda8(this, extractSubscriptionId));
        } else if (TextUtils.equals(action, "android.settings.MMS_MESSAGE_SETTING")) {
            function = identity.andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda9(this, extractSubscriptionId)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda10(this)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda11(this, intent)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda12(this, extractSubscriptionId));
        } else if (TextUtils.equals(action, "android.telephony.ims.action.SHOW_CAPABILITY_DISCOVERY_OPT_IN")) {
            function = identity.andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda13(this, extractSubscriptionId)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda14(this, extractSubscriptionId)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda15(this, intent)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda1(this, extractSubscriptionId));
        } else if (sTargetComponent.compareTo(this.mComponent) != 0 || (action != null && !"android.intent.action.MAIN".equals(action))) {
            return null;
        } else {
            Log.d("MobileNetworkIntentConverter", "Support default actions direct to this component");
            function = identity.andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda2(this, extractSubscriptionId)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda3(this, intent)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda4(this)).andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda5(this, extractSubscriptionId));
        }
        if (!isAttachedToExposedComponents()) {
            function = function.andThen(new MobileNetworkIntentConverter$$ExternalSyntheticLambda6(this));
        }
        Intent intent2 = (Intent) function.apply(intent);
        if (intent2 != null) {
            long elapsedRealtimeNanos2 = SystemClock.elapsedRealtimeNanos();
            Log.d("MobileNetworkIntentConverter", this.mComponent.toString() + " intent conversion: " + (elapsedRealtimeNanos2 - elapsedRealtimeNanos) + " ns");
        }
        return intent2;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Intent lambda$apply$2(int i, Intent intent) {
        return updateFragment(intent, this.mAppContext, i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Intent lambda$apply$6(int i, Intent intent) {
        return updateFragment(intent, this.mAppContext, i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Bundle lambda$apply$8(int i, Bundle bundle) {
        return supportContactDiscoveryDialog(bundle, this.mAppContext, i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Intent lambda$apply$10(int i, Intent intent) {
        return updateFragment(intent, this.mAppContext, i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Intent lambda$apply$14(int i, Intent intent) {
        return updateFragment(intent, this.mAppContext, i);
    }

    /* access modifiers changed from: protected */
    public boolean isAttachedToExposedComponents() {
        return sTargetComponent.compareTo(this.mComponent) == 0;
    }

    /* access modifiers changed from: protected */
    public int extractSubscriptionId(Intent intent) {
        return intent.getIntExtra("android.provider.extra.SUB_ID", -1);
    }

    /* access modifiers changed from: protected */
    /* renamed from: extractArguments */
    public Bundle lambda$apply$7(Intent intent, int i) {
        Bundle bundle;
        Bundle bundleExtra = intent.getBundleExtra(":settings:show_fragment_args");
        if (bundleExtra == null) {
            bundle = new Bundle();
        }
        bundle.putParcelable("intent", intent);
        bundle.putInt("android.provider.extra.SUB_ID", i);
        return bundle;
    }

    /* access modifiers changed from: protected */
    /* renamed from: convertMmsArguments */
    public Bundle lambda$apply$4(Bundle bundle) {
        bundle.putString(":settings:fragment_args_key", "mms_message");
        return bundle;
    }

    /* access modifiers changed from: protected */
    public boolean mayShowContactDiscoveryDialog(Context context, int i) {
        return MobileNetworkUtils.isContactDiscoveryVisible(context, i) && !MobileNetworkUtils.isContactDiscoveryEnabled(context, i);
    }

    /* access modifiers changed from: protected */
    public Bundle supportContactDiscoveryDialog(Bundle bundle, Context context, int i) {
        boolean mayShowContactDiscoveryDialog = mayShowContactDiscoveryDialog(context, i);
        Log.d("MobileNetworkIntentConverter", "maybeShowContactDiscoveryDialog subId=" + i + ", show=" + mayShowContactDiscoveryDialog);
        bundle.putBoolean("show_capability_discovery_opt_in", mayShowContactDiscoveryDialog);
        return bundle;
    }

    /* access modifiers changed from: protected */
    /* renamed from: rePackIntent */
    public Intent lambda$apply$9(Bundle bundle, Intent intent) {
        Intent intent2 = new Intent(intent);
        intent2.setComponent(sTargetComponent);
        intent2.putExtra("android.provider.extra.SUB_ID", bundle.getInt("android.provider.extra.SUB_ID"));
        intent2.putExtra(":settings:show_fragment_args", bundle);
        return intent2;
    }

    /* access modifiers changed from: protected */
    /* renamed from: replaceIntentAction */
    public Intent lambda$apply$13(Intent intent) {
        intent.setAction("android.settings.NETWORK_OPERATOR_SETTINGS");
        return intent;
    }

    /* access modifiers changed from: protected */
    public CharSequence getFragmentTitle(Context context, int i) {
        return SubscriptionUtil.getUniqueSubscriptionDisplayName(SubscriptionUtil.getSubscriptionOrDefault(context, i), context);
    }

    /* access modifiers changed from: protected */
    public Intent updateFragment(Intent intent, Context context, int i) {
        CharSequence fragmentTitle;
        if (intent.getStringExtra(":settings:show_fragment_title") == null && (fragmentTitle = getFragmentTitle(context, i)) != null) {
            intent.putExtra(":settings:show_fragment_title", fragmentTitle.toString());
        }
        intent.putExtra(":settings:show_fragment", getFragmentClass(context));
        return intent;
    }

    /* access modifiers changed from: protected */
    public String getFragmentClass(Context context) {
        Bundle bundle;
        AtomicReference<String> atomicReference = mCachedClassName;
        String str = atomicReference.get();
        if (str != null) {
            return str;
        }
        try {
            ActivityInfo activityInfo = context.getPackageManager().getActivityInfo(sTargetComponent, 128);
            if (activityInfo == null || (bundle = activityInfo.metaData) == null) {
                return null;
            }
            String string = bundle.getString("com.android.settings.FRAGMENT_CLASS");
            if (string != null) {
                atomicReference.set(string);
            }
            return string;
        } catch (PackageManager.NameNotFoundException unused) {
            Log.d("MobileNetworkIntentConverter", "Cannot get Metadata for: " + sTargetComponent.toString());
            return null;
        }
    }

    /* access modifiers changed from: protected */
    /* renamed from: configForReRoute */
    public Intent lambda$apply$15(Intent intent) {
        if (!intent.hasExtra(":reroute:MobileNetworkIntentConverter")) {
            return intent.putExtra(":reroute:MobileNetworkIntentConverter", intent.getAction()).setComponent((ComponentName) null);
        }
        Log.d("MobileNetworkIntentConverter", "Skip re-routed intent " + intent);
        return null;
    }

    protected static boolean mayRequireConvert(Intent intent) {
        if (intent == null) {
            return false;
        }
        return Arrays.stream(sPotentialActions).anyMatch(new MobileNetworkIntentConverter$$ExternalSyntheticLambda16(intent.getAction()));
    }

    /* access modifiers changed from: protected */
    public Intent convertFromDeepLink(Intent intent) {
        if (intent == null) {
            return null;
        }
        if (!TextUtils.equals(intent.getAction(), "android.settings.SETTINGS_EMBED_DEEP_LINK_ACTIVITY")) {
            return intent;
        }
        try {
            return Intent.parseUri(intent.getStringExtra("android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI"), 1);
        } catch (URISyntaxException e) {
            Log.d("MobileNetworkIntentConverter", "Intent URI corrupted", e);
            return null;
        }
    }
}
