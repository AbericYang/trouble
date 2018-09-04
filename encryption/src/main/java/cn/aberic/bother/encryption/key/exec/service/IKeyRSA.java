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

import cn.aberic.bother.encryption.key.rsa.RSAEncrypt;
import cn.aberic.bother.encryption.key.rsa.RSASignature;

import javax.annotation.Nullable;

/**
 * 作者：Aberic on 2018/09/04 16:41
 * 邮箱：abericyang@gmail.com
 */
public interface IKeyRSA {

    RSAEncrypt getRSAEncrypt();

    RSASignature getRSASignature();

    /**
     * 使用公钥文件来执行对info内容的加密操作
     *
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @param info     待加密内容
     * @return 加密后字符串
     */
    default String encryptPubFileRSA(String filePath, String info) {
        return getRSAEncrypt().encryptPubFile(filePath, info);
    }

    /**
     * 使用公钥字符串信息来执行对info内容的加密操作
     *
     * @param keyStr 公钥字符串信息
     * @param info   待加密内容
     * @return 加密后字符串
     */
    default String encryptPubStrRSA(String keyStr, String info) {
        return getRSAEncrypt().encryptPubStr(keyStr, info);
    }

    /**
     * 使用私钥文件来执行对info内容的加密操作
     *
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @param info     待加密内容
     * @return 加密后字符串
     */
    default String encryptPriFileRSA(String filePath, String info) {
        return getRSAEncrypt().encryptPriFile(filePath, info);
    }

    /**
     * 使用私钥字符串信息来执行对info内容的加密操作
     *
     * @param keyStr 私钥字符串信息
     * @param info   待加密内容
     * @return 加密后字符串
     */
    default String encryptPriStrRSA(String keyStr, String info) {
        return getRSAEncrypt().encryptPriStr(keyStr, info);
    }

    /**
     * 使用公钥文件来执行对info内容的解密操作
     *
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @param info     待解密内容
     * @return 解密后字符串
     */
    default String decryptPubFileRSA(String filePath, String info) {
        return getRSAEncrypt().decryptPubFile(filePath, info);
    }

    /**
     * 使用公钥字符串信息来执行对info内容的解密操作
     *
     * @param keyStr 公钥字符串信息
     * @param info   待解密内容
     * @return 解密后字符串
     */
    default String decryptPubStrRSA(String keyStr, String info) {
        return getRSAEncrypt().decryptPubStr(keyStr, info);
    }

    /**
     * 使用私钥文件来执行对info内容的解密操作
     *
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @param info     待解密内容
     * @return 解密后字符串
     */
    default String decryptPriFileRSA(String filePath, String info) {
        return getRSAEncrypt().decryptPriFile(filePath, info);
    }

    /**
     * 使用私钥字符串信息来执行对info内容的解密操作
     *
     * @param keyStr 私钥字符串信息
     * @param info   待解密内容
     * @return 解密后字符串
     */
    default String decryptPriStrRSA(String keyStr, String info) {
        return getRSAEncrypt().decryptPriStr(keyStr, info);
    }

    /**
     * RSA私钥签名字符串
     *
     * @param info     待签名字符串
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @return 签名值
     */
    default String signByFileRSA(String info, String filePath) {
        return getRSASignature().signByFile(info, filePath);
    }

    /**
     * RSA私钥签名字符串
     *
     * @param info     待签名字符串
     * @param filePath 私钥文件所在路径（如：xx/xx/privateKey.keystore）
     * @param encode   编码
     * @return 签名值
     */
    default String signByFileRSA(String info, String filePath, @Nullable String encode) {
        return getRSASignature().signByFile(info, filePath, encode);
    }

    /**
     * RSA私钥签名字符串
     *
     * @param info          待签名字符串
     * @param privateKeyStr 私钥字符串内容
     * @return 签名值
     */
    default String signByStrRSA(String info, String privateKeyStr) {
        return getRSASignature().signByStr(info, privateKeyStr);
    }

    /**
     * RSA私钥签名字符串
     *
     * @param info          待签名字符串
     * @param privateKeyStr 私钥字符串内容
     * @param encode        编码
     * @return 签名值
     */
    default String signByStrRSA(String info, String privateKeyStr, @Nullable String encode) {
        return getRSASignature().signByStr(info, privateKeyStr, encode);
    }

    /**
     * RSA公钥签名校验
     *
     * @param info     待签名字符串
     * @param sign     签名值
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @return 校验结果
     */
    default boolean verifyByFileRSA(String info, String sign, String filePath) {
        return getRSASignature().verifyByFile(info, sign, filePath);
    }

    /**
     * RSA公钥签名校验
     *
     * @param info     待签名字符串
     * @param sign     签名值
     * @param filePath 公钥文件所在路径（如：xx/xx/publicKey.keystore）
     * @return 校验结果
     */
    default boolean verifyByFileRSA(String info, String sign, String filePath, @Nullable String encode) {
        return getRSASignature().verifyByFile(info, sign, filePath, encode);
    }

    /**
     * RSA公钥签名校验
     *
     * @param info         待签名字符串
     * @param sign         签名值
     * @param publicKeyStr 公钥字符串内容
     * @return 校验结果
     */
    default boolean verifyByStrRSA(String info, String sign, String publicKeyStr) {
        return getRSASignature().verifyByStr(info, sign, publicKeyStr);
    }

    /**
     * RSA公钥签名校验
     *
     * @param info         待签名字符串
     * @param sign         签名值
     * @param publicKeyStr 公钥字符串内容
     * @param encode       编码
     * @return 校验结果
     */
    default boolean verifyByStrRSA(String info, String sign, String publicKeyStr, @Nullable String encode) {
        return getRSASignature().verifyByStr(info, sign, publicKeyStr, encode);
    }
}
