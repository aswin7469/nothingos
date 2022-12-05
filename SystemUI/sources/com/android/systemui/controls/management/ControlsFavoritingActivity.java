package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.android.systemui.Prefs;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.TooltipManager;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.management.ControlsModel;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.util.LifecycleActivity;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritingActivity extends LifecycleActivity {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private CharSequence appName;
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @Nullable
    private Runnable cancelLoadRunnable;
    private Comparator<StructureContainer> comparator;
    @Nullable
    private ComponentName component;
    @NotNull
    private final ControlsControllerImpl controller;
    @NotNull
    private final ControlsFavoritingActivity$currentUserTracker$1 currentUserTracker;
    private View doneButton;
    @NotNull
    private final Executor executor;
    private boolean fromProviderSelector;
    private boolean isPagerLoaded;
    @NotNull
    private List<StructureContainer> listOfStructures;
    @NotNull
    private final ControlsListingController listingController;
    @Nullable
    private TooltipManager mTooltipManager;
    private View otherAppsButton;
    private ManagementPageIndicator pageIndicator;
    private TextView statusText;
    @Nullable
    private CharSequence structureExtra;
    private ViewPager2 structurePager;
    private TextView subtitleView;
    private TextView titleView;
    @NotNull
    private final ControlsUiController uiController;
    @NotNull
    private final ControlsFavoritingActivity$listingCallback$1 listingCallback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$listingCallback$1
        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
        public void onServicesUpdated(@NotNull List<ControlsServiceInfo> serviceInfos) {
            View view;
            Intrinsics.checkNotNullParameter(serviceInfos, "serviceInfos");
            if (serviceInfos.size() > 1) {
                view = ControlsFavoritingActivity.this.otherAppsButton;
                if (view == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("otherAppsButton");
                    throw null;
                }
                final ControlsFavoritingActivity controlsFavoritingActivity = ControlsFavoritingActivity.this;
                view.post(new Runnable() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$listingCallback$1$onServicesUpdated$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        View view2;
                        view2 = ControlsFavoritingActivity.this.otherAppsButton;
                        if (view2 != null) {
                            view2.setVisibility(0);
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("otherAppsButton");
                            throw null;
                        }
                    }
                });
            }
        }
    };
    @NotNull
    private final ControlsFavoritingActivity$controlsModelCallback$1 controlsModelCallback = new ControlsModel.ControlsModelCallback() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$controlsModelCallback$1
        @Override // com.android.systemui.controls.management.ControlsModel.ControlsModelCallback
        public void onFirstChange() {
            View view;
            view = ControlsFavoritingActivity.this.doneButton;
            if (view != null) {
                view.setEnabled(true);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("doneButton");
                throw null;
            }
        }
    };

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.controls.management.ControlsFavoritingActivity$currentUserTracker$1] */
    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.controls.management.ControlsFavoritingActivity$listingCallback$1] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.controls.management.ControlsFavoritingActivity$controlsModelCallback$1] */
    public ControlsFavoritingActivity(@NotNull Executor executor, @NotNull ControlsControllerImpl controller, @NotNull ControlsListingController listingController, @NotNull final BroadcastDispatcher broadcastDispatcher, @NotNull ControlsUiController uiController) {
        List<StructureContainer> emptyList;
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(controller, "controller");
        Intrinsics.checkNotNullParameter(listingController, "listingController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(uiController, "uiController");
        this.executor = executor;
        this.controller = controller;
        this.listingController = listingController;
        this.broadcastDispatcher = broadcastDispatcher;
        this.uiController = uiController;
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        this.listOfStructures = emptyList;
        this.currentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$currentUserTracker$1
            private final int startingUser;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                ControlsControllerImpl controlsControllerImpl;
                controlsControllerImpl = ControlsFavoritingActivity.this.controller;
                this.startingUser = controlsControllerImpl.getCurrentUserId();
            }

            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                if (i != this.startingUser) {
                    stopTracking();
                    ControlsFavoritingActivity.this.finish();
                }
            }
        };
    }

    /* compiled from: ControlsFavoritingActivity.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (!this.fromProviderSelector) {
            openControlsOrigin();
        }
        animateExitAndFinish();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        final Collator collator = Collator.getInstance(getResources().getConfiguration().getLocales().get(0));
        Intrinsics.checkNotNullExpressionValue(collator, "collator");
        this.comparator = new Comparator<T>() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$onCreate$$inlined$compareBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return collator.compare(((StructureContainer) t).getStructureName(), ((StructureContainer) t2).getStructureName());
            }
        };
        this.appName = getIntent().getCharSequenceExtra("extra_app_label");
        this.structureExtra = getIntent().getCharSequenceExtra("extra_structure");
        this.component = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        this.fromProviderSelector = getIntent().getBooleanExtra("extra_from_provider_selector", false);
        bindViews();
    }

    private final void loadControls() {
        ComponentName componentName = this.component;
        if (componentName == null) {
            return;
        }
        TextView textView = this.statusText;
        if (textView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("statusText");
            throw null;
        }
        textView.setText(getResources().getText(17040512));
        final CharSequence text = getResources().getText(R$string.controls_favorite_other_zone_header);
        this.controller.loadForComponent(componentName, new Consumer<ControlsController.LoadData>() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1
            @Override // java.util.function.Consumer
            public final void accept(@NotNull ControlsController.LoadData data) {
                Comparator comparator;
                List sortedWith;
                List list;
                Executor executor;
                List list2;
                List listOf;
                CharSequence charSequence;
                ControlsFavoritingActivity$controlsModelCallback$1 controlsFavoritingActivity$controlsModelCallback$1;
                Intrinsics.checkNotNullParameter(data, "data");
                List<ControlStatus> allControls = data.getAllControls();
                List<String> favoritesIds = data.getFavoritesIds();
                final boolean errorOnLoad = data.getErrorOnLoad();
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                for (Object obj : allControls) {
                    CharSequence structure = ((ControlStatus) obj).getControl().getStructure();
                    if (structure == null) {
                        structure = "";
                    }
                    Object obj2 = linkedHashMap.get(structure);
                    if (obj2 == null) {
                        obj2 = new ArrayList();
                        linkedHashMap.put(structure, obj2);
                    }
                    ((List) obj2).add(obj);
                }
                ControlsFavoritingActivity controlsFavoritingActivity = ControlsFavoritingActivity.this;
                CharSequence emptyZoneString = text;
                ArrayList arrayList = new ArrayList(linkedHashMap.size());
                for (Map.Entry entry : linkedHashMap.entrySet()) {
                    Intrinsics.checkNotNullExpressionValue(emptyZoneString, "emptyZoneString");
                    controlsFavoritingActivity$controlsModelCallback$1 = controlsFavoritingActivity.controlsModelCallback;
                    arrayList.add(new StructureContainer((CharSequence) entry.getKey(), new AllModel((List) entry.getValue(), favoritesIds, emptyZoneString, controlsFavoritingActivity$controlsModelCallback$1)));
                }
                comparator = ControlsFavoritingActivity.this.comparator;
                if (comparator != null) {
                    sortedWith = CollectionsKt___CollectionsKt.sortedWith(arrayList, comparator);
                    controlsFavoritingActivity.listOfStructures = sortedWith;
                    list = ControlsFavoritingActivity.this.listOfStructures;
                    ControlsFavoritingActivity controlsFavoritingActivity2 = ControlsFavoritingActivity.this;
                    Iterator it = list.iterator();
                    final int i = 0;
                    while (true) {
                        if (!it.hasNext()) {
                            i = -1;
                            break;
                        }
                        CharSequence structureName = ((StructureContainer) it.next()).getStructureName();
                        charSequence = controlsFavoritingActivity2.structureExtra;
                        if (Intrinsics.areEqual(structureName, charSequence)) {
                            break;
                        }
                        i++;
                    }
                    if (i == -1) {
                        i = 0;
                    }
                    if (ControlsFavoritingActivity.this.getIntent().getBooleanExtra("extra_single_structure", false)) {
                        ControlsFavoritingActivity controlsFavoritingActivity3 = ControlsFavoritingActivity.this;
                        list2 = controlsFavoritingActivity3.listOfStructures;
                        listOf = CollectionsKt__CollectionsJVMKt.listOf(list2.get(i));
                        controlsFavoritingActivity3.listOfStructures = listOf;
                    }
                    executor = ControlsFavoritingActivity.this.executor;
                    final ControlsFavoritingActivity controlsFavoritingActivity4 = ControlsFavoritingActivity.this;
                    executor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1.2
                        @Override // java.lang.Runnable
                        public final void run() {
                            ViewPager2 viewPager2;
                            List list3;
                            ViewPager2 viewPager22;
                            List list4;
                            TextView textView2;
                            ManagementPageIndicator managementPageIndicator;
                            List list5;
                            ManagementPageIndicator managementPageIndicator2;
                            ManagementPageIndicator managementPageIndicator3;
                            List list6;
                            ManagementPageIndicator managementPageIndicator4;
                            ViewPager2 viewPager23;
                            TextView textView3;
                            TextView textView4;
                            TextView textView5;
                            Object obj3;
                            TextView textView6;
                            viewPager2 = ControlsFavoritingActivity.this.structurePager;
                            if (viewPager2 != null) {
                                list3 = ControlsFavoritingActivity.this.listOfStructures;
                                viewPager2.setAdapter(new StructureAdapter(list3));
                                viewPager22 = ControlsFavoritingActivity.this.structurePager;
                                if (viewPager22 == null) {
                                    Intrinsics.throwUninitializedPropertyAccessException("structurePager");
                                    throw null;
                                }
                                viewPager22.setCurrentItem(i);
                                int i2 = 0;
                                if (errorOnLoad) {
                                    textView5 = ControlsFavoritingActivity.this.statusText;
                                    if (textView5 != null) {
                                        Resources resources = ControlsFavoritingActivity.this.getResources();
                                        int i3 = R$string.controls_favorite_load_error;
                                        Object[] objArr = new Object[1];
                                        obj3 = ControlsFavoritingActivity.this.appName;
                                        if (obj3 == null) {
                                            obj3 = "";
                                        }
                                        objArr[0] = obj3;
                                        textView5.setText(resources.getString(i3, objArr));
                                        textView6 = ControlsFavoritingActivity.this.subtitleView;
                                        if (textView6 != null) {
                                            textView6.setVisibility(8);
                                            return;
                                        } else {
                                            Intrinsics.throwUninitializedPropertyAccessException("subtitleView");
                                            throw null;
                                        }
                                    }
                                    Intrinsics.throwUninitializedPropertyAccessException("statusText");
                                    throw null;
                                }
                                list4 = ControlsFavoritingActivity.this.listOfStructures;
                                if (list4.isEmpty()) {
                                    textView3 = ControlsFavoritingActivity.this.statusText;
                                    if (textView3 != null) {
                                        textView3.setText(ControlsFavoritingActivity.this.getResources().getString(R$string.controls_favorite_load_none));
                                        textView4 = ControlsFavoritingActivity.this.subtitleView;
                                        if (textView4 != null) {
                                            textView4.setVisibility(8);
                                            return;
                                        } else {
                                            Intrinsics.throwUninitializedPropertyAccessException("subtitleView");
                                            throw null;
                                        }
                                    }
                                    Intrinsics.throwUninitializedPropertyAccessException("statusText");
                                    throw null;
                                }
                                textView2 = ControlsFavoritingActivity.this.statusText;
                                if (textView2 != null) {
                                    textView2.setVisibility(8);
                                    managementPageIndicator = ControlsFavoritingActivity.this.pageIndicator;
                                    if (managementPageIndicator != null) {
                                        list5 = ControlsFavoritingActivity.this.listOfStructures;
                                        managementPageIndicator.setNumPages(list5.size());
                                        managementPageIndicator2 = ControlsFavoritingActivity.this.pageIndicator;
                                        if (managementPageIndicator2 != null) {
                                            managementPageIndicator2.setLocation(0.0f);
                                            managementPageIndicator3 = ControlsFavoritingActivity.this.pageIndicator;
                                            if (managementPageIndicator3 != null) {
                                                list6 = ControlsFavoritingActivity.this.listOfStructures;
                                                if (list6.size() <= 1) {
                                                    i2 = 4;
                                                }
                                                managementPageIndicator3.setVisibility(i2);
                                                ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                                                managementPageIndicator4 = ControlsFavoritingActivity.this.pageIndicator;
                                                if (managementPageIndicator4 == null) {
                                                    Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                                    throw null;
                                                }
                                                Animator enterAnimation = controlsAnimations.enterAnimation(managementPageIndicator4);
                                                final ControlsFavoritingActivity controlsFavoritingActivity5 = ControlsFavoritingActivity.this;
                                                enterAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$1$2$1$1
                                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                                    public void onAnimationEnd(@Nullable Animator animator) {
                                                        ManagementPageIndicator managementPageIndicator5;
                                                        TooltipManager tooltipManager;
                                                        ManagementPageIndicator managementPageIndicator6;
                                                        ManagementPageIndicator managementPageIndicator7;
                                                        ManagementPageIndicator managementPageIndicator8;
                                                        TooltipManager tooltipManager2;
                                                        managementPageIndicator5 = ControlsFavoritingActivity.this.pageIndicator;
                                                        if (managementPageIndicator5 == null) {
                                                            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                                            throw null;
                                                        } else if (managementPageIndicator5.getVisibility() != 0) {
                                                        } else {
                                                            tooltipManager = ControlsFavoritingActivity.this.mTooltipManager;
                                                            if (tooltipManager == null) {
                                                                return;
                                                            }
                                                            int[] iArr = new int[2];
                                                            managementPageIndicator6 = ControlsFavoritingActivity.this.pageIndicator;
                                                            if (managementPageIndicator6 == null) {
                                                                Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                                                throw null;
                                                            }
                                                            managementPageIndicator6.getLocationOnScreen(iArr);
                                                            int i4 = iArr[0];
                                                            managementPageIndicator7 = ControlsFavoritingActivity.this.pageIndicator;
                                                            if (managementPageIndicator7 == null) {
                                                                Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                                                throw null;
                                                            }
                                                            int width = i4 + (managementPageIndicator7.getWidth() / 2);
                                                            int i5 = iArr[1];
                                                            managementPageIndicator8 = ControlsFavoritingActivity.this.pageIndicator;
                                                            if (managementPageIndicator8 != null) {
                                                                int height = i5 + managementPageIndicator8.getHeight();
                                                                tooltipManager2 = ControlsFavoritingActivity.this.mTooltipManager;
                                                                if (tooltipManager2 == null) {
                                                                    return;
                                                                }
                                                                tooltipManager2.show(R$string.controls_structure_tooltip, width, height);
                                                                return;
                                                            }
                                                            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                                            throw null;
                                                        }
                                                    }
                                                });
                                                enterAnimation.start();
                                                viewPager23 = ControlsFavoritingActivity.this.structurePager;
                                                if (viewPager23 != null) {
                                                    controlsAnimations.enterAnimation(viewPager23).start();
                                                    return;
                                                } else {
                                                    Intrinsics.throwUninitializedPropertyAccessException("structurePager");
                                                    throw null;
                                                }
                                            }
                                            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                            throw null;
                                        }
                                        Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                        throw null;
                                    }
                                    Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                                    throw null;
                                }
                                Intrinsics.throwUninitializedPropertyAccessException("statusText");
                                throw null;
                            }
                            Intrinsics.throwUninitializedPropertyAccessException("structurePager");
                            throw null;
                        }
                    });
                    return;
                }
                Intrinsics.throwUninitializedPropertyAccessException("comparator");
                throw null;
            }
        }, new Consumer<Runnable>() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$loadControls$1$2
            @Override // java.util.function.Consumer
            public final void accept(@NotNull Runnable runnable) {
                Intrinsics.checkNotNullParameter(runnable, "runnable");
                ControlsFavoritingActivity.this.cancelLoadRunnable = runnable;
            }
        });
    }

    private final void setUpPager() {
        List emptyList;
        ViewPager2 viewPager2 = this.structurePager;
        if (viewPager2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structurePager");
            throw null;
        }
        viewPager2.setAlpha(0.0f);
        ManagementPageIndicator managementPageIndicator = this.pageIndicator;
        if (managementPageIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
            throw null;
        }
        managementPageIndicator.setAlpha(0.0f);
        ViewPager2 viewPager22 = this.structurePager;
        if (viewPager22 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structurePager");
            throw null;
        }
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        viewPager22.setAdapter(new StructureAdapter(emptyList));
        viewPager22.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$setUpPager$1$1
            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public void onPageSelected(int i) {
                List list;
                TextView textView;
                TextView textView2;
                super.onPageSelected(i);
                list = ControlsFavoritingActivity.this.listOfStructures;
                CharSequence structureName = ((StructureContainer) list.get(i)).getStructureName();
                if (TextUtils.isEmpty(structureName)) {
                    structureName = ControlsFavoritingActivity.this.appName;
                }
                textView = ControlsFavoritingActivity.this.titleView;
                if (textView != null) {
                    textView.setText(structureName);
                    textView2 = ControlsFavoritingActivity.this.titleView;
                    if (textView2 != null) {
                        textView2.requestFocus();
                        return;
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("titleView");
                        throw null;
                    }
                }
                Intrinsics.throwUninitializedPropertyAccessException("titleView");
                throw null;
            }

            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public void onPageScrolled(int i, float f, int i2) {
                ManagementPageIndicator managementPageIndicator2;
                super.onPageScrolled(i, f, i2);
                managementPageIndicator2 = ControlsFavoritingActivity.this.pageIndicator;
                if (managementPageIndicator2 != null) {
                    managementPageIndicator2.setLocation(i + f);
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                    throw null;
                }
            }
        });
    }

    private final void bindViews() {
        setContentView(R$layout.controls_management);
        Lifecycle mo1437getLifecycle = mo1437getLifecycle();
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        View requireViewById = requireViewById(R$id.controls_management_root);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGroup>(R.id.controls_management_root)");
        Window window = getWindow();
        Intrinsics.checkNotNullExpressionValue(window, "window");
        Intent intent = getIntent();
        Intrinsics.checkNotNullExpressionValue(intent, "intent");
        mo1437getLifecycle.addObserver(controlsAnimations.observerForAnimations((ViewGroup) requireViewById, window, intent));
        ViewStub viewStub = (ViewStub) requireViewById(R$id.stub);
        viewStub.setLayoutResource(R$layout.controls_management_favorites);
        viewStub.inflate();
        View requireViewById2 = requireViewById(R$id.status_message);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(R.id.status_message)");
        this.statusText = (TextView) requireViewById2;
        if (shouldShowTooltip()) {
            TextView textView = this.statusText;
            if (textView != null) {
                Context context = textView.getContext();
                Intrinsics.checkNotNullExpressionValue(context, "statusText.context");
                TooltipManager tooltipManager = new TooltipManager(context, "ControlsStructureSwipeTooltipCount", 2, false, 8, null);
                this.mTooltipManager = tooltipManager;
                addContentView(tooltipManager.getLayout(), new FrameLayout.LayoutParams(-2, -2, 51));
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("statusText");
                throw null;
            }
        }
        View requireViewById3 = requireViewById(R$id.structure_page_indicator);
        ManagementPageIndicator managementPageIndicator = (ManagementPageIndicator) requireViewById3;
        managementPageIndicator.setVisibilityListener(new ControlsFavoritingActivity$bindViews$2$1(this));
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "requireViewById<ManagementPageIndicator>(\n            R.id.structure_page_indicator).apply {\n            visibilityListener = {\n                if (it != View.VISIBLE) {\n                    mTooltipManager?.hide(true)\n                }\n            }\n        }");
        this.pageIndicator = managementPageIndicator;
        CharSequence charSequence = this.structureExtra;
        if (charSequence == null && (charSequence = this.appName) == null) {
            charSequence = getResources().getText(R$string.controls_favorite_default_title);
        }
        View requireViewById4 = requireViewById(R$id.title);
        TextView textView2 = (TextView) requireViewById4;
        textView2.setText(charSequence);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "requireViewById<TextView>(R.id.title).apply {\n            text = title\n        }");
        this.titleView = textView2;
        View requireViewById5 = requireViewById(R$id.subtitle);
        TextView textView3 = (TextView) requireViewById5;
        textView3.setText(textView3.getResources().getText(R$string.controls_favorite_subtitle));
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "requireViewById<TextView>(R.id.subtitle).apply {\n            text = resources.getText(R.string.controls_favorite_subtitle)\n        }");
        this.subtitleView = textView3;
        View requireViewById6 = requireViewById(R$id.structure_pager);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "requireViewById<ViewPager2>(R.id.structure_pager)");
        ViewPager2 viewPager2 = (ViewPager2) requireViewById6;
        this.structurePager = viewPager2;
        if (viewPager2 != null) {
            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindViews$5
                @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
                public void onPageSelected(int i) {
                    TooltipManager tooltipManager2;
                    super.onPageSelected(i);
                    tooltipManager2 = ControlsFavoritingActivity.this.mTooltipManager;
                    if (tooltipManager2 == null) {
                        return;
                    }
                    tooltipManager2.hide(true);
                }
            });
            bindButtons();
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("structurePager");
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void animateExitAndFinish() {
        ViewGroup rootView = (ViewGroup) requireViewById(R$id.controls_management_root);
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(rootView, "rootView");
        ControlsAnimations.exitAnimation(rootView, new Runnable() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$animateExitAndFinish$1
            @Override // java.lang.Runnable
            public void run() {
                ControlsFavoritingActivity.this.finish();
            }
        }).start();
    }

    private final void bindButtons() {
        View requireViewById = requireViewById(R$id.other_apps);
        final Button button = (Button) requireViewById;
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindButtons$1$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                View view2;
                view2 = ControlsFavoritingActivity.this.doneButton;
                if (view2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("doneButton");
                    throw null;
                }
                if (view2.isEnabled()) {
                    Toast.makeText(ControlsFavoritingActivity.this.getApplicationContext(), R$string.controls_favorite_toast_no_changes, 0).show();
                }
                ControlsFavoritingActivity.this.startActivity(new Intent(button.getContext(), ControlsProviderSelectorActivity.class), ActivityOptions.makeSceneTransitionAnimation(ControlsFavoritingActivity.this, new Pair[0]).toBundle());
                ControlsFavoritingActivity.this.animateExitAndFinish();
            }
        });
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<Button>(R.id.other_apps).apply {\n            setOnClickListener {\n                if (doneButton.isEnabled) {\n                    // The user has made changes\n                    Toast.makeText(\n                            applicationContext,\n                            R.string.controls_favorite_toast_no_changes,\n                            Toast.LENGTH_SHORT\n                            ).show()\n                }\n                startActivity(\n                    Intent(context, ControlsProviderSelectorActivity::class.java),\n                    ActivityOptions\n                        .makeSceneTransitionAnimation(this@ControlsFavoritingActivity).toBundle()\n                )\n                animateExitAndFinish()\n            }\n        }");
        this.otherAppsButton = requireViewById;
        View requireViewById2 = requireViewById(R$id.done);
        Button button2 = (Button) requireViewById2;
        button2.setEnabled(false);
        button2.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlsFavoritingActivity$bindButtons$2$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ComponentName componentName;
                List<StructureContainer> list;
                ControlsControllerImpl controlsControllerImpl;
                ComponentName componentName2;
                componentName = ControlsFavoritingActivity.this.component;
                if (componentName == null) {
                    return;
                }
                list = ControlsFavoritingActivity.this.listOfStructures;
                ControlsFavoritingActivity controlsFavoritingActivity = ControlsFavoritingActivity.this;
                for (StructureContainer structureContainer : list) {
                    List<ControlInfo> favorites = structureContainer.getModel().getFavorites();
                    controlsControllerImpl = controlsFavoritingActivity.controller;
                    componentName2 = controlsFavoritingActivity.component;
                    Intrinsics.checkNotNull(componentName2);
                    controlsControllerImpl.replaceFavoritesForStructure(new StructureInfo(componentName2, structureContainer.getStructureName(), favorites));
                }
                ControlsFavoritingActivity.this.animateExitAndFinish();
                ControlsFavoritingActivity.this.openControlsOrigin();
            }
        });
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById<Button>(R.id.done).apply {\n            isEnabled = false\n            setOnClickListener {\n                if (component == null) return@setOnClickListener\n                listOfStructures.forEach {\n                    val favoritesForStorage = it.model.favorites\n                    controller.replaceFavoritesForStructure(\n                        StructureInfo(component!!, it.structureName, favoritesForStorage)\n                    )\n                }\n                animateExitAndFinish()\n                openControlsOrigin()\n            }\n        }");
        this.doneButton = requireViewById2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void openControlsOrigin() {
        startActivity(new Intent(getApplicationContext(), ControlsActivity.class), ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager == null) {
            return;
        }
        tooltipManager.hide(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        this.listingController.addCallback(this.listingCallback);
        startTracking();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (!this.isPagerLoaded) {
            setUpPager();
            loadControls();
            this.isPagerLoaded = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        this.listingController.removeCallback(this.listingCallback);
        stopTracking();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        Intrinsics.checkNotNullParameter(newConfig, "newConfig");
        super.onConfigurationChanged(newConfig);
        TooltipManager tooltipManager = this.mTooltipManager;
        if (tooltipManager == null) {
            return;
        }
        tooltipManager.hide(false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
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
