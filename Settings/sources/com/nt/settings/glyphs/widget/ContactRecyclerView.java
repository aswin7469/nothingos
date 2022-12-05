package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.nt.settings.glyphs.utils.ContactsManager;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class ContactRecyclerView extends RecyclerView {
    private ContactAdapter mContactAdapter;
    private OnItemClickListener mListener;
    private ContactsManager.Contact mSearchResultTip;

    /* loaded from: classes2.dex */
    public interface OnItemClickListener {
        void onItemClick(View view, ContactsManager.Contact contact, int i);
    }

    public ContactRecyclerView(Context context) {
        this(context, null);
    }

    public ContactRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ContactRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        setLayoutManager(new LinearLayoutManager(context, 1, false));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    public void setContactList(List<ContactsManager.Contact> list) {
        if (this.mContactAdapter == null) {
            ContactAdapter contactAdapter = new ContactAdapter();
            this.mContactAdapter = contactAdapter;
            setAdapter(contactAdapter);
        }
        this.mContactAdapter.setData(list);
    }

    public void search(String str) {
        ContactAdapter contactAdapter = this.mContactAdapter;
        if (contactAdapter == null) {
            return;
        }
        contactAdapter.search(str);
    }

    /* loaded from: classes2.dex */
    public class ContactAdapter extends RecyclerView.Adapter {
        private List<ContactsManager.Contact> mContactList = new ArrayList();
        private List<ContactsManager.Contact> mTotalContactList;

        public ContactAdapter() {
        }

        public void setData(List<ContactsManager.Contact> list) {
            this.mTotalContactList = list;
            this.mContactList.clear();
            this.mContactList.addAll(list);
            notifyDataSetChanged();
        }

        public void search(String str) {
            String[] split;
            List<ContactsManager.Contact> list = this.mTotalContactList;
            if (list == null || list.size() == 0) {
                return;
            }
            this.mContactList.clear();
            this.mContactList.add(getResultTitle());
            for (ContactsManager.Contact contact : this.mTotalContactList) {
                if (this.mTotalContactList != null && contact.getItemType() == 2) {
                    String displayName = contact.getDisplayName();
                    if (!TextUtils.isEmpty(displayName) && (split = displayName.split(" ")) != null && split.length != 0) {
                        int length = split.length;
                        int i = 0;
                        while (true) {
                            if (i >= length) {
                                break;
                            } else if (split[i].toLowerCase().startsWith(str.toLowerCase())) {
                                this.mContactList.add(contact);
                                break;
                            } else {
                                i++;
                            }
                        }
                    }
                }
            }
            notifyDataSetChanged();
        }

        private ContactsManager.Contact getResultTitle() {
            if (ContactRecyclerView.this.mSearchResultTip != null) {
                return ContactRecyclerView.this.mSearchResultTip;
            }
            ContactRecyclerView.this.mSearchResultTip = new ContactsManager.Contact();
            ContactRecyclerView.this.mSearchResultTip.setItemType(1);
            ContactRecyclerView.this.mSearchResultTip.setDisplayName(ContactRecyclerView.this.getContext().getResources().getString(R.string.nt_glyphs_contact_search_tip));
            return ContactRecyclerView.this.mSearchResultTip;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public RecyclerView.ViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == 0) {
                final DefaultRingtoneViewHolder defaultRingtoneViewHolder = new DefaultRingtoneViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nt_glyphs_default_ringtone, viewGroup, false));
                defaultRingtoneViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.ContactRecyclerView.ContactAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (ContactRecyclerView.this.mListener == null) {
                            return;
                        }
                        ContactRecyclerView.this.mListener.onItemClick(view, (ContactsManager.Contact) ContactAdapter.this.mContactList.get(defaultRingtoneViewHolder.getAdapterPosition()), defaultRingtoneViewHolder.getAdapterPosition());
                    }
                });
                return defaultRingtoneViewHolder;
            } else if (i == 1) {
                return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nt_item_contact_header_list, viewGroup, false));
            } else {
                final ContactViewHolder contactViewHolder = new ContactViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nt_glyphs_item_contact_list, viewGroup, false));
                contactViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.ContactRecyclerView.ContactAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (ContactRecyclerView.this.mListener == null) {
                            return;
                        }
                        ContactRecyclerView.this.mListener.onItemClick(view, (ContactsManager.Contact) ContactAdapter.this.mContactList.get(contactViewHolder.getAdapterPosition()), contactViewHolder.getAdapterPosition());
                    }
                });
                return contactViewHolder;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) viewHolder).mTvName.setText(this.mContactList.get(i).getDisplayName());
            } else if (viewHolder instanceof ContactViewHolder) {
                ContactViewHolder contactViewHolder = (ContactViewHolder) viewHolder;
                contactViewHolder.mTvContactName.setText(this.mContactList.get(i).getDisplayName());
                contactViewHolder.mTvRingtoneName.setText(this.mContactList.get(i).getRingtoneTitle() == null ? "" : this.mContactList.get(i).getRingtoneTitle());
            } else if (!(viewHolder instanceof DefaultRingtoneViewHolder)) {
            } else {
                DefaultRingtoneViewHolder defaultRingtoneViewHolder = (DefaultRingtoneViewHolder) viewHolder;
                defaultRingtoneViewHolder.mTvTitle.setText(this.mContactList.get(i).getDisplayName());
                defaultRingtoneViewHolder.mTvSummary.setText(this.mContactList.get(i).getRingtoneTitle());
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return this.mContactList.get(i).getItemType();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List<ContactsManager.Contact> list = this.mContactList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }
    }

    /* loaded from: classes2.dex */
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvContactName;
        public TextView mTvRingtoneName;

        public ContactViewHolder(View view) {
            super(view);
            this.mTvContactName = (TextView) view.findViewById(R.id.tv_contact_name);
            this.mTvRingtoneName = (TextView) view.findViewById(R.id.tv_ringtone_name);
        }
    }

    /* loaded from: classes2.dex */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvName;

        public HeaderViewHolder(View view) {
            super(view);
            this.mTvName = (TextView) view.findViewById(R.id.tv_name);
        }
    }

    /* loaded from: classes2.dex */
    public class DefaultRingtoneViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIcon;
        public TextView mTvSummary;
        public TextView mTvTitle;

        public DefaultRingtoneViewHolder(View view) {
            super(view);
            this.mIcon = (ImageView) view.findViewById(R.id.icon);
            this.mTvTitle = (TextView) view.findViewById(R.id.tv_title);
            this.mTvSummary = (TextView) view.findViewById(R.id.tv_summary);
        }
    }
}
