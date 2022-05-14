package cuit.pymjl.entity.pojo;


import lombok.Data;

/**
 * 获取OSS上传文件授权返回结果
 *
 * @author Pymjl
 * @date 2022/2/25 12:15
 */

@Data
public class OssPolicyResult {
    private String accessKeyId;
    private String policy;
    private String signature;
    private String dir;
    private String host;
    private String callback;
}
