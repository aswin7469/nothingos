package com.android.systemui.clipboardoverlay;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.android.systemui.C1893R;
import java.util.Objects;

public class EditTextActivity extends Activity implements ClipboardManager.OnPrimaryClipChangedListener {
    private static final String TAG = "EditTextActivity";
    private TextView mAttribution;
    private ClipboardManager mClipboardManager;
    private EditText mEditText;
    private boolean mSensitive;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(C1893R.layout.clipboard_edit_text_activity);
        findViewById(C1893R.C1897id.done_button).setOnClickListener(new EditTextActivity$$ExternalSyntheticLambda0(this));
        this.mEditText = (EditText) findViewById(C1893R.C1897id.edit_text);
        this.mAttribution = (TextView) findViewById(C1893R.C1897id.attribution);
        this.mClipboardManager = (ClipboardManager) Objects.requireNonNull((ClipboardManager) getSystemService(ClipboardManager.class));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onCreate$0$com-android-systemui-clipboardoverlay-EditTextActivity */
    public /* synthetic */ void mo31343xa3388fd9(View view) {
        saveToClipboard();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        ClipData primaryClip = this.mClipboardManager.getPrimaryClip();
        if (primaryClip == null) {
            finish();
            return;
        }
        PackageManager packageManager = getApplicationContext().getPackageManager();
        boolean z = true;
        try {
            this.mAttribution.setText(getResources().getString(C1893R.string.clipboard_edit_source, new Object[]{packageManager.getApplicationLabel(packageManager.getApplicationInfo(this.mClipboardManager.getPrimaryClipSource(), PackageManager.ApplicationInfoFlags.of(0)))}));
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Package not found: " + this.mClipboardManager.getPrimaryClipSource(), e);
        }
        this.mEditText.setText(primaryClip.getItemAt(0).getText());
        this.mEditText.requestFocus();
        if (primaryClip.getDescription().getExtras() == null || !primaryClip.getDescription().getExtras().getBoolean("android.content.extra.IS_SENSITIVE")) {
            z = false;
        }
        this.mSensitive = z;
        this.mClipboardManager.addPrimaryClipChangedListener(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        this.mClipboardManager.removePrimaryClipChangedListener(this);
        super.onPause();
    }

    public void onPrimaryClipChanged() {
        hideIme();
        finish();
    }

    private void saveToClipboard() {
        hideIme();
        Editable text = this.mEditText.getText();
        text.clearSpans();
        ClipData newPlainText = ClipData.newPlainText("text", text);
        PersistableBundle persistableBundle = new PersistableBundle();
        persistableBundle.putBoolean("android.content.extra.IS_SENSITIVE", this.mSensitive);
        newPlainText.getDescription().setExtras(persistableBundle);
        this.mClipboardManager.setPrimaryClip(newPlainText);
        finish();
    }

    private void hideIme() {
        ((InputMethodManager) getSystemService(InputMethodManager.class)).hideSoftInputFromWindow(this.mEditText.getWindowToken(), 0);
    }
}
