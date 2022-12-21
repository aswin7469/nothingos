package com.android.systemui.p012qs.tileimpl;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.p012qs.QSEvent;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.SideLabelTileLayout;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.p011qs.QSTile.State;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.google.zxing.pdf417.PDF417Common;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.tileimpl.QSTileImplEx;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.android.systemui.qs.tileimpl.QSTileImpl */
public abstract class QSTileImpl<TState extends QSTile.State> implements QSTile, LifecycleOwner, Dumpable {
    public static final Object ARG_SHOW_TRANSIENT_ENABLING = new Object();
    public static final Intent BLUETOOTH_PANEL = new Intent("android.settings.panel.action.NT_BLUE_TOOTH");
    /* access modifiers changed from: protected */
    public static final boolean DEBUG = Log.isLoggable("Tile", 3);
    private static final long DEFAULT_STALE_TIMEOUT = 600000;
    public static final Intent INTERNET_PANEL = new Intent("android.settings.panel.action.NT_INTERNET_CONNECTIVITY");
    private static final int READY_STATE_NOT_READY = 0;
    private static final int READY_STATE_READY = 2;
    private static final int READY_STATE_READYING = 1;
    /* access modifiers changed from: protected */
    public final String TAG = ("Tile." + getClass().getSimpleName());
    /* access modifiers changed from: protected */
    public final ActivityStarter mActivityStarter;
    private boolean mAnnounceNextStateChange;
    private final ArrayList<QSTile.Callback> mCallbacks = new ArrayList<>();
    /* access modifiers changed from: protected */
    public final Context mContext;
    protected RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    /* access modifiers changed from: protected */
    public QSTileImplEx mEx = ((QSTileImplEx) NTDependencyEx.get(QSTileImplEx.class));
    private final FalsingManager mFalsingManager;
    /* access modifiers changed from: protected */
    public final QSTileImpl<TState>.H mHandler;
    /* access modifiers changed from: protected */
    public final QSHost mHost;
    private final InstanceId mInstanceId;
    private int mIsFullQs;
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);
    private final ArraySet<Object> mListeners = new ArraySet<>();
    private final MetricsLogger mMetricsLogger;
    protected final QSLogger mQSLogger;
    private volatile int mReadyState;
    private boolean mShowingDetail;
    private final Object mStaleListener = new Object();
    protected TState mState;
    private final StatusBarStateController mStatusBarStateController;
    private String mTileSpec;
    private TState mTmpState;
    private final UiEventLogger mUiEventLogger;
    protected final Handler mUiHandler;

    public abstract Intent getLongClickIntent();

    @Deprecated
    public int getMetricsCategory() {
        return 0;
    }

    /* access modifiers changed from: protected */
    public long getStaleTimeout() {
        return DEFAULT_STALE_TIMEOUT;
    }

    public abstract CharSequence getTileLabel();

    /* access modifiers changed from: protected */
    public abstract void handleClick(View view);

    /* access modifiers changed from: protected */
    public void handleInitialize() {
    }

    /* access modifiers changed from: protected */
    public abstract void handleUpdateState(TState tstate, Object obj);

    public boolean isAvailable() {
        return true;
    }

    public abstract TState newTileState();

    public void setDetailListening(boolean z) {
    }

    protected QSTileImpl(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        this.mHost = qSHost;
        this.mContext = qSHost.getContext();
        this.mInstanceId = qSHost.getNewInstanceId();
        this.mUiEventLogger = qSHost.getUiEventLogger();
        this.mUiHandler = handler;
        this.mHandler = new C2383H(looper);
        this.mFalsingManager = falsingManager;
        this.mQSLogger = qSLogger;
        this.mMetricsLogger = metricsLogger;
        this.mStatusBarStateController = statusBarStateController;
        this.mActivityStarter = activityStarter;
        this.mEx.init(statusBarStateController);
        resetStates();
        handler.post(new QSTileImpl$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-qs-tileimpl-QSTileImpl  reason: not valid java name */
    public /* synthetic */ void m2958lambda$new$0$comandroidsystemuiqstileimplQSTileImpl() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    /* access modifiers changed from: protected */
    public final void resetStates() {
        this.mState = newTileState();
        this.mTmpState = newTileState();
        this.mState.spec = this.mTileSpec;
        this.mTmpState.spec = this.mTileSpec;
    }

    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    public InstanceId getInstanceId() {
        return this.mInstanceId;
    }

    public void setListening(Object obj, boolean z) {
        this.mHandler.obtainMessage(10, z ? 1 : 0, 0, obj).sendToTarget();
    }

    /* access modifiers changed from: protected */
    public void handleStale() {
        if (!this.mListeners.isEmpty()) {
            refreshState();
        } else {
            setListening(this.mStaleListener, true);
        }
    }

    public String getTileSpec() {
        return this.mTileSpec;
    }

    public void setTileSpec(String str) {
        this.mTileSpec = str;
        this.mState.spec = str;
        this.mTmpState.spec = str;
    }

    public QSHost getHost() {
        return this.mHost;
    }

    public QSIconView createTileView(Context context) {
        return new QSIconViewImpl(context);
    }

    public void addCallback(QSTile.Callback callback) {
        this.mHandler.obtainMessage(1, callback).sendToTarget();
    }

    public void removeCallback(QSTile.Callback callback) {
        this.mHandler.obtainMessage(9, callback).sendToTarget();
    }

    public void removeCallbacks() {
        this.mHandler.sendEmptyMessage(8);
    }

    public void click(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(925).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_CLICK, 0, getMetricsSpec(), getInstanceId());
        this.mQSLogger.logTileClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mHandler.obtainMessage(2, view).sendToTarget();
        }
    }

    public void secondaryClick(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(926).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_SECONDARY_CLICK, 0, getMetricsSpec(), getInstanceId());
        this.mQSLogger.logTileSecondaryClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        this.mHandler.obtainMessage(3, view).sendToTarget();
    }

    public void longClick(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(366).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_LONG_PRESS, 0, getMetricsSpec(), getInstanceId());
        this.mQSLogger.logTileLongClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        this.mHandler.obtainMessage(4, view).sendToTarget();
    }

    public LogMaker populate(LogMaker logMaker) {
        TState tstate = this.mState;
        if (tstate instanceof QSTile.BooleanState) {
            logMaker.addTaggedData(PDF417Common.MAX_CODEWORDS_IN_BARCODE, Integer.valueOf(((QSTile.BooleanState) tstate).value ? 1 : 0));
        }
        return logMaker.setSubtype(getMetricsCategory()).addTaggedData(1593, Integer.valueOf(this.mIsFullQs)).addTaggedData(927, Integer.valueOf(this.mHost.indexOf(this.mTileSpec)));
    }

    public void refreshState() {
        refreshState((Object) null);
    }

    public final boolean isListening() {
        return getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED);
    }

    /* access modifiers changed from: protected */
    public final void refreshState(Object obj) {
        this.mHandler.obtainMessage(5, obj).sendToTarget();
    }

    public void userSwitch(int i) {
        this.mHandler.obtainMessage(6, i, 0).sendToTarget();
    }

    public void destroy() {
        this.mHandler.sendEmptyMessage(7);
    }

    public void initialize() {
        this.mHandler.sendEmptyMessage(12);
    }

    public TState getState() {
        return this.mState;
    }

    /* access modifiers changed from: private */
    public void handleAddCallback(QSTile.Callback callback) {
        this.mCallbacks.add(callback);
        callback.onStateChanged(this.mState);
    }

    /* access modifiers changed from: private */
    public void handleRemoveCallback(QSTile.Callback callback) {
        this.mCallbacks.remove((Object) callback);
    }

    /* access modifiers changed from: private */
    public void handleRemoveCallbacks() {
        this.mCallbacks.clear();
    }

    public void postStale() {
        this.mHandler.sendEmptyMessage(11);
    }

    /* access modifiers changed from: protected */
    public void handleSecondaryClick(View view) {
        handleClick(view);
    }

    /* access modifiers changed from: protected */
    public void handleLongClick(View view) {
        ActivityLaunchAnimator.Controller fromView = view != null ? ActivityLaunchAnimator.Controller.fromView(view, 32) : null;
        if (!this.mEx.postStartActivityDismissingKeyguard(view, this.mActivityStarter, fromView)) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(getLongClickIntent(), 0, fromView);
        }
    }

    /* access modifiers changed from: protected */
    public final void handleRefreshState(Object obj) {
        handleUpdateState(this.mTmpState, obj);
        boolean copyTo = this.mTmpState.copyTo(this.mState);
        if (this.mReadyState == 1) {
            this.mReadyState = 2;
            copyTo = true;
        }
        if (copyTo) {
            this.mQSLogger.logTileUpdated(this.mTileSpec, this.mState);
            handleStateChanged();
        }
        this.mHandler.removeMessages(11);
        this.mHandler.sendEmptyMessageDelayed(11, getStaleTimeout());
        setListening(this.mStaleListener, false);
    }

    private void handleStateChanged() {
        if (this.mCallbacks.size() != 0) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                this.mCallbacks.get(i).onStateChanged(this.mState);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handleUserSwitch(int i) {
        handleRefreshState((Object) null);
    }

    /* access modifiers changed from: private */
    public void handleSetListeningInternal(Object obj, boolean z) {
        if (z) {
            if (this.mListeners.add(obj) && this.mListeners.size() == 1) {
                if (DEBUG) {
                    Log.d(this.TAG, "handleSetListening true");
                }
                handleSetListening(z);
                this.mUiHandler.post(new QSTileImpl$$ExternalSyntheticLambda0(this));
            }
        } else if (this.mListeners.remove(obj) && this.mListeners.size() == 0) {
            if (DEBUG) {
                Log.d(this.TAG, "handleSetListening false");
            }
            handleSetListening(z);
            this.mUiHandler.post(new QSTileImpl$$ExternalSyntheticLambda1(this));
        }
        updateIsFullQs();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleSetListeningInternal$1$com-android-systemui-qs-tileimpl-QSTileImpl */
    public /* synthetic */ void mo36753xdf09088() {
        if (!this.mLifecycle.getCurrentState().equals(Lifecycle.State.DESTROYED)) {
            this.mLifecycle.setCurrentState(Lifecycle.State.RESUMED);
            if (this.mReadyState == 0) {
                this.mReadyState = 1;
            }
            refreshState();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleSetListeningInternal$2$com-android-systemui-qs-tileimpl-QSTileImpl */
    public /* synthetic */ void mo36754xa8915309() {
        if (!this.mLifecycle.getCurrentState().equals(Lifecycle.State.DESTROYED)) {
            this.mLifecycle.setCurrentState(Lifecycle.State.STARTED);
        }
    }

    private void updateIsFullQs() {
        Iterator<Object> it = this.mListeners.iterator();
        while (it.hasNext()) {
            if (SideLabelTileLayout.class.equals(it.next().getClass())) {
                this.mIsFullQs = 1;
                return;
            }
        }
        this.mIsFullQs = 0;
    }

    /* access modifiers changed from: protected */
    public void handleSetListening(boolean z) {
        String str = this.mTileSpec;
        if (str != null) {
            this.mQSLogger.logTileChangeListening(str, z);
        }
    }

    /* access modifiers changed from: protected */
    public void handleDestroy() {
        this.mQSLogger.logTileDestroyed(this.mTileSpec, "Handle destroy");
        if (this.mListeners.size() != 0) {
            handleSetListening(false);
            this.mListeners.clear();
        }
        this.mCallbacks.clear();
        this.mHandler.removeCallbacksAndMessages((Object) null);
        this.mUiHandler.post(new QSTileImpl$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$handleDestroy$3$com-android-systemui-qs-tileimpl-QSTileImpl */
    public /* synthetic */ void mo36752x7f70ed04() {
        this.mLifecycle.setCurrentState(Lifecycle.State.DESTROYED);
    }

    /* access modifiers changed from: protected */
    public void checkIfRestrictionEnforcedByAdminOnly(QSTile.State state, String str) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, str, this.mHost.getUserId());
        if (checkIfRestrictionEnforced == null || RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, str, this.mHost.getUserId())) {
            state.disabledByPolicy = false;
            this.mEnforcedAdmin = null;
            return;
        }
        state.disabledByPolicy = true;
        this.mEnforcedAdmin = checkIfRestrictionEnforced;
    }

    public String getMetricsSpec() {
        return this.mTileSpec;
    }

    public boolean isTileReady() {
        return this.mReadyState == 2;
    }

    /* renamed from: com.android.systemui.qs.tileimpl.QSTileImpl$H */
    protected final class C2383H extends Handler {
        private static final int ADD_CALLBACK = 1;
        private static final int CLICK = 2;
        private static final int DESTROY = 7;
        private static final int INITIALIZE = 12;
        private static final int LONG_CLICK = 4;
        private static final int REFRESH_STATE = 5;
        private static final int REMOVE_CALLBACK = 9;
        private static final int REMOVE_CALLBACKS = 8;
        private static final int SECONDARY_CLICK = 3;
        private static final int SET_LISTENING = 10;
        protected static final int STALE = 11;
        private static final int USER_SWITCH = 6;

        protected C2383H(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            try {
                boolean z = true;
                if (message.what == 1) {
                    QSTileImpl.this.handleAddCallback((QSTile.Callback) message.obj);
                } else if (message.what == 8) {
                    QSTileImpl.this.handleRemoveCallbacks();
                } else if (message.what == 9) {
                    QSTileImpl.this.handleRemoveCallback((QSTile.Callback) message.obj);
                } else if (message.what == 2) {
                    if (QSTileImpl.this.mState.disabledByPolicy) {
                        QSTileImpl.this.mActivityStarter.postStartActivityDismissingKeyguard(RestrictedLockUtils.getShowAdminSupportDetailsIntent(QSTileImpl.this.mContext, QSTileImpl.this.mEnforcedAdmin), 0);
                        return;
                    }
                    QSTileImpl.this.handleClick((View) message.obj);
                } else if (message.what == 3) {
                    QSTileImpl.this.handleSecondaryClick((View) message.obj);
                } else if (message.what == 4) {
                    QSTileImpl.this.handleLongClick((View) message.obj);
                } else if (message.what == 5) {
                    QSTileImpl.this.handleRefreshState(message.obj);
                } else if (message.what == 6) {
                    QSTileImpl.this.handleUserSwitch(message.arg1);
                } else if (message.what == 7) {
                    QSTileImpl.this.handleDestroy();
                } else if (message.what == 10) {
                    QSTileImpl qSTileImpl = QSTileImpl.this;
                    Object obj = message.obj;
                    if (message.arg1 == 0) {
                        z = false;
                    }
                    qSTileImpl.handleSetListeningInternal(obj, z);
                } else if (message.what == 11) {
                    QSTileImpl.this.handleStale();
                } else if (message.what == 12) {
                    QSTileImpl.this.handleInitialize();
                } else {
                    throw new IllegalArgumentException("Unknown msg: " + message.what);
                }
            } catch (Throwable th) {
                String str = "Error in " + null;
                Log.w(QSTileImpl.this.TAG, str, th);
                QSTileImpl.this.mHost.warn(str, th);
            }
        }
    }

    /* renamed from: com.android.systemui.qs.tileimpl.QSTileImpl$DrawableIcon */
    public static class DrawableIcon extends QSTile.Icon {
        protected final Drawable mDrawable;
        protected final Drawable mInvisibleDrawable;

        public String toString() {
            return "DrawableIcon";
        }

        public DrawableIcon(Drawable drawable) {
            this.mDrawable = drawable;
            this.mInvisibleDrawable = drawable.getConstantState().newDrawable();
        }

        public Drawable getDrawable(Context context) {
            return this.mDrawable;
        }

        public Drawable getInvisibleDrawable(Context context) {
            return this.mInvisibleDrawable;
        }
    }

    /* renamed from: com.android.systemui.qs.tileimpl.QSTileImpl$DrawableIconWithRes */
    public static class DrawableIconWithRes extends DrawableIcon {
        private final int mId;

        public DrawableIconWithRes(Drawable drawable, int i) {
            super(drawable);
            this.mId = i;
        }

        public boolean equals(Object obj) {
            return (obj instanceof DrawableIconWithRes) && ((DrawableIconWithRes) obj).mId == this.mId;
        }

        public String toString() {
            return String.format("DrawableIconWithRes[resId=0x%08x]", Integer.valueOf(this.mId));
        }
    }

    /* renamed from: com.android.systemui.qs.tileimpl.QSTileImpl$ResourceIcon */
    public static class ResourceIcon extends QSTile.Icon {
        private static final SparseArray<QSTile.Icon> ICONS = new SparseArray<>();
        protected final int mResId;

        private ResourceIcon(int i) {
            this.mResId = i;
        }

        public static synchronized QSTile.Icon get(int i) {
            QSTile.Icon icon;
            synchronized (ResourceIcon.class) {
                SparseArray<QSTile.Icon> sparseArray = ICONS;
                icon = sparseArray.get(i);
                if (icon == null) {
                    icon = new ResourceIcon(i);
                    sparseArray.put(i, icon);
                }
            }
            return icon;
        }

        public Drawable getDrawable(Context context) {
            return context.getDrawable(this.mResId);
        }

        public Drawable getInvisibleDrawable(Context context) {
            return context.getDrawable(this.mResId);
        }

        public boolean equals(Object obj) {
            if (obj instanceof ResourceIcon) {
                ResourceIcon resourceIcon = (ResourceIcon) obj;
                return resourceIcon.mResId == this.mResId && resourceIcon.isTesla == this.isTesla && resourceIcon.skipTintBt == this.skipTintBt;
            }
        }

        public String toString() {
            return String.format("ResourceIcon[resId=0x%08x]", Integer.valueOf(this.mResId));
        }
    }

    /* renamed from: com.android.systemui.qs.tileimpl.QSTileImpl$AnimationIcon */
    protected static class AnimationIcon extends ResourceIcon {
        private final int mAnimatedResId;

        public AnimationIcon(int i, int i2) {
            super(i2);
            this.mAnimatedResId = i;
        }

        public Drawable getDrawable(Context context) {
            return context.getDrawable(this.mAnimatedResId).getConstantState().newDrawable();
        }

        public String toString() {
            return String.format("AnimationIcon[resId=0x%08x]", Integer.valueOf(this.mResId));
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.print("    ");
        printWriter.println(getState().toString());
    }
}
