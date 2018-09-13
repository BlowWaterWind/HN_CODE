package com.ai.ecs.ecm.mall.wap.platform.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.codec.binary.Hex;

/**
 * Created by cc on 2018/3/13.
 * 浦发接口规范
 *
 */
public class ThreeDes  {

    private static final Logger logger = LoggerFactory.getLogger(ThreeDes.class);

    private static final String CHARSET_UTF_8       = "UTF-8";

    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    public static final String DES_PRIVATE_KEY = "MhDOtYx6ukoshXX7uepz9NmJUf5rHIp8";

    //public static final String MD5_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ2YodO/UynWr3mL3zibse1Hsssz35HMRQZYq8f2rUsv7k9BlRpitw+kgCEZQJ5jtvPgC2uFxEhipE/7xSycHnBx9p5yk9Vuf5njuOlXgKacZ08LwPOJVNxuFMR5o8oduyqvCDYVmGC1uHeMy0eUcx2XO54WElRKNr8PhnXbt1b7AgMBAAECgYALQtvVcKd1puSVD9ycg+ub4/KvuH4yQeFwyadEu6i4Za1mtm38G6+vC3coZlCKwN9S3+CRB1b2QS4ylPAK9wDptTKBMZnliymRO7Tu+14NREG9yKIpsn6uHq55d2ActxdGrwRTYViGqyQ+RZhxda2Ewzo0uSezx8V61sSsdPH8mQJBAN22Ie2DsMwCKAqfGWnACsyWZJfUCwld8vlE6x/NFo4n7wfS99floaX4zK//v5c9GCSx3eL4CSOJ3A8d1DYA66cCQQC1+BZl9Al2bF4LRmHCuoGOKXjkNkBr06xt8jnKJTJR2590+DbhC3fZomYMCrjMj0Q3uCotSO1RkuMY7PcOX5SNAkApOoRGobWPylrd1sYoByE6+ECOh0ziGglj1zAOAtpN5xkpEE8sY7RSo9iUZe+f1SmXMUg8u7kfVbSJTxe2TyeDAkB6FAD6zxiWESeaitJFMJiABkSrXdYkQLLaY/nvKrsyfZPhhjBTFD2IjVHG3F6UJb1PYEczcEx+QHwImdk5FFh1AkApgNt/Ib5S5Bu3IEIRMUaE/9XA9ocVxGVjcg4RRUOlgYOHd/RnWXIcdHi8DbaB7WO+9BURNRiWnV1lJyhPDJ5J";


    /**
     * 生产私钥 我们生产 提供对方公钥
     */
    public static final String MD5_PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ0gw+W8Vz+ylsGIwt5JYhgvaKOrlZxl9AXSPjg9EXuT/zLLql8MZcN2AJ5Fjh8f7Ez3kU+ZpGb2Sy2LVnsb66RHBkAEYxj571ulpVzYJykN6b8wGhRTant9roSXb2a4+jKNn7BafDfw6tBKdjNhZD9tvKtB2ZJXRtrY870ngAVvAgMBAAECgYB896E2JzwRGh2SoQ2zMxQEfjgv6/iaoe+ZbaQ0oEU2Tvep05eV7stE0KYfOtyU3pPWhxOYBm9dwEb8WxSV5xfalcilwR5PfpS8S2lIBCJq0UisF4IVKCs1GNQlN4zvKQPy64LwT+DfF41SSwA0apqgjo6ohRBaNO9+HE21TdIJiQJBAOt+XzhQkmdZeWZXWaum78XmMt2VS/36Tlr85o+h1q5RTXGBPzOJdGHEjmhQKEmEZkuu4Sy7GTVsduMlT/8fPr0CQQCqz3cD6lPIoo0RLuz41Id5JApegHULQjVqg01RKiosXdaeZ+iR/Nysnv8cSv19TP1l+S94my8Bz+yGOUq3Sj2bAkBK/QnLP/TuzhIXYbdfLqBpjz0hTDpPnNY4qRGuKSXYinEztKlwetPBkjqawvOhPXTpor13ZfjIB0rwV+BaaBOtAkBiQMiTJf1f0bEXi0igsY/j7QlR5s5s1X7ob/LPl1N/BdNodxjesPc3DJZubex2YQz51WarF4jon/PMGbSSzk+1AkBNCPgtLRMYAQSLpqCfF3+uVAbxlzTFlAsDycGRyNfU7ReNRjc2qfpncUmRbT5my6rXGTLvA7I8NeOyC57V8o/B";
    /**
     *
     * @param src = parameterString
     * @param key
     * @return
     */
    public static String desEncryptAndUrlEncode(String src, String key) throws Exception// 3DESECB加密,key必须是长度大于等于 3*8 = 24 位
    {
        byte[] b = null;
        try
        {
            KeySpec dks = new DESedeKeySpec(Base64.decodeBase64(key.getBytes()));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey securekey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            b = cipher.doFinal(src.getBytes(CHARSET_UTF_8));
        }
        catch (Exception e)
        {
            logger.error("3DES加密出错",e);
        }
        return URLEncoder.encode(new String(Base64.encodeBase64(b)),"UTF-8");
    }



    /**
     * 用私钥对信息生成数字签名
     * data = 上面方法得到的接口 paratemerString.getBytes
     * privateKey = 私钥
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        // 构造PKCS8EncodedKeySpec对象
        byte[] keyDecode = decryptBASE64(privateKey);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyDecode);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(priKey);
        signature.update(data);
        String hexString = new String(Hex.encodeHex(signature.sign()));
        return URLEncoder.encode(hexString,"UTF-8");
    }


    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      数字签名
     * @return 校验成功返回true 失败返回false
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        String signOri = URLEncoder.encode(sign,"UTF-8");
        byte[] signData = Hex.decodeHex(signOri.toCharArray());
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(pubKey);
        signature.update(data);
        // 验证签名是否正常
        return signature.verify(signData);
    }



    private static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key.getBytes());
    }

    public static String getUrl() throws Exception{
        String parameter = "ValidTime="+System.currentTimeMillis()
                +"&OrderId="+"61894096821764096"
                +"&BuyerInfo="+"谢江琼长沙测试"
                +"&BuyModel="+"中电软件园"
                +"&FreeZeTime="+"002"
                +"&AmountFrozen"+"100.00"
                +"&CtfId="+"360323197612215390"
                +"&BeginDate="+"20181016"
                +"&EndDate="+"20191016"
                +"&url="+"https://15.15.20.88:8084/shop/spdbank/linkto"
               // +"&DeptinfoOrgid=4503&ManagerID=11111111"
                +"&FreezeDate"+"20191018";
        String parameterString = ThreeDes.desEncryptAndUrlEncode(parameter,ThreeDes.DES_PRIVATE_KEY);
        String encrpt = URLDecoder.decode(parameterString,"UTF-8");//传入时用
        String signData = ThreeDes.sign(encrpt.getBytes(),ThreeDes.MD5_PRIVATE_KEY);
        String url = "http://etest.spdb.com.cn/wap/H5HouseGreet.hl";
        return url+"?H5Channel=109015&SubChannel=&ParameterString="+parameterString+"&SignData="+signData;
    }


    public static void main(String args[]) throws Exception{

        //少了等于号 导致验证签名不通过
        String parameter = "ValidTime=1528277855412&OrderId=CSGJ2018060601321216&BuyerInfo=海控三&BuyModel=新版和家庭129元融合套餐&FreeZeTime=001&AmountFrozen=1000&CtfId=420202198705163140&BeginDate=20180606&EndDate=20300606&FreezeDate=20300706&url=http://localhost:8084/shop/spdbank/afterFreeze?orderSubId=70220832291037184";
        String parameterString = ThreeDes.desEncryptAndUrlEncode(parameter,"MhDOtYx6ukoshXX7uepz9NmJUf5rHIp8");
        System.out.println(parameterString);

        String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAJ0gw+W8Vz+ylsGIwt5JYhgvaKOrlZxl9AXSPjg9EXuT/zLLql8MZcN2AJ5Fjh8f7Ez3kU+ZpGb2Sy2LVnsb66RHBkAEYxj571ulpVzYJykN6b8wGhRTant9roSXb2a4+jKNn7BafDfw6tBKdjNhZD9tvKtB2ZJXRtrY870ngAVvAgMBAAECgYB896E2JzwRGh2SoQ2zMxQEfjgv6/iaoe+ZbaQ0oEU2Tvep05eV7stE0KYfOtyU3pPWhxOYBm9dwEb8WxSV5xfalcilwR5PfpS8S2lIBCJq0UisF4IVKCs1GNQlN4zvKQPy64LwT+DfF41SSwA0apqgjo6ohRBaNO9+HE21TdIJiQJBAOt+XzhQkmdZeWZXWaum78XmMt2VS/36Tlr85o+h1q5RTXGBPzOJdGHEjmhQKEmEZkuu4Sy7GTVsduMlT/8fPr0CQQCqz3cD6lPIoo0RLuz41Id5JApegHULQjVqg01RKiosXdaeZ+iR/Nysnv8cSv19TP1l+S94my8Bz+yGOUq3Sj2bAkBK/QnLP/TuzhIXYbdfLqBpjz0hTDpPnNY4qRGuKSXYinEztKlwetPBkjqawvOhPXTpor13ZfjIB0rwV+BaaBOtAkBiQMiTJf1f0bEXi0igsY/j7QlR5s5s1X7ob/LPl1N/BdNodxjesPc3DJZubex2YQz51WarF4jon/PMGbSSzk+1AkBNCPgtLRMYAQSLpqCfF3+uVAbxlzTFlAsDycGRyNfU7ReNRjc2qfpncUmRbT5my6rXGTLvA7I8NeOyC57V8o/B";

        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCdIMPlvFc/spbBiMLeSWIYL2ijq5WcZfQF0j44PRF7k/8yy6pfDGXDdgCeRY4fH+xM95FPmaRm9ksti1Z7G+ukRwZABGMY+e9bpaVc2CcpDem/MBoUU2p7fa6El29muPoyjZ+wWnw38OrQSnYzYWQ/bbyrQdmSV0ba2PO9J4AFbwIDAQAB";

        String encrpt = URLDecoder.decode(parameterString,"UTF-8");//传入时用
        String signData = sign(encrpt.getBytes(),privateKey);
        System.out.println("转码之前 signData\n"+signData);
        Boolean flag =  verify(encrpt.getBytes(),publicKey,signData);

        System.out.println(flag);
    }
}
