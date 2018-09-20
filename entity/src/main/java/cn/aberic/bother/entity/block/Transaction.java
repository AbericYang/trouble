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

package cn.aberic.bother.entity.block;

import cn.aberic.bother.encryption.key.exec.KeyExec;
import cn.aberic.bother.entity.BeanProtoFormat;
import cn.aberic.bother.entity.contract.Request;
import cn.aberic.bother.entity.enums.TransactionStatus;
import cn.aberic.bother.entity.proto.block.TransactionProto;
import cn.aberic.bother.tools.DateTool;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * 事务/交易/业务对象——数据操作层-data manipulation
 * <p>
 * 作者：Aberic on 2018/8/20 21:28
 * <p>
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
@Setter
@Getter
@ToString
public class Transaction implements BeanProtoFormat {

    /** 本次写入值链码hash */
    @JSONField(name = "h")
    private String hash;
    /** 本次写入值所用合约名称 */
    @JSONField(name = "n")
    private String contractName;
    /** 本次写入值所用合约版本 */
    @JSONField(name = "v")
    private String contractVersion;
    /** 发起方 */
    @JSONField(name = "c")
    private String creator;
    /** 发起方签名 */
    @JSONField(name = "s")
    private String sign;
    /** 交易读写集 */
    @JSONField(name = "rw")
    private RWSet rwSet;
    /** 交易时间戳 */
    @JSONField(name = "t")
    private Long timestamp;
    /**
     * 交易hash
     * <p>
     * 为creator、sign、JSON.toJSONString(readSet)、JSON.toJSONString(writeSet)及timestamp拼接后md5
     */
    @JSONField(name = "txh")
    private String txHash;
    /** 交易状态 */
    @JSONField(name = "ts")
    private int transactionStatusCode = TransactionStatus.SUCCESS.getCode();
    /** 交易错误信息 */
    @JSONField(name = "e")
    private String errorMessage;
    /** 交易时间戳转字符串——yyyy/MM/dd HH:mm:ss */
    @JSONField(serialize = false)
    private String time; // 序列化时不写入
    /** 智能合约请求对象 */
    @JSONField(name = "r")
    private Request request;
    /** 仅在创建账户时需要赋值验证 */
    @JSONField(name = "p")
    private String pubECCKey;

    public Transaction build(String priECCKey) {
        sign = signStringResult(priECCKey);
        txHash = Hashing.sha256().hashString(String.format("%s%s%s%s",
                creator, sign, JSON.toJSONString(rwSet), timestamp), Charset.forName("UTF-8")).toString();
        return this;
    }

    /**
     * 对交易进行签名
     *
     * @param priECCKey 本次交易请求账户私钥
     * @return 签名结果字符串
     */
    private String signStringResult(String priECCKey) {
        return KeyExec.obtain().signByStrECDSA(signString(), priECCKey, "UTF-8");
    }

    public String signString() {
        return String.format("%s%s%s", creator, rwSet.toString(), timestamp);
    }

    public String getTime() {
        return DateTool.timestampToString(timestamp, "yyyy/MM/dd HH:mm:ss");
    }

    /**
     * Block 对象转成 {@link cn.aberic.bother.entity.proto.block.BlockProto.Block} 字节流
     *
     * @return proto 字节流
     */
    @Override
    public byte[] bean2ProtoByteArray() {
        TransactionProto.Transaction.Builder builder = TransactionProto.Transaction.newBuilder();
        String jsonFormat = this.toJsonString();
        log.debug("jsonFormat = {}", jsonFormat);
        try {
            JsonFormat.parser().merge(jsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return builder.build().toByteArray();
    }

    @Override
    public <M extends GeneratedMessageV3> Transaction proto2Bean(M m) throws InvalidProtocolBufferException {
        String jsonObject = JsonFormat.printer().print(m);
        return new Gson().fromJson(jsonObject, Transaction.class);
    }

    @Override
    public Transaction protoByteArray2Bean(byte[] bytes) throws InvalidProtocolBufferException {
        return proto2Bean(TransactionProto.Transaction.parseFrom(bytes));
    }
}
