package cuit.pymjl.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/15 23:58
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    /**
     * 主键ID，自增
     */
    private Long id;

    /**
     * 用户名，学生邮箱,唯一，登录输入的账号
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 身份，默认0，0为用户，1为管理员
     */
    private Integer identity;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
