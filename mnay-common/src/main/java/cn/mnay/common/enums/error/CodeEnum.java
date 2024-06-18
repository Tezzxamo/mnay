package cn.mnay.common.enums.error;

import cn.mnay.common.enums.base.BaseEnum;

import java.util.HashMap;
import java.util.Map;

public enum CodeEnum implements BaseEnum {

    ////////////////////////////////////////////////////////////////////////////////////////////
    // 通用部分:0-10
    SUCCESS(0, "成功"),
    ERROR(1, "失败"),
    VALIDATION_ERROR(2, "入参校验异常："),
    SYS_INIT_ERROR(3, "系统初始化异常"),
    UNSUPPORTED_OPERATION(4, "不支持的操作"),


    ////////////////////////////////////////////////////////////////////////////////////////////
    INTERFACE_CALL_ERROR(400, "接口调用失败"),
    AUTH_NO_AUTH_ERROR(401, "没有权限执行操作"),
    // JWT token错误码
    JWT_ERROR(403, "JWT错误"),

    ////////////////////////////////////////////////////////////////////////////////////////////
    // 工具类错误码:2000-2100
    CONNECT_MINIO_ERROR(2000, "连接MinIO文件服务器失败"),
    MINIO_BUCKET_EXISTS_ERROR(2001, "查看存储bucket是否存在失败"),
    MINIO_BUCKET_CREATE_ERROR(2002, "创建存储bucket时失败"),
    MINIO_BUCKET_DELETE_ERROR(2003, "删除存储bucket时失败"),
    MINIO_BUCKET_LIST_ERROR(2004, "查看全部存储bucket时失败"),
    MINIO_OBJECT_DELETE_ERROR(2005, "文件删除失败"),
    MINIO_OBJECT_LIST_ERROR(2006, "查看文件对象列表时失败"),
    SM_ENCRYPT_ERROR(2007, "国密算法加密失败"),
    FILE_HASH_ERROR(2008, "计算文件hash失败"),
    VERIFICATION_CODE_NOT_EXPIRED(2009, "当前验证码未过期，请稍后再试"),
    FILE_NULL_ERROR(2010, "文件为空或文件名为空"),
    FILE_SIZE_OUT_OF_SUPPORTED_ERROR(2011, "文件大小超出支持范围"),
    GET_CURRENT_IP_ERROR(2012, "获取本机IP失败"),
    ROLE_UTIL_JUDGE_ERROR(2013, "角色判定异常"),

    ////////////////////////////////////////////////////////////////////////////////////////////
    // 用户相关错误码:3000-3099
    USER_GET_ERROR(3000, "获取用户失败或用户不存在!"),
    USER_NULL_ERROR(3001, "用户信息不能为空!"),
    USER_TOKEN_GET_ERROR(3002, "获取用户的登录令牌失败!"),
    USER_NAME_OR_PWD_ISNULL(3003, "用户名或密码为空!"),
    USER_LOGOUT_NO_AUTH_ERROR(3004, "没有登出权限!"),
    USER_EXISTS_ERROR(3005, "用户创建使用的邮箱已经存在，无法创建!"),
    USER_LOGIN_ERROR(3006, "用户名或密码错误，请重新输入!"),
    USER_LOCKED_ERROR(3007, "用户已经被锁定，请联系管理员!"),
    USER_PASSWORD_DECODE_ERROR(3008, "密码解码失败!"),
    USER_PASSWORD_SIZE_ERROR(3009, "密码格式有误，请重新输入！"),
    USER_PASSWORD_RESET_ERROR(3009, "密码重置失败"),
    USER_CREATE_ERROR(3010, "用户创建失败!"),
    USER_LOGIN_CONFLICT_ERROR(3011, "登录冲突"),
    CURRENT_MEMBER_INFO_GET_ERROR(3012, "获取当前登录用户信息异常"),
    USER_INFO_ERROR(3013, "用户信息有误"),

    ////////////////////////////////////////////////////////////////////////////////////////////
    // 权限相关错误码:5000-5999
    PERMISSION_GET_ERROR(5000, "获取权限信息失败"),
    PERMISSION_COULD_NOT_GRANT_ERROR(5001, "无法授权，请检查当前角色或授予权限"),
    PERMISSION_RESOURCE_ASSEMBLE_ERROR(5002, "组装资源名称失败"),


    ////////////////////////////////////////////////////////////////////////////////////////////

    DOCUMENT_PREVIEW_ERROR(6000, "预览文件信息失败"),
    DOCUMENT_QUERY_ERROR(6001, "查询文件信息失败"),
    DOCUMENT_UPDATE_ERROR(6002, "修改文件信息失败"),
    DOCUMENT_DOWNLOAD_ERROR(6003, "下载文件信息失败"),
    DOCUMENT_FINISH_ERROR(6004, "整理文件信息失败"),
    DOCUMENT_DELETE_ERROR(6005, "删除文件信息失败"),
    DOCUMENT_UPLOAD_ERROR(6006, "上传文件信息失败"),
    DOCUMENT_NULL_ERROR(6007, "文件为空或文件名为空"),
    DOCUMENT_SIZE_OUT_OF_SUPPORTED_ERROR(6008, "文件大小超出支持范围"),

    ////////////////////////////////////////////////////////////////////////////////////////////
    // 特殊情况：
    UNKNOWN_ABNORMAL(9999, "未知异常"),
    NULL_POINTER_ERROR(9998, "发生空指针异常，请联系开发排查问题，错误详情："),


    ;

    private static final Map<Integer, CodeEnum> CODE_ENUM_MAP = new HashMap<>();

    static {
        for (CodeEnum a : CodeEnum.values()) {
            CODE_ENUM_MAP.put(a.getCode(), a);
        }
    }

    private final int code;
    private final String description;

    /**
     * 构造方法
     *
     * @param code        code
     * @param description 描述信息
     */
    CodeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getMsgByCodeInt(int codeInt) {
        for (CodeEnum e : CodeEnum.values()) {
            if (e.getCode() == codeInt) {
                return e.description;
            }
        }
        throw new IllegalArgumentException("未定义的code码:" + codeInt);
    }

    /**
     * 通过code获取CodeEnum
     *
     * @param code code
     * @return CodeEnum
     */
    public static CodeEnum getEnumByCode(Integer code) {
        return CODE_ENUM_MAP.get(code);
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public int getCode() {
        return code;
    }
}
