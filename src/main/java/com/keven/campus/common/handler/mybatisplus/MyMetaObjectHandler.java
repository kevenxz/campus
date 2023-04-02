package com.keven.campus.common.handler.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.keven.campus.common.utils.SecurityUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Keven
 * @version 1.0
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyMetaObjectHandler.class);

    /**
     * 插入时填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //第二 参数要和实体类中字段名一致，第三个参数字段类型要和实体类中字段类型一致，最后一个参数是待填入的数据
        LOGGER.info("start insert fill ....");
        Long userId = SecurityUtil.getUserId();
        this.strictInsertFill(metaObject, "createdTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updatedTime", Date.class, new Date());
        this.setFieldValByName("user_id", userId, metaObject);
    }

    /**
     * 更新时填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        LOGGER.info("start modify fill ....");
        this.strictInsertFill(metaObject, "updatedTime", Date.class, new Date());
    }
}
