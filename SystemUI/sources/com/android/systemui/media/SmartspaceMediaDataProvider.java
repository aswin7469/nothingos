package com.android.systemui.media;

import android.app.smartspace.SmartspaceTarget;
import android.util.Log;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0016\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\b0\u0007H\u0016J\u0010\u0010\f\u001a\u00020\n2\u0006\u0010\r\u001a\u00020\u0005H\u0016J\u0012\u0010\u000e\u001a\u00020\n2\b\u0010\r\u001a\u0004\u0018\u00010\u0005H\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u000e¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo65043d2 = {"Lcom/android/systemui/media/SmartspaceMediaDataProvider;", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin;", "()V", "smartspaceMediaTargetListeners", "", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$SmartspaceTargetListener;", "smartspaceMediaTargets", "", "Landroid/app/smartspace/SmartspaceTarget;", "onTargetsAvailable", "", "targets", "registerListener", "smartspaceTargetListener", "unregisterListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SmartspaceMediaDataProvider.kt */
public final class SmartspaceMediaDataProvider implements BcSmartspaceDataPlugin {
    private final List<BcSmartspaceDataPlugin.SmartspaceTargetListener> smartspaceMediaTargetListeners = new ArrayList();
    private List<SmartspaceTarget> smartspaceMediaTargets = CollectionsKt.emptyList();

    public void registerListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        Intrinsics.checkNotNullParameter(smartspaceTargetListener, "smartspaceTargetListener");
        this.smartspaceMediaTargetListeners.add(smartspaceTargetListener);
    }

    public void unregisterListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        TypeIntrinsics.asMutableCollection(this.smartspaceMediaTargetListeners).remove(smartspaceTargetListener);
    }

    public void onTargetsAvailable(List<SmartspaceTarget> list) {
        Intrinsics.checkNotNullParameter(list, "targets");
        List<SmartspaceTarget> arrayList = new ArrayList<>();
        for (SmartspaceTarget next : list) {
            if (next.getFeatureType() == 15) {
                arrayList.add(next);
            }
        }
        if (!arrayList.isEmpty()) {
            Log.d("SsMediaDataProvider", "Forwarding Smartspace media updates " + arrayList);
        }
        this.smartspaceMediaTargets = arrayList;
        for (BcSmartspaceDataPlugin.SmartspaceTargetListener onSmartspaceTargetsUpdated : this.smartspaceMediaTargetListeners) {
            onSmartspaceTargetsUpdated.onSmartspaceTargetsUpdated(this.smartspaceMediaTargets);
        }
    }
}
