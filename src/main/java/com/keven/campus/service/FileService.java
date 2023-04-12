package com.keven.campus.service;

import com.keven.campus.common.utils.R;
import com.keven.campus.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author
 * @description 针对表【t_campus_file(文件表)】的数据库操作Service
 * @createDate 2023-01-12 14:26:48
 */
public interface FileService extends IService<File> {

    /**
     * 根据实体类型和实体id 确定文件所属
     *
     * @param files
     * @param entityType
     * @param entityId
     * @return
     */
    R createList(List<File> files, Integer entityType, Long entityId);
}
