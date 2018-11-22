package com.liu.authserver.service;

import com.liu.authserver.domain.UserEntity;
import com.liu.authserver.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/8/30.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public UserEntity getOneByName(String userName){
        return userMapper.getOneByName(userName);
    }
}
