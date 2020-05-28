/*
 * Copyright 2016 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.util.concurrent;

import io.netty.util.internal.UnstableApi;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Default implementation which uses simple round-robin to choose next {@link EventExecutor}.
 *
 * 设计模式：工厂模式
 */
@UnstableApi
public final class DefaultEventExecutorChooserFactory implements EventExecutorChooserFactory {

    public static final DefaultEventExecutorChooserFactory INSTANCE = new DefaultEventExecutorChooserFactory();

    private DefaultEventExecutorChooserFactory() {
    }

    /**
     * 主要是为新连接绑定 NioEventLoop
     * @param executors
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    public EventExecutorChooser newChooser(EventExecutor[] executors) {
        /*
            TODO：疑问：两种模式有什么区别？
            幂次效率是很高的，计算机底层是没有取模的操作的，取模需要重新调取 api，效率低下

            数组循环，
            idx                 1 1 1 0 1 0
                                          &
            executors.length - 1    1 1 1 1
            result                  1 0 1 0

            从上面可以知道，进行下一轮循环的时候，后面四位都变成了 0，那么和 length - 1 相与 也变成了 0，下表也就是 0
         */
        if (isPowerOfTwo(executors.length)) {
            return new PowerOfTwoEventExecutorChooser(executors);
        } else {
            return new GenericEventExecutorChooser(executors);
        }
    }

    /**
     * TODO：疑问：这个判断是什么意思？
     * 根据 nThreads 的大小, 创建不同的 Chooser, 即如果 nThreads 是 2 的幂, 则使用 PowerOfTwoEventExecutorChooser,
     * 反之使用 GenericEventExecutorChooser. 不论使用哪个 Chooser, 它们的功能都是一样的,
     * 即从 children 数组中选出一个合适的 EventExecutor 实例.
     *
     * @param val length
     * @return true/false
     */
    private static boolean isPowerOfTwo(int val) {
        return (val & -val) == val;
    }

    private static final class PowerOfTwoEventExecutorChooser implements EventExecutorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        PowerOfTwoEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override
        public EventExecutor next() {
            return executors[idx.getAndIncrement() & executors.length - 1];
        }
    }

    /**
     * 普通方式
     * 其实就是循环数组返回
     */
    private static final class GenericEventExecutorChooser implements EventExecutorChooser {
        private final AtomicInteger idx = new AtomicInteger();
        private final EventExecutor[] executors;

        GenericEventExecutorChooser(EventExecutor[] executors) {
            this.executors = executors;
        }

        @Override
        public EventExecutor next() {
            return executors[Math.abs(idx.getAndIncrement() % executors.length)];
        }
    }
}
