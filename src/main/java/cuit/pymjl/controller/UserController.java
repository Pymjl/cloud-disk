package cuit.pymjl.controller;


import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.StrUtil;
import cuit.pymjl.entity.dto.UserDTO;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.result.Result;
import cuit.pymjl.result.ResultUtil;
import cuit.pymjl.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
@RestController
@Validated
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("/get/image/{uid}")
    public void getImageCode(@NotBlank(message = "uid不能为空")
                             @PathVariable("uid") String uid, HttpServletResponse response)
            throws IOException {
//        if (uid == null || "".equals(uid)) {
//            throw new AppException("uid不能为空");
//        }
        //得到图片验证码
        CircleCaptcha circleCaptcha = userService.getImageVerificationCode(uid);
        //写回客户端
        ServletOutputStream outputStream = response.getOutputStream();
        circleCaptcha.write(outputStream);
        //关闭流
        if (outputStream != null) {
            outputStream.close();
        }
    }

    @GetMapping("/send/email")
    public Result<String> sendEmailCode(@NotBlank(message = "uid不能为空") @RequestParam("uid") String uid,
                                        @NotBlank(message = "图片验证码不能为空") @RequestParam("code") String code,
                                        @Email(message = "用户名格式异常,用户名必须为邮箱") @RequestParam("userName") String email) {
//        if (StrUtil.isBlank(uid) || StrUtil.isBlank(code) || StrUtil.isBlank(email)) {
//            throw new AppException("参数异常，某个参数为空或者为空字符串");
//        }
//        boolean isEmail = Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email);
//        if (!isEmail) {
//            throw new AppException("邮箱格式异常");
//        }
        //将key作为结果返回
        String key = userService.getEmailVerifyCode(uid, code, email);
        return ResultUtil.success(key);
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        return null;
    }

}

