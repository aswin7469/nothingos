package com.android.systemui.p012qs.external;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.statusbar.IAddTileResultCallback;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.statusbar.CommandQueue;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00003\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J0\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016¨\u0006\u0010"}, mo64987d2 = {"com/android/systemui/qs/external/TileServiceRequestController$commandQueueCallback$1", "Lcom/android/systemui/statusbar/CommandQueue$Callbacks;", "cancelRequestAddTile", "", "packageName", "", "requestAddTile", "componentName", "Landroid/content/ComponentName;", "appName", "", "label", "icon", "Landroid/graphics/drawable/Icon;", "callback", "Lcom/android/internal/statusbar/IAddTileResultCallback;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.external.TileServiceRequestController$commandQueueCallback$1 */
/* compiled from: TileServiceRequestController.kt */
public final class TileServiceRequestController$commandQueueCallback$1 implements CommandQueue.Callbacks {
    final /* synthetic */ TileServiceRequestController this$0;

    TileServiceRequestController$commandQueueCallback$1(TileServiceRequestController tileServiceRequestController) {
        this.this$0 = tileServiceRequestController;
    }

    public void requestAddTile(ComponentName componentName, CharSequence charSequence, CharSequence charSequence2, Icon icon, IAddTileResultCallback iAddTileResultCallback) {
        Intrinsics.checkNotNullParameter(componentName, BaseIconCache.IconDB.COLUMN_COMPONENT);
        Intrinsics.checkNotNullParameter(charSequence, "appName");
        Intrinsics.checkNotNullParameter(charSequence2, BaseIconCache.IconDB.COLUMN_LABEL);
        Intrinsics.checkNotNullParameter(icon, BaseIconCache.IconDB.COLUMN_ICON);
        Intrinsics.checkNotNullParameter(iAddTileResultCallback, "callback");
        this.this$0.requestTileAdd$SystemUI_nothingRelease(componentName, charSequence, charSequence2, icon, new C2377xec6a29e7(iAddTileResultCallback));
    }

    /* access modifiers changed from: private */
    /* renamed from: requestAddTile$lambda-0  reason: not valid java name */
    public static final void m2956requestAddTile$lambda0(IAddTileResultCallback iAddTileResultCallback, Integer num) {
        Intrinsics.checkNotNullParameter(iAddTileResultCallback, "$callback");
        Intrinsics.checkNotNullParameter(num, "it");
        try {
            iAddTileResultCallback.onTileRequest(num.intValue());
        } catch (RemoteException e) {
            Log.e("TileServiceRequestController", "Couldn't respond to request", e);
        }
    }

    public void cancelRequestAddTile(String str) {
        Intrinsics.checkNotNullParameter(str, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        Function1 access$getDialogCanceller$p = this.this$0.dialogCanceller;
        if (access$getDialogCanceller$p != null) {
            access$getDialogCanceller$p.invoke(str);
        }
    }
}
