package com.keven.campus;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.keven.campus.common.utils.TencentCosConstant;
import com.keven.campus.entity.School;
import com.keven.campus.service.SchoolService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class CampusApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private SchoolService schoolService;

    private String imageBuckerUrl;

    @Resource
    private TencentCosConstant tencentCosConstant;

    @Test
    void contextLoads() {
        System.out.println(tencentCosConstant.toString());
    }

    @Test
    void testPasswordEncoder() {
        System.out.println(passwordEncoder.encode("123456"));
    }

    /**
     * 边读文件边插入到数据库
     * 插入json
     */
    @Test
    void insertJson() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\周鑫杰\\Desktop\\school.json"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 解析json数据
                JSONArray jsonArray = new JSONArray(line);
                ArrayList<School> schools = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    School school = new School();
                    school.setCity(jsonObject.getString("city"));
                    school.setProvince(jsonObject.getString("province"));
                    school.setName(jsonObject.getString("name"));
                    schools.add(school);
                }
                schoolService.saveBatch(schools);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
