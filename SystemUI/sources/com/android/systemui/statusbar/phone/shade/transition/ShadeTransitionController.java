package com.android.systemui.statusbar.phone.shade.transition;

import android.content.Context;
import android.content.res.Configuration;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.p011qs.C2304QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.panelstate.PanelStateListener;
import com.android.systemui.statusbar.phone.shade.transition.SplitShadeOverScroller;
import com.android.systemui.statusbar.policy.ConfigurationController;
import javax.inject.Inject;
import kotlin.Function;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.FunctionAdapter;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0010\u0010+\u001a\u00020,2\u0006\u0010-\u001a\u00020.H\u0002J\u0010\u0010/\u001a\u00020,2\u0006\u00100\u001a\u000201H\u0002J\b\u00102\u001a\u00020\u000eH\u0002J\b\u00103\u001a\u00020,H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0012\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\u0015\u001a\u00020\u0016X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u001a\u0010\u001b\u001a\u00020\u001cX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001d\u0010\u001e\"\u0004\b\u001f\u0010 R\u0014\u0010!\u001a\u00020\"8BX\u0004¢\u0006\u0006\u001a\u0004\b#\u0010$R\u001b\u0010%\u001a\u00020&8BX\u0002¢\u0006\f\n\u0004\b)\u0010*\u001a\u0004\b'\u0010(R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u00064"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/shade/transition/ShadeTransitionController;", "", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "panelExpansionStateManager", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;", "context", "Landroid/content/Context;", "splitShadeOverScrollerFactory", "Lcom/android/systemui/statusbar/phone/shade/transition/SplitShadeOverScroller$Factory;", "noOpOverScroller", "Lcom/android/systemui/statusbar/phone/shade/transition/NoOpOverScroller;", "(Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;Landroid/content/Context;Lcom/android/systemui/statusbar/phone/shade/transition/SplitShadeOverScroller$Factory;Lcom/android/systemui/statusbar/phone/shade/transition/NoOpOverScroller;)V", "inSplitShade", "", "notificationPanelViewController", "Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;", "getNotificationPanelViewController", "()Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;", "setNotificationPanelViewController", "(Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;)V", "notificationStackScrollLayoutController", "Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "getNotificationStackScrollLayoutController", "()Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;", "setNotificationStackScrollLayoutController", "(Lcom/android/systemui/statusbar/notification/stack/NotificationStackScrollLayoutController;)V", "qs", "Lcom/android/systemui/plugins/qs/QS;", "getQs", "()Lcom/android/systemui/plugins/qs/QS;", "setQs", "(Lcom/android/systemui/plugins/qs/QS;)V", "shadeOverScroller", "Lcom/android/systemui/statusbar/phone/shade/transition/ShadeOverScroller;", "getShadeOverScroller", "()Lcom/android/systemui/statusbar/phone/shade/transition/ShadeOverScroller;", "splitShadeOverScroller", "Lcom/android/systemui/statusbar/phone/shade/transition/SplitShadeOverScroller;", "getSplitShadeOverScroller", "()Lcom/android/systemui/statusbar/phone/shade/transition/SplitShadeOverScroller;", "splitShadeOverScroller$delegate", "Lkotlin/Lazy;", "onPanelExpansionChanged", "", "event", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionChangeEvent;", "onPanelStateChanged", "state", "", "propertiesInitialized", "updateResources", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ShadeTransitionController.kt */
public final class ShadeTransitionController {
    private final Context context;
    private boolean inSplitShade;
    private final NoOpOverScroller noOpOverScroller;
    public NotificationPanelViewController notificationPanelViewController;
    public NotificationStackScrollLayoutController notificationStackScrollLayoutController;

    /* renamed from: qs */
    public C2304QS f391qs;
    private final Lazy splitShadeOverScroller$delegate = LazyKt.lazy(new ShadeTransitionController$splitShadeOverScroller$2(this));
    /* access modifiers changed from: private */
    public final SplitShadeOverScroller.Factory splitShadeOverScrollerFactory;

    @Inject
    public ShadeTransitionController(ConfigurationController configurationController, PanelExpansionStateManager panelExpansionStateManager, Context context2, SplitShadeOverScroller.Factory factory, NoOpOverScroller noOpOverScroller2) {
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(panelExpansionStateManager, "panelExpansionStateManager");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(factory, "splitShadeOverScrollerFactory");
        Intrinsics.checkNotNullParameter(noOpOverScroller2, "noOpOverScroller");
        this.context = context2;
        this.splitShadeOverScrollerFactory = factory;
        this.noOpOverScroller = noOpOverScroller2;
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ ShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateResources();
            }
        });
        panelExpansionStateManager.addExpansionListener(new Object() {
            public final boolean equals(Object obj) {
                if (!(obj instanceof PanelExpansionListener) || !(obj instanceof FunctionAdapter)) {
                    return false;
                }
                return Intrinsics.areEqual((Object) getFunctionDelegate(), (Object) ((FunctionAdapter) obj).getFunctionDelegate());
            }

            public final Function<?> getFunctionDelegate() {
                return new FunctionReferenceImpl(1, ShadeTransitionController.this, ShadeTransitionController.class, "onPanelExpansionChanged", "onPanelExpansionChanged(Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionChangeEvent;)V", 0);
            }

            public final int hashCode() {
                return getFunctionDelegate().hashCode();
            }

            public final void onPanelExpansionChanged(PanelExpansionChangeEvent panelExpansionChangeEvent) {
                Intrinsics.checkNotNullParameter(panelExpansionChangeEvent, "p0");
                ShadeTransitionController.this.onPanelExpansionChanged(panelExpansionChangeEvent);
            }
        });
        panelExpansionStateManager.addStateListener(new Object() {
            public final boolean equals(Object obj) {
                if (!(obj instanceof PanelStateListener) || !(obj instanceof FunctionAdapter)) {
                    return false;
                }
                return Intrinsics.areEqual((Object) getFunctionDelegate(), (Object) ((FunctionAdapter) obj).getFunctionDelegate());
            }

            public final Function<?> getFunctionDelegate() {
                return new FunctionReferenceImpl(1, ShadeTransitionController.this, ShadeTransitionController.class, "onPanelStateChanged", "onPanelStateChanged(I)V", 0);
            }

            public final int hashCode() {
                return getFunctionDelegate().hashCode();
            }

            public final void onPanelStateChanged(int i) {
                ShadeTransitionController.this.onPanelStateChanged(i);
            }
        });
    }

    public final NotificationPanelViewController getNotificationPanelViewController() {
        NotificationPanelViewController notificationPanelViewController2 = this.notificationPanelViewController;
        if (notificationPanelViewController2 != null) {
            return notificationPanelViewController2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("notificationPanelViewController");
        return null;
    }

    public final void setNotificationPanelViewController(NotificationPanelViewController notificationPanelViewController2) {
        Intrinsics.checkNotNullParameter(notificationPanelViewController2, "<set-?>");
        this.notificationPanelViewController = notificationPanelViewController2;
    }

    public final NotificationStackScrollLayoutController getNotificationStackScrollLayoutController() {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.notificationStackScrollLayoutController;
        if (notificationStackScrollLayoutController2 != null) {
            return notificationStackScrollLayoutController2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("notificationStackScrollLayoutController");
        return null;
    }

    public final void setNotificationStackScrollLayoutController(NotificationStackScrollLayoutController notificationStackScrollLayoutController2) {
        Intrinsics.checkNotNullParameter(notificationStackScrollLayoutController2, "<set-?>");
        this.notificationStackScrollLayoutController = notificationStackScrollLayoutController2;
    }

    public final C2304QS getQs() {
        C2304QS qs = this.f391qs;
        if (qs != null) {
            return qs;
        }
        Intrinsics.throwUninitializedPropertyAccessException("qs");
        return null;
    }

    public final void setQs(C2304QS qs) {
        Intrinsics.checkNotNullParameter(qs, "<set-?>");
        this.f391qs = qs;
    }

    private final SplitShadeOverScroller getSplitShadeOverScroller() {
        return (SplitShadeOverScroller) this.splitShadeOverScroller$delegate.getValue();
    }

    private final ShadeOverScroller getShadeOverScroller() {
        if (!this.inSplitShade || !propertiesInitialized()) {
            return this.noOpOverScroller;
        }
        return getSplitShadeOverScroller();
    }

    /* access modifiers changed from: private */
    public final void updateResources() {
        this.inSplitShade = this.context.getResources().getBoolean(C1894R.bool.config_use_split_notification_shade);
    }

    /* access modifiers changed from: private */
    public final void onPanelStateChanged(int i) {
        getShadeOverScroller().onPanelStateChanged(i);
    }

    /* access modifiers changed from: private */
    public final void onPanelExpansionChanged(PanelExpansionChangeEvent panelExpansionChangeEvent) {
        getShadeOverScroller().onDragDownAmountChanged(panelExpansionChangeEvent.getDragDownPxAmount());
    }

    private final boolean propertiesInitialized() {
        return (this.f391qs == null || this.notificationPanelViewController == null || this.notificationStackScrollLayoutController == null) ? false : true;
    }
}
