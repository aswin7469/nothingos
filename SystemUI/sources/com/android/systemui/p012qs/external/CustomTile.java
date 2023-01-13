package com.android.systemui.p012qs.external;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.service.quicksettings.IQSTileService;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.IWindowManager;
import android.view.WindowManagerGlobal;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.external.TileLifecycleManager;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import dagger.Lazy;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.external.CustomTile */
public class CustomTile extends QSTileImpl<QSTile.State> implements TileLifecycleManager.TileChangeListener {
    private static final long CUSTOM_STALE_TIMEOUT = 3600000;
    private static final boolean DEBUG = false;
    public static final String PREFIX = "custom(";
    private static final long UNBIND_DELAY = 30000;
    private final ComponentName mComponent;
    private final CustomTileStatePersister mCustomTileStatePersister;
    private Icon mDefaultIcon;
    private CharSequence mDefaultLabel;
    private final AtomicBoolean mInitialDefaultIconFetched;
    private boolean mIsShowingDialog;
    private boolean mIsTokenGranted;
    private final TileServiceKey mKey;
    private boolean mListening;
    private final IQSTileService mService;
    private final TileServiceManager mServiceManager;
    private final Tile mTile;
    private final TileServices mTileServices;
    private final IBinder mToken;
    private final int mUser;
    private final Context mUserContext;
    private final IWindowManager mWindowManager;

    public int getMetricsCategory() {
        return 268;
    }

    private CustomTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, String str, Context context, CustomTileStatePersister customTileStatePersister, TileServices tileServices) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mToken = new Binder();
        this.mInitialDefaultIconFetched = new AtomicBoolean(false);
        this.mTileServices = tileServices;
        this.mWindowManager = WindowManagerGlobal.getWindowManagerService();
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        this.mComponent = unflattenFromString;
        this.mTile = new Tile();
        this.mUserContext = context;
        int userId = context.getUserId();
        this.mUser = userId;
        this.mKey = new TileServiceKey(unflattenFromString, userId);
        TileServiceManager tileWrapper = tileServices.getTileWrapper(this);
        this.mServiceManager = tileWrapper;
        this.mService = tileWrapper.getTileService();
        this.mCustomTileStatePersister = customTileStatePersister;
    }

    /* access modifiers changed from: protected */
    public void handleInitialize() {
        Tile readState;
        updateDefaultTileAndIcon();
        if (this.mInitialDefaultIconFetched.compareAndSet(false, true) && this.mDefaultIcon == null) {
            Log.w(this.TAG, "No default icon for " + getTileSpec() + ", destroying tile");
            this.mHost.removeTile(getTileSpec());
        }
        if (this.mServiceManager.isToggleableTile()) {
            resetStates();
        }
        this.mServiceManager.setTileChangeListener(this);
        if (this.mServiceManager.isActiveTile() && (readState = this.mCustomTileStatePersister.readState(this.mKey)) != null) {
            applyTileState(readState, false);
            this.mServiceManager.clearPendingBind();
            refreshState();
        }
    }

    /* access modifiers changed from: protected */
    public long getStaleTimeout() {
        return (((long) this.mHost.indexOf(getTileSpec())) * 60000) + CUSTOM_STALE_TIMEOUT;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0042 A[Catch:{ NameNotFoundException -> 0x007c }] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004d A[Catch:{ NameNotFoundException -> 0x007c }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0052 A[Catch:{ NameNotFoundException -> 0x007c }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0076 A[Catch:{ NameNotFoundException -> 0x007c }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateDefaultTileAndIcon() {
        /*
            r8 = this;
            r0 = 0
            android.content.Context r1 = r8.mUserContext     // Catch:{ NameNotFoundException -> 0x007c }
            android.content.pm.PackageManager r1 = r1.getPackageManager()     // Catch:{ NameNotFoundException -> 0x007c }
            boolean r2 = r8.isSystemApp(r1)     // Catch:{ NameNotFoundException -> 0x007c }
            if (r2 == 0) goto L_0x0011
            r2 = 786944(0xc0200, float:1.102743E-39)
            goto L_0x0013
        L_0x0011:
            r2 = 786432(0xc0000, float:1.102026E-39)
        L_0x0013:
            android.content.ComponentName r3 = r8.mComponent     // Catch:{ NameNotFoundException -> 0x007c }
            android.content.pm.ServiceInfo r2 = r1.getServiceInfo(r3, r2)     // Catch:{ NameNotFoundException -> 0x007c }
            int r3 = r2.icon     // Catch:{ NameNotFoundException -> 0x007c }
            if (r3 == 0) goto L_0x0020
            int r3 = r2.icon     // Catch:{ NameNotFoundException -> 0x007c }
            goto L_0x0024
        L_0x0020:
            android.content.pm.ApplicationInfo r3 = r2.applicationInfo     // Catch:{ NameNotFoundException -> 0x007c }
            int r3 = r3.icon     // Catch:{ NameNotFoundException -> 0x007c }
        L_0x0024:
            android.service.quicksettings.Tile r4 = r8.mTile     // Catch:{ NameNotFoundException -> 0x007c }
            android.graphics.drawable.Icon r4 = r4.getIcon()     // Catch:{ NameNotFoundException -> 0x007c }
            r5 = 0
            r6 = 1
            if (r4 == 0) goto L_0x003f
            android.service.quicksettings.Tile r4 = r8.mTile     // Catch:{ NameNotFoundException -> 0x007c }
            android.graphics.drawable.Icon r4 = r4.getIcon()     // Catch:{ NameNotFoundException -> 0x007c }
            android.graphics.drawable.Icon r7 = r8.mDefaultIcon     // Catch:{ NameNotFoundException -> 0x007c }
            boolean r4 = r8.iconEquals(r4, r7)     // Catch:{ NameNotFoundException -> 0x007c }
            if (r4 == 0) goto L_0x003d
            goto L_0x003f
        L_0x003d:
            r4 = r5
            goto L_0x0040
        L_0x003f:
            r4 = r6
        L_0x0040:
            if (r3 == 0) goto L_0x004d
            android.content.ComponentName r7 = r8.mComponent     // Catch:{ NameNotFoundException -> 0x007c }
            java.lang.String r7 = r7.getPackageName()     // Catch:{ NameNotFoundException -> 0x007c }
            android.graphics.drawable.Icon r3 = android.graphics.drawable.Icon.createWithResource(r7, r3)     // Catch:{ NameNotFoundException -> 0x007c }
            goto L_0x004e
        L_0x004d:
            r3 = r0
        L_0x004e:
            r8.mDefaultIcon = r3     // Catch:{ NameNotFoundException -> 0x007c }
            if (r4 == 0) goto L_0x0057
            android.service.quicksettings.Tile r4 = r8.mTile     // Catch:{ NameNotFoundException -> 0x007c }
            r4.setIcon(r3)     // Catch:{ NameNotFoundException -> 0x007c }
        L_0x0057:
            android.service.quicksettings.Tile r3 = r8.mTile     // Catch:{ NameNotFoundException -> 0x007c }
            java.lang.CharSequence r3 = r3.getLabel()     // Catch:{ NameNotFoundException -> 0x007c }
            if (r3 == 0) goto L_0x006d
            android.service.quicksettings.Tile r3 = r8.mTile     // Catch:{ NameNotFoundException -> 0x007c }
            java.lang.CharSequence r3 = r3.getLabel()     // Catch:{ NameNotFoundException -> 0x007c }
            java.lang.CharSequence r4 = r8.mDefaultLabel     // Catch:{ NameNotFoundException -> 0x007c }
            boolean r3 = android.text.TextUtils.equals(r3, r4)     // Catch:{ NameNotFoundException -> 0x007c }
            if (r3 == 0) goto L_0x006e
        L_0x006d:
            r5 = r6
        L_0x006e:
            java.lang.CharSequence r1 = r2.loadLabel(r1)     // Catch:{ NameNotFoundException -> 0x007c }
            r8.mDefaultLabel = r1     // Catch:{ NameNotFoundException -> 0x007c }
            if (r5 == 0) goto L_0x0080
            android.service.quicksettings.Tile r2 = r8.mTile     // Catch:{ NameNotFoundException -> 0x007c }
            r2.setLabel(r1)     // Catch:{ NameNotFoundException -> 0x007c }
            goto L_0x0080
        L_0x007c:
            r8.mDefaultIcon = r0
            r8.mDefaultLabel = r0
        L_0x0080:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p012qs.external.CustomTile.updateDefaultTileAndIcon():void");
    }

    private boolean isSystemApp(PackageManager packageManager) throws PackageManager.NameNotFoundException {
        return packageManager.getApplicationInfo(this.mComponent.getPackageName(), 0).isSystemApp();
    }

    private boolean iconEquals(Icon icon, Icon icon2) {
        if (icon == icon2) {
            return true;
        }
        return icon != null && icon2 != null && icon.getType() == 2 && icon2.getType() == 2 && icon.getResId() == icon2.getResId() && Objects.equals(icon.getResPackage(), icon2.getResPackage());
    }

    public void onTileChanged(ComponentName componentName) {
        this.mHandler.post(new CustomTile$$ExternalSyntheticLambda3(this));
    }

    public boolean isAvailable() {
        if (!this.mInitialDefaultIconFetched.get() || this.mDefaultIcon != null) {
            return true;
        }
        return false;
    }

    public int getUser() {
        return this.mUser;
    }

    public ComponentName getComponent() {
        return this.mComponent;
    }

    public LogMaker populate(LogMaker logMaker) {
        return super.populate(logMaker).setComponentName(this.mComponent);
    }

    public Tile getQsTile() {
        updateDefaultTileAndIcon();
        return this.mTile;
    }

    public void updateTileState(Tile tile) {
        this.mHandler.post(new CustomTile$$ExternalSyntheticLambda0(this, tile));
    }

    /* access modifiers changed from: private */
    /* renamed from: handleUpdateTileState */
    public void mo36573x95f2ff5b(Tile tile) {
        applyTileState(tile, true);
        if (this.mServiceManager.isActiveTile()) {
            this.mCustomTileStatePersister.persistState(this.mKey, tile);
        }
    }

    private void applyTileState(Tile tile, boolean z) {
        if (tile.getIcon() != null || z) {
            this.mTile.setIcon(tile.getIcon());
        }
        if (tile.getLabel() != null || z) {
            this.mTile.setLabel(tile.getLabel());
        }
        if (tile.getSubtitle() != null || z) {
            this.mTile.setSubtitle(tile.getSubtitle());
        }
        if (tile.getContentDescription() != null || z) {
            this.mTile.setContentDescription(tile.getContentDescription());
        }
        if (tile.getStateDescription() != null || z) {
            this.mTile.setStateDescription(tile.getStateDescription());
        }
        this.mTile.setState(tile.getState());
    }

    public void onDialogShown() {
        this.mIsShowingDialog = true;
    }

    public void onDialogHidden() {
        this.mIsShowingDialog = false;
        try {
            this.mWindowManager.removeWindowToken(this.mToken, 0);
        } catch (RemoteException unused) {
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:13|14|15|16) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x003b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleSetListening(boolean r3) {
        /*
            r2 = this;
            super.handleSetListening(r3)
            boolean r0 = r2.mListening
            if (r0 != r3) goto L_0x0008
            return
        L_0x0008:
            r2.mListening = r3
            if (r3 == 0) goto L_0x0026
            r2.updateDefaultTileAndIcon()     // Catch:{ RemoteException -> 0x0044 }
            r2.refreshState()     // Catch:{ RemoteException -> 0x0044 }
            com.android.systemui.qs.external.TileServiceManager r3 = r2.mServiceManager     // Catch:{ RemoteException -> 0x0044 }
            boolean r3 = r3.isActiveTile()     // Catch:{ RemoteException -> 0x0044 }
            if (r3 != 0) goto L_0x0044
            com.android.systemui.qs.external.TileServiceManager r3 = r2.mServiceManager     // Catch:{ RemoteException -> 0x0044 }
            r0 = 1
            r3.setBindRequested(r0)     // Catch:{ RemoteException -> 0x0044 }
            android.service.quicksettings.IQSTileService r2 = r2.mService     // Catch:{ RemoteException -> 0x0044 }
            r2.onStartListening()     // Catch:{ RemoteException -> 0x0044 }
            goto L_0x0044
        L_0x0026:
            android.service.quicksettings.IQSTileService r3 = r2.mService     // Catch:{ RemoteException -> 0x0044 }
            r3.onStopListening()     // Catch:{ RemoteException -> 0x0044 }
            boolean r3 = r2.mIsTokenGranted     // Catch:{ RemoteException -> 0x0044 }
            r0 = 0
            if (r3 == 0) goto L_0x003d
            boolean r3 = r2.mIsShowingDialog     // Catch:{ RemoteException -> 0x0044 }
            if (r3 != 0) goto L_0x003d
            android.view.IWindowManager r3 = r2.mWindowManager     // Catch:{ RemoteException -> 0x003b }
            android.os.IBinder r1 = r2.mToken     // Catch:{ RemoteException -> 0x003b }
            r3.removeWindowToken(r1, r0)     // Catch:{ RemoteException -> 0x003b }
        L_0x003b:
            r2.mIsTokenGranted = r0     // Catch:{ RemoteException -> 0x0044 }
        L_0x003d:
            r2.mIsShowingDialog = r0     // Catch:{ RemoteException -> 0x0044 }
            com.android.systemui.qs.external.TileServiceManager r2 = r2.mServiceManager     // Catch:{ RemoteException -> 0x0044 }
            r2.setBindRequested(r0)     // Catch:{ RemoteException -> 0x0044 }
        L_0x0044:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p012qs.external.CustomTile.handleSetListening(boolean):void");
    }

    /* access modifiers changed from: protected */
    public void handleDestroy() {
        super.handleDestroy();
        if (this.mIsTokenGranted) {
            try {
                this.mWindowManager.removeWindowToken(this.mToken, 0);
            } catch (RemoteException unused) {
            }
        }
        this.mTileServices.freeService(this, this.mServiceManager);
    }

    public QSTile.State newTileState() {
        TileServiceManager tileServiceManager = this.mServiceManager;
        if (tileServiceManager == null || !tileServiceManager.isToggleableTile()) {
            return new QSTile.State();
        }
        return new QSTile.BooleanState();
    }

    public Intent getLongClickIntent() {
        Intent intent = new Intent("android.service.quicksettings.action.QS_TILE_PREFERENCES");
        intent.setPackage(this.mComponent.getPackageName());
        Intent resolveIntent = resolveIntent(intent);
        if (resolveIntent == null) {
            return new Intent("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", this.mComponent.getPackageName(), (String) null));
        }
        resolveIntent.putExtra("android.intent.extra.COMPONENT_NAME", this.mComponent);
        resolveIntent.putExtra(AuthDialog.KEY_BIOMETRIC_STATE, this.mTile.getState());
        return resolveIntent;
    }

    private Intent resolveIntent(Intent intent) {
        ResolveInfo resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(intent, 0, this.mUser);
        if (resolveActivityAsUser != null) {
            return new Intent("android.service.quicksettings.action.QS_TILE_PREFERENCES").setClassName(resolveActivityAsUser.activityInfo.packageName, resolveActivityAsUser.activityInfo.name);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(8:3|4|5|6|7|(1:9)|10|12) */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0017 */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001f A[Catch:{ RemoteException -> 0x0030 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleClick(android.view.View r6) {
        /*
            r5 = this;
            android.service.quicksettings.Tile r6 = r5.mTile
            int r6 = r6.getState()
            if (r6 != 0) goto L_0x0009
            return
        L_0x0009:
            r6 = 1
            android.view.IWindowManager r0 = r5.mWindowManager     // Catch:{ RemoteException -> 0x0017 }
            android.os.IBinder r1 = r5.mToken     // Catch:{ RemoteException -> 0x0017 }
            r2 = 0
            r3 = 0
            r4 = 2035(0x7f3, float:2.852E-42)
            r0.addWindowToken(r1, r4, r2, r3)     // Catch:{ RemoteException -> 0x0017 }
            r5.mIsTokenGranted = r6     // Catch:{ RemoteException -> 0x0017 }
        L_0x0017:
            com.android.systemui.qs.external.TileServiceManager r0 = r5.mServiceManager     // Catch:{ RemoteException -> 0x0030 }
            boolean r0 = r0.isActiveTile()     // Catch:{ RemoteException -> 0x0030 }
            if (r0 == 0) goto L_0x0029
            com.android.systemui.qs.external.TileServiceManager r0 = r5.mServiceManager     // Catch:{ RemoteException -> 0x0030 }
            r0.setBindRequested(r6)     // Catch:{ RemoteException -> 0x0030 }
            android.service.quicksettings.IQSTileService r6 = r5.mService     // Catch:{ RemoteException -> 0x0030 }
            r6.onStartListening()     // Catch:{ RemoteException -> 0x0030 }
        L_0x0029:
            android.service.quicksettings.IQSTileService r6 = r5.mService     // Catch:{ RemoteException -> 0x0030 }
            android.os.IBinder r5 = r5.mToken     // Catch:{ RemoteException -> 0x0030 }
            r6.onClick(r5)     // Catch:{ RemoteException -> 0x0030 }
        L_0x0030:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.p012qs.external.CustomTile.handleClick(android.view.View):void");
    }

    public CharSequence getTileLabel() {
        return getState().label;
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.State state, Object obj) {
        Drawable drawable;
        int state2 = this.mTile.getState();
        boolean z = false;
        if (this.mServiceManager.hasPendingBind()) {
            state2 = 0;
        }
        state.state = state2;
        try {
            drawable = this.mTile.getIcon().loadDrawable(this.mUserContext);
        } catch (Exception unused) {
            Log.w(this.TAG, "Invalid icon, forcing into unavailable state");
            state.state = 0;
            drawable = this.mDefaultIcon.loadDrawable(this.mUserContext);
        }
        state.iconSupplier = new CustomTile$$ExternalSyntheticLambda2(drawable);
        state.label = this.mTile.getLabel();
        CharSequence subtitle = this.mTile.getSubtitle();
        if (subtitle == null || subtitle.length() <= 0) {
            state.secondaryLabel = null;
        } else {
            state.secondaryLabel = subtitle;
        }
        if (this.mTile.getContentDescription() != null) {
            state.contentDescription = this.mTile.getContentDescription();
        } else {
            state.contentDescription = state.label;
        }
        if (this.mTile.getStateDescription() != null) {
            state.stateDescription = this.mTile.getStateDescription();
        } else {
            state.stateDescription = null;
        }
        if (state instanceof QSTile.BooleanState) {
            state.expandedAccessibilityClassName = Switch.class.getName();
            QSTile.BooleanState booleanState = (QSTile.BooleanState) state;
            if (state.state == 2) {
                z = true;
            }
            booleanState.value = z;
        }
    }

    static /* synthetic */ QSTile.Icon lambda$handleUpdateState$1(Drawable drawable) {
        Drawable.ConstantState constantState;
        if (drawable == null || (constantState = drawable.getConstantState()) == null) {
            return null;
        }
        return new QSTileImpl.DrawableIcon(constantState.newDrawable());
    }

    public final String getMetricsSpec() {
        return this.mComponent.getPackageName();
    }

    public void startUnlockAndRun() {
        this.mActivityStarter.postQSRunnableDismissingKeyguard(new CustomTile$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startUnlockAndRun$2$com-android-systemui-qs-external-CustomTile */
    public /* synthetic */ void mo36572xbca43afd() {
        try {
            this.mService.onUnlockComplete();
        } catch (RemoteException unused) {
        }
    }

    public static String toSpec(ComponentName componentName) {
        return PREFIX + componentName.flattenToShortString() + NavigationBarInflaterView.KEY_CODE_END;
    }

    public static ComponentName getComponentFromSpec(String str) {
        String substring = str.substring(7, str.length() - 1);
        if (!substring.isEmpty()) {
            return ComponentName.unflattenFromString(substring);
        }
        throw new IllegalArgumentException("Empty custom tile spec action");
    }

    /* access modifiers changed from: private */
    public static String getAction(String str) {
        if (str == null || !str.startsWith(PREFIX) || !str.endsWith(NavigationBarInflaterView.KEY_CODE_END)) {
            throw new IllegalArgumentException("Bad custom tile spec: " + str);
        }
        String substring = str.substring(7, str.length() - 1);
        if (!substring.isEmpty()) {
            return substring;
        }
        throw new IllegalArgumentException("Empty custom tile spec action");
    }

    public static CustomTile create(Builder builder, String str, Context context) {
        return builder.setSpec(str).setUserContext(context).build();
    }

    /* renamed from: com.android.systemui.qs.external.CustomTile$Builder */
    public static class Builder {
        final ActivityStarter mActivityStarter;
        final Looper mBackgroundLooper;
        final CustomTileStatePersister mCustomTileStatePersister;
        private final FalsingManager mFalsingManager;
        final Handler mMainHandler;
        final MetricsLogger mMetricsLogger;
        final Lazy<QSHost> mQSHostLazy;
        final QSLogger mQSLogger;
        String mSpec = "";
        final StatusBarStateController mStatusBarStateController;
        private TileServices mTileServices;
        Context mUserContext;

        @Inject
        public Builder(Lazy<QSHost> lazy, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, CustomTileStatePersister customTileStatePersister, TileServices tileServices) {
            this.mQSHostLazy = lazy;
            this.mBackgroundLooper = looper;
            this.mMainHandler = handler;
            this.mFalsingManager = falsingManager;
            this.mMetricsLogger = metricsLogger;
            this.mStatusBarStateController = statusBarStateController;
            this.mActivityStarter = activityStarter;
            this.mQSLogger = qSLogger;
            this.mCustomTileStatePersister = customTileStatePersister;
            this.mTileServices = tileServices;
        }

        /* access modifiers changed from: package-private */
        public Builder setSpec(String str) {
            this.mSpec = str;
            return this;
        }

        /* access modifiers changed from: package-private */
        public Builder setUserContext(Context context) {
            this.mUserContext = context;
            return this;
        }

        public CustomTile build() {
            if (this.mUserContext != null) {
                return new CustomTile(this.mQSHostLazy.get(), this.mBackgroundLooper, this.mMainHandler, this.mFalsingManager, this.mMetricsLogger, this.mStatusBarStateController, this.mActivityStarter, this.mQSLogger, CustomTile.getAction(this.mSpec), this.mUserContext, this.mCustomTileStatePersister, this.mTileServices);
            }
            throw new NullPointerException("UserContext cannot be null");
        }
    }
}
