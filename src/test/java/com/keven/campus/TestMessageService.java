package com.keven.campus;

import com.keven.campus.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Keven
 * @version 1.0
 */
@SpringBootTest
public class TestMessageService {

    @Autowired
    private MessageService messageService;

    @Test
    void testGetConversations() {
        System.out.println(messageService.getConversations(1, 10));
    }


}
