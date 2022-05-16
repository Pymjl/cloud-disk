package cuit.pymjl.constant;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/15 18:02
 **/
public enum StringEnum {
    /**
     * TODO 用户默认头像,用fdfs
     */
    USER_DEFAULT_AVATAR("user/avatar/default/default.jpg"),
    USER_AVATAR_PREFIX("user/avatar/");
    private String value;

    StringEnum() {
    }

    StringEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
