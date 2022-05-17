package cuit.pymjl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cuit.pymjl.entity.Garbage;
import cuit.pymjl.mapper.GarbageMapper;
import cuit.pymjl.service.GarbageService;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/17 15:47
 **/
public class GarbageServiceImpl extends ServiceImpl<GarbageMapper, Garbage> implements GarbageService {
    @Override
    public void deleteFileForever(Long userId, Long fileId) {
        //TODO
    }

    @Override
    public void addFileToGarbage(String path, String fileName, Long size, String type) {
        //TODO
    }
}
