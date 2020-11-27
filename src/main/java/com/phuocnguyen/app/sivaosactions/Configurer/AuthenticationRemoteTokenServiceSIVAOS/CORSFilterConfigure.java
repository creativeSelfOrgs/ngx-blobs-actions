package com.phuocnguyen.app.sivaosactions.Configurer.AuthenticationRemoteTokenServiceSIVAOS;

import com.sivaos.Configurer.CustomFilterRequest.TypeSafeRequest;
import org.springframework.stereotype.Component;

import javax.servlet.*;

@Component
public class CORSFilterConfigure implements Filter {

    public CORSFilterConfigure() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) {
        TypeSafeRequest.buildServletDoFilterChain(servletRequest, servletResponse, chain);
    }

    public void init(FilterConfig fConfig) {
        TypeSafeRequest.init(fConfig);
    }
}
