package com.liu.authserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.liu.authserver.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/7/12.
 */
@RestController
@RequestMapping("/app")
public class ClientController {

    @Autowired
    ClientService appService;

    @RequestMapping(value="/add")
    public String add(HttpServletRequest request) {
        appService.add(request);
        return "succeed";
    }

    @RequestMapping(value="/test")
    public String test(@RequestBody JSONObject maps){
        String jsonObject = maps.getString("name");
        System.out.println(jsonObject);
        return jsonObject;
    }


}
