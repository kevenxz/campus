package com.keven.campus.controller;

import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.Msg;
import com.keven.campus.common.validator.ValidatorUtils;
import com.keven.campus.entity.Tag;
import com.keven.campus.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 用户个性标签
 *
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/tag")
@Api(tags = {"用户个性标签"})
public class TagController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TagController.class);
    @Autowired
    private TagService tagService;


    /**
     * 新增标签
     *
     * @param tag
     * @return
     */
    @PostMapping("/create")
    @ApiOperation(value = "新增")
    public R createTag(@RequestBody Tag tag) {

        ValidatorUtils.validateEntity(tag);
        tagService.save(tag);


        return R.ok(Msg.MSG_CREATE.getMsg());
    }


    /**
     * 获取所有组的所有标签
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取所有组的所有标签")
    public R list(@RequestParam Map<String, Object> params) {

        return R.ok().put(tagService.queryPage(params));
    }

    /**
     * 删除标签
     *
     * @param tagIds
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除标签")
    public R delete(@RequestBody List<Long> tagIds) {

        tagService.deleteBatch(tagIds);
        return R.ok(Msg.MSG_DELETE.getMsg());
    }


}
