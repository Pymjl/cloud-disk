package cuit.pymjl.entity.pojo;

import lombok.Data;

/**
 * @author Pymjl
 * @date 2022/2/24 21:18
 */
@Data
public class StsMessage {
    private String expiration;
    private String keyId;
    private String secret;
    private String token;
    private String requestId;
}
