package com.android.settings.password;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
/* loaded from: classes.dex */
public class PasswordRequirementAdapter extends RecyclerView.Adapter<PasswordRequirementViewHolder> {
    private String[] mRequirements;

    public PasswordRequirementAdapter() {
        setHasStableIds(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public PasswordRequirementViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PasswordRequirementViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.password_requirement_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mRequirements.length;
    }

    public void setRequirements(String[] strArr) {
        this.mRequirements = strArr;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return this.mRequirements[i].hashCode();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(PasswordRequirementViewHolder passwordRequirementViewHolder, int i) {
        passwordRequirementViewHolder.mDescriptionText.setText(this.mRequirements[i]);
    }

    /* loaded from: classes.dex */
    public static class PasswordRequirementViewHolder extends RecyclerView.ViewHolder {
        private TextView mDescriptionText;

        public PasswordRequirementViewHolder(View view) {
            super(view);
            this.mDescriptionText = (TextView) view;
        }
    }
}
