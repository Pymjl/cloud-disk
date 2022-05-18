package cuit.pymjl.service;

import cuit.pymjl.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;
import cuit.pymjl.entity.vo.FileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
public interface FilesService extends IService<File> {
    /**
     * 上传
     * 上传文件
     *
     * @param file   文件
     * @param userId 用户id
     * @param path   路径
     */
    void upload(MultipartFile file, Long userId, String path);

    /**
     * 查询文件信息
     *
     * @param path   路径
     * @param userId 用户id
     * @return {@code FileVO}
     */
    FileVO queryFile(String path, Long userId);

    /**
     * 查询文件列表
     *
     * @param path   路径
     * @param userId 用户id
     * @return {@code List<FileVO>}
     */
    List<FileVO> queryFiles(String path, Long userId);

    /**
     * 更新文件名字
     *
     * @param newFileName  文件名称
     * @param originalName 原来的名字
     * @param userId       用户id
     */
    void updateFileName(String newFileName, String originalName, Long userId);

    /**
     * 删除文件或文件夹
     *
     * @param originalFileName 原始文件名字
     * @param userId           用户id
     */
    void deleteFileOrFolder(String originalFileName, Long userId);

    /**
     * 移动文件
     *
     * @param originalPath 原始路径
     * @param targetPath   目标路径
     * @param userId       用户id
     */
    void moveFile(String originalPath, String targetPath, Long userId);

    /**
     * 永远删除文件
     *
     * @param targetFileName 目标文件名字
     * @param userId         用户id
     */
    void deleteFileForever(String targetFileName, Long userId);

}
