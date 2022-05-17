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

}
