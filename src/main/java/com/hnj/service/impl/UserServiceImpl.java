package com.hnj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hnj.domain.User;
import com.hnj.service.UserService;
import com.hnj.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




