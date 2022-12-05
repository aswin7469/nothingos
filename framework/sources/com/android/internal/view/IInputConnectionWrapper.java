package com.android.internal.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Trace;
import android.util.Log;
import android.util.imetracing.ImeTracing;
import android.util.imetracing.InputConnectionHelper;
import android.util.proto.ProtoOutputStream;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.DumpableInputConnection;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionInspector;
import android.view.inputmethod.InputContentInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.SurroundingText;
import com.android.internal.inputmethod.ICharSequenceResultCallback;
import com.android.internal.inputmethod.IExtractedTextResultCallback;
import com.android.internal.inputmethod.IIntResultCallback;
import com.android.internal.inputmethod.ISurroundingTextResultCallback;
import com.android.internal.os.SomeArgs;
import com.android.internal.view.IInputContext;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes4.dex */
public final class IInputConnectionWrapper extends IInputContext.Stub {
    private static final boolean DEBUG = false;
    private static final int DO_BEGIN_BATCH_EDIT = 90;
    private static final int DO_CLEAR_META_KEY_STATES = 130;
    private static final int DO_CLOSE_CONNECTION = 150;
    private static final int DO_COMMIT_COMPLETION = 55;
    private static final int DO_COMMIT_CONTENT = 160;
    private static final int DO_COMMIT_CORRECTION = 56;
    private static final int DO_COMMIT_TEXT = 50;
    private static final int DO_DELETE_SURROUNDING_TEXT = 80;
    private static final int DO_DELETE_SURROUNDING_TEXT_IN_CODE_POINTS = 81;
    private static final int DO_END_BATCH_EDIT = 95;
    private static final int DO_FINISH_COMPOSING_TEXT = 65;
    private static final int DO_GET_CURSOR_CAPS_MODE = 30;
    private static final int DO_GET_EXTRACTED_TEXT = 40;
    private static final int DO_GET_SELECTED_TEXT = 25;
    private static final int DO_GET_SURROUNDING_TEXT = 41;
    private static final int DO_GET_TEXT_AFTER_CURSOR = 10;
    private static final int DO_GET_TEXT_BEFORE_CURSOR = 20;
    private static final int DO_PERFORM_CONTEXT_MENU_ACTION = 59;
    private static final int DO_PERFORM_EDITOR_ACTION = 58;
    private static final int DO_PERFORM_PRIVATE_COMMAND = 120;
    private static final int DO_PERFORM_SPELL_CHECK = 110;
    private static final int DO_REQUEST_UPDATE_CURSOR_ANCHOR_INFO = 140;
    private static final int DO_SEND_KEY_EVENT = 70;
    private static final int DO_SET_COMPOSING_REGION = 63;
    private static final int DO_SET_COMPOSING_TEXT = 60;
    private static final int DO_SET_IME_CONSUMES_INPUT = 170;
    private static final int DO_SET_SELECTION = 57;
    private static final String TAG = "IInputConnectionWrapper";
    private Handler mH;
    private InputConnection mInputConnection;
    private Looper mMainLooper;
    private final InputMethodManager mParentInputMethodManager;
    private final WeakReference<View> mServedView;
    private final Object mLock = new Object();
    private boolean mFinished = false;

    /* loaded from: classes4.dex */
    class MyHandler extends Handler {
        MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            IInputConnectionWrapper.this.executeMessage(msg);
        }
    }

    public IInputConnectionWrapper(Looper mainLooper, InputConnection inputConnection, InputMethodManager inputMethodManager, View servedView) {
        this.mInputConnection = inputConnection;
        this.mMainLooper = mainLooper;
        this.mH = new MyHandler(this.mMainLooper);
        this.mParentInputMethodManager = inputMethodManager;
        this.mServedView = new WeakReference<>(servedView);
    }

    public InputConnection getInputConnection() {
        InputConnection inputConnection;
        synchronized (this.mLock) {
            inputConnection = this.mInputConnection;
        }
        return inputConnection;
    }

    private boolean isFinished() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mFinished;
        }
        return z;
    }

    public boolean isActive() {
        return this.mParentInputMethodManager.isActive() && !isFinished();
    }

    public View getServedView() {
        return this.mServedView.get();
    }

    public void deactivate() {
        Handler handler;
        if (isFinished()) {
            return;
        }
        closeConnection();
        final View servedView = this.mServedView.get();
        if (servedView != null && (handler = servedView.getHandler()) != null) {
            if (handler.getLooper().isCurrentThread()) {
                servedView.onInputConnectionClosedInternal();
                return;
            }
            Objects.requireNonNull(servedView);
            handler.post(new Runnable() { // from class: com.android.internal.view.IInputConnectionWrapper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    View.this.onInputConnectionClosedInternal();
                }
            });
        }
    }

    public String toString() {
        return "IInputConnectionWrapper{connection=" + getInputConnection() + " finished=" + isFinished() + " mParentInputMethodManager.isActive()=" + this.mParentInputMethodManager.isActive() + " mServedView=" + this.mServedView.get() + "}";
    }

    public void dumpDebug(ProtoOutputStream proto, long fieldId) {
        synchronized (this.mLock) {
            if ((this.mInputConnection instanceof DumpableInputConnection) && Looper.myLooper() == this.mMainLooper) {
                ((DumpableInputConnection) this.mInputConnection).dumpDebug(proto, fieldId);
            }
        }
    }

    @Override // com.android.internal.view.IInputContext
    public void getTextAfterCursor(int length, int flags, ICharSequenceResultCallback callback) {
        dispatchMessage(this.mH.obtainMessage(10, length, flags, callback));
    }

    @Override // com.android.internal.view.IInputContext
    public void getTextBeforeCursor(int length, int flags, ICharSequenceResultCallback callback) {
        dispatchMessage(this.mH.obtainMessage(20, length, flags, callback));
    }

    @Override // com.android.internal.view.IInputContext
    public void getSelectedText(int flags, ICharSequenceResultCallback callback) {
        dispatchMessage(this.mH.obtainMessage(25, flags, 0, callback));
    }

    @Override // com.android.internal.view.IInputContext
    public void getSurroundingText(int beforeLength, int afterLength, int flags, ISurroundingTextResultCallback callback) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = Integer.valueOf(beforeLength);
        args.arg2 = Integer.valueOf(afterLength);
        args.arg3 = Integer.valueOf(flags);
        args.arg4 = callback;
        dispatchMessage(this.mH.obtainMessage(41, flags, 0, args));
    }

    @Override // com.android.internal.view.IInputContext
    public void getCursorCapsMode(int reqModes, IIntResultCallback callback) {
        dispatchMessage(this.mH.obtainMessage(30, reqModes, 0, callback));
    }

    @Override // com.android.internal.view.IInputContext
    public void getExtractedText(ExtractedTextRequest request, int flags, IExtractedTextResultCallback callback) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = request;
        args.arg2 = callback;
        dispatchMessage(this.mH.obtainMessage(40, flags, 0, args));
    }

    @Override // com.android.internal.view.IInputContext
    public void commitText(CharSequence text, int newCursorPosition) {
        dispatchMessage(obtainMessageIO(50, newCursorPosition, text));
    }

    @Override // com.android.internal.view.IInputContext
    public void commitCompletion(CompletionInfo text) {
        dispatchMessage(obtainMessageO(55, text));
    }

    @Override // com.android.internal.view.IInputContext
    public void commitCorrection(CorrectionInfo info) {
        dispatchMessage(obtainMessageO(56, info));
    }

    @Override // com.android.internal.view.IInputContext
    public void setSelection(int start, int end) {
        dispatchMessage(obtainMessageII(57, start, end));
    }

    @Override // com.android.internal.view.IInputContext
    public void performEditorAction(int id) {
        dispatchMessage(obtainMessageII(58, id, 0));
    }

    @Override // com.android.internal.view.IInputContext
    public void performContextMenuAction(int id) {
        dispatchMessage(obtainMessageII(59, id, 0));
    }

    @Override // com.android.internal.view.IInputContext
    public void setComposingRegion(int start, int end) {
        dispatchMessage(obtainMessageII(63, start, end));
    }

    @Override // com.android.internal.view.IInputContext
    public void setComposingText(CharSequence text, int newCursorPosition) {
        dispatchMessage(obtainMessageIO(60, newCursorPosition, text));
    }

    @Override // com.android.internal.view.IInputContext
    public void finishComposingText() {
        dispatchMessage(obtainMessage(65));
    }

    @Override // com.android.internal.view.IInputContext
    public void sendKeyEvent(KeyEvent event) {
        dispatchMessage(obtainMessageO(70, event));
    }

    @Override // com.android.internal.view.IInputContext
    public void clearMetaKeyStates(int states) {
        dispatchMessage(obtainMessageII(130, states, 0));
    }

    @Override // com.android.internal.view.IInputContext
    public void deleteSurroundingText(int beforeLength, int afterLength) {
        dispatchMessage(obtainMessageII(80, beforeLength, afterLength));
    }

    @Override // com.android.internal.view.IInputContext
    public void deleteSurroundingTextInCodePoints(int beforeLength, int afterLength) {
        dispatchMessage(obtainMessageII(81, beforeLength, afterLength));
    }

    @Override // com.android.internal.view.IInputContext
    public void beginBatchEdit() {
        dispatchMessage(obtainMessage(90));
    }

    @Override // com.android.internal.view.IInputContext
    public void endBatchEdit() {
        dispatchMessage(obtainMessage(95));
    }

    @Override // com.android.internal.view.IInputContext
    public void performSpellCheck() {
        dispatchMessage(obtainMessage(110));
    }

    @Override // com.android.internal.view.IInputContext
    public void performPrivateCommand(String action, Bundle data) {
        dispatchMessage(obtainMessageOO(120, action, data));
    }

    @Override // com.android.internal.view.IInputContext
    public void requestUpdateCursorAnchorInfo(int cursorUpdateMode, IIntResultCallback callback) {
        dispatchMessage(this.mH.obtainMessage(140, cursorUpdateMode, 0, callback));
    }

    public void closeConnection() {
        dispatchMessage(obtainMessage(150));
    }

    @Override // com.android.internal.view.IInputContext
    public void commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts, IIntResultCallback callback) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = inputContentInfo;
        args.arg2 = opts;
        args.arg3 = callback;
        dispatchMessage(this.mH.obtainMessage(160, flags, 0, args));
    }

    @Override // com.android.internal.view.IInputContext
    public void setImeConsumesInput(boolean imeConsumesInput) {
        dispatchMessage(obtainMessageB(170, imeConsumesInput));
    }

    void dispatchMessage(Message msg) {
        if (Looper.myLooper() == this.mMainLooper) {
            executeMessage(msg);
            msg.recycle();
            return;
        }
        this.mH.sendMessage(msg);
    }

    /* JADX WARN: Can't wrap try/catch for region: R(10:26|27|(8:32|(6:37|(1:39)|40|41|42|43)|48|(0)|40|41|42|43)|49|50|(0)|40|41|42|43) */
    /* JADX WARN: Can't wrap try/catch for region: R(10:396|397|(7:402|403|(1:405)|406|407|408|409)|414|403|(0)|406|407|408|409) */
    /* JADX WARN: Can't wrap try/catch for region: R(10:419|420|(7:425|426|(1:428)|429|430|431|432)|437|426|(0)|429|430|431|432) */
    /* JADX WARN: Can't wrap try/catch for region: R(10:465|466|(7:471|472|(1:474)|475|476|477|478)|483|472|(0)|475|476|477|478) */
    /* JADX WARN: Can't wrap try/catch for region: R(10:511|512|(7:517|518|(1:520)|521|522|523|524)|529|518|(0)|521|522|523|524) */
    /* JADX WARN: Can't wrap try/catch for region: R(11:441|442|443|(7:448|449|(1:451)|452|453|454|455)|460|449|(0)|452|453|454|455) */
    /* JADX WARN: Can't wrap try/catch for region: R(11:487|488|489|(7:494|495|(1:497)|498|499|500|501)|506|495|(0)|498|499|500|501) */
    /* JADX WARN: Can't wrap try/catch for region: R(11:99|100|101|(6:106|(1:108)|109|110|111|112)|117|118|(0)|109|110|111|112) */
    /* JADX WARN: Code restructure failed: missing block: B:114:0x014b, code lost:
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:115:0x014c, code lost:
        android.util.Log.w(com.android.internal.view.IInputConnectionWrapper.TAG, "Failed to return the result to requestCursorUpdates(). result=" + r6, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x04e9, code lost:
        r9 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x04ea, code lost:
        android.util.Log.w(com.android.internal.view.IInputConnectionWrapper.TAG, "Failed to return the result to getSurroundingText(). result=" + r8, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:434:0x055c, code lost:
        r7 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:435:0x055d, code lost:
        android.util.Log.w(com.android.internal.view.IInputConnectionWrapper.TAG, "Failed to return the result to getExtractedText(). result=" + r6, r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:457:0x05c7, code lost:
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x05c8, code lost:
        android.util.Log.w(com.android.internal.view.IInputConnectionWrapper.TAG, "Failed to return the result to getCursorCapsMode(). result=" + r2, r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00ab, code lost:
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00ac, code lost:
        android.util.Log.w(com.android.internal.view.IInputConnectionWrapper.TAG, "Failed to return the result to commitContent(). result=" + r9, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:480:0x062c, code lost:
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x062d, code lost:
        android.util.Log.w(com.android.internal.view.IInputConnectionWrapper.TAG, "Failed to return the result to getSelectedText(). result=" + ((java.lang.Object) r2), r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x0695, code lost:
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x0696, code lost:
        android.util.Log.w(com.android.internal.view.IInputConnectionWrapper.TAG, "Failed to return the result to getTextBeforeCursor(). result=" + ((java.lang.Object) r2), r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x06fe, code lost:
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x06ff, code lost:
        android.util.Log.w(com.android.internal.view.IInputConnectionWrapper.TAG, "Failed to return the result to getTextAfterCursor(). result=" + ((java.lang.Object) r2), r5);
     */
    /* JADX WARN: Removed duplicated region for block: B:108:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:405:0x04d6 A[Catch: all -> 0x0508, TRY_LEAVE, TryCatch #20 {all -> 0x0508, blocks: (B:397:0x0496, B:399:0x04b8, B:402:0x04bf, B:403:0x04cc, B:405:0x04d6, B:407:0x04e5, B:412:0x04ea, B:414:0x04c4), top: B:396:0x0496, inners: #4 }] */
    /* JADX WARN: Removed duplicated region for block: B:428:0x0547 A[Catch: all -> 0x057b, TRY_LEAVE, TryCatch #14 {all -> 0x057b, blocks: (B:420:0x0519, B:422:0x0527, B:425:0x052e, B:426:0x053d, B:428:0x0547, B:430:0x0558, B:435:0x055d, B:437:0x0535), top: B:419:0x0519, inners: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:451:0x05b2 A[Catch: all -> 0x05e3, TRY_LEAVE, TryCatch #1 {all -> 0x05e3, blocks: (B:443:0x0588, B:445:0x0592, B:448:0x0599, B:449:0x05a8, B:451:0x05b2, B:453:0x05c3, B:458:0x05c8, B:460:0x05a0), top: B:442:0x0588, inners: #27 }] */
    /* JADX WARN: Removed duplicated region for block: B:474:0x0617 A[Catch: all -> 0x0648, TRY_LEAVE, TryCatch #19 {all -> 0x0648, blocks: (B:466:0x05ed, B:468:0x05f7, B:471:0x05fe, B:472:0x060d, B:474:0x0617, B:476:0x0628, B:481:0x062d, B:483:0x0605), top: B:465:0x05ed, inners: #12 }] */
    /* JADX WARN: Removed duplicated region for block: B:497:0x067e A[Catch: all -> 0x06b1, TRY_LEAVE, TryCatch #9 {all -> 0x06b1, blocks: (B:489:0x0652, B:491:0x065c, B:494:0x0663, B:495:0x0674, B:497:0x067e, B:499:0x0691, B:504:0x0696, B:506:0x066c), top: B:488:0x0652, inners: #35 }] */
    /* JADX WARN: Removed duplicated region for block: B:520:0x06e7 A[Catch: all -> 0x071a, TRY_LEAVE, TryCatch #34 {all -> 0x071a, blocks: (B:512:0x06bb, B:514:0x06c5, B:517:0x06cc, B:518:0x06dd, B:520:0x06e7, B:522:0x06fa, B:527:0x06ff, B:529:0x06d5), top: B:511:0x06bb, inners: #18 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void executeMessage(Message msg) {
        CharSequence result;
        CharSequence result2;
        CharSequence result3;
        int result4;
        ExtractedText result5;
        SurroundingText result6;
        boolean result7;
        boolean result8;
        int i = 0;
        boolean z = false;
        int i2 = 0;
        switch (msg.what) {
            case 10:
                Trace.traceBegin(4L, "InputConnection#getTextAfterCursor");
                try {
                    ICharSequenceResultCallback callback = (ICharSequenceResultCallback) msg.obj;
                    InputConnection ic = getInputConnection();
                    if (ic != null && isActive()) {
                        result = ic.getTextAfterCursor(msg.arg1, msg.arg2);
                        if (ImeTracing.getInstance().isEnabled()) {
                            ProtoOutputStream icProto = InputConnectionHelper.buildGetTextAfterCursorProto(msg.arg1, msg.arg2, result);
                            ImeTracing.getInstance().triggerClientDump("IInputConnectionWrapper#getTextAfterCursor", this.mParentInputMethodManager, icProto);
                        }
                        callback.onResult(result);
                        return;
                    }
                    Log.w(TAG, "getTextAfterCursor on inactive InputConnection");
                    result = null;
                    if (ImeTracing.getInstance().isEnabled()) {
                    }
                    callback.onResult(result);
                    return;
                } finally {
                }
            case 20:
                Trace.traceBegin(4L, "InputConnection#getTextBeforeCursor");
                try {
                    ICharSequenceResultCallback callback2 = (ICharSequenceResultCallback) msg.obj;
                    InputConnection ic2 = getInputConnection();
                    if (ic2 != null && isActive()) {
                        result2 = ic2.getTextBeforeCursor(msg.arg1, msg.arg2);
                        if (ImeTracing.getInstance().isEnabled()) {
                            ProtoOutputStream icProto2 = InputConnectionHelper.buildGetTextBeforeCursorProto(msg.arg1, msg.arg2, result2);
                            ImeTracing.getInstance().triggerClientDump("IInputConnectionWrapper#getTextBeforeCursor", this.mParentInputMethodManager, icProto2);
                        }
                        callback2.onResult(result2);
                        return;
                    }
                    Log.w(TAG, "getTextBeforeCursor on inactive InputConnection");
                    result2 = null;
                    if (ImeTracing.getInstance().isEnabled()) {
                    }
                    callback2.onResult(result2);
                    return;
                } finally {
                }
            case 25:
                Trace.traceBegin(4L, "InputConnection#getSelectedText");
                try {
                    ICharSequenceResultCallback callback3 = (ICharSequenceResultCallback) msg.obj;
                    InputConnection ic3 = getInputConnection();
                    if (ic3 != null && isActive()) {
                        result3 = ic3.getSelectedText(msg.arg1);
                        if (ImeTracing.getInstance().isEnabled()) {
                            ProtoOutputStream icProto3 = InputConnectionHelper.buildGetSelectedTextProto(msg.arg1, result3);
                            ImeTracing.getInstance().triggerClientDump("IInputConnectionWrapper#getSelectedText", this.mParentInputMethodManager, icProto3);
                        }
                        callback3.onResult(result3);
                        return;
                    }
                    Log.w(TAG, "getSelectedText on inactive InputConnection");
                    result3 = null;
                    if (ImeTracing.getInstance().isEnabled()) {
                    }
                    callback3.onResult(result3);
                    return;
                } finally {
                }
            case 30:
                Trace.traceBegin(4L, "InputConnection#getCursorCapsMode");
                try {
                    IIntResultCallback callback4 = (IIntResultCallback) msg.obj;
                    InputConnection ic4 = getInputConnection();
                    if (ic4 != null && isActive()) {
                        result4 = ic4.getCursorCapsMode(msg.arg1);
                        if (ImeTracing.getInstance().isEnabled()) {
                            ProtoOutputStream icProto4 = InputConnectionHelper.buildGetCursorCapsModeProto(msg.arg1, result4);
                            ImeTracing.getInstance().triggerClientDump("IInputConnectionWrapper#getCursorCapsMode", this.mParentInputMethodManager, icProto4);
                        }
                        callback4.onResult(result4);
                        return;
                    }
                    Log.w(TAG, "getCursorCapsMode on inactive InputConnection");
                    result4 = 0;
                    if (ImeTracing.getInstance().isEnabled()) {
                    }
                    callback4.onResult(result4);
                    return;
                } finally {
                }
            case 40:
                SomeArgs args = (SomeArgs) msg.obj;
                Trace.traceBegin(4L, "InputConnection#getExtractedText");
                try {
                    ExtractedTextRequest request = (ExtractedTextRequest) args.arg1;
                    IExtractedTextResultCallback callback5 = (IExtractedTextResultCallback) args.arg2;
                    InputConnection ic5 = getInputConnection();
                    if (ic5 != null && isActive()) {
                        result5 = ic5.getExtractedText(request, msg.arg1);
                        if (ImeTracing.getInstance().isEnabled()) {
                            ProtoOutputStream icProto5 = InputConnectionHelper.buildGetExtractedTextProto(request, msg.arg1, result5);
                            ImeTracing.getInstance().triggerClientDump("IInputConnectionWrapper#getExtractedText", this.mParentInputMethodManager, icProto5);
                        }
                        callback5.onResult(result5);
                        return;
                    }
                    Log.w(TAG, "getExtractedText on inactive InputConnection");
                    result5 = null;
                    if (ImeTracing.getInstance().isEnabled()) {
                    }
                    callback5.onResult(result5);
                    return;
                } finally {
                }
            case 41:
                SomeArgs args2 = (SomeArgs) msg.obj;
                Trace.traceBegin(4L, "InputConnection#getSurroundingText");
                try {
                    int beforeLength = ((Integer) args2.arg1).intValue();
                    int afterLength = ((Integer) args2.arg2).intValue();
                    int flags = ((Integer) args2.arg3).intValue();
                    ISurroundingTextResultCallback callback6 = (ISurroundingTextResultCallback) args2.arg4;
                    InputConnection ic6 = getInputConnection();
                    if (ic6 != null && isActive()) {
                        result6 = ic6.getSurroundingText(beforeLength, afterLength, flags);
                        if (ImeTracing.getInstance().isEnabled()) {
                            ProtoOutputStream icProto6 = InputConnectionHelper.buildGetSurroundingTextProto(beforeLength, afterLength, flags, result6);
                            ImeTracing.getInstance().triggerClientDump("IInputConnectionWrapper#getSurroundingText", this.mParentInputMethodManager, icProto6);
                        }
                        callback6.onResult(result6);
                        return;
                    }
                    Log.w(TAG, "getSurroundingText on inactive InputConnection");
                    result6 = null;
                    if (ImeTracing.getInstance().isEnabled()) {
                    }
                    callback6.onResult(result6);
                    return;
                } finally {
                }
            case 50:
                Trace.traceBegin(4L, "InputConnection#commitText");
                try {
                    InputConnection ic7 = getInputConnection();
                    if (ic7 != null && isActive()) {
                        ic7.commitText((CharSequence) msg.obj, msg.arg1);
                        return;
                    }
                    Log.w(TAG, "commitText on inactive InputConnection");
                    return;
                } finally {
                }
            case 55:
                Trace.traceBegin(4L, "InputConnection#commitCompletion");
                try {
                    InputConnection ic8 = getInputConnection();
                    if (ic8 != null && isActive()) {
                        ic8.commitCompletion((CompletionInfo) msg.obj);
                        return;
                    }
                    Log.w(TAG, "commitCompletion on inactive InputConnection");
                    return;
                } finally {
                }
            case 56:
                Trace.traceBegin(4L, "InputConnection#commitCorrection");
                try {
                    InputConnection ic9 = getInputConnection();
                    if (ic9 != null && isActive()) {
                        ic9.commitCorrection((CorrectionInfo) msg.obj);
                        return;
                    }
                    Log.w(TAG, "commitCorrection on inactive InputConnection");
                    return;
                } finally {
                }
            case 57:
                Trace.traceBegin(4L, "InputConnection#setSelection");
                try {
                    InputConnection ic10 = getInputConnection();
                    if (ic10 != null && isActive()) {
                        ic10.setSelection(msg.arg1, msg.arg2);
                        return;
                    }
                    Log.w(TAG, "setSelection on inactive InputConnection");
                    return;
                } finally {
                }
            case 58:
                Trace.traceBegin(4L, "InputConnection#performEditorAction");
                try {
                    InputConnection ic11 = getInputConnection();
                    if (ic11 != null && isActive()) {
                        ic11.performEditorAction(msg.arg1);
                        return;
                    }
                    Log.w(TAG, "performEditorAction on inactive InputConnection");
                    return;
                } finally {
                }
            case 59:
                Trace.traceBegin(4L, "InputConnection#performContextMenuAction");
                try {
                    InputConnection ic12 = getInputConnection();
                    if (ic12 != null && isActive()) {
                        ic12.performContextMenuAction(msg.arg1);
                        return;
                    }
                    Log.w(TAG, "performContextMenuAction on inactive InputConnection");
                    return;
                } finally {
                }
            case 60:
                Trace.traceBegin(4L, "InputConnection#setComposingText");
                try {
                    InputConnection ic13 = getInputConnection();
                    if (ic13 != null && isActive()) {
                        ic13.setComposingText((CharSequence) msg.obj, msg.arg1);
                        return;
                    }
                    Log.w(TAG, "setComposingText on inactive InputConnection");
                    return;
                } finally {
                }
            case 63:
                Trace.traceBegin(4L, "InputConnection#setComposingRegion");
                try {
                    InputConnection ic14 = getInputConnection();
                    if (ic14 != null && isActive()) {
                        ic14.setComposingRegion(msg.arg1, msg.arg2);
                        return;
                    }
                    Log.w(TAG, "setComposingRegion on inactive InputConnection");
                    return;
                } finally {
                }
            case 65:
                Trace.traceBegin(4L, "InputConnection#finishComposingText");
                try {
                    if (isFinished()) {
                        return;
                    }
                    InputConnection ic15 = getInputConnection();
                    if (ic15 == null) {
                        Log.w(TAG, "finishComposingText on inactive InputConnection");
                        return;
                    } else {
                        ic15.finishComposingText();
                        return;
                    }
                } finally {
                }
            case 70:
                Trace.traceBegin(4L, "InputConnection#sendKeyEvent");
                try {
                    InputConnection ic16 = getInputConnection();
                    if (ic16 != null && isActive()) {
                        ic16.sendKeyEvent((KeyEvent) msg.obj);
                        return;
                    }
                    Log.w(TAG, "sendKeyEvent on inactive InputConnection");
                    return;
                } finally {
                }
            case 80:
                Trace.traceBegin(4L, "InputConnection#deleteSurroundingText");
                try {
                    InputConnection ic17 = getInputConnection();
                    if (ic17 != null && isActive()) {
                        ic17.deleteSurroundingText(msg.arg1, msg.arg2);
                        return;
                    }
                    Log.w(TAG, "deleteSurroundingText on inactive InputConnection");
                    return;
                } finally {
                }
            case 81:
                Trace.traceBegin(4L, "InputConnection#deleteSurroundingTextInCodePoints");
                try {
                    InputConnection ic18 = getInputConnection();
                    if (ic18 != null && isActive()) {
                        ic18.deleteSurroundingTextInCodePoints(msg.arg1, msg.arg2);
                        return;
                    }
                    Log.w(TAG, "deleteSurroundingTextInCodePoints on inactive InputConnection");
                    return;
                } finally {
                }
            case 90:
                Trace.traceBegin(4L, "InputConnection#beginBatchEdit");
                try {
                    InputConnection ic19 = getInputConnection();
                    if (ic19 != null && isActive()) {
                        ic19.beginBatchEdit();
                        return;
                    }
                    Log.w(TAG, "beginBatchEdit on inactive InputConnection");
                    return;
                } finally {
                }
            case 95:
                Trace.traceBegin(4L, "InputConnection#endBatchEdit");
                try {
                    InputConnection ic20 = getInputConnection();
                    if (ic20 != null && isActive()) {
                        ic20.endBatchEdit();
                        return;
                    }
                    Log.w(TAG, "endBatchEdit on inactive InputConnection");
                    return;
                } finally {
                }
            case 110:
                Trace.traceBegin(4L, "InputConnection#performSpellCheck");
                try {
                    InputConnection ic21 = getInputConnection();
                    if (ic21 != null && isActive()) {
                        ic21.performSpellCheck();
                        return;
                    }
                    Log.w(TAG, "performSpellCheck on inactive InputConnection");
                    return;
                } finally {
                }
            case 120:
                SomeArgs args3 = (SomeArgs) msg.obj;
                Trace.traceBegin(4L, "InputConnection#performPrivateCommand");
                try {
                    String action = (String) args3.arg1;
                    Bundle data = (Bundle) args3.arg2;
                    InputConnection ic22 = getInputConnection();
                    if (ic22 != null && isActive()) {
                        ic22.performPrivateCommand(action, data);
                        return;
                    }
                    Log.w(TAG, "performPrivateCommand on inactive InputConnection");
                    return;
                } finally {
                }
            case 130:
                Trace.traceBegin(4L, "InputConnection#clearMetaKeyStates");
                try {
                    InputConnection ic23 = getInputConnection();
                    if (ic23 != null && isActive()) {
                        ic23.clearMetaKeyStates(msg.arg1);
                        return;
                    }
                    Log.w(TAG, "clearMetaKeyStates on inactive InputConnection");
                    return;
                } finally {
                }
            case 140:
                Trace.traceBegin(4L, "InputConnection#requestCursorUpdates");
                try {
                    IIntResultCallback callback7 = (IIntResultCallback) msg.obj;
                    InputConnection ic24 = getInputConnection();
                    if (ic24 != null && isActive()) {
                        result7 = ic24.requestCursorUpdates(msg.arg1);
                        if (result7) {
                            i = 1;
                        }
                        callback7.onResult(i);
                        return;
                    }
                    Log.w(TAG, "requestCursorAnchorInfo on inactive InputConnection");
                    result7 = false;
                    if (result7) {
                    }
                    callback7.onResult(i);
                    return;
                } finally {
                }
            case 150:
                if (isFinished()) {
                    return;
                }
                Trace.traceBegin(4L, "InputConnection#closeConnection");
                try {
                    InputConnection ic25 = getInputConnection();
                    if (ic25 == null) {
                        synchronized (this.mLock) {
                            this.mInputConnection = null;
                            this.mFinished = true;
                        }
                        return;
                    }
                    int missingMethods = InputConnectionInspector.getMissingMethodFlags(ic25);
                    if ((missingMethods & 64) == 0) {
                        ic25.closeConnection();
                    }
                    synchronized (this.mLock) {
                        this.mInputConnection = null;
                        this.mFinished = true;
                    }
                    return;
                } catch (Throwable th) {
                    synchronized (this.mLock) {
                        this.mInputConnection = null;
                        this.mFinished = true;
                        throw th;
                    }
                }
            case 160:
                int flags2 = msg.arg1;
                SomeArgs args4 = (SomeArgs) msg.obj;
                Trace.traceBegin(4L, "InputConnection#commitContent");
                try {
                    IIntResultCallback callback8 = (IIntResultCallback) args4.arg3;
                    InputConnection ic26 = getInputConnection();
                    if (ic26 != null && isActive()) {
                        InputContentInfo inputContentInfo = (InputContentInfo) args4.arg1;
                        if (inputContentInfo != null && inputContentInfo.validate()) {
                            result8 = ic26.commitContent(inputContentInfo, flags2, (Bundle) args4.arg2);
                            if (result8) {
                                i2 = 1;
                            }
                            callback8.onResult(i2);
                            return;
                        }
                        Log.w(TAG, "commitContent with invalid inputContentInfo=" + inputContentInfo);
                        result8 = false;
                        if (result8) {
                        }
                        callback8.onResult(i2);
                        return;
                    }
                    Log.w(TAG, "commitContent on inactive InputConnection");
                    result8 = false;
                    if (result8) {
                    }
                    callback8.onResult(i2);
                    return;
                } finally {
                }
            case 170:
                Trace.traceBegin(4L, "InputConnection#setImeConsumesInput");
                try {
                    InputConnection ic27 = getInputConnection();
                    if (ic27 != null && isActive()) {
                        if (msg.arg1 == 1) {
                            z = true;
                        }
                        ic27.setImeConsumesInput(z);
                        return;
                    }
                    Log.w(TAG, "setImeConsumesInput on inactive InputConnection");
                    return;
                } finally {
                }
            default:
                Log.w(TAG, "Unhandled message code: " + msg.what);
                return;
        }
    }

    Message obtainMessage(int what) {
        return this.mH.obtainMessage(what);
    }

    Message obtainMessageII(int what, int arg1, int arg2) {
        return this.mH.obtainMessage(what, arg1, arg2);
    }

    Message obtainMessageO(int what, Object arg1) {
        return this.mH.obtainMessage(what, 0, 0, arg1);
    }

    Message obtainMessageIO(int what, int arg1, Object arg2) {
        return this.mH.obtainMessage(what, arg1, 0, arg2);
    }

    Message obtainMessageOO(int what, Object arg1, Object arg2) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = arg1;
        args.arg2 = arg2;
        return this.mH.obtainMessage(what, 0, 0, args);
    }

    Message obtainMessageB(int what, boolean arg1) {
        return this.mH.obtainMessage(what, arg1 ? 1 : 0, 0);
    }
}
