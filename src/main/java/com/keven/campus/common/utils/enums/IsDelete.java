package com.keven.campus.common.utils.enums;

import lombok.Getter;

/**
 * @author Keven
 * @version 1.0
 */
@Getter
public enum IsDelete {
    IS_DELETE(1),
    NO_DELETE(0);
    public final int TYPE;

    private IsDelete(int value) {
        this.TYPE = value;
    }
}
