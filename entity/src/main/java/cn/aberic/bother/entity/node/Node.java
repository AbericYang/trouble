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

import java.util.List;

/**
 * 节点对象<p>
 * 新节点A加入后，会将自己的节点基本信息{@link NodeBase}发送至某一已知节点M<p>
 * 如果M本身即为当前竞选节点之一，则将自己的协助节点、当前竞选中的节点集合以及备用节点集合发回给A<p>
 * 如果M本身即为当前竞选节点都协助节点，则直接将A加入到当前竞选节点序列之下，告知A保持心跳并返回当前竞选节点，并允许A参与下一轮排序<p>
 * 如果M为普通节点，则M接收到A加入请求后，会将自己当前访问的竞选节点X发回给A<p>
 * A接收到当前可访问X后，会向X发送接入请求<p>
 * X收到A的接入请求后首先判断自己是否依旧是当前竞选节点之一，如果不是，则重复M的操作<p>
 * 如果X目前还是当前竞选节点之一，则将自己的协助节点Y、当前竞选中的节点集合以及备用节点集合发回给A<p>
 * A收到X的推送后保存并向Y发送加入请求，Y收到A的加入请求后将A加入到当前竞选节点序列之下，告知A保持心跳并返回当前竞选节点，并允许A参与下一轮排序
 * <p>
 * 作者：Aberic on 2018/09/17 14:05
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class Node extends NodeBase {

    /** 当前访问的竞选中节点 */
    private String addressElection;
    /**
     * 当前访问的竞选节点指定协助节点<p>
     * 协助节点将辅助竞选节点管理竞选节点下的子节点<p>
     * 即用于处理除了竞选工作以外的所有事务<p>
     * 如：内部排序、节点加减管理等
     */
    private NodeBase nodeBaseAssist;
    /** 当前竞选中节点集合 <= 50 */
    private List<String> addressElections;
    /** 当前已知备用节点集合/随机节点数 <= 100 */
    private List<String> addresses;

    /** 当前协助节点对象 */
    private NodeAssist nodeAssist;
    /** 当前节点竞选对象 */
    private NodeElection nodeElection;

}
