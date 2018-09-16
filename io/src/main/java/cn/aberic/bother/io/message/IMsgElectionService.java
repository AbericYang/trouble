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

package cn.aberic.bother.io.message;

import cn.aberic.bother.entity.consensus.ConnectSelf;
import cn.aberic.bother.entity.consensus.ElectionVote;
import cn.aberic.bother.entity.consensus.GroupInfo;
import cn.aberic.bother.entity.consensus.VoteResult;
import cn.aberic.bother.entity.enums.ConnectStatus;
import cn.aberic.bother.entity.enums.JoinLevel;
import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.tools.MsgPackTool;

import java.util.HashMap;
import java.util.Map;

/**
 * 应答选举消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/14 14:08
 * 邮箱：abericyang@gmail.com
 */
public interface IMsgElectionService extends IMsgRequestService {

    /**
     * 应答选举消息业务处理方案，由{@link IMsgReceiveService}继承并启用该方案
     *
     * @param address 当前指定通道的连接地址
     * @param msgData 协议消息对象
     */
    default void election(String address, MessageData msgData) {
        switch (msgData.getProtocol()) {
            case ELECTION_QUICK: // 接收到通知同组节点尽快完成投票操作
                JoinLevel level = JoinLevel.get(MsgPackTool.bytes2String(msgData.getBytes()));
                // 发起投票应该把投票结果发送到同组的每一个节点
                IOContext.obtain().sync(msgData.getProtocol(), ConnectSelf.obtain().getGroups().get(0).election(), level);
                break;
            case ELECTION_TOWER: // 接收到发起楼选举协议-0x20
                level = JoinLevel.TOWER;
                electionExec(address, msgData, level);
                break;
            case ELECTION_COMMUNITY: // 接收到发起社区选举协议-0x21
                level = JoinLevel.COMMUNITY;
                electionExec(address, msgData, level);
                break;
            case ELECTION_COUNTY: // 接收到发起县城选举协议-0x22
                level = JoinLevel.COUNTY;
                electionExec(address, msgData, level);
                break;
            case ELECTION_CITY: // 接收到发起市选举协议-0x23
                level = JoinLevel.CITY;
                electionExec(address, msgData, level);
                break;
            case ELECTION_PROVINCE: // 接收到发起省选举协议-0x24
                level = JoinLevel.PROVINCE;
                electionExec(address, msgData, level);
                break;
            case ELECTION_RESULT: // 接收到发起楼选举结果协议-0x25

                break;
        }
    }

    default void electionExec(String address, MessageData msgData, JoinLevel level) {
        ElectionVote vote = new ElectionVote(); // 新建选票
        vote.setAddress(address); // 该选票的投票节点地址
        vote.setAddresses(MsgPackTool.bytes2List(msgData.getBytes())); // 接收到的该选票节点地址提交的选举结果
        GroupInfo info = ConnectSelf.obtain().getGroups().get(level.getLevel()); // 获取当前楼小组
        info.add(vote); // 将选票放入下一轮选举结果集合中
        // 如果自身不是Leader节点，则执行到此结束
        if (ConnectSelf.obtain().getGroups().get(level.getLevel()).getStatus() != ConnectStatus.LEADER) {
            return;
        }
        if (info.electionTime()) { // 判断是否超过投票时间
            // 超过投票时间，则不再接收新的投票提交，直接开启本轮投票结果，并将投票结果广播至本组所有节点进行结果比对
            Map<String, Integer> voteMap = new HashMap<>();
            ConnectSelf.obtain().getGroups().get(level.getLevel()).getVotes().forEach(electionVote -> electionVote.getAddresses().forEach(ip -> {
                Integer i = voteMap.get(ip);
                if (null != i) {
                    voteMap.put(ip, i + 1);
                }
            }));
            String ipVote = "";
            for (Map.Entry<String, Integer> entry : voteMap.entrySet()) {
                if (ipVote.isEmpty() || voteMap.get(ipVote) < entry.getValue()) {
                    ipVote = entry.getKey();
                }
            }
            VoteResult result = new VoteResult();
            result.setLevel(level);
            result.setAddress(ipVote);
            IOContext.obtain().broadcast(ProtocolStatus.ELECTION_RESULT, result, level);
        } else {
            // 通知同组节点尽快完成投票操作
            IOContext.obtain().broadcast(ProtocolStatus.ELECTION_QUICK, level.getAlias(), level);
        }
    }

}