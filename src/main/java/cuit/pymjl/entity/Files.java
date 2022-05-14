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
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_files")
public class Files implements Serializable {

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
     * 文件类型,dir或者file
     */
    private String fileType;

    /**
     * 拥有者的id
     */
    private Long ownerId;

    /**
     * 逻辑删除，默认0，1表示已被用户删除，但是还存在于回收站中
     */
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;


}
