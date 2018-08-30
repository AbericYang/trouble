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

package cn.aberic.bother.consensus.exec;

import cn.aberic.bother.entity.consensus.ConsensusStatus;

/**
 * 主动发起共识操作对象——共识实现层-cta to achieve
 * <p>
 * 作者：Aberic on 2018/8/24 21:27
 * 邮箱：abericyang@gmail.com
 */
public class Proactive {

    /**
     * 对当前高度的区块发起大小共识
     *
     * @param height 区块高度
     * @param status 共识级别
     */
    public void verifyBlock(int height, ConsensusStatus status) {
        // TODO: 2018/8/24
        // 对同组范围和已连接其他组一起发起结果共识
        // 若共识级别为初始和中间区块冲突，则仅对当前区块进行共识，节省共识时间
        // 并根据所有共识节点的上一区块hash比对后决定是否追溯对上一区块再次进行共识
        // 若对上一区块发起新的共识，则本次共识结果放弃，本次区块同步放弃，组织恶意节点的持续扩充
        // 共识结果有网络层进行回执并调用共识层的解决方案，共识结束后继续同步后续区块
    }
}
