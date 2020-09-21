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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TestDefaultHashAlgorithms {

    private static void assertHash(final DefaultHashAlgorithms algorithm,
                                   final String key,
                                   final long expectedValue) {
        assertThat(algorithm.hash(key)).isEqualTo(expectedValue);
    }

    @Test
    public void verifyNativeHashAlgorithm() {
        assertHash(DefaultHashAlgorithms.NATIVE, "Test1", "Test1".hashCode());
        assertHash(DefaultHashAlgorithms.NATIVE, "Test2", "Test2".hashCode());
        assertHash(DefaultHashAlgorithms.NATIVE, "Test3", "Test3".hashCode());
        assertHash(DefaultHashAlgorithms.NATIVE, "Test4", "Test4".hashCode());
    }

    @Test
    public void verifyCrcHashAlgorithm() {
        assertHash(DefaultHashAlgorithms.CRC, "Test1", 19315L);
        assertHash(DefaultHashAlgorithms.CRC, "Test2", 21114L);
        assertHash(DefaultHashAlgorithms.CRC, "Test3", 9597L);
        assertHash(DefaultHashAlgorithms.CRC, "Test4", 15129L);
        assertHash(DefaultHashAlgorithms.CRC, "UDATA:edevil@sapo.pt", 558L);
    }

    @Test
    public void verifyFnV164HashAlgorithm() {
        assertHash(DefaultHashAlgorithms.FNV1_64, "", 0x84222325L);
        assertHash(DefaultHashAlgorithms.FNV1_64, " ", 0x8601b7ffL);
        assertHash(DefaultHashAlgorithms.FNV1_64, "hello world!", 0xb97b86bcL);
        assertHash(DefaultHashAlgorithms.FNV1_64, "Lorem ipsum dolor sit amet, consectetuer adipiscing elit.", 0xe87c054aL);
        assertHash(DefaultHashAlgorithms.FNV1_64, "wd:com.google", 0x071b08f8L);
        assertHash(DefaultHashAlgorithms.FNV1_64, "wd:com.google ", 0x12f03d48L);
    }

    @Test
    public void verifyFnV1A64HashAlgorithm() {
        assertHash(DefaultHashAlgorithms.FNV1A_64, "", 0x84222325L);
        assertHash(DefaultHashAlgorithms.FNV1A_64, " ", 0x8601817fL);
        assertHash(DefaultHashAlgorithms.FNV1A_64, "hello world!", 0xcd5a2672L);
        assertHash(DefaultHashAlgorithms.FNV1A_64, "Lorem ipsum dolor sit amet, consectetuer adipiscing elit.", 0xbec309a8L);
        assertHash(DefaultHashAlgorithms.FNV1A_64, "wd:com.google", 0x097b3f26L);
        assertHash(DefaultHashAlgorithms.FNV1A_64, "wd:com.google ", 0x1c6c1732L);
    }


    @Test
    public void verifyFnV132HashAlgorithm() {
        assertHash(DefaultHashAlgorithms.FNV1_32, "", 0x811c9dc5L);
        assertHash(DefaultHashAlgorithms.FNV1_32, " ", 0x050c5d3fL);
        assertHash(DefaultHashAlgorithms.FNV1_32, "hello world!", 0x8a01b99cL);
        assertHash(DefaultHashAlgorithms.FNV1_32, "Lorem ipsum dolor sit amet, consectetuer adipiscing elit.", 0x9277524aL);
        assertHash(DefaultHashAlgorithms.FNV1_32, "wd:com.google", 0x455e0df8L);
        assertHash(DefaultHashAlgorithms.FNV1_32, "wd:com.google ", 0x2b0ffd48L);
    }

    @Test
    public void verifyFnV1A32HashAlgorithm() {
        assertHash(DefaultHashAlgorithms.FNV1A_32, "", 0x811c9dc5L);
        assertHash(DefaultHashAlgorithms.FNV1A_32, " ", 0x250c8f7fL);
        assertHash(DefaultHashAlgorithms.FNV1A_32, "hello world!", 0xb034fff2L);
        assertHash(DefaultHashAlgorithms.FNV1A_32, "Lorem ipsum dolor sit amet, consectetuer adipiscing elit.", 0xa9795ec8L);
        assertHash(DefaultHashAlgorithms.FNV1A_32, "wd:com.google", 0xaa90fcc6L);
        assertHash(DefaultHashAlgorithms.FNV1A_32, "wd:com.google ", 0x683e1e12L);
    }

    @Test
    public void verifyKetamaHashAlgorithm() {
        assertHash(DefaultHashAlgorithms.KETAMA, "26", 3979113294L);
        assertHash(DefaultHashAlgorithms.KETAMA, "1404", 2065000984L);
        assertHash(DefaultHashAlgorithms.KETAMA, "4177", 1125759251L);
        assertHash(DefaultHashAlgorithms.KETAMA, "9315", 3302915307L);
        assertHash(DefaultHashAlgorithms.KETAMA, "14745", 2580083742L);
        assertHash(DefaultHashAlgorithms.KETAMA, "105106", 3986458246L);
        assertHash(DefaultHashAlgorithms.KETAMA, "355107", 3611074310L);
    }

    @Test
    public void verifyMysqlHashAlgorithm() {
        assertHash(DefaultHashAlgorithms.MYSQL, "abcdefghijklmnopqrstuvwxyz1234567890", 3201966090L);
    }

    @Test
    public void verifyElfHashAlgorithm() {
        assertHash(DefaultHashAlgorithms.ELF, "jdfgsdhfsdfsd 6445dsfsd7fg/*/+bfjsdgf%$^", 248446350L);
    }

    @Test
    public void verifyRsHashAlgorithm() {
        assertHash(DefaultHashAlgorithms.RS, "abcdefghijklmnopqrstuvwxyz1234567890", 1950351854L);
    }

    @Test
    public void verifyLuaHashAlgorithm() {
        assertHash(DefaultHashAlgorithms.LUA, "abcdefghijklmnopqrstuvwxyz1234567890", 1994113120L);
    }

    @Test
    public void verifyOneAtATimeHashAlgorithm() {
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "sausage", 2834523395L);
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "blubber", 1103975961L);
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "pencil", 3318404908L);
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "cloud", 670342857L);
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "moon", 2385442906L);
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "water", 3403519606L);
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "computer", 2375101981L);
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "school", 1513618861L);
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "network", 2981967937L);
        assertHash(DefaultHashAlgorithms.ONE_AT_A_TIME, "hammer", 1218821080L);
    }
}
