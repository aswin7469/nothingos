package com.android.systemui.controls.p010ui;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016¨\u0006\u0007"}, mo65043d2 = {"com/android/systemui/controls/ui/ControlsUiControllerImpl$createCallback$1", "Lcom/android/systemui/controls/management/ControlsListingController$ControlsListingCallback;", "onServicesUpdated", "", "serviceInfos", "", "Lcom/android/systemui/controls/ControlsServiceInfo;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.controls.ui.ControlsUiControllerImpl$createCallback$1 */
/* compiled from: ControlsUiControllerImpl.kt */
public final class ControlsUiControllerImpl$createCallback$1 implements ControlsListingController.ControlsListingCallback {
    final /* synthetic */ Function1<List<SelectionItem>, Unit> $onResult;
    final /* synthetic */ ControlsUiControllerImpl this$0;

    ControlsUiControllerImpl$createCallback$1(ControlsUiControllerImpl controlsUiControllerImpl, Function1<? super List<SelectionItem>, Unit> function1) {
        this.this$0 = controlsUiControllerImpl;
        this.$onResult = function1;
    }

    public void onServicesUpdated(List<ControlsServiceInfo> list) {
        Intrinsics.checkNotNullParameter(list, "serviceInfos");
        Iterable<ControlsServiceInfo> iterable = list;
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
        for (ControlsServiceInfo controlsServiceInfo : iterable) {
            int i = controlsServiceInfo.getServiceInfo().applicationInfo.uid;
            CharSequence loadLabel = controlsServiceInfo.loadLabel();
            Intrinsics.checkNotNullExpressionValue(loadLabel, "it.loadLabel()");
            Drawable loadIcon = controlsServiceInfo.loadIcon();
            Intrinsics.checkNotNullExpressionValue(loadIcon, "it.loadIcon()");
            ComponentName componentName = controlsServiceInfo.componentName;
            Intrinsics.checkNotNullExpressionValue(componentName, "it.componentName");
            arrayList.add(new SelectionItem(loadLabel, "", loadIcon, componentName, i));
        }
        this.this$0.getUiExecutor().execute(new C2036xb617c9db(this.this$0, (List) arrayList, this.$onResult));
    }

    /* access modifiers changed from: private */
    /* renamed from: onServicesUpdated$lambda-1  reason: not valid java name */
    public static final void m2714onServicesUpdated$lambda1(ControlsUiControllerImpl controlsUiControllerImpl, List list, Function1 function1) {
        Intrinsics.checkNotNullParameter(controlsUiControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(list, "$lastItems");
        Intrinsics.checkNotNullParameter(function1, "$onResult");
        ViewGroup access$getParent$p = controlsUiControllerImpl.parent;
        if (access$getParent$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("parent");
            access$getParent$p = null;
        }
        access$getParent$p.removeAllViews();
        if (list.size() > 0) {
            function1.invoke(list);
        }
    }
}
