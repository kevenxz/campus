package com.keven.campus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.utils.*;
import com.keven.campus.entity.Message;
import com.keven.campus.entity.vo.ConversationVo;
import com.keven.campus.service.MessageService;
import com.keven.campus.mapper.MessageMapper;
import com.sun.xml.internal.ws.api.model.MEP;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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
        // 分页查询会话
        IPage<Message> page = page(new Query<Message>().getPage(CampusUtil.getPageMap(curPage, limit)),
                new QueryWrapper<Message>()
                        .select("max(id) as id ")
                        .ne("msg_status", MESSAGE_STATUS_DELETE)
                        .ne("from_id", SYSTEM_USER_ID)
                        .eq("from_id", userId).or().eq("to_id", userId)
                        .groupBy("conversation_id").orderByDesc("id"));
        // 没有会话直接返回
        if (page.getRecords() == null || page.getRecords().isEmpty()) {
            return R.ok().put(page);
        }
        // 查询最后一条信息
        List<Message> list = list(new LambdaQueryWrapper<Message>()
                .in(Message::getId, page.getRecords().stream().map(Message::getId).collect(Collectors.toList())));
        // 包装每个会话未读的数量
        List<ConversationVo> conversationVos = list.stream().map(message -> {
            ConversationVo conversationVo = new ConversationVo();
            BeanUtil.copyProperties(message, conversationVo);
            conversationVo.setUnReadCount(getUnreadCount(userId, message.getConversationId()));
            return conversationVo;
        }).collect(Collectors.toList());


        PageUtils pageUtils = new PageUtils(conversationVos, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
        pageUtils.setList(conversationVos);
        return R.ok().put(pageUtils);
    }

    @Override
    public R getConversationOfMsgs(Integer curPage, Integer limit, String conversationId) {
        return null;
    }


    /**
     * 查询未读私信的数量
     *
     * @param userId         当前登录用户id
     * @param conversationId 可为null，则查询所有未读私信的数量
     * @return
     */
    public Integer getUnreadCount(Long userId, String conversationId) {
        // 查询数量 除去系统通知
        long count = count(new LambdaQueryWrapper<Message>().eq(Message::getMsgStatus, MESSAGE_STATUS_UNREAD)
                .ne(Message::getFromId, SYSTEM_USER_ID)
                .eq(Message::getToId, userId)
                .eq(StrUtil.isNotEmpty(conversationId), Message::getConversationId, conversationId));
        return Integer.parseInt(String.valueOf(count));
    }

}




