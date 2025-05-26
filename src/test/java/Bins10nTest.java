import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigInteger;

import static com.github.robng.bins10n.Bins10n.*;

public class Bins10nTest {

    @Test
    void consistentReadWriteU8() throws Exception {
        final short VALUE = 101;

        var o = new ByteArrayOutputStream();
        writeU8(o, VALUE);

        var i = new ByteArrayInputStream(o.toByteArray());
        assertEquals(VALUE, readU8(i));
    }

    @Test
    void consistentReadWriteU16() throws Exception {
        final int VALUE = 36364;

        var o = new ByteArrayOutputStream();
        writeU16(o, VALUE);

        var i = new ByteArrayInputStream(o.toByteArray());
        assertEquals(VALUE, readU16(i));
    }

    @Test
    void consistentReadWriteU32() throws Exception {
        final long VALUE = 2552447309L;

        var o = new ByteArrayOutputStream();
        writeU32(o, VALUE);

        var i = new ByteArrayInputStream(o.toByteArray());
        assertEquals(VALUE, readU32(i));
    }

    @Test
    void consistentReadWriteU64() throws Exception {
        final BigInteger VALUE = new BigInteger("13481027116271895000");

        var o = new ByteArrayOutputStream();
        writeU64(o, VALUE);

        var i = new ByteArrayInputStream(o.toByteArray());
        assertEquals(VALUE, readU64(i));
    }

    @Test
    void consistentReadWriteI8() throws Exception {
        final byte VALUE = -59;

        var o = new ByteArrayOutputStream();
        writeI8(o, VALUE);

        var i = new ByteArrayInputStream(o.toByteArray());
        assertEquals(VALUE, readI8(i));
    }

    @Test
    void consistentReadWriteI16() throws Exception {
        final short VALUE = -7370;

        var o = new ByteArrayOutputStream();
        writeI16(o, VALUE);

        var i = new ByteArrayInputStream(o.toByteArray());
        assertEquals(VALUE, readI16(i));
    }

    @Test
    void consistentReadWriteI32() throws Exception {
        final int VALUE = -1860949839;

        var o = new ByteArrayOutputStream();
        writeI32(o, VALUE);

        var i = new ByteArrayInputStream(o.toByteArray());
        assertEquals(VALUE, readI32(i));
    }

    @Test
    void consistentReadWriteI64() throws Exception {
        final long VALUE = -8232757259224574000L;

        var o = new ByteArrayOutputStream();
        writeI64(o, VALUE);

        var i = new ByteArrayInputStream(o.toByteArray());
        assertEquals(VALUE, readI64(i));
    }
}
