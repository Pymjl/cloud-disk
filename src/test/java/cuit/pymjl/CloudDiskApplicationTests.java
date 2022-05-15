package cuit.pymjl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.digest.SM3;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cuit.pymjl.service.UserService;
import cuit.pymjl.util.AliyunUtils;
import cuit.pymjl.util.JwtUtils;
import cuit.pymjl.util.PasswordUtils;
import cuit.pymjl.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.UUID;

@SpringBootTest
class CloudDiskApplicationTests {

    @Resource
    DataSource dataSource;

    @Resource
    RedisUtil redisUtil;

    @Resource
    UserService userService;

    @Test
    void contextLoads() throws SQLException {
        System.out.println(dataSource.getConnection());
    }

    @Test
    void testRedis() {
//        redisUtil.set("k1", "v1");
//        System.out.println(redisUtil.get("k1"));
        System.out.println(redisUtil.del("k2"));
    }

    @Test
    void testImage() {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 6, 40);
        //CircleCaptcha captcha = new CircleCaptcha(200, 100, 4, 20);
        //图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write("C:\\Users\\Admin\\Pictures\\Camera Roll\\circle.png");
        System.out.println(captcha.getCode());
        //验证图形验证码的有效性，返回boolean值
        System.out.println(captcha.verify("1234"));
    }

    @Test
    void testMail() {
        MailUtil.send("pymjl@qq.com", "测试", "邮件来自Hutool测试", false);
    }

    @Test
    void testPassword() {
        String encrypt = PasswordUtils.encrypt("12346");
        System.out.println(encrypt);
        System.out.println(PasswordUtils.match("123456", encrypt));
    }

    @Test
    void testAliyunUtil() {
        AliyunUtils.makeDir("test/testDir/");
    }

    @Test
    void testJwt() {
        System.out.println(JwtUtils.generateToken(1L, "pymjl"));
    }

    @Test
    void testUser() {
        System.out.println(userService.queryUserById(1L));
    }


}
