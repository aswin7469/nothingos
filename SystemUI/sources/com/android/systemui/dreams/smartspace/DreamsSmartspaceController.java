package com.android.systemui.dreams.smartspace;

import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.smartspace.SmartspacePrecondition;
import com.android.systemui.smartspace.SmartspaceTargetFilter;
import com.android.systemui.smartspace.dagger.SmartspaceViewComponent;
import com.android.systemui.util.concurrency.Execution;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import javax.inject.Named;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0007\u0018\u0000 ?2\u00020\u0001:\u0001?B[\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r\u0012\u000e\b\u0001\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f\u0012\u000e\b\u0001\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u000f¢\u0006\u0002\u0010\u0013J\u000e\u00101\u001a\u0002022\u0006\u00103\u001a\u00020\u001cJ\u0010\u00104\u001a\u0004\u0018\u0001052\u0006\u00106\u001a\u000207J\u0012\u00108\u001a\u0004\u0018\u0001052\u0006\u00106\u001a\u000207H\u0002J\b\u00109\u001a\u000202H\u0002J\b\u0010:\u001a\u000202H\u0002J\b\u0010;\u001a\u00020<H\u0002J\b\u0010=\u001a\u000202H\u0002J\u000e\u0010>\u001a\u0002022\u0006\u00103\u001a\u00020\u001cR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0014\u001a\u00020\u0015X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u001c0\u001bX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u0004\u0018\u00010\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u00020\u001fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010!\"\u0004\b\"\u0010#R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020'X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010(\u001a\b\u0012\u0004\u0012\u00020)0\u001bX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010*\u001a\u00020+X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b,\u0010-\"\u0004\b.\u0010/R\u0010\u00100\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006@"}, mo65043d2 = {"Lcom/android/systemui/dreams/smartspace/DreamsSmartspaceController;", "", "context", "Landroid/content/Context;", "smartspaceManager", "Landroid/app/smartspace/SmartspaceManager;", "execution", "Lcom/android/systemui/util/concurrency/Execution;", "uiExecutor", "Ljava/util/concurrent/Executor;", "smartspaceViewComponentFactory", "Lcom/android/systemui/smartspace/dagger/SmartspaceViewComponent$Factory;", "precondition", "Lcom/android/systemui/smartspace/SmartspacePrecondition;", "optionalTargetFilter", "Ljava/util/Optional;", "Lcom/android/systemui/smartspace/SmartspaceTargetFilter;", "optionalPlugin", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin;", "(Landroid/content/Context;Landroid/app/smartspace/SmartspaceManager;Lcom/android/systemui/util/concurrency/Execution;Ljava/util/concurrent/Executor;Lcom/android/systemui/smartspace/dagger/SmartspaceViewComponent$Factory;Lcom/android/systemui/smartspace/SmartspacePrecondition;Ljava/util/Optional;Ljava/util/Optional;)V", "filterListener", "Lcom/android/systemui/smartspace/SmartspaceTargetFilter$Listener;", "getFilterListener", "()Lcom/android/systemui/smartspace/SmartspaceTargetFilter$Listener;", "setFilterListener", "(Lcom/android/systemui/smartspace/SmartspaceTargetFilter$Listener;)V", "listeners", "", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$SmartspaceTargetListener;", "plugin", "preconditionListener", "Lcom/android/systemui/smartspace/SmartspacePrecondition$Listener;", "getPreconditionListener", "()Lcom/android/systemui/smartspace/SmartspacePrecondition$Listener;", "setPreconditionListener", "(Lcom/android/systemui/smartspace/SmartspacePrecondition$Listener;)V", "session", "Landroid/app/smartspace/SmartspaceSession;", "sessionListener", "Landroid/app/smartspace/SmartspaceSession$OnTargetsAvailableListener;", "smartspaceViews", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$SmartspaceView;", "stateChangeListener", "Landroid/view/View$OnAttachStateChangeListener;", "getStateChangeListener", "()Landroid/view/View$OnAttachStateChangeListener;", "setStateChangeListener", "(Landroid/view/View$OnAttachStateChangeListener;)V", "targetFilter", "addListener", "", "listener", "buildAndConnectView", "Landroid/view/View;", "parent", "Landroid/view/ViewGroup;", "buildView", "connectSession", "disconnect", "hasActiveSessionListeners", "", "reloadSmartspace", "removeListener", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DreamsSmartspaceController.kt */
public final class DreamsSmartspaceController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "DreamsSmartspaceCtrlr";
    private final Context context;
    private final Execution execution;
    private SmartspaceTargetFilter.Listener filterListener;
    private Set<BcSmartspaceDataPlugin.SmartspaceTargetListener> listeners = new LinkedHashSet();
    private final Optional<SmartspaceTargetFilter> optionalTargetFilter;
    private final BcSmartspaceDataPlugin plugin;
    private final SmartspacePrecondition precondition;
    private SmartspacePrecondition.Listener preconditionListener;
    private SmartspaceSession session;
    private final SmartspaceSession.OnTargetsAvailableListener sessionListener;
    private final SmartspaceManager smartspaceManager;
    private final SmartspaceViewComponent.Factory smartspaceViewComponentFactory;
    /* access modifiers changed from: private */
    public Set<BcSmartspaceDataPlugin.SmartspaceView> smartspaceViews = new LinkedHashSet();
    private View.OnAttachStateChangeListener stateChangeListener;
    private SmartspaceTargetFilter targetFilter;
    private final Executor uiExecutor;

    @Inject
    public DreamsSmartspaceController(Context context2, SmartspaceManager smartspaceManager2, Execution execution2, @Main Executor executor, SmartspaceViewComponent.Factory factory, @Named("dream_smartspace_precondition") SmartspacePrecondition smartspacePrecondition, @Named("dream_smartspace_target_filter") Optional<SmartspaceTargetFilter> optional, @Named("dreams_smartspace_data_plugin") Optional<BcSmartspaceDataPlugin> optional2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(smartspaceManager2, "smartspaceManager");
        Intrinsics.checkNotNullParameter(execution2, "execution");
        Intrinsics.checkNotNullParameter(executor, "uiExecutor");
        Intrinsics.checkNotNullParameter(factory, "smartspaceViewComponentFactory");
        Intrinsics.checkNotNullParameter(smartspacePrecondition, "precondition");
        Intrinsics.checkNotNullParameter(optional, "optionalTargetFilter");
        Intrinsics.checkNotNullParameter(optional2, "optionalPlugin");
        this.context = context2;
        this.smartspaceManager = smartspaceManager2;
        this.execution = execution2;
        this.uiExecutor = executor;
        this.smartspaceViewComponentFactory = factory;
        this.precondition = smartspacePrecondition;
        this.optionalTargetFilter = optional;
        this.plugin = optional2.orElse(null);
        this.targetFilter = optional.orElse(null);
        SmartspacePrecondition.Listener dreamsSmartspaceController$preconditionListener$1 = new DreamsSmartspaceController$preconditionListener$1(this);
        this.preconditionListener = dreamsSmartspaceController$preconditionListener$1;
        smartspacePrecondition.addListener(dreamsSmartspaceController$preconditionListener$1);
        SmartspaceTargetFilter.Listener dreamsSmartspaceController$filterListener$1 = new DreamsSmartspaceController$filterListener$1(this);
        this.filterListener = dreamsSmartspaceController$filterListener$1;
        SmartspaceTargetFilter smartspaceTargetFilter = this.targetFilter;
        if (smartspaceTargetFilter != null) {
            smartspaceTargetFilter.addListener(dreamsSmartspaceController$filterListener$1);
        }
        this.stateChangeListener = new DreamsSmartspaceController$stateChangeListener$1(this);
        this.sessionListener = new DreamsSmartspaceController$$ExternalSyntheticLambda1(this);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/dreams/smartspace/DreamsSmartspaceController$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: DreamsSmartspaceController.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final SmartspacePrecondition.Listener getPreconditionListener() {
        return this.preconditionListener;
    }

    public final void setPreconditionListener(SmartspacePrecondition.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "<set-?>");
        this.preconditionListener = listener;
    }

    public final SmartspaceTargetFilter.Listener getFilterListener() {
        return this.filterListener;
    }

    public final void setFilterListener(SmartspaceTargetFilter.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "<set-?>");
        this.filterListener = listener;
    }

    public final View.OnAttachStateChangeListener getStateChangeListener() {
        return this.stateChangeListener;
    }

    public final void setStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
        Intrinsics.checkNotNullParameter(onAttachStateChangeListener, "<set-?>");
        this.stateChangeListener = onAttachStateChangeListener;
    }

    /* access modifiers changed from: private */
    /* renamed from: sessionListener$lambda-1  reason: not valid java name */
    public static final void m2750sessionListener$lambda1(DreamsSmartspaceController dreamsSmartspaceController, List list) {
        boolean z;
        Intrinsics.checkNotNullParameter(dreamsSmartspaceController, "this$0");
        dreamsSmartspaceController.execution.assertIsMainThread();
        Intrinsics.checkNotNullExpressionValue(list, "targets");
        Collection arrayList = new ArrayList();
        for (Object next : list) {
            SmartspaceTarget smartspaceTarget = (SmartspaceTarget) next;
            SmartspaceTargetFilter smartspaceTargetFilter = dreamsSmartspaceController.targetFilter;
            if (smartspaceTargetFilter != null) {
                Intrinsics.checkNotNullExpressionValue(smartspaceTarget, "it");
                z = smartspaceTargetFilter.filterSmartspaceTarget(smartspaceTarget);
            } else {
                z = true;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        List list2 = (List) arrayList;
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = dreamsSmartspaceController.plugin;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.onTargetsAvailable(list2);
        }
    }

    public final View buildAndConnectView(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        this.execution.assertIsMainThread();
        if (this.precondition.conditionsMet()) {
            View buildView = buildView(viewGroup);
            connectSession();
            return buildView;
        }
        throw new RuntimeException("Cannot build view when not enabled");
    }

    private final View buildView(ViewGroup viewGroup) {
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin != null) {
            BcSmartspaceDataPlugin.SmartspaceView view = this.smartspaceViewComponentFactory.create(viewGroup, bcSmartspaceDataPlugin, this.stateChangeListener).getView();
            if (!(view instanceof View)) {
                return null;
            }
            view.setIsDreaming(true);
            return (View) view;
        }
        View view2 = null;
        return null;
    }

    private final boolean hasActiveSessionListeners() {
        return (this.smartspaceViews.isEmpty() ^ true) || (this.listeners.isEmpty() ^ true);
    }

    /* access modifiers changed from: private */
    public final void connectSession() {
        if (this.plugin != null && this.session == null && hasActiveSessionListeners() && this.precondition.conditionsMet()) {
            SmartspaceSession createSmartspaceSession = this.smartspaceManager.createSmartspaceSession(new SmartspaceConfig.Builder(this.context, "dream").build());
            Log.d(TAG, "Starting smartspace session for dream");
            createSmartspaceSession.addOnTargetsAvailableListener(this.uiExecutor, this.sessionListener);
            this.session = createSmartspaceSession;
            this.plugin.registerSmartspaceEventNotifier(new DreamsSmartspaceController$$ExternalSyntheticLambda0(this));
            reloadSmartspace();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: connectSession$lambda-2  reason: not valid java name */
    public static final void m2749connectSession$lambda2(DreamsSmartspaceController dreamsSmartspaceController, SmartspaceTargetEvent smartspaceTargetEvent) {
        Intrinsics.checkNotNullParameter(dreamsSmartspaceController, "this$0");
        SmartspaceSession smartspaceSession = dreamsSmartspaceController.session;
        if (smartspaceSession != null) {
            smartspaceSession.notifySmartspaceEvent(smartspaceTargetEvent);
        }
    }

    /* access modifiers changed from: private */
    public final void disconnect() {
        if (!hasActiveSessionListeners()) {
            this.execution.assertIsMainThread();
            SmartspaceSession smartspaceSession = this.session;
            if (smartspaceSession != null) {
                if (smartspaceSession != null) {
                    smartspaceSession.removeOnTargetsAvailableListener(this.sessionListener);
                    smartspaceSession.close();
                }
                this.session = null;
                BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
                if (bcSmartspaceDataPlugin != null) {
                    bcSmartspaceDataPlugin.registerSmartspaceEventNotifier((BcSmartspaceDataPlugin.SmartspaceEventNotifier) null);
                }
                BcSmartspaceDataPlugin bcSmartspaceDataPlugin2 = this.plugin;
                if (bcSmartspaceDataPlugin2 != null) {
                    bcSmartspaceDataPlugin2.onTargetsAvailable(CollectionsKt.emptyList());
                }
                Log.d(TAG, "Ending smartspace session for dream");
            }
        }
    }

    public final void addListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        Intrinsics.checkNotNullParameter(smartspaceTargetListener, "listener");
        this.execution.assertIsMainThread();
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.registerListener(smartspaceTargetListener);
        }
        this.listeners.add(smartspaceTargetListener);
        connectSession();
    }

    public final void removeListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        Intrinsics.checkNotNullParameter(smartspaceTargetListener, "listener");
        this.execution.assertIsMainThread();
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.unregisterListener(smartspaceTargetListener);
        }
        this.listeners.remove(smartspaceTargetListener);
        disconnect();
    }

    /* access modifiers changed from: private */
    public final void reloadSmartspace() {
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
    }
}
