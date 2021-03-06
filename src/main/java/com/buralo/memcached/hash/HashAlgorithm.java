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

/**
 * Calculate the hash of a Memcached key.
 *
 * @author Brian Matthews
 * @since 1.0.0
 */
@FunctionalInterface
public interface HashAlgorithm {

    /**
     * Calculate the hash of a Memcached key.
     *
     * @param key The Memcached key.
     * @return The hash.
     */
    long hash(String key);
}
