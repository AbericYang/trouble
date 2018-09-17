/*
 * MIT License
 *
 * Copyright (c) 2018 Aberic Yang
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.aberic.bother.entity.node;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

/**
 * 当前协助节点对象
 * <p>
 * 作者：Aberic on 2018/09/17 16:58
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class NodeAssist {

    /** 当前竞选节点下的节点集合 */
    private List<NodeBase> nodeBases;

    /** 新增节点 */
    public void add(NodeBase nodeBase) {
        nodeBases.add(nodeBase);
    }

    /** 获取下标节点 */
    public NodeBase get(int index) {
        return nodeBases.get(index);
    }

    /** 移除下标节点 */
    public void remove(int index) {
        nodeBases.remove(index);
    }

    /** 移除节点对象 */
    public void remove(NodeBase nodeBase) {
        nodeBases.remove(nodeBase);
    }

    /** 节点排序 */
    public void sort(Comparator<NodeBase> c) {
        nodeBases.sort(c);
    }

}
