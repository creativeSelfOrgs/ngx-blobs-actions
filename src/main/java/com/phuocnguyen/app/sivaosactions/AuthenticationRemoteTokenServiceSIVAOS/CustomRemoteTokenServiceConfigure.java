package com.phuocnguyen.app.sivaosactions.AuthenticationRemoteTokenServiceSIVAOS;

import com.sivaos.Utils.PropertiesUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

public class CustomRemoteTokenServiceConfigure implements ResourceServerTokenServices {

    private RestOperations restOperations;
    private AccessTokenConverter tokenConverter = new DefaultAccessTokenConverter();

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

    protected static void loadProperties() {
        PropertiesUtils.setApplicationSource("application-config-keys.yml");
        PropertiesUtils.loadProperties();
    }


    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        loadProperties();
        HttpHeaders headers = new HttpHeaders();
        Map<String, Object> map = executeGet(PropertiesUtils.readPropertiesByAttributed("accessTokenUri") + accessToken, headers);
        if (map == null || map.isEmpty() || map.get("error") != null) {
            throw new InvalidTokenException("Token not allowed");
        }
        return tokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private Map<String, Object> executeGet(String path, HttpHeaders headers) {
        try {
            if (headers.getContentType() == null) {
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            }
            @SuppressWarnings("rawtypes")
            Map map = restOperations.exchange(path, HttpMethod.GET, new HttpEntity<MultiValueMap<String, String>>(null, headers), Map.class).getBody();
            @SuppressWarnings("unchecked")
            Map<String, Object> result = map;
            return result;
        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
        return null;
    }
}
