package com.android.systemui.tuner;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.util.ArrayUtils;
import com.android.systemui.C1893R;
import com.android.systemui.DejankUtils;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.leak.LeakDetector;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Inject;

@SysUISingleton
public class TunerServiceImpl extends TunerService {
    private static final int CURRENT_TUNER_VERSION = 4;
    private static final String[] RESET_EXCEPTION_LIST = {QSTileHost.TILES_SETTING, "doze_always_on", "qs_media_resumption", "qs_media_recommend"};
    private static final String TAG = "TunerService";
    private static final String TUNER_VERSION = "sysui_tuner_version";
    private ContentResolver mContentResolver;
    private final Context mContext;
    /* access modifiers changed from: private */
    public int mCurrentUser;
    private UserTracker.Callback mCurrentUserTracker;
    private final DemoModeController mDemoModeController;
    private final LeakDetector mLeakDetector;
    private final ArrayMap<Uri, String> mListeningUris = new ArrayMap<>();
    private final Observer mObserver = new Observer();
    private final ConcurrentHashMap<String, Set<TunerService.Tunable>> mTunableLookup = new ConcurrentHashMap<>();
    private final HashSet<TunerService.Tunable> mTunables;
    private final ComponentName mTunerComponent;
    /* access modifiers changed from: private */
    public UserTracker mUserTracker;

    @Inject
    public TunerServiceImpl(Context context, @Main Handler handler, LeakDetector leakDetector, DemoModeController demoModeController, UserTracker userTracker) {
        super(context);
        this.mTunables = LeakDetector.ENABLED ? new HashSet<>() : null;
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mLeakDetector = leakDetector;
        this.mDemoModeController = demoModeController;
        this.mUserTracker = userTracker;
        this.mTunerComponent = new ComponentName(context, TunerActivity.class);
        for (UserInfo userHandle : UserManager.get(context).getUsers()) {
            this.mCurrentUser = userHandle.getUserHandle().getIdentifier();
            if (getValue(TUNER_VERSION, 0) != 4) {
                upgradeTuner(getValue(TUNER_VERSION, 0), 4, handler);
            }
        }
        this.mCurrentUser = this.mUserTracker.getUserId();
        C32231 r2 = new UserTracker.Callback() {
            public void onUserChanged(int i, Context context) {
                int unused = TunerServiceImpl.this.mCurrentUser = i;
                TunerServiceImpl.this.reloadAll();
                TunerServiceImpl.this.reregisterAll();
            }
        };
        this.mCurrentUserTracker = r2;
        this.mUserTracker.addCallback(r2, new HandlerExecutor(handler));
    }

    public void destroy() {
        this.mUserTracker.removeCallback(this.mCurrentUserTracker);
    }

    private void upgradeTuner(int i, int i2, Handler handler) {
        String value;
        if (i < 1 && (value = getValue(StatusBarIconController.ICON_HIDE_LIST)) != null) {
            ArraySet<String> iconHideList = StatusBarIconController.getIconHideList(this.mContext, value);
            iconHideList.add("rotate");
            iconHideList.add("headset");
            Settings.Secure.putStringForUser(this.mContentResolver, StatusBarIconController.ICON_HIDE_LIST, TextUtils.join(NavigationBarInflaterView.BUTTON_SEPARATOR, iconHideList), this.mCurrentUser);
        }
        if (i < 2) {
            setTunerEnabled(false);
        }
        if (i < 4) {
            handler.postDelayed(new TunerServiceImpl$$ExternalSyntheticLambda0(this, this.mCurrentUser), 5000);
        }
        setValue(TUNER_VERSION, i2);
    }

    public String getValue(String str) {
        return Settings.Secure.getStringForUser(this.mContentResolver, str, this.mCurrentUser);
    }

    public void setValue(String str, String str2) {
        Settings.Secure.putStringForUser(this.mContentResolver, str, str2, this.mCurrentUser);
    }

    public int getValue(String str, int i) {
        return Settings.Secure.getIntForUser(this.mContentResolver, str, i, this.mCurrentUser);
    }

    public String getValue(String str, String str2) {
        String stringForUser = Settings.Secure.getStringForUser(this.mContentResolver, str, this.mCurrentUser);
        return stringForUser == null ? str2 : stringForUser;
    }

    public void setValue(String str, int i) {
        Settings.Secure.putIntForUser(this.mContentResolver, str, i, this.mCurrentUser);
    }

    public void addTunable(TunerService.Tunable tunable, String... strArr) {
        for (String addTunable : strArr) {
            addTunable(tunable, addTunable);
        }
    }

    private void addTunable(TunerService.Tunable tunable, String str) {
        if (!this.mTunableLookup.containsKey(str)) {
            this.mTunableLookup.put(str, new ArraySet());
        }
        this.mTunableLookup.get(str).add(tunable);
        if (LeakDetector.ENABLED) {
            this.mTunables.add(tunable);
            this.mLeakDetector.trackCollection(this.mTunables, "TunerService.mTunables");
        }
        Uri uriFor = Settings.Secure.getUriFor(str);
        if (!this.mListeningUris.containsKey(uriFor)) {
            this.mListeningUris.put(uriFor, str);
            this.mContentResolver.registerContentObserver(uriFor, false, this.mObserver, this.mCurrentUser);
        }
        tunable.onTuningChanged(str, (String) DejankUtils.whitelistIpcs(new TunerServiceImpl$$ExternalSyntheticLambda2(this, str)));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addTunable$1$com-android-systemui-tuner-TunerServiceImpl  reason: not valid java name */
    public /* synthetic */ String m3261lambda$addTunable$1$comandroidsystemuitunerTunerServiceImpl(String str) {
        return Settings.Secure.getStringForUser(this.mContentResolver, str, this.mCurrentUser);
    }

    public void removeTunable(TunerService.Tunable tunable) {
        for (Set<TunerService.Tunable> remove : this.mTunableLookup.values()) {
            remove.remove(tunable);
        }
        if (LeakDetector.ENABLED) {
            this.mTunables.remove(tunable);
        }
    }

    /* access modifiers changed from: protected */
    public void reregisterAll() {
        if (this.mListeningUris.size() != 0) {
            this.mContentResolver.unregisterContentObserver(this.mObserver);
            for (Uri registerContentObserver : this.mListeningUris.keySet()) {
                this.mContentResolver.registerContentObserver(registerContentObserver, false, this.mObserver, this.mCurrentUser);
            }
        }
    }

    /* access modifiers changed from: private */
    public void reloadSetting(Uri uri) {
        String str = this.mListeningUris.get(uri);
        Set<TunerService.Tunable> set = this.mTunableLookup.get(str);
        if (set != null) {
            String stringForUser = Settings.Secure.getStringForUser(this.mContentResolver, str, this.mCurrentUser);
            for (TunerService.Tunable onTuningChanged : set) {
                onTuningChanged.onTuningChanged(str, stringForUser);
            }
        }
    }

    /* access modifiers changed from: private */
    public void reloadAll() {
        for (String next : this.mTunableLookup.keySet()) {
            String stringForUser = Settings.Secure.getStringForUser(this.mContentResolver, next, this.mCurrentUser);
            for (TunerService.Tunable onTuningChanged : this.mTunableLookup.get(next)) {
                onTuningChanged.onTuningChanged(next, stringForUser);
            }
        }
    }

    public void clearAll() {
        mo46472xc2e3231(this.mCurrentUser);
    }

    /* renamed from: clearAllFromUser */
    public void mo46472xc2e3231(int i) {
        this.mDemoModeController.requestFinishDemoMode();
        this.mDemoModeController.requestSetDemoModeAllowed(false);
        for (String next : this.mTunableLookup.keySet()) {
            if (!ArrayUtils.contains(RESET_EXCEPTION_LIST, next)) {
                Settings.Secure.putStringForUser(this.mContentResolver, next, (String) null, i);
            }
        }
    }

    public void setTunerEnabled(boolean z) {
        this.mUserTracker.getUserContext().getPackageManager().setComponentEnabledSetting(this.mTunerComponent, z ? 1 : 2, 1);
    }

    public boolean isTunerEnabled() {
        return this.mUserTracker.getUserContext().getPackageManager().getComponentEnabledSetting(this.mTunerComponent) == 1;
    }

    public void showResetRequest(Runnable runnable) {
        SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
        systemUIDialog.setShowForAllUsers(true);
        systemUIDialog.setMessage(C1893R.string.remove_from_settings_prompt);
        DialogInterface.OnClickListener onClickListener = null;
        systemUIDialog.setButton(-2, this.mContext.getString(C1893R.string.cancel), (DialogInterface.OnClickListener) null);
        systemUIDialog.setButton(-1, this.mContext.getString(C1893R.string.qs_customize_remove), new TunerServiceImpl$$ExternalSyntheticLambda1(this, runnable));
        systemUIDialog.show();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showResetRequest$2$com-android-systemui-tuner-TunerServiceImpl */
    public /* synthetic */ void mo46471xaead92(Runnable runnable, DialogInterface dialogInterface, int i) {
        this.mContext.sendBroadcast(new Intent(TunerService.ACTION_CLEAR));
        setTunerEnabled(false);
        Settings.Secure.putInt(this.mContext.getContentResolver(), TunerFragment.SETTING_SEEN_TUNER_WARNING, 0);
        if (runnable != null) {
            runnable.run();
        }
    }

    private class Observer extends ContentObserver {
        public Observer() {
            super(new Handler(Looper.getMainLooper()));
        }

        public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
            if (i2 == TunerServiceImpl.this.mUserTracker.getUserId()) {
                for (Uri access$300 : collection) {
                    TunerServiceImpl.this.reloadSetting(access$300);
                }
            }
        }
    }
}
