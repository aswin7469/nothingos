package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.ControlAdapter;
import com.android.systemui.controls.p010ui.ControlsActivity;
import com.android.systemui.controls.p010ui.ControlsUiController;
import com.android.systemui.util.LifecycleActivity;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0007*\u0002\u000e\u0011\u0018\u0000 (2\u00020\u0001:\u0001(B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\b\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u001cH\u0002J\b\u0010\u001e\u001a\u00020\u001cH\u0002J\b\u0010\u001f\u001a\u00020\u001cH\u0016J\u0012\u0010 \u001a\u00020\u001c2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0014J\b\u0010#\u001a\u00020\u001cH\u0014J\b\u0010$\u001a\u00020\u001cH\u0014J\b\u0010%\u001a\u00020\u001cH\u0014J\b\u0010&\u001a\u00020\u001cH\u0002J\b\u0010'\u001a\u00020\u001cH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0004\n\u0002\u0010\u000fR\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0004\n\u0002\u0010\u0012R\u000e\u0010\u0013\u001a\u00020\u0014X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX.¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006)"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlsEditingActivity;", "Lcom/android/systemui/util/LifecycleActivity;", "controller", "Lcom/android/systemui/controls/controller/ControlsControllerImpl;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "customIconCache", "Lcom/android/systemui/controls/CustomIconCache;", "uiController", "Lcom/android/systemui/controls/ui/ControlsUiController;", "(Lcom/android/systemui/controls/controller/ControlsControllerImpl;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/controls/CustomIconCache;Lcom/android/systemui/controls/ui/ControlsUiController;)V", "component", "Landroid/content/ComponentName;", "currentUserTracker", "com/android/systemui/controls/management/ControlsEditingActivity$currentUserTracker$1", "Lcom/android/systemui/controls/management/ControlsEditingActivity$currentUserTracker$1;", "favoritesModelCallback", "com/android/systemui/controls/management/ControlsEditingActivity$favoritesModelCallback$1", "Lcom/android/systemui/controls/management/ControlsEditingActivity$favoritesModelCallback$1;", "model", "Lcom/android/systemui/controls/management/FavoritesModel;", "saveButton", "Landroid/view/View;", "structure", "", "subtitle", "Landroid/widget/TextView;", "animateExitAndFinish", "", "bindButtons", "bindViews", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onStart", "onStop", "saveFavorites", "setUpList", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsEditingActivity.kt */
public final class ControlsEditingActivity extends LifecycleActivity {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final int EMPTY_TEXT_ID = C1893R.string.controls_favorite_removed;
    private static final String EXTRA_STRUCTURE = "extra_structure";
    /* access modifiers changed from: private */
    public static final int SUBTITLE_ID = C1893R.string.controls_favorite_rearrange;
    private static final String TAG = "ControlsEditingActivity";
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final BroadcastDispatcher broadcastDispatcher;
    private ComponentName component;
    /* access modifiers changed from: private */
    public final ControlsControllerImpl controller;
    private final ControlsEditingActivity$currentUserTracker$1 currentUserTracker;
    private final CustomIconCache customIconCache;
    private final ControlsEditingActivity$favoritesModelCallback$1 favoritesModelCallback;
    private FavoritesModel model;
    /* access modifiers changed from: private */
    public View saveButton;
    private CharSequence structure;
    /* access modifiers changed from: private */
    public TextView subtitle;
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
    public ControlsEditingActivity(ControlsControllerImpl controlsControllerImpl, BroadcastDispatcher broadcastDispatcher2, CustomIconCache customIconCache2, ControlsUiController controlsUiController) {
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "controller");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(customIconCache2, "customIconCache");
        Intrinsics.checkNotNullParameter(controlsUiController, "uiController");
        this.controller = controlsControllerImpl;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.customIconCache = customIconCache2;
        this.uiController = controlsUiController;
        this.currentUserTracker = new ControlsEditingActivity$currentUserTracker$1(this, broadcastDispatcher2);
        this.favoritesModelCallback = new ControlsEditingActivity$favoritesModelCallback$1(this);
    }

    @Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/controls/management/ControlsEditingActivity$Companion;", "", "()V", "EMPTY_TEXT_ID", "", "EXTRA_STRUCTURE", "", "SUBTITLE_ID", "TAG", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsEditingActivity.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        Unit unit;
        super.onCreate(bundle);
        ComponentName componentName = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        Unit unit2 = null;
        if (componentName != null) {
            this.component = componentName;
            unit = Unit.INSTANCE;
        } else {
            unit = null;
        }
        if (unit == null) {
            finish();
        }
        CharSequence charSequenceExtra = getIntent().getCharSequenceExtra("extra_structure");
        if (charSequenceExtra != null) {
            this.structure = charSequenceExtra;
            unit2 = Unit.INSTANCE;
        }
        if (unit2 == null) {
            finish();
        }
        bindViews();
        bindButtons();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        setUpList();
        this.currentUserTracker.startTracking();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.currentUserTracker.stopTracking();
    }

    public void onBackPressed() {
        animateExitAndFinish();
    }

    private final void animateExitAndFinish() {
        ViewGroup viewGroup = (ViewGroup) requireViewById(C1893R.C1897id.controls_management_root);
        Intrinsics.checkNotNullExpressionValue(viewGroup, "rootView");
        ControlsAnimations.exitAnimation(viewGroup, new ControlsEditingActivity$animateExitAndFinish$1(this)).start();
    }

    private final void bindViews() {
        setContentView(C1893R.layout.controls_management);
        Lifecycle lifecycle = getLifecycle();
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        View requireViewById = requireViewById(C1893R.C1897id.controls_management_root);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGrou…controls_management_root)");
        Window window = getWindow();
        Intrinsics.checkNotNullExpressionValue(window, "window");
        Intent intent = getIntent();
        Intrinsics.checkNotNullExpressionValue(intent, "intent");
        lifecycle.addObserver(controlsAnimations.observerForAnimations((ViewGroup) requireViewById, window, intent));
        ViewStub viewStub = (ViewStub) requireViewById(C1893R.C1897id.stub);
        viewStub.setLayoutResource(C1893R.layout.controls_management_editing);
        viewStub.inflate();
        TextView textView = (TextView) requireViewById(C1893R.C1897id.title);
        CharSequence charSequence = this.structure;
        CharSequence charSequence2 = null;
        if (charSequence == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structure");
            charSequence = null;
        }
        textView.setText(charSequence);
        CharSequence charSequence3 = this.structure;
        if (charSequence3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structure");
        } else {
            charSequence2 = charSequence3;
        }
        setTitle(charSequence2);
        View requireViewById2 = requireViewById(C1893R.C1897id.subtitle);
        TextView textView2 = (TextView) requireViewById2;
        textView2.setText(SUBTITLE_ID);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById<TextView…xt(SUBTITLE_ID)\n        }");
        this.subtitle = textView2;
    }

    private final void bindButtons() {
        View requireViewById = requireViewById(C1893R.C1897id.done);
        Button button = (Button) requireViewById;
        button.setEnabled(false);
        button.setText(C1893R.string.save);
        button.setOnClickListener(new ControlsEditingActivity$$ExternalSyntheticLambda0(this));
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<Button>(…)\n            }\n        }");
        this.saveButton = requireViewById;
    }

    /* access modifiers changed from: private */
    /* renamed from: bindButtons$lambda-6$lambda-5  reason: not valid java name */
    public static final void m2638bindButtons$lambda6$lambda5(ControlsEditingActivity controlsEditingActivity, View view) {
        Intrinsics.checkNotNullParameter(controlsEditingActivity, "this$0");
        controlsEditingActivity.saveFavorites();
        controlsEditingActivity.startActivity(new Intent(controlsEditingActivity.getApplicationContext(), ControlsActivity.class), ActivityOptions.makeSceneTransitionAnimation(controlsEditingActivity, new Pair[0]).toBundle());
        controlsEditingActivity.animateExitAndFinish();
    }

    private final void saveFavorites() {
        ControlsControllerImpl controlsControllerImpl = this.controller;
        ComponentName componentName = this.component;
        FavoritesModel favoritesModel = null;
        if (componentName == null) {
            Intrinsics.throwUninitializedPropertyAccessException("component");
            componentName = null;
        }
        CharSequence charSequence = this.structure;
        if (charSequence == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structure");
            charSequence = null;
        }
        FavoritesModel favoritesModel2 = this.model;
        if (favoritesModel2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("model");
        } else {
            favoritesModel = favoritesModel2;
        }
        controlsControllerImpl.replaceFavoritesForStructure(new StructureInfo(componentName, charSequence, favoritesModel.getFavorites()));
    }

    private final void setUpList() {
        ControlsControllerImpl controlsControllerImpl = this.controller;
        ComponentName componentName = this.component;
        FavoritesModel favoritesModel = null;
        if (componentName == null) {
            Intrinsics.throwUninitializedPropertyAccessException("component");
            componentName = null;
        }
        CharSequence charSequence = this.structure;
        if (charSequence == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structure");
            charSequence = null;
        }
        List<ControlInfo> favoritesForStructure = controlsControllerImpl.getFavoritesForStructure(componentName, charSequence);
        CustomIconCache customIconCache2 = this.customIconCache;
        ComponentName componentName2 = this.component;
        if (componentName2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("component");
            componentName2 = null;
        }
        this.model = new FavoritesModel(customIconCache2, componentName2, favoritesForStructure, this.favoritesModelCallback);
        float f = getResources().getFloat(C1893R.dimen.control_card_elevation);
        RecyclerView recyclerView = (RecyclerView) requireViewById(C1893R.C1897id.list);
        recyclerView.setAlpha(0.0f);
        ControlAdapter controlAdapter = new ControlAdapter(f);
        controlAdapter.registerAdapterDataObserver(new ControlsEditingActivity$setUpList$adapter$1$1(recyclerView));
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1893R.dimen.controls_card_margin);
        MarginItemDecorator marginItemDecorator = new MarginItemDecorator(dimensionPixelSize, dimensionPixelSize);
        ControlAdapter.Companion companion = ControlAdapter.Companion;
        Resources resources = getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "resources");
        int findMaxColumns = companion.findMaxColumns(resources);
        RecyclerView.Adapter adapter = controlAdapter;
        recyclerView.setAdapter(adapter);
        ControlsEditingActivity$setUpList$1$1 controlsEditingActivity$setUpList$1$1 = new ControlsEditingActivity$setUpList$1$1(findMaxColumns, recyclerView.getContext());
        controlsEditingActivity$setUpList$1$1.setSpanSizeLookup(new ControlsEditingActivity$setUpList$1$2$1(controlAdapter, findMaxColumns));
        recyclerView.setLayoutManager(controlsEditingActivity$setUpList$1$1);
        recyclerView.addItemDecoration(marginItemDecorator);
        FavoritesModel favoritesModel2 = this.model;
        if (favoritesModel2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("model");
            favoritesModel2 = null;
        }
        controlAdapter.changeModel(favoritesModel2);
        FavoritesModel favoritesModel3 = this.model;
        if (favoritesModel3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("model");
            favoritesModel3 = null;
        }
        favoritesModel3.attachAdapter(adapter);
        FavoritesModel favoritesModel4 = this.model;
        if (favoritesModel4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("model");
        } else {
            favoritesModel = favoritesModel4;
        }
        new ItemTouchHelper(favoritesModel.getItemTouchHelperCallback()).attachToRecyclerView(recyclerView);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.currentUserTracker.stopTracking();
        super.onDestroy();
    }
}
