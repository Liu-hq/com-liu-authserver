package com.liu.authserver.mapper;


import com.liu.authserver.domain.Client;

import java.util.List;

/**
 * Created by Administrator on 2018/7/20.
 */
public interface ClientMapper {

    List<Client> getAll();

    Client getOne(String id);

    Client getOneByClientId(String appId);

    Client getOneByClientSecret(String clientSecret);

    void insert(Client app);

    void update(Client app);

    void delete(String id);

}
