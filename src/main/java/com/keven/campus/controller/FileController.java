package com.keven.campus.controller;

import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.Msg;
import com.keven.campus.entity.File;
import com.keven.campus.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @PostMapping("/create/{entityType}/{entityId}")
    public R create(@RequestBody List<File> files,
                    @ApiParam("实体类型") @PathVariable("entityType") Integer entityType,
                    @ApiParam("实体id") @PathVariable("entityId") Long entityId) {
        return fileService.createList(files, entityType, entityId);
    }


}
