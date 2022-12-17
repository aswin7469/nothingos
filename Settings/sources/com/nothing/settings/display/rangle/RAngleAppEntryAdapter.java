package com.nothing.settings.display.rangle;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.nothing.p006ui.support.NtCustSwitch;
import com.nothing.settings.display.rangle.AppIconMemoryOptimizeHelper;
import com.nothing.settings.utils.NtSettingsVibrateUtils;
import java.util.List;

public class RAngleAppEntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private AppIconMemoryOptimizeHelper mAppIconMemoryOptimizeHelper;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public final RAngleController mController = RAngleController.getInstance(this.mContext);
    private List<AppEntry> mEntries;
    private LayoutInflater mInflater;
    /* access modifiers changed from: private */
    public RecyclerView mListView;

    public int getItemViewType(int i) {
        return i == 0 ? 0 : 1;
    }

    public RAngleAppEntryAdapter(Context context, RecyclerView recyclerView, List<AppEntry> list, AppIconMemoryOptimizeHelper appIconMemoryOptimizeHelper) {
        this.mListView = recyclerView;
        this.mEntries = list;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mAppIconMemoryOptimizeHelper = appIconMemoryOptimizeHelper;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new HeaderViewHolder(this.mListView, this.mInflater.inflate(R$layout.app_entry_list_desc_header, viewGroup, false));
        } else if (i != 1) {
            return null;
        } else {
            return new ItemViewHolder(this.mListView, this.mInflater.inflate(R$layout.app_entry_list_layout, viewGroup, false));
        }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int itemViewType = getItemViewType(i);
        if (itemViewType == 0) {
            ((HeaderViewHolder) viewHolder).setAllText(this.mContext.getString(R$string.r_angle_adjust_description), this.mContext.getString(R$string.r_angle_adjust_tips));
        } else if (itemViewType == 1) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
            final AppEntry appEntry = this.mEntries.get(i - 1);
            if (appEntry != null) {
                ApplicationInfo info = appEntry.getInfo();
                String str = info != null ? info.packageName : "";
                final String label = appEntry.getLabel();
                final boolean isRAngleAdjust = appEntry.isRAngleAdjust();
                if (appEntry.getIconLoaded() != null) {
                    itemViewHolder.setInfo(appEntry, appEntry.getIconLoaded(), str, label, isRAngleAdjust);
                } else {
                    this.mAppIconMemoryOptimizeHelper.getIcon(str, new AppIconMemoryOptimizeHelper.OnIconLoadedListener() {
                        public void onIconLoaded(String str, Drawable drawable) {
                            appEntry.setIcon(drawable);
                            itemViewHolder.setInfo(appEntry, drawable, str, label, isRAngleAdjust);
                        }
                    });
                }
            }
        }
    }

    public int getItemCount() {
        List<AppEntry> list = this.mEntries;
        if (list == null || (list != null && list.size() == 0)) {
            return 1;
        }
        return this.mEntries.size() + 1;
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        protected int height;

        public BaseViewHolder(RecyclerView recyclerView, View view) {
            super(view);
            RAngleAppEntryAdapter.this.mListView = recyclerView;
        }

        /* access modifiers changed from: protected */
        public int measureHeight() {
            this.itemView.measure(View.MeasureSpec.makeMeasureSpec(RAngleAppEntryAdapter.this.mListView.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
            int measuredHeight = this.itemView.getMeasuredHeight();
            this.height = measuredHeight;
            return measuredHeight;
        }

        /* access modifiers changed from: protected */
        public void setItemHeight() {
            ViewGroup.LayoutParams layoutParams = this.itemView.getLayoutParams();
            layoutParams.height = this.height;
            this.itemView.setLayoutParams(layoutParams);
        }
    }

    public class HeaderViewHolder extends BaseViewHolder {
        public TextView categoryTitleTextView;
        public TextView descriptionTextView;
        public View divider;
        public int height = measureHeight();

        public HeaderViewHolder(RecyclerView recyclerView, View view) {
            super(recyclerView, view);
            this.descriptionTextView = (TextView) view.findViewById(R$id.r_angle_description);
            this.categoryTitleTextView = (TextView) view.findViewById(R$id.category_title);
            this.divider = view.findViewById(R$id.divider);
        }

        public void setAllText(String str, String str2) {
            this.descriptionTextView.setText(str);
            this.categoryTitleTextView.setText(str2);
            this.divider.setVisibility(0);
            setItemHeight();
        }
    }

    public class ItemViewHolder extends BaseViewHolder {
        public ImageView appIconImage;
        public TextView appNameText;
        public int height = measureHeight();
        public NtCustSwitch rAngleSwitch;

        public ItemViewHolder(RecyclerView recyclerView, View view) {
            super(recyclerView, view);
            this.appIconImage = (ImageView) view.findViewById(R$id.app_icon);
            this.appNameText = (TextView) view.findViewById(R$id.app_name);
            this.rAngleSwitch = (NtCustSwitch) view.findViewById(R$id.r_angle_switch);
        }

        public void setInfo(final AppEntry appEntry, final Drawable drawable, final String str, String str2, boolean z) {
            if (drawable != null) {
                this.appIconImage.postDelayed(new Runnable() {
                    public void run() {
                        ItemViewHolder.this.appIconImage.setImageDrawable(drawable);
                    }
                }, 100);
            }
            if (str2 != null && !str2.isEmpty()) {
                this.appNameText.setText(str2);
            }
            this.rAngleSwitch.setChecked(z);
            this.rAngleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (compoundButton.isPressed()) {
                        RAngleAppEntryAdapter.this.mController.setRAngleNotAdjustOpened(str, z, true);
                        appEntry.setIsRAngleAdjust(z);
                    }
                }
            });
            this.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    NtCustSwitch ntCustSwitch = ItemViewHolder.this.rAngleSwitch;
                    ntCustSwitch.setChecked(!ntCustSwitch.isChecked());
                    NtSettingsVibrateUtils.getInstance(RAngleAppEntryAdapter.this.mContext).playSwitchVibrate();
                }
            });
            setItemHeight();
        }
    }
}
