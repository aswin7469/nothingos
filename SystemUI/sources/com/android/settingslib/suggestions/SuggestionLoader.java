package com.android.settingslib.suggestions;

import android.content.Context;
import android.service.settings.suggestions.Suggestion;
import android.util.Log;
import com.android.settingslib.utils.AsyncLoader;
import java.util.List;

@Deprecated
public class SuggestionLoader extends AsyncLoader<List<Suggestion>> {
    public static final int LOADER_ID_SUGGESTIONS = 42;
    private static final String TAG = "SuggestionLoader";
    private final SuggestionController mSuggestionController;

    /* access modifiers changed from: protected */
    public void onDiscardResult(List<Suggestion> list) {
    }

    public SuggestionLoader(Context context, SuggestionController suggestionController) {
        super(context);
        this.mSuggestionController = suggestionController;
    }

    public List<Suggestion> loadInBackground() {
        List<Suggestion> suggestions = this.mSuggestionController.getSuggestions();
        if (suggestions == null) {
            Log.d(TAG, "data is null");
        } else {
            Log.d(TAG, "data size " + suggestions.size());
        }
        return suggestions;
    }
}
