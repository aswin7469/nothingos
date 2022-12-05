package com.android.systemui.controls.management;

import android.app.ActivityOptions;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.ui.ControlsActivity;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.util.LifecycleActivity;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsProviderSelectorActivity.kt */
/* loaded from: classes.dex */
public final class ControlsProviderSelectorActivity extends LifecycleActivity {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final Executor backExecutor;
    private boolean backShouldExit;
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    @NotNull
    private final ControlsController controlsController;
    @NotNull
    private final ControlsProviderSelectorActivity$currentUserTracker$1 currentUserTracker;
    @NotNull
    private final Executor executor;
    @NotNull
    private final ControlsListingController listingController;
    private RecyclerView recyclerView;
    @NotNull
    private final ControlsUiController uiController;

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.controls.management.ControlsProviderSelectorActivity$currentUserTracker$1] */
    public ControlsProviderSelectorActivity(@NotNull Executor executor, @NotNull Executor backExecutor, @NotNull ControlsListingController listingController, @NotNull ControlsController controlsController, @NotNull final BroadcastDispatcher broadcastDispatcher, @NotNull ControlsUiController uiController) {
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(backExecutor, "backExecutor");
        Intrinsics.checkNotNullParameter(listingController, "listingController");
        Intrinsics.checkNotNullParameter(controlsController, "controlsController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(uiController, "uiController");
        this.executor = executor;
        this.backExecutor = backExecutor;
        this.listingController = listingController;
        this.controlsController = controlsController;
        this.broadcastDispatcher = broadcastDispatcher;
        this.uiController = uiController;
        this.currentUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$currentUserTracker$1
            private final int startingUser;

            /* JADX INFO: Access modifiers changed from: package-private */
            {
                ControlsListingController controlsListingController;
                controlsListingController = ControlsProviderSelectorActivity.this.listingController;
                this.startingUser = controlsListingController.getCurrentUserId();
            }

            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                if (i != this.startingUser) {
                    stopTracking();
                    ControlsProviderSelectorActivity.this.finish();
                }
            }
        };
    }

    /* compiled from: ControlsProviderSelectorActivity.kt */
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
        super.onCreate(bundle);
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
        viewStub.setLayoutResource(R$layout.controls_management_apps);
        viewStub.inflate();
        View requireViewById2 = requireViewById(R$id.list);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(R.id.list)");
        RecyclerView recyclerView = (RecyclerView) requireViewById2;
        this.recyclerView = recyclerView;
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            TextView textView = (TextView) requireViewById(R$id.title);
            textView.setText(textView.getResources().getText(R$string.controls_providers_title));
            Button button = (Button) requireViewById(R$id.other_apps);
            button.setVisibility(0);
            button.setText(17039360);
            button.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$onCreate$3$1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ControlsProviderSelectorActivity.this.onBackPressed();
                }
            });
            requireViewById(R$id.done).setVisibility(8);
            this.backShouldExit = getIntent().getBooleanExtra("back_should_exit", false);
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
        throw null;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (!this.backShouldExit) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getApplicationContext(), ControlsActivity.class));
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
        }
        animateExitAndFinish();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        startTracking();
        RecyclerView recyclerView = this.recyclerView;
        if (recyclerView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            throw null;
        }
        recyclerView.setAlpha(0.0f);
        RecyclerView recyclerView2 = this.recyclerView;
        if (recyclerView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            throw null;
        }
        Executor executor = this.backExecutor;
        Executor executor2 = this.executor;
        Lifecycle mo1437getLifecycle = mo1437getLifecycle();
        ControlsListingController controlsListingController = this.listingController;
        LayoutInflater from = LayoutInflater.from(this);
        Intrinsics.checkNotNullExpressionValue(from, "from(this)");
        ControlsProviderSelectorActivity$onStart$1 controlsProviderSelectorActivity$onStart$1 = new ControlsProviderSelectorActivity$onStart$1(this);
        Resources resources = getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "resources");
        FavoritesRenderer favoritesRenderer = new FavoritesRenderer(resources, new ControlsProviderSelectorActivity$onStart$2(this.controlsController));
        Resources resources2 = getResources();
        Intrinsics.checkNotNullExpressionValue(resources2, "resources");
        AppAdapter appAdapter = new AppAdapter(executor, executor2, mo1437getLifecycle, controlsListingController, from, controlsProviderSelectorActivity$onStart$1, favoritesRenderer, resources2);
        appAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$onStart$3$1
            private boolean hasAnimated;

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                RecyclerView recyclerView3;
                if (!this.hasAnimated) {
                    this.hasAnimated = true;
                    ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
                    recyclerView3 = ControlsProviderSelectorActivity.this.recyclerView;
                    if (recyclerView3 != null) {
                        controlsAnimations.enterAnimation(recyclerView3).start();
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
                        throw null;
                    }
                }
            }
        });
        Unit unit = Unit.INSTANCE;
        recyclerView2.setAdapter(appAdapter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        stopTracking();
    }

    public final void launchFavoritingActivity(@Nullable final ComponentName componentName) {
        this.executor.execute(new Runnable() { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$launchFavoritingActivity$1
            @Override // java.lang.Runnable
            public final void run() {
                ControlsListingController controlsListingController;
                ComponentName componentName2 = componentName;
                if (componentName2 == null) {
                    return;
                }
                ControlsProviderSelectorActivity controlsProviderSelectorActivity = this;
                Intent intent = new Intent(controlsProviderSelectorActivity.getApplicationContext(), ControlsFavoritingActivity.class);
                controlsListingController = controlsProviderSelectorActivity.listingController;
                intent.putExtra("extra_app_label", controlsListingController.getAppLabel(componentName2));
                intent.putExtra("android.intent.extra.COMPONENT_NAME", componentName2);
                intent.putExtra("extra_from_provider_selector", true);
                controlsProviderSelectorActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(controlsProviderSelectorActivity, new Pair[0]).toBundle());
                controlsProviderSelectorActivity.animateExitAndFinish();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onDestroy() {
        stopTracking();
        super.onDestroy();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void animateExitAndFinish() {
        ViewGroup rootView = (ViewGroup) requireViewById(R$id.controls_management_root);
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(rootView, "rootView");
        ControlsAnimations.exitAnimation(rootView, new Runnable() { // from class: com.android.systemui.controls.management.ControlsProviderSelectorActivity$animateExitAndFinish$1
            @Override // java.lang.Runnable
            public void run() {
                ControlsProviderSelectorActivity.this.finish();
            }
        }).start();
    }
}
