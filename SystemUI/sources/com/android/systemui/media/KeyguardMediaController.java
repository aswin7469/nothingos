package com.android.systemui.media;

import android.content.Context;
import android.content.res.Configuration;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.stack.MediaHeaderView;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.Utils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: KeyguardMediaController.kt */
/* loaded from: classes.dex */
public final class KeyguardMediaController {
    @NotNull
    private final KeyguardBypassController bypassController;
    @NotNull
    private final Context context;
    @NotNull
    private final FeatureFlags featureFlags;
    @NotNull
    private final MediaHost mediaHost;
    @NotNull
    private final NotificationLockscreenUserManager notifLockscreenUserManager;
    @Nullable
    private MediaHeaderView singlePaneContainer;
    @Nullable
    private ViewGroup splitShadeContainer;
    @NotNull
    private final SysuiStatusBarStateController statusBarStateController;
    private boolean useSplitShade;
    @Nullable
    private Function1<? super Boolean, Unit> visibilityChangedListener;
    private boolean visible;

    public static /* synthetic */ void getUseSplitShade$annotations() {
    }

    public KeyguardMediaController(@NotNull MediaHost mediaHost, @NotNull KeyguardBypassController bypassController, @NotNull SysuiStatusBarStateController statusBarStateController, @NotNull NotificationLockscreenUserManager notifLockscreenUserManager, @NotNull FeatureFlags featureFlags, @NotNull Context context, @NotNull ConfigurationController configurationController) {
        Intrinsics.checkNotNullParameter(mediaHost, "mediaHost");
        Intrinsics.checkNotNullParameter(bypassController, "bypassController");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(notifLockscreenUserManager, "notifLockscreenUserManager");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        this.mediaHost = mediaHost;
        this.bypassController = bypassController;
        this.statusBarStateController = statusBarStateController;
        this.notifLockscreenUserManager = notifLockscreenUserManager;
        this.featureFlags = featureFlags;
        this.context = context;
        statusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.media.KeyguardMediaController.1
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                KeyguardMediaController.this.refreshMediaPosition();
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.media.KeyguardMediaController.2
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onConfigChanged(@Nullable Configuration configuration) {
                KeyguardMediaController.this.updateResources();
            }
        });
        mediaHost.setExpansion(0.0f);
        mediaHost.setShowsOnlyActiveMedia(true);
        mediaHost.setFalsingProtectionNeeded(true);
        mediaHost.init(2);
        updateResources();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateResources() {
        setUseSplitShade(Utils.shouldUseSplitNotificationShade(this.featureFlags, this.context.getResources()));
    }

    public final void setUseSplitShade(boolean z) {
        if (this.useSplitShade == z) {
            return;
        }
        this.useSplitShade = z;
        reattachHostView();
        refreshMediaPosition();
    }

    public final void setVisibilityChangedListener(@Nullable Function1<? super Boolean, Unit> function1) {
        this.visibilityChangedListener = function1;
    }

    @Nullable
    public final MediaHeaderView getSinglePaneContainer() {
        return this.singlePaneContainer;
    }

    public final void attachSinglePaneContainer(@Nullable MediaHeaderView mediaHeaderView) {
        boolean z = this.singlePaneContainer == null;
        this.singlePaneContainer = mediaHeaderView;
        if (z) {
            this.mediaHost.addVisibilityChangeListener(new KeyguardMediaController$attachSinglePaneContainer$1(this));
        }
        reattachHostView();
        onMediaHostVisibilityChanged(this.mediaHost.getVisible());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onMediaHostVisibilityChanged(boolean z) {
        refreshMediaPosition();
        if (z) {
            ViewGroup.LayoutParams layoutParams = this.mediaHost.getHostView().getLayoutParams();
            layoutParams.height = -2;
            layoutParams.width = -1;
        }
    }

    public final void attachSplitShadeContainer(@NotNull ViewGroup container) {
        Intrinsics.checkNotNullParameter(container, "container");
        this.splitShadeContainer = container;
        reattachHostView();
        refreshMediaPosition();
    }

    private final void reattachHostView() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        if (this.useSplitShade) {
            viewGroup2 = this.splitShadeContainer;
            viewGroup = this.singlePaneContainer;
        } else {
            viewGroup = this.splitShadeContainer;
            viewGroup2 = this.singlePaneContainer;
        }
        ViewGroup viewGroup3 = null;
        Integer valueOf = viewGroup == null ? null : Integer.valueOf(viewGroup.getChildCount());
        if (valueOf != null && valueOf.intValue() == 1) {
            viewGroup.removeAllViews();
        }
        Integer valueOf2 = viewGroup2 == null ? null : Integer.valueOf(viewGroup2.getChildCount());
        if (valueOf2 != null && valueOf2.intValue() == 0) {
            ViewParent parent = this.mediaHost.getHostView().getParent();
            if (parent != null) {
                if (parent instanceof ViewGroup) {
                    viewGroup3 = (ViewGroup) parent;
                }
                if (viewGroup3 != null) {
                    viewGroup3.removeView(this.mediaHost.getHostView());
                }
            }
            viewGroup2.addView(this.mediaHost.getHostView());
        }
    }

    public final void refreshMediaPosition() {
        boolean z = false;
        boolean z2 = this.statusBarStateController.getState() == 1 || this.statusBarStateController.getState() == 3;
        if (this.mediaHost.getVisible() && !this.bypassController.getBypassEnabled() && z2 && this.notifLockscreenUserManager.shouldShowLockscreenNotifications()) {
            z = true;
        }
        this.visible = z;
        if (z) {
            showMediaPlayer();
        } else {
            hideMediaPlayer();
        }
    }

    private final void showMediaPlayer() {
        if (this.useSplitShade) {
            setVisibility(this.splitShadeContainer, 0);
            setVisibility(this.singlePaneContainer, 8);
            return;
        }
        setVisibility(this.singlePaneContainer, 0);
        setVisibility(this.splitShadeContainer, 8);
    }

    private final void hideMediaPlayer() {
        setVisibility(this.splitShadeContainer, 8);
        setVisibility(this.singlePaneContainer, 8);
    }

    private final void setVisibility(ViewGroup viewGroup, int i) {
        Function1<? super Boolean, Unit> function1;
        Integer valueOf = viewGroup == null ? null : Integer.valueOf(viewGroup.getVisibility());
        if (viewGroup != null) {
            viewGroup.setVisibility(i);
        }
        if ((valueOf != null && valueOf.intValue() == i) || (function1 = this.visibilityChangedListener) == null) {
            return;
        }
        function1.mo1949invoke(Boolean.valueOf(i == 0));
    }
}
