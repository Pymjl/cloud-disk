package cuit.pymjl.result;

import cuit.pymjl.constant.ResultEnum;

/**
 * @author Pymjl
 * @date 2022/2/26 18:44
 */
public class ResultUtil {
    public static Result<String> success() {
        return new Result<>(ResultEnum.OK);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, ResultEnum.OK);
    }

    public static Result<String> fail() {
        return new Result<>(ResultEnum.UNKNOWN_MISTAKE);
    }

    public static Result<String> fail(String message) {
        return new Result<>(false, message);
    }
}
