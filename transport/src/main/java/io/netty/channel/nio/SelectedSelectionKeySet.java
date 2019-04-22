/*
 * Copyright 2013 The Netty Project
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
package io.netty.channel.nio;

import java.nio.channels.SelectionKey;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * TODO：优化
 * 添加新 select 到就绪事件的 SelectionKey 到 keys 中。
 * 当超过数组大小上限时，调用 #increaseCapacity() 方法，进行两倍扩容。
 * 相比 SelectorImpl(JDK 底层) 中使用的 selectedKeys 所使用的 HashSet 的 #add(E e) 方法，事件复杂度从 O(lgn) 降低到 O(1) 。
 */
final class SelectedSelectionKeySet extends AbstractSet<SelectionKey> {

    /**
     * SelectKey 数组
     */
    SelectionKey[] keys;
    /**
     * 数组可读大小
     */
    int size;

    SelectedSelectionKeySet() {
        keys = new SelectionKey[1024];
    }

    @Override
    public boolean add(SelectionKey o) {
        if (o == null) {
            return false;
        }

        keys[size++] = o;
        if (size == keys.length) {
            increaseCapacity();
        }

        return true;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<SelectionKey> iterator() {
        return new Iterator<SelectionKey>() {
            private int idx;

            @Override
            public boolean hasNext() {
                return idx < size;
            }

            @Override
            public SelectionKey next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return keys[idx++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    void reset() {
        reset(0);
    }

    void reset(int start) {
        Arrays.fill(keys, start, size, null);
        size = 0;
    }

    private void increaseCapacity() {
        // 两倍扩容
        SelectionKey[] newKeys = new SelectionKey[keys.length << 1];
        // 复制老数组到新数组
        System.arraycopy(keys, 0, newKeys, 0, size);
        // 赋值给老数组
        keys = newKeys;
    }
}
