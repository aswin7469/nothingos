package androidx.leanback.widget;

import android.database.Observable;

public abstract class ObjectAdapter {
    public static final int NO_ID = -1;
    private boolean mHasStableIds;
    private final DataObservable mObservable = new DataObservable();
    private PresenterSelector mPresenterSelector;

    public abstract Object get(int i);

    public long getId(int i) {
        return -1;
    }

    public boolean isImmediateNotifySupported() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void onHasStableIdsChanged() {
    }

    /* access modifiers changed from: protected */
    public void onPresenterSelectorChanged() {
    }

    public abstract int size();

    public static abstract class DataObserver {
        public void onChanged() {
        }

        public void onItemRangeChanged(int i, int i2) {
            onChanged();
        }

        public void onItemRangeChanged(int i, int i2, Object obj) {
            onChanged();
        }

        public void onItemRangeInserted(int i, int i2) {
            onChanged();
        }

        public void onItemMoved(int i, int i2) {
            onChanged();
        }

        public void onItemRangeRemoved(int i, int i2) {
            onChanged();
        }
    }

    private static final class DataObservable extends Observable<DataObserver> {
        DataObservable() {
        }

        public void notifyChanged() {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((DataObserver) this.mObservers.get(size)).onChanged();
            }
        }

        public void notifyItemRangeChanged(int i, int i2) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((DataObserver) this.mObservers.get(size)).onItemRangeChanged(i, i2);
            }
        }

        public void notifyItemRangeChanged(int i, int i2, Object obj) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((DataObserver) this.mObservers.get(size)).onItemRangeChanged(i, i2, obj);
            }
        }

        public void notifyItemRangeInserted(int i, int i2) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((DataObserver) this.mObservers.get(size)).onItemRangeInserted(i, i2);
            }
        }

        public void notifyItemRangeRemoved(int i, int i2) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((DataObserver) this.mObservers.get(size)).onItemRangeRemoved(i, i2);
            }
        }

        public void notifyItemMoved(int i, int i2) {
            for (int size = this.mObservers.size() - 1; size >= 0; size--) {
                ((DataObserver) this.mObservers.get(size)).onItemMoved(i, i2);
            }
        }

        /* access modifiers changed from: package-private */
        public boolean hasObserver() {
            return this.mObservers.size() > 0;
        }
    }

    public ObjectAdapter(PresenterSelector presenterSelector) {
        setPresenterSelector(presenterSelector);
    }

    public ObjectAdapter(Presenter presenter) {
        setPresenterSelector(new SinglePresenterSelector(presenter));
    }

    public ObjectAdapter() {
    }

    public final void setPresenterSelector(PresenterSelector presenterSelector) {
        if (presenterSelector != null) {
            PresenterSelector presenterSelector2 = this.mPresenterSelector;
            boolean z = true;
            boolean z2 = presenterSelector2 != null;
            if (!z2 || presenterSelector2 == presenterSelector) {
                z = false;
            }
            this.mPresenterSelector = presenterSelector;
            if (z) {
                onPresenterSelectorChanged();
            }
            if (z2) {
                notifyChanged();
                return;
            }
            return;
        }
        throw new IllegalArgumentException("Presenter selector must not be null");
    }

    public final PresenterSelector getPresenterSelector() {
        return this.mPresenterSelector;
    }

    public final void registerObserver(DataObserver dataObserver) {
        this.mObservable.registerObserver(dataObserver);
    }

    public final void unregisterObserver(DataObserver dataObserver) {
        this.mObservable.unregisterObserver(dataObserver);
    }

    public final boolean hasObserver() {
        return this.mObservable.hasObserver();
    }

    public final void unregisterAllObservers() {
        this.mObservable.unregisterAll();
    }

    public final void notifyItemRangeChanged(int i, int i2) {
        this.mObservable.notifyItemRangeChanged(i, i2);
    }

    public final void notifyItemRangeChanged(int i, int i2, Object obj) {
        this.mObservable.notifyItemRangeChanged(i, i2, obj);
    }

    /* access modifiers changed from: protected */
    public final void notifyItemRangeInserted(int i, int i2) {
        this.mObservable.notifyItemRangeInserted(i, i2);
    }

    /* access modifiers changed from: protected */
    public final void notifyItemRangeRemoved(int i, int i2) {
        this.mObservable.notifyItemRangeRemoved(i, i2);
    }

    /* access modifiers changed from: protected */
    public final void notifyItemMoved(int i, int i2) {
        this.mObservable.notifyItemMoved(i, i2);
    }

    /* access modifiers changed from: protected */
    public final void notifyChanged() {
        this.mObservable.notifyChanged();
    }

    public final boolean hasStableIds() {
        return this.mHasStableIds;
    }

    public final void setHasStableIds(boolean z) {
        boolean z2 = this.mHasStableIds != z;
        this.mHasStableIds = z;
        if (z2) {
            onHasStableIdsChanged();
        }
    }

    public final Presenter getPresenter(Object obj) {
        PresenterSelector presenterSelector = this.mPresenterSelector;
        if (presenterSelector != null) {
            return presenterSelector.getPresenter(obj);
        }
        throw new IllegalStateException("Presenter selector must not be null");
    }
}
