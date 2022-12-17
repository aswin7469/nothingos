package com.nothing.settings.security;

public class SecurityUtils {
    private static int sGestureMode;

    public static void updateGestureMode(int i) {
        if (sGestureMode != i) {
            sGestureMode = i;
            writeGestureNode("/sys/bus/spi/devices/spi0.0/fts_gesture_mode", i);
        }
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void writeGestureNode(java.lang.String r4, int r5) {
        /*
            java.lang.String r0 = "Security"
            r1 = 0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0042 }
            r2.<init>()     // Catch:{ IOException -> 0x0042 }
            java.lang.String r3 = "writeGestureNode:"
            r2.append(r3)     // Catch:{ IOException -> 0x0042 }
            r2.append(r4)     // Catch:{ IOException -> 0x0042 }
            java.lang.String r3 = ", data:"
            r2.append(r3)     // Catch:{ IOException -> 0x0042 }
            r2.append(r5)     // Catch:{ IOException -> 0x0042 }
            java.lang.String r2 = r2.toString()     // Catch:{ IOException -> 0x0042 }
            android.util.Log.d(r0, r2)     // Catch:{ IOException -> 0x0042 }
            r2 = 0
            java.lang.String[] r3 = new java.lang.String[r2]     // Catch:{ IOException -> 0x0042 }
            java.nio.file.Path r3 = java.nio.file.Paths.get(r4, r3)     // Catch:{ IOException -> 0x0042 }
            java.nio.file.OpenOption[] r2 = new java.nio.file.OpenOption[r2]     // Catch:{ IOException -> 0x0042 }
            java.io.OutputStream r1 = java.nio.file.Files.newOutputStream(r3, r2)     // Catch:{ IOException -> 0x0042 }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ IOException -> 0x0042 }
            java.lang.String r2 = "US-ASCII"
            byte[] r5 = r5.getBytes(r2)     // Catch:{ IOException -> 0x0042 }
            r1.write(r5)     // Catch:{ IOException -> 0x0042 }
            r1.flush()     // Catch:{ IOException -> 0x0042 }
        L_0x003c:
            r1.close()     // Catch:{ IOException -> 0x0068 }
            goto L_0x0068
        L_0x0040:
            r4 = move-exception
            goto L_0x0069
        L_0x0042:
            r5 = move-exception
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0040 }
            r2.<init>()     // Catch:{ all -> 0x0040 }
            java.lang.String r3 = "Unable to write "
            r2.append(r3)     // Catch:{ all -> 0x0040 }
            r2.append(r4)     // Catch:{ all -> 0x0040 }
            java.lang.String r4 = r5.getMessage()     // Catch:{ all -> 0x0040 }
            r2.append(r4)     // Catch:{ all -> 0x0040 }
            java.lang.String r4 = r2.toString()     // Catch:{ all -> 0x0040 }
            android.util.Log.e(r0, r4)     // Catch:{ all -> 0x0040 }
            r5.printStackTrace()     // Catch:{ all -> 0x0040 }
            if (r1 != 0) goto L_0x003c
            if (r1 == 0) goto L_0x0068
            r1.close()     // Catch:{  }
        L_0x0068:
            return
        L_0x0069:
            if (r1 == 0) goto L_0x006e
            r1.close()     // Catch:{ IOException -> 0x006e }
        L_0x006e:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.settings.security.SecurityUtils.writeGestureNode(java.lang.String, int):void");
    }
}
