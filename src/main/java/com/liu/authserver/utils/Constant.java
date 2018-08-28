package com.liu.authserver.utils;


/**
 * Discription: 常量类
 * Author: leixy
 * Create Date Time: 2017-10-12
 * Update Date Time:
 *
 * @see
 */
public class Constant {

    public static String SEQUENCE_KEY = "sequenceKey";
    public static long V = 1L;

    public Constant() {
    }

    public static final String AUTH = "auth:";
    public static final String AUTH_USERINFO = AUTH + "auth_userInfo";
    public static final String AUTH_RESOURCE_ROLE = AUTH+ "auth_resource_role";
    public static final String AUTH_ROLE_RESOURCE = AUTH+ "auth_role_resource";
    public static final String AUTH_USER_ROLE = AUTH + "auth_user_role";
    public static final String AUTH_DOMAIN_TENANT = AUTH + "auth_domain_tenant";

    public static final String TENANT_INFO = "tenant_info";//租户信息

    public static final String TENANTID_COOKIE="DY_T_ID";
    public static final String TOKEN_COOKIE="DY_TOKEN";
    public static final String TOKEN_HEADER="DY_Authorization";


    public static final String PROVINCE="province";
    public static final String CITY="city";
    public static final String AREA="area";

    // 账户类型
    public class AccountType {
        // 用户姓名
        public static final int ONE_WOKRNUMBER = 1;
        // 手机
        public static final int TWO_MOBILE = 2;
        // 手机+短信
        public static final int PHONE_ACCOUNT = 3;
        // 内部账户
        public static final int INTERNAL_ACCOUNT = 7;
        // 默认密码
        public static final String DEFALT_PASSWORD = "dyrs111";
    }

    // 状态类型
    public class CodeType {

        // 成功状态码
        public static final int SUCCESS_CODE = 2000;
        // 失败状态码
        public static final int FAIL_CODE = 2004;
        // 异常状态码
        public static final int EXCEPTION_CODE = 2005;
        // 数据不存在状态码
        public static final int NO_DATA_CODE = 2006;
        // 数据不存在状态码
        public static final int NO_ACCOUNT_CODE = 2007;
        // 成功消息
        public static final String SUCCESS_MSG = "操作成功";
        // 失败消息
        public static final String FAIL_MSG = "操作失败";
    }

    // 数据有效类型
    class EffectStatus {
        // 禁用状态
        public static final boolean OFF = true;
        // 启用状态
        public static final boolean ON = false;
    }

    // 数据状态类型
    class DataStatus {
        // 禁用状态
        public static final boolean OFF = true;
        // 启用状态
        public static final boolean ON = false;
    }

    // 接口地址
    class InterfaceAPI {
        //文件服务获取图片path
        public static final String ATTCHAPIURL = "http://com-dyrs-mtsp-fileservice/fileservice/fileAttachmentAll/api";
        //内部登录地址
        public static final String INNERLOGINURL = "http://com-dyrs-mtsp-loginservice/security/login/api";
    }
}
