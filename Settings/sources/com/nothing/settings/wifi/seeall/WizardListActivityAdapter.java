package com.nothing.settings.wifi.seeall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WizardListActivityAdapter extends RecyclerView.Adapter<WizardListViewHolder> {
    private WizardListActivityAdapterCallBack mCallBack;
    private final Context mContext;
    private List<?> mDataList;
    private View mFooterView;
    private View mHeaderView;
    private int mItemResId;
    private boolean mShowFooter;
    private boolean mShowHeader;

    public interface WizardListActivityAdapterCallBack {
        void onItemClick(View view, Object obj);

        void onItemShow(View view, Object obj);
    }

    public WizardListActivityAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<?> list) {
        this.mDataList = list;
    }

    public void setHeaderView(View view) {
        if (view != null) {
            this.mHeaderView = view;
            this.mShowHeader = true;
            return;
        }
        this.mHeaderView = null;
        this.mShowHeader = false;
    }

    public void setItemResId(int i) {
        this.mItemResId = i;
    }

    public void setFooterView(View view) {
        if (view != null) {
            this.mFooterView = view;
            this.mShowFooter = true;
            return;
        }
        this.mFooterView = null;
        this.mShowFooter = false;
    }

    public int getItemCount() {
        List<?> list = this.mDataList;
        return (list != null ? list.size() : 0) + (this.mShowHeader ? 1 : 0) + (this.mShowFooter ? 1 : 0);
    }

    public int getItemViewType(int i) {
        boolean z = this.mShowHeader;
        if (z && i == 0) {
            return 1;
        }
        if (!this.mShowFooter) {
            return 2;
        }
        int i2 = 0;
        if (z) {
            List<?> list = this.mDataList;
            if (i == (list != null ? list.size() : 0) + 1) {
                return 3;
            }
        }
        if (this.mShowHeader) {
            return 2;
        }
        List<?> list2 = this.mDataList;
        if (list2 != null) {
            i2 = list2.size();
        }
        if (i == i2) {
            return 3;
        }
        return 2;
    }

    public WizardListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (1 == i) {
            return new WizardListViewHolder(this.mHeaderView, 1);
        }
        if (2 == i) {
            return new WizardListViewHolder(LayoutInflater.from(this.mContext).inflate(this.mItemResId, viewGroup, false), 2);
        }
        if (3 == i) {
            return new WizardListViewHolder(this.mFooterView, 3);
        }
        return null;
    }

    public void onBindViewHolder(WizardListViewHolder wizardListViewHolder, int i) {
        View view;
        int itemViewType = getItemViewType(i);
        if (1 != itemViewType) {
            int i2 = -1;
            if (2 == itemViewType) {
                View view2 = wizardListViewHolder.itemView;
                List<?> list = this.mDataList;
                if (!this.mShowHeader) {
                    i2 = 0;
                }
                view2.setTag(list.get(i + i2));
                WizardListActivityAdapterCallBack wizardListActivityAdapterCallBack = this.mCallBack;
                View view3 = wizardListViewHolder.itemView;
                wizardListActivityAdapterCallBack.onItemShow(view3, view3.getTag());
                wizardListViewHolder.mItemView.setOnClickListener(new WizardListActivityAdapter$$ExternalSyntheticLambda0(this));
            } else if (3 == itemViewType && (view = wizardListViewHolder.mFooterView) != null && view.getLayoutParams() != null) {
                wizardListViewHolder.mFooterView.getLayoutParams().width = -1;
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        this.mCallBack.onItemClick(view, view.getTag());
    }

    public static class WizardListViewHolder extends RecyclerView.ViewHolder {
        public View mFooterView;
        public View mHeaderView;
        public View mItemView;

        public WizardListViewHolder(View view, int i) {
            super(view);
            if (1 == i) {
                this.mHeaderView = view;
            } else if (2 == i) {
                this.mItemView = view;
            } else if (3 == i) {
                this.mFooterView = view;
            }
        }
    }

    public void setCallBack(WizardListActivityAdapterCallBack wizardListActivityAdapterCallBack) {
        this.mCallBack = wizardListActivityAdapterCallBack;
    }
}
