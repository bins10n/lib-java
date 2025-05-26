package com.github.robng.bins10n;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class Bins10n {
    private static final int        N_BYTES_1 = 1;
    private static final int        N_BYTES_2 = 2;
    private static final int        N_BYTES_4 = 4;
    private static final int        N_BYTES_8 = 8;

    public static final short       MAX_U8      = (short)   (Math.pow(2, 8)  - 1);
    public static final int         MAX_U16     = (int)     (Math.pow(2, 16) - 1);
    public static final long        MAX_U32     = (long)    (Math.pow(2, 32) - 1);
    public static final BigInteger  MAX_U64     = BigInteger.TWO.pow(64).subtract(BigInteger.ONE);

    public interface ReadSerial {
        void readSerial(InputStream s) throws IOException;
    }

    public static short readU8(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_1);
        return (short)(bytes[0] & MAX_U8);
    }

    public static int readU16(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_2);
        var buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort() & MAX_U16;
    }

    public static long readU32(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_4);
        var buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt() & MAX_U32;
    }

    public static BigInteger readU64(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_8);
        var buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return BigInteger.valueOf(buf.getLong()).and(MAX_U64);
    }

    public static byte readI8(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_1);
        return bytes[0];
    }

    public static short readI16(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_4);
        var buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return buf.getShort();
    }

    public static int readI32(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_4);
        var buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return buf.getInt();
    }

    public static long readI64(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_8);
        var buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return buf.getLong();
    }

    public static float readF32(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_4);
        var buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return buf.getFloat();
    }

    public static double readF64(InputStream s) throws IOException {
        var bytes = s.readNBytes(N_BYTES_8);
        var buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        return buf.getDouble();
    }

    public static String readStr(InputStream s) throws IOException {
        var len = readU16(s); // TODO: Use variable size integer
        var bytes = s.readNBytes(len);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public interface WriteSerial {
        int writeSerial(OutputStream s) throws Exception;
    }

    public static int writeU8(OutputStream s, short val) throws Exception {
        assertWithinBounds(val, (short)0, MAX_U8);
        s.write((byte)val);
        return N_BYTES_1;
    }

    public static int writeU16(OutputStream s, int val) throws Exception {
        assertWithinBounds(val, 0, MAX_U16);
        var buf = ByteBuffer.allocate(N_BYTES_2).order(ByteOrder.LITTLE_ENDIAN);
        buf.putShort((short)val);
        s.write(buf.array());
        return N_BYTES_2;
    }

    public static int writeU32(OutputStream s, long val) throws Exception {
        assertWithinBounds(val, 0L, MAX_U32);
        var buf = ByteBuffer.allocate(N_BYTES_4).order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt((int)val);
        s.write(buf.array());
        return N_BYTES_4;
    }

    public static int writeU64(OutputStream s, BigInteger val) throws Exception {
        assertWithinBounds(val, BigInteger.ZERO, MAX_U64);
        var buf = ByteBuffer.allocate(N_BYTES_8).order(ByteOrder.LITTLE_ENDIAN);
        buf.putLong(val.longValue());
        s.write(buf.array());
        return N_BYTES_8;
    }

    public static int writeI8(OutputStream s, byte val) throws IOException {
        s.write(val);
        return N_BYTES_1;
    }

    public static int writeI16(OutputStream s, short val) throws IOException {
        var buf = ByteBuffer.allocate(N_BYTES_2).order(ByteOrder.LITTLE_ENDIAN);
        buf.putShort(val);
        s.write(buf.array());
        return N_BYTES_2;
    }

    public static int writeI32(OutputStream s, int val) throws IOException {
        var buf = ByteBuffer.allocate(N_BYTES_4).order(ByteOrder.LITTLE_ENDIAN);
        buf.putInt(val);
        s.write(buf.array());
        return N_BYTES_4;
    }

    public static int writeI64(OutputStream s, long val) throws IOException {
        var buf = ByteBuffer.allocate(N_BYTES_8).order(ByteOrder.LITTLE_ENDIAN);
        buf.putLong(val);
        s.write(buf.array());
        return N_BYTES_8;
    }

    public static int writeF32(OutputStream s, float val) throws IOException {
        var buf = ByteBuffer.allocate(N_BYTES_4).order(ByteOrder.LITTLE_ENDIAN);
        buf.putFloat(val);
        s.write(buf.array());
        return N_BYTES_4;
    }

    public static int writeF64(OutputStream s, double val) throws IOException {
        var buf = ByteBuffer.allocate(N_BYTES_8).order(ByteOrder.LITTLE_ENDIAN);
        buf.putDouble(val);
        s.write(buf.array());
        return N_BYTES_8;
    }

    public static int writeStr(OutputStream s, String str) throws Exception {
        var written = 0;
        var bytes = str.getBytes(StandardCharsets.UTF_8);
        written += writeU16(s, bytes.length); // TODO: Use variable size integer
        s.write(bytes);
        written += bytes.length;
        return written;
    }

    public static <T extends WriteSerial> int writeOpt(OutputStream s, Optional<T> opt) throws Exception {
        var written = 0;
        if (opt.isPresent()) {
            written += writeU8(s, (short)0);
            written += opt.get().writeSerial(s);
        }
        return written;
    }

    private static <T extends Number & Comparable<T>> void assertWithinBounds(T val, T min, T max) throws Exception {
        if (val.compareTo(min) < 0 || val.compareTo(max) > 0)
            throw new Exception(); // TODO: Throw a proper exception with a message
    }
}
