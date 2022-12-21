package com.android.systemui.controls.management;

import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.text.Collator;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u001b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016¨\u0006\u0007"}, mo64987d2 = {"com/android/systemui/controls/management/AppAdapter$callback$1", "Lcom/android/systemui/controls/management/ControlsListingController$ControlsListingCallback;", "onServicesUpdated", "", "serviceInfos", "", "Lcom/android/systemui/controls/ControlsServiceInfo;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AppAdapter.kt */
public final class AppAdapter$callback$1 implements ControlsListingController.ControlsListingCallback {
    final /* synthetic */ Executor $backgroundExecutor;
    final /* synthetic */ Executor $uiExecutor;
    final /* synthetic */ AppAdapter this$0;

    AppAdapter$callback$1(Executor executor, AppAdapter appAdapter, Executor executor2) {
        this.$backgroundExecutor = executor;
        this.this$0 = appAdapter;
        this.$uiExecutor = executor2;
    }

    public void onServicesUpdated(List<ControlsServiceInfo> list) {
        Intrinsics.checkNotNullParameter(list, "serviceInfos");
        this.$backgroundExecutor.execute(new AppAdapter$callback$1$$ExternalSyntheticLambda0(this.this$0, list, this.$uiExecutor));
    }

    /* access modifiers changed from: private */
    /* renamed from: onServicesUpdated$lambda-1  reason: not valid java name */
    public static final void m2634onServicesUpdated$lambda1(AppAdapter appAdapter, List list, Executor executor) {
        Intrinsics.checkNotNullParameter(appAdapter, "this$0");
        Intrinsics.checkNotNullParameter(list, "$serviceInfos");
        Intrinsics.checkNotNullParameter(executor, "$uiExecutor");
        Collator instance = Collator.getInstance(appAdapter.resources.getConfiguration().getLocales().get(0));
        Intrinsics.checkNotNullExpressionValue(instance, "collator");
        appAdapter.listOfServices = CollectionsKt.sortedWith(list, new C2031x25e6d7e3(instance));
        executor.execute(new AppAdapter$callback$1$$ExternalSyntheticLambda1(appAdapter));
    }
}
