package com.phuocnguyen.app.sivaosactions.config.authConfig;

import com.sivaos.Configurer.AuthenticationRemoteTokenServiceSIVAOS.CustomRemoteTokenServiceConfigure;
import com.sivaos.Service.SIVAOSServiceImplement.SIVAOSAuthenticationServiceImplement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

@Configuration
@EnableResourceServer
public class ResourceServerConfigure extends ResourceServerConfigurerAdapter {

    SIVAOSAuthenticationServiceImplement sivaOsAuthenticationService;


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        sivaOsAuthenticationService = new SIVAOSAuthenticationServiceImplement();
        sivaOsAuthenticationService.configureResourceServerSecurityConfigurer(resources, tokenService(), authenticationManagerBean());
    }

    @Bean
    public ResourceServerTokenServices tokenService() {
        return new CustomRemoteTokenServiceConfigure();
    }


    @Bean
    public AuthenticationManager authenticationManagerBean() {
        OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
        authenticationManager.setTokenServices(tokenService());
        return authenticationManager;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        sivaOsAuthenticationService = new SIVAOSAuthenticationServiceImplement();
        sivaOsAuthenticationService.configureHttpSecurity(http);
    }
}
