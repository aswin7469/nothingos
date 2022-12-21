package java.util;

import java.p026io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import sun.util.locale.LanguageTag;

public final class UUID implements Serializable, Comparable<UUID> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = -4856846361193249489L;
    private final long leastSigBits;
    private final long mostSigBits;

    private static class Holder {
        static final SecureRandom numberGenerator = new SecureRandom();

        private Holder() {
        }
    }

    private UUID(byte[] bArr) {
        long j = 0;
        long j2 = 0;
        for (int i = 0; i < 8; i++) {
            j2 = (j2 << 8) | ((long) (bArr[i] & 255));
        }
        for (int i2 = 8; i2 < 16; i2++) {
            j = (j << 8) | ((long) (bArr[i2] & 255));
        }
        this.mostSigBits = j2;
        this.leastSigBits = j;
    }

    public UUID(long j, long j2) {
        this.mostSigBits = j;
        this.leastSigBits = j2;
    }

    public static UUID randomUUID() {
        byte[] bArr = new byte[16];
        Holder.numberGenerator.nextBytes(bArr);
        byte b = (byte) (bArr[6] & 15);
        bArr[6] = b;
        bArr[6] = (byte) (b | 64);
        byte b2 = (byte) (bArr[8] & 63);
        bArr[8] = b2;
        bArr[8] = (byte) (b2 | 128);
        return new UUID(bArr);
    }

    public static UUID nameUUIDFromBytes(byte[] bArr) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(bArr);
            byte b = (byte) (digest[6] & 15);
            digest[6] = b;
            digest[6] = (byte) (b | 48);
            byte b2 = (byte) (digest[8] & 63);
            digest[8] = b2;
            digest[8] = (byte) (b2 | 128);
            return new UUID(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new InternalError("MD5 not supported", e);
        }
    }

    public static UUID fromString(String str) {
        String[] split = str.split(LanguageTag.SEP);
        if (split.length == 5) {
            for (int i = 0; i < 5; i++) {
                split[i] = "0x" + split[i];
            }
            return new UUID((((Long.decode(split[0]).longValue() << 16) | Long.decode(split[1]).longValue()) << 16) | Long.decode(split[2]).longValue(), (Long.decode(split[3]).longValue() << 48) | Long.decode(split[4]).longValue());
        }
        throw new IllegalArgumentException("Invalid UUID string: " + str);
    }

    public long getLeastSignificantBits() {
        return this.leastSigBits;
    }

    public long getMostSignificantBits() {
        return this.mostSigBits;
    }

    public int version() {
        return (int) ((this.mostSigBits >> 12) & 15);
    }

    public int variant() {
        long j = this.leastSigBits;
        return (int) ((j >> 63) & (j >>> ((int) (64 - (j >>> 62)))));
    }

    public long timestamp() {
        if (version() == 1) {
            long j = this.mostSigBits;
            return (j >>> 32) | ((4095 & j) << 48) | (((j >> 16) & 65535) << 32);
        }
        throw new UnsupportedOperationException("Not a time-based UUID");
    }

    public int clockSequence() {
        if (version() == 1) {
            return (int) ((this.leastSigBits & 4611404543450677248L) >>> 48);
        }
        throw new UnsupportedOperationException("Not a time-based UUID");
    }

    public long node() {
        if (version() == 1) {
            return this.leastSigBits & 281474976710655L;
        }
        throw new UnsupportedOperationException("Not a time-based UUID");
    }

    public String toString() {
        return digits(this.mostSigBits >> 32, 8) + LanguageTag.SEP + digits(this.mostSigBits >> 16, 4) + LanguageTag.SEP + digits(this.mostSigBits, 4) + LanguageTag.SEP + digits(this.leastSigBits >> 48, 4) + LanguageTag.SEP + digits(this.leastSigBits, 12);
    }

    private static String digits(long j, int i) {
        long j2 = 1 << (i * 4);
        return Long.toHexString((j & (j2 - 1)) | j2).substring(1);
    }

    public int hashCode() {
        long j = this.mostSigBits ^ this.leastSigBits;
        return ((int) (j >> 32)) ^ ((int) j);
    }

    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != UUID.class) {
            return false;
        }
        UUID uuid = (UUID) obj;
        if (this.mostSigBits == uuid.mostSigBits && this.leastSigBits == uuid.leastSigBits) {
            return true;
        }
        return false;
    }

    public int compareTo(UUID uuid) {
        long j = this.mostSigBits;
        long j2 = uuid.mostSigBits;
        if (j < j2) {
            return -1;
        }
        if (j <= j2) {
            long j3 = this.leastSigBits;
            long j4 = uuid.leastSigBits;
            if (j3 < j4) {
                return -1;
            }
            if (j3 <= j4) {
                return 0;
            }
        }
        return 1;
    }
}
