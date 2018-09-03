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

package cn.aberic.bother.encryption;

import cn.aberic.bother.encryption.key.KeyExec;
import cn.aberic.bother.encryption.key.ecc.ECDSAEncrypt;
import cn.aberic.bother.encryption.key.ecc.ECDSASignature;
import cn.aberic.bother.encryption.key.rsa.RSAEncrypt;
import cn.aberic.bother.encryption.key.rsa.RSASignature;
import cn.aberic.bother.storage.Common;
import lombok.extern.slf4j.Slf4j;

/**
 * 作者：Aberic on 2018/09/03 16:21
 * 邮箱：abericyang@gmail.com
 */
@Slf4j
public class KeyTest {

    public static void main(String[] args) {
        testCrypt();
    }

    private static void testCrypt() {
        String filePath = Common.KEY_FILE_DIR;
        KeyExec.obtain().createRSAKeyPair(filePath, "Test");
        String pubFilePath = String.format("%s/%s_publicRSA.key", filePath, "Test");
        String priFilePath = String.format("%s/%s_privateRSA.key", filePath, "Test");
        log.debug("--------------RSA公钥加密私钥解密过程-------------------");
        String info = "RSA公钥加密私钥解密过程";
        //公钥加密
        String enStr = RSAEncrypt.obtain().encryptPubFile(pubFilePath, info);
        //私钥解密
        String deStr = RSAEncrypt.obtain().decryptPriFile(priFilePath, enStr);
        log.debug("原文：" + info);
        log.debug("加密：" + enStr);
        log.debug("解密：" + deStr);
        log.info("=====================================================================================");
        log.debug("--------------RSA私钥加密公钥解密过程-------------------");
        info = "RSA私钥加密公钥解密过程";
        //私钥加密
        enStr = RSAEncrypt.obtain().encryptPriFile(priFilePath, info);
        //公钥解密
        deStr = RSAEncrypt.obtain().decryptPubFile(pubFilePath, enStr);
        log.debug("原文：" + info);
        log.debug("加密：" + enStr);
        log.debug("解密：" + deStr);
        log.info("=====================================================================================");
        log.debug("--------------RSA私钥签名过程-------------------");
        info = "这是用于RSA签名的原始数据";
        String signStr = RSASignature.obtain().signByFile(info, priFilePath);
        log.debug("签名原串：" + info);
        log.debug("签名串：" + signStr);
        log.info("=====================================================================================");
        log.debug("--------------RSA公钥校验签名-------------------");
        log.debug("校验结果 = " + RSASignature.obtain().verifyByFile(info, signStr, pubFilePath));


        log.info("=====================================================================================");
        log.debug("=====================================================================================");
        log.info("=====================================================================================");
        KeyExec.obtain().createECCDSAKeyPair(filePath, "Test");
        pubFilePath = String.format("%s/%s_publicECDSA.key", filePath, "Test");
        priFilePath = String.format("%s/%s_privateECDSA.key", filePath, "Test");
        log.debug("--------------ECDSA公钥加密私钥解密过程-------------------");
        info = "ECDSA公钥加密私钥解密过程";
        //公钥加密
        enStr = ECDSAEncrypt.obtain().encryptByFile(pubFilePath, info);
        //私钥解密
        deStr = ECDSAEncrypt.obtain().decryptPriFile(priFilePath, enStr);
        log.debug("原文：" + info);
        log.debug("加密：" + enStr);
        log.debug("解密：" + deStr);
        log.info("=====================================================================================");
        log.debug("--------------ECDSA私钥签名过程-------------------");
        info = "这是用于ECDSA签名的原始数据";
        signStr = ECDSASignature.obtain().signByFile(info, priFilePath);
        log.debug("签名原串：" + info);
        log.debug("签名串：" + signStr);
        log.info("=====================================================================================");
        log.debug("--------------ECDSA公钥校验签名-------------------");
        log.debug("校验结果 = " + ECDSASignature.obtain().verifyByFile(info, signStr, pubFilePath));
    }

}
