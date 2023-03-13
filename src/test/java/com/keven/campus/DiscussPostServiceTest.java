package com.keven.campus;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.keven.campus.entity.File;
import com.keven.campus.entity.Topic;
import com.keven.campus.mapper.FileMapper;
import com.keven.campus.mapper.TopicMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Keven
 * @version 1.0
 */

@SpringBootTest
public class DiscussPostServiceTest {
    @Autowired
    private TopicMapper topicMapper;

    @Autowired
    private FileMapper fileMapper;

    @Test
    void testDisCount() {
        topicMapper.update(null, new LambdaUpdateWrapper<Topic>()
                .setSql("discuss_count = (case when t_campus_topic.discuss_count > 0 then t_campus_topic.discuss_count-1 else 0  end)").eq(Topic::getId, 1));
    }

    @Test
    void testInsertFiles() {
        List<File> files = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            files.add(File.builder()
                    .fileName("xxx")
                    .fileUrl("uuu")
                    .fileSize("22323")
                    .businessId(23232l)
                    .entityType(2)
                    .build());
        }
        System.out.println(fileMapper.insertByForeach(files));

    }
}
