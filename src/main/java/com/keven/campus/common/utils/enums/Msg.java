package com.keven.campus.common.utils.enums;

import lombok.Getter;

/**
 * @author Keven
 * @version 1.0
 */
@Getter
public enum Msg {
    MSG_DELETE("删除成功"),
    MSG_UPDATE("更新成功"),
    MSG_CREATE("新增成功"),
    MSG_DISCUSSPOST("发布成功");
    public final String msg;

    private Msg(String msg) {
        this.msg = msg;
    }
}
