package cuit.pymjl;

import cuit.pymjl.service.FilesService;
import cuit.pymjl.util.AliyunUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/18 14:13
 **/
@SpringBootTest
class FilesServiceTest {
    @Resource
    FilesService filesService;

    @Test
    void queryFile() {
        System.out.println(filesService.queryFile("test/dir/R-C.jpg", 1L));

    }

    @Test
    void queryFiles() {
        filesService.queryFiles("foo/bar/", 1L).forEach(System.out::println);
    }

    @Test
    void updateFileName() {
        filesService.updateFileName("dest/bar/test3.webp", "dest/bar/test2.webp", 1L);
    }

    @Test
    void deleteFileOrDir() {
        filesService.deleteFileOrFolder("foo/bar/t0.png", 1L);
    }

    @Test
    void moveFile() {
        filesService.moveFile("foo/bar/pymjl/", "foo/bar/", 1L);
    }

    @Test
    void deleteFileForever() {
        String name = "cloud-disk/files/1/foo/bar/";
        if (name.contains(".")) {
            String fileName = name.substring(0, name.lastIndexOf("/") + 1);
            System.out.println(fileName);
        } else {
            System.out.println(name);
        }
    }

}