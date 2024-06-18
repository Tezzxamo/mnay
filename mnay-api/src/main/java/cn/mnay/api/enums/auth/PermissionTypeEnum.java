package cn.mnay.api.enums.auth;

import cn.mnay.common.enums.base.BaseJsonEnum;

public enum PermissionTypeEnum implements BaseJsonEnum<PermissionTypeEnum> {

    // 权限分类：
    //  ①页面权限(菜单权限)
    //  ②资源操作权限
    PERMISSION_PAGE(1, "页面权限"),
    PERMISSION_RESOURCE(2, "资源操作权限");

    private final int code;
    private final String description;

    PermissionTypeEnum(final int code, final String description) {
        this.description = description;
        this.code = code;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public int getCode() {
        return this.code;
    }
}
