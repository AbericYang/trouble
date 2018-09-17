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
import cn.aberic.bother.entity.consensus.ConnectSelf;
import cn.aberic.bother.entity.enums.JoinLevel;
import cn.aberic.bother.entity.enums.ProtocolStatus;
import cn.aberic.bother.entity.io.MessageData;
import cn.aberic.bother.entity.io.Remote;
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
        MessageData msgData = new MessageData(ProtocolStatus.JOIN, null);
        Objects.requireNonNull(ioClientCache.getIfPresent(address)).send(msgData);
    }

    /**
     * 广播消息
     * 根据当前Server所在组级别进行消息广播
     *
     * @param msgData 消息体
     * @param level   当前广播所在组级别
     */
    public void broadcast(MessageData msgData, JoinLevel level) {
        ConnectSelf.obtain().getGroups().get(level.getLevel()).getAddresses().forEach(address -> {
            Objects.requireNonNull(ioServerCache.getIfPresent(address)).push(msgData);
        });
    }

    /**
     * 广播消息
     * 根据当前Server所在组级别进行消息广播
     *
     * @param status 消息协议
     * @param t      请求对象——继承BeanProtoFormat的对象
     * @param level  当前广播所在组级别
     */
    public <T extends BeanProtoFormat> void broadcast(ProtocolStatus status, T t, JoinLevel level) {
        broadcast(new MessageData(status, t.bean2ProtoByteArray()), level);
    }

    /**
     * 广播空消息
     * 根据当前Server所在组级别进行消息广播
     *
     * @param status 消息协议
     * @param level  当前广播所在组级别
     */
    public void broadcast(ProtocolStatus status, JoinLevel level) {
        broadcast(new MessageData(status, null), level);
    }

    /**
     * 广播字符串消息
     * 根据当前Server所在组级别进行消息广播
     *
     * @param status 消息协议
     * @param string 字符串消息
     * @param level  当前广播所在组级别
     */
    public void broadcast(ProtocolStatus status, String string, JoinLevel level) {
        broadcast(new MessageData(status, MsgPackTool.string2Bytes(string)), level);
    }

    /**
     * 广播字符串集合消息
     * 根据当前Server所在组级别进行消息广播
     *
     * @param status 消息协议
     * @param stringList 字符串集合消息
     * @param level  当前广播所在组级别
     */
    public void broadcast(ProtocolStatus status, List<String> stringList, JoinLevel level) {
        broadcast(new MessageData(status, MsgPackTool.list2Bytes(stringList)), level);
    }

    /**
     * 全组同步消息
     * 根据当前所在组级别进行消息同步
     *
     * @param msgData 消息体
     * @param level   当前同步所在组级别
     */
    public void sync(MessageData msgData, JoinLevel level) {
        // 如果自身组级别大于当前组级别，则为组Leader节点，执行广播操作
        if (ConnectSelf.obtain().getLevel() > level.getLevel()) {
            broadcast(msgData, level);
        } else { // 否则为Follow节点，执行发送操作
            ConnectSelf.obtain().getGroups().get(level.getLevel()).getAddresses().forEach(address -> {
                Objects.requireNonNull(ioClientCache.getIfPresent(address)).send(msgData);
            });
        }
    }

    /**
     * 全组同步消息
     * 根据当前所在组级别进行消息同步
     *
     * @param status 请求协议
     * @param t      请求对象——继承BeanProtoFormat的对象
     * @param level  当前广播所在组级别
     */
    public <T extends BeanProtoFormat> void sync(ProtocolStatus status, T t, JoinLevel level) {
        sync(new MessageData(status, t.bean2ProtoByteArray()), level);
    }

    /**
     * 全组同步消息
     * 根据当前所在组级别进行消息同步
     *
     * @param status 消息协议
     * @param level  当前同步所在组级别
     */
    public void sync(ProtocolStatus status, JoinLevel level) {
        sync(new MessageData(status, null), level);
    }

    /**
     * 全组同步消息
     * 根据当前所在组级别进行消息同步
     *
     * @param status 消息协议
     * @param string 字符串消息
     * @param level  当前同步所在组级别
     */
    public void sync(ProtocolStatus status, String string, JoinLevel level) {
        sync(new MessageData(status, MsgPackTool.string2Bytes(string)), level);
    }

    /**
     * 全组同步消息
     * 根据当前所在组级别进行消息同步
     *
     * @param status 消息协议
     * @param stringList 字符串集合消息
     * @param level  当前同步所在组级别
     */
    public void sync(ProtocolStatus status, List<String> stringList, JoinLevel level) {
        sync(new MessageData(status, MsgPackTool.list2Bytes(stringList)), level);
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
