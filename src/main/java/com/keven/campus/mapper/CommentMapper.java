package com.keven.campus.mapper;

import com.keven.campus.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 周鑫杰
 * @description 针对表【t_campus_comment(评论表)】的数据库操作Mapper
 * @createDate 2023-02-28 16:26:52
 * @Entity com.keven.campus.entity.Comment
 */

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}




