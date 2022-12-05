package com.android.systemui.qs.external;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.service.quicksettings.IQSTileService;
import android.service.quicksettings.Tile;
import android.text.TextUtils;
import android.util.Log;
import android.view.IWindowManager;
import android.view.View;
import android.view.WindowManagerGlobal;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Dependency;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import dagger.Lazy;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class CustomTile extends QSTileImpl<QSTile.State> implements TileLifecycleManager.TileChangeListener {
    private final ComponentName mComponent;
    private final CustomTileStatePersister mCustomTileStatePersister;
    private Icon mDefaultIcon;
    private CharSequence mDefaultLabel;
    private boolean mIsShowingDialog;
    private boolean mIsTokenGranted;
    private final TileServiceKey mKey;
    private boolean mListening;
    private final IQSTileService mService;
    private final TileServiceManager mServiceManager;
    private final Tile mTile;
    private final IBinder mToken;
    private final int mUser;
    private final Context mUserContext;
    private final IWindowManager mWindowManager;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 268;
    }

    private CustomTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, String str, Context context, CustomTileStatePersister customTileStatePersister) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mToken = new Binder();
        this.mWindowManager = WindowManagerGlobal.getWindowManagerService();
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        this.mComponent = unflattenFromString;
        this.mTile = new Tile();
        this.mUserContext = context;
        int userId = context.getUserId();
        this.mUser = userId;
        this.mKey = new TileServiceKey(unflattenFromString, userId);
        TileServiceManager tileWrapper = qSHost.getTileServices().getTileWrapper(this);
        this.mServiceManager = tileWrapper;
        this.mService = tileWrapper.getTileService();
        this.mCustomTileStatePersister = customTileStatePersister;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleInitialize() {
        Tile readState;
        updateDefaultTileAndIcon();
        if (this.mServiceManager.isToggleableTile()) {
            resetStates();
        }
        this.mServiceManager.setTileChangeListener(this);
        if (!this.mServiceManager.isActiveTile() || (readState = this.mCustomTileStatePersister.readState(this.mKey)) == null) {
            return;
        }
        applyTileState(readState, false);
        this.mServiceManager.clearPendingBind();
        refreshState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected long getStaleTimeout() {
        return (this.mHost.indexOf(getTileSpec()) * 60000) + 3600000;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x003f A[Catch: NameNotFoundException -> 0x0079, TryCatch #0 {NameNotFoundException -> 0x0079, blocks: (B:3:0x0001, B:6:0x0012, B:9:0x0021, B:11:0x002b, B:16:0x003f, B:17:0x004b, B:19:0x004f, B:20:0x0054, B:22:0x005c, B:24:0x006b, B:26:0x0073, B:34:0x001d), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:19:0x004f A[Catch: NameNotFoundException -> 0x0079, TryCatch #0 {NameNotFoundException -> 0x0079, blocks: (B:3:0x0001, B:6:0x0012, B:9:0x0021, B:11:0x002b, B:16:0x003f, B:17:0x004b, B:19:0x004f, B:20:0x0054, B:22:0x005c, B:24:0x006b, B:26:0x0073, B:34:0x001d), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0073 A[Catch: NameNotFoundException -> 0x0079, TRY_LEAVE, TryCatch #0 {NameNotFoundException -> 0x0079, blocks: (B:3:0x0001, B:6:0x0012, B:9:0x0021, B:11:0x002b, B:16:0x003f, B:17:0x004b, B:19:0x004f, B:20:0x0054, B:22:0x005c, B:24:0x006b, B:26:0x0073, B:34:0x001d), top: B:2:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x004a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void updateDefaultTileAndIcon() {
        boolean z;
        try {
            PackageManager packageManager = this.mUserContext.getPackageManager();
            int i = 786432;
            if (isSystemApp(packageManager)) {
                i = 786944;
            }
            ServiceInfo serviceInfo = packageManager.getServiceInfo(this.mComponent, i);
            int i2 = serviceInfo.icon;
            if (i2 == 0) {
                i2 = serviceInfo.applicationInfo.icon;
            }
            boolean z2 = false;
            if (this.mTile.getIcon() != null && !iconEquals(this.mTile.getIcon(), this.mDefaultIcon)) {
                z = false;
                Icon createWithResource = i2 == 0 ? Icon.createWithResource(this.mComponent.getPackageName(), i2) : null;
                this.mDefaultIcon = createWithResource;
                if (z) {
                    this.mTile.setIcon(createWithResource);
                }
                if (this.mTile.getLabel() != null || TextUtils.equals(this.mTile.getLabel(), this.mDefaultLabel)) {
                    z2 = true;
                }
                CharSequence loadLabel = serviceInfo.loadLabel(packageManager);
                this.mDefaultLabel = loadLabel;
                if (z2) {
                    return;
                }
                this.mTile.setLabel(loadLabel);
                return;
            }
            z = true;
            if (i2 == 0) {
            }
            this.mDefaultIcon = createWithResource;
            if (z) {
            }
            if (this.mTile.getLabel() != null) {
            }
            z2 = true;
            CharSequence loadLabel2 = serviceInfo.loadLabel(packageManager);
            this.mDefaultLabel = loadLabel2;
            if (z2) {
            }
        } catch (PackageManager.NameNotFoundException unused) {
            this.mDefaultIcon = null;
            this.mDefaultLabel = null;
        }
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

    @Override // com.android.systemui.qs.external.TileLifecycleManager.TileChangeListener
    public void onTileChanged(ComponentName componentName) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.external.CustomTile$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CustomTile.this.updateDefaultTileAndIcon();
            }
        });
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.mDefaultIcon != null;
    }

    public int getUser() {
        return this.mUser;
    }

    public ComponentName getComponent() {
        return this.mComponent;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public LogMaker populate(LogMaker logMaker) {
        return super.populate(logMaker).setComponentName(this.mComponent);
    }

    public Tile getQsTile() {
        updateDefaultTileAndIcon();
        return this.mTile;
    }

    public void updateTileState(final Tile tile) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.external.CustomTile$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CustomTile.this.lambda$updateTileState$0(tile);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: handleUpdateTileState */
    public void lambda$updateTileState$0(Tile tile) {
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

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening == z) {
            return;
        }
        this.mListening = z;
        try {
            if (z) {
                updateDefaultTileAndIcon();
                refreshState();
                if (this.mServiceManager.isActiveTile()) {
                    return;
                }
                this.mServiceManager.setBindRequested(true);
                this.mService.onStartListening();
                return;
            }
            this.mService.onStopListening();
            if (this.mIsTokenGranted && !this.mIsShowingDialog) {
                try {
                    this.mWindowManager.removeWindowToken(this.mToken, 0);
                } catch (RemoteException unused) {
                }
                this.mIsTokenGranted = false;
            }
            this.mIsShowingDialog = false;
            this.mServiceManager.setBindRequested(false);
        } catch (RemoteException unused2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleDestroy() {
        super.handleDestroy();
        if (this.mIsTokenGranted) {
            try {
                this.mWindowManager.removeWindowToken(this.mToken, 0);
            } catch (RemoteException unused) {
            }
        }
        this.mHost.getTileServices().freeService(this, this.mServiceManager);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /* renamed from: newTileState */
    public QSTile.State mo1926newTileState() {
        TileServiceManager tileServiceManager = this.mServiceManager;
        if (tileServiceManager != null && tileServiceManager.isToggleableTile()) {
            return new QSTile.BooleanState();
        }
        return new QSTile.State();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        Intent intent = new Intent("android.service.quicksettings.action.QS_TILE_PREFERENCES");
        intent.setPackage(this.mComponent.getPackageName());
        Intent resolveIntent = resolveIntent(intent);
        if (resolveIntent != null) {
            resolveIntent.putExtra("android.intent.extra.COMPONENT_NAME", this.mComponent);
            resolveIntent.putExtra("state", this.mTile.getState());
            return resolveIntent;
        }
        return new Intent("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", this.mComponent.getPackageName(), null));
    }

    private Intent resolveIntent(Intent intent) {
        ResolveInfo resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(intent, 0, this.mUser);
        if (resolveActivityAsUser != null) {
            Intent intent2 = new Intent("android.service.quicksettings.action.QS_TILE_PREFERENCES");
            ActivityInfo activityInfo = resolveActivityAsUser.activityInfo;
            return intent2.setClassName(activityInfo.packageName, activityInfo.name);
        }
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        if (this.mTile.getState() == 0) {
            return;
        }
        try {
            this.mWindowManager.addWindowToken(this.mToken, 2035, 0, (Bundle) null);
            this.mIsTokenGranted = true;
        } catch (RemoteException unused) {
        }
        try {
            if (this.mServiceManager.isActiveTile()) {
                this.mServiceManager.setBindRequested(true);
                this.mService.onStartListening();
            }
            this.mService.onClick(this.mToken);
        } catch (RemoteException unused2) {
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return getState().label;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleUpdateState(QSTile.State state, Object obj) {
        final Drawable loadDrawable;
        int state2 = this.mTile.getState();
        boolean z = false;
        if (this.mServiceManager.hasPendingBind()) {
            state2 = 0;
        }
        state.state = state2;
        try {
            loadDrawable = this.mTile.getIcon().loadDrawable(this.mUserContext);
        } catch (Exception unused) {
            Log.w(this.TAG, "Invalid icon, forcing into unavailable state");
            state.state = 0;
            loadDrawable = this.mDefaultIcon.loadDrawable(this.mUserContext);
        }
        state.iconSupplier = new Supplier() { // from class: com.android.systemui.qs.external.CustomTile$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                QSTile.Icon lambda$handleUpdateState$1;
                lambda$handleUpdateState$1 = CustomTile.lambda$handleUpdateState$1(loadDrawable);
                return lambda$handleUpdateState$1;
            }
        };
        state.label = this.mTile.getLabel();
        CharSequence subtitle = this.mTile.getSubtitle();
        if (subtitle != null && subtitle.length() > 0) {
            state.secondaryLabel = subtitle;
        } else {
            state.secondaryLabel = null;
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ QSTile.Icon lambda$handleUpdateState$1(Drawable drawable) {
        Drawable.ConstantState constantState;
        if (drawable == null || (constantState = drawable.getConstantState()) == null) {
            return null;
        }
        return new QSTileImpl.DrawableIcon(constantState.newDrawable());
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final String getMetricsSpec() {
        return this.mComponent.getPackageName();
    }

    public void startUnlockAndRun() {
        ((ActivityStarter) Dependency.get(ActivityStarter.class)).postQSRunnableDismissingKeyguard(new Runnable() { // from class: com.android.systemui.qs.external.CustomTile$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CustomTile.this.lambda$startUnlockAndRun$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startUnlockAndRun$2() {
        try {
            this.mService.onUnlockComplete();
        } catch (RemoteException unused) {
        }
    }

    public static String toSpec(ComponentName componentName) {
        return "custom(" + componentName.flattenToShortString() + ")";
    }

    public static ComponentName getComponentFromSpec(String str) {
        String substring = str.substring(7, str.length() - 1);
        if (substring.isEmpty()) {
            throw new IllegalArgumentException("Empty custom tile spec action");
        }
        return ComponentName.unflattenFromString(substring);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getAction(String str) {
        if (str == null || !str.startsWith("custom(") || !str.endsWith(")")) {
            throw new IllegalArgumentException("Bad custom tile spec: " + str);
        }
        String substring = str.substring(7, str.length() - 1);
        if (substring.isEmpty()) {
            throw new IllegalArgumentException("Empty custom tile spec action");
        }
        return substring;
    }

    public static CustomTile create(Builder builder, String str, Context context) {
        return builder.setSpec(str).setUserContext(context).build();
    }

    /* loaded from: classes.dex */
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
        Context mUserContext;

        public Builder(Lazy<QSHost> lazy, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, CustomTileStatePersister customTileStatePersister) {
            this.mQSHostLazy = lazy;
            this.mBackgroundLooper = looper;
            this.mMainHandler = handler;
            this.mFalsingManager = falsingManager;
            this.mMetricsLogger = metricsLogger;
            this.mStatusBarStateController = statusBarStateController;
            this.mActivityStarter = activityStarter;
            this.mQSLogger = qSLogger;
            this.mCustomTileStatePersister = customTileStatePersister;
        }

        Builder setSpec(String str) {
            this.mSpec = str;
            return this;
        }

        Builder setUserContext(Context context) {
            this.mUserContext = context;
            return this;
        }

        CustomTile build() {
            Objects.requireNonNull(this.mUserContext, "UserContext cannot be null");
            return new CustomTile(this.mQSHostLazy.get(), this.mBackgroundLooper, this.mMainHandler, this.mFalsingManager, this.mMetricsLogger, this.mStatusBarStateController, this.mActivityStarter, this.mQSLogger, CustomTile.getAction(this.mSpec), this.mUserContext, this.mCustomTileStatePersister);
        }
    }
}
