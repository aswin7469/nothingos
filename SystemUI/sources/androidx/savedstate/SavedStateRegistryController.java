package androidx.savedstate;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
/* loaded from: classes.dex */
public final class SavedStateRegistryController {
    private final SavedStateRegistryOwner mOwner;
    private final SavedStateRegistry mRegistry = new SavedStateRegistry();

    private SavedStateRegistryController(SavedStateRegistryOwner savedStateRegistryOwner) {
        this.mOwner = savedStateRegistryOwner;
    }

    public SavedStateRegistry getSavedStateRegistry() {
        return this.mRegistry;
    }

    public void performRestore(Bundle bundle) {
        Lifecycle mo1437getLifecycle = this.mOwner.mo1437getLifecycle();
        if (mo1437getLifecycle.getCurrentState() != Lifecycle.State.INITIALIZED) {
            throw new IllegalStateException("Restarter must be created only during owner's initialization stage");
        }
        mo1437getLifecycle.addObserver(new Recreator(this.mOwner));
        this.mRegistry.performRestore(mo1437getLifecycle, bundle);
    }

    public void performSave(Bundle bundle) {
        this.mRegistry.performSave(bundle);
    }

    public static SavedStateRegistryController create(SavedStateRegistryOwner savedStateRegistryOwner) {
        return new SavedStateRegistryController(savedStateRegistryOwner);
    }
}
