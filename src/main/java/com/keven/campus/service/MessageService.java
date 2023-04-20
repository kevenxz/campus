package com.keven.campus.service;

import com.keven.campus.common.utils.R;
import com.keven.campus.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author
 * @description 针对表【t_campus_message(私聊信息表)】的数据库操作Service
 * @createDate 2023-01-12 14:26:48
 */
public interface MessageService extends IService<Message> {

    /**
     * 获取登录用户的私信列表
     * 会话列表--> 会话的总数量、每个会话未读的信息数量、每个会话的最新信息、每个会话的总信息数
     *
     * @param curPage
     * @param limit
     * @return
     */
    R getConversations(Integer curPage, Integer limit);

    /**
     * 获取单个会话的所有信息
     *
     * @param curPage
     * @param limit
     * @param conversationId
     * @return
     */
    R getConversationOfMsgs(Integer curPage, Integer limit, String conversationId);

    /**
     * 发送消息
     * @param toId 目标id
     * @param msgContent 消息内容
     * @return
     */
    R sendLetter(Long toId, String msgContent,Long fileId);
}
