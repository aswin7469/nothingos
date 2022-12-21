package com.android.systemui.p012qs.external;

import com.android.internal.logging.UiEventLogger;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u00012\u00020\u0002B\u000f\b\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000j\u0002\b\u0007j\u0002\b\bj\u0002\b\tj\u0002\b\nj\u0002\b\u000b¨\u0006\f"}, mo64987d2 = {"Lcom/android/systemui/qs/external/TileRequestDialogEvent;", "", "Lcom/android/internal/logging/UiEventLogger$UiEventEnum;", "_id", "", "(Ljava/lang/String;II)V", "getId", "TILE_REQUEST_DIALOG_TILE_ALREADY_ADDED", "TILE_REQUEST_DIALOG_SHOWN", "TILE_REQUEST_DIALOG_DISMISSED", "TILE_REQUEST_DIALOG_TILE_ADDED", "TILE_REQUEST_DIALOG_TILE_NOT_ADDED", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.external.TileRequestDialogEvent */
/* compiled from: TileRequestDialogEventLogger.kt */
public enum TileRequestDialogEvent implements UiEventLogger.UiEventEnum {
    TILE_REQUEST_DIALOG_TILE_ALREADY_ADDED(917),
    TILE_REQUEST_DIALOG_SHOWN(918),
    TILE_REQUEST_DIALOG_DISMISSED(919),
    TILE_REQUEST_DIALOG_TILE_ADDED(920),
    TILE_REQUEST_DIALOG_TILE_NOT_ADDED(921);
    
    private final int _id;

    private TileRequestDialogEvent(int i) {
        this._id = i;
    }

    public int getId() {
        return this._id;
    }
}
