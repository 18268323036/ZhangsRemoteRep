package com.cy.driver.common.enumer;

/**
 * @author zhangxy 2016/7/20
 */
public enum PackTypeEnum {
    /**
     * 包装类型（1塑料 2纸 3木（纤维） 4金属 5编织袋 6无 7其他）
     */

    PLASTIC(1,"塑料"),
    PAPER(2,"纸"),
    XYLON(3,"木"),
    METAL(4,"金属"),
    SACK(5,"编织袋"),
    NONE(6,"无"),
    OTHERS(7,"其他"),
    TARPAULIN(8,"篷布"),
    ;

    private int code;
    private String type;


    PackTypeEnum(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public static PackTypeEnum get(int code){
        for (PackTypeEnum ptm : PackTypeEnum.values()){
            if(ptm.getCode()==code){
                return ptm;
            }
        }
        return null;
    }
}
