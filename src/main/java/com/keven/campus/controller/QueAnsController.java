package com.keven.campus.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.keven.campus.common.utils.PageUtils;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.IsDelete;
import com.keven.campus.common.utils.enums.Msg;
import com.keven.campus.common.validator.ValidatorUtils;
import com.keven.campus.common.validator.group.AddGroup;
import com.keven.campus.common.validator.group.UpdateGroup;
import com.keven.campus.entity.QueAns;
import com.keven.campus.service.QueAnsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 匿名问 答
 *
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/queans")
@Api(tags = {"匿名提问"})
public class QueAnsController {


    @Autowired
    private QueAnsService queAnsService;

    @PostMapping("/create")
    @ApiOperation("创建提问")
    public R create(@RequestBody QueAns queAns) {
        ValidatorUtils.validateEntity(queAns, AddGroup.class);
        if (StringUtils.isBlank(queAns.getQuestionNickname())) {
            queAns.setQuestionNickname("无名氏");
        }
        queAns.setQuestionTime(new Date());
        queAns.setIsDeleted(0);
        queAnsService.save(queAns);
        //TODO 需要做消息的推送
        return R.ok();
    }

    /**
     * userId - 用户id(回答用户id)
     * curPage 当前页数
     * limit  每页条数
     * isDelete 1 , 0 是否是删除的
     *
     * @param params
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询当前用户收到的所有提问")
    public R list(@RequestParam Map<String, Object> params) {

        //
        PageUtils page = queAnsService.queryPage(params);
        return R.ok().put(page);
    }

    @PostMapping("/delete/{queAnsId}")
    @ApiOperation("删除当前提问")
    public R delete(@PathVariable("queAnsId") Long queAnsId) {
        QueAns queAns = QueAns.builder()
                .isDeleted(IsDelete.IS_DELETE.getTYPE())
                .id(queAnsId).build();
        queAnsService.updateById(queAns);
        return R.ok(Msg.MSG_CREATE.getMsg());

    }

    @PostMapping("/update")
    @ApiOperation("回答对应的提问")
    public R update(@RequestBody QueAns queAns) {
        // TODO 做权限 ， 当前回答的用户只能回答提问自己的 获取当前用户id
        ValidatorUtils.validateEntity(queAns, UpdateGroup.class);
        queAns.setAnswerTime(new Date());
//        queAnsService.updateByIdAndAnsId(queAns);
        return R.ok();
    }
}
