package com.android.systemui.controls.p010ui;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.os.UserHandle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.management.ControlsAnimations;
import com.android.systemui.util.LifecycleActivity;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u000b\u001a\u00020\fH\u0002J\b\u0010\r\u001a\u00020\fH\u0016J\u0012\u0010\u000e\u001a\u00020\f2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010H\u0014J\b\u0010\u0011\u001a\u00020\fH\u0014J\b\u0010\u0012\u001a\u00020\fH\u0014J\b\u0010\u0013\u001a\u00020\fH\u0014R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0014"}, mo64987d2 = {"Lcom/android/systemui/controls/ui/ControlsActivity;", "Lcom/android/systemui/util/LifecycleActivity;", "uiController", "Lcom/android/systemui/controls/ui/ControlsUiController;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "(Lcom/android/systemui/controls/ui/ControlsUiController;Lcom/android/systemui/broadcast/BroadcastDispatcher;)V", "broadcastReceiver", "Landroid/content/BroadcastReceiver;", "parent", "Landroid/view/ViewGroup;", "initBroadcastReceiver", "", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onStart", "onStop", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlsActivity */
/* compiled from: ControlsActivity.kt */
public final class ControlsActivity extends LifecycleActivity {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private final BroadcastDispatcher broadcastDispatcher;
    private BroadcastReceiver broadcastReceiver;
    private ViewGroup parent;
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
    public ControlsActivity(ControlsUiController controlsUiController, BroadcastDispatcher broadcastDispatcher2) {
        Intrinsics.checkNotNullParameter(controlsUiController, "uiController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        this.uiController = controlsUiController;
        this.broadcastDispatcher = broadcastDispatcher2;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C1893R.layout.controls_fullscreen);
        Lifecycle lifecycle = getLifecycle();
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        View requireViewById = requireViewById(C1893R.C1897id.control_detail_root);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGrou…R.id.control_detail_root)");
        Window window = getWindow();
        Intrinsics.checkNotNullExpressionValue(window, "window");
        Intent intent = getIntent();
        Intrinsics.checkNotNullExpressionValue(intent, "intent");
        lifecycle.addObserver(controlsAnimations.observerForAnimations((ViewGroup) requireViewById, window, intent));
        ((ViewGroup) requireViewById(C1893R.C1897id.control_detail_root)).setOnApplyWindowInsetsListener(new ControlsActivity$$ExternalSyntheticLambda1());
        initBroadcastReceiver();
    }

    /* access modifiers changed from: private */
    /* renamed from: onCreate$lambda-2$lambda-1  reason: not valid java name */
    public static final WindowInsets m2701onCreate$lambda2$lambda1(View view, WindowInsets windowInsets) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        Intrinsics.checkNotNullParameter(windowInsets, "insets");
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsets.getInsets(WindowInsets.Type.systemBars()).bottom);
        return WindowInsets.CONSUMED;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        View requireViewById = requireViewById(C1893R.C1897id.global_actions_controls);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGrou….global_actions_controls)");
        ViewGroup viewGroup = (ViewGroup) requireViewById;
        this.parent = viewGroup;
        ViewGroup viewGroup2 = null;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup = null;
        }
        viewGroup.setAlpha(0.0f);
        ControlsUiController controlsUiController = this.uiController;
        ViewGroup viewGroup3 = this.parent;
        if (viewGroup3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            viewGroup3 = null;
        }
        controlsUiController.show(viewGroup3, new ControlsActivity$$ExternalSyntheticLambda0(this), this);
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        ViewGroup viewGroup4 = this.parent;
        if (viewGroup4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
        } else {
            viewGroup2 = viewGroup4;
        }
        controlsAnimations.enterAnimation(viewGroup2).start();
    }

    /* access modifiers changed from: private */
    /* renamed from: onStart$lambda-3  reason: not valid java name */
    public static final void m2702onStart$lambda3(ControlsActivity controlsActivity) {
        Intrinsics.checkNotNullParameter(controlsActivity, "this$0");
        controlsActivity.finish();
    }

    public void onBackPressed() {
        finish();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.uiController.hide();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        BroadcastDispatcher broadcastDispatcher2 = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        if (broadcastReceiver2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("broadcastReceiver");
            broadcastReceiver2 = null;
        }
        broadcastDispatcher2.unregisterReceiver(broadcastReceiver2);
    }

    private final void initBroadcastReceiver() {
        this.broadcastReceiver = new ControlsActivity$initBroadcastReceiver$1(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        BroadcastDispatcher broadcastDispatcher2 = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver2 = this.broadcastReceiver;
        if (broadcastReceiver2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("broadcastReceiver");
            broadcastReceiver2 = null;
        }
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, broadcastReceiver2, intentFilter, (Executor) null, (UserHandle) null, 0, (String) null, 60, (Object) null);
    }
}
