package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.utils.*;
import com.keven.campus.entity.Message;
import com.keven.campus.service.MessageService;
import com.keven.campus.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author
 * @description 针对表【t_campus_message(私聊信息表)】的数据库操作Service实现
 * @createDate 2023-01-12 14:26:48
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService, CampusConstant {


    @Override
    public R getConversations(Integer curPage, Integer limit) {
        Long userId = SecurityUtil.getUserId();
        IPage<Long> page = new Query<Long>().getPage(CampusUtil.getPageMap(curPage, limit));

        QueryWrapper<Object> objectQueryWrapper = new QueryWrapper<>().select("max(id)")
                .ne("msg_status", MESSAGE_STATUS_DELETE)
                .ne("from_id", SYSTEM_USER_ID)
                .eq("from_id", userId).or().eq("to_id", userId)
                .groupBy("conversation_id");
        return null;
    }

    @Override
    public R getConversationOfMsgs(Integer curPage, Integer limit, String conversationId) {
        return null;
    }

    /**
     * 获取会话的数量
     *
     * @param userId
     * @return
     */
    public Integer getConversationCount(Long userId) {

        return null;
    }

    /**
     * 某个会话所包含的私信数量
     *
     * @param conversationId
     * @return
     */
    public Integer getLetterCount(String conversationId) {
        return null;
    }

}




