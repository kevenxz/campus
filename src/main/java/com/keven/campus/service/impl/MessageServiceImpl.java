package com.keven.campus.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.utils.*;
import com.keven.campus.entity.Message;
import com.keven.campus.entity.User;
import com.keven.campus.entity.vo.ConversationVo;
import com.keven.campus.entity.vo.MessageVo;
import com.keven.campus.entity.vo.UserVo;
import com.keven.campus.mapper.UserMapper;
import com.keven.campus.service.MessageService;
import com.keven.campus.mapper.MessageMapper;
import com.sun.xml.internal.ws.api.model.MEP;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.query.QueryUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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

    @Resource
    private UserMapper userMapper;

    @Override
    public R getConversations(Integer curPage, Integer limit) {
//        Long userId = SecurityUtil.getUserId();
        Long userId = 1L;
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
        R r = new R();
        User currentUser = SecurityUtil.getUser();
        IPage<Message> page = page(new Query<Message>().getPage(CampusUtil.getPageMap(curPage, limit)),
                new LambdaQueryWrapper<Message>().ne(Message::getMsgStatus, MESSAGE_STATUS_DELETE)
                        .ne(Message::getFromId, SYSTEM_USER_ID)
                        .eq(Message::getConversationId, conversationId)
                        .orderByDesc(Message::getId));
        // 获取目标人的信息
        UserVo targetUser = getTargetUser(conversationId);
        r.put("targetUser", targetUser);
        // 没有信息直接返回
        if (page.getRecords() == null || page.getRecords().isEmpty()) {
            return r.put(page);
        }
        // 查询私信的内容
        List<Message> list = list(new LambdaQueryWrapper<Message>()
                .in(Message::getId, page.getRecords().stream().map(Message::getId).collect(Collectors.toList())));
        // 每条私信做一个包装，加上用户的头像，id，照片，加上一个标识
        List<MessageVo> messageVos = list.stream().map(message -> {
            MessageVo messageVo = new MessageVo();
            if (currentUser != null && message.getFromId().equals(currentUser.getId())) {
                messageVo.setCurrent(true);
                messageVo.setUserId(currentUser.getId());
                messageVo.setAvatarurl(currentUser.getAvatarurl());
                messageVo.setNickname(currentUser.getNickname());
            } else {
                messageVo.setCurrent(false);
                messageVo.setUserId(targetUser.getId());
                messageVo.setAvatarurl(targetUser.getAvatarurl());
                messageVo.setNickname(targetUser.getNickname());
            }
            BeanUtils.copyProperties(message, messageVo);
            return messageVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(messageVos, (int) page.getTotal(), (int) page.getSize(), (int) page.getCurrent());
        return r.put(pageUtils);
    }

    /**
     * 得到目标用户(私信的用户)
     *
     * @param conversationId 会话id
     * @return {@link User}
     */
    private UserVo getTargetUser(String conversationId) {
        String[] ids = conversationId.split("-");
        Long d0 = Long.parseLong(ids[0]);
        Long d1 = Long.parseLong(ids[1]);
        User targetUser;
        if (Objects.equals(SecurityUtil.getUserId(), d0)) {
            targetUser = userMapper.selectById(d1);
        } else {
            targetUser = userMapper.selectById(d0);
        }
        return BeanUtil.copyProperties(targetUser, UserVo.class);
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




