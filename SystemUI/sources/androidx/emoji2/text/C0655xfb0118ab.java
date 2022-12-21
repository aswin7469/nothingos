package androidx.emoji2.text;

import android.os.Handler;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.text.EmojiCompatInitializer;

/* renamed from: androidx.emoji2.text.EmojiCompatInitializer$BackgroundDefaultLoader$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C0655xfb0118ab implements Runnable {
    public final /* synthetic */ EmojiCompatInitializer.BackgroundDefaultLoader f$0;
    public final /* synthetic */ EmojiCompat.MetadataRepoLoaderCallback f$1;
    public final /* synthetic */ Handler f$2;

    public /* synthetic */ C0655xfb0118ab(EmojiCompatInitializer.BackgroundDefaultLoader backgroundDefaultLoader, EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback, Handler handler) {
        this.f$0 = backgroundDefaultLoader;
        this.f$1 = metadataRepoLoaderCallback;
        this.f$2 = handler;
    }

    public final void run() {
        this.f$0.mo14342x5cc8028a(this.f$1, this.f$2);
    }
}
