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
X}
