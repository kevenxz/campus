package com.keven.campus.controller;

import com.keven.campus.common.utils.CampusConstant;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.Msg;
import com.keven.campus.common.validator.ValidatorUtils;
import com.keven.campus.entity.Comment;
import com.keven.campus.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/comment")
@Api(tags = {"评论表"})
public class CommentController implements CampusConstant {


    @Autowired
    private CommentService commentService;

    /**
     * 评论的所属帖子  -- >  评论的类型
     *
     * @param discussPostId
     * @param comment
     * @return
     */
    @PostMapping("/create/{discussPostId}")
    @ApiOperation(value = "添加评论")
    public R create(@PathVariable("discussPostId") Long discussPostId, @RequestBody Comment comment) {
        ValidatorUtils.validateEntity(comment);
        // todo 做一个统一权限认证 token
//        comment.setUserId(); // 设置当前登录的用户 通过 token 获取用户的id
        // todo 触发评论事件


        if (comment.getEntityType() == ENTITY_TYPE_POST) {
            // todo 当前是针对帖子就出发帖子事件
        }
        return R.ok().put("评论成功");
    }

    /**
     * * userId - 用户id(回答用户id)
     * * curPage 当前页数
     * * limit  每页条数
     * * commentStatus 评论状态  1 删除 or 0 未删  -- > 暂且不考虑
     *
     * @param entityId
     * @param entityType 1 为帖子评论 2 为评论的评论
     * @param curPage
     * @param limit
     * @return
     */
    @GetMapping("/list/{entityType}/{entityId}")
    @ApiOperation(value = "查看帖子的评论 或者 评论的评论")
    public R list(@PathVariable("entityType") Integer entityType,
                  @PathVariable("entityId") Long entityId,
                  @RequestParam Integer curPage,
                  @RequestParam Integer limit) {
        return commentService.getPageComments(entityType, entityId, curPage, limit);
    }


}
