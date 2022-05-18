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
import javax.validation.constraints.NotNull;
import java.util.List;

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
    public Result<String> upload(@RequestParam("file") MultipartFile file,
                                 HttpServletRequest request,
                                 @RequestParam("path") String path) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        filesService.upload(file, Long.parseLong(userId), path);
        return ResultUtil.success();
    }

    @GetMapping("/info")
    public Result<FileVO> queryFile(@NotNull(message = "文件名不能为空") @RequestParam("path") String path,
                                    HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        FileVO file = filesService.queryFile(path, Long.parseLong(userId));
        return ResultUtil.success(file);
    }

    @GetMapping("/list")
    public Result<List<FileVO>> queryFiles(@RequestParam("path") String path,
                                           HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        List<FileVO> vos = filesService.queryFiles(path, Long.parseLong(userId));
        return ResultUtil.success(vos);
    }

    @PostMapping("/move")
    public Result<String> copy(@NotNull(message = "文件名不能为空") @RequestParam("originPath") String originPath,
                               @NotNull(message = "文件名不能为空") @RequestParam("targetPath") String targetPath,
                               HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        filesService.moveFile(originPath, targetPath, Long.parseLong(userId));
        return ResultUtil.success();
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

