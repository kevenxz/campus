package com.keven.campus.mapper;

import com.keven.campus.entity.QueAns;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author
 * @description 针对表【t_campus_que_ans((匿名问答表) -- 默认提问为(无名氏))】的数据库操作Mapper
 * @createDate 2023-01-16 11:15:22
 * @Entity com.keven.campus.entity.QueAns
 */
@Mapper
public interface QueAnsMapper extends BaseMapper<QueAns> {

}




