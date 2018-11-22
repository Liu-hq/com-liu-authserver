package com.liu.authserver.controller;

import com.liu.authserver.domain.UserEntity;
import com.liu.authserver.service.OAuthService;
import com.liu.authserver.service.UserService;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.apache.oltu.oauth2.rs.response.OAuthRSResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2018/8/14.
 */
@RestController
public class UserInfoController {

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private UserService userService;

    @Autowired
    private RestOperations restTemplate;

    @RequestMapping("/userInfo")
    public HttpEntity userInfo(HttpServletRequest request) throws OAuthSystemException {
        try {
            //构建OAuth资源请求
            OAuthAccessResourceRequest oauthRequest =
                    new OAuthAccessResourceRequest(request, ParameterStyle.QUERY);
            //获取Access Token
            String accessToken = oauthRequest.getAccessToken();

            //验证Access Token
            if (!oAuthService.checkAccessToken(accessToken)) {
                // 如果不存在/过期了，返回未验证错误，需重新验证
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm(OAuthError.TokenResponse.INVALID_CLIENT)
                        .setError(OAuthError.ResourceResponse.INVALID_TOKEN)
                        .buildHeaderMessage();

                HttpHeaders headers = new HttpHeaders();
                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
            }
            //返回用户信息
            String username = oAuthService.getUsernameByAccessToken(accessToken);
            UserEntity userEntity = userService.getOneByName(username);
            return new ResponseEntity(userEntity, HttpStatus.OK);
        } catch (OAuthProblemException e) {
            //检查是否设置了错误码
            String errorCode = e.getError();
            if (OAuthUtils.isEmpty(errorCode)) {
                OAuthResponse oauthResponse = OAuthRSResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setRealm(OAuthError.TokenResponse.INVALID_CLIENT)
                        .buildHeaderMessage();

                HttpHeaders headers = new HttpHeaders();
                headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                        oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
                return new ResponseEntity(headers, HttpStatus.UNAUTHORIZED);
            }

            OAuthResponse oauthResponse = OAuthRSResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .setRealm(OAuthError.TokenResponse.INVALID_CLIENT)
                    .setError(e.getError())
                    .setErrorDescription(e.getDescription())
                    .setErrorUri(e.getUri())
                    .buildHeaderMessage();

            HttpHeaders headers = new HttpHeaders();
            headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
                    oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/abc")
    public String abc(HttpServletRequest req, HttpServletResponse resp){
        //获取code然后
//获取code然后
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
            map.add("client_id", "dfd6e6c0-4856-4525-8fed-68e450828665");
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity( "http://com-dyrs-mtsp-authorityservice/def", request , String.class );
            System.out.println(response.getBody());
            return "def";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "aa";
    }

    @RequestMapping(value="/def")
    public String def(HttpServletRequest req, HttpServletResponse resp){
        return "def";
    }
}
