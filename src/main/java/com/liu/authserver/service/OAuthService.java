package com.liu.authserver.service;

import com.liu.authserver.domain.Client;
import com.liu.authserver.mapper.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/8/10.
 */
@Service
public class OAuthService {

    @Autowired
    private ClientMapper appMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 添加 auth code
    public void addAuthCode(String authCode, String username)
    {
        redisTemplate.opsForValue().set(authCode,username,300, TimeUnit.SECONDS);
    }
    // 添加 access token
    public void addAccessToken(String accessToken, String username)
    {
        redisTemplate.opsForValue().set(accessToken,username,7200, TimeUnit.MINUTES);
    }
    // 验证auth code是否有效
    public boolean checkAuthCode(String authCode)
    {
        String username = redisTemplate.opsForValue().get(authCode);
        return !StringUtils.isEmpty(username);
    }
    // 验证access token是否有效
    public boolean checkAccessToken(String accessToken){
        String username = redisTemplate.opsForValue().get(accessToken);
        return !StringUtils.isEmpty(username);
    }
    // 根据auth code获取用户名
    public String getUsernameByAuthCode(String authCode){
        return redisTemplate.opsForValue().get(authCode);
    }
    // 根据access token获取用户名
    public String getUsernameByAccessToken(String accessToken){
        return redisTemplate.opsForValue().get(accessToken);
    }

    //auth code / access token 过期时间
    public long getExpireIn(){
        return 7200L;
    }

    // 检查客户端id是否存在
    public boolean checkClientId(String appId){
        Client app = appMapper.getOneByClientId(appId);
        if(app!=null){
            return true;
        }
        return false;
    }

    // 坚持客户端安全KEY是否存在
    public boolean checkClientSecret(String clientSecret){
        Client app = appMapper.getOneByClientSecret(clientSecret);
        if(app!=null){
            return true;
        }
        return false;
    }

}
