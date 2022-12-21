package com.android.systemui.statusbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Debug;
import android.os.SystemClock;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import android.view.View;
import com.android.systemui.C1893R;
import com.android.systemui.Dumpable;
import com.android.systemui.ExpandHelper;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.UdfpsKeyguardViewController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.C2301QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.LockscreenShadeKeyguardTransitionController;
import com.android.systemui.statusbar.SingleShadeLockScreenOverScroller;
import com.android.systemui.statusbar.SplitShadeLockScreenOverScroller;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.LargeScreenUtils;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;
import javax.inject.Inject;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000°\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0001\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u0012\u0006\u0010\u0018\u001a\u00020\u0019\u0012\u0006\u0010\u001a\u001a\u00020\u001b\u0012\u0006\u0010\u001c\u001a\u00020\u001d\u0012\u0006\u0010\u001e\u001a\u00020\u001f\u0012\u0006\u0010 \u001a\u00020!\u0012\u0006\u0010\"\u001a\u00020#¢\u0006\u0002\u0010$J\u0014\u0010\u0001\u001a\u00020*2\t\b\u0002\u0010\u0001\u001a\u00020)H\u0002J\u0011\u0010\u0001\u001a\u00020*2\b\u0010\u0001\u001a\u00030\u0001J\u000f\u0010\u0001\u001a\u00020HH\u0000¢\u0006\u0003\b\u0001J+\u0010\u0001\u001a\u00020*2\b\u0010\u0001\u001a\u00030\u00012\u0010\u0010\u0001\u001a\u000b\u0012\u0006\b\u0001\u0012\u00020&0\u0001H\u0016¢\u0006\u0003\u0010\u0001J\u0010\u0010\u0001\u001a\u00020*2\u0007\u0010\u0001\u001a\u00020HJ \u0010\u0001\u001a\u00020*2\n\u0010\u0001\u001a\u0005\u0018\u00010\u00012\t\b\u0002\u0010\u0001\u001a\u00020HH\u0007J<\u0010 \u0001\u001a\u00020*2\n\u0010¡\u0001\u001a\u0005\u0018\u00010\u00012\u0017\b\u0002\u0010¢\u0001\u001a\u0010\u0012\u0004\u0012\u00020)\u0012\u0004\u0012\u00020*\u0018\u00010(2\f\b\u0002\u0010£\u0001\u001a\u0005\u0018\u00010¤\u0001H\u0002J\u001b\u0010¥\u0001\u001a\u00020H2\n\u0010¦\u0001\u001a\u0005\u0018\u00010§\u0001H\u0000¢\u0006\u0003\b¨\u0001J\u0018\u0010©\u0001\u001a\u00020*2\u0007\u0010ª\u0001\u001a\u00020HH\u0000¢\u0006\u0003\b«\u0001J\u000f\u0010¬\u0001\u001a\u00020*H\u0000¢\u0006\u0003\b­\u0001J\u001b\u0010®\u0001\u001a\u00020*2\n\u0010¯\u0001\u001a\u0005\u0018\u00010§\u0001H\u0000¢\u0006\u0003\b°\u0001J$\u0010±\u0001\u001a\u00020*2\n\u0010¯\u0001\u001a\u0005\u0018\u00010\u00012\u0007\u0010²\u0001\u001a\u000202H\u0000¢\u0006\u0003\b³\u0001J\u0019\u0010´\u0001\u001a\u00020*2\u0007\u0010\u0001\u001a\u00020)2\u0007\u0010µ\u0001\u001a\u000202J\u0007\u0010¶\u0001\u001a\u00020*J\u0012\u0010·\u0001\u001a\u00020*2\u0007\u0010\u0001\u001a\u00020)H\u0002J1\u0010¸\u0001\u001a\u00020*2\u0007\u0010¹\u0001\u001a\u0002072\t\b\u0002\u0010\u0001\u001a\u00020)2\u0012\b\u0002\u0010º\u0001\u001a\u000b\u0012\u0004\u0012\u00020*\u0018\u00010»\u0001H\u0002J\u001b\u0010¼\u0001\u001a\u00020*2\u0007\u0010½\u0001\u001a\u0002072\t\b\u0002\u0010¾\u0001\u001a\u00020HJ\u000f\u0010¿\u0001\u001a\u00020*2\u0006\u0010b\u001a\u00020cJ\u0011\u0010À\u0001\u001a\u00020*2\u0006\u00108\u001a\u000207H\u0002J\t\u0010Á\u0001\u001a\u00020*H\u0002R\u000e\u0010%\u001a\u00020&XD¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u001c\u0010'\u001a\u0010\u0012\u0004\u0012\u00020)\u0012\u0004\u0012\u00020*\u0018\u00010(X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010+\u001a\u00020,X.¢\u0006\u000e\n\u0000\u001a\u0004\b-\u0010.\"\u0004\b/\u00100R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u000202X\u000e¢\u0006\u0002\n\u0000R\u0011\u00103\u001a\u0002028F¢\u0006\u0006\u001a\u0004\b4\u00105R$\u00108\u001a\u0002072\u0006\u00106\u001a\u000207@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b9\u0010:\"\u0004\b;\u0010<R&\u0010=\u001a\u0004\u0018\u00010>8\u0000@\u0000X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b?\u0010@\u001a\u0004\bA\u0010B\"\u0004\bC\u0010DR\u0010\u0010E\u001a\u0004\u0018\u00010FX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010G\u001a\u00020HX\u000e¢\u0006\u0002\n\u0000R \u0010J\u001a\u0002072\u0006\u0010I\u001a\u0002078G@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\bK\u0010:R\u000e\u0010L\u001a\u000202X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010M\u001a\u000202X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010N\u001a\u00020H8@X\u0004¢\u0006\u0006\u001a\u0004\bO\u0010PR\u0014\u0010Q\u001a\u00020H8@X\u0004¢\u0006\u0006\u001a\u0004\bR\u0010PR\u001e\u0010S\u001a\u00020H2\u0006\u0010I\u001a\u00020H@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\bS\u0010PR\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u001b\u0010T\u001a\u00020U8BX\u0002¢\u0006\f\n\u0004\bX\u0010Y\u001a\u0004\bV\u0010WR\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010Z\u001a\u00020HX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010[\u001a\u00020\\X.¢\u0006\u000e\n\u0000\u001a\u0004\b]\u0010^\"\u0004\b_\u0010`R\u000e\u0010a\u001a\u000202X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010b\u001a\u00020cX.¢\u0006\u0002\n\u0000R\u001b\u0010d\u001a\u00020e8BX\u0002¢\u0006\f\n\u0004\bh\u0010Y\u001a\u0004\bf\u0010gR\u000e\u0010i\u001a\u000207X\u000e¢\u0006\u0002\n\u0000R&\u0010j\u001a\u0004\u0018\u00010>8\u0000@\u0000X\u000e¢\u0006\u0014\n\u0000\u0012\u0004\bk\u0010@\u001a\u0004\bl\u0010B\"\u0004\bm\u0010DR\u001a\u0010n\u001a\u00020oX.¢\u0006\u000e\n\u0000\u001a\u0004\bp\u0010q\"\u0004\br\u0010sR\u001e\u0010t\u001a\u0002072\u0006\u0010I\u001a\u000207@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\bu\u0010:R\u000e\u0010v\u001a\u000202X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010w\u001a\u00020x8BX\u0004¢\u0006\u0006\u001a\u0004\by\u0010zR\u000e\u0010\u001a\u001a\u00020\u001bX\u0004¢\u0006\u0002\n\u0000R\u001b\u0010{\u001a\u00020|8BX\u0002¢\u0006\f\n\u0004\b\u0010Y\u001a\u0004\b}\u0010~R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000f\u0010\u0001\u001a\u000202X\u000e¢\u0006\u0002\n\u0000R\u0015\u0010\u0001\u001a\u00030\u0001¢\u0006\n\n\u0000\u001a\u0006\b\u0001\u0010\u0001R\"\u0010\u0001\u001a\u0005\u0018\u00010\u0001X\u000e¢\u0006\u0012\n\u0000\u001a\u0006\b\u0001\u0010\u0001\"\u0006\b\u0001\u0010\u0001R\u000f\u0010\u0001\u001a\u000202X\u000e¢\u0006\u0002\n\u0000R\u000f\u0010\u0001\u001a\u00020HX\u000e¢\u0006\u0002\n\u0000¨\u0006Â\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/LockscreenShadeTransitionController;", "Lcom/android/systemui/Dumpable;", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "logger", "Lcom/android/systemui/statusbar/phone/LSShadeTransitionLogger;", "keyguardBypassController", "Lcom/android/systemui/statusbar/phone/KeyguardBypassController;", "lockScreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "falsingCollector", "Lcom/android/systemui/classifier/FalsingCollector;", "ambientState", "Lcom/android/systemui/statusbar/notification/stack/AmbientState;", "mediaHierarchyManager", "Lcom/android/systemui/media/MediaHierarchyManager;", "scrimTransitionController", "Lcom/android/systemui/statusbar/LockscreenShadeScrimTransitionController;", "keyguardTransitionControllerFactory", "Lcom/android/systemui/statusbar/LockscreenShadeKeyguardTransitionController$Factory;", "depthController", "Lcom/android/systemui/statusbar/NotificationShadeDepthController;", "context", "Landroid/content/Context;", "splitShadeOverScrollerFactory", "Lcom/android/systemui/statusbar/SplitShadeLockScreenOverScroller$Factory;", "singleShadeOverScrollerFactory", "Lcom/android/systemui/statusbar/SingleShadeLockScreenOverScroller$Factory;", "wakefulnessLifecycle", "Lcom/android/systemui/keyguard/WakefulnessLifecycle;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/statusbar/phone/LSShadeTransitionLogger;Lcom/android/systemui/statusbar/phone/KeyguardBypassController;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Lcom/android/systemui/classifier/FalsingCollector;Lcom/android/systemui/statusbar/notification/stack/AmbientState;Lcom/android/systemui/media/MediaHierarchyManager;Lcom/android/systemui/statusbar/LockscreenShadeScrimTransitionController;Lcom/android/systemui/statusbar/LockscreenShadeKeyguardTransitionController$Factory;Lcom/android/systemui/statusbar/NotificationShadeDepthController;Landroid/content/Context;Lcom/android/systemui/statusbar/SplitShadeLockScreenOverScroller$Factory;Lcom/android/systemui/statusbar/SingleShadeLockScreenOverScroller$Factory;Lcom/android/systemui/keyguard/WakefulnessLifecycle;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/plugins/FalsingManager;Lcom/android/systemui/dump/DumpManager;)V", "TAG", "", "animationHandlerOnKeyguardDismiss", "Lkotlin/Function1;", "", "", "centralSurfaces", "Lcom/android/systemui/statusbar/phone/CentralSurfaces;", "getCentralSurfaces", "()Lcom/android/systemui/statusbar/phone/CentralSurfaces;", "setCentralSurfaces", "(Lcom/android/systemui/statusbar/phone/CentralSurfaces;)V", "depthControllerTransitionDistance", "", "distanceUntilShowingPulsingNotifications", "getDistanceUntilShowingPulsingNotifications", "()I", "value", "", "dragDownAmount", "getDragDownAmount", "()F", "setDragDownAmount", "(F)V", "dragDownAnimator", "Landroid/animation/ValueAnimator;", "getDragDownAnimator$SystemUI_nothingRelease$annotations", "()V", "getDragDownAnimator$SystemUI_nothingRelease", "()Landroid/animation/ValueAnimator;", "setDragDownAnimator$SystemUI_nothingRelease", "(Landroid/animation/ValueAnimator;)V", "draggedDownEntry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "forceApplyAmount", "", "<set-?>", "fractionToShade", "getFractionToShade", "fullTransitionDistance", "fullTransitionDistanceByTap", "isDragDownAnywhereEnabled", "isDragDownAnywhereEnabled$SystemUI_nothingRelease", "()Z", "isFalsingCheckNeeded", "isFalsingCheckNeeded$SystemUI_nothingRelease", "isWakingToShadeLocked", "keyguardTransitionController", "Lcom/android/systemui/statusbar/LockscreenShadeKeyguardTransitionController;", "getKeyguardTransitionController", "()Lcom/android/systemui/statusbar/LockscreenShadeKeyguardTransitionController;", "keyguardTransitionController$delegate", "Lkotlin/Lazy;", "nextHideKeyguardNeedsNoAnimation", "notificationPanelController", "Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;", "getNotificationPanelController", "()Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;", "setNotificationPanelController", "(Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;)V", "notificationShelfTransitionDistance", "nsslController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "phoneShadeOverScroller", "Lcom/android/systemui/statusbar/SingleShadeLockScreenOverScroller;", "getPhoneShadeOverScroller", "()Lcom/android/systemui/statusbar/SingleShadeLockScreenOverScroller;", "phoneShadeOverScroller$delegate", "pulseHeight", "pulseHeightAnimator", "getPulseHeightAnimator$SystemUI_nothingRelease$annotations", "getPulseHeightAnimator$SystemUI_nothingRelease", "setPulseHeightAnimator$SystemUI_nothingRelease", "qS", "Lcom/android/systemui/plugins/qs/QS;", "getQS", "()Lcom/android/systemui/plugins/qs/QS;", "setQS", "(Lcom/android/systemui/plugins/qs/QS;)V", "qSDragProgress", "getQSDragProgress", "qsTransitionDistance", "shadeOverScroller", "Lcom/android/systemui/statusbar/LockScreenShadeOverScroller;", "getShadeOverScroller", "()Lcom/android/systemui/statusbar/LockScreenShadeOverScroller;", "splitShadeOverScroller", "Lcom/android/systemui/statusbar/SplitShadeLockScreenOverScroller;", "getSplitShadeOverScroller", "()Lcom/android/systemui/statusbar/SplitShadeLockScreenOverScroller;", "splitShadeOverScroller$delegate", "statusBarTransitionDistance", "touchHelper", "Lcom/android/systemui/statusbar/DragDownHelper;", "getTouchHelper", "()Lcom/android/systemui/statusbar/DragDownHelper;", "udfpsKeyguardViewController", "Lcom/android/systemui/biometrics/UdfpsKeyguardViewController;", "getUdfpsKeyguardViewController", "()Lcom/android/systemui/biometrics/UdfpsKeyguardViewController;", "setUdfpsKeyguardViewController", "(Lcom/android/systemui/biometrics/UdfpsKeyguardViewController;)V", "udfpsTransitionDistance", "useSplitShade", "animateAppear", "delay", "bindController", "notificationShelfController", "Lcom/android/systemui/statusbar/NotificationShelfController;", "canDragDown", "canDragDown$SystemUI_nothingRelease", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "finishPulseAnimation", "cancelled", "goToLockedShade", "expandedView", "Landroid/view/View;", "needsQSAnimation", "goToLockedShadeInternal", "expandView", "animationHandler", "cancelAction", "Ljava/lang/Runnable;", "isDragDownEnabledForView", "view", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "isDragDownEnabledForView$SystemUI_nothingRelease", "onCrossedThreshold", "above", "onCrossedThreshold$SystemUI_nothingRelease", "onDragDownReset", "onDragDownReset$SystemUI_nothingRelease", "onDragDownStarted", "startingChild", "onDragDownStarted$SystemUI_nothingRelease", "onDraggedDown", "dragLengthY", "onDraggedDown$SystemUI_nothingRelease", "onHideKeyguard", "previousState", "onPulseExpansionStarted", "performDefaultGoToFullShadeAnimation", "setDragDownAmountAnimated", "target", "endlistener", "Lkotlin/Function0;", "setPulseHeight", "height", "animate", "setStackScroller", "transitionToShadeAmountCommon", "updateResources", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LockscreenShadeTransitionController.kt */
public final class LockscreenShadeTransitionController implements Dumpable {
    private final String TAG = "LockscreenShadeTransitionController";
    private final AmbientState ambientState;
    private Function1<? super Long, Unit> animationHandlerOnKeyguardDismiss;
    public CentralSurfaces centralSurfaces;
    /* access modifiers changed from: private */
    public final Context context;
    private final NotificationShadeDepthController depthController;
    private int depthControllerTransitionDistance;
    private float dragDownAmount;
    private ValueAnimator dragDownAnimator;
    private NotificationEntry draggedDownEntry;
    private final FalsingCollector falsingCollector;
    /* access modifiers changed from: private */
    public boolean forceApplyAmount;
    private float fractionToShade;
    private int fullTransitionDistance;
    private int fullTransitionDistanceByTap;
    /* access modifiers changed from: private */
    public boolean isWakingToShadeLocked;
    private final KeyguardBypassController keyguardBypassController;
    private final Lazy keyguardTransitionController$delegate;
    /* access modifiers changed from: private */
    public final LockscreenShadeKeyguardTransitionController.Factory keyguardTransitionControllerFactory;
    private final NotificationLockscreenUserManager lockScreenUserManager;
    /* access modifiers changed from: private */
    public final LSShadeTransitionLogger logger;
    private final MediaHierarchyManager mediaHierarchyManager;
    private boolean nextHideKeyguardNeedsNoAnimation;
    public NotificationPanelViewController notificationPanelController;
    private int notificationShelfTransitionDistance;
    /* access modifiers changed from: private */
    public NotificationStackScrollLayoutController nsslController;
    private final Lazy phoneShadeOverScroller$delegate;
    /* access modifiers changed from: private */
    public float pulseHeight;
    private ValueAnimator pulseHeightAnimator;

    /* renamed from: qS */
    public C2301QS f369qS;
    private float qSDragProgress;
    private int qsTransitionDistance;
    private final LockscreenShadeScrimTransitionController scrimTransitionController;
    /* access modifiers changed from: private */
    public final SingleShadeLockScreenOverScroller.Factory singleShadeOverScrollerFactory;
    private final Lazy splitShadeOverScroller$delegate;
    /* access modifiers changed from: private */
    public final SplitShadeLockScreenOverScroller.Factory splitShadeOverScrollerFactory;
    private final SysuiStatusBarStateController statusBarStateController;
    private int statusBarTransitionDistance;
    private final DragDownHelper touchHelper;
    private UdfpsKeyguardViewController udfpsKeyguardViewController;
    private int udfpsTransitionDistance;
    private boolean useSplitShade;

    public static /* synthetic */ void getDragDownAnimator$SystemUI_nothingRelease$annotations() {
    }

    public static /* synthetic */ void getPulseHeightAnimator$SystemUI_nothingRelease$annotations() {
    }

    public final void goToLockedShade(View view) {
        goToLockedShade$default(this, view, false, 2, (Object) null);
    }

    @Inject
    public LockscreenShadeTransitionController(SysuiStatusBarStateController sysuiStatusBarStateController, LSShadeTransitionLogger lSShadeTransitionLogger, KeyguardBypassController keyguardBypassController2, NotificationLockscreenUserManager notificationLockscreenUserManager, FalsingCollector falsingCollector2, AmbientState ambientState2, MediaHierarchyManager mediaHierarchyManager2, LockscreenShadeScrimTransitionController lockscreenShadeScrimTransitionController, LockscreenShadeKeyguardTransitionController.Factory factory, NotificationShadeDepthController notificationShadeDepthController, Context context2, SplitShadeLockScreenOverScroller.Factory factory2, SingleShadeLockScreenOverScroller.Factory factory3, WakefulnessLifecycle wakefulnessLifecycle, ConfigurationController configurationController, FalsingManager falsingManager, DumpManager dumpManager) {
        SysuiStatusBarStateController sysuiStatusBarStateController2 = sysuiStatusBarStateController;
        LSShadeTransitionLogger lSShadeTransitionLogger2 = lSShadeTransitionLogger;
        KeyguardBypassController keyguardBypassController3 = keyguardBypassController2;
        NotificationLockscreenUserManager notificationLockscreenUserManager2 = notificationLockscreenUserManager;
        FalsingCollector falsingCollector3 = falsingCollector2;
        AmbientState ambientState3 = ambientState2;
        MediaHierarchyManager mediaHierarchyManager3 = mediaHierarchyManager2;
        LockscreenShadeScrimTransitionController lockscreenShadeScrimTransitionController2 = lockscreenShadeScrimTransitionController;
        LockscreenShadeKeyguardTransitionController.Factory factory4 = factory;
        NotificationShadeDepthController notificationShadeDepthController2 = notificationShadeDepthController;
        Context context3 = context2;
        SplitShadeLockScreenOverScroller.Factory factory5 = factory2;
        SingleShadeLockScreenOverScroller.Factory factory6 = factory3;
        ConfigurationController configurationController2 = configurationController;
        FalsingManager falsingManager2 = falsingManager;
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController2, "statusBarStateController");
        Intrinsics.checkNotNullParameter(lSShadeTransitionLogger2, "logger");
        Intrinsics.checkNotNullParameter(keyguardBypassController3, "keyguardBypassController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager2, "lockScreenUserManager");
        Intrinsics.checkNotNullParameter(falsingCollector3, "falsingCollector");
        Intrinsics.checkNotNullParameter(ambientState3, "ambientState");
        Intrinsics.checkNotNullParameter(mediaHierarchyManager3, "mediaHierarchyManager");
        Intrinsics.checkNotNullParameter(lockscreenShadeScrimTransitionController2, "scrimTransitionController");
        Intrinsics.checkNotNullParameter(factory4, "keyguardTransitionControllerFactory");
        Intrinsics.checkNotNullParameter(notificationShadeDepthController2, "depthController");
        Intrinsics.checkNotNullParameter(context3, "context");
        Intrinsics.checkNotNullParameter(factory5, "splitShadeOverScrollerFactory");
        Intrinsics.checkNotNullParameter(factory6, "singleShadeOverScrollerFactory");
        Intrinsics.checkNotNullParameter(wakefulnessLifecycle, "wakefulnessLifecycle");
        Intrinsics.checkNotNullParameter(configurationController2, "configurationController");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.statusBarStateController = sysuiStatusBarStateController2;
        this.logger = lSShadeTransitionLogger2;
        this.keyguardBypassController = keyguardBypassController3;
        this.lockScreenUserManager = notificationLockscreenUserManager2;
        this.falsingCollector = falsingCollector3;
        this.ambientState = ambientState3;
        this.mediaHierarchyManager = mediaHierarchyManager3;
        this.scrimTransitionController = lockscreenShadeScrimTransitionController2;
        this.keyguardTransitionControllerFactory = factory4;
        this.depthController = notificationShadeDepthController2;
        this.context = context3;
        this.splitShadeOverScrollerFactory = factory5;
        this.singleShadeOverScrollerFactory = factory6;
        this.touchHelper = new DragDownHelper(falsingManager, falsingCollector3, this, context3);
        this.splitShadeOverScroller$delegate = LazyKt.lazy(new LockscreenShadeTransitionController$splitShadeOverScroller$2(this));
        this.phoneShadeOverScroller$delegate = LazyKt.lazy(new LockscreenShadeTransitionController$phoneShadeOverScroller$2(this));
        this.keyguardTransitionController$delegate = LazyKt.lazy(new C2557x794b57c2(this));
        updateResources();
        configurationController2.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ LockscreenShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateResources();
                this.this$0.getTouchHelper().updateResources(this.this$0.context);
            }
        });
        dumpManager.registerDumpable(this);
        sysuiStatusBarStateController2.addCallback(new StatusBarStateController.StateListener(this) {
            final /* synthetic */ LockscreenShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onExpandedChanged(boolean z) {
                if (!z) {
                    boolean z2 = true;
                    if (!(this.this$0.getDragDownAmount() == 0.0f)) {
                        ValueAnimator dragDownAnimator$SystemUI_nothingRelease = this.this$0.getDragDownAnimator$SystemUI_nothingRelease();
                        if (!(dragDownAnimator$SystemUI_nothingRelease != null && dragDownAnimator$SystemUI_nothingRelease.isRunning())) {
                            this.this$0.logger.logDragDownAmountResetWhenFullyCollapsed();
                            this.this$0.setDragDownAmount(0.0f);
                        }
                    }
                    if (!(this.this$0.pulseHeight == 0.0f)) {
                        ValueAnimator pulseHeightAnimator$SystemUI_nothingRelease = this.this$0.getPulseHeightAnimator$SystemUI_nothingRelease();
                        if (pulseHeightAnimator$SystemUI_nothingRelease == null || !pulseHeightAnimator$SystemUI_nothingRelease.isRunning()) {
                            z2 = false;
                        }
                        if (!z2) {
                            this.this$0.logger.logPulseHeightNotResetWhenFullyCollapsed();
                            this.this$0.setPulseHeight(0.0f, false);
                        }
                    }
                }
            }
        });
        wakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer(this) {
            final /* synthetic */ LockscreenShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onPostFinishedWakingUp() {
                this.this$0.isWakingToShadeLocked = false;
            }
        });
    }

    public final float getFractionToShade() {
        return this.fractionToShade;
    }

    public final NotificationPanelViewController getNotificationPanelController() {
        NotificationPanelViewController notificationPanelViewController = this.notificationPanelController;
        if (notificationPanelViewController != null) {
            return notificationPanelViewController;
        }
        Intrinsics.throwUninitializedPropertyAccessException("notificationPanelController");
        return null;
    }

    public final void setNotificationPanelController(NotificationPanelViewController notificationPanelViewController) {
        Intrinsics.checkNotNullParameter(notificationPanelViewController, "<set-?>");
        this.notificationPanelController = notificationPanelViewController;
    }

    public final CentralSurfaces getCentralSurfaces() {
        CentralSurfaces centralSurfaces2 = this.centralSurfaces;
        if (centralSurfaces2 != null) {
            return centralSurfaces2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("centralSurfaces");
        return null;
    }

    public final void setCentralSurfaces(CentralSurfaces centralSurfaces2) {
        Intrinsics.checkNotNullParameter(centralSurfaces2, "<set-?>");
        this.centralSurfaces = centralSurfaces2;
    }

    public final C2301QS getQS() {
        C2301QS qs = this.f369qS;
        if (qs != null) {
            return qs;
        }
        Intrinsics.throwUninitializedPropertyAccessException("qS");
        return null;
    }

    public final void setQS(C2301QS qs) {
        Intrinsics.checkNotNullParameter(qs, "<set-?>");
        this.f369qS = qs;
    }

    public final ValueAnimator getDragDownAnimator$SystemUI_nothingRelease() {
        return this.dragDownAnimator;
    }

    public final void setDragDownAnimator$SystemUI_nothingRelease(ValueAnimator valueAnimator) {
        this.dragDownAnimator = valueAnimator;
    }

    public final ValueAnimator getPulseHeightAnimator$SystemUI_nothingRelease() {
        return this.pulseHeightAnimator;
    }

    public final void setPulseHeightAnimator$SystemUI_nothingRelease(ValueAnimator valueAnimator) {
        this.pulseHeightAnimator = valueAnimator;
    }

    public final boolean isWakingToShadeLocked() {
        return this.isWakingToShadeLocked;
    }

    public final int getDistanceUntilShowingPulsingNotifications() {
        return this.fullTransitionDistance;
    }

    public final UdfpsKeyguardViewController getUdfpsKeyguardViewController() {
        return this.udfpsKeyguardViewController;
    }

    public final void setUdfpsKeyguardViewController(UdfpsKeyguardViewController udfpsKeyguardViewController2) {
        this.udfpsKeyguardViewController = udfpsKeyguardViewController2;
    }

    public final DragDownHelper getTouchHelper() {
        return this.touchHelper;
    }

    private final SplitShadeLockScreenOverScroller getSplitShadeOverScroller() {
        return (SplitShadeLockScreenOverScroller) this.splitShadeOverScroller$delegate.getValue();
    }

    private final SingleShadeLockScreenOverScroller getPhoneShadeOverScroller() {
        return (SingleShadeLockScreenOverScroller) this.phoneShadeOverScroller$delegate.getValue();
    }

    private final LockscreenShadeKeyguardTransitionController getKeyguardTransitionController() {
        return (LockscreenShadeKeyguardTransitionController) this.keyguardTransitionController$delegate.getValue();
    }

    private final LockScreenShadeOverScroller getShadeOverScroller() {
        return this.useSplitShade ? getSplitShadeOverScroller() : getPhoneShadeOverScroller();
    }

    /* access modifiers changed from: private */
    public final void updateResources() {
        this.fullTransitionDistance = this.context.getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_full_transition_distance);
        this.fullTransitionDistanceByTap = this.context.getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_transition_by_tap_distance);
        this.notificationShelfTransitionDistance = this.context.getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_notif_shelf_transition_distance);
        this.qsTransitionDistance = this.context.getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_qs_transition_distance);
        this.depthControllerTransitionDistance = this.context.getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_depth_controller_transition_distance);
        this.udfpsTransitionDistance = this.context.getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_udfps_keyguard_transition_distance);
        this.statusBarTransitionDistance = this.context.getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_status_bar_transition_distance);
        Resources resources = this.context.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "context.resources");
        this.useSplitShade = LargeScreenUtils.shouldUseSplitNotificationShade(resources);
    }

    public final void setStackScroller(NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutController, "nsslController");
        this.nsslController = notificationStackScrollLayoutController;
        DragDownHelper dragDownHelper = this.touchHelper;
        NotificationStackScrollLayout view = notificationStackScrollLayoutController.getView();
        Intrinsics.checkNotNullExpressionValue(view, "nsslController.view");
        dragDownHelper.setHost(view);
        DragDownHelper dragDownHelper2 = this.touchHelper;
        ExpandHelper.Callback expandHelperCallback = notificationStackScrollLayoutController.getExpandHelperCallback();
        Intrinsics.checkNotNullExpressionValue(expandHelperCallback, "nsslController.expandHelperCallback");
        dragDownHelper2.setExpandCallback(expandHelperCallback);
    }

    public final void bindController(NotificationShelfController notificationShelfController) {
        Intrinsics.checkNotNullParameter(notificationShelfController, "notificationShelfController");
        notificationShelfController.setOnClickListener(new LockscreenShadeTransitionController$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: bindController$lambda-0  reason: not valid java name */
    public static final void m3027bindController$lambda0(LockscreenShadeTransitionController lockscreenShadeTransitionController, View view) {
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "this$0");
        if (lockscreenShadeTransitionController.statusBarStateController.getState() == 1) {
            lockscreenShadeTransitionController.getCentralSurfaces().wakeUpIfDozing(SystemClock.uptimeMillis(), view, "SHADE_CLICK");
            goToLockedShade$default(lockscreenShadeTransitionController, view, false, 2, (Object) null);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0018, code lost:
        if (r0.isInLockedDownShade() != false) goto L_0x001a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean canDragDown$SystemUI_nothingRelease() {
        /*
            r2 = this;
            com.android.systemui.statusbar.SysuiStatusBarStateController r0 = r2.statusBarStateController
            int r0 = r0.getState()
            r1 = 1
            if (r0 == r1) goto L_0x001a
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r2.nsslController
            if (r0 != 0) goto L_0x0014
            java.lang.String r0 = "nsslController"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r0)
            r0 = 0
        L_0x0014:
            boolean r0 = r0.isInLockedDownShade()
            if (r0 == 0) goto L_0x0029
        L_0x001a:
            com.android.systemui.plugins.qs.QS r0 = r2.getQS()
            boolean r0 = r0.isFullyCollapsed()
            if (r0 != 0) goto L_0x002a
            boolean r2 = r2.useSplitShade
            if (r2 == 0) goto L_0x0029
            goto L_0x002a
        L_0x0029:
            r1 = 0
        L_0x002a:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.LockscreenShadeTransitionController.canDragDown$SystemUI_nothingRelease():boolean");
    }

    public final void onDraggedDown$SystemUI_nothingRelease(View view, int i) {
        if (canDragDown$SystemUI_nothingRelease()) {
            LockscreenShadeTransitionController$$ExternalSyntheticLambda4 lockscreenShadeTransitionController$$ExternalSyntheticLambda4 = new LockscreenShadeTransitionController$$ExternalSyntheticLambda4(this);
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
            if (notificationStackScrollLayoutController == null) {
                Intrinsics.throwUninitializedPropertyAccessException("nsslController");
                notificationStackScrollLayoutController = null;
            }
            if (notificationStackScrollLayoutController.isInLockedDownShade()) {
                this.logger.logDraggedDownLockDownShade(view);
                this.statusBarStateController.setLeaveOpenOnKeyguardHide(true);
                getCentralSurfaces().dismissKeyguardThenExecute(new LockscreenShadeTransitionController$$ExternalSyntheticLambda5(this), lockscreenShadeTransitionController$$ExternalSyntheticLambda4, false);
                return;
            }
            this.logger.logDraggedDown(view, i);
            if (!this.ambientState.isDozing() || view != null) {
                Function1 lockscreenShadeTransitionController$onDraggedDown$animationHandler$1 = new C2558x21fef5f(view, this);
                NotificationLockscreenUserManager notificationLockscreenUserManager = this.lockScreenUserManager;
                if (!notificationLockscreenUserManager.userAllowsNotificationsAndContents(notificationLockscreenUserManager.getCurrentUserId())) {
                    setDragDownAmount((float) getQS().getQsMinExpansionHeight());
                }
                goToLockedShadeInternal(view, lockscreenShadeTransitionController$onDraggedDown$animationHandler$1, lockscreenShadeTransitionController$$ExternalSyntheticLambda4);
                return;
            }
            return;
        }
        this.logger.logUnSuccessfulDragDown(view);
        setDragDownAmountAnimated$default(this, 0.0f, 0, (Function0) null, 6, (Object) null);
    }

    /* access modifiers changed from: private */
    /* renamed from: onDraggedDown$lambda-1  reason: not valid java name */
    public static final void m3030onDraggedDown$lambda1(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "this$0");
        lockscreenShadeTransitionController.logger.logGoingToLockedShadeAborted();
        setDragDownAmountAnimated$default(lockscreenShadeTransitionController, 0.0f, 0, (Function0) null, 6, (Object) null);
    }

    /* access modifiers changed from: private */
    /* renamed from: onDraggedDown$lambda-2  reason: not valid java name */
    public static final boolean m3031onDraggedDown$lambda2(LockscreenShadeTransitionController lockscreenShadeTransitionController) {
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "this$0");
        lockscreenShadeTransitionController.nextHideKeyguardNeedsNoAnimation = true;
        return false;
    }

    public final void onDragDownReset$SystemUI_nothingRelease() {
        this.logger.logDragDownAborted();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = null;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.setDimmed(true, true);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = this.nsslController;
        if (notificationStackScrollLayoutController3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            notificationStackScrollLayoutController3 = null;
        }
        notificationStackScrollLayoutController3.resetScrollPosition();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController4 = this.nsslController;
        if (notificationStackScrollLayoutController4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
        } else {
            notificationStackScrollLayoutController2 = notificationStackScrollLayoutController4;
        }
        notificationStackScrollLayoutController2.resetCheckSnoozeLeavebehind();
        setDragDownAmountAnimated$default(this, 0.0f, 0, (Function0) null, 6, (Object) null);
    }

    public final void onCrossedThreshold$SystemUI_nothingRelease(boolean z) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.setDimmed(!z, true);
    }

    public final void onDragDownStarted$SystemUI_nothingRelease(ExpandableView expandableView) {
        this.logger.logDragDownStarted(expandableView);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = null;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            notificationStackScrollLayoutController = null;
        }
        notificationStackScrollLayoutController.cancelLongPress();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = this.nsslController;
        if (notificationStackScrollLayoutController3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
        } else {
            notificationStackScrollLayoutController2 = notificationStackScrollLayoutController3;
        }
        notificationStackScrollLayoutController2.checkSnoozeLeavebehind();
        ValueAnimator valueAnimator = this.dragDownAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.logger.logAnimationCancelled(false);
            valueAnimator.cancel();
        }
    }

    public final boolean isFalsingCheckNeeded$SystemUI_nothingRelease() {
        return this.statusBarStateController.getState() == 1;
    }

    public final boolean isDragDownEnabledForView$SystemUI_nothingRelease(ExpandableView expandableView) {
        if (isDragDownAnywhereEnabled$SystemUI_nothingRelease()) {
            return true;
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            notificationStackScrollLayoutController = null;
        }
        if (!notificationStackScrollLayoutController.isInLockedDownShade()) {
            return false;
        }
        if (expandableView == null) {
            return true;
        }
        if (expandableView instanceof ExpandableNotificationRow) {
            return ((ExpandableNotificationRow) expandableView).getEntry().isSensitive();
        }
        return false;
    }

    public final boolean isDragDownAnywhereEnabled$SystemUI_nothingRelease() {
        if (this.statusBarStateController.getState() != 1 || this.keyguardBypassController.getBypassEnabled() || (!getQS().isFullyCollapsed() && !this.useSplitShade)) {
            return false;
        }
        return true;
    }

    public float getDragDownAmount() {
        return this.dragDownAmount;
    }

    public void setDragDownAmount(float f) {
        boolean z = true;
        if (!(this.dragDownAmount == f) || this.forceApplyAmount) {
            this.dragDownAmount = f;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = null;
            if (notificationStackScrollLayoutController == null) {
                Intrinsics.throwUninitializedPropertyAccessException("nsslController");
                notificationStackScrollLayoutController = null;
            }
            if (notificationStackScrollLayoutController.isInLockedDownShade()) {
                if (this.dragDownAmount != 0.0f) {
                    z = false;
                }
                if (!z && !this.forceApplyAmount) {
                    return;
                }
            }
            this.fractionToShade = MathUtils.saturate(getDragDownAmount() / ((float) this.notificationShelfTransitionDistance));
            NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = this.nsslController;
            if (notificationStackScrollLayoutController3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            } else {
                notificationStackScrollLayoutController2 = notificationStackScrollLayoutController3;
            }
            notificationStackScrollLayoutController2.setTransitionToFullShadeAmount(this.fractionToShade);
            this.qSDragProgress = MathUtils.saturate(getDragDownAmount() / ((float) this.qsTransitionDistance));
            getQS().setTransitionToFullShadeAmount(this.dragDownAmount, this.qSDragProgress);
            getNotificationPanelController().setTransitionToFullShadeAmount(this.dragDownAmount, false, 0);
            this.mediaHierarchyManager.setTransitionToFullShadeAmount(this.dragDownAmount);
            this.scrimTransitionController.setDragDownAmount(f);
            transitionToShadeAmountCommon(this.dragDownAmount);
            getKeyguardTransitionController().setDragDownAmount(f);
            getShadeOverScroller().setExpansionDragDownAmount(getDragDownAmount());
        }
    }

    public final float getQSDragProgress() {
        return this.qSDragProgress;
    }

    private final void transitionToShadeAmountCommon(float f) {
        int i = this.depthControllerTransitionDistance;
        if (i == 0) {
            this.depthController.setTransitionToFullShadeProgress(0.0f);
        } else {
            this.depthController.setTransitionToFullShadeProgress(MathUtils.saturate(f / ((float) i)));
        }
        float saturate = MathUtils.saturate(f / ((float) this.udfpsTransitionDistance));
        NTLogUtil.m1680d(this.TAG, "udfpsProgress " + saturate + " dragDownAmount " + f + " udfpsTransitionDistance " + this.udfpsTransitionDistance + " - " + Debug.getCallers(5));
        UdfpsKeyguardViewController udfpsKeyguardViewController2 = this.udfpsKeyguardViewController;
        if (udfpsKeyguardViewController2 != null) {
            udfpsKeyguardViewController2.setTransitionToFullShadeProgress(saturate);
        }
        getCentralSurfaces().setTransitionToFullShadeProgress(MathUtils.saturate(f / ((float) this.statusBarTransitionDistance)));
    }

    static /* synthetic */ void setDragDownAmountAnimated$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, float f, long j, Function0 function0, int i, Object obj) {
        if ((i & 2) != 0) {
            j = 0;
        }
        if ((i & 4) != 0) {
            function0 = null;
        }
        lockscreenShadeTransitionController.setDragDownAmountAnimated(f, j, function0);
    }

    private final void setDragDownAmountAnimated(float f, long j, Function0<Unit> function0) {
        this.logger.logDragDownAnimation(f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{getDragDownAmount(), f});
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.setDuration(375);
        ofFloat.addUpdateListener(new LockscreenShadeTransitionController$$ExternalSyntheticLambda6(this));
        if (j > 0) {
            ofFloat.setStartDelay(j);
        }
        if (function0 != null) {
            ofFloat.addListener(new LockscreenShadeTransitionController$setDragDownAmountAnimated$2(function0));
        }
        ofFloat.start();
        this.dragDownAnimator = ofFloat;
    }

    /* access modifiers changed from: private */
    /* renamed from: setDragDownAmountAnimated$lambda-4  reason: not valid java name */
    public static final void m3032setDragDownAmountAnimated$lambda4(LockscreenShadeTransitionController lockscreenShadeTransitionController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "animation");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            lockscreenShadeTransitionController.setDragDownAmount(((Float) animatedValue).floatValue());
            lockscreenShadeTransitionController.getNotificationPanelController().requestScrollerTopPaddingUpdate(false);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    static /* synthetic */ void animateAppear$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, long j, int i, Object obj) {
        if ((i & 1) != 0) {
            j = 0;
        }
        lockscreenShadeTransitionController.animateAppear(j);
    }

    private final void animateAppear(long j) {
        this.forceApplyAmount = true;
        setDragDownAmount(1.0f);
        setDragDownAmountAnimated((float) this.fullTransitionDistanceByTap, j, new LockscreenShadeTransitionController$animateAppear$1(this));
    }

    public static /* synthetic */ void goToLockedShade$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, View view, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = true;
        }
        lockscreenShadeTransitionController.goToLockedShade(view, z);
    }

    public final void goToLockedShade(View view, boolean z) {
        Function1 function1;
        boolean z2 = true;
        if (this.statusBarStateController.getState() != 1) {
            z2 = false;
        }
        this.logger.logTryGoToLockedShade(z2);
        if (z2) {
            if (z) {
                function1 = null;
            } else {
                function1 = new LockscreenShadeTransitionController$goToLockedShade$1(this);
            }
            goToLockedShadeInternal(view, function1, (Runnable) null);
        }
    }

    static /* synthetic */ void goToLockedShadeInternal$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, View view, Function1 function1, Runnable runnable, int i, Object obj) {
        if ((i & 2) != 0) {
            function1 = null;
        }
        if ((i & 4) != 0) {
            runnable = null;
        }
        lockscreenShadeTransitionController.goToLockedShadeInternal(view, function1, runnable);
    }

    private final void goToLockedShadeInternal(View view, Function1<? super Long, Unit> function1, Runnable runnable) {
        NotificationEntry notificationEntry;
        if (getCentralSurfaces().isShadeDisabled()) {
            if (runnable != null) {
                runnable.run();
            }
            this.logger.logShadeDisabledOnGoToLockedShade();
            return;
        }
        int currentUserId = this.lockScreenUserManager.getCurrentUserId();
        LockscreenShadeTransitionController$$ExternalSyntheticLambda0 lockscreenShadeTransitionController$$ExternalSyntheticLambda0 = null;
        if (view instanceof ExpandableNotificationRow) {
            notificationEntry = ((ExpandableNotificationRow) view).getEntry();
            notificationEntry.setUserExpanded(true, true);
            notificationEntry.setGroupExpansionChanging(true);
            currentUserId = notificationEntry.getSbn().getUserId();
        } else {
            notificationEntry = null;
        }
        NotificationLockscreenUserManager notificationLockscreenUserManager = this.lockScreenUserManager;
        boolean z = false;
        boolean z2 = !notificationLockscreenUserManager.userAllowsPrivateNotificationsInPublic(notificationLockscreenUserManager.getCurrentUserId()) || !this.lockScreenUserManager.shouldShowLockscreenNotifications() || this.falsingCollector.shouldEnforceBouncer();
        if (this.keyguardBypassController.getBypassEnabled()) {
            z2 = false;
        }
        if (!this.lockScreenUserManager.isLockscreenPublicMode(currentUserId) || !z2) {
            LSShadeTransitionLogger lSShadeTransitionLogger = this.logger;
            if (function1 != null) {
                z = true;
            }
            lSShadeTransitionLogger.logGoingToLockedShade(z);
            if (this.statusBarStateController.isDozing()) {
                this.isWakingToShadeLocked = true;
            }
            this.statusBarStateController.setState(2);
            if (function1 != null) {
                function1.invoke(0L);
            } else {
                performDefaultGoToFullShadeAnimation(0);
            }
        } else {
            this.statusBarStateController.setLeaveOpenOnKeyguardHide(true);
            if (function1 != null) {
                lockscreenShadeTransitionController$$ExternalSyntheticLambda0 = new LockscreenShadeTransitionController$$ExternalSyntheticLambda0(this, function1);
            }
            LockscreenShadeTransitionController$$ExternalSyntheticLambda1 lockscreenShadeTransitionController$$ExternalSyntheticLambda1 = new LockscreenShadeTransitionController$$ExternalSyntheticLambda1(this, runnable);
            this.logger.logShowBouncerOnGoToLockedShade();
            getCentralSurfaces().showBouncerWithDimissAndCancelIfKeyguard(lockscreenShadeTransitionController$$ExternalSyntheticLambda0, lockscreenShadeTransitionController$$ExternalSyntheticLambda1);
            this.draggedDownEntry = notificationEntry;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: goToLockedShadeInternal$lambda-5  reason: not valid java name */
    public static final boolean m3028goToLockedShadeInternal$lambda5(LockscreenShadeTransitionController lockscreenShadeTransitionController, Function1 function1) {
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "this$0");
        lockscreenShadeTransitionController.animationHandlerOnKeyguardDismiss = function1;
        return false;
    }

    /* access modifiers changed from: private */
    /* renamed from: goToLockedShadeInternal$lambda-7  reason: not valid java name */
    public static final void m3029goToLockedShadeInternal$lambda7(LockscreenShadeTransitionController lockscreenShadeTransitionController, Runnable runnable) {
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "this$0");
        NotificationEntry notificationEntry = lockscreenShadeTransitionController.draggedDownEntry;
        if (notificationEntry != null) {
            notificationEntry.setUserLocked(false);
            notificationEntry.notifyHeightChanged(false);
            lockscreenShadeTransitionController.draggedDownEntry = null;
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    public final void onHideKeyguard(long j, int i) {
        this.logger.logOnHideKeyguard();
        Function1<? super Long, Unit> function1 = this.animationHandlerOnKeyguardDismiss;
        if (function1 != null) {
            Intrinsics.checkNotNull(function1);
            function1.invoke(Long.valueOf(j));
            this.animationHandlerOnKeyguardDismiss = null;
        } else if (this.nextHideKeyguardNeedsNoAnimation) {
            this.nextHideKeyguardNeedsNoAnimation = false;
        } else if (i != 2) {
            performDefaultGoToFullShadeAnimation(j);
        }
        NotificationEntry notificationEntry = this.draggedDownEntry;
        if (notificationEntry != null) {
            notificationEntry.setUserLocked(false);
            this.draggedDownEntry = null;
        }
    }

    private final void performDefaultGoToFullShadeAnimation(long j) {
        this.logger.logDefaultGoToFullShadeAnimation(j);
        getNotificationPanelController().animateToFullShade(j);
        animateAppear(j);
    }

    public static /* synthetic */ void setPulseHeight$default(LockscreenShadeTransitionController lockscreenShadeTransitionController, float f, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        lockscreenShadeTransitionController.setPulseHeight(f, z);
    }

    public final void setPulseHeight(float f, boolean z) {
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{this.pulseHeight, f});
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofFloat.setDuration(375);
            ofFloat.addUpdateListener(new LockscreenShadeTransitionController$$ExternalSyntheticLambda3(this));
            ofFloat.start();
            this.pulseHeightAnimator = ofFloat;
            return;
        }
        this.pulseHeight = f;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("nsslController");
            notificationStackScrollLayoutController = null;
        }
        getNotificationPanelController().setOverStrechAmount(notificationStackScrollLayoutController.setPulseHeight(f));
        if (!this.keyguardBypassController.getBypassEnabled()) {
            f = 0.0f;
        }
        transitionToShadeAmountCommon(f);
    }

    /* access modifiers changed from: private */
    /* renamed from: setPulseHeight$lambda-9  reason: not valid java name */
    public static final void m3033setPulseHeight$lambda9(LockscreenShadeTransitionController lockscreenShadeTransitionController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(lockscreenShadeTransitionController, "this$0");
        Intrinsics.checkNotNullParameter(valueAnimator, "animation");
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            setPulseHeight$default(lockscreenShadeTransitionController, ((Float) animatedValue).floatValue(), false, 2, (Object) null);
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    public final void finishPulseAnimation(boolean z) {
        this.logger.logPulseExpansionFinished(z);
        if (z) {
            setPulseHeight(0.0f, true);
            return;
        }
        getNotificationPanelController().onPulseExpansionFinished();
        setPulseHeight(0.0f, false);
    }

    public final void onPulseExpansionStarted() {
        this.logger.logPulseExpansionStarted();
        ValueAnimator valueAnimator = this.pulseHeightAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.logger.logAnimationCancelled(true);
            valueAnimator.cancel();
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("LSShadeTransitionController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("pulseHeight: " + this.pulseHeight);
        indentingPrintWriter.println("useSplitShade: " + this.useSplitShade);
        indentingPrintWriter.println("dragDownAmount: " + getDragDownAmount());
        indentingPrintWriter.println("qSDragProgress: " + this.qSDragProgress);
        indentingPrintWriter.println("isDragDownAnywhereEnabled: " + isDragDownAnywhereEnabled$SystemUI_nothingRelease());
        indentingPrintWriter.println("isFalsingCheckNeeded: " + isFalsingCheckNeeded$SystemUI_nothingRelease());
        indentingPrintWriter.println("isWakingToShadeLocked: " + this.isWakingToShadeLocked);
        indentingPrintWriter.println("hasPendingHandlerOnKeyguardDismiss: " + (this.animationHandlerOnKeyguardDismiss != null));
    }
}
