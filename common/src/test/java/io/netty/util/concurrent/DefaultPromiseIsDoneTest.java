/*
 * Copyright (C) 2009-2018 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package io.netty.util.concurrent;

/**
 * DefaultPromiseIsDoneTest
 *
 * @author luobosi@2dfire.com
 * @since 2019-03-07
 */
public class DefaultPromiseIsDoneTest {

    private final Promise defaultPromise = GlobalEventExecutor.INSTANCE.newPromise();

    public static void main(String[] args) {
        DefaultPromiseIsDoneTest promiseIsDoneTest = new DefaultPromiseIsDoneTest();

        promiseIsDoneTest.isDoneTest();
    }

    private void isDoneTest() {
        defaultPromise.setUncancellable();
        defaultPromise.cancel(Boolean.FALSE);

        boolean done = defaultPromise.isDone();
        System.out.println(done);
    }
}
