package java.util.zip;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.p026io.IOException;
import java.p026io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

public class ZipOutputStream extends DeflaterOutputStream implements ZipConstants {
    public static final int DEFLATED = 8;
    public static final int STORED = 0;
    private static final boolean inhibitZip64 = false;
    private boolean closed;
    private byte[] comment;
    private CRC32 crc;
    private XEntry current;
    private boolean finished;
    private long locoff;
    private int method;
    private HashSet<String> names;
    private long written;
    private Vector<XEntry> xentries;

    /* renamed from: zc */
    private final ZipCoder f812zc;

    private static class XEntry {
        final ZipEntry entry;
        final long offset;

        public XEntry(ZipEntry zipEntry, long j) {
            this.entry = zipEntry;
            this.offset = j;
        }
    }

    private static int version(ZipEntry zipEntry) throws ZipException {
        int i = zipEntry.method;
        if (i == 0) {
            return 10;
        }
        if (i == 8) {
            return 20;
        }
        throw new ZipException("unsupported compression method");
    }

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public ZipOutputStream(OutputStream outputStream) {
        this(outputStream, StandardCharsets.UTF_8);
    }

    public ZipOutputStream(OutputStream outputStream, Charset charset) {
        super(outputStream, new Deflater(-1, true));
        this.xentries = new Vector<>();
        this.names = new HashSet<>();
        this.crc = new CRC32();
        this.written = 0;
        this.locoff = 0;
        this.method = 8;
        this.closed = false;
        if (charset != null) {
            this.f812zc = ZipCoder.get(charset);
            this.usesDefaultDeflater = true;
            return;
        }
        throw new NullPointerException("charset is null");
    }

    public void setComment(String str) {
        if (str != null) {
            byte[] bytes = this.f812zc.getBytes(str);
            this.comment = bytes;
            if (bytes.length > 65535) {
                throw new IllegalArgumentException("ZIP file comment too long.");
            }
        }
    }

    public void setMethod(int i) {
        if (i == 8 || i == 0) {
            this.method = i;
            return;
        }
        throw new IllegalArgumentException("invalid compression method");
    }

    public void setLevel(int i) {
        this.def.setLevel(i);
    }

    public void putNextEntry(ZipEntry zipEntry) throws IOException {
        ensureOpen();
        if (this.current != null) {
            closeEntry();
        }
        if (zipEntry.xdostime == -1) {
            zipEntry.setTime(System.currentTimeMillis());
        }
        if (zipEntry.method == -1) {
            zipEntry.method = this.method;
        }
        zipEntry.flag = 0;
        int i = zipEntry.method;
        if (i == 0) {
            if (zipEntry.size == -1) {
                zipEntry.size = zipEntry.csize;
            } else if (zipEntry.csize == -1) {
                zipEntry.csize = zipEntry.size;
            } else if (zipEntry.size != zipEntry.csize) {
                throw new ZipException("STORED entry where compressed != uncompressed size");
            }
            if (zipEntry.size == -1 || zipEntry.crc == -1) {
                throw new ZipException("STORED entry missing size, compressed size, or crc-32");
            }
        } else if (i != 8) {
            throw new ZipException("unsupported compression method");
        } else if (zipEntry.size == -1 || zipEntry.csize == -1 || zipEntry.crc == -1) {
            zipEntry.flag = 8;
        }
        if (this.names.add(zipEntry.name)) {
            if (this.f812zc.isUTF8()) {
                zipEntry.flag |= 2048;
            }
            XEntry xEntry = new XEntry(zipEntry, this.written);
            this.current = xEntry;
            this.xentries.add(xEntry);
            writeLOC(this.current);
            return;
        }
        throw new ZipException("duplicate entry: " + zipEntry.name);
    }

    public void closeEntry() throws IOException {
        ensureOpen();
        XEntry xEntry = this.current;
        if (xEntry != null) {
            ZipEntry zipEntry = xEntry.entry;
            int i = zipEntry.method;
            if (i != 0) {
                if (i == 8) {
                    this.def.finish();
                    while (!this.def.finished()) {
                        deflate();
                    }
                    if ((zipEntry.flag & 8) != 0) {
                        zipEntry.size = this.def.getBytesRead();
                        zipEntry.csize = this.def.getBytesWritten();
                        zipEntry.crc = this.crc.getValue();
                        writeEXT(zipEntry);
                    } else if (zipEntry.size != this.def.getBytesRead()) {
                        throw new ZipException("invalid entry size (expected " + zipEntry.size + " but got " + this.def.getBytesRead() + " bytes)");
                    } else if (zipEntry.csize != this.def.getBytesWritten()) {
                        throw new ZipException("invalid entry compressed size (expected " + zipEntry.csize + " but got " + this.def.getBytesWritten() + " bytes)");
                    } else if (zipEntry.crc != this.crc.getValue()) {
                        throw new ZipException("invalid entry CRC-32 (expected 0x" + Long.toHexString(zipEntry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + NavigationBarInflaterView.KEY_CODE_END);
                    }
                    this.def.reset();
                    this.written += zipEntry.csize;
                } else {
                    throw new ZipException("invalid compression method");
                }
            } else if (zipEntry.size != this.written - this.locoff) {
                throw new ZipException("invalid entry size (expected " + zipEntry.size + " but got " + (this.written - this.locoff) + " bytes)");
            } else if (zipEntry.crc != this.crc.getValue()) {
                throw new ZipException("invalid entry crc-32 (expected 0x" + Long.toHexString(zipEntry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + NavigationBarInflaterView.KEY_CODE_END);
            }
            this.crc.reset();
            this.current = null;
        }
    }

    public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
        ensureOpen();
        if (i < 0 || i2 < 0 || i > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        } else if (i2 != 0) {
            XEntry xEntry = this.current;
            if (xEntry != null) {
                ZipEntry zipEntry = xEntry.entry;
                int i3 = zipEntry.method;
                if (i3 == 0) {
                    long j = this.written + ((long) i2);
                    this.written = j;
                    if (j - this.locoff <= zipEntry.size) {
                        this.out.write(bArr, i, i2);
                    } else {
                        throw new ZipException("attempt to write past end of STORED entry");
                    }
                } else if (i3 == 8) {
                    super.write(bArr, i, i2);
                } else {
                    throw new ZipException("invalid compression method");
                }
                this.crc.update(bArr, i, i2);
                return;
            }
            throw new ZipException("no current ZIP entry");
        }
    }

    public void finish() throws IOException {
        ensureOpen();
        if (!this.finished) {
            if (this.current != null) {
                closeEntry();
            }
            long j = this.written;
            Iterator<XEntry> it = this.xentries.iterator();
            while (it.hasNext()) {
                writeCEN(it.next());
            }
            writeEND(j, this.written - j);
            this.finished = true;
        }
    }

    public void close() throws IOException {
        if (!this.closed) {
            super.close();
            this.closed = true;
        }
    }

    private void writeLOC(XEntry xEntry) throws IOException {
        boolean z;
        int i;
        int i2;
        ZipEntry zipEntry = xEntry.entry;
        int i3 = zipEntry.flag;
        int extraLen = getExtraLen(zipEntry.extra);
        writeInt(ZipConstants.LOCSIG);
        if ((i3 & 8) == 8) {
            writeShort(version(zipEntry));
            writeShort(i3);
            writeShort(zipEntry.method);
            writeInt(zipEntry.xdostime);
            writeInt(0);
            writeInt(0);
            writeInt(0);
            z = false;
        } else {
            if (zipEntry.csize >= UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER || zipEntry.size >= UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
                writeShort(45);
                z = true;
            } else {
                writeShort(version(zipEntry));
                z = false;
            }
            writeShort(i3);
            writeShort(zipEntry.method);
            writeInt(zipEntry.xdostime);
            writeInt(zipEntry.crc);
            if (z) {
                writeInt(UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
                writeInt(UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER);
                extraLen += 20;
            } else {
                writeInt(zipEntry.csize);
                writeInt(zipEntry.size);
            }
        }
        byte[] bytes = this.f812zc.getBytes(zipEntry.name);
        writeShort(bytes.length);
        if (zipEntry.mtime != null) {
            i2 = 4;
            i = 1;
        } else {
            i2 = 0;
            i = 0;
        }
        if (zipEntry.atime != null) {
            i2 += 4;
            i |= 2;
        }
        if (zipEntry.ctime != null) {
            i2 += 4;
            i |= 4;
        }
        if (i != 0) {
            extraLen += i2 + 5;
        }
        writeShort(extraLen);
        writeBytes(bytes, 0, bytes.length);
        if (z) {
            writeShort(1);
            writeShort(16);
            writeLong(zipEntry.size);
            writeLong(zipEntry.csize);
        }
        if (i != 0) {
            writeShort(21589);
            writeShort(i2 + 1);
            writeByte(i);
            if (zipEntry.mtime != null) {
                writeInt(ZipUtils.fileTimeToUnixTime(zipEntry.mtime));
            }
            if (zipEntry.atime != null) {
                writeInt(ZipUtils.fileTimeToUnixTime(zipEntry.atime));
            }
            if (zipEntry.ctime != null) {
                writeInt(ZipUtils.fileTimeToUnixTime(zipEntry.ctime));
            }
        }
        writeExtra(zipEntry.extra);
        this.locoff = this.written;
    }

    private void writeEXT(ZipEntry zipEntry) throws IOException {
        writeInt(ZipConstants.EXTSIG);
        writeInt(zipEntry.crc);
        if (zipEntry.csize >= UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER || zipEntry.size >= UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
            writeLong(zipEntry.csize);
            writeLong(zipEntry.size);
            return;
        }
        writeInt(zipEntry.csize);
        writeInt(zipEntry.size);
    }

    private void writeCEN(XEntry xEntry) throws IOException {
        long j;
        long j2;
        boolean z;
        long j3;
        int i;
        boolean z2;
        long j4;
        long j5;
        int i2;
        byte[] bArr;
        XEntry xEntry2 = xEntry;
        ZipEntry zipEntry = xEntry2.entry;
        int i3 = zipEntry.flag;
        int version = version(zipEntry);
        long j6 = zipEntry.csize;
        long j7 = zipEntry.size;
        long j8 = xEntry2.offset;
        if (zipEntry.csize >= UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
            i = 8;
            j2 = j7;
            j = j8;
            j3 = 4294967295L;
            z = true;
        } else {
            j2 = j7;
            j = j8;
            z = false;
            j3 = j6;
            i = 0;
        }
        if (zipEntry.size >= UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
            i += 8;
            j4 = 4294967295L;
            z2 = true;
        } else {
            j4 = j2;
            z2 = z;
        }
        if (xEntry2.offset >= UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
            i += 8;
            j5 = 4294967295L;
            z2 = true;
        } else {
            j5 = j;
        }
        writeInt(ZipConstants.CENSIG);
        if (z2) {
            writeShort(45);
            writeShort(45);
        } else {
            writeShort(version);
            writeShort(version);
        }
        writeShort(i3);
        writeShort(zipEntry.method);
        writeInt(zipEntry.xdostime);
        writeInt(zipEntry.crc);
        writeInt(j3);
        writeInt(j4);
        byte[] bytes = this.f812zc.getBytes(zipEntry.name);
        writeShort(bytes.length);
        int extraLen = getExtraLen(zipEntry.extra);
        if (z2) {
            extraLen += i + 4;
        }
        if (zipEntry.mtime != null) {
            extraLen += 4;
            i2 = 1;
        } else {
            i2 = 0;
        }
        if (zipEntry.atime != null) {
            i2 |= 2;
        }
        if (zipEntry.ctime != null) {
            i2 |= 4;
        }
        if (i2 != 0) {
            extraLen += 5;
        }
        writeShort(extraLen);
        if (zipEntry.comment != null) {
            bArr = this.f812zc.getBytes(zipEntry.comment);
            writeShort(Math.min(bArr.length, 65535));
        } else {
            writeShort(0);
            bArr = null;
        }
        writeShort(0);
        writeShort(0);
        writeInt(0);
        writeInt(j5);
        writeBytes(bytes, 0, bytes.length);
        if (z2) {
            writeShort(1);
            writeShort(i);
            if (j4 == UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
                writeLong(zipEntry.size);
            }
            if (j3 == UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
                writeLong(zipEntry.csize);
            }
            if (j5 == UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
                writeLong(xEntry2.offset);
            }
        }
        if (i2 != 0) {
            writeShort(21589);
            if (zipEntry.mtime != null) {
                writeShort(5);
                writeByte(i2);
                writeInt(ZipUtils.fileTimeToUnixTime(zipEntry.mtime));
            } else {
                writeShort(1);
                writeByte(i2);
            }
        }
        writeExtra(zipEntry.extra);
        if (bArr != null) {
            writeBytes(bArr, 0, Math.min(bArr.length, 65535));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0027, code lost:
        r6 = r6 | true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writeEND(long r17, long r19) throws java.p026io.IOException {
        /*
            r16 = this;
            r0 = r16
            r1 = r19
            r3 = 4294967295(0xffffffff, double:2.1219957905E-314)
            int r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            r6 = 1
            r7 = 0
            if (r5 < 0) goto L_0x0012
            r8 = r3
            r5 = r6
            goto L_0x0014
        L_0x0012:
            r8 = r1
            r5 = r7
        L_0x0014:
            int r10 = (r17 > r3 ? 1 : (r17 == r3 ? 0 : -1))
            if (r10 < 0) goto L_0x0019
            goto L_0x001c
        L_0x0019:
            r3 = r17
            r6 = r5
        L_0x001c:
            java.util.Vector<java.util.zip.ZipOutputStream$XEntry> r5 = r0.xentries
            int r5 = r5.size()
            r10 = 65535(0xffff, float:9.1834E-41)
            if (r5 < r10) goto L_0x002c
            r6 = r6 | 1
            if (r6 == 0) goto L_0x002c
            r5 = r10
        L_0x002c:
            if (r6 == 0) goto L_0x0076
            long r10 = r0.written
            r12 = 101075792(0x6064b50, double:4.99380765E-316)
            r0.writeInt(r12)
            r12 = 44
            r0.writeLong(r12)
            r6 = 45
            r0.writeShort(r6)
            r0.writeShort(r6)
            r12 = 0
            r0.writeInt(r12)
            r0.writeInt(r12)
            java.util.Vector<java.util.zip.ZipOutputStream$XEntry> r6 = r0.xentries
            int r6 = r6.size()
            long r14 = (long) r6
            r0.writeLong(r14)
            java.util.Vector<java.util.zip.ZipOutputStream$XEntry> r6 = r0.xentries
            int r6 = r6.size()
            long r14 = (long) r6
            r0.writeLong(r14)
            r0.writeLong(r1)
            r16.writeLong(r17)
            r1 = 117853008(0x7064b50, double:5.82271225E-316)
            r0.writeInt(r1)
            r0.writeInt(r12)
            r0.writeLong(r10)
            r1 = 1
            r0.writeInt(r1)
        L_0x0076:
            r1 = 101010256(0x6054b50, double:4.99056974E-316)
            r0.writeInt(r1)
            r0.writeShort(r7)
            r0.writeShort(r7)
            r0.writeShort(r5)
            r0.writeShort(r5)
            r0.writeInt(r8)
            r0.writeInt(r3)
            byte[] r1 = r0.comment
            if (r1 == 0) goto L_0x009d
            int r1 = r1.length
            r0.writeShort(r1)
            byte[] r1 = r0.comment
            int r2 = r1.length
            r0.writeBytes(r1, r7, r2)
            goto L_0x00a0
        L_0x009d:
            r0.writeShort(r7)
        L_0x00a0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.zip.ZipOutputStream.writeEND(long, long):void");
    }

    private int getExtraLen(byte[] bArr) {
        int i = 0;
        if (bArr == null) {
            return 0;
        }
        int length = bArr.length;
        int i2 = 0;
        while (true) {
            int i3 = i + 4;
            if (i3 > length) {
                break;
            }
            int r3 = ZipUtils.get16(bArr, i);
            int r4 = ZipUtils.get16(bArr, i + 2);
            if (r4 < 0 || i3 + r4 > length) {
                break;
            }
            if (r3 == 21589 || r3 == 1) {
                i2 += r4 + 4;
            }
            i += r4 + 4;
        }
        return length - i2;
    }

    private void writeExtra(byte[] bArr) throws IOException {
        if (bArr != null) {
            int length = bArr.length;
            int i = 0;
            while (true) {
                int i2 = i + 4;
                if (i2 <= length) {
                    int r3 = ZipUtils.get16(bArr, i);
                    int r4 = ZipUtils.get16(bArr, i + 2);
                    if (r4 < 0 || i2 + r4 > length) {
                        writeBytes(bArr, i, length - i);
                    } else {
                        if (!(r3 == 21589 || r3 == 1)) {
                            writeBytes(bArr, i, r4 + 4);
                        }
                        i += r4 + 4;
                    }
                } else if (i < length) {
                    writeBytes(bArr, i, length - i);
                    return;
                } else {
                    return;
                }
            }
            writeBytes(bArr, i, length - i);
        }
    }

    private void writeByte(int i) throws IOException {
        this.out.write(i & 255);
        this.written++;
    }

    private void writeShort(int i) throws IOException {
        OutputStream outputStream = this.out;
        outputStream.write((i >>> 0) & 255);
        outputStream.write((i >>> 8) & 255);
        this.written += 2;
    }

    private void writeInt(long j) throws IOException {
        OutputStream outputStream = this.out;
        outputStream.write((int) ((j >>> 0) & 255));
        outputStream.write((int) ((j >>> 8) & 255));
        outputStream.write((int) ((j >>> 16) & 255));
        outputStream.write((int) ((j >>> 24) & 255));
        this.written += 4;
    }

    private void writeLong(long j) throws IOException {
        OutputStream outputStream = this.out;
        outputStream.write((int) ((j >>> 0) & 255));
        outputStream.write((int) ((j >>> 8) & 255));
        outputStream.write((int) ((j >>> 16) & 255));
        outputStream.write((int) ((j >>> 24) & 255));
        outputStream.write((int) ((j >>> 32) & 255));
        outputStream.write((int) ((j >>> 40) & 255));
        outputStream.write((int) ((j >>> 48) & 255));
        outputStream.write((int) ((j >>> 56) & 255));
        this.written += 8;
    }

    private void writeBytes(byte[] bArr, int i, int i2) throws IOException {
        this.out.write(bArr, i, i2);
        this.written += (long) i2;
    }
}
