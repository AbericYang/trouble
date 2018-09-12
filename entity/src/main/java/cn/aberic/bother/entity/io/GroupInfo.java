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
 *
 */

package cn.aberic.bother.entity.io;

import cn.aberic.bother.entity.enums.ConnectStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 连接小组对象
 * <p>
 * 作者：Aberic on 2018/9/12 21:30
 * 邮箱：abericyang@gmail.com
 */
@Setter
@Getter
public class GroupInfo {

    /** 当前Leader的远程地址 */
    private String leaderAddress;
    /** 下一个Leader的远程地址 */
    private String nextLeaderAddress;
    /** 自身在此小组中的当前连接状态 */
    private ConnectStatus status;
    /** 当前Leader选举时间戳 */
    private long timestamp;
    /** 当前连接小组集合 */
    private List<ConnectInfo> infoList;

    /**
     * 返回当前小组是否满员状态，设定每个小组允许最大节点数为21个
     *
     * @return 当前小组是否满员
     */
    public boolean max() {
        return infoList.size() >= 21;
    }

    /**
     * 是否到了开始选举下一节点的时间
     * <p>
     * 默认一小时进行一次选举
     *
     * @return 与否
     */
    public boolean isElectionTime() {
        return new Date().getTime() - timestamp > 3600000;
    }

    /**
     * 自行选举三次，并将本次选举结果进行同步
     * <p>
     * 每小时选举一次
     *
     * @return 选举结果数组
     */
    public String[] election() {
        int size = infoList.size() - 1;
        return new String[]{
                infoList.get(new Random().nextInt(size)).getAddress(),
                infoList.get(new Random().nextInt(size)).getAddress(),
                infoList.get(new Random().nextInt(size)).getAddress()};
    }

}
