package com.android.settingslib.suggestions;

import android.content.Context;
import android.service.settings.suggestions.Suggestion;
import com.android.settingslib.utils.AsyncLoader;
import java.util.List;
@Deprecated
/* loaded from: classes.dex */
public class SuggestionLoader extends AsyncLoader<List<Suggestion>> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settingslib.utils.AsyncLoader
    public void onDiscardResult(List<Suggestion> list) {
    }

    public SuggestionLoader(Context context, SuggestionController suggestionController) {
        super(context);
    }

    @Override // android.content.AsyncTaskLoader
    public List<Suggestion> loadInBackground() {
        throw null;
    }
}