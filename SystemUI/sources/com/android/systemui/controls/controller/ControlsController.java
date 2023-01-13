package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.util.UserAwareController;
import java.util.List;
import java.util.function.Consumer;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001:\u0001+J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\bH&J \u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0006\u001a\u00020\u0007H&J\u0016\u0010\f\u001a\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\r0\u000fH&J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0004\u001a\u00020\u0005H&J\u000e\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00140\u0013H&J\u0016\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00140\u00132\u0006\u0010\u0004\u001a\u00020\u0005H&J\u001e\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00070\u00132\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\n\u001a\u00020\u000bH&J\b\u0010\u0017\u001a\u00020\u0014H&J,\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\u000f2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u000fH&J \u0010\u001d\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020\u0011H&J\u0018\u0010!\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\"\u001a\u00020#H&J\u0010\u0010$\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u0014H&J$\u0010&\u001a\u00020\u00032\f\u0010'\u001a\b\u0012\u0004\u0012\u00020\u00050\u00132\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020(0\u000fH&J\u0010\u0010)\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\u0014H&J\b\u0010*\u001a\u00020\u0003H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006,À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/ControlsController;", "Lcom/android/systemui/util/UserAwareController;", "action", "", "componentName", "Landroid/content/ComponentName;", "controlInfo", "Lcom/android/systemui/controls/controller/ControlInfo;", "Landroid/service/controls/actions/ControlAction;", "addFavorite", "structureName", "", "addSeedingFavoritesCallback", "", "callback", "Ljava/util/function/Consumer;", "countFavoritesForComponent", "", "getFavorites", "", "Lcom/android/systemui/controls/controller/StructureInfo;", "getFavoritesForComponent", "getFavoritesForStructure", "getPreferredStructure", "loadForComponent", "dataCallback", "Lcom/android/systemui/controls/controller/ControlsController$LoadData;", "cancelWrapper", "Ljava/lang/Runnable;", "onActionResponse", "controlId", "", "response", "refreshStatus", "control", "Landroid/service/controls/Control;", "replaceFavoritesForStructure", "structureInfo", "seedFavoritesForComponents", "componentNames", "Lcom/android/systemui/controls/controller/SeedResponse;", "subscribeToFavorites", "unsubscribe", "LoadData", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsController.kt */
public interface ControlsController extends UserAwareController {

    @Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001R\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00020\bX¦\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0018\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u0006ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u000eÀ\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/controls/controller/ControlsController$LoadData;", "", "allControls", "", "Lcom/android/systemui/controls/ControlStatus;", "getAllControls", "()Ljava/util/List;", "errorOnLoad", "", "getErrorOnLoad", "()Z", "favoritesIds", "", "getFavoritesIds", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlsController.kt */
    public interface LoadData {
        List<ControlStatus> getAllControls();

        boolean getErrorOnLoad();

        List<String> getFavoritesIds();
    }

    void action(ComponentName componentName, ControlInfo controlInfo, ControlAction controlAction);

    void addFavorite(ComponentName componentName, CharSequence charSequence, ControlInfo controlInfo);

    boolean addSeedingFavoritesCallback(Consumer<Boolean> consumer);

    int countFavoritesForComponent(ComponentName componentName);

    List<StructureInfo> getFavorites();

    List<StructureInfo> getFavoritesForComponent(ComponentName componentName);

    List<ControlInfo> getFavoritesForStructure(ComponentName componentName, CharSequence charSequence);

    StructureInfo getPreferredStructure();

    void loadForComponent(ComponentName componentName, Consumer<LoadData> consumer, Consumer<Runnable> consumer2);

    void onActionResponse(ComponentName componentName, String str, int i);

    void refreshStatus(ComponentName componentName, Control control);

    void replaceFavoritesForStructure(StructureInfo structureInfo);

    void seedFavoritesForComponents(List<ComponentName> list, Consumer<SeedResponse> consumer);

    void subscribeToFavorites(StructureInfo structureInfo);

    void unsubscribe();
}
