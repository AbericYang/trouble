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

package cn.aberic.bother.io;

import cn.aberic.bother.entity.BeanProtoFormat;
import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.io.Remote;
import cn.aberic.bother.entity.node.Node;
import cn.aberic.bother.entity.node.NodeBase;
import cn.aberic.bother.io.exec.client.EchoClient;
import cn.aberic.bother.io.exec.factory.*;
import cn.aberic.bother.io.exec.server.EchoServer;
import cn.aberic.bother.tools.Constant;
import cn.aberic.bother.tools.MsgPackTool;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * IO网络事务
 * <p>
 * 作者：Aberic on 2018/9/9 21:34
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class IOContext {

    /** 作为服务端所接收到的链接集合 */
    private Cache<String, IOServer> ioServerCache;
    /** 作为客户端所接收到的链接集合 */
    private Cache<String, IOClient> ioClientCache;
    private IOFactory ioServerFactory;
    private IOFactory ioClientFactory;

    private static IOContext instance;

    public static IOContext obtain() {
        if (null == instance) {
            synchronized (IOContext.class) {
                if (null == instance) {
                    instance = new IOContext();
                }
            }
        }
        return instance;
    }

    private IOContext() {
        ioServerCache = CacheBuilder.newBuilder().maximumSize(10).expireAfterAccess(15, TimeUnit.MINUTES).build();
        ioClientCache = CacheBuilder.newBuilder().maximumSize(10).expireAfterAccess(15, TimeUnit.MINUTES).build();
        ioServerFactory = new IONettyServerFactory();
        ioClientFactory = new IONettyClientFactory();
    }

    /** 根据端口号启动监听服务 */
    public void start() {
        try {
            EchoServer.obtain().start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据远程地址对象启动客户端
     *
     * @param remote 远程地址对象
     */
    public void startClient(Remote remote) throws Exception {
        log.info("向服务端发起链接 address = {}", remote.getAddress());
        new EchoClient().createClient(remote);
    }

    public IOServer ioServerGet(String ip) {
        try {
            return (IOServer) ioServerFactory.getOrCreate(new Remote(ip, Constant.NETTY_CONNECT_TIMEOUT_MILLIS));
        } catch (Exception e) {
            return null;
        }
    }

    public void ioServerPut(String ip, IOServer ioServer) {
        log.info("存储客户端链接上下文 {}", ip);
        ioServerCache.put(ip, ioServer);
    }

    public void ioServerRemove(String ip) {
        ioServerCache.invalidate(ip);
    }


    public IOClient ioClientGet(String ip) {
        try {
            return (IOClient) ioClientFactory.getOrCreate(new Remote(ip, Constant.NETTY_CONNECT_TIMEOUT_MILLIS));
        } catch (Exception e) {
            return null;
        }
    }

    public void ioClientPut(String ip, IOClient ioClient) {
        log.info("存储服务端链接上下文 {}", ip);
        ioClientCache.put(ip, ioClient);
    }

    public void ioClientRemove(String ip) {
        ioClientCache.invalidate(ip);
    }

    /**
     * 作为客户端发起请求协议
     *
     * @param address 请求地址
     * @param msgData 请求消息对象
     */
    public void send(String address, MessageData msgData) {
        Objects.requireNonNull(ioClientCache.getIfPresent(address)).send(msgData);
    }

    /**
     * 作为客户端发起请求协议
     *
     * @param address 请求地址
     * @param status  消息协议
     * @param t       请求对象——继承BeanProtoFormat的对象
     */
    public <T extends BeanProtoFormat> void send(String address, ProtocolStatus status, T t) {
        MessageData msgData = new MessageData(status, t.bean2ProtoByteArray());
        send(address, msgData);
    }

    /**
     * 作为服务端推送消息协议
     *
     * @param address 推送地址
     * @param msgData 推送消息对象
     */
    public void push(String address, MessageData msgData) {
        Objects.requireNonNull(ioServerCache.getIfPresent(address)).push(msgData);
    }

    /**
     * 作为客户端发送请求加入协议
     *
     * @param address 申请的服务端地址
     */
    public void join(String address) {
        send(address, ProtocolStatus.JOIN, new NodeBase());
    }

    /**
     * 广播消息
     * 根据当前Server所在智能合约Hash
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param msgData      消息体
     */
    public void broadcast(String contractHash, MessageData msgData) {
        Node.obtain().getNodeAssistMap().get(contractHash).getNodeBases().forEach(nodeBase -> {
            Objects.requireNonNull(ioServerCache.getIfPresent(nodeBase.getAddress())).push(msgData);
        });
    }

    /**
     * 广播消息
     * 根据当前Server所在智能合约Hash
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param status       消息协议
     * @param t            请求对象——继承BeanProtoFormat的对象
     */
    public <T extends BeanProtoFormat> void broadcast(String contractHash, ProtocolStatus status, T t) {
        broadcast(contractHash, new MessageData(status, t.bean2ProtoByteArray()));
    }

    /**
     * 广播空消息
     * 根据当前Server所在智能合约Hash
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param status       消息协议
     */
    public void broadcast(String contractHash, ProtocolStatus status) {
        broadcast(contractHash, new MessageData(status, null));
    }

    /**
     * 广播消息
     * 根据当前Server所在智能合约Hash
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param status       消息协议
     * @param string       字符串消息
     */
    public void broadcast(String contractHash, ProtocolStatus status, String string) {
        broadcast(contractHash, new MessageData(status, MsgPackTool.string2Bytes(string)));
    }

    /**
     * 广播消息
     * 根据当前Server所在智能合约Hash
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param status       消息协议
     * @param stringList   字符串集合消息
     */
    public void broadcast(String contractHash, ProtocolStatus status, List<String> stringList) {
        broadcast(contractHash, new MessageData(status, MsgPackTool.list2Bytes(stringList)));
    }

    /**
     * 全组同步消息
     * 根据当前广播所在智能合约Hash进行消息同步
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param msgData      消息体
     */
    public void sync(String contractHash, MessageData msgData) {
        // 判断是否为辅助节点
        if (Node.obtain().isAssistNode(contractHash)) {
            broadcast(contractHash, msgData);
        } else {
            log.debug("无当前Hash合约竞选节点下的广播权限");
        }
    }

    /**
     * 全组同步消息
     * 根据当前广播所在智能合约Hash进行消息同步
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param status       请求协议
     * @param t            请求对象——继承BeanProtoFormat的对象
     */
    public <T extends BeanProtoFormat> void sync(String contractHash, ProtocolStatus status, T t) {
        sync(contractHash, new MessageData(status, t.bean2ProtoByteArray()));
    }

    /**
     * 全组同步消息
     * 根据当前广播所在智能合约Hash进行消息同步
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param status       消息协议
     */
    public void sync(String contractHash, ProtocolStatus status) {
        sync(contractHash, new MessageData(status, null));
    }

    /**
     * 全组同步消息
     * 根据当前广播所在智能合约Hash进行消息同步
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param status       消息协议
     * @param string       字符串消息
     */
    public void sync(String contractHash, ProtocolStatus status, String string) {
        sync(contractHash, new MessageData(status, MsgPackTool.string2Bytes(string)));
    }

    /**
     * 全组同步消息
     * 根据当前广播所在智能合约Hash进行消息同步
     *
     * @param contractHash 当前广播所在智能合约Hash
     * @param status       消息协议
     * @param stringList   字符串集合消息
     */
    public void sync(String contractHash, ProtocolStatus status, List<String> stringList) {
        sync(contractHash, new MessageData(status, MsgPackTool.list2Bytes(stringList)));
    }

    /**
     * 关闭除了指定地址外地所有作为客户端发起的请求
     *
     * @param address 指定地址
     */
    public void closeClient(String address) {
        ioClientCache.asMap().forEach((ip, ioClient) -> {
            if (!StringUtils.equals(address, ip)) {
                ioClient.shutdown();
            }
        });
    }

}
