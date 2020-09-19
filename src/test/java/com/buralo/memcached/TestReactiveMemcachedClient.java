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

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Testcontainers
public class TestReactiveMemcachedClient {

    @Container
    public static GenericContainer<?> MEMCACHED_CONTAINER = new GenericContainer<>("memcached:latest")
            .withExposedPorts(11211)
            .waitingFor(new HostPortWaitStrategy());

    @Test
    public void isAlive() throws UnknownHostException {
        final Client client = new ClientImpl();
        final Boolean isAlive = client.connect(new InetSocketAddress(InetAddress.getLocalHost(), MEMCACHED_CONTAINER.getMappedPort(11211)))
                .map(Session::isAlive)
                .block();
        assertThat(isAlive).isTrue();
    }
}
