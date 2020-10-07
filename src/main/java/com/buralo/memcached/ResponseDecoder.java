/*
 * Copyright 2021 Búraló Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.buralo.memcached;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

public class ResponseDecoder {
    public Mono<Response> decode(final ByteBuf buf) {
        if (buf.readableBytes() < 16) {
            return Mono.empty();
        }
        if (buf.readByte() != (byte) 0x81) {
            return Mono.error(new IllegalArgumentException());
        }
        final byte opcode = buf.readByte();
        final short keyLength = buf.readShort();
        final byte extrasLength = buf.readByte();
        if (buf.readByte() != (byte) 0x00) {
            return Mono.error(new IllegalArgumentException());
        }
        final short status = buf.readShort();
        final int totalLength = buf.readInt();
        final int opaque = buf.readInt();
        final long cas = buf.readLong();
        final ByteBuf extras = extrasLength == 0 ? Unpooled.EMPTY_BUFFER : buf.readBytes(extrasLength);
        final String key = keyLength == 0 ? "" : buf.readBytes(keyLength).toString(StandardCharsets.UTF_8);
        final int valueLength = totalLength - (keyLength + extrasLength);
        final ByteBuf value = valueLength == 0 ? Unpooled.EMPTY_BUFFER : buf.readBytes(valueLength);
        return Mono.just(
                new Response(
                        Opcode.fromCode(opcode),
                        Status.fromCode(status),
                        opaque,
                        cas,
                        key,
                        extras,
                        value));
    }
}
