package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsAppsListPreference extends Preference {
    public GlyphsAppsListPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.nt_glyphs_apps_list);
    }

    public GlyphsAppsListPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.nt_glyphs_apps_list);
    }

    public GlyphsAppsListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.nt_glyphs_apps_list);
    }

    public GlyphsAppsListPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.nt_glyphs_apps_list);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        RecyclerView recyclerView = (RecyclerView) preferenceViewHolder.findViewById(R.id.apps_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        linearLayoutManager.setOrientation(1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 20; i++) {
            arrayList.add("位置:" + i);
        }
        recyclerView.setAdapter(new AppListAdapter(recyclerView.getContext(), arrayList));
        recyclerView.setVisibility(0);
    }

    /* loaded from: classes2.dex */
    private class AppListAdapter extends RecyclerView.Adapter<AppsViewHolder> {
        private List<Object> appLists;
        private LayoutInflater inflater;

        public AppListAdapter(Context context, List<Object> list) {
            this.appLists = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public AppsViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new AppsViewHolder(this.inflater.inflate(R.layout.nt_glyphs_item_apps_list, (ViewGroup) null));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(AppsViewHolder appsViewHolder, int i) {
            appsViewHolder.tvAppName.setText(this.appLists.get(i).toString());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List<Object> list = this.appLists;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        /* loaded from: classes2.dex */
        public class AppsViewHolder extends RecyclerView.ViewHolder {
            public ImageView imgDel;
            public ImageView imgIcon;
            public TextView tvAppName;

            public AppsViewHolder(View view) {
                super(view);
                this.imgIcon = (ImageView) view.findViewById(R.id.img_app_icon);
                this.tvAppName = (TextView) view.findViewById(R.id.tv_app_name);
                this.imgDel = (ImageView) view.findViewById(R.id.img_del);
            }
        }
    }
}
