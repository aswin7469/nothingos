package com.nt.settings.display.RAngleAdjust;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settings.R;
import com.android.settingslib.utils.ThreadUtils;
import com.nt.common.widget.PartitionItemLayout;
import com.nt.common.widget.SingleArrayPartitionAdapter;
import com.nt.settings.applications.AppIconMemoryOptimizeHelper;
import com.nt.settings.utils.NtSettingsVibrateUtils;
import com.nt.settings.widget.NtAppEntry;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes2.dex */
public class NtRAngleAdjustAdapter extends SingleArrayPartitionAdapter<NtAppEntry> {
    private AppIconMemoryOptimizeHelper mAppIconMemoryOptimizeHelper;
    private HashMap<String, String> mAppNames;
    private Context mContext;
    private final int[] mCounts;
    private LayoutInflater mInflate;
    private NtRAngleAdjustController mNtRAngleAdustHelper;

    /* loaded from: classes2.dex */
    public static class AppViewHolder {
        ImageView appIconImage;
        TextView appNameText;
        View descriptionLayout;
        TextView emptyTextView;
        View itemsLayout;
        Switch rAngleAdustSwitch;
    }

    public NtRAngleAdjustAdapter(Context context, List<NtAppEntry> list, int[] iArr, HashMap<String, String> hashMap, AppIconMemoryOptimizeHelper appIconMemoryOptimizeHelper) {
        super(context, list, iArr);
        int[] iArr2 = {0, 0};
        this.mCounts = iArr2;
        this.mAppNames = new HashMap<>();
        this.mContext = context;
        this.mInflate = LayoutInflater.from(context);
        this.mNtRAngleAdustHelper = NtRAngleAdjustController.getInstance(this.mContext);
        this.mAppNames = hashMap;
        iArr2[0] = iArr[0];
        iArr2[1] = iArr[1];
        this.mAppIconMemoryOptimizeHelper = appIconMemoryOptimizeHelper;
    }

    @Override // com.nt.common.widget.BasePartitionAdapter
    protected View newHeaderView(Context context, int i, int i2, ViewGroup viewGroup) {
        return this.mInflate.inflate(R.layout.dashboard_category_title, viewGroup, false);
    }

    @Override // com.nt.common.widget.BasePartitionAdapter
    protected void bindHeaderView(View view, Context context, int i, int i2) {
        view.setLayoutParams(new AbsListView.LayoutParams(view.getWidth(), 1));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nt.common.widget.SingleArrayPartitionAdapter
    public View newView(Context context, int i, int i2, NtAppEntry ntAppEntry, int i3, int i4, ViewGroup viewGroup) {
        PartitionItemLayout partitionItemLayout = (PartitionItemLayout) this.mInflate.inflate(R.layout.mc_group_list_item_layout, viewGroup, false);
        this.mInflate.inflate(R.layout.r_angle_settings, (ViewGroup) partitionItemLayout, true);
        AppViewHolder appViewHolder = new AppViewHolder();
        appViewHolder.descriptionLayout = partitionItemLayout.findViewById(R.id.r_angle_description_layout);
        appViewHolder.itemsLayout = partitionItemLayout.findViewById(R.id.r_angle_item_layout);
        appViewHolder.appNameText = (TextView) partitionItemLayout.findViewById(R.id.app_name);
        appViewHolder.rAngleAdustSwitch = (Switch) partitionItemLayout.findViewById(R.id.r_angle_switch);
        appViewHolder.appIconImage = (ImageView) partitionItemLayout.findViewById(R.id.app_icon);
        appViewHolder.emptyTextView = (TextView) appViewHolder.descriptionLayout.findViewById(R.id.empty_text);
        partitionItemLayout.setTag(appViewHolder);
        return partitionItemLayout;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.nt.common.widget.SingleArrayPartitionAdapter
    public void bindView(View view, Context context, int i, int i2, NtAppEntry ntAppEntry, int i3, int i4) {
        AppViewHolder appViewHolder = (AppViewHolder) view.getTag();
        if (i2 == 0) {
            appViewHolder.descriptionLayout.setEnabled(false);
            appViewHolder.descriptionLayout.setOnClickListener(null);
            appViewHolder.descriptionLayout.setVisibility(0);
            appViewHolder.itemsLayout.setVisibility(8);
            if (this.mCounts[1] == 0) {
                appViewHolder.emptyTextView.setVisibility(0);
                return;
            } else {
                appViewHolder.emptyTextView.setVisibility(8);
                return;
            }
        }
        initItemView(appViewHolder, ntAppEntry);
    }

    private void initItemView(final AppViewHolder appViewHolder, final NtAppEntry ntAppEntry) {
        appViewHolder.itemsLayout.setVisibility(0);
        appViewHolder.descriptionLayout.setVisibility(8);
        if (ntAppEntry == null || ntAppEntry.mInfo == null) {
            return;
        }
        final Switch r0 = appViewHolder.rAngleAdustSwitch;
        appViewHolder.itemsLayout.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.display.RAngleAdjust.NtRAngleAdjustAdapter$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NtRAngleAdjustAdapter.this.lambda$initItemView$0(ntAppEntry, r0, view);
            }
        });
        final String str = ntAppEntry.mInfo.packageName;
        if (ntAppEntry.getIconLoaded() != null) {
            appViewHolder.appIconImage.setImageDrawable(ntAppEntry.getIconLoaded());
        } else {
            ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.display.RAngleAdjust.NtRAngleAdjustAdapter.1
                @Override // java.lang.Runnable
                public void run() {
                    ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.display.RAngleAdjust.NtRAngleAdjustAdapter.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                            appViewHolder.appIconImage.setImageDrawable(ntAppEntry.getIcon());
                        }
                    });
                }
            });
        }
        String str2 = this.mAppNames.get(str);
        TextView textView = appViewHolder.appNameText;
        if (str2 != null && str2.contains("|")) {
            textView.setText(str2);
        } else {
            textView.setText(ntAppEntry.getLabel());
        }
        appViewHolder.rAngleAdustSwitch.setOnCheckedChangeListener(null);
        if (ntAppEntry.mIsRAngleAdjust) {
            r0.setChecked(true);
        } else {
            r0.setChecked(false);
        }
        appViewHolder.rAngleAdustSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.nt.settings.display.RAngleAdjust.NtRAngleAdjustAdapter.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                Log.d("NtRAngleAdjustAdapter", "@_@ ---------- onCheckedChanged " + z);
                ntAppEntry.mIsRAngleAdjust = z;
                NtRAngleAdjustAdapter.this.mNtRAngleAdustHelper.setRAngleUnadustOpened(str, z, true);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initItemView$0(NtAppEntry ntAppEntry, Switch r3, View view) {
        Log.d("NtRAngleAdjustAdapter", "@_@ ---------- click item " + ntAppEntry.mIsRAngleAdjust + ", " + r3.isChecked());
        r3.setChecked(r3.isChecked() ^ true);
        NtSettingsVibrateUtils.getInstance(this.mContext).playSwitchVibrate();
    }
}
