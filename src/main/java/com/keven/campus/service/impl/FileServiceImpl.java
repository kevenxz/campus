package com.keven.campus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keven.campus.entity.File;
import com.keven.campus.service.FileService;
import com.keven.campus.mapper.FileMapper;
import org.springframework.stereotype.Service;

/**
 * @author
 * @description 针对表【t_campus_file(文件表)】的数据库操作Service实现
 * @createDate 2023-01-12 14:26:48
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File>
        implements FileService {

}




