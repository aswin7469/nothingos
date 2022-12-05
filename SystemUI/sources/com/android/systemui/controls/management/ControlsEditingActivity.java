package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.CustomIconCache;
import com.android.systemui.controls.controller.ControlInfo;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.StructureInfo;
import com.android.systemui.controls.management.FavoritesModel;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.util.LifecycleActivity;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsEditingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsEditingActivity extends LifecycleActivity {
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    private ComponentName component;
    @NotNull
    private final ControlsControllerImpl controller;
    @NotNull
    private final ControlsEditingActivity$currentUserTracker$1 currentUserTracker;
    @NotNull
    private final CustomIconCache customIconCache;
    @NotNull
    private final ControlsEditingActivity$favoritesModelCallback$1 favoritesModelCallback = new FavoritesModel.FavoritesModelCallback() { // from class: com.android.systemui.controls.management.ControlsEditingActivity$favoritesModelCallback$1
        @Override // com.android.systemui.controls.management.FavoritesModel.FavoritesModelCallback
        public void onNoneChanged(boolean z) {
            TextView textView;
            int i;
            TextView textView2;
            int i2;
            if (z) {
                textView2 = ControlsEditingActivity.this.subtitle;
                if (textView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("subtitle");
                    throw null;
                }
                i2 = ControlsEditingActivity.EMPTY_TEXT_ID;
                textView2.setText(i2);
                return;
            }
            textView = ControlsEditingActivity.this.subtitle;
            if (textView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("subtitle");
                throw null;
            }
            i = ControlsEditingActivity.SUBTITLE_ID;
            textView.setText(i);
        }

        @Override // com.android.systemui.controls.management.ControlsModel.ControlsModelCallback
        public void onFirstChange() {
            View view;
            view = ControlsEditingActivity.this.saveButton;
            if (view != null) {
                view.setEnabled(true);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("saveButton");
                throw null;
            }
        }
    };
    private FavoritesModel model;
    private View saveButton;
    private CharSequence structure;
    private TextView subtitle;
    @NotNull
    private final ControlsUiController uiController;
    @NotNull
    public static final Companion Companion = new Companion(null);
    private static final int SUBTITLE_ID = R$string.controls_favorite_rearrange;
    private static final int EMPTY_TEXT_ID = R$string.controls_favorite_removed;

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.controls.management.ControlsEditingActivity$currentUserTracker$1] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.controls.management.ControlsEditingActivity$favoritesModelCallback$1] */
    public ControlsEditingActivity(@NotNull ControlsControllerImpl controller, @NotNull final BroadcastDispatcher broadcastDispatcher, @NotNull CustomIconCache customIconCache, @NotNull ControlsUiController uiController) {
        Intrinsics.checkNotNullParameter(controller, "controller");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(customIconCache, "customIconCache");
        Intrinsics.checkNotNullParameter(uiController, "uiController");
        this.controller = controller;
        this.broadcastDispatcher = broadcastDispatcher;
        this.customIconCache = customIconCache;
        this.uiController = uiController;
        this.currentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.controls.management.ControlsEditingActivity$currentUserTracker$1
            private final int startingUser;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                ControlsControllerImpl controlsControllerImpl;
                controlsControllerImpl = ControlsEditingActivity.this.controller;
                this.startingUser = controlsControllerImpl.getCurrentUserId();
            }

            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                if (i != this.startingUser) {
                    stopTracking();
                    ControlsEditingActivity.this.finish();
                }
            }
        };
    }

    /* compiled from: ControlsEditingActivity.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onCreate(@Nullable Bundle bundle) {
        Unit unit;
        super.onCreate(bundle);
        ComponentName componentName = (ComponentName) getIntent().getParcelableExtra("android.intent.extra.COMPONENT_NAME");
        Unit unit2 = null;
        if (componentName == null) {
            unit = null;
        } else {
            this.component = componentName;
            unit = Unit.INSTANCE;
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        setUpList();
        startTracking();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        stopTracking();
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        animateExitAndFinish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void animateExitAndFinish() {
        ViewGroup rootView = (ViewGroup) requireViewById(R$id.controls_management_root);
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(rootView, "rootView");
        ControlsAnimations.exitAnimation(rootView, new Runnable() { // from class: com.android.systemui.controls.management.ControlsEditingActivity$animateExitAndFinish$1
            @Override // java.lang.Runnable
            public void run() {
                ControlsEditingActivity.this.finish();
            }
        }).start();
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
        viewStub.setLayoutResource(R$layout.controls_management_editing);
        viewStub.inflate();
        TextView textView = (TextView) requireViewById(R$id.title);
        CharSequence charSequence = this.structure;
        if (charSequence == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structure");
            throw null;
        }
        textView.setText(charSequence);
        CharSequence charSequence2 = this.structure;
        if (charSequence2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structure");
            throw null;
        }
        setTitle(charSequence2);
        View requireViewById2 = requireViewById(R$id.subtitle);
        TextView textView2 = (TextView) requireViewById2;
        textView2.setText(SUBTITLE_ID);
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById<TextView>(R.id.subtitle).apply {\n            setText(SUBTITLE_ID)\n        }");
        this.subtitle = textView2;
    }

    private final void bindButtons() {
        View requireViewById = requireViewById(R$id.done);
        Button button = (Button) requireViewById;
        button.setEnabled(false);
        button.setText(R$string.save);
        button.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlsEditingActivity$bindButtons$1$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlsEditingActivity.this.saveFavorites();
                ControlsEditingActivity.this.startActivity(new Intent(ControlsEditingActivity.this.getApplicationContext(), ControlsActivity.class), ActivityOptions.makeSceneTransitionAnimation(ControlsEditingActivity.this, new Pair[0]).toBundle());
                ControlsEditingActivity.this.animateExitAndFinish();
            }
        });
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<Button>(R.id.done).apply {\n            isEnabled = false\n            setText(R.string.save)\n            setOnClickListener {\n                saveFavorites()\n                startActivity(\n                    Intent(applicationContext, ControlsActivity::class.java),\n                    ActivityOptions\n                        .makeSceneTransitionAnimation(this@ControlsEditingActivity).toBundle()\n                )\n                animateExitAndFinish()\n            }\n        }");
        this.saveButton = requireViewById;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void saveFavorites() {
        ControlsControllerImpl controlsControllerImpl = this.controller;
        ComponentName componentName = this.component;
        if (componentName == null) {
            Intrinsics.throwUninitializedPropertyAccessException("component");
            throw null;
        }
        CharSequence charSequence = this.structure;
        if (charSequence == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structure");
            throw null;
        }
        FavoritesModel favoritesModel = this.model;
        if (favoritesModel != null) {
            controlsControllerImpl.replaceFavoritesForStructure(new StructureInfo(componentName, charSequence, favoritesModel.getFavorites()));
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("model");
            throw null;
        }
    }

    private final void setUpList() {
        ControlsControllerImpl controlsControllerImpl = this.controller;
        ComponentName componentName = this.component;
        if (componentName == null) {
            Intrinsics.throwUninitializedPropertyAccessException("component");
            throw null;
        }
        CharSequence charSequence = this.structure;
        if (charSequence == null) {
            Intrinsics.throwUninitializedPropertyAccessException("structure");
            throw null;
        }
        List<ControlInfo> favoritesForStructure = controlsControllerImpl.getFavoritesForStructure(componentName, charSequence);
        CustomIconCache customIconCache = this.customIconCache;
        ComponentName componentName2 = this.component;
        if (componentName2 != null) {
            this.model = new FavoritesModel(customIconCache, componentName2, favoritesForStructure, this.favoritesModelCallback);
            float f = getResources().getFloat(R$dimen.control_card_elevation);
            final RecyclerView recyclerView = (RecyclerView) requireViewById(R$id.list);
            recyclerView.setAlpha(0.0f);
            ControlAdapter controlAdapter = new ControlAdapter(f);
            controlAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() { // from class: com.android.systemui.controls.management.ControlsEditingActivity$setUpList$adapter$1$1
                private boolean hasAnimated;

                @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
                public void onChanged() {
                    if (!this.hasAnimated) {
                        this.hasAnimated = true;
                        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                        RecyclerView recyclerView2 = RecyclerView.this;
                        Intrinsics.checkNotNullExpressionValue(recyclerView2, "recyclerView");
                        controlsAnimations.enterAnimation(recyclerView2).start();
                    }
                }
            });
            int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.controls_card_margin);
            MarginItemDecorator marginItemDecorator = new MarginItemDecorator(dimensionPixelSize, dimensionPixelSize);
            recyclerView.setAdapter(controlAdapter);
            final Context context = recyclerView.getContext();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context) { // from class: com.android.systemui.controls.management.ControlsEditingActivity$setUpList$1$1
                @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
                public int getRowCountForAccessibility(@NotNull RecyclerView.Recycler recycler, @NotNull RecyclerView.State state) {
                    Intrinsics.checkNotNullParameter(recycler, "recycler");
                    Intrinsics.checkNotNullParameter(state, "state");
                    int rowCountForAccessibility = super.getRowCountForAccessibility(recycler, state);
                    return rowCountForAccessibility > 0 ? rowCountForAccessibility - 1 : rowCountForAccessibility;
                }
            };
            gridLayoutManager.setSpanSizeLookup(controlAdapter.getSpanSizeLookup());
            Unit unit = Unit.INSTANCE;
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addItemDecoration(marginItemDecorator);
            FavoritesModel favoritesModel = this.model;
            if (favoritesModel == null) {
                Intrinsics.throwUninitializedPropertyAccessException("model");
                throw null;
            }
            controlAdapter.changeModel(favoritesModel);
            FavoritesModel favoritesModel2 = this.model;
            if (favoritesModel2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("model");
                throw null;
            }
            favoritesModel2.attachAdapter(controlAdapter);
            FavoritesModel favoritesModel3 = this.model;
            if (favoritesModel3 != null) {
                new ItemTouchHelper(favoritesModel3.getItemTouchHelperCallback()).attachToRecyclerView(recyclerView);
                return;
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("model");
                throw null;
            }
        }
        Intrinsics.throwUninitializedPropertyAccessException("component");
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onDestroy() {
        stopTracking();
        super.onDestroy();
    }
}
