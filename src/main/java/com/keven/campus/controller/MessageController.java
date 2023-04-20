package com.keven.campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keven.campus.common.utils.CampusConstant;
import com.keven.campus.common.utils.R;
import com.keven.campus.entity.Message;
import com.keven.campus.entity.User;
import com.keven.campus.service.MessageService;
import com.keven.campus.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/conversation/list")
    public R getConversationList(@ApiParam("当前页") Integer curPage,
                                 @ApiParam("每页条数") Integer limit) {
        return messageService.getConversations(curPage, limit);
    }

    @ApiOperation("单个会话的所有列表信息")
    @GetMapping("/conversation/detail/{conversationId}")
    public R getConversationDetail(@ApiParam("当前页") Integer curPage,
                                   @ApiParam("每页条数") Integer limit,
                                   @ApiParam("会话id") @PathVariable String conversationId) {
        return messageService.getConversationOfMsgs(curPage, limit, conversationId);
    }

    @ApiOperation("新增一条私信信息")
    @PostMapping("/letter/send")
    public R sendLetter(@ApiParam("目标用户id") Long toId,
                        @ApiParam("消息内容") String msgContent,
                        @ApiParam("文件id")Long fileId) {
        return messageService.sendLetter(toId,msgContent,fileId);
    }
}
