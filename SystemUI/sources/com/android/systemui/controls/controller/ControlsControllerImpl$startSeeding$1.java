package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.icu.text.DateFormat;
import android.service.controls.Control;
import android.util.ArrayMap;
import android.util.Log;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005H\u0016J\u0010\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, mo64987d2 = {"com/android/systemui/controls/controller/ControlsControllerImpl$startSeeding$1", "Lcom/android/systemui/controls/controller/ControlsBindingController$LoadCallback;", "accept", "", "controls", "", "Landroid/service/controls/Control;", "error", "message", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ControlsControllerImpl.kt */
public final class ControlsControllerImpl$startSeeding$1 implements ControlsBindingController.LoadCallback {
    final /* synthetic */ Consumer<SeedResponse> $callback;
    final /* synthetic */ ComponentName $componentName;
    final /* synthetic */ boolean $didAnyFail;
    final /* synthetic */ List<ComponentName> $remaining;
    final /* synthetic */ ControlsControllerImpl this$0;

    ControlsControllerImpl$startSeeding$1(ControlsControllerImpl controlsControllerImpl, Consumer<SeedResponse> consumer, ComponentName componentName, List<ComponentName> list, boolean z) {
        this.this$0 = controlsControllerImpl;
        this.$callback = consumer;
        this.$componentName = componentName;
        this.$remaining = list;
        this.$didAnyFail = z;
    }

    public void accept(List<Control> list) {
        Intrinsics.checkNotNullParameter(list, AutoTileManager.DEVICE_CONTROLS);
        this.this$0.executor.execute(new ControlsControllerImpl$startSeeding$1$$ExternalSyntheticLambda0(list, this.this$0, this.$callback, this.$componentName, this.$remaining, this.$didAnyFail));
    }

    /* access modifiers changed from: private */
    /* renamed from: accept$lambda-2  reason: not valid java name */
    public static final void m2623accept$lambda2(List list, ControlsControllerImpl controlsControllerImpl, Consumer consumer, ComponentName componentName, List list2, boolean z) {
        Intrinsics.checkNotNullParameter(list, "$controls");
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(consumer, "$callback");
        Intrinsics.checkNotNullParameter(componentName, "$componentName");
        Intrinsics.checkNotNullParameter(list2, "$remaining");
        ArrayMap arrayMap = new ArrayMap();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Control control = (Control) it.next();
            CharSequence structure = control.getStructure();
            if (structure != null) {
                Intrinsics.checkNotNullExpressionValue(structure, "it.structure ?: \"\"");
            }
            List list3 = (List) arrayMap.get(structure);
            if (list3 == null) {
                list3 = new ArrayList();
            }
            Intrinsics.checkNotNullExpressionValue(list3, "structureToControls.get(…ableListOf<ControlInfo>()");
            if (list3.size() < 6) {
                String controlId = control.getControlId();
                Intrinsics.checkNotNullExpressionValue(controlId, "it.controlId");
                CharSequence title = control.getTitle();
                Intrinsics.checkNotNullExpressionValue(title, "it.title");
                CharSequence subtitle = control.getSubtitle();
                Intrinsics.checkNotNullExpressionValue(subtitle, "it.subtitle");
                list3.add(new ControlInfo(controlId, title, subtitle, control.getDeviceType()));
                arrayMap.put(structure, list3);
            }
        }
        for (Map.Entry entry : arrayMap.entrySet()) {
            CharSequence charSequence = (CharSequence) entry.getKey();
            List list4 = (List) entry.getValue();
            Favorites favorites = Favorites.INSTANCE;
            Intrinsics.checkNotNullExpressionValue(charSequence, DateFormat.SECOND);
            Intrinsics.checkNotNullExpressionValue(list4, "cs");
            favorites.replaceControls(new StructureInfo(componentName, charSequence, list4));
        }
        controlsControllerImpl.persistenceWrapper.storeFavorites(Favorites.INSTANCE.getAllStructures());
        String packageName = componentName.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "componentName.packageName");
        consumer.accept(new SeedResponse(packageName, true));
        controlsControllerImpl.startSeeding(list2, consumer, z);
    }

    public void error(String str) {
        Intrinsics.checkNotNullParameter(str, "message");
        Log.e("ControlsControllerImpl", "Unable to seed favorites: " + str);
        this.this$0.executor.execute(new ControlsControllerImpl$startSeeding$1$$ExternalSyntheticLambda1(this.$callback, this.$componentName, this.this$0, this.$remaining));
    }

    /* access modifiers changed from: private */
    /* renamed from: error$lambda-3  reason: not valid java name */
    public static final void m2624error$lambda3(Consumer consumer, ComponentName componentName, ControlsControllerImpl controlsControllerImpl, List list) {
        Intrinsics.checkNotNullParameter(consumer, "$callback");
        Intrinsics.checkNotNullParameter(componentName, "$componentName");
        Intrinsics.checkNotNullParameter(controlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(list, "$remaining");
        String packageName = componentName.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "componentName.packageName");
        consumer.accept(new SeedResponse(packageName, false));
        controlsControllerImpl.startSeeding(list, consumer, true);
    }
}
