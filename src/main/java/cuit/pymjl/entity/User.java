package cuit.pymjl.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名，学生学号,唯一，登录输入的账号
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 邮箱账号
     */
    private String email;

    /**
     * 电话，可为空
     */
    private String phone;

    /**
     * 该用户已经使用的云盘空间，为后面扩展做可能
     */
    private Long usedSpace;

    /**
     * 该用户所拥有的最大的云盘空间，为后面扩展做可能
     */
    private Long maxSpace;

    /**
     * 身份，默认0，0为用户，1为管理员
     */
    private Integer identity;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}
