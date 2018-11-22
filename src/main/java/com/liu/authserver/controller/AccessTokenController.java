package com.liu.authserver.controller;

import com.alibaba.fastjson.JSONObject;
import com.liu.authserver.service.OAuthService;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.request.OAuthUnauthenticatedTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/8/10.
 */
@RestController
@RequestMapping("/oauth2")
public class AccessTokenController {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenController.class);

    @Autowired
    private OAuthService oAuthService;

    /**
     1、首先通过如http://localhost:8060/access_token，POST提交如下数据：client_id=c1ebe466-1cdc-4bd3-ab69-77c3561b9dee&client_secret=d8346ea2-6017-43ed-ad68-19c0f971738b&grant_type=authorization_code&code=828beda907066d058584f37bcfd597b6&redirect_uri=http://localhost:8051/test访问；
     2、该控制器会验证client_id、client_secret、auth code的正确性，如果错误会返回相应的错误；
     3、如果验证通过会生成并返回相应的访问令牌access token。
     * @param request
     * @return
     * @throws URISyntaxException
     * @throws OAuthSystemException
     */

    @RequestMapping("/access_token")
    public HttpEntity token(HttpServletRequest request)throws URISyntaxException, OAuthSystemException {
        try {
            //构建OAuth请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);
            //检查提交的客户端id是否正确
            if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
                OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(OAuthError.OAUTH_ERROR_DESCRIPTION)
                        .buildJSONMessage();
                return new ResponseEntity(
                        response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }
            // 检查客户端安全KEY是否正确
            if (!oAuthService.checkClientSecret(oauthRequest.getClientSecret())) {
                OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
                        .setErrorDescription(OAuthError.OAUTH_ERROR_DESCRIPTION)
                        .buildJSONMessage();
                return new ResponseEntity(
                        response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }
            String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
            // 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
            if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
                    GrantType.AUTHORIZATION_CODE.toString())) {
                if (!oAuthService.checkAuthCode(authCode)) {
                    OAuthResponse response = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_GRANT)
                            .setErrorDescription("错误的授权码")
                            .buildJSONMessage();
                    return new ResponseEntity(
                            response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
                }
            }
            //生成Access Token
            String username = oAuthService.getUsernameByAuthCode(authCode);
            int openId = hash(oauthRequest.getClientId() + username );
            OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
            final String accessToken = oauthIssuerImpl.accessToken();
            oAuthService.addAccessToken(accessToken, username);

            final String refreshToken = oauthIssuerImpl.refreshToken();
            Map map = new HashMap();
            map.put("username",username);
            map.put("accessToken",accessToken);
            oAuthService.addRefreshToken(refreshToken,
                    JSONObject.toJSONString(map));

            //生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(accessToken)
                    .setExpiresIn(String.valueOf(oAuthService.getExpireIn()))
                    .setRefreshToken(refreshToken)
                    .setParam("openid",String.valueOf(openId))
                    .setScope("user_info")
                    .buildJSONMessage();

            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity(
                    response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        } catch (OAuthProblemException e) {
            //构建错误响应
            OAuthResponse res = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            logger.error("构建错误响应",e);
            return new ResponseEntity(res.getBody(), HttpStatus.valueOf(res.getResponseStatus()));
        }
    }

    @RequestMapping("/refresh_token")
    public HttpEntity refreshToken(HttpServletRequest request) throws URISyntaxException, OAuthSystemException {
        try {
        //构建OAuth请求
            OAuthUnauthenticatedTokenRequest oauthRequest = new OAuthUnauthenticatedTokenRequest(request);
            //检查提交的客户端id是否正确
            if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
                OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setErrorDescription(OAuthError.OAUTH_ERROR_DESCRIPTION)
                        .buildJSONMessage();
                return new ResponseEntity(
                        response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }

            //检查refreshToken是否正确
            if (!oAuthService.checkRefreshToken(oauthRequest.getRefreshToken())) {
                OAuthResponse response = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                        .setError(OAuthError.TokenResponse.INVALID_REQUEST)
                        .setErrorDescription("refreshToken is not exist")
                        .buildJSONMessage();
                return new ResponseEntity(
                        response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
            }

            //刷新Access Token
            String new_access_token;
            String refreshTokenObj = oAuthService.getAccessTokenByRefreshToken(oauthRequest.getRefreshToken());
            JSONObject jsonObject = analysis(refreshTokenObj);
            String accessToken = jsonObject.getString("accessToken");
            String username = jsonObject.getString("username");
            int openId = hash(oauthRequest.getClientId() + username );
            if(oAuthService.checkAccessToken(accessToken)){
                new_access_token = accessToken;
            }else{
                OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                new_access_token = oauthIssuerImpl.accessToken();
            }
            oAuthService.addAccessToken(new_access_token,username );

            //生成OAuth响应
            OAuthResponse response = OAuthASResponse
                    .tokenResponse(HttpServletResponse.SC_OK)
                    .setAccessToken(new_access_token)
                    .setExpiresIn(String.valueOf(oAuthService.getExpireIn()))
                    .setRefreshToken(oauthRequest.getRefreshToken())
                    .setParam("openid",String.valueOf(openId))
                    .setScope("user_info")
                    .buildJSONMessage();
            //根据OAuthResponse生成ResponseEntity
            return new ResponseEntity(
                    response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));

        } catch (OAuthProblemException e) {
            //构建错误响应
            OAuthResponse res = OAuthASResponse
                    .errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
                    .buildJSONMessage();
            logger.error("构建错误响应",e);
            return new ResponseEntity(res.getBody(), HttpStatus.valueOf(res.getResponseStatus()));
        }
    }

    // 刷新access refresh token
    //1. 若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间；
    //2. 若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。
    public void refreshToken(String refreshToken) throws URISyntaxException, OAuthSystemException
    {
        try{
            String new_access_token;
            String refreshTokenObj = oAuthService.getAccessTokenByRefreshToken(refreshToken);
            JSONObject jsonObject = analysis(refreshTokenObj);
            String accessToken = jsonObject.getString("accessToken");
            if(oAuthService.checkAccessToken(accessToken)){
                new_access_token = accessToken;
            }else{
                OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                new_access_token = oauthIssuerImpl.accessToken();
            }
            oAuthService.addAccessToken(new_access_token,jsonObject.getString("username") );
        }catch (Exception e){
            logger.error("refreshToken error",e);
        }
    }

    public JSONObject analysis(String refreshTokenObj){
        JSONObject jsonObject = JSONObject.parseObject(refreshTokenObj);
        return jsonObject;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}