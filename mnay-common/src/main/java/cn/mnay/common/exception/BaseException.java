package cn.mnay.common.exception;

import cn.mnay.common.model.model.R;
import cn.mnay.common.model.model.RFactory;

public abstract class BaseException extends RuntimeException {
    private R<?> r;

    BaseException() {
        super();
    }

    BaseException(int codeInt, String msg) {
        super(msg);
        this.r = RFactory.newR(codeInt, msg);
    }

    public int getCodeInt() {
        return r.getCode();
    }

    public Object getResult() {
        return r.getResult();
    }

    public String getMsg() {
        return r.getMessage();
    }

    public R<?> getReturnMsg() {
        return r;
    }
}
