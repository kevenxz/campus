package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.entity.Message;
import com.keven.campus.service.MessageService;
import com.keven.campus.mapper.MessageMapper;
import org.springframework.stereotype.Service;

/**
 * @author
 * @description 针对表【t_campus_message(私聊信息表)】的数据库操作Service实现
 * @createDate 2023-01-12 14:26:48
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

}




