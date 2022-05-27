package cuit.pymjl.controller;

import cn.hutool.core.util.StrUtil;
import cuit.pymjl.entity.vo.FileVO;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.result.Result;
import cuit.pymjl.result.ResultUtil;
import cuit.pymjl.service.FilesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/21 18:10
 **/
@RestController
@RequestMapping("/trash")
public class GarbageController {
    @Resource
    FilesService filesService;

    @GetMapping("/list")
    public Result<List<FileVO>> listFiles(HttpServletRequest request,
                                          @RequestParam("path") String path) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        List<FileVO> files = filesService.queryFilesFromGarbage(path, Long.parseLong(userId));
        return ResultUtil.success(files);
    }

    @DeleteMapping("/file")
    public Result<String> deleteFile(HttpServletRequest request,
                                     @RequestParam("path") String path) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        filesService.deleteFileForever(path, Long.parseLong(userId));
        return ResultUtil.success();
    }


    @PostMapping("/recover")
    public Result<String> recover(HttpServletRequest request,
                                  @RequestParam("path") String path) {
        String userId = (String) request.getAttribute("userId");
        check(userId);
        if (path.contains(".")) {
            filesService.recoverFile(path, Long.parseLong(userId));
        } else {
            filesService.recoverFolder(path, Long.parseLong(userId));
        }
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
