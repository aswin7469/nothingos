package com.android.systemui.media;

import android.app.smartspace.SmartspaceTarget;
import android.util.Log;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: SmartspaceMediaDataProvider.kt */
/* loaded from: classes.dex */
public final class SmartspaceMediaDataProvider implements BcSmartspaceDataPlugin {
    @NotNull
    private final List<BcSmartspaceDataPlugin.SmartspaceTargetListener> smartspaceMediaTargetListeners = new ArrayList();
    @NotNull
    private List<SmartspaceTarget> smartspaceMediaTargets = CollectionsKt.emptyList();

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void registerListener(@NotNull BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        Intrinsics.checkNotNullParameter(smartspaceTargetListener, "smartspaceTargetListener");
        this.smartspaceMediaTargetListeners.add(smartspaceTargetListener);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void unregisterListener(@Nullable BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        List<BcSmartspaceDataPlugin.SmartspaceTargetListener> list = this.smartspaceMediaTargetListeners;
        Objects.requireNonNull(list, "null cannot be cast to non-null type kotlin.collections.MutableCollection<T>");
        TypeIntrinsics.asMutableCollection(list).remove(smartspaceTargetListener);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void onTargetsAvailable(@NotNull List<SmartspaceTarget> targets) {
        Intrinsics.checkNotNullParameter(targets, "targets");
        ArrayList arrayList = new ArrayList();
        for (SmartspaceTarget smartspaceTarget : targets) {
            if (smartspaceTarget.getFeatureType() == 15) {
                arrayList.add(smartspaceTarget);
            }
        }
        if (!arrayList.isEmpty()) {
            Log.d("SsMediaDataProvider", Intrinsics.stringPlus("Forwarding Smartspace media updates ", arrayList));
        }
        this.smartspaceMediaTargets = arrayList;
        for (BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener : this.smartspaceMediaTargetListeners) {
            smartspaceTargetListener.onSmartspaceTargetsUpdated(this.smartspaceMediaTargets);
        }
    }
}
