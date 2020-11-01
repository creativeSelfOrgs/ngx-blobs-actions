package com.phuocnguyen.app.sivaosactions.Configurer.AuthenticationRemoteTokenServiceSIVAOS;

import com.sivaos.Service.SIVAOSServiceImplement.SIVAOSAuthenticationServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class CustomRemoteTokenServiceConfigure implements ResourceServerTokenServices {

    @Value("${spring.profiles.active}")
    private String profileActives;

    private RestOperations restOperations;
    SIVAOSAuthenticationServiceImplement sivaosAuthenticationService;

    /**
     * - Ignore error code 400
     */
    @Autowired
    public CustomRemoteTokenServiceConfigure() {
        restOperations = new RestTemplate();
        ((RestTemplate) restOperations).setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400) {
                    super.handleError(response);
                }
            }
        });
    }


    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        sivaosAuthenticationService = new SIVAOSAuthenticationServiceImplement();
        return sivaosAuthenticationService.loadAuthentication(accessToken, restOperations, profileActives);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

}
