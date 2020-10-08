package com.phuocnguyen.app.sivaosactions.AuthenticationRemoteTokenServiceSIVAOS;

import com.sivaos.Service.SIVAOSServiceImplement.SIVAOSAuthenticationServiceImplement;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;

import javax.servlet.http.HttpServletRequest;

public class CustomTokenExtractor implements TokenExtractor {

    SIVAOSAuthenticationServiceImplement sivaosAuthenticationService;

    @Override
    public Authentication extract(HttpServletRequest request) {
        sivaosAuthenticationService = new SIVAOSAuthenticationServiceImplement();
        return sivaosAuthenticationService.extractTokensAsAuthentication(request);
    }
}
