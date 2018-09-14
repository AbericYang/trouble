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

import cn.aberic.bother.entity.BeanProtoFormat;
import cn.aberic.bother.entity.enums.ConnectStatus;
import cn.aberic.bother.entity.proto.consensus.ConnectSelfProto;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;

/**
 * 连接信息对象，有且仅有一个实例
 * <p>
 * 作者：Aberic on 2018/9/12 22:23
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
@ToString
public class ConnectSelf implements BeanProtoFormat {

    /** 自身节点等级，等级0表示未成为任何小组Leader，1表示是一个小组Leader，2表示51个小组Leader，以此类推 */
    private int level;
    /** 当前连接小组集合 */
    private List<GroupInfo> groups;

    private static ConnectSelf instance;

    public static ConnectSelf obtain() {
        if (null == instance) {
            synchronized (GroupInfo.class) {
                if (null == instance) {
                    instance = new ConnectSelf();
                }
            }
        }
        return instance;
    }

    public ConnectSelf() {
        GroupInfo info = new GroupInfo();
        info.setLeaderAddress("");
        info.setNextLeaderAddress("");
        info.setStatus(ConnectStatus.NONE);
        info.setTimestamp(0);
        info.setAddresses(new LinkedList<>());
        groups = new LinkedList<>();
        groups.add(info);
    }

    /**
     * 初始化外部传来的连接信息对象
     *
     * @param connectSelf 连接信息对象
     */
    public void init(ConnectSelf connectSelf) {
        instance = connectSelf;
        setLevel(0);
        getGroups().get(0).setStatus(ConnectStatus.FOLLOW);
    }

    /**
     * ConnectSelf 对象转成 {@link cn.aberic.bother.entity.proto.consensus.ConnectSelfProto.ConnectSelf} 字节流
     *
     * @return proto 字节流
     */
    @Override
    public byte[] bean2ProtoByteArray() {
        ConnectSelfProto.ConnectSelf.Builder builder = ConnectSelfProto.ConnectSelf.newBuilder();
        String selfJsonFormat = this.toJsonString();
        log.debug("selfJsonFormat = {}", selfJsonFormat);
        try {
            JsonFormat.parser().merge(selfJsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return builder.build().toByteArray();
    }
}
