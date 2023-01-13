package com.nothing.systemui.statusbar.notification;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.ConfigurationControllerImpl;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.nothing.gamemode.NTGameModeHelper;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.ArrayList;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 =2\u00020\u0001:\u0001=B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u001e\u001a\u00020\u001fJ\u0006\u0010 \u001a\u00020\u001fJ\u0006\u0010!\u001a\u00020\u001fJ\b\u0010\"\u001a\u00020\u001fH\u0002J\b\u0010#\u001a\u00020\u001fH\u0002J\b\u0010$\u001a\u00020\u001fH\u0002J\u0010\u0010%\u001a\u00020\u00062\b\u0010&\u001a\u0004\u0018\u00010'J\u0006\u0010\u0015\u001a\u00020\u0006J\u0006\u0010(\u001a\u00020\u0006J\u0010\u0010)\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020\u0006H\u0016J\u0006\u0010+\u001a\u00020\u001fJ\u0006\u0010,\u001a\u00020\u001fJ\u0012\u0010,\u001a\u00020\u001f2\b\u0010-\u001a\u0004\u0018\u00010.H\u0002J\u0006\u0010/\u001a\u00020\u001fJ\u0010\u00100\u001a\u00020\u001f2\b\u00101\u001a\u0004\u0018\u00010\u0013J\u0010\u00102\u001a\u00020\u001f2\u0006\u00103\u001a\u00020\u0006H\u0002J\u0018\u00104\u001a\u00020\u001f2\u0006\u00105\u001a\u00020\u00062\b\u00106\u001a\u0004\u0018\u00010\u000bJ\u0006\u00107\u001a\u00020\u0006J\u0006\u00108\u001a\u00020\u001fJ\u000e\u00109\u001a\u00020\u001f2\u0006\u0010:\u001a\u00020;J\u0010\u0010<\u001a\u00020\u001f2\b\u0010&\u001a\u0004\u0018\u00010'R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0016\u001a\u0004\u0018\u00010\u0017X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u000bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0004¢\u0006\u0002\n\u0000¨\u0006>"}, mo65043d2 = {"Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupManager;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "DEBUG", "", "autoDismissNotificationDecay", "", "densityDpi", "dissmissPopView", "Ljava/lang/Runnable;", "fontScale", "", "gameModeToast", "Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationToast;", "handler", "Landroid/os/Handler;", "headsUpManager", "Lcom/android/systemui/statusbar/policy/HeadsUpManager;", "hideGameToastRunnable", "isForceQuickReply", "popNotificationView", "Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationView;", "showGameToastRunnable", "showPopNotification", "windowLayoutParams", "Landroid/view/WindowManager$LayoutParams;", "windowManager", "Landroid/view/WindowManager;", "addToWindow", "", "hideGameModeToast", "hidePopNotificationView", "initContentResolver", "initViews", "initWindowLayoutParams", "isDisturbForPop", "pkg", "", "isShowPopNotification", "onGameModeStatusChanged", "enable", "onPanelTrackingStarted", "removeFromWindow", "view", "Landroid/view/View;", "removeGameModeToastFromWindow", "setHeadsUpManager", "manager", "setPopViewVisibilityWithAnimation", "vis", "setPopWithAnimation", "show", "runnable", "shouldShowLightweightHeadsup", "showGameModeToast", "showPopNotificationView", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "snooze", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NTLightweightHeadsupManager.kt */
public final class NTLightweightHeadsupManager {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final long SHOW_GAME_TOAST_DELAY = 500;
    private static final long SHOW_GAME_TOAST_DURATION = 3500;
    private static final String TAG = "NTLightweightHeadsupManager";
    private static final ArrayList<String> disturbPKgForPop;
    private final boolean DEBUG = Log.isLoggable(TAG, 3);
    private final int autoDismissNotificationDecay;
    private final Context context;
    /* access modifiers changed from: private */
    public int densityDpi;
    private final Runnable dissmissPopView = new NTLightweightHeadsupManager$$ExternalSyntheticLambda2(this);
    /* access modifiers changed from: private */
    public float fontScale;
    private NTLightweightHeadsupNotificationToast gameModeToast;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private HeadsUpManager headsUpManager;
    private Runnable hideGameToastRunnable = new NTLightweightHeadsupManager$$ExternalSyntheticLambda3(this);
    private boolean isForceQuickReply = true;
    private NTLightweightHeadsupNotificationView popNotificationView;
    private Runnable showGameToastRunnable = new NTLightweightHeadsupManager$$ExternalSyntheticLambda4(this);
    private boolean showPopNotification = true;
    private final WindowManager.LayoutParams windowLayoutParams;
    private final WindowManager windowManager;

    @Inject
    public NTLightweightHeadsupManager(Context context2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
        Object systemService = context2.getSystemService("window");
        if (systemService != null) {
            this.windowManager = (WindowManager) systemService;
            this.autoDismissNotificationDecay = context2.getResources().getInteger(C1894R.integer.heads_up_notification_decay);
            initViews();
            initContentResolver();
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(2038, 8782632, -3);
            this.windowLayoutParams = layoutParams;
            layoutParams.privateFlags |= 16;
            this.densityDpi = context2.getResources().getConfiguration().densityDpi;
            this.fontScale = context2.getResources().getConfiguration().fontScale;
            ((ConfigurationControllerImpl) NTDependencyEx.get(ConfigurationControllerImpl.class)).addCallback((ConfigurationController.ConfigurationListener) new NTLightweightHeadsupManager$configurationListener$1(this));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.WindowManager");
    }

    /* access modifiers changed from: private */
    /* renamed from: dissmissPopView$lambda-0  reason: not valid java name */
    public static final void m3541dissmissPopView$lambda0(NTLightweightHeadsupManager nTLightweightHeadsupManager) {
        Intrinsics.checkNotNullParameter(nTLightweightHeadsupManager, "this$0");
        nTLightweightHeadsupManager.hidePopNotificationView();
    }

    /* access modifiers changed from: private */
    /* renamed from: hideGameToastRunnable$lambda-1  reason: not valid java name */
    public static final void m3543hideGameToastRunnable$lambda1(NTLightweightHeadsupManager nTLightweightHeadsupManager) {
        Intrinsics.checkNotNullParameter(nTLightweightHeadsupManager, "this$0");
        nTLightweightHeadsupManager.hideGameModeToast();
    }

    /* access modifiers changed from: private */
    /* renamed from: showGameToastRunnable$lambda-2  reason: not valid java name */
    public static final void m3545showGameToastRunnable$lambda2(NTLightweightHeadsupManager nTLightweightHeadsupManager) {
        Intrinsics.checkNotNullParameter(nTLightweightHeadsupManager, "this$0");
        nTLightweightHeadsupManager.showGameModeToast();
    }

    /* access modifiers changed from: private */
    public final void initViews() {
        removeFromWindow(this.popNotificationView);
        removeFromWindow(this.gameModeToast);
        NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView = new NTLightweightHeadsupNotificationView(this.context, (AttributeSet) null, 2, (DefaultConstructorMarker) null);
        this.popNotificationView = nTLightweightHeadsupNotificationView;
        nTLightweightHeadsupNotificationView.setVisibility(8);
        NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView2 = this.popNotificationView;
        if (nTLightweightHeadsupNotificationView2 != null) {
            nTLightweightHeadsupNotificationView2.setFocusable(true);
        }
        this.gameModeToast = new NTLightweightHeadsupNotificationToast(this.context, (AttributeSet) null, 2, (DefaultConstructorMarker) null);
        NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView3 = this.popNotificationView;
        if (nTLightweightHeadsupNotificationView3 != null) {
            nTLightweightHeadsupNotificationView3.setVisibility(8);
        }
    }

    public final boolean shouldShowLightweightHeadsup() {
        Object obj = NTDependencyEx.get(NTGameModeHelper.class);
        if (obj != null) {
            return ((NTGameModeHelper) obj).shouldShowLightweightHeadsup();
        }
        throw new NullPointerException("null cannot be cast to non-null type com.nothing.gamemode.NTGameModeHelper");
    }

    public final boolean isDisturbForPop(String str) {
        ArrayList<String> arrayList = disturbPKgForPop;
        return (arrayList != null ? Boolean.valueOf(CollectionsKt.contains(arrayList, str)) : null).booleanValue();
    }

    public final boolean isShowPopNotification() {
        return this.showPopNotification;
    }

    public final boolean isForceQuickReply() {
        return this.isForceQuickReply;
    }

    private final void initContentResolver() {
        this.showPopNotification = true;
        this.isForceQuickReply = true;
    }

    public final void addToWindow() {
        initWindowLayoutParams();
        this.windowManager.addView(this.popNotificationView, this.windowLayoutParams);
        NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView = this.popNotificationView;
        if (nTLightweightHeadsupNotificationView != null) {
            nTLightweightHeadsupNotificationView.invalidate();
        }
    }

    private final void initWindowLayoutParams() {
        this.windowLayoutParams.flags |= 16777216;
        this.windowLayoutParams.gravity = 49;
        this.windowLayoutParams.softInputMode = 16;
        this.windowLayoutParams.setTitle("Lightweight Headsup");
        this.windowLayoutParams.packageName = this.context.getPackageName();
        this.windowLayoutParams.width = -2;
        Resources resources = this.context.getResources();
        this.windowLayoutParams.height = ((int) resources.getDimension(C1894R.dimen.nt_pop_view_height)) + ((int) resources.getDimension(C1894R.dimen.nt_game_mode_toast_height));
    }

    public final void removeFromWindow() {
        removeFromWindow(this.popNotificationView);
    }

    private final void removeFromWindow(View view) {
        if (view != null && view.isAttachedToWindow()) {
            this.windowManager.removeView(view);
        }
    }

    public final void showPopNotificationView(NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        ExpandableNotificationRow row = notificationEntry.getRow();
        Intrinsics.checkNotNullExpressionValue(row.getEntry().getSbn().getOpPkg(), "row.getEntry().getSbn().getOpPkg()");
        NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView = this.popNotificationView;
        if (nTLightweightHeadsupNotificationView != null) {
            Intrinsics.checkNotNullExpressionValue(row, "row");
            nTLightweightHeadsupNotificationView.updateNotificationRow(row);
        }
        initWindowLayoutParams();
        NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView2 = this.popNotificationView;
        Boolean valueOf = nTLightweightHeadsupNotificationView2 != null ? Boolean.valueOf(nTLightweightHeadsupNotificationView2.isAttachedToWindow()) : null;
        Intrinsics.checkNotNull(valueOf);
        if (valueOf.booleanValue()) {
            this.windowManager.updateViewLayout(this.popNotificationView, this.windowLayoutParams);
        } else {
            this.windowManager.addView(this.popNotificationView, this.windowLayoutParams);
        }
        setPopViewVisibilityWithAnimation(true);
        this.handler.removeCallbacks(this.dissmissPopView);
        this.handler.postDelayed(this.dissmissPopView, (long) this.autoDismissNotificationDecay);
    }

    public final void hidePopNotificationView() {
        setPopViewVisibilityWithAnimation(false);
    }

    public void onGameModeStatusChanged(boolean z) {
        NTLogUtil.m1686d(TAG, "onGameModeStatusChanged enable = " + z);
        Handler.getMain().removeCallbacks(this.hideGameToastRunnable);
        Handler.getMain().removeCallbacks(this.showGameToastRunnable);
        if (z) {
            Handler.getMain().postDelayed(this.showGameToastRunnable, 500);
        } else {
            hideGameModeToast();
        }
    }

    public final void showGameModeToast() {
        StringBuilder sb = new StringBuilder("showGameModeToast isAttach = ");
        NTLightweightHeadsupNotificationToast nTLightweightHeadsupNotificationToast = this.gameModeToast;
        Intrinsics.checkNotNull(nTLightweightHeadsupNotificationToast);
        NTLogUtil.m1686d(TAG, sb.append(nTLightweightHeadsupNotificationToast.isAttachedToWindow()).toString());
        Handler.getMain().removeCallbacks(this.hideGameToastRunnable);
        NTLightweightHeadsupNotificationToast nTLightweightHeadsupNotificationToast2 = this.gameModeToast;
        Intrinsics.checkNotNull(nTLightweightHeadsupNotificationToast2);
        if (!nTLightweightHeadsupNotificationToast2.isAttachedToWindow()) {
            initWindowLayoutParams();
            this.windowManager.addView(this.gameModeToast, this.windowLayoutParams);
            NTLightweightHeadsupNotificationToast nTLightweightHeadsupNotificationToast3 = this.gameModeToast;
            if (nTLightweightHeadsupNotificationToast3 != null) {
                nTLightweightHeadsupNotificationToast3.invalidate();
            }
            NTLightweightHeadsupNotificationToast nTLightweightHeadsupNotificationToast4 = this.gameModeToast;
            if (nTLightweightHeadsupNotificationToast4 != null) {
                nTLightweightHeadsupNotificationToast4.releasePopWithAnimation(true, (Runnable) null);
            }
            Handler.getMain().postDelayed(this.hideGameToastRunnable, SHOW_GAME_TOAST_DURATION);
        }
    }

    public final void hideGameModeToast() {
        NTLightweightHeadsupNotificationToast nTLightweightHeadsupNotificationToast;
        StringBuilder sb = new StringBuilder("hideGameModeToast isAttach = ");
        NTLightweightHeadsupNotificationToast nTLightweightHeadsupNotificationToast2 = this.gameModeToast;
        Intrinsics.checkNotNull(nTLightweightHeadsupNotificationToast2);
        NTLogUtil.m1686d(TAG, sb.append(nTLightweightHeadsupNotificationToast2.isAttachedToWindow()).toString());
        NTLightweightHeadsupNotificationToast nTLightweightHeadsupNotificationToast3 = this.gameModeToast;
        Intrinsics.checkNotNull(nTLightweightHeadsupNotificationToast3);
        if (nTLightweightHeadsupNotificationToast3.isAttachedToWindow() && (nTLightweightHeadsupNotificationToast = this.gameModeToast) != null) {
            nTLightweightHeadsupNotificationToast.releasePopWithAnimation(false, new NTLightweightHeadsupManager$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: hideGameModeToast$lambda-3  reason: not valid java name */
    public static final void m3542hideGameModeToast$lambda3(NTLightweightHeadsupManager nTLightweightHeadsupManager) {
        Intrinsics.checkNotNullParameter(nTLightweightHeadsupManager, "this$0");
        NTLogUtil.m1686d(TAG, "removeGameModeToastFromWindow");
        nTLightweightHeadsupManager.removeGameModeToastFromWindow();
    }

    public final void removeGameModeToastFromWindow() {
        NTLightweightHeadsupNotificationToast nTLightweightHeadsupNotificationToast = this.gameModeToast;
        Intrinsics.checkNotNull(nTLightweightHeadsupNotificationToast);
        if (nTLightweightHeadsupNotificationToast.isAttachedToWindow()) {
            this.windowManager.removeView(this.gameModeToast);
        }
    }

    private final void setPopViewVisibilityWithAnimation(boolean z) {
        if (z) {
            NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView = this.popNotificationView;
            if (nTLightweightHeadsupNotificationView != null) {
                nTLightweightHeadsupNotificationView.setVisibility(0);
            }
            setPopWithAnimation(false, (Runnable) null);
            setPopWithAnimation(true, (Runnable) null);
            return;
        }
        Log.d(TAG, "setHeadsUpViewVisibilityWithAnimation: setVisible = false");
        setPopWithAnimation(false, new NTLightweightHeadsupManager$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: setPopViewVisibilityWithAnimation$lambda-4  reason: not valid java name */
    public static final void m3544setPopViewVisibilityWithAnimation$lambda4(NTLightweightHeadsupManager nTLightweightHeadsupManager) {
        Intrinsics.checkNotNullParameter(nTLightweightHeadsupManager, "this$0");
        NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView = nTLightweightHeadsupManager.popNotificationView;
        if (nTLightweightHeadsupNotificationView != null) {
            nTLightweightHeadsupNotificationView.setVisibility(8);
        }
    }

    public final void setPopWithAnimation(boolean z, Runnable runnable) {
        NTLightweightHeadsupNotificationView nTLightweightHeadsupNotificationView = this.popNotificationView;
        if (nTLightweightHeadsupNotificationView != null) {
            nTLightweightHeadsupNotificationView.releasePopWithAnimation(z, runnable);
        }
    }

    public final void setHeadsUpManager(HeadsUpManager headsUpManager2) {
        this.headsUpManager = headsUpManager2;
    }

    public final void snooze(String str) {
        HeadsUpManager headsUpManager2;
        if (this.headsUpManager != null && !isDisturbForPop(str) && (headsUpManager2 = this.headsUpManager) != null) {
            headsUpManager2.snoozePackage(str);
        }
    }

    public final void onPanelTrackingStarted() {
        removeFromWindow(this.popNotificationView);
    }

    @Metadata(mo65042d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u001e\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\u00070\tj\b\u0012\u0004\u0012\u00020\u0007`\nX\u0004¢\u0006\u0002\n\u0000¨\u0006\u000b"}, mo65043d2 = {"Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupManager$Companion;", "", "()V", "SHOW_GAME_TOAST_DELAY", "", "SHOW_GAME_TOAST_DURATION", "TAG", "", "disturbPKgForPop", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NTLightweightHeadsupManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        ArrayList<String> arrayList = new ArrayList<>();
        disturbPKgForPop = arrayList;
        arrayList.add("com.android.incallui");
    }
}
