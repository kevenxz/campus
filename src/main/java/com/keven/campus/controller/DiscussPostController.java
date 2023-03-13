package com.keven.campus.controller;

import com.keven.campus.common.utils.Constant;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.Msg;
import com.keven.campus.common.validator.ValidatorUtils;
import com.keven.campus.common.validator.group.AddGroup;
import com.keven.campus.entity.DiscussPost;
import com.keven.campus.entity.File;
import com.keven.campus.entity.dto.DiscussPostDTO;
import com.keven.campus.service.DiscussPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/discussPost")
@Api(tags = {"帖子"})
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @PostMapping("/create")
    @ApiOperation("新增帖子")
    public R create(DiscussPostDTO discussPostDTO) {
        ValidatorUtils.validateEntity(discussPostDTO, AddGroup.class);
        return discussPostService.add(discussPostDTO);
    }


    @GetMapping("/detail/{discussPostId}")
    @ApiOperation("帖子详情")
    public R detail(@PathVariable("discussPostId") Long discussPostId) {
        return discussPostService.getPostDetail(discussPostId);
    }

    /**
     * @param topicId 主题id 有: 进行查询 无: 直接分页
     *                * userId - 用户id(回答用户id)
     *                * curPage 当前页数
     *                * limit  每页条数
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("贴子列表")
    public R list(Long topicId, Integer curPage, Integer limit, Integer type) {
        // todo  判断当前用户的权限 获取redis 中用户的type
        Map<String, Object> params = new HashMap<>();
        params.put(Constant.PAGE, curPage);
        params.put(Constant.LIMIT, limit);
        params.put("type", 0); // 放置当前的权限，通过权限判断是否查询已拉黑的帖子 0-普通用户 1-管理员 2-版主
        return discussPostService.queryPage(topicId, params);
    }

    /**
     * 删除帖子
     * 话题下的帖子数量也要减去
     *
     * @param discussPostId
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("删除帖子")
    public R delete(@RequestParam Long discussPostId, @RequestParam Long topicId) {

        if (discussPostId == null || discussPostId <= 0 || topicId == null || topicId <= 0) {
            return R.error("参数不为空");
        }
        discussPostService.updateStatus(topicId, discussPostId, 2);
        // 删除之后 elasticsearch 要删除对应的帖子
        // TODO 触发删帖 事件

        return R.ok(Msg.MSG_DELETE.getMsg());
    }

}
