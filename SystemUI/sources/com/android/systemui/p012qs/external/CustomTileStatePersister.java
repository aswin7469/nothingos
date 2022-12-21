package com.android.systemui.p012qs.external;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.util.Log;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONException;

@Metadata(mo64986d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rJ\u0010\u0010\u000e\u001a\u0004\u0018\u00010\r2\u0006\u0010\n\u001a\u00020\u000bJ\u000e\u0010\u000f\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bR\u0016\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/qs/external/CustomTileStatePersister;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "sharedPreferences", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "persistState", "", "key", "Lcom/android/systemui/qs/external/TileServiceKey;", "tile", "Landroid/service/quicksettings/Tile;", "readState", "removeState", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.qs.external.CustomTileStatePersister */
/* compiled from: CustomTileStatePersister.kt */
public final class CustomTileStatePersister {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String FILE_NAME = "custom_tiles_state";
    private final SharedPreferences sharedPreferences;

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/qs/external/CustomTileStatePersister$Companion;", "", "()V", "FILE_NAME", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.android.systemui.qs.external.CustomTileStatePersister$Companion */
    /* compiled from: CustomTileStatePersister.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Inject
    public CustomTileStatePersister(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.sharedPreferences = context.getSharedPreferences(FILE_NAME, 0);
    }

    public final Tile readState(TileServiceKey tileServiceKey) {
        Intrinsics.checkNotNullParameter(tileServiceKey, "key");
        String string = this.sharedPreferences.getString(tileServiceKey.toString(), (String) null);
        if (string == null) {
            return null;
        }
        try {
            return CustomTileStatePersisterKt.readTileFromString(string);
        } catch (JSONException e) {
            Log.e("TileServicePersistence", "Bad saved state: " + string, e);
            Tile tile = null;
            return null;
        }
    }

    public final void persistState(TileServiceKey tileServiceKey, Tile tile) {
        Intrinsics.checkNotNullParameter(tileServiceKey, "key");
        Intrinsics.checkNotNullParameter(tile, "tile");
        this.sharedPreferences.edit().putString(tileServiceKey.toString(), CustomTileStatePersisterKt.writeToString(tile)).apply();
    }

    public final void removeState(TileServiceKey tileServiceKey) {
        Intrinsics.checkNotNullParameter(tileServiceKey, "key");
        this.sharedPreferences.edit().remove(tileServiceKey.toString()).apply();
    }
}
