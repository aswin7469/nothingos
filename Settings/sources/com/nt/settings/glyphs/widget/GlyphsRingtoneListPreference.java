package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.nt.settings.glyphs.utils.MusicUtils;
import java.io.IOException;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsRingtoneListPreference extends Preference {
    private static OnSelectedListener mListener = null;
    private static List<MusicUtils.Song> mLists = null;
    private static MediaPlayer mMediaPlayer = null;
    private static int mSelectedPosition = -1;

    /* loaded from: classes2.dex */
    public interface OnSelectedListener {
        void onSelected(int i);
    }

    public GlyphsRingtoneListPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.nt_glyphs_ringtone_list);
    }

    public GlyphsRingtoneListPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.nt_glyphs_ringtone_list);
    }

    public GlyphsRingtoneListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.nt_glyphs_ringtone_list);
    }

    public GlyphsRingtoneListPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.nt_glyphs_ringtone_list);
    }

    public void setRingtonesList(List<MusicUtils.Song> list) {
        mLists = list;
        mSelectedPosition = -1;
        notifyChanged();
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        RecyclerView recyclerView = (RecyclerView) preferenceViewHolder.findViewById(R.id.rlv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        linearLayoutManager.setOrientation(1);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new RingtoneListAdapter(recyclerView.getContext(), mLists));
        recyclerView.setVisibility(0);
        DisplayMetrics displayMetrics = recyclerView.getContext().getResources().getDisplayMetrics();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        layoutParams.height = displayMetrics.heightPixels;
        recyclerView.setLayoutParams(layoutParams);
    }

    /* loaded from: classes2.dex */
    public static class RingtoneListAdapter extends RecyclerView.Adapter<RingtoneViewHolder> {
        private LayoutInflater inflater;
        private List<MusicUtils.Song> mLists;

        public RingtoneListAdapter(Context context, List<MusicUtils.Song> list) {
            this.mLists = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public RingtoneViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new RingtoneViewHolder(this.inflater.inflate(R.layout.nt_glyphs_item_ringtone_list, (ViewGroup) null));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(final RingtoneViewHolder ringtoneViewHolder, int i) {
            ringtoneViewHolder.cbFileName.setText(this.mLists.get(i).song);
            ringtoneViewHolder.cbFileName.setChecked(this.mLists.get(ringtoneViewHolder.getAdapterPosition()).isChecked);
            ringtoneViewHolder.cbFileName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.nt.settings.glyphs.widget.GlyphsRingtoneListPreference.RingtoneListAdapter.1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (!z || ringtoneViewHolder.getAdapterPosition() < 0 || z == ((MusicUtils.Song) RingtoneListAdapter.this.mLists.get(ringtoneViewHolder.getAdapterPosition())).isChecked) {
                        return;
                    }
                    ((MusicUtils.Song) RingtoneListAdapter.this.mLists.get(ringtoneViewHolder.getAdapterPosition())).isChecked = z;
                    if (GlyphsRingtoneListPreference.mSelectedPosition > -1) {
                        ((MusicUtils.Song) RingtoneListAdapter.this.mLists.get(GlyphsRingtoneListPreference.mSelectedPosition)).isChecked = false;
                    }
                    int unused = GlyphsRingtoneListPreference.mSelectedPosition = ringtoneViewHolder.getAdapterPosition();
                    RingtoneListAdapter.this.notifyDataSetChanged();
                    if (GlyphsRingtoneListPreference.mSelectedPosition > -1) {
                        GlyphsRingtoneListPreference.playMusic(compoundButton.getContext(), Uri.parse(((MusicUtils.Song) RingtoneListAdapter.this.mLists.get(GlyphsRingtoneListPreference.mSelectedPosition)).path));
                    }
                    if (GlyphsRingtoneListPreference.mListener == null) {
                        return;
                    }
                    GlyphsRingtoneListPreference.mListener.onSelected(GlyphsRingtoneListPreference.mSelectedPosition);
                }
            });
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List<MusicUtils.Song> list = this.mLists;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        /* loaded from: classes2.dex */
        public class RingtoneViewHolder extends RecyclerView.ViewHolder {
            public RadioButton cbFileName;

            public RingtoneViewHolder(View view) {
                super(view);
                this.cbFileName = (RadioButton) view.findViewById(R.id.cb_file_name);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void playMusic(Context context, Uri uri) {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(context, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mMediaPlayer.prepare();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        mMediaPlayer.start();
    }

    public void release() {
        MediaPlayer mediaPlayer = mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
