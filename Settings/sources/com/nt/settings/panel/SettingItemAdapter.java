package com.nt.settings.panel;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.nothing.tesla.service.CmdObject;
import com.nothing.tesla.service.CmdObjectList;
import java.util.List;
/* loaded from: classes2.dex */
public class SettingItemAdapter extends RecyclerView.Adapter<SettingItemViewHolder> {
    private ImageView mActionIcon;
    Context mContext;
    private View mDivider;
    private View mHeadedDivider;
    private LiveData<List<SettingItemData>> mLiveData;
    private View mPinnedHeader;
    private LiveData<List<SettingItemData>> mPinnedHeaderLiveData;
    private List<SettingItemData> mPinnedHeaderSettingListsData;
    private List<SettingItemData> mSettingListsData;
    private TextView mSummary;
    private Switch mSwitch;
    private CmdObjectList mTeslaCmdList;
    private LinearLayout mTeslaCmdParentView;
    private View mTeslaExpandViewParent;
    private boolean mTeslaExpanded;
    private ImageView mTeslaIconExpand;
    private ImageView mTeslaIconTempDown;
    private ImageView mTeslaIconTempUp;
    private View mTeslaLoadingView;
    private TeslaConnectPanelPlugin mTeslaPlugin;
    private TextView mTeslaRange;
    private TextView mTeslaTempText;
    private ImageView mTeslaUserIcon;
    private TextView mTeslaUserName;
    private View mTeslaView;
    private TextView mTitle;
    private ImageView mTitleIcon;
    private float currentTemp = 23.0f;
    private float tempStep = 0.5f;
    private float minTemp = 15.5f;
    private float maxTemp = 27.0f;

    public void setPinnedHeader(View view) {
        this.mPinnedHeader = view;
        if (view != null) {
            this.mHeadedDivider = view.findViewById(R.id.headed_divider);
            this.mTitle = (TextView) this.mPinnedHeader.findViewById(R.id.title);
            this.mSummary = (TextView) this.mPinnedHeader.findViewById(R.id.summary);
            this.mDivider = this.mPinnedHeader.findViewById(R.id.divider);
            this.mSwitch = (Switch) this.mPinnedHeader.findViewById(R.id.switch_widget);
            this.mTitleIcon = (ImageView) this.mPinnedHeader.findViewById(R.id.icon_title);
            this.mActionIcon = (ImageView) this.mPinnedHeader.findViewById(R.id.icon_action);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public SettingItemViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SettingItemViewHolder(viewGroup);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(SettingItemViewHolder settingItemViewHolder, int i) {
        SettingItemData settingItemData = this.mSettingListsData.get(i);
        settingItemViewHolder.mTitle.setText(settingItemData.title);
        int i2 = 8;
        settingItemViewHolder.mSummary.setVisibility(TextUtils.isEmpty(settingItemData.subTitle) ? 8 : 0);
        settingItemViewHolder.mSummary.setText(settingItemData.subTitle);
        if (this.mContext.getString(R.string.bluetooth_connecting).equals(settingItemData.subTitle) || this.mContext.getString(R.string.bluetooth_pairing).equals(settingItemData.subTitle)) {
            settingItemViewHolder.mTitle.setAlpha(0.33f);
            settingItemViewHolder.mSummary.setAlpha(0.33f);
        }
        if (settingItemData.hasToggle) {
            this.mSwitch.setOnCheckedChangeListener(null);
            if (settingItemViewHolder.mSwitch.getVisibility() != 0) {
                settingItemViewHolder.mDivider.setVisibility(0);
                settingItemViewHolder.mSwitch.setVisibility(0);
                settingItemViewHolder.mSwitch.setChecked(settingItemData.isChecked);
                if (settingItemViewHolder.mTitle.getText().equals(this.mContext.getString(R.string.wifi_settings))) {
                    settingItemViewHolder.mHeadedDivider.setVisibility(0);
                }
            } else if (settingItemData.isForceUpdate) {
                settingItemData.isForceUpdate = false;
                settingItemViewHolder.mSwitch.setChecked(settingItemData.isChecked);
            }
            settingItemViewHolder.mSwitch.setOnCheckedChangeListener(settingItemData.switchListener);
        } else {
            settingItemViewHolder.mSwitch.setVisibility(8);
            settingItemViewHolder.mDivider.setVisibility(8);
        }
        settingItemViewHolder.mLayView.setOnClickListener(settingItemData.contentClickListener);
        settingItemViewHolder.mRowView.setOnClickListener(settingItemData.contentClickListener);
        settingItemViewHolder.mActionIcon.setOnClickListener(settingItemData.actionClickListener);
        Drawable drawable = settingItemData.titleDrawable;
        if (drawable != null) {
            settingItemViewHolder.mTitleIcon.setImageDrawable(drawable);
            settingItemViewHolder.mTitleIcon.setVisibility(0);
            if ("android.settings.panel.action.NT_BLUE_TOOTH".equals(((Activity) this.mContext).getIntent().getAction())) {
                ViewGroup.LayoutParams layoutParams = settingItemViewHolder.mTitleIcon.getLayoutParams();
                float dimension = this.mContext.getResources().getDimension(R.dimen.settings_panel_title_margin);
                int i3 = (int) (dimension + dimension);
                layoutParams.height = i3;
                layoutParams.width = i3;
                settingItemViewHolder.mTitleIcon.setLayoutParams(layoutParams);
            }
        } else {
            settingItemViewHolder.mTitleIcon.setVisibility(8);
        }
        settingItemViewHolder.mActionIcon.setImageDrawable(settingItemData.actionDrawable);
        ImageView imageView = settingItemViewHolder.mActionIcon;
        if (settingItemData.actionDrawable != null) {
            i2 = 0;
        }
        imageView.setVisibility(i2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<SettingItemData> list = this.mSettingListsData;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setLiveData(Context context, LiveData<List<SettingItemData>> liveData) {
        this.mContext = context;
        NtSettingsPanelActivity ntSettingsPanelActivity = (NtSettingsPanelActivity) context;
        if (this.mLiveData == liveData) {
            return;
        }
        this.mLiveData = liveData;
        this.mSettingListsData = liveData.getValue();
        this.mLiveData.observe(ntSettingsPanelActivity, new Observer<List<SettingItemData>>() { // from class: com.nt.settings.panel.SettingItemAdapter.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<SettingItemData> list) {
                SettingItemAdapter.this.mSettingListsData = list;
                SettingItemAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public void setPinnedLiveData(LifecycleOwner lifecycleOwner, LiveData<List<SettingItemData>> liveData) {
        if (liveData == null) {
            View view = this.mPinnedHeader;
            if (view == null) {
                return;
            }
            view.setVisibility(8);
            return;
        }
        this.mPinnedHeaderLiveData = liveData;
        List<SettingItemData> value = liveData.getValue();
        this.mPinnedHeaderSettingListsData = value;
        updatePinnedHeader(value);
        this.mPinnedHeaderLiveData.observe(lifecycleOwner, new Observer<List<SettingItemData>>() { // from class: com.nt.settings.panel.SettingItemAdapter.2
            @Override // androidx.lifecycle.Observer
            public void onChanged(List<SettingItemData> list) {
                SettingItemAdapter.this.mPinnedHeaderSettingListsData = list;
                SettingItemAdapter settingItemAdapter = SettingItemAdapter.this;
                settingItemAdapter.updatePinnedHeader(settingItemAdapter.mPinnedHeaderSettingListsData);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePinnedHeader(List<SettingItemData> list) {
        if (this.mPinnedHeader != null) {
            if (list != null && list.size() > 0) {
                this.mPinnedHeader.setVisibility(0);
                for (SettingItemData settingItemData : list) {
                    this.mTitle.setText(settingItemData.title);
                    if (TextUtils.isEmpty(settingItemData.subTitle)) {
                        this.mSummary.setVisibility(8);
                    } else {
                        this.mSummary.setVisibility(0);
                        this.mSummary.setText(settingItemData.subTitle);
                    }
                    if (this.mTitle.getText().equals(this.mContext.getString(R.string.wifi_settings))) {
                        this.mHeadedDivider.setVisibility(0);
                    }
                    if (settingItemData.hasToggle) {
                        this.mSwitch.setVisibility(0);
                        this.mSwitch.setOnCheckedChangeListener(null);
                        this.mSwitch.setChecked(settingItemData.isChecked);
                        this.mSwitch.setOnCheckedChangeListener(settingItemData.switchListener);
                        this.mDivider.setVisibility(0);
                    } else {
                        this.mSwitch.setVisibility(8);
                        this.mDivider.setVisibility(8);
                    }
                    this.mPinnedHeader.setOnClickListener(settingItemData.contentClickListener);
                    Drawable drawable = settingItemData.titleDrawable;
                    if (drawable != null) {
                        this.mTitleIcon.setImageDrawable(drawable);
                        this.mTitleIcon.setVisibility(0);
                    } else {
                        this.mTitleIcon.setVisibility(8);
                    }
                    this.mActionIcon.setImageDrawable(settingItemData.actionDrawable);
                    this.mActionIcon.setVisibility(settingItemData.actionDrawable != null ? 0 : 8);
                }
                return;
            }
            this.mPinnedHeader.setVisibility(8);
        }
    }

    /* loaded from: classes2.dex */
    public class SettingItemViewHolder extends RecyclerView.ViewHolder {
        public Switch mSwitch;
        public LinearLayout mLayView = (LinearLayout) this.itemView.findViewById(R.id.lay_view);
        public View mRowView = this.itemView.findViewById(R.id.row_view);
        public View mHeadedDivider = this.itemView.findViewById(R.id.headed_divider);
        public TextView mTitle = (TextView) this.itemView.findViewById(R.id.title);
        public TextView mSummary = (TextView) this.itemView.findViewById(R.id.summary);
        public ImageView mTitleIcon = (ImageView) this.itemView.findViewById(R.id.icon_title);
        public View mDivider = this.itemView.findViewById(R.id.divider);
        public ImageView mActionIcon = (ImageView) this.itemView.findViewById(R.id.icon_action);

        public SettingItemViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nt_panel_setting_item_row, viewGroup, false));
            Switch r3 = (Switch) this.itemView.findViewById(R.id.switch_widget);
            this.mSwitch = r3;
            r3.setVisibility(8);
        }
    }

    public void setTeslaView(View view, boolean z) {
        this.mTeslaView = view;
        if (view != null) {
            boolean z2 = false;
            view.setVisibility(z ? 0 : 8);
            this.mTeslaIconExpand = (ImageView) view.findViewById(R.id.expand_icon);
            this.mTeslaLoadingView = view.findViewById(R.id.loading_progress_bar);
            this.mTeslaUserIcon = (ImageView) view.findViewById(R.id.car_image);
            this.mTeslaExpandViewParent = view.findViewById(R.id.expandeable_control_panel_parent);
            this.mTeslaCmdParentView = (LinearLayout) view.findViewById(R.id.control_panel);
            this.mTeslaIconTempDown = (ImageView) view.findViewById(R.id.climate_down);
            this.mTeslaIconTempUp = (ImageView) view.findViewById(R.id.climate_up);
            this.mTeslaTempText = (TextView) view.findViewById(R.id.climate_temp);
            this.mTeslaUserName = (TextView) view.findViewById(R.id.car_display_name);
            this.mTeslaRange = (TextView) view.findViewById(R.id.car_range);
            if (this.mTeslaExpandViewParent.getVisibility() == 0) {
                z2 = true;
            }
            this.mTeslaExpanded = z2;
            updateExpandIcon(z2);
            this.mTeslaIconExpand.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.panel.SettingItemAdapter.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    SettingItemAdapter.this.mTeslaExpandViewParent.setVisibility(SettingItemAdapter.this.mTeslaExpanded ? 8 : 0);
                    SettingItemAdapter settingItemAdapter = SettingItemAdapter.this;
                    settingItemAdapter.mTeslaExpanded = !settingItemAdapter.mTeslaExpanded;
                    SettingItemAdapter settingItemAdapter2 = SettingItemAdapter.this;
                    settingItemAdapter2.updateExpandIcon(settingItemAdapter2.mTeslaExpanded);
                }
            });
            this.mTeslaIconTempUp.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.panel.SettingItemAdapter.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    SettingItemAdapter settingItemAdapter = SettingItemAdapter.this;
                    if (!settingItemAdapter.tempInNormalRange(settingItemAdapter.currentTemp)) {
                        SettingItemAdapter.this.mTeslaIconTempUp.setEnabled(false);
                        return;
                    }
                    SettingItemAdapter.this.currentTemp += SettingItemAdapter.this.tempStep;
                    String valueOf = String.valueOf(SettingItemAdapter.this.currentTemp);
                    SettingItemAdapter.this.mTeslaPlugin.sendCmd(9, valueOf);
                    SettingItemAdapter.this.calculateTemp(valueOf);
                }
            });
            this.mTeslaIconTempDown.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.panel.SettingItemAdapter.5
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    SettingItemAdapter settingItemAdapter = SettingItemAdapter.this;
                    if (!settingItemAdapter.tempInNormalRange(settingItemAdapter.currentTemp)) {
                        SettingItemAdapter.this.mTeslaIconTempDown.setEnabled(false);
                        return;
                    }
                    SettingItemAdapter.this.currentTemp -= SettingItemAdapter.this.tempStep;
                    String valueOf = String.valueOf(SettingItemAdapter.this.currentTemp);
                    SettingItemAdapter.this.mTeslaPlugin.sendCmd(8, valueOf);
                    SettingItemAdapter.this.calculateTemp(valueOf);
                }
            });
        }
        bindTeslaData(this.mTeslaCmdList);
    }

    public void setupTeslaPlugin(TeslaConnectPanelPlugin teslaConnectPanelPlugin) {
        this.mTeslaPlugin = teslaConnectPanelPlugin;
    }

    public void bindTeslaData(CmdObjectList cmdObjectList) {
        List<CmdObject> cmdObjects;
        if (cmdObjectList == null || (cmdObjects = cmdObjectList.getCmdObjects()) == null || cmdObjects.size() <= 0) {
            return;
        }
        View view = this.mTeslaLoadingView;
        if (view != null) {
            view.setVisibility(8);
        }
        this.mTeslaIconExpand.setVisibility(0);
        this.mTeslaCmdParentView.removeAllViews();
        for (CmdObject cmdObject : cmdObjects) {
            if (cmdObject.getType() == 1) {
                this.mTeslaUserName.setText(cmdObject.getTitle());
                this.mTeslaRange.setText(cmdObject.getSubTitle());
            } else if (cmdObject.getType() == 2) {
                String title = cmdObject.getTitle();
                if (title != null) {
                    try {
                        this.currentTemp = Float.valueOf(title).floatValue();
                    } catch (Exception unused) {
                    }
                }
                TextView textView = this.mTeslaTempText;
                textView.setText(title + " ℃");
                boolean tempInNormalRange = tempInNormalRange(this.currentTemp);
                this.mTeslaIconTempDown.setEnabled(tempInNormalRange);
                this.mTeslaIconTempUp.setEnabled(tempInNormalRange);
            } else {
                try {
                    final View inflate = LayoutInflater.from(this.mTeslaCmdParentView.getContext()).inflate(R.layout.tesla_cmd_item, (ViewGroup) null, false);
                    updateIconStatus(cmdObject.getStatus(), cmdObject.getCmd(), (ImageView) inflate.findViewById(R.id.cmd_icon));
                    ((TextView) inflate.findViewById(R.id.cmd_title)).setText(cmdObject.getTitle());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
                    layoutParams.weight = 1.0f;
                    inflate.setId(cmdObject.getCmd());
                    inflate.setTag(cmdObject);
                    inflate.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.panel.SettingItemAdapter.6
                        @Override // android.view.View.OnClickListener
                        public void onClick(View view2) {
                            boolean status = ((CmdObject) inflate.getTag()).getStatus();
                            SettingItemAdapter.this.mTeslaPlugin.sendCmd(view2.getId(), "");
                            SettingItemAdapter.this.updateIconStatus(status, view2.getId(), (ImageView) view2.findViewById(R.id.cmd_icon));
                        }
                    });
                    this.mTeslaCmdParentView.addView(inflate, layoutParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateIconStatus(boolean z, int i, ImageView imageView) {
        Integer num = TeslaConnectPanelPlugin.CMD_ICON_ON.get(Integer.valueOf(i));
        Integer num2 = TeslaConnectPanelPlugin.CMD_ICON_MAPPING.get(Integer.valueOf(i));
        if (z) {
            imageView.setBackgroundResource(R.drawable.tesla_cmd_item_background_selected);
            if (num == null) {
                return;
            }
            imageView.setImageResource(num.intValue());
            return;
        }
        imageView.setBackgroundResource(R.drawable.tesla_cmd_item_background);
        if (num2 == null) {
            return;
        }
        imageView.setImageResource(num2.intValue());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean tempInNormalRange(float f) {
        return f > this.minTemp && f < this.maxTemp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void calculateTemp(String str) {
        TextView textView = this.mTeslaTempText;
        textView.setText(str + " ℃");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateExpandIcon(boolean z) {
        if (z) {
            this.mTeslaIconExpand.setImageResource(R.drawable.ic_tesla_panel_up);
        } else {
            this.mTeslaIconExpand.setImageResource(R.drawable.ic_tesla_panel_down);
        }
    }
}
