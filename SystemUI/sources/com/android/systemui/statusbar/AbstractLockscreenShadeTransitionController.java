package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.IndentingPrintWriter;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.LargeScreenUtils;
import java.p026io.PrintWriter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0005\b&\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH&J%\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001c2\u000e\u0010\u001d\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001f0\u001eH\u0016¢\u0006\u0002\u0010 J\u0010\u0010!\u001a\u00020\u00192\u0006\u0010\r\u001a\u00020\fH$J\b\u0010\"\u001a\u00020\u0019H$J\b\u0010#\u001a\u00020\u0019H\u0002R\u0014\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR$\u0010\r\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f@FX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0014\u0010\u0015\"\u0004\b\u0016\u0010\u0017¨\u0006$"}, mo65043d2 = {"Lcom/android/systemui/statusbar/AbstractLockscreenShadeTransitionController;", "Lcom/android/systemui/Dumpable;", "context", "Landroid/content/Context;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/dump/DumpManager;)V", "getContext", "()Landroid/content/Context;", "value", "", "dragDownAmount", "getDragDownAmount", "()F", "setDragDownAmount", "(F)V", "useSplitShade", "", "getUseSplitShade", "()Z", "setUseSplitShade", "(Z)V", "dump", "", "pw", "Landroid/util/IndentingPrintWriter;", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "onDragDownAmountChanged", "updateResources", "updateResourcesInternal", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: AbstractLockscreenShadeTransitionController.kt */
public abstract class AbstractLockscreenShadeTransitionController implements Dumpable {
    private final Context context;
    private float dragDownAmount;
    private boolean useSplitShade;

    public abstract void dump(IndentingPrintWriter indentingPrintWriter);

    /* access modifiers changed from: protected */
    public abstract void onDragDownAmountChanged(float f);

    /* access modifiers changed from: protected */
    public abstract void updateResources();

    public AbstractLockscreenShadeTransitionController(Context context2, ConfigurationController configurationController, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.context = context2;
        updateResourcesInternal();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ AbstractLockscreenShadeTransitionController this$0;

            {
                this.this$0 = r1;
            }

            public void onConfigChanged(Configuration configuration) {
                this.this$0.updateResourcesInternal();
            }
        });
        dumpManager.registerDumpable(this);
    }

    /* access modifiers changed from: protected */
    public final Context getContext() {
        return this.context;
    }

    /* access modifiers changed from: protected */
    public final boolean getUseSplitShade() {
        return this.useSplitShade;
    }

    /* access modifiers changed from: protected */
    public final void setUseSplitShade(boolean z) {
        this.useSplitShade = z;
    }

    public final float getDragDownAmount() {
        return this.dragDownAmount;
    }

    public final void setDragDownAmount(float f) {
        if (!(f == this.dragDownAmount)) {
            this.dragDownAmount = f;
            onDragDownAmountChanged(f);
        }
    }

    /* access modifiers changed from: private */
    public final void updateResourcesInternal() {
        Resources resources = this.context.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "context.resources");
        this.useSplitShade = LargeScreenUtils.shouldUseSplitNotificationShade(resources);
        updateResources();
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        dump(new IndentingPrintWriter(printWriter, "  "));
    }
}
