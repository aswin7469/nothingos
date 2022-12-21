package java.util.zip;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.p026io.EOFException;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.PushbackInputStream;

public class ZipInputStream extends InflaterInputStream implements ZipConstants {
    private static final int DEFLATED = 8;
    private static final int STORED = 0;

    /* renamed from: b */
    private byte[] f810b;
    private boolean closed;
    private CRC32 crc;
    private ZipEntry entry;
    private boolean entryEOF;
    private int flag;
    private long remaining;
    private byte[] tmpbuf;

    /* renamed from: zc */
    private ZipCoder f811zc;

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public ZipInputStream(InputStream inputStream) {
        this(inputStream, StandardCharsets.UTF_8);
    }

    public ZipInputStream(InputStream inputStream, Charset charset) {
        super(new PushbackInputStream(inputStream, 512), new Inflater(true), 512);
        this.crc = new CRC32();
        this.tmpbuf = new byte[512];
        this.closed = false;
        this.entryEOF = false;
        this.f810b = new byte[256];
        if (inputStream == null) {
            throw new NullPointerException("in is null");
        } else if (charset != null) {
            this.f811zc = ZipCoder.get(charset);
        } else {
            throw new NullPointerException("charset is null");
        }
    }

    public ZipEntry getNextEntry() throws IOException {
        ensureOpen();
        if (this.entry != null) {
            closeEntry();
        }
        this.crc.reset();
        this.inf.reset();
        ZipEntry readLOC = readLOC();
        this.entry = readLOC;
        if (readLOC == null) {
            return null;
        }
        if (readLOC.method == 0 || this.entry.method == 8) {
            this.remaining = this.entry.size;
        }
        this.entryEOF = false;
        return this.entry;
    }

    public void closeEntry() throws IOException {
        byte[] bArr;
        ensureOpen();
        do {
            bArr = this.tmpbuf;
        } while (read(bArr, 0, bArr.length) != -1);
        this.entryEOF = true;
    }

    public int available() throws IOException {
        ensureOpen();
        if (!this.entryEOF) {
            return (this.entry == null || this.remaining != 0) ? 1 : 0;
        }
        return 0;
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        ensureOpen();
        if (i < 0 || i2 < 0 || i > bArr.length - i2) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            ZipEntry zipEntry = this.entry;
            if (zipEntry == null) {
                return -1;
            }
            int i3 = zipEntry.method;
            if (i3 == 0) {
                long j = this.remaining;
                if (j <= 0) {
                    this.entryEOF = true;
                    this.entry = null;
                    return -1;
                }
                if (((long) i2) > j) {
                    i2 = (int) j;
                }
                int read = this.f521in.read(bArr, i, i2);
                if (read != -1) {
                    this.crc.update(bArr, i, read);
                    long j2 = this.remaining - ((long) read);
                    this.remaining = j2;
                    if (j2 != 0 || this.entry.crc == this.crc.getValue()) {
                        return read;
                    }
                    throw new ZipException("invalid entry CRC (expected 0x" + Long.toHexString(this.entry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + NavigationBarInflaterView.KEY_CODE_END);
                }
                throw new ZipException("unexpected EOF");
            } else if (i3 == 8) {
                int read2 = super.read(bArr, i, i2);
                if (read2 == -1) {
                    readEnd(this.entry);
                    this.entryEOF = true;
                    this.entry = null;
                } else {
                    this.crc.update(bArr, i, read2);
                    this.remaining -= (long) read2;
                }
                return read2;
            } else {
                throw new ZipException("invalid compression method");
            }
        }
    }

    public long skip(long j) throws IOException {
        if (j >= 0) {
            ensureOpen();
            int min = (int) Math.min(j, 2147483647L);
            int i = 0;
            while (true) {
                if (i >= min) {
                    break;
                }
                int i2 = min - i;
                byte[] bArr = this.tmpbuf;
                if (i2 > bArr.length) {
                    i2 = bArr.length;
                }
                int read = read(bArr, 0, i2);
                if (read == -1) {
                    this.entryEOF = true;
                    break;
                }
                i += read;
            }
            return (long) i;
        }
        throw new IllegalArgumentException("negative skip length");
    }

    public void close() throws IOException {
        if (!this.closed) {
            super.close();
            this.closed = true;
        }
    }

    private ZipEntry readLOC() throws IOException {
        String str;
        try {
            boolean z = false;
            readFully(this.tmpbuf, 0, 30);
            if (ZipUtils.get32(this.tmpbuf, 0) != ZipConstants.LOCSIG) {
                return null;
            }
            this.flag = ZipUtils.get16(this.tmpbuf, 6);
            int r0 = ZipUtils.get16(this.tmpbuf, 26);
            int length = this.f810b.length;
            if (r0 > length) {
                do {
                    length *= 2;
                } while (r0 > length);
                this.f810b = new byte[length];
            }
            readFully(this.f810b, 0, r0);
            if ((this.flag & 2048) != 0) {
                str = this.f811zc.toStringUTF8(this.f810b, r0);
            } else {
                str = this.f811zc.toString(this.f810b, r0);
            }
            ZipEntry createZipEntry = createZipEntry(str);
            if ((this.flag & 1) != 1) {
                createZipEntry.method = ZipUtils.get16(this.tmpbuf, 8);
                createZipEntry.xdostime = ZipUtils.get32(this.tmpbuf, 10);
                if ((this.flag & 8) != 8) {
                    createZipEntry.crc = ZipUtils.get32(this.tmpbuf, 14);
                    createZipEntry.csize = ZipUtils.get32(this.tmpbuf, 18);
                    createZipEntry.size = ZipUtils.get32(this.tmpbuf, 22);
                }
                int r1 = ZipUtils.get16(this.tmpbuf, 28);
                if (r1 > 0) {
                    byte[] bArr = new byte[r1];
                    readFully(bArr, 0, r1);
                    if (createZipEntry.csize == UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER || createZipEntry.size == UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
                        z = true;
                    }
                    createZipEntry.setExtra0(bArr, z);
                }
                return createZipEntry;
            }
            throw new ZipException("encrypted ZIP entry not supported");
        } catch (EOFException unused) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public ZipEntry createZipEntry(String str) {
        return new ZipEntry(str);
    }

    private void readEnd(ZipEntry zipEntry) throws IOException {
        int remaining2 = this.inf.getRemaining();
        if (remaining2 > 0) {
            ((PushbackInputStream) this.f521in).unread(this.buf, this.len - remaining2, remaining2);
        }
        if ((this.flag & 8) == 8) {
            if (this.inf.getBytesWritten() > UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER || this.inf.getBytesRead() > UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER) {
                readFully(this.tmpbuf, 0, 24);
                long r4 = ZipUtils.get32(this.tmpbuf, 0);
                if (r4 != ZipConstants.EXTSIG) {
                    zipEntry.crc = r4;
                    zipEntry.csize = ZipUtils.get64(this.tmpbuf, 4);
                    zipEntry.size = ZipUtils.get64(this.tmpbuf, 12);
                    ((PushbackInputStream) this.f521in).unread(this.tmpbuf, 20, 4);
                } else {
                    zipEntry.crc = ZipUtils.get32(this.tmpbuf, 4);
                    zipEntry.csize = ZipUtils.get64(this.tmpbuf, 8);
                    zipEntry.size = ZipUtils.get64(this.tmpbuf, 16);
                }
            } else {
                readFully(this.tmpbuf, 0, 16);
                long r42 = ZipUtils.get32(this.tmpbuf, 0);
                if (r42 != ZipConstants.EXTSIG) {
                    zipEntry.crc = r42;
                    zipEntry.csize = ZipUtils.get32(this.tmpbuf, 4);
                    zipEntry.size = ZipUtils.get32(this.tmpbuf, 8);
                    ((PushbackInputStream) this.f521in).unread(this.tmpbuf, 12, 4);
                } else {
                    zipEntry.crc = ZipUtils.get32(this.tmpbuf, 4);
                    zipEntry.csize = ZipUtils.get32(this.tmpbuf, 8);
                    zipEntry.size = ZipUtils.get32(this.tmpbuf, 12);
                }
            }
        }
        if (zipEntry.size != this.inf.getBytesWritten()) {
            throw new ZipException("invalid entry size (expected " + zipEntry.size + " but got " + this.inf.getBytesWritten() + " bytes)");
        } else if (zipEntry.csize != this.inf.getBytesRead()) {
            throw new ZipException("invalid entry compressed size (expected " + zipEntry.csize + " but got " + this.inf.getBytesRead() + " bytes)");
        } else if (zipEntry.crc != this.crc.getValue()) {
            throw new ZipException("invalid entry CRC (expected 0x" + Long.toHexString(zipEntry.crc) + " but got 0x" + Long.toHexString(this.crc.getValue()) + NavigationBarInflaterView.KEY_CODE_END);
        }
    }

    private void readFully(byte[] bArr, int i, int i2) throws IOException {
        while (i2 > 0) {
            int read = this.f521in.read(bArr, i, i2);
            if (read != -1) {
                i += read;
                i2 -= read;
            } else {
                throw new EOFException();
            }
        }
    }
}
