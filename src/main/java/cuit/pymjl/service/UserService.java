package cuit.pymjl.service;

import cn.hutool.captcha.CircleCaptcha;
import cuit.pymjl.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import cuit.pymjl.entity.dto.UserDTO;
import cuit.pymjl.entity.dto.UserInfoDTO;
import cuit.pymjl.entity.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 登录
     *
     * @param userDTO 用户dto
     * @return {@code String}
     */
    String login(UserDTO userDTO);

    /**
     * 注册
     *
     * @param userInfoDTO 用户信息dto
     * @return {@code Boolean}
     */
    @SuppressWarnings("all")
    Boolean register(UserInfoDTO userInfoDTO);

    /**
     * 根据用户ID查询用户
     *
     * @param id id
     * @return {@code UserVO}
     */
    UserVO queryUserById(Long id);

    /**
     * 更新《阿凡达》
     * 更新头像
     *
     * @param id   id
     * @param file 文件
     * @return {@code String}
     */
    String updateAvatar(Long id, MultipartFile file);


}
