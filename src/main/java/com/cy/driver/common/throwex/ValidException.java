package com.cy.driver.common.throwex;

/**
 * Created by wyh on 2016/3/30.
 */
public class ValidException extends RuntimeException {
    /** 业务逻辑 */
    public static final int LOGIC_ERROR = 0;
    /** 查询错误 */
    public static final int QUERY_ERROR = 1;
    /** 保存出错 */
    public static final int SAVE_ERROR = 1;

    private int code;

    public ValidException(int code, String message){
        super(message);
        this.code = code;
    }

    public boolean isLogicError(){
        if (code == LOGIC_ERROR) {
            return true;
        } else {
            return false;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
