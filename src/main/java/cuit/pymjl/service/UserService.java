package cuit.pymjl.service;

import cn.hutool.captcha.CircleCaptcha;
import cuit.pymjl.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
public interface UserService extends IService<User> {

    /**
     * 得到图像验证码
     * 获得图片验证码
     *
     * @param uid uid,全局唯一标识
     * @return {@code CircleCaptcha}
     */
    CircleCaptcha getImageVerificationCode(String uid);

    /**
     * 获得电子邮件验证代码
     *
     * @param uid   uid
     * @param code  代码
     * @param email 电子邮件
     * @return {@code String}
     */
    String getEmailVerifyCode(String uid, String code, String email);


}
