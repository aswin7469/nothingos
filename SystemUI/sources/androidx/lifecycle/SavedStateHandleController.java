package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
/* loaded from: classes.dex */
final class SavedStateHandleController implements LifecycleEventObserver {
    private boolean mIsAttached;

    boolean isAttached() {
        return this.mIsAttached;
    }

    void attachToLifecycle(SavedStateRegistry registry, Lifecycle lifecycle) {
        if (this.mIsAttached) {
            throw new IllegalStateException("Already attached to lifecycleOwner");
        }
        this.mIsAttached = true;
        lifecycle.addObserver(this);
        throw null;
    }

    @Override // androidx.lifecycle.LifecycleEventObserver
    public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            this.mIsAttached = false;
            source.mo1437getLifecycle().removeObserver(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class OnRecreation implements SavedStateRegistry.AutoRecreated {
        OnRecreation() {
        }

        @Override // androidx.savedstate.SavedStateRegistry.AutoRecreated
        public void onRecreated(SavedStateRegistryOwner owner) {
            if (!(owner instanceof ViewModelStoreOwner)) {
                throw new IllegalStateException("Internal error: OnRecreation should be registered only on componentsthat implement ViewModelStoreOwner");
            }
            ViewModelStore viewModelStore = ((ViewModelStoreOwner) owner).getViewModelStore();
            SavedStateRegistry savedStateRegistry = owner.getSavedStateRegistry();
            for (String str : viewModelStore.keys()) {
                SavedStateHandleController.attachHandleIfNeeded(viewModelStore.get(str), savedStateRegistry, owner.mo1437getLifecycle());
            }
            if (viewModelStore.keys().isEmpty()) {
                return;
            }
            savedStateRegistry.runOnNextRecreation(OnRecreation.class);
        }
    }

    static void attachHandleIfNeeded(ViewModel viewModel, SavedStateRegistry registry, Lifecycle lifecycle) {
        SavedStateHandleController savedStateHandleController = (SavedStateHandleController) viewModel.getTag("androidx.lifecycle.savedstate.vm.tag");
        if (savedStateHandleController == null || savedStateHandleController.isAttached()) {
            return;
        }
        savedStateHandleController.attachToLifecycle(registry, lifecycle);
        tryToAddRecreator(registry, lifecycle);
    }

    private static void tryToAddRecreator(final SavedStateRegistry registry, final Lifecycle lifecycle) {
        Lifecycle.State currentState = lifecycle.getCurrentState();
        if (currentState == Lifecycle.State.INITIALIZED || currentState.isAtLeast(Lifecycle.State.STARTED)) {
            registry.runOnNextRecreation(OnRecreation.class);
        } else {
            lifecycle.addObserver(new LifecycleEventObserver() { // from class: androidx.lifecycle.SavedStateHandleController.1
                @Override // androidx.lifecycle.LifecycleEventObserver
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_START) {
                        Lifecycle.this.removeObserver(this);
                        registry.runOnNextRecreation(OnRecreation.class);
                    }
                }
            });
        }
    }
}
