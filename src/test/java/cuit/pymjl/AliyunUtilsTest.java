package cuit.pymjl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsV2Result;
import com.aliyun.oss.model.OSSObjectSummary;
import cuit.pymjl.constant.StringEnum;
import cuit.pymjl.util.AliyunUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/18 0:11
 **/
@SpringBootTest
public class AliyunUtilsTest {
    @Test
    void testMakeDir() {
        AliyunUtils.makeDir("test/sourceCopy");
    }

    @Test
    void testCopy() {
        AliyunUtils.copyFile("test/sourceCopy", "test/targetCopy");
    }

    @Test
    void testList() {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5t67HB3DcZyQhrDtfkCz";
        String accessKeySecret = "HabwxAzd3DL6D1UOJy1mlnBj4rk0lq";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "pymjl-cloud-disk";
        // 指定前缀，例如exampledir/object。
        String keyPrefix = "";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 列举文件。如果不设置keyPrefix，则列举存储空间下的所有文件。如果设置keyPrefix，则列举包含指定前缀的文件。
            ListObjectsV2Result result = ossClient.listObjectsV2(bucketName, keyPrefix);
            List<OSSObjectSummary> ossObjectSummaries = result.getObjectSummaries();

            for (OSSObjectSummary s : ossObjectSummaries) {
                System.out.println("\t" + s.getKey());
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

    @Test
    void testListAllPath() {
        AliyunUtils.listAllPath("cloud-disk/files/1/foo/").forEach(System.out::println);
    }

    @Test
    void testString() {
        String source = "test/dir/";
        String target = "test/des/";
        String file = "test/dir/cloud-disk/files/1/foo/bar/";
        System.out.println(target + file.replaceAll(source, ""));
    }

    @Test
    void testCopyFile() {
        AliyunUtils.copyFile("cloud-disk/files/1/foo/bar/regedit.exe",
                "cloud-disk/files/1/foo/bar/pymjl/regedit.exe");
    }

    @Test
    void testCopyFolder() {
        String path = "cloud-disk/files/1/foo/bar/default.jpg";
        String target = "cloud-disk/files/1/foo/dest/default.jpg";
        AliyunUtils.copyFolder(path, target);
    }

    @Test
    void testListFiles() {
        AliyunUtils.listFile("cloud-disk/files/1/foo").forEach(System.out::println);
    }

    @Test
    void testDeleteFile() {
        String prefix = "cloud-disk/files/1/foo/bar/default.jpg";
        System.out.println(prefix.substring(0, prefix.lastIndexOf("/") + 1));
    }

    @Test
    void testMakeFile() {
        AliyunUtils.makeFile(StringEnum.FILE_DEFAULT_PREFIX.getValue() + 1L + "/foo/bar/");
    }
}
