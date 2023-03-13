package com.keven.campus;

import com.qcloud.cos.*;
import com.qcloud.cos.auth.*;
import com.qcloud.cos.exception.*;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.*;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.TreeMap;

/**
 * @author Keven
 * @version 1.0
 */
class A {
    int i;
    static String s;

    void method1() {
    }

    static void method2() {
    }
}

public class TencentTest {

    @Test
    void test9() {
        A a = new A();
        a.method1();
    }

    public static void main(String[] args) {
        TreeMap<String, Object> config = new TreeMap<String, Object>();

        try {
            //这里的 SecretId 和 SecretKey 代表了用于申请临时密钥的永久身份（主账号、子账号等），子账号需要具有操作存储桶的权限。
            String secretId = "AKIDNaZEj26tYAas7E4gEhwxiEYfaSJ2X8bz";//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
            String secretKey = "QyRhnhPs8Ayc5g290e3C4lHgtsfGdqtk";//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
            // 替换为您的云 api 密钥 SecretId
            config.put("secretId", secretId);
            // 替换为您的云 api 密钥 SecretKey
            config.put("secretKey", secretKey);

            // 设置域名:
            // 如果您使用了腾讯云 cvm，可以设置内部域名
            //config.put("host", "sts.internal.tencentcloudapi.com");

            // 临时密钥有效时长，单位是秒，默认 1800 秒，目前主账号最长 2 小时（即 7200 秒），子账号最长 36 小时（即 129600）秒
            config.put("durationSeconds", 1800);

            // 换成您的 bucket
            config.put("bucket", "campus-images-1313096908");
            // 换成 bucket 所在地区
            config.put("region", "ap-guangzhou");

            // 这里改成允许的路径前缀，可以根据自己网站的用户登录态判断允许上传的具体路径
            // 列举几种典型的前缀授权场景：
            // 1、允许访问所有对象："*"
            // 2、允许访问指定的对象："a/a1.txt", "b/b1.txt"
            // 3、允许访问指定前缀的对象："a*", "a/*", "b/*"
            // 如果填写了“*”，将允许用户访问所有资源；除非业务需要，否则请按照最小权限原则授予用户相应的访问权限范围。
            config.put("allowPrefixes", new String[]{
                    "*"
            });

            // 密钥的权限列表。必须在这里指定本次临时密钥所需要的权限。
            // 简单上传、表单上传和分块上传需要以下的权限，其他权限列表请参见 https://cloud.tencent.com/document/product/436/31923
            String[] allowActions = new String[]{
                    // 简单上传
                    "name/cos:PutObject",
                    // 表单上传、小程序上传
                    "name/cos:PostObject",
                    // 分块上传
                    "name/cos:InitiateMultipartUpload",
                    "name/cos:ListMultipartUploads",
                    "name/cos:ListParts",
                    "name/cos:UploadPart",
                    "name/cos:CompleteMultipartUpload"
            };
            config.put("allowActions", allowActions);
            /**
             * 设置condition（如有需要）
             //# 临时密钥生效条件，关于condition的详细设置规则和COS支持的condition类型可以参考 https://cloud.tencent.com/document/product/436/71307
             final String raw_policy = "{\n" +
             "  \"version\":\"2.0\",\n" +
             "  \"statement\":[\n" +
             "    {\n" +
             "      \"effect\":\"allow\",\n" +
             "      \"action\":[\n" +
             "          \"name/cos:PutObject\",\n" +
             "          \"name/cos:PostObject\",\n" +
             "          \"name/cos:InitiateMultipartUpload\",\n" +
             "          \"name/cos:ListMultipartUploads\",\n" +
             "          \"name/cos:ListParts\",\n" +
             "          \"name/cos:UploadPart\",\n" +
             "          \"name/cos:CompleteMultipartUpload\"\n" +
             "        ],\n" +
             "      \"resource\":[\n" +
             "          \"qcs::cos:ap-shanghai:uid/1250000000:examplebucket-1250000000/*\"\n" +
             "      ],\n" +
             "      \"condition\": {\n" +
             "        \"ip_equal\": {\n" +
             "            \"qcs:ip\": [\n" +
             "                \"192.168.1.0/24\",\n" +
             "                \"101.226.100.185\",\n" +
             "                \"101.226.100.186\"\n" +
             "            ]\n" +
             "        }\n" +
             "      }\n" +
             "    }\n" +
             "  ]\n" +
             "}";

             config.put("policy", raw_policy);
             */


            Response response = CosStsClient.getCredential(config);
            System.out.println(response.credentials.tmpSecretId);
            System.out.println(response.credentials.tmpSecretKey);
            System.out.println(response.credentials.sessionToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no valid secret !");
        }
    }

    @Test
    public void test() throws Exception {

        // 用户基本信息
        String tmpSecretId = "AKID3DCOn41O8R0oWaykKVOJINO1BXdo5836O79tH_8EdaXzh2DHtgM-ow4iUwCNBD9u";   // 替换为 STS 接口返回给您的临时 SecretId
        String tmpSecretKey = "KhbqsR08wnLEMMZAXwkSt0X7dYBSBfQPBEwzAwHpF1I=";  // 替换为 STS 接口返回给您的临时 SecretKey
        String sessionToken = "pmRFSCbq7zduqfWZYwtv0wUWxd5udTJa2723c2ce4d5cd1fae1324827b5638ae61kH0QmE834F9wd2erkteXvEPqpBAD6N3S7LBe_Vti7wwsVJekP49tTbcwdpOoCBK5GLPN1Y4sWDVkAfW5dYPGaSOlmNMPg5QmwSKHx2fPC7YTocv0c4sZ6dXxRHiMYSGph8p1oMHphqap2x16cWes5kSnyXEWh0jNqCyPrCo_r--rGIrpaPdZR_N80VTj6TxVYqbz7Sdh4vistMIHselshHCLfhyiWAzLo7vEVCXIWl8BegesO2qe_Lz6fpdB3tO8UyXPmygAWOioYhlLoMWL0WdmTvOgi8GKgLwAXQLr3-sl2WHUvFnDGDZb8onnS5htgEhqPzVqo47TjNpahZPgX064Vh-OFQu59AcBXwj9X6_m_L5uXR5X46J_tIFIiJJFp1V3LVkAi-hPpQCphQVMRBkT0vDhlD9FbzgelD1evzVIcnn_ck2hn4ucyvwB6Qq4c5D2PSXM2-2TXbwxE7V7BRtsrZU_slMhZcRxTUtMpLXqkk4dzRS3rqlfcGg8Yx0BGOUpDJZOE5dw3cGxEWecb_BEz6tY5MPR1SiytNDSXPX0DmrUbyrLczmK2uF5cBikyobdFKgkCOulmsgRiVySQ";  // 替换为 STS 接口返回给您的临时 Token

        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(tmpSecretId, tmpSecretKey);
        // 2 设置 bucket 区域,详情请参见 COS 地域 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region("ap-guangzhou"));
        // 3 生成 cos 客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket 名需包含 appid
        String bucketName = "campus-images-1313096908";

        String key = "avatar";
        // 上传 object, 建议 20M 以下的文件使用该接口
        File localFile = new File("src/main/resources/sensitive-words.txt");
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);

        // 设置 x-cos-security-token header 字段
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setSecurityToken(sessionToken);
        putObjectRequest.setMetadata(objectMetadata);

        try {
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            // 成功：putobjectResult 会返回文件的 etag
            String etag = putObjectResult.getETag();
        } catch (CosServiceException e) {
            //失败，抛出 CosServiceException
            e.printStackTrace();
        } catch (CosClientException e) {
            //失败，抛出 CosClientException
            e.printStackTrace();
        }

        // 关闭客户端
        cosclient.shutdown();

    }


    @Test
    void testCatch() {
        byte a = (byte) 139;
        System.out.println(a);

    }

    @Test
    void test2() {
        String str = null;
        str.concat("abc");
        str.concat("123");
        System.out.println(str);
    }
}
