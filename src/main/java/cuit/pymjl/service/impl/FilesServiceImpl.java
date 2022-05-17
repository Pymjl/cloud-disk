package cuit.pymjl.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import cuit.pymjl.constant.StringEnum;
import cuit.pymjl.entity.File;
import cuit.pymjl.entity.vo.FileVO;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.mapper.FilesMapper;
import cuit.pymjl.service.FilesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cuit.pymjl.util.AliyunUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    @Transactional(rollbackFor = Exception.class)
    public Long upload(MultipartFile file, Long userId, String path) {
        log.info("开始上传文件........");
        String name = file.getOriginalFilename();
        long size = file.getSize();
        //TODO 如果要扩展功能，这里需要先判断用户的剩余空间是否还足够
        log.info("文件名为==>[{}],文件大小为==>[{}]", name, size);
        if (path != null && path.startsWith(SEPARATOR)) {
            path = path.substring(1, path.length());
        }
        String objectName = StringEnum.FILE_DEFAULT_PREFIX.getValue() + userId + SEPARATOR + path + name;

        //将记录插入到数据库
        log.info("开始插入记录....");
        File sourceFile = File.builder().fileName(name)
                .filePath(objectName)
                .size(size)
                .type(StringEnum.FILE_TYPE_DOC.getValue())
                .ownerId(userId)
                .build();
        try {
            baseMapper.insert(sourceFile);
        } catch (DuplicateKeyException e) {
            throw new AppException("重复的文件名，请更改你的文件名后再提交");
        }

        //上传文件到阿里云OSS
        log.info("文件的完整路径为==>[{}],开始上传文件到阿里云OSS", objectName);
        AliyunUtils.upload(objectName, file);
        log.info("文件上传成功");
        return sourceFile.getId();
    }

    @Override
    public FileVO queryFileById(Long id) {
        File file = baseMapper.selectById(id);
        if (ObjectUtil.isEmpty(file)) {
            throw new AppException("文件异常,不存在该文件");
        }
        FileVO res = new FileVO();
        BeanUtil.copyProperties(file, res);
        res.setLink(getFileLink(file.getFilePath()));
        return res;
    }

    @Override
    public List<FileVO> queryFiles(String path, Long userId) {
        while (path.startsWith(SEPARATOR)) {
            path = path.substring(1, path.length());
        }
        path = StringEnum.FILE_DEFAULT_PREFIX.getValue() + userId + SEPARATOR + path;
        List<FileVO> files = AliyunUtils.listFile(path);

        LambdaQueryWrapper<File> wrapper = new LambdaQueryWrapper<File>()
                .eq(File::getOwnerId, userId)
                .likeRight(File::getFilePath, path);
        List<File> fileList = baseMapper.selectList(wrapper);
        for (FileVO file : files) {
            for (File temp : fileList) {
                if (file.getFileName().equals(temp.getFileName())) {
                    BeanUtil.copyProperties(temp, file);
                }
            }
        }
        return files;
    }

    @Override
    public void updateFileName(String fileName, Long userId, Long fileId) {
        //因为阿里云OSS不支持更新文件名称，只能重新上传，所以我们需要对文件进行拷贝再删除
        //TODO
    }

    @Override
    public void deleteFile(Long userId, Long fileId, String path) {
        //TODO


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
