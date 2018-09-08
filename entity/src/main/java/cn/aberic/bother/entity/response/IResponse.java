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

package cn.aberic.bother.entity.response;

import cn.aberic.bother.entity.BeanJsonField;
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
    enum ResponseType {
        /** 返回码 200，返回信息 success */
        SUCCESS(200, "success"),
        /** 返回码 8000，返回信息 fail */
        FAIL(8000, "fail"),
        /** 返回码 8001，返回信息 param error */
        PARAM_ERROR(8001, "param error"),
        /** 返回码 8990，返回信息 请求类型不存在 */
        REQUEST_TYPE_NOT_FOUND(8990, "请求类型不存在"),
        /** 返回码 9000，返回信息 账户余额不足 */
        ACCOUNT_LACK_OF_BALANCE(9000, "账户余额不足"),
        /** 返回码 9001，返回信息 账户信息无效 */
        ACCOUNT_INFO_INVALID(9001, "账户信息无效"),
        /** 返回码 9002，返回信息 账户未找到 */
        ACCOUNT_NOT_FOUND(9002, "账户未找到"),
        /** 返回码 9010，返回信息 Token 已存在 */
        TOKEN_IS_ALLREADY_EXIST(9010, "Token 已存在"),
        /** 返回码 9020，返回信息 支票金额不足 */
        CHEQUE_LACK_OF_BALANCE(9020, "支票金额不足"),
        /** 返回码 9021，返回信息 支票无效 */
        CHEQUE_INVALID(9021, "支票无效"),
        /** 返回码 9022，返回信息 支票不在使用有效期内 */
        CHEQUE_OVERDUE(9022, "支票不在使用有效期内"),
        /** 返回码 9030，返回信息 授权余额不足 */
        APPROVE_LACK_OF_BALANCE(9030, "授权余额不足"),
        /** 返回码 9040，返回信息 日期格式错误 */
        DATE_FORMAT_ERROR(9040, "日期格式错误");


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
        ResponseType(int code, String msg) {
            this.msg = msg;
            this.code = code;
        }
    }

    default Response response() {
        return response(ResponseType.SUCCESS);
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
    default Response response(String result) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResponseType.SUCCESS.code);
        return new Response(true, VerifyTool.verifyJSON(jsonObject, result).toString());
    }

    /**
     * 根据传入对象 {@param obj} 返回一个json字符串，此json有两个key，分别是code和data。
     * <p>
     * {@param obj} 直接作为data的值。
     *
     * @param obj 传入对象
     * @return json字符串
     */
    default Response response(Object obj) {
        if (obj instanceof BeanJsonField) {
            return responseBean((BeanJsonField) obj);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ResponseType.SUCCESS.code);
        jsonObject.put("data", obj);
        return new Response(true, jsonObject.toString());
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
    default Response responseBean(BeanJsonField bean) {
        return response(bean.toJsonString());
    }

    /**
     * 通用错误信息返回，返回内容内置且匹配返回码
     *
     * @param responseType 返回枚举对象
     * @return 返回 success json字符串
     */
    default Response response(ResponseType responseType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", responseType.code);
        jsonObject.put("data", responseType.msg);
        return new Response(responseType.code == 200, jsonObject.toString());
    }

    /**
     * 通用错误信息返回，返回内容内置且匹配返回码
     *
     * @param responseType 返回枚举对象
     * @param msg      返回自定义信息
     * @return 返回 success json字符串
     */
    default Response response(ResponseType responseType, String msg) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", responseType.code);
        jsonObject.put("data", msg);
        return new Response(responseType.code == 200, jsonObject.toString());
    }


}
