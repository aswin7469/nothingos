package com.android.p019wm.shell.dagger;

import com.android.p019wm.shell.draganddrop.DragAndDrop;
import com.android.p019wm.shell.draganddrop.DragAndDropController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;

/* renamed from: com.android.wm.shell.dagger.WMShellBaseModule_ProvideDragAndDropFactory */
public final class WMShellBaseModule_ProvideDragAndDropFactory implements Factory<Optional<DragAndDrop>> {
    private final Provider<DragAndDropController> dragAndDropControllerProvider;

    public WMShellBaseModule_ProvideDragAndDropFactory(Provider<DragAndDropController> provider) {
        this.dragAndDropControllerProvider = provider;
    }

    public Optional<DragAndDrop> get() {
        return provideDragAndDrop(this.dragAndDropControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideDragAndDropFactory create(Provider<DragAndDropController> provider) {
        return new WMShellBaseModule_ProvideDragAndDropFactory(provider);
    }

    public static Optional<DragAndDrop> provideDragAndDrop(DragAndDropController dragAndDropController) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideDragAndDrop(dragAndDropController));
    }
}
