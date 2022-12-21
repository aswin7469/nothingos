package androidx.emoji2.text;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.MetaKeyKeyListener;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import androidx.core.graphics.PaintCompat;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.text.MetadataRepo;
import java.util.Arrays;

final class EmojiProcessor {
    private static final int ACTION_ADVANCE_BOTH = 1;
    private static final int ACTION_ADVANCE_END = 2;
    private static final int ACTION_FLUSH = 3;
    private final int[] mEmojiAsDefaultStyleExceptions;
    private EmojiCompat.GlyphChecker mGlyphChecker;
    private final MetadataRepo mMetadataRepo;
    private final EmojiCompat.SpanFactory mSpanFactory;
    private final boolean mUseEmojiAsDefaultStyle;

    private static boolean hasInvalidSelection(int i, int i2) {
        return i == -1 || i2 == -1 || i != i2;
    }

    EmojiProcessor(MetadataRepo metadataRepo, EmojiCompat.SpanFactory spanFactory, EmojiCompat.GlyphChecker glyphChecker, boolean z, int[] iArr) {
        this.mSpanFactory = spanFactory;
        this.mMetadataRepo = metadataRepo;
        this.mGlyphChecker = glyphChecker;
        this.mUseEmojiAsDefaultStyle = z;
        this.mEmojiAsDefaultStyleExceptions = iArr;
    }

    /* access modifiers changed from: package-private */
    public EmojiMetadata getEmojiMetadata(CharSequence charSequence) {
        ProcessorSm processorSm = new ProcessorSm(this.mMetadataRepo.getRootNode(), this.mUseEmojiAsDefaultStyle, this.mEmojiAsDefaultStyleExceptions);
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            int codePointAt = Character.codePointAt(charSequence, i);
            if (processorSm.check(codePointAt) != 2) {
                return null;
            }
            i += Character.charCount(codePointAt);
        }
        if (processorSm.isInFlushableState()) {
            return processorSm.getCurrentMetadata();
        }
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: android.text.Spannable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: android.text.Spannable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: android.text.Spannable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.lang.CharSequence} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v6, resolved type: android.text.Spannable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v7, resolved type: android.text.Spannable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: android.text.Spannable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: android.text.Spannable} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v10, resolved type: android.text.Spannable} */
    /* JADX WARNING: type inference failed for: r2v4, types: [java.lang.CharSequence] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0043 A[Catch:{ all -> 0x0117 }] */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x005f A[Catch:{ all -> 0x0117 }] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0110  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.CharSequence process(java.lang.CharSequence r10, int r11, int r12, int r13, boolean r14) {
        /*
            r9 = this;
            boolean r0 = r10 instanceof androidx.emoji2.text.SpannableBuilder
            if (r0 == 0) goto L_0x000a
            r1 = r10
            androidx.emoji2.text.SpannableBuilder r1 = (androidx.emoji2.text.SpannableBuilder) r1
            r1.beginBatchEdit()
        L_0x000a:
            if (r0 != 0) goto L_0x002c
            boolean r1 = r10 instanceof android.text.Spannable     // Catch:{ all -> 0x0117 }
            if (r1 == 0) goto L_0x0011
            goto L_0x002c
        L_0x0011:
            boolean r1 = r10 instanceof android.text.Spanned     // Catch:{ all -> 0x0117 }
            if (r1 == 0) goto L_0x002a
            r1 = r10
            android.text.Spanned r1 = (android.text.Spanned) r1     // Catch:{ all -> 0x0117 }
            int r2 = r11 + -1
            int r3 = r12 + 1
            java.lang.Class<androidx.emoji2.text.EmojiSpan> r4 = androidx.emoji2.text.EmojiSpan.class
            int r1 = r1.nextSpanTransition(r2, r3, r4)     // Catch:{ all -> 0x0117 }
            if (r1 > r12) goto L_0x002a
            android.text.SpannableString r1 = new android.text.SpannableString     // Catch:{ all -> 0x0117 }
            r1.<init>(r10)     // Catch:{ all -> 0x0117 }
            goto L_0x002f
        L_0x002a:
            r1 = 0
            goto L_0x002f
        L_0x002c:
            r1 = r10
            android.text.Spannable r1 = (android.text.Spannable) r1     // Catch:{ all -> 0x0117 }
        L_0x002f:
            r2 = 0
            if (r1 == 0) goto L_0x005d
            java.lang.Class<androidx.emoji2.text.EmojiSpan> r3 = androidx.emoji2.text.EmojiSpan.class
            java.lang.Object[] r3 = r1.getSpans(r11, r12, r3)     // Catch:{ all -> 0x0117 }
            androidx.emoji2.text.EmojiSpan[] r3 = (androidx.emoji2.text.EmojiSpan[]) r3     // Catch:{ all -> 0x0117 }
            if (r3 == 0) goto L_0x005d
            int r4 = r3.length     // Catch:{ all -> 0x0117 }
            if (r4 <= 0) goto L_0x005d
            int r4 = r3.length     // Catch:{ all -> 0x0117 }
            r5 = r2
        L_0x0041:
            if (r5 >= r4) goto L_0x005d
            r6 = r3[r5]     // Catch:{ all -> 0x0117 }
            int r7 = r1.getSpanStart(r6)     // Catch:{ all -> 0x0117 }
            int r8 = r1.getSpanEnd(r6)     // Catch:{ all -> 0x0117 }
            if (r7 == r12) goto L_0x0052
            r1.removeSpan(r6)     // Catch:{ all -> 0x0117 }
        L_0x0052:
            int r11 = java.lang.Math.min((int) r7, (int) r11)     // Catch:{ all -> 0x0117 }
            int r12 = java.lang.Math.max((int) r8, (int) r12)     // Catch:{ all -> 0x0117 }
            int r5 = r5 + 1
            goto L_0x0041
        L_0x005d:
            if (r11 == r12) goto L_0x010e
            int r3 = r10.length()     // Catch:{ all -> 0x0117 }
            if (r11 < r3) goto L_0x0067
            goto L_0x010e
        L_0x0067:
            r3 = 2147483647(0x7fffffff, float:NaN)
            if (r13 == r3) goto L_0x007c
            if (r1 == 0) goto L_0x007c
            int r3 = r1.length()     // Catch:{ all -> 0x0117 }
            java.lang.Class<androidx.emoji2.text.EmojiSpan> r4 = androidx.emoji2.text.EmojiSpan.class
            java.lang.Object[] r3 = r1.getSpans(r2, r3, r4)     // Catch:{ all -> 0x0117 }
            androidx.emoji2.text.EmojiSpan[] r3 = (androidx.emoji2.text.EmojiSpan[]) r3     // Catch:{ all -> 0x0117 }
            int r3 = r3.length     // Catch:{ all -> 0x0117 }
            int r13 = r13 - r3
        L_0x007c:
            androidx.emoji2.text.EmojiProcessor$ProcessorSm r3 = new androidx.emoji2.text.EmojiProcessor$ProcessorSm     // Catch:{ all -> 0x0117 }
            androidx.emoji2.text.MetadataRepo r4 = r9.mMetadataRepo     // Catch:{ all -> 0x0117 }
            androidx.emoji2.text.MetadataRepo$Node r4 = r4.getRootNode()     // Catch:{ all -> 0x0117 }
            boolean r5 = r9.mUseEmojiAsDefaultStyle     // Catch:{ all -> 0x0117 }
            int[] r6 = r9.mEmojiAsDefaultStyleExceptions     // Catch:{ all -> 0x0117 }
            r3.<init>(r4, r5, r6)     // Catch:{ all -> 0x0117 }
            int r4 = java.lang.Character.codePointAt((java.lang.CharSequence) r10, (int) r11)     // Catch:{ all -> 0x0117 }
            r5 = r4
            r4 = r2
            r2 = r1
        L_0x0092:
            r1 = r11
        L_0x0093:
            if (r11 >= r12) goto L_0x00e0
            if (r4 >= r13) goto L_0x00e0
            int r6 = r3.check(r5)     // Catch:{ all -> 0x0117 }
            r7 = 1
            if (r6 == r7) goto L_0x00ce
            r7 = 2
            if (r6 == r7) goto L_0x00c2
            r7 = 3
            if (r6 == r7) goto L_0x00a5
            goto L_0x0093
        L_0x00a5:
            if (r14 != 0) goto L_0x00b1
            androidx.emoji2.text.EmojiMetadata r6 = r3.getFlushMetadata()     // Catch:{ all -> 0x0117 }
            boolean r6 = r9.hasGlyph(r10, r1, r11, r6)     // Catch:{ all -> 0x0117 }
            if (r6 != 0) goto L_0x0092
        L_0x00b1:
            if (r2 != 0) goto L_0x00b8
            android.text.SpannableString r2 = new android.text.SpannableString     // Catch:{ all -> 0x0117 }
            r2.<init>(r10)     // Catch:{ all -> 0x0117 }
        L_0x00b8:
            androidx.emoji2.text.EmojiMetadata r6 = r3.getFlushMetadata()     // Catch:{ all -> 0x0117 }
            r9.addEmoji(r2, r6, r1, r11)     // Catch:{ all -> 0x0117 }
            int r4 = r4 + 1
            goto L_0x0092
        L_0x00c2:
            int r6 = java.lang.Character.charCount(r5)     // Catch:{ all -> 0x0117 }
            int r11 = r11 + r6
            if (r11 >= r12) goto L_0x0093
            int r5 = java.lang.Character.codePointAt((java.lang.CharSequence) r10, (int) r11)     // Catch:{ all -> 0x0117 }
            goto L_0x0093
        L_0x00ce:
            int r11 = java.lang.Character.codePointAt((java.lang.CharSequence) r10, (int) r1)     // Catch:{ all -> 0x0117 }
            int r11 = java.lang.Character.charCount(r11)     // Catch:{ all -> 0x0117 }
            int r1 = r1 + r11
            if (r1 >= r12) goto L_0x00de
            int r11 = java.lang.Character.codePointAt((java.lang.CharSequence) r10, (int) r1)     // Catch:{ all -> 0x0117 }
            r5 = r11
        L_0x00de:
            r11 = r1
            goto L_0x0093
        L_0x00e0:
            boolean r12 = r3.isInFlushableState()     // Catch:{ all -> 0x0117 }
            if (r12 == 0) goto L_0x0103
            if (r4 >= r13) goto L_0x0103
            if (r14 != 0) goto L_0x00f4
            androidx.emoji2.text.EmojiMetadata r12 = r3.getCurrentMetadata()     // Catch:{ all -> 0x0117 }
            boolean r12 = r9.hasGlyph(r10, r1, r11, r12)     // Catch:{ all -> 0x0117 }
            if (r12 != 0) goto L_0x0103
        L_0x00f4:
            if (r2 != 0) goto L_0x00fc
            android.text.SpannableString r12 = new android.text.SpannableString     // Catch:{ all -> 0x0117 }
            r12.<init>(r10)     // Catch:{ all -> 0x0117 }
            r2 = r12
        L_0x00fc:
            androidx.emoji2.text.EmojiMetadata r12 = r3.getCurrentMetadata()     // Catch:{ all -> 0x0117 }
            r9.addEmoji(r2, r12, r1, r11)     // Catch:{ all -> 0x0117 }
        L_0x0103:
            if (r2 != 0) goto L_0x0106
            r2 = r10
        L_0x0106:
            if (r0 == 0) goto L_0x010d
            androidx.emoji2.text.SpannableBuilder r10 = (androidx.emoji2.text.SpannableBuilder) r10
            r10.endBatchEdit()
        L_0x010d:
            return r2
        L_0x010e:
            if (r0 == 0) goto L_0x0116
            r9 = r10
            androidx.emoji2.text.SpannableBuilder r9 = (androidx.emoji2.text.SpannableBuilder) r9
            r9.endBatchEdit()
        L_0x0116:
            return r10
        L_0x0117:
            r9 = move-exception
            if (r0 == 0) goto L_0x011f
            androidx.emoji2.text.SpannableBuilder r10 = (androidx.emoji2.text.SpannableBuilder) r10
            r10.endBatchEdit()
        L_0x011f:
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.emoji2.text.EmojiProcessor.process(java.lang.CharSequence, int, int, int, boolean):java.lang.CharSequence");
    }

    static boolean handleOnKeyDown(Editable editable, int i, KeyEvent keyEvent) {
        boolean z;
        if (i != 67) {
            z = i != 112 ? false : delete(editable, keyEvent, true);
        } else {
            z = delete(editable, keyEvent, false);
        }
        if (!z) {
            return false;
        }
        MetaKeyKeyListener.adjustMetaAfterKeypress(editable);
        return true;
    }

    private static boolean delete(Editable editable, KeyEvent keyEvent, boolean z) {
        EmojiSpan[] emojiSpanArr;
        if (hasModifiers(keyEvent)) {
            return false;
        }
        int selectionStart = Selection.getSelectionStart(editable);
        int selectionEnd = Selection.getSelectionEnd(editable);
        if (!hasInvalidSelection(selectionStart, selectionEnd) && (emojiSpanArr = (EmojiSpan[]) editable.getSpans(selectionStart, selectionEnd, EmojiSpan.class)) != null && emojiSpanArr.length > 0) {
            int length = emojiSpanArr.length;
            int i = 0;
            while (i < length) {
                EmojiSpan emojiSpan = emojiSpanArr[i];
                int spanStart = editable.getSpanStart(emojiSpan);
                int spanEnd = editable.getSpanEnd(emojiSpan);
                if ((!z || spanStart != selectionStart) && ((z || spanEnd != selectionStart) && (selectionStart <= spanStart || selectionStart >= spanEnd))) {
                    i++;
                } else {
                    editable.delete(spanStart, spanEnd);
                    return true;
                }
            }
        }
        return false;
    }

    static boolean handleDeleteSurroundingText(InputConnection inputConnection, Editable editable, int i, int i2, boolean z) {
        int i3;
        int i4;
        if (editable != null && inputConnection != null && i >= 0 && i2 >= 0) {
            int selectionStart = Selection.getSelectionStart(editable);
            int selectionEnd = Selection.getSelectionEnd(editable);
            if (hasInvalidSelection(selectionStart, selectionEnd)) {
                return false;
            }
            if (z) {
                i4 = CodepointIndexFinder.findIndexBackward(editable, selectionStart, Math.max(i, 0));
                i3 = CodepointIndexFinder.findIndexForward(editable, selectionEnd, Math.max(i2, 0));
                if (i4 == -1 || i3 == -1) {
                    return false;
                }
            } else {
                i4 = Math.max(selectionStart - i, 0);
                i3 = Math.min(selectionEnd + i2, editable.length());
            }
            EmojiSpan[] emojiSpanArr = (EmojiSpan[]) editable.getSpans(i4, i3, EmojiSpan.class);
            if (emojiSpanArr != null && emojiSpanArr.length > 0) {
                for (EmojiSpan emojiSpan : emojiSpanArr) {
                    int spanStart = editable.getSpanStart(emojiSpan);
                    int spanEnd = editable.getSpanEnd(emojiSpan);
                    i4 = Math.min(spanStart, i4);
                    i3 = Math.max(spanEnd, i3);
                }
                int max = Math.max(i4, 0);
                int min = Math.min(i3, editable.length());
                inputConnection.beginBatchEdit();
                editable.delete(max, min);
                inputConnection.endBatchEdit();
                return true;
            }
        }
        return false;
    }

    private static boolean hasModifiers(KeyEvent keyEvent) {
        return !KeyEvent.metaStateHasNoModifiers(keyEvent.getMetaState());
    }

    private void addEmoji(Spannable spannable, EmojiMetadata emojiMetadata, int i, int i2) {
        spannable.setSpan(this.mSpanFactory.createSpan(emojiMetadata), i, i2, 33);
    }

    private boolean hasGlyph(CharSequence charSequence, int i, int i2, EmojiMetadata emojiMetadata) {
        if (emojiMetadata.getHasGlyph() == 0) {
            emojiMetadata.setHasGlyph(this.mGlyphChecker.hasGlyph(charSequence, i, i2, emojiMetadata.getSdkAdded()));
        }
        return emojiMetadata.getHasGlyph() == 2;
    }

    static final class ProcessorSm {
        private static final int STATE_DEFAULT = 1;
        private static final int STATE_WALKING = 2;
        private int mCurrentDepth;
        private MetadataRepo.Node mCurrentNode;
        private final int[] mEmojiAsDefaultStyleExceptions;
        private MetadataRepo.Node mFlushNode;
        private int mLastCodepoint;
        private final MetadataRepo.Node mRootNode;
        private int mState = 1;
        private final boolean mUseEmojiAsDefaultStyle;

        private static boolean isEmojiStyle(int i) {
            return i == 65039;
        }

        private static boolean isTextStyle(int i) {
            return i == 65038;
        }

        ProcessorSm(MetadataRepo.Node node, boolean z, int[] iArr) {
            this.mRootNode = node;
            this.mCurrentNode = node;
            this.mUseEmojiAsDefaultStyle = z;
            this.mEmojiAsDefaultStyleExceptions = iArr;
        }

        /* access modifiers changed from: package-private */
        public int check(int i) {
            MetadataRepo.Node node = this.mCurrentNode.get(i);
            int i2 = 2;
            if (this.mState != 2) {
                if (node == null) {
                    i2 = reset();
                } else {
                    this.mState = 2;
                    this.mCurrentNode = node;
                    this.mCurrentDepth = 1;
                }
            } else if (node != null) {
                this.mCurrentNode = node;
                this.mCurrentDepth++;
            } else if (isTextStyle(i)) {
                i2 = reset();
            } else if (!isEmojiStyle(i)) {
                if (this.mCurrentNode.getData() != null) {
                    i2 = 3;
                    if (this.mCurrentDepth != 1) {
                        this.mFlushNode = this.mCurrentNode;
                        reset();
                    } else if (shouldUseEmojiPresentationStyleForSingleCodepoint()) {
                        this.mFlushNode = this.mCurrentNode;
                        reset();
                    } else {
                        i2 = reset();
                    }
                } else {
                    i2 = reset();
                }
            }
            this.mLastCodepoint = i;
            return i2;
        }

        private int reset() {
            this.mState = 1;
            this.mCurrentNode = this.mRootNode;
            this.mCurrentDepth = 0;
            return 1;
        }

        /* access modifiers changed from: package-private */
        public EmojiMetadata getFlushMetadata() {
            return this.mFlushNode.getData();
        }

        /* access modifiers changed from: package-private */
        public EmojiMetadata getCurrentMetadata() {
            return this.mCurrentNode.getData();
        }

        /* access modifiers changed from: package-private */
        public boolean isInFlushableState() {
            if (this.mState != 2 || this.mCurrentNode.getData() == null || (this.mCurrentDepth <= 1 && !shouldUseEmojiPresentationStyleForSingleCodepoint())) {
                return false;
            }
            return true;
        }

        private boolean shouldUseEmojiPresentationStyleForSingleCodepoint() {
            if (this.mCurrentNode.getData().isDefaultEmoji() || isEmojiStyle(this.mLastCodepoint)) {
                return true;
            }
            if (this.mUseEmojiAsDefaultStyle) {
                if (this.mEmojiAsDefaultStyleExceptions == null) {
                    return true;
                }
                if (Arrays.binarySearch(this.mEmojiAsDefaultStyleExceptions, this.mCurrentNode.getData().getCodepointAt(0)) < 0) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final class CodepointIndexFinder {
        private static final int INVALID_INDEX = -1;

        private CodepointIndexFinder() {
        }

        static int findIndexBackward(CharSequence charSequence, int i, int i2) {
            int length = charSequence.length();
            if (i < 0 || length < i || i2 < 0) {
                return -1;
            }
            while (true) {
                boolean z = false;
                while (i2 != 0) {
                    i--;
                    if (i < 0) {
                        return z ? -1 : 0;
                    }
                    char charAt = charSequence.charAt(i);
                    if (z) {
                        if (!Character.isHighSurrogate(charAt)) {
                            return -1;
                        }
                        i2--;
                    } else if (!Character.isSurrogate(charAt)) {
                        i2--;
                    } else if (Character.isHighSurrogate(charAt)) {
                        return -1;
                    } else {
                        z = true;
                    }
                }
                return i;
            }
        }

        static int findIndexForward(CharSequence charSequence, int i, int i2) {
            int length = charSequence.length();
            if (i < 0 || length < i || i2 < 0) {
                return -1;
            }
            while (true) {
                boolean z = false;
                while (i2 != 0) {
                    if (r7 < length) {
                        char charAt = charSequence.charAt(r7);
                        if (z) {
                            if (!Character.isLowSurrogate(charAt)) {
                                return -1;
                            }
                            i2--;
                            i = r7 + 1;
                        } else if (!Character.isSurrogate(charAt)) {
                            i2--;
                            r7++;
                        } else if (Character.isLowSurrogate(charAt)) {
                            return -1;
                        } else {
                            r7++;
                            z = true;
                        }
                    } else if (z) {
                        return -1;
                    } else {
                        return length;
                    }
                }
                return r7;
            }
        }
    }

    public static class DefaultGlyphChecker implements EmojiCompat.GlyphChecker {
        private static final int PAINT_TEXT_SIZE = 10;
        private static final ThreadLocal<StringBuilder> sStringBuilder = new ThreadLocal<>();
        private final TextPaint mTextPaint;

        DefaultGlyphChecker() {
            TextPaint textPaint = new TextPaint();
            this.mTextPaint = textPaint;
            textPaint.setTextSize(10.0f);
        }

        public boolean hasGlyph(CharSequence charSequence, int i, int i2, int i3) {
            StringBuilder stringBuilder = getStringBuilder();
            stringBuilder.setLength(0);
            while (i < i2) {
                stringBuilder.append(charSequence.charAt(i));
                i++;
            }
            return PaintCompat.hasGlyph(this.mTextPaint, stringBuilder.toString());
        }

        private static StringBuilder getStringBuilder() {
            ThreadLocal<StringBuilder> threadLocal = sStringBuilder;
            if (threadLocal.get() == null) {
                threadLocal.set(new StringBuilder());
            }
            return threadLocal.get();
        }
    }
}
