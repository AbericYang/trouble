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

package cn.aberic.bother.tools;

import java.io.IOException;

/**
 * 作者：Aberic on 2018/08/29 13:54
 * 邮箱：abericyang@gmail.com
 */
public class DeflaterTest {

    public static void main(String[] args) {
        deflaterTest("{\"b\":{\"c\":10,\"t\":[{\"c\":\"haha0\",\"e\":\"error message 0\",\"h\":\"d54f38cae95de3dc784165c839ba679e99799f8d58ac2eb16b21b38221668047\",\"rw\":[{\"r\":{\"c\":\"contract_900\",\"n\":0,\"s\":[\"9\",\"0\",\"0\"],\"v\":\"v_900\"},\"w\":{\"c\":\"contract_900\",\"n\":0,\"s\":[\"9\",\"0\",\"0\"],\"v\":\"v_900\"}},{\"r\":{\"c\":\"contract_901\",\"n\":1,\"s\":[\"9\",\"0\",\"1\"],\"v\":\"v_901\"},\"w\":{\"c\":\"contract_901\",\"n\":1,\"s\":[\"9\",\"0\",\"1\"],\"v\":\"v_901\"}},{\"r\":{\"c\":\"contract_902\",\"n\":2,\"s\":[\"9\",\"0\",\"2\"],\"v\":\"v_902\"},\"w\":{\"c\":\"contract_902\",\"n\":2,\"s\":[\"9\",\"0\",\"2\"],\"v\":\"v_902\"}}],\"s\":\"sign 0\",\"t\":1535522384421,\"ts\":200},{\"c\":\"haha1\",\"e\":\"error message 1\",\"h\":\"5e9515ae13a2e37fbac8ea7fb83e02406ba4b282fdcc3550c1920172e94f260c\",\"rw\":[{\"r\":{\"c\":\"contract_910\",\"n\":1,\"s\":[\"9\",\"1\",\"0\"],\"v\":\"v_910\"},\"w\":{\"c\":\"contract_910\",\"n\":1,\"s\":[\"9\",\"1\",\"0\"],\"v\":\"v_910\"}},{\"r\":{\"c\":\"contract_911\",\"n\":2,\"s\":[\"9\",\"1\",\"1\"],\"v\":\"v_911\"},\"w\":{\"c\":\"contract_911\",\"n\":2,\"s\":[\"9\",\"1\",\"1\"],\"v\":\"v_911\"}},{\"r\":{\"c\":\"contract_912\",\"n\":3,\"s\":[\"9\",\"1\",\"2\"],\"v\":\"v_912\"},\"w\":{\"c\":\"contract_912\",\"n\":3,\"s\":[\"9\",\"1\",\"2\"],\"v\":\"v_912\"}}],\"s\":\"sign 1\",\"t\":1535522384422,\"ts\":200},{\"c\":\"haha2\",\"e\":\"error message 2\",\"h\":\"a8c5c43b69b98dd9dee63c6e799f74cc058eabef571856cbd6d48a8d31b955e7\",\"rw\":[{\"r\":{\"c\":\"contract_920\",\"n\":2,\"s\":[\"9\",\"2\",\"0\"],\"v\":\"v_920\"},\"w\":{\"c\":\"contract_920\",\"n\":2,\"s\":[\"9\",\"2\",\"0\"],\"v\":\"v_920\"}},{\"r\":{\"c\":\"contract_921\",\"n\":3,\"s\":[\"9\",\"2\",\"1\"],\"v\":\"v_921\"},\"w\":{\"c\":\"contract_921\",\"n\":3,\"s\":[\"9\",\"2\",\"1\"],\"v\":\"v_921\"}},{\"r\":{\"c\":\"contract_922\",\"n\":4,\"s\":[\"9\",\"2\",\"2\"],\"v\":\"v_922\"},\"w\":{\"c\":\"contract_922\",\"n\":4,\"s\":[\"9\",\"2\",\"2\"],\"v\":\"v_922\"}}],\"s\":\"sign 2\",\"t\":1535522384422,\"ts\":200},{\"c\":\"haha3\",\"e\":\"error message 3\",\"h\":\"3f5b6ab705a0078ae638e16ac663d3a7ac46f3e677847b8a0696b34bfe35b4ad\",\"rw\":[{\"r\":{\"c\":\"contract_930\",\"n\":3,\"s\":[\"9\",\"3\",\"0\"],\"v\":\"v_930\"},\"w\":{\"c\":\"contract_930\",\"n\":3,\"s\":[\"9\",\"3\",\"0\"],\"v\":\"v_930\"}},{\"r\":{\"c\":\"contract_931\",\"n\":4,\"s\":[\"9\",\"3\",\"1\"],\"v\":\"v_931\"},\"w\":{\"c\":\"contract_931\",\"n\":4,\"s\":[\"9\",\"3\",\"1\"],\"v\":\"v_931\"}},{\"r\":{\"c\":\"contract_932\",\"n\":5,\"s\":[\"9\",\"3\",\"2\"],\"v\":\"v_932\"},\"w\":{\"c\":\"contract_932\",\"n\":5,\"s\":[\"9\",\"3\",\"2\"],\"v\":\"v_932\"}}],\"s\":\"sign 3\",\"t\":1535522384422,\"ts\":200},{\"c\":\"haha4\",\"e\":\"error message 4\",\"h\":\"87bc139becf861f1f3469fa8315ea0851bbc436928ec7716a36a1dcf639192f5\",\"rw\":[{\"r\":{\"c\":\"contract_940\",\"n\":4,\"s\":[\"9\",\"4\",\"0\"],\"v\":\"v_940\"},\"w\":{\"c\":\"contract_940\",\"n\":4,\"s\":[\"9\",\"4\",\"0\"],\"v\":\"v_940\"}},{\"r\":{\"c\":\"contract_941\",\"n\":5,\"s\":[\"9\",\"4\",\"1\"],\"v\":\"v_941\"},\"w\":{\"c\":\"contract_941\",\"n\":5,\"s\":[\"9\",\"4\",\"1\"],\"v\":\"v_941\"}},{\"r\":{\"c\":\"contract_942\",\"n\":6,\"s\":[\"9\",\"4\",\"2\"],\"v\":\"v_942\"},\"w\":{\"c\":\"contract_942\",\"n\":6,\"s\":[\"9\",\"4\",\"2\"],\"v\":\"v_942\"}}],\"s\":\"sign 4\",\"t\":1535522384422,\"ts\":200},{\"c\":\"haha5\",\"e\":\"error message 5\",\"h\":\"0b5f6bed04258fa6a2e671ba61a80c03cae19d911c1f11e131028541b81c6e1e\",\"rw\":[{\"r\":{\"c\":\"contract_950\",\"n\":5,\"s\":[\"9\",\"5\",\"0\"],\"v\":\"v_950\"},\"w\":{\"c\":\"contract_950\",\"n\":5,\"s\":[\"9\",\"5\",\"0\"],\"v\":\"v_950\"}},{\"r\":{\"c\":\"contract_951\",\"n\":6,\"s\":[\"9\",\"5\",\"1\"],\"v\":\"v_951\"},\"w\":{\"c\":\"contract_951\",\"n\":6,\"s\":[\"9\",\"5\",\"1\"],\"v\":\"v_951\"}},{\"r\":{\"c\":\"contract_952\",\"n\":7,\"s\":[\"9\",\"5\",\"2\"],\"v\":\"v_952\"},\"w\":{\"c\":\"contract_952\",\"n\":7,\"s\":[\"9\",\"5\",\"2\"],\"v\":\"v_952\"}}],\"s\":\"sign 5\",\"t\":1535522384422,\"ts\":200},{\"c\":\"haha6\",\"e\":\"error message 6\",\"h\":\"9492009646507a69b950bc1335e38dc2f825ea298551d6fa22581450079410b3\",\"rw\":[{\"r\":{\"c\":\"contract_960\",\"n\":6,\"s\":[\"9\",\"6\",\"0\"],\"v\":\"v_960\"},\"w\":{\"c\":\"contract_960\",\"n\":6,\"s\":[\"9\",\"6\",\"0\"],\"v\":\"v_960\"}},{\"r\":{\"c\":\"contract_961\",\"n\":7,\"s\":[\"9\",\"6\",\"1\"],\"v\":\"v_961\"},\"w\":{\"c\":\"contract_961\",\"n\":7,\"s\":[\"9\",\"6\",\"1\"],\"v\":\"v_961\"}},{\"r\":{\"c\":\"contract_962\",\"n\":8,\"s\":[\"9\",\"6\",\"2\"],\"v\":\"v_962\"},\"w\":{\"c\":\"contract_962\",\"n\":8,\"s\":[\"9\",\"6\",\"2\"],\"v\":\"v_962\"}}],\"s\":\"sign 6\",\"t\":1535522384423,\"ts\":200},{\"c\":\"haha7\",\"e\":\"error message 7\",\"h\":\"71ca2359cd4b2bba0ebf6c225aa6fec8a63dc18177f47df083f3f0461d9b954d\",\"rw\":[{\"r\":{\"c\":\"contract_970\",\"n\":7,\"s\":[\"9\",\"7\",\"0\"],\"v\":\"v_970\"},\"w\":{\"c\":\"contract_970\",\"n\":7,\"s\":[\"9\",\"7\",\"0\"],\"v\":\"v_970\"}},{\"r\":{\"c\":\"contract_971\",\"n\":8,\"s\":[\"9\",\"7\",\"1\"],\"v\":\"v_971\"},\"w\":{\"c\":\"contract_971\",\"n\":8,\"s\":[\"9\",\"7\",\"1\"],\"v\":\"v_971\"}},{\"r\":{\"c\":\"contract_972\",\"n\":9,\"s\":[\"9\",\"7\",\"2\"],\"v\":\"v_972\"},\"w\":{\"c\":\"contract_972\",\"n\":9,\"s\":[\"9\",\"7\",\"2\"],\"v\":\"v_972\"}}],\"s\":\"sign 7\",\"t\":1535522384423,\"ts\":200},{\"c\":\"haha8\",\"e\":\"error message 8\",\"h\":\"4d33c7192f6738a86ba9d866bb5ce1b57a5f22d2dbf496d8f2f21e69132dd6ec\",\"rw\":[{\"r\":{\"c\":\"contract_980\",\"n\":8,\"s\":[\"9\",\"8\",\"0\"],\"v\":\"v_980\"},\"w\":{\"c\":\"contract_980\",\"n\":8,\"s\":[\"9\",\"8\",\"0\"],\"v\":\"v_980\"}},{\"r\":{\"c\":\"contract_981\",\"n\":9,\"s\":[\"9\",\"8\",\"1\"],\"v\":\"v_981\"},\"w\":{\"c\":\"contract_981\",\"n\":9,\"s\":[\"9\",\"8\",\"1\"],\"v\":\"v_981\"}},{\"r\":{\"c\":\"contract_982\",\"n\":10,\"s\":[\"9\",\"8\",\"2\"],\"v\":\"v_982\"},\"w\":{\"c\":\"contract_982\",\"n\":10,\"s\":[\"9\",\"8\",\"2\"],\"v\":\"v_982\"}}],\"s\":\"sign 8\",\"t\":1535522384423,\"ts\":200},{\"c\":\"haha9\",\"e\":\"error message 9\",\"h\":\"51f837da717cac423fa02ec1b46e08c796c036f76a212ed33350897dd9e50e22\",\"rw\":[{\"r\":{\"c\":\"contract_990\",\"n\":9,\"s\":[\"9\",\"9\",\"0\"],\"v\":\"v_990\"},\"w\":{\"c\":\"contract_990\",\"n\":9,\"s\":[\"9\",\"9\",\"0\"],\"v\":\"v_990\"}},{\"r\":{\"c\":\"contract_991\",\"n\":10,\"s\":[\"9\",\"9\",\"1\"],\"v\":\"v_991\"},\"w\":{\"c\":\"contract_991\",\"n\":10,\"s\":[\"9\",\"9\",\"1\"],\"v\":\"v_991\"}},{\"r\":{\"c\":\"contract_992\",\"n\":11,\"s\":[\"9\",\"9\",\"2\"],\"v\":\"v_992\"},\"w\":{\"c\":\"contract_992\",\"n\":11,\"s\":[\"9\",\"9\",\"2\"],\"v\":\"v_992\"}}],\"s\":\"sign 9\",\"t\":1535522384423,\"ts\":200}]},\"h\":{\"c\":\"d43f5694d44481f1d7282b2d7c8478759b19ce29737a266cc0f8329944f5d8f3\",\"h\":9,\"nc\":120,\"p\":\"cccaaa57e3b63cb6c704a353d506cb29b8124e39c9e08b40a80cff72e759d307\",\"s\":true,\"t\":1535522384421}}");
    }

    private static void deflaterTest(String str) {
        try {
            SystemOut.println("str1 = " + str);
            String str2 = DeflaterTool.compress(str);
            SystemOut.println("str2 = " + str2);
            String str3 = DeflaterTool.uncompress(str2);
            SystemOut.println("str3 = " + str3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
