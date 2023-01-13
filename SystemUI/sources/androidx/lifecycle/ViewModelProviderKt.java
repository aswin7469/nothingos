package androidx.lifecycle;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001e\u0010\u0000\u001a\u0002H\u0001\"\n\b\u0000\u0010\u0001\u0018\u0001*\u00020\u0002*\u00020\u0003H\b¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, mo65043d2 = {"get", "VM", "Landroidx/lifecycle/ViewModel;", "Landroidx/lifecycle/ViewModelProvider;", "(Landroidx/lifecycle/ViewModelProvider;)Landroidx/lifecycle/ViewModel;", "lifecycle-viewmodel_release"}, mo65044k = 2, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: ViewModelProvider.kt */
public final class ViewModelProviderKt {
    public static final /* synthetic */ <VM extends ViewModel> VM get(ViewModelProvider viewModelProvider) {
        Intrinsics.checkNotNullParameter(viewModelProvider, "<this>");
        Intrinsics.reifiedOperationMarker(4, "VM");
        Class<ViewModel> cls = ViewModel.class;
        Class cls2 = cls;
        return viewModelProvider.get(cls);
    }
}
