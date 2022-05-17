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
     * @return {@code Long}
     */
    Long upload(MultipartFile file, Long userId, String path);

    /**
     * 通过用户ID查询文件
     *
     * @param id id
     * @return {@code FileVO}
     */
    FileVO queryFileById(Long id);

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
     * @param fileName 文件名称
     * @param userId   用户id
     * @param fileId   文件标识
     */
    void updateFileName(String fileName, Long userId, Long fileId);

    /**
     * 删除文件,这是逻辑删除，并不会删除存在于阿里云OSS上的源文件
     *
     * @param userId 用户id
     * @param fileId 文件标识
     * @param path   路径
     */
    void deleteFile(Long userId, Long fileId, String path);

}
