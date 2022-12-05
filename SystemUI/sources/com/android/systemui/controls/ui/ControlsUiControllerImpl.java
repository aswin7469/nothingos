package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.Space;
import android.widget.TextView;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$integer;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlsEditingActivity;
import com.android.systemui.controls.management.ControlsFavoritingActivity;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.globalactions.GlobalActionsPopupMenu;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlin.ranges.RangesKt___RangesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsUiControllerImpl.kt */
/* loaded from: classes.dex */
public final class ControlsUiControllerImpl implements ControlsUiController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private static final ComponentName EMPTY_COMPONENT;
    @NotNull
    private static final StructureInfo EMPTY_STRUCTURE;
    private Context activityContext;
    @NotNull
    private final ActivityStarter activityStarter;
    private List<StructureInfo> allStructures;
    @NotNull
    private final DelayableExecutor bgExecutor;
    private final Collator collator;
    @NotNull
    private final Context context;
    @NotNull
    private final ControlActionCoordinator controlActionCoordinator;
    @NotNull
    private final Lazy<ControlsController> controlsController;
    @NotNull
    private final Lazy<ControlsListingController> controlsListingController;
    @NotNull
    private final ControlsMetricsLogger controlsMetricsLogger;
    @NotNull
    private final CustomIconCache iconCache;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    private ControlsListingController.ControlsListingCallback listingCallback;
    @NotNull
    private final Comparator<SelectionItem> localeComparator;
    private Runnable onDismiss;
    private ViewGroup parent;
    @Nullable
    private ListPopupWindow popup;
    @NotNull
    private final ContextThemeWrapper popupThemedContext;
    private boolean retainCache;
    @NotNull
    private final ShadeController shadeController;
    @NotNull
    private final SharedPreferences sharedPreferences;
    @NotNull
    private final DelayableExecutor uiExecutor;
    @NotNull
    private StructureInfo selectedStructure = EMPTY_STRUCTURE;
    @NotNull
    private final Map<ControlKey, ControlWithState> controlsById = new LinkedHashMap();
    @NotNull
    private final Map<ControlKey, ControlViewHolder> controlViewsById = new LinkedHashMap();
    private boolean hidden = true;
    @NotNull
    private final Consumer<Boolean> onSeedingComplete = new Consumer<Boolean>() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$onSeedingComplete$1
        @Override // java.util.function.Consumer
        public final void accept(Boolean accepted) {
            ViewGroup viewGroup;
            Object next;
            StructureInfo structureInfo;
            Intrinsics.checkNotNullExpressionValue(accepted, "accepted");
            if (accepted.booleanValue()) {
                ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                Iterator<T> it = controlsUiControllerImpl.getControlsController().get().getFavorites().iterator();
                if (!it.hasNext()) {
                    next = null;
                } else {
                    next = it.next();
                    if (it.hasNext()) {
                        int size = ((StructureInfo) next).getControls().size();
                        do {
                            Object next2 = it.next();
                            int size2 = ((StructureInfo) next2).getControls().size();
                            if (size < size2) {
                                next = next2;
                                size = size2;
                            }
                        } while (it.hasNext());
                    }
                }
                StructureInfo structureInfo2 = (StructureInfo) next;
                if (structureInfo2 == null) {
                    structureInfo2 = ControlsUiControllerImpl.EMPTY_STRUCTURE;
                }
                controlsUiControllerImpl.selectedStructure = structureInfo2;
                ControlsUiControllerImpl controlsUiControllerImpl2 = ControlsUiControllerImpl.this;
                structureInfo = controlsUiControllerImpl2.selectedStructure;
                controlsUiControllerImpl2.updatePreferences(structureInfo);
            }
            ControlsUiControllerImpl controlsUiControllerImpl3 = ControlsUiControllerImpl.this;
            viewGroup = controlsUiControllerImpl3.parent;
            if (viewGroup != null) {
                controlsUiControllerImpl3.reload(viewGroup);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
        }
    };

    public ControlsUiControllerImpl(@NotNull Lazy<ControlsController> controlsController, @NotNull Context context, @NotNull DelayableExecutor uiExecutor, @NotNull DelayableExecutor bgExecutor, @NotNull Lazy<ControlsListingController> controlsListingController, @NotNull SharedPreferences sharedPreferences, @NotNull ControlActionCoordinator controlActionCoordinator, @NotNull ActivityStarter activityStarter, @NotNull ShadeController shadeController, @NotNull CustomIconCache iconCache, @NotNull ControlsMetricsLogger controlsMetricsLogger, @NotNull KeyguardStateController keyguardStateController) {
        Intrinsics.checkNotNullParameter(controlsController, "controlsController");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uiExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(bgExecutor, "bgExecutor");
        Intrinsics.checkNotNullParameter(controlsListingController, "controlsListingController");
        Intrinsics.checkNotNullParameter(sharedPreferences, "sharedPreferences");
        Intrinsics.checkNotNullParameter(controlActionCoordinator, "controlActionCoordinator");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(shadeController, "shadeController");
        Intrinsics.checkNotNullParameter(iconCache, "iconCache");
        Intrinsics.checkNotNullParameter(controlsMetricsLogger, "controlsMetricsLogger");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        this.controlsController = controlsController;
        this.context = context;
        this.uiExecutor = uiExecutor;
        this.bgExecutor = bgExecutor;
        this.controlsListingController = controlsListingController;
        this.sharedPreferences = sharedPreferences;
        this.controlActionCoordinator = controlActionCoordinator;
        this.activityStarter = activityStarter;
        this.shadeController = shadeController;
        this.iconCache = iconCache;
        this.controlsMetricsLogger = controlsMetricsLogger;
        this.keyguardStateController = keyguardStateController;
        this.popupThemedContext = new ContextThemeWrapper(context, R$style.Control_ListPopupWindow);
        final Collator collator = Collator.getInstance(context.getResources().getConfiguration().getLocales().get(0));
        this.collator = collator;
        Intrinsics.checkNotNullExpressionValue(collator, "collator");
        this.localeComparator = new Comparator<T>() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$special$$inlined$compareBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                return collator.compare(((SelectionItem) t).getTitle(), ((SelectionItem) t2).getTitle());
            }
        };
    }

    @NotNull
    public final Lazy<ControlsController> getControlsController() {
        return this.controlsController;
    }

    @NotNull
    public final DelayableExecutor getUiExecutor() {
        return this.uiExecutor;
    }

    @NotNull
    public final DelayableExecutor getBgExecutor() {
        return this.bgExecutor;
    }

    @NotNull
    public final Lazy<ControlsListingController> getControlsListingController() {
        return this.controlsListingController;
    }

    @NotNull
    public final ControlActionCoordinator getControlActionCoordinator() {
        return this.controlActionCoordinator;
    }

    /* compiled from: ControlsUiControllerImpl.kt */
    /* loaded from: classes.dex */
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

    private final ControlsListingController.ControlsListingCallback createCallback(final Function1<? super List<SelectionItem>, Unit> function1) {
        return new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1
            @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
            public void onServicesUpdated(@NotNull List<ControlsServiceInfo> serviceInfos) {
                int collectionSizeOrDefault;
                Intrinsics.checkNotNullParameter(serviceInfos, "serviceInfos");
                collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(serviceInfos, 10);
                final ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
                for (ControlsServiceInfo controlsServiceInfo : serviceInfos) {
                    int i = controlsServiceInfo.getServiceInfo().applicationInfo.uid;
                    CharSequence loadLabel = controlsServiceInfo.loadLabel();
                    Intrinsics.checkNotNullExpressionValue(loadLabel, "it.loadLabel()");
                    Drawable loadIcon = controlsServiceInfo.loadIcon();
                    Intrinsics.checkNotNullExpressionValue(loadIcon, "it.loadIcon()");
                    ComponentName componentName = controlsServiceInfo.componentName;
                    Intrinsics.checkNotNullExpressionValue(componentName, "it.componentName");
                    arrayList.add(new SelectionItem(loadLabel, "", loadIcon, componentName, i));
                }
                DelayableExecutor uiExecutor = ControlsUiControllerImpl.this.getUiExecutor();
                final ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                final Function1<List<SelectionItem>, Unit> function12 = function1;
                uiExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1$onServicesUpdated$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ViewGroup viewGroup;
                        viewGroup = ControlsUiControllerImpl.this.parent;
                        if (viewGroup == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("parent");
                            throw null;
                        }
                        viewGroup.removeAllViews();
                        if (arrayList.size() <= 0) {
                            return;
                        }
                        function12.mo1949invoke(arrayList);
                    }
                });
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.systemui.controls.ui.ControlsUiController
    public void show(@NotNull ViewGroup parent, @NotNull Runnable onDismiss, @NotNull Context activityContext) {
        int collectionSizeOrDefault;
        Intrinsics.checkNotNullParameter(parent, "parent");
        Intrinsics.checkNotNullParameter(onDismiss, "onDismiss");
        Intrinsics.checkNotNullParameter(activityContext, "activityContext");
        Log.d("ControlsUiController", "show()");
        this.parent = parent;
        this.onDismiss = onDismiss;
        this.activityContext = activityContext;
        this.hidden = false;
        this.retainCache = false;
        this.controlActionCoordinator.setActivityContext(activityContext);
        List<StructureInfo> favorites = this.controlsController.get().getFavorites();
        this.allStructures = favorites;
        if (favorites != null) {
            this.selectedStructure = getPreferredStructure(favorites);
            if (this.controlsController.get().addSeedingFavoritesCallback(this.onSeedingComplete)) {
                this.listingCallback = createCallback(new ControlsUiControllerImpl$show$1(this));
            } else {
                if (this.selectedStructure.getControls().isEmpty()) {
                    List<StructureInfo> list = this.allStructures;
                    if (list == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("allStructures");
                        throw null;
                    } else if (list.size() <= 1) {
                        this.listingCallback = createCallback(new ControlsUiControllerImpl$show$2(this));
                    }
                }
                List<ControlInfo> controls = this.selectedStructure.getControls();
                collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(controls, 10);
                ArrayList arrayList = new ArrayList(collectionSizeOrDefault);
                for (ControlInfo controlInfo : controls) {
                    arrayList.add(new ControlWithState(this.selectedStructure.getComponentName(), controlInfo, null));
                }
                Map<ControlKey, ControlWithState> map = this.controlsById;
                for (Object obj : arrayList) {
                    map.put(new ControlKey(this.selectedStructure.getComponentName(), ((ControlWithState) obj).getCi().getControlId()), obj);
                }
                this.listingCallback = createCallback(new ControlsUiControllerImpl$show$5(this));
                this.controlsController.get().subscribeToFavorites(this.selectedStructure);
            }
            ControlsListingController controlsListingController = this.controlsListingController.get();
            ControlsListingController.ControlsListingCallback controlsListingCallback = this.listingCallback;
            if (controlsListingCallback != null) {
                controlsListingController.addCallback(controlsListingCallback);
                return;
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("listingCallback");
                throw null;
            }
        }
        Intrinsics.throwUninitializedPropertyAccessException("allStructures");
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void reload(final ViewGroup viewGroup) {
        if (this.hidden) {
            return;
        }
        ControlsListingController controlsListingController = this.controlsListingController.get();
        ControlsListingController.ControlsListingCallback controlsListingCallback = this.listingCallback;
        if (controlsListingCallback == null) {
            Intrinsics.throwUninitializedPropertyAccessException("listingCallback");
            throw null;
        }
        controlsListingController.removeCallback(controlsListingCallback);
        this.controlsController.get().unsubscribe();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(viewGroup, "alpha", 1.0f, 0.0f);
        ofFloat.setInterpolator(new AccelerateInterpolator(1.0f));
        ofFloat.setDuration(200L);
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$reload$1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(@NotNull Animator animation) {
                Map map;
                Map map2;
                Runnable runnable;
                Context context;
                Intrinsics.checkNotNullParameter(animation, "animation");
                map = ControlsUiControllerImpl.this.controlViewsById;
                map.clear();
                map2 = ControlsUiControllerImpl.this.controlsById;
                map2.clear();
                ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                ViewGroup viewGroup2 = viewGroup;
                runnable = controlsUiControllerImpl.onDismiss;
                if (runnable == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("onDismiss");
                    throw null;
                }
                context = ControlsUiControllerImpl.this.activityContext;
                if (context == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("activityContext");
                    throw null;
                }
                controlsUiControllerImpl.show(viewGroup2, runnable, context);
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(viewGroup, "alpha", 0.0f, 1.0f);
                ofFloat2.setInterpolator(new DecelerateInterpolator(1.0f));
                ofFloat2.setDuration(200L);
                ofFloat2.start();
            }
        });
        ofFloat.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showSeedingView(List<SelectionItem> list) {
        LayoutInflater from = LayoutInflater.from(this.context);
        int i = R$layout.controls_no_favorites;
        ViewGroup viewGroup = this.parent;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        from.inflate(i, viewGroup, true);
        ViewGroup viewGroup2 = this.parent;
        if (viewGroup2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        } else {
            ((TextView) viewGroup2.requireViewById(R$id.controls_subtitle)).setText(this.context.getResources().getString(R$string.controls_seeding_in_progress));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showInitialSetupView(List<SelectionItem> list) {
        startProviderSelectorActivity();
        Runnable runnable = this.onDismiss;
        if (runnable != null) {
            runnable.run();
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("onDismiss");
            throw null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void startFavoritingActivity(StructureInfo structureInfo) {
        startTargetedActivity(structureInfo, ControlsFavoritingActivity.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void startEditingActivity(StructureInfo structureInfo) {
        startTargetedActivity(structureInfo, ControlsEditingActivity.class);
    }

    private final void startTargetedActivity(StructureInfo structureInfo, Class<?> cls) {
        Context context = this.activityContext;
        if (context != null) {
            Intent intent = new Intent(context, cls);
            putIntentExtras(intent, structureInfo);
            startActivity(intent);
            this.retainCache = true;
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("activityContext");
        throw null;
    }

    private final void putIntentExtras(Intent intent, StructureInfo structureInfo) {
        intent.putExtra("extra_app_label", getControlsListingController().get().getAppLabel(structureInfo.getComponentName()));
        intent.putExtra("extra_structure", structureInfo.getStructure());
        intent.putExtra("android.intent.extra.COMPONENT_NAME", structureInfo.getComponentName());
    }

    private final void startProviderSelectorActivity() {
        Context context = this.activityContext;
        if (context != null) {
            Intent intent = new Intent(context, ControlsProviderSelectorActivity.class);
            intent.putExtra("back_should_exit", true);
            startActivity(intent);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("activityContext");
        throw null;
    }

    private final SelectionItem findSelectionItem(StructureInfo structureInfo, List<SelectionItem> list) {
        Object obj;
        boolean z;
        Iterator<T> it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            SelectionItem selectionItem = (SelectionItem) obj;
            if (!Intrinsics.areEqual(selectionItem.getComponentName(), structureInfo.getComponentName()) || !Intrinsics.areEqual(selectionItem.getStructure(), structureInfo.getStructure())) {
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

    private final void startActivity(Intent intent) {
        intent.putExtra("extra_animate", true);
        if (this.keyguardStateController.isShowing()) {
            this.activityStarter.postStartActivityDismissingKeyguard(intent, 0);
            return;
        }
        Context context = this.activityContext;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("activityContext");
            throw null;
        } else if (context != null) {
            context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, new Pair[0]).toBundle());
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("activityContext");
            throw null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void showControlsView(List<SelectionItem> list) {
        int collectionSizeOrDefault;
        int mapCapacity;
        int coerceAtLeast;
        this.controlViewsById.clear();
        collectionSizeOrDefault = CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10);
        mapCapacity = MapsKt__MapsJVMKt.mapCapacity(collectionSizeOrDefault);
        coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(mapCapacity, 16);
        LinkedHashMap linkedHashMap = new LinkedHashMap(coerceAtLeast);
        for (Object obj : list) {
            linkedHashMap.put(((SelectionItem) obj).getComponentName(), obj);
        }
        ArrayList arrayList = new ArrayList();
        List<StructureInfo> list2 = this.allStructures;
        if (list2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("allStructures");
            throw null;
        }
        for (StructureInfo structureInfo : list2) {
            SelectionItem selectionItem = (SelectionItem) linkedHashMap.get(structureInfo.getComponentName());
            SelectionItem copy$default = selectionItem == null ? null : SelectionItem.copy$default(selectionItem, null, structureInfo.getStructure(), null, null, 0, 29, null);
            if (copy$default != null) {
                arrayList.add(copy$default);
            }
        }
        CollectionsKt__MutableCollectionsJVMKt.sortWith(arrayList, this.localeComparator);
        SelectionItem findSelectionItem = findSelectionItem(this.selectedStructure, arrayList);
        if (findSelectionItem == null) {
            findSelectionItem = list.get(0);
        }
        this.controlsMetricsLogger.refreshBegin(findSelectionItem.getUid(), !this.keyguardStateController.isUnlocked());
        createListView(findSelectionItem);
        createDropDown(arrayList, findSelectionItem);
        createMenu();
    }

    /* JADX WARN: Type inference failed for: r2v4, types: [android.widget.ArrayAdapter, T] */
    private final void createMenu() {
        String[] strArr = {this.context.getResources().getString(R$string.controls_menu_add), this.context.getResources().getString(R$string.controls_menu_edit)};
        final Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        ref$ObjectRef.element = new ArrayAdapter(this.context, R$layout.controls_more_item, strArr);
        ViewGroup viewGroup = this.parent;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        final ImageView imageView = (ImageView) viewGroup.requireViewById(R$id.controls_more);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1
            @Override // android.view.View.OnClickListener
            public void onClick(@NotNull View v) {
                ContextThemeWrapper contextThemeWrapper;
                Intrinsics.checkNotNullParameter(v, "v");
                ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                contextThemeWrapper = ControlsUiControllerImpl.this.popupThemedContext;
                final GlobalActionsPopupMenu globalActionsPopupMenu = new GlobalActionsPopupMenu(contextThemeWrapper, false);
                ImageView imageView2 = imageView;
                Ref$ObjectRef<ArrayAdapter<String>> ref$ObjectRef2 = ref$ObjectRef;
                final ControlsUiControllerImpl controlsUiControllerImpl2 = ControlsUiControllerImpl.this;
                globalActionsPopupMenu.setAnchorView(imageView2);
                globalActionsPopupMenu.setAdapter(ref$ObjectRef2.element);
                globalActionsPopupMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createMenu$1$onClick$1$1
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(@NotNull AdapterView<?> parent, @NotNull View view, int i, long j) {
                        StructureInfo structureInfo;
                        StructureInfo structureInfo2;
                        Intrinsics.checkNotNullParameter(parent, "parent");
                        Intrinsics.checkNotNullParameter(view, "view");
                        if (i == 0) {
                            ControlsUiControllerImpl controlsUiControllerImpl3 = ControlsUiControllerImpl.this;
                            structureInfo = controlsUiControllerImpl3.selectedStructure;
                            controlsUiControllerImpl3.startFavoritingActivity(structureInfo);
                        } else if (i == 1) {
                            ControlsUiControllerImpl controlsUiControllerImpl4 = ControlsUiControllerImpl.this;
                            structureInfo2 = controlsUiControllerImpl4.selectedStructure;
                            controlsUiControllerImpl4.startEditingActivity(structureInfo2);
                        }
                        globalActionsPopupMenu.dismiss();
                    }
                });
                globalActionsPopupMenu.show();
                Unit unit = Unit.INSTANCE;
                controlsUiControllerImpl.popup = globalActionsPopupMenu;
            }
        });
    }

    private final void createListView(SelectionItem selectionItem) {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        int i = R$layout.controls_with_favorites;
        ViewGroup viewGroup = this.parent;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        inflater.inflate(i, viewGroup, true);
        ViewGroup viewGroup2 = this.parent;
        if (viewGroup2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        ImageView imageView = (ImageView) viewGroup2.requireViewById(R$id.controls_close);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createListView$1$1
            @Override // android.view.View.OnClickListener
            public final void onClick(@NotNull View noName_0) {
                Runnable runnable;
                Intrinsics.checkNotNullParameter(noName_0, "$noName_0");
                runnable = ControlsUiControllerImpl.this.onDismiss;
                if (runnable != null) {
                    runnable.run();
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("onDismiss");
                    throw null;
                }
            }
        });
        imageView.setVisibility(0);
        int findMaxColumns = findMaxColumns();
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        View requireViewById = viewGroup3.requireViewById(R$id.global_actions_controls_list);
        Objects.requireNonNull(requireViewById, "null cannot be cast to non-null type android.view.ViewGroup");
        ViewGroup viewGroup4 = (ViewGroup) requireViewById;
        Intrinsics.checkNotNullExpressionValue(inflater, "inflater");
        ViewGroup createRow = createRow(inflater, viewGroup4);
        for (ControlInfo controlInfo : this.selectedStructure.getControls()) {
            ControlKey controlKey = new ControlKey(this.selectedStructure.getComponentName(), controlInfo.getControlId());
            ControlWithState controlWithState = this.controlsById.get(controlKey);
            if (controlWithState != null) {
                if (createRow.getChildCount() == findMaxColumns) {
                    createRow = createRow(inflater, viewGroup4);
                }
                View inflate = inflater.inflate(R$layout.controls_base_item, createRow, false);
                Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
                ViewGroup viewGroup5 = (ViewGroup) inflate;
                createRow.addView(viewGroup5);
                if (createRow.getChildCount() == 1) {
                    ViewGroup.LayoutParams layoutParams = viewGroup5.getLayoutParams();
                    Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                    ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(0);
                }
                ControlsController controlsController = getControlsController().get();
                Intrinsics.checkNotNullExpressionValue(controlsController, "controlsController.get()");
                ControlViewHolder controlViewHolder = new ControlViewHolder(viewGroup5, controlsController, getUiExecutor(), getBgExecutor(), getControlActionCoordinator(), this.controlsMetricsLogger, selectionItem.getUid());
                controlViewHolder.bindData(controlWithState, false);
                this.controlViewsById.put(controlKey, controlViewHolder);
            }
        }
        int size = this.selectedStructure.getControls().size() % findMaxColumns;
        int dimensionPixelSize = this.context.getResources().getDimensionPixelSize(R$dimen.control_spacing);
        for (int i2 = size == 0 ? 0 : findMaxColumns - size; i2 > 0; i2--) {
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(0, 0, 1.0f);
            layoutParams2.setMarginStart(dimensionPixelSize);
            createRow.addView(new Space(this.context), layoutParams2);
        }
    }

    private final int findMaxColumns() {
        int i;
        Resources resources = this.context.getResources();
        int integer = resources.getInteger(R$integer.controls_max_columns);
        int integer2 = resources.getInteger(R$integer.controls_max_columns_adjust_below_width_dp);
        TypedValue typedValue = new TypedValue();
        boolean z = true;
        resources.getValue(R$dimen.controls_max_columns_adjust_above_font_scale, typedValue, true);
        float f = typedValue.getFloat();
        Configuration configuration = resources.getConfiguration();
        if (configuration.orientation != 1) {
            z = false;
        }
        return (!z || (i = configuration.screenWidthDp) == 0 || i > integer2 || configuration.fontScale < f) ? integer : integer - 1;
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    @NotNull
    public StructureInfo getPreferredStructure(@NotNull List<StructureInfo> structures) {
        boolean z;
        Intrinsics.checkNotNullParameter(structures, "structures");
        if (structures.isEmpty()) {
            return EMPTY_STRUCTURE;
        }
        Object obj = null;
        String string = this.sharedPreferences.getString("controls_component", null);
        ComponentName unflattenFromString = string == null ? null : ComponentName.unflattenFromString(string);
        if (unflattenFromString == null) {
            unflattenFromString = EMPTY_COMPONENT;
        }
        String string2 = this.sharedPreferences.getString("controls_structure", "");
        Iterator<T> it = structures.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            StructureInfo structureInfo = (StructureInfo) next;
            if (!Intrinsics.areEqual(unflattenFromString, structureInfo.getComponentName()) || !Intrinsics.areEqual(string2, structureInfo.getStructure())) {
                z = false;
                continue;
            } else {
                z = true;
                continue;
            }
            if (z) {
                obj = next;
                break;
            }
        }
        StructureInfo structureInfo2 = (StructureInfo) obj;
        return structureInfo2 == null ? structures.get(0) : structureInfo2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updatePreferences(StructureInfo structureInfo) {
        if (Intrinsics.areEqual(structureInfo, EMPTY_STRUCTURE)) {
            return;
        }
        this.sharedPreferences.edit().putString("controls_component", structureInfo.getComponentName().flattenToString()).putString("controls_structure", structureInfo.getStructure().toString()).commit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void switchAppOrStructure(SelectionItem selectionItem) {
        boolean z;
        List<StructureInfo> list = this.allStructures;
        if (list != null) {
            for (StructureInfo structureInfo : list) {
                if (!Intrinsics.areEqual(structureInfo.getStructure(), selectionItem.getStructure()) || !Intrinsics.areEqual(structureInfo.getComponentName(), selectionItem.getComponentName())) {
                    z = false;
                    continue;
                } else {
                    z = true;
                    continue;
                }
                if (z) {
                    if (Intrinsics.areEqual(structureInfo, this.selectedStructure)) {
                        return;
                    }
                    this.selectedStructure = structureInfo;
                    updatePreferences(structureInfo);
                    ViewGroup viewGroup = this.parent;
                    if (viewGroup != null) {
                        reload(viewGroup);
                        return;
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("parent");
                        throw null;
                    }
                }
            }
            throw new NoSuchElementException("Collection contains no element matching the predicate.");
        }
        Intrinsics.throwUninitializedPropertyAccessException("allStructures");
        throw null;
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
        for (Map.Entry<ControlKey, ControlViewHolder> entry : this.controlViewsById.entrySet()) {
            entry.getValue().dismiss();
        }
        this.controlActionCoordinator.closeDialogs();
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public void hide() {
        this.hidden = true;
        closeDialogs(true);
        this.controlsController.get().unsubscribe();
        ViewGroup viewGroup = this.parent;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        viewGroup.removeAllViews();
        this.controlsById.clear();
        this.controlViewsById.clear();
        ControlsListingController controlsListingController = this.controlsListingController.get();
        ControlsListingController.ControlsListingCallback controlsListingCallback = this.listingCallback;
        if (controlsListingCallback == null) {
            Intrinsics.throwUninitializedPropertyAccessException("listingCallback");
            throw null;
        }
        controlsListingController.removeCallback(controlsListingCallback);
        if (this.retainCache) {
            return;
        }
        RenderInfo.Companion.clearCache();
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public void onRefreshState(@NotNull ComponentName componentName, @NotNull List<Control> controls) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(controls, "controls");
        final boolean z = !this.keyguardStateController.isUnlocked();
        for (Control control : controls) {
            Map<ControlKey, ControlWithState> map = this.controlsById;
            String controlId = control.getControlId();
            Intrinsics.checkNotNullExpressionValue(controlId, "c.getControlId()");
            ControlWithState controlWithState = map.get(new ControlKey(componentName, controlId));
            if (controlWithState != null) {
                Log.d("ControlsUiController", Intrinsics.stringPlus("onRefreshState() for id: ", control.getControlId()));
                CustomIconCache customIconCache = this.iconCache;
                String controlId2 = control.getControlId();
                Intrinsics.checkNotNullExpressionValue(controlId2, "c.controlId");
                customIconCache.store(componentName, controlId2, control.getCustomIcon());
                final ControlWithState controlWithState2 = new ControlWithState(componentName, controlWithState.getCi(), control);
                String controlId3 = control.getControlId();
                Intrinsics.checkNotNullExpressionValue(controlId3, "c.getControlId()");
                ControlKey controlKey = new ControlKey(componentName, controlId3);
                this.controlsById.put(controlKey, controlWithState2);
                final ControlViewHolder controlViewHolder = this.controlViewsById.get(controlKey);
                if (controlViewHolder != null) {
                    getUiExecutor().execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$onRefreshState$1$1$1$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ControlViewHolder.this.bindData(controlWithState2, z);
                        }
                    });
                }
            }
        }
    }

    @Override // com.android.systemui.controls.ui.ControlsUiController
    public void onActionResponse(@NotNull ComponentName componentName, @NotNull String controlId, final int i) {
        Intrinsics.checkNotNullParameter(componentName, "componentName");
        Intrinsics.checkNotNullParameter(controlId, "controlId");
        final ControlKey controlKey = new ControlKey(componentName, controlId);
        this.uiExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$onActionResponse$1
            @Override // java.lang.Runnable
            public final void run() {
                Map map;
                map = ControlsUiControllerImpl.this.controlViewsById;
                ControlViewHolder controlViewHolder = (ControlViewHolder) map.get(controlKey);
                if (controlViewHolder == null) {
                    return;
                }
                controlViewHolder.actionResponse(i);
            }
        });
    }

    private final ViewGroup createRow(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R$layout.controls_row, viewGroup, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        ViewGroup viewGroup2 = (ViewGroup) inflate;
        viewGroup.addView(viewGroup2);
        return viewGroup2;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.controls.ui.ItemAdapter, android.widget.ArrayAdapter, T] */
    private final void createDropDown(List<SelectionItem> list, SelectionItem selectionItem) {
        for (SelectionItem selectionItem2 : list) {
            RenderInfo.Companion.registerComponentIcon(selectionItem2.getComponentName(), selectionItem2.getIcon());
        }
        final Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        ?? itemAdapter = new ItemAdapter(this.context, R$layout.controls_spinner_item);
        itemAdapter.addAll(list);
        Unit unit = Unit.INSTANCE;
        ref$ObjectRef.element = itemAdapter;
        ViewGroup viewGroup = this.parent;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        TextView textView = (TextView) viewGroup.requireViewById(R$id.app_or_structure_spinner);
        textView.setText(selectionItem.getTitle());
        Drawable background = textView.getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        ((LayerDrawable) background).getDrawable(0).setTint(textView.getContext().getResources().getColor(R$color.control_spinner_dropdown, null));
        if (list.size() == 1) {
            textView.setBackground(null);
            return;
        }
        ViewGroup viewGroup2 = this.parent;
        if (viewGroup2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            throw null;
        }
        final ViewGroup viewGroup3 = (ViewGroup) viewGroup2.requireViewById(R$id.controls_header);
        viewGroup3.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createDropDown$2
            @Override // android.view.View.OnClickListener
            public void onClick(@NotNull View v) {
                ContextThemeWrapper contextThemeWrapper;
                Intrinsics.checkNotNullParameter(v, "v");
                ControlsUiControllerImpl controlsUiControllerImpl = ControlsUiControllerImpl.this;
                contextThemeWrapper = ControlsUiControllerImpl.this.popupThemedContext;
                final GlobalActionsPopupMenu globalActionsPopupMenu = new GlobalActionsPopupMenu(contextThemeWrapper, true);
                ViewGroup viewGroup4 = viewGroup3;
                Ref$ObjectRef<ItemAdapter> ref$ObjectRef2 = ref$ObjectRef;
                final ControlsUiControllerImpl controlsUiControllerImpl2 = ControlsUiControllerImpl.this;
                globalActionsPopupMenu.setAnchorView(viewGroup4);
                globalActionsPopupMenu.setAdapter(ref$ObjectRef2.element);
                globalActionsPopupMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.android.systemui.controls.ui.ControlsUiControllerImpl$createDropDown$2$onClick$1$1
                    @Override // android.widget.AdapterView.OnItemClickListener
                    public void onItemClick(@NotNull AdapterView<?> parent, @NotNull View view, int i, long j) {
                        Intrinsics.checkNotNullParameter(parent, "parent");
                        Intrinsics.checkNotNullParameter(view, "view");
                        Object itemAtPosition = parent.getItemAtPosition(i);
                        Objects.requireNonNull(itemAtPosition, "null cannot be cast to non-null type com.android.systemui.controls.ui.SelectionItem");
                        ControlsUiControllerImpl.this.switchAppOrStructure((SelectionItem) itemAtPosition);
                        globalActionsPopupMenu.dismiss();
                    }
                });
                globalActionsPopupMenu.show();
                Unit unit2 = Unit.INSTANCE;
                controlsUiControllerImpl.popup = globalActionsPopupMenu;
            }
        });
    }
}
