package cn.mnay.common.exception;


import cn.mnay.common.enums.error.CodeEnum;

public class BusinessException extends BaseException {

    public BusinessException() {
        super(CodeEnum.INTERFACE_CALL_ERROR.getCode(), CodeEnum.INTERFACE_CALL_ERROR.description());
    }

    public BusinessException(CodeEnum codeEnum) {
        super(codeEnum.getCode(), codeEnum.description());
    }

    public BusinessException(CodeEnum codeEnum, String msg) {
        super(codeEnum.getCode(), codeEnum.description() + ": [" + msg + "]");
    }

    public BusinessException(int codeInt, String errorMsg) {
        super(codeInt, errorMsg);
    }
}
