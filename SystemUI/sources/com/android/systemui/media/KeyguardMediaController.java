package com.android.systemui.media;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.stack.MediaContainerView;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.LargeScreenUtils;
import javax.inject.Inject;
import javax.inject.Named;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0011\n\u0002\u0010\b\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B9\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u0010\u0010(\u001a\u00020!2\b\u0010)\u001a\u0004\u0018\u00010\u0010J\u000e\u0010*\u001a\u00020!2\u0006\u0010+\u001a\u00020\u0015J\b\u0010,\u001a\u00020!H\u0002J\u0010\u0010-\u001a\u00020!2\u0006\u0010&\u001a\u00020\u0017H\u0002J\b\u0010.\u001a\u00020!H\u0002J\u0006\u0010/\u001a\u00020!J\u001a\u00100\u001a\u00020!2\b\u00101\u001a\u0004\u0018\u00010\u00152\u0006\u00102\u001a\u000203H\u0002J\b\u00104\u001a\u00020!H\u0002J\b\u00105\u001a\u00020!H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\"\u0010\u0011\u001a\u0004\u0018\u00010\u00102\b\u0010\u000f\u001a\u0004\u0018\u00010\u0010@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0015X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R,\u0010\u0018\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u00178\u0006@FX\u000e¢\u0006\u0014\n\u0000\u0012\u0004\b\u0019\u0010\u001a\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR(\u0010\u001f\u001a\u0010\u0012\u0004\u0012\u00020\u0017\u0012\u0004\u0012\u00020!\u0018\u00010 X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001e\u0010&\u001a\u00020\u00172\u0006\u0010\u000f\u001a\u00020\u0017@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u001c¨\u00066"}, mo65043d2 = {"Lcom/android/systemui/media/KeyguardMediaController;", "", "mediaHost", "Lcom/android/systemui/media/MediaHost;", "bypassController", "Lcom/android/systemui/statusbar/phone/KeyguardBypassController;", "statusBarStateController", "Lcom/android/systemui/statusbar/SysuiStatusBarStateController;", "notifLockscreenUserManager", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "context", "Landroid/content/Context;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "(Lcom/android/systemui/media/MediaHost;Lcom/android/systemui/statusbar/phone/KeyguardBypassController;Lcom/android/systemui/statusbar/SysuiStatusBarStateController;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Landroid/content/Context;Lcom/android/systemui/statusbar/policy/ConfigurationController;)V", "<set-?>", "Lcom/android/systemui/statusbar/notification/stack/MediaContainerView;", "singlePaneContainer", "getSinglePaneContainer", "()Lcom/android/systemui/statusbar/notification/stack/MediaContainerView;", "splitShadeContainer", "Landroid/view/ViewGroup;", "value", "", "useSplitShade", "getUseSplitShade$annotations", "()V", "getUseSplitShade", "()Z", "setUseSplitShade", "(Z)V", "visibilityChangedListener", "Lkotlin/Function1;", "", "getVisibilityChangedListener", "()Lkotlin/jvm/functions/Function1;", "setVisibilityChangedListener", "(Lkotlin/jvm/functions/Function1;)V", "visible", "getVisible", "attachSinglePaneContainer", "mediaView", "attachSplitShadeContainer", "container", "hideMediaPlayer", "onMediaHostVisibilityChanged", "reattachHostView", "refreshMediaPosition", "setVisibility", "view", "newVisibility", "", "showMediaPlayer", "updateResources", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardMediaController.kt */
public final class KeyguardMediaController {
    private final KeyguardBypassController bypassController;
    private final Context context;
    private final MediaHost mediaHost;
    private final NotificationLockscreenUserManager notifLockscreenUserManager;
    private MediaContainerView singlePaneContainer;
    private ViewGroup splitShadeContainer;
    private final SysuiStatusBarStateController statusBarStateController;
    private boolean useSplitShade;
    private Function1<? super Boolean, Unit> visibilityChangedListener;
    private boolean visible;

    public static /* synthetic */ void getUseSplitShade$annotations() {
    }

    @Inject
    public KeyguardMediaController(@Named("media_keyguard") MediaHost mediaHost2, KeyguardBypassController keyguardBypassController, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationLockscreenUserManager notificationLockscreenUserManager, Context context2, ConfigurationController configurationController) {
        Intrinsics.checkNotNullParameter(mediaHost2, "mediaHost");
        Intrinsics.checkNotNullParameter(keyguardBypassController, "bypassController");
        Intrinsics.checkNotNullParameter(sysuiStatusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "notifLockscreenUserManager");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        this.mediaHost = mediaHost2;
        this.bypassController = keyguardBypassController;
        this.statusBarStateController = sysuiStatusBarStateController;
        this.notifLockscreenUserManager = notificationLockscreenUserManager;
        this.context = context2;
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener(this) {
            final /* synthetic */ KeyguardMediaController this$0;

            {
                this.this$0 = r1;
            }

            public void onStateChanged(int i) {
                this.this$0.refreshMediaPosition();
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ KeyguardMediaController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateResources();
            }
        });
        mediaHost2.setExpansion(1.0f);
        mediaHost2.setShowsOnlyActiveMedia(true);
        mediaHost2.setFalsingProtectionNeeded(true);
        mediaHost2.init(2);
        updateResources();
    }

    /* access modifiers changed from: private */
    public final void updateResources() {
        Resources resources = this.context.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "context.resources");
        setUseSplitShade(LargeScreenUtils.shouldUseSplitNotificationShade(resources));
    }

    public final boolean getUseSplitShade() {
        return this.useSplitShade;
    }

    public final void setUseSplitShade(boolean z) {
        if (this.useSplitShade != z) {
            this.useSplitShade = z;
            reattachHostView();
            refreshMediaPosition();
        }
    }

    public final boolean getVisible() {
        return this.visible;
    }

    public final Function1<Boolean, Unit> getVisibilityChangedListener() {
        return this.visibilityChangedListener;
    }

    public final void setVisibilityChangedListener(Function1<? super Boolean, Unit> function1) {
        this.visibilityChangedListener = function1;
    }

    public final MediaContainerView getSinglePaneContainer() {
        return this.singlePaneContainer;
    }

    public final void attachSinglePaneContainer(MediaContainerView mediaContainerView) {
        boolean z = this.singlePaneContainer == null;
        this.singlePaneContainer = mediaContainerView;
        if (z) {
            this.mediaHost.addVisibilityChangeListener(new KeyguardMediaController$attachSinglePaneContainer$1(this));
        }
        reattachHostView();
        onMediaHostVisibilityChanged(this.mediaHost.getVisible());
    }

    /* access modifiers changed from: private */
    public final void onMediaHostVisibilityChanged(boolean z) {
        refreshMediaPosition();
        if (z) {
            ViewGroup.LayoutParams layoutParams = this.mediaHost.getHostView().getLayoutParams();
            layoutParams.height = -2;
            layoutParams.width = -1;
        }
    }

    public final void attachSplitShadeContainer(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "container");
        this.splitShadeContainer = viewGroup;
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
        boolean z = true;
        if (viewGroup != null && viewGroup.getChildCount() == 1) {
            viewGroup.removeAllViews();
        }
        if (viewGroup2 == null || viewGroup2.getChildCount() != 0) {
            z = false;
        }
        if (z) {
            ViewParent parent = this.mediaHost.getHostView().getParent();
            if (parent != null) {
                ViewGroup viewGroup3 = parent instanceof ViewGroup ? (ViewGroup) parent : null;
                if (viewGroup3 != null) {
                    viewGroup3.removeView(this.mediaHost.getHostView());
                }
            }
            viewGroup2.addView(this.mediaHost.getHostView());
        }
    }

    public final void refreshMediaPosition() {
        boolean z = false;
        boolean z2 = this.statusBarStateController.getState() == 1;
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
        Integer valueOf = viewGroup != null ? Integer.valueOf(viewGroup.getVisibility()) : null;
        if (viewGroup != null) {
            viewGroup.setVisibility(i);
        }
        if ((valueOf == null || valueOf.intValue() != i) && (function1 = this.visibilityChangedListener) != null) {
            function1.invoke(Boolean.valueOf(i == 0));
        }
    }
}
