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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ImageTileSet {
    private CallbackRegistry<OnContentChangedListener, ImageTileSet, Rect> mContentListeners;
    private final Handler mHandler;
    private final List<ImageTile> mTiles = new ArrayList();
    private final Region mRegion = new Region();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public interface OnContentChangedListener {
        void onContentChanged();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImageTileSet(Handler handler) {
        this.mHandler = handler;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addOnContentChangedListener(OnContentChangedListener onContentChangedListener) {
        if (this.mContentListeners == null) {
            this.mContentListeners = new CallbackRegistry<>(new CallbackRegistry.NotifierCallback<OnContentChangedListener, ImageTileSet, Rect>() { // from class: com.android.systemui.screenshot.ImageTileSet.1
                public void onNotifyCallback(OnContentChangedListener onContentChangedListener2, ImageTileSet imageTileSet, int i, Rect rect) {
                    onContentChangedListener2.onContentChanged();
                }
            });
        }
        this.mContentListeners.add(onContentChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: addTile */
    public void lambda$addTile$0(final ImageTile imageTile) {
        if (!this.mHandler.getLooper().isCurrentThread()) {
            this.mHandler.post(new Runnable() { // from class: com.android.systemui.screenshot.ImageTileSet$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    ImageTileSet.this.lambda$addTile$0(imageTile);
                }
            });
            return;
        }
        this.mTiles.add(imageTile);
        this.mRegion.op(imageTile.getLocation(), this.mRegion, Region.Op.UNION);
        notifyContentChanged();
    }

    private void notifyContentChanged() {
        CallbackRegistry<OnContentChangedListener, ImageTileSet, Rect> callbackRegistry = this.mContentListeners;
        if (callbackRegistry != null) {
            callbackRegistry.notifyCallbacks(this, 0, (Object) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Drawable getDrawable() {
        return new TiledImageDrawable(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int size() {
        return this.mTiles.size();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Rect getGaps() {
        Region region = new Region();
        region.op(this.mRegion.getBounds(), this.mRegion, Region.Op.DIFFERENCE);
        return region.getBounds();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImageTile get(int i) {
        return this.mTiles.get(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap toBitmap() {
        return toBitmap(new Rect(0, 0, getWidth(), getHeight()));
    }

    Bitmap toBitmap(Rect rect) {
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

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getLeft() {
        return this.mRegion.getBounds().left;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getTop() {
        return this.mRegion.getBounds().top;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getBottom() {
        return this.mRegion.getBounds().bottom;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getWidth() {
        return this.mRegion.getBounds().width();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getHeight() {
        return this.mRegion.getBounds().height();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void clear() {
        if (this.mTiles.isEmpty()) {
            return;
        }
        this.mRegion.setEmpty();
        Iterator<ImageTile> it = this.mTiles.iterator();
        while (it.hasNext()) {
            it.next().close();
            it.remove();
        }
        notifyContentChanged();
    }
}
