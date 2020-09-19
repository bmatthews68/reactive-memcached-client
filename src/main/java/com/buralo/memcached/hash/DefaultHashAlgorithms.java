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

public enum DefaultHashAlgorithms implements HashAlgorithm {

    NATIVE(DefaultHashAlgorithms::nativeHash),
    CRC(DefaultHashAlgorithms::crcHash),
    FNV1_64(DefaultHashAlgorithms::fnv1_64Hash),
    FNV1A_64(DefaultHashAlgorithms::fnv1a_64Hash),
    FNV1_32(DefaultHashAlgorithms::fnv1_32Hash),
    FNV1A_32(DefaultHashAlgorithms::fnv1a_32Hash),
    KETAMA(DefaultHashAlgorithms::ketamaHash);

    private static final MessageDigest MESSAGE_DIGEST;

    private static final long FNV_64_INIT = 0xcbf29ce484222325L;
    private static final long FNV_64_PRIME = 0x100000001b3L;

    private static final long FNV_32_INIT = 2166136261L;
    private static final long FNV_32_PRIME = 16777619;

    private final HashFunction hashFunction;

    static {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("MD5 not supported", e);
        }
    }

    DefaultHashAlgorithms(final HashFunction hashFunction) {
        this.hashFunction = hashFunction;
    }

    @Override
    public long hash(final String key) {
        return hashFunction.hash(key) & 0xffffffffL;
    }

    private static long nativeHash(final String key) {
        return key.hashCode();
    }

    private static long crcHash(final String key) {
        final CRC32 crc32 = new CRC32();
        crc32.update(key.getBytes(StandardCharsets.UTF_8));
        return (crc32.getValue() >> 16) & 0x7fff;
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
            return ((long) (hashBytes[3] & 0xFF) << 24) | ((long) (hashBytes[2] & 0xFF) << 16)
                    | ((long) (hashBytes[1] & 0xFF) << 8) | (hashBytes[0] & 0xFF);
        } catch (CloneNotSupportedException e) {
            throw new UnsupportedOperationException("Clone of MD5 message digest not supported", e);
        }
    }

    @FunctionalInterface
    interface HashFunction {
        long hash(String key);
    }
}
