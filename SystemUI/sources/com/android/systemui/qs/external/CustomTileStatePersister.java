package com.android.systemui.qs.external;

import android.content.Context;
import android.content.SharedPreferences;
import android.service.quicksettings.Tile;
import android.util.Log;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
/* compiled from: CustomTileStatePersister.kt */
/* loaded from: classes.dex */
public final class CustomTileStatePersister {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final SharedPreferences sharedPreferences;

    /* compiled from: CustomTileStatePersister.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public CustomTileStatePersister(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.sharedPreferences = context.getSharedPreferences("custom_tiles_state", 0);
    }

    @Nullable
    public final Tile readState(@NotNull TileServiceKey key) {
        Intrinsics.checkNotNullParameter(key, "key");
        String string = this.sharedPreferences.getString(key.toString(), null);
        if (string == null) {
            return null;
        }
        try {
            return CustomTileStatePersisterKt.readTileFromString(string);
        } catch (JSONException e) {
            Log.e("TileServicePersistence", Intrinsics.stringPlus("Bad saved state: ", string), e);
            return null;
        }
    }

    public final void persistState(@NotNull TileServiceKey key, @NotNull Tile tile) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(tile, "tile");
        this.sharedPreferences.edit().putString(key.toString(), CustomTileStatePersisterKt.writeToString(tile)).apply();
    }

    public final void removeState(@NotNull TileServiceKey key) {
        Intrinsics.checkNotNullParameter(key, "key");
        this.sharedPreferences.edit().remove(key.toString()).apply();
    }
}
