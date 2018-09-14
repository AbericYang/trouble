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
import cn.aberic.bother.entity.enums.JoinLevel;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.tools.MsgPackTool;
import io.netty.channel.Channel;

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
     * @param channel 当前指定通道
     * @param msgData 协议消息对象
     */
    default void election(Channel channel, MessageData msgData) {
        log().debug("请求协议：{}，数据ID：{}", msgData.getProtocol().getB(), msgData.getDataId());
        String address = channel.remoteAddress().toString().split(":")[0].split("/")[1];
        switch (msgData.getProtocol()) {
            case ELECTION_TOWER: // 接收到发起楼选举协议-0x20
                ElectionVote vote = new ElectionVote(); // 新建选票
                vote.setAddress(address); // 该选票的投票节点地址
                vote.setAddresses(MsgPackTool.bytes2List(msgData.getBytes())); // 接收到的该选票节点地址提交的选举结果
                GroupInfo info = ConnectSelf.obtain().getGroups().get(JoinLevel.TOWER.getLevel()); // 获取当前楼小组
                info.add(vote); // 将选票放入下一轮选举结果集合中
                if (info.electionTime()) { // 判断是否超过投票时间
                    // TODO: 2018/9/14 超过投票时间，则不再接收新的投票提交，直接开启本轮投票结果，并将投票结果发送至已投票的节点进行结果比对
                } else {
                    // TODO: 2018/9/14 通知同组节点尽快完成投票操作
                }
                if (info.getAddresses().size() - 1 == info.getNextVotes().size()) { // 判断是否全部投票

                }
                break;
        }
    }

}