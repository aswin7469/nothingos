package sun.net;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class ProgressMonitor {
    private static ProgressMeteringPolicy meteringPolicy = new DefaultProgressMeteringPolicy();

    /* renamed from: pm */
    private static ProgressMonitor f861pm = new ProgressMonitor();
    private ArrayList<ProgressListener> progressListenerList = new ArrayList<>();
    private ArrayList<ProgressSource> progressSourceList = new ArrayList<>();

    public static synchronized ProgressMonitor getDefault() {
        ProgressMonitor progressMonitor;
        synchronized (ProgressMonitor.class) {
            progressMonitor = f861pm;
        }
        return progressMonitor;
    }

    public static synchronized void setDefault(ProgressMonitor progressMonitor) {
        synchronized (ProgressMonitor.class) {
            if (progressMonitor != null) {
                f861pm = progressMonitor;
            }
        }
    }

    public static synchronized void setMeteringPolicy(ProgressMeteringPolicy progressMeteringPolicy) {
        synchronized (ProgressMonitor.class) {
            if (progressMeteringPolicy != null) {
                meteringPolicy = progressMeteringPolicy;
            }
        }
    }

    public ArrayList<ProgressSource> getProgressSources() {
        ArrayList<ProgressSource> arrayList = new ArrayList<>();
        try {
            synchronized (this.progressSourceList) {
                Iterator<ProgressSource> it = this.progressSourceList.iterator();
                while (it.hasNext()) {
                    arrayList.add((ProgressSource) it.next().clone());
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public synchronized int getProgressUpdateThreshold() {
        return meteringPolicy.getProgressUpdateThreshold();
    }

    public boolean shouldMeterInput(URL url, String str) {
        return meteringPolicy.shouldMeterInput(url, str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
        if (r12.progressListenerList.size() <= 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        r0 = new java.util.ArrayList();
        r1 = r12.progressListenerList;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0022, code lost:
        monitor-enter(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r12 = r12.progressListenerList.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002d, code lost:
        if (r12.hasNext() == false) goto L_0x0039;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002f, code lost:
        r0.add(r12.next());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0039, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003a, code lost:
        r12 = r0.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0042, code lost:
        if (r12.hasNext() == false) goto L_0x0070;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0044, code lost:
        ((sun.net.ProgressListener) r12.next()).progressStart(new sun.net.ProgressEvent(r13, r13.getURL(), r13.getMethod(), r13.getContentType(), r13.getState(), r13.getProgress(), r13.getExpected()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void registerSource(sun.net.ProgressSource r13) {
        /*
            r12 = this;
            java.util.ArrayList<sun.net.ProgressSource> r0 = r12.progressSourceList
            monitor-enter(r0)
            java.util.ArrayList<sun.net.ProgressSource> r1 = r12.progressSourceList     // Catch:{ all -> 0x0071 }
            boolean r1 = r1.contains(r13)     // Catch:{ all -> 0x0071 }
            if (r1 == 0) goto L_0x000d
            monitor-exit(r0)     // Catch:{ all -> 0x0071 }
            return
        L_0x000d:
            java.util.ArrayList<sun.net.ProgressSource> r1 = r12.progressSourceList     // Catch:{ all -> 0x0071 }
            r1.add(r13)     // Catch:{ all -> 0x0071 }
            monitor-exit(r0)     // Catch:{ all -> 0x0071 }
            java.util.ArrayList<sun.net.ProgressListener> r0 = r12.progressListenerList
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0070
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.ArrayList<sun.net.ProgressListener> r1 = r12.progressListenerList
            monitor-enter(r1)
            java.util.ArrayList<sun.net.ProgressListener> r12 = r12.progressListenerList     // Catch:{ all -> 0x006d }
            java.util.Iterator r12 = r12.iterator()     // Catch:{ all -> 0x006d }
        L_0x0029:
            boolean r2 = r12.hasNext()     // Catch:{ all -> 0x006d }
            if (r2 == 0) goto L_0x0039
            java.lang.Object r2 = r12.next()     // Catch:{ all -> 0x006d }
            sun.net.ProgressListener r2 = (sun.net.ProgressListener) r2     // Catch:{ all -> 0x006d }
            r0.add(r2)     // Catch:{ all -> 0x006d }
            goto L_0x0029
        L_0x0039:
            monitor-exit(r1)     // Catch:{ all -> 0x006d }
            java.util.Iterator r12 = r0.iterator()
        L_0x003e:
            boolean r0 = r12.hasNext()
            if (r0 == 0) goto L_0x0070
            java.lang.Object r0 = r12.next()
            sun.net.ProgressListener r0 = (sun.net.ProgressListener) r0
            sun.net.ProgressEvent r11 = new sun.net.ProgressEvent
            java.net.URL r3 = r13.getURL()
            java.lang.String r4 = r13.getMethod()
            java.lang.String r5 = r13.getContentType()
            sun.net.ProgressSource$State r6 = r13.getState()
            long r7 = r13.getProgress()
            long r9 = r13.getExpected()
            r1 = r11
            r2 = r13
            r1.<init>(r2, r3, r4, r5, r6, r7, r9)
            r0.progressStart(r11)
            goto L_0x003e
        L_0x006d:
            r12 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x006d }
            throw r12
        L_0x0070:
            return
        L_0x0071:
            r12 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0071 }
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.ProgressMonitor.registerSource(sun.net.ProgressSource):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
        if (r12.progressListenerList.size() <= 0) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001e, code lost:
        r0 = new java.util.ArrayList();
        r1 = r12.progressListenerList;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0025, code lost:
        monitor-enter(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        r12 = r12.progressListenerList.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0030, code lost:
        if (r12.hasNext() == false) goto L_0x003c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0032, code lost:
        r0.add(r12.next());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x003c, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x003d, code lost:
        r12 = r0.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0045, code lost:
        if (r12.hasNext() == false) goto L_0x0073;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0047, code lost:
        ((sun.net.ProgressListener) r12.next()).progressFinish(new sun.net.ProgressEvent(r13, r13.getURL(), r13.getMethod(), r13.getContentType(), r13.getState(), r13.getProgress(), r13.getExpected()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void unregisterSource(sun.net.ProgressSource r13) {
        /*
            r12 = this;
            java.util.ArrayList<sun.net.ProgressSource> r0 = r12.progressSourceList
            monitor-enter(r0)
            java.util.ArrayList<sun.net.ProgressSource> r1 = r12.progressSourceList     // Catch:{ all -> 0x0074 }
            boolean r1 = r1.contains(r13)     // Catch:{ all -> 0x0074 }
            if (r1 != 0) goto L_0x000d
            monitor-exit(r0)     // Catch:{ all -> 0x0074 }
            return
        L_0x000d:
            r13.close()     // Catch:{ all -> 0x0074 }
            java.util.ArrayList<sun.net.ProgressSource> r1 = r12.progressSourceList     // Catch:{ all -> 0x0074 }
            r1.remove((java.lang.Object) r13)     // Catch:{ all -> 0x0074 }
            monitor-exit(r0)     // Catch:{ all -> 0x0074 }
            java.util.ArrayList<sun.net.ProgressListener> r0 = r12.progressListenerList
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x0073
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.ArrayList<sun.net.ProgressListener> r1 = r12.progressListenerList
            monitor-enter(r1)
            java.util.ArrayList<sun.net.ProgressListener> r12 = r12.progressListenerList     // Catch:{ all -> 0x0070 }
            java.util.Iterator r12 = r12.iterator()     // Catch:{ all -> 0x0070 }
        L_0x002c:
            boolean r2 = r12.hasNext()     // Catch:{ all -> 0x0070 }
            if (r2 == 0) goto L_0x003c
            java.lang.Object r2 = r12.next()     // Catch:{ all -> 0x0070 }
            sun.net.ProgressListener r2 = (sun.net.ProgressListener) r2     // Catch:{ all -> 0x0070 }
            r0.add(r2)     // Catch:{ all -> 0x0070 }
            goto L_0x002c
        L_0x003c:
            monitor-exit(r1)     // Catch:{ all -> 0x0070 }
            java.util.Iterator r12 = r0.iterator()
        L_0x0041:
            boolean r0 = r12.hasNext()
            if (r0 == 0) goto L_0x0073
            java.lang.Object r0 = r12.next()
            sun.net.ProgressListener r0 = (sun.net.ProgressListener) r0
            sun.net.ProgressEvent r11 = new sun.net.ProgressEvent
            java.net.URL r3 = r13.getURL()
            java.lang.String r4 = r13.getMethod()
            java.lang.String r5 = r13.getContentType()
            sun.net.ProgressSource$State r6 = r13.getState()
            long r7 = r13.getProgress()
            long r9 = r13.getExpected()
            r1 = r11
            r2 = r13
            r1.<init>(r2, r3, r4, r5, r6, r7, r9)
            r0.progressFinish(r11)
            goto L_0x0041
        L_0x0070:
            r12 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0070 }
            throw r12
        L_0x0073:
            return
        L_0x0074:
            r12 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0074 }
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.ProgressMonitor.unregisterSource(sun.net.ProgressSource):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0016, code lost:
        r0 = new java.util.ArrayList();
        r1 = r12.progressListenerList;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001d, code lost:
        monitor-enter(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r12 = r12.progressListenerList.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
        if (r12.hasNext() == false) goto L_0x0034;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002a, code lost:
        r0.add(r12.next());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0034, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0035, code lost:
        r12 = r0.iterator();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003d, code lost:
        if (r12.hasNext() == false) goto L_0x006b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x003f, code lost:
        ((sun.net.ProgressListener) r12.next()).progressUpdate(new sun.net.ProgressEvent(r13, r13.getURL(), r13.getMethod(), r13.getContentType(), r13.getState(), r13.getProgress(), r13.getExpected()));
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
        if (r12.progressListenerList.size() <= 0) goto L_?;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateProgress(sun.net.ProgressSource r13) {
        /*
            r12 = this;
            java.util.ArrayList<sun.net.ProgressSource> r0 = r12.progressSourceList
            monitor-enter(r0)
            java.util.ArrayList<sun.net.ProgressSource> r1 = r12.progressSourceList     // Catch:{ all -> 0x006c }
            boolean r1 = r1.contains(r13)     // Catch:{ all -> 0x006c }
            if (r1 != 0) goto L_0x000d
            monitor-exit(r0)     // Catch:{ all -> 0x006c }
            return
        L_0x000d:
            monitor-exit(r0)     // Catch:{ all -> 0x006c }
            java.util.ArrayList<sun.net.ProgressListener> r0 = r12.progressListenerList
            int r0 = r0.size()
            if (r0 <= 0) goto L_0x006b
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.ArrayList<sun.net.ProgressListener> r1 = r12.progressListenerList
            monitor-enter(r1)
            java.util.ArrayList<sun.net.ProgressListener> r12 = r12.progressListenerList     // Catch:{ all -> 0x0068 }
            java.util.Iterator r12 = r12.iterator()     // Catch:{ all -> 0x0068 }
        L_0x0024:
            boolean r2 = r12.hasNext()     // Catch:{ all -> 0x0068 }
            if (r2 == 0) goto L_0x0034
            java.lang.Object r2 = r12.next()     // Catch:{ all -> 0x0068 }
            sun.net.ProgressListener r2 = (sun.net.ProgressListener) r2     // Catch:{ all -> 0x0068 }
            r0.add(r2)     // Catch:{ all -> 0x0068 }
            goto L_0x0024
        L_0x0034:
            monitor-exit(r1)     // Catch:{ all -> 0x0068 }
            java.util.Iterator r12 = r0.iterator()
        L_0x0039:
            boolean r0 = r12.hasNext()
            if (r0 == 0) goto L_0x006b
            java.lang.Object r0 = r12.next()
            sun.net.ProgressListener r0 = (sun.net.ProgressListener) r0
            sun.net.ProgressEvent r11 = new sun.net.ProgressEvent
            java.net.URL r3 = r13.getURL()
            java.lang.String r4 = r13.getMethod()
            java.lang.String r5 = r13.getContentType()
            sun.net.ProgressSource$State r6 = r13.getState()
            long r7 = r13.getProgress()
            long r9 = r13.getExpected()
            r1 = r11
            r2 = r13
            r1.<init>(r2, r3, r4, r5, r6, r7, r9)
            r0.progressUpdate(r11)
            goto L_0x0039
        L_0x0068:
            r12 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0068 }
            throw r12
        L_0x006b:
            return
        L_0x006c:
            r12 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x006c }
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.net.ProgressMonitor.updateProgress(sun.net.ProgressSource):void");
    }

    public void addProgressListener(ProgressListener progressListener) {
        synchronized (this.progressListenerList) {
            this.progressListenerList.add(progressListener);
        }
    }

    public void removeProgressListener(ProgressListener progressListener) {
        synchronized (this.progressListenerList) {
            this.progressListenerList.remove((Object) progressListener);
        }
    }
}
