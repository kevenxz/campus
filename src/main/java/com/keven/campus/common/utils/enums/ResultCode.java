package com.keven.campus.common.utils.enums;

/**
 * @author Keven
 * @version 1.0
 */
public enum ResultCode {
    Success(200, "成功"),
    Exception(1, "异常"),
    RuntimeException(2, "网络异常"),
    RuntimeExceptionForGlobal(3, "网络异常，请稍后重试"),
    LoginTimeout(401, "登录已失效，请重新登录"),

    InvalidRequest(-2, "无效请求"), // 无token

    MessageCodeError(122, "短信验证码不对"),
    RequestUrlRepeat(333, "请求路径重复有误，请检查"),
    RequestParamsNull(411, "请求参数为空"),
    SelectDataIsNull(412, "查询数据不存在，请核对"),


    // 文件管理
    UploadFileIsNull(131, "上传文件为空"),
    UploadFileFailed(133, "失败上传"),
    FileSizeOverLimit(134, "文件大小超过限制"),
    FileUnFound(135, "文件不存在"),


    NullData(151, "空数据"),
    OperationFailed(152, "操作失败"),
    ImportFailed(155, "导入失败，请检查excel数据是否正确"),
    ExportCreateDataFailed(156, "导出生成数据失败"),
    ExportNullDataFailed(157, "导出无数据"),
    IllegalData(158, "无效数据"),

    // 数据库操作
    AddException(171, "添加失败"),
    UpdateException(172, "修改失败"),
    DelException(173, "删除失败"),
    SelectException(174, "查询失败"),

    ;

    private final int code;
    private final String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getMsgForRemark(String remark) {
        return msg + ":" + remark;
    }
}