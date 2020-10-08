package com.phuocnguyen.app.sivaosactions.AuthenticationRemoteTokenServiceSIVAOS;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilterConfigure implements Filter {

    public CORSFilterConfigure() {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return;
        }
        chain.doFilter(request, servletResponse);
    }

    public void init(FilterConfig fConfig) {
    }
}
