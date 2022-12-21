package androidx.leanback.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.leanback.C0742R;
import androidx.leanback.widget.MultiActionsProvider;
import androidx.leanback.widget.Presenter;

class MediaItemActionPresenter extends Presenter {
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    MediaItemActionPresenter() {
    }

    static class ViewHolder extends Presenter.ViewHolder {
        final ImageView mIcon;

        public ViewHolder(View view) {
            super(view);
            this.mIcon = (ImageView) view.findViewById(C0742R.C0745id.actionIcon);
        }

        public ImageView getIcon() {
            return this.mIcon;
        }
    }

    public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C0742R.layout.lb_row_media_item_action, viewGroup, false));
    }

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
        ((ViewHolder) viewHolder).getIcon().setImageDrawable(((MultiActionsProvider.MultiAction) obj).getCurrentDrawable());
    }
}
