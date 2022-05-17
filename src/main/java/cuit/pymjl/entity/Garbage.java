package cuit.pymjl.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/17 15:39
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_garbage")
public class Garbage implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键，唯一，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 阿里云OSS对象名（即路径）
     */
    private String filePath;

    /**
     * 拥有者的id
     */
    private Long ownerId;

    /**
     * 类型
     */
    private String type;

    /**
     * 文件大小，默认0，单位byte
     */
    private Long size;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 删除时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
