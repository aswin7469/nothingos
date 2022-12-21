package androidx.leanback.widget;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.leanback.C0742R;
import androidx.leanback.widget.RowPresenter;

public abstract class AbstractMediaListHeaderPresenter extends RowPresenter {
    private int mBackgroundColor;
    private boolean mBackgroundColorSet;
    private final Context mContext;

    public boolean isUsingDefaultSelectEffect() {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract void onBindMediaListHeaderViewHolder(ViewHolder viewHolder, Object obj);

    public static class ViewHolder extends RowPresenter.ViewHolder {
        private final TextView mHeaderView;

        public ViewHolder(View view) {
            super(view);
            this.mHeaderView = (TextView) view.findViewById(C0742R.C0745id.mediaListHeader);
        }

        public TextView getHeaderView() {
            return this.mHeaderView;
        }
    }

    public AbstractMediaListHeaderPresenter(Context context, int i) {
        this.mBackgroundColor = 0;
        this.mContext = new ContextThemeWrapper(context.getApplicationContext(), i);
        setHeaderPresenter((RowHeaderPresenter) null);
    }

    public AbstractMediaListHeaderPresenter() {
        this.mBackgroundColor = 0;
        this.mContext = null;
        setHeaderPresenter((RowHeaderPresenter) null);
    }

    /* access modifiers changed from: protected */
    public RowPresenter.ViewHolder createRowViewHolder(ViewGroup viewGroup) {
        Context context = this.mContext;
        if (context == null) {
            context = viewGroup.getContext();
        }
        View inflate = LayoutInflater.from(context).inflate(C0742R.layout.lb_media_list_header, viewGroup, false);
        inflate.setFocusable(false);
        inflate.setFocusableInTouchMode(false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        if (this.mBackgroundColorSet) {
            viewHolder.view.setBackgroundColor(this.mBackgroundColor);
        }
        return viewHolder;
    }

    /* access modifiers changed from: protected */
    public void onBindRowViewHolder(RowPresenter.ViewHolder viewHolder, Object obj) {
        super.onBindRowViewHolder(viewHolder, obj);
        onBindMediaListHeaderViewHolder((ViewHolder) viewHolder, obj);
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColorSet = true;
        this.mBackgroundColor = i;
    }
}
