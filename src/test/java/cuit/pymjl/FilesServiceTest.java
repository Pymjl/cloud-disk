package cuit.pymjl;

import cuit.pymjl.service.FilesService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

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
        filesService.updateFileName("foo/bar/update/test/default01.jpg", "foo/bar/update/test/default.jpg", 1L);
    }

    @Test
    void updateFolderName() {
        filesService.updateFolderName("foo/bar/update/test/", "foo/bar/update/dest/", 1L);
    }

    @Test
    void deleteFileOrDir() {
        filesService.deleteFileOrFolder("foo/bar/", 1L);
    }

    @Test
    void moveFile() {
        filesService.moveFile("foo/dest/default.jpg", "foo/bar/default.jpg", 1L);
    }

    @Test
    void moveFolder() {
        filesService.moveFolder("foo/bar/dest/", "foo/bar/", 1L);
    }

    @Test
    void deleteFileOrFolder() {
        filesService.deleteFileOrFolder("foo/bar/update/test/", 1L);
    }

    @Test
    void deleteFileForever() {
        filesService.deleteFileForever("foo/bar/update/test/", 1L);
    }

    @Test
    void addFolder() {
        filesService.addFolder("foo/ggg/", 1L);
    }

    @Test
    void copyFloder() {
        filesService.copyFileOrDirectory("foo/dest/", "foo/bar/", 1L);
    }

}