package cuit.pymjl.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/15 15:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    @Email(message = "用户名格式错误，必须为邮箱")
    private String userName;

    @Length(min = 6, max = 255, message = "密码至少为6个字符")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String verifyCode;

    @NotBlank(message = "验证标识不能为空")
    private String verifyKey;
}
