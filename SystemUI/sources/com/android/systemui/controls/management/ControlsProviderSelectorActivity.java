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
import com.android.systemui.C1894R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.p010ui.ControlsActivity;
import com.android.systemui.controls.p010ui.ControlsUiController;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.LifecycleActivity;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000W\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005*\u0001\u0011\u0018\u0000 !2\u00020\u0001:\u0001!B;\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ\b\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u00162\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019J\b\u0010\u001a\u001a\u00020\u0016H\u0016J\u0012\u0010\u001b\u001a\u00020\u00162\b\u0010\u001c\u001a\u0004\u0018\u00010\u001dH\u0014J\b\u0010\u001e\u001a\u00020\u0016H\u0014J\b\u0010\u001f\u001a\u00020\u0016H\u0014J\b\u0010 \u001a\u00020\u0016H\u0014R\u000e\u0010\u0004\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0004\n\u0002\u0010\u0012R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsProviderSelectorActivity;", "Lcom/android/systemui/util/LifecycleActivity;", "executor", "Ljava/util/concurrent/Executor;", "backExecutor", "listingController", "Lcom/android/systemui/controls/management/ControlsListingController;", "controlsController", "Lcom/android/systemui/controls/controller/ControlsController;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "uiController", "Lcom/android/systemui/controls/ui/ControlsUiController;", "(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lcom/android/systemui/controls/management/ControlsListingController;Lcom/android/systemui/controls/controller/ControlsController;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/controls/ui/ControlsUiController;)V", "backShouldExit", "", "currentUserTracker", "com/android/systemui/controls/management/ControlsProviderSelectorActivity$currentUserTracker$1", "Lcom/android/systemui/controls/management/ControlsProviderSelectorActivity$currentUserTracker$1;", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "animateExitAndFinish", "", "launchFavoritingActivity", "component", "Landroid/content/ComponentName;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onStart", "onStop", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsProviderSelectorActivity.kt */
public final class ControlsProviderSelectorActivity extends LifecycleActivity {
    public static final String BACK_SHOULD_EXIT = "back_should_exit";
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "ControlsProviderSelectorActivity";
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final Executor backExecutor;
    private boolean backShouldExit;
    private final BroadcastDispatcher broadcastDispatcher;
    private final ControlsController controlsController;
    private final ControlsProviderSelectorActivity$currentUserTracker$1 currentUserTracker;
    private final Executor executor;
    /* access modifiers changed from: private */
    public final ControlsListingController listingController;
    /* access modifiers changed from: private */
    public RecyclerView recyclerView;
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
    public ControlsProviderSelectorActivity(@Main Executor executor2, @Background Executor executor3, ControlsListingController controlsListingController, ControlsController controlsController2, BroadcastDispatcher broadcastDispatcher2, ControlsUiController controlsUiController) {
        Intrinsics.checkNotNullParameter(executor2, "executor");
        Intrinsics.checkNotNullParameter(executor3, "backExecutor");
        Intrinsics.checkNotNullParameter(controlsListingController, "listingController");
        Intrinsics.checkNotNullParameter(controlsController2, "controlsController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(controlsUiController, "uiController");
        this.executor = executor2;
        this.backExecutor = executor3;
        this.listingController = controlsListingController;
        this.controlsController = controlsController2;
        this.broadcastDispatcher = broadcastDispatcher2;
        this.uiController = controlsUiController;
        this.currentUserTracker = new ControlsProviderSelectorActivity$currentUserTracker$1(this, broadcastDispatcher2);
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0006"}, mo65043d2 = {"Lcom/android/systemui/controls/management/ControlsProviderSelectorActivity$Companion;", "", "()V", "BACK_SHOULD_EXIT", "", "TAG", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlsProviderSelectorActivity.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
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
        viewStub.setLayoutResource(C1894R.layout.controls_management_apps);
        viewStub.inflate();
        View requireViewById2 = requireViewById(C1894R.C1898id.list);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(R.id.list)");
        RecyclerView recyclerView2 = (RecyclerView) requireViewById2;
        this.recyclerView = recyclerView2;
        if (recyclerView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            recyclerView2 = null;
        }
        recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        TextView textView = (TextView) requireViewById(C1894R.C1898id.title);
        textView.setText(textView.getResources().getText(C1894R.string.controls_providers_title));
        Button button = (Button) requireViewById(C1894R.C1898id.other_apps);
        button.setVisibility(0);
        button.setText(17039360);
        button.setOnClickListener(new ControlsProviderSelectorActivity$$ExternalSyntheticLambda1(this));
        requireViewById(C1894R.C1898id.done).setVisibility(8);
        this.backShouldExit = getIntent().getBooleanExtra(BACK_SHOULD_EXIT, false);
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-3$lambda-2  reason: not valid java name */
    public static final void m2664onCreate$lambda3$lambda2(ControlsProviderSelectorActivity controlsProviderSelectorActivity, View view) {
        Intrinsics.checkNotNullParameter(controlsProviderSelectorActivity, "this$0");
        controlsProviderSelectorActivity.onBackPressed();
    }

    public void onBackPressed() {
        if (!this.backShouldExit) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getApplicationContext(), ControlsActivity.class));
            startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, new Pair[0]).toBundle());
        }
        animateExitAndFinish();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.currentUserTracker.startTracking();
        RecyclerView recyclerView2 = this.recyclerView;
        RecyclerView recyclerView3 = null;
        if (recyclerView2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
            recyclerView2 = null;
        }
        recyclerView2.setAlpha(0.0f);
        RecyclerView recyclerView4 = this.recyclerView;
        if (recyclerView4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("recyclerView");
        } else {
            recyclerView3 = recyclerView4;
        }
        ControlsListingController controlsListingController = this.listingController;
        LayoutInflater from = LayoutInflater.from(this);
        Intrinsics.checkNotNullExpressionValue(from, "from(this)");
        Resources resources = getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "resources");
        FavoritesRenderer favoritesRenderer = new FavoritesRenderer(resources, new ControlsProviderSelectorActivity$onStart$2(this.controlsController));
        Resources resources2 = getResources();
        Intrinsics.checkNotNullExpressionValue(resources2, "resources");
        AppAdapter appAdapter = new AppAdapter(this.backExecutor, this.executor, getLifecycle(), controlsListingController, from, new ControlsProviderSelectorActivity$onStart$1(this), favoritesRenderer, resources2);
        appAdapter.registerAdapterDataObserver(new ControlsProviderSelectorActivity$onStart$3$1(this));
        recyclerView3.setAdapter(appAdapter);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.currentUserTracker.stopTracking();
    }

    public final void launchFavoritingActivity(ComponentName componentName) {
        this.executor.execute(new ControlsProviderSelectorActivity$$ExternalSyntheticLambda0(componentName, this));
    }

    /* access modifiers changed from: private */
    /* renamed from: launchFavoritingActivity$lambda-8  reason: not valid java name */
    public static final void m2663launchFavoritingActivity$lambda8(ComponentName componentName, ControlsProviderSelectorActivity controlsProviderSelectorActivity) {
        Intrinsics.checkNotNullParameter(controlsProviderSelectorActivity, "this$0");
        if (componentName != null) {
            Intent intent = new Intent(controlsProviderSelectorActivity.getApplicationContext(), ControlsFavoritingActivity.class);
            intent.putExtra(ControlsFavoritingActivity.EXTRA_APP, controlsProviderSelectorActivity.listingController.getAppLabel(componentName));
            intent.putExtra("android.intent.extra.COMPONENT_NAME", componentName);
            intent.putExtra(ControlsFavoritingActivity.EXTRA_FROM_PROVIDER_SELECTOR, true);
            controlsProviderSelectorActivity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(controlsProviderSelectorActivity, new Pair[0]).toBundle());
            controlsProviderSelectorActivity.animateExitAndFinish();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.currentUserTracker.stopTracking();
        super.onDestroy();
    }

    private final void animateExitAndFinish() {
        ViewGroup viewGroup = (ViewGroup) requireViewById(C1894R.C1898id.controls_management_root);
        Intrinsics.checkNotNullExpressionValue(viewGroup, "rootView");
        ControlsAnimations.exitAnimation(viewGroup, new ControlsProviderSelectorActivity$animateExitAndFinish$1(this)).start();
    }
}
