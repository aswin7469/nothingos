package com.android.settingslib.users;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.util.UserIcons;
import com.android.settingslib.C1757R;
import com.android.settingslib.users.AvatarPhotoController;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.C3963R;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvatarPickerActivity extends Activity {
    static final String EXTRA_DEFAULT_ICON_TINT_COLOR = "default_icon_tint_color";
    static final String EXTRA_FILE_AUTHORITY = "file_authority";
    private static final String KEY_AWAITING_RESULT = "awaiting_result";
    private static final String KEY_SELECTED_POSITION = "selected_position";
    private AvatarAdapter mAdapter;
    /* access modifiers changed from: private */
    public AvatarPhotoController mAvatarPhotoController;
    /* access modifiers changed from: private */
    public FooterButton mDoneButton;
    private boolean mWaitingForActivityResult;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTheme(C3963R.style.SudThemeGlifV3_DayNight);
        ThemeHelper.trySetDynamicColor(this);
        setContentView(C1757R.layout.avatar_picker);
        setUpButtons();
        RecyclerView recyclerView = (RecyclerView) findViewById(C1757R.C1760id.avatar_grid);
        AvatarAdapter avatarAdapter = new AvatarAdapter();
        this.mAdapter = avatarAdapter;
        recyclerView.setAdapter(avatarAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(C1757R.integer.avatar_picker_columns)));
        restoreState(bundle);
        this.mAvatarPhotoController = new AvatarPhotoController(new AvatarPhotoController.AvatarUiImpl(this), new AvatarPhotoController.ContextInjectorImpl(this, getFileAuthority()), this.mWaitingForActivityResult);
    }

    private void setUpButtons() {
        FooterBarMixin footerBarMixin = (FooterBarMixin) ((GlifLayout) findViewById(C1757R.C1760id.glif_layout)).getMixin(FooterBarMixin.class);
        FooterButton build = new FooterButton.Builder(this).setText(getString(17039360)).setListener(new AvatarPickerActivity$$ExternalSyntheticLambda0(this)).build();
        FooterButton build2 = new FooterButton.Builder(this).setText(getString(C1757R.string.done)).setListener(new AvatarPickerActivity$$ExternalSyntheticLambda1(this)).build();
        this.mDoneButton = build2;
        build2.setEnabled(false);
        footerBarMixin.setSecondaryButton(build);
        footerBarMixin.setPrimaryButton(this.mDoneButton);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setUpButtons$0$com-android-settingslib-users-AvatarPickerActivity */
    public /* synthetic */ void mo29205x54065827(View view) {
        cancel();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setUpButtons$1$com-android-settingslib-users-AvatarPickerActivity */
    public /* synthetic */ void mo29206x81def286(View view) {
        this.mAdapter.returnSelectionResult();
    }

    private String getFileAuthority() {
        String stringExtra = getIntent().getStringExtra(EXTRA_FILE_AUTHORITY);
        if (stringExtra != null) {
            return stringExtra;
        }
        throw new IllegalStateException("File authority must be provided");
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        this.mWaitingForActivityResult = false;
        this.mAvatarPhotoController.onActivityResult(i, i2, intent);
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putBoolean(KEY_AWAITING_RESULT, this.mWaitingForActivityResult);
        bundle.putInt(KEY_SELECTED_POSITION, this.mAdapter.mSelectedPosition);
        super.onSaveInstanceState(bundle);
    }

    private void restoreState(Bundle bundle) {
        if (bundle != null) {
            boolean z = false;
            this.mWaitingForActivityResult = bundle.getBoolean(KEY_AWAITING_RESULT, false);
            int unused = this.mAdapter.mSelectedPosition = bundle.getInt(KEY_SELECTED_POSITION, -1);
            FooterButton footerButton = this.mDoneButton;
            if (this.mAdapter.mSelectedPosition != -1) {
                z = true;
            }
            footerButton.setEnabled(z);
        }
    }

    public void startActivityForResult(Intent intent, int i) {
        this.mWaitingForActivityResult = true;
        super.startActivityForResult(intent, i);
    }

    /* access modifiers changed from: package-private */
    public void returnUriResult(Uri uri) {
        Intent intent = new Intent();
        intent.setData(uri);
        setResult(-1, intent);
        finish();
    }

    /* access modifiers changed from: package-private */
    public void returnColorResult(int i) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DEFAULT_ICON_TINT_COLOR, i);
        setResult(-1, intent);
        finish();
    }

    private void cancel() {
        setResult(0);
        finish();
    }

    private class AvatarAdapter extends RecyclerView.Adapter<AvatarViewHolder> {
        private static final int NONE = -1;
        private final int mChoosePhotoPosition;
        private final List<String> mImageDescriptions;
        private final List<Drawable> mImageDrawables;
        private final int mPreselectedImageStartPosition;
        private final TypedArray mPreselectedImages;
        /* access modifiers changed from: private */
        public int mSelectedPosition = -1;
        private final int mTakePhotoPosition;
        private final int[] mUserIconColors;

        AvatarAdapter() {
            int i = -1;
            boolean canTakePhoto = PhotoCapabilityUtils.canTakePhoto(AvatarPickerActivity.this);
            boolean canChoosePhoto = PhotoCapabilityUtils.canChoosePhoto(AvatarPickerActivity.this);
            this.mTakePhotoPosition = canTakePhoto ? 0 : -1;
            this.mChoosePhotoPosition = canChoosePhoto ? canTakePhoto ? 1 : 0 : i;
            this.mPreselectedImageStartPosition = (canTakePhoto ? 1 : 0) + (canChoosePhoto ? 1 : 0);
            this.mPreselectedImages = AvatarPickerActivity.this.getResources().obtainTypedArray(C1757R.array.avatar_images);
            this.mUserIconColors = UserIcons.getUserIconColors(AvatarPickerActivity.this.getResources());
            this.mImageDrawables = buildDrawableList();
            this.mImageDescriptions = buildDescriptionsList();
        }

        public AvatarViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new AvatarViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(C1757R.layout.avatar_item, viewGroup, false));
        }

        public void onBindViewHolder(AvatarViewHolder avatarViewHolder, int i) {
            if (i == this.mTakePhotoPosition) {
                avatarViewHolder.setDrawable(AvatarPickerActivity.this.getDrawable(C1757R.C1759drawable.avatar_take_photo_circled));
                avatarViewHolder.setContentDescription(AvatarPickerActivity.this.getString(C1757R.string.user_image_take_photo));
                avatarViewHolder.setClickListener(new AvatarPickerActivity$AvatarAdapter$$ExternalSyntheticLambda0(this));
            } else if (i == this.mChoosePhotoPosition) {
                avatarViewHolder.setDrawable(AvatarPickerActivity.this.getDrawable(C1757R.C1759drawable.avatar_choose_photo_circled));
                avatarViewHolder.setContentDescription(AvatarPickerActivity.this.getString(C1757R.string.user_image_choose_photo));
                avatarViewHolder.setClickListener(new AvatarPickerActivity$AvatarAdapter$$ExternalSyntheticLambda1(this));
            } else if (i >= this.mPreselectedImageStartPosition) {
                int indexFromPosition = indexFromPosition(i);
                avatarViewHolder.setSelected(i == this.mSelectedPosition);
                avatarViewHolder.setDrawable(this.mImageDrawables.get(indexFromPosition));
                List<String> list = this.mImageDescriptions;
                if (list != null) {
                    avatarViewHolder.setContentDescription(list.get(indexFromPosition));
                } else {
                    avatarViewHolder.setContentDescription(AvatarPickerActivity.this.getString(C1757R.string.default_user_icon_description));
                }
                avatarViewHolder.setClickListener(new AvatarPickerActivity$AvatarAdapter$$ExternalSyntheticLambda2(this, i));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBindViewHolder$0$com-android-settingslib-users-AvatarPickerActivity$AvatarAdapter */
        public /* synthetic */ void mo29213xbac58150(View view) {
            AvatarPickerActivity.this.mAvatarPhotoController.takePhoto();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBindViewHolder$1$com-android-settingslib-users-AvatarPickerActivity$AvatarAdapter */
        public /* synthetic */ void mo29214x24f5096f(View view) {
            AvatarPickerActivity.this.mAvatarPhotoController.choosePhoto();
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBindViewHolder$2$com-android-settingslib-users-AvatarPickerActivity$AvatarAdapter */
        public /* synthetic */ void mo29215x8f24918e(int i, View view) {
            if (this.mSelectedPosition == i) {
                deselect(i);
            } else {
                select(i);
            }
        }

        public int getItemCount() {
            return this.mPreselectedImageStartPosition + this.mImageDrawables.size();
        }

        private List<Drawable> buildDrawableList() {
            ArrayList arrayList = new ArrayList();
            int i = 0;
            while (i < this.mPreselectedImages.length()) {
                Drawable drawable = this.mPreselectedImages.getDrawable(i);
                if (drawable instanceof BitmapDrawable) {
                    arrayList.add(circularDrawableFrom((BitmapDrawable) drawable));
                    i++;
                } else {
                    throw new IllegalStateException("Avatar drawables must be bitmaps");
                }
            }
            if (!arrayList.isEmpty()) {
                return arrayList;
            }
            for (int defaultUserIconInColor : this.mUserIconColors) {
                arrayList.add(UserIcons.getDefaultUserIconInColor(AvatarPickerActivity.this.getResources(), defaultUserIconInColor));
            }
            return arrayList;
        }

        private List<String> buildDescriptionsList() {
            if (this.mPreselectedImages.length() > 0) {
                return Arrays.asList(AvatarPickerActivity.this.getResources().getStringArray(C1757R.array.avatar_image_descriptions));
            }
            return null;
        }

        private Drawable circularDrawableFrom(BitmapDrawable bitmapDrawable) {
            RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(AvatarPickerActivity.this.getResources(), bitmapDrawable.getBitmap());
            create.setCircular(true);
            return create;
        }

        private int indexFromPosition(int i) {
            return i - this.mPreselectedImageStartPosition;
        }

        private void select(int i) {
            int i2 = this.mSelectedPosition;
            this.mSelectedPosition = i;
            notifyItemChanged(i);
            if (i2 != -1) {
                notifyItemChanged(i2);
            } else {
                AvatarPickerActivity.this.mDoneButton.setEnabled(true);
            }
        }

        private void deselect(int i) {
            this.mSelectedPosition = -1;
            notifyItemChanged(i);
            AvatarPickerActivity.this.mDoneButton.setEnabled(false);
        }

        /* access modifiers changed from: private */
        public void returnSelectionResult() {
            int indexFromPosition = indexFromPosition(this.mSelectedPosition);
            if (this.mPreselectedImages.length() > 0) {
                int resourceId = this.mPreselectedImages.getResourceId(indexFromPosition, -1);
                if (resourceId != -1) {
                    AvatarPickerActivity.this.returnUriResult(uriForResourceId(resourceId));
                    return;
                }
                throw new IllegalStateException("Preselected avatar images must be resources.");
            }
            AvatarPickerActivity.this.returnColorResult(this.mUserIconColors[indexFromPosition]);
        }

        private Uri uriForResourceId(int i) {
            return new Uri.Builder().scheme("android.resource").authority(AvatarPickerActivity.this.getResources().getResourcePackageName(i)).appendPath(AvatarPickerActivity.this.getResources().getResourceTypeName(i)).appendPath(AvatarPickerActivity.this.getResources().getResourceEntryName(i)).build();
        }
    }

    private static class AvatarViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mImageView;

        AvatarViewHolder(View view) {
            super(view);
            this.mImageView = (ImageView) view.findViewById(C1757R.C1760id.avatar_image);
        }

        public void setDrawable(Drawable drawable) {
            this.mImageView.setImageDrawable(drawable);
        }

        public void setContentDescription(String str) {
            this.mImageView.setContentDescription(str);
        }

        public void setClickListener(View.OnClickListener onClickListener) {
            this.mImageView.setOnClickListener(onClickListener);
        }

        public void setSelected(boolean z) {
            this.mImageView.setSelected(z);
        }
    }
}
