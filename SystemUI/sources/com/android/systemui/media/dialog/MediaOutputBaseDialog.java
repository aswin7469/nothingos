package com.android.systemui.media.dialog;

import android.app.WallpaperColors;
import android.bluetooth.BluetoothLeBroadcast;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class MediaOutputBaseDialog extends SystemUIDialog implements MediaOutputController.Callback, Window.Callback {
    private static final boolean DEBUG = true;
    private static final String EMPTY_TITLE = " ";
    private static final int HANDLE_BROADCAST_FAILED_DELAY = 3000;
    private static final String PREF_IS_LE_BROADCAST_FIRST_LAUNCH = "PrefIsLeBroadcastFirstLaunch";
    private static final String PREF_NAME = "MediaOutputDialog";
    private static final String TAG = "MediaOutputDialog";
    MediaOutputBaseAdapter mAdapter;
    private Button mAppButton;
    private ImageView mAppResourceIcon;
    private final BluetoothLeBroadcast.Callback mBroadcastCallback = new BluetoothLeBroadcast.Callback() {
        public void onPlaybackStarted(int i, int i2) {
        }

        public void onPlaybackStopped(int i, int i2) {
        }

        public void onBroadcastStarted(int i, int i2) {
            Log.d("MediaOutputDialog", "onBroadcastStarted(), reason = " + i + ", broadcastId = " + i2);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBroadcastStarted$0$com-android-systemui-media-dialog-MediaOutputBaseDialog$1 */
        public /* synthetic */ void mo34345x4a886a93() {
            MediaOutputBaseDialog.this.handleLeBroadcastStarted();
        }

        public void onBroadcastStartFailed(int i) {
            Log.d("MediaOutputDialog", "onBroadcastStartFailed(), reason = " + i);
            MediaOutputBaseDialog.this.mMainThreadHandler.postDelayed(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda2(this), 3000);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBroadcastStartFailed$1$com-android-systemui-media-dialog-MediaOutputBaseDialog$1 */
        public /* synthetic */ void mo34344x3d9d392() {
            MediaOutputBaseDialog.this.handleLeBroadcastStartFailed();
        }

        public void onBroadcastMetadataChanged(int i, BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
            Log.d("MediaOutputDialog", "onBroadcastMetadataChanged(), broadcastId = " + i + ", metadata = " + bluetoothLeBroadcastMetadata);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda6(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBroadcastMetadataChanged$2$com-android-systemui-media-dialog-MediaOutputBaseDialog$1 */
        public /* synthetic */ void mo34343x5e96f719() {
            MediaOutputBaseDialog.this.handleLeBroadcastMetadataChanged();
        }

        public void onBroadcastStopped(int i, int i2) {
            Log.d("MediaOutputDialog", "onBroadcastStopped(), reason = " + i + ", broadcastId = " + i2);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBroadcastStopped$3$com-android-systemui-media-dialog-MediaOutputBaseDialog$1 */
        public /* synthetic */ void mo34347xf2224822() {
            MediaOutputBaseDialog.this.handleLeBroadcastStopped();
        }

        public void onBroadcastStopFailed(int i) {
            Log.d("MediaOutputDialog", "onBroadcastStopFailed(), reason = " + i);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda3(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBroadcastStopFailed$4$com-android-systemui-media-dialog-MediaOutputBaseDialog$1 */
        public /* synthetic */ void mo34346x51758a1f() {
            MediaOutputBaseDialog.this.handleLeBroadcastStopFailed();
        }

        public void onBroadcastUpdated(int i, int i2) {
            Log.d("MediaOutputDialog", "onBroadcastUpdated(), reason = " + i + ", broadcastId = " + i2);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda5(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBroadcastUpdated$5$com-android-systemui-media-dialog-MediaOutputBaseDialog$1 */
        public /* synthetic */ void mo34349x31a76792() {
            MediaOutputBaseDialog.this.handleLeBroadcastUpdated();
        }

        public void onBroadcastUpdateFailed(int i, int i2) {
            Log.d("MediaOutputDialog", "onBroadcastUpdateFailed(), reason = " + i + ", broadcastId = " + i2);
            MediaOutputBaseDialog.this.mMainThreadHandler.post(new MediaOutputBaseDialog$1$$ExternalSyntheticLambda4(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onBroadcastUpdateFailed$6$com-android-systemui-media-dialog-MediaOutputBaseDialog$1 */
        public /* synthetic */ void mo34348x38f7f168() {
            MediaOutputBaseDialog.this.handleLeBroadcastUpdateFailed();
        }
    };
    private ImageView mBroadcastIcon;
    final BroadcastSender mBroadcastSender;
    private LinearLayout mCastAppLayout;
    final Context mContext;
    private LinearLayout mDeviceListLayout;
    private final ViewTreeObserver.OnGlobalLayoutListener mDeviceListLayoutListener = new MediaOutputBaseDialog$$ExternalSyntheticLambda4(this);
    private RecyclerView mDevicesRecyclerView;
    View mDialogView;
    private Button mDoneButton;
    private Executor mExecutor;
    private ImageView mHeaderIcon;
    private TextView mHeaderSubtitle;
    private TextView mHeaderTitle;
    private final RecyclerView.LayoutManager mLayoutManager;
    private int mListMaxHeight;
    /* access modifiers changed from: private */
    public final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    final MediaOutputController mMediaOutputController;
    private boolean mShouldLaunchLeBroadcastDialog;
    private Button mStopButton;
    private WallpaperColors mWallpaperColors;

    /* access modifiers changed from: package-private */
    public abstract Drawable getAppSourceIcon();

    public int getBroadcastIconVisibility() {
        return 8;
    }

    /* access modifiers changed from: package-private */
    public abstract IconCompat getHeaderIcon();

    /* access modifiers changed from: package-private */
    public abstract int getHeaderIconRes();

    /* access modifiers changed from: package-private */
    public abstract int getHeaderIconSize();

    /* access modifiers changed from: package-private */
    public abstract CharSequence getHeaderSubtitle();

    /* access modifiers changed from: package-private */
    public abstract CharSequence getHeaderText();

    /* access modifiers changed from: package-private */
    public abstract int getStopButtonVisibility();

    public boolean isBroadcastSupported() {
        return false;
    }

    public void onBroadcastIconClick() {
    }

    /* access modifiers changed from: package-private */
    public void onHeaderIconClick() {
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-media-dialog-MediaOutputBaseDialog */
    public /* synthetic */ void mo34318xccc14f8d() {
        if (this.mDeviceListLayout.getHeight() > this.mListMaxHeight) {
            ViewGroup.LayoutParams layoutParams = this.mDeviceListLayout.getLayoutParams();
            layoutParams.height = this.mListMaxHeight;
            this.mDeviceListLayout.setLayoutParams(layoutParams);
        }
    }

    private class LayoutManagerWrapper extends LinearLayoutManager {
        LayoutManagerWrapper(Context context) {
            super(context);
        }

        public void onLayoutCompleted(RecyclerView.State state) {
            super.onLayoutCompleted(state);
            MediaOutputBaseDialog.this.mMediaOutputController.setRefreshing(false);
            MediaOutputBaseDialog.this.mMediaOutputController.refreshDataSetIfNeeded();
        }
    }

    public MediaOutputBaseDialog(Context context, BroadcastSender broadcastSender, MediaOutputController mediaOutputController) {
        super(context, (int) C1893R.style.Theme_SystemUI_Dialog_Media);
        Context context2 = getContext();
        this.mContext = context2;
        this.mBroadcastSender = broadcastSender;
        this.mMediaOutputController = mediaOutputController;
        this.mLayoutManager = new LayoutManagerWrapper(context2);
        this.mListMaxHeight = context.getResources().getDimensionPixelSize(C1893R.dimen.media_output_dialog_list_max_height);
        this.mExecutor = Executors.newSingleThreadExecutor();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDialogView = LayoutInflater.from(this.mContext).inflate(C1893R.layout.media_output_dialog, (ViewGroup) null);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 17;
        attributes.setFitInsetsTypes(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        attributes.setFitInsetsSides(WindowInsets.Side.all());
        attributes.setFitInsetsIgnoringVisibility(true);
        window.setAttributes(attributes);
        window.setContentView(this.mDialogView);
        window.setTitle(this.mContext.getString(C1893R.string.media_output_dialog_accessibility_title));
        this.mHeaderTitle = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.header_title);
        this.mHeaderSubtitle = (TextView) this.mDialogView.requireViewById(C1893R.C1897id.header_subtitle);
        this.mHeaderIcon = (ImageView) this.mDialogView.requireViewById(C1893R.C1897id.header_icon);
        this.mDevicesRecyclerView = (RecyclerView) this.mDialogView.requireViewById(C1893R.C1897id.list_result);
        this.mDeviceListLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.device_list);
        this.mDoneButton = (Button) this.mDialogView.requireViewById(C1893R.C1897id.done);
        this.mStopButton = (Button) this.mDialogView.requireViewById(C1893R.C1897id.stop);
        this.mAppButton = (Button) this.mDialogView.requireViewById(C1893R.C1897id.launch_app_button);
        this.mAppResourceIcon = (ImageView) this.mDialogView.requireViewById(C1893R.C1897id.app_source_icon);
        this.mCastAppLayout = (LinearLayout) this.mDialogView.requireViewById(C1893R.C1897id.cast_app_section);
        this.mBroadcastIcon = (ImageView) this.mDialogView.requireViewById(C1893R.C1897id.broadcast_icon);
        this.mDeviceListLayout.getViewTreeObserver().addOnGlobalLayoutListener(this.mDeviceListLayoutListener);
        this.mDevicesRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mDevicesRecyclerView.setAdapter(this.mAdapter);
        this.mHeaderIcon.setOnClickListener(new MediaOutputBaseDialog$$ExternalSyntheticLambda7(this));
        this.mDoneButton.setOnClickListener(new MediaOutputBaseDialog$$ExternalSyntheticLambda8(this));
        this.mStopButton.setOnClickListener(new MediaOutputBaseDialog$$ExternalSyntheticLambda9(this));
        this.mAppButton.setOnClickListener(new MediaOutputBaseDialog$$ExternalSyntheticLambda10(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$1$com-android-systemui-media-dialog-MediaOutputBaseDialog */
    public /* synthetic */ void mo34319xabe20d03(View view) {
        onHeaderIconClick();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$2$com-android-systemui-media-dialog-MediaOutputBaseDialog */
    public /* synthetic */ void mo34320xd1761604(View view) {
        dismiss();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$3$com-android-systemui-media-dialog-MediaOutputBaseDialog */
    public /* synthetic */ void mo34321xf70a1f05(View view) {
        this.mMediaOutputController.releaseSession();
        dismiss();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$4$com-android-systemui-media-dialog-MediaOutputBaseDialog */
    public /* synthetic */ void mo34322x1c9e2806(View view) {
        this.mBroadcastSender.closeSystemDialogs();
        if (this.mMediaOutputController.getAppLaunchIntent() != null) {
            this.mContext.startActivity(this.mMediaOutputController.getAppLaunchIntent());
        }
        dismiss();
    }

    public void onStart() {
        super.onStart();
        this.mMediaOutputController.start(this);
        if (isBroadcastSupported()) {
            this.mMediaOutputController.registerLeBroadcastServiceCallBack(this.mExecutor, this.mBroadcastCallback);
        }
    }

    public void onStop() {
        super.onStop();
        if (isBroadcastSupported()) {
            this.mMediaOutputController.unregisterLeBroadcastServiceCallBack(this.mBroadcastCallback);
        }
        this.mMediaOutputController.stop();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: refresh */
    public void mo34329x86da2bdb() {
        refresh(false);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00fa  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0108  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x011f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void refresh(boolean r10) {
        /*
            r9 = this;
            com.android.systemui.media.dialog.MediaOutputController r0 = r9.mMediaOutputController
            boolean r0 = r0.isRefreshing()
            if (r0 == 0) goto L_0x0009
            return
        L_0x0009:
            com.android.systemui.media.dialog.MediaOutputController r0 = r9.mMediaOutputController
            r1 = 1
            r0.setRefreshing(r1)
            int r0 = r9.getHeaderIconRes()
            androidx.core.graphics.drawable.IconCompat r2 = r9.getHeaderIcon()
            android.graphics.drawable.Drawable r3 = r9.getAppSourceIcon()
            android.widget.LinearLayout r4 = r9.mCastAppLayout
            com.android.systemui.media.dialog.MediaOutputController r5 = r9.mMediaOutputController
            boolean r5 = r5.shouldShowLaunchSection()
            r6 = 8
            r7 = 0
            if (r5 == 0) goto L_0x002a
            r5 = r7
            goto L_0x002b
        L_0x002a:
            r5 = r6
        L_0x002b:
            r4.setVisibility(r5)
            if (r3 == 0) goto L_0x004d
            android.widget.ImageView r4 = r9.mAppResourceIcon
            r4.setImageDrawable(r3)
            android.widget.Button r4 = r9.mAppButton
            android.content.Context r5 = r9.mContext
            android.content.res.Resources r5 = r5.getResources()
            r8 = 2131166303(0x7f07045f, float:1.7946848E38)
            int r5 = r5.getDimensionPixelSize(r8)
            android.graphics.drawable.Drawable r3 = r9.resizeDrawable(r3, r5)
            r5 = 0
            r4.setCompoundDrawablesWithIntrinsicBounds(r3, r5, r5, r5)
            goto L_0x0052
        L_0x004d:
            android.widget.ImageView r3 = r9.mAppResourceIcon
            r3.setVisibility(r6)
        L_0x0052:
            if (r0 == 0) goto L_0x005f
            android.widget.ImageView r2 = r9.mHeaderIcon
            r2.setVisibility(r7)
            android.widget.ImageView r2 = r9.mHeaderIcon
            r2.setImageResource(r0)
            goto L_0x00b7
        L_0x005f:
            if (r2 == 0) goto L_0x00b2
            android.content.Context r0 = r9.mContext
            android.graphics.drawable.Icon r0 = r2.toIcon(r0)
            int r2 = r0.getType()
            if (r2 == r1) goto L_0x0079
            int r2 = r0.getType()
            r3 = 5
            if (r2 == r3) goto L_0x0079
            r9.updateButtonBackgroundColorFilter()
            r4 = r7
            goto L_0x00a7
        L_0x0079:
            android.content.Context r2 = r9.mContext
            android.content.res.Resources r2 = r2.getResources()
            android.content.res.Configuration r2 = r2.getConfiguration()
            int r2 = r2.uiMode
            r2 = r2 & 48
            r3 = 32
            if (r2 != r3) goto L_0x008d
            r2 = r1
            goto L_0x008e
        L_0x008d:
            r2 = r7
        L_0x008e:
            android.graphics.Bitmap r3 = r0.getBitmap()
            android.app.WallpaperColors r3 = android.app.WallpaperColors.fromBitmap(r3)
            android.app.WallpaperColors r4 = r9.mWallpaperColors
            boolean r4 = r3.equals(r4)
            r4 = r4 ^ r1
            if (r4 == 0) goto L_0x00a7
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r5 = r9.mAdapter
            r5.updateColorScheme(r3, r2)
            r9.updateButtonBackgroundColorFilter()
        L_0x00a7:
            android.widget.ImageView r2 = r9.mHeaderIcon
            r2.setVisibility(r7)
            android.widget.ImageView r2 = r9.mHeaderIcon
            r2.setImageIcon(r0)
            goto L_0x00b8
        L_0x00b2:
            android.widget.ImageView r0 = r9.mHeaderIcon
            r0.setVisibility(r6)
        L_0x00b7:
            r4 = r7
        L_0x00b8:
            android.widget.ImageView r0 = r9.mHeaderIcon
            int r0 = r0.getVisibility()
            if (r0 != 0) goto L_0x00dc
            int r0 = r9.getHeaderIconSize()
            android.content.Context r2 = r9.mContext
            android.content.res.Resources r2 = r2.getResources()
            r3 = 2131166307(0x7f070463, float:1.7946856E38)
            int r2 = r2.getDimensionPixelSize(r3)
            android.widget.ImageView r3 = r9.mHeaderIcon
            android.widget.LinearLayout$LayoutParams r5 = new android.widget.LinearLayout$LayoutParams
            int r2 = r2 + r0
            r5.<init>(r2, r0)
            r3.setLayoutParams(r5)
        L_0x00dc:
            android.widget.Button r0 = r9.mAppButton
            com.android.systemui.media.dialog.MediaOutputController r2 = r9.mMediaOutputController
            java.lang.String r2 = r2.getAppSourceName()
            r0.setText(r2)
            android.widget.TextView r0 = r9.mHeaderTitle
            java.lang.CharSequence r2 = r9.getHeaderText()
            r0.setText(r2)
            java.lang.CharSequence r0 = r9.getHeaderSubtitle()
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 == 0) goto L_0x0108
            android.widget.TextView r0 = r9.mHeaderSubtitle
            r0.setVisibility(r6)
            android.widget.TextView r0 = r9.mHeaderTitle
            r2 = 8388627(0x800013, float:1.175497E-38)
            r0.setGravity(r2)
            goto L_0x0117
        L_0x0108:
            android.widget.TextView r2 = r9.mHeaderSubtitle
            r2.setVisibility(r7)
            android.widget.TextView r2 = r9.mHeaderSubtitle
            r2.setText(r0)
            android.widget.TextView r0 = r9.mHeaderTitle
            r0.setGravity(r7)
        L_0x0117:
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r0 = r9.mAdapter
            boolean r0 = r0.isDragging()
            if (r0 != 0) goto L_0x013e
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r0 = r9.mAdapter
            int r0 = r0.getCurrentActivePosition()
            if (r4 != 0) goto L_0x0139
            if (r10 != 0) goto L_0x0139
            if (r0 < 0) goto L_0x0139
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r10 = r9.mAdapter
            int r10 = r10.getItemCount()
            if (r0 >= r10) goto L_0x0139
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r10 = r9.mAdapter
            r10.notifyItemChanged(r0)
            goto L_0x013e
        L_0x0139:
            com.android.systemui.media.dialog.MediaOutputBaseAdapter r10 = r9.mAdapter
            r10.notifyDataSetChanged()
        L_0x013e:
            android.widget.Button r10 = r9.mStopButton
            int r0 = r9.getStopButtonVisibility()
            r10.setVisibility(r0)
            android.widget.Button r10 = r9.mStopButton
            r10.setEnabled(r1)
            android.widget.Button r10 = r9.mStopButton
            java.lang.CharSequence r0 = r9.getStopButtonText()
            r10.setText(r0)
            android.widget.Button r10 = r9.mStopButton
            com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda11 r0 = new com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda11
            r0.<init>(r9)
            r10.setOnClickListener(r0)
            android.widget.ImageView r10 = r9.mBroadcastIcon
            int r0 = r9.getBroadcastIconVisibility()
            r10.setVisibility(r0)
            android.widget.ImageView r10 = r9.mBroadcastIcon
            com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda1 r0 = new com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda1
            r0.<init>(r9)
            r10.setOnClickListener(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.dialog.MediaOutputBaseDialog.refresh(boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$refresh$5$com-android-systemui-media-dialog-MediaOutputBaseDialog */
    public /* synthetic */ void mo34326x1414ac8d(View view) {
        onStopButtonClick();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$refresh$6$com-android-systemui-media-dialog-MediaOutputBaseDialog */
    public /* synthetic */ void mo34327x39a8b58e(View view) {
        onBroadcastIconClick();
    }

    private void updateButtonBackgroundColorFilter() {
        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(this.mAdapter.getController().getColorButtonBackground(), PorterDuff.Mode.SRC_IN);
        this.mDoneButton.getBackground().setColorFilter(porterDuffColorFilter);
        this.mStopButton.getBackground().setColorFilter(porterDuffColorFilter);
        this.mDoneButton.setTextColor(this.mAdapter.getController().getColorPositiveButtonText());
    }

    private Drawable resizeDrawable(Drawable drawable, int i) {
        Bitmap.Config config;
        if (drawable == null) {
            return null;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (drawable.getOpacity() != -1) {
            config = Bitmap.Config.ARGB_8888;
        } else {
            config = Bitmap.Config.RGB_565;
        }
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, config);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return new BitmapDrawable(this.mContext.getResources(), Bitmap.createScaledBitmap(createBitmap, i, i, false));
    }

    public void handleLeBroadcastStarted() {
        this.mShouldLaunchLeBroadcastDialog = true;
    }

    public void handleLeBroadcastStartFailed() {
        this.mStopButton.setText(C1893R.string.media_output_broadcast_start_failed);
        this.mStopButton.setEnabled(false);
        mo34329x86da2bdb();
    }

    public void handleLeBroadcastMetadataChanged() {
        if (this.mShouldLaunchLeBroadcastDialog) {
            startLeBroadcastDialog();
            this.mShouldLaunchLeBroadcastDialog = false;
        }
        mo34329x86da2bdb();
    }

    public void handleLeBroadcastStopped() {
        this.mShouldLaunchLeBroadcastDialog = false;
        mo34329x86da2bdb();
    }

    public void handleLeBroadcastStopFailed() {
        mo34329x86da2bdb();
    }

    public void handleLeBroadcastUpdated() {
        mo34329x86da2bdb();
    }

    public void handleLeBroadcastUpdateFailed() {
        mo34329x86da2bdb();
    }

    /* access modifiers changed from: protected */
    public void startLeBroadcast() {
        this.mStopButton.setText(C1893R.string.media_output_broadcast_starting);
        this.mStopButton.setEnabled(false);
        if (!this.mMediaOutputController.startBluetoothLeBroadcast()) {
            handleLeBroadcastStartFailed();
        }
    }

    /* access modifiers changed from: protected */
    public boolean startLeBroadcastDialogForFirstTime() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("MediaOutputDialog", 0);
        if (sharedPreferences == null || !sharedPreferences.getBoolean(PREF_IS_LE_BROADCAST_FIRST_LAUNCH, true)) {
            return false;
        }
        Log.d("MediaOutputDialog", "PREF_IS_LE_BROADCAST_FIRST_LAUNCH: true");
        this.mMediaOutputController.launchLeBroadcastNotifyDialog(this.mDialogView, this.mBroadcastSender, MediaOutputController.BroadcastNotifyDialog.ACTION_FIRST_LAUNCH, new MediaOutputBaseDialog$$ExternalSyntheticLambda0(this));
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(PREF_IS_LE_BROADCAST_FIRST_LAUNCH, false);
        edit.apply();
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$startLeBroadcastDialogForFirstTime$7$com-android-systemui-media-dialog-MediaOutputBaseDialog */
    public /* synthetic */ void mo34328x490262b0(DialogInterface dialogInterface, int i) {
        startLeBroadcast();
    }

    /* access modifiers changed from: protected */
    public void startLeBroadcastDialog() {
        this.mMediaOutputController.launchMediaOutputBroadcastDialog(this.mDialogView, this.mBroadcastSender);
        mo34329x86da2bdb();
    }

    /* access modifiers changed from: protected */
    public void stopLeBroadcast() {
        this.mStopButton.setEnabled(false);
        if (!this.mMediaOutputController.stopBluetoothLeBroadcast()) {
            this.mMainThreadHandler.post(new MediaOutputBaseDialog$$ExternalSyntheticLambda2(this));
        }
    }

    public CharSequence getStopButtonText() {
        return this.mContext.getText(C1893R.string.media_output_dialog_button_stop_casting);
    }

    public void onStopButtonClick() {
        this.mMediaOutputController.releaseSession();
        dismiss();
    }

    public void onMediaChanged() {
        this.mMainThreadHandler.post(new MediaOutputBaseDialog$$ExternalSyntheticLambda6(this));
    }

    public void onMediaStoppedOrPaused() {
        if (isShowing()) {
            dismiss();
        }
    }

    public void onRouteChanged() {
        this.mMainThreadHandler.post(new MediaOutputBaseDialog$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onDeviceListChanged$11$com-android-systemui-media-dialog-MediaOutputBaseDialog */
    public /* synthetic */ void mo34323x77ace8be() {
        refresh(true);
    }

    public void onDeviceListChanged() {
        this.mMainThreadHandler.post(new MediaOutputBaseDialog$$ExternalSyntheticLambda3(this));
    }

    public void dismissDialog() {
        dismiss();
    }

    /* access modifiers changed from: package-private */
    public View getDialogView() {
        return this.mDialogView;
    }
}
