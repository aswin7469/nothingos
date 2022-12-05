package com.nt.settings.wifi;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settingslib.Utils;
import com.android.wifitrackerlib.WifiEntry;
import com.nt.settings.wifi.WifiListAdapter;
import java.util.List;
/* loaded from: classes2.dex */
public class WifiListAdapter extends RecyclerView.Adapter<WifiItemViewHolder> {
    private WifiListAdapterCallBack mCallBack;
    private Context mContext;
    private List<WifiEntry> mDataList;
    private boolean mNeedShowSeeAllButton = false;

    /* loaded from: classes2.dex */
    public interface WifiListAdapterCallBack {
        void onItemClick(WifiEntry wifiEntry);

        void onSeeAllClick();
    }

    public WifiListAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<WifiEntry> list) {
        this.mDataList = list;
        if (this.mNeedShowSeeAllButton) {
            list.add(null);
        }
    }

    public void setNeedShowSeeAllButton(boolean z) {
        this.mNeedShowSeeAllButton = z;
    }

    public boolean getNeedShowSeeAllButton() {
        return this.mNeedShowSeeAllButton;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return (!this.mNeedShowSeeAllButton || i != this.mDataList.size() - 1) ? 0 : 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public WifiItemViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate;
        if (i == 1) {
            inflate = LayoutInflater.from(this.mContext).inflate(R.layout.layout_wifi_seeall, viewGroup, false);
        } else {
            inflate = LayoutInflater.from(this.mContext).inflate(R.layout.layout_wifi_item, viewGroup, false);
        }
        return new WifiItemViewHolder(inflate, i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(WifiItemViewHolder wifiItemViewHolder, int i) {
        if (wifiItemViewHolder.getViewType() == 0) {
            WifiEntry wifiEntry = this.mDataList.get(i);
            wifiItemViewHolder.mWifiInfo = wifiEntry;
            wifiItemViewHolder.mIconImageView.setImageResource(Utils.getWifiIconResource(Math.max(wifiEntry.getLevel(), 0), wifiEntry.getWifiStandard(), wifiEntry.canConnect()));
            wifiItemViewHolder.mTitleTextView.setText(wifiEntry.getTitle());
            wifiItemViewHolder.mContentTextView.setText(wifiEntry.getSecondSummary());
            String summary = wifiEntry.getSummary(false);
            if (wifiEntry.isPskSaeTransitionMode()) {
                summary = "WPA3(SAE Transition Mode) " + summary;
            } else if (wifiEntry.isOweTransitionMode()) {
                summary = "WPA3(OWE Transition Mode) " + summary;
            } else if (wifiEntry.getSecurity() == 5) {
                summary = "WPA3(SAE) " + summary;
            } else if (wifiEntry.getSecurity() == 4) {
                summary = "WPA3(OWE) " + summary;
            }
            wifiItemViewHolder.mContentTextView.setText(summary);
            if (summary == null || summary.length() == 0) {
                wifiItemViewHolder.mContentTextView.setVisibility(8);
            } else {
                wifiItemViewHolder.mContentTextView.setVisibility(0);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<WifiEntry> list = this.mDataList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    /* loaded from: classes2.dex */
    public class WifiItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mContentTextView;
        public ImageView mIconImageView;
        public TextView mTitleTextView;
        private int mViewType;
        public WifiEntry mWifiInfo;

        public WifiItemViewHolder(View view, int i) {
            super(view);
            this.mViewType = i;
            if (1 == i) {
                view.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.wifi.WifiListAdapter$WifiItemViewHolder$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        WifiListAdapter.WifiItemViewHolder.this.lambda$new$0(view2);
                    }
                });
            } else {
                this.mIconImageView = (ImageView) view.findViewById(R.id.layout_wifi_item_icon_imageview);
                this.mTitleTextView = (TextView) view.findViewById(R.id.layout_wifi_item_title_textview);
                this.mContentTextView = (TextView) view.findViewById(R.id.layout_wifi_item_content_textview);
                view.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.wifi.WifiListAdapter$WifiItemViewHolder$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        WifiListAdapter.WifiItemViewHolder.this.lambda$new$1(view2);
                    }
                });
            }
            updateRippleColor(view);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0(View view) {
            WifiListAdapter.this.mCallBack.onSeeAllClick();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$1(View view) {
            WifiListAdapter.this.mCallBack.onItemClick(this.mWifiInfo);
        }

        public int getViewType() {
            return this.mViewType;
        }

        private void updateRippleColor(View view) {
            view.setBackground(new RippleDrawable(new ColorStateList(new int[][]{new int[]{16842919}, StateSet.NOTHING}, new int[]{convertRgbToArgb(view.getContext().getResources().getColor(R.color.colorAccent), 10.0f), 0}), view.getBackground(), null));
        }

        private int convertRgbToArgb(int i, float f) {
            return Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
        }
    }

    public void setCallBack(WifiListAdapterCallBack wifiListAdapterCallBack) {
        this.mCallBack = wifiListAdapterCallBack;
    }
}
