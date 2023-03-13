package com.keven.campus.mapper;

import com.keven.campus.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author
 * @description 针对表【t_campus_message(私聊信息表)】的数据库操作Mapper
 * @createDate 2023-01-12 14:26:48
 * @Entity com.keven.campus.entity.Message
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

}




