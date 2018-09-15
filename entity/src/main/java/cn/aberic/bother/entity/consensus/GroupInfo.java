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

package cn.aberic.bother.entity.consensus;

import cn.aberic.bother.entity.BeanJsonField;
import cn.aberic.bother.entity.enums.ConnectStatus;
import cn.aberic.bother.tools.Constant;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
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
public class GroupInfo implements BeanJsonField {

    /** 当前Leader的远程地址 */
    private String leaderAddress;
    /** 下一个Leader的远程地址 */
    private String nextLeaderAddress;
    /** 自身在此小组中的当前连接状态 */
    private ConnectStatus status;
    /** 当前Leader选举时间戳 */
    private long timestamp;
    /** 当前连接小组集合 */
    private List<String> addresses;
    /** 当前选举集合 */
    private List<ElectionVote> votes = new ArrayList<>();
    /** 下轮选举集合 */
    private List<ElectionVote> nextVotes = new ArrayList<>();

    /**
     * 当前连接小组集合新增节点
     *
     * @param address 节点地址
     */
    public void add(String address) {
        addresses.add(address);
    }

    /**
     * 下轮选举集合新增选票
     *
     * @param vote 选票
     */
    public void add(ElectionVote vote) {
        boolean voted = false; // 该节点是否对本次选举进行过投票
        for (ElectionVote v : nextVotes) {
            if (StringUtils.equals(v.getAddress(), vote.getAddress())) {
                voted = true;
            }
        }
        if (!voted) {
            nextVotes.add(vote);
        }
    }

    /**
     * 返回当前小组是否满员状态，设定每个小组允许最大节点数为51个
     *
     * @return 当前小组是否满员
     */
    public boolean max() {
        return addresses.size() >= Constant.GROUP_COUNT;
    }

    /**
     * 是否到了开始选举下一节点的时间
     * <p>
     * 默认10分钟进行一次选举
     *
     * @return 与否
     */
    public boolean electionTime() {
        return System.currentTimeMillis() - timestamp > Constant.GROUP_ELECTION_TIME;
    }

    /**
     * 自行选举三次，并将本次选举结果进行同步
     * <p>
     * 每小时选举一次
     *
     * @return 选举结果数组
     */
    public List<String> election() {
        int size = addresses.size() - 1;
        List<String> strings = new ArrayList<>();
        strings.add(addresses.get(new Random().nextInt(size)));
        strings.add(addresses.get(new Random().nextInt(size)));
        strings.add(addresses.get(new Random().nextInt(size)));
        return strings;
    }

}
