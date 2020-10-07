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

public enum Status {
    /**
     * No error
     */
    NO_ERROR(0),
    /**
     * Key not found
     */
    NOT_FOUND(1),
    /**
     * Key exists
     */
    EXISTS(2),
    /**
     * Value too large
     */
    TOO_LARGE(3),
    /**
     * Invalid arguments
     */
    INVALID_ARGUMENTS(4),
    /**
     * Item not stored
     */
    NOT_STORED(5),
    /**
     * Incr/Decr on non-numeric value
     */
    NON_NUMERIC_VALUE(6),
    /**
     * The vbucket belongs to another server
     */
    INVALID_VBUCKET(7),
    /**
     * Authentication error
     */
    AUTH_ERROR(8),
    /**
     * Authentication continue
     */
    AUTH_CONT(9),
    /**
     * Unknown command
     */
    UNKNOWN_COMMAND(129),
    /**
     * Out of memory
     */
    OUT_OF_MEMORY(130),
    /**
     * Not supported
     */
    NOT_SUPPORTED(131),
    /**
     * Internal error
     */
    INTERNAL_ERROR(132),
    /**
     * Busy
     */
    BUSY(133),
    /**
     * Temporary failure
     */
    TEMPORARY_FAILURE(134);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }

    public static Status fromCode(int code) {
        for (final Status status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null;
    }
}
