package androidx.leanback.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.leanback.C0742R;
import androidx.leanback.widget.Presenter;

public class DividerPresenter extends Presenter {
    private final int mLayoutResourceId;

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
    }

    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    public DividerPresenter() {
        this(C0742R.layout.lb_divider);
    }

    public DividerPresenter(int i) {
        this.mLayoutResourceId = i;
    }

    public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new Presenter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(this.mLayoutResourceId, viewGroup, false));
    }
}
