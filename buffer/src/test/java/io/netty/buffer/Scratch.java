/*
 * Copyright (C) 2009-2018 Hangzhou 2Dfire Technology Co., Ltd. All rights reserved
 */
package io.netty.buffer;

/**
 * Scratch
 *
 * @author luobosi@2dfire.com
 * @since 2019-01-25
 */
public class Scratch {

    public static void main(String[] args) {
        int page = 1024 * 8;

        PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;

        allocator.directBuffer(2 * page);
    }
}
