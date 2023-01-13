package com.android.systemui.controls.p010ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.util.Log;
import android.util.Pair;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Space;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.Key;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.C1894R;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlAdapter;
import com.android.systemui.controls.management.ControlsEditingActivity;
import com.android.systemui.controls.management.ControlsFavoritingActivity;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.collections.MapsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000þ\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0007\u0018\u0000 y2\u00020\u0001:\u0001yBy\b\u0007\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\b\b\u0001\u0010\t\u001a\u00020\b\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003\u0012\b\b\u0001\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u0012\u0006\u0010\u0018\u001a\u00020\u0019¢\u0006\u0002\u0010\u001aJ\u0010\u0010I\u001a\u00020J2\u0006\u0010K\u001a\u000202H\u0016J\"\u0010L\u001a\u0002062\u0018\u0010M\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002040\u001d\u0012\u0004\u0012\u00020J0NH\u0002J\u001e\u0010O\u001a\u00020J2\f\u0010P\u001a\b\u0012\u0004\u0012\u0002040\u001d2\u0006\u0010Q\u001a\u000204H\u0002J\u0010\u0010R\u001a\u00020J2\u0006\u0010Q\u001a\u000204H\u0002J\b\u0010S\u001a\u00020JH\u0002J\u0018\u0010T\u001a\u00020?2\u0006\u0010U\u001a\u00020V2\u0006\u0010W\u001a\u00020?H\u0002J \u0010X\u001a\u0004\u0018\u0001042\u0006\u0010Y\u001a\u00020\u001e2\f\u0010P\u001a\b\u0012\u0004\u0012\u0002040\u001dH\u0002J\u0016\u0010Z\u001a\u00020\u001e2\f\u0010[\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dH\u0016J\b\u0010\\\u001a\u00020JH\u0016J \u0010]\u001a\u00020J2\u0006\u0010^\u001a\u00020_2\u0006\u0010`\u001a\u00020a2\u0006\u0010b\u001a\u00020cH\u0016J\u001e\u0010d\u001a\u00020J2\u0006\u0010^\u001a\u00020_2\f\u0010e\u001a\b\u0012\u0004\u0012\u00020f0\u001dH\u0016J\u0018\u0010g\u001a\u00020J2\u0006\u0010h\u001a\u00020i2\u0006\u0010Y\u001a\u00020\u001eH\u0002J\u0010\u0010j\u001a\u00020J2\u0006\u0010>\u001a\u00020?H\u0002J \u0010k\u001a\u00020J2\u0006\u0010>\u001a\u00020?2\u0006\u0010:\u001a\u00020;2\u0006\u0010\u001b\u001a\u00020\u0006H\u0016J\u0016\u0010l\u001a\u00020J2\f\u0010P\u001a\b\u0012\u0004\u0012\u0002040\u001dH\u0002J\u0016\u0010m\u001a\u00020J2\f\u0010P\u001a\b\u0012\u0004\u0012\u0002040\u001dH\u0002J\u0016\u0010n\u001a\u00020J2\f\u0010P\u001a\b\u0012\u0004\u0012\u0002040\u001dH\u0002J\u0010\u0010o\u001a\u00020J2\u0006\u0010h\u001a\u00020iH\u0002J\u0010\u0010p\u001a\u00020J2\u0006\u0010Y\u001a\u00020\u001eH\u0002J\u0010\u0010q\u001a\u00020J2\u0006\u0010Y\u001a\u00020\u001eH\u0002J\b\u0010r\u001a\u00020JH\u0002J\u001c\u0010s\u001a\u00020J2\u0006\u0010Y\u001a\u00020\u001e2\n\u0010t\u001a\u0006\u0012\u0002\b\u00030uH\u0002J\u0010\u0010v\u001a\u00020J2\u0006\u0010w\u001a\u000204H\u0002J\u0010\u0010x\u001a\u00020J2\u0006\u0010Y\u001a\u00020\u001eH\u0002R\u000e\u0010\u001b\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u001e0\u001dX.¢\u0006\u0002\n\u0000R\u0011\u0010\t\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0016\u0010!\u001a\n #*\u0004\u0018\u00010\"0\"X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010\u000e\u001a\u00020\u000f¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u001a\u0010(\u001a\u000e\u0012\u0004\u0012\u00020*\u0012\u0004\u0012\u00020+0)X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010,\u001a\u000e\u0012\u0004\u0012\u00020*\u0012\u0004\u0012\u00020-0)X\u0004¢\u0006\u0002\n\u0000R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0003¢\u0006\b\n\u0000\u001a\u0004\b0\u0010/R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u000202X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u0014\u00103\u001a\b\u0012\u0004\u0012\u0002040\u001dX.¢\u0006\u0002\n\u0000R\u000e\u00105\u001a\u000206X.¢\u0006\u0002\n\u0000R\u001e\u00107\u001a\u0012\u0012\u0004\u0012\u00020408j\b\u0012\u0004\u0012\u000204`9X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010:\u001a\u00020;X.¢\u0006\u0002\n\u0000R\u0014\u0010<\u001a\b\u0012\u0004\u0012\u0002020=X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010>\u001a\u00020?X.¢\u0006\u0002\n\u0000R\u0010\u0010@\u001a\u0004\u0018\u00010AX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010B\u001a\u00020CX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010D\u001a\u000202X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010E\u001a\u00020\u001eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\bF\u0010GR\u0011\u0010\u0007\u001a\u00020\b¢\u0006\b\n\u0000\u001a\u0004\bH\u0010 ¨\u0006z"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/ControlsUiControllerImpl;", "Lcom/android/systemui/controls/ui/ControlsUiController;", "controlsController", "Ldagger/Lazy;", "Lcom/android/systemui/controls/controller/ControlsController;", "context", "Landroid/content/Context;", "uiExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "bgExecutor", "controlsListingController", "Lcom/android/systemui/controls/management/ControlsListingController;", "sharedPreferences", "Landroid/content/SharedPreferences;", "controlActionCoordinator", "Lcom/android/systemui/controls/ui/ControlActionCoordinator;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "shadeController", "Lcom/android/systemui/statusbar/phone/ShadeController;", "iconCache", "Lcom/android/systemui/controls/CustomIconCache;", "controlsMetricsLogger", "Lcom/android/systemui/controls/ControlsMetricsLogger;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "(Ldagger/Lazy;Landroid/content/Context;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/util/concurrency/DelayableExecutor;Ldagger/Lazy;Landroid/content/SharedPreferences;Lcom/android/systemui/controls/ui/ControlActionCoordinator;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/statusbar/phone/ShadeController;Lcom/android/systemui/controls/CustomIconCache;Lcom/android/systemui/controls/ControlsMetricsLogger;Lcom/android/systemui/statusbar/policy/KeyguardStateController;)V", "activityContext", "allStructures", "", "Lcom/android/systemui/controls/controller/StructureInfo;", "getBgExecutor", "()Lcom/android/systemui/util/concurrency/DelayableExecutor;", "collator", "Ljava/text/Collator;", "kotlin.jvm.PlatformType", "getContext", "()Landroid/content/Context;", "getControlActionCoordinator", "()Lcom/android/systemui/controls/ui/ControlActionCoordinator;", "controlViewsById", "", "Lcom/android/systemui/controls/ui/ControlKey;", "Lcom/android/systemui/controls/ui/ControlViewHolder;", "controlsById", "Lcom/android/systemui/controls/ui/ControlWithState;", "getControlsController", "()Ldagger/Lazy;", "getControlsListingController", "hidden", "", "lastItems", "Lcom/android/systemui/controls/ui/SelectionItem;", "listingCallback", "Lcom/android/systemui/controls/management/ControlsListingController$ControlsListingCallback;", "localeComparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "onDismiss", "Ljava/lang/Runnable;", "onSeedingComplete", "Ljava/util/function/Consumer;", "parent", "Landroid/view/ViewGroup;", "popup", "Landroid/widget/ListPopupWindow;", "popupThemedContext", "Landroid/view/ContextThemeWrapper;", "retainCache", "selectedStructure", "getSharedPreferences", "()Landroid/content/SharedPreferences;", "getUiExecutor", "closeDialogs", "", "immediately", "createCallback", "onResult", "Lkotlin/Function1;", "createDropDown", "items", "selected", "createListView", "createMenu", "createRow", "inflater", "Landroid/view/LayoutInflater;", "listView", "findSelectionItem", "si", "getPreferredStructure", "structures", "hide", "onActionResponse", "componentName", "Landroid/content/ComponentName;", "controlId", "", "response", "", "onRefreshState", "controls", "Landroid/service/controls/Control;", "putIntentExtras", "intent", "Landroid/content/Intent;", "reload", "show", "showControlsView", "showInitialSetupView", "showSeedingView", "startActivity", "startEditingActivity", "startFavoritingActivity", "startProviderSelectorActivity", "startTargetedActivity", "klazz", "Ljava/lang/Class;", "switchAppOrStructure", "item", "updatePreferences", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlsUiControllerImpl */
/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl implements ControlsUiController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final ComponentName EMPTY_COMPONENT;
    private static final StructureInfo EMPTY_STRUCTURE;
    private static final long FADE_IN_MILLIS = 200;
    private static final String PREF_COMPONENT = "controls_component";
    private static final String PREF_STRUCTURE = "controls_structure";
    /* access modifiers changed from: private */
    public Context activityContext;
    private final ActivityStarter activityStarter;
    private List<StructureInfo> allStructures;
    private final DelayableExecutor bgExecutor;
    private final Collator collator;
    private final Context context;
    private final ControlActionCoordinator controlActionCoordinator;
    /* access modifiers changed from: private */
    public final Map<ControlKey, ControlViewHolder> controlViewsById = new LinkedHashMap();
    /* access modifiers changed from: private */
    public final Map<ControlKey, ControlWithState> controlsById = new LinkedHashMap();
    private final Lazy<ControlsController> controlsController;
    private final Lazy<ControlsListingController> controlsListingController;
    private final ControlsMetricsLogger controlsMetricsLogger;
    private boolean hidden = true;
    private final CustomIconCache iconCache;
    private final KeyguardStateController keyguardStateController;
    private List<SelectionItem> lastItems;
    private ControlsListingController.ControlsListingCallback listingCallback;
    private final Comparator<SelectionItem> localeComparator;
    /* access modifiers changed from: private */
    public Runnable onDismiss;
    private final Consumer<Boolean> onSeedingComplete;
    /* access modifiers changed from: private */
    public ViewGroup parent;
    /* access modifiers changed from: private */
    public ListPopupWindow popup;
    /* access modifiers changed from: private */
    public final ContextThemeWrapper popupThemedContext;
    private boolean retainCache;
    /* access modifiers changed from: private */
    public StructureInfo selectedStructure = EMPTY_STRUCTURE;
    private final ShadeController shadeController;
    private final SharedPreferences sharedPreferences;
    private final DelayableExecutor uiExecutor;

    @Inject
    public ControlsUiControllerImpl(Lazy<ControlsController> lazy, Context context2, @Main DelayableExecutor delayableExecutor, @Background DelayableExecutor delayableExecutor2, Lazy<ControlsListingController> lazy2, @Main SharedPreferences sharedPreferences2, ControlActionCoordinator controlActionCoordinator2, ActivityStarter activityStarter2, ShadeController shadeController2, CustomIconCache customIconCache, ControlsMetricsLogger controlsMetricsLogger2, KeyguardStateController keyguardStateController2) {
        Intrinsics.checkNotNullParameter(lazy, "controlsController");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(delayableExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "bgExecutor");
        Intrinsics.checkNotNullParameter(lazy2, "controlsListingController");
        Intrinsics.checkNotNullParameter(sharedPreferences2, "sharedPreferences");
        Intrinsics.checkNotNullParameter(controlActionCoordinator2, "controlActionCoordinator");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(shadeController2, "shadeController");
        Intrinsics.checkNotNullParameter(customIconCache, "iconCache");
        Intrinsics.checkNotNullParameter(controlsMetricsLogger2, "controlsMetricsLogger");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        this.controlsController = lazy;
        this.context = context2;
        this.uiExecutor = delayableExecutor;
        this.bgExecutor = delayableExecutor2;
        this.controlsListingController = lazy2;
        this.sharedPreferences = sharedPreferences2;
        this.controlActionCoordinator = controlActionCoordinator2;
        this.activityStarter = activityStarter2;
        this.shadeController = shadeController2;
        this.iconCache = customIconCache;
        this.controlsMetricsLogger = controlsMetricsLogger2;
        this.keyguardStateController = keyguardStateController2;
        this.popupThemedContext = new ContextThemeWrapper(context2, C1894R.style.Control_ListPopupWindow);
        Collator instance = Collator.getInstance(context2.getResources().getConfiguration().getLocales().get(0));
        this.collator = instance;
        Intrinsics.checkNotNullExpressionValue(instance, "collator");
        this.localeComparator = new ControlsUiControllerImpl$special$$inlined$compareBy$1(instance);
        this.onSeedingComplete = new ControlsUiControllerImpl$$ExternalSyntheticLambda1(this);
    }

    public final Lazy<ControlsController> getControlsController() {
        return this.controlsController;
    }

    public final Context getContext() {
        return this.context;
    }

    public final DelayableExecutor getUiExecutor() {
        return this.uiExecutor;
    }

    public final DelayableExecutor getBgExecutor() {
        return this.bgExecutor;
    }

    public final Lazy<ControlsListingController> getControlsListingController() {
        return this.controlsListingController;
    }

    public final SharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    public final ControlActionCoordinator getControlActionCoordinator() {
        return this.controlActionCoordinator;
    }

    @Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nXT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/controls/ui/ControlsUiControllerImpl$Companion;", "", "()V", "EMPTY_COMPONENT", "Landroid/content/ComponentName;", "EMPTY_STRUCTURE", "Lcom/android/systemui/controls/controller/StructureInfo;", "FADE_IN_MILLIS", "", "PREF_COMPONENT", "", "PREF_STRUCTURE", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.controls.ui.ControlsUiControllerImpl$Companion */
    /* compiled from: ControlsUiControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        ComponentName componentName = new ComponentName("", "");
        EMPTY_COMPONENT = componentName;
        EMPTY_STRUCTURE = new StructureInfo(componentName, "", new ArrayList());
    }

    /* access modifiers changed from: private */
    /* renamed from: onSeedingComplete$lambda-2  reason: not valid java name */
    public static final void m2713onSeedingComplete$lambda2(ControlsUiControllerImpl controlsUiControllerImpl, Boolean bool) {
        Object obj;
        Intrinsics.checkNotNullParameter(controlsUiControllerImpl, "this$0");
        Intrinsics.checkNotNullExpressionValue(bool, "accepted");
        ViewGroup viewGroup = null;
        if (bool.booleanValue()) {
            Iterator it = controlsUiControllerImpl.controlsController.get().getFavorites().iterator();
            if (!it.hasNext()) {
                obj = null;
            } else {
                obj = it.next();
                if (it.hasNext()) {
                    int size = ((StructureInfo) obj).getControls().size();
                    do {
                        Object next = it.next();
                        int size2 = ((StructureInfo) next).getControls().size();
                        if (size < size2) {
                            obj = next;
                            size = size2;
                        }
                    } while (it.hasNext());
                }
            }
            StructureInfo structureInfo = (StructureInfo) obj;
            if (structureInfo == null) {
                structureInfo = EMPTY_STRUCTURE;
            }
            controlsUiControllerImpl.selectedStructure = structureInfo;
            controlsUiControllerImpl.updatePreferences(structureInfo);
        }
        ViewGroup viewGroup2 = controlsUiControllerImpl.parent;
        if (viewGroup2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
        } else {
            viewGroup = viewGroup2;
        }
        controlsUiControllerImpl.reload(viewGroup);
    }

    private final ControlsListingController.ControlsListingCallback createCallback(Function1<? super List<SelectionItem>, Unit> function1) {
        return new ControlsUiControllerImpl$createCallback$1(this, function1);
    }

    public void show(ViewGroup viewGroup, Runnable runnable, Context context2) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        Intrinsics.checkNotNullParameter(runnable, "onDismiss");
        Intrinsics.checkNotNullParameter(context2, "activityContext");
        Log.d("ControlsUiController", "show()");
        this.parent = viewGroup;
        this.onDismiss = runnable;
        this.activityContext = context2;
        this.hidden = false;
        this.retainCache = false;
        this.controlActionCoordinator.setActivityContext(context2);
        List<StructureInfo> favorites = this.controlsController.get().getFavorites();
        this.allStructures = favorites;
        ControlsListingController.ControlsListingCallback controlsListingCallback = null;
        if (favorites == null) {
            Intrinsics.throwUninitializedPropertyAccessException("allStructures");
            favorites = null;
        }
        this.selectedStructure = getPreferredStructure(favorites);
        if (this.controlsController.get().addSeedingFavoritesCallback(this.onSeedingComplete)) {
            this.listingCallback = createCallback(new ControlsUiControllerImpl$show$1(this));
        } else {
            if (this.selectedStructure.getControls().isEmpty()) {
                List<StructureInfo> list = this.allStructures;
                if (list == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("allStructures");
                    list = null;
                }
                if (list.size() <= 1) {
                    this.listingCallback = createCallback(new ControlsUiControllerImpl$show$2(this));
                }
            }
            Iterable<ControlInfo> controls = this.selectedStructure.getControls();
            Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(controls, 10));
            for (ControlInfo controlWithState : controls) {
                arrayList.add(new ControlWithState(this.selectedStructure.getComponentName(), controlWithState, (Control) null));
            }
            Map<ControlKey, ControlWithState> map = this.controlsById;
            for (Object next : (List) arrayList) {
                map.put(new ControlKey(this.selectedStructure.getComponentName(), ((ControlWithState) next).getCi().getControlId()), next);
            }
            this.listingCallback = createCallback(new ControlsUiControllerImpl$show$5(this));
            this.controlsController.get().subscribeToFavorites(this.selectedStructure);
        }
        ControlsListingController controlsListingController2 = this.controlsListingController.get();
        ControlsListingController.ControlsListingCallback controlsListingCallback2 = this.listingCallback;
        if (controlsListingCallback2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("listingCallback");
        } else {
            controlsListingCallback = controlsListingCallback2;
        }
        controlsListingController2.addCallback(controlsListingCallback);
    }

    private final void reload(ViewGroup viewGroup) {
        if (!this.hidden) {
            ControlsListingController controlsListingController2 = this.controlsListingController.get();
            ControlsListingController.ControlsListingCallback controlsListingCallback = this.listingCallback;
            if (controlsListingCallback == null) {
                Intrinsics.throwUninitializedPropertyAccessException("listingCallback");
                controlsListingCallback = null;
            }
            controlsListingController2.removeCallback(controlsListingCallback);
            this.controlsController.get().unsubscribe();
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(viewGroup, Key.ALPHA, new float[]{1.0f, 0.0f});
            ofFloat.setInterpolator(new AccelerateInterpolator(1.0f));
            ofFloat.setDuration(200);
            ofFloat.addListener(new ControlsUiControllerImpl$reload$1(this, viewGroup));
            ofFloat.start();
        }
    }

    /* access modifiers changed from: private */
    public final void showSeedingView(List<SelectionItem> list) {
        LayoutInflater from = LayoutInflater.from(this.context);
        ViewGroup viewGroup = this.parent;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup = null;
        }
        from.inflate(C1894R.layout.controls_no_favorites, viewGroup, true);
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
        } else {
            viewGroup2 = viewGroup3;
        }
        ((TextView) viewGroup2.requireViewById(C1894R.C1898id.controls_subtitle)).setText(this.context.getResources().getString(C1894R.string.controls_seeding_in_progress));
    }

    /* access modifiers changed from: private */
    public final void showInitialSetupView(List<SelectionItem> list) {
        startProviderSelectorActivity();
        Runnable runnable = this.onDismiss;
        if (runnable == null) {
            Intrinsics.throwUninitializedPropertyAccessException("onDismiss");
            runnable = null;
        }
        runnable.run();
    }

    /* access modifiers changed from: private */
    public final void startFavoritingActivity(StructureInfo structureInfo) {
        startTargetedActivity(structureInfo, ControlsFavoritingActivity.class);
    }

    /* access modifiers changed from: private */
    public final void startEditingActivity(StructureInfo structureInfo) {
        startTargetedActivity(structureInfo, ControlsEditingActivity.class);
    }

    private final void startTargetedActivity(StructureInfo structureInfo, Class<?> cls) {
        Context context2 = this.activityContext;
        if (context2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("activityContext");
            context2 = null;
        }
        Intent intent = new Intent(context2, cls);
        putIntentExtras(intent, structureInfo);
        startActivity(intent);
        this.retainCache = true;
    }

    private final void putIntentExtras(Intent intent, StructureInfo structureInfo) {
        intent.putExtra(ControlsFavoritingActivity.EXTRA_APP, this.controlsListingController.get().getAppLabel(structureInfo.getComponentName()));
        intent.putExtra(ControlsFavoritingActivity.EXTRA_STRUCTURE, structureInfo.getStructure());
        intent.putExtra("android.intent.extra.COMPONENT_NAME", structureInfo.getComponentName());
    }

    private final void startProviderSelectorActivity() {
        Context context2 = this.activityContext;
        if (context2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("activityContext");
            context2 = null;
        }
        Intent intent = new Intent(context2, ControlsProviderSelectorActivity.class);
        intent.putExtra(ControlsProviderSelectorActivity.BACK_SHOULD_EXIT, true);
        startActivity(intent);
    }

    private final void startActivity(Intent intent) {
        intent.putExtra("extra_animate", true);
        if (this.keyguardStateController.isShowing()) {
            this.activityStarter.postStartActivityDismissingKeyguard(intent, 0);
            return;
        }
        Context context2 = this.activityContext;
        Context context3 = null;
        if (context2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("activityContext");
            context2 = null;
        }
        Context context4 = this.activityContext;
        if (context4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("activityContext");
        } else {
            context3 = context4;
        }
        context2.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context3, new Pair[0]).toBundle());
    }

    /* access modifiers changed from: private */
    public final void showControlsView(List<SelectionItem> list) {
        this.controlViewsById.clear();
        Iterable iterable = list;
        Map linkedHashMap = new LinkedHashMap(RangesKt.coerceAtLeast(MapsKt.mapCapacity(CollectionsKt.collectionSizeOrDefault(iterable, 10)), 16));
        for (Object next : iterable) {
            linkedHashMap.put(((SelectionItem) next).getComponentName(), next);
        }
        List arrayList = new ArrayList();
        List<StructureInfo> list2 = this.allStructures;
        if (list2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("allStructures");
            list2 = null;
        }
        for (StructureInfo structureInfo : list2) {
            SelectionItem selectionItem = (SelectionItem) linkedHashMap.get(structureInfo.getComponentName());
            SelectionItem copy$default = selectionItem != null ? SelectionItem.copy$default(selectionItem, (CharSequence) null, structureInfo.getStructure(), (Drawable) null, (ComponentName) null, 0, 29, (Object) null) : null;
            if (copy$default != null) {
                arrayList.add(copy$default);
            }
        }
        Collection collection = arrayList;
        CollectionsKt.sortWith(arrayList, this.localeComparator);
        SelectionItem findSelectionItem = findSelectionItem(this.selectedStructure, arrayList);
        if (findSelectionItem == null) {
            findSelectionItem = list.get(0);
        }
        this.controlsMetricsLogger.refreshBegin(findSelectionItem.getUid(), !this.keyguardStateController.isUnlocked());
        createListView(findSelectionItem);
        createDropDown(arrayList, findSelectionItem);
        createMenu();
    }

    private final void createMenu() {
        String[] strArr = {this.context.getResources().getString(C1894R.string.controls_menu_add), this.context.getResources().getString(C1894R.string.controls_menu_edit)};
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        objectRef.element = new ArrayAdapter(this.context, C1894R.layout.controls_more_item, strArr);
        ViewGroup viewGroup = this.parent;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup = null;
        }
        ImageView imageView = (ImageView) viewGroup.requireViewById(C1894R.C1898id.controls_more);
        imageView.setOnClickListener(new ControlsUiControllerImpl$createMenu$1(this, imageView, objectRef));
    }

    private final void createDropDown(List<SelectionItem> list, SelectionItem selectionItem) {
        for (SelectionItem selectionItem2 : list) {
            RenderInfo.Companion.registerComponentIcon(selectionItem2.getComponentName(), selectionItem2.getIcon());
        }
        Ref.ObjectRef objectRef = new Ref.ObjectRef();
        T itemAdapter = new ItemAdapter(this.context, C1894R.layout.controls_spinner_item);
        itemAdapter.addAll(list);
        objectRef.element = itemAdapter;
        ViewGroup viewGroup = this.parent;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup = null;
        }
        TextView textView = (TextView) viewGroup.requireViewById(C1894R.C1898id.app_or_structure_spinner);
        textView.setText(selectionItem.getTitle());
        Drawable background = textView.getBackground();
        if (background != null) {
            ((LayerDrawable) background).getDrawable(0).setTint(textView.getContext().getResources().getColor(C1894R.C1895color.control_spinner_dropdown, (Resources.Theme) null));
            if (list.size() == 1) {
                textView.setBackground((Drawable) null);
                return;
            }
            ViewGroup viewGroup3 = this.parent;
            if (viewGroup3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
            } else {
                viewGroup2 = viewGroup3;
            }
            ViewGroup viewGroup4 = (ViewGroup) viewGroup2.requireViewById(C1894R.C1898id.controls_header);
            viewGroup4.setOnClickListener(new ControlsUiControllerImpl$createDropDown$2(this, viewGroup4, objectRef));
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
    }

    private final void createListView(SelectionItem selectionItem) {
        LayoutInflater from = LayoutInflater.from(this.context);
        ViewGroup viewGroup = this.parent;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup = null;
        }
        int i = 1;
        from.inflate(C1894R.layout.controls_with_favorites, viewGroup, true);
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup3 = null;
        }
        ImageView imageView = (ImageView) viewGroup3.requireViewById(C1894R.C1898id.controls_close);
        imageView.setOnClickListener(new ControlsUiControllerImpl$$ExternalSyntheticLambda0(this));
        boolean z = false;
        imageView.setVisibility(0);
        ControlAdapter.Companion companion = ControlAdapter.Companion;
        Context context2 = this.activityContext;
        if (context2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("activityContext");
            context2 = null;
        }
        Resources resources = context2.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "activityContext.resources");
        int findMaxColumns = companion.findMaxColumns(resources);
        ViewGroup viewGroup4 = this.parent;
        if (viewGroup4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
        } else {
            viewGroup2 = viewGroup4;
        }
        View requireViewById = viewGroup2.requireViewById(C1894R.C1898id.global_actions_controls_list);
        if (requireViewById != null) {
            ViewGroup viewGroup5 = (ViewGroup) requireViewById;
            Intrinsics.checkNotNullExpressionValue(from, "inflater");
            ViewGroup createRow = createRow(from, viewGroup5);
            for (ControlInfo controlId : this.selectedStructure.getControls()) {
                ControlKey controlKey = new ControlKey(this.selectedStructure.getComponentName(), controlId.getControlId());
                ControlWithState controlWithState = this.controlsById.get(controlKey);
                if (controlWithState != null) {
                    if (createRow.getChildCount() == findMaxColumns) {
                        createRow = createRow(from, viewGroup5);
                    }
                    View inflate = from.inflate(C1894R.layout.controls_base_item, createRow, z);
                    if (inflate != null) {
                        ViewGroup viewGroup6 = (ViewGroup) inflate;
                        createRow.addView(viewGroup6);
                        if (createRow.getChildCount() == i) {
                            ViewGroup.LayoutParams layoutParams = viewGroup6.getLayoutParams();
                            if (layoutParams != null) {
                                ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(z ? 1 : 0);
                            } else {
                                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                            }
                        }
                        ControlsController controlsController2 = this.controlsController.get();
                        Intrinsics.checkNotNullExpressionValue(controlsController2, "controlsController.get()");
                        ControlViewHolder controlViewHolder = new ControlViewHolder(viewGroup6, controlsController2, this.uiExecutor, this.bgExecutor, this.controlActionCoordinator, this.controlsMetricsLogger, selectionItem.getUid());
                        controlViewHolder.bindData(controlWithState, false);
                        this.controlViewsById.put(controlKey, controlViewHolder);
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
                    }
                }
                z = false;
                i = 1;
            }
            int size = this.selectedStructure.getControls().size() % findMaxColumns;
            int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(C1894R.dimen.control_spacing);
            for (int i2 = size == 0 ? 0 : findMaxColumns - size; i2 > 0; i2--) {
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, 0, 1.0f);
                layoutParams2.setMarginStart(dimensionPixelSize);
                createRow.addView(new Space(this.context), layoutParams2);
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    /* access modifiers changed from: private */
    /* renamed from: createListView$lambda-12$lambda-11  reason: not valid java name */
    public static final void m2710createListView$lambda12$lambda11(ControlsUiControllerImpl controlsUiControllerImpl, View view) {
        Intrinsics.checkNotNullParameter(controlsUiControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(view, "<anonymous parameter 0>");
        Runnable runnable = controlsUiControllerImpl.onDismiss;
        if (runnable == null) {
            Intrinsics.throwUninitializedPropertyAccessException("onDismiss");
            runnable = null;
        }
        runnable.run();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v0, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: com.android.systemui.controls.controller.StructureInfo} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.android.systemui.controls.controller.StructureInfo getPreferredStructure(java.util.List<com.android.systemui.controls.controller.StructureInfo> r8) {
        /*
            r7 = this;
            java.lang.String r0 = "structures"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)
            boolean r0 = r8.isEmpty()
            if (r0 == 0) goto L_0x000f
            com.android.systemui.controls.controller.StructureInfo r7 = EMPTY_STRUCTURE
            return r7
        L_0x000f:
            android.content.SharedPreferences r0 = r7.sharedPreferences
            java.lang.String r1 = "controls_component"
            r2 = 0
            java.lang.String r0 = r0.getString(r1, r2)
            if (r0 == 0) goto L_0x001f
            android.content.ComponentName r0 = android.content.ComponentName.unflattenFromString(r0)
            goto L_0x0020
        L_0x001f:
            r0 = r2
        L_0x0020:
            if (r0 != 0) goto L_0x0024
            android.content.ComponentName r0 = EMPTY_COMPONENT
        L_0x0024:
            android.content.SharedPreferences r7 = r7.sharedPreferences
            java.lang.String r1 = "controls_structure"
            java.lang.String r3 = ""
            java.lang.String r7 = r7.getString(r1, r3)
            r1 = r8
            java.lang.Iterable r1 = (java.lang.Iterable) r1
            java.util.Iterator r1 = r1.iterator()
        L_0x0035:
            boolean r3 = r1.hasNext()
            r4 = 0
            if (r3 == 0) goto L_0x005d
            java.lang.Object r3 = r1.next()
            r5 = r3
            com.android.systemui.controls.controller.StructureInfo r5 = (com.android.systemui.controls.controller.StructureInfo) r5
            android.content.ComponentName r6 = r5.getComponentName()
            boolean r6 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r0, (java.lang.Object) r6)
            if (r6 == 0) goto L_0x0059
            java.lang.CharSequence r5 = r5.getStructure()
            boolean r5 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r7, (java.lang.Object) r5)
            if (r5 == 0) goto L_0x0059
            r5 = 1
            goto L_0x005a
        L_0x0059:
            r5 = r4
        L_0x005a:
            if (r5 == 0) goto L_0x0035
            r2 = r3
        L_0x005d:
            com.android.systemui.controls.controller.StructureInfo r2 = (com.android.systemui.controls.controller.StructureInfo) r2
            if (r2 != 0) goto L_0x0068
            java.lang.Object r7 = r8.get(r4)
            r2 = r7
            com.android.systemui.controls.controller.StructureInfo r2 = (com.android.systemui.controls.controller.StructureInfo) r2
        L_0x0068:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.p010ui.ControlsUiControllerImpl.getPreferredStructure(java.util.List):com.android.systemui.controls.controller.StructureInfo");
    }

    private final void updatePreferences(StructureInfo structureInfo) {
        if (!Intrinsics.areEqual((Object) structureInfo, (Object) EMPTY_STRUCTURE)) {
            this.sharedPreferences.edit().putString(PREF_COMPONENT, structureInfo.getComponentName().flattenToString()).putString(PREF_STRUCTURE, structureInfo.getStructure().toString()).commit();
        }
    }

    /* access modifiers changed from: private */
    public final void switchAppOrStructure(SelectionItem selectionItem) {
        boolean z;
        List<StructureInfo> list = this.allStructures;
        ViewGroup viewGroup = null;
        if (list == null) {
            Intrinsics.throwUninitializedPropertyAccessException("allStructures");
            list = null;
        }
        for (StructureInfo structureInfo : list) {
            if (!Intrinsics.areEqual((Object) structureInfo.getStructure(), (Object) selectionItem.getStructure()) || !Intrinsics.areEqual((Object) structureInfo.getComponentName(), (Object) selectionItem.getComponentName())) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                if (!Intrinsics.areEqual((Object) structureInfo, (Object) this.selectedStructure)) {
                    this.selectedStructure = structureInfo;
                    updatePreferences(structureInfo);
                    ViewGroup viewGroup2 = this.parent;
                    if (viewGroup2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("parent");
                    } else {
                        viewGroup = viewGroup2;
                    }
                    reload(viewGroup);
                    return;
                }
                return;
            }
        }
        throw new NoSuchElementException("Collection contains no element matching the predicate.");
    }

    public void closeDialogs(boolean z) {
        if (z) {
            ListPopupWindow listPopupWindow = this.popup;
            if (listPopupWindow != null) {
                listPopupWindow.dismissImmediate();
            }
        } else {
            ListPopupWindow listPopupWindow2 = this.popup;
            if (listPopupWindow2 != null) {
                listPopupWindow2.dismiss();
            }
        }
        this.popup = null;
        for (Map.Entry<ControlKey, ControlViewHolder> value : this.controlViewsById.entrySet()) {
            ((ControlViewHolder) value.getValue()).dismiss();
        }
        this.controlActionCoordinator.closeDialogs();
    }

    public void hide() {
        this.hidden = true;
        closeDialogs(true);
        this.controlsController.get().unsubscribe();
        ViewGroup viewGroup = this.parent;
        ControlsListingController.ControlsListingCallback controlsListingCallback = null;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup = null;
        }
        viewGroup.removeAllViews();
        this.controlsById.clear();
        this.controlViewsById.clear();
        ControlsListingController controlsListingController2 = this.controlsListingController.get();
        ControlsListingController.ControlsListingCallback controlsListingCallback2 = this.listingCallback;
        if (controlsListingCallback2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("listingCallback");
        } else {
            controlsListingCallback = controlsListingCallback2;
        }
        controlsListingController2.removeCallback(controlsListingCallback);
        if (!this.retainCache) {
            RenderInfo.Companion.clearCache();
        }
    }

    public void onRefreshState(ComponentName componentName, List<Control> list) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(list, AutoTileManager.DEVICE_CONTROLS);
        boolean z = !this.keyguardStateController.isUnlocked();
        for (Control control : list) {
            Map<ControlKey, ControlWithState> map = this.controlsById;
            String controlId = control.getControlId();
            Intrinsics.checkNotNullExpressionValue(controlId, "c.getControlId()");
            ControlWithState controlWithState = map.get(new ControlKey(componentName, controlId));
            if (controlWithState != null) {
                Log.d("ControlsUiController", "onRefreshState() for id: " + control.getControlId());
                CustomIconCache customIconCache = this.iconCache;
                String controlId2 = control.getControlId();
                Intrinsics.checkNotNullExpressionValue(controlId2, "c.controlId");
                customIconCache.store(componentName, controlId2, control.getCustomIcon());
                ControlWithState controlWithState2 = new ControlWithState(componentName, controlWithState.getCi(), control);
                String controlId3 = control.getControlId();
                Intrinsics.checkNotNullExpressionValue(controlId3, "c.getControlId()");
                ControlKey controlKey = new ControlKey(componentName, controlId3);
                this.controlsById.put(controlKey, controlWithState2);
                ControlViewHolder controlViewHolder = this.controlViewsById.get(controlKey);
                if (controlViewHolder != null) {
                    this.uiExecutor.execute(new ControlsUiControllerImpl$$ExternalSyntheticLambda2(controlViewHolder, controlWithState2, z));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: onRefreshState$lambda-23$lambda-22$lambda-21$lambda-20  reason: not valid java name */
    public static final void m2712onRefreshState$lambda23$lambda22$lambda21$lambda20(ControlViewHolder controlViewHolder, ControlWithState controlWithState, boolean z) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "$it");
        Intrinsics.checkNotNullParameter(controlWithState, "$cws");
        controlViewHolder.bindData(controlWithState, z);
    }

    public void onActionResponse(ComponentName componentName, String str, int i) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(str, "controlId");
        this.uiExecutor.execute(new ControlsUiControllerImpl$$ExternalSyntheticLambda3(this, new ControlKey(componentName, str), i));
    }

    /* access modifiers changed from: private */
    /* renamed from: onActionResponse$lambda-24  reason: not valid java name */
    public static final void m2711onActionResponse$lambda24(ControlsUiControllerImpl controlsUiControllerImpl, ControlKey controlKey, int i) {
        Intrinsics.checkNotNullParameter(controlsUiControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(controlKey, "$key");
        ControlViewHolder controlViewHolder = controlsUiControllerImpl.controlViewsById.get(controlKey);
        if (controlViewHolder != null) {
            controlViewHolder.actionResponse(i);
        }
    }

    private final ViewGroup createRow(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(C1894R.layout.controls_row, viewGroup, false);
        if (inflate != null) {
            ViewGroup viewGroup2 = (ViewGroup) inflate;
            viewGroup.addView(viewGroup2);
            return viewGroup2;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    private final SelectionItem findSelectionItem(StructureInfo structureInfo, List<SelectionItem> list) {
        Object obj;
        boolean z;
        Iterator it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            SelectionItem selectionItem = (SelectionItem) obj;
            if (!Intrinsics.areEqual((Object) selectionItem.getComponentName(), (Object) structureInfo.getComponentName()) || !Intrinsics.areEqual((Object) selectionItem.getStructure(), (Object) structureInfo.getStructure())) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                break;
            }
        }
        return (SelectionItem) obj;
    }
}
