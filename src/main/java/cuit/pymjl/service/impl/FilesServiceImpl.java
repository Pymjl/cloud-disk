package cuit.pymjl.service.impl;

import cuit.pymjl.constant.StringEnum;
import cuit.pymjl.entity.File;
import cuit.pymjl.entity.vo.FileVO;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.mapper.FilesMapper;
import cuit.pymjl.service.FilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cuit.pymjl.util.AliyunUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
@Service
@Slf4j
public class FilesServiceImpl extends ServiceImpl<FilesMapper, File> implements FilesService {

    private final static String SEPARATOR = "/";

    @Override
    public void upload(MultipartFile file, Long userId, String path) {
        log.info("开始上传文件........");
        String name = file.getOriginalFilename();
        log.info("文件名为==>[{}]", name);
        while (path != null && path.startsWith(SEPARATOR)) {
            path = path.substring(1, path.length());
        }
        String objectName = StringEnum.FILE_DEFAULT_PREFIX.getValue() + userId + SEPARATOR + path + name;

        //上传文件到阿里云OSS
        log.info("文件的完整路径为==>[{}],开始上传文件到阿里云OSS...", objectName);
        AliyunUtils.upload(objectName, file);
        log.info("文件上传成功");
    }

    @Override
    public FileVO queryFile(String path, Long userId) {
        while (path.startsWith(SEPARATOR)) {
            path = path.substring(1);
        }
        //拼接完整的文件名
        String objectName = StringEnum.FILE_DEFAULT_PREFIX.getValue() + userId + SEPARATOR + path;
        log.info("完整的路径名为==>[{}]", objectName);
        FileVO fileInfo = AliyunUtils.findFileInfo(objectName);
        fileInfo.setFileName(fileInfo.getFileName().substring(fileInfo.getFileName().lastIndexOf("/") + 1));
        return fileInfo;
    }

    @Override
    public List<FileVO> queryFiles(String path, Long userId) {
        while (path.startsWith(SEPARATOR)) {
            path = path.substring(1);
        }
        path = StringEnum.FILE_DEFAULT_PREFIX.getValue() + userId + SEPARATOR + path;
        if (!path.endsWith(SEPARATOR)) {
            path += SEPARATOR;
        }
        log.info("完整的查找路径为==>{}", path);
        List<FileVO> vos = AliyunUtils.listFile(path);
        for (FileVO file : vos) {
            file.setFileName(file.getFileName().replaceAll(path, ""));
        }
        return vos;
    }

    @Override
    public void updateFileName(String newFileName, String originalName, Long userId) {
        //因为阿里云OSS不支持更新文件名称，只能重新上传，所以我们需要对文件进行拷贝再删除
        moveFile(originalName, newFileName, userId);
    }

    @Override
    public void deleteFileOrFolder(String originalFileName, Long userId) {
        //校验文件名
        while (originalFileName.startsWith("/")) {
            originalFileName = originalFileName.substring(1);
        }
        //拼接文件名
        String originalPath = StringEnum.FILE_DEFAULT_PREFIX.getValue() + userId + SEPARATOR + originalFileName;
        log.info("源文件的完整路径名为==>[{}]", originalPath);
        String targetPath = StringEnum.FILE_DEFAULT_GARBAGE_PREFIX.getValue() + userId + SEPARATOR + originalFileName;
        log.info("目标文件的完整路径名为==>[{}]", targetPath);
        AliyunUtils.copyFolder(originalPath, targetPath);

        AliyunUtils.deleteDirAndFiles(originalPath);
        //因为阿里云OSS不支持文件夹的概念，一旦删除，不会保留空文件夹，所以需要创建一个空的文件夹占位
        makeIgnoreFile(originalPath);
    }


    @Override
    public void moveFile(String originalPath, String targetPath, Long userId) {
        while (originalPath.startsWith(SEPARATOR)) {
            originalPath = originalPath.substring(1, originalPath.length());
        }
        while (targetPath.startsWith(SEPARATOR)) {
            targetPath = targetPath.substring(1, targetPath.length());
        }
        String sourcePath = StringEnum.FILE_DEFAULT_PREFIX.getValue() + userId + SEPARATOR + originalPath;
        log.info("源文件的完整路径名为==>[{}]", sourcePath);
        String destPath = StringEnum.FILE_DEFAULT_PREFIX.getValue() + userId + SEPARATOR + targetPath;
        log.info("目标文件的完整路径名为==>[{}]", destPath);
        AliyunUtils.copyFolder(sourcePath, destPath);
        AliyunUtils.deleteDirAndFiles(sourcePath);
        //因为阿里云OSS不支持文件夹的概念，一旦删除，不会保留空文件夹，所以需要创建一个空的文件夹占位
        makeIgnoreFile(sourcePath);
    }

    @Override
    public void deleteFileForever(String targetFileName, Long userId) {
        while (targetFileName.startsWith("/")) {
            targetFileName = targetFileName.substring(1);
        }
        //拼接文件名
        String targetPath = StringEnum.FILE_DEFAULT_GARBAGE_PREFIX.getValue() + userId + SEPARATOR + targetFileName;
        log.info("目标文件的完整路径名为==>[{}]", targetPath);
        //开始删除
        AliyunUtils.deleteDirAndFiles(targetPath);
        //因为阿里云OSS不支持文件夹的概念，一旦删除，不会保留空文件夹，所以需要创建一个空的文件夹占位
        makeIgnoreFile(targetPath);
    }

    /**
     * 创建忽略文件
     *
     * @param originalPath 原始路径
     */
    private void makeIgnoreFile(String originalPath) {
        if (originalPath.contains(".")) {
            String fileName = originalPath.substring(0, originalPath.lastIndexOf(SEPARATOR) + 1);
            AliyunUtils.makeFile(fileName);
        } else {
            AliyunUtils.makeFile(originalPath);
        }
    }

    /**
     * 得到文件链接
     *
     * @param path 路径
     * @return {@code String}
     */
    private String getFileLink(String path) {
        return AliyunUtils.findFileInfo(path).getLink();
    }
}
