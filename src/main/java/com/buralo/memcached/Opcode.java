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

/**
 * Operation codes.
 */
public enum Opcode {
    GET((byte) 0x00),
    SET((byte) 0x01),
    ADD((byte) 0x02),
    REPLACE((byte) 0x03),
    DELETE((byte) 0x04),
    INCREMENT((byte) 0x05),
    DECREMENT((byte) 0x06),
    QUIT((byte) 0x07),
    FLUSH((byte) 0x08),
    GETQ((byte) 0x09),
    NOOP((byte) 0x0a),
    VERSION((byte) 0x0b),
    GETK((byte) 0x0c),
    GETKQ((byte) 0x0d),
    APPEND((byte) 0x0e),
    PREPEND((byte) 0x0f),
    STATS((byte) 0x11);
    private byte code;

    Opcode(final byte code) {
        this.code = code;
    }

    public byte code() {
        return code;
    }

    public static Opcode fromCode(final byte code) {
        for (final Opcode opcode : values()) {
            if (opcode.code == code) {
                return opcode;
            }
        }
        return null;
    }
}
