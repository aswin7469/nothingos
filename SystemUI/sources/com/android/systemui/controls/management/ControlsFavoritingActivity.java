package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.viewpager2.widget.ViewPager2;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.C1894R;
import com.android.systemui.Prefs;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.TooltipManager;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.p010ui.ControlsActivity;
import com.android.systemui.controls.p010ui.ControlsUiController;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.LifecycleActivity;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000¥\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n*\u0003\u0018\u001b%\u0018\u0000 G2\u00020\u0001:\u0001GB1\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\b\u00103\u001a\u000204H\u0002J\b\u00105\u001a\u000204H\u0002J\b\u00106\u001a\u000204H\u0002J\b\u00107\u001a\u000204H\u0002J\b\u00108\u001a\u000204H\u0016J\u0010\u00109\u001a\u0002042\u0006\u0010:\u001a\u00020;H\u0016J\u0012\u0010<\u001a\u0002042\b\u0010=\u001a\u0004\u0018\u00010>H\u0014J\b\u0010?\u001a\u000204H\u0014J\b\u0010@\u001a\u000204H\u0014J\b\u0010A\u001a\u000204H\u0014J\b\u0010B\u001a\u000204H\u0014J\b\u0010C\u001a\u000204H\u0014J\b\u0010D\u001a\u000204H\u0002J\b\u0010E\u001a\u000204H\u0002J\b\u0010F\u001a\u00020 H\u0002R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000R\u001e\u0010\u0011\u001a\u0012\u0012\u0004\u0012\u00020\u00130\u0012j\b\u0012\u0004\u0012\u00020\u0013`\u0014X.¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0004\n\u0002\u0010\u0019R\u0010\u0010\u001a\u001a\u00020\u001bX\u0004¢\u0006\u0004\n\u0002\u0010\u001cR\u000e\u0010\u001d\u001a\u00020\u001eX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020 X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u00130#X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u00020%X\u0004¢\u0006\u0004\n\u0002\u0010&R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010'\u001a\u0004\u0018\u00010(X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010)\u001a\u00020\u001eX.¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020+X.¢\u0006\u0002\n\u0000R\u000e\u0010,\u001a\u00020-X.¢\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010/\u001a\u000200X.¢\u0006\u0002\n\u0000R\u000e\u00101\u001a\u00020-X.¢\u0006\u0002\n\u0000R\u000e\u00102\u001a\u00020-X.¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000¨\u0006H"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsFavoritingActivity;", "Lcom/android/systemui/util/LifecycleActivity;", "executor", "Ljava/util/concurrent/Executor;", "controller", "Lcom/android/systemui/controls/controller/ControlsControllerImpl;", "listingController", "Lcom/android/systemui/controls/management/ControlsListingController;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "uiController", "Lcom/android/systemui/controls/ui/ControlsUiController;", "(Ljava/util/concurrent/Executor;Lcom/android/systemui/controls/controller/ControlsControllerImpl;Lcom/android/systemui/controls/management/ControlsListingController;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/controls/ui/ControlsUiController;)V", "appName", "", "cancelLoadRunnable", "Ljava/lang/Runnable;", "comparator", "Ljava/util/Comparator;", "Lcom/android/systemui/controls/management/StructureContainer;", "Lkotlin/Comparator;", "component", "Landroid/content/ComponentName;", "controlsModelCallback", "com/android/systemui/controls/management/ControlsFavoritingActivity$controlsModelCallback$1", "Lcom/android/systemui/controls/management/ControlsFavoritingActivity$controlsModelCallback$1;", "currentUserTracker", "com/android/systemui/controls/management/ControlsFavoritingActivity$currentUserTracker$1", "Lcom/android/systemui/controls/management/ControlsFavoritingActivity$currentUserTracker$1;", "doneButton", "Landroid/view/View;", "fromProviderSelector", "", "isPagerLoaded", "listOfStructures", "", "listingCallback", "com/android/systemui/controls/management/ControlsFavoritingActivity$listingCallback$1", "Lcom/android/systemui/controls/management/ControlsFavoritingActivity$listingCallback$1;", "mTooltipManager", "Lcom/android/systemui/controls/TooltipManager;", "otherAppsButton", "pageIndicator", "Lcom/android/systemui/controls/management/ManagementPageIndicator;", "statusText", "Landroid/widget/TextView;", "structureExtra", "structurePager", "Landroidx/viewpager2/widget/ViewPager2;", "subtitleView", "titleView", "animateExitAndFinish", "", "bindButtons", "bindViews", "loadControls", "onBackPressed", "onConfigurationChanged", "newConfig", "Landroid/content/res/Configuration;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onPause", "onResume", "onStart", "onStop", "openControlsOrigin", "setUpPager", "shouldShowTooltip", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity extends LifecycleActivity {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String EXTRA_APP = "extra_app_label";
    public static final String EXTRA_FROM_PROVIDER_SELECTOR = "extra_from_provider_selector";
    public static final String EXTRA_SINGLE_STRUCTURE = "extra_single_structure";
    public static final String EXTRA_STRUCTURE = "extra_structure";
    private static final String TAG = "ControlsFavoritingActivity";
    private static final int TOOLTIP_MAX_SHOWN = 2;
    private static final String TOOLTIP_PREFS_KEY = "ControlsStructureSwipeTooltipCount";
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    /* access modifiers changed from: private */
    public CharSequence appName;
    private final BroadcastDispatcher broadcastDispatcher;
    private Runnable cancelLoadRunnable;
    private Comparator<StructureContainer> comparator;
    private ComponentName component;
    /* access modifiers changed from: private */
    public final ControlsControllerImpl controller;
    private final ControlsFavoritingActivity$controlsModelCallback$1 controlsModelCallback;
    private final ControlsFavoritingActivity$currentUserTracker$1 currentUserTracker;
    /* access modifiers changed from: private */
    public View doneButton;
    private final Executor executor;
    private boolean fromProviderSelector;
    private boolean isPagerLoaded;
    /* access modifiers changed from: private */
    public List<StructureContainer> listOfStructures;
    private final ControlsFavoritingActivity$listingCallback$1 listingCallback;
    private final ControlsListingController listingController;
    /* access modifiers changed from: private */
    public TooltipManager mTooltipManager;
    /* access modifiers changed from: private */
    public View otherAppsButton;
    /* access modifiers changed from: private */
    public ManagementPageIndicator pageIndicator;
    private TextView statusText;
    private CharSequence structureExtra;
    private ViewPager2 structurePager;
    private TextView subtitleView;
    /* access modifiers changed from: private */
    public TextView titleView;
    private final ControlsUiController uiController;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    @Inject
    public ControlsFavoritingActivity(@Main Executor executor2, ControlsControllerImpl controlsControllerImpl, ControlsListingController controlsListingController, BroadcastDispatcher broadcastDispatcher2, ControlsUiController controlsUiController) {
        Intrinsics.checkNotNullParameter(executor2, "executor");
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "controller");
        Intrinsics.checkNotNullParameter(controlsListingController, "listingController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(controlsUiController, "uiController");
        this.executor = executor2;
        this.controller = controlsControllerImpl;
        this.listingController = controlsListingController;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.uiController = controlsUiController;
        this.listOfStructures = CollectionsKt.emptyList();
        this.currentUserTracker = new ControlsFavoritingActivity$currentUserTracker$1(this, broadcastDispatcher2);
        this.listingCallback = new ControlsFavoritingActivity$listingCallback$1(this);
        this.controlsModelCallback = new ControlsFavoritingActivity$controlsModelCallback$1(this);
    }

    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsFavoritingActivity$Companion;", "", "()V", "EXTRA_APP", "", "EXTRA_FROM_PROVIDER_SELECTOR", "EXTRA_SINGLE_STRUCTURE", "EXTRA_STRUCTURE", "TAG", "TOOLTIP_MAX_SHOWN", "", "TOOLTIP_PREFS_KEY", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlsFavoritingActivity.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public void onBackPressed() {
        if (!this.fromProviderSelector) {
            openControlsOrigin();
        }
        animateExitAndFinish();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Collator instance = Collator.getInstance(getResources().getConfiguration().getLocales().get(0));
        Intrinsics.checkNotNullExpressionValue(instance, "collator");
        this.comparator = new ControlsFavoritingActivity$onCreate$$inlined$compareBy$1(instance);
        this.appName = getIntent().getCharSequenceExtra(EXTRA_APP);
        this.structureExtra = getIntent().getCharSequenceExtra(EXTRA_STRUCTURE);
        this.component = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        this.fromProviderSelector = getIntent().getBooleanExtra(EXTRA_FROM_PROVIDER_SELECTOR, false);
        bindViews();
    }

    private final void loadControls() {
        ComponentName componentName = this.component;
        if (componentName != null) {
            TextView textView = this.statusText;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("statusText");
                textView = null;
            }
            textView.setText(getResources().getText(17040589));
            this.controller.loadForComponent(componentName, new ControlsFavoritingActivity$$ExternalSyntheticLambda3(this, getResources().getText(C1894R.string.controls_favorite_other_zone_header)), new ControlsFavoritingActivity$$ExternalSyntheticLambda4(this));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: loadControls$lambda-9$lambda-7  reason: not valid java name */
    public static final void m2648loadControls$lambda9$lambda7(ControlsFavoritingActivity controlsFavoritingActivity, CharSequence charSequence, ControlsController.LoadData loadData) {
        Intrinsics.checkNotNullParameter(controlsFavoritingActivity, "this$0");
        Intrinsics.checkNotNullParameter(loadData, "data");
        List<ControlStatus> allControls = loadData.getAllControls();
        List<String> favoritesIds = loadData.getFavoritesIds();
        boolean errorOnLoad = loadData.getErrorOnLoad();
        Map linkedHashMap = new LinkedHashMap();
        for (Object next : allControls) {
            CharSequence structure = ((ControlStatus) next).getControl().getStructure();
            if (structure != null) {
                Intrinsics.checkNotNullExpressionValue(structure, "it.control.structure ?: \"\"");
            }
            Object obj = linkedHashMap.get(structure);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(structure, obj);
            }
            ((List) obj).add(next);
        }
        Collection arrayList = new ArrayList(linkedHashMap.size());
        for (Map.Entry entry : linkedHashMap.entrySet()) {
            Intrinsics.checkNotNullExpressionValue(charSequence, "emptyZoneString");
            arrayList.add(new StructureContainer((CharSequence) entry.getKey(), new AllModel((List) entry.getValue(), favoritesIds, charSequence, controlsFavoritingActivity.controlsModelCallback)));
        }
        Iterable iterable = (List) arrayList;
        Comparator<StructureContainer> comparator2 = controlsFavoritingActivity.comparator;
        if (comparator2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("comparator");
            comparator2 = null;
        }
        List<StructureContainer> sortedWith = CollectionsKt.sortedWith(iterable, comparator2);
        controlsFavoritingActivity.listOfStructures = sortedWith;
        Iterator<StructureContainer> it = sortedWith.iterator();
        int i = 0;
        while (true) {
            if (!it.hasNext()) {
                i = -1;
                break;
            } else if (Intrinsics.areEqual((Object) it.next().getStructureName(), (Object) controlsFavoritingActivity.structureExtra)) {
                break;
            } else {
                i++;
            }
        }
        if (i == -1) {
            i = 0;
        }
        if (controlsFavoritingActivity.getIntent().getBooleanExtra(EXTRA_SINGLE_STRUCTURE, false)) {
            controlsFavoritingActivity.listOfStructures = CollectionsKt.listOf(controlsFavoritingActivity.listOfStructures.get(i));
        }
        controlsFavoritingActivity.executor.execute(new ControlsFavoritingActivity$$ExternalSyntheticLambda2(controlsFavoritingActivity, i, errorOnLoad));
    }

    /* JADX WARNING: type inference failed for: r6v1, types: [androidx.viewpager2.widget.ViewPager2] */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* renamed from: loadControls$lambda-9$lambda-7$lambda-6  reason: not valid java name */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void m2649loadControls$lambda9$lambda7$lambda6(com.android.systemui.controls.management.ControlsFavoritingActivity r6, int r7, boolean r8) {
        /*
            java.lang.String r0 = "this$0"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r0)
            androidx.viewpager2.widget.ViewPager2 r0 = r6.structurePager
            java.lang.String r1 = "structurePager"
            r2 = 0
            if (r0 != 0) goto L_0x0012
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1)
            r0 = r2
        L_0x0012:
            com.android.systemui.controls.management.StructureAdapter r3 = new com.android.systemui.controls.management.StructureAdapter
            java.util.List<com.android.systemui.controls.management.StructureContainer> r4 = r6.listOfStructures
            r3.<init>(r4)
            androidx.recyclerview.widget.RecyclerView$Adapter r3 = (androidx.recyclerview.widget.RecyclerView.Adapter) r3
            r0.setAdapter(r3)
            androidx.viewpager2.widget.ViewPager2 r0 = r6.structurePager
            if (r0 != 0) goto L_0x0026
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1)
            r0 = r2
        L_0x0026:
            r0.setCurrentItem(r7)
            java.lang.String r7 = "subtitleView"
            r0 = 0
            r3 = 1
            r4 = 8
            java.lang.String r5 = "statusText"
            if (r8 == 0) goto L_0x0067
            android.widget.TextView r8 = r6.statusText
            if (r8 != 0) goto L_0x003d
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r5)
            r8 = r2
        L_0x003d:
            android.content.res.Resources r1 = r6.getResources()
            java.lang.Object[] r3 = new java.lang.Object[r3]
            java.lang.CharSequence r5 = r6.appName
            if (r5 != 0) goto L_0x004b
            java.lang.String r5 = ""
            java.lang.CharSequence r5 = (java.lang.CharSequence) r5
        L_0x004b:
            r3[r0] = r5
            r0 = 2131952195(0x7f130243, float:1.9540826E38)
            java.lang.String r0 = r1.getString(r0, r3)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            r8.setText(r0)
            android.widget.TextView r6 = r6.subtitleView
            if (r6 != 0) goto L_0x0061
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r7)
            goto L_0x0062
        L_0x0061:
            r2 = r6
        L_0x0062:
            r2.setVisibility(r4)
            goto L_0x0106
        L_0x0067:
            java.util.List<com.android.systemui.controls.management.StructureContainer> r8 = r6.listOfStructures
            boolean r8 = r8.isEmpty()
            if (r8 == 0) goto L_0x0095
            android.widget.TextView r8 = r6.statusText
            if (r8 != 0) goto L_0x0077
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r5)
            r8 = r2
        L_0x0077:
            android.content.res.Resources r0 = r6.getResources()
            r1 = 2131952196(0x7f130244, float:1.9540828E38)
            java.lang.String r0 = r0.getString(r1)
            java.lang.CharSequence r0 = (java.lang.CharSequence) r0
            r8.setText(r0)
            android.widget.TextView r6 = r6.subtitleView
            if (r6 != 0) goto L_0x008f
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r7)
            goto L_0x0090
        L_0x008f:
            r2 = r6
        L_0x0090:
            r2.setVisibility(r4)
            goto L_0x0106
        L_0x0095:
            android.widget.TextView r7 = r6.statusText
            if (r7 != 0) goto L_0x009d
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r5)
            r7 = r2
        L_0x009d:
            r7.setVisibility(r4)
            com.android.systemui.controls.management.ManagementPageIndicator r7 = r6.pageIndicator
            java.lang.String r8 = "pageIndicator"
            if (r7 != 0) goto L_0x00ab
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r8)
            r7 = r2
        L_0x00ab:
            java.util.List<com.android.systemui.controls.management.StructureContainer> r4 = r6.listOfStructures
            int r4 = r4.size()
            r7.setNumPages(r4)
            com.android.systemui.controls.management.ManagementPageIndicator r7 = r6.pageIndicator
            if (r7 != 0) goto L_0x00bc
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r8)
            r7 = r2
        L_0x00bc:
            r4 = 0
            r7.setLocation(r4)
            com.android.systemui.controls.management.ManagementPageIndicator r7 = r6.pageIndicator
            if (r7 != 0) goto L_0x00c8
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r8)
            r7 = r2
        L_0x00c8:
            java.util.List<com.android.systemui.controls.management.StructureContainer> r4 = r6.listOfStructures
            int r4 = r4.size()
            if (r4 <= r3) goto L_0x00d1
            goto L_0x00d2
        L_0x00d1:
            r0 = 4
        L_0x00d2:
            r7.setVisibility(r0)
            com.android.systemui.controls.management.ControlsAnimations r7 = com.android.systemui.controls.management.ControlsAnimations.INSTANCE
            com.android.systemui.controls.management.ManagementPageIndicator r0 = r6.pageIndicator
            if (r0 != 0) goto L_0x00df
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r8)
            r0 = r2
        L_0x00df:
            android.view.View r0 = (android.view.View) r0
            android.animation.Animator r7 = r7.enterAnimation(r0)
            com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1$2$1$1 r8 = new com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1$2$1$1
            r8.<init>(r6)
            android.animation.Animator$AnimatorListener r8 = (android.animation.Animator.AnimatorListener) r8
            r7.addListener(r8)
            r7.start()
            com.android.systemui.controls.management.ControlsAnimations r7 = com.android.systemui.controls.management.ControlsAnimations.INSTANCE
            androidx.viewpager2.widget.ViewPager2 r6 = r6.structurePager
            if (r6 != 0) goto L_0x00fc
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r1)
            goto L_0x00fd
        L_0x00fc:
            r2 = r6
        L_0x00fd:
            android.view.View r2 = (android.view.View) r2
            android.animation.Animator r6 = r7.enterAnimation(r2)
            r6.start()
        L_0x0106:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.management.ControlsFavoritingActivity.m2649loadControls$lambda9$lambda7$lambda6(com.android.systemui.controls.management.ControlsFavoritingActivity, int, boolean):void");
    }

    /* access modifiers changed from: private */
    /* renamed from: loadControls$lambda-9$lambda-8  reason: not valid java name */
    public static final void m2650loadControls$lambda9$lambda8(ControlsFavoritingActivity controlsFavoritingActivity, Runnable runnable) {
        Intrinsics.checkNotNullParameter(controlsFavoritingActivity, "this$0");
        Intrinsics.checkNotNullParameter(runnable, "runnable");
        controlsFavoritingActivity.cancelLoadRunnable = runnable;
    }

    private final void setUpPager() {
        ViewPager2 viewPager2 = this.structurePager;
        ViewPager2 viewPager22 = null;
        if (viewPager2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structurePager");
            viewPager2 = null;
        }
        viewPager2.setAlpha(0.0f);
        ManagementPageIndicator managementPageIndicator = this.pageIndicator;
        if (managementPageIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
            managementPageIndicator = null;
        }
        managementPageIndicator.setAlpha(0.0f);
        ViewPager2 viewPager23 = this.structurePager;
        if (viewPager23 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structurePager");
        } else {
            viewPager22 = viewPager23;
        }
        viewPager22.setAdapter(new StructureAdapter(CollectionsKt.emptyList()));
        viewPager22.registerOnPageChangeCallback(new ControlsFavoritingActivity$setUpPager$1$1(this));
    }

    private final void bindViews() {
        setContentView(C1894R.layout.controls_management);
        Lifecycle lifecycle = getLifecycle();
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        View requireViewById = requireViewById(C1894R.C1898id.controls_management_root);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGrou…controls_management_root)");
        Window window = getWindow();
        Intrinsics.checkNotNullExpressionValue(window, "window");
        Intent intent = getIntent();
        Intrinsics.checkNotNullExpressionValue(intent, "intent");
        lifecycle.addObserver(controlsAnimations.observerForAnimations((ViewGroup) requireViewById, window, intent));
        ViewStub viewStub = (ViewStub) requireViewById(C1894R.C1898id.stub);
        viewStub.setLayoutResource(C1894R.layout.controls_management_favorites);
        viewStub.inflate();
        View requireViewById2 = requireViewById(C1894R.C1898id.status_message);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(R.id.status_message)");
        this.statusText = (TextView) requireViewById2;
        ViewPager2 viewPager2 = null;
        if (shouldShowTooltip()) {
            TextView textView = this.statusText;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("statusText");
                textView = null;
            }
            Context context = textView.getContext();
            Intrinsics.checkNotNullExpressionValue(context, "statusText.context");
            TooltipManager tooltipManager = new TooltipManager(context, "ControlsStructureSwipeTooltipCount", 2, false, 8, (DefaultConstructorMarker) null);
            this.mTooltipManager = tooltipManager;
            addContentView(tooltipManager.getLayout(), new FrameLayout.LayoutParams(-2, -2, 51));
        }
        View requireViewById3 = requireViewById(C1894R.C1898id.structure_page_indicator);
        ManagementPageIndicator managementPageIndicator = (ManagementPageIndicator) requireViewById3;
        managementPageIndicator.setVisibilityListener(new ControlsFavoritingActivity$bindViews$2$1(this));
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "requireViewById<Manageme…}\n            }\n        }");
        this.pageIndicator = managementPageIndicator;
        CharSequence charSequence = this.structureExtra;
        if (charSequence == null && (charSequence = this.appName) == null) {
            charSequence = getResources().getText(C1894R.string.controls_favorite_default_title);
        }
        View requireViewById4 = requireViewById(C1894R.C1898id.title);
        TextView textView2 = (TextView) requireViewById4;
        textView2.setText(charSequence);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "requireViewById<TextView…   text = title\n        }");
        this.titleView = textView2;
        View requireViewById5 = requireViewById(C1894R.C1898id.subtitle);
        TextView textView3 = (TextView) requireViewById5;
        textView3.setText(textView3.getResources().getText(C1894R.string.controls_favorite_subtitle));
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "requireViewById<TextView…orite_subtitle)\n        }");
        this.subtitleView = textView3;
        View requireViewById6 = requireViewById(C1894R.C1898id.structure_pager);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "requireViewById<ViewPager2>(R.id.structure_pager)");
        ViewPager2 viewPager22 = (ViewPager2) requireViewById6;
        this.structurePager = viewPager22;
        if (viewPager22 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structurePager");
        } else {
            viewPager2 = viewPager22;
        }
        viewPager2.registerOnPageChangeCallback(new ControlsFavoritingActivity$bindViews$5(this));
        bindButtons();
    }

    private final void animateExitAndFinish() {
        ViewGroup viewGroup = (ViewGroup) requireViewById(C1894R.C1898id.controls_management_root);
        Intrinsics.checkNotNullExpressionValue(viewGroup, "rootView");
        ControlsAnimations.exitAnimation(viewGroup, new ControlsFavoritingActivity$animateExitAndFinish$1(this)).start();
    }

    private final void bindButtons() {
        View requireViewById = requireViewById(C1894R.C1898id.other_apps);
        Button button = (Button) requireViewById;
        button.setOnClickListener(new ControlsFavoritingActivity$$ExternalSyntheticLambda0(this, button));
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<Button>(…)\n            }\n        }");
        this.otherAppsButton = requireViewById;
        View requireViewById2 = requireViewById(C1894R.C1898id.done);
        Button button2 = (Button) requireViewById2;
        button2.setEnabled(false);
        button2.setOnClickListener(new ControlsFavoritingActivity$$ExternalSyntheticLambda1(this));
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById<Button>(…)\n            }\n        }");
        this.doneButton = requireViewById2;
    }

    /* access modifiers changed from: private */
    /* renamed from: bindButtons$lambda-16$lambda-15  reason: not valid java name */
    public static final void m2646bindButtons$lambda16$lambda15(ControlsFavoritingActivity controlsFavoritingActivity, Button button, View view) {
        Intrinsics.checkNotNullParameter(controlsFavoritingActivity, "this$0");
        View view2 = controlsFavoritingActivity.doneButton;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("doneButton");
            view2 = null;
        }
        if (view2.isEnabled()) {
            Toast.makeText(controlsFavoritingActivity.getApplicationContext(), C1894R.string.controls_favorite_toast_no_changes, 0).show();
        }
        controlsFavoritingActivity.startActivity(new Intent(button.getContext(), ControlsProviderSelectorActivity.class), ActivityOptions.makeSceneTransitionAnimation(controlsFavoritingActivity, new Pair[0]).toBundle());
        controlsFavoritingActivity.animateExitAndFinish();
    }

    /* access modifiers changed from: private */
    /* renamed from: bindButtons$lambda-19$lambda-18  reason: not valid java name */
    public static final void m2647bindButtons$lambda19$lambda18(ControlsFavoritingActivity controlsFavoritingActivity, View view) {
        Intrinsics.checkNotNullParameter(controlsFavoritingActivity, "this$0");
        if (controlsFavoritingActivity.component != null) {
            for (StructureContainer structureContainer : controlsFavoritingActivity.listOfStructures) {
                List<ControlInfo> favorites = structureContainer.getModel().getFavorites();
                ControlsControllerImpl controlsControllerImpl = controlsFavoritingActivity.controller;
                ComponentName componentName = controlsFavoritingActivity.component;
                Intrinsics.checkNotNull(componentName);
                controlsControllerImpl.replaceFavoritesForStructure(new StructureInfo(componentName, structureContainer.getStructureName(), favorites));
            }
            controlsFavoritingActivity.animateExitAndFinish();
            controlsFavoritingActivity.openControlsOrigin();
        }
    }

    private final void openControlsOrigin() {
        startActivity(new Intent(getApplicationContext(), ControlsActivity.class), ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager != null) {
            tooltipManager.hide(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.listingController.addCallback(this.listingCallback);
        this.currentUserTracker.startTracking();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!this.isPagerLoaded) {
            setUpPager();
            loadControls();
            this.isPagerLoaded = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.listingController.removeCallback(this.listingCallback);
        this.currentUserTracker.stopTracking();
    }

    public void onConfigurationChanged(Configuration configuration) {
        Intrinsics.checkNotNullParameter(configuration, "newConfig");
        super.onConfigurationChanged(configuration);
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager != null) {
            tooltipManager.hide(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        Runnable runnable = this.cancelLoadRunnable;
        if (runnable != null) {
            runnable.run();
        }
        super.onDestroy();
    }

    private final boolean shouldShowTooltip() {
        return Prefs.getInt(getApplicationContext(), "ControlsStructureSwipeTooltipCount", 0) < 2;
    }
}
