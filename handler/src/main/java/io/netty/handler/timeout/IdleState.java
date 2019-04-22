/*
 * Copyright 2012 The Netty Project
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
package io.netty.handler.timeout;

import io.netty.channel.Channel;


/**
 * An {@link Enum} that represents the idle state of a {@link Channel}.
 */
public enum IdleState {
    /**
     * No data was received for a while.
     * 读超时：在一定时间内没有从通道内读取到任何数据
     */
    READER_IDLE,
    /**
     * No data was sent for a while.
     * 写超时：一定时间内没有从通道内写入任何数据
     */
    WRITER_IDLE,
    /**
     * No data was either received or sent for a while.
     * 读写超时：一定时间内没有从通道读取或者写入任何数据
     */
    ALL_IDLE
}
