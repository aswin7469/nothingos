package com.android.systemui.statusbar;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.BufferedWriter;
import java.p026io.FileWriter;
import java.p026io.IOException;
import java.p026io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class GestureRecorder {
    public static final boolean DEBUG = true;
    static final long SAVE_DELAY = 5000;
    static final int SAVE_MESSAGE = 6351;
    public static final String TAG = "GestureRecorder";
    private Gesture mCurrentGesture;
    private LinkedList<Gesture> mGestures;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == GestureRecorder.SAVE_MESSAGE) {
                GestureRecorder.this.save();
            }
        }
    };
    private int mLastSaveLen = -1;
    private String mLogfile;

    public class Gesture {
        boolean mComplete = false;
        long mDownTime = -1;
        private LinkedList<Record> mRecords = new LinkedList<>();
        private HashSet<String> mTags = new HashSet<>();

        public abstract class Record {
            long time;

            public abstract String toJson();

            public Record() {
            }
        }

        public Gesture() {
        }

        public class MotionEventRecord extends Record {
            public MotionEvent event;

            public MotionEventRecord(long j, MotionEvent motionEvent) {
                super();
                this.time = j;
                this.event = MotionEvent.obtain(motionEvent);
            }

            /* access modifiers changed from: package-private */
            public String actionName(int i) {
                if (i == 0) {
                    return "down";
                }
                if (i == 1) {
                    return "up";
                }
                if (i != 2) {
                    return i != 3 ? String.valueOf(i) : "cancel";
                }
                return "move";
            }

            public String toJson() {
                return String.format("{\"type\":\"motion\", \"time\":%d, \"action\":\"%s\", \"x\":%.2f, \"y\":%.2f, \"s\":%.2f, \"p\":%.2f}", Long.valueOf(this.time), actionName(this.event.getAction()), Float.valueOf(this.event.getRawX()), Float.valueOf(this.event.getRawY()), Float.valueOf(this.event.getSize()), Float.valueOf(this.event.getPressure()));
            }
        }

        public class TagRecord extends Record {
            public String info;
            public String tag;

            public TagRecord(long j, String str, String str2) {
                super();
                this.time = j;
                this.tag = str;
                this.info = str2;
            }

            public String toJson() {
                return String.format("{\"type\":\"tag\", \"time\":%d, \"tag\":\"%s\", \"info\":\"%s\"}", Long.valueOf(this.time), this.tag, this.info);
            }
        }

        public void add(MotionEvent motionEvent) {
            this.mRecords.add(new MotionEventRecord(motionEvent.getEventTime(), motionEvent));
            long j = this.mDownTime;
            if (j < 0) {
                this.mDownTime = motionEvent.getDownTime();
            } else if (j != motionEvent.getDownTime()) {
                Log.w(GestureRecorder.TAG, "Assertion failure in GestureRecorder: event downTime (" + motionEvent.getDownTime() + ") does not match gesture downTime (" + this.mDownTime + NavigationBarInflaterView.KEY_CODE_END);
            }
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 1 || actionMasked == 3) {
                this.mComplete = true;
            }
        }

        public void tag(long j, String str, String str2) {
            this.mRecords.add(new TagRecord(j, str, str2));
            this.mTags.add(str);
        }

        public boolean isComplete() {
            return this.mComplete;
        }

        public String toJson() {
            StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
            Iterator<Record> it = this.mRecords.iterator();
            boolean z = true;
            while (it.hasNext()) {
                Record next = it.next();
                if (!z) {
                    sb.append(", ");
                }
                sb.append(next.toJson());
                z = false;
            }
            sb.append(NavigationBarInflaterView.SIZE_MOD_END);
            return sb.toString();
        }
    }

    public GestureRecorder(String str) {
        this.mLogfile = str;
        this.mGestures = new LinkedList<>();
        this.mCurrentGesture = null;
    }

    public void add(MotionEvent motionEvent) {
        synchronized (this.mGestures) {
            Gesture gesture = this.mCurrentGesture;
            if (gesture == null || gesture.isComplete()) {
                Gesture gesture2 = new Gesture();
                this.mCurrentGesture = gesture2;
                this.mGestures.add(gesture2);
            }
            this.mCurrentGesture.add(motionEvent);
        }
        saveLater();
    }

    public void tag(long j, String str, String str2) {
        synchronized (this.mGestures) {
            if (this.mCurrentGesture == null) {
                Gesture gesture = new Gesture();
                this.mCurrentGesture = gesture;
                this.mGestures.add(gesture);
            }
            this.mCurrentGesture.tag(j, str, str2);
        }
        saveLater();
    }

    public void tag(long j, String str) {
        tag(j, str, (String) null);
    }

    public void tag(String str) {
        tag(SystemClock.uptimeMillis(), str, (String) null);
    }

    public void tag(String str, String str2) {
        tag(SystemClock.uptimeMillis(), str, str2);
    }

    public String toJsonLocked() {
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        Iterator<Gesture> it = this.mGestures.iterator();
        boolean z = true;
        int i = 0;
        while (it.hasNext()) {
            Gesture next = it.next();
            if (next.isComplete()) {
                if (!z) {
                    sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
                }
                sb.append(next.toJson());
                i++;
                z = false;
            }
        }
        this.mLastSaveLen = i;
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }

    public String toJson() {
        String jsonLocked;
        synchronized (this.mGestures) {
            jsonLocked = toJsonLocked();
        }
        return jsonLocked;
    }

    public void saveLater() {
        this.mHandler.removeMessages(SAVE_MESSAGE);
        this.mHandler.sendEmptyMessageDelayed(SAVE_MESSAGE, 5000);
    }

    public void save() {
        synchronized (this.mGestures) {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.mLogfile, true));
                bufferedWriter.append((CharSequence) toJsonLocked() + "\n");
                bufferedWriter.close();
                this.mGestures.clear();
                Gesture gesture = this.mCurrentGesture;
                if (gesture != null && !gesture.isComplete()) {
                    this.mGestures.add(this.mCurrentGesture);
                }
                Log.v(TAG, String.format("Wrote %d complete gestures to %s", Integer.valueOf(this.mLastSaveLen), this.mLogfile));
            } catch (IOException e) {
                Log.e(TAG, String.format("Couldn't write gestures to %s", this.mLogfile), e);
                this.mLastSaveLen = -1;
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        save();
        if (this.mLastSaveLen >= 0) {
            printWriter.println(String.valueOf(this.mLastSaveLen) + " gestures written to " + this.mLogfile);
        } else {
            printWriter.println("error writing gestures");
        }
    }
}
