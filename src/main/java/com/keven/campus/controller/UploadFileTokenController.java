package com.keven.campus.controller;

import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.TencentCosConstant;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Keven
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/uploadToken")
@Api(tags = {"获取上传文件的token"})
public class UploadFileTokenController {

    private static final Logger logger = LoggerFactory.getLogger(UploadFileTokenController.class);

    @Resource
    private TencentCosConstant tencentCosConstant;

    /**
     * 解释：
     * 前端上传文件 --- >  直接好上传给对应的服务器 -- > 得到返回的信息 -- > 再把返回的信息传给后端
     *      存在问题 --> 上传之后  要把对应的url 存到后端
     *      解决 -- > 提交时候可以把所有的照片和帖子一起提交 可以实现一个数组 把所有的文件url 转为一个数组 然后实现批量插入数据库
     *
     *
     * 后端上传文件 --- >  后端生成的文件 --> 传给服务器 --> 得到返回的信息进行存储
     *      存在问题: 造成服务器压力大 吃带宽
     */

    /**
     * 获得上传的凭证
     * 前端 访问后端 获取对应的凭证才能上传照片 然后在进行file表的对应数据的存储
     * 前端传文件到对应的服务器 然后把对应的URL 传给后端
     *
     * @return
     */
    // todo 设置权限 只有登录之后才可以上传文件
    @ApiOperation("获取文件上传的凭证")
    @RequestMapping("/get")
    public R getUploadToken(String type) {
        TreeMap<String, Object> config = new TreeMap<String, Object>();
        try {
            //这里的 SecretId 和 SecretKey 代表了用于申请临时密钥的永久身份（主账号、子账号等），子账号需要具有操作存储桶的权限。
            String secretId = TencentCosConstant.SECRET_ID;//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
            String secretKey = TencentCosConstant.SECRET_KEY;//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
            String bucket = TencentCosConstant.BUCKET_NAME;
            String region = TencentCosConstant.REGION;
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
            config.put("bucket", bucket);
            // 换成 bucket 所在地区
            config.put("region", region);

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
            String tmpSecretId = response.credentials.tmpSecretId;
            String tmpSecretKey = response.credentials.tmpSecretKey;
            String sessionToken = response.credentials.sessionToken;
            Map<String, String> msg = new HashMap<>();
            msg.put("tmpSecretId", tmpSecretId);
            msg.put("tmpSecretKey", tmpSecretKey);
            msg.put("sessionToken", sessionToken);
            System.out.println(tmpSecretId);
            System.out.println(tmpSecretKey);
            System.out.println(sessionToken);
            return R.ok().put(msg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no valid secret !");
        }
    }
}
