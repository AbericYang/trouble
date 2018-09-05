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

import cn.aberic.bother.tools.VerifyTool;
import com.alibaba.fastjson.JSONObject;

/**
 * 返回内容通用接口
 * <p>
 * 作者：Aberic on 2018/08/30 14:08
 * 邮箱：abericyang@gmail.com
 */
public interface IResponse {

    /**
     * 请求结果枚举
     * <p>
     * 作者：Aberic on 2018/09/05 14:04
     * 邮箱：abericyang@gmail.com
     */
    enum Response {
        /** 返回码 200，返回信息 success */
        SUCCESS(200, "success"),
        /** 返回码 9000，返回信息 账户余额不足 */
        ACCOUNT_LACK_OF_BALANCE(9000, "账户余额不足"),
        /** 返回码 9001，返回信息 账户信息无效 */
        ACCOUNT_INFO_INVALID(9001, "账户信息无效");


        /** 交易结果信息 */
        private String msg;
        /** 交易结果码 */
        private int code;

        /**
         * 请求结果
         *
         * @param code 请求结果码
         * @param msg  请求结果信息
         */
        Response(int code, String msg) {
            this.msg = msg;
            this.code = code;
        }
    }

    /**
     * 根据传入字符串 {@param result} 返回一个json字符串，此json有两个key，分别是code和data。
     * <p>
     * data会验证 {@param result} 是否为json对象或json array对象
     * <p>
     * 如果是json对象，则转成json对象后作为data的值
     * <p>
     * 如果是json array对象，则转成json array对象后作为data的值
     * <p>
     * 如果不属于json类型，则直接将此字符串作为data的值。
     *
     * @param result 传入字符串
     * @return json字符串
     */
    default String response(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", Response.SUCCESS.code);
        return VerifyTool.verifyJSON(jsonObject, result).toString();
    }

    /**
     * 根据传入对象 {@param obj} 返回一个json字符串，此json有两个key，分别是code和data。
     * <p>
     * {@param obj} 直接作为data的值。
     *
     * @param obj 传入对象
     * @return json字符串
     */
    default String response(Object obj) {
        if (obj instanceof BeanJsonField) {
            return responseBean((BeanJsonField) obj);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", Response.SUCCESS.code);
        jsonObject.put("data", obj);
        return jsonObject.toString();
    }

    /**
     * 根据传入实现了 {@link BeanJsonField} 接口的对象 {@param bean} 返回一个json字符串
     * <p>
     * 此json有两个key，分别是code和data。
     * <p>
     * {@param bean} 在调用接口方法toJsonString后直接作为data的值。
     *
     * @param bean 实现 BeanJsonField 接口的对象
     * @return json字符串
     */
    default String responseBean(BeanJsonField bean) {
        return response(bean.toJsonString());
    }

    /**
     * 通用错误信息返回，返回内容内置且匹配返回码
     *
     * @param response 返回枚举对象
     * @return 返回 success json字符串
     */
    default String response(Response response) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", response.code);
        jsonObject.put("data", response.msg);
        return jsonObject.toString();
    }


}
