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

package com.buralo.memcached.hash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

/**
 * The built-in default hashing algorithms.
 *
 * @author Brian Matthews
 * @since 1.0.0
 */
public enum DefaultHashAlgorithms implements HashAlgorithm {

    /**
     * Uses JDK hashCode function to calculate the hash value.
     */
    NATIVE(String::hashCode),
    CRC(DefaultHashAlgorithms::crcHash),
    FNV1_64(DefaultHashAlgorithms::fnv1_64Hash),
    FNV1A_64(DefaultHashAlgorithms::fnv1a_64Hash),
    FNV1_32(DefaultHashAlgorithms::fnv1_32Hash),
    FNV1A_32(DefaultHashAlgorithms::fnv1a_32Hash),
    KETAMA(DefaultHashAlgorithms::ketamaHash),
    MYSQL(DefaultHashAlgorithms::mysqlHash),
    ELF(DefaultHashAlgorithms::elfHash),
    RS(DefaultHashAlgorithms::rsHash),
    LUA(DefaultHashAlgorithms::luaHash),
    ONE_AT_A_TIME(DefaultHashAlgorithms::oneAtATimeHash);

    private static final MessageDigest MESSAGE_DIGEST;

    private static final long FNV_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV_64_PRIME = 0x100000001b3L;

    private static final long FNV_32_INIT = 2166136261L;
    private static final long FNV_32_PRIME = 16777619;

    private static final int RS_B = 378551;
    private static final int RS_A = 63689;


    private final HashFunction delegate;

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("MD5 not supported", e);
        }
    }

    /**
     * Initialize an enumeration value by supplying the delegate hash function that calculates the hash value.
     *
     * @param delegate The delegate hash function.
     */
    DefaultHashAlgorithms(final HashFunction delegate) {
        this.delegate = delegate;
    }

    /**
     * Calculate the hash of a Memcached key.
     *
     * @param key The Memcached key.
     * @return The hash value.
     */
    @Override
    public long hash(final String key) {
        return delegate.hash(key) & 0xFFFFFFFFL;
    }

    /**
     * Calculate a string hash using CRC32.
     *
     * @param key The Memcached key.
     * @return The hash value.
     */
    private static long crcHash(final String key) {
        final CRC32 crc32 = new CRC32();
        crc32.update(key.getBytes(StandardCharsets.UTF_8));
        return (crc32.getValue() >> 16) & 0x7FFFL;
    }

    private static long fnv1_64Hash(final String key) {
        return fnv1_Hash(key, FNV_64_INIT, FNV_64_PRIME);
    }

    private static long fnv1a_64Hash(final String key) {
        return fnv1a_Hash(key, FNV_64_INIT, FNV_64_PRIME);
    }

    private static long fnv1_32Hash(final String key) {
        return fnv1_Hash(key, FNV_32_INIT, FNV_32_PRIME);
    }

    private static long fnv1a_32Hash(final String key) {
        return fnv1a_Hash(key, FNV_32_INIT, FNV_32_PRIME);
    }

    private static long fnv1_Hash(final String key,
                                  final long init,
                                  final long prime) {
        return key.chars().asLongStream().reduce(init, (l, r) -> (l * prime) ^ r);
    }

    private static long fnv1a_Hash(final String key,
                                   final long init,
                                   final long prime) {
        return key.chars().asLongStream().reduce(init, (l, r) -> (l ^ r) * prime);
    }

    private static long ketamaHash(final String key) {
        try {
            final MessageDigest messageDigest = (MessageDigest) MESSAGE_DIGEST.clone();
            messageDigest.update(key.getBytes(StandardCharsets.UTF_8));
            final byte[] hashBytes = messageDigest.digest();
            return ((hashBytes[3] & 0xFFL) << 24) | ((hashBytes[2] & 0xFFL) << 16) | ((hashBytes[1] & 0xFFL) << 8) | (hashBytes[0] & 0xFFL);
        } catch (final CloneNotSupportedException e) {
            throw new UnsupportedOperationException("Clone of MD5 message digest not supported", e);
        }
    }

    private static long mysqlHash(final String key) {
        long rv = 0L;
        for (int i = 0, nr2 = 4; i < key.length(); i++, nr2 += 3) {
            rv ^= ((rv & 63) + nr2) * key.charAt(i) + (rv << 8);
        }
        return rv;
    }

    private static long elfHash(final String key) {
        return key.chars().asLongStream().reduce(0L, (l, r) -> {
            l = (l << 4) + r;
            long x = l & 0xF0000000L;
            if (x != 0) {
                return (l ^ x >> 24) & ~x;
            } else {
                return l;
            }
        }) & 0x7FFFFFFFL;
    }

    private static long rsHash(final String key) {
        long rv = 0L;
        int a = RS_A;
        for (int i = 0; i < key.length(); i++) {
            rv = rv * a + key.charAt(i);
            a *= RS_B;
        }
        return rv & 0x7FFFFFFF;
    }

    private static long luaHash(final String key) {
        final int step = (key.length() >> 5) + 1;
        long rv = key.length();
        for (int len = key.length(); len >= step; len -= step) {
            rv = rv ^ (rv << 5) + (rv >> 2) + key.charAt(len - 1);
        }
        return rv;
    }

    private static long oneAtATimeHash(final String key) {
        int hash = 0;
        for (final byte bt : key.getBytes(StandardCharsets.UTF_8)) {
            hash += (bt & 0xFF);
            hash += (hash << 10);
            hash ^= (hash >>> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >>> 11);
        hash += (hash << 15);
        return hash;
    }

    /**
     * Defines an interface for lambda functions that calculate hashes from key strings.
     */
    @FunctionalInterface
    interface HashFunction {

        /**
         * Calculate hashes from key strings.
         *
         * @param key Key string.
         * @return The hash value.
         */
        long hash(String key);
    }
}
