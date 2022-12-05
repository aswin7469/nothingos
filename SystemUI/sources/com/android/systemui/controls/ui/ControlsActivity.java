package com.android.systemui.controls.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.android.settingslib.core.lifecycle.Lifecycle;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.controls.management.ControlsAnimations;
import com.android.systemui.util.LifecycleActivity;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ControlsActivity.kt */
/* loaded from: classes.dex */
public final class ControlsActivity extends LifecycleActivity {
    @NotNull
    private final BroadcastDispatcher broadcastDispatcher;
    private BroadcastReceiver broadcastReceiver;
    private ViewGroup parent;
    @NotNull
    private final ControlsUiController uiController;

    public ControlsActivity(@NotNull ControlsUiController uiController, @NotNull BroadcastDispatcher broadcastDispatcher) {
        Intrinsics.checkNotNullParameter(uiController, "uiController");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        this.uiController = uiController;
        this.broadcastDispatcher = broadcastDispatcher;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.controls_fullscreen);
        Lifecycle mo1437getLifecycle = mo1437getLifecycle();
        ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
        int i = R$id.control_detail_root;
        View requireViewById = requireViewById(i);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGroup>(R.id.control_detail_root)");
        Window window = getWindow();
        Intrinsics.checkNotNullExpressionValue(window, "window");
        Intent intent = getIntent();
        Intrinsics.checkNotNullExpressionValue(intent, "intent");
        mo1437getLifecycle.addObserver(controlsAnimations.observerForAnimations((ViewGroup) requireViewById, window, intent));
        ((ViewGroup) requireViewById(i)).setOnApplyWindowInsetsListener(ControlsActivity$onCreate$1$1.INSTANCE);
        initBroadcastReceiver();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        View requireViewById = requireViewById(R$id.global_actions_controls);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById<ViewGroup>(R.id.global_actions_controls)");
        ViewGroup viewGroup = (ViewGroup) requireViewById;
        this.parent = viewGroup;
        if (viewGroup != null) {
            viewGroup.setAlpha(0.0f);
            ControlsUiController controlsUiController = this.uiController;
            ViewGroup viewGroup2 = this.parent;
            if (viewGroup2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
            controlsUiController.show(viewGroup2, new Runnable() { // from class: com.android.systemui.controls.ui.ControlsActivity$onResume$1
                @Override // java.lang.Runnable
                public final void run() {
                    ControlsActivity.this.finish();
                }
            }, this);
            ControlsAnimations controlsAnimations = ControlsAnimations.INSTANCE;
            ViewGroup viewGroup3 = this.parent;
            if (viewGroup3 != null) {
                controlsAnimations.enterAnimation(viewGroup3).start();
                return;
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
                throw null;
            }
        }
        Intrinsics.throwUninitializedPropertyAccessException("parent");
        throw null;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        finish();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.uiController.hide();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.util.LifecycleActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        BroadcastDispatcher broadcastDispatcher = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver = this.broadcastReceiver;
        if (broadcastReceiver != null) {
            broadcastDispatcher.unregisterReceiver(broadcastReceiver);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("broadcastReceiver");
            throw null;
        }
    }

    private final void initBroadcastReceiver() {
        this.broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.controls.ui.ControlsActivity$initBroadcastReceiver$1
            @Override // android.content.BroadcastReceiver
            public void onReceive(@NotNull Context context, @NotNull Intent intent) {
                Intrinsics.checkNotNullParameter(context, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                    ControlsActivity.this.finish();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.SCREEN_OFF");
        BroadcastDispatcher broadcastDispatcher = this.broadcastDispatcher;
        BroadcastReceiver broadcastReceiver = this.broadcastReceiver;
        if (broadcastReceiver != null) {
            BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, broadcastReceiver, intentFilter, null, null, 12, null);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("broadcastReceiver");
            throw null;
        }
    }
}
