package com.cy.driver.common.enumer;

/**
 * Created by nixianjing on 17/8/18.
 */
public enum UtmsBusinessEventEnum {

    /**
     * Freight-运费、Recharge-充值、Withdrawal-提现
     */
    FREIGHT("Freight", "运费"),
    WITHDRAWAL("Withdrawal", "提现"),
    PROC_FEE("ProcFee", "手续费"),
    ;

    private String code;
    private String name;

    private UtmsBusinessEventEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static UtmsBusinessEventEnum convert(String code) {
        if (code == null || "".equals(code)) {
            return null;
        }
        for (UtmsBusinessEventEnum item : UtmsBusinessEventEnum.values()) {
            if (code.equals(item.getCode())) {
                return item;
            }
        }
        return null;
    }

    public static boolean contain(String code) {
        if (code == null || "".equals(code)) {
            return false;
        }
        for (UtmsBusinessEventEnum item : UtmsBusinessEventEnum.values()) {
            if (code.equals(item.getCode())) {
                return true;
            }
        }
        return false;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
