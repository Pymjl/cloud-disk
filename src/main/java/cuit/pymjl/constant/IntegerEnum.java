package cuit.pymjl.constant;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/15 18:11
 **/
public enum IntegerEnum {
    /**
     * 初始的空间大小，默认0 MB
     */
    INITIAL_SPACE_SIZE(0),
    /**
     * 用户的最大空间大小
     */
    MAX_SPACE_SIZE(2048);
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
