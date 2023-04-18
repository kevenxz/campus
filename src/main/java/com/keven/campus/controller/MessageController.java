package com.keven.campus.controller;

import com.keven.campus.common.utils.R;
import com.keven.campus.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @ApiOperation("私信列表,所有的会话")
    @GetMapping("/letter/list")
    public R getLetterList(@ApiParam("当前页") Integer curPage,
                           @ApiParam("每页条数") Integer limit) {
        return messageService.getConversations(curPage, limit);
    }

    @ApiOperation("单个会话的所有列表信息")
    @GetMapping("/letter/detail/{conversationId}")
    public R getLetterDetail(@ApiParam("当前页") Integer curPage,
                             @ApiParam("每页条数") Integer limit,
                             @ApiParam("会话id") @PathVariable String conversationId) {
        return messageService.getConversationOfMsgs(curPage, limit, conversationId);
    }
}
