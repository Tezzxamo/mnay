package cn.mnay.common.model.dto.common;

import cn.mnay.common.enums.base.BaseEnum;
import io.swagger.v3.oas.annotations.Hidden;

import java.util.HashMap;
import java.util.Map;

/**
 * 继承该类，指明子类是一个查询条件
 * 注：不在swagger中对外暴露
 */
@Hidden
public class BaseCriteria {

    public enum OPEnum implements BaseEnum {
        EQ(1, "等于"),
        NEQ(2,"不等于"),
        GT(3, "大于"),
        GT_OR_EQ(4,"大于等于"),
        LT(5,"小于"),
        LT_OR_EQ(6,"小于等于"),
        START(7,"模糊查询-以...开始"),
        END(8,"模糊查询-以...结束"),
        CONTAINS(9,"模糊查询-包含"),
        BETWEEN(10, "区间"),
        ;

        private final int code;
        private final String description;

        OPEnum(int code, String description) {
            this.code = code;
            this.description = description;
        }

        @Override
        public String description() {
            return this.description;
        }

        @Override
        public int getCode() {
            return code;
        }

        public static String getMsgByCodeInt(int codeInt) {
            for (OPEnum e : OPEnum.values()) {
                if (e.getCode() == codeInt) {
                    return e.description;
                }
            }
            throw new IllegalArgumentException("未定义的code码:" + codeInt);
        }

        private static final Map<Integer, OPEnum> OP_ENUM_MAP = new HashMap<>();

        static {
            for (OPEnum a : OPEnum.values()) {
                OP_ENUM_MAP.put(a.getCode(), a);
            }
        }

        /**
         * 通过code获取OPEnum
         *
         * @param code code
         * @return OPEnum
         */
        public static OPEnum getEnumByCode(Integer code) {
            return OP_ENUM_MAP.get(code);
        }

    }

}
