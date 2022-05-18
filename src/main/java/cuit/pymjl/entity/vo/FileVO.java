package cuit.pymjl.entity.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Pymjl
 * @date 2022/2/24 22:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileVO {
    private String type;
    @JsonProperty("name")
    private String fileName;
    private String link;
}
