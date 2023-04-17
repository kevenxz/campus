package com.keven.campus.controller;

import com.keven.campus.service.MessageService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@Api(tags = {"消息发送"})
@RequestMapping("/message")
public class MessageController {

    @Resource
    private MessageService messageService;

}
