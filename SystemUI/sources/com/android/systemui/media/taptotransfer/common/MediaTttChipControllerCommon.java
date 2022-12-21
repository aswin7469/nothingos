package com.android.systemui.media.taptotransfer.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.widget.CachingIconView;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.media.taptotransfer.common.ChipInfoCommon;
import com.android.systemui.statusbar.gesture.TapGestureDetector;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.view.ViewUtil;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0004\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003BG\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\b\b\u0001\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013¢\u0006\u0002\u0010\u0014J\u0013\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00028\u0000¢\u0006\u0002\u0010\"J\u0012\u0010#\u001a\u00020$2\b\u0010%\u001a\u0004\u0018\u00010&H\u0002J\u0017\u0010'\u001a\u0004\u0018\u00010\u00132\u0006\u0010(\u001a\u00020)H\u0016¢\u0006\u0002\u0010*J\u0010\u0010+\u001a\u00020 2\u0006\u0010,\u001a\u00020-H\u0002J\u0010\u0010.\u001a\u00020 2\u0006\u0010/\u001a\u00020&H\u0016J7\u00100\u001a\u00020 2\u0006\u00101\u001a\u00020\u00182\b\u0010%\u001a\u0004\u0018\u00010&2\n\b\u0002\u00102\u001a\u0004\u0018\u0001032\n\b\u0002\u00104\u001a\u0004\u0018\u000105H\u0000¢\u0006\u0002\b6J\u001d\u00107\u001a\u00020 2\u0006\u0010!\u001a\u00028\u00002\u0006\u00101\u001a\u00020\u0018H&¢\u0006\u0002\u00108R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0018X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0014\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001d\u001a\u00020\u001e8\u0002X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u00069"}, mo64987d2 = {"Lcom/android/systemui/media/taptotransfer/common/MediaTttChipControllerCommon;", "T", "Lcom/android/systemui/media/taptotransfer/common/ChipInfoCommon;", "", "context", "Landroid/content/Context;", "logger", "Lcom/android/systemui/media/taptotransfer/common/MediaTttLogger;", "windowManager", "Landroid/view/WindowManager;", "viewUtil", "Lcom/android/systemui/util/view/ViewUtil;", "mainExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "tapGestureDetector", "Lcom/android/systemui/statusbar/gesture/TapGestureDetector;", "powerManager", "Landroid/os/PowerManager;", "chipLayoutRes", "", "(Landroid/content/Context;Lcom/android/systemui/media/taptotransfer/common/MediaTttLogger;Landroid/view/WindowManager;Lcom/android/systemui/util/view/ViewUtil;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/statusbar/gesture/TapGestureDetector;Landroid/os/PowerManager;I)V", "cancelChipViewTimeout", "Ljava/lang/Runnable;", "chipView", "Landroid/view/ViewGroup;", "getContext$SystemUI_nothingRelease", "()Landroid/content/Context;", "getLogger$SystemUI_nothingRelease", "()Lcom/android/systemui/media/taptotransfer/common/MediaTttLogger;", "windowLayoutParams", "Landroid/view/WindowManager$LayoutParams;", "displayChip", "", "chipInfo", "(Lcom/android/systemui/media/taptotransfer/common/ChipInfoCommon;)V", "getIconInfo", "Lcom/android/systemui/media/taptotransfer/common/IconInfo;", "appPackageName", "", "getIconSize", "isAppIcon", "", "(Z)Ljava/lang/Integer;", "onScreenTapped", "e", "Landroid/view/MotionEvent;", "removeChip", "removalReason", "setIcon", "currentChipView", "appIconDrawableOverride", "Landroid/graphics/drawable/Drawable;", "appNameOverride", "", "setIcon$SystemUI_nothingRelease", "updateChipView", "(Lcom/android/systemui/media/taptotransfer/common/ChipInfoCommon;Landroid/view/ViewGroup;)V", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaTttChipControllerCommon.kt */
public abstract class MediaTttChipControllerCommon<T extends ChipInfoCommon> {
    private Runnable cancelChipViewTimeout;
    private final int chipLayoutRes;
    private ViewGroup chipView;
    private final Context context;
    private final MediaTttLogger logger;
    private final DelayableExecutor mainExecutor;
    private final PowerManager powerManager;
    private final TapGestureDetector tapGestureDetector;
    private final ViewUtil viewUtil;
    private final WindowManager.LayoutParams windowLayoutParams;
    private final WindowManager windowManager;

    public Integer getIconSize(boolean z) {
        return null;
    }

    public abstract void updateChipView(T t, ViewGroup viewGroup);

    public MediaTttChipControllerCommon(Context context2, MediaTttLogger mediaTttLogger, WindowManager windowManager2, ViewUtil viewUtil2, @Main DelayableExecutor delayableExecutor, TapGestureDetector tapGestureDetector2, PowerManager powerManager2, int i) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(mediaTttLogger, "logger");
        Intrinsics.checkNotNullParameter(windowManager2, "windowManager");
        Intrinsics.checkNotNullParameter(viewUtil2, "viewUtil");
        Intrinsics.checkNotNullParameter(delayableExecutor, "mainExecutor");
        Intrinsics.checkNotNullParameter(tapGestureDetector2, "tapGestureDetector");
        Intrinsics.checkNotNullParameter(powerManager2, "powerManager");
        this.context = context2;
        this.logger = mediaTttLogger;
        this.windowManager = windowManager2;
        this.viewUtil = viewUtil2;
        this.mainExecutor = delayableExecutor;
        this.tapGestureDetector = tapGestureDetector2;
        this.powerManager = powerManager2;
        this.chipLayoutRes = i;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -2;
        layoutParams.height = -2;
        layoutParams.gravity = 49;
        layoutParams.type = 2020;
        layoutParams.flags = 32;
        layoutParams.setTitle("Media Transfer Chip View");
        layoutParams.format = -3;
        layoutParams.setTrustedOverlay();
        this.windowLayoutParams = layoutParams;
    }

    public final Context getContext$SystemUI_nothingRelease() {
        return this.context;
    }

    public final MediaTttLogger getLogger$SystemUI_nothingRelease() {
        return this.logger;
    }

    public final void displayChip(T t) {
        Intrinsics.checkNotNullParameter(t, "chipInfo");
        ViewGroup viewGroup = this.chipView;
        if (viewGroup == null) {
            View inflate = LayoutInflater.from(this.context).inflate(this.chipLayoutRes, (ViewGroup) null);
            if (inflate != null) {
                this.chipView = (ViewGroup) inflate;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
            }
        }
        ViewGroup viewGroup2 = this.chipView;
        Intrinsics.checkNotNull(viewGroup2);
        updateChipView(t, viewGroup2);
        if (viewGroup == null) {
            this.tapGestureDetector.addOnGestureDetectedCallback(MediaTttChipControllerCommonKt.TAG, new MediaTttChipControllerCommon$displayChip$1(this));
            this.windowManager.addView(this.chipView, this.windowLayoutParams);
            this.powerManager.wakeUp(SystemClock.uptimeMillis(), 2, "com.android.systemui:media_tap_to_transfer_activated");
        }
        Runnable runnable = this.cancelChipViewTimeout;
        if (runnable != null) {
            runnable.run();
        }
        this.cancelChipViewTimeout = this.mainExecutor.executeDelayed(new MediaTttChipControllerCommon$$ExternalSyntheticLambda0(this), t.getTimeoutMs());
    }

    /* access modifiers changed from: private */
    /* renamed from: displayChip$lambda-1  reason: not valid java name */
    public static final void m2843displayChip$lambda1(MediaTttChipControllerCommon mediaTttChipControllerCommon) {
        Intrinsics.checkNotNullParameter(mediaTttChipControllerCommon, "this$0");
        mediaTttChipControllerCommon.removeChip(MediaTttRemovalReason.REASON_TIMEOUT);
    }

    public void removeChip(String str) {
        Intrinsics.checkNotNullParameter(str, "removalReason");
        if (this.chipView != null) {
            this.logger.logChipRemoval(str);
            this.tapGestureDetector.removeOnGestureDetectedCallback(MediaTttChipControllerCommonKt.TAG);
            this.windowManager.removeView(this.chipView);
            this.chipView = null;
            Runnable runnable = this.cancelChipViewTimeout;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    public static /* synthetic */ void setIcon$SystemUI_nothingRelease$default(MediaTttChipControllerCommon mediaTttChipControllerCommon, ViewGroup viewGroup, String str, Drawable drawable, CharSequence charSequence, int i, Object obj) {
        if (obj == null) {
            if ((i & 4) != 0) {
                drawable = null;
            }
            if ((i & 8) != 0) {
                charSequence = null;
            }
            mediaTttChipControllerCommon.setIcon$SystemUI_nothingRelease(viewGroup, str, drawable, charSequence);
            return;
        }
        throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: setIcon");
    }

    public final void setIcon$SystemUI_nothingRelease(ViewGroup viewGroup, String str, Drawable drawable, CharSequence charSequence) {
        Intrinsics.checkNotNullParameter(viewGroup, "currentChipView");
        CachingIconView requireViewById = viewGroup.requireViewById(C1893R.C1897id.app_icon);
        IconInfo iconInfo = getIconInfo(str);
        Integer iconSize = getIconSize(iconInfo.isAppIcon());
        if (iconSize != null) {
            int intValue = iconSize.intValue();
            ViewGroup.LayoutParams layoutParams = requireViewById.getLayoutParams();
            layoutParams.width = intValue;
            layoutParams.height = intValue;
            requireViewById.setLayoutParams(layoutParams);
        }
        if (charSequence == null) {
            charSequence = iconInfo.getIconName();
        }
        requireViewById.setContentDescription(charSequence);
        if (drawable == null) {
            drawable = iconInfo.getIcon();
        }
        requireViewById.setImageDrawable(drawable);
    }

    private final IconInfo getIconInfo(String str) {
        if (str != null) {
            try {
                String obj = this.context.getPackageManager().getApplicationInfo(str, PackageManager.ApplicationInfoFlags.of(0)).loadLabel(this.context.getPackageManager()).toString();
                Drawable applicationIcon = this.context.getPackageManager().getApplicationIcon(str);
                Intrinsics.checkNotNullExpressionValue(applicationIcon, "context.packageManager.g…ationIcon(appPackageName)");
                return new IconInfo(obj, applicationIcon, true);
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(MediaTttChipControllerCommonKt.TAG, "Cannot find package " + str, e);
            }
        }
        String string = this.context.getString(C1893R.string.media_output_dialog_unknown_launch_app_name);
        Intrinsics.checkNotNullExpressionValue(string, "context.getString(R.stri…_unknown_launch_app_name)");
        Drawable drawable = this.context.getResources().getDrawable(C1893R.C1895drawable.ic_cast);
        drawable.setTint(Utils.getColorAttrDefaultColor(this.context, 16842806));
        Intrinsics.checkNotNullExpressionValue(drawable, "context.resources.getDra…          )\n            }");
        return new IconInfo(string, drawable, false);
    }

    /* access modifiers changed from: private */
    public final void onScreenTapped(MotionEvent motionEvent) {
        ViewGroup viewGroup = this.chipView;
        if (viewGroup != null && !this.viewUtil.touchIsWithinView(viewGroup, motionEvent.getX(), motionEvent.getY())) {
            removeChip(MediaTttRemovalReason.REASON_SCREEN_TAP);
        }
    }
}
