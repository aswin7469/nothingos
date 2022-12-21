package com.android.systemui.statusbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.IndentingPrintWriter;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import androidx.core.app.NotificationCompat;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.p026io.PrintWriter;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000¶\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u0007\u0018\u0000 a2\u00020\u00012\u00020\u0002:\u0001aB_\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0018¢\u0006\u0002\u0010\u0019J\b\u0010B\u001a\u00020\u001bH\u0002J\b\u0010C\u001a\u00020DH\u0002J\u0018\u0010E\u001a\u00020D2\u0006\u0010F\u001a\u00020)2\u0006\u0010G\u001a\u00020)H\u0002J%\u0010H\u001a\u00020D2\u0006\u0010I\u001a\u00020J2\u000e\u0010K\u001a\n\u0012\u0006\b\u0001\u0012\u00020M0LH\u0016¢\u0006\u0002\u0010NJ\u001a\u0010O\u001a\u0004\u0018\u00010/2\u0006\u0010F\u001a\u00020)2\u0006\u0010G\u001a\u00020)H\u0002J\b\u0010P\u001a\u00020DH\u0002J\u0010\u0010Q\u001a\u00020D2\u0006\u0010\u0003\u001a\u00020\u0004H\u0002J\u0010\u0010R\u001a\u00020\u001b2\u0006\u0010S\u001a\u00020TH\u0016J\u0010\u0010U\u001a\u00020\u001b2\u0006\u0010S\u001a\u00020TH\u0016J\b\u0010V\u001a\u00020DH\u0002J\u0010\u0010W\u001a\u00020D2\u0006\u0010X\u001a\u00020/H\u0002J\u000e\u0010Y\u001a\u00020D2\u0006\u0010Z\u001a\u00020\u001bJ\u000e\u0010[\u001a\u00020D2\u0006\u0010=\u001a\u00020>J\u0018\u0010\\\u001a\u00020D2\u0006\u0010X\u001a\u00020/2\u0006\u0010]\u001a\u00020\u001bH\u0002J\u0010\u0010^\u001a\u00020\u001b2\u0006\u0010S\u001a\u00020TH\u0002J\u0010\u0010_\u001a\u00020D2\u0006\u0010`\u001a\u00020)H\u0002R\u001a\u0010\u001a\u001a\u00020\u001bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R$\u0010!\u001a\u00020\u001b2\u0006\u0010 \u001a\u00020\u001b@BX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u001d\"\u0004\b\"\u0010\u001fR\u0014\u0010#\u001a\u00020\u001b8BX\u0004¢\u0006\u0006\u001a\u0004\b#\u0010\u001dR\u001e\u0010%\u001a\u00020\u001b2\u0006\u0010$\u001a\u00020\u001b@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001dR\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020\u001bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020)X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010+\u001a\u0004\u0018\u00010,X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020\u001bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010/X\u000e¢\u0006\u0002\n\u0000R\u000e\u00100\u001a\u000201X\u0004¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u000203X\u000e¢\u0006\u0002\n\u0000R\u001c\u00104\u001a\u0004\u0018\u000105X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u00107\"\u0004\b8\u00109R\u001a\u0010:\u001a\u00020\u001bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b;\u0010\u001d\"\u0004\b<\u0010\u001fR\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010=\u001a\u00020>X.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010?\u001a\u00020)X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010@\u001a\u0004\u0018\u00010AX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006b"}, mo64987d2 = {"Lcom/android/systemui/statusbar/PulseExpansionHandler;", "Lcom/android/systemui/Gefingerpoken;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "wakeUpCoordinator", "Lcom/android/systemui/statusbar/notification/NotificationWakeUpCoordinator;", "bypassController", "Lcom/android/systemui/statusbar/phone/KeyguardBypassController;", "headsUpManager", "Lcom/android/systemui/statusbar/phone/HeadsUpManagerPhone;", "roundnessManager", "Lcom/android/systemui/statusbar/notification/stack/NotificationRoundnessManager;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "lockscreenShadeTransitionController", "Lcom/android/systemui/statusbar/LockscreenShadeTransitionController;", "falsingCollector", "Lcom/android/systemui/classifier/FalsingCollector;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/notification/NotificationWakeUpCoordinator;Lcom/android/systemui/statusbar/phone/KeyguardBypassController;Lcom/android/systemui/statusbar/phone/HeadsUpManagerPhone;Lcom/android/systemui/statusbar/notification/stack/NotificationRoundnessManager;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/plugins/FalsingManager;Lcom/android/systemui/statusbar/LockscreenShadeTransitionController;Lcom/android/systemui/classifier/FalsingCollector;Lcom/android/systemui/dump/DumpManager;)V", "bouncerShowing", "", "getBouncerShowing", "()Z", "setBouncerShowing", "(Z)V", "value", "isExpanding", "setExpanding", "isFalseTouch", "<set-?>", "leavingLockscreen", "getLeavingLockscreen", "mDraggedFarEnough", "mInitialTouchX", "", "mInitialTouchY", "mPowerManager", "Landroid/os/PowerManager;", "mPulsing", "mStartingChild", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "mTemp2", "", "minDragDistance", "", "pulseExpandAbortListener", "Ljava/lang/Runnable;", "getPulseExpandAbortListener", "()Ljava/lang/Runnable;", "setPulseExpandAbortListener", "(Ljava/lang/Runnable;)V", "qsExpanded", "getQsExpanded", "setQsExpanded", "stackScrollerController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "touchSlop", "velocityTracker", "Landroid/view/VelocityTracker;", "canHandleMotionEvent", "cancelExpansion", "", "captureStartingChild", "x", "y", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "findView", "finishExpansion", "initResources", "onInterceptTouchEvent", "event", "Landroid/view/MotionEvent;", "onTouchEvent", "recycleVelocityTracker", "reset", "child", "setPulsing", "pulsing", "setUp", "setUserLocked", "userLocked", "startExpansion", "updateExpansionHeight", "height", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PulseExpansionHandler.kt */
public final class PulseExpansionHandler implements Gefingerpoken, Dumpable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int SPRING_BACK_ANIMATION_LENGTH_MS = 375;
    private boolean bouncerShowing;
    private final KeyguardBypassController bypassController;
    private final ConfigurationController configurationController;
    private final FalsingCollector falsingCollector;
    private final FalsingManager falsingManager;
    private final HeadsUpManagerPhone headsUpManager;
    private boolean isExpanding;
    private boolean leavingLockscreen;
    private final LockscreenShadeTransitionController lockscreenShadeTransitionController;
    private boolean mDraggedFarEnough;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private final PowerManager mPowerManager;
    private boolean mPulsing;
    private ExpandableView mStartingChild;
    private final int[] mTemp2 = new int[2];
    private int minDragDistance;
    private Runnable pulseExpandAbortListener;
    private boolean qsExpanded;
    private final NotificationRoundnessManager roundnessManager;
    private NotificationStackScrollLayoutController stackScrollerController;
    private final StatusBarStateController statusBarStateController;
    private float touchSlop;
    private VelocityTracker velocityTracker;
    private final NotificationWakeUpCoordinator wakeUpCoordinator;

    @Inject
    public PulseExpansionHandler(final Context context, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, HeadsUpManagerPhone headsUpManagerPhone, NotificationRoundnessManager notificationRoundnessManager, ConfigurationController configurationController2, StatusBarStateController statusBarStateController2, FalsingManager falsingManager2, LockscreenShadeTransitionController lockscreenShadeTransitionController2, FalsingCollector falsingCollector2, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(notificationWakeUpCoordinator, "wakeUpCoordinator");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(headsUpManagerPhone, "headsUpManager");
        Intrinsics.checkNotNullParameter(notificationRoundnessManager, "roundnessManager");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        Intrinsics.checkNotNullParameter(falsingManager2, "falsingManager");
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController2, "lockscreenShadeTransitionController");
        Intrinsics.checkNotNullParameter(falsingCollector2, "falsingCollector");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.wakeUpCoordinator = notificationWakeUpCoordinator;
        this.bypassController = keyguardBypassController;
        this.headsUpManager = headsUpManagerPhone;
        this.roundnessManager = notificationRoundnessManager;
        this.configurationController = configurationController2;
        this.statusBarStateController = statusBarStateController2;
        this.falsingManager = falsingManager2;
        this.lockscreenShadeTransitionController = lockscreenShadeTransitionController2;
        this.falsingCollector = falsingCollector2;
        initResources(context);
        configurationController2.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ PulseExpansionHandler this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.initResources(context);
            }
        });
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        dumpManager.registerDumpable(this);
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/statusbar/PulseExpansionHandler$Companion;", "", "()V", "SPRING_BACK_ANIMATION_LENGTH_MS", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PulseExpansionHandler.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final boolean isExpanding() {
        return this.isExpanding;
    }

    private final void setExpanding(boolean z) {
        boolean z2 = this.isExpanding != z;
        this.isExpanding = z;
        this.bypassController.setPulseExpanding(z);
        if (z2) {
            if (z) {
                NotificationEntry topEntry = this.headsUpManager.getTopEntry();
                if (topEntry != null) {
                    this.roundnessManager.setTrackingHeadsUp(topEntry.getRow());
                }
                this.lockscreenShadeTransitionController.onPulseExpansionStarted();
            } else {
                this.roundnessManager.setTrackingHeadsUp((ExpandableNotificationRow) null);
                if (!this.leavingLockscreen) {
                    this.bypassController.maybePerformPendingUnlock();
                    Runnable runnable = this.pulseExpandAbortListener;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
            this.headsUpManager.unpinAll(true);
        }
    }

    public final boolean getLeavingLockscreen() {
        return this.leavingLockscreen;
    }

    private final boolean isFalseTouch() {
        return this.falsingManager.isFalseTouch(2);
    }

    public final boolean getQsExpanded() {
        return this.qsExpanded;
    }

    public final void setQsExpanded(boolean z) {
        this.qsExpanded = z;
    }

    public final Runnable getPulseExpandAbortListener() {
        return this.pulseExpandAbortListener;
    }

    public final void setPulseExpandAbortListener(Runnable runnable) {
        this.pulseExpandAbortListener = runnable;
    }

    public final boolean getBouncerShowing() {
        return this.bouncerShowing;
    }

    public final void setBouncerShowing(boolean z) {
        this.bouncerShowing = z;
    }

    /* access modifiers changed from: private */
    public final void initResources(Context context) {
        this.minDragDistance = context.getResources().getDimensionPixelSize(C1893R.dimen.keyguard_drag_down_min_distance);
        this.touchSlop = (float) ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, NotificationCompat.CATEGORY_EVENT);
        return canHandleMotionEvent() && startExpansion(motionEvent);
    }

    private final boolean canHandleMotionEvent() {
        return this.wakeUpCoordinator.getCanShowPulsingHuns() && !this.qsExpanded && !this.bouncerShowing;
    }

    private final boolean startExpansion(MotionEvent motionEvent) {
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        VelocityTracker velocityTracker2 = this.velocityTracker;
        Intrinsics.checkNotNull(velocityTracker2);
        velocityTracker2.addMovement(motionEvent);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mDraggedFarEnough = false;
            setExpanding(false);
            this.leavingLockscreen = false;
            this.mStartingChild = null;
            this.mInitialTouchY = y;
            this.mInitialTouchX = x;
        } else if (actionMasked == 1) {
            recycleVelocityTracker();
            setExpanding(false);
        } else if (actionMasked == 2) {
            float f = y - this.mInitialTouchY;
            if (f > this.touchSlop && f > Math.abs(x - this.mInitialTouchX)) {
                this.falsingCollector.onStartExpandingFromPulse();
                setExpanding(true);
                captureStartingChild(this.mInitialTouchX, this.mInitialTouchY);
                this.mInitialTouchY = y;
                this.mInitialTouchX = x;
                return true;
            }
        } else if (actionMasked == 3) {
            recycleVelocityTracker();
            setExpanding(false);
        }
        return false;
    }

    private final void recycleVelocityTracker() {
        VelocityTracker velocityTracker2 = this.velocityTracker;
        if (velocityTracker2 != null) {
            velocityTracker2.recycle();
        }
        this.velocityTracker = null;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, NotificationCompat.CATEGORY_EVENT);
        boolean z = false;
        boolean z2 = (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) && this.isExpanding;
        ExpandableView expandableView = this.mStartingChild;
        boolean z3 = (expandableView != null && expandableView.showingPulsing()) || this.bypassController.canBypass();
        if ((!canHandleMotionEvent() || !z3) && !z2) {
            return false;
        }
        if (this.velocityTracker == null || !this.isExpanding || motionEvent.getActionMasked() == 0) {
            return startExpansion(motionEvent);
        }
        VelocityTracker velocityTracker2 = this.velocityTracker;
        Intrinsics.checkNotNull(velocityTracker2);
        velocityTracker2.addMovement(motionEvent);
        float y = motionEvent.getY() - this.mInitialTouchY;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1) {
            VelocityTracker velocityTracker3 = this.velocityTracker;
            Intrinsics.checkNotNull(velocityTracker3);
            velocityTracker3.computeCurrentVelocity(1000);
            if (y > 0.0f) {
                VelocityTracker velocityTracker4 = this.velocityTracker;
                Intrinsics.checkNotNull(velocityTracker4);
                if (velocityTracker4.getYVelocity() > -1000.0f && this.statusBarStateController.getState() != 0) {
                    z = true;
                }
            }
            if (this.falsingManager.isUnlockingDisabled() || isFalseTouch() || !z) {
                cancelExpansion();
            } else {
                finishExpansion();
            }
            recycleVelocityTracker();
        } else if (actionMasked == 2) {
            updateExpansionHeight(y);
        } else if (actionMasked == 3) {
            cancelExpansion();
            recycleVelocityTracker();
        }
        return this.isExpanding;
    }

    private final void finishExpansion() {
        ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            setUserLocked(expandableView, false);
            this.mStartingChild = null;
        }
        if (this.statusBarStateController.isDozing()) {
            this.wakeUpCoordinator.setWillWakeUp(true);
            PowerManager powerManager = this.mPowerManager;
            Intrinsics.checkNotNull(powerManager);
            powerManager.wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:PULSEDRAG");
        }
        this.lockscreenShadeTransitionController.goToLockedShade(expandableView, false);
        this.lockscreenShadeTransitionController.finishPulseAnimation(false);
        this.leavingLockscreen = true;
        setExpanding(false);
        ExpandableView expandableView2 = this.mStartingChild;
        if (expandableView2 instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView2;
            Intrinsics.checkNotNull(expandableNotificationRow);
            expandableNotificationRow.onExpandedByGesture(true);
        }
    }

    private final void updateExpansionHeight(float f) {
        float max = Math.max(f, 0.0f);
        ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            expandableView.setActualHeight(Math.min((int) (((float) expandableView.getCollapsedHeight()) + max), expandableView.getMaxContentHeight()));
        } else {
            this.wakeUpCoordinator.setNotificationsVisibleForExpansion(f > ((float) this.lockscreenShadeTransitionController.getDistanceUntilShowingPulsingNotifications()), true, true);
        }
        this.lockscreenShadeTransitionController.setPulseHeight(max, false);
    }

    private final void captureStartingChild(float f, float f2) {
        if (this.mStartingChild == null && !this.bypassController.getBypassEnabled()) {
            ExpandableView findView = findView(f, f2);
            this.mStartingChild = findView;
            if (findView != null) {
                Intrinsics.checkNotNull(findView);
                setUserLocked(findView, true);
            }
        }
    }

    private final void reset(ExpandableView expandableView) {
        if (expandableView.getActualHeight() == expandableView.getCollapsedHeight()) {
            setUserLocked(expandableView, false);
            return;
        }
        ObjectAnimator ofInt = ObjectAnimator.ofInt(expandableView, "actualHeight", new int[]{expandableView.getActualHeight(), expandableView.getCollapsedHeight()});
        ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofInt.setDuration((long) SPRING_BACK_ANIMATION_LENGTH_MS);
        ofInt.addListener(new PulseExpansionHandler$reset$1(this, expandableView));
        ofInt.start();
    }

    /* access modifiers changed from: private */
    public final void setUserLocked(ExpandableView expandableView, boolean z) {
        if (expandableView instanceof ExpandableNotificationRow) {
            ((ExpandableNotificationRow) expandableView).setUserLocked(z);
        }
    }

    private final void cancelExpansion() {
        setExpanding(false);
        this.falsingCollector.onExpansionFromPulseStopped();
        ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            Intrinsics.checkNotNull(expandableView);
            reset(expandableView);
            this.mStartingChild = null;
        }
        this.lockscreenShadeTransitionController.finishPulseAnimation(true);
        this.wakeUpCoordinator.setNotificationsVisibleForExpansion(false, true, false);
    }

    private final ExpandableView findView(float f, float f2) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.stackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stackScrollerController");
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.getLocationOnScreen(this.mTemp2);
        int[] iArr = this.mTemp2;
        float f3 = f + ((float) iArr[0]);
        float f4 = f2 + ((float) iArr[1]);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.stackScrollerController;
        if (notificationStackScrollLayoutController2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("stackScrollerController");
            notificationStackScrollLayoutController2 = null;
        }
        ExpandableView childAtRawPosition = notificationStackScrollLayoutController2.getChildAtRawPosition(f3, f4);
        if (childAtRawPosition == null || !childAtRawPosition.isContentExpandable()) {
            return null;
        }
        return childAtRawPosition;
    }

    public final void setUp(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutController, "stackScrollerController");
        this.stackScrollerController = notificationStackScrollLayoutController;
    }

    public final void setPulsing(boolean z) {
        this.mPulsing = z;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("PulseExpansionHandler:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("isExpanding: " + this.isExpanding);
        indentingPrintWriter.println("leavingLockscreen: " + this.leavingLockscreen);
        indentingPrintWriter.println("mPulsing: " + this.mPulsing);
        indentingPrintWriter.println("qsExpanded: " + this.qsExpanded);
        indentingPrintWriter.println("bouncerShowing: " + this.bouncerShowing);
    }
}
