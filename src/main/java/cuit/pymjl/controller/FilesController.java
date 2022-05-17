package cuit.pymjl.controller;


import cn.hutool.core.util.StrUtil;
import cuit.pymjl.entity.vo.FileVO;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.result.Result;
import cuit.pymjl.result.ResultUtil;
import cuit.pymjl.service.FilesService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
@RestController
@RequestMapping("files")
@Validated
public class FilesController {
    @Resource
    FilesService filesService;

    @PostMapping("/upload")
    public Result<Long> upload(@RequestParam("file") MultipartFile file,
                               HttpServletRequest request,
                               @RequestParam("path") String path) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        Long fileId = filesService.upload(file, Long.parseLong(userId), path);
        return ResultUtil.success(fileId);
    }

    @GetMapping("/{id}")
    public Result<FileVO> queryFileById(@PathVariable("id") Long id) {
        FileVO file = filesService.queryFileById(id);
        return ResultUtil.success(file);
    }

    /**
     * 检查
     *
     * @param data 数据
     */
    private void check(String... data) {
        for (String datum : data) {
            if (StrUtil.isBlank(datum)) {
                throw new AppException("发生了未知错误，参数异常，请联系管理员或稍后重试");
            }
        }
    }


}

