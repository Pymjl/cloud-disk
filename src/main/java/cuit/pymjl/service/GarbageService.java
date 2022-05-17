package cuit.pymjl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cuit.pymjl.entity.Garbage;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/17 15:46
 **/
public interface GarbageService extends IService<Garbage> {

    /**
     * 永远删除文件
     *
     * @param userId 用户id
     * @param fileId 文件标识
     */
    void deleteFileForever(Long userId, Long fileId);

    /**
     * 将文件添加到回收站
     *
     * @param path     路径
     * @param fileName 文件名称
     * @param size     大小
     * @param type     类型
     */
    void addFileToGarbage(String path, String fileName, Long size, String type);
}
