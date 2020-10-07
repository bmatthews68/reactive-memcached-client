package com.buralo.memcached;

import io.netty.buffer.ByteBuf;

public class Response {
    private final Opcode opcode;
    private final Status status;
    private final int opaque;
    private final long cas;
    private final String key;
    private final ByteBuf extras;
    private final ByteBuf value;

    public Response(final Opcode opcode,
                    final Status status,
                    final int opaque,
                    final long cas,
                    final String key,
                    final ByteBuf extras,
                    final ByteBuf value) {
        this.opcode = opcode;
        this.status = status;
        this.opaque = opaque;
        this.cas = cas;
        this.key = key;
        this.extras = extras;
        this.value = value;
    }

    public Opcode opcode() {
        return opcode;
    }

    public Status status() {
        return status;
    }

    public int opaque() {
        return opaque;
    }

    public long cas() {
        return cas;
    }

    public String key() {
        return key;
    }

    public ByteBuf extras() {
        return extras;
    }

    public ByteBuf value() {
        return value;
    }
}
