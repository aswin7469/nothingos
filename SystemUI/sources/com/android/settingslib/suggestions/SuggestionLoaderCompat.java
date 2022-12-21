package com.android.settingslib.suggestions;

import android.content.Context;
import android.service.settings.suggestions.Suggestion;
import android.util.Log;
import com.android.settingslib.utils.AsyncLoaderCompat;
import java.util.List;

public class SuggestionLoaderCompat extends AsyncLoaderCompat<List<Suggestion>> {
    public static final int LOADER_ID_SUGGESTIONS = 42;
    private static final String TAG = "SuggestionLoader";
    private final SuggestionController mSuggestionController;

    /* access modifiers changed from: protected */
    public void onDiscardResult(List<Suggestion> list) {
    }

    public SuggestionLoaderCompat(Context context, SuggestionController suggestionController) {
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
