package com.android.systemui.screenshot;

import android.graphics.Bitmap;
import android.graphics.HardwareRenderer;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RenderNode;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import com.android.internal.util.CallbackRegistry;
import com.nothing.systemui.util.NTLogUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;

class ImageTileSet {
    private static final String TAG = "ImageTileSet";
    private CallbackRegistry<OnContentChangedListener, ImageTileSet, Rect> mContentListeners;
    private final Handler mHandler;
    private Object mLock = new Object();
    private final Region mRegion = new Region();
    private final List<ImageTile> mTiles = new ArrayList();

    interface OnContentChangedListener {
        void onContentChanged();
    }

    @Inject
    ImageTileSet(Handler handler) {
        this.mHandler = handler;
    }

    /* access modifiers changed from: package-private */
    public void addOnContentChangedListener(OnContentChangedListener onContentChangedListener) {
        if (this.mContentListeners == null) {
            this.mContentListeners = new CallbackRegistry<>(new CallbackRegistry.NotifierCallback<OnContentChangedListener, ImageTileSet, Rect>() {
                public void onNotifyCallback(OnContentChangedListener onContentChangedListener, ImageTileSet imageTileSet, int i, Rect rect) {
                    onContentChangedListener.onContentChanged();
                }
            });
        }
        this.mContentListeners.add(onContentChangedListener);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: addTile */
    public void m3002lambda$addTile$0$comandroidsystemuiscreenshotImageTileSet(ImageTile imageTile) {
        if (!this.mHandler.getLooper().isCurrentThread()) {
            this.mHandler.post(new ImageTileSet$$ExternalSyntheticLambda0(this, imageTile));
            return;
        }
        synchronized (this.mLock) {
            NTLogUtil.m1686d(TAG, " addTile");
            this.mTiles.add(imageTile);
        }
        this.mRegion.op(imageTile.getLocation(), this.mRegion, Region.Op.UNION);
        notifyContentChanged();
    }

    private void notifyContentChanged() {
        CallbackRegistry<OnContentChangedListener, ImageTileSet, Rect> callbackRegistry = this.mContentListeners;
        if (callbackRegistry != null) {
            callbackRegistry.notifyCallbacks(this, 0, (Object) null);
        }
    }

    /* access modifiers changed from: package-private */
    public Drawable getDrawable() {
        return new TiledImageDrawable(this);
    }

    /* access modifiers changed from: package-private */
    public boolean isEmpty() {
        return this.mTiles.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public int size() {
        return this.mTiles.size();
    }

    /* access modifiers changed from: package-private */
    public Rect getGaps() {
        Region region = new Region();
        region.op(this.mRegion.getBounds(), this.mRegion, Region.Op.DIFFERENCE);
        return region.getBounds();
    }

    /* access modifiers changed from: package-private */
    public ImageTile get(int i) {
        return this.mTiles.get(i);
    }

    /* access modifiers changed from: package-private */
    public Bitmap toBitmap() {
        return toBitmap(new Rect(0, 0, getWidth(), getHeight()));
    }

    /* access modifiers changed from: package-private */
    public Bitmap toBitmap(Rect rect) {
        if (this.mTiles.isEmpty()) {
            return null;
        }
        RenderNode renderNode = new RenderNode("Bitmap Export");
        renderNode.setPosition(0, 0, rect.width(), rect.height());
        RecordingCanvas beginRecording = renderNode.beginRecording();
        Drawable drawable = getDrawable();
        drawable.setBounds(rect);
        drawable.draw(beginRecording);
        renderNode.endRecording();
        return HardwareRenderer.createHardwareBitmap(renderNode, rect.width(), rect.height());
    }

    /* access modifiers changed from: package-private */
    public int getLeft() {
        return this.mRegion.getBounds().left;
    }

    /* access modifiers changed from: package-private */
    public int getTop() {
        return this.mRegion.getBounds().top;
    }

    /* access modifiers changed from: package-private */
    public int getRight() {
        return this.mRegion.getBounds().right;
    }

    /* access modifiers changed from: package-private */
    public int getBottom() {
        return this.mRegion.getBounds().bottom;
    }

    /* access modifiers changed from: package-private */
    public int getWidth() {
        return this.mRegion.getBounds().width();
    }

    /* access modifiers changed from: package-private */
    public int getHeight() {
        return this.mRegion.getBounds().height();
    }

    /* access modifiers changed from: package-private */
    public void clear() {
        if (!this.mTiles.isEmpty()) {
            this.mRegion.setEmpty();
            synchronized (this.mLock) {
                NTLogUtil.m1686d(TAG, "remove mTiles start");
                Iterator<ImageTile> it = this.mTiles.iterator();
                while (it.hasNext()) {
                    it.next().close();
                    it.remove();
                }
                NTLogUtil.m1686d(TAG, "remove mTiles end");
            }
            notifyContentChanged();
        }
    }
}
