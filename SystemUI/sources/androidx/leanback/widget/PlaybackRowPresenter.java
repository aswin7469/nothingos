package androidx.leanback.widget;

import android.view.View;
import androidx.leanback.widget.RowPresenter;

public abstract class PlaybackRowPresenter extends RowPresenter {
    public void onReappear(RowPresenter.ViewHolder viewHolder) {
    }

    public static class ViewHolder extends RowPresenter.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }
}
