package cuit.pymjl.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Pymjl
 * @date 2022/2/24 22:33
 */
@Data
public class FileVO {
    private Long id;
    private String type;
    @JsonProperty("name")
    private String fileName;
    private String link;
    private Date createTime;
    private Date updateTime;
    private Long size;
}
