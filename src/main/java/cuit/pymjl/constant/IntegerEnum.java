package cuit.pymjl.constant;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/15 18:11
 **/
public enum IntegerEnum {
    /**
     * 零
     */
    ZERO(0);
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    IntegerEnum() {
    }

    IntegerEnum(Integer value) {
        this.value = value;
    }
}
