package cn.mnay.common.jpa.comment.enums;

public enum DbTypeEnum implements IEnum{

    /**
     * 各个数据库的枚举值
     */
    MYSQL {
        @Override
        public String getCode() {
            return "1";
        }

        @Override
        public String getValue() {
            return "MYSQL";
        }
    },
    SQLSERVER {
        @Override
        public String getCode() {
            return "2";
        }

        @Override
        public String getValue() {
            return "MICROSOFT SQL SERVER";
        }
    },
    ORACLE {
        @Override
        public String getCode() {
            return "3";
        }

        @Override
        public String getValue() {
            return "ORACLE";
        }
    },
    POSTGRESQL {
        @Override
        public String getCode() {
            return "4";
        }

        @Override
        public String getValue() {
            return "POSTGRESQL";
        }
    };

    DbTypeEnum() {
    }

}
