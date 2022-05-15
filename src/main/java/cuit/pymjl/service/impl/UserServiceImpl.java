package cuit.pymjl.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.extra.mail.MailUtil;
import cuit.pymjl.entity.User;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.mapper.UserMapper;
import cuit.pymjl.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cuit.pymjl.util.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
@Service
@Log4j2
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    RedisUtil redisUtil;

    /**
     * 过期时间,单位秒
     */
    private static final long EXPIRATION = 300;

    /**
     * 验证码长度
     */
    private static final int VERIFY_CODE_LENGTH = 6;


    @Override
    public CircleCaptcha getImageVerificationCode(String uid) {
        log.info("开始生成验证码.........");
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, VERIFY_CODE_LENGTH, 30);
        //获取图片验证码的文本串
        String code = captcha.getCode();
        //将文本串放入redis,uid作为Key,code作为value
        redisUtil.set(uid, code);
        log.info("验证码生成成功");
        return captcha;
    }

    @Override
    public String getEmailVerifyCode(String uid, String code, String email) {
        //先验证图片验证码
        Object imageCode = redisUtil.get(uid);
        if (null == imageCode || "".equals(imageCode)) {
            throw new AppException("图片验证码错误,请刷新验证码后重试");
        }
        redisUtil.del(uid);
        if (!imageCode.equals(code)) {
            throw new AppException("图片验证码不匹配，请刷新验证码后重试");
        }
        log.info("图片验证码验证成功,开始生成邮箱验证码......");
        //开始生成邮箱验证码
        String verifyCode = getRandomString(VERIFY_CODE_LENGTH);
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        log.info("邮箱验证码为==>{},开始发送邮件......", verifyCode);
        MailUtil.send(email, "邮箱验证码",
                "亲爱的用户您好，您的邮箱验码是" + verifyCode + ",有效期为5分钟", false);
        redisUtil.set(key, verifyCode, EXPIRATION);
        return key;
    }

    //length用户要求产生字符串的长度
    @SuppressWarnings("all")
    private String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
