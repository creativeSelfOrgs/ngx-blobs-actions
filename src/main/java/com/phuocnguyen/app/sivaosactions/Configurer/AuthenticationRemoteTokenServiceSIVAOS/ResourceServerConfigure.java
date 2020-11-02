package com.phuocnguyen.app.sivaosactions.Configurer.AuthenticationRemoteTokenServiceSIVAOS;

import com.sivaos.Configurer.AuthenticationRemoteTokenServiceSIVAOS.CustomRemoteTokenServiceConfigure;
import com.sivaos.Configurer.AuthenticationRemoteTokenServiceSIVAOS.CustomTokenExtractor;
import com.sivaos.Service.SIVAOSServiceImplement.SIVAOSAccessDeniedHandlerImplement;
import com.sivaos.Service.SIVAOSServiceImplement.SIVAOSAuthenticationEntryPointImplement;
import com.sivaos.Utils.PropertiesUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    protected static void loadProperties() {
        PropertiesUtils.setApplicationSource("application-config-keys.yml");
        PropertiesUtils.loadProperties();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        loadProperties();
        resources.resourceId(PropertiesUtils.readPropertiesByAttributed("resourcesIds")).authenticationManager(authenticationManagerBean())
                .tokenExtractor(new CustomTokenExtractor()).tokenServices(tokenService()).authenticationEntryPoint(new SIVAOSAuthenticationEntryPointImplement()).accessDeniedHandler(new SIVAOSAccessDeniedHandlerImplement());
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

        http
                .authorizeRequests()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/**/actuator/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/v2/api-docs/**").permitAll()
                .antMatchers("/api/v18/actions/download-ldp").permitAll()
                .antMatchers(HttpMethod.GET, "/**").access("#oauth2.hasScope('trust')")
                .antMatchers(HttpMethod.POST, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PATCH, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.PUT, "/**").access("#oauth2.hasScope('write')")
                .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write')")
                .and()
                .headers().addHeaderWriter((request, response) -> {
            response.addHeader("Access-Control-Allow-Origin", "*");
            if (request.getMethod().equals("OPTIONS")) {
                response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
            }
        });
    }
}
