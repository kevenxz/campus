package com.keven.campus.common.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 在主启动类启动时初始化配置文件中的内容
 *
 * @author Keven
 * @version 1.0
 */
@Component
public class TencentCosConstant implements InitializingBean {

    @Value("${tencent.cos.secretId}")
    private String secretId;
    @Value("${tencent.cos.secretKey}")
    private String secretKey;
    @Value("${tencent.cos.bucketName}")
    private String bucketName;
    @Value("${tencent.cos.region}")
    private String region;
    @Value("${tencent.cos.url}")
    private String url;

    public static String SECRET_ID;
    public static String SECRET_KEY;
    public static String BUCKET_NAME;
    public static String REGION;
    public static String URL;

    @Override
    public void afterPropertiesSet() throws Exception {
        SECRET_ID = secretId;
        SECRET_KEY = secretKey;
        BUCKET_NAME = bucketName;
        REGION = region;
        URL = url;
    }

    @Override
    public String toString() {
        return "TencentCosConstant{" +
                "secretId='" + secretId + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", region='" + region + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
