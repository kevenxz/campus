package com.keven.campus.mapper;

import com.keven.campus.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author
 * @description 针对表【t_campus_file(文件表)】的数据库操作Mapper
 * @createDate 2023-01-12 14:26:48
 * @Entity com.keven.campus.entity.File
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

    int insertByForeach(List<File> files);
}




