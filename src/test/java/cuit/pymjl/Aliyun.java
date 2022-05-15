package cuit.pymjl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/15 14:59
 **/
public class Aliyun {
    public static void main(String[] args) throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5t67HB3DcZyQhrDtfkCz";
        String accessKeySecret = "HabwxAzd3DL6D1UOJy1mlnBj4rk0lq";
        // 填写源Bucket名称。
        String sourceBucketName = "pymjl-cloud-disk";
        // 填写源Object的完整路径，完整路径中不能包含Bucket名称。
        String sourceKey = "test/dir/R-C.jpg";
        // 填写与源Bucket处于同一地域的目标Bucket名称。
        String destinationBucketName = "pymjl-cloud-disk";
        // 填写目标Object的完整路径。Object完整路径中不能包含Bucket名称。
        String destinationKey = "dest/dir/R-C.jpg";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 拷贝文件。
            CopyObjectResult result = ossClient.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
            System.out.println("ETag: " + result.getETag() + " LastModified: " + result.getLastModified());
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
