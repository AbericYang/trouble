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
import cn.aberic.bother.entity.consensus.JoinFeedback;
import cn.aberic.bother.entity.consensus.JoinNode;
import cn.aberic.bother.entity.enums.JoinLevel;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.proto.consensus.ConnectSelfProto;
import cn.aberic.bother.entity.proto.consensus.JoinFeedbackProto;
import cn.aberic.bother.entity.proto.consensus.JoinNodeProto;
import cn.aberic.bother.io.IOContext;
import cn.aberic.bother.tools.MsgPackTool;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

/**
 * 应答加入新节点消息业务处理接口
 * <p>
 * version 1.0
 * 当前版本是请求加入的新节点自行不断遍历所有网络中的楼来询问加入
 * 直到询问到尽头依旧没有找到可加入楼的时候
 * 会自行创建一个新楼来匹配到目前的网络环境中
 * <p>
 * 作者：Aberic on 2018/09/14 10:49
 * 邮箱：abericyang@gmail.com
 */
public interface IMsgJoinService extends IMsgRequestService {

    /**
     * 应答加入新节点消息业务处理方案，由{@link IMsgReceiveService}继承并启用该方案
     *
     * @param channel 当前指定通道
     * @param msgData 协议消息对象
     */
    default void join(Channel channel, MessageData msgData) throws InvalidProtocolBufferException {
        log().debug("请求协议：{}，数据ID：{}", msgData.getProtocol().getB(), msgData.getDataId());
        String address = channel.remoteAddress().toString().split(":")[0].split("/")[1];
        switch (msgData.getProtocol()) {
            case JOIN: // 加入新节点协议，follow节点收到新节点加入通知后，发送此协议告知leader节点有新节点加入请求，leader节点直接处理该协议-0x01
                log().debug("接收加入新节点[{}]协议，执行加入方案", address);
                // 如果是孤岛节点
                if ((ConnectSelf.obtain().getLevel() == 0 && ConnectSelf.obtain().getGroups().get(0).getAddresses().size() == 0) ||
                        // 或当前节点是本楼Leader节点且当前楼住户未满
                        (ConnectSelf.obtain().getLevel() == 1 && !ConnectSelf.obtain().getGroups().get(0).max())) {
                    pushJoinAccept(channel); // 告知新的接入地址可加入协议
                    pushAddNode(new JoinNode(address, JoinLevel.TOWER)); // 通知所有楼中住户有新节点加入
                    ConnectSelf.obtain().getGroups().get(0).add(address); // 有且仅有楼来执行加入
                } else {
                    // 执行其它应答加入新节点消息业务处理方案——请求加入
                    joinExec(channel, msgData);
                }
                break;
            case JOIN_ACCEPT: // 告知新的接入地址可加入协议-0x05
                log().debug("告知新的接入地址可加入协议，执行楼同步操作");
                ConnectSelfProto.ConnectSelf connectSelfProto = ConnectSelfProto.ConnectSelf.parseFrom(msgData.getBytes());
                String jsonObject = JsonFormat.printer().print(connectSelfProto);
                ConnectSelf.obtain().init(new Gson().fromJson(jsonObject, ConnectSelf.class));
                log().debug("connectSelf = {}", ConnectSelf.obtain().toJsonString());
                // 遍历作为客户端的请求并全部关闭
                IOContext.obtain().closeClient(address);
                // 如果新加入楼之前也是孤岛入住，则发起Leader选举
                if (StringUtils.isEmpty(ConnectSelf.obtain().getGroups().get(0).getLeaderAddress())) {
                    sendElection(address, ConnectSelf.obtain().getGroups().get(0).election());
                }
                break;
            case JOIN_FEEDBACK: // 告知新的接入节点反馈协议-0x06
                JoinFeedbackProto.JoinFeedback joinFeedbackProto = JoinFeedbackProto.JoinFeedback.parseFrom(msgData.getBytes());
                jsonObject = JsonFormat.printer().print(joinFeedbackProto);
                JoinFeedback joinFeedback = new Gson().fromJson(jsonObject, JoinFeedback.class);
                log().debug("joinFeedback = {}", joinFeedback.toJsonString());
                joinFeedbackExec(joinFeedback);
                break;
            case ADD_NODE: // 由leader节点发出新增小组节点协议-0x07
                JoinNodeProto.JoinNode joinNodeProto = JoinNodeProto.JoinNode.parseFrom(msgData.getBytes());
                jsonObject = JsonFormat.printer().print(joinNodeProto);
                JoinNode joinNode = new Gson().fromJson(jsonObject, JoinNode.class);
                log().debug("joinNode = {}", joinNode.toJsonString());
                log().debug("接收由leader节点发出新增小组节点{}协议，加入级别{}", joinNode.getAddress(), joinNode.getLevel());
                ConnectSelf.obtain().getGroups().get(joinNode.getLevel().getLevel()).add(address); // 有且仅有楼来执行加入
                break;
            case UPGRADE_NODE: // 由leader节点发出更新小组节点集合协议-0x08
                break;
        }
    }

    /**
     * 应答加入新节点消息业务处理方案——请求加入
     *
     * @param channel 当前指定通道
     * @param msgData 协议消息对象
     */
    default void joinExec(Channel channel, MessageData msgData) {
        // 请求加入节点协议为孤岛，如果不是孤岛节点且自己不是楼Leader节点则返回当前楼Leader
        if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.LONELY.getAlias())) {
            pushJoinFeedback(channel, JoinLevel.LONELY, ConnectSelf.obtain().getGroups().get(0));
            // 请求加入节点协议为楼
        } else if ((StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.TOWER.getAlias()) ||
                StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.TOWER_NO_EXEC.getAlias()))
                // 且当前楼有上层社区节点，返回当前社区群组
                && ConnectSelf.obtain().getGroups().size() > 1) {
            if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.TOWER.getAlias())) {
                pushJoinFeedback(channel, JoinLevel.TOWER, ConnectSelf.obtain().getGroups().get(1));
            }
            // 请求加入节点协议为社区
        } else if ((StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.COMMUNITY.getAlias()) ||
                StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.COMMUNITY_NO_EXEC.getAlias()))
                // 且当前社区有上层县区节点，返回当前县区群组
                && ConnectSelf.obtain().getGroups().size() > 2) {
            if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.COMMUNITY.getAlias())) {
                pushJoinFeedback(channel, JoinLevel.COMMUNITY, ConnectSelf.obtain().getGroups().get(2));
            } else {
                pushJoinFeedback(channel, JoinLevel.COMMUNITY_NO_EXEC, ConnectSelf.obtain().getGroups().get(2));
            }
            // 请求加入节点协议为县区，返回当前社区Leader集合以及市Leader
        } else if ((StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.COUNTY.getAlias()) ||
                StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.COUNTY_NO_EXEC.getAlias()))
                // 且当前县区有上层市节点，返回当前市群组
                && ConnectSelf.obtain().getGroups().size() > 3) {
            if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.COUNTY.getAlias())) {
                pushJoinFeedback(channel, JoinLevel.COUNTY, ConnectSelf.obtain().getGroups().get(3));
            } else {
                pushJoinFeedback(channel, JoinLevel.COUNTY_NO_EXEC, ConnectSelf.obtain().getGroups().get(3));
            }
            // 请求加入节点协议为市
        } else if ((StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.CITY.getAlias()) ||
                StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.CITY_NO_EXEC.getAlias()))
                // 且当前市有上层省节点，返回当前省群组
                && ConnectSelf.obtain().getGroups().size() > 4) {
            if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.CITY.getAlias())) {
                pushJoinFeedback(channel, JoinLevel.CITY, ConnectSelf.obtain().getGroups().get(4));
            } else {
                pushJoinFeedback(channel, JoinLevel.CITY_NO_EXEC, ConnectSelf.obtain().getGroups().get(4));
            }
            // 请求加入节点协议为省
        } else if ((StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.PROVINCE.getAlias()) ||
                StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.PROVINCE_NO_EXEC.getAlias()))
                // 且当前省有上层国节点，返回当前国群组
                && ConnectSelf.obtain().getGroups().size() > 5) {
            if (StringUtils.equals(MsgPackTool.bytes2String(msgData.getBytes()), JoinLevel.PROVINCE.getAlias())) {
                pushJoinFeedback(channel, JoinLevel.PROVINCE, ConnectSelf.obtain().getGroups().get(5));
            } else {
                pushJoinFeedback(channel, JoinLevel.PROVINCE_NO_EXEC, ConnectSelf.obtain().getGroups().get(5));
            }
        } else {
            // 在当前channel下发送当前没有可加入小组，自建小组并参与小组间选举协议
            pushCreateGroup(channel);
        }
    }

    /**
     * 应答加入新节点消息业务处理方案——请求加入应答
     *
     * @param joinFeedback 告知新的接入节点反馈协议对象
     */
    default void joinFeedbackExec(JoinFeedback joinFeedback) {
        switch (joinFeedback.getLevel()) {
            case LONELY: // 孤岛，得到楼Leader
                // 向获取到的楼Leader发起加入申请
                sendJoin(joinFeedback.getAddress(), JoinLevel.TOWER);
                break;
            case TOWER: // 楼，得到社区Leader及该社区下楼集合
                // 向获取到的社区Leader发起加入申请
                sendJoin(joinFeedback.getAddress(), JoinLevel.COMMUNITY);
                break;
            case COMMUNITY: // 社区，得到县城Leader及该县城下社区集合
            case COMMUNITY_NO_EXEC: // 社区，得到县城Leader及该县城下社区集合，但不再向县城Leader发起加入申请
                // 遍历社区下所有的楼，看是否可以加入其中之一
                for (String address : joinFeedback.getAddresses()) {
                    sendJoin(address, JoinLevel.TOWER_NO_EXEC);
                }
                if (joinFeedback.getLevel() == JoinLevel.COMMUNITY) {
                    // 向获取到的县城Leader发起加入申请
                    sendJoin(joinFeedback.getAddress(), JoinLevel.COUNTY);
                }
                break;
            case COUNTY: // 县城，得到市Leader及该市下县城集合
            case COUNTY_NO_EXEC: // 县城，得到市Leader及该市下县城集合，但不再向市Leader发起加入申请
                // 遍历县城下所有的社区
                for (String address : joinFeedback.getAddresses()) {
                    sendJoin(address, JoinLevel.COMMUNITY_NO_EXEC);
                }
                if (joinFeedback.getLevel() == JoinLevel.COUNTY) {
                    // 向获取到的市Leader发起加入申请
                    sendJoin(joinFeedback.getAddress(), JoinLevel.CITY);
                }
                break;
            case CITY: // 市，得到省Leader及该省下市集合
            case CITY_NO_EXEC: // 市，得到省Leader及该省下市集合，但不再向省Leader发起加入申请
                // 遍历市下所有的县城
                for (String address : joinFeedback.getAddresses()) {
                    sendJoin(address, JoinLevel.COUNTY_NO_EXEC);
                }
                if (joinFeedback.getLevel() == JoinLevel.CITY) {
                    // 向获取到的省Leader发起加入申请
                    sendJoin(joinFeedback.getAddress(), JoinLevel.PROVINCE);
                }
                break;
            case PROVINCE: // 省，得到国Leader及该国下省集合
            case PROVINCE_NO_EXEC: // 省，得到国Leader及该国下省集合，但不再向国Leader发起加入申请
                // 遍历市下所有的县城
                for (String address : joinFeedback.getAddresses()) {
                    sendJoin(address, JoinLevel.CITY_NO_EXEC);
                }
                if (joinFeedback.getLevel() == JoinLevel.PROVINCE) {
                    // 向获取到的国Leader发起加入申请，到市级就已经存在6765201个节点了，理论上不会节点个数不会超出县城范围
                    // sendJoin(joinFeedback.getAddress(), JoinLevel.COUNTRY);
                }
                break;
        }
    }

}
