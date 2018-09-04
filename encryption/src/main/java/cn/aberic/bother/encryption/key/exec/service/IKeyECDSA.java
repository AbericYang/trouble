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

package cn.aberic.bother.encryption.key.exec.service;

import cn.aberic.bother.encryption.key.ecc.ECDSAEncrypt;
import cn.aberic.bother.encryption.key.ecc.ECDSASignature;

import javax.annotation.Nullable;

/**
 * 作者：Aberic on 2018/09/04 16:41
 * 邮箱：abericyang@gmail.com
 */
public interface IKeyECDSA {

    ECDSAEncrypt getECDSAEncrypt();

    ECDSASignature getECDSASignature();

    /**
     * ECDSA
     * <p>
     * 使用私钥文件来执行对info内容的解密操作
     *
     * @param filePath 私钥文件所在路径（如：xx/xx/privateECDSA.key）
     * @param info     待解密内容
     * @return 解密后字符串
     */
    default String decryptPriFileECDSA(String filePath, String info) {
        return getECDSAEncrypt().decryptPriFile(filePath, info);
    }

    /**
     * 使用私钥字符串信息来执行对info内容的解密操作
     *
     * @param keyStr 私钥字符串信息
     * @param info   待解密内容
     * @return 解密后字符串
     */
    default String decryptPriStrECDSA(String keyStr, String info) {
        return getECDSAEncrypt().decryptPriStr(keyStr, info);
    }

    /**
     * 使用公钥文件来执行对info内容的加解密操作
     *
     * @param filePath 公钥文件所在路径（如：xx/xx/publicECDSA.key）
     * @param info     待加解密内容
     * @return 加解密后字符串
     */
    default String encryptByFileECDSA(String filePath, String info) {
        return getECDSAEncrypt().encryptByFile(filePath, info);
    }

    /**
     * 使用公钥字符串信息来执行对info内容的加密操作
     *
     * @param keyStr 公钥字符串信息
     * @param info   待加密内容
     * @return 加密后字符串
     */
    default String encryptByStrECDSA(String keyStr, String info) {
        return getECDSAEncrypt().encryptByStr(keyStr, info);
    }


    /**
     * ECDSA私钥签名字符串
     *
     * @param info     待签名字符串
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @return 签名值
     */
    default String signByFileECDSA(String info, String filePath) {
        return getECDSASignature().signByFile(info, filePath);
    }

    /**
     * ECDSA私钥签名字符串
     *
     * @param info     待签名字符串
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @param encode   编码
     * @return 签名值
     */
    default String signByFileECDSA(String info, String filePath, @Nullable String encode) {
        return getECDSASignature().signByFile(info, filePath, encode);
    }

    /**
     * ECDSA私钥签名字符串
     *
     * @param info          待签名字符串
     * @param privateKeyStr 私钥字符串内容
     * @return 签名值
     */
    default String signByStrECDSA(String info, String privateKeyStr) {
        return getECDSASignature().signByStr(info, privateKeyStr);
    }

    /**
     * ECDSA私钥签名字符串
     *
     * @param info          待签名字符串
     * @param privateKeyStr 私钥字符串内容
     * @param encode        编码
     * @return 签名值
     */
    default String signByStrECDSA(String info, String privateKeyStr, @Nullable String encode) {
        return getECDSASignature().signByStr(info, privateKeyStr, encode);
    }

    /**
     * ECDSA公钥签名校验
     *
     * @param info     待签名字符串
     * @param sign     签名值
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @return 校验结果
     */
    default boolean verifyByFileECDSA(String info, String sign, String filePath) {
        return getECDSASignature().verifyByFile(info, sign, filePath);
    }

    /**
     * ECDSA公钥签名校验
     *
     * @param info     待签名字符串
     * @param sign     签名值
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @return 校验结果
     */
    default boolean verifyByFileECDSA(String info, String sign, String filePath, @Nullable String encode) {
        return getECDSASignature().verifyByFile(info, sign, filePath, encode);
    }

    /**
     * ECDSA公钥签名校验
     *
     * @param info         待签名字符串
     * @param sign         签名值
     * @param publicKeyStr 公钥字符串内容
     * @return 校验结果
     */
    default boolean verifyByStrECDSA(String info, String sign, String publicKeyStr) {
        return getECDSASignature().verifyByStr(info, sign, publicKeyStr);
    }

    /**
     * ECDSA公钥签名校验
     *
     * @param info         待签名字符串
     * @param sign         签名值
     * @param publicKeyStr 公钥字符串内容
     * @param encode       编码
     * @return 校验结果
     */
    default boolean verifyByStrECDSA(String info, String sign, String publicKeyStr, @Nullable String encode) {
        return getECDSASignature().verifyByStr(info, sign, publicKeyStr, encode);
    }

}
