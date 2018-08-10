package com.liu.authserver.service;

import com.liu.authserver.domain.App;
import com.liu.authserver.mapper.AppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/8/9.
 */
@Service
@Transactional
public class AppService {

    @Autowired
    private AppMapper appMapper;

    @PostMapping(value="/add")
    public String add(HttpServletRequest request) {
        try {
            App app = new App();
            app.setClientId("111");
            app.setClientName("222");
            app.setClientSecret("333");
            app.setCallbackUrl("444");
            app.setHomePageUrl("555");
            app.setScope("6666");
            app.setCode("77777");
            app.setDescription("8888888");
            appMapper.insert(app);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "succeed";
    }

    public boolean checkClientId(String appId){
        App app = appMapper.getOneByClientId(appId);
        if(app!=null){
            return true;
        }
        return false;
    }

    public App findByClientId(String appId){
        return appMapper.getOneByClientId(appId);
    }

}
