authserver
================
       Oauth2.0认证
       
     
## 应用注册
首先需应用系统注册到认证系统，注册地址如下
```
    http://localhost:8060/client/add?clientName=应用名称&redirectUrl=http://localhost:8051/test&description=应用描述
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

1. 引导需要授权的用户到如下地址,访问授权页面<br>
```
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
        return "bbb";
```

参数 | 是否必填 | 说明
---|---|---
client_id  | 是 | 应用唯一标识
redirect_uri  | 是 | 请使用urlEncode对链接进行处理
response_type  | 是 | 填code
scope  | 是 | 应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login即可
state  | 否 | 用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验



重定向到登录页面，在登录页面输入用户名和密码后，跳转到redirect_uri地址；

2. 如果用户同意授权，页面跳转至 YOUR_REGISTERED_REDIRECT_URI/?code=CODE<br>
3. 换取Access Token
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
参数 | 是否必填 | 说明
---|---|---
access_token  | 是 | 接口调用凭证
expires_in  | 是 | access_token接口调用凭证超时时间，单位（秒）
refresh_token  | 是 | 用户刷新access_token
openid  | 是 | 授权用户唯一标识
scope  | 是 | 用户授权的作用域，使用逗号（,）分隔

4. 刷新access_token有效期
```
    post请求
    http://localhost:8060/oauth2/refresh_token?client_id=dfd6e6c0-4856-4525-8fed-68e450828665&grant_type=refresh_token&refresh_token=4b7be8d3a378884c3cfe6665f6008f35
```
参数说明
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

5. 使用获得的Access Token调用API