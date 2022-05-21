package cuit.pymjl.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cuit.pymjl.entity.vo.UserVO;
import cuit.pymjl.result.PageResult;
import cuit.pymjl.result.Result;
import cuit.pymjl.result.ResultUtil;
import cuit.pymjl.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/16 20:02
 **/
@RestController
@Validated
@RequestMapping("/admin")
public class AdminController {
    @Resource
    UserService userService;

    @GetMapping("/users/{currentPage}/{pageSize}")
    public PageResult<UserVO> listUsers(@NotNull(message = "参数当前页不能为null") @PathVariable("currentPage") Integer currentPage,
                                        @NotNull(message = "参数每页的宽度不能为null") @PathVariable("pageSize") Integer pageSize) {
        Page<UserVO> page = userService.listUsers(currentPage, pageSize);
        return new PageResult<>(page);
    }

    @DeleteMapping("/user/{id}")
    public Result<String> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return ResultUtil.success();
    }

    @PatchMapping("/user/{id}")
    public Result<String> addAdmin(@PathVariable("id") Long id) {
        userService.addAdmin(id);
        return ResultUtil.success();
    }
}
