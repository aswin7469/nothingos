package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DetailsOverviewRow extends Row {
    private ObjectAdapter mActionsAdapter = new ArrayObjectAdapter(this.mDefaultActionPresenter);
    private PresenterSelector mDefaultActionPresenter = new ActionPresenterSelector();
    private Drawable mImageDrawable;
    private boolean mImageScaleUpAllowed = true;
    private Object mItem;
    private ArrayList<WeakReference<Listener>> mListeners;

    public static class Listener {
        public void onActionsAdapterChanged(DetailsOverviewRow detailsOverviewRow) {
        }

        public void onImageDrawableChanged(DetailsOverviewRow detailsOverviewRow) {
        }

        public void onItemChanged(DetailsOverviewRow detailsOverviewRow) {
        }
    }

    public DetailsOverviewRow(Object obj) {
        super((HeaderItem) null);
        this.mItem = obj;
        verify();
    }

    /* access modifiers changed from: package-private */
    public final void addListener(Listener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        } else {
            int i = 0;
            while (i < this.mListeners.size()) {
                Listener listener2 = (Listener) this.mListeners.get(i).get();
                if (listener2 == null) {
                    this.mListeners.remove(i);
                } else if (listener2 != listener) {
                    i++;
                } else {
                    return;
                }
            }
        }
        this.mListeners.add(new WeakReference(listener));
    }

    /* access modifiers changed from: package-private */
    public final void removeListener(Listener listener) {
        if (this.mListeners != null) {
            int i = 0;
            while (i < this.mListeners.size()) {
                Listener listener2 = (Listener) this.mListeners.get(i).get();
                if (listener2 == null) {
                    this.mListeners.remove(i);
                } else if (listener2 == listener) {
                    this.mListeners.remove(i);
                    return;
                } else {
                    i++;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void notifyItemChanged() {
        if (this.mListeners != null) {
            int i = 0;
            while (i < this.mListeners.size()) {
                Listener listener = (Listener) this.mListeners.get(i).get();
                if (listener == null) {
                    this.mListeners.remove(i);
                } else {
                    listener.onItemChanged(this);
                    i++;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void notifyImageDrawableChanged() {
        if (this.mListeners != null) {
            int i = 0;
            while (i < this.mListeners.size()) {
                Listener listener = (Listener) this.mListeners.get(i).get();
                if (listener == null) {
                    this.mListeners.remove(i);
                } else {
                    listener.onImageDrawableChanged(this);
                    i++;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void notifyActionsAdapterChanged() {
        if (this.mListeners != null) {
            int i = 0;
            while (i < this.mListeners.size()) {
                Listener listener = (Listener) this.mListeners.get(i).get();
                if (listener == null) {
                    this.mListeners.remove(i);
                } else {
                    listener.onActionsAdapterChanged(this);
                    i++;
                }
            }
        }
    }

    public final Object getItem() {
        return this.mItem;
    }

    public final void setItem(Object obj) {
        if (obj != this.mItem) {
            this.mItem = obj;
            notifyItemChanged();
        }
    }

    public final void setImageDrawable(Drawable drawable) {
        if (this.mImageDrawable != drawable) {
            this.mImageDrawable = drawable;
            notifyImageDrawableChanged();
        }
    }

    public final void setImageBitmap(Context context, Bitmap bitmap) {
        this.mImageDrawable = new BitmapDrawable(context.getResources(), bitmap);
        notifyImageDrawableChanged();
    }

    public final Drawable getImageDrawable() {
        return this.mImageDrawable;
    }

    public void setImageScaleUpAllowed(boolean z) {
        if (z != this.mImageScaleUpAllowed) {
            this.mImageScaleUpAllowed = z;
            notifyImageDrawableChanged();
        }
    }

    public boolean isImageScaleUpAllowed() {
        return this.mImageScaleUpAllowed;
    }

    private ArrayObjectAdapter getArrayObjectAdapter() {
        return (ArrayObjectAdapter) this.mActionsAdapter;
    }

    @Deprecated
    public final void addAction(Action action) {
        getArrayObjectAdapter().add(action);
    }

    @Deprecated
    public final void addAction(int i, Action action) {
        getArrayObjectAdapter().add(i, action);
    }

    @Deprecated
    public final boolean removeAction(Action action) {
        return getArrayObjectAdapter().remove(action);
    }

    @Deprecated
    public final List<Action> getActions() {
        return getArrayObjectAdapter().unmodifiableList();
    }

    public final ObjectAdapter getActionsAdapter() {
        return this.mActionsAdapter;
    }

    public final void setActionsAdapter(ObjectAdapter objectAdapter) {
        if (objectAdapter != this.mActionsAdapter) {
            this.mActionsAdapter = objectAdapter;
            if (objectAdapter.getPresenterSelector() == null) {
                this.mActionsAdapter.setPresenterSelector(this.mDefaultActionPresenter);
            }
            notifyActionsAdapterChanged();
        }
    }

    public Action getActionForKeyCode(int i) {
        ObjectAdapter actionsAdapter = getActionsAdapter();
        if (actionsAdapter == null) {
            return null;
        }
        for (int i2 = 0; i2 < actionsAdapter.size(); i2++) {
            Action action = (Action) actionsAdapter.get(i2);
            if (action.respondsToKeyCode(i)) {
                return action;
            }
        }
        return null;
    }

    private void verify() {
        if (this.mItem == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
    }
}
