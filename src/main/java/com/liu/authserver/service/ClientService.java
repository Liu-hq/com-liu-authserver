package com.liu.authserver.service;

import com.liu.authserver.domain.Client;
import com.liu.authserver.mapper.ClientMapper;
import com.liu.authserver.utils.Constant;
import com.liu.authserver.utils.ReturnJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2018/8/9.
 */
@Service
@Transactional
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientMapper appMapper;

    @PostMapping(value="/add")
    public Map add(HttpServletRequest request) {
        try {
            Client client = new Client();
            client.setClientId(UUID.randomUUID().toString());
            client.setClientName(request.getParameter("clientName"));
            client.setClientSecret(UUID.randomUUID().toString());
            client.setRedirectUrl(request.getParameter("redirectUrl"));
            client.setDescription(request.getParameter("description"));
            appMapper.insert(client);
            logger.info("client add success");
            return ReturnJson.jsonData(true,client, Constant.CodeType.SUCCESS_CODE,Constant.CodeType.SUCCESS_MSG);
        }catch (Exception e){
            logger.info("client add exception e:{}",e);
            return ReturnJson.jsonData(true,"", Constant.CodeType.FAIL_CODE,Constant.CodeType.FAIL_MSG);
        }
    }

    public Client findByClientId(String appId){
        return appMapper.getOneByClientId(appId);
    }

}
