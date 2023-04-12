package com.keven.campus.controller;

import com.keven.campus.common.utils.R;
import com.keven.campus.common.validator.ValidatorUtils;
import com.keven.campus.common.validator.group.AddGroup;
import com.keven.campus.entity.Follow;
import com.keven.campus.service.FollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@Api(tags = {"关注功能"})
@RequestMapping("/follow")
public class FollowController {


    @Autowired
    private FollowService followService;

    @ApiOperation("关注实体")
    @PostMapping("/{isFollow}")
    public R follow(@RequestBody Follow follow,
                    @ApiParam("是否关注") @PathVariable("isFollow") Boolean isFollow) {
        ValidatorUtils.validateEntity(follow, AddGroup.class);
        return followService.follow(follow, isFollow);
    }

    @ApiOperation("是否关注当前实体")
    @GetMapping("/or/not/{entityType}/{entityId}")
    public R isFollow(@PathVariable("entityId") Long entityId,
                      @PathVariable("entityType") Integer entityType) {
        return followService.isFollow(entityId, entityType);
    }

    @ApiOperation("获取某个实体的粉丝")
    @GetMapping("/getFans/{entityType}/{entityId}")
    public R getFans(@ApiParam("实体类型") @PathVariable("entityType") Integer entityType,
                     @ApiParam("实体Id") @PathVariable("entityId") Long entityId,
                     @ApiParam("当前页") Integer curPage,
                     @ApiParam("每页条数") Integer limit) {
        return followService.getFans(entityType, entityId, curPage, limit);
    }

    @ApiOperation("获取实体的关注")
    @GetMapping("/getFollows/{entityType}/{entityId}")
    public R getFollows(@ApiParam("实体类型") @PathVariable("entityType") Integer entityType,
                        @ApiParam("实体Id") @PathVariable("entityId") Long entityId,
                        @ApiParam("当前页") Integer curPage,
                        @ApiParam("每页条数") Integer limit) {
        return followService.getFollows(entityType, entityId, curPage, limit);
    }


}
