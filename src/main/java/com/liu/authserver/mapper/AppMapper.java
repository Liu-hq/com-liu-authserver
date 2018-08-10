package com.liu.authserver.mapper;


import com.liu.authserver.domain.App;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public interface AppMapper {

    List<App> getAll();

    App getOne(String code);

    App getOneByClientId(String appId);

    void insert(App app);

    void update(App app);

    void delete(String code);

}
