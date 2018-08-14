package com.liu.authserver.service;

import com.liu.authserver.domain.Client;
import com.liu.authserver.mapper.ClientMapper;
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
public class ClientService {

    @Autowired
    private ClientMapper appMapper;

    @PostMapping(value="/add")
    public String add(HttpServletRequest request) {
        try {
            Client app = new Client();
            app.setClientId("111");
            app.setClientName("222");
            app.setClientSecret("333");
            app.setRedirectUrl("444");
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

    public Client findByClientId(String appId){
        return appMapper.getOneByClientId(appId);
    }

}
