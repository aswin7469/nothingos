package com.nt.settings.wifi.seeall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
/* loaded from: classes2.dex */
public class WizardListActivityAdapter extends RecyclerView.Adapter<WizardListViewHolder> {
    private WizardListActivityAdapterCallBack mCallBack;
    private final Context mContext;
    private List<?> mDataList;
    private View mFooterView;
    private View mHeaderView;
    private int mItemResId;
    private boolean mShowFooter;
    private boolean mShowHeader;

    /* loaded from: classes2.dex */
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

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<?> list = this.mDataList;
        return (list != null ? list.size() : 0) + (this.mShowHeader ? 1 : 0) + (this.mShowFooter ? 1 : 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        boolean z = this.mShowHeader;
        if (!z || i != 0) {
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
            return i == i2 ? 3 : 2;
        }
        return 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public WizardListViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (1 == i) {
            return new WizardListViewHolder(this.mHeaderView, 1);
        }
        if (2 == i) {
            return new WizardListViewHolder(LayoutInflater.from(this.mContext).inflate(this.mItemResId, viewGroup, false), 2);
        }
        if (3 != i) {
            return null;
        }
        return new WizardListViewHolder(this.mFooterView, 3);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(WizardListViewHolder wizardListViewHolder, int i) {
        View view;
        int itemViewType = getItemViewType(i);
        if (1 == itemViewType) {
            return;
        }
        int i2 = -1;
        if (2 != itemViewType) {
            if (3 != itemViewType || (view = wizardListViewHolder.mFooterView) == null || view.getLayoutParams() == null) {
                return;
            }
            wizardListViewHolder.mFooterView.getLayoutParams().width = -1;
            return;
        }
        View view2 = wizardListViewHolder.itemView;
        List<?> list = this.mDataList;
        if (!this.mShowHeader) {
            i2 = 0;
        }
        view2.setTag(list.get(i + i2));
        this.mCallBack.onItemShow(wizardListViewHolder.mItemView, wizardListViewHolder.itemView.getTag());
        wizardListViewHolder.mItemView.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.wifi.seeall.WizardListActivityAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                WizardListActivityAdapter.this.lambda$onBindViewHolder$0(view3);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        this.mCallBack.onItemClick(view, view.getTag());
    }

    /* loaded from: classes2.dex */
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
            } else if (3 != i) {
            } else {
                this.mFooterView = view;
            }
        }
    }

    public void setCallBack(WizardListActivityAdapterCallBack wizardListActivityAdapterCallBack) {
        this.mCallBack = wizardListActivityAdapterCallBack;
    }
}
