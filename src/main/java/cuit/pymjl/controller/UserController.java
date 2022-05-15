package cuit.pymjl.controller;


import cn.hutool.captcha.CircleCaptcha;
import cuit.pymjl.constant.ResultEnum;
import cuit.pymjl.entity.dto.UserDTO;
import cuit.pymjl.entity.dto.UserInfoDTO;
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

    @GetMapping("/get/image/{key}")
    public void getImageCode(@NotBlank(message = "key不能为空")
                             @PathVariable("key") String uid, HttpServletResponse response)
            throws IOException {
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
    public Result<String> sendEmailCode(@NotBlank(message = "key不能为空") @RequestParam("key") String uid,
                                        @NotBlank(message = "图片验证码不能为空") @RequestParam("code") String code,
                                        @Email(message = "用户名格式异常,用户名必须为邮箱") @RequestParam("username") String email) {
        //将key作为结果返回
        String key = userService.getEmailVerifyCode(uid, code, email);
        return ResultUtil.success(key);
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserDTO userDTO) {
        String token = userService.login(userDTO);
        return ResultUtil.success(token);
    }

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody UserInfoDTO userInfoDTO) {
        userService.register(userInfoDTO);
        return ResultUtil.success(ResultEnum.REGISTER_SUCCESS);
    }

}

