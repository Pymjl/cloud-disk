package cuit.pymjl.controller;


import cuit.pymjl.result.Result;
import cuit.pymjl.result.ResultUtil;
import cuit.pymjl.util.AliyunUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
@RestController
public class FilesController {

    @PostMapping("/upload")
    public Result<String> test(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        AliyunUtils.upload("user/avatar/default/" + name, file);
        System.out.println("user/avatar/default/" + name);
        return ResultUtil.success();
    }


}

