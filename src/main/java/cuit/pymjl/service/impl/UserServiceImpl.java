package cuit.pymjl.service.impl;

import cuit.pymjl.entity.User;
import cuit.pymjl.mapper.UserMapper;
import cuit.pymjl.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
