package cuit.pymjl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsV2Request;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.OSSObjectSummary;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/18 14:44
 **/
public class Test {
    public static void main(String[] args) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5t67HB3DcZyQhrDtfkCz";
        String accessKeySecret = "HabwxAzd3DL6D1UOJy1mlnBj4rk0lq";
        // 填写源Bucket名称。
        String bucketName = "pymjl-cloud-disk";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 构造ListObjectsV2Request请求。
            ListObjectsV2Request listObjectsV2Request = new ListObjectsV2Request(bucketName);

            // 设置prefix参数来获取fun目录下的所有文件与文件夹。
            listObjectsV2Request.setPrefix("cloud-disk/files/1/foo");

            // 设置正斜线（/）为文件夹的分隔符。
            listObjectsV2Request.setDelimiter("/");

            // 发起列举请求。
            ListObjectsV2Result result = ossClient.listObjectsV2(listObjectsV2Request);

            // 遍历文件。
            System.out.println("Objects:");
            // objectSummaries的列表中给出的是fun目录下的文件。
            for (OSSObjectSummary objectSummary : result.getObjectSummaries()) {
                System.out.println(objectSummary.getKey());
            }

            // 遍历commonPrefix。
            System.out.println("\nCommonPrefixes:");
            // commonPrefixs列表中显示的是fun目录下的所有子文件夹。由于fun/movie/001.avi和fun/movie/007.avi属于fun文件夹下的movie目录，因此这两个文件未在列表中。
            for (String commonPrefix : result.getCommonPrefixes()) {
                System.out.println(commonPrefix);
            }
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
