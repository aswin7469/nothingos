package com.android.systemui.analytics;

import android.hardware.SensorEvent;
import android.os.Build;
import android.view.MotionEvent;
import com.android.systemui.statusbar.phone.nano.TouchAnalyticsProto;
import java.util.ArrayList;

public class SensorLoggerSession {
    private static final String TAG = "SensorLoggerSession";
    private long mEndTimestampMillis;
    private ArrayList<TouchAnalyticsProto.Session.TouchEvent> mMotionEvents = new ArrayList<>();
    private ArrayList<TouchAnalyticsProto.Session.PhoneEvent> mPhoneEvents = new ArrayList<>();
    private int mResult = 2;
    private ArrayList<TouchAnalyticsProto.Session.SensorEvent> mSensorEvents = new ArrayList<>();
    private final long mStartSystemTimeNanos;
    private final long mStartTimestampMillis;
    private int mTouchAreaHeight;
    private int mTouchAreaWidth;
    private int mType;

    public SensorLoggerSession(long j, long j2) {
        this.mStartTimestampMillis = j;
        this.mStartSystemTimeNanos = j2;
        this.mType = 3;
    }

    public void setType(int i) {
        this.mType = i;
    }

    public void end(long j, int i) {
        this.mResult = i;
        this.mEndTimestampMillis = j;
    }

    public void addMotionEvent(MotionEvent motionEvent) {
        this.mMotionEvents.add(motionEventToProto(motionEvent));
    }

    public void addSensorEvent(SensorEvent sensorEvent, long j) {
        this.mSensorEvents.add(sensorEventToProto(sensorEvent, j));
    }

    public void addPhoneEvent(int i, long j) {
        this.mPhoneEvents.add(phoneEventToProto(i, j));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Session{mStartTimestampMillis=");
        sb.append(this.mStartTimestampMillis);
        sb.append(", mStartSystemTimeNanos=").append(this.mStartSystemTimeNanos);
        sb.append(", mEndTimestampMillis=").append(this.mEndTimestampMillis);
        sb.append(", mResult=").append(this.mResult);
        sb.append(", mTouchAreaHeight=").append(this.mTouchAreaHeight);
        sb.append(", mTouchAreaWidth=").append(this.mTouchAreaWidth);
        sb.append(", mMotionEvents=[size=").append(this.mMotionEvents.size()).append("], mSensorEvents=[size=");
        sb.append(this.mSensorEvents.size()).append("], mPhoneEvents=[size=");
        sb.append(this.mPhoneEvents.size()).append("]}");
        return sb.toString();
    }

    public TouchAnalyticsProto.Session toProto() {
        TouchAnalyticsProto.Session session = new TouchAnalyticsProto.Session();
        session.startTimestampMillis = this.mStartTimestampMillis;
        session.durationMillis = this.mEndTimestampMillis - this.mStartTimestampMillis;
        session.build = Build.FINGERPRINT;
        session.deviceId = Build.DEVICE;
        session.result = this.mResult;
        session.type = this.mType;
        session.sensorEvents = (TouchAnalyticsProto.Session.SensorEvent[]) this.mSensorEvents.toArray(session.sensorEvents);
        session.touchEvents = (TouchAnalyticsProto.Session.TouchEvent[]) this.mMotionEvents.toArray(session.touchEvents);
        session.phoneEvents = (TouchAnalyticsProto.Session.PhoneEvent[]) this.mPhoneEvents.toArray(session.phoneEvents);
        session.touchAreaWidth = this.mTouchAreaWidth;
        session.touchAreaHeight = this.mTouchAreaHeight;
        return session;
    }

    private TouchAnalyticsProto.Session.PhoneEvent phoneEventToProto(int i, long j) {
        TouchAnalyticsProto.Session.PhoneEvent phoneEvent = new TouchAnalyticsProto.Session.PhoneEvent();
        phoneEvent.type = i;
        phoneEvent.timeOffsetNanos = j - this.mStartSystemTimeNanos;
        return phoneEvent;
    }

    private TouchAnalyticsProto.Session.SensorEvent sensorEventToProto(SensorEvent sensorEvent, long j) {
        TouchAnalyticsProto.Session.SensorEvent sensorEvent2 = new TouchAnalyticsProto.Session.SensorEvent();
        sensorEvent2.type = sensorEvent.sensor.getType();
        sensorEvent2.timeOffsetNanos = j - this.mStartSystemTimeNanos;
        sensorEvent2.timestamp = sensorEvent.timestamp;
        sensorEvent2.values = (float[]) sensorEvent.values.clone();
        return sensorEvent2;
    }

    private TouchAnalyticsProto.Session.TouchEvent motionEventToProto(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        TouchAnalyticsProto.Session.TouchEvent touchEvent = new TouchAnalyticsProto.Session.TouchEvent();
        touchEvent.timeOffsetNanos = motionEvent.getEventTimeNano() - this.mStartSystemTimeNanos;
        touchEvent.action = motionEvent.getActionMasked();
        touchEvent.actionIndex = motionEvent.getActionIndex();
        touchEvent.pointers = new TouchAnalyticsProto.Session.TouchEvent.Pointer[pointerCount];
        for (int i = 0; i < pointerCount; i++) {
            TouchAnalyticsProto.Session.TouchEvent.Pointer pointer = new TouchAnalyticsProto.Session.TouchEvent.Pointer();
            pointer.f389x = motionEvent.getX(i);
            pointer.f390y = motionEvent.getY(i);
            pointer.size = motionEvent.getSize(i);
            pointer.pressure = motionEvent.getPressure(i);
            pointer.f388id = motionEvent.getPointerId(i);
            touchEvent.pointers[i] = pointer;
        }
        return touchEvent;
    }

    public void setTouchArea(int i, int i2) {
        this.mTouchAreaWidth = i;
        this.mTouchAreaHeight = i2;
    }

    public int getResult() {
        return this.mResult;
    }

    public long getStartTimestampMillis() {
        return this.mStartTimestampMillis;
    }
}
