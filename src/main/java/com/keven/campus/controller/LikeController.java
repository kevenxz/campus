package com.keven.campus.controller;

import com.keven.campus.common.utils.R;
import com.keven.campus.service.LikeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/like")
@Api(tags = {"点赞功能"})
public class LikeController {

    @Autowired
    private LikeService likeService;

    /**
     * 点赞
     *
     * @param entityType   实体类型（1、帖子 2、评论 ）
     * @param entityId     实体id
     * @param entityUserId 实体作者
     * @return {@link R}
     */
    @ApiOperation("点赞")
    @PostMapping("/{entityType}")
    public R like(@PathVariable("entityType") Integer entityType,
                  Long entityId,
                  Long entityUserId) {
        return likeService.like(entityType, entityId, entityUserId);
    }
}
