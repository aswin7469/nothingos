package sun.nio.p035fs;

/* renamed from: sun.nio.fs.MimeTypesFileTypeDetector */
class MimeTypesFileTypeDetector extends AbstractFileTypeDetector {
    MimeTypesFileTypeDetector() {
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x0021  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String implProbeContentType(java.nio.file.Path r2) {
        /*
            r1 = this;
            java.nio.file.Path r1 = r2.getFileName()
            r2 = 0
            if (r1 != 0) goto L_0x0008
            return r2
        L_0x0008:
            java.lang.String r1 = r1.toString()
            java.lang.String r1 = getExtension(r1)
            boolean r0 = r1.isEmpty()
            if (r0 == 0) goto L_0x0017
            return r2
        L_0x0017:
            libcore.content.type.MimeMap r2 = libcore.content.type.MimeMap.getDefault()
            java.lang.String r2 = r2.guessMimeTypeFromExtension(r1)
            if (r2 != 0) goto L_0x0025
            java.lang.String r1 = getExtension(r1)
        L_0x0025:
            if (r2 != 0) goto L_0x002d
            boolean r0 = r1.isEmpty()
            if (r0 == 0) goto L_0x0017
        L_0x002d:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.MimeTypesFileTypeDetector.implProbeContentType(java.nio.file.Path):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0008, code lost:
        r0 = r2.indexOf(46);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String getExtension(java.lang.String r2) {
        /*
            if (r2 == 0) goto L_0x001f
            boolean r0 = r2.isEmpty()
            if (r0 != 0) goto L_0x001f
            r0 = 46
            int r0 = r2.indexOf((int) r0)
            if (r0 < 0) goto L_0x001f
            int r1 = r2.length()
            int r1 = r1 + -1
            if (r0 >= r1) goto L_0x001f
            int r0 = r0 + 1
            java.lang.String r2 = r2.substring(r0)
            goto L_0x0021
        L_0x001f:
            java.lang.String r2 = ""
        L_0x0021:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.nio.p035fs.MimeTypesFileTypeDetector.getExtension(java.lang.String):java.lang.String");
    }
}
