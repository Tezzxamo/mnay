package cn.mnay.common.enums.auth;

import cn.mnay.common.enums.base.BaseJsonEnum;

public enum MemberTypeEnum implements BaseJsonEnum<MemberTypeEnum> {

    /**
     * 管理员用户:如果判断是管理员用户，则拥有所有权限
     */
    ADMIN(1, "管理员用户"),
    /**
     * 普通用户：关联了部门的用户，可以查看自己所在部门
     */
    NORMAL(2, "普通用户"),
    /**
     * 初始化用户：初始化用户，没有关联部门，也不是管理员用户，也不是脚本用户，该类型用户不能做任何事情，需要等待对应部门/组织负责人进行分配部门和角色；
     */
    INIT(3, "初始化用户"),
    /**
     * 脚本用户：特殊用户；
     */
    SCRIPT(4, "脚本用户");

    private final int code;
    private final String description;

    MemberTypeEnum(final int code, final String description) {
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
