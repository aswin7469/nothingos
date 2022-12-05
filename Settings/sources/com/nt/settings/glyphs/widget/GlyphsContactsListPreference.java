package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.nt.settings.glyphs.utils.ContactsManager;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsContactsListPreference extends Preference {
    private List<ContactsManager.Contact> mContactsList;
    private OnItemClickListener mListener;

    /* loaded from: classes2.dex */
    public interface OnItemClickListener {
        void onItemClick(RecyclerView.Adapter adapter, View view, int i);
    }

    public GlyphsContactsListPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.nt_glyphs_contacts_list);
    }

    public GlyphsContactsListPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.nt_glyphs_contacts_list);
    }

    public GlyphsContactsListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.nt_glyphs_contacts_list);
    }

    public GlyphsContactsListPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.nt_glyphs_contacts_list);
    }

    public void setContactsList(List<ContactsManager.Contact> list) {
        this.mContactsList = list;
        notifyChanged();
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        RecyclerView recyclerView = (RecyclerView) preferenceViewHolder.findViewById(R.id.apps_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        linearLayoutManager.setOrientation(1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new ContactListAdapter(recyclerView.getContext(), this.mContactsList));
        recyclerView.setVisibility(0);
    }

    /* loaded from: classes2.dex */
    public class ContactListAdapter extends RecyclerView.Adapter<ContactViewHolder> {
        private List<ContactsManager.Contact> mContactLists;
        private LayoutInflater mInflater;

        public ContactListAdapter(Context context, List<ContactsManager.Contact> list) {
            this.mContactLists = list;
            this.mInflater = LayoutInflater.from(context);
        }

        public List<ContactsManager.Contact> getmContactLists() {
            return this.mContactLists;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public ContactViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ContactViewHolder(this.mInflater.inflate(R.layout.nt_glyphs_item_contact_list, (ViewGroup) null));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(final ContactViewHolder contactViewHolder, int i) {
            contactViewHolder.tvAppName.setText(this.mContactLists.get(i).getDisplayName());
            contactViewHolder.tvRingtoneName.setText(this.mContactLists.get(i).getRingtoneTitle() == null ? "" : this.mContactLists.get(i).getRingtoneTitle());
            contactViewHolder.tvRingtoneName.setVisibility(TextUtils.isEmpty(this.mContactLists.get(i).getRingtoneTitle()) ? 8 : 0);
            contactViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.GlyphsContactsListPreference.ContactListAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (GlyphsContactsListPreference.this.mListener == null) {
                        return;
                    }
                    GlyphsContactsListPreference.this.mListener.onItemClick(ContactListAdapter.this, view, contactViewHolder.getAdapterPosition());
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List<ContactsManager.Contact> list = this.mContactLists;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        /* loaded from: classes2.dex */
        public class ContactViewHolder extends RecyclerView.ViewHolder {
            public TextView tvAppName;
            public TextView tvRingtoneName;

            public ContactViewHolder(View view) {
                super(view);
                this.tvAppName = (TextView) view.findViewById(R.id.tv_contact_name);
                this.tvRingtoneName = (TextView) view.findViewById(R.id.tv_ringtone_name);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }
}
