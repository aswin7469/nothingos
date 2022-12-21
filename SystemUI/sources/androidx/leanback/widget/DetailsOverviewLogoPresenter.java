package androidx.leanback.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.leanback.C0742R;
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter;
import androidx.leanback.widget.Presenter;

public class DetailsOverviewLogoPresenter extends Presenter {
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
    }

    public static class ViewHolder extends Presenter.ViewHolder {
        protected FullWidthDetailsOverviewRowPresenter mParentPresenter;
        protected FullWidthDetailsOverviewRowPresenter.ViewHolder mParentViewHolder;
        private boolean mSizeFromDrawableIntrinsic;

        public ViewHolder(View view) {
            super(view);
        }

        public FullWidthDetailsOverviewRowPresenter getParentPresenter() {
            return this.mParentPresenter;
        }

        public FullWidthDetailsOverviewRowPresenter.ViewHolder getParentViewHolder() {
            return this.mParentViewHolder;
        }

        public boolean isSizeFromDrawableIntrinsic() {
            return this.mSizeFromDrawableIntrinsic;
        }

        public void setSizeFromDrawableIntrinsic(boolean z) {
            this.mSizeFromDrawableIntrinsic = z;
        }
    }

    public View onCreateView(ViewGroup viewGroup) {
        return LayoutInflater.from(viewGroup.getContext()).inflate(C0742R.layout.lb_fullwidth_details_overview_logo, viewGroup, false);
    }

    public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        View onCreateView = onCreateView(viewGroup);
        ViewHolder viewHolder = new ViewHolder(onCreateView);
        ViewGroup.LayoutParams layoutParams = onCreateView.getLayoutParams();
        viewHolder.setSizeFromDrawableIntrinsic(layoutParams.width == -2 && layoutParams.height == -2);
        return viewHolder;
    }

    public void setContext(ViewHolder viewHolder, FullWidthDetailsOverviewRowPresenter.ViewHolder viewHolder2, FullWidthDetailsOverviewRowPresenter fullWidthDetailsOverviewRowPresenter) {
        viewHolder.mParentViewHolder = viewHolder2;
        viewHolder.mParentPresenter = fullWidthDetailsOverviewRowPresenter;
    }

    public boolean isBoundToImage(ViewHolder viewHolder, DetailsOverviewRow detailsOverviewRow) {
        return (detailsOverviewRow == null || detailsOverviewRow.getImageDrawable() == null) ? false : true;
    }

    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
        DetailsOverviewRow detailsOverviewRow = (DetailsOverviewRow) obj;
        ImageView imageView = (ImageView) viewHolder.view;
        imageView.setImageDrawable(detailsOverviewRow.getImageDrawable());
        ViewHolder viewHolder2 = (ViewHolder) viewHolder;
        if (isBoundToImage(viewHolder2, detailsOverviewRow)) {
            if (viewHolder2.isSizeFromDrawableIntrinsic()) {
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                layoutParams.width = detailsOverviewRow.getImageDrawable().getIntrinsicWidth();
                layoutParams.height = detailsOverviewRow.getImageDrawable().getIntrinsicHeight();
                if (imageView.getMaxWidth() > 0 || imageView.getMaxHeight() > 0) {
                    float f = 1.0f;
                    float maxWidth = (imageView.getMaxWidth() <= 0 || layoutParams.width <= imageView.getMaxWidth()) ? 1.0f : ((float) imageView.getMaxWidth()) / ((float) layoutParams.width);
                    if (imageView.getMaxHeight() > 0 && layoutParams.height > imageView.getMaxHeight()) {
                        f = ((float) imageView.getMaxHeight()) / ((float) layoutParams.height);
                    }
                    float min = Math.min(maxWidth, f);
                    layoutParams.width = (int) (((float) layoutParams.width) * min);
                    layoutParams.height = (int) (((float) layoutParams.height) * min);
                }
                imageView.setLayoutParams(layoutParams);
            }
            viewHolder2.mParentPresenter.notifyOnBindLogo(viewHolder2.mParentViewHolder);
        }
    }
}
