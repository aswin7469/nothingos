package com.android.systemui.p012qs.external;

import android.service.quicksettings.Tile;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;

@Metadata(mo65042d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0001H\u0001\u001a\u0010\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u0007H\u0001\u001a\u0016\u0010\u000b\u001a\u0004\u0018\u00010\u0001*\u00020\f2\u0006\u0010\r\u001a\u00020\u0001H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u000e\u0010\u0005\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo65043d2 = {"CONTENT_DESCRIPTION", "", "LABEL", "STATE", "STATE_DESCRIPTION", "SUBTITLE", "readTileFromString", "Landroid/service/quicksettings/Tile;", "stateString", "writeToString", "tile", "getStringOrNull", "Lorg/json/JSONObject;", "name", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.external.CustomTileStatePersisterKt */
/* compiled from: CustomTileStatePersister.kt */
public final class CustomTileStatePersisterKt {
    private static final String CONTENT_DESCRIPTION = "content_description";
    private static final String LABEL = "label";
    private static final String STATE = "state";
    private static final String STATE_DESCRIPTION = "state_description";
    private static final String SUBTITLE = "subtitle";

    public static final Tile readTileFromString(String str) {
        Intrinsics.checkNotNullParameter(str, "stateString");
        JSONObject jSONObject = new JSONObject(str);
        Tile tile = new Tile();
        tile.setState(jSONObject.getInt("state"));
        tile.setLabel(getStringOrNull(jSONObject, "label"));
        tile.setSubtitle(getStringOrNull(jSONObject, SUBTITLE));
        tile.setContentDescription(getStringOrNull(jSONObject, CONTENT_DESCRIPTION));
        tile.setStateDescription(getStringOrNull(jSONObject, STATE_DESCRIPTION));
        return tile;
    }

    private static final String getStringOrNull(JSONObject jSONObject, String str) {
        if (jSONObject.has(str)) {
            return jSONObject.getString(str);
        }
        return null;
    }

    public static final String writeToString(Tile tile) {
        Intrinsics.checkNotNullParameter(tile, "tile");
        String jSONObject = new JSONObject().put("state", tile.getState()).put("label", (Object) tile.getLabel()).put(SUBTITLE, (Object) tile.getSubtitle()).put(CONTENT_DESCRIPTION, (Object) tile.getContentDescription()).put(STATE_DESCRIPTION, (Object) tile.getStateDescription()).toString();
        Intrinsics.checkNotNullExpressionValue(jSONObject, "with(tile) {\n        JSO…        .toString()\n    }");
        return jSONObject;
    }
}
