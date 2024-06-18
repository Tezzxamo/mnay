package cn.mnay.api.enums.auth;

import cn.hutool.core.collection.ListUtil;
import cn.mnay.common.enums.base.BaseJsonEnum;
import lombok.Getter;

import java.util.List;

public enum ResourceTypeEnum implements BaseJsonEnum<ResourceTypeEnum> {

    DOCUMENT(1, "文件", "DOCUMENT", ListUtil.toList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 13, 14)),
    ARCHIVE(2, "文件夹", "ARCHIVE", ListUtil.toList(6, 8, 9, 11, 12, 13, 14)),
    DEPARTMENT(3, "部门", "DEPARTMENT", ListUtil.toList(11, 12)),
    MEMBER(4, "成员", "MEMBER", ListUtil.toList(11, 12)),
    ROLE(5, "角色", "ROLE", ListUtil.toList(11, 12)),
    ;

    private final int code;
    private final String description;
    @Getter
    private final String matcher;
    @Getter
    private final List<Integer> operationList;      // 对应拥有的操作的code码


    ResourceTypeEnum(int code, String description, String matcher, List<Integer> operationList) {
        this.code = code;
        this.description = description;
        this.matcher = matcher;
        this.operationList = operationList;
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
