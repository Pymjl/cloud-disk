package cuit.pymjl.controller;


import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.StrUtil;
import cuit.pymjl.constant.ResultEnum;
import cuit.pymjl.entity.dto.UserDTO;
import cuit.pymjl.entity.dto.UserInfoDTO;
import cuit.pymjl.entity.vo.UserVO;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.result.Result;
import cuit.pymjl.result.ResultUtil;
import cuit.pymjl.service.UserService;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @GetMapping("/codes/image/{key}")
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

    @GetMapping("/codes/email")
    public Result<String> sendEmailCode(@NotBlank(message = "key不能为空") @RequestParam("key") String uid,
                                        @NotBlank(message = "图片验证码不能为空") @RequestParam("code") String code,
                                        @Email(message = "用户名格式异常,用户名必须为邮箱") @RequestParam("username") String email) {
        //将key作为结果返回
        System.out.println(uid);
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

    @GetMapping("/user/info")
    public Result<UserVO> getMyUserInfo(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        UserVO userVO = userService.queryUserById(Long.parseLong(userId));
        return ResultUtil.success(userVO);
    }

    @PatchMapping("/user/avatar")
    public Result<String> updateAvatar(HttpServletRequest request,
                                       @NotNull(message = "文件不能为空") @RequestParam("file") MultipartFile file) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        String avatar = userService.updateAvatar(Long.parseLong(userId), file);
        return ResultUtil.success(avatar);
    }

    @PatchMapping("/user/nickname/{name}")
    public Result<String> updateNickname(HttpServletRequest request,
                                         @PathVariable("name") String name) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        userService.updateNickname(Long.parseLong(userId), name);
        return ResultUtil.success();
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        check(token);
        userService.logout(token);
        return ResultUtil.success();
    }

    @PatchMapping("/user/password/{verifyKey}/{verifyCode}")
    public Result<String> resetPassword(HttpServletRequest request,
                                        @PathVariable("verifyKey") String verifyKey,
                                        @PathVariable("verifyCode") String verifyCode) {
        String userId = (String) request.getAttribute("userId");
        String token = (String) request.getAttribute("token");
        check(userId, token);
        userService.resetPassword(verifyKey, verifyCode, token, Long.parseLong(userId));
        return ResultUtil.success(true, "重置密码成功，请重新登录");
    }

    @PatchMapping("/user/password")
    public Result<String> updatePassword(@Length(min = 6, max = 255, message = "密码长度至少为6位") @RequestParam("password") String password,
                                         HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        userService.updatePassword(password, Long.parseLong(userId));
        return ResultUtil.success();

    }

    /**
     * 检查
     *
     * @param data 数据
     */
    private void check(String... data) {
        for (String datum : data) {
            if (StrUtil.isBlank(datum)) {
                throw new AppException("发生了未知错误，参数异常，请联系管理员或稍后重试");
            }
        }
    }

}

