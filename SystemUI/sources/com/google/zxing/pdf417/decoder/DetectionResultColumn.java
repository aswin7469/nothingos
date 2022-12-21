package com.google.zxing.pdf417.decoder;

class DetectionResultColumn {
    private static final int MAX_NEARBY_DISTANCE = 5;
    private final BoundingBox boundingBox;
    private final Codeword[] codewords;

    DetectionResultColumn(BoundingBox boundingBox2) {
        this.boundingBox = new BoundingBox(boundingBox2);
        this.codewords = new Codeword[((boundingBox2.getMaxY() - boundingBox2.getMinY()) + 1)];
    }

    /* access modifiers changed from: package-private */
    public final Codeword getCodewordNearby(int i) {
        Codeword codeword;
        Codeword codeword2;
        Codeword codeword3 = getCodeword(i);
        if (codeword3 != null) {
            return codeword3;
        }
        for (int i2 = 1; i2 < 5; i2++) {
            int imageRowToCodewordIndex = imageRowToCodewordIndex(i) - i2;
            if (imageRowToCodewordIndex >= 0 && (codeword2 = this.codewords[imageRowToCodewordIndex]) != null) {
                return codeword2;
            }
            int imageRowToCodewordIndex2 = imageRowToCodewordIndex(i) + i2;
            Codeword[] codewordArr = this.codewords;
            if (imageRowToCodewordIndex2 < codewordArr.length && (codeword = codewordArr[imageRowToCodewordIndex2]) != null) {
                return codeword;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public final int imageRowToCodewordIndex(int i) {
        return i - this.boundingBox.getMinY();
    }

    /* access modifiers changed from: package-private */
    public final void setCodeword(int i, Codeword codeword) {
        this.codewords[imageRowToCodewordIndex(i)] = codeword;
    }

    /* access modifiers changed from: package-private */
    public final Codeword getCodeword(int i) {
        return this.codewords[imageRowToCodewordIndex(i)];
    }

    /* access modifiers changed from: package-private */
    public final BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    /* access modifiers changed from: package-private */
    public final Codeword[] getCodewords() {
        return this.codewords;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0056, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x005b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x005c, code lost:
        r10.addSuppressed(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005f, code lost:
        throw r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString() {
        /*
            r10 = this;
            java.util.Formatter r0 = new java.util.Formatter
            r0.<init>()
            com.google.zxing.pdf417.decoder.Codeword[] r10 = r10.codewords     // Catch:{ all -> 0x0054 }
            int r1 = r10.length     // Catch:{ all -> 0x0054 }
            r2 = 0
            r3 = r2
            r4 = r3
        L_0x000b:
            if (r3 >= r1) goto L_0x004c
            r5 = r10[r3]     // Catch:{ all -> 0x0054 }
            r6 = 1
            if (r5 != 0) goto L_0x0023
            java.lang.String r5 = "%3d:    |   %n"
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x0054 }
            int r7 = r4 + 1
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r4)     // Catch:{ all -> 0x0054 }
            r6[r2] = r4     // Catch:{ all -> 0x0054 }
            r0.format(r5, r6)     // Catch:{ all -> 0x0054 }
            r4 = r7
            goto L_0x0049
        L_0x0023:
            java.lang.String r7 = "%3d: %3d|%3d%n"
            r8 = 3
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ all -> 0x0054 }
            int r9 = r4 + 1
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r4)     // Catch:{ all -> 0x0054 }
            r8[r2] = r4     // Catch:{ all -> 0x0054 }
            int r4 = r5.getRowNumber()     // Catch:{ all -> 0x0054 }
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r4)     // Catch:{ all -> 0x0054 }
            r8[r6] = r4     // Catch:{ all -> 0x0054 }
            int r4 = r5.getValue()     // Catch:{ all -> 0x0054 }
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r4)     // Catch:{ all -> 0x0054 }
            r5 = 2
            r8[r5] = r4     // Catch:{ all -> 0x0054 }
            r0.format(r7, r8)     // Catch:{ all -> 0x0054 }
            r4 = r9
        L_0x0049:
            int r3 = r3 + 1
            goto L_0x000b
        L_0x004c:
            java.lang.String r10 = r0.toString()     // Catch:{ all -> 0x0054 }
            r0.close()
            return r10
        L_0x0054:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x0056 }
        L_0x0056:
            r1 = move-exception
            r0.close()     // Catch:{ all -> 0x005b }
            goto L_0x005f
        L_0x005b:
            r0 = move-exception
            r10.addSuppressed(r0)
        L_0x005f:
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.pdf417.decoder.DetectionResultColumn.toString():java.lang.String");
    }
}
