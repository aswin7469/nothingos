package com.android.systemui.statusbar;

import android.content.Context;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import com.android.systemui.C1893R;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001:\u0001\u001fB1\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0010\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u000eH\u0002J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0016J\u0010\u0010\u001d\u001a\u00020\u001a2\u0006\u0010\u0018\u001a\u00020\u000eH\u0014J\b\u0010\u001e\u001a\u00020\u001aH\u0014R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0011X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000¨\u0006 "}, mo64987d2 = {"Lcom/android/systemui/statusbar/LockscreenShadeKeyguardTransitionController;", "Lcom/android/systemui/statusbar/AbstractLockscreenShadeTransitionController;", "mediaHierarchyManager", "Lcom/android/systemui/media/MediaHierarchyManager;", "notificationPanelController", "Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;", "context", "Landroid/content/Context;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/media/MediaHierarchyManager;Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;Landroid/content/Context;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/dump/DumpManager;)V", "alpha", "", "alphaProgress", "alphaTransitionDistance", "", "keyguardTransitionDistance", "keyguardTransitionOffset", "statusBarAlpha", "translationY", "translationYProgress", "calculateKeyguardTranslationY", "dragDownAmount", "dump", "", "indentingPrintWriter", "Landroid/util/IndentingPrintWriter;", "onDragDownAmountChanged", "updateResources", "Factory", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LockscreenShadeKeyguardTransitionController.kt */
public final class LockscreenShadeKeyguardTransitionController extends AbstractLockscreenShadeTransitionController {
    private float alpha;
    private float alphaProgress;
    private int alphaTransitionDistance;
    private int keyguardTransitionDistance;
    private int keyguardTransitionOffset;
    private final MediaHierarchyManager mediaHierarchyManager;
    private final NotificationPanelViewController notificationPanelController;
    private float statusBarAlpha;
    private int translationY;
    private float translationYProgress;

    @AssistedFactory
    @Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bç\u0001\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/LockscreenShadeKeyguardTransitionController$Factory;", "", "create", "Lcom/android/systemui/statusbar/LockscreenShadeKeyguardTransitionController;", "notificationPanelController", "Lcom/android/systemui/statusbar/phone/NotificationPanelViewController;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: LockscreenShadeKeyguardTransitionController.kt */
    public interface Factory {
        LockscreenShadeKeyguardTransitionController create(NotificationPanelViewController notificationPanelViewController);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @AssistedInject
    public LockscreenShadeKeyguardTransitionController(MediaHierarchyManager mediaHierarchyManager2, @Assisted NotificationPanelViewController notificationPanelViewController, Context context, ConfigurationController configurationController, DumpManager dumpManager) {
        super(context, configurationController, dumpManager);
        Intrinsics.checkNotNullParameter(mediaHierarchyManager2, "mediaHierarchyManager");
        Intrinsics.checkNotNullParameter(notificationPanelViewController, "notificationPanelController");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.mediaHierarchyManager = mediaHierarchyManager2;
        this.notificationPanelController = notificationPanelViewController;
    }

    /* access modifiers changed from: protected */
    public void updateResources() {
        this.alphaTransitionDistance = getContext().getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_npvc_keyguard_content_alpha_transition_distance);
        this.keyguardTransitionDistance = getContext().getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_keyguard_transition_distance);
        this.keyguardTransitionOffset = getContext().getResources().getDimensionPixelSize(C1893R.dimen.lockscreen_shade_keyguard_transition_vertical_offset);
    }

    /* access modifiers changed from: protected */
    public void onDragDownAmountChanged(float f) {
        float saturate = MathUtils.saturate(f / ((float) this.alphaTransitionDistance));
        this.alphaProgress = saturate;
        this.alpha = 1.0f - saturate;
        int calculateKeyguardTranslationY = calculateKeyguardTranslationY(f);
        this.translationY = calculateKeyguardTranslationY;
        this.notificationPanelController.setKeyguardTransitionProgress(this.alpha, calculateKeyguardTranslationY);
        float f2 = getUseSplitShade() ? this.alpha : -1.0f;
        this.statusBarAlpha = f2;
        this.notificationPanelController.setKeyguardStatusBarAlpha(f2);
    }

    private final int calculateKeyguardTranslationY(float f) {
        if (!getUseSplitShade()) {
            return 0;
        }
        if (this.mediaHierarchyManager.isCurrentlyInGuidedTransformation()) {
            return this.mediaHierarchyManager.getGuidedTransformationTranslationY();
        }
        float saturate = MathUtils.saturate(f / ((float) this.keyguardTransitionDistance));
        this.translationYProgress = saturate;
        return (int) (saturate * ((float) this.keyguardTransitionOffset));
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        Intrinsics.checkNotNullParameter(indentingPrintWriter, "indentingPrintWriter");
        indentingPrintWriter.println("LockscreenShadeKeyguardTransitionController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Resources:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("alphaTransitionDistance: " + this.alphaTransitionDistance);
        indentingPrintWriter.println("keyguardTransitionDistance: " + this.keyguardTransitionDistance);
        indentingPrintWriter.println("keyguardTransitionOffset: " + this.keyguardTransitionOffset);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("State:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("dragDownAmount: " + getDragDownAmount());
        indentingPrintWriter.println("alpha: " + this.alpha);
        indentingPrintWriter.println("alphaProgress: " + this.alphaProgress);
        indentingPrintWriter.println("statusBarAlpha: " + this.statusBarAlpha);
        indentingPrintWriter.println("translationProgress: " + this.translationYProgress);
        indentingPrintWriter.println("translationY: " + this.translationY);
    }
}
