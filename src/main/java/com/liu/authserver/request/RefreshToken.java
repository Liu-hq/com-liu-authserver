package com.liu.authserver.request;

import org.apache.oltu.oauth2.as.request.AbstractOAuthTokenRequest;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/8/28.
 */
public class RefreshToken extends AbstractOAuthTokenRequest {

    protected RefreshToken(HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
        super(request);
    }

    @Override
    protected void validate() throws OAuthSystemException, OAuthProblemException {

        super.validate();
    }
}
