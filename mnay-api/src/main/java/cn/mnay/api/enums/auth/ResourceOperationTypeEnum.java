package cn.mnay.api.enums.auth;

import cn.mnay.common.enums.base.BaseJsonEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public enum ResourceOperationTypeEnum implements BaseJsonEnum<ResourceOperationTypeEnum> {

    SCAN(1, "扫描", "SCAN"),
    SYNC(2, "同步", "SYNC"),
    UPLOAD(3, "上传", "UPLOAD"),
    FINISH(4, "整理", "FINISH"),
    PUSH_FINISH(5, "推送至整理库", "PUSH_FINISH"),
    PUSH_OTHER_SYS(6, "推送至其他系统", "PUSH_OTHER_SYS"),
    PREVIEW(7, "预览", "PREVIEW"),
    VIEW(8, "查看", "VIEW"),
    DOWNLOAD(9, "下载", "DOWNLOAD"),
    PRINT(10, "打印", "PRINT"),
    CREATE(11, "创建", "CREATE"),
    UPDATE(12, "更新", "UPDATE"),
    CHECK(13, "校验", "CHECK"),   // 指申请校验的权限
    VERIFY(14, "鉴定", "VERIFY"), // 审批员应该有的权限

    ;
    private static final Map<Integer, ResourceOperationTypeEnum> ENUM_CODE_MAP = new HashMap<>();

    static {
        for (ResourceOperationTypeEnum u : ResourceOperationTypeEnum.values()) {
            ENUM_CODE_MAP.put(u.getCode(), u);
        }
    }

    private final int code;
    private final String description;
    @Getter
    private final String matcher;

    ResourceOperationTypeEnum(final int code, final String description, final String matcher) {
        this.description = description;
        this.code = code;
        this.matcher = matcher;
    }

    public static ResourceOperationTypeEnum getEnumByCode(int code) {
        return ENUM_CODE_MAP.get(code);
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    @JsonValue
    public int getCode() {
        return this.code;
    }
}
