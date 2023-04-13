package com.keven.campus.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.common.utils.R;
import com.keven.campus.common.utils.enums.ResultCode;
import com.keven.campus.entity.File;
import com.keven.campus.service.FileService;
import com.keven.campus.mapper.FileMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author
 * @description 针对表【t_campus_file(文件表)】的数据库操作Service实现
 * @createDate 2023-01-12 14:26:48
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
        implements FileService {
    @Override
    public R createList(List<File> files, Integer entityType, Long entityId) {
        if (ObjectUtil.hasNull(files, entityType, entityId)) {
            return R.error(ResultCode.RequestParamsNull);
        }
        // 设置文件的实体类型 和 实体id
        files = files.stream().peek(file -> {
            file.setEntityType(entityType);
            file.setBusinessId(entityId);
        }).collect(Collectors.toList());
        return saveBatch(files) ? R.ok() : R.error();
    }
}




