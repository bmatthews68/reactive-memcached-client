package com.buralo.memcached;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class TestResponseDecoder {

    private static final byte[] ERROR_MESSAGE = {
            (byte) 0x81, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x09,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
            (byte) 0x4e, (byte) 0x6f, (byte) 0x74, (byte) 0x20,
            (byte) 0x66, (byte) 0x6f, (byte) 0x75, (byte) 0x6e,
            (byte) 0x64
    };

    private final ResponseDecoder decoder = new ResponseDecoder();

    @Test
    public void parseErrorMessageResponse() {
        final Response header = decoder.decode(Unpooled.wrappedBuffer(ERROR_MESSAGE)).block();
        assertThat(header).isNotNull();
        assertThat(header.opcode()).isEqualTo(Opcode.GET);
        assertThat(header.status()).isEqualTo(Status.NOT_FOUND);
        assertThat(header.opaque()).isEqualTo(0);
        assertThat(header.cas()).isEqualTo(0L);
        assertThat(header.key()).isEqualTo("");
        assertThat(header.extras()).isEqualTo(Unpooled.EMPTY_BUFFER);
        assertThat(header.value().toString(StandardCharsets.UTF_8)).isEqualTo("Not found");
    }
}
