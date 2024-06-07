package cn.mnay.common.enums.base;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BooleanEnum implements BaseJsonEnum<BooleanEnum> {

    YES(1, "是"),
    NO(0, "否"),
    UNKNOWN(-1, "未知"),
    ;


    private final Integer code;
    private final String description;

    public static BooleanEnum getByValue(String stringValue) {
        for (BooleanEnum booleanEnum : BooleanEnum.values()) {
            if (booleanEnum.description().equals(stringValue)) {
                return booleanEnum;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String description() {
        return this.description;
    }

    @Override
    public int getCode() {
        return this.code;
    }

}
