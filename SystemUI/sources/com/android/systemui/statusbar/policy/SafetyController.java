package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.safetycenter.SafetyCenterManager;
import com.android.systemui.dagger.qualifiers.Background;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Inject;

public class SafetyController implements CallbackController<Listener> {
    private static final IntentFilter PKG_CHANGE_INTENT_FILTER;
    /* access modifiers changed from: private */
    public final Handler mBgHandler;
    private final Context mContext;
    private final ArrayList<Listener> mListeners = new ArrayList<>();
    /* access modifiers changed from: private */
    public final PackageManager mPackageManager;
    final BroadcastReceiver mPermControllerChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getData() != null ? intent.getData().getSchemeSpecificPart() : null, SafetyController.this.mPackageManager.getPermissionControllerPackageName())) {
                boolean access$100 = SafetyController.this.mSafetyCenterEnabled;
                SafetyController safetyController = SafetyController.this;
                boolean unused = safetyController.mSafetyCenterEnabled = safetyController.mSafetyCenterManager.isSafetyCenterEnabled();
                if (access$100 != SafetyController.this.mSafetyCenterEnabled) {
                    SafetyController.this.mBgHandler.post(new SafetyController$1$$ExternalSyntheticLambda0(this));
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onReceive$0$com-android-systemui-statusbar-policy-SafetyController$1 */
        public /* synthetic */ void mo46043xf3842ebb() {
            SafetyController.this.handleSafetyCenterEnableChange();
        }
    };
    /* access modifiers changed from: private */
    public boolean mSafetyCenterEnabled;
    /* access modifiers changed from: private */
    public final SafetyCenterManager mSafetyCenterManager;

    public interface Listener {
        void onSafetyCenterEnableChanged(boolean z);
    }

    static {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_CHANGED");
        PKG_CHANGE_INTENT_FILTER = intentFilter;
        intentFilter.addDataScheme("package");
    }

    @Inject
    public SafetyController(Context context, PackageManager packageManager, SafetyCenterManager safetyCenterManager, @Background Handler handler) {
        this.mContext = context;
        this.mSafetyCenterManager = safetyCenterManager;
        this.mPackageManager = packageManager;
        this.mBgHandler = handler;
        this.mSafetyCenterEnabled = safetyCenterManager.isSafetyCenterEnabled();
    }

    public boolean isSafetyCenterEnabled() {
        return this.mSafetyCenterEnabled;
    }

    public void addCallback(Listener listener) {
        synchronized (this.mListeners) {
            this.mListeners.add(listener);
            if (this.mListeners.size() == 1) {
                this.mContext.registerReceiver(this.mPermControllerChangeReceiver, PKG_CHANGE_INTENT_FILTER);
                this.mBgHandler.post(new SafetyController$$ExternalSyntheticLambda0(this, listener));
            } else {
                listener.onSafetyCenterEnableChanged(isSafetyCenterEnabled());
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addCallback$0$com-android-systemui-statusbar-policy-SafetyController */
    public /* synthetic */ void mo46041xba028ac(Listener listener) {
        this.mSafetyCenterEnabled = this.mSafetyCenterManager.isSafetyCenterEnabled();
        listener.onSafetyCenterEnableChanged(isSafetyCenterEnabled());
    }

    public void removeCallback(Listener listener) {
        synchronized (this.mListeners) {
            this.mListeners.remove((Object) listener);
            if (this.mListeners.isEmpty()) {
                this.mContext.unregisterReceiver(this.mPermControllerChangeReceiver);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleSafetyCenterEnableChange() {
        synchronized (this.mListeners) {
            for (int i = 0; i < this.mListeners.size(); i++) {
                this.mListeners.get(i).onSafetyCenterEnableChanged(this.mSafetyCenterEnabled);
            }
        }
    }
}
