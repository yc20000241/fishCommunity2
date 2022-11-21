package com.yc.community.sys.service.impl;

import com.yc.community.sys.entity.User;
import com.yc.community.sys.mapper.UserMapper;
import com.yc.community.sys.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yc001
 * @since 2022-11-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
