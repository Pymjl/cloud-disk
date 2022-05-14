package cuit.pymjl.mapper;

import cuit.pymjl.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author pymjl
 * @since 2022-05-14
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
