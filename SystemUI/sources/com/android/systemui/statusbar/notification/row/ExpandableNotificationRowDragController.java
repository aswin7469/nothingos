package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.util.Log;
import android.view.DragEvent;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import javax.inject.Inject;

public class ExpandableNotificationRowDragController {
    private static final String TAG = "ExpandableNotificationRowDragController";
    private final Context mContext;
    private final HeadsUpManager mHeadsUpManager;
    private int mIconSize;
    private final ShadeController mShadeController;

    @Inject
    public ExpandableNotificationRowDragController(Context context, HeadsUpManager headsUpManager, ShadeController shadeController) {
        this.mContext = context;
        this.mHeadsUpManager = headsUpManager;
        this.mShadeController = shadeController;
        init();
    }

    private void init() {
        this.mIconSize = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.drag_and_drop_icon_size);
    }

    public void startDragAndDrop(View view) {
        PendingIntent pendingIntent;
        ExpandableNotificationRow expandableNotificationRow = view instanceof ExpandableNotificationRow ? (ExpandableNotificationRow) view : null;
        Notification notification = expandableNotificationRow.getEntry().getSbn().getNotification();
        if (notification.contentIntent != null) {
            pendingIntent = notification.contentIntent;
        } else {
            pendingIntent = notification.fullScreenIntent;
        }
        if (pendingIntent == null) {
            if (!expandableNotificationRow.isPinned()) {
                dismissShade();
            }
            Toast.makeText(this.mContext, C1893R.string.drag_split_not_supported, 0).show();
            return;
        }
        Bitmap bitmapFromDrawable = getBitmapFromDrawable(getPkgIcon(expandableNotificationRow.getEntry().getSbn().getPackageName()));
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageBitmap(bitmapFromDrawable);
        int i = this.mIconSize;
        imageView.layout(0, 0, i, i);
        ClipDescription clipDescription = new ClipDescription("Drag And Drop", new String[]{"application/vnd.android.activity"});
        Intent intent = new Intent();
        intent.putExtra("android.intent.extra.PENDING_INTENT", pendingIntent);
        intent.putExtra("android.intent.extra.USER", Process.myUserHandle());
        ClipData clipData = new ClipData(clipDescription, new ClipData.Item(intent));
        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(imageView);
        view.setOnDragListener(getDraggedViewDragListener());
        if (view.startDragAndDrop(clipData, dragShadowBuilder, (Object) null, 2304)) {
            view.performHapticFeedback(0);
            if (expandableNotificationRow.isPinned()) {
                this.mHeadsUpManager.releaseAllImmediately();
            } else {
                dismissShade();
            }
        }
    }

    private void dismissShade() {
        this.mShadeController.animateCollapsePanels(0, true, false, 1.1f);
    }

    private Drawable getPkgIcon(String str) {
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 795136);
            if (applicationInfo != null) {
                return packageManager.getApplicationIcon(applicationInfo);
            }
            Log.d(TAG, " application info is null ");
            return packageManager.getDefaultActivityIcon();
        } catch (PackageManager.NameNotFoundException unused) {
            Log.d(TAG, "can not find package with : " + str);
            return packageManager.getDefaultActivityIcon();
        }
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    private View.OnDragListener getDraggedViewDragListener() {
        return new C2749x70846d1b(this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getDraggedViewDragListener$0$com-android-systemui-statusbar-notification-row-ExpandableNotificationRowDragController */
    public /* synthetic */ boolean mo41258x5aa35130(View view, DragEvent dragEvent) {
        int action = dragEvent.getAction();
        if (action != 1) {
            if (action != 4) {
                return false;
            }
            if (!dragEvent.getResult()) {
                fadeOutAndRemoveDragSurface(dragEvent);
            } else if (view instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) view).dragAndDropSuccess();
            }
            view.setOnDragListener((View.OnDragListener) null);
        }
        return true;
    }

    private void fadeOutAndRemoveDragSurface(DragEvent dragEvent) {
        final SurfaceControl dragSurface = dragEvent.getDragSurface();
        final SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setDuration(200);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.addUpdateListener(new C2748x70846d1a(transaction, dragSurface));
        ofFloat.addListener(new AnimatorListenerAdapter() {
            private boolean mCanceled = false;

            public void onAnimationCancel(Animator animator) {
                cleanUpSurface();
                this.mCanceled = true;
            }

            public void onAnimationEnd(Animator animator) {
                if (!this.mCanceled) {
                    cleanUpSurface();
                }
            }

            private void cleanUpSurface() {
                transaction.remove(dragSurface);
                transaction.apply();
                transaction.close();
            }
        });
        ofFloat.start();
    }

    static /* synthetic */ void lambda$fadeOutAndRemoveDragSurface$1(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, ValueAnimator valueAnimator) {
        transaction.setAlpha(surfaceControl, 1.0f - valueAnimator.getAnimatedFraction());
        transaction.apply();
    }
}
