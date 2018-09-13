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

import cn.aberic.bother.entity.block.Block;
import cn.aberic.bother.entity.consensus.ConnectSelf;
import cn.aberic.bother.entity.enums.JoinLevel;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.proto.block.BlockProto;
import cn.aberic.bother.entity.proto.consensus.ConnectSelfProto;
import cn.aberic.bother.tools.MsgPackTool;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

/**
 * 应答消息业务处理接口
 * <p>
 * 作者：Aberic on 2018/09/12 14:13
 * 邮箱：abericyang@gmail.com
 */
interface IMsgReceiveService extends IMsgRequestService {

    default void receive(Channel channel, MessageData msgData) {
        log().debug("请求协议：{}，数据ID：{}", msgData.getProtocol().getB(), msgData.getDataId());
        switch (msgData.getProtocol()) {
            case HEART: // 心跳协议-0x00
                log().debug("接收心跳协议，什么也不做");
                log().debug("接收心跳协议，connectSelf = {}", ConnectSelf.obtain().toJsonString());
                break;
            case JOIN: // 加入新节点协议，follow节点收到新节点加入通知后，发送此协议告知leader节点有新节点加入请求，leader节点直接处理该协议-0x01
                String address = channel.remoteAddress().toString().split(":")[0].split("/")[1];
                log().debug("接收加入新节点[{}]协议，执行加入方案", address);
                // 如果是孤岛节点
                if ((ConnectSelf.obtain().getLevel() == 0 && ConnectSelf.obtain().getGroups().get(0).getAddresses().size() == 0) ||
                        // 或当前节点是本楼Leader节点且当前楼住户未满
                        (ConnectSelf.obtain().getLevel() == 1 && !ConnectSelf.obtain().getGroups().get(0).max())) {
                    pushJoinAccept(channel); // 告知新的接入地址可加入协议
                    pushAddNode(address); // 通知所有住户有新节点加入
                    ConnectSelf.obtain().getGroups().get(0).add(address); // 有且仅有楼来执行加入
                }
                // 请求加入节点协议为孤岛，如果不是孤岛节点且自己不是楼Leader节点则返回当前楼Leader
                if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.LONEY.getAlias())) {
                    // TODO: 2018/9/13
                    // 请求加入节点协议为楼，返回当前社区Leader
                } else if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.TOWER.getAlias())) {
                    // TODO: 2018/9/13
                    // 请求加入节点协议为社区，返回当前楼Leader集合以及县区Leader
                } else if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.COMMUNITY.getAlias())) {
                    // TODO: 2018/9/13
                    // 请求加入节点协议为县区，返回当前社区Leader集合以及市Leader
                } else if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.COUNTY.getAlias())) {
                    // TODO: 2018/9/13
                    // 请求加入节点协议为市，返回当前县区Leader集合以及省Leader
                } else if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.CITY.getAlias())) {
                    // TODO: 2018/9/13
                    // 请求加入节点协议为省，返回当前市Leader集合以及国Leader
                } else if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.PROVINCE.getAlias())) {
                    // TODO: 2018/9/13
                }
                break;
            case JOIN_ACCEPT: // 告知新的接入地址可加入协议-0x05
                log().debug("告知新的接入地址可加入协议，执行楼同步操作");
                try {
                    ConnectSelfProto.ConnectSelf connectSelfProto = ConnectSelfProto.ConnectSelf.parseFrom(msgData.getBytes());
                    String jsonObject = JsonFormat.printer().print(connectSelfProto);
                    ConnectSelf.obtain().init(new Gson().fromJson(jsonObject, ConnectSelf.class));
                    log().debug("connectSelf = {}", ConnectSelf.obtain().toJsonString());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            case ADD_NODE: // 由leader节点发出新增小组节点协议-0x02
                address = MsgPackTool.bytes2String(msgData.getBytes());
                log().debug("接收由leader节点发出新增小组节点{}协议", address);
                ConnectSelf.obtain().getGroups().get(0).add(address); // 有且仅有楼来执行加入
                break;
            case UPGRADE_NODE: // 由leader节点发出更新小组节点集合协议-0x03
                break;
            case BLOCK: // 区块协议-0x51
                log().debug("接收区块协议，执行区块同步操作");
                try {
                    BlockProto.Block blockProto = BlockProto.Block.parseFrom(msgData.getBytes());
                    String jsonObject = JsonFormat.printer().print(blockProto);
                    // log().debug("jsonObject = {}", jsonObject);
                    Block block = new Gson().fromJson(jsonObject, Block.class);
                    log().debug("block = {}", block.toJsonString());
                    // TODO: 2018/9/12 验证并存储
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                break;
            default: // 非法协议请求直接关闭异常节点连入
                log().info("关闭协议异常channel");
                channel.close();
                break;
        }
    }

}
