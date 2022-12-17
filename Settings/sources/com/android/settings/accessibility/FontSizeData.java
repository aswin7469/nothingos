package com.android.settings.accessibility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import com.android.settings.R$array;
import com.android.settings.display.ToggleFontSizePreferenceFragment;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class FontSizeData extends PreviewSizeData<Float> {
    FontSizeData(Context context) {
        super(context);
        Resources resources = getContext().getResources();
        ContentResolver contentResolver = getContext().getContentResolver();
        List asList = Arrays.asList(resources.getStringArray(R$array.entryvalues_font_size));
        setDefaultValue(Float.valueOf(1.0f));
        setInitialIndex(ToggleFontSizePreferenceFragment.fontSizeValueToIndex(Settings.System.getFloat(contentResolver, "font_scale", ((Float) getDefaultValue()).floatValue()), (String[]) asList.toArray(new String[0])));
        setValues((List) asList.stream().map(new FontSizeData$$ExternalSyntheticLambda0()).collect(Collectors.toList()));
    }

    /* access modifiers changed from: package-private */
    public void commit(int i) {
        Settings.System.putFloat(getContext().getContentResolver(), "font_scale", ((Float) getValues().get(i)).floatValue());
    }
}
