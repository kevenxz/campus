package com.keven.campus.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.Msg;
import com.keven.campus.common.validator.ValidatorUtils;
import com.keven.campus.common.validator.group.AddGroup;
import com.keven.campus.common.validator.group.UpdateGroup;
import com.keven.campus.entity.Topic;
import com.keven.campus.service.DiscussPostService;
import com.keven.campus.service.TopicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 帖子话题
 *
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/topic")
@Api(tags = {"帖子话题"})
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private DiscussPostService discussPostService;

    @PostMapping("/create")
    @ApiOperation(value = "新增话题")
    public R createTopic(@RequestBody Topic topic) {
        ValidatorUtils.validateEntity(topic, AddGroup.class);
        topicService.save(topic);
        return R.ok(Msg.MSG_CREATE.getMsg()).put(topic);
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新话题")
    public R updateTopic(@RequestBody Topic topic) {
        ValidatorUtils.validateEntity(topic, UpdateGroup.class);
        topicService.updateById(topic);
        return R.ok(Msg.MSG_UPDATE.getMsg());
    }


    /**
     * 话题详情 显示话题信息和话题中的帖子
     * 分页查询对应的东西
     *
     * @param topicId
     * @return
     */
    @GetMapping("/info/{topicId}")
    @ApiOperation(value = "话题详情")
    public R info(@PathVariable("topicId") Long topicId, Integer curPage, Integer limit) {
        return topicService.getInfoAndDisPosts(topicId, curPage, limit);
    }

}
