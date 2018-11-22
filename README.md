authserver
================
       Oauth2.0认证
      
![image](https://images2017.cnblogs.com/blog/1096103/201708/1096103-20170824142737402-1297004164.png)


```
sequenceDiagram
client->>应用系统A: get /引导点击登录方式
应用系统A->>client: 302重定向到授权页面
client->>OAuth授权服务: get /oauth2/authorize
OAuth授权服务->>client: 302 登录后重定向到callback，带着code
client->>应用系统A: get /重新加载首页
应用系统A->>OAuth授权服务: post /oauth/access_token?code=
OAuth授权服务->>应用系统A: 200{access_token:,refresh_token:}
应用系统A->>OAuth授权服务: post /userInfo?access_token=
OAuth授权服务->>应用系统A: 200{userInfo:}
应用系统A->>client: 返回结果到首页
```

授权流程说明

```
1.第三方发起授权登录请求，用户允许授权后，重定向到第三方并带上临时票据code;
2.通过code参数加上client_id、client_secret等参数，通过api换取access_token;
3.通过access_token获取用户信息;
```


## 应用注册
应用系统到授权系统注册，注册地址如下
```
http://localhost:8060/client/add?clientName=应用名称&redirectUrl=http://localhost:8051/test&description=应用描述
```
参数 | 是否必填 | 说明
---|---|---
clientName  | 是 | 应用名称
redirectUrl  | 是 | 回调地址
description  | 否 | 应用描述

```
返回格式如下
{
    "result": true,
    "errcode": 2000,
    "msg": "操作成功",
    "data": {
        "clientId": "dfd6e6c0-4856-4525-8fed-68e450828665",
        "clientName": "应用名称",
        "clientSecret": "62f16478-7b6c-4194-9a19-cc804c85fe27",
        "description": "应用描述",
        "redirectUrl": "http://localhost:8051/test"
    }
}
    
```
clientId和clientSecret尽量不要写在项目配置文件里，应动态从数据库中获取。


## 授权流程

###### 1. 引导需要授权的用户到如下地址,访问授权页面<br>
```
get请求
https://localhost:8060/authorize?client_id=dfd6e6c0-4856-4525-8fed-68e450828665&response_type=code&redirect_uri=http://localhost:8051/test
代码示例
    int radnom = (int)(Math.random()*10000);
    StringBuffer stringBuffer = new StringBuffer("http://localhost:8060/oauth2/authorize?");
    stringBuffer.append("client_id=dfd6e6c0-4856-4525-8fed-68e450828665")
            .append("&response_type=code")
            .append("&redirect_uri=http://localhost:8051/test")
            .append("&scope=snsapi_login")
            .append("&state=").append(radnom);
    try {
        resp.sendRedirect(stringBuffer.toString());
    }catch (IOException e){
        logger.error("重定向错误",e);
    }
```

参数 | 是否必填 | 说明
---|---|---
client_id  | 是 | 应用唯一标识
redirect_uri  | 是 | 请使用urlEncode对链接进行处理
response_type  | 是 | 填code
scope  | 是 | 应用授权作用域，多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login即可
state  | 否 | 可设置为简单的随机数加session进行校验

###### 2. 如果用户同意授权，页面跳转至 跳转到redirect_uri/?code=CODE<br>
###### 3. 换取Access Token
```
https://localhost:8060/oauth2/access_token?client_id=YOUR_CLIENT_ID&client_secret=YOUR_CLIENT_SECRET&grant_type=authorization_code&=YOUR_REGISTERED_REDIRECT_URI&code=CODE
```

参数 | 是否必填 | 说明
---|---|---
client_id  | 是 | 应用唯一标识，在平台提交应用审核通过后获得
client_secret  | 是 | 应用密钥AppSecret，在平台提交应用审核通过后获得
response_type  | 是 | 填authorization_code
code  | 是 | 填写第一步获取的code参数
redirect_uri | 是 | 回调地址 验证是否是同源
```
返回结果
{"access_token":"3c10fc3f10dfac52cf514c69dd7e0e53","refresh_token":"44d2777696202cbe3c2a63c84d083d14","openid":"a12","scope":"user_info","expires_in":7200}
```
参数 |  说明
---|---
access_token  | 接口调用凭证
expires_in  | access_token接口调用凭证超时时间，单位（秒）
refresh_token  | 用户刷新access_token
openid  | 授权用户唯一标识
scope  | 用户授权的作用域，使用逗号（,）分隔

###### 4. 刷新access_token有效期

access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种：
```
1. 若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
2. 若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
```
refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权。
请求方法

获取第一步的code后，请求以下链接进行refresh_token：
```
http://localhost:8060/oauth2/refresh_token?client_id=dfd6e6c0-4856-4525-8fed-68e450828665&grant_type=refresh_token&refresh_token=4b7be8d3a378884c3cfe6665f6008f35
```

参数 | 是否必填 | 说明
---|---|---
client_id  | 是 | 应用唯一标识
grant_type  | 是 | 填refresh_token
refresh_token  | 是 | 填写通过access_token获取到的refresh_token参数

```
    返回结果
    {"access_token":"3c10fc3f10dfac52cf514c69dd7e0e53","refresh_token":"44d2777696202cbe3c2a63c84d083d14","openid":"a12","scope":"user_info","expires_in":7200}
```
返回说明
参数 |  说明
---|---
access_token  | 接口调用凭证
expires_in  | access_token接口调用凭证超时时间，单位（秒）
refresh_token  | 用户刷新access_token
openid  | 授权用户唯一标识
scope  | 用户授权的作用域，使用逗号（,）分隔

###### 5. 使用获得的access_token调用API

```
localhost:8060/userInfo?access_token=a8c78732dcc6cfcb78e1c5599e0932a8
```
参数 | 是否必填 | 说明
---|---|---
access_token  | 是 | 凭证
返回结果

```
{用户信息}
```
