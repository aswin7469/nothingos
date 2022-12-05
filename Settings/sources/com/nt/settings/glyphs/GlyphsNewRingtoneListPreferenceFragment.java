package com.nt.settings.glyphs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.nt.settings.glyphs.utils.ContactsManager;
import com.nt.settings.glyphs.utils.LedSettingUtils;
import com.nt.settings.glyphs.utils.MusicUtils;
import com.nt.settings.glyphs.utils.ThreadUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
/* loaded from: classes2.dex */
public class GlyphsNewRingtoneListPreferenceFragment extends InstrumentedFragment implements MenuItem.OnActionExpandListener {
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER = new BaseSearchIndexProvider(R.xml.nt_glyphs_add_new_ringtones);
    private RingtoneListAdapter mAdapter;
    private List<MusicUtils.Song> mAllSongs;
    private MediaPlayer mMediaPlayer;
    private MenuInflater mMenuInflater;
    private Menu mOptionsMenu;
    private RecyclerView mRecyclerView;
    private View mRootView;

    /* loaded from: classes2.dex */
    public interface OnSelectedListener {
        void onSelected(int i);
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1845;
    }

    @Override // android.view.MenuItem.OnActionExpandListener
    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        return false;
    }

    @Override // android.view.MenuItem.OnActionExpandListener
    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        return false;
    }

    @Override // com.android.settings.core.InstrumentedFragment, com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override // com.android.settings.core.InstrumentedFragment, com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        if (getActivity() == null) {
            return;
        }
        this.mOptionsMenu = menu;
        this.mMenuInflater = menuInflater;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        ((SettingsActivity) getActivity()).setTitle(getActivity().getString(R.string.nt_glyphs_add_new_ringtone_title));
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.nt_glyphs_ringtone_list, (ViewGroup) null);
        this.mRootView = inflate;
        this.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.rlv_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(1);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);
        updateData(getActivity());
        return this.mRootView;
    }

    private void updateData(final Context context) {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsNewRingtoneListPreferenceFragment$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsNewRingtoneListPreferenceFragment.this.lambda$updateData$1(context);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateData$1(final Context context) {
        this.mAllSongs = MusicUtils.getMusicData(context);
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.glyphs.GlyphsNewRingtoneListPreferenceFragment$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                GlyphsNewRingtoneListPreferenceFragment.this.lambda$updateData$0(context);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateData$0(Context context) {
        MusicUtils.Song song = new MusicUtils.Song();
        song.song = getResources().getString(R.string.nt_glyphs_add_new_ringtone_tip);
        this.mAllSongs.add(0, song);
        RingtoneListAdapter ringtoneListAdapter = new RingtoneListAdapter(context, this.mAllSongs);
        this.mAdapter = ringtoneListAdapter;
        ringtoneListAdapter.setOnSelectedListener(new OnSelectedListener() { // from class: com.nt.settings.glyphs.GlyphsNewRingtoneListPreferenceFragment.1
            @Override // com.nt.settings.glyphs.GlyphsNewRingtoneListPreferenceFragment.OnSelectedListener
            public void onSelected(int i) {
                if (!GlyphsNewRingtoneListPreferenceFragment.this.mOptionsMenu.hasVisibleItems()) {
                    GlyphsNewRingtoneListPreferenceFragment.this.mMenuInflater.inflate(R.menu.app_ringtone_selector, GlyphsNewRingtoneListPreferenceFragment.this.mOptionsMenu);
                }
            }
        });
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setVisibility(0);
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_menu) {
            if (this.mAdapter.getSelectedSong() == null) {
                return super.onOptionsItemSelected(menuItem);
            }
            showCustomRingtoneTypeDialog();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public Dialog onCreateDialog(int i) {
        if (i != 1) {
            return null;
        }
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.nt_glyphs_dialog_use_sound_only_title).setMessage(R.string.nt_glyphs_dialog_use_sound_only_message).setNegativeButton(R.string.nt_glyphs_ringtone_sound_only, new DialogInterface.OnClickListener() { // from class: com.nt.settings.glyphs.GlyphsNewRingtoneListPreferenceFragment.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                GlyphsNewRingtoneListPreferenceFragment.this.selectSoundOnly();
            }
        }).setPositiveButton(R.string.nt_glyphs_ringtone_sound_and_glyphs, new DialogInterface.OnClickListener() { // from class: com.nt.settings.glyphs.GlyphsNewRingtoneListPreferenceFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                GlyphsNewRingtoneListPreferenceFragment.this.selectSoundAndGlyphs();
            }
        }).create();
    }

    private void showCustomRingtoneTypeDialog() {
        Dialog onCreateDialog = onCreateDialog(1);
        if (onCreateDialog != null) {
            onCreateDialog.show();
        }
    }

    private void saveContactRingtoneMode(String str, String str2, String str3, String str4, String str5, int i, boolean z) {
        Uri saveAsRingtone;
        if (z) {
            saveAsRingtone = MediaStore.scanFile(getActivity().getContentResolver(), new File(str4));
        } else {
            saveAsRingtone = LedSettingUtils.saveAsRingtone(getActivity(), str4, str, 1);
        }
        if (saveAsRingtone != null) {
            String ringtoneTitle = MusicUtils.getRingtoneTitle(getActivity(), saveAsRingtone);
            FragmentActivity activity = getActivity();
            LedSettingUtils.setContactLed(activity, str, str2, str3, saveAsRingtone.toString() + "?title=" + ringtoneTitle, str5, i);
            if (!TextUtils.isEmpty(str5)) {
                ContactsManager contactsManager = ContactsManager.getInstance();
                FragmentActivity activity2 = getActivity();
                contactsManager.updateContactRingtone(activity2, str5, saveAsRingtone.toString() + "?title=" + ringtoneTitle);
            } else {
                Settings.Global.putInt(getActivity().getContentResolver(), "led_default_ringtone_sound_mode", i);
                RingtoneManager.setActualDefaultRingtoneUri(getActivity(), 1, saveAsRingtone);
            }
        } else {
            Log.d("NewRingtoneList", "fail to add ringtone");
        }
        getActivity().finishAffinity();
    }

    private void saveNotificationRingtoneMode(String str, String str2, String str3, String str4, int i, boolean z) {
        Uri saveAsRingtone;
        if (z) {
            saveAsRingtone = MediaStore.scanFile(getActivity().getContentResolver(), new File(str4));
        } else {
            saveAsRingtone = LedSettingUtils.saveAsRingtone(getActivity(), str4, str, 2);
        }
        RingtoneManager.setActualDefaultRingtoneUri(getActivity(), 2, saveAsRingtone);
        Settings.Global.putInt(getActivity().getContentResolver(), "led_notification_mode", i);
        getActivity().finishAffinity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectSoundOnly() {
        saveCustomRingtone(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectSoundAndGlyphs() {
        saveCustomRingtone(0);
    }

    private void saveCustomRingtone(int i) {
        MusicUtils.Song selectedSong = this.mAdapter.getSelectedSong();
        if (isContactRingtone(getActivity().getIntent())) {
            saveContactRingtoneMode(selectedSong.song, "", "", selectedSong.path, getActivity().getIntent().getStringExtra("contact_id"), i, selectedSong.isRingtone);
        } else {
            saveNotificationRingtoneMode(selectedSong.song, "", "", selectedSong.path, i, selectedSong.isNotificationRing);
        }
    }

    private boolean isContactRingtone(Intent intent) {
        return intent == null || intent.getIntExtra("android.intent.extra.ringtone.TYPE", 1) == 1;
    }

    /* loaded from: classes2.dex */
    public class RingtoneListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;
        private OnSelectedListener mListener;
        private List<MusicUtils.Song> mLists;
        private int mSelectedPosition = -1;

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return i == 0 ? 0 : 1;
        }

        public RingtoneListAdapter(Context context, List<MusicUtils.Song> list) {
            this.mLists = list;
            this.inflater = LayoutInflater.from(context);
        }

        public MusicUtils.Song getSelectedSong() {
            int i = this.mSelectedPosition;
            if (i <= -1 || i >= this.mLists.size()) {
                return null;
            }
            return this.mLists.get(this.mSelectedPosition);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public RecyclerView.ViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
            if (i == 0) {
                return new HeaderViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nt_item_contact_header_list, viewGroup, false));
            }
            return new RingtoneViewHolder(this.inflater.inflate(R.layout.nt_glyphs_item_ringtone_list, (ViewGroup) null));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
            if (viewHolder instanceof HeaderViewHolder) {
                ((HeaderViewHolder) viewHolder).mTvName.setText(this.mLists.get(i).song);
                return;
            }
            RingtoneViewHolder ringtoneViewHolder = (RingtoneViewHolder) viewHolder;
            ringtoneViewHolder.cbFileName.setText(this.mLists.get(i).song);
            ringtoneViewHolder.cbFileName.setChecked(this.mLists.get(viewHolder.getAdapterPosition()).isChecked);
            ((RingtoneViewHolder) viewHolder).cbFileName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.nt.settings.glyphs.GlyphsNewRingtoneListPreferenceFragment.RingtoneListAdapter.1
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (!z || viewHolder.getAdapterPosition() < 0 || z == ((MusicUtils.Song) RingtoneListAdapter.this.mLists.get(viewHolder.getAdapterPosition())).isChecked) {
                        return;
                    }
                    ((MusicUtils.Song) RingtoneListAdapter.this.mLists.get(viewHolder.getAdapterPosition())).isChecked = z;
                    if (RingtoneListAdapter.this.mSelectedPosition > -1) {
                        ((MusicUtils.Song) RingtoneListAdapter.this.mLists.get(RingtoneListAdapter.this.mSelectedPosition)).isChecked = false;
                    }
                    RingtoneListAdapter.this.mSelectedPosition = viewHolder.getAdapterPosition();
                    RingtoneListAdapter.this.notifyDataSetChanged();
                    if (RingtoneListAdapter.this.mSelectedPosition > -1) {
                        GlyphsNewRingtoneListPreferenceFragment.this.playMusic(compoundButton.getContext(), Uri.parse(((MusicUtils.Song) RingtoneListAdapter.this.mLists.get(RingtoneListAdapter.this.mSelectedPosition)).path));
                    }
                    if (RingtoneListAdapter.this.mListener == null) {
                        return;
                    }
                    RingtoneListAdapter.this.mListener.onSelected(RingtoneListAdapter.this.mSelectedPosition);
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

        /* loaded from: classes2.dex */
        public class HeaderViewHolder extends RecyclerView.ViewHolder {
            public TextView mTvName;

            public HeaderViewHolder(View view) {
                super(view);
                this.mTvName = (TextView) view.findViewById(R.id.tv_name);
            }
        }

        public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
            this.mListener = onSelectedListener;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playMusic(Context context, Uri uri) {
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = new MediaPlayer();
        }
        this.mMediaPlayer.reset();
        try {
            this.mMediaPlayer.setDataSource(context, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            this.mMediaPlayer.prepare();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        this.mMediaPlayer.start();
    }
}
