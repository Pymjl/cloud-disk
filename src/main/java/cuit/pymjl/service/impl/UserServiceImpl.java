package cuit.pymjl.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cuit.pymjl.constant.IdentityEnum;
import cuit.pymjl.constant.IntegerEnum;
import cuit.pymjl.constant.StringEnum;
import cuit.pymjl.entity.User;
import cuit.pymjl.entity.dto.UserDTO;
import cuit.pymjl.entity.dto.UserInfoDTO;
import cuit.pymjl.entity.vo.UserVO;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.mapper.UserMapper;
import cuit.pymjl.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cuit.pymjl.util.AliyunUtils;
import cuit.pymjl.util.JwtUtils;
import cuit.pymjl.util.PasswordUtils;
import cuit.pymjl.util.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
     * 验证码过期时间,单位秒
     */
    private static final long EXPIRATION = 300;

    /**
     * 验证码长度
     */
    private static final int VERIFY_CODE_LENGTH = 6;

    /**
     * 图片验证码复杂度，如果太大图片验证码会很多干扰元素
     */
    private static final int VERIFY_CODE_COMPLEXITY = 30;

    /**
     * 图片验证码宽度
     */
    private static final int VERIFY_CODE_WIDTH = 200;

    /**
     * 图片验证码高度
     */
    private static final int VERIFY_CODE_HEIGHT = 100;


    @Override
    public CircleCaptcha getImageVerificationCode(String uid) {
        log.info("开始生成验证码.........");
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(VERIFY_CODE_WIDTH, VERIFY_CODE_HEIGHT,
                VERIFY_CODE_LENGTH, VERIFY_CODE_COMPLEXITY);
        //获取图片验证码的文本串
        String code = captcha.getCode();
        //将文本串放入redis,uid作为Key,code作为value
        redisUtil.set(uid, code, EXPIRATION);
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

    @Override
    public String login(UserDTO userDTO) {
        //验证验证码
        log.info("开始验证邮箱验证码==>[{}]......", userDTO.getVerifyCode());
        String code = (String) redisUtil.get(userDTO.getVerifyKey());
        redisUtil.del(userDTO.getVerifyKey());
        if (StrUtil.isBlank(code) || !code.equals(userDTO.getVerifyCode())) {
            throw new AppException("邮箱验证码错误");
        }
        log.info("邮箱验证码验证成功，开始校验用户账号......");
        //校验账号
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, userDTO.getUsername())
                .eq(User::getPassword, PasswordUtils.encrypt(userDTO.getPassword()));
        User user = baseMapper.selectOne(wrapper);
        if (BeanUtil.isEmpty(user)) {
            throw new AppException("用户名或密码错误，请重新输入");
        }
        //用户校验通过
        log.info("校验通过，开始发放token......");
        String token = JwtUtils.generateToken(user.getId(), user.getNickName());
        //token放入redis,过期时间为token过期时间
        redisUtil.set(token, user.getId().toString(), JwtUtils.getTokenExpiredTime());
        return token;
    }

    @Override
    public Boolean register(UserInfoDTO userInfoDTO) {
        //先验证邮箱验证码
        log.info("开始验证邮箱验证码......");
        String code = (String) redisUtil.get(userInfoDTO.getVerifyKey());
        redisUtil.del(userInfoDTO.getVerifyKey());
        if (null == code || !code.equals(userInfoDTO.getVerifyCode())) {
            throw new AppException("邮箱验证码错误");
        }
        //开始注册
        log.info("邮箱验证码注册成功，开始注册用户......");
        if (StrUtil.isBlank(userInfoDTO.getAvatar())) {
            userInfoDTO.setAvatar(StringEnum.USER_DEFAULT_AVATAR.getValue());
        }
        User user = User.builder()
                .username(userInfoDTO.getUsername())
                .nickName(userInfoDTO.getNickName())
                .password(PasswordUtils.encrypt(userInfoDTO.getPassword()))
                .avatar(userInfoDTO.getAvatar())
                .identity(IdentityEnum.USER.getIdentity())
                .maxSpace(Integer.toUnsignedLong(IntegerEnum.MAX_SPACE_SIZE.getValue()))
                .usedSpace(Integer.toUnsignedLong(IntegerEnum.INITIAL_SPACE_SIZE.getValue()))
                .build();
        return baseMapper.insert(user) == 1;
    }

    @Override
    public UserVO queryUserById(Long id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getId, id);
        User user = baseMapper.selectOne(wrapper);
        UserVO userVO = new UserVO();
        BeanUtil.copyProperties(user, userVO);
        userVO.setAvatar(AliyunUtils.findFileInfo(user.getAvatar()).getLink());
        return userVO;
    }

    /**
     * 得到随机字符串
     *
     * @param length 长度
     * @return {@code String}
     */
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
