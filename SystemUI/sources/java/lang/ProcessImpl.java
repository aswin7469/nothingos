package java.lang;

final class ProcessImpl {
    static final /* synthetic */ boolean $assertionsDisabled = false;

    private ProcessImpl() {
    }

    private static byte[] toCString(String str) {
        if (str == null) {
            return null;
        }
        byte[] bytes = str.getBytes();
        int length = bytes.length + 1;
        byte[] bArr = new byte[length];
        System.arraycopy((Object) bytes, 0, (Object) bArr, 0, bytes.length);
        bArr[length - 1] = 0;
        return bArr;
    }

    /* JADX WARNING: Removed duplicated region for block: B:107:0x0154  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0078 A[Catch:{ all -> 0x0120 }] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x007b A[Catch:{ all -> 0x0120 }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00a6 A[Catch:{ all -> 0x011d }] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a9 A[Catch:{ all -> 0x011d }] */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x012a A[SYNTHETIC, Splitter:B:83:0x012a] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0146 A[SYNTHETIC, Splitter:B:99:0x0146] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.Process start(java.lang.String[] r15, java.util.Map<java.lang.String, java.lang.String> r16, java.lang.String r17, java.lang.ProcessBuilder.Redirect[] r18, boolean r19) throws java.p026io.IOException {
        /*
            r0 = r15
            int r1 = r0.length
            r2 = 1
            int r6 = r1 + -1
            byte[][] r1 = new byte[r6][]
            r3 = 0
            r4 = r3
            r5 = r6
        L_0x000a:
            if (r4 >= r6) goto L_0x001a
            int r7 = r4 + 1
            r8 = r0[r7]
            byte[] r8 = r8.getBytes()
            r1[r4] = r8
            int r4 = r8.length
            int r5 = r5 + r4
            r4 = r7
            goto L_0x000a
        L_0x001a:
            byte[] r5 = new byte[r5]
            r4 = r3
            r7 = r4
        L_0x001e:
            if (r4 >= r6) goto L_0x002c
            r8 = r1[r4]
            int r9 = r8.length
            java.lang.System.arraycopy((java.lang.Object) r8, (int) r3, (java.lang.Object) r5, (int) r7, (int) r9)
            int r8 = r8.length
            int r8 = r8 + r2
            int r7 = r7 + r8
            int r4 = r4 + 1
            goto L_0x001e
        L_0x002c:
            int[] r1 = new int[r2]
            r4 = r16
            byte[] r7 = java.lang.ProcessEnvironment.toEnvironmentBlock(r4, r1)
            r4 = 3
            r8 = 0
            r9 = -1
            r10 = 2
            if (r18 != 0) goto L_0x0048
            int[] r4 = new int[r4]     // Catch:{ all -> 0x0125 }
            r4[r3] = r9     // Catch:{ all -> 0x0125 }
            r4[r2] = r9     // Catch:{ all -> 0x0125 }
            r4[r10] = r9     // Catch:{ all -> 0x0125 }
            r10 = r4
            r2 = r8
            r12 = r2
            r13 = r12
            goto L_0x00d3
        L_0x0048:
            int[] r4 = new int[r4]     // Catch:{ all -> 0x0125 }
            r11 = r18[r3]     // Catch:{ all -> 0x0125 }
            java.lang.ProcessBuilder$Redirect r12 = java.lang.ProcessBuilder.Redirect.PIPE     // Catch:{ all -> 0x0125 }
            if (r11 != r12) goto L_0x0053
            r4[r3] = r9     // Catch:{ all -> 0x0125 }
            goto L_0x005b
        L_0x0053:
            r11 = r18[r3]     // Catch:{ all -> 0x0125 }
            java.lang.ProcessBuilder$Redirect r12 = java.lang.ProcessBuilder.Redirect.INHERIT     // Catch:{ all -> 0x0125 }
            if (r11 != r12) goto L_0x005d
            r4[r3] = r3     // Catch:{ all -> 0x0125 }
        L_0x005b:
            r11 = r8
            goto L_0x0072
        L_0x005d:
            java.io.FileInputStream r11 = new java.io.FileInputStream     // Catch:{ all -> 0x0125 }
            r12 = r18[r3]     // Catch:{ all -> 0x0125 }
            java.io.File r12 = r12.file()     // Catch:{ all -> 0x0125 }
            r11.<init>((java.p026io.File) r12)     // Catch:{ all -> 0x0125 }
            java.io.FileDescriptor r12 = r11.getFD()     // Catch:{ all -> 0x0120 }
            int r12 = r12.getInt$()     // Catch:{ all -> 0x0120 }
            r4[r3] = r12     // Catch:{ all -> 0x0120 }
        L_0x0072:
            r12 = r18[r2]     // Catch:{ all -> 0x0120 }
            java.lang.ProcessBuilder$Redirect r13 = java.lang.ProcessBuilder.Redirect.PIPE     // Catch:{ all -> 0x0120 }
            if (r12 != r13) goto L_0x007b
            r4[r2] = r9     // Catch:{ all -> 0x0120 }
            goto L_0x0083
        L_0x007b:
            r12 = r18[r2]     // Catch:{ all -> 0x0120 }
            java.lang.ProcessBuilder$Redirect r13 = java.lang.ProcessBuilder.Redirect.INHERIT     // Catch:{ all -> 0x0120 }
            if (r12 != r13) goto L_0x0085
            r4[r2] = r2     // Catch:{ all -> 0x0120 }
        L_0x0083:
            r12 = r8
            goto L_0x00a0
        L_0x0085:
            java.io.FileOutputStream r12 = new java.io.FileOutputStream     // Catch:{ all -> 0x0120 }
            r13 = r18[r2]     // Catch:{ all -> 0x0120 }
            java.io.File r13 = r13.file()     // Catch:{ all -> 0x0120 }
            r14 = r18[r2]     // Catch:{ all -> 0x0120 }
            boolean r14 = r14.append()     // Catch:{ all -> 0x0120 }
            r12.<init>((java.p026io.File) r13, (boolean) r14)     // Catch:{ all -> 0x0120 }
            java.io.FileDescriptor r13 = r12.getFD()     // Catch:{ all -> 0x011d }
            int r13 = r13.getInt$()     // Catch:{ all -> 0x011d }
            r4[r2] = r13     // Catch:{ all -> 0x011d }
        L_0x00a0:
            r2 = r18[r10]     // Catch:{ all -> 0x011d }
            java.lang.ProcessBuilder$Redirect r13 = java.lang.ProcessBuilder.Redirect.PIPE     // Catch:{ all -> 0x011d }
            if (r2 != r13) goto L_0x00a9
            r4[r10] = r9     // Catch:{ all -> 0x011d }
            goto L_0x00b1
        L_0x00a9:
            r2 = r18[r10]     // Catch:{ all -> 0x011d }
            java.lang.ProcessBuilder$Redirect r9 = java.lang.ProcessBuilder.Redirect.INHERIT     // Catch:{ all -> 0x011d }
            if (r2 != r9) goto L_0x00b5
            r4[r10] = r10     // Catch:{ all -> 0x011d }
        L_0x00b1:
            r10 = r4
            r13 = r8
        L_0x00b3:
            r2 = r11
            goto L_0x00d3
        L_0x00b5:
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch:{ all -> 0x011d }
            r9 = r18[r10]     // Catch:{ all -> 0x011d }
            java.io.File r9 = r9.file()     // Catch:{ all -> 0x011d }
            r13 = r18[r10]     // Catch:{ all -> 0x011d }
            boolean r13 = r13.append()     // Catch:{ all -> 0x011d }
            r2.<init>((java.p026io.File) r9, (boolean) r13)     // Catch:{ all -> 0x011d }
            java.io.FileDescriptor r8 = r2.getFD()     // Catch:{ all -> 0x011a }
            int r8 = r8.getInt$()     // Catch:{ all -> 0x011a }
            r4[r10] = r8     // Catch:{ all -> 0x011a }
            r13 = r2
            r10 = r4
            goto L_0x00b3
        L_0x00d3:
            java.lang.UNIXProcess r14 = new java.lang.UNIXProcess     // Catch:{ all -> 0x0117 }
            r0 = r0[r3]     // Catch:{ all -> 0x0117 }
            byte[] r4 = toCString(r0)     // Catch:{ all -> 0x0117 }
            r8 = r1[r3]     // Catch:{ all -> 0x0117 }
            byte[] r9 = toCString(r17)     // Catch:{ all -> 0x0117 }
            r3 = r14
            r11 = r19
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x0117 }
            if (r2 == 0) goto L_0x0103
            r2.close()     // Catch:{ all -> 0x00ed }
            goto L_0x0103
        L_0x00ed:
            r0 = move-exception
            r1 = r0
            if (r12 == 0) goto L_0x00fd
            r12.close()     // Catch:{ all -> 0x00f5 }
            goto L_0x00fd
        L_0x00f5:
            r0 = move-exception
            r1 = r0
            if (r13 == 0) goto L_0x00fc
            r13.close()
        L_0x00fc:
            throw r1
        L_0x00fd:
            if (r13 == 0) goto L_0x0102
            r13.close()
        L_0x0102:
            throw r1
        L_0x0103:
            if (r12 == 0) goto L_0x0111
            r12.close()     // Catch:{ all -> 0x0109 }
            goto L_0x0111
        L_0x0109:
            r0 = move-exception
            r1 = r0
            if (r13 == 0) goto L_0x0110
            r13.close()
        L_0x0110:
            throw r1
        L_0x0111:
            if (r13 == 0) goto L_0x0116
            r13.close()
        L_0x0116:
            return r14
        L_0x0117:
            r0 = move-exception
            r8 = r2
            goto L_0x0128
        L_0x011a:
            r0 = move-exception
            r13 = r2
            goto L_0x0123
        L_0x011d:
            r0 = move-exception
            r13 = r8
            goto L_0x0123
        L_0x0120:
            r0 = move-exception
            r12 = r8
            r13 = r12
        L_0x0123:
            r8 = r11
            goto L_0x0128
        L_0x0125:
            r0 = move-exception
            r12 = r8
            r13 = r12
        L_0x0128:
            if (r8 == 0) goto L_0x0144
            r8.close()     // Catch:{ all -> 0x012e }
            goto L_0x0144
        L_0x012e:
            r0 = move-exception
            r1 = r0
            if (r12 == 0) goto L_0x013e
            r12.close()     // Catch:{ all -> 0x0136 }
            goto L_0x013e
        L_0x0136:
            r0 = move-exception
            r1 = r0
            if (r13 == 0) goto L_0x013d
            r13.close()
        L_0x013d:
            throw r1
        L_0x013e:
            if (r13 == 0) goto L_0x0143
            r13.close()
        L_0x0143:
            throw r1
        L_0x0144:
            if (r12 == 0) goto L_0x0152
            r12.close()     // Catch:{ all -> 0x014a }
            goto L_0x0152
        L_0x014a:
            r0 = move-exception
            r1 = r0
            if (r13 == 0) goto L_0x0151
            r13.close()
        L_0x0151:
            throw r1
        L_0x0152:
            if (r13 == 0) goto L_0x0157
            r13.close()
        L_0x0157:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.ProcessImpl.start(java.lang.String[], java.util.Map, java.lang.String, java.lang.ProcessBuilder$Redirect[], boolean):java.lang.Process");
    }
}
