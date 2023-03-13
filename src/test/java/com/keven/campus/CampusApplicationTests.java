package com.keven.campus;

import com.keven.campus.common.utils.TencentCosConstant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class CampusApplicationTests {

    private String imageBuckerUrl;

    @Resource
    private TencentCosConstant tencentCosConstant;

    @Test
    void contextLoads() {
        System.out.println(tencentCosConstant.toString());
    }


}
