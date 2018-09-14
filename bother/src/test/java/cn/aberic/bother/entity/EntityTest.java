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

package cn.aberic.bother.entity;

import cn.aberic.bother.entity.block.*;
import cn.aberic.bother.entity.consensus.ConnectSelf;
import cn.aberic.bother.entity.consensus.GroupInfo;
import cn.aberic.bother.entity.consensus.JoinFeedback;
import cn.aberic.bother.entity.contract.Account;
import cn.aberic.bother.entity.enums.JoinLevel;
import cn.aberic.bother.entity.enums.TransactionStatus;
import cn.aberic.bother.entity.proto.ProtoDemo;
import cn.aberic.bother.entity.proto.block.BlockHeaderProto;
import cn.aberic.bother.entity.proto.block.BlockProto;
import cn.aberic.bother.entity.proto.consensus.ConnectSelfProto;
import cn.aberic.bother.entity.proto.consensus.JoinFeedbackProto;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 作者：Aberic on 2018/9/11 22:26
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class EntityTest {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        // protoDemo();
        // createBlockProtobuf();
        createConnectSelfProtobuf();
        // createJoinFeedbackProtobuf();
    }

    private static void createBlockProtobuf() {
        BlockProto.Block.Builder builder = BlockProto.Block.newBuilder();
        String blockJsonFormat = new JSONObject(createBlock(0)).toString();
        log.debug("blockJsonFormat = {}", blockJsonFormat);
        try {
            JsonFormat.parser().merge(blockJsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        BlockHeaderProto.BlockHeader header = builder.getHeader();
        log.debug("height = {}", header.getHeight());
        log.debug("currentDataHash = {}", header.getCurrentDataHash());
        log.debug("previousDataHash = {}", header.getPreviousDataHash());
        log.debug("timestamp = {}", header.getTimestamp());
        log.debug("time = {}", header.getTime());
    }

    private static void createConnectSelfProtobuf() throws InvalidProtocolBufferException {
        ConnectSelfProto.ConnectSelf.Builder builder = ConnectSelfProto.ConnectSelf.newBuilder();
        String selfJsonFormat = new JSONObject(createConnectSelf()).toString();
        log.debug("selfJsonFormat = {}", selfJsonFormat);
        try {
            JsonFormat.parser().merge(selfJsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        log.debug("getLevel = {}", builder.getLevel());
        log.debug("getLeaderAddress = {}", builder.getGroups(0).getLeaderAddress());
        log.debug("getNextLeaderAddress = {}", builder.getGroups(0).getNextLeaderAddress());
        log.debug("getTimestamp = {}", builder.getGroups(0).getTimestamp());
        log.debug("getLeaderAddress = {}", builder.getGroups(0).getAddresses(1));
        log.debug("==============================================");
        byte[] bytes = builder.build().toByteArray();
        log.debug("toByteArray = {}", bytes);


        ConnectSelfProto.ConnectSelf selfProto = ConnectSelfProto.ConnectSelf.parseFrom(bytes);
        String jsonObject = JsonFormat.printer().print(selfProto);
        log.debug("jsonObject = {}", jsonObject);
        log.debug("Object = {}", JSON.parseObject(jsonObject, new TypeReference<ConnectSelf>() {}).toJsonString());
    }

    private static void createJoinFeedbackProtobuf() throws InvalidProtocolBufferException {
        JoinFeedbackProto.JoinFeedback.Builder builder = JoinFeedbackProto.JoinFeedback.newBuilder();
        String joinJsonFormat = new JSONObject(createJoinFeedback()).toString();
        log.debug("joinJsonFormat = {}", joinJsonFormat);
        try {
            JsonFormat.parser().merge(joinJsonFormat, builder);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        log.debug("getLevel = {}", builder.getLevel());
        log.debug("getAddress = {}", builder.getAddress());
        log.debug("getAddressesCount = {}", builder.getAddressesCount());
        for (String s : builder.getAddressesList()) {
            log.debug("next() = {}", s);
        }
        log.debug("==============================================");
        byte[] bytes = builder.build().toByteArray();
        log.debug("toByteArray = {}", bytes);


        JoinFeedbackProto.JoinFeedback joinProto = JoinFeedbackProto.JoinFeedback.parseFrom(bytes);
        String jsonObject = JsonFormat.printer().print(joinProto);
        log.debug("jsonObject = {}", jsonObject);
        log.debug("Object = {}", JSON.parseObject(jsonObject, new TypeReference<JoinFeedback>() {}).toJsonString());
    }

    public static byte[] getBlockBytes() {
        return createBlock(0).bean2ProtoByteArray();
    }

    private static void protoDemo() {
        //获取Student对象
        //这里的Student对象构造器被私有化,我们通过Student的内部类Builder来构建builder
        ProtoDemo.Student.Builder builder = ProtoDemo.Student.newBuilder();
        //通过Student的内部类builder提供了构建Student相关属性的set方法
        builder.setId(1);
        builder.setName("凌晨0点0分");
        builder.setEmail("31346337@qq.com");
        builder.setSex(ProtoDemo.Student.Sex.MAN);
        //获取PhoneNumber对象
        ProtoDemo.Student.PhoneNumber.Builder builder1 = ProtoDemo.Student.PhoneNumber.newBuilder();
        builder1.setNumber("13657177663");
        builder1.setType(ProtoDemo.Student.PhoneType.MOBILE);
        ProtoDemo.Student.PhoneNumber pn = builder1.build();
        builder.addPhone(pn);
        //再创建1个PhoneNumber对象
        pn = ProtoDemo.Student.PhoneNumber.newBuilder()
                .setNumber("13581491939").setType(ProtoDemo.Student.PhoneType.HOME).build();
        builder.addPhone(pn);
        //序列化
        ProtoDemo.Student stu = builder.build();
        System.out.println("protobuf数据字符串大小: " + stu.toByteString().size());
        System.out.println("protobuf数据字节数组大小: " + stu.toByteArray().length);
        System.out.println("protobuf数据字节数组大小: " + Arrays.toString(stu.toByteArray()));
        //再将封装有数据的对象实例，转换为字节数组，用于数据传输、存储等
        byte[] stuByte = stu.toByteArray();
        //这里得到了stuBte字节数组后，我们可以将该数据进行数据传输或存储，这里至于用什么技术传输就根据具体情况而定
        //假如这里stuByt通过传输，下面的代码接到了该数据
        //接收方 ,这里为了方便我们就写在一个类里面
        //将字节数据反序列化为对应的对象实例
        ProtoDemo.Student student = null;
        try {
            student = ProtoDemo.Student.parseFrom(stuByte);
            //这里得到了Student实例了，就可以根据需要来操作里面的数据了
            System.out.println("学生ID:" + student.getId());
            System.out.println("姓名：" + student.getName());
            System.out.println("性别：" + (student.getSex().getNumber() == 0 ? "男" : "女"));
            System.out.println("邮箱：" + student.getEmail());
            //遍历phoneNumber字段
            List<ProtoDemo.Student.PhoneNumber> phList = student.getPhoneList();
            for (ProtoDemo.Student.PhoneNumber p : phList) {
                System.out.println(p.getType() + "电话:" + p.getNumber());
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        /*如何快速的进行json格式化*/
        String jsonObject = "";
        try {
            jsonObject = JsonFormat.printer().print(student);
        } catch (InvalidProtocolBufferException e) {
            e.getMessage();
        }
        System.out.println(jsonObject);
        System.out.println("json数据大小: " + jsonObject.getBytes().length);
        System.out.println("fastjson数据大小: " + JSON.toJSONString(jsonObject).getBytes().length);
    }

    public static Block createBlock(int count) {
        BlockHeader header = BlockHeader.newInstance().create();

        BlockBody body = new BlockBody();
        List<Transaction> transactions = new ArrayList<>();
        for (int transactionCount = 0; transactionCount < 10; transactionCount++) {
            Transaction transaction = new Transaction();
            transaction.setCreator(String.format("haha%s", transactionCount));
            transaction.setErrorMessage(String.format("error message %s", transactionCount));
            transaction.setSign(String.format("sign %s", transactionCount));
            transaction.setTransactionStatusCode(TransactionStatus.SUCCESS.getCode());
            transaction.setTimestamp(System.currentTimeMillis());

            RWSet rwSet = new RWSet();
            List<ValueRead> reads = new ArrayList<>();
            List<ValueWrite> writes = new ArrayList<>();
            for (int rwCount = 0; rwCount < 3; rwCount++) {

                ValueRead valueRead = new ValueRead();
                valueRead.setKey(String.valueOf(count));

                ValueWrite valueWrite = new ValueWrite();
                valueWrite.setStrings(new String[]{String.valueOf(count), String.valueOf(transactionCount), String.valueOf(rwCount)});


                reads.add(valueRead);
                writes.add(valueWrite);

            }
            rwSet.setReads(reads);
            rwSet.setWrites(writes);

            transaction.setRwSet(rwSet);
            transaction.setContractName(String.format("contract_%s%s%s", count, transactionCount, reads.size()));
            transaction.setContractVersion(String.format("v_%s%s%s", count, transactionCount, writes.size()));

            transactions.add(transaction);
        }
        body.setTxCount(transactions.size());
        body.setTransactions(transactions);

        return new Block(header, body);
    }

    private static ConnectSelf createConnectSelf() {
        List<String> addresses = new ArrayList<>();
        addresses.add("a");
        addresses.add("b");
        addresses.add("c");

        List<GroupInfo> infoList = new ArrayList<>();
        GroupInfo info = new GroupInfo();
        info.setLeaderAddress("setLeaderAddress");
        info.setNextLeaderAddress("setNextLeaderAddress");
        info.setTimestamp(1111111111111111L);
        info.setAddresses(addresses);
        infoList.add(info);

        ConnectSelf connectSelf = new ConnectSelf();
        connectSelf.setLevel(0);
        connectSelf.setGroups(infoList);
        return connectSelf;
    }

    private static JoinFeedback createJoinFeedback() {
        List<String> addresses = new ArrayList<>();
        addresses.add("a");
        addresses.add("b");
        addresses.add("c");

        JoinFeedback joinFeedback = new JoinFeedback();
        joinFeedback.setAddress("test");
        joinFeedback.setAddresses(addresses);
        joinFeedback.setLevel(JoinLevel.CITY);
        return joinFeedback;
    }

    public static Account createAccount() {
        Account account = new Account();
        account.setCount(new BigDecimal(1));
        account.setTimestamp(438756873L);
        account.setJsonAccountInfoString("jshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhkjshdfkjhsdkhfksdhkjfksdhk");
        account.setPubRSAKey("ksdjflkjsdlkfjsldjflksjlksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkksdjflkjsdlkfjsldjflksjlkjlkkjlk");
        account.setAddress("knmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskdsknmlkmldkkflkfskds");
        account.setPubECCKey("cnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjwcnowjodjwoefjoejfokejofjw");
        return account;
    }

}
