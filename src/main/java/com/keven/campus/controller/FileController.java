package com.keven.campus.controller;

import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.Msg;
import com.keven.campus.entity.File;
import com.keven.campus.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Keven
 * @version 1.0
 */
@RestController
@RequestMapping("/file")
@Api(tags = {"文件上传"})
public class FileController {

    @Autowired
    private FileService fileService;

    @ApiOperation("插入数据 ")
    @PostMapping("/create")
    public R create(@RequestBody List<File> files) {

        // todo 获取当前登录的人
        if (!fileService.saveBatch(files)) {
            return R.error().put("插入失败");
        }
        return R.ok();
    }


}
