/*
 * Copyright 2008-2024 MOIS(Ministry of the Interior and Safety).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.egovframe.rte.bat.core.item.file.mapping;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * EgovObjectMapper 의 호출별 매핑 대상 격리를 검증한다.
 */
class EgovObjectMapperConcurrencyTest {

    @Test
    void mapObject_doesNotShareTargetBetweenConcurrentInvocations() throws Exception {
        RacingVO.resetSynchronization();

        EgovObjectMapper<RacingVO> mapper = new EgovObjectMapper<>();
        mapper.setType(RacingVO.class);
        mapper.setNames(new String[]{"col1", "col2"});
        mapper.afterPropertiesSet();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<RacingVO> aFuture = executor.submit(
                    () -> mapper.mapObject(Arrays.asList("A1", "A2")));

            assertTrue(RacingVO.firstSetterEntered.await(5, TimeUnit.SECONDS),
                    "Thread A did not enter the first setter");

            RacingVO b = mapper.mapObject(Arrays.asList("B1", "B2"));

            RacingVO.releaseA.countDown();
            RacingVO a = aFuture.get(5, TimeUnit.SECONDS);

            assertAll(
                    () -> assertNotSame(a, b),
                    () -> assertEquals("A1", a.getCol1()),
                    () -> assertEquals("A2", a.getCol2()),
                    () -> assertEquals("B1", b.getCol1()),
                    () -> assertEquals("B2", b.getCol2()));
        } finally {
            RacingVO.releaseA.countDown();
            executor.shutdownNow();
            assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS),
                    "Executor did not terminate");
        }
    }

    /**
     * 첫 번째 row의 첫 setter 안에서 매핑을 멈춰 두 번째 row가 먼저 완료되게 한다.
     */
    public static class RacingVO {

        private static volatile CountDownLatch firstSetterEntered;
        private static volatile CountDownLatch releaseA;

        private String col1;

        private String col2;

        static void resetSynchronization() {
            firstSetterEntered = new CountDownLatch(1);
            releaseA = new CountDownLatch(1);
        }

        public String getCol1() {
            return col1;
        }

        public void setCol1(String col1) {
            this.col1 = col1;
            if ("A1".equals(col1)) {
                firstSetterEntered.countDown();
                try {
                    if (!releaseA.await(5, TimeUnit.SECONDS)) {
                        throw new AssertionError("Thread A was not released");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new AssertionError(e);
                }
            }
        }

        public String getCol2() {
            return col2;
        }

        public void setCol2(String col2) {
            this.col2 = col2;
        }

    }

}
